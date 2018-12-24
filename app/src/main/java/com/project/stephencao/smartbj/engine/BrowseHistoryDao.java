package com.project.stephencao.smartbj.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.project.stephencao.smartbj.utils.MyDbHelperForViewHistory;

import java.util.ArrayList;
import java.util.List;

public class BrowseHistoryDao {
    public static void insertARecordToDB(Context context, String pageID) {
        MyDbHelperForViewHistory myDbHelperForViewHistory = new MyDbHelperForViewHistory(context);
        SQLiteDatabase sqLiteDatabase = myDbHelperForViewHistory.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("page_id", pageID);
        sqLiteDatabase.insert("history", null, contentValues);
        sqLiteDatabase.close();
        myDbHelperForViewHistory.close();
    }

    public static List<String> queryRecordsFromDB(Context context) {
        List<String> historyRecordList = new ArrayList<>();
        MyDbHelperForViewHistory myDbHelperForViewHistory = new MyDbHelperForViewHistory(context);
        SQLiteDatabase sqLiteDatabase = myDbHelperForViewHistory.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("history", new String[]{"page_id"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            historyRecordList.add(cursor.getString(0));
        }
        sqLiteDatabase.close();
        myDbHelperForViewHistory.close();
        return historyRecordList;
    }
}
