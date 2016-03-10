package com.akexorcist.sleepingforless.util;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class AnimationUtility {
    private static AnimationUtility utility;

    public static AnimationUtility getInstance() {
        if (utility == null) {
            utility = new AnimationUtility();
        }
        return utility;
    }

    public static final int DEFAULT_DURATION = 300;

    public void fadeOut(View view, int duration) {
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f)
                .setDuration(duration)
                .start();
    }

    public void fadeOut(View view) {
        ObjectAnimator.ofFloat(view, View.ALPHA, 0f)
                .setDuration(DEFAULT_DURATION)
                .start();
    }

    public void fadeIn(View view, int duration) {
        ObjectAnimator.ofFloat(view, View.ALPHA, 1f)
                .setDuration(duration)
                .start();
    }

    public void fadeIn(View view) {
        ObjectAnimator.ofFloat(view, View.ALPHA, 1f)
                .setDuration(DEFAULT_DURATION)
                .start();
    }
}
