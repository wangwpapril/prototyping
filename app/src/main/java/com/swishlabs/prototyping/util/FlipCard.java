package com.swishlabs.prototyping.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by will on 16/2/20.
 */
public class FlipCard {

    public static void flipCard(View front_view, View back_view) {
        Interpolator accelerator = new AccelerateInterpolator();
        Interpolator decelerator = new DecelerateInterpolator();
        final View visibleList,invisibleList;
        final ObjectAnimator visToInvis, invisToVis;
        if (front_view.getVisibility() == View.GONE) {
            visibleList = back_view;
            invisibleList = front_view;
            visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f);
            invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f);
        } else {
            invisibleList = back_view;
            visibleList = front_view;
            visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, -90f);
            invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", 90f, 0f);
        }
        visToInvis.setDuration(300);
        invisToVis.setDuration(300);
        visToInvis.setInterpolator(accelerator);
        invisToVis.setInterpolator(decelerator);
        visToInvis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                visibleList.setVisibility(View.GONE);
                invisToVis.start();
                invisibleList.setVisibility(View.VISIBLE);
            }
        });
        visToInvis.start();


    }
}
