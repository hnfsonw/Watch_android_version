package com.loybin.baidumap.util;


/**
 * 回传当前改变的网络状态
 */
public interface NetEvent {
    void onNetChange(int netMobile);
}
