package com.android.calendar.jalali;

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.text.format.Time;

import com.android.calendar.CalendarApplication;
import com.congenialmobile.calendar.R;

public class DateUtils {
	public static final long SECOND_IN_MILLIS = 1000;
	public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
	public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
	public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
	public static final long WEEK_IN_MILLIS = DAY_IN_MILLIS * 7;
	/**
	 * This constant is actually the length of 364 days, not of a year!
	 */
	public static final long YEAR_IN_MILLIS = WEEK_IN_MILLIS * 52;

	// The following FORMAT_* symbols are used for specifying the format of
	// dates and times in the formatDateRange method.
	public static final int FORMAT_SHOW_TIME = 0x00001;
	public static final int FORMAT_SHOW_WEEKDAY = 0x00002;
	public static final int FORMAT_SHOW_YEAR = 0x00004;
	public static final int FORMAT_NO_YEAR = 0x00008;
	public static final int FORMAT_SHOW_DATE = 0x00010;
	public static final int FORMAT_NO_MONTH_DAY = 0x00020;
	@Deprecated
	public static final int FORMAT_12HOUR = 0x00040;
	@Deprecated
	public static final int FORMAT_24HOUR = 0x00080;
	@Deprecated
	public static final int FORMAT_CAP_AMPM = 0x00100;
	public static final int FORMAT_NO_NOON = 0x00200;
	@Deprecated
	public static final int FORMAT_CAP_NOON = 0x00400;
	public static final int FORMAT_NO_MIDNIGHT = 0x00800;
	@Deprecated
	public static final int FORMAT_CAP_MIDNIGHT = 0x01000;
	/**
	 * @deprecated Use
	 *             {@link #formatDateRange(Context, Formatter, long, long, int, String)
	 *             formatDateRange} and pass in {@link Time#TIMEZONE_UTC
	 *             Time.TIMEZONE_UTC} for the timeZone instead.
	 */
	@Deprecated
	public static final int FORMAT_UTC = 0x02000;
	public static final int FORMAT_ABBREV_TIME = 0x04000;
	public static final int FORMAT_ABBREV_WEEKDAY = 0x08000;
	public static final int FORMAT_ABBREV_MONTH = 0x10000;
	public static final int FORMAT_NUMERIC_DATE = 0x20000;
	public static final int FORMAT_ABBREV_RELATIVE = 0x40000;
	public static final int FORMAT_ABBREV_ALL = 0x80000;
	@Deprecated
	public static final int FORMAT_CAP_NOON_MIDNIGHT = (FORMAT_CAP_NOON | FORMAT_CAP_MIDNIGHT);
	@Deprecated
	public static final int FORMAT_NO_NOON_MIDNIGHT = (FORMAT_NO_NOON | FORMAT_NO_MIDNIGHT);
	/**
	 * Request the full spelled-out name. For use with the 'abbrev' parameter of
	 * {@link #getDayOfWeekString} and {@link #getMonthString}.
	 *
	 * @more <p>
	 *       e.g. "Sunday" or "January"
	 * @deprecated Use {@link java.text.SimpleDateFormat} instead.
	 */
	@Deprecated
	public static final int LENGTH_LONG = 10;

	/**
	 * Request an abbreviated version of the name. For use with the 'abbrev'
	 * parameter of {@link #getDayOfWeekString} and {@link #getMonthString}.
	 *
	 * @more <p>
	 *       e.g. "Sun" or "Jan"
	 * @deprecated Use {@link java.text.SimpleDateFormat} instead.
	 */
	@Deprecated
	public static final int LENGTH_MEDIUM = 20;

	/**
	 * Request a shorter abbreviated version of the name. For use with the
	 * 'abbrev' parameter of {@link #getDayOfWeekString} and
	 * {@link #getMonthString}.
	 * 
	 * @more <p>
	 *       e.g. "Su" or "Jan"
	 *       <p>
	 *       In most languages, the results returned for LENGTH_SHORT will be
	 *       the same as the results returned for {@link #LENGTH_MEDIUM}.
	 * @deprecated Use {@link java.text.SimpleDateFormat} instead.
	 */
	@Deprecated
	public static final int LENGTH_SHORT = 30;

	/**
	 * Request an even shorter abbreviated version of the name. Do not use this.
	 * Currently this will always return the same result as
	 * {@link #LENGTH_SHORT}.
	 * 
	 * @deprecated Use {@link java.text.SimpleDateFormat} instead.
	 */
	@Deprecated
	public static final int LENGTH_SHORTER = 40;

	/**
	 * Request an even shorter abbreviated version of the name. For use with the
	 * 'abbrev' parameter of {@link #getDayOfWeekString} and
	 * {@link #getMonthString}.
	 * 
	 * @more <p>
	 *       e.g. "S", "T", "T" or "J"
	 *       <p>
	 *       In some languages, the results returned for LENGTH_SHORTEST will be
	 *       the same as the results returned for {@link #LENGTH_SHORT}.
	 * @deprecated Use {@link java.text.SimpleDateFormat} instead.
	 */
	@Deprecated
	public static final int LENGTH_SHORTEST = 50;

