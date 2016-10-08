/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.calendar;

import java.util.List;

import android.content.Context;

import com.activeandroid.app.Application;
import com.activeandroid.query.Select;
import com.android.calendar.occasion.OccasionHelper;
import com.android.calendar.occasion.OccasionMaintanance;
import com.congenialmobile.calendar.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class CalendarApplication extends Application {

	private static Context mContext;
	private Tracker mTracker;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		List<OccasionMaintanance> result = new Select().from(OccasionMaintanance.class).where("installed = 1")
				.execute();
		if (result == null || result.size() == 0) {
			OccasionHelper.installOccasions();
		}
		getTracker();
		/*
		 * Ensure the default values are set for any receiver, activity,
		 * service, etc. of Calendar
		 */
		GeneralPreferences.setDefaultValues(this);

		// Save the version number, for upcoming 'What's new' screen. This will
		// be later be
		// moved to that implementation.
		Utils.setSharedPreference(this, GeneralPreferences.KEY_VERSION, Utils.getVersionCode(this));

		// Initialize the registry mapping some custom behavior.
		ExtensionsFactory.init(getAssets());
	}

	public static Context getContext() {
		return mContext;
	}

	synchronized Tracker getTracker() {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		if (mTracker == null)
			mTracker = analytics.newTracker(R.xml.global_tracker);
		return mTracker;
	}
}
