package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
public class WelcomeActivity extends Activity{

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        UserFunction.updateVersion(mRequestQueue,"1.0",mHandler);
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
                    Log.i("wk",msg.obj.toString());
                    UpdateInfo info = new UpdateInfo();
                    JSONArray jarr = (JSONArray) msg.obj;
                    for(int i = 0; i < jarr.length();i++){
                        try {
                            String str = jarr.getString(i);
                            JSONObject result = new JSONObject(str);
                            info.setmMsg(result.getString("msg"));
                            info.setmSucess(result.getString("success"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i("wk",info.toString());
                    }

                    break;
            }
        }
    };
}
