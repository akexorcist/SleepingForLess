package com.akexorcist.sleepingforless;

import android.app.Application;

import com.akexorcist.sleepingforless.util.Contextor;

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
    }
}
