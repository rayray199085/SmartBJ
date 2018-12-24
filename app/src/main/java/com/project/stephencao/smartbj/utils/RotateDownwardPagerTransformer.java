package com.project.stephencao.smartbj.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class RotateDownwardPagerTransformer implements ViewPager.PageTransformer {

    private static final float MAX_DEGREE = 20f;
    private static final float MIN_SCALE = 0.75f;

    private float mRotation;

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setRotation(0f);

        } else if (position <= 0) { // [-1,0] A页
            // Use the default slide transition when moving to the left page
            mRotation = MAX_DEGREE * position;// position between 0 ~ -1
            view.setPivotX(pageWidth / 2);//pivot x-axis
            view.setPivotY(view.getMeasuredHeight());// y-axis
            view.setRotation(mRotation);// rotation animation


        } else if (position <= 1) { // (0,1] B页
            // Fade the page out.

            // Counteract the default slide transition
            mRotation = MAX_DEGREE * position;// position between 0 ~ -1
            view.setPivotX(pageWidth / 2);//pivot x-axis
            view.setPivotY(view.getMeasuredHeight());// y-axis
            view.setRotation(mRotation);//rotation animation

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setRotation(0f);
        }
    }
}
