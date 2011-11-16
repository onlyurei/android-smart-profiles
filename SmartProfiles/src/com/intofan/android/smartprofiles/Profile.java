package com.intofan.android.smartprofiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.media.AudioManager;
import android.text.format.Time;

public class Profile {
	private static List<Profile> profiles = new ArrayList<Profile>();
	public static String activeProfileName = "";
	public static final String PROFILE_DELIMITER = "DeLiMiTeR=P=DeLiMiTeR";
	private static final String PROFILE_FIELD_DELIMITER = "DeLiMiTeR=F=DeLiMiTeR";
	private static final String PROFILE_FIELD_ARRAY_DELIMITER = ",";

	/* Rules */
	public String name = "";
	public List<String> ssids = new ArrayList<String>();
	public List<String> days = new ArrayList<String>();
	public int startHour = 0;
	public int startMin = 0;
	public int startSec = 0;
	public int endHour = 23;
	public int endMin = 59;
	public int endSec = 59;

	/* Settings */
	public int ringerMode = -1;
	public int ringerVolume = -1;
	public int notificationVolume = -1;
	public int systemVolume = -1;
	public int alarmVolume = -1;
	public int mediaVolume = -1;
	public int voiceCallVolume = -1;

	/* Properties */
	public boolean locked = false;
	public static boolean hasLockedProfile = false;

	public Profile() {
		for (int i = 0; i <= 6; i++) {
			days.add(String.valueOf(i));
		}
	}

	public Profile(String s) {
		String[] p = s.split(PROFILE_FIELD_DELIMITER);
		if (p.length < 16 || p.length > 17)
			return;
		String[] array;
		name = p[0];
		if (p[1].length() > 0) {
			array = p[1].split(PROFILE_FIELD_ARRAY_DELIMITER);
			if (array != null) {
				for (String ssid : array) {
					ssids.add(ssid);
				}
			}
		}
		array = p[2].split(PROFILE_FIELD_ARRAY_DELIMITER);
		if (array != null) {
			for (String day : array) {
				days.add(day);
			}
		}
		startHour = Integer.parseInt(p[3]);
		startMin = Integer.parseInt(p[4]);
		startSec = Integer.parseInt(p[5]);
		endHour = Integer.parseInt(p[6]);
		endMin = Integer.parseInt(p[7]);
		endSec = Integer.parseInt(p[8]);
		ringerMode = Integer.parseInt(p[9]);
		ringerVolume = Integer.parseInt(p[10]);
		notificationVolume = Integer.parseInt(p[11]);
		systemVolume = Integer.parseInt(p[12]);
		alarmVolume = Integer.parseInt(p[13]);
		mediaVolume = Integer.parseInt(p[14]);
		voiceCallVolume = Integer.parseInt(p[15]);
		if (p.length > 16) {
			locked = Boolean.parseBoolean(p[16]);
			if (locked)
				hasLockedProfile = true;
		}
	}

	public String toString() {
		String s = "";
		int i;
		s += name + PROFILE_FIELD_DELIMITER;
		if (ssids.size() > 0) {
			for (i = 0; i < ssids.size() - 1; i++) {
				s += ssids.get(i) + PROFILE_FIELD_ARRAY_DELIMITER;
			}
			s += ssids.get(i);
		}
		s += PROFILE_FIELD_DELIMITER;
		for (i = 0; i < days.size() - 1; i++) {
			s += days.get(i) + PROFILE_FIELD_ARRAY_DELIMITER;
		}
		s += days.get(i) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(startHour) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(startMin) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(startSec) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(endHour) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(endMin) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(endSec) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(ringerMode) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(ringerVolume) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(notificationVolume) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(systemVolume) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(alarmVolume) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(mediaVolume) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(voiceCallVolume) + PROFILE_FIELD_DELIMITER;
		s += String.valueOf(locked);
		return s;
	}

