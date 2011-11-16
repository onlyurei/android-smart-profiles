package com.intofan.android.smartprofiles;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class ActivityMain extends TabActivity {

	public static TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		Resources res = getResources();
		Intent intent;

		intent = new Intent(getApplicationContext(), ActivityActiveProfile.class);
		tabSpec = tabHost
				.newTabSpec("tab_active_profile")
				.setIndicator(getString(R.string.active_profile),
						res.getDrawable(R.drawable.ic_tab_active_profile)).setContent(intent);
		tabHost.addTab(tabSpec);

		intent = new Intent(getApplicationContext(), ActivityDefinedProfiles.class);
		tabSpec = tabHost
				.newTabSpec("tab_defined_profiles")
				.setIndicator(getString(R.string.defined_profiles),
						res.getDrawable(R.drawable.ic_tab_defined_profiles)).setContent(intent);
		tabHost.addTab(tabSpec);

		tabHost.setCurrentTab(0);

		try {
			int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			if (Setting.versionCode < versionCode) {
				showChangeLog();
				Setting.versionCode = versionCode;
				Setting.savePreferences(getApplicationContext());
			}
		} catch (NameNotFoundException e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(getApplicationContext(), ActivitySettings.class));
			return true;
		case R.id.menu_help:
			showHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showHelp() {
		startActivity(new Intent(getApplicationContext(), ActivityHelp.class));
	}

	private void showChangeLog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.change_log))
				.setMessage(R.string.change_log_content)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.rate_the_app),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("market://details?id=com.intofan.android.smartprofiles"));
								try {
									startActivity(intent);
								} catch (Exception e) {
								}
							}
						})
				.setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}