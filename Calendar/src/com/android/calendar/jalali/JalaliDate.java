package com.android.calendar.jalali;

public class JalaliDate {
	public int year;
	public int month;
	public int day;

	public JalaliDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public JalaliDate(JalaliDate that) {
		this(that.year, that.month, that.day);
	}

	public void set(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public void set(JalaliDate that) {
		set(that.year, that.month, that.day);
	}

	public void increaseMonth(int num) {
		if (num < 0) {
			decreaseMonth(-num);
		}
		if (num > 12) {
			year += num / 12;
			num %= 12;
		}
		month += num;
		if (month > 12) {
			year++;
			month -= 12;
		}

		checkMonthDay();
	}

	public void increasOneDay() {
		if (month <= 6 && day <= 30) {
			day++;
		} else if (month <= 6) {
			day = 1;
			month++;
		} else if (month <= 11 && day <= 29) {
			day++;
		} else if (month <= 11) {
			day = 1;
			month++;
		} else if (month == 12 && day <= 28) {
			day++;
		} else if (month == 12 && day == 29) {
			if (Jalali.isJalaliLeapYear(year)) {
				day++;
			} else {
				year++;
				month = 1;
				day = 1;
			}
		} else if (month == 12 && day == 30) {
			year++;
			month = 1;
			day = 1;
		}
	}

	public void decreaseMonth(int num) {
		if (num < 0) {
			increaseMonth(-num);
		}
		if (num > 12) {
			year -= num / 12;
			num %= 12;
		}
		month -= num;
		if (month < 1) {
			year--;
			month += 12;
		}

		checkMonthDay();
	}

	public void checkMonthDay() {
		if (day < 29)
			return;
		int max = Jalali.getMaxMonthDay(year, month);
		if (day > max) {
			day = max;
		}
	}

	@Override
	public boolean equals(Object o) {
		try {
			JalaliDate date = (JalaliDate) o;
			if (date.day == day && date.month == month && date.year == year)
				return true;
		} catch (ClassCastException e) {
			return false;
		}
		return false;
	}

	public int getWeekNumber() {
		int days = 0;
		if (month <= 7) {
			days += (month - 1) * 31;
		} else {
			days += 6 * 31;
			days += (month - 7) * 30;
		}
		days += day;
		return days / 7 + 1;
	}

	@Override
	public String toString() {
		return year + "/" + month + "/" + day;
	}
}