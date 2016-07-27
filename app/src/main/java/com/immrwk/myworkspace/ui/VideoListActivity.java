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
import com.immrwk.myworkspace.db.DatabaseImpl;
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
    private String classifyId = "";

    private boolean is_first_load = true;

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
                getData(tag);
            }
        });
        loadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
        });

        loadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startVideoPlayActivity(videos.get(position));
            }
        });
    }

    /**
     * 跳转到视频播放界面
     */
    private void startVideoPlayActivity(VideoModel vm) {
        Intent intent = new Intent(VideoListActivity.this, VideoPlayActivity.class);
        intent.putExtra("videoUrl", vm.getUrl());
        intent.putExtra("imgUrl", vm.getImgurl());
        intent.putExtra("videoName", vm.getVideoName());
        intent.putExtra("className", vm.getClassName());
        intent.putExtra("createDate", vm.getCreateDate());
        intent.putExtra("click", vm.getClick());
        intent.putExtra("videoId", vm.getVideoId());
        startActivity(intent);
    }

    private void getData(int tag) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(VideoListActivity.this);
        }
        switch (tag) {
            case FunctionTag.FROM_SEARCH:
                UserFunction.getSearchResult(mRequestQueue, AppConfig.user.userId, pageNow, content, mHandler);
                break;
            case FunctionTag.FROM_CLASSIFY:
            case FunctionTag.FROM_HOME_DEMAND:
                UserFunction.getDemandVideo(mRequestQueue, classifyId, pageNow, AppConfig.user.userId, mHandler);
                break;
            case FunctionTag.FROM_HOME_LIVE:
                UserFunction.getLiveVideo(mRequestQueue, pageNow, AppConfig.user.userId, mHandler);
                break;
            case FunctionTag.FROM_HISTORY:
                if (is_first_load) {
                    getVideoHistory();
                    is_first_load = false;
                }
                break;
            default:
                break;
        }
    }

    private void getVideoHistory() {
        DatabaseImpl db = DatabaseImpl.getInstance(VideoListActivity.this);
        db.open();
        videos = db.queryVideoHistory();
        db.close();

//        if (is_first_load) {
//            adapter = new VideoListAdapter(VideoListActivity.this, videos);
//            loadListView.setAdapter(adapter);
//            is_first_load = false;
//        } else {
//            adapter.setData(videos);
//            adapter.notifyDataSetChanged();
//        }

        adapter = new VideoListAdapter(VideoListActivity.this, videos);
        loadListView.setAdapter(adapter);
        loadListView.onLoadFinish(LoadListView.LOADMORE_SUCCESS);
    }

    private void initData() {
        getData(tag);
    }

    private void initViews() {
        loadListView = (LoadListView) findViewById(R.id.loadlistview);
        fl_nodata = (FrameLayout) findViewById(R.id.fl_nodata);
        iv_back = (ImageView) findViewById(R.id.iv_back);
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
            case FunctionTag.FROM_HOME_DEMAND:
                classifyId = b.getString("classifyId");
                break;
            case FunctionTag.FROM_HOME_LIVE:

                break;
            case FunctionTag.FROM_HISTORY:
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
                if (result.getJSONObject(0).has("success")) {
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
                vm.setVideoType(obj.getString("videoType"));
                vm.setImgurl(obj.getString("imgurl"));
                vm.setClick(obj.getString("click"));
                vm.setVideoInfo(obj.getString("videoInfo"));
                vm.setCreateDate(obj.getString("createDate"));
                if (tag != FunctionTag.FROM_HOME_LIVE) {
                    if (tag == FunctionTag.FROM_SEARCH) {
                        vm.setUrl(obj.getString("url"));
                    } else {
                        vm.setUrl("http://106.120.203.85/install/videoupload" + obj.getString("url"));
                    }
                    vm.setClassName(obj.getString("className"));
                }
                videos.add(vm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadListView.onLoadFinish(LoadListView.LOADMORE_SUCCESS);
        pageNow += 1;

        if (is_first_load) {
            adapter = new VideoListAdapter(VideoListActivity.this, videos);
            loadListView.setAdapter(adapter);
            is_first_load = false;
        } else {
            adapter.setData(videos);
            adapter.notifyDataSetChanged();
        }
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
                case FunctionTag.LIVEVIDEO:
                    JSONArray liveResult = (JSONArray) msg.obj;
                    refreshData(liveResult);
                    UserFunction.getVideoUrl(mRequestQueue, videos.get(0).getVideoId(), mHandler);
                    break;
                case FunctionTag.GETVIDEOURL:
                    JSONArray urlArr = (JSONArray) msg.obj;

                    if (videos.size() <= 0) {
                        return;
                    }
                    try {
                        videos.get(0).setUrl(urlArr.getJSONObject(0).getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FunctionTag.ERROR:
                    fl_nodata.setVisibility(View.VISIBLE);
                    loadListView.setVisibility(View.GONE);
                    break;
            }
        }
    };

}
