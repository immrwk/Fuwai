package com.immrwk.myworkspace.ui;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.immrwk.myworkspace.R;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    private LinearLayout ll_home;
    private LinearLayout ll_classify;
    private LinearLayout ll_search;
    private LinearLayout ll_setting;

    private ImageView iv_home;
    private ImageView iv_classify;
    private ImageView iv_search;
    private ImageView iv_setting;

    private TextView tv_home;
    private TextView tv_classify;
    private TextView tv_search;
    private TextView tv_setting;

    private Fragment homeFragment;
    private Fragment classifyFragment;
    private Fragment searchFragment;
    private Fragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnclickListener();
        setSelect(0);
    }

    private void setOnclickListener() {
        ll_home.setOnClickListener(this);
        ll_classify.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    private void initViews() {
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_classify = (LinearLayout) findViewById(R.id.ll_classify);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_classify = (ImageView) findViewById(R.id.iv_classify);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_classify = (TextView) findViewById(R.id.tv_classify);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
    }

    private void setSelect(int i) {
        resetResources();
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.commonPurple);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                iv_home.setImageResource(R.drawable.home_selected);
                tv_home.setTextColor(csl);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                iv_classify.setImageResource(R.drawable.classify_selected);
                tv_classify.setTextColor(csl);
                if (classifyFragment == null) {
                    classifyFragment = new ClassifyFragment();
                    transaction.add(R.id.fl_content, classifyFragment);
                } else {
                    transaction.show(classifyFragment);
                }
                break;
            case 2:
                iv_search.setImageResource(R.drawable.search_selected);
                tv_search.setTextColor(csl);
                if (searchFragment == null) {
                    searchFragment = new SearchFragment();
                    transaction.add(R.id.fl_content, searchFragment);
                } else {
                    transaction.show(searchFragment);
                }
                break;
            case 3:
                iv_setting.setImageResource(R.drawable.setting_selected);
                tv_setting.setTextColor(csl);
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.fl_content, settingFragment);
                } else {
                    transaction.show(settingFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (classifyFragment != null) {
            transaction.hide(classifyFragment);
        }
        if (searchFragment != null) {
            transaction.hide(searchFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }

    private void resetResources() {
        iv_home.setImageResource(R.drawable.home_normal);
        iv_classify.setImageResource(R.drawable.classify_normal);
        iv_search.setImageResource(R.drawable.search_normal);
        iv_setting.setImageResource(R.drawable.setting_normal);

        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorBlack);
        tv_home.setTextColor(csl);
        tv_classify.setTextColor(csl);
        tv_search.setTextColor(csl);
        tv_setting.setTextColor(csl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                setSelect(0);
                break;
            case R.id.ll_classify:
                setSelect(1);
                break;
            case R.id.ll_search:
                setSelect(2);
                break;
            case R.id.ll_setting:
                setSelect(3);
                break;
            default:
                break;
        }
    }
}
