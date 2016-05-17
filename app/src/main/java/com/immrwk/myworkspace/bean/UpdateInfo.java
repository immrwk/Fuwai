package com.immrwk.myworkspace.bean;

/**
 * Created by Administrator on 2016/5/15 0015.
 */
public class UpdateInfo {

    private String sucess;
    private String msg;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSucess() {
        return sucess;
    }

    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "sucess='" + sucess + '\'' +
                ", msg='" + msg + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
