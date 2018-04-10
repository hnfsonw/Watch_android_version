package com.loybin.baidumap.widget.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

/**
 * Created by LoyBin on 2017/8/28.
 * 通知栏按钮接收
 */

public class MyPlayerReceiver extends BroadcastReceiver {

    private static final String TAG = "MyPlayerReceiver";
    private XmPlayerManager mPlayerManager;

    @Override
    public void onReceive(Context context, Intent intent) {
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(10);
//        notificationManager.cancelAll();

//        XmNotificationCreater instanse = XmNotificationCreater.getInstanse(context);
//        instanse.
//        mPlayerManager = XmPlayerManager.getInstance(context);
//        mPlayerManager.notify();
//        XmPlayerManager.release();
//        XmPlayerManager.unBind();

//        mPlayerManager = XmPlayerManager.getInstance(context);
//        CommonRequest.release();

        LogUtils.e(TAG, "通知栏按钮接收 == " + intent.getAction() + Thread.currentThread().getName());
    }
}