	public void clone(Profile profile) {
		name = profile.name;
		ssids.clear();
		for (String s : profile.ssids) {
			ssids.add(s);
		}
		days.clear();
		for (String d : profile.days) {
			days.add(d);
		}
		startHour = profile.startHour;
		startMin = profile.startMin;
		startSec = profile.startSec;
		endHour = profile.endHour;
		endMin = profile.endMin;
		endSec = profile.endSec;

		ringerMode = profile.ringerMode;
		ringerVolume = profile.ringerVolume;
		notificationVolume = profile.notificationVolume;
		systemVolume = profile.systemVolume;
		alarmVolume = profile.alarmVolume;
		mediaVolume = profile.mediaVolume;
		voiceCallVolume = profile.voiceCallVolume;
	}

	public boolean equals(Profile profile) {
		boolean nameMatch;
		boolean ssidsMatch;
		boolean daysMatch;
		boolean timeMatch;
		nameMatch = name.compareTo(profile.name) == 0;
		ssidsMatch = ssids.size() == profile.ssids.size()
				&& (ssids.size() == 0 || ssids.containsAll(profile.ssids));
		daysMatch = days.size() == profile.days.size()
				&& (days.size() == 0 || days.containsAll(profile.days));
		timeMatch = startHour == profile.startHour
				&& startMin == profile.startMin && startSec == profile.startSec
				&& endHour == profile.endHour && endMin == profile.endMin
				&& endSec == profile.endSec;
		return nameMatch && ssidsMatch && daysMatch && timeMatch;
	}

	public int getTimeSpan() {
		return ((endHour - startHour) * 3600 + (endMin - startMin) * 60 + (endSec - startSec));
	}

	private static Comparator<Profile> specifityComparator = new Comparator<Profile>() {
		public int compare(Profile p1, Profile p2) {
			int result = p2.ssids.size() - p1.ssids.size();
			if (result == 0) {
				result = p1.days.size() - p2.days.size();
				if (result == 0) {
					result = p1.getTimeSpan() - p2.getTimeSpan();
					if (result == 0) {
						return (p1.name.compareToIgnoreCase(p2.name));
					}
				}
			}
			return result;
		}
	};

	private static Comparator<Profile> nameComparator = new Comparator<Profile>() {
		public int compare(Profile p1, Profile p2) {
			return (p1.name.compareToIgnoreCase(p2.name));
		}
	};

	public static String addProfile(Profile profile) {
		String result = validateProfile(profile);
		if (result == "TrUe+_+TrUe") {
			profiles.add(profile);
			sortProfiles();
		}
		return result;
	}

	public static void sortProfiles() {
		if (Setting.profileListSortingMethod == Setting.PROFILE_LIST_SORTING_PRIORITY) {
			Collections.sort(profiles, specifityComparator);
		} else if (Setting.profileListSortingMethod == Setting.PROFILE_LIST_SORTING_NAME) {
			Collections.sort(profiles, nameComparator);
		}
	}

	public static Profile getProfileByName(String name) {
		if (name == "" || name == null || profiles.size() == 0) {
			return null;
		}
		for (Profile p : profiles) {
			if (p.name.compareTo(name) == 0) {
				return p;
			}
		}
		return null;
	}

	public static List<Profile> getProfiles() {
		return profiles;
	}

