package com.project.stephencao.smartbj.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.adapter.MyFragmentPagerAdapter;
import com.project.stephencao.smartbj.fragment.*;
import com.project.stephencao.smartbj.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener, NewsFragment.SendData {
    private MyViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private List<Fragment> mFragmentList;
    private ImageView mOpenDrawerImageView, mCloseDrawerImageView, mSwitchImagesDisplayStyleIV;
    private LinearLayout mHomeLayout, mNewsLayout, mServiceLayout, mHelpLayout, mSettingsLayout, mSlidingMenuContentLayout;
    private ImageButton mHomeButton, mNewsButton, mServiceButton, mHelpButton, mSettingsButton;
    private TextView mTitleTextView, mHomeTextView, mNewsTextView, mServiceTextView, mHelpTextView, mSettingsTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEnterPage();
    }

    private void initEnterPage() {
        mHomeButton.setImageResource(R.drawable.home_selected);
        mHomeTextView.setTextColor(Color.RED);
        mTitleTextView.setText("主页");
    }

    /**
     * initialise sliding menu data and view pager
     */
    private void initData() {

        /**
         * initialize fragments for the view pager
         */
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        NewsFragment newsFragment = new NewsFragment();
        mFragmentList.add(newsFragment);
        mFragmentList.add(new ServiceFragment());
        mFragmentList.add(new HelpFragment());
        mFragmentList.add(new SettingsFragment());
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(myFragmentPagerAdapter);

        newsFragment.updateSlidingMenu(mListView,mTitleTextView,mSlidingMenuContentLayout,mDrawerLayout, mSwitchImagesDisplayStyleIV);
    }

    private void initView() {
        mTitleTextView = findViewById(R.id.tv_main_title);
        mListView = findViewById(R.id.lv_main_activity_menu);
        mDrawerLayout = findViewById(R.id.dl_main_activity_drawer);
        mOpenDrawerImageView = findViewById(R.id.iv_main_drawer_open_drawer);
        mCloseDrawerImageView = findViewById(R.id.iv_main_drawer_close_drawer);

        mHomeLayout = findViewById(R.id.ll_main_bottom_bar_home);
        mNewsLayout = findViewById(R.id.ll_main_bottom_bar_news);
        mServiceLayout = findViewById(R.id.ll_main_bottom_bar_service);
        mHelpLayout = findViewById(R.id.ll_main_bottom_bar_help);
        mSettingsLayout = findViewById(R.id.ll_main_bottom_bar_settings);

        mHomeLayout.setOnClickListener(this);
        mNewsLayout.setOnClickListener(this);
        mServiceLayout.setOnClickListener(this);
        mHelpLayout.setOnClickListener(this);
        mSettingsLayout.setOnClickListener(this);

        mHomeButton = findViewById(R.id.ib_main_bottom_bar_home);
        mNewsButton = findViewById(R.id.ib_main_bottom_bar_news);
        mServiceButton = findViewById(R.id.ib_main_bottom_bar_service);
        mHelpButton = findViewById(R.id.ib_main_bottom_bar_help);
        mSettingsButton = findViewById(R.id.ib_main_bottom_bar_settings);

        mHomeTextView = findViewById(R.id.tv_main_bottom_bar_home);
        mNewsTextView = findViewById(R.id.tv_main_bottom_bar_news);
        mServiceTextView = findViewById(R.id.tv_main_bottom_bar_service);
        mHelpTextView = findViewById(R.id.tv_main_bottom_bar_help);
        mSettingsTextView = findViewById(R.id.tv_main_bottom_bar_settings);

        mOpenDrawerImageView.setOnClickListener(this);
        mCloseDrawerImageView.setOnClickListener(this);

        mViewPager = findViewById(R.id.vp_main_activity_content);

        mSlidingMenuContentLayout = findViewById(R.id.ll_sliding_menu_content_fragment);

        mSwitchImagesDisplayStyleIV = findViewById(R.id.iv_main_drawer_switch_images_style);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_drawer_open_drawer: {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            }
            case R.id.iv_main_drawer_close_drawer: {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            }
            case R.id.ll_main_bottom_bar_home: {
                mSlidingMenuContentLayout.setVisibility(View.INVISIBLE);
                resetBottomBarIcons();
                mHomeButton.setImageResource(R.drawable.home_selected);
                mHomeTextView.setTextColor(Color.RED);
                mTitleTextView.setText("主页");
                mViewPager.setCurrentItem(0);
                break;
            }
            case R.id.ll_main_bottom_bar_news: {
                mSlidingMenuContentLayout.setVisibility(View.INVISIBLE);
                jumpToNews();
                break;
            }
            case R.id.ll_main_bottom_bar_service: {
                mSlidingMenuContentLayout.setVisibility(View.INVISIBLE);
                resetBottomBarIcons();
                mServiceButton.setImageResource(R.drawable.service_selected);
                mServiceTextView.setTextColor(Color.RED);
                mTitleTextView.setText("服务");
                mViewPager.setCurrentItem(2);
                break;
            }
            case R.id.ll_main_bottom_bar_help: {
                mSlidingMenuContentLayout.setVisibility(View.INVISIBLE);
                resetBottomBarIcons();
                mHelpButton.setImageResource(R.drawable.help_selected);
                mHelpTextView.setTextColor(Color.RED);
                mTitleTextView.setText("帮助");
                mViewPager.setCurrentItem(3);
                break;
            }
            case R.id.ll_main_bottom_bar_settings: {
                mSlidingMenuContentLayout.setVisibility(View.INVISIBLE);
                resetBottomBarIcons();
                mSettingsButton.setImageResource(R.drawable.settings_selected);
                mSettingsTextView.setTextColor(Color.RED);
                mTitleTextView.setText("设置");
                mViewPager.setCurrentItem(4);
                break;
            }
        }
    }

    private void jumpToNews() {
        resetBottomBarIcons();
        mNewsButton.setImageResource(R.drawable.news_selected);
        mNewsTextView.setTextColor(Color.RED);
        mTitleTextView.setText("新闻");
        mViewPager.setCurrentItem(1);
    }

    /**
     * reset all icons and text to white color
     */
    private void resetBottomBarIcons() {
        mHomeButton.setImageResource(R.drawable.home_normal);
        mNewsButton.setImageResource(R.drawable.news_normal);
        mServiceButton.setImageResource(R.drawable.service_normal);
        mHelpButton.setImageResource(R.drawable.help_normal);
        mSettingsButton.setImageResource(R.drawable.settings_normal);

        mHomeTextView.setTextColor(Color.WHITE);
        mNewsTextView.setTextColor(Color.WHITE);
        mServiceTextView.setTextColor(Color.WHITE);
        mHelpTextView.setTextColor(Color.WHITE);
        mSettingsTextView.setTextColor(Color.WHITE);
    }

    @Override
    public void delivery(boolean turnToNews) {
        if(turnToNews){
            jumpToNews();
        }
    }
}