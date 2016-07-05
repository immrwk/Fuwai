package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.os.Bundle;

import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.adapter.VideoListAdapter;
import com.immrwk.myworkspace.bean.VideoClassifyModel;
import com.immrwk.myworkspace.widget.LoadListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class VideoListActivity extends Activity {

    private LoadListView loadListView;
    private List<VideoClassifyModel> videos = new ArrayList<>();
    private VideoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        initViews();
    }

    private void initViews() {
        loadListView = (LoadListView) findViewById(R.id.loadlistview);
    }


}
