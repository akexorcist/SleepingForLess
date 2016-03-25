package com.akexorcist.sleepingforless.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.akexorcist.sleepingforless.util.Contextor;

/**
 * Created by Akexorcist on 3/18/2016 AD.
 */
public class FirstLaunchPreference {
    private static final String FIRST_LAUNCH_PREFERENCE = "first_launcher_preference";
    private static final String KEY_SPLASH_SCREEN = "splash_screen";

    private static FirstLaunchPreference contentPreference;

    public static FirstLaunchPreference getInstance() {
        if (contentPreference == null) {
            contentPreference = new FirstLaunchPreference();
        }
        return contentPreference;
    }

    private SharedPreferences getPreference() {
        return Contextor.getContext().getSharedPreferences(FIRST_LAUNCH_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void setSplashWasLaunched() {
        getPreference()
                .edit()
                .putBoolean(KEY_SPLASH_SCREEN, true)
                .apply();
    }

    public boolean isSplashWasLaunchedBefore() {
        return getPreference().getBoolean(KEY_SPLASH_SCREEN, false);
    }
}
