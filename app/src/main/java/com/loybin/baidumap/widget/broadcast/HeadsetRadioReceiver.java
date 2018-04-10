package com.loybin.baidumap.widget.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/08 下午6:37
 * 描   述: 耳机广播view
 */
public class HeadsetRadioReceiver extends BroadcastReceiver {
    private static final String TAG = "BaseActivity";

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", 0);
        LogUtils.e(TAG, "state " + intent.getIntExtra("state", 0) + "");
        if (state == 1) {
            MyApplication.sHeadset = false;
        } else if (intent.getIntExtra("state", 0) == 0) {
            MyApplication.sHeadset = true;
        } else {
            MyApplication.sHeadset = false;
        }
    }
}
