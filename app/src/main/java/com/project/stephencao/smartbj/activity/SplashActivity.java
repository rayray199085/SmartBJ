package com.project.stephencao.smartbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.*;
import android.widget.LinearLayout;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.utils.ConstantValues;
import com.project.stephencao.smartbj.utils.SharedPreferenceUtil;

public class SplashActivity extends Activity {
    private LinearLayout mRootLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initAnimation();
    }

    private void initAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
                1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        mRootLinearLayout.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!SharedPreferenceUtil.getBoolean(SplashActivity.this, ConstantValues.IS_FIRST_TIME_LAUNCH)) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    SharedPreferenceUtil.recordBoolean(SplashActivity.this,ConstantValues.IS_FIRST_TIME_LAUNCH,true);
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView() {
        mRootLinearLayout = findViewById(R.id.ll_splash_activity_root);
    }
}


