package com.akexorcist.sleepingforless.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_simple_fade_in, R.anim.anim_simple_fade_out);
    }

    protected void openActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    protected void openActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        startActivity(new Intent(this, activityClass).putExtras(bundle));
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
