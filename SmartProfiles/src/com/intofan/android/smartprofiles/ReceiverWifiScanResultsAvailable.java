package com.intofan.android.smartprofiles;

import java.util.HashMap;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.text.format.Time;

public class ReceiverWifiScanResultsAvailable extends BroadcastReceiver {

	private Time now = new Time();
	private Profile profile;

	@Override
	public void onReceive(Context context, Intent intent) {
		List<ScanResult> hotspots = UtilSystem.getWifiManager(context)
				.getScanResults();
		HashMap<String, Integer> wifiSignals = new HashMap<String, Integer>();
		if (hotspots != null && hotspots.size() > 0) {
			for (ScanResult hotspot : hotspots) {
				if (!wifiSignals.keySet().contains(hotspot.SSID)) {
					wifiSignals.put(hotspot.SSID, hotspot.level);
				}
			}
		}
		now.setToNow();
		profile = Profile.matchProfile(Profile.getProfiles(), wifiSignals, now);
		ServiceChangeProfile.setProfile(profile, context);
	}

}
