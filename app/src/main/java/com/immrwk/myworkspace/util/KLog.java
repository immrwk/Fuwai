package com.immrwk.myworkspace.util;

import android.util.Log;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class KLog {
    private final static String TAG = "FUWAI";

    public static void d(String content) {
        Log.d(TAG, content);
    }

    public static void i(String content) {
        Log.i(TAG, content);
    }

    public static void e(String content) {
        Log.e(TAG, content);
    }

    public static void d(String tag, String content) {
        Log.d(tag, content);
    }

    public static void i(String tag, String content) {
        Log.i(tag, content);
    }

    public static void e(String tag, String content) {
        Log.e(tag, content);
    }
}
