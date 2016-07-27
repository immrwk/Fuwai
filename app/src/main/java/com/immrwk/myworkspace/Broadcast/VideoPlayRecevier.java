package com.immrwk.myworkspace.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.immrwk.myworkspace.bean.VideoModel;
import com.immrwk.myworkspace.db.DatabaseImpl;
import com.immrwk.myworkspace.util.KLog;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class VideoPlayRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String videoId = bundle.getString("videoId");
        DatabaseImpl db = DatabaseImpl.getInstance(context);
        db.open();
        List<VideoModel> videos = db.queryVideoHistory();
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).getVideoId().equals(videoId)) {
                /**
                 * 将该条记录移到最上面
                 */
                db.close();
                return;
            }
        }

        VideoModel video = new VideoModel();
        video.setVideoId(bundle.getString("videoId"));
        video.setUrl(bundle.getString("url"));
        video.setImgurl(bundle.getString("imgUrl"));
        video.setClassName(bundle.getString("className"));
        video.setClick(bundle.getString("click"));
        video.setCreateDate(bundle.getString("createDate"));
        video.setVideoName(bundle.getString("videoName"));

        db.insertOrUpdateVideoHistory(video);
        db.close();

    }
}
