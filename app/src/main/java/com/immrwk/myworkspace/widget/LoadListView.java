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
        if (getAdapter().getCount() < 10) {
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
     * @param iOver 返回加载状态（0：最后一页，没有更多数据。1：下拉刷新成功。-1：下拉刷新失败）
     */
    public void onLoadEnd(int iOver) {
        if (iOver == 0) {
            tv_load_more.setText(getResources().getString(R.string.no_more));
            pb_load_progress.setVisibility(View.GONE);
        } else if (iOver == 1) {
            blLoad = false;
        } else if (iOver == -1) {
            page--;
            blLoad = false;
        }
    }

    public DataListener mDataListener;
    /**
     * 设置线程接口
     * @param listener 线程接口
     */
    public void setDataListener(DataListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        moreView = inflater.inflate(R.layout.footer_more, null);

        tv_load_more = (TextView) moreView.findViewById(R.id.tv_load_more);
        pb_load_progress = (ProgressBar) moreView.findViewById(R.id.pb_load_progress);
        tv_load_more.setText(getResources().getString(R.string.no_more));
        pb_load_progress.setVisibility(View.GONE);
        this.addFooterView(moreView);

        this.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int maxItem = 0;
                if (getAdapter().getCount() < 10)
                    return;
                else {
                    tv_load_more.setText(getResources().getString(R.string.more_loading));
                    pb_load_progress.setVisibility(View.VISIBLE);
                }
                if (blLoad)
                    return;
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
         * @param tag 线程数据标识
         * @param objects 返回数据
         */
        void onloadMoreData(int page);
    }

}