	// returns the profile in @profileList that has most matching ssids in
	// @ssidList,
	// and which has the minimal span between start time and end
	// time in which @time falls
	public static Profile matchProfile(List<Profile> profileList,
			HashMap<String, Integer> wifiSignals, Time time) {
		// no profiles yet
		if (profileList == null || profileList.size() == 0 || time == null) {
			return null;
		}
		// no wifi networks detected
		if (wifiSignals == null || wifiSignals.size() == 0) {
			return matchProfile(profileList, time);
		}
		// detected at least one wifi network
		List<Profile> candidates = new ArrayList<Profile>();
		// do time match first
		Time start = new Time();
		Time end = new Time();
		for (Profile profile : profileList) {
			if (profile.locked)
				return profile;
			if (profile.days.contains(String.valueOf(time.weekDay))) {
				start.hour = profile.startHour;
				start.minute = profile.startMin;
				start.second = profile.startSec;
				end.hour = profile.endHour;
				end.minute = profile.endMin;
				end.second = profile.endSec;
				if (UtilTime.isAfter(time, start)
						&& UtilTime.isBefore(time, end)) {
					candidates.add(profile);
				}
			}
		}
		if (candidates.size() > 0) {
			// then do ssids match
			HashMap<Profile, Integer> matchedSsids = new HashMap<Profile, Integer>();
			List<Profile> profilesToRemove = new ArrayList<Profile>();
			int num;
			for (Profile profile : candidates) {
				// specified wifi networks in a profile - match with detected
				// wifi
				// networks
				num = 0;
				if (profile.ssids.size() > 0) {
					for (String ssid : profile.ssids) {
						if (wifiSignals.keySet().contains(ssid)) {
							num++;
						}
					}
					// all of the specified SSIDs must be detected
					if (num != profile.ssids.size()) {
						profilesToRemove.add(profile);
					} else {
						matchedSsids.put(profile, num);
					}
				} else {
					matchedSsids.put(profile, num);
				}
			}
			candidates.removeAll(profilesToRemove);
			// find the most specific profile(s) (with max number of matched
			// ssid(s))
			int max = 0;
			for (Integer m : matchedSsids.values()) {
				if (m > max) {
					max = m;
				}
			}
			for (Entry<Profile, Integer> set : matchedSsids.entrySet()) {
				if (set.getValue() < max) {
					profilesToRemove.add(set.getKey());
				}
			}
			candidates.removeAll(profilesToRemove);
			int min = 7;
			for (Profile profile : candidates) {
				if (profile.days.size() < min) {
					min = profile.days.size();
				}
			}
			List<Profile> toRemove = new ArrayList<Profile>();
			for (Profile profile : candidates) {
				if (profile.days.size() > min) {
					toRemove.add(profile);
				}
			}
			candidates.removeAll(toRemove);
			min = 3600 * 24;
			for (Profile profile : candidates) {
				if (profile.getTimeSpan() < min) {
					min = profile.getTimeSpan();
				}
			}
			toRemove = new ArrayList<Profile>();
			for (Profile profile : candidates) {
				if (profile.getTimeSpan() > min) {
					toRemove.add(profile);
				}
			}
			candidates.removeAll(toRemove);
			if (candidates.size() > 0) {
				if (candidates.size() > 1) {
					double maxP = 0.0;
					HashMap<String, Double> candidateStrengths = new HashMap<String, Double>();
					double power;
					for (Profile profile : candidates) {
						power = 0.0;
						for (String SSID : profile.ssids) {
							if (wifiSignals.keySet().contains(SSID)) {
								power += Math.pow(10,
										wifiSignals.get(SSID) / 10.0);
							}
						}
						candidateStrengths.put(profile.name, power);
						if (power > maxP)
							maxP = power;
					}
					toRemove = new ArrayList<Profile>();
					for (Profile profile : candidates) {
						if (candidateStrengths.get(profile.name) < maxP) {
							toRemove.add(profile);
						}
					}
					candidates.removeAll(toRemove);
					if (candidates.size() > 0) {
						return candidates.get(0);
					}
				} else {
					return candidates.get(0);
				}
			}
		}
		return null;
	}

