package com.project.stephencao.smartbj.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.adapter.MySlidingMenuAdapter;
import com.project.stephencao.smartbj.bean.NewsMenu;
import com.project.stephencao.smartbj.bean.NewsMenuData;
import com.project.stephencao.smartbj.bean.NewsMenuDataChildren;
import com.project.stephencao.smartbj.bean.SlidingMenuItems;
import com.project.stephencao.smartbj.utils.CacheUtil;
import com.project.stephencao.smartbj.utils.ConstantValues;
import com.project.stephencao.smartbj.utils.RotateDownwardPagerTransformer;
import com.project.stephencao.smartbj.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private View mView;
    private List<String> mMenuItemsList;
    private List<SlidingMenuItems> mSlidingMenuItemsList;
    private ListView mListView;
    private SendData mSendData;
    private TextView mTitleTextView;
    private LinearLayout mSlidingMenuContentLayout;
    private MySlidingMenuAdapter mySlidingMenuAdapter;
    private NewsMenu mNewsMenu;
    private DrawerLayout mDrawerLayout;
    private ViewPagerIndicator mViewPagerIndicator;
    private ViewPager mViewPager;
    private List<SimpleFragment> mFragmentList;
    private List<NewsMenuDataChildren> mNewsMenuDataChildrenList = new ArrayList<>();
    private boolean mShouldAddData = false;
    private ImageView mSwitchImagesDisplayStyleIV;


    public void updateSlidingMenu(ListView mListView, TextView mTitleTextView, LinearLayout mSlidingMenuContentLayout,
                                  DrawerLayout mDrawerLayout, ImageView mSwitchImagesDisplayStyleIV) {
        this.mListView = mListView;
        this.mTitleTextView = mTitleTextView;
        this.mSlidingMenuContentLayout = mSlidingMenuContentLayout;
        this.mDrawerLayout = mDrawerLayout;
        this.mSwitchImagesDisplayStyleIV = mSwitchImagesDisplayStyleIV;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            return mView;
        }
        mView = inflater.inflate(R.layout.view_news, container, false);
        initData();
        initView();
        return mView;
    }

    private void initView() {
        mFragmentList = new ArrayList<>();
        mViewPagerIndicator = mView.findViewById(R.id.vi_news_fragment_title);
        mViewPager = mView.findViewById(R.id.vp_news_fragment_content);
        /**
         * get news fragments title
         */
        List<String> mTitles = new ArrayList<>();
        List<String> mUrls = new ArrayList<>();

        for (NewsMenuDataChildren children : mNewsMenuDataChildrenList) {
            mTitles.add(children.title);
            mUrls.add(children.url);

        }
        /**
         *
         */
        if (mUrls.size()> 0) {
            BeijingSimpleFragment beijingSimpleFragment = BeijingSimpleFragment.newInstance(mUrls.get(0));
            mFragmentList.add(beijingSimpleFragment);
        }

        for (int i = 1; i < mTitles.size();i++) {
            SimpleFragment fragment = SimpleFragment.newInstance(mTitles.get(i));
            mFragmentList.add(fragment);
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentList.get(i);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        mViewPagerIndicator.setTabVisibleNum(3);
        mViewPagerIndicator.setTabItemTitles(mTitles);
        mViewPager.setPageTransformer(true, new RotateDownwardPagerTransformer());
        mViewPager.setAdapter(adapter);
        mViewPagerIndicator.setViewPager(mViewPager, 0);
    }

    private void initData() {
        String mJson = CacheUtil.withdrawCache(getContext(), ConstantValues.JSON_CATEGORY_URL);
        if (mJson != null && !"".equals(mJson)) {
            processData(mJson);
        }
        getDataFromServer();
    }

    private void initSlidingMenu() {
        mMenuItemsList = new ArrayList<>();
        for (NewsMenuData dataList : mNewsMenu.data) {
            mMenuItemsList.add(dataList.title);
            if ("新闻".equals(dataList.title) && !mShouldAddData) {
                for (int i = 0; i < dataList.children.size(); i++) {
                    mNewsMenuDataChildrenList.add(dataList.children.get(i));
                    mShouldAddData = true;
                }
            }
        }
        mSlidingMenuItemsList = new ArrayList<>();
        for (int i = 0; i < mMenuItemsList.size(); i++) {
            SlidingMenuItems slidingMenuItems = new SlidingMenuItems();
            slidingMenuItems.setImageID(R.drawable.sliding_menu_items_arrow);
            slidingMenuItems.setItemName(mMenuItemsList.get(i));
            mSlidingMenuItemsList.add(slidingMenuItems);
        }

        mySlidingMenuAdapter = new MySlidingMenuAdapter(mSlidingMenuItemsList, getContext());
        mListView.setAdapter(mySlidingMenuAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (position) {
                    case ConstantValues.SLIDING_MENU_NEWS: {
                        mSlidingMenuContentLayout.setVisibility(View.INVISIBLE);
                        mSwitchImagesDisplayStyleIV.setVisibility(View.INVISIBLE);
                        mSendData.delivery(true);
                        break;
                    }
                    case ConstantValues.SLIDING_MENU_TOPICS: {
                        mSlidingMenuContentLayout.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.ll_sliding_menu_content_fragment, new TopicsFragment());
                        mSwitchImagesDisplayStyleIV.setVisibility(View.INVISIBLE);
                        mTitleTextView.setText(mMenuItemsList.get(position));
                        break;
                    }
                    case ConstantValues.SLIDING_MENU_IMAGES: {
                        mSlidingMenuContentLayout.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.ll_sliding_menu_content_fragment, ImagesFragment.newInstance(mSwitchImagesDisplayStyleIV));
                        mTitleTextView.setText(mMenuItemsList.get(position));
                        break;
                    }
                    case ConstantValues.SLIDING_MENU_INTERACTION: {
                        mSlidingMenuContentLayout.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.ll_sliding_menu_content_fragment, new InteractionFragment());
                        mSwitchImagesDisplayStyleIV.setVisibility(View.INVISIBLE);
                        mTitleTextView.setText(mMenuItemsList.get(position));
                        break;
                    }
                }
                transaction.commit();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,
                ConstantValues.JSON_CATEGORY_URL, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        processData(result);
                        CacheUtil.storeCache(getContext(), ConstantValues.JSON_CATEGORY_URL, result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        mNewsMenu = gson.fromJson(result, NewsMenu.class);
        if (mNewsMenu != null) {
            initSlidingMenu();
        }
    }

    public interface SendData {
        public void delivery(boolean turnToNews);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSendData = (SendData) getActivity();
    }

}
