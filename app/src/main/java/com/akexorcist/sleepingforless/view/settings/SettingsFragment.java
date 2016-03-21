package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.StorageUtility;

public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PUSH_NOTIFICATION = "key_push_notification";
    public static final String KEY_CLEAR_APP_CACHE = "key_clear_app_cache";

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCacheSize();
    }

    private void updateCacheSize() {
        Preference preference = findPreference(KEY_CLEAR_APP_CACHE);
        preference.setSummary(getString(R.string.cache_file_size, StorageUtility.getInstance().getAllCacheSize()));
    }
}
