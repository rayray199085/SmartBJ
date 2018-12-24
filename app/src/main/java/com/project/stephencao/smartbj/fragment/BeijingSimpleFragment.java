package com.project.stephencao.smartbj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.activity.WebPageActivity;
import com.project.stephencao.smartbj.adapter.MyListViewAdapter;
import com.project.stephencao.smartbj.adapter.MyNewsPicsAdapter;
import com.project.stephencao.smartbj.bean.NewsTab;
import com.project.stephencao.smartbj.bean.NewsTabDataChildren;
import com.project.stephencao.smartbj.bean.NewsTabDataChildrenNews;
import com.project.stephencao.smartbj.engine.BrowseHistoryDao;
import com.project.stephencao.smartbj.utils.CacheUtil;
import com.project.stephencao.smartbj.utils.ConstantValues;
import com.project.stephencao.smartbj.view.RefreshListView;

import java.util.*;

public class BeijingSimpleFragment extends SimpleFragment implements RefreshListView.OnRefreshListener {
    private View mView;
    private ViewPager mViewPager;
    private TextView mTextView;
    private static String mUrl;
    private RefreshListView mListView;
    private View mHeaderView;
    private String mLoadMore;
    private Timer mTimer;
    private Set<String> mRecordSet;
    private boolean hasRefreshNewsList = false;
    private MyListViewAdapter myListViewAdapter;
    private ImageView mImageView1, mImageView2, mImageView3, mImageView4;
    private ArrayList<NewsTabDataChildren> mTopnews;
    private int mCurrentPage = 0;
    private ArrayList<NewsTabDataChildrenNews> mNews = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantValues.INFINITY_RECYCLE_DISPLAY_VIEWPAGER: {
                    mViewPager.setCurrentItem(mCurrentPage);
                    mCurrentPage++;
                    break;
                }
            }
        }
    };

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
        mView = inflater.inflate(R.layout.view_beijing, container, false);
        mRecordSet = new HashSet<>();
        initViews();
        return mView;
    }

    private void initViews() {
        mListView = mView.findViewById(R.id.lv_news_beijing_content);
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.view_listview_header, null);
        mImageView1 = mHeaderView.findViewById(R.id.iv_news_beigjing_img1);
        mImageView2 = mHeaderView.findViewById(R.id.iv_news_beigjing_img2);
        mImageView3 = mHeaderView.findViewById(R.id.iv_news_beigjing_img3);
        mImageView4 = mHeaderView.findViewById(R.id.iv_news_beigjing_img4);
        resetAllIndicators();
        mImageView1.setBackgroundResource(R.drawable.on);
        mViewPager = mHeaderView.findViewById(R.id.vp_news_beijing_fragment_pager);
        mTextView = mHeaderView.findViewById(R.id.tv_news_beijing_fragment_title);
        setUrlAddress();
        if (mTopnews != null) {
            prepareViewPager();
        }
        prepareListView();
        displayHistoryRecords();
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRecordSet.add(mNews.get(position - 2).id)) {
                    BrowseHistoryDao.insertARecordToDB(getContext(), mNews.get(position - 2).id);
                    mNews.get(position - 2).hasBeenRead = true;
                    myListViewAdapter.notifyDataSetChanged();
                }
                Intent intent = new Intent(getContext(), WebPageActivity.class);
                intent.putExtra(ConstantValues.WEB_PAGE_URL, mNews.get(position - 2).url);
                startActivity(intent);
            }
        });

        mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mCurrentPage > mTopnews.size() - 1) {
                    mCurrentPage = 0;
                }
                mHandler.sendEmptyMessage(ConstantValues.INFINITY_RECYCLE_DISPLAY_VIEWPAGER);
            }
        };
        mTimer.schedule(mTimerTask, 0, 3000);
    }

    @Override
    public void onPause() {
        mTimer.cancel();
        super.onPause();
    }

    private void displayHistoryRecords() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mRecordSet.addAll(BrowseHistoryDao.queryRecordsFromDB(getContext()));
                for (NewsTabDataChildrenNews news : mNews) {
                    if (mRecordSet.contains(news.id)) {
                        news.hasBeenRead = true;
                    }
                }
            }
        }).start();
    }

    private void prepareListView() {
        mListView.addHeaderView(mHeaderView);
        myListViewAdapter = new MyListViewAdapter(getContext(), mNews);
        mListView.setAdapter(myListViewAdapter);
    }

    private void resetAllIndicators() {
        mImageView1.setBackgroundResource(R.drawable.off);
        mImageView2.setBackgroundResource(R.drawable.off);
        mImageView3.setBackgroundResource(R.drawable.off);
        mImageView4.setBackgroundResource(R.drawable.off);
    }

    private void prepareViewPager() {
        MyNewsPicsAdapter myNewsPicsAdapter = new MyNewsPicsAdapter(getContext(), mTopnews);
        mViewPager.setAdapter(myNewsPicsAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTextView.setText(mTopnews.get(i).title);
                resetAllIndicators();
                switch (i) {
                    case 0: {
                        mImageView1.setBackgroundResource(R.drawable.on);
                        break;
                    }
                    case 1: {
                        mImageView2.setBackgroundResource(R.drawable.on);
                        break;
                    }
                    case 2: {
                        mImageView3.setBackgroundResource(R.drawable.on);
                        break;
                    }
                    case 3: {
                        mImageView4.setBackgroundResource(R.drawable.on);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public static BeijingSimpleFragment newInstance(String url) {
        mUrl = url;
        return new BeijingSimpleFragment();
    }

    public void setUrlAddress() {
        String mJson = CacheUtil.withdrawCache(getContext(), ConstantValues.SERVER_URL + mUrl);
        if (mJson != null && !"".equals(mJson)) {
            processData(mJson);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,
                ConstantValues.SERVER_URL + mUrl, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        processData(result);
                        CacheUtil.storeCache(getContext(), ConstantValues.SERVER_URL + mUrl, result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                    }
                });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        NewsTab newsTab = gson.fromJson(result, NewsTab.class);
        mTopnews = newsTab.data.topnews;
        if (!hasRefreshNewsList) {
            mNews.addAll(newsTab.data.news);
            hasRefreshNewsList = true;
        } else {
            mNews.clear();
            mNews.addAll(newsTab.data.news);
        }
        mLoadMore = newsTab.data.more;
    }

    @Override
    public void doRefresh() {
        getDataFromServer();
    }

    @Override
    public int getMoreData() {
        if (mLoadMore != null && !"".equals(mLoadMore)) {
            loadMoreDataFromServer();
            return 0;
        } else {
            Toast.makeText(getContext(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
            return ConstantValues.LIST_VIEW_NO_MORE_DATA;
        }
    }

    private void loadMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET,
                ConstantValues.SERVER_URL + mLoadMore, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Gson gson = new Gson();
                        NewsTab newsTab = gson.fromJson(result, NewsTab.class);
                        ArrayList<NewsTabDataChildrenNews> moreNews = newsTab.data.news;
                        mLoadMore = newsTab.data.more;
                        mNews.addAll(moreNews);
                        myListViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
    }
}
