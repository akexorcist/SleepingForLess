package com.akexorcist.sleepingforless.view.splashscreen;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.akexorcist.sleepingforless.config.FirstLaunchPreference;
import com.akexorcist.sleepingforless.util.content.EasterEggUtility;
import com.akexorcist.sleepingforless.view.feed.FeedActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;

public class SplashActivity extends SFLActivity {
    @Bind(R.id.vra_splash_screen)
    ViewRevealAnimator vraSplashScreen;

    @Bind(R.id.iv_logo)
    ImageView ivLogo;

    boolean isScreenShowing;

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
        ButterKnife.bind(this);
        isScreenShowing = true;
        setupView();
    }

    private void setupView() {
        // Easter Egg for April Fool Day
        if (EasterEggUtility.newInstance().isAprilFoolDay()) {
            ivLogo.setImageResource(R.mipmap.ic_apple_logo);
        }

        // Avoid animator start on a detached view
        vraSplashScreen.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                startSplash();
            }
        });
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
