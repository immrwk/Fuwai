package com.immrwk.myworkspace.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.immrwk.myworkspace.R;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class SearchFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSearchBtnSize();
    }

    private void setSearchBtnSize() {
        EditText editText1 = (EditText) getView().findViewById(R.id.edt_search_input);
        Drawable drawable1 = getResources().getDrawable(R.drawable.search_normal);
        drawable1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
        editText1.setCompoundDrawables(drawable1, null, null, null);//只放左边
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