	private static final int[] sDaysLong = new int[] { R.string.day_of_week_long_sunday,
			R.string.day_of_week_long_monday, R.string.day_of_week_long_tuesday, R.string.day_of_week_long_wednesday,
			R.string.day_of_week_long_thursday, R.string.day_of_week_long_friday, R.string.day_of_week_long_saturday, };
	private static final int[] sDaysShort = new int[] { R.string.day_of_week_short_sunday,
			R.string.day_of_week_short_monday, R.string.day_of_week_short_tuesday,
			R.string.day_of_week_short_wednesday, R.string.day_of_week_short_thursday,
			R.string.day_of_week_short_friday, R.string.day_of_week_short_saturday, };

	public static String getDayOfWeekString(int dayOfWeek, int abbrev) {
		int[] names;
		switch (abbrev) {
		case LENGTH_LONG:
			names = sDaysLong;
			break;
		case LENGTH_MEDIUM:
			names = sDaysShort;
			break;
		case LENGTH_SHORT:
			names = sDaysShort;
			break; // TODO
		case LENGTH_SHORTER:
			names = sDaysShort;
			break; // TODO
		case LENGTH_SHORTEST:
			names = sDaysShort;
			break;
		default:
			names = sDaysShort;
			break;
		}
		return CalendarApplication.getContext().getString(names[dayOfWeek - 1]);
	}

	public static String formatDateTime(Context context, long millis, int flags) {
		return formatDateRange(context, millis, millis, flags);
	}

	public static String formatDateRange(Context context, long startMillis, long endMillis, int flags) {
		Formatter f = new Formatter(new StringBuilder(50), Locale.getDefault());
		return formatDateRange(context, f, startMillis, endMillis, flags).toString();
	}

	public static Formatter formatDateRange(Context context, Formatter formatter, long startMillis, long endMillis,
			int flags) {
		return formatDateRange(context, formatter, startMillis, endMillis, flags, null);
	}

	public static Formatter formatDateRange(Context context, Formatter formatter, long startMillis, long endMillis,
			int flags, String timeZone) {
		return formatDateRange(context, formatter, startMillis, endMillis, flags, timeZone, true);
	}

