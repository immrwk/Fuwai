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

import java.util.List;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class VideoListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<VideoModel> videos;
    private Context context;

    public VideoListAdapter(Context context, List<VideoModel> videosd) {
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
            convertView = mInflater.inflate(R.layout.item_videolist, null);
            holder = new ViewHolder();
            holder.videoPic = (ImageView) convertView.findViewById(R.id.iv_videopic);
            holder.tvVideoName = (TextView) convertView.findViewById(R.id.tv_videoname);
            holder.tvVideoType = (TextView) convertView.findViewById(R.id.tv_videoType);
            holder.tvVideoHits = (TextView) convertView.findViewById(R.id.tv_videoHits);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoModel vm = videos.get(position);
        holder.tvVideoType.setText(vm.getClassName());
        holder.tvVideoName.setText(vm.getVideoName());
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
