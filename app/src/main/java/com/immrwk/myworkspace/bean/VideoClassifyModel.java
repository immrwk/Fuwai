package com.immrwk.myworkspace.bean;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class VideoClassifyModel {
    private String classifyId;
    private String classifyName;
    private String imgurl;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public String toString() {
        return "VideoClassifyModel{" +
                "classifyId='" + classifyId + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }
}
