package com.intofan.android.smartprofiles;

import android.text.format.Time;

public class UtilTime extends Time {

	public static boolean isAfter(Time a, Time b) {
		if (a == null || b == null) {
			return false;
		}
		if (a.hour > b.hour) {
			return true;
		}
		if (a.hour == b.hour) {
			if (a.minute > b.minute) {
				return true;
			}
			if (a.minute == b.minute) {
				if (a.second > b.second) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isBefore(Time a, Time b) {
		if (a == null || b == null) {
			return false;
		}
		if (a.hour < b.hour) {
			return true;
		}
		if (a.hour == b.hour) {
			if (a.minute < b.minute) {
				return true;
			}
			if (a.minute == b.minute) {
				if (a.second < b.second) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isSame(Time a, Time b) {
		if (a == null || b == null) {
			return false;
		}
		return a.hour == b.hour && a.minute == b.minute && a.second == b.second;
	}

	public static String convertTo12Hour(int h, int m, int s) {
		if (h >= 0 && h <= 23) {
			if (h <= 11) {
				return add0(h) + ":" + add0(m) + ":" + add0(s) + " AM";
			} else {
				if (h > 12) {
					h = h - 12;
				}
				return add0(h) + ":" + add0(m) + ":" + add0(s) + " PM";
			}
		} else {
			return "";
		}
	}

	public static String toShortTime(int h, int m, int s) {
		return add0(h) + ":" + add0(m) + ":" + add0(s);
	}

	public static String add0(int num) {
		return num < 10 ? "0" + String.valueOf(num) : String.valueOf(num);
	}
}
