package com.project.stephencao.smartbj.utils;

import android.content.Context;

public class CacheUtil {
    public static void storeCache(Context context, String url, String json) {
        SharedPreferenceUtil.recordString(context, url, json);
    }

    public static String withdrawCache(Context context, String url) {
        return SharedPreferenceUtil.getString(context, url);
    }
}
