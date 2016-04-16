package com.akexorcist.sleepingforless.view.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventKey;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.common.SFLActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends SFLActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Bind(R.id.tb_title)
    Toolbar tbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            addSettingsFragment();
        }

        ButterKnife.bind(this);
        setupView();
        setToolbar();

        if (savedInstanceState == null) {
            screenTracking();
        }
    }

    private void addSettingsFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.layout_settings_container, new SettingsFragment())
                .commit();
    }

    private void setupView() {

    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(R.string.title_settings);
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerPreferenceListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterPreferenceListener();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(getString(R.string.settings_key_push_notification))) {
            boolean state = sharedPreferences.getBoolean(getString(R.string.settings_key_push_notification), false);
            turnOffNotification(state);
        }
    }

    private void registerPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void unregisterPreferenceListener() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    // Google Analytics
    private void screenTracking() {
        EventTracking.getInstance().addScreen(EventKey.Page.Settings);
    }

    private void turnOffNotification(boolean state) {
        if (state) {
            EventTracking.getInstance().addSettingsTracking(EventKey.Action.PUSH_NOTIFICATION, EventKey.Label.ENABLE);
        } else {
            EventTracking.getInstance().addSettingsTracking(EventKey.Action.PUSH_NOTIFICATION, EventKey.Label.DISABLE);
        }
    }
}
