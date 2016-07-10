package com.immrwk.myworkspace.ui;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.bean.UpdateInfo;
import com.immrwk.myworkspace.function.UserFunction;
import com.immrwk.myworkspace.util.DataCleanManager;
import com.immrwk.myworkspace.widget.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class SettingFragment extends Fragment {

    private RelativeLayout rl_clean_cache;
    private RelativeLayout rl_check_update;
    private RelativeLayout rl_aboutus;
    private RelativeLayout rl_special_note;

    private Button btn_logout;

    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindViews();
        bindEvents();
    }

    private void bindEvents() {
        /** 清除缓存 */
        rl_clean_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCleanManager.cleanInternalCache(getActivity());
                DataCleanManager.cleanExternalCache(getActivity());
                Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_SHORT).show();
                ;
            }
        });
        /** 检查更新 */
        rl_check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestQueue = Volley.newRequestQueue(getActivity());
                UserFunction.updateVersion(mRequestQueue, AppConfig.versionCode, mHandler);
            }
        });
        /** 关于我们 */
        rl_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KAlertDialog(getActivity()).builder().setMsg("阜外医院视频客户端").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
            }
        });
        /** 特别说明 */
        rl_special_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SpecialNoteActivity.class);
                getActivity().startActivity(intent);
            }
        });

        /**  退出登录 */
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    private String getCacheSize() {
        try {
            Log.i("@@@", "cachedir" + getActivity().getCacheDir());
            Log.i("@@@", "getExternalCacheDir" + getActivity().getExternalCacheDir());
            return DataCleanManager.getFormatSize(DataCleanManager.getFolderSize(getActivity().getCacheDir())
                    + DataCleanManager.getFolderSize(getActivity().getExternalCacheDir()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "0KB";
    }

    private void bindViews() {
        rl_clean_cache = (RelativeLayout) getView().findViewById(R.id.rl_clean_cache);
        rl_check_update = (RelativeLayout) getView().findViewById(R.id.rl_check_update);
        rl_aboutus = (RelativeLayout) getView().findViewById(R.id.rl_aboutus);
        rl_special_note = (RelativeLayout) getView().findViewById(R.id.rl_special_note);
        btn_logout = (Button) getView().findViewById(R.id.btn_logout);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FunctionTag.UPDATE:
                    JSONArray jarr = (JSONArray) msg.obj;
                    UpdateInfo info = new UpdateInfo();
                    try {
                        JSONObject obj = jarr.getJSONObject(0);
                        info.setSucess(obj.getString("success"));
                        info.setMsg(obj.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (info.getSucess().equals("false")) {
                        Toast.makeText(getActivity(), "版本已是最新", Toast.LENGTH_SHORT).show();
                    } else if (info.getSucess().equals("true")) {
                        Toast.makeText(getActivity(), "有新版本可用，请更新", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
