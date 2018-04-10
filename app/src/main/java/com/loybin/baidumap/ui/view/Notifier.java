package com.loybin.baidumap.ui.view;

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
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.hyphenate.util.EMLog;
import com.hyphenate.util.EasyUtils;
import com.loybin.baidumap.model.MQTTModel;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashSet;
import java.util.Locale;

/**
 * new message notifier class
 * 
 * this class is subject to be inherited and implement the relative APIs
 */
public class Notifier {
    private final static String TAG = "DevicesHomeActivity";
    private static final String TAG2 = "MQTT";
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
    protected EaseNotificationInfo notificationInfoProvider;
    private String mText;
    private String mSummaryBody;
    private String mUsername;
    private RemoteViews mRemoteViews;
    private NotificationCompat.BigTextStyle mBigTextStyle;
    protected EaseNotificationInfo mEaseNotificationInfo;


    public Notifier() {
    }

    /**
     * this function can be override
     * @param context
     * @return
     */
    public Notifier init(Context context){
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



    public synchronized void onNewMsg(MQTTModel message, boolean isNotifi) {
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG2, "App在后台");
            Log.e(TAG,"App在后台");
            sendNotification(message,isNotifi);
        } else {
            EMLog.d(TAG2, "App在界面显示");
            Log.e(TAG,"App在界面显示");
            sendNotification(message,isNotifi);
        }

        vibrateAndPlayTone(message);
    }

    protected void sendNotification (MQTTModel mqttModel, boolean isForeground){
        sendNotification(mqttModel, isForeground, true);
    }

    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     * @param message
     */
    protected void sendNotification(MQTTModel message,boolean isForeground, boolean numIncrease) {
        mUsername = message.getFrom();
        if (mUsername.length() >= 11){
            mUsername = "宝贝";
        }

        try {
            String body = message.getMsg();
            String substring = body.substring(body.indexOf(":")+1);

            if (mUsername.equals("HOJY")){
                    mText = substring;
            }else {
            mText = mUsername + substring;
            }
            Log.d(TAG, "sendNotification :  body: " + body);
            Log.d(TAG, "sendNotification :  bodys~~~~~~: " + mText);
            mText += msgs[0];

            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

            // notification title
            String contentTitle = appname;


            // create and send notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                                                                        .setSmallIcon(appContext.getApplicationInfo().icon)
                                                                        .setWhen(System.currentTimeMillis())
                                                                        .setAutoCancel(true);

            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);


            if (mEaseNotificationInfo != null) {
                LogUtils.e(TAG,"notificationInfoProvider != null");
                msgIntent = mEaseNotificationInfo.getLaunchIntent(message);
            }else {
                LogUtils.e(TAG,"notificationInfoProvider ======== null");
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent,PendingIntent.FLAG_UPDATE_CURRENT);


            if(numIncrease){
                if(!isForeground){
                    notificationNum++;
                    fromUsers.add(message.getFrom());
                }
            }

            int fromUsersNum = fromUsers.size();
//            String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2",Integer.toString(notificationNum));
               mSummaryBody = mText;
            Log.d("ChatActivity", "sendNotification:message.getFrom()  " + message.getFrom());
            Log.d("ChatActivity", "summaryBody  " + mSummaryBody);

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
    public void vibrateAndPlayTone(MQTTModel message) {
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
     */
    public void setEaseNotificationInfo(EaseNotificationInfo easeNotificationInfo){
        mEaseNotificationInfo = easeNotificationInfo;
    }


    public interface EaseNotificationInfo{
        Intent getLaunchIntent(MQTTModel message);
    }

}
