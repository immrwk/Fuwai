package com.immrwk.myworkspace.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.CaptioningManager;
import android.widget.GridView;

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
    private List<VideoModel> videos = new ArrayList<>();
    private GridView gv_demand;
    private GridView gv_live;
    private GridView gv_history;
    private VideoAdapter adapter;

    private static final int NORMAL_VIDEO = 1;
    private static final int INTERACTION_VIDEO = 2;

    enum VideosType {
        NONE,
        NORMAL,
        INTERACTION
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
//        return inflater.inflate(R.layout.item_video, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        getDemandVideos();
    }

    private void initViews() {
        gv_demand = (GridView) getView().findViewById(R.id.gv_demand);
//        gv_live = (GridView) getView().findViewById(R.id.gv_live);
//        gv_history = (GridView) getView().findViewById(R.id.gv_history);
    }

    public void getDemandVideos() {
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case FunctionTag.DEMANDVIDEO:
                    JSONArray jarr = (JSONArray) msg.obj;
                    JSONObject obj;
                    videos.clear();
                    try {
                        for (int i = 0; i < jarr.length(); i++) {
                            obj = jarr.getJSONObject(i);
                            VideoModel vm = new VideoModel();
                            vm.setVideoName(obj.getString("className"));
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

                    adapter = new VideoAdapter(getActivity(), videos);
                    gv_demand.setAdapter(adapter);
                    break;
            }
        }
    };
}
