package com.intofan.android.smartprofiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class ActivitySetProfile extends Activity implements
		OnCheckedChangeListener, OnSeekBarChangeListener, OnClickListener {

	private EditText editTextProfileName;
	private Button buttonScanWifi;
	private Button buttonSelectKnownWifi;
	private EditText editTextSsid;
	private Button buttonAddSsid;
	private TextView textViewSelectedWifi;
	private TextView textViewMultiWifiAlert;
	private Button buttonClearWifi;
	private CheckBox checkBoxSunday;
	private CheckBox checkBoxMonday;
	private CheckBox checkBoxTuesday;
	private CheckBox checkBoxWednesday;
	private CheckBox checkBoxThursday;
	private CheckBox checkBoxFriday;
	private CheckBox checkBoxSaturday;
	private TimePicker timePickerStartTime;
	private TimePicker timePickerEndTime;
	private RadioButton radioButtonRingerModeNormal;
	private RadioButton radioButtonRingerModeVibrate;
	private RadioButton radioButtonRingerModeSilent;
	private CheckBox checkBoxSetRingerVolume;
	private CheckBox checkBoxSetSystemVolume;
	private CheckBox checkBoxSetNotificationVolume;
	private CheckBox checkBoxSetAlarmVolume;
	private CheckBox checkBoxSetMediaVolume;
	private CheckBox checkBoxSetVoiceCallVolume;
	private TextView textViewSetRingerVolume;
	private TextView textViewSetSystemVolume;
	private TextView textViewSetNotificationVolume;
	private TextView textViewSetAlarmVolume;
	private TextView textViewSetMediaVolume;
	private TextView textViewSetVoiceCallVolume;
	private SeekBar seekBarRingerVolume;
	private SeekBar seekBarNotificationVolume;
	private SeekBar seekBarSystemVolume;
	private SeekBar seekBarAlarmVolume;
	private SeekBar seekBarMediaVolume;
	private SeekBar seekBarVoiceCallVolume;
	private Button buttonSummary;
	private Button buttonSave;
	private Button buttonCancel;

	public static Profile profile;
	public static boolean isNew;
	public static String oldName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_profile);

		editTextProfileName = (EditText) findViewById(R.id.edittext_profile_name);
		buttonScanWifi = (Button) findViewById(R.id.button_scan_wifi);
		buttonSelectKnownWifi = (Button) findViewById(R.id.button_select_known_wifi);
		editTextSsid = (EditText) findViewById(R.id.edittext_ssid);
		buttonAddSsid = (Button) findViewById(R.id.button_add_ssid);
		textViewSelectedWifi = (TextView) findViewById(R.id.textview_selected_wifi_networks);
		textViewMultiWifiAlert = (TextView) findViewById(R.id.textview_multi_wifi_alert);
		buttonClearWifi = (Button) findViewById(R.id.button_clear_wifi_networks);
		checkBoxSunday = (CheckBox) findViewById(R.id.checkbox_sunday);
		checkBoxMonday = (CheckBox) findViewById(R.id.checkbox_monday);
		checkBoxTuesday = (CheckBox) findViewById(R.id.checkbox_tuesday);
		checkBoxWednesday = (CheckBox) findViewById(R.id.checkbox_wednesday);
		checkBoxThursday = (CheckBox) findViewById(R.id.checkbox_thursday);
		checkBoxFriday = (CheckBox) findViewById(R.id.checkbox_friday);
		checkBoxSaturday = (CheckBox) findViewById(R.id.checkbox_saturday);
		timePickerStartTime = (TimePicker) findViewById(R.id.timepicker_start_time);
		timePickerStartTime.setIs24HourView(Setting.is24hour);
		timePickerEndTime = (TimePicker) findViewById(R.id.timepicker_end_time);
		timePickerEndTime.setIs24HourView(Setting.is24hour);
		radioButtonRingerModeNormal = (RadioButton) findViewById(R.id.radiobutton_ringer_mode_normal);
		radioButtonRingerModeVibrate = (RadioButton) findViewById(R.id.radiobutton_ringer_mode_vibrate);
		radioButtonRingerModeSilent = (RadioButton) findViewById(R.id.radiobutton_ringer_mode_silent);
		checkBoxSetRingerVolume = (CheckBox) findViewById(R.id.checkbox_set_ringer_volume);
		checkBoxSetSystemVolume = (CheckBox) findViewById(R.id.checkbox_set_system_volume);
		checkBoxSetNotificationVolume = (CheckBox) findViewById(R.id.checkbox_set_notification_volume);
		checkBoxSetAlarmVolume = (CheckBox) findViewById(R.id.checkbox_set_alarm_volume);
		checkBoxSetMediaVolume = (CheckBox) findViewById(R.id.checkbox_set_media_volume);
		checkBoxSetVoiceCallVolume = (CheckBox) findViewById(R.id.checkbox_set_voice_call_volume);
		textViewSetRingerVolume = (TextView) findViewById(R.id.textview_set_ringer_volume);
		textViewSetSystemVolume = (TextView) findViewById(R.id.textview_set_system_volume);
		textViewSetNotificationVolume = (TextView) findViewById(R.id.textview_set_notification_volume);
		textViewSetAlarmVolume = (TextView) findViewById(R.id.textview_set_alarm_volume);
		textViewSetMediaVolume = (TextView) findViewById(R.id.textview_set_media_volume);
		textViewSetVoiceCallVolume = (TextView) findViewById(R.id.textview_set_voice_call_volume);
		seekBarRingerVolume = (SeekBar) findViewById(R.id.seekbar_ringer_volume);
		seekBarNotificationVolume = (SeekBar) findViewById(R.id.seekbar_notification_volume);
		seekBarSystemVolume = (SeekBar) findViewById(R.id.seekbar_system_volume);
		seekBarAlarmVolume = (SeekBar) findViewById(R.id.seekbar_alarm_volume);
		seekBarMediaVolume = (SeekBar) findViewById(R.id.seekbar_media_volume);
		seekBarVoiceCallVolume = (SeekBar) findViewById(R.id.seekbar_voice_call_volume);
		buttonSummary = (Button) findViewById(R.id.button_summary);
		buttonSave = (Button) findViewById(R.id.button_save_profile);
		buttonCancel = (Button) findViewById(R.id.button_cancel);

		if (profile.ssids.size() == 0) {
			textViewSelectedWifi.setText(R.string.not_specified);
			textViewMultiWifiAlert.setVisibility(View.GONE);
			buttonClearWifi.setEnabled(false);
		} else {
			String str = "";
			str += "\n";
			for (String wifi : profile.ssids) {
				str += wifi + "\n";
			}
			textViewSelectedWifi.setText(str);
			if (profile.ssids.size() == 1) {
				textViewMultiWifiAlert.setVisibility(View.GONE);
			}
			buttonClearWifi.setEnabled(true);
		}

		buttonScanWifi.setOnClickListener(this);
		buttonSelectKnownWifi.setOnClickListener(this);
		buttonAddSsid.setOnClickListener(this);
		buttonClearWifi.setOnClickListener(this);

		setDayCheckBox("1", checkBoxMonday);
		setDayCheckBox("2", checkBoxTuesday);
		setDayCheckBox("3", checkBoxWednesday);
		setDayCheckBox("4", checkBoxThursday);
		setDayCheckBox("5", checkBoxFriday);
		setDayCheckBox("6", checkBoxSaturday);
		setDayCheckBox("0", checkBoxSunday);

		timePickerStartTime.setCurrentHour(profile.startHour);
		timePickerStartTime.setCurrentMinute(profile.startMin);
		timePickerStartTime
				.setOnTimeChangedListener(new OnTimeChangedListener() {

					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {
						if (hourOfDay > profile.endHour
								|| hourOfDay == profile.endHour
								&& minute > profile.endMin) {
							timePickerStartTime.setCurrentHour(0);
							timePickerStartTime.setCurrentMinute(0);
							UtilAlert.alert(
									view.getContext(),
									getString(R.string.error),
									getString(R.string.start_time_cannot_be_after_end_time));
						} else {
							profile.startHour = hourOfDay;
							profile.startMin = minute;
						}
					}

				});
		timePickerEndTime.setCurrentHour(profile.endHour);
		timePickerEndTime.setCurrentMinute(profile.endMin);
		timePickerEndTime.setOnTimeChangedListener(new OnTimeChangedListener() {

			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if (hourOfDay < profile.startHour
						|| hourOfDay == profile.startHour
						&& minute < profile.startMin) {
					timePickerEndTime.setCurrentHour(23);
					timePickerEndTime.setCurrentMinute(59);
					UtilAlert.alert(
							view.getContext(),
							getString(R.string.error),
							getString(R.string.end_time_cannot_be_ahead_of_start_time));
				} else {
					profile.endHour = hourOfDay;
					profile.endMin = minute;
				}
			}

		});

		editTextProfileName.setText(profile.name);
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (source.charAt(i) == '\\') {
						return "";
					}
				}
				return null;
			}
		};
		editTextProfileName.setFilters(new InputFilter[] { filter });
		editTextProfileName.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				profile.name = editTextProfileName.getText().toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});

		setRingerModeRadioButton(radioButtonRingerModeNormal);
		setRingerModeRadioButton(radioButtonRingerModeVibrate);
		setRingerModeRadioButton(radioButtonRingerModeSilent);

		setSeekBar(checkBoxSetRingerVolume, textViewSetRingerVolume,
				seekBarRingerVolume, profile.ringerVolume, UtilSystem
						.getAudioManager(getApplicationContext())
						.getStreamMaxVolume(AudioManager.STREAM_RING),
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamVolume(AudioManager.STREAM_RING));
		setSeekBar(checkBoxSetNotificationVolume,
				textViewSetNotificationVolume, seekBarNotificationVolume,
				profile.notificationVolume,
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION),
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
		setSeekBar(checkBoxSetSystemVolume, textViewSetSystemVolume,
				seekBarSystemVolume, profile.systemVolume, UtilSystem
						.getAudioManager(getApplicationContext())
						.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamVolume(AudioManager.STREAM_SYSTEM));
		setSeekBar(checkBoxSetAlarmVolume, textViewSetAlarmVolume,
				seekBarAlarmVolume, profile.alarmVolume, UtilSystem
						.getAudioManager(getApplicationContext())
						.getStreamMaxVolume(AudioManager.STREAM_ALARM),
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamVolume(AudioManager.STREAM_ALARM));
		setSeekBar(checkBoxSetMediaVolume, textViewSetMediaVolume,
				seekBarMediaVolume, profile.mediaVolume, UtilSystem
						.getAudioManager(getApplicationContext())
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamVolume(AudioManager.STREAM_MUSIC));
		setSeekBar(checkBoxSetVoiceCallVolume, textViewSetVoiceCallVolume,
				seekBarVoiceCallVolume, profile.voiceCallVolume, UtilSystem
						.getAudioManager(getApplicationContext())
						.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
				UtilSystem.getAudioManager(getApplicationContext())
						.getStreamVolume(AudioManager.STREAM_VOICE_CALL));

		buttonSummary.setOnClickListener(this);
		buttonSave.setOnClickListener(this);
		buttonCancel.setOnClickListener(this);

	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.checkbox_monday:
			setOnDayCheckedChanged("1", buttonView, isChecked);
			break;
		case R.id.checkbox_tuesday:
			setOnDayCheckedChanged("2", buttonView, isChecked);
			break;
		case R.id.checkbox_wednesday:
			setOnDayCheckedChanged("3", buttonView, isChecked);
			break;
		case R.id.checkbox_thursday:
			setOnDayCheckedChanged("4", buttonView, isChecked);
			break;
		case R.id.checkbox_friday:
			setOnDayCheckedChanged("5", buttonView, isChecked);
			break;
		case R.id.checkbox_saturday:
			setOnDayCheckedChanged("6", buttonView, isChecked);
			break;
		case R.id.checkbox_sunday:
			setOnDayCheckedChanged("0", buttonView, isChecked);
			break;
		case R.id.checkbox_set_ringer_volume:
			if (!isChecked) {
				profile.ringerVolume = -1;
				textViewSetRingerVolume.setVisibility(View.VISIBLE);
				seekBarRingerVolume.setVisibility(View.GONE);
			} else {
				profile.ringerVolume = seekBarRingerVolume.getProgress();
				textViewSetRingerVolume.setVisibility(View.GONE);
				seekBarRingerVolume.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.checkbox_set_system_volume:
			if (!isChecked) {
				profile.systemVolume = -1;
				textViewSetSystemVolume.setVisibility(View.VISIBLE);
				seekBarSystemVolume.setVisibility(View.GONE);
			} else {
				profile.systemVolume = seekBarSystemVolume.getProgress();
				textViewSetSystemVolume.setVisibility(View.GONE);
				seekBarSystemVolume.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.checkbox_set_notification_volume:
			if (!isChecked) {
				profile.notificationVolume = -1;
				textViewSetNotificationVolume.setVisibility(View.VISIBLE);
				seekBarNotificationVolume.setVisibility(View.GONE);
			} else {
				profile.notificationVolume = seekBarNotificationVolume
						.getProgress();
				textViewSetNotificationVolume.setVisibility(View.GONE);
				seekBarNotificationVolume.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.checkbox_set_alarm_volume:
			if (!isChecked) {
				profile.alarmVolume = -1;
				textViewSetAlarmVolume.setVisibility(View.VISIBLE);
				seekBarAlarmVolume.setVisibility(View.GONE);
			} else {
				profile.alarmVolume = seekBarAlarmVolume.getProgress();
				textViewSetAlarmVolume.setVisibility(View.GONE);
				seekBarAlarmVolume.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.checkbox_set_media_volume:
			if (!isChecked) {
				profile.mediaVolume = -1;
				textViewSetMediaVolume.setVisibility(View.VISIBLE);
				seekBarMediaVolume.setVisibility(View.GONE);
			} else {
				profile.mediaVolume = seekBarMediaVolume.getProgress();
				textViewSetMediaVolume.setVisibility(View.GONE);
				seekBarMediaVolume.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.checkbox_set_voice_call_volume:
			if (!isChecked) {
				profile.voiceCallVolume = -1;
				textViewSetVoiceCallVolume.setVisibility(View.VISIBLE);
				seekBarVoiceCallVolume.setVisibility(View.GONE);
			} else {
				profile.voiceCallVolume = seekBarVoiceCallVolume.getProgress();
				textViewSetVoiceCallVolume.setVisibility(View.GONE);
				seekBarVoiceCallVolume.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.radiobutton_ringer_mode_normal:
			if (isChecked) {
				profile.ringerMode = AudioManager.RINGER_MODE_NORMAL;
				checkBoxSetRingerVolume.setVisibility(View.VISIBLE);
				checkBoxSetSystemVolume.setVisibility(View.VISIBLE);
				checkBoxSetNotificationVolume.setVisibility(View.VISIBLE);
				checkBoxSetAlarmVolume.setVisibility(View.VISIBLE);
				checkBoxSetMediaVolume.setVisibility(View.VISIBLE);
				checkBoxSetVoiceCallVolume.setVisibility(View.VISIBLE);
				textViewSetRingerVolume.setText(R.string.check_to_set);
				textViewSetSystemVolume.setText(R.string.check_to_set);
				textViewSetNotificationVolume.setText(R.string.check_to_set);
				textViewSetAlarmVolume.setText(R.string.check_to_set);
				textViewSetMediaVolume.setText(R.string.check_to_set);
				textViewSetVoiceCallVolume.setText(R.string.check_to_set);
			}
			break;
		case R.id.radiobutton_ringer_mode_vibrate:
			if (isChecked) {
				profile.ringerMode = AudioManager.RINGER_MODE_VIBRATE;
				checkBoxSetRingerVolume.setChecked(false);
				checkBoxSetRingerVolume.setVisibility(View.GONE);
				checkBoxSetSystemVolume.setChecked(false);
				checkBoxSetSystemVolume.setVisibility(View.GONE);
				checkBoxSetNotificationVolume.setChecked(false);
				checkBoxSetNotificationVolume.setVisibility(View.GONE);
				checkBoxSetAlarmVolume.setVisibility(View.VISIBLE);
				checkBoxSetMediaVolume.setVisibility(View.VISIBLE);
				checkBoxSetVoiceCallVolume.setVisibility(View.VISIBLE);
				textViewSetRingerVolume.setText(R.string.vibrate);
				textViewSetSystemVolume.setText(R.string.silent);
				textViewSetNotificationVolume.setText(R.string.vibrate);
				textViewSetAlarmVolume.setText(R.string.check_to_set);
				textViewSetMediaVolume.setText(R.string.check_to_set);
				textViewSetVoiceCallVolume.setText(R.string.check_to_set);
			}
			break;
		case R.id.radiobutton_ringer_mode_silent:
			if (isChecked) {
				profile.ringerMode = AudioManager.RINGER_MODE_SILENT;
				checkBoxSetRingerVolume.setChecked(false);
				checkBoxSetRingerVolume.setVisibility(View.GONE);
				checkBoxSetSystemVolume.setChecked(false);
				checkBoxSetSystemVolume.setVisibility(View.GONE);
				checkBoxSetNotificationVolume.setChecked(false);
				checkBoxSetNotificationVolume.setVisibility(View.GONE);
				checkBoxSetAlarmVolume.setVisibility(View.VISIBLE);
				checkBoxSetMediaVolume.setVisibility(View.VISIBLE);
				checkBoxSetVoiceCallVolume.setVisibility(View.VISIBLE);
				textViewSetRingerVolume.setText(R.string.silent);
				textViewSetSystemVolume.setText(R.string.silent);
				textViewSetNotificationVolume.setText(R.string.silent);
				textViewSetAlarmVolume.setText(R.string.check_to_set);
				textViewSetMediaVolume.setText(R.string.check_to_set);
				textViewSetVoiceCallVolume.setText(R.string.check_to_set);
			}
			break;
		default:
			break;
		}
	}

	private void setDayCheckBox(String day, CheckBox checkBox) {
		if (profile.days.contains(day)) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		checkBox.setOnCheckedChangeListener(this);
	}

	private void setOnDayCheckedChanged(String day, CompoundButton buttonView,
			boolean isChecked) {
		if (isChecked) {
			if (!profile.days.contains(day)) {
				profile.days.add(day);
			}
		} else {
			if (profile.days.contains(day)) {
				if (profile.days.size() == 1) {
					buttonView.setChecked(true);
					UtilAlert
							.toast(getApplicationContext(),
									getString(R.string.at_least_one_day_of_week_should_be_selected),
									1);
				} else {
					profile.days.remove(day);
				}
			}
		}
		Collections.sort(profile.days);
	}

	private void setRingerModeRadioButton(RadioButton radioButton) {
		switch (radioButton.getId()) {
		case R.id.radiobutton_ringer_mode_normal:
			if (profile.ringerMode == AudioManager.RINGER_MODE_NORMAL) {
				radioButton.setChecked(true);
			}
			break;
		case R.id.radiobutton_ringer_mode_vibrate:
			if (profile.ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
				radioButton.setChecked(true);
			}
			break;
		case R.id.radiobutton_ringer_mode_silent:
			if (profile.ringerMode == AudioManager.RINGER_MODE_SILENT) {
				radioButton.setChecked(true);
			}
			break;
		default:
			break;
		}
		radioButton.setOnCheckedChangeListener(this);
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.seekbar_ringer_volume:
			profile.ringerVolume = progress;
			break;
		case R.id.seekbar_system_volume:
			profile.systemVolume = progress;
			break;
		case R.id.seekbar_notification_volume:
			profile.notificationVolume = progress;
			break;
		case R.id.seekbar_alarm_volume:
			profile.alarmVolume = progress;
			break;
		case R.id.seekbar_media_volume:
			profile.mediaVolume = progress;
			break;
		case R.id.seekbar_voice_call_volume:
			profile.voiceCallVolume = progress;
			break;
		default:
			break;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_scan_wifi:
			buttonScanWifi.setText(R.string.scanning___);
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
			int wifiState = UtilSystem.getWifiManager(getApplicationContext())
					.getWifiState();
			if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
				if (UtilSystem.getWifiManager(getApplicationContext())
						.startScan()) {
					BroadcastReceiver receiver = new BroadcastReceiver() {

						public void onReceive(Context context, Intent intent) {
							try {
								context.unregisterReceiver(this);
							} catch (Exception e) {
							}
							List<ScanResult> networks = UtilSystem
									.getWifiManager(context).getScanResults();
							if (networks != null && networks.size() > 0) {
								List<String> networkList = new ArrayList<String>();
								for (ScanResult network : networks) {
									if (!networkList.contains(network.SSID)) {
										networkList.add(network.SSID);
									}
								}
								showWifiNetworks(networkList, context);
							} else {
								UtilAlert
										.alert(context,
												getString(R.string.info),
												getString(R.string.no_wifi_networks_detected));
								buttonScanWifi.setText(R.string.scan_now);
							}
						}
					};
					registerReceiver(receiver, intentFilter);
				} else {
					UtilAlert.alert(this, getString(R.string.error),
							getString(R.string.error_failed_to_scan_wifi));
					buttonScanWifi.setText(R.string.scan_now);
				}
			} else {
				UtilAlert.alert(this, getString(R.string.info),
						getString(R.string.wifi_is_off));
				buttonScanWifi.setText(R.string.scan_now);
			}
			break;
		case R.id.button_select_known_wifi:
			List<WifiConfiguration> configs = UtilSystem.getWifiManager(
					getApplicationContext()).getConfiguredNetworks();
			List<String> networkList = new ArrayList<String>();
			String ssid;
			if (configs != null && configs.size() > 0) {
				for (WifiConfiguration config : configs) {
					ssid = config.SSID;
					ssid = ssid.replace("\"", "");
					if (!networkList.contains(ssid)) {
						networkList.add(ssid);
					}
				}
			}
			if (networkList.size() > 0) {
				showWifiNetworks(networkList, this);
			} else {
				UtilAlert.alert(this, getString(R.string.info),
						getString(R.string.no_wifi_networks_configured));
			}
			break;
		case R.id.button_add_ssid:
			String ssids = editTextSsid.getText().toString();
			String[] temp;
			String delimiter = ",";
			temp = ssids.split(delimiter);
			if (temp != null && temp.length > 0) {
				for (String item : temp) {
					if (item != "" && item != " "
							&& !profile.ssids.contains(item)) {
						profile.ssids.add(item);
					}
				}
			}
			if (profile.ssids.size() > 0) {
				Collections.sort(profile.ssids,
						alphabeticalIgnoreCaseComparator);
				String str = "";
				str += "\n";
				for (String wifi : profile.ssids) {
					str += wifi + "\n";
				}
				textViewSelectedWifi.setText(str);
				if (profile.ssids.size() > 1) {
					textViewMultiWifiAlert.setVisibility(View.VISIBLE);
				}
				buttonClearWifi.setEnabled(true);
			}
			editTextSsid.setText("");
			break;
		case R.id.button_clear_wifi_networks:
			profile.ssids.clear();
			buttonClearWifi.setEnabled(false);
			textViewSelectedWifi.setText(R.string.not_specified);
			textViewMultiWifiAlert.setVisibility(View.GONE);
			break;
		case R.id.button_summary:
			UtilAlert.alert(this, getString(R.string.profile_summary),
					profile.toSummary(getApplicationContext()));
			break;
		case R.id.button_save_profile:
			String val = validateSettings();
			if (val != "TrUe+_+TrUe") {
				UtilAlert.alert(this, getString(R.string.error), val);
			} else {
				String result;
				if (isNew) {
					result = Profile.addProfile(profile);
				} else {
					result = Profile.updateProfile(profile, oldName);
				}
				if (result == "TrUe+_+TrUe") {
					Setting.saveProfilesString(getApplicationContext(),
							Profile.toProfilesString());
					profile = new Profile();
					isNew = true;
					finish();
					if (ServiceChangeProfile.started) {
						stopService(new Intent(getApplicationContext(),
								ServiceChangeProfile.class));
						startService(new Intent(getApplicationContext(),
								ServiceChangeProfile.class));
					}
					UtilAlert.toast(getApplicationContext(),
							getString(R.string.profile_saved), 1);
				} else {
					if (result == "DuPlIcAtEnAmE+_+DuPlIcAtEnAmE") {
						UtilAlert
								.alert(this,
										getString(R.string.error),
										getString(R.string.this_profile_name_already_exist)
												+ " " + profile.name);
					} else if (result == "PrOFiLeAlReAdYeXiSt+_+PrOFiLeAlReAdYeXiSt") {
						UtilAlert.alert(this, getString(R.string.error),
								getString(R.string.profile_alread_exists));
					} else {
						UtilAlert
								.alert(this,
										getString(R.string.error),
										getString(R.string.this_profile_has_exact_same_rules_as)
												+ " " + result);
					}
				}
			}
			break;
		case R.id.button_cancel:
			finish();
			break;
		default:
			break;
		}
	}

	private static Comparator<String> alphabeticalIgnoreCaseComparator = new Comparator<String>() {
		public int compare(String s1, String s2) {
			return (s1.compareToIgnoreCase(s2));
		}
	};

	private void showWifiNetworks(List<String> networkList, Context context) {
		final CharSequence[] items = new CharSequence[networkList.size()];

		Collections.sort(networkList, alphabeticalIgnoreCaseComparator);
		networkList.toArray(items);
		boolean[] checked = new boolean[items.length];
		for (int i = 0; i < checked.length; i++) {
			if (profile.ssids.contains(items[i].toString())) {
				checked[i] = true;
			} else {
				checked[i] = false;
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.select_wifi_networks);
		builder.setMultiChoiceItems(items, checked,
				new DialogInterface.OnMultiChoiceClickListener() {

					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						if (isChecked) {
							if (!profile.ssids.contains(items[which].toString())) {
								profile.ssids.add(items[which].toString());
							}
						} else {
							if (profile.ssids.contains(items[which].toString())) {
								profile.ssids.remove(items[which].toString());
							}
						}
					}

				});
		builder.setNeutralButton(R.string.add,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						String str = "";
						if (profile.ssids.size() == 0) {
							textViewSelectedWifi
									.setText(R.string.not_specified);
							textViewMultiWifiAlert.setVisibility(View.GONE);
							buttonClearWifi.setEnabled(false);
						} else {
							Collections.sort(profile.ssids,
									alphabeticalIgnoreCaseComparator);
							str += "\n";
							for (String wifi : profile.ssids) {
								str += wifi + "\n";
							}
							textViewSelectedWifi.setText(str);
							if (profile.ssids.size() > 1) {
								textViewMultiWifiAlert
										.setVisibility(View.VISIBLE);
							} else {
								textViewMultiWifiAlert.setVisibility(View.GONE);
							}
							buttonClearWifi.setEnabled(true);
						}
						dialog.cancel();
						buttonScanWifi.setText(R.string.scan_now);
					}

				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void setSeekBar(CheckBox checkBox, TextView textView,
			SeekBar seekBar, int profileVolume, int maxVolume, int currentVolume) {
		seekBar.setMax(maxVolume);
		if (profileVolume == -1) {
			seekBar.setProgress(currentVolume);
			checkBox.setChecked(false);
			textView.setVisibility(View.VISIBLE);
			seekBar.setVisibility(View.GONE);
		} else {
			seekBar.setProgress(profileVolume);
			checkBox.setChecked(true);
			textView.setVisibility(View.GONE);
			seekBar.setVisibility(View.VISIBLE);
		}
		seekBar.setOnSeekBarChangeListener(this);
		checkBox.setOnCheckedChangeListener(this);
		switch (profile.ringerMode) {
		case -1:
			checkBox.setVisibility(View.GONE);
			textView.setText(R.string.choose_ringer_mode_first);
			break;
		case AudioManager.RINGER_MODE_NORMAL:
			checkBox.setVisibility(View.VISIBLE);
			textView.setText(R.string.check_to_set);
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			switch (checkBox.getId()) {
			case R.id.checkbox_set_ringer_volume:
				checkBox.setVisibility(View.GONE);
				textView.setText(R.string.vibrate);
				break;
			case R.id.checkbox_set_system_volume:
				checkBox.setVisibility(View.GONE);
				textView.setText(R.string.silent);
				break;
			case R.id.checkbox_set_notification_volume:
				checkBox.setVisibility(View.GONE);
				textView.setText(R.string.vibrate);
				break;
			case R.id.checkbox_set_alarm_volume:
			case R.id.checkbox_set_media_volume:
			case R.id.checkbox_set_voice_call_volume:
				checkBox.setVisibility(View.VISIBLE);
				textView.setText(R.string.check_to_set);
				break;
			default:
				break;
			}
			break;
		case AudioManager.RINGER_MODE_SILENT:
			switch (checkBox.getId()) {
			case R.id.checkbox_set_ringer_volume:
			case R.id.checkbox_set_system_volume:
			case R.id.checkbox_set_notification_volume:
				checkBox.setVisibility(View.GONE);
				textView.setText(R.string.silent);
				break;
			case R.id.checkbox_set_alarm_volume:
			case R.id.checkbox_set_media_volume:
			case R.id.checkbox_set_voice_call_volume:
				checkBox.setVisibility(View.VISIBLE);
				textView.setText(R.string.check_to_set);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	private String validateSettings() {
		String s = "";

		CharSequence inputStr = editTextProfileName.getText();
		String patternStr = "\\s+";
		String replacementStr = " ";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		String output = matcher.replaceAll(replacementStr);
		editTextProfileName.setText(output);
		profile.name = output;

		if (profile.name.length() == 0
				|| Character.isSpaceChar(profile.name.charAt(0))
				|| profile.ringerMode == -1) {
			if (profile.name.length() == 0
					|| Character.isSpaceChar(profile.name.charAt(0))) {
				s += getString(R.string.error_profile_name);
			}
			if (profile.ringerMode == -1) {
				s += getString(R.string.error_ringer_mode);
			}
		} else {
			s = "TrUe+_+TrUe";
		}
		return s;
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}