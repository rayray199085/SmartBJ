package com.project.stephencao.smartbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.bean.NewsImageDataChildren;

import java.util.ArrayList;

public class MyImagesListViewAdapter extends BaseAdapter {
    private ArrayList<NewsImageDataChildren> mNews;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyImagesListViewAdapter(Context mContext, ArrayList<NewsImageDataChildren> mNews) {
        this.mNews = mNews;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mNews.size();
    }

    @Override
    public Object getItem(int position) {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.view_image_list_view_item, null);
            viewHolder.imageView = convertView.findViewById(R.id.iv_news_images_list_view_item_img);
            viewHolder.textView = convertView.findViewById(R.id.tv_news_images_list_view_item_title);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsImageDataChildren newsImageDataChildren = mNews.get(position);
        BitmapUtils bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.display(viewHolder.imageView,newsImageDataChildren.largeimage);
        viewHolder.textView.setText(newsImageDataChildren.title);
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
