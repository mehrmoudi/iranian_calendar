/* TODO: write copyright note
 */

package com.android.calendar.jalali;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.format.Time;

import com.android.calendar.GeneralPreferences;
import com.congenialmobile.calendar.R;

/**
 * Utility class for converting between Jalali and Gregorian calendars.
 */

public class Jalali {

	private static int[] gDaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static int[] jDaysInMonth = { 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29 };

	public final static char PERSIAN_ZERO = 0x06f0;
	public final static char PERSIAN_NINE = 0x06f9;
	public final static char PERSIAN_DECIMAL_POINT = 0x066b;
	public final static char DIGIT_DIFF = PERSIAN_ZERO - '0';

	private static Context mContext;
	@SuppressWarnings("unused")
	private static SharedPreferences mSettings;

	private static Jalali instance;

	private Jalali(Context context) {
		mContext = context;
		mSettings = GeneralPreferences.getSharedPreferences(context);
	}

	public static Jalali get(Context context) {
		if (instance == null)
			instance = new Jalali(context);
		return instance;
	}

	public String format(String format, Time time) {

		JalaliDate jDate = Jalali.gregorianToJalali(time.year, time.month + 1, time.monthDay);

		final char QUOTE = '%';

		int len = format.length();
		int count = 0;

		for (int i = 0; i < len; i += count) {
			count = 1;
			char ch = format.charAt(i);

			if (ch != QUOTE) {
				continue;
			}

			int hookStart = i;
			int hookEnd = hookStart + 1;
			while (!"aAbBcCdDeFgGhHIjklmMnpPrRsStTuUVwWxXyYzZ%".contains(format.subSequence(hookEnd, hookEnd + 1))) {
				hookEnd++;
			}

			String replacement = Jalali.replaceHook(format.substring(hookStart + 1, hookEnd + 1), jDate, time);
			format = format.replace(format.subSequence(hookStart, hookEnd + 1), replacement);
			len = format.length();
			count = replacement.length();
		}
		return format;
	}

	private static String replaceHook(String hook, JalaliDate jDate, Time time) {
		boolean padNumbers = true;
		int padLength = 0;
		String padChar = " ";

		char ch;
		if (hook.contains("_")) {
			padChar = " ";
		} else if (hook.contains("0")) {
			padChar = "0"; // TODO
		}

		if (hook.contains("-")) {
			padNumbers = false;
		}

		for (int i = hook.length() - 1; i > 0; i--) {
			ch = hook.charAt(i);
			if ((ch >= '1') && (ch <= '9')) {
				padLength = ch - '0';
				break;
			}
		}

		ch = hook.charAt(hook.length() - 1);
		String result;
		switch (ch) {
		case 'b':
		case 'h':
			result = getMonthName(mContext, jDate.month);
			break;
		case 'C':
			if (padLength == 0)
				padLength = 2;
			result = Jalali.padNumber(jDate.year / 100, padLength, padNumbers, padChar);
			break;
		case 'd':
			if (padLength == 0)
				padLength = 2;
			result = Jalali.padNumber(jDate.day, padLength, padNumbers, padChar);
			break;
		case 'e':
			if (padLength == 0)
				padLength = 2;
			result = Jalali.padNumber(jDate.day, padLength, padNumbers, "0");
			break;
		case 'm':
			if (padLength == 0)
				padLength = 2;
			result = Jalali.padNumber(jDate.month, padLength, padNumbers, "0");
			break;
		case 'y':
			if (padLength == 0)
				padLength = 2;
			result = Jalali.padNumber(jDate.year % 100, padLength, padNumbers, padChar);
			break;
		case 'Y':
			if (padLength == 0)
				padLength = 4;
			result = Jalali.padNumber(jDate.year, padLength, padNumbers, padChar);
			break;
		case '%':
			result = "%";
			break;
		case 'B':
			result = getMonthName(mContext, jDate.month); // should be short
			// month name
			break;
		case 'p':
			result = getAmPm(mContext, time.hour);
			break;
		case 'a':// short weekday
		case 'A':
			int weekDay = Jalali.jalaliToGregorian(jDate).get(Calendar.DAY_OF_WEEK);
			result = getWeekday(mContext, weekDay);
			break;
		case 'M':
			result = String.format("%02d", time.minute);
			break;
		case 'k':
			result = String.format("%02d", time.hour);
			break;
		case 'l':
			int hour = time.hour;
			if (hour == 0)
				hour = 12;
			if (hour > 12)
				hour -= 12;
			result = String.format("%02d", hour);
			break;
		default:
			result = "%" + hook;
		}

		return result;
	}

