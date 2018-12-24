package com.project.stephencao.smartbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.project.stephencao.smartbj.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {
    private ViewPager mViewPager;
    private int[] mImageIDs;
    private List<ImageView> mImageViewList;
    private Button mStartButton;
    private ImageView mRedDotImageView;
    private int mDotDistance;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initAdapterData();
    }

    private void initAdapterData() {
        mImageIDs = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIDs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIDs[i]);
            mImageViewList.add(imageView);

            ImageView dotImageView = new ImageView(this);
            dotImageView.setImageResource(R.drawable.page_indicator_bg);

            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 20;
            }
            dotImageView.setLayoutParams(params);
            mLinearLayout.addView(dotImageView);

        }

        mRedDotImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // layout has been set done
                mDotDistance = mLinearLayout.getChildAt(1).getLeft() - mLinearLayout.getChildAt(0).getLeft();
                if (mDotDistance > 0) {
                    mRedDotImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int leftMargin = (int) (mDotDistance * v + i * mDotDistance + 0.5f);
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) mRedDotImageView.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                mRedDotImageView.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int i) {
                if (i == mImageIDs.length - 1) {
                    mStartButton.setVisibility(View.VISIBLE);
                }
                else {
                    mStartButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageIDs.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp_guide_activity_view_pager);
        mStartButton = findViewById(R.id.btn_guide_activity_start);
        mLinearLayout = findViewById(R.id.ll_guide_activity_circle_container);
        mRedDotImageView = findViewById(R.id.iv_guide_activity_red_dot);
    }
}
