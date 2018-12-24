package com.project.stephencao.smartbj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.lidroid.xutils.BitmapUtils;
import com.project.stephencao.smartbj.bean.NewsTabDataChildren;

import java.util.ArrayList;
import java.util.List;

public class MyNewsPicsAdapter extends PagerAdapter {
    private Context context;
    private List<NewsTabDataChildren> mTopNews;
    private BitmapUtils mBitmapUtils;


    public MyNewsPicsAdapter(Context context, ArrayList<NewsTabDataChildren> mTopNews) {
        this.context = context;
        this.mTopNews = mTopNews;
        mBitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return mTopNews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        NewsTabDataChildren children = mTopNews.get(position);
        String topImage = children.topimage;
        mBitmapUtils.display(imageView,topImage);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
