package com.project.stephencao.smartbj.bean;

import java.util.ArrayList;

public class NewsTabData {
    public String more;
    public ArrayList<NewsTabDataChildren> topnews;
    public ArrayList<NewsTabDataChildrenNews> news;

    @Override
    public String toString() {
        return "NewsTabData{" +
                "more='" + more + '\'' +
                ", topnews=" + topnews +
                ", news=" + news +
                '}';
    }
}
