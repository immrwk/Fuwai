package com.immrwk.myworkspace.ui;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.function.UserFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class SearchFragment extends Fragment {

    private RequestQueue mRequestQueue;

    private TextView tv_hotsort_first;
    private TextView tv_hotsort_second;
    private TextView tv_hotsort_third;
    private EditText edt_search_input;

    private Button btn_search;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void bindViews() {
        tv_hotsort_first = (TextView) getView().findViewById(R.id.tv_hotsort_first);
        tv_hotsort_second = (TextView) getView().findViewById(R.id.tv_hotsort_second);
        tv_hotsort_third = (TextView) getView().findViewById(R.id.tv_hotsort_third);
        btn_search = (Button) getView().findViewById(R.id.btn_search);
        edt_search_input = (EditText) getView().findViewById(R.id.edt_search_input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
        bindEvents();
    }

    private void bindEvents() {
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = edt_search_input.getText().toString();
                if (content != null && !content.equals("")) {
                    Log.i("@@@", "content=" + content);
                    if (mRequestQueue == null) {
                        mRequestQueue = Volley.newRequestQueue(getActivity());
                    }
                    UserFunction.getSearchResult(mRequestQueue, content, mHandler);
                }
            }
        });
    }

    private void getSearchSort() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        if (AppConfig.user != null) {
            UserFunction.getSearchSort(mRequestQueue, AppConfig.user.userId, mHandler);
        }
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

                    try {
                        JSONObject obj = jarr.getJSONObject(0);
                        JSONArray jarr2 = obj.getJSONArray("title");
                        if (jarr2.length() != 3) {
                            Toast.makeText(getActivity(), "获取搜索排行出错", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tv_hotsort_first.setText(jarr2.get(0).toString());
                        tv_hotsort_second.setText(jarr2.get(1).toString());
                        tv_hotsort_third.setText(jarr2.get(2).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FunctionTag.SEARCHRESULT:
                    JSONArray searchResult = (JSONArray) msg.obj;

                    try {
                        for (int i = 0; i < searchResult.length(); i++) {

                            JSONObject objResult = searchResult.getJSONObject(i);
                            Log.i("@@@", objResult.toString());
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
