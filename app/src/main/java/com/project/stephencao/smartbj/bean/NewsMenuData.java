package com.project.stephencao.smartbj.bean;

import java.util.ArrayList;

public class NewsMenuData {
    public String id;
    public String title;
    public int type;
    // tab data
    public ArrayList<NewsMenuDataChildren> children;

    @Override
    public String toString() {
        return "NewsMenuData{" +
                "title='" + title + '\'' +
                ", children=" + children +
                '}';
    }
}
