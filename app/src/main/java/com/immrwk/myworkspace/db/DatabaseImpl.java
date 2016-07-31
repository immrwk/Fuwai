package com.immrwk.myworkspace.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.webkit.WebView;

import com.immrwk.myworkspace.bean.VideoModel;
import com.immrwk.myworkspace.util.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class DatabaseImpl {
    /**
     * DatabaseImpl实例
     */
    private static DatabaseImpl instance;
    /**
     * SQLiteOpenHelper对象
     */
    private DatabaseHelper helper;
    /**
     * SQLiteDatabase对象
     */
    private SQLiteDatabase db;
    /**
     * Context
     */
    private Context mContext;

    /**
     * 构造方法
     *
     * @param context Context
     */
    private DatabaseImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 获取DatabaseImpl实例
     *
     * @param context Context
     * @return instance
     */
    public static synchronized DatabaseImpl getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseImpl(context);
        }
        return instance;
    }

    /**
     * 打开数据库
     */
    public void open() {
        helper = new DatabaseHelper(mContext);
        db = helper.getWritableDatabase();
    }

    /**
     * 关闭数据库
     */
    public void close() {
        helper.close();
    }

    /**
     * 插入或更新观看记录
     */
    public void insertOrUpdateVideoHistory(VideoModel video) {
        ContentValues values = new ContentValues();
        values.put(TableVideoInfos.VIDEO_ID, video.getVideoId());
        values.put(TableVideoInfos.URL, video.getUrl());
        values.put(TableVideoInfos.IMG_URL, video.getImgurl());
        values.put(TableVideoInfos.NAME, video.getVideoName());
        values.put(TableVideoInfos.CLASS_NAME, video.getClassName());
        values.put(TableVideoInfos.CREATE_DATE, video.getCreateDate());
        values.put(TableVideoInfos.CLICK, video.getClick());

        Cursor cursor = null;
        try {
//            String selection = TableVideoInfos.VIDEO_ID + "=?";
//            String[] selectionArgs = new String[]{video.getVideoId()};
            cursor = db.query(TableVideoInfos.TABLE_NAME, null, null, null, null, null, null, null);

            //插入
            db.insert(TableVideoInfos.TABLE_NAME, null, values);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 查询观看记录
     */
    public List<VideoModel> queryVideoHistory() {
        List<VideoModel> videos = new ArrayList<VideoModel>();
        String selection = "";
        String[] seletion_args = new String[]{};
        Cursor cursor = null;

        try {
            cursor = db.query(TableVideoInfos.TABLE_NAME, null, selection, seletion_args, null, null, TableVideoInfos.ID + " DESC");
            while (cursor != null && cursor.moveToNext()) {
                VideoModel video = new VideoModel();
                video.setVideoName(cursor.getString(cursor.getColumnIndex(TableVideoInfos.NAME)));
                video.setUrl(cursor.getString(cursor.getColumnIndex(TableVideoInfos.URL)));
                video.setImgurl(cursor.getString(cursor.getColumnIndex(TableVideoInfos.IMG_URL)));
                video.setClassName(cursor.getString(cursor.getColumnIndex(TableVideoInfos.CLASS_NAME)));
                video.setCreateDate(cursor.getString(cursor.getColumnIndex(TableVideoInfos.CREATE_DATE)));
                video.setVideoId(cursor.getString(cursor.getColumnIndex(TableVideoInfos.VIDEO_ID)));
                video.setClick(cursor.getString(cursor.getColumnIndex(TableVideoInfos.CLICK)));
                videos.add(video);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videos;
    }

    /**
     * 删除某条记录
     */
    public void deleteVideoHistory(VideoModel video) {
        String whereClause = TableVideoInfos.VIDEO_ID + "=?";
        String[] whereArgs = new String[]{video.getVideoId()};
        db.delete(TableVideoInfos.TABLE_NAME, whereClause, whereArgs);
    }
}
