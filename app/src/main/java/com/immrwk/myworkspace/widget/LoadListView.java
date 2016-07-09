package com.immrwk.myworkspace.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.immrwk.myworkspace.R;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class LoadListView extends ListView {

    public static final int NOMORE_DATA = 0;
    public static final int LOADMORE_SUCCESS = 1;
    public static final int LOADMORE_FAILED = -1;


    /* 加载更多控件 */
    private View moreView;
    private TextView tv_load_more;
    private ProgressBar pb_load_progress;

    /* 加载更多参数 */
    private boolean blLoad = false;
    private int lastItem;
    private int page = 0;
    private Context context;

    public LoadListView(Context context) {
        super(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 获取当前第几页
     */
    public int getPage() {
        return page;
    }

    /**
     * 设置当前第几页
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 重置ListView拉下刷新
     */
    public void onResetListPage() {
        if (getAdapter().getCount() < 1) {
            tv_load_more.setText(getResources().getString(R.string.no_more));
            pb_load_progress.setVisibility(View.GONE);
        } else {
            tv_load_more.setText(getResources().getString(R.string.more_loading));
            pb_load_progress.setVisibility(View.VISIBLE);
        }
        blLoad = true;
        page = 0;
    }

    /**
     * 下拉刷新加载完成
     *
     * @param iOver 返回加载状态（0：最后一页，没有更多数据。1：下拉刷新成功。-1：下拉刷新失败）
     */
    public void onLoadFinish(int iOver) {
        if (iOver == NOMORE_DATA) {
            tv_load_more.setText(getResources().getString(R.string.no_more));
            pb_load_progress.setVisibility(View.GONE);
        } else if (iOver == LOADMORE_SUCCESS) {
            blLoad = false;
        } else if (iOver == LOADMORE_FAILED) {
            page--;
            blLoad = false;
        }
    }

    public DataListener mDataListener;

    /**
     * 设置线程接口
     *
     * @param listener 线程接口
     */
    public void setDataListener(DataListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        moreView = inflater.inflate(R.layout.footer_more, null);
        /**
         * 设置点击加载
         */
        moreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_load_more == null) {
                    return;
                }
                if (tv_load_more.getText().toString().equals(getResources().getString(R.string.no_more))) {
                    tv_load_more.setText(getResources().getString(R.string.more_loading));
                    if (pb_load_progress != null) {
                        pb_load_progress.setVisibility(View.VISIBLE);
                    }
                    mDataListener.onloadMoreData(page);
                }
            }
        });
        tv_load_more = (TextView) moreView.findViewById(R.id.tv_load_more);
        pb_load_progress = (ProgressBar) moreView.findViewById(R.id.pb_load_progress);
        tv_load_more.setText(getResources().getString(R.string.no_more));
        pb_load_progress.setVisibility(View.GONE);
        this.addFooterView(moreView);

        this.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int maxItem = 0;
                if (getAdapter().getCount() < 1) {
                    return;
                }
//                else {
//                    tv_load_more.setText(getResources().getString(R.string.more_loading));
//                    pb_load_progress.setVisibility(View.VISIBLE);
//                }
                if (blLoad) {
                    return;
                }
                maxItem = getAdapter().getCount() - 1;
                if (lastItem == maxItem && (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING)) {
                    blLoad = true;
                    page++;
                    moreView.setVisibility(View.VISIBLE);
                    tv_load_more.setText(getResources().getString(R.string.more_loading));
                    pb_load_progress.setVisibility(View.VISIBLE);
                    mDataListener.onloadMoreData(page);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });
        mDataListener = listener;
    }

    /**
     * 线程接口
     */
    public interface DataListener {
        /**
         * 线程数据处理完成返回函数
         */
        void onloadMoreData(int page);
    }

}
