package com.project.stephencao.smartbj.bean;

import java.util.ArrayList;

public class NewsMenu {
    /**
     * names should be the same as them in json file
     * class name can be self-defined
     */
    public int retcode;
    public ArrayList<NewsMenuData> data;
    public ArrayList<String> extend;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }
}

