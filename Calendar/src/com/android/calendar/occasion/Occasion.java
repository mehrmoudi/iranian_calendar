package com.android.calendar.occasion;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Occasion")
public class Occasion extends Model /* SugarRecord<Occasion> */{

	@Column(name = "calendar_type")
	public int calendarType;
	@Column(name = "month")
	public int month;
	@Column(name = "day")
	public int day;
	@Column(name = "title")
	public String title;
	@Column(name = "is_holliday")
	public boolean isHolliday;

	static final int CALENDAR_JALALI = 1;
	static final int CALENDAR_GREGORIAN = 2;
	static final int CALENDAR_HIJRI = 3;

	public Occasion() {
		super();
	}

	public Occasion(int calendarType, int month, int day, String title, boolean isHolliday) {
		super();
		this.calendarType = calendarType;
		this.month = month;
		this.day = day;
		this.title = title;
		this.isHolliday = isHolliday;
	}

	// int calendarType, month, day;
	// String title;
	// boolean isHolliday;
	//
	// @Ignore
	// static final int CALENDAR_JALALI = 1;
	// @Ignore
	// static final int CALENDAR_GREGORIAN = 2;
	// @Ignore
	// static final int CALENDAR_HIJRI = 3;
	//
	// public Occasion() {
	// }
	//
	// public Occasion(int calendarType, int month, int day, String title,
	// boolean isHolliday) {
	// super();
	// this.calendarType = calendarType;
	// this.month = month;
	// this.day = day;
	// this.title = title;
	// this.isHolliday = isHolliday;
	// }

}