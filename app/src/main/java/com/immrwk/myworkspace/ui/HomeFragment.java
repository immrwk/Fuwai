package com.immrwk.myworkspace.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.webkit.WebView;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.adapter.VideoAdapter;
import com.immrwk.myworkspace.bean.VideoModel;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.function.UserFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class HomeFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private List<VideoModel> demandVideos = new ArrayList<>();
    private List<VideoModel> liveVideos = new ArrayList<>();

    private GridView gv_demand;
    private GridView gv_live;
    private GridView gv_history;
    private VideoAdapter adapter;

    private static final String NORMAL_VIDEO = "1";
    private static final int INTERACTION_VIDEO = 2;

    /**
     * 内容类别
     */
    private static final int LIVE_VIDEO = 1;
    private static final int DEMAND_VIDEO = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        //获取直播视频内容
        getLiveVideos();
        //获取点播视频内容
        getDemandVideos();
    }

    private void initViews() {
        gv_demand = (GridView) getView().findViewById(R.id.gv_demand);
        gv_live = (GridView) getView().findViewById(R.id.gv_live);
//        gv_history = (GridView) getView().findViewById(R.id.gv_history);
    }

    private void getLiveVideos() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getLiveVideo(mRequestQueue, 0, AppConfig.user.userId, mHandler);
    }

    private void getDemandVideos() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getDemandVideo(mRequestQueue, NORMAL_VIDEO, 0, AppConfig.user.userId, mHandler);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

    private void setVideoData(JSONArray arr, List<VideoModel> videos, GridView gv, int videoType) {
        JSONObject obj;
        videos.clear();
        try {
            for (int i = 0; i < arr.length(); i++) {
                obj = arr.getJSONObject(i);
                VideoModel vm = new VideoModel();
                vm.setVideoName(obj.getString("videoName"));
                vm.setVideoId(obj.getString("videoId"));
                vm.setVideoType(obj.getString("videoType"));
                vm.setImgurl(obj.getString("imgurl"));
                vm.setClick(obj.getString("click"));
                vm.setVideoInfo(obj.getString("videoInfo"));
                vm.setCreateDate(obj.getString("createDate"));
                if (videoType == DEMAND_VIDEO) {
                    vm.setUrl(obj.getString("url"));
                    vm.setClassName(obj.getString("className"));
                }
                videos.add(vm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new VideoAdapter(getActivity(), videos);
        gv.setAdapter(adapter);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case FunctionTag.DEMANDVIDEO:
                    JSONArray demandArr = (JSONArray) msg.obj;
                    setVideoData(demandArr, demandVideos, gv_demand, DEMAND_VIDEO);
                    break;
                case FunctionTag.LIVEVIDEO:
                    JSONArray liveArr = (JSONArray) msg.obj;
                    setVideoData(liveArr, liveVideos, gv_live, LIVE_VIDEO);
                    break;
                case FunctionTag.ERROR:

                    break;
            }
        }
    };
}
