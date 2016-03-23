package com.akexorcist.sleepingforless.analytic;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.Contextor;
import com.akexorcist.sleepingforless.util.Utility;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Akexorcist on 3/24/2016 AD.
 */
public final class AnalyticsTrackers {
    private static AnalyticsTrackers analyticsTrackers;

    public static synchronized AnalyticsTrackers getInstance() {
        if (analyticsTrackers == null) {
            analyticsTrackers = new AnalyticsTrackers();
        }
        return analyticsTrackers;
    }

    private Tracker tracker;

    public synchronized Tracker getTracker() {
        if (tracker == null) {
            tracker = GoogleAnalytics.getInstance(Contextor.getContext()).newTracker(R.xml.global_tracker);
            tracker.setAppId(Contextor.getContext().getPackageName());
            tracker.setAppVersion(Utility.getInstance().getAppVersion());
            tracker.setAppName(Contextor.getContext().getString(R.string.app_name));
            tracker.enableAutoActivityTracking(false);
            tracker.enableExceptionReporting(true);
        }
        return tracker;
    }
}
