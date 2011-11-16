package com.intofan.android.smartprofiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Setting {
	public static SharedPreferences settings;

	public static final int PROFILE_LIST_SORTING_PRIORITY = 0;
	public static final int PROFILE_LIST_SORTING_NAME = 1;
	public static final int MENU_OPTION_DELETE = 2;
	public static final int MENU_OPTION_LOCK = 3;
	public static final int MENU_OPTION_UNLOCK = 4;

	// preferences
	public static String profilesString = "";
	public static boolean startAfterBoot = true;
	public static boolean notify = true;
	public static boolean toast = false;
	public static boolean pauseWhenWifiIsOff = false;
	public static boolean stayAwake = false;
	public static boolean is24hour = false;
	public static int checkFrequency = 60; // second
	public static int profileListSortingMethod = PROFILE_LIST_SORTING_PRIORITY;

	public static int versionCode = 0;
	
	//public static boolean keyFound = false;
	//public static boolean keyValid = false;

	public static void loadPreferences(Context context) {
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		profilesString = settings.getString("profilesString", "");
		startAfterBoot = settings.getBoolean("startAfterBoot", true);
		notify = settings.getBoolean("notify", true);
		toast = settings.getBoolean("toast", false);
		pauseWhenWifiIsOff = settings.getBoolean("pauseWhenWifiIsOff", false);
		stayAwake = settings.getBoolean("stayAwake", false);
		is24hour = settings.getBoolean("is24hour", false);
		checkFrequency = Integer.parseInt(settings.getString("checkFrequency",
				"60"));
		profileListSortingMethod = Integer.parseInt(settings.getString(
				"profileListSortingMethod", "0"));
		versionCode = settings.getInt("versionCode", 0);
	}

	public static void savePreferences(Context context) {
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("startAfterBoot", startAfterBoot);
		editor.putBoolean("notify", notify);
		editor.putBoolean("toast", toast);
		editor.putBoolean("pauseWhenWifiIsOff", pauseWhenWifiIsOff);
		editor.putBoolean("stayAwake", stayAwake);
		editor.putBoolean("is24hour", is24hour);
		editor.putString("checkFrequency", String.valueOf(checkFrequency));
		editor.putString("profileListSortingMethod",
				String.valueOf(profileListSortingMethod));
		editor.putInt("versionCode", versionCode);
		editor.commit();
	}

	public static void saveProfilesString(Context context, String s) {
		profilesString = s;
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("profilesString", profilesString);
		editor.commit();
	}
}
