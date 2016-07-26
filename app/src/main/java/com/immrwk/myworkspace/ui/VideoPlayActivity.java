package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.util.KLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class VideoPlayActivity extends Activity {

    private JCVideoPlayer videoPlayer;
    private ImageView iv_back;

    private String videoName = "";
    private String videoUrl = "";
    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        getData();
        initViews();
        initEvents();
        toFullScreen();
        finish();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        videoUrl = bundle.getString("videoUrl");
        imgUrl = bundle.getString("imgUrl");
        videoName = bundle.getString("videoName");
    }

    private void initViews() {
        videoPlayer = (JCVideoPlayer) findViewById(R.id.videoplayer);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void initEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        KLog.e("wang", "videoName=" + videoName + ",videoUrl=" + videoUrl + ", imgUrl=" + imgUrl);
        JCVideoPlayer.setThumbImageViewScalType(ImageView.ScaleType.FIT_XY);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(VideoPlayActivity.this);
        ImageLoader.getInstance().init(configuration);
//        videoUrl = "http://106.120.203.85:5050/video/Channel2/index.m3u8";
        videoPlayer.setUp(videoUrl, imgUrl, videoName);
    }

    private void toFullScreen() {
        JCVideoPlayer.toFullscreenActivity(this, videoUrl, imgUrl, videoName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
