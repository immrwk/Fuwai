package com.immrwk.myworkspace.ui;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class BaseFragmentActivity extends FragmentActivity {
    private final int TIME_INTERVAL = 800;
    private long mBackPressed = 0;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            System.exit(0);
            return;
        } else {
            Toast.makeText(BaseFragmentActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}
