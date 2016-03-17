package com.akexorcist.sleepingforless.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.akexorcist.sleepingforless.util.Contextor;

/**
 * Created by Akexorcist on 3/18/2016 AD.
 */
public class ContentPreference {
    private static final String CONTENT_PREFERENCE = "content_preference";
    private static final String KEY_WARN_OFFLINE_BOOKMARK = "warn_offline_bookmark";

    private static ContentPreference contentPreference;

    public static ContentPreference getInstance() {
        if (contentPreference == null) {
            contentPreference = new ContentPreference();
        }
        return contentPreference;
    }

    private SharedPreferences getPreference() {
        return Contextor.getContext().getSharedPreferences(CONTENT_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void dontWarnOfflineBookmark() {
        getPreference()
                .edit()
                .putBoolean(KEY_WARN_OFFLINE_BOOKMARK, false)
                .apply();
    }

    public boolean shouldWarnOfflineBookmark() {
        return getPreference().getBoolean(KEY_WARN_OFFLINE_BOOKMARK, true);
    }
}