	private static String getAmPm(Context context, int hour) {
		Resources res = context.getResources();
		if (hour >= 12) {
			return res.getString(R.string.pm);
		} else {
			return res.getString(R.string.am);
		}
	}

	private static String getWeekday(Context context, int weekDay) {
		return getWeekday(context.getResources(), weekDay);
	}

	public static String getWeekday(Resources res, int weekDay) {
		switch (weekDay) {
		case 1:
			return res.getString(R.string.day_of_week_long_sunday);
		case 2:
			return res.getString(R.string.day_of_week_long_monday);
		case 3:
			return res.getString(R.string.day_of_week_long_tuesday);
		case 4:
			return res.getString(R.string.day_of_week_long_wednesday);
		case 5:
			return res.getString(R.string.day_of_week_long_thursday);
		case 6:
			return res.getString(R.string.day_of_week_long_friday);
		case 7:
			return res.getString(R.string.day_of_week_long_saturday);
		}
		return null;
	}

	private static String padNumber(int number, int length, boolean pad, String ch) {
		String str = "" + number;
		int resultLength = str.length();
		int i;

		if (pad && (resultLength < length)) {
			String padding = "";
			for (i = 0; i < (resultLength - length); i++)
				padding += ch;
			str = padding + str;
		}

		return str;
	}

	public static String persianDigits(String str) {

		// String correctionStr = mSettings.getString(
		// GeneralPreferences.KEY_FARSI_CORRECTION,
		// GeneralPreferences.DEFAULT_FARSI_CORRECTION);

		boolean numberFix = false;// !correctionStr.equals(GeneralPreferences.FARSI_CORRECTION_TYPE_13);

		String result = "";

		char ch;

		int i;

		for (i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if ((ch >= '0') && (ch <= '9'))
				result += Character.toString((char) (PERSIAN_ZERO + (ch - '0')));
			else
				result += ch;
		}

		if (numberFix) {

			String changedResult = "";

			String temp = "";

			boolean onNumber = false;

			for (i = 0; i < result.length(); i++) {
				ch = result.charAt(i);
				if ((ch >= PERSIAN_ZERO) && (ch <= PERSIAN_NINE)) {
					if (onNumber) {
						temp = ch + temp;
					} else {
						temp = String.valueOf(ch);
						onNumber = true;
					}
				} else {
					if (onNumber) {
						changedResult += temp;
						onNumber = false;
					}
					changedResult += ch;
				}
			}

			if (onNumber) {
				changedResult += temp;
			}

			return changedResult;
		}

		return result;
	}

	public static JalaliDate gregorianToJalali(Time gTime) {
		return gregorianToJalali(gTime.year, gTime.month + 1, gTime.monthDay);
	}

	public static JalaliDate gregorianToJalali(Date gDate) {
		return gregorianToJalali(gDate.getYear() + 1900, gDate.getMonth() + 1, gDate.getDate());
	}

