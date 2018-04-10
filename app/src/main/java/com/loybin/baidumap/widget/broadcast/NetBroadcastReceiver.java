package com.loybin.baidumap.widget.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.loybin.baidumap.util.NetEvent;
import com.loybin.baidumap.util.NetUtil;

/**
 * 自定义检查手机网络状态的广播接受器
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    private NetEvent netEvent;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWrokState = NetUtil.getNetWorkState(context);
            Log.e("NetBroadcastReceiver", netWrokState + "");
            if (netEvent != null)
                // 接口回传网络状态的类型
                netEvent.onNetChange(netWrokState);
        }
    }


    public void setNetEvent(NetEvent netEvent) {
        this.netEvent = netEvent;
    }

}