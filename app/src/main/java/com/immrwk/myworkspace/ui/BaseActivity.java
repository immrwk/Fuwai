package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/16 0016.
 */
public class BaseActivity extends Activity {

    private final int TIME_INTERVAL = 800;
    private long mBackPressed = 0;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            System.exit(0);
            return;
        } else {
            Toast.makeText(BaseActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

}
