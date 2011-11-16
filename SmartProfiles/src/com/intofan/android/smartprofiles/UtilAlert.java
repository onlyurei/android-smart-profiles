package com.intofan.android.smartprofiles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class UtilAlert {
	public static final void toast(Context context, String text, int length) {
		Toast.makeText(context, text, length).show();
	}

	public static final void alert(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNeutralButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}

				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static final void showQuickSettings(final Context context) {
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.quick_volume_settings_dialog);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		DisplayMetrics dm = new DisplayMetrics();
		UtilSystem.getWindowManager(context).getDefaultDisplay().getMetrics(dm);
		ScrollView scrollViewQuickSettings = (ScrollView) dialog
				.findViewById(R.id.scrollview_quick_settings);
		scrollViewQuickSettings.setMinimumWidth(dm.widthPixels - 10);
		scrollViewQuickSettings.setScrollbarFadingEnabled(false);

		RadioButton radioButtonRingerModeNormal = (RadioButton) dialog
				.findViewById(R.id.radiobutton_ringer_mode_normal);
		RadioButton radioButtonRingerModeVibrate = (RadioButton) dialog
				.findViewById(R.id.radiobutton_ringer_mode_vibrate);
		RadioButton radioButtonRingerModeSilent = (RadioButton) dialog
				.findViewById(R.id.radiobutton_ringer_mode_silent);
		final SeekBar seekBarRingerVolume = (SeekBar) dialog
				.findViewById(R.id.seekbar_ringer_volume);
		final SeekBar seekBarNotificationVolume = (SeekBar) dialog
				.findViewById(R.id.seekbar_notification_volume);
		final SeekBar seekBarSystemVolume = (SeekBar) dialog
				.findViewById(R.id.seekbar_system_volume);
		SeekBar seekBarAlarmVolume = (SeekBar) dialog
				.findViewById(R.id.seekbar_alarm_volume);
		SeekBar seekBarMediaVolume = (SeekBar) dialog
				.findViewById(R.id.seekbar_media_volume);
		SeekBar seekBarVoiceCallVolume = (SeekBar) dialog
				.findViewById(R.id.seekbar_voice_call_volume);

		int ringerMode = UtilSystem.getAudioManager(context).getRingerMode();

		switch (ringerMode) {
		case AudioManager.RINGER_MODE_NORMAL:
			radioButtonRingerModeNormal.setChecked(true);
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			radioButtonRingerModeVibrate.setChecked(true);
			break;
		case AudioManager.RINGER_MODE_SILENT:
			radioButtonRingerModeSilent.setChecked(true);
			break;
		}

		if (ServiceChangeProfile.currentProfile != null) {
			if (Setting.toast) {
				UtilAlert
						.toast(context,
								context.getString(R.string.quick_volume_settings_items_not_available),
								1);
			}
			if (ServiceChangeProfile.currentProfile.ringerMode != -1) {
				radioButtonRingerModeNormal.setEnabled(false);
				radioButtonRingerModeVibrate.setEnabled(false);
				radioButtonRingerModeSilent.setEnabled(false);
			}
			if (ServiceChangeProfile.currentProfile.ringerVolume != -1
					|| ServiceChangeProfile.currentProfile.ringerMode == AudioManager.RINGER_MODE_VIBRATE
					|| ServiceChangeProfile.currentProfile.ringerMode == AudioManager.RINGER_MODE_SILENT) {
				seekBarRingerVolume.setEnabled(false);
			}
			if (ServiceChangeProfile.currentProfile.notificationVolume != -1
					|| ServiceChangeProfile.currentProfile.ringerMode == AudioManager.RINGER_MODE_VIBRATE
					|| ServiceChangeProfile.currentProfile.ringerMode == AudioManager.RINGER_MODE_SILENT) {
				seekBarNotificationVolume.setEnabled(false);
			}
			if (ServiceChangeProfile.currentProfile.systemVolume != -1
					|| ServiceChangeProfile.currentProfile.ringerMode == AudioManager.RINGER_MODE_VIBRATE
					|| ServiceChangeProfile.currentProfile.ringerMode == AudioManager.RINGER_MODE_SILENT) {
				seekBarSystemVolume.setEnabled(false);
			}
			if (ServiceChangeProfile.currentProfile.alarmVolume != -1) {
				seekBarAlarmVolume.setEnabled(false);
			}
			if (ServiceChangeProfile.currentProfile.mediaVolume != -1) {
				seekBarMediaVolume.setEnabled(false);
			}
			if (ServiceChangeProfile.currentProfile.voiceCallVolume != -1) {
				seekBarVoiceCallVolume.setEnabled(false);
			}
		} else {
			if (ringerMode == AudioManager.RINGER_MODE_VIBRATE
					|| ringerMode == AudioManager.RINGER_MODE_SILENT) {
				seekBarRingerVolume.setEnabled(false);
				seekBarNotificationVolume.setEnabled(false);
				seekBarSystemVolume.setEnabled(false);
			}
		}

		radioButtonRingerModeNormal.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UtilSystem.getAudioManager(context).setRingerMode(
						AudioManager.RINGER_MODE_NORMAL);
				seekBarRingerVolume.setEnabled(true);
				seekBarNotificationVolume.setEnabled(true);
				seekBarSystemVolume.setEnabled(true);
				seekBarRingerVolume.setProgress(UtilSystem.getAudioManager(
						context).getStreamVolume(AudioManager.STREAM_RING));
				seekBarNotificationVolume.setProgress(UtilSystem
						.getAudioManager(context).getStreamVolume(
								AudioManager.STREAM_NOTIFICATION));
				seekBarSystemVolume.setProgress(UtilSystem.getAudioManager(
						context).getStreamVolume(AudioManager.STREAM_SYSTEM));
			}
		});
		radioButtonRingerModeVibrate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UtilSystem.getAudioManager(context).setRingerMode(
						AudioManager.RINGER_MODE_VIBRATE);
				seekBarRingerVolume.setEnabled(false);
				seekBarNotificationVolume.setEnabled(false);
				seekBarSystemVolume.setEnabled(false);
				seekBarRingerVolume.setProgress(UtilSystem.getAudioManager(
						context).getStreamVolume(AudioManager.STREAM_RING));
				seekBarNotificationVolume.setProgress(UtilSystem
						.getAudioManager(context).getStreamVolume(
								AudioManager.STREAM_NOTIFICATION));
				seekBarSystemVolume.setProgress(UtilSystem.getAudioManager(
						context).getStreamVolume(AudioManager.STREAM_SYSTEM));
			}
		});
		radioButtonRingerModeSilent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UtilSystem.getAudioManager(context).setRingerMode(
						AudioManager.RINGER_MODE_SILENT);
				seekBarRingerVolume.setEnabled(false);
				seekBarNotificationVolume.setEnabled(false);
				seekBarSystemVolume.setEnabled(false);
				seekBarRingerVolume.setProgress(UtilSystem.getAudioManager(
						context).getStreamVolume(AudioManager.STREAM_RING));
				seekBarNotificationVolume.setProgress(UtilSystem
						.getAudioManager(context).getStreamVolume(
								AudioManager.STREAM_NOTIFICATION));
				seekBarSystemVolume.setProgress(UtilSystem.getAudioManager(
						context).getStreamVolume(AudioManager.STREAM_SYSTEM));
			}
		});

		seekBarRingerVolume.setMax(UtilSystem.getAudioManager(context)
				.getStreamMaxVolume(AudioManager.STREAM_RING));
		seekBarRingerVolume.setProgress(UtilSystem.getAudioManager(context)
				.getStreamVolume(AudioManager.STREAM_RING));
		seekBarNotificationVolume.setMax(UtilSystem.getAudioManager(context)
				.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
		seekBarNotificationVolume.setProgress(UtilSystem.getAudioManager(
				context).getStreamVolume(AudioManager.STREAM_NOTIFICATION));
		seekBarSystemVolume.setMax(UtilSystem.getAudioManager(context)
				.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
		seekBarSystemVolume.setProgress(UtilSystem.getAudioManager(context)
				.getStreamVolume(AudioManager.STREAM_SYSTEM));
		seekBarAlarmVolume.setMax(UtilSystem.getAudioManager(context)
				.getStreamMaxVolume(AudioManager.STREAM_ALARM));
		seekBarAlarmVolume.setProgress(UtilSystem.getAudioManager(context)
				.getStreamVolume(AudioManager.STREAM_ALARM));
		seekBarMediaVolume.setMax(UtilSystem.getAudioManager(context)
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBarMediaVolume.setProgress(UtilSystem.getAudioManager(context)
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		seekBarVoiceCallVolume.setMax(UtilSystem.getAudioManager(context)
				.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
		seekBarVoiceCallVolume.setProgress(UtilSystem.getAudioManager(context)
				.getStreamVolume(AudioManager.STREAM_VOICE_CALL));

		seekBarRingerVolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						UtilSystem.getAudioManager(context).setStreamVolume(
								AudioManager.STREAM_RING, progress, 0);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});
		seekBarNotificationVolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						UtilSystem.getAudioManager(context).setStreamVolume(
								AudioManager.STREAM_NOTIFICATION, progress, 0);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});
		seekBarSystemVolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						UtilSystem.getAudioManager(context).setStreamVolume(
								AudioManager.STREAM_SYSTEM, progress, 0);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});
		seekBarAlarmVolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						UtilSystem.getAudioManager(context).setStreamVolume(
								AudioManager.STREAM_ALARM, progress, 0);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});
		seekBarMediaVolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						UtilSystem.getAudioManager(context).setStreamVolume(
								AudioManager.STREAM_MUSIC, progress, 0);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});
		seekBarVoiceCallVolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						UtilSystem.getAudioManager(context).setStreamVolume(
								AudioManager.STREAM_VOICE_CALL, progress, 0);
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		dialog.show();
	}
}
