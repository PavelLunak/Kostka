package com.lupa.kostka.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.lupa.kostka.listeners.OnAnimationEndListener;


public class Animators {

    public static void animateShowLayoutSettings(final View view, final OnAnimationEndListener listener) {

        final ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.0f, 1.0f, 1.03f, 1.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());

        final ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.0f, 1.0f, 1.03f, 1.0f);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(objectAnimatorScaleX, objectAnimatorScaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animateSettingsLayout(view, listener);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public static void animateShowLayoutSettingsColor(final View view, final OnAnimationEndListener listener) {

        final ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.0f, 1.0f, 1.03f, 1.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.play(objectAnimatorScaleX);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                animateSettingsLayout(view, listener);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public static void animateHideLayoutSettings(final View view, final OnAnimationEndListener listener) {

        final ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 1.1f, 0.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());

        final ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 1.1f, 0.0f);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(objectAnimatorScaleX, objectAnimatorScaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public static void animateHideLayoutSettingsColor(final View view, final OnAnimationEndListener listener) {

        final ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 1.1f, 0.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.play(objectAnimatorScaleX);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public static AnimatorSet animateDice(final View view) {
        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, 1f);
        objectAnimatorAlpha.setInterpolator(new LinearInterpolator());

        final ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.99f, 1.0f, 1.01f, 1f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());
        objectAnimatorScaleX.setRepeatMode(ObjectAnimator.RESTART);

        final ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 1.01f, 1.0f, 0.99f, 1f);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());
        objectAnimatorScaleY.setRepeatMode(ObjectAnimator.RESTART);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(objectAnimatorScaleX, objectAnimatorScaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                animateDice(view);
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
        return animatorSet;
    }

    public static void animateSettingsLayout(final View view, final OnAnimationEndListener listener) {
        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, 1f);
        objectAnimatorAlpha.setInterpolator(new LinearInterpolator());

        final ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(
                view,
                View.SCALE_X,
                1.0f, 0.99f, 1.01f, 0.995f, 1.005f, 0.99f, 1.01f, 1.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());

        final ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(
                view,
                View.SCALE_Y,
                1.0f, 0.99f, 1.01f, 0.99f, 1.01f, 0.99f, 1.01f, 1.0f);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(objectAnimatorScaleX, objectAnimatorScaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (listener != null) listener.onAnimationEnd();
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public static void animateRotate(View view, float angle) {
        ObjectAnimator objectAnimatorRotate = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, angle);
        objectAnimatorRotate.setInterpolator(new LinearInterpolator());

        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, 1f);
        objectAnimatorAlpha.setInterpolator(new LinearInterpolator());

        ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.8f, 1.2f, 1f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());
        objectAnimatorScaleX.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.8f, 1.2f, 1f);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());
        objectAnimatorScaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(objectAnimatorRotate, objectAnimatorAlpha, objectAnimatorScaleX, objectAnimatorScaleY);
        animatorSet.start();
    }

    public static void animateButtonClick2(final View view, final float endAlpha) {
        ObjectAnimator objectAnimatorAlpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, endAlpha);
        objectAnimatorAlpha.setInterpolator(new LinearInterpolator());
        objectAnimatorAlpha.setDuration(200);
        objectAnimatorAlpha.start();
    }

    public static void showViewScaleSmoothly(final View view, boolean x, boolean y, long startDelay) {
        ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.0f, 1.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());
        objectAnimatorScaleX.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.0f, 1.0f);
        objectAnimatorScaleY.setInterpolator(new AccelerateInterpolator());
        objectAnimatorScaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.setStartDelay(startDelay);

        if (x && y) animatorSet.playTogether(objectAnimatorScaleX, objectAnimatorScaleY);
        else if (x && !y) animatorSet.play(objectAnimatorScaleX);
        else if (!x && y) animatorSet.play(objectAnimatorScaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                view.setScaleX(1f);
                view.setScaleY(1f);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                view.setScaleX(1f);
                view.setScaleY(1f);
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }

    public static void hideViewScaleSmoothly(final View view, boolean x, boolean y, long startDelay) {
        ObjectAnimator objectAnimatorScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0.0f);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());
        objectAnimatorScaleX.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator objectAnimatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0.0f);
        objectAnimatorScaleY.setInterpolator(new AccelerateInterpolator());
        objectAnimatorScaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.setStartDelay(startDelay);

        if (x && y) animatorSet.playTogether(objectAnimatorScaleX, objectAnimatorScaleY);
        else if (x && !y) animatorSet.play(objectAnimatorScaleX);
        else if (!x && y) animatorSet.play(objectAnimatorScaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                view.setScaleX(0f);
                view.setScaleY(0f);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                view.setScaleX(0f);
                view.setScaleY(0f);
            }

            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        });

        animatorSet.start();
    }
}
