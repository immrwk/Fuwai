package com.immrwk.myworkspace.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.immrwk.myworkspace.R;

/**
 * Created by Administrator on 2016/6/19 0019.
 */
public class SpecialNoteActivity extends Activity {

    private WebView wv;
    private ImageView iv_return;
    String content = "本APP阜外医院视频客户端<br>非官方！！！<br>非官方！！！";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_note);

        iv_return = (ImageView) findViewById(R.id.iv_return);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setDefaultTextEncodingName("utf-8");
//        wv.loadData(content, "text/html", "UTF-8");
        wv.loadData(content, "text/html; charset=UTF-8", null);
    }

}
