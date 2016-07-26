package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.immrwk.myworkspace.util.KLog;
import com.immrwk.myworkspace.util.MD5Util;
import com.immrwk.myworkspace.widget.UnderlineEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class LoginActivity extends Activity {

    private UnderlineEditText edt_account;
    private UnderlineEditText edt_password;
    private UnderlineEditText edt_ip;
    private UnderlineEditText edt_email;

    private Button btn_login;
    private RequestQueue mRequestQueue;
    private TextView tv_register;

    private boolean isLogin = true;

    private String account = "";
    private String password = "";
    private String ip = "";
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initEvents();
    }


    /**
     * 检查输入信息是否完整
     */
    private boolean checkInput() {
        account = edt_account.getText().toString();
        password = edt_password.getText().toString();
        ip = edt_ip.getText().toString();
        email = edt_email.getText().toString();
        if (account.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isLogin) {
            if (email.equals("")) {
                Toast.makeText(LoginActivity.this, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (ip.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入ip地址", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initEvents() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkInput()) {
                    return;
                }
                KLog.e("btn_login islogin=" + isLogin);
                if (isLogin) {
                    if (mRequestQueue == null) {
                        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    }
                    UserFunction.login(mRequestQueue, account, MD5Util.string2MD5(password), handler);
                } else if (!isLogin) {
                    if (mRequestQueue == null) {
                        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                    }
                    UserFunction.register(mRequestQueue, account, MD5Util.string2MD5(password), email, handler);
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    tv_register.setText(Html.fromHtml("<u>" + getResources().getString(R.string.now_login) + "</u>"));
                    btn_login.setText("注册");
                    edt_email.setVisibility(View.VISIBLE);
                    isLogin = false;
                } else {
                    btn_login.setText("登录");
                    tv_register.setText(Html.fromHtml("<u>" + getResources().getString(R.string.now_register) + "</u>"));
                    edt_email.setVisibility(View.GONE);
                    isLogin = true;
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
        edt_account = (UnderlineEditText) findViewById(R.id.edt_account);
        edt_password = (UnderlineEditText) findViewById(R.id.edt_password);
        edt_ip = (UnderlineEditText) findViewById(R.id.edt_ip);
        edt_email = (UnderlineEditText) findViewById(R.id.edt_email);
        tv_register = (TextView) findViewById(R.id.tv_register);
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
                case FunctionTag.REGISTER:
                    JSONArray regArr = (JSONArray) msg.obj;
                    try {
                        if (regArr.getJSONObject(0).getString("success").equals("true")) {
                            UserFunction.login(mRequestQueue, account, MD5Util.string2MD5(password), handler);
                        } else {
                            Toast.makeText(LoginActivity.this, regArr.getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
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
