package com.intofan.android.smartprofiles;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.view.WindowManager;

public class UtilSystem {
	private static WifiManager wifiManager;
	private static AudioManager audioManager;
	private static NotificationManager notificationManager;
	private static ConnectivityManager connectivityManager;
	private static PowerManager powerManager;
	private static WindowManager windowManager;

	public static WifiManager getWifiManager(Context context) {
		if (wifiManager == null) {
			wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
		}
		return wifiManager;
	}

	public static AudioManager getAudioManager(Context context) {
		if (audioManager == null) {
			audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
		}
		return audioManager;
	}

	public static NotificationManager getNotificationManager(Context context) {
		if (notificationManager == null) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return notificationManager;
	}

	public static ConnectivityManager getConnectivityManager(Context context) {
		if (connectivityManager == null) {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return connectivityManager;
	}

	public static PowerManager getPowerManager(Context context) {
		if (powerManager == null) {
			powerManager = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
		}
		return powerManager;
	}

	public static WindowManager getWindowManager(Context context) {
		if (windowManager == null) {
			windowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return windowManager;
	}

}
