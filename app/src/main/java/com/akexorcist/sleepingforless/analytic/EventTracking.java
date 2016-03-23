package com.akexorcist.sleepingforless.analytic;

import com.akexorcist.sleepingforless.config.DeveloperConfig;
import com.akexorcist.sleepingforless.util.Contextor;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Akexorcist on 3/23/2016 AD.
 */
public class EventTracking {
    private static EventTracking eventTracking;

    public static EventTracking getInstance() {
        if (eventTracking == null) {
            eventTracking = new EventTracking();
        }
        return eventTracking;
    }

    public Tracker getAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.getTracker();
    }

    public void addContentTracking(String action, String label) {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            Tracker tracker = getAnalyticsTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(EventKey.Category.CONTENT_TRACKING)
                    .setAction(action)
                    .setLabel(label)
                    .build());
            tracker.send(new HitBuilders.EventBuilder().build());
            GoogleAnalytics.getInstance(Contextor.getContext()).dispatchLocalHits();
        }
    }

    public void addSearchTracking(String keyword) {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            Tracker tracker = getAnalyticsTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(EventKey.Category.SEARCH_TRACKING)
                    .setAction(EventKey.Action.SEARCH)
                    .setLabel(keyword)
                    .build());
            tracker.send(new HitBuilders.EventBuilder().build());
            GoogleAnalytics.getInstance(Contextor.getContext()).dispatchLocalHits();
        }
    }

    public void addSortPostTracking(String sortType) {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            Tracker tracker = getAnalyticsTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(EventKey.Category.SORT_TYPE_TRACKING)
                    .setAction(EventKey.Action.SORT)
                    .setLabel(sortType)
                    .build());
            tracker.send(new HitBuilders.EventBuilder().build());
            GoogleAnalytics.getInstance(Contextor.getContext()).dispatchLocalHits();
        }
    }

    public void addSettingsTracking(String action, String label) {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            Tracker tracker = getAnalyticsTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(EventKey.Category.SETTINGS_TRACKING)
                    .setAction(action)
                    .setLabel(label)
                    .build());
            tracker.send(new HitBuilders.EventBuilder().build());
            GoogleAnalytics.getInstance(Contextor.getContext()).dispatchLocalHits();
        }
    }

    public void addScreen(String screenName) {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            Tracker tracker = getAnalyticsTracker();
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
            GoogleAnalytics.getInstance(Contextor.getContext()).dispatchLocalHits();
        }
    }
}
