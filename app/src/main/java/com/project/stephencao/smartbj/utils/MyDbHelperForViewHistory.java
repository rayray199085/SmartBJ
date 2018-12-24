package com.project.stephencao.smartbj.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelperForViewHistory extends SQLiteOpenHelper {

    public MyDbHelperForViewHistory(Context context) {
        this(context, "browseHistory.db", null, 1);
    }

    public MyDbHelperForViewHistory(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table history(_id integer primary key autoincrement,page_id verchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
