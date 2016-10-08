package com.android.calendar.occasion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.format.DateUtils;
import android.text.format.Time;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.android.calendar.CalendarApplication;
import com.android.calendar.hijri.HijriCalendar;
import com.android.calendar.jalali.Jalali;
import com.android.calendar.jalali.JalaliDate;
import com.congenialmobile.calendar.R;

public class OccasionHelper {

	private static HashMap<String, List<Occasion>> mCahcedOccasions = new HashMap<String, List<Occasion>>();
	private static HashMap<String, Boolean> mCahcedHollidays = new HashMap<String, Boolean>();

	private static String dateStringBuilder(JalaliDate jDate) {
		return jDate.year + String.format("%02d", jDate.month) + String.format("%02d", jDate.day);
	}

	public static List<Occasion> getOccasionsForDay(int year, int month, int day) {
		return getOccasionsForDay(new JalaliDate(year, month, day));
	}

	public static List<Occasion> getOccasionsForDay(JalaliDate jDate) {
		String dateString = dateStringBuilder(jDate);
		if (mCahcedOccasions.containsKey(dateString)) {
			return mCahcedOccasions.get(dateString);
		}

		Time gDate = Jalali.jalaliToGregorianTime(jDate);
		HijriCalendar hDate = new HijriCalendar(jDate);
		StringBuilder queryBuilder = new StringBuilder();
		// Jalali
		queryBuilder.append("(calendar_type = ");
		queryBuilder.append(Occasion.CALENDAR_JALALI);
		queryBuilder.append(" and month = ");
		queryBuilder.append(jDate.month);
		queryBuilder.append(" and day = ");
		queryBuilder.append(jDate.day);
		queryBuilder.append(")");
		// Gregorian
		queryBuilder.append(" or (calendar_type = ");
		queryBuilder.append(Occasion.CALENDAR_GREGORIAN);
		queryBuilder.append(" and month = ");
		queryBuilder.append(gDate.month + 1);
		queryBuilder.append(" and day = ");
		queryBuilder.append(gDate.monthDay);
		queryBuilder.append(")");
		// Hijri
		queryBuilder.append(" or (calendar_type = ");
		queryBuilder.append(Occasion.CALENDAR_HIJRI);
		queryBuilder.append(" and month = ");
		queryBuilder.append(hDate.month);
		queryBuilder.append(" and day = ");
		queryBuilder.append(hDate.day);
		queryBuilder.append(")");

		List<Occasion> result = null;
		result = new Select().from(Occasion.class).where(queryBuilder.toString()).execute();
		// result = Occasion.findWithQuery(Occasion.class,
		// queryBuilder.toString(), new String[] {});
		if (result == null)
			result = new ArrayList<Occasion>();

		for (Occasion occasion : result) {
			if (occasion.calendarType != Occasion.CALENDAR_JALALI) {
				String dayString = "";
				if (occasion.calendarType == Occasion.CALENDAR_GREGORIAN) {
					Time time = new Time();
					time.set(occasion.day, occasion.month - 1, 2015);
					dayString = DateUtils.formatDateTime(CalendarApplication.getContext(), time.toMillis(true),
							DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR);
				} else if (occasion.calendarType == Occasion.CALENDAR_HIJRI) {
					dayString = Jalali.persianDigits(Integer.toString(occasion.day))
							+ " "
							+ CalendarApplication.getContext().getResources().getStringArray(R.array.hijri_months)[occasion.month - 1];
				}
				occasion.title += " (" + dayString + ")";
			}

		}
		mCahcedOccasions.put(dateString, result);
		return result;
	}

	public static boolean isHolliday(long millis) {
		Time time = new Time();
		time.set(millis);
		return isHolliday(Jalali.gregorianToJalali(time));
	}

	public static boolean isHolliday(int year, int month, int day) {

		return isHolliday(new JalaliDate(year, month, day));
	}

	public static boolean isHolliday(JalaliDate jDate) {
		String dateString = dateStringBuilder(jDate);
		if (mCahcedHollidays.containsKey(dateString)) {
			return mCahcedHollidays.get(dateString);
		}

		boolean isHolliday = false;
		Time time = Jalali.jalaliToGregorianTime(jDate);
		time.normalize(true);
		if (time.weekDay == Time.FRIDAY) {
			isHolliday = true;
		} else {
			List<Occasion> result = getOccasionsForDay(jDate.year, jDate.month, jDate.day);
			for (Occasion occasion : result) {
				if (occasion.isHolliday) {
					isHolliday = true;
					break;
				}
			}
		}

		mCahcedHollidays.put(dateString, isHolliday);
		return isHolliday;
	}

	public static void fillWeekHollidays(boolean[] isHolliday, JalaliDate firstDay) {
		JalaliDate iteratorDay = new JalaliDate(firstDay);
		for (int i = 0; i < isHolliday.length; i++, iteratorDay.increasOneDay()) {
			isHolliday[i] = isHolliday(iteratorDay);
			// System.out.println(iteratorDay + "  " + isHolliday[i]);
		}
		// StringBuilder queryBuilder = new StringBuilder();
		// queryBuilder.append("select * from Occasion where ");
		// for (int i = 0; i < isHolliday.length; i++,
		// iteratorDay.increasOneDay()) {
		// if (i > 0)
		// queryBuilder.append(" or ");
		// queryBuilder.append("((year = 0 or year = ");
		// queryBuilder.append(iteratorDay.year);
		// queryBuilder.append(") and month = ");
		// queryBuilder.append(iteratorDay.month);
		// queryBuilder.append(" and day = ");
		// queryBuilder.append(iteratorDay.day);
		// queryBuilder.append(" and is_holliday != 0)");
		// }
		// queryBuilder.append(" order by year, month, day");
		// List<Occasion> result = null;
		// result = Occasion.findWithQuery(Occasion.class,
		// queryBuilder.toString(), new String[] {});
		// int i = 0;
		// iteratorDay = new JalaliDate(firstDay);
		// for (Occasion occasion : result) {
		// JalaliDate occasionDate = new JalaliDate(occasion.year,
		// occasion.month, occasion.day);
		// while (!occasionDate.equals(iteratorDay)) {
		// iteratorDay.increasOneDay();
		// i++;
		// }
		// isHolliday[i] = true;
		// }
	}

	public static void installOccasions() {
		try {
			InputStream inputStream = CalendarApplication.getContext().getAssets().open("occasions");
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			try {
				ActiveAndroid.beginTransaction();
				Occasion.delete(Occasion.class);
				OccasionMaintanance.delete(Occasion.class);
				while ((line = in.readLine()) != null) {
					String[] occasionString = line.split(",");
					Occasion occasion = new Occasion(Integer.valueOf(occasionString[0]),
							Integer.valueOf(occasionString[1]), Integer.valueOf(occasionString[2]), occasionString[3],
							occasionString[4].equals("1"));
					occasion.save();
				}
				in.close();
				OccasionMaintanance om = new OccasionMaintanance(1);
				om.save();
				ActiveAndroid.setTransactionSuccessful();
			} finally {
				ActiveAndroid.endTransaction();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
