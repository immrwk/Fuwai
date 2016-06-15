package com.immrwk.myworkspace.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.UserInfo;
import com.immrwk.myworkspace.api.FunctionTag;
import com.immrwk.myworkspace.function.UserFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class ClassifyFragment extends Fragment {

    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classify, container, false);
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
        Log.i("wang", "############");
        UserFunction.getVideoClassify(mRequestQueue, mHandler);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FunctionTag.VIDEOCLASSIFY:
                    JSONArray jarr = (JSONArray) msg.obj;
                    JSONObject obj;

                    try {

                        for (int i = 0; i < jarr.length(); i++) {
                            obj = jarr.getJSONObject(i);
                            Log.i("wang", jarr.length() + "!!!" + i + "@@@" + obj.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FunctionTag.ERROR:
                    break;
            }
        }
    };
}
