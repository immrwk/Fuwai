package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.adapter.VideoAdapter;
import com.immrwk.myworkspace.adapter.VideoListAdapter;
import com.immrwk.myworkspace.bean.User;
import com.immrwk.myworkspace.bean.VideoClassifyModel;
import com.immrwk.myworkspace.bean.VideoModel;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.function.UserFunction;
import com.immrwk.myworkspace.util.KLog;
import com.immrwk.myworkspace.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class VideoListActivity extends Activity {

    private LoadListView loadListView;
    private List<VideoModel> videos = new ArrayList<>();
    private VideoListAdapter adapter;
    private FrameLayout fl_nodata;
    private ImageView iv_back;

    private RequestQueue mRequestQueue;
    private int pageNow = 1;

    private int tag = 0;
    /**
     * 搜索内容
     */
    private String content;

    /**
     * classifyid  视频分类ID
     */
    private String classifyid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        initViews();
        initEvents();
        getIncomeData();
        initData();
    }

    private void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadListView.setDataListener(new LoadListView.DataListener() {
            @Override
            public void onloadMoreData(int page) {
                UserFunction.getSearchResult(mRequestQueue, AppConfig.user.userId, pageNow, content, mHandler);
            }
        });
        loadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });
    }

    private void initData() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(VideoListActivity.this);
        }
//        adapter = new VideoListAdapter(VideoListActivity.this, videos);
//        loadListView.setAdapter(adapter);
        switch (tag) {
            case FunctionTag.FROM_SEARCH:
                UserFunction.getSearchResult(mRequestQueue, AppConfig.user.userId, pageNow, content, mHandler);
                break;
            case FunctionTag.FROM_CLASSIFY:
                UserFunction.getDemandVideo(mRequestQueue, classifyid, pageNow, AppConfig.user.userId, mHandler);
                break;
            default:
                break;
        }
    }

    private void initViews() {
        loadListView = (LoadListView) findViewById(R.id.loadlistview);
        fl_nodata = (FrameLayout) findViewById(R.id.fl_nodata);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        loadListView.setFooterDividersEnabled(false);
    }

    /**
     * 获取上个界面跳转传来的数据
     */
    public void getIncomeData() {
        Bundle b = getIntent().getExtras();
        tag = b.getInt("tag");
        switch (tag) {
            case FunctionTag.FROM_SEARCH:
                content = b.getString("content");
                break;
            case FunctionTag.FROM_CLASSIFY:
                classifyid = b.getString("classifyid");
                break;
            default:
                break;
        }
    }

    /**
     * 更新listview数据
     *
     * @param result
     */
    private void refreshData(JSONArray result) {


        try {
            if (result.length() == 1) {
                if (result.getJSONObject(0).getString("success").equals("false")) {
                    if (pageNow == 1) {
                        fl_nodata.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                    } else {
                        loadListView.onLoadFinish(LoadListView.NOMORE_DATA);
                    }
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            for (int i = 0; i < result.length(); i++) {
                JSONObject obj = result.getJSONObject(i);
                VideoModel vm = new VideoModel();
                vm.setVideoName(obj.getString("videoName"));
                vm.setVideoId(obj.getString("videoId"));
                vm.setClassName(obj.getString("className"));
                vm.setVideoType(obj.getString("videoType"));
                vm.setImgurl(obj.getString("imgurl"));
                vm.setUrl(obj.getString("url"));
                vm.setClick(obj.getString("click"));
                vm.setVideoInfo(obj.getString("videoInfo"));
                vm.setCreateDate(obj.getString("createDate"));
                videos.add(vm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadListView.onLoadFinish(LoadListView.LOADMORE_SUCCESS);
        pageNow += 1;
//        adapter.setData(videos);
//        adapter.notifyDataSetChanged();
        adapter = new VideoListAdapter(VideoListActivity.this, videos);
        loadListView.setAdapter(adapter);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FunctionTag.SEARCHRESULT:
                    JSONArray searchResult = (JSONArray) msg.obj;
                    refreshData(searchResult);
                    break;
                case FunctionTag.DEMANDVIDEO:
                    JSONArray dvResult = (JSONArray) msg.obj;
                    refreshData(dvResult);
                    break;
                case FunctionTag.ERROR:
                    break;
            }
        }
    };

}
