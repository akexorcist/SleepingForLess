package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;

public class SettingsActivity extends SFLActivity {
    private Toolbar tbTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            addSettingsFragment();
        }

        bindView();
        setupView();
        setToolbar();
    }

    private void addSettingsFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.layout_settings_container, new SettingsFragment())
                .commit();
    }

    private void bindView() {
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
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
}
