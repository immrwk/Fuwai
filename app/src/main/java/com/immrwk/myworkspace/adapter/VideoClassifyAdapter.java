package com.immrwk.myworkspace.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.immrwk.myworkspace.R;
import com.immrwk.myworkspace.bean.VideoClassifyModel;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class VideoClassifyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<VideoClassifyModel> videos;

    public VideoClassifyAdapter(Context context, List<VideoClassifyModel> videos) {
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
            convertView = mInflater.inflate(R.layout.item_videoclassify, null);
            holder = new ViewHolder();
            holder.ivVideoPic = (ImageView) convertView.findViewById(R.id.iv_videopic);
            holder.tvVideoName = (TextView) convertView.findViewById(R.id.tv_videoname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoClassifyModel model = videos.get(position);
        holder.tvVideoName.setText(model.getClassifyName());
        //图片处理


//        new Task(holder.ivVideoPic, model.getImgurl()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");

        //volley框架imageloader加载图片，加载太慢！！！
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.ivVideoPic, R.drawable.search_normal, R.drawable.search_selected);
//        RequestQueue mQueue = Volley.newRequestQueue(context);
//        ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache());
//        mImageLoader.get(model.getImgurl(), listener);

        Glide.with(context)
                .load(model.getImgurl())
                .into(holder.ivVideoPic);

        return convertView;
    }

    class Task extends AsyncTask<String, Integer, Bitmap> {
        ImageView iv;
        String url;
        Bitmap bitMap;

        public Task(ImageView iv, String url) {
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            /** 得到可用的图片 */
            bitMap = getHttpBitmap(url);
            return bitMap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (result != null) {
                iv.setImageBitmap(result);
                Log.i("@@@", url);
                Log.i("@@@", result.getRowBytes() * result.getHeight() + "");
            }
        }
    }

    /**
     * 从网络获取图片
     *
     * @param urlpath 图片地址
     * @return
     */
    public static Bitmap getHttpBitmap(String urlpath) {
        Bitmap bitmap = null;
        try {
            String source = URLEncoder.encode(urlpath, "utf-8");
            source = source.replace("%2F", "/");
            source = source.replace("%3A", ":");
            URL url = new URL(source);
            /** 打开连接 */
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setDoInput(true);
            conn.connect();
            /** 得到数据流 */
            InputStream inputstream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputstream);
            /** 关闭输入流 */
            inputstream.close();
            /** 关闭连接 */
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static class ViewHolder {
        ImageView ivVideoPic;
        TextView tvVideoName;
    }

    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }

            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }
}
