package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.bean.User;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.function.UserFunction;
import com.immrwk.myworkspace.util.MD5Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class LoginActivity extends Activity {

    private Button btn_login;
    private EditText edt_account;
    private EditText edt_password;
    private EditText edt_ip;
    private RequestQueue mRequestQueue;
    private TextView tv_register;
    private EditText edt_email;

    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setOnClickListener();
    }

    private void setOnClickListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = edt_account.getText().toString();
                String password = edt_password.getText().toString();
                String ip = edt_ip.getText().toString();
                if (account.equals("") || password.equals("") || ip.equals("")) {
                    Toast.makeText(LoginActivity.this, "信息不全", Toast.LENGTH_SHORT).show();
                } else {
                    mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    UserFunction.login(mRequestQueue, account, MD5Util.string2MD5(password), handler);
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin) {
                    tv_register.setText(" 登录 ");
                    btn_login.setText("注册");
                    edt_email.setVisibility(View.VISIBLE);
                    isLogin = true;
                } else {
                    tv_register.setText(" 注册 ");
                    btn_login.setText("登录");
                    edt_email.setVisibility(View.GONE);
                    isLogin = false;
                }
//                setAllTextNull();
            }
        });
    }

    private void setAllTextNull() {
        edt_account.setText("");
        edt_password.setText("");
        edt_ip.setText("");
        edt_email.setText("");
    }

    private void initViews() {
        btn_login = (Button) findViewById(R.id.btn_login);
        edt_account = (EditText) findViewById(R.id.edt_account);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_ip = (EditText) findViewById(R.id.edt_ip);
        tv_register = (TextView) findViewById(R.id.tv_register);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_account.setText("15501095357");
        edt_password.setText("123456");
        edt_ip.setText("106.120.203.85:8088");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FunctionTag.LOGIN:
                    JSONArray jarr = (JSONArray) msg.obj;
                    JSONObject obj;
                    try {
                        obj = jarr.getJSONObject(0);
                        AppConfig.user = new User();
                        AppConfig.user.userId = (obj.getString("userId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    finish();
                    break;
                case FunctionTag.ERROR:
                    break;
            }
        }
    };
}
