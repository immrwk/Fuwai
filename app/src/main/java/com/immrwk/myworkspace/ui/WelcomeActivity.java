package com.immrwk.myworkspace.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.api.FunctionTag;
import com.immrwk.myworkspace.bean.UpdateInfo;
import com.immrwk.myworkspace.function.UserFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class WelcomeActivity extends BaseActivity{

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        UserFunction.updateVersion(mRequestQueue, AppConfig.versionCode,mHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null){
            mRequestQueue.stop();
        }
    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FunctionTag.UPDATE:
                    JSONArray jarr = (JSONArray) msg.obj;
                    JSONObject obj;
                    UpdateInfo info = new UpdateInfo();
                    try {
                        obj = jarr.getJSONObject(0);
                        info.setSucess(obj.getString("success"));
                        info.setMsg(obj.getString("msg"));
                        info.setUserId(obj.getString("userId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(info.getSucess().equals("false")){
                        Log.i("welcomeactivity","版本已是最新");
                        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();
                    }else if(info.getSucess().equals("true")){
                        Log.i("welcomeactivity","有新版本可用");
                    }

//                    for(int i = 0; i < jarr.length();i++){
//
//                        try {
//                            JSONObject result = jarr.getJSONObject(i);
//                            UpdateInfo info = new UpdateInfo();
//                            info.setmMsg(result.getString("msg"));
//                            info.setmSucess(result.getString("success"));
//                            Log.i("kkkk",info.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }

                    break;
            }
        }
    };
}