	// returns the profile in @profileList that has the minimal span between
	// start
	// time and end
	// time in which @time falls
	public static Profile matchProfile(List<Profile> profileList, Time time) {
		if (profileList == null || profileList.size() == 0 || time == null) {
			return null;
		}
		List<Profile> candidates = new ArrayList<Profile>();
		Time start = new Time();
		Time end = new Time();
		for (Profile profile : profileList) {
			if (profile.locked)
				return profile;
			if (profile.ssids.size() == 0) {
				if (profile.days.contains(String.valueOf(time.weekDay))) {
					start.hour = profile.startHour;
					start.minute = profile.startMin;
					start.second = profile.startSec;
					end.hour = profile.endHour;
					end.minute = profile.endMin;
					end.second = profile.endSec;
					if (UtilTime.isAfter(time, start)
							&& UtilTime.isBefore(time, end)) {
						candidates.add(profile);
					}
				}
			}
		}
		if (candidates.size() > 0) {
			int min = 7;
			for (Profile profile : candidates) {
				if (profile.days.size() < min) {
					min = profile.days.size();
				}
			}
			List<Profile> toRemove = new ArrayList<Profile>();
			for (Profile profile : candidates) {
				if (profile.days.size() > min) {
					toRemove.add(profile);
				}
			}
			candidates.removeAll(toRemove);
			min = 3600 * 24;
			int position = -1;
			for (int i = 0; i < candidates.size(); i++) {
				if (candidates.get(i).getTimeSpan() < min) {
					min = candidates.get(i).getTimeSpan();
					position = i;
				}
			}
			if (position != -1) {
				return candidates.get(position);
			}
		}
		return null;
	}

	public static boolean removeProfile(Profile profile) {
		if (profiles.contains(profile)) {
			if (profile.locked) {
				hasLockedProfile = false;
			}
			profiles.remove(profile);
			sortProfiles();
			return true;
		} else {
			return false;
		}
	}

	public static String toProfilesString() {
		String toSave = "";
		int i;
		if (profiles.size() > 0) {
			for (i = 0; i < profiles.size() - 1; i++) {
				toSave += profiles.get(i).toString() + PROFILE_DELIMITER;
			}
			toSave += profiles.get(i).toString();
		}
		return toSave;
	}

	public static void initProfilesFromString(String s) {
		if (s == null || s.length() < 1) {
			return;
		}
		String[] ps = s.split(PROFILE_DELIMITER);
		for (String p : ps) {
			addProfile(new Profile(p));
		}
	}

	public static void setProfile(Profile profile, Context context) {
		if (profile != null) {
			if (profile.ringerMode != -1) {
				UtilSystem.getAudioManager(context).setRingerMode(
						profile.ringerMode);
			}
			if (profile.ringerVolume != -1) {
				UtilSystem.getAudioManager(context).setStreamVolume(
						AudioManager.STREAM_RING, profile.ringerVolume, 0);
			}
			if (profile.notificationVolume != -1) {
				UtilSystem.getAudioManager(context).setStreamVolume(
						AudioManager.STREAM_NOTIFICATION,
						profile.notificationVolume, 0);
			}
			if (profile.systemVolume != -1) {
				UtilSystem.getAudioManager(context).setStreamVolume(
						AudioManager.STREAM_SYSTEM, profile.systemVolume, 0);
			}
			if (profile.alarmVolume != -1) {
				UtilSystem.getAudioManager(context).setStreamVolume(
						AudioManager.STREAM_ALARM, profile.alarmVolume, 0);
			}
			if (profile.mediaVolume != -1) {
				UtilSystem.getAudioManager(context).setStreamVolume(
						AudioManager.STREAM_MUSIC, profile.mediaVolume, 0);
			}
			if (profile.voiceCallVolume != -1) {
				UtilSystem.getAudioManager(context).setStreamVolume(
						AudioManager.STREAM_VOICE_CALL,
						profile.voiceCallVolume, 0);
			}
		}
	}

	public static String[] toNotification(Context context) {
		String[] n = new String[2];
		n[0] = context.getString(R.string.app_name);
		n[1] = activeProfileName == "" ? context
				.getString(R.string.no_matching_profile_found) : context
				.getString(R.string.active_profile)
				+ ": "
				+ (Profile.getProfileByName(activeProfileName).locked ? "("
						+ context.getString(R.string.locked) + ") "
						+ activeProfileName : activeProfileName);
		return n;
	}

	public static String updateProfile(Profile profile, String oldName) {
		String result = validateProfile(profile, oldName);
		if (result == "TrUe+_+TrUe") {
			for (int i = 0; i < profiles.size(); i++) {
				if (profiles.get(i).name.compareTo(oldName) == 0) {
					profiles.set(i, profile);
					break;
				}
			}
			sortProfiles();
		}
		return result;
	}

