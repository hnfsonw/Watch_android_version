package com.loybin.baidumap.model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by huangz on 17/8/18.
 */

public class PhoneCostsBean implements Serializable {

    private int id;
    private int commandType;
    private String content;
    private Long time;
    private String imgrul;

    public PhoneCostsBean() {

    }

    public String getImgrul() {
        return imgrul;
    }

    public void setImgrul(String imgrul) {
        this.imgrul = imgrul;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {

        Log.e("PhoneCostsListBean", "commandType :" + commandType + " content :" + content + " time :" + time);
        return super.toString();
    }
}
