package com.loybin.baidumap.widget.broadcast;


import android.content.Context;

import com.hyphenate.chat.EMMipushReceiver;
import com.loybin.baidumap.util.LogUtils;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/21 下午5:54
 * 描   述:   小米自定义广播
 */
public class MIPushReceiver extends EMMipushReceiver {

    private static final String TAG = "MIPushReceiver";

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        LogUtils.e(TAG, "onNotificationMessageClicked " + miPushMessage.getMessageType());

        super.onNotificationMessageClicked(context, miPushMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        LogUtils.e(TAG, "onReceiveRegisterResult " + miPushCommandMessage.getCommand());
        super.onReceiveRegisterResult(context, miPushCommandMessage);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        LogUtils.e(TAG, "onCommandResult " + miPushCommandMessage.getCategory());
        super.onCommandResult(context, miPushCommandMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        LogUtils.e(TAG, "onNotificationMessageArrived " + miPushMessage.getCategory());
        super.onNotificationMessageArrived(context, miPushMessage);
    }

    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        LogUtils.e(TAG, "onReceiveMessage " + miPushMessage.getTitle());
        super.onReceiveMessage(context, miPushMessage);
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        LogUtils.e(TAG, "onReceivePassThroughMessage " + miPushMessage.getAlias());
        super.onReceivePassThroughMessage(context, miPushMessage);
    }
}
