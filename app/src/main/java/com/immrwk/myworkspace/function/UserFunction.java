package com.immrwk.myworkspace.function;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.immrwk.myworkspace.api.FunctionTag;
import com.immrwk.myworkspace.api.FuwaiAPI;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class UserFunction {

    public static void updateVersion(RequestQueue mRequestQueue, String versionCode, final Handler mHandler){
        final String url = FuwaiAPI.UpdateVersionUrl+versionCode;

        JsonArrayRequest rep = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.UPDATE;
                mHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!",volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }
}
