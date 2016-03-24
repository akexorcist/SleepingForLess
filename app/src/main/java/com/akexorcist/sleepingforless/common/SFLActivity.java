package com.akexorcist.sleepingforless.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.bus.BusProvider;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class SFLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.anim_default_fade_in, R.anim.anim_default_fade_out);
        super.onCreate(savedInstanceState);
    }

    protected void openActivityDelayed(final Class<? extends Activity> activityClass) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(activityClass);
            }
        }, 500);
    }

    protected void openActivityDelayed(final Class<? extends Activity> activityClass, final Bundle bundle) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(activityClass, bundle);
            }
        }, 500);
    }

    protected void openActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    protected void openActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        startActivity(new Intent(this, activityClass).putExtras(bundle));
    }

    protected void openActivity(Class<? extends Activity> activityClass, Bundle bundle, boolean singleTop) {
        Intent intent = new Intent(this, activityClass).putExtras(bundle);
        if (singleTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
