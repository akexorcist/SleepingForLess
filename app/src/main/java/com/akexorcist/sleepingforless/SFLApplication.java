package com.akexorcist.sleepingforless;

import android.app.Application;

import com.akexorcist.sleepingforless.analytic.AnalyticsTrackers;
import com.akexorcist.sleepingforless.config.DeveloperConfig;
import com.akexorcist.sleepingforless.util.Contextor;
import com.akexorcist.sleepingforless.util.Utility;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class SFLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.init(getApplicationContext());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CSPraJad.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        initAnalyticsTrackers();
    }

    public void initAnalyticsTrackers() {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            AnalyticsTrackers.getInstance().getTracker().setAppId(getPackageName());
            AnalyticsTrackers.getInstance().getTracker().setAppVersion(Utility.getInstance().getAppVersion());
            AnalyticsTrackers.getInstance().getTracker().setAppName(getString(R.string.app_name));
        }
    }
}
