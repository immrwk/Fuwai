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
    private String className = "";
    private String createDate = "";
    private String click = "";
    private String videoId = "";

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
        className = bundle.getString("className");
        createDate = bundle.getString("createDate");
        click = bundle.getString("click");
        videoId = bundle.getString("videoId");
        KLog.e(className+"@@@@@@@@");
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
        JCVideoPlayer.setThumbImageViewScalType(ImageView.ScaleType.FIT_XY);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(VideoPlayActivity.this);
        ImageLoader.getInstance().init(configuration);
        videoPlayer.setUp(videoUrl, imgUrl, videoName);
    }

    private void toFullScreen() {
        JCVideoPlayer.toFullscreenActivity(this, videoUrl, imgUrl, videoName, className, createDate, click, videoId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
