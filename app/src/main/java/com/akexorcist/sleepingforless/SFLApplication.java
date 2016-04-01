package com.akexorcist.sleepingforless;

import android.app.Application;

import com.akexorcist.sleepingforless.analytic.AnalyticsTrackers;
import com.akexorcist.sleepingforless.config.DeveloperConfig;
import com.akexorcist.sleepingforless.config.GcmTokenPreference;
import com.akexorcist.sleepingforless.gcm.GcmTokenManager;
import com.akexorcist.sleepingforless.util.Contextor;
import com.akexorcist.sleepingforless.util.Utility;
import com.akexorcist.sleepingforless.util.content.EasterEggUtility;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
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
        initFont();
        initRealm();
        initGcmToken();
        initCrashActivity();
        initAnalyticsTrackers();
        initEasterEgg();
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/CSPraJad.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void initRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initCrashActivity() {
        if (DeveloperConfig.ALLOW_CRASH_ACTIVITY) {
            CustomActivityOnCrash.install(this);
            CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.mipmap.ic_force_close);
            CustomActivityOnCrash.setShowErrorDetails(true);
            CustomActivityOnCrash.setEnableAppRestart(true);
        }
    }

    private void initAnalyticsTrackers() {
        if (DeveloperConfig.ALLOW_ANALYTICS) {
            AnalyticsTrackers.getInstance().getTracker().setAppId(getPackageName());
            AnalyticsTrackers.getInstance().getTracker().setAppVersion(Utility.getInstance().getAppVersion());
            AnalyticsTrackers.getInstance().getTracker().setAppName(getString(R.string.app_name));
        }
    }

    private void initGcmToken() {
        if (!GcmTokenPreference.getInstance().isNewTokenSent()) {
            String token = GcmTokenPreference.getInstance().getTokenId();
            GcmTokenManager.getInstance().sendTokenToServer(token);
        }
    }

    private void initEasterEgg() {
        EasterEggUtility.newInstance().updateToday();
    }
}
