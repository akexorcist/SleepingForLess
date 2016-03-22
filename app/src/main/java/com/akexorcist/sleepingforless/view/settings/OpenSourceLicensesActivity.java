package com.akexorcist.sleepingforless.view.settings;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class OpenSourceLicensesActivity extends SFLActivity {
    private Toolbar tbTitle;
    private RecyclerView rvOpenSourceLicenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_licenses);

        bindView();
        setupView();
        setToolbar();
    }

    private void bindView() {
        tbTitle = (Toolbar) findViewById(R.id.tb_title);
        rvOpenSourceLicenseList = (RecyclerView) findViewById(R.id.rv_open_source_license_list);
    }

    private void setupView() {


    }

    private void setToolbar() {
        setSupportActionBar(tbTitle);
        setTitle(R.string.title_open_source_licenses);
        tbTitle.setNavigationIcon(R.drawable.vector_ic_back);
        tbTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<OpenSourceLicense.License> getOpenSourceLicense() {
        OpenSourceLicense license = new Gson().fromJson(getLicenseJsonString().toString(), OpenSourceLicense.class);
        return license.getLicenseList();
    }

    private String getLicenseJsonString() {
        try {
            InputStream inputStream = getAssets().open("license/OpenSourceLicense.json");
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
