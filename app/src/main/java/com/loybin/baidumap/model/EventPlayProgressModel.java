package com.loybin.baidumap.model;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/29 下午2:59
 * 描   述: Event Progress 显示 不显示
 */
public class EventPlayProgressModel {
    private boolean isProgress;


    public void setProgress(boolean seekBar) {
        isProgress = seekBar;
    }

    public boolean isProgress() {
        return isProgress;
    }
}