	public static JalaliDate gregorianToJalali(int gYear, int gMonth, int gDay) {
		int gy, gm, gd;
		int jy, jm, jd;
		long g_day_no, j_day_no;
		int j_np;
		int i;

		gy = gYear - 1600;
		gm = gMonth - 1;
		gd = gDay - 1;

		g_day_no = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400;
		for (i = 0; i < gm; ++i)
			g_day_no += gDaysInMonth[i];
		if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)))
			/* leap and after Feb */
			++g_day_no;
		g_day_no += gd;

		j_day_no = g_day_no - 79;

		j_np = new Long(j_day_no / 12053).intValue();
		j_day_no %= 12053;

		jy = new Long(979 + 33 * j_np + 4 * (j_day_no / 1461)).intValue();
		j_day_no %= 1461;

		if (j_day_no >= 366) {
			jy += (j_day_no - 1) / 365;
			j_day_no = (j_day_no - 1) % 365;
		}

		for (i = 0; i < 11 && j_day_no >= jDaysInMonth[i]; ++i) {
			j_day_no -= jDaysInMonth[i];
		}
		jm = i + 1;
		jd = new Long(j_day_no + 1).intValue();
		return new JalaliDate(jy, jm, jd);
	}

	public static Time jalaliToGregorianTime(JalaliDate jDate) {
		Calendar resultCalendar = jalaliToGregorian(jDate.year, jDate.month, jDate.day);
		Time result = new Time();
		result.set(resultCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.MONTH),
				resultCalendar.get(Calendar.YEAR));
		return result;
	}

	public static Time jalaliToGregorianTime(int jYear, int jMonth, int jDay) {
		Calendar resultCalendar = jalaliToGregorian(jYear, jMonth, jDay);
		Time result = new Time();
		result.set(resultCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.MONTH),
				resultCalendar.get(Calendar.YEAR));
		return result;
	}

	public static Calendar jalaliToGregorian(JalaliDate jDate) {
		return jalaliToGregorian(jDate.year, jDate.month, jDate.day);
	}

	public static Calendar jalaliToGregorian(int jYear, int jMonth, int jDay) {
		int gy, gm, gd;
		int jy, jm, jd;
		long g_day_no, j_day_no;
		boolean leap;

		int i;

		jy = jYear - 979;
		jm = jMonth - 1;
		jd = jDay - 1;

		j_day_no = 365 * jy + (jy / 33) * 8 + (jy % 33 + 3) / 4;
		for (i = 0; i < jm; ++i)
			j_day_no += jDaysInMonth[i];

		j_day_no += jd;

		g_day_no = j_day_no + 79;

		gy = new Long(1600 + 400 * (g_day_no / 146097)).intValue(); /*
																	 * 146097 =
																	 * 365*400 +
																	 * 400/4 -
																	 * 400/100 +
																	 * 400/400
																	 */
		g_day_no = g_day_no % 146097;

		leap = true;
		if (g_day_no >= 36525) /* 36525 = 365*100 + 100/4 */
		{
			g_day_no--;
			gy += 100 * (g_day_no / 36524); /* 36524 = 365*100 + 100/4 - 100/100 */
			g_day_no = g_day_no % 36524;

			if (g_day_no >= 365)
				g_day_no++;
			else
				leap = false;
		}

		gy += 4 * (g_day_no / 1461); /* 1461 = 365*4 + 4/4 */
		g_day_no %= 1461;

		if (g_day_no >= 366) {
			leap = false;

			g_day_no--;
			gy += g_day_no / 365;
			g_day_no = g_day_no % 365;
		}

		for (i = 0; g_day_no >= gDaysInMonth[i] + ((i == 1 && leap) ? 1 : 0); i++)
			g_day_no -= gDaysInMonth[i] + ((i == 1 && leap) ? 1 : 0);
		gm = i + 1;
		gd = new Long(g_day_no + 1).intValue();

		Calendar calendar = Calendar.getInstance();
		calendar.set(gy, gm - 1, gd);
		return calendar;
	}

	public static boolean isJalaliLeapYear(int year) {
		int mod = (year + 11) % 33;
		if ((mod != 32) && ((mod % 4) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getMaxMonthDay(int year, int month) {
		if (month < 7) {
			return 31; // months 1..6
		}
		if (month < 12) {
			return 30; // months 7..11
		}
		if (isJalaliLeapYear(year)) {
			return 30; // month 12, but leap year
		}
		return 29; // month 12 and not a leap year
	}

	public static String getMonthName(Context context, int month) {
		Resources res = context.getResources();
		return getMonthName(res, month);
	}

	public static String getMonthName(Resources res, int month) {
		switch (month) {
		case 1:
			return res.getString(R.string.month_farvardin);
		case 2:
			return res.getString(R.string.month_ordibehesht);
		case 3:
			return res.getString(R.string.month_khordad);
		case 4:
			return res.getString(R.string.month_tir);
		case 5:
			return res.getString(R.string.month_mordad);
		case 6:
			return res.getString(R.string.month_shahrivar);
		case 7:
			return res.getString(R.string.month_mehr);
		case 8:
			return res.getString(R.string.month_aban);
		case 9:
			return res.getString(R.string.month_azar);
		case 10:
			return res.getString(R.string.month_dey);
		case 11:
			return res.getString(R.string.month_bahman);
		case 12:
			return res.getString(R.string.month_esfand);
		}
		return null;
	}
}
