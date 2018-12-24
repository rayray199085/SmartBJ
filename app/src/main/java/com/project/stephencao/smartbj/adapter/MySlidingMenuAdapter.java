package com.project.stephencao.smartbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.bean.SlidingMenuItems;

import java.util.List;

public class MySlidingMenuAdapter extends BaseAdapter {
    private List<SlidingMenuItems> slidingMenuItemsList;
    private Context context;
    private LayoutInflater inflater;

    public MySlidingMenuAdapter(List<SlidingMenuItems> slidingMenuItemsList, Context context) {
        this.slidingMenuItemsList = slidingMenuItemsList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return slidingMenuItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return slidingMenuItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.view_main_activity_list_items,null);
            viewHolder.imageView = convertView.findViewById(R.id.iv_main_activity_menu_items_icon);
            viewHolder.textView = convertView.findViewById(R.id.tv_main_activity_menu_items_names);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SlidingMenuItems items = slidingMenuItemsList.get(position);
        viewHolder.imageView.setImageResource(items.getImageID());
        viewHolder.textView.setText(items.getItemName());
        return convertView;
    }
    class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }
}
