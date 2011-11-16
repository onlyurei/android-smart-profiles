package com.intofan.android.smartprofiles;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.format.Time;

public class ServiceChangeProfile extends Service {

	public static ReceiverWifiStateChanged receiverWifiStateChanged = null;
	public static ReceiverWifiScanResultsAvailable receiverWifiScanResultsAvailable = null;
	public static ReceiverRingerModeChanged receiverRingerModeChanged = null;
	public static boolean started = false;
	public static boolean paused = false;
	public static Timer timerTimer = new Timer();
	public static Timer wifiScanTimer = new Timer();
	final static Time now = new Time();
	private static Profile profile;
	private static Context context;
	private static final int NOTIFICATION_ACTIVE_PROFILE = 1;
	public static Profile currentProfile;
	public static Intent activeProfileChangedIntent;
	public static boolean isWifiEnabled;
	public static PowerManager.WakeLock wl = null;

	@Override
	public void onCreate() {
		context = getApplicationContext();
		activeProfileChangedIntent = new Intent(
				getString(R.string.intent_action_active_profile_changed));
		start();
	}

	// This is the old onStart method that will be called on the pre-2.0
	// platform. On 2.0 or later we override onStartCommand() so this
	// method will not be called.
	@Override
	public void onStart(Intent intent, int startId) {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	private void start() {
		if (Setting.stayAwake) {
			acquirePartialWakeLock();
		}
		started = true;
		if (receiverWifiStateChanged == null) {
			receiverWifiStateChanged = new ReceiverWifiStateChanged();
		}
		registerReceiver(receiverWifiStateChanged, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));
		if (receiverWifiScanResultsAvailable == null) {
			receiverWifiScanResultsAvailable = new ReceiverWifiScanResultsAvailable();
		}
		registerReceiver(receiverWifiScanResultsAvailable, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		if (receiverRingerModeChanged == null) {
			receiverRingerModeChanged = new ReceiverRingerModeChanged();
		}
		registerReceiver(receiverRingerModeChanged, new IntentFilter(
				AudioManager.RINGER_MODE_CHANGED_ACTION));
		int wifiState = UtilSystem.getWifiManager(getApplicationContext())
				.getWifiState();
		if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
			isWifiEnabled = true;
			UtilSystem.getWifiManager(getApplicationContext()).startScan();
			cancelWifiScanTask();
			scheduleWifiScanTask();
		} else {
			isWifiEnabled = false;
			cancelTimerTask();
			if (Setting.pauseWhenWifiIsOff) {
				pauseService();
			} else {
				ServiceChangeProfile.scheduleTimerTask();
			}
		}
		if (!paused) {
			if (Setting.toast) {
				UtilAlert
						.toast(context,
								context.getString(R.string.smarter_profiles_service_is_running),
								0);
			}
		}
		context.sendBroadcast(activeProfileChangedIntent);
	}

	@Override
	public void onDestroy() {
		unregisterReceivers();
		cancelTimerTask();
		cancelWifiScanTask();
		UtilSystem.getNotificationManager(context).cancel(
				NOTIFICATION_ACTIVE_PROFILE);
		currentProfile = null;
		Profile.activeProfileName = "";
		context.sendBroadcast(activeProfileChangedIntent);
		started = false;
		paused = false;
		releasePartialWakeLock();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void unregisterReceivers() {
		if (receiverWifiStateChanged != null) {
			try {
				unregisterReceiver(receiverWifiStateChanged);
			} catch (Exception e) {
			}
		}
		if (receiverWifiScanResultsAvailable != null) {
			try {
				unregisterReceiver(receiverWifiScanResultsAvailable);
			} catch (Exception e) {
			}
		}
		if (receiverRingerModeChanged != null) {
			try {
				unregisterReceiver(receiverRingerModeChanged);
			} catch (Exception e) {
			}
		}
	}

	public static void notifyProfile() {
		if (context == null) {
			return;
		}
		if (Profile.activeProfileName == "") {
			cancelNotification();
		} else {
			String[] n = Profile.toNotification(context);
			int notificationIcon;
			if (isWifiEnabled) {
				notificationIcon = R.drawable.ic_stat_notify;
			} else {
				notificationIcon = R.drawable.ic_stat_notify_mono;
			}
			Notification notification = new Notification(notificationIcon,
					n[1], 0);
			notification.flags = Notification.FLAG_ONGOING_EVENT
					| Notification.FLAG_NO_CLEAR;
			Intent notificationIntent = new Intent(context, ActivityMain.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(context, n[0], n[1], contentIntent);
			UtilSystem.getNotificationManager(context).notify(
					NOTIFICATION_ACTIVE_PROFILE, notification);
		}
	}

	public static void cancelNotification() {
		if (context == null) {
			return;
		}
		UtilSystem.getNotificationManager(context).cancel(
				NOTIFICATION_ACTIVE_PROFILE);
	}

	public static void scheduleTimerTask() {
		TimerTask timeTask = new TimerTask() {
			public void run() {
				now.setToNow();
				profile = Profile.matchProfile(Profile.getProfiles(),
						new HashMap<String, Integer>(), now);
				setProfile(profile, context);
			}
		};
		timerTimer = new Timer();
		timerTimer.schedule(timeTask, 0, Setting.checkFrequency * 1000);
	}

	public static void setProfile(Profile profile, Context context) {
		if (profile != null) {
			Profile.setProfile(profile, context);
			Profile.activeProfileName = profile.name;
		} else {
			Profile.activeProfileName = "";
		}
		if (profile != currentProfile) {
			currentProfile = profile;
			if (Setting.notify) {
				notifyProfile();
			}
			context.sendBroadcast(activeProfileChangedIntent);
		}
	}

	public static void cancelTimerTask() {
		if (timerTimer != null) {
			try {
				timerTimer.cancel();
			} catch (Exception e) {
			}
			;
		}
	}

	public static void scheduleWifiScanTask() {
		TimerTask timeTask = new TimerTask() {
			public void run() {
				UtilSystem.getWifiManager(context).startScan();
			}
		};
		wifiScanTimer = new Timer();
		wifiScanTimer.schedule(timeTask, 0, Setting.checkFrequency * 1000);
	}

	public static void cancelWifiScanTask() {
		if (wifiScanTimer != null) {
			try {
				wifiScanTimer.cancel();
			} catch (Exception e) {
			}
			;
		}
	}

	public static void pauseService() {
		paused = true;
		Profile.activeProfileName = "";
		currentProfile = null;
		if (Setting.toast) {
			UtilAlert.toast(context, context
					.getString(R.string.smarter_profiles_service_is_paused), 1);
		}
		releasePartialWakeLock();
	}

	public static void resumeService() {
		if (Setting.stayAwake) {
			acquirePartialWakeLock();
		}
		paused = false;
		if (Setting.toast) {
			UtilAlert
					.toast(context,
							context.getString(R.string.smarter_profiles_service_is_running),
							0);
		}
	}

	public static void acquirePartialWakeLock() {
		if (wl == null) {
			wl = UtilSystem.getPowerManager(context).newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, "ServiceChangeProfile");
			wl.acquire();
		}
	}

	public static void releasePartialWakeLock() {
		if (wl != null) {
			wl.release();
			wl = null;
		}
	}
}