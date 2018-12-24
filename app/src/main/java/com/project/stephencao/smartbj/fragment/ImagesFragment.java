package com.project.stephencao.smartbj.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.adapter.MyImagesGridViewAdapter;
import com.project.stephencao.smartbj.adapter.MyImagesListViewAdapter;
import com.project.stephencao.smartbj.bean.NewsImage;
import com.project.stephencao.smartbj.bean.NewsImageDataChildren;
import com.project.stephencao.smartbj.utils.CacheUtil;
import com.project.stephencao.smartbj.utils.ConstantValues;

import java.util.ArrayList;

public class ImagesFragment extends Fragment implements View.OnClickListener {
    private static ImageView mSwitchImagesDisplayStyleIV;
    private ListView mListView;
    private GridView mGridView;
    private View mView;
    private boolean mISDisplayingListView = true;
    private ArrayList<NewsImageDataChildren> mNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.view_images, container, false);
        initView();
        initData();
        initListViewData();
        initGridViewData();
        return mView;
    }

    private void initGridViewData() {
        MyImagesGridViewAdapter myImagesGridViewAdapter = new MyImagesGridViewAdapter(getContext(),mNews);
        mGridView.setAdapter(myImagesGridViewAdapter);
    }

    private void initListViewData() {
        MyImagesListViewAdapter myImagesListViewAdapter = new MyImagesListViewAdapter(getContext(),mNews);
        mListView.setAdapter(myImagesListViewAdapter);
    }

    private void initData() {
        setUrlAddress();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, ConstantValues.IMAGES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);
                CacheUtil.storeCache(getContext(), ConstantValues.IMAGES_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
    public void setUrlAddress() {
        String mJson = CacheUtil.withdrawCache(getContext(), ConstantValues.IMAGES_URL);
        if (mJson != null && !"".equals(mJson)) {
            processData(mJson);
        }
        getDataFromServer();
    }

    private void processData(String result) {
        Gson gson = new Gson();
        NewsImage newsImage = gson.fromJson(result, NewsImage.class);
        mNews = newsImage.data.news;
    }

    private void initView() {
        if (mSwitchImagesDisplayStyleIV != null) {
            mSwitchImagesDisplayStyleIV.setVisibility(View.VISIBLE);
            mSwitchImagesDisplayStyleIV.setOnClickListener(this);
        }
        mListView = mView.findViewById(R.id.lv_news_images_list_content);
        mGridView = mView.findViewById(R.id.gv_news_images_grid_content);
    }

    public static ImagesFragment newInstance(ImageView imageView) {
        ImagesFragment imagesFragment = new ImagesFragment();
        mSwitchImagesDisplayStyleIV = imageView;
        return imagesFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_drawer_switch_images_style: {
                if(mISDisplayingListView){
                    mListView.setVisibility(View.INVISIBLE);
                    mGridView.setVisibility(View.VISIBLE);
                    mISDisplayingListView = false;
                }
                else {
                    mListView.setVisibility(View.VISIBLE);
                    mGridView.setVisibility(View.INVISIBLE);
                    mISDisplayingListView = true;
                }
                break;
            }
        }
    }

    @Override
    public void onPause() {
        mSwitchImagesDisplayStyleIV.setVisibility(View.INVISIBLE);
        super.onPause();
    }
}
