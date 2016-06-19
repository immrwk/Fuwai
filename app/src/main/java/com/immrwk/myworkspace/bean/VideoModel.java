package com.immrwk.myworkspace.bean;

/**
 * Created by Administrator on 2016/6/19 0019.
 */
public class VideoModel {

    /**
     * 视频Id-videoId   视频名称-videoName   视频关键帧图片-imgurl
     * 视频类型-videoType(1代表普通视频   2 代表互动视频)
     * 视频播放路径-url  视频内容-videoInfo   视频时间-createDate
     * 视频点击量-click  视频分类名称-className
     */
    private String videoId;
    private String videoName;
    private String videoType;
    private String imgurl;
    private String url;
    private String videoInfo;
    private String createDate;
    private String click;
    private String className;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(String videoInfo) {
        this.videoInfo = videoInfo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
                "videoId='" + videoId + '\'' +
                ", videoName='" + videoName + '\'' +
                ", videoType='" + videoType + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", url='" + url + '\'' +
                ", videoInfo='" + videoInfo + '\'' +
                ", createDate='" + createDate + '\'' +
                ", click='" + click + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
