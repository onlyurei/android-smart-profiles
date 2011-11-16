package com.intofan.android.smartprofiles;

import android.app.Application;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;

public class Main extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		/*
		 * try { PackageManager manager = getApplicationContext()
		 * .getPackageManager(); PackageInfo appInfo = manager.getPackageInfo(
		 * "com.intofan.android.smartprofiles",
		 * PackageManager.GET_SIGNATURES); PackageInfo keyInfo =
		 * manager.getPackageInfo( "com.intofan.android.smartprofiles.Key",
		 * PackageManager.GET_SIGNATURES); if
		 * (appInfo.signatures[0].toCharsString().equals(
		 * keyInfo.signatures[0].toCharsString())) { Setting.keyFound = true;
		 * Setting.keyValid = true; } else { Setting.keyFound = true;
		 * Setting.keyValid = false; } } catch (NameNotFoundException e) {
		 * Setting.keyFound = false; Setting.keyValid = false; }
		 */
		Setting.loadPreferences(getApplicationContext());
		Profile.initProfilesFromString(Setting.profilesString);
	}
}
