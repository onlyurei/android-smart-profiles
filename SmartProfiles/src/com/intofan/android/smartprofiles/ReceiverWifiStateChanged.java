package com.intofan.android.smartprofiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class ReceiverWifiStateChanged extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int wifiStateIntExtra = intent.getIntExtra(
				WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
		if (wifiStateIntExtra == WifiManager.WIFI_STATE_DISABLED) {
			ServiceChangeProfile.isWifiEnabled = false;
			ServiceChangeProfile.cancelWifiScanTask();
			ServiceChangeProfile.cancelTimerTask();
			if (Setting.pauseWhenWifiIsOff) {
				ServiceChangeProfile.pauseService();
			} else {
				ServiceChangeProfile.scheduleTimerTask();
			}
		} else if (wifiStateIntExtra == WifiManager.WIFI_STATE_ENABLED) {
			ServiceChangeProfile.isWifiEnabled = true;
			ServiceChangeProfile.cancelTimerTask();
			ServiceChangeProfile.cancelWifiScanTask();
			ServiceChangeProfile.scheduleWifiScanTask();
			if (Setting.pauseWhenWifiIsOff) {
				ServiceChangeProfile.resumeService();
			}
		}
		if (Setting.notify) {
			ServiceChangeProfile.notifyProfile();
		}
		context.sendBroadcast(ServiceChangeProfile.activeProfileChangedIntent);
	}

}