package com.intofan.android.smartprofiles;

import android.content.Context;
import android.media.AudioManager;

public class UtilAudio {

	public static void setNormal(Context context) {
		UtilSystem.getAudioManager(context).setRingerMode(
				AudioManager.RINGER_MODE_NORMAL);
	}

	public static void setVibrate(Context context) {
		UtilSystem.getAudioManager(context).setRingerMode(
				AudioManager.RINGER_MODE_VIBRATE);
		UtilSystem.getAudioManager(context).setVibrateSetting(
				AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_ONLY_SILENT);
		UtilSystem.getAudioManager(context).setVibrateSetting(
				AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_ONLY_SILENT);
	}

	public static void setSilent(Context context) {
		UtilSystem.getAudioManager(context).setRingerMode(
				AudioManager.RINGER_MODE_SILENT);
	}

}
