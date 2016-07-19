package com.immrwk.myworkspace.function;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.immrwk.myworkspace.api.FuwaiAPI;
import com.immrwk.myworkspace.util.KLog;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class UserFunction {

    public static void updateVersion(RequestQueue mRequestQueue, String versionCode, final Handler handler) {
        final String url = FuwaiAPI.UpdateVersionUrl + versionCode;

        JsonArrayRequest rep = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.UPDATE;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    public static void login(RequestQueue mRequestQueue, String account, String password, final Handler handler) {
        String loginUrl = FuwaiAPI.LoginUrl + "?userName=" + account + "&password=" + password;

        JsonArrayRequest rep = new JsonArrayRequest(loginUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.LOGIN;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    public static void getSearchSort(RequestQueue mRequestQueue, String userId, final Handler handler) {
        String searchSortUrl = FuwaiAPI.SearchSortUrl + "?userId=" + userId;

        JsonArrayRequest rep = new JsonArrayRequest(searchSortUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.SEARCHSORT;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    public static void getVideoClassify(RequestQueue mRequestQueue, final Handler handler) {
        String videoClassifyUrl = FuwaiAPI.VideoClassify;
        JsonArrayRequest rep = new JsonArrayRequest(videoClassifyUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.VIDEOCLASSIFY;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    /**
     * 搜索接口
     *
     * @param mRequestQueue
     * @param userId
     * @param pageNow
     * @param title
     * @param handler
     */
    public static void getSearchResult(RequestQueue mRequestQueue, String userId, int pageNow, String title, final Handler handler) {
        String searchUrl = FuwaiAPI.SearchUrl + "?title=" + title + "&userId=" + userId + "&pageNow=" + pageNow;
        JsonArrayRequest rep = new JsonArrayRequest(searchUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.SEARCHRESULT;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    /**
     * 获取点播你内容
     *
     * @param mRequestQueue
     * @param classifyId
     * @param pageNow
     * @param userId
     * @param handler
     */
    public static void getDemandVideo(RequestQueue mRequestQueue, String classifyId, int pageNow, String userId, final Handler handler) {
        String demandVideoUrl = FuwaiAPI.DemandVideoUrl + "?classifyId=" + classifyId + "&pageNow=" + pageNow + "&userId=" + userId;
        JsonArrayRequest rep = new JsonArrayRequest(demandVideoUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.DEMANDVIDEO;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    /**
     * 获取直播内容
     *
     * @param mRequestQueue
     * @param pageNow
     * @param userId
     * @param handler
     */
    public static void getLiveVideo(RequestQueue mRequestQueue, int pageNow, String userId, final Handler handler) {
        String liveVideoUrl = FuwaiAPI.LiveVideoUrl + "?pageNow=" + pageNow + "&userId=" + userId;
        JsonArrayRequest rep = new JsonArrayRequest(liveVideoUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.LIVEVIDEO;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    /**
     * 获取推荐视频
     *
     * @param mRequestQueue
     * @param userId
     * @param handler
     */
    public static void getRecommendVideo(RequestQueue mRequestQueue, String userId, final Handler handler) {
        String recommendVideoUrl = FuwaiAPI.RecommendVideoUrl + "?userId=" + userId;
        JsonArrayRequest rep = new JsonArrayRequest(recommendVideoUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.RECOMMENDVIDEO;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

    /**
     * 根据视频id获取视频地址
     *
     * @param mRequestQueue
     * @param videoId
     * @param handler
     */


    public static void getVideoUrl(RequestQueue mRequestQueue, String videoId, final Handler handler) {
        String getVideoUrl = FuwaiAPI.GetVideoUrl + "?videoId=" + videoId;
        JsonArrayRequest rep = new JsonArrayRequest(getVideoUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Message msg = Message.obtain();
                msg.obj = jsonArray;
                msg.what = FunctionTag.GETVIDEOURL;
                handler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error!!", volleyError.toString());
            }
        });
        mRequestQueue.add(rep);
        mRequestQueue.start();
    }

}
