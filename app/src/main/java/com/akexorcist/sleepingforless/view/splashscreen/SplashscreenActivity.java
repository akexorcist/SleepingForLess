package com.akexorcist.sleepingforless.view.splashscreen;

import android.os.Handler;
import android.os.Bundle;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.config.FirstLaunchPreference;
import com.akexorcist.sleepingforless.view.feed.FeedActivity;

import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;

public class SplashScreenActivity extends SFLActivity {
    private ViewRevealAnimator vraSplashScreen;
    private boolean isScreenShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstLaunch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isScreenShowing = false;
        finish();
    }

    private void checkFirstLaunch() {
        if (FirstLaunchPreference.getInstance().isSplashWasLaunchedBefore()) {
            goToFeedActivity();
        } else {
            setupScreenAndSplash();
        }
    }

    private void goToFeedActivity() {
        openActivity(FeedActivity.class);
        finish();
    }

    private void setupScreenAndSplash() {
        setContentView(R.layout.activity_splash_screen);
        vraSplashScreen = (ViewRevealAnimator) findViewById(R.id.vra_splash_screen);
        isScreenShowing = true;
        startSplash();
    }

    private void startSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                vraSplashScreen.setDisplayedChild(1);
                openFeedActivity();
            }
        }, 500);
    }

    private void openFeedActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScreenShowing) {
                    openActivity(FeedActivity.class);
                    FirstLaunchPreference.getInstance().setSplashWasLaunched();
                    finish();
                }
            }
        }, 2000);
    }
}
