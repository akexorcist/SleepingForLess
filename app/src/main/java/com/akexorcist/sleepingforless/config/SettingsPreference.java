package com.akexorcist.sleepingforless.config;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.akexorcist.sleepingforless.util.Contextor;
import com.akexorcist.sleepingforless.view.settings.SettingsFragment;

/**
 * Created by Akexorcist on 3/18/2016 AD.
 */
public class SettingsPreference {
    private static SettingsPreference gcmTokenPreference;

    public static SettingsPreference getInstance() {
        if (gcmTokenPreference == null) {
            gcmTokenPreference = new SettingsPreference();
        }
        return gcmTokenPreference;
    }

    private SharedPreferences getPreference() {
        return PreferenceManager.getDefaultSharedPreferences(Contextor.getContext());
    }

    public boolean isNotificationEnable() {
        return getPreference().getBoolean(SettingsFragment.KEY_PUSH_NOTIFICATION, true);
    }
}
