package com.immrwk.myworkspace.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.immrwk.myworkspace.AppConfig;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.adapter.VideoAdapter;
import com.immrwk.myworkspace.bean.VideoModel;
import com.immrwk.myworkspace.db.DatabaseImpl;
import com.immrwk.myworkspace.function.FunctionTag;
import com.immrwk.myworkspace.function.UserFunction;
import com.immrwk.myworkspace.util.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.FullScreenActivity;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class HomeFragment extends Fragment {

    private RequestQueue mRequestQueue;
    private List<VideoModel> demandVideos = new ArrayList<>();
    private List<VideoModel> liveVideos = new ArrayList<>();
    private List<VideoModel> historyVideos = new ArrayList<>();
    private int pageNow = 0;

    private GridView gv_demand;
    private GridView gv_live;
    private GridView gv_history;
    private VideoAdapter adapter;

    private LinearLayout ll_livevideo;
    private LinearLayout ll_demandvideo;
    private LinearLayout ll_videohistory;

    /**
     * viewpager
     */
    private ViewPager mViewPager;
    private ImageView[] mIndicator;
    private TextView tv_title;
    private boolean mIsUserTouched = false;
    private int mBannerPosition = 0;
    private final int FAKE_BANNER_SIZE = 100;
    private final int DEFAULT_BANNER_SIZE = 3;
    private Timer mTimer = new Timer();
    private List<VideoModel> bannerVideos = new ArrayList<>();
    private BannerAdapter bannerAdapter;

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
                /**
                 * Android在子线程更新UI的几种方法
                 * Handler，AsyncTask,view.post,runOnUiThread
                 */
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
                            mViewPager.setCurrentItem(DEFAULT_BANNER_SIZE - 1, false);
                        } else {
                            mViewPager.setCurrentItem(mBannerPosition);
                        }
                    }
                });
            }
        }
    };

    /**
     * 点播内容类别
     */
    private static String CLASSIFYID = "";
    private static final String ADULTS = "-1";  //成人术后恢复中心
    private static final String NORMAL_VIDEO = "1";  //心血管内科
    private static final int INTERACTION_VIDEO = 2;  //

    /**
     * 内容类别
     */
    private static final int LIVE_VIDEO = 1;
    private static final int DEMAND_VIDEO = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CLASSIFYID = ADULTS;
        initViews();
        initEvents();
        //获取所有视频
        getAllVideos();
        mTimer.schedule(mTimerTask, 5000, 5000);
    }

    /**
     * 初始化监听事件
     */
    private void initEvents() {
        ll_livevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllData(FunctionTag.FROM_HOME_LIVE);
            }
        });
        ll_demandvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllData(FunctionTag.FROM_HOME_DEMAND);
            }
        });
        //播放历史
        ll_videohistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllData(FunctionTag.FROM_HISTORY);
            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBannerPosition = position;
                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        gv_demand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startVideoPlayActivity(demandVideos.get(position));
