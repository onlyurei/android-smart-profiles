package com.intofan.android.smartprofiles;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityActiveProfile extends Activity implements OnClickListener {

	private LinearLayout ll;
	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView t4;
	private Button buttonStartService;
	private Button buttonStopService;
	private Button buttonLockProfile;
	private Button buttonUnlockProfile;
	private Button buttonQuickSettings;

	private BroadcastReceiver receiver;
	private IntentFilter filter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_profile);

		ll = (LinearLayout) findViewById(R.id.linearlayout_defined_profiles_item);
		t1 = (TextView) findViewById(R.id.textview_defined_profiles_t1);
		t2 = (TextView) findViewById(R.id.textview_defined_profiles_t2);
		t3 = (TextView) findViewById(R.id.textview_defined_profiles_t3);
		t4 = (TextView) findViewById(R.id.textview_defined_profiles_t4);
		buttonStartService = (Button) findViewById(R.id.button_start_service);
		buttonStopService = (Button) findViewById(R.id.button_stop_service);
		buttonLockProfile = (Button) findViewById(R.id.button_lock_profile);
		buttonUnlockProfile = (Button) findViewById(R.id.button_unlock_profile);
		buttonQuickSettings = (Button) findViewById(R.id.button_quick_settings);

		ll.setOnClickListener((OnClickListener) this);
		buttonStartService.setEnabled(!ServiceChangeProfile.started);
		buttonStartService.setOnClickListener((OnClickListener) this);
		buttonStopService.setEnabled(ServiceChangeProfile.started);
		buttonStopService.setOnClickListener((OnClickListener) this);
		buttonLockProfile.setEnabled(Profile.activeProfileName != ""
				&& !Profile.hasLockedProfile);
		buttonLockProfile.setOnClickListener((OnClickListener) this);
		buttonUnlockProfile.setEnabled(Profile.activeProfileName != ""
				&& Profile.hasLockedProfile);
		buttonUnlockProfile.setOnClickListener((OnClickListener) this);
		buttonQuickSettings.setOnClickListener((OnClickListener) this);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				displayActiveProfile();
			}
		};
		filter = new IntentFilter();
		filter.addAction(getString(R.string.intent_action_active_profile_changed));
	}

	@Override
	public void onResume() {
		super.onResume();
		displayActiveProfile();
		registerReceiver(receiver, filter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearlayout_defined_profiles_item:
			if (ServiceChangeProfile.started) {
				Profile p = Profile.getProfileByName(Profile.activeProfileName);
				if (p != null) {
					Profile profile = new Profile();
					profile.clone(p);
					ActivitySetProfile.profile = profile;
					ActivitySetProfile.isNew = false;
					ActivitySetProfile.oldName = profile.name;
					startActivity(new Intent(v.getContext(),
							ActivitySetProfile.class));
				}
			}
			break;
		case R.id.button_start_service:
			if (startService(new Intent(getApplicationContext(),
					ServiceChangeProfile.class)) != null) {
				buttonStartService.setEnabled(false);
				buttonStopService.setEnabled(true);
			} else {
				UtilAlert
						.toast(getApplicationContext(),
								getString(R.string.error_failed_to_start_smart_profile_service),
								1);
				buttonStartService.setEnabled(true);
				buttonStopService.setEnabled(false);
			}
			break;
		case R.id.button_stop_service:
			buttonStartService.setEnabled(true);
			buttonStopService.setEnabled(false);
			if (!stopService(new Intent(getApplicationContext(),
					ServiceChangeProfile.class))) {
				UtilAlert
						.toast(getApplicationContext(),
								getString(R.string.smarter_profiles_service_is_not_running),
								1);
			}
			break;
		case R.id.button_lock_profile:
			if (!Profile.hasLockedProfile) {
				Profile p = Profile.getProfileByName(Profile.activeProfileName);
				if (p != null) {
					p.locked = true;
					Profile.hasLockedProfile = true;
					Setting.saveProfilesString(getApplicationContext(),
							Profile.toProfilesString());
					displayActiveProfile();
				}
				buttonLockProfile.setEnabled(false);
				buttonUnlockProfile.setEnabled(true);
			}
			break;
		case R.id.button_unlock_profile:
			if (Profile.hasLockedProfile) {
				Profile p = Profile.getProfileByName(Profile.activeProfileName);
				if (p != null) {
					p.locked = false;
					Profile.hasLockedProfile = false;
					Setting.saveProfilesString(getApplicationContext(),
							Profile.toProfilesString());
					displayActiveProfile();
				}
				buttonLockProfile.setEnabled(true);
				buttonUnlockProfile.setEnabled(false);
				if (ServiceChangeProfile.started) {
					stopService(new Intent(getApplicationContext(),
							ServiceChangeProfile.class));
					startService(new Intent(getApplicationContext(),
							ServiceChangeProfile.class));
				}
			}
			break;
		case R.id.button_quick_settings:
			UtilAlert.showQuickSettings(this);
			break;
		}
	}

	private void displayActiveProfile() {
		if (ServiceChangeProfile.paused) {
			ll.setBackgroundResource(R.color.grey);
			t1.setText(getString(R.string.service_is_paused));
			t2.setText("");
			t3.setText("");
			t4.setText("");
		} else {
			if (ServiceChangeProfile.started) {
				Profile p = Profile.getProfileByName(Profile.activeProfileName);
				if (p != null) {
					ll.setBackgroundResource(R.color.gold);
					String[] list = new String[4];
					list = p.toListItem(getApplicationContext());
					t1.setText(list[0]);
					t2.setText(list[1]);
					t3.setText(list[2]);
					t4.setText(list[3]);
				} else {
					ll.setBackgroundResource(R.color.transparent);
					t1.setText(getString(R.string.no_matching_profile_found));
					t2.setText("");
					t3.setText("");
					t4.setText("");
				}
			} else {
				ll.setBackgroundResource(R.color.grey);
				t1.setText(getString(R.string.service_is_not_running));
				t2.setText("");
				t3.setText("");
				t4.setText("");
			}
		}
		if (Profile.activeProfileName != "") {
			if (Profile.hasLockedProfile) {
				buttonLockProfile.setEnabled(false);
				buttonUnlockProfile.setEnabled(true);
			}
			else {
				buttonLockProfile.setEnabled(true);
				buttonUnlockProfile.setEnabled(false);
			}
		}
		else {
			buttonLockProfile.setEnabled(false);
			buttonUnlockProfile.setEnabled(false);
		}
	}

}
