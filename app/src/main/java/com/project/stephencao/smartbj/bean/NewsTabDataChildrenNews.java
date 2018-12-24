package com.project.stephencao.smartbj.bean;

public class NewsTabDataChildrenNews {
    public String id;
    public String title;
    public String listimage;
    public String pubdate;
    public String url;
    public boolean hasBeenRead;

    @Override
    public String toString() {
        return "NewsTabDataChildrenNews{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", listimage='" + listimage + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