//                FullScreenActivity.toActivity(getActivity(), demandVideos.get(position).getUrl(), null, demandVideos.get(position).getVideoName());
            }
        });
        gv_live.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startVideoPlayActivity(liveVideos.get(position));
            }
        });
        gv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startVideoPlayActivity(historyVideos.get(position));
            }
        });
    }

    private void setIndicator(int position) {
        position %= DEFAULT_BANNER_SIZE;
        //遍历mIndicator重置src为normal
        for (ImageView indicator : mIndicator) {
            indicator.setImageResource(R.drawable.dot_normal);
        }
        mIndicator[position].setImageResource(R.drawable.dot_focused);
        mIndicator[position].setImageResource(R.drawable.dot_focused);
        tv_title.setText(bannerVideos.get(position).getVideoName());
    }

    private void getAllData(int tag) {
        Intent intent = new Intent(getActivity(), VideoListActivity.class);
        intent.putExtra("tag", tag);
        if (tag == FunctionTag.FROM_HOME_DEMAND) {
            intent.putExtra("classifyId", CLASSIFYID);
        }
        startActivity(intent);
    }

    private void initViews() {
        gv_demand = (GridView) getView().findViewById(R.id.gv_demand);
        gv_live = (GridView) getView().findViewById(R.id.gv_live);
        gv_history = (GridView) getView().findViewById(R.id.gv_history);
        ll_livevideo = (LinearLayout) getView().findViewById(R.id.ll_livevideo);
        ll_demandvideo = (LinearLayout) getView().findViewById(R.id.ll_demandvideo);
        ll_videohistory = (LinearLayout) getView().findViewById(R.id.ll_videohistory);
        mViewPager = (ViewPager) getView().findViewById(R.id.view_pager);
        mIndicator = new ImageView[]{
                (ImageView) getView().findViewById(R.id.dot_indicator1),
                (ImageView) getView().findViewById(R.id.dot_indicator2),
                (ImageView) getView().findViewById(R.id.dot_indicator3),
        };
        tv_title = (TextView) getView().findViewById(R.id.tv_title);
    }


    private void getAllVideos() {
        //获取推荐视频内容
        getRecommendVideos();
//        //获取播放历史
//        getVideoHistory();
        //获取直播视频内容
        getLiveVideos();
        //获取点播视频内容
        getDemandVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoHistory();
    }

    private VideoAdapter hitoryAdapter;

    private void getVideoHistory() {
        DatabaseImpl db = DatabaseImpl.getInstance(getActivity());
        db.open();
        historyVideos = db.queryVideoHistory();
        db.close();
        if (historyVideos.size() <= 4) {
            hitoryAdapter = new VideoAdapter(getActivity(), historyVideos);
            gv_history.setAdapter(hitoryAdapter);
        } else {
            for (int i = 4; i < historyVideos.size(); i++) {
                historyVideos.remove(i);
            }
            hitoryAdapter = new VideoAdapter(getActivity(), historyVideos);
            gv_history.setAdapter(hitoryAdapter);
        }
    }

    private void getLiveVideos() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getLiveVideo(mRequestQueue, pageNow, AppConfig.user.userId, mHandler);
    }

    private void getDemandVideos() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getDemandVideo(mRequestQueue, CLASSIFYID, pageNow, AppConfig.user.userId, mHandler);
    }


    /**
     * 获取视频推荐内容
     */
    private void getRecommendVideos() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        UserFunction.getRecommendVideo(mRequestQueue, AppConfig.user.userId, mHandler);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

    private void setVideoData(JSONArray arr, List<VideoModel> videos, GridView gv, int videoType) {
        JSONObject obj;
        videos.clear();
        try {
            for (int i = 0; i < arr.length(); i++) {
                obj = arr.getJSONObject(i);
                VideoModel vm = new VideoModel();
                vm.setVideoName(obj.getString("videoName"));
                vm.setVideoId(obj.getString("videoId"));
                vm.setVideoType(obj.getString("videoType"));
                vm.setImgurl(obj.getString("imgurl"));
                vm.setClick(obj.getString("click"));
                vm.setVideoInfo(obj.getString("videoInfo"));
                vm.setCreateDate(obj.getString("createDate"));
                if (videoType == DEMAND_VIDEO) {
                    vm.setUrl("http://106.120.203.85/install/videoupload" + obj.getString("url"));
                    vm.setClassName(obj.getString("className"));
                }
                videos.add(vm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new VideoAdapter(getActivity(), videos);
        gv.setAdapter(adapter);
    }


    /**
     * 视频推荐
     */
    private void handleRecommendVideoResult(JSONArray result) {
        JSONObject obj;
        bannerVideos.clear();
        try {
            for (int i = 0; i < result.length(); i++) {
                obj = result.getJSONObject(i);
                VideoModel vm = new VideoModel();
                vm.setVideoName(obj.getString("videoName"));
                vm.setVideoId(obj.getString("videoId"));
                vm.setVideoType(obj.getString("videoType"));
                vm.setImgurl(obj.getString("imgurl"));
                vm.setClick(obj.getString("click"));
                vm.setVideoInfo(obj.getString("videoInfo"));
                vm.setCreateDate(obj.getString("createDate"));
                vm.setUrl(obj.getString("url"));

                bannerVideos.add(vm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bannerAdapter = new BannerAdapter(getActivity(), bannerVideos);
        mViewPager.setAdapter(bannerAdapter);
    }

    /**
     * 跳转到视频播放界面
     */
    private void startVideoPlayActivity(VideoModel vm) {
        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
        intent.putExtra("videoUrl", vm.getUrl());
        intent.putExtra("imgUrl", vm.getImgurl());
        intent.putExtra("videoName", vm.getVideoName());
        intent.putExtra("className", vm.getClassName());
        intent.putExtra("createDate", vm.getCreateDate());
        intent.putExtra("click", vm.getClick());
        intent.putExtra("videoId", vm.getVideoId());
        KLog.e(vm.toString());
        startActivity(intent);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case FunctionTag.DEMANDVIDEO:
                    JSONArray demandArr = (JSONArray) msg.obj;
                    setVideoData(demandArr, demandVideos, gv_demand, DEMAND_VIDEO);
                    break;
                case FunctionTag.LIVEVIDEO:
                    JSONArray liveArr = (JSONArray) msg.obj;
                    setVideoData(liveArr, liveVideos, gv_live, LIVE_VIDEO);
                    UserFunction.getVideoUrl(mRequestQueue, liveVideos.get(0).getVideoId(), mHandler);
                    break;
                case FunctionTag.GETVIDEOURL:
                    JSONArray urlArr = (JSONArray) msg.obj;

                    if (liveVideos.size() <= 0) {
                        return;
                    }
                    try {
                        liveVideos.get(0).setUrl(urlArr.getJSONObject(0).getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case FunctionTag.RECOMMENDVIDEO:
                    JSONArray recommendVideo = (JSONArray) msg.obj;
                    handleRecommendVideoResult(recommendVideo);

                case FunctionTag.ERROR:

                    break;
            }
        }
    };

    private class BannerAdapter extends PagerAdapter {

        private Context context;
        private List<VideoModel> recommendVideos;

        public BannerAdapter(Context context, List<VideoModel> recommendVideos) {
            this.context = context;
            this.recommendVideos = recommendVideos;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= DEFAULT_BANNER_SIZE;

            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, container, false);
            ImageView image = (ImageView) view.findViewById(R.id.image);

            Glide.with(context)
                    .load(recommendVideos.get(position).getImgurl())
                    .into(image);

            final int pos = position;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startVideoPlayActivity(recommendVideos.get(pos));
                }

            });
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void finishUpdate(ViewGroup container) {
            //这个有点懵逼..
            int position = mViewPager.getCurrentItem();
            if (position == 0) {
                position = DEFAULT_BANNER_SIZE;
                mViewPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = DEFAULT_BANNER_SIZE - 1;
                mViewPager.setCurrentItem(position, false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
