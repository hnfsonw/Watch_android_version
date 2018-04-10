/************************************************************
 *  * Hyphenate CONFIDENTIAL 
 * __________________ 
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved. 
 *  
 * NOTICE: All information contained herein is, and remains 
 * the property of Hyphenate Inc.
 * Dissemination of this information or reproduction of this material 
 * is strictly forbidden unless prior written permission is obtained
 * from Hyphenate Inc.
 */
package com.hyphenate.easeui.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.annotation.BoolRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseSettingsProvider;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.EasyUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * new message notifier class
 * 
 * this class is subject to be inherited and implement the relative APIs
 */
public class EaseNotifier {
    private final static String TAG = "ChatActivity";
    Ringtone ringtone = null;

    protected final static String[] msg_eng = { "sent a message", "sent a picture", "sent a voice",
                                                "sent location message", "sent a video", "sent a file", "%1 contacts sent %2 messages"
                                              };
    protected final static String[] msg_ch = { "", "发来一张图片", "发来一段语音", "发来位置信息", "发来一个视频", "发来一个文件",
                                               "%1个联系人发来%2条语音消息"
                                             };

    protected static int notifyID = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;

    protected NotificationManager notificationManager = null;

    protected HashSet<String> fromUsers = new HashSet<String>();
    protected int notificationNum = 0;

    protected Context appContext;
    protected String packageName;
    protected String[] msgs;
    protected long lastNotifiyTime;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    protected EaseNotificationInfoProvider notificationInfoProvider;
    private String mText;
    private String mSummaryBody;
    private String mUsername;
    private RemoteViews mRemoteViews;
    private NotificationCompat.BigTextStyle mBigTextStyle;

    public EaseNotifier() {
    }
    
