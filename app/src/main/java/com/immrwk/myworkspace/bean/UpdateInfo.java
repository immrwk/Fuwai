package com.immrwk.myworkspace.bean;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class UpdateInfo {

    private String mSucess;
    private String mMsg;

    public String getmSucess() {
        return mSucess;
    }

    public void setmSucess(String mSucess) {
        this.mSucess = mSucess;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "mSucess='" + mSucess + '\'' +
                ", mMsg='" + mMsg + '\'' +
                '}';
    }
}
