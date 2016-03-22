package com.akexorcist.sleepingforless.util;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.akexorcist.sleepingforless.R;

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
    public static final int DEFAULT_START_DELAY = 0;

    public void fadeOut(View view) {
        fadeOut(view, DEFAULT_DURATION, DEFAULT_START_DELAY, null);
    }

    public void fadeOut(View view, AnimationFinishCallback callback) {
        fadeOut(view, DEFAULT_DURATION, DEFAULT_START_DELAY, callback);
    }

    public void fadeOut(View view, int duration) {
        fadeOut(view, duration, DEFAULT_START_DELAY, null);
    }

    public void fadeOut(View view, int duration, AnimationFinishCallback callback) {
        fadeOut(view, duration, DEFAULT_START_DELAY, callback);
    }

    public void fadeOut(View view, long startDelay) {
        fadeOut(view, DEFAULT_DURATION, startDelay, null);
    }

    public void fadeOut(final View view, int duration, long startDelay, final AnimationFinishCallback callback) {
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(Contextor.getContext(), R.animator.animator_default_fade_out);
        objectAnimator.setTarget(view);
        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(startDelay);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (callback != null) {
                    callback.onAnimationFinished();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.start();
    }

    public void fadeIn(View view) {
        fadeIn(view, DEFAULT_DURATION, DEFAULT_START_DELAY);
    }

    public void fadeIn(View view, int duration) {
        fadeIn(view, duration, DEFAULT_START_DELAY);
    }

    public void fadeIn(View view, long startDelay) {
        fadeIn(view, DEFAULT_DURATION, startDelay);
    }

    public void fadeIn(final View view, int duration, long startDelay) {
        view.setEnabled(false);
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(Contextor.getContext(), R.animator.animator_default_fade_in);
        objectAnimator.setTarget(view);
        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(startDelay);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.start();
    }

    public void scaleUp(View view, long startDelay) {
        scaleUp(view, DEFAULT_DURATION, startDelay);
    }

    public void scaleUp(View view, int duration) {
        scaleUp(view, duration, DEFAULT_START_DELAY);
    }

    public void scaleUp(View view) {
        scaleUp(view, DEFAULT_DURATION, DEFAULT_START_DELAY);
    }

    public void scaleUp(final View view, int duration, long startDelay) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(Contextor.getContext(), R.animator.animator_default_scale_up);
        animatorSet.setTarget(view);
        animatorSet.setDuration(duration);
        animatorSet.setStartDelay(startDelay);
        animatorSet.start();
    }

    public void scaleDown(View view, long startDelay) {
        scaleDown(view, DEFAULT_DURATION, startDelay);
    }

    public void scaleDown(View view, int duration) {
        scaleDown(view, duration, DEFAULT_START_DELAY);
    }

    public void scaleDown(View view) {
        scaleDown(view, DEFAULT_DURATION, DEFAULT_START_DELAY);
    }

    public void scaleDown(final View view, int duration, long startDelay) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(Contextor.getContext(), R.animator.animator_default_scale_down);
        animatorSet.setTarget(view);
        animatorSet.setDuration(duration);
        animatorSet.setStartDelay(startDelay);
        animatorSet.start();
    }

    public void scaleBack(View view, long startDelay) {
        scaleBack(view, DEFAULT_DURATION, startDelay);
    }

    public void scaleBack(View view, int duration) {
        scaleBack(view, duration, DEFAULT_START_DELAY);
    }

    public void scaleBack(View view) {
        scaleBack(view, DEFAULT_DURATION, DEFAULT_START_DELAY);
    }

    public void scaleBack(final View view, int duration, long startDelay) {
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(Contextor.getContext(), R.animator.animator_default_scale_back);
        animatorSet.setTarget(view);
        animatorSet.setDuration(duration);
        animatorSet.setStartDelay(startDelay);
        animatorSet.start();
    }

    public interface AnimationFinishCallback {
        void onAnimationFinished();
    }
}
