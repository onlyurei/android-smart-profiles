package com.intofan.android.smartprofiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverRingerModeChanged extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!ServiceChangeProfile.paused) {
			if (Setting.toast) {
				UtilAlert
						.toast(context,
								context.getString(R.string.smarter_profiles_service_is_running),
								0);
			}
		}
	}

}