    /**
     * this function can be override
     * @param context
     * @return
     */
    public EaseNotifier init(Context context){
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } else {
            msgs = msg_eng;
        }

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        
        return this;
    }
    
    /**这个函数可以被覆盖
     * this function can be override
     */
    public void reset(){
         Log.e(TAG,"这个函数可以被覆盖 在后台");
            resetNotificationCount();
            cancelNotificaton();

    }

    void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }
    
    void cancelNotificaton() {
        if (notificationManager != null)
            Log.e(TAG,"notifyID " +notifyID);
        notificationManager.cancelAll();
    }



    public synchronized void onNewMsg(EMMessage message,String body, boolean isNotifi) {
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            Log.e(TAG,"App在后台");
            sendNotification(message, body,isNotifi);
        } else {
            Log.e(TAG,"App在界面显示");
            sendNotification(message,body, isNotifi);

        }

        vibrateAndPlayTone(message);
    }

    public synchronized void onNewMsg(EMMessage message,String body) {
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            Log.e(TAG,"App在后台");
            sendNotification(message, body,false);
        } else {
            Log.e(TAG,"App在界面显示");
            sendNotification(message,body, false);

        }
        vibrateAndPlayTone(message);
    }

    public synchronized void onNewMsg(EMMessage message,Boolean isNotifi) {
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            Log.e(TAG,"App在后台");
            sendNotification(message, "",isNotifi);
        } else {
            Log.e(TAG,"App在界面显示");
            sendNotification(message,"", isNotifi);

        }
        vibrateAndPlayTone(message);
    }


    public synchronized void onNewMesg(List<EMMessage> messages,String body) {
        if(EaseCommonUtils.isSilentMessage(messages.get(messages.size()-1))){
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        if(!settingsProvider.isMsgNotifyAllowed(null)){
            return;
        }
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            sendNotification(messages,body, false);
        } else {
            sendNotification(messages, body,true);
        }
        vibrateAndPlayTone(messages.get(messages.size()-1));
    }



    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     * @param messages
     * @param isForeground
     */
    protected void sendNotification (List<EMMessage> messages,String body ,boolean isForeground){
        for(EMMessage message : messages){
            if(!isForeground){
                notificationNum++;
                fromUsers.add(message.getFrom());
            }
        }
        sendNotification(messages.get(messages.size()-1),body, isForeground, false);
    }


    
    protected void sendNotification (EMMessage message,String body, boolean isForeground){
        sendNotification(message,body, isForeground, true);
    }
    
    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     * @param message
     */
    protected void sendNotification(EMMessage message,String body ,boolean isForeground, boolean numIncrease) {
        mUsername = message.getFrom();
        if (mUsername.length() >= 11){
            mUsername = "宝贝";
        }

        try {
            String substring = body.substring(body.indexOf(":")+1);

//            Log.e(TAG,"substring " + substring);
//            String[] split = bodys.split(":");
//            Log.e(TAG,split.length+" split.length");
//            if (split.length >= 1){
//                mText = mUsername +": "+split[1];
//                if (mUsername.equals("hojy")){
//                    mText = split[1];
//                }
//            }else {
//                mText = "";
//            }
            if (mUsername.equals("hojy")){
                    mText = substring;
            }else {
            mText = mUsername + substring;
            }
            String notifyText = "微聊";
            Log.d(TAG, "notifyText: " + notifyText);
            Log.d(TAG, "sendNotification :  body: " + body);
            Log.d(TAG, "sendNotification :  bodys~~~~~~: " + mText);

            switch (message.getType()) {
            case TXT:
                mText += msgs[0];
                break;

            case IMAGE:
                notifyText += msgs[1];
                break;

            case VOICE:
                notifyText += msgs[2];
                break;

            case LOCATION:
                notifyText += msgs[3];
                break;

            case VIDEO:
                notifyText += msgs[4];
                break;

            case FILE:
                notifyText += msgs[5];
                break;

            default:
                mText += msgs[0];
                break;
            }


            
            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());
            
            // notification title
            String contentTitle = appname;
            if (notificationInfoProvider != null) {
                String customNotifyText = notificationInfoProvider.getDisplayedText(message);
                String customCotentTitle = notificationInfoProvider.getTitle(message);
                if (customNotifyText != null){
                    notifyText = customNotifyText;
                }
                    
                if (customCotentTitle != null){
                    contentTitle = customCotentTitle;
                }   
            }

            // create and send notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                                                        .setSmallIcon(appContext.getApplicationInfo().icon)
                                                                        .setWhen(System.currentTimeMillis())
                                                                        .setAutoCancel(true);


            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);


            if (notificationInfoProvider != null) {
                msgIntent = notificationInfoProvider.getLaunchIntent(message);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            if(numIncrease){
                // prepare latest event info section
                if(!isForeground){
                    notificationNum++;
                    fromUsers.add(message.getFrom());
                }
            }

            int fromUsersNum = fromUsers.size();
//            String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2",Integer.toString(notificationNum));
            if (EMMessage.Type.TXT.equals(message.getType())){
               mSummaryBody = mText;
            }else {
            mSummaryBody = "微聊发来语音消息";
            }
            Log.d("ChatActivity", "sendNotification:message.getFrom()  " + message.getFrom());
            Log.d("ChatActivity", "summaryBody  " + mSummaryBody);
            if (notificationInfoProvider != null) {
                // lastest text
                String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum,notificationNum);
                Log.d("ChatActivity", "sendNotification:message.getFrom()  " + message.getFrom());
                if (customSummaryBody != null){
                    mSummaryBody = customSummaryBody;
                }
                // small icon
                int smallIcon = notificationInfoProvider.getSmallIcon(message);
                if (smallIcon != 0){
                    mBuilder.setSmallIcon(smallIcon);
                }
            }

            if (mBigTextStyle == null){
                mBigTextStyle = new NotificationCompat.BigTextStyle();
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            mBuilder.setContentTitle(contentTitle);
            }
            mBuilder.setTicker(mSummaryBody);
            mBuilder.setContentText(mSummaryBody);
            mBuilder.setContentIntent(pendingIntent);
             mBuilder.setNumber(notificationNum);
            // 设置通知样式为大型文本样式
            mBuilder.setStyle(mBigTextStyle.bigText(mSummaryBody));


            Notification notification = mBuilder.build();

