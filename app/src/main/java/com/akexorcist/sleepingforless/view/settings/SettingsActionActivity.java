package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;

import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.util.ExternalBrowserUtility;
import com.akexorcist.sleepingforless.util.StorageUtility;
import com.akexorcist.sleepingforless.util.Utility;

public class SettingsActionActivity extends SFLActivity {
    private static final String URL_ABOUT_ME = "http://www.akexorcist.com/2014/09/about-me-sleeping-for-less.html";
    public static final String ACTION_CLEAR_CACHE = "com.akexorcist.sleepingforless.view.settings.SettingsActionActivity.ACTION_CLEAR_CACHE";
    public static final String ACTION_ABOUT_ME = "com.akexorcist.sleepingforless.view.settings.SettingsActionActivity.ACTION_ABOUT_ME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getAction().equalsIgnoreCase(ACTION_CLEAR_CACHE)) {
            clearAppCache();
            StorageUtility.getInstance().clearAllCache();
        } else if (getIntent() != null && getIntent().getAction().equalsIgnoreCase(ACTION_ABOUT_ME)) {
            ExternalBrowserUtility.getInstance().open(this, URL_ABOUT_ME);
        }
        finish();
    }

    // Google Analytics
    private void clearAppCache() {
        EventTracking.getInstance().addSettingsTracking(EventKey.Action.CLEAR_APP_CACHE, "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        ExternalBrowserUtility.getInstance().bindService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ExternalBrowserUtility.getInstance().unbindService(this);

    }
}
