package com.project.stephencao.smartbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.bean.NewsTabDataChildrenNews;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsTabDataChildrenNews> mNews;
    private LayoutInflater inflater;
    private BitmapUtils mBitmapUtils;

    public MyListViewAdapter(Context context, ArrayList<NewsTabDataChildrenNews> mNews) {
        this.context = context;
        this.mNews = mNews;
        inflater = LayoutInflater.from(this.context);
        mBitmapUtils = new BitmapUtils(context);
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
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_news_listview_items,null);
            viewHolder.imageView = convertView.findViewById(R.id.iv_news_listview_img);
            viewHolder.contentTextView = convertView.findViewById(R.id.tv_news_listview_items_title);
            viewHolder.dateTextView = convertView.findViewById(R.id.tv_news_listview_items_date);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsTabDataChildrenNews newsTabDataChildrenNews = mNews.get(position);
        mBitmapUtils.display(viewHolder.imageView,newsTabDataChildrenNews.listimage);
        viewHolder.contentTextView.setText(newsTabDataChildrenNews.title);
        viewHolder.dateTextView.setText(newsTabDataChildrenNews.pubdate);
        if(newsTabDataChildrenNews.hasBeenRead){
            viewHolder.contentTextView.setTextColor(Color.GRAY);
            viewHolder.dateTextView.setTextColor(Color.GRAY);
        }
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView contentTextView, dateTextView;
    }
}
