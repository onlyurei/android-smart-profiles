package com.intofan.android.smartprofiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverBootCompleted extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Setting.startAfterBoot) {
			context.startService(new Intent(context, ServiceChangeProfile.class));
		}
	}

}