//            //建立一个RemoteView的布局，并通过RemoteView加载这个布局
//            if (mRemoteViews == null){
//                mRemoteViews = new RemoteViews(appContext.getPackageName(), R.layout.layout_notification);
//            }
//            mRemoteViews.setTextViewText(R.id.tv_message,mSummaryBody);
////            remoteViews.setChronometer(R.id.tv_time,System.currentTimeMillis(),null,true);
//            notification.contentView = mRemoteViews;


            notification.contentIntent = pendingIntent;//通知绑定 PendingIntent
            notification.flags = Notification.FLAG_AUTO_CANCEL;//设置自动取消
                Log.e(TAG,"isForeground " +isForeground);
            if (isForeground) {
                Log.e(TAG,"isForeground foregroundNotifyID " + foregroundNotifyID);
                notificationManager.notify(foregroundNotifyID, notification);
//                notificationManager.cancel(foregroundNotifyID);
            } else {
                Log.e(TAG,"isForeground notifyID 22222 = " + notifyID);
                notificationManager.notify(notifyID++, notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * vibrate and  play tone
     */
    public void vibrateAndPlayTone(EMMessage message) {
        if(message != null){
            if(EaseCommonUtils.isSilentMessage(message)){
                return;
            } 
        }
        
        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }
        
        try {
            lastNotifiyTime = System.currentTimeMillis();
            
            // check if in silent mode
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                EMLog.e(TAG, "in slient mode now");
                return;
            }
            EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
            if(settingsProvider.isMsgVibrateAllowed(message)){
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);
            }

            if(settingsProvider.isMsgSoundAllowed(message)){
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        EMLog.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                        return;
                    }
                }
                
                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;
                    
                    ringtone.play();
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * vibrate and  play tone
     */
    public void vibrateAndPlayTone() {


        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }

        try {
            lastNotifiyTime = System.currentTimeMillis();

            // check if in silent mode
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                EMLog.e(TAG, "in slient mode now");
                return;
            }


            EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);

                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        EMLog.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                        return;
                    }
                }

                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;

                    ringtone.play();
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }).start();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * set notification info Provider
     * 
     * @param provider
     */
    public void setNotificationInfoProvider(EaseNotificationInfoProvider provider) {
        notificationInfoProvider = provider;
    }


    protected EaseNotificationInfo mEaseNotificationInfo;

    public void setEaseNotificationInfo(EaseNotificationInfo easeNotificationInfo){
        mEaseNotificationInfo = easeNotificationInfo;
    }




    public interface EaseNotificationInfo{
        Intent getLaunchIntent(MQTTModel message);
    }



    public interface EaseNotificationInfoProvider {
        /**
         * set the notification content, such as "you received a new image from xxx"
         * 
         * @param message
         * @return null-will use the default text
         */
        String getDisplayedText(EMMessage message);

        /**
         * set the notification content: such as "you received 5 message from 2 contacts"
         * 
         * @param message
         * @param fromUsersNum- number of message sender
         * @param messageNum -number of messages
         * @return null-will use the default text
         */
        String getLatestText(EMMessage message, int fromUsersNum, int messageNum);

        /**
         * 设置notification标题
         * 
         * @param message
         * @return null- will use the default text
         */
        String getTitle(EMMessage message);

        /**
         * set the small icon
         * 
         * @param message
         * @return 0- will use the default icon
         */
        int getSmallIcon(EMMessage message);

        /**
         * set the intent when notification is pressed
         * 
         * @param message
         * @return null- will use the default icon
         */
        Intent getLaunchIntent(EMMessage message);
    }
}
