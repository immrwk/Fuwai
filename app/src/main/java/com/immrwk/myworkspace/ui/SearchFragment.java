package com.immrwk.myworkspace.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
public class SearchFragment extends Fragment {

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getSearchSort() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getSearchSort(mRequestQueue, UserInfo.userId, mHandler);
    }

    private void setSearchBtnSize() {
        EditText editText1 = (EditText) getView().findViewById(R.id.edt_search_input);
        Drawable drawable1 = getResources().getDrawable(R.drawable.search_normal);
        drawable1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
        editText1.setCompoundDrawables(drawable1, null, null, null);//只放左边
    }

    @Override
    public void onStart() {
        super.onStart();
        setSearchBtnSize();
    }

    @Override
    public void onResume() {
        super.onResume();
        getSearchSort();
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
                case FunctionTag.SEARCHSORT:
                    JSONArray jarr = (JSONArray) msg.obj;
                    JSONObject obj;

                    try {
                        obj = jarr.getJSONObject(0);
                        String sortArr = obj.getString("title");
                        Log.i("@@@@@", obj.toString());
                        Log.i("@@@@@", sortArr);
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
