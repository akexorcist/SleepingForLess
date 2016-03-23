package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;

import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.util.StorageUtility;

public class SettingsActionActivity extends SFLActivity {
    public static final String ACTION_CLEAR_CACHE = "com.akexorcist.sleepingforless.view.settings.SettingsActionActivity.ACTION_CLEAR_CACHE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getAction().equalsIgnoreCase(ACTION_CLEAR_CACHE)) {
            clearAppCache();
            StorageUtility.getInstance().clearAllCache();
        }
        finish();
    }

    // Google Analytics
    private void clearAppCache() {
        EventTracking.getInstance().addSettingsTracking(EventKey.Action.CLEAR_APP_CACHE, "");
    }
}