	public static String validateProfile(Profile profile) {
		boolean ssidsMatch;
		boolean daysMatch;
		boolean timeMatch;
		for (Profile p : profiles) {
			if (p.equals(profile)) {
				return "PrOFiLeAlReAdYeXiSt+_+PrOFiLeAlReAdYeXiSt";
			} else {
				if (p.name.compareToIgnoreCase(profile.name) == 0) {
					return "DuPlIcAtEnAmE+_+DuPlIcAtEnAmE";
				}
				ssidsMatch = p.ssids.size() == profile.ssids.size()
						&& (p.ssids.size() == 0 || p.ssids
								.containsAll(profile.ssids));
				daysMatch = p.days.size() == profile.days.size()
						&& (p.days.size() == 0 || p.days
								.containsAll(profile.days));
				timeMatch = p.startHour == profile.startHour
						&& p.startMin == profile.startMin
						&& p.startSec == profile.startSec
						&& p.endHour == profile.endHour
						&& p.endMin == profile.endMin
						&& p.endSec == profile.endSec;
				if (ssidsMatch && daysMatch && timeMatch) {
					return p.name;
				}
			}
		}
		return "TrUe+_+TrUe";
	}

	public static String validateProfile(Profile profile, String oldName) {
		boolean ssidsMatch;
		boolean daysMatch;
		boolean timeMatch;
		for (Profile p : profiles) {
			if (p.name.compareTo(oldName) != 0) {
				if (p.equals(profile)) {
					return "PrOFiLeAlReAdYeXiSt+_+PrOFiLeAlReAdYeXiSt";
				} else {
					if (p.name.compareToIgnoreCase(profile.name) == 0) {
						return "DuPlIcAtEnAmE+_+DuPlIcAtEnAmE";
					}
					ssidsMatch = p.ssids.size() == profile.ssids.size()
							&& (p.ssids.size() == 0 || p.ssids
									.containsAll(profile.ssids));
					daysMatch = p.days.size() == profile.days.size()
							&& (p.days.size() == 0 || p.days
									.containsAll(profile.days));
					timeMatch = p.startHour == profile.startHour
							&& p.startMin == profile.startMin
							&& p.startSec == profile.startSec
							&& p.endHour == profile.endHour
							&& p.endMin == profile.endMin
							&& p.endSec == profile.endSec;
					if (ssidsMatch && daysMatch && timeMatch) {
						return p.name;
					}
				}
			}
		}
		return "TrUe+_+TrUe";
	}

