package com.immrwk.myworkspace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.bean.VideoModel;
import com.immrwk.myworkspace.util.Tools;

import java.util.List;


/**
 * Created by Administrator on 2016/7/2 0002.
 */
public class VideoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<VideoModel> videos;
    private Context context;

    public VideoAdapter(Context context, List<VideoModel> videos) {
        this.context = context;
        this.videos = videos;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_video, null);
            holder = new ViewHolder();
            holder.videoPic = (ImageView) convertView.findViewById(R.id.iv_videopic);
            holder.tvVideoName = (TextView) convertView.findViewById(R.id.tv_videoname);
            holder.tvVideoCreateTime = (TextView) convertView.findViewById(R.id.tv_videoCreateTime);
            holder.tvVideoType = (TextView) convertView.findViewById(R.id.tv_videoType);
            holder.tvVideoHits = (TextView) convertView.findViewById(R.id.tv_videoHits);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int screenWidth = Tools.getScreenWidth(context);
        ViewGroup.LayoutParams lp = holder.videoPic.getLayoutParams();
        lp.width = screenWidth/2 - Tools.dip2px(context,20);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.videoPic.setLayoutParams(lp);

        VideoModel vm = videos.get(position);
        holder.tvVideoType.setText("类别:" + vm.getClassName());
        holder.tvVideoName.setText(vm.getVideoName());
        holder.tvVideoCreateTime.setText(vm.getCreateDate());
        holder.tvVideoHits.setText(vm.getClick());

        Glide.with(context)
                .load(vm.getImgurl())
                .into(holder.videoPic);

        return convertView;
    }

    public static class ViewHolder {
        ImageView videoPic; //视频关键帧图片
        TextView tvVideoName;//视频名称
        TextView tvVideoCreateTime;//视频创建时间
        TextView tvVideoType;//视频类别 分类名称
        TextView tvVideoHits;//视频类别 分类名称
    }
}
