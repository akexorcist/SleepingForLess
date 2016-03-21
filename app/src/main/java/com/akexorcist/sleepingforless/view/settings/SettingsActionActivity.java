package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;
import android.util.Log;

import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.util.StorageUtility;

public class SettingsActionActivity extends SFLActivity {
    public static final String ACTION_CLEAR_CACHE = "com.akexorcist.sleepingforless.view.settings.SettingsActionActivity.ACTION_CLEAR_CACHE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getAction().equalsIgnoreCase(ACTION_CLEAR_CACHE)) {
            Log.e("Check", "Clear");
            StorageUtility.getInstance().clearAllCache();
        }
        finish();
    }
}
