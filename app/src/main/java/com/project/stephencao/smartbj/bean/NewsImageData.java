package com.project.stephencao.smartbj.bean;

import java.util.ArrayList;

public class NewsImageData {
    public String more;
    public ArrayList<NewsImageDataChildren> news;

    @Override
    public String toString() {
        return "NewsImageData{" +
                "more='" + more + '\'' +
                ", news=" + news +
                '}';
    }
}
