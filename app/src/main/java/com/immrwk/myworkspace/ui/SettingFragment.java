package com.immrwk.myworkspace.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.immrwk.myworkspace.R;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class SettingFragment extends Fragment {

    private RelativeLayout rl_clean_cache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindViews();
        bindEvents();
    }

    private void bindEvents() {
        rl_clean_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void bindViews() {
        rl_clean_cache = (RelativeLayout) getView().findViewById(R.id.rl_clean_cache);
    }
}
