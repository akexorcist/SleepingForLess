package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.StorageUtility;
import com.akexorcist.sleepingforless.util.Utility;

public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PUSH_NOTIFICATION = "key_push_notification";
    public static final String KEY_CLEAR_APP_CACHE = "key_clear_app_cache";
    public static final String KEY_APP_VERSION = "key_app_version";

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);
        updateAppVersion();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        removeBottomLine(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCacheSize();
    }

    private void updateCacheSize() {
        Preference preference = findPreference(KEY_CLEAR_APP_CACHE);
        preference.setSummary(StorageUtility.getInstance().getAllCacheSize());
    }

    private void updateAppVersion() {
        Preference preference = findPreference(KEY_APP_VERSION);
        preference.setSummary(Utility.getInstance().getAppVersion());
    }

    private void removeBottomLine(View rootView) {
        ListView list = (ListView) rootView.findViewById(android.R.id.list);
//        list.setDivider(new ColorDrawable(Color.RED)); // or some other color int
        list.setDividerHeight((0));
//        list.setVerticalScrollBarEnabled(false);
    }
}
