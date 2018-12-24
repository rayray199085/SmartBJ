package com.project.stephencao.smartbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceUtil {
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void initSPfile(Context context) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfo", MODE_PRIVATE);
    }

    public static void recordBoolean(Context context, String key, Boolean flag) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfo", MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putBoolean(key, flag);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfo", MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }

    public static void recordString(Context context, String key, String value) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfo", MODE_PRIVATE);
        if (!"".equals(value)) {
            editor = mSharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getString(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfo", MODE_PRIVATE);
        return mSharedPreferences.getString(key, "");
    }

    public static void remove(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfo", MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