	public static Formatter formatDateRange(Context context, Formatter formatter, long startMillis, long endMillis,
			int flags, String timeZone, boolean isJalali) {
		if (formatter == null)
			formatter = new Formatter(new StringBuilder(50), Locale.getDefault());
		if (!isJalali) {
			return android.text.format.DateUtils.formatDateRange(context, formatter, startMillis, endMillis, flags,
					timeZone);
		}
		Time startTime = new Time();
		startTime.set(startMillis);
		JalaliDate startDate = Jalali.gregorianToJalali(startTime);

		Time endTime = new Time();
		endTime.set(endMillis);
		JalaliDate endDate = Jalali.gregorianToJalali(endTime);

		Time currentTime = new Time();
		currentTime.setToNow();
		JalaliDate currentDate = Jalali.gregorianToJalali(currentTime);

		boolean showWeekDay = (flags & FORMAT_SHOW_WEEKDAY) != 0;
		boolean weekDayAbbrev = false/*
									 * ((flags & FORMAT_ABBREV_WEEKDAY) | (flags
									 * & FORMAT_ABBREV_ALL)) != 0
									 */;
		boolean numbericFormat = (flags & FORMAT_NUMERIC_DATE) != 0;
		boolean showDate = ((flags & FORMAT_SHOW_DATE) != 0) || numbericFormat;
		boolean noMonthDay = (flags & FORMAT_NO_MONTH_DAY) != 0;
		boolean showYear = (((flags & FORMAT_SHOW_YEAR) != 0) || (startDate.year != endDate.year) || (currentDate.year != startDate.year))
				&& ((flags & FORMAT_NO_YEAR) == 0) && ((flags & FORMAT_SHOW_TIME) == 0);
		boolean showTime = (flags & FORMAT_SHOW_TIME) != 0;
		boolean is12HourFormat = ((flags & FORMAT_12HOUR) != 0) && ((flags & FORMAT_24HOUR) == 0);

		if (startMillis == endMillis) {
			StringBuilder result = new StringBuilder();

			if (showWeekDay) {
				if (weekDayAbbrev) {
					result.append(context.getString(sDaysShort[startTime.weekDay]));
				} else {
					result.append(context.getString(sDaysLong[startTime.weekDay]));
				}
				result.append("، ");
			}
			if (showDate) {
				if (numbericFormat) {
					result.append(startDate.month);
					if (!noMonthDay) {
						result.append("/");
						result.append(startDate.day);
					}
				} else {
					if (!noMonthDay) {
						result.append(startDate.day);
						result.append(" ");
					}
					result.append(Jalali.getMonthName(context, startDate.month));
				}
				result.append(" ");
			}
			if (showYear) {
				result.append(startDate.year);
				result.append("، ");
			}

			if (showTime) {
				result.append(getTime(startTime, is12HourFormat, context));
			}

			return formatter.format("%s", Jalali.persianDigits(trimString(result.toString())));
		} else {
			StringBuilder fromString = new StringBuilder();
			StringBuilder toString = new StringBuilder();

			if (startDate.year != endDate.year || startDate.month != endDate.month
					|| (startDate.day != endDate.day && showWeekDay)) {
				if (showWeekDay) {
					if (weekDayAbbrev) {
						fromString.append(context.getString(sDaysShort[startTime.weekDay]));
						toString.append(context.getString(sDaysShort[endTime.weekDay]));
					} else {
						fromString.append(context.getString(sDaysLong[startTime.weekDay]));
						toString.append(context.getString(sDaysLong[endTime.weekDay]));
					}
					fromString.append("، ");
					toString.append("، ");
				}
				if (showDate) {
					if (!noMonthDay) {
						fromString.append(startDate.day);
						toString.append(endDate.day);
						if (numbericFormat) {
							fromString.append("/");
							toString.append("/");
						} else {
							fromString.append(" ");
							toString.append(" ");
						}
					}
					if (numbericFormat) {
						fromString.append(startDate.month);
						toString.append(endDate.month);
					} else {
						fromString.append(Jalali.getMonthName(context, startDate.month));
						toString.append(Jalali.getMonthName(context, endDate.month));
					}
					fromString.append(" ");
					toString.append(" ");
				}
				if (showYear) {
					fromString.append(startDate.year);
					toString.append(endDate.year);
					fromString.append("، ");
					toString.append("، ");
				}
				if (showTime) {
					fromString.append(getTime(startTime, is12HourFormat, context));
					toString.append(getTime(endTime, is12HourFormat, context));
				}
			} else if (startDate.day != endDate.day) {
				if (showDate) {
					if (!noMonthDay) {
						fromString.append(startDate.day);
						toString.append(endDate.day);
						if (numbericFormat) {
							fromString.append("/");
							toString.append("/");
						} else {
							fromString.append(" ");
							toString.append(" ");
						}
					}
					if (numbericFormat) {
						fromString.append(startDate.month);
						toString.append(endDate.month);
					} else {
						// fromString.append(Jalali.getMonthName(context,
						// startDate.month));
						toString.append(Jalali.getMonthName(context, endDate.month));
					}
					fromString.append(" ");
					toString.append(" ");
				}
				if (showYear) {
					// fromString.append(startDate.year);
					toString.append(endDate.year);
					// fromString.append("، ");
					toString.append("، ");
				}
				if (showTime) {
					fromString.append(getTime(startTime, is12HourFormat, context));
					toString.append(getTime(endTime, is12HourFormat, context));
				}
			} else {
				if (showWeekDay) {
					if (weekDayAbbrev) {
						fromString.append(context.getString(sDaysShort[startTime.weekDay]));
					} else {
						fromString.append(context.getString(sDaysLong[startTime.weekDay]));
					}
					fromString.append("، ");
				}
				if (showDate) {
					if (!noMonthDay) {
						fromString.append(startDate.day);
						if (numbericFormat) {
							fromString.append("/");
						} else {
							fromString.append(" ");
						}
					}
					if (numbericFormat) {
						fromString.append(startDate.month);
					} else {
						fromString.append(Jalali.getMonthName(context, startDate.month));
					}
					fromString.append(" ");
				}
				if (showYear) {
					fromString.append(startDate.year);
					fromString.append("، ");
				}
				if (showTime) {
					fromString.append(getTime(startTime, is12HourFormat, context));
					toString.append(getTime(endTime, is12HourFormat, context));
				}
			}
			if (trimString(toString.toString()).equals("")) {
				return formatter.format("%s", Jalali.persianDigits(trimString(fromString.toString())));
			} else if (trimString(fromString.toString()).equals("")) {
				return formatter.format("%s", Jalali.persianDigits(trimString(toString.toString())));
			} else {
				return formatter.format(
						"%s",
						Jalali.persianDigits(trimString(fromString.toString()) + " - "
								+ trimString(toString.toString())));
			}
		}

	}

	private static String trimString(String string) {
		string = string.trim();
		while (string.startsWith("،")) {
			string = string.substring(1).trim();
		}
		while (string.endsWith("،")) {
			string = string.substring(0, string.length() - 1).trim();
		}
		return string;
	}

	private static String getTime(Time time, boolean is12HourFormat, Context context) {
		StringBuilder result = new StringBuilder();
		if (is12HourFormat) {
			result.append(get12FormatHour(time.hour));
			result.append(":");
			result.append(String.format("%02d", time.minute));
			result.append(" ");
			int ampmRes = time.hour >= 12 ? R.string.pm : R.string.am;
			result.append(context.getString(ampmRes));
		} else {
			result.append(String.format("%02d", time.hour));
			result.append(":");
			result.append(String.format("%02d", time.minute));
		}
		return result.toString();
	}

	private static int get12FormatHour(int hour) {
		if (hour == 0)
			return 12;
		if (hour > 12)
			return hour - 12;
		return hour;
	}

	/**
	 * @return true if the supplied when is today else false
	 */
	public static boolean isToday(long when) {
		Time time = new Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year) && (thenMonth == time.month) && (thenMonthDay == time.monthDay);
	}
}