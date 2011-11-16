package com.intofan.android.smartprofiles;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ActivitySettings extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	public void onPause() {
		super.onPause();
		Setting.loadPreferences(getApplicationContext());
		Profile.sortProfiles();
		if (ServiceChangeProfile.started) {
			stopService(new Intent(getApplicationContext(),
					ServiceChangeProfile.class));
			startService(new Intent(getApplicationContext(),
					ServiceChangeProfile.class));
		}
	}
}
