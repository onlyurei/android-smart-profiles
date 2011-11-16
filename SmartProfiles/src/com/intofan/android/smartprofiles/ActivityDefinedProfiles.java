package com.intofan.android.smartprofiles;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityDefinedProfiles extends ListActivity implements
		OnClickListener {

	private Button buttonNewProfile;
	private TextView textviewProfilesOrder;
	private BroadcastReceiver receiver;
	private IntentFilter filter;
	public static ProfileArrayAdapter profileListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defined_profiles);
		buttonNewProfile = (Button) findViewById(R.id.button_new_profile);
		textviewProfilesOrder = (TextView) findViewById(R.id.textview_profiles_order);
		buttonNewProfile.setOnClickListener((OnClickListener) this);
		profileListAdapter = new ProfileArrayAdapter(this,
				R.layout.profile_list);
		registerForContextMenu(getListView());
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				refreshList();
			}
		};
		filter = new IntentFilter();
		filter.addAction(getString(R.string.intent_action_active_profile_changed));
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshList();
		registerReceiver(receiver, filter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_new_profile:
			ActivitySetProfile.profile = new Profile();
			ActivitySetProfile.isNew = true;
			startActivity(new Intent(getApplicationContext(),
					ActivitySetProfile.class));
			break;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Profile p = Profile.getProfiles().get(position);
		Profile profile = new Profile();
		profile.clone(p);
		ActivitySetProfile.profile = profile;
		ActivitySetProfile.isNew = false;
		ActivitySetProfile.oldName = profile.name;
		v.getContext().startActivity(
				new Intent(v.getContext(), ActivitySetProfile.class));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		if (Profile.getProfiles().get(info.position).locked) {
			menu.add(0, Setting.MENU_OPTION_UNLOCK, 0, R.string.unlock);
		} else if (!Profile.hasLockedProfile) {
			menu.add(0, Setting.MENU_OPTION_LOCK, 0, R.string.lock);
		}
		menu.add(0, Setting.MENU_OPTION_DELETE, 0, R.string.delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Profile p = (Profile) getListView().getAdapter().getItem(info.position);
		switch (item.getItemId()) {
		case Setting.MENU_OPTION_DELETE:
			Profile.removeProfile(p);
			break;
		case Setting.MENU_OPTION_LOCK:
			if (!Profile.hasLockedProfile) {
				p.locked = true;
				Profile.hasLockedProfile = true;
			} else {
				return false;
			}
			break;
		case Setting.MENU_OPTION_UNLOCK:
			if (Profile.hasLockedProfile) {
				p.locked = false;
				Profile.hasLockedProfile = false;
			} else {
				return false;
			}
			break;
		default:
			return false;
		}
		Setting.saveProfilesString(getApplicationContext(),
				Profile.toProfilesString());
		refreshList();
		if (ServiceChangeProfile.started) {
			stopService(new Intent(getApplicationContext(),
					ServiceChangeProfile.class));
			startService(new Intent(getApplicationContext(),
					ServiceChangeProfile.class));
		}
		return true;
	}

	public void refreshList() {
		if (Setting.profileListSortingMethod == Setting.PROFILE_LIST_SORTING_PRIORITY) {
			textviewProfilesOrder
					.setText(getString(R.string.ordered_by_profile_priorities));
		} else if (Setting.profileListSortingMethod == Setting.PROFILE_LIST_SORTING_NAME) {
			textviewProfilesOrder
					.setText(getString(R.string.ordered_by_profile_names));
		}
		profileListAdapter.notifyDataSetChanged();
		setListAdapter(profileListAdapter);
	}
}

final class ProfileArrayAdapter extends ArrayAdapter<Profile> {

	private Context context;

	public ProfileArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId, Profile.getProfiles());
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.profile_list, null);
		}
		Profile p = Profile.getProfiles().get(position);
		LinearLayout ll = (LinearLayout) v
				.findViewById(R.id.linearlayout_defined_profiles_item);
		if (p != null) {
			if (!ServiceChangeProfile.started || ServiceChangeProfile.paused) {
				ll.setBackgroundResource(R.color.grey);
			} else {
				if (p.name.compareTo(Profile.activeProfileName) == 0) {
					ll.setBackgroundResource(R.color.gold);
				} else {
					if (p.ssids.size() > 0) {
						if (ServiceChangeProfile.isWifiEnabled) {
							ll.setBackgroundResource(R.color.transparent);
						} else {
							ll.setBackgroundResource(R.color.grey);
						}
					}
				}
			}
			TextView t1 = (TextView) v
					.findViewById(R.id.textview_defined_profiles_t1);
			TextView t2 = (TextView) v
					.findViewById(R.id.textview_defined_profiles_t2);
			TextView t3 = (TextView) v
					.findViewById(R.id.textview_defined_profiles_t3);
			TextView t4 = (TextView) v
					.findViewById(R.id.textview_defined_profiles_t4);
			String[] list = new String[4];
			list = p.toListItem(context);
			t1.setText(list[0]);
			t2.setText(list[1]);
			t3.setText(list[2]);
			t4.setText(list[3]);
		}
		return v;
	}
}