	public String[] toListItem(Context context) {
		String[] list = new String[4];
		String l;
		list[0] = locked ? "(" + context.getString(R.string.locked) + ") "
				+ name : name;
		l = context.getString(R.string.wi_fi) + ": ";
		if (ssids.size() > 0) {
			for (String s : ssids) {
				l += s + " ";
			}
		} else {
			l += context.getString(R.string.not_specified);
		}
		list[1] = l;
		l = Setting.is24hour ? UtilTime.toShortTime(startHour, startMin,
				startSec) : UtilTime.convertTo12Hour(startHour, startMin,
				startSec);
		l += " - "
				+ (Setting.is24hour ? UtilTime.toShortTime(endHour, endMin,
						endSec) : UtilTime.convertTo12Hour(endHour, endMin,
						endSec));
		l += " |";
		for (String s : days) {
			if (s.compareTo("0") == 0) {
				l += " " + context.getString(R.string.sunday_short);
			} else if (s.compareTo("1") == 0) {
				l += " " + context.getString(R.string.monday_short);
			} else if (s.compareTo("2") == 0) {
				l += " " + context.getString(R.string.tuesday_short);
			} else if (s.compareTo("3") == 0) {
				l += " " + context.getString(R.string.wednesday_short);
			} else if (s.compareTo("4") == 0) {
				l += " " + context.getString(R.string.thursday_short);
			} else if (s.compareTo("5") == 0) {
				l += " " + context.getString(R.string.friday_short);
			} else if (s.compareTo("6") == 0) {
				l += " " + context.getString(R.string.saturday_short);
			}
		}
		list[2] = l;
		l = context.getString(R.string.unknown);
		if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
			l = context.getString(R.string.normal);
		} else if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
			l = context.getString(R.string.vibrate);
		} else if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
			l = context.getString(R.string.silent);
		}
		if (ringerVolume != -1 || notificationVolume != -1
				|| systemVolume != -1 || alarmVolume != -1 || mediaVolume != -1
				|| voiceCallVolume != -1) {
			l += " |";
			if (ringerVolume != -1) {
				l += " ";
				l += context.getString(R.string.ringer_volume_short) + ":";
				l += String.valueOf(ringerVolume)
						+ "/"
						+ String.valueOf(UtilSystem.getAudioManager(context)
								.getStreamMaxVolume(AudioManager.STREAM_RING));
			}
			if (notificationVolume != -1) {
				l += " ";
				l += context.getString(R.string.notification_volume_short)
						+ ":";
				l += String.valueOf(notificationVolume)
						+ "/"
						+ String.valueOf(UtilSystem.getAudioManager(context)
								.getStreamMaxVolume(
										AudioManager.STREAM_NOTIFICATION));
			}
			if (systemVolume != -1) {
				l += " ";
				l += context.getString(R.string.system_volume_short) + ":";
				l += String.valueOf(systemVolume)
						+ "/"
						+ String.valueOf(UtilSystem.getAudioManager(context)
								.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
			}
			if (alarmVolume != -1) {
				l += " ";
				l += context.getString(R.string.alarm_volume_short) + ":";
				l += String.valueOf(alarmVolume)
						+ "/"
						+ String.valueOf(UtilSystem.getAudioManager(context)
								.getStreamMaxVolume(AudioManager.STREAM_ALARM));
			}
			if (mediaVolume != -1) {
				l += " ";
				l += context.getString(R.string.media_volume_short) + ":";
				l += String.valueOf(mediaVolume)
						+ "/"
						+ String.valueOf(UtilSystem.getAudioManager(context)
								.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			}
			if (voiceCallVolume != -1) {
				l += " ";
				l += context.getString(R.string.voice_call_volume_short) + ":";
				l += String.valueOf(voiceCallVolume)
						+ "/"
						+ String.valueOf(UtilSystem.getAudioManager(context)
								.getStreamMaxVolume(
										AudioManager.STREAM_VOICE_CALL));
			}
		}
		list[3] = l;
		return list;
	}

	public String toSummary(Context context) {
		String summary = "";
		summary += context.getString(R.string.cap_profile_name) + "\n" + name
				+ "\n";
		summary += "\n" + context.getString(R.string.cap_rules) + "\n";
		String ssidSum = "";
		if (ssids.size() > 0) {
			ssidSum += "\n";
			for (String ssid : ssids) {
				ssidSum += ssid + "\n";
			}
		} else {
			ssidSum += context.getString(R.string.not_specified);
		}
		summary += context.getString(R.string.wifi_networks) + ":" + ssidSum;
		String daysOfWeek = "";
		int d;
		for (String day : days) {
			d = Integer.parseInt(day);
			switch (d) {
			case 1:
				daysOfWeek += context.getString(R.string.monday) + "\n";
				break;
			case 2:
				daysOfWeek += context.getString(R.string.tuesday) + "\n";
				break;
			case 3:
				daysOfWeek += context.getString(R.string.wednesday) + "\n";
				break;
			case 4:
				daysOfWeek += context.getString(R.string.thursday) + "\n";
				break;
			case 5:
				daysOfWeek += context.getString(R.string.friday) + "\n";
				break;
			case 6:
				daysOfWeek += context.getString(R.string.saturday) + "\n";
				break;
			case 0:
				daysOfWeek += context.getString(R.string.sunday) + "\n";
				break;
			default:
				break;
			}
		}
		summary += context.getString(R.string.days_of_week) + ":\n"
				+ daysOfWeek;
		summary += context.getString(R.string.start_time_of_day)
				+ ":\n"
				+ (Setting.is24hour ? UtilTime.toShortTime(startHour, startMin,
						startSec) : UtilTime.convertTo12Hour(startHour,
						startMin, startSec)) + "\n";
		summary += context.getString(R.string.end_time_of_day)
				+ ":\n"
				+ (Setting.is24hour ? UtilTime.toShortTime(endHour, endMin,
						endSec) : UtilTime.convertTo12Hour(endHour, endMin,
						endSec)) + "\n";
		summary += "\n" + context.getString(R.string.cap_settings) + "\n";
		summary += context.getString(R.string.ringer_mode) + ":\n";
		switch (ringerMode) {
		case AudioManager.RINGER_MODE_NORMAL:
			summary += context.getString(R.string.normal) + "\n";
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			summary += context.getString(R.string.vibrate) + "\n";
			break;
		case AudioManager.RINGER_MODE_SILENT:
			summary += context.getString(R.string.silent) + "\n";
			break;
		default:
			break;
		}
		summary += context.getString(R.string.ringer_volume) + ":\n";
		if (ringerVolume == -1) {
			if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
				summary += context.getString(R.string.vibrate) + "\n";
			} else if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
				summary += context.getString(R.string.silent) + "\n";
			} else {
				summary += context.getString(R.string.unchanged) + "\n";
			}
		} else {
			summary += String.valueOf(ringerVolume)
					+ "/"
					+ String.valueOf(UtilSystem.getAudioManager(context)
							.getStreamMaxVolume(AudioManager.STREAM_RING))
					+ "\n";
		}
		summary += context.getString(R.string.notification_volume) + ":\n";
		if (notificationVolume == -1) {
			if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
				summary += context.getString(R.string.vibrate) + "\n";
			} else if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
				summary += context.getString(R.string.silent) + "\n";
			} else {
				summary += context.getString(R.string.unchanged) + "\n";
			}
		} else {
			summary += String.valueOf(notificationVolume)
					+ "/"
					+ String.valueOf(UtilSystem.getAudioManager(context)
							.getStreamMaxVolume(
									AudioManager.STREAM_NOTIFICATION)) + "\n";
		}
		summary += context.getString(R.string.system_volume) + ":\n";
		if (systemVolume == -1) {
			if (ringerMode == AudioManager.RINGER_MODE_VIBRATE
					|| ringerMode == AudioManager.RINGER_MODE_SILENT) {
				summary += context.getString(R.string.silent) + "\n";
			} else {
				summary += context.getString(R.string.unchanged) + "\n";
			}
		} else {
			summary += String.valueOf(systemVolume)
					+ "/"
					+ String.valueOf(UtilSystem.getAudioManager(context)
							.getStreamMaxVolume(AudioManager.STREAM_SYSTEM))
					+ "\n";
		}
		summary += context.getString(R.string.alarm_volume) + ":\n";
		if (alarmVolume == -1) {
			summary += context.getString(R.string.unchanged) + "\n";
		} else {
			summary += String.valueOf(alarmVolume)
					+ "/"
					+ String.valueOf(UtilSystem.getAudioManager(context)
							.getStreamMaxVolume(AudioManager.STREAM_ALARM))
					+ "\n";
		}
		summary += context.getString(R.string.media_volume) + ":\n";
		if (mediaVolume == -1) {
			summary += context.getString(R.string.unchanged) + "\n";
		} else {
			summary += String.valueOf(mediaVolume)
					+ "/"
					+ String.valueOf(UtilSystem.getAudioManager(context)
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
					+ "\n";
		}
		summary += context.getString(R.string.voice_call_volume) + ":\n";
		if (voiceCallVolume == -1) {
			summary += context.getString(R.string.unchanged) + "\n";
		} else {
			summary += String.valueOf(voiceCallVolume)
					+ "/"
					+ String.valueOf(UtilSystem.getAudioManager(context)
							.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL))
					+ "\n";
		}
		return summary;
	}
}