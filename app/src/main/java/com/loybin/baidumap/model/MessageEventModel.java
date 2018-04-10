package com.loybin.baidumap.model;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/29 上午10:35
 * 描   述: 音乐播放器 eventBus 数据类
 */
public class MessageEventModel {

    private String title;
    private String imagurl;
    private String currPos;
    private String duration;
    private int progress;
    private int durations;

    public MessageEventModel() {
    }

    public int getDurations() {
        return durations;
    }

    public void setDurations(int durations) {
        this.durations = durations;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getCurrPos() {
        return currPos;
    }

    public void setCurrPos(String currPos) {
        this.currPos = currPos;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagurl() {
        return imagurl;
    }

    public void setImagurl(String imagurl) {
        this.imagurl = imagurl;
    }
}
