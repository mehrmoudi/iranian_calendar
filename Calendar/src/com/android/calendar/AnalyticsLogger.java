package com.android.calendar;

import android.app.Application;

/**
 * Interface for analytics logging.
 */
public interface AnalyticsLogger {

    /**
     * Open backend of logger.
     *
     * @param context need to open backend of logger.
     * @return true, if analytics logging is ready to be use.
     */
    public boolean initialize(Application application);

    /**
     * Track what view people are using.
     *
     * @param name of the view.
     */
    public void trackView(String name);
}
