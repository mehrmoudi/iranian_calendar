package com.android.calendar.hijri;

import com.android.calendar.CalendarApplication;
import com.android.calendar.jalali.JalaliDate;
import com.congenialmobile.calendar.R;

public class HijriCalendar {

	private String[] mMonthNames;
	// private int[] mMonthsLength1436 = { 29, 30, 29, 29, 30, 29, 30, 29, 30,
	// 29, 30, 30 };
	// private int[] mMonthsLength1437 = { 29, 30, 29, 29, 30, 29, 30, 29, 30,
	// 29, 30, 30 };

	public int year, month, day;

	public HijriCalendar(JalaliDate jDate) {
		mMonthNames = CalendarApplication.getContext().getResources().getStringArray(R.array.hijri_months);
		if (jDate.year == 1394) {
			year = 1436;
			switch (jDate.month) {
			case 1:
				if (jDate.day == 1) {
					day = 30;
					month = 4;
				} else if (jDate.day == 31) {
					day = 1;
					month = 6;
				} else {
					month = 5;
					day = jDate.day - 1;
				}
				break;
			case 2:
				if (jDate.day >= 30) {
					day = jDate.day - 29;
					month = 7;
				} else {
					month = 6;
					day = jDate.day + 1;
				}
				break;
			case 3:
				if (jDate.day >= 28) {
					day = jDate.day - 27;
					month = 8;
				} else {
					month = 7;
					day = jDate.day + 2;
				}
				break;
			case 4:
				if (jDate.day >= 27) {
					day = jDate.day - 26;
					month = 9;
				} else {
					month = 8;
					day = jDate.day + 4;
				}
				break;
			case 5:
				if (jDate.day >= 25) {
					day = jDate.day - 24;
					month = 10;
				} else {
					month = 9;
					day = jDate.day + 5;
				}
				break;
			case 6:
				if (jDate.day >= 24) {
					day = jDate.day - 23;
					month = 11;
				} else {
					month = 10;
					day = jDate.day + 7;
				}
				break;
			case 7:
				if (jDate.day >= 23) {
					day = jDate.day - 22;
					month = 0;
					year++;
				} else {
					month = 11;
					day = jDate.day + 8;
				}
				break;
			case 8:
				if (jDate.day >= 22) {
					day = jDate.day - 21;
					month = 1;
					year++;
				} else {
					month = 0;
					day = jDate.day + 8;
					year++;
				}
				break;
			case 9:
				if (jDate.day >= 22) {
					day = jDate.day - 21;
					month = 2;
					year++;
				} else {
					month = 1;
					day = jDate.day + 9;
					year++;
				}
				break;
			case 10:
				if (jDate.day >= 22) {
					day = jDate.day - 21;
					month = 3;
					year++;
				} else {
					month = 2;
					day = jDate.day + 9;
					year++;
				}
				break;
			case 11:
				if (jDate.day >= 21) {
					day = jDate.day - 20;
					month = 4;
					year++;
				} else {
					month = 3;
					day = jDate.day + 9;
					year++;
				}
				break;
			case 12:
				if (jDate.day >= 21) {
					day = jDate.day - 20;
					month = 5;
					year++;
				} else {
					month = 4;
					day = jDate.day + 10;
					year++;
				}
				break;
			}
			month++;
		} else {
			EstimateHijriCalendar esHijri = new EstimateHijriCalendar(jDate);
			this.year = esHijri.year;
			this.month = esHijri.month;
			this.day = esHijri.day;
		}
	}

	public String getMonthName() {
		return mMonthNames[month - 1];
	}
}
