package com.immrwk.myworkspace.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "fuwai.db";
    /**
     * 数据库版本
     */
    private static final int VERSION = 1;

    /**
     * 构造方法
     *
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            String str_sql_videoinfos = "CREATE TABLE " + TableVideoInfos.TABLE_NAME + "(" + TableVideoInfos.ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + TableVideoInfos.VIDEO_ID + " text," + TableVideoInfos.NAME + " text,"
                    + TableVideoInfos.URL + " text," + TableVideoInfos.IMG_URL + " text," + TableVideoInfos.CLASS_NAME + " text,"
                    + TableVideoInfos.CLICK + " text," + TableVideoInfos.CREATE_DATE + " text);";

            db.execSQL(str_sql_videoinfos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
