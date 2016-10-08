package com.android.calendar.occasion;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Maintanance")
public class OccasionMaintanance extends Model {
	@Column(name = "installed")
	int installed;

	public OccasionMaintanance() {
		super();
	}

	public OccasionMaintanance(int installed) {
		super();
		this.installed = installed;
	}

}
