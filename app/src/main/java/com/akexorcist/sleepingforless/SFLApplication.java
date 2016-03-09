package com.akexorcist.sleepingforless;

import android.app.Application;

import com.akexorcist.sleepingforless.util.Contextor;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class SFLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.init(getApplicationContext());
    }
}
