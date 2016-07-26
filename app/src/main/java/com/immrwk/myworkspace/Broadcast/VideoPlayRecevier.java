package com.immrwk.myworkspace.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.immrwk.myworkspace.util.KLog;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class VideoPlayRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        KLog.e("onreceive");
        Bundle bundle = intent.getExtras();
        KLog.e(bundle.toString() + "\n"
                + bundle.getString("url") + "\n"
                + bundle.getString("imgUrl") + "\n"
                + bundle.getString("videoName"));
    }
}
