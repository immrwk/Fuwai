package com.immrwk.myworkspace.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.adapter.VideoClassifyAdapter;
import com.immrwk.myworkspace.adapter.VideoListAdapter;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.bean.VideoClassifyModel;
import com.immrwk.myworkspace.function.UserFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class ClassifyFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private GridView gv_videoclassify;
    private List<VideoClassifyModel> videos = new ArrayList<VideoClassifyModel>();
    private VideoClassifyAdapter vfAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classify, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gv_videoclassify = (GridView) getView().findViewById(R.id.gv_videoclassify);
        initEvents();
    }

    private void initEvents() {
        gv_videoclassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), VideoListActivity.class);
                intent.putExtra("tag", FunctionTag.FROM_CLASSIFY);
                intent.putExtra("classifyId", videos.get(position).getClassifyId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoClassify();
    }

    private void getVideoClassify() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getVideoClassify(mRequestQueue, mHandler);
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
                case FunctionTag.VIDEOCLASSIFY:
                    JSONArray jarr = (JSONArray) msg.obj;
                    JSONObject obj;
                    videos.clear();
                    try {
                        for (int i = 0; i < jarr.length(); i++) {
                            obj = jarr.getJSONObject(i);
                            VideoClassifyModel model = new VideoClassifyModel();
                            model.setClassifyId(obj.getString("classifyId"));
                            model.setClassifyName(obj.getString("classifyName"));
                            model.setImgurl(obj.getString("imgurl"));
                            videos.add(model);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    vfAdapter = new VideoClassifyAdapter(getActivity(), videos);
                    gv_videoclassify.setAdapter(vfAdapter);
                    break;
                case FunctionTag.ERROR:
                    break;
            }
        }
    };
}
