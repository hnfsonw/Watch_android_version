package com.loybin.baidumap.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
//import android.support.multidex.MultiDexApplication;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.service.PlayServiceStub;
import com.loybin.baidumap.widget.broadcast.MyPlayerReceiver;
import com.loybin.baidumap.service.PlayService;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater;
import com.ximalaya.ting.android.opensdk.util.BaseUtil;
import com.ximalaya.ting.android.opensdk.util.Logger;
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager;
import com.ximalaya.ting.android.sdkdownloader.http.RequestParams;


import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.widget.broadcast.LiveService;
import com.loybin.baidumap.widget.chatrow.Constant;
import com.loybin.baidumap.widget.chatrow.HxEaseuiHelper;
import com.umeng.analytics.MobclickAgent;
import com.ximalaya.ting.android.sdkdownloader.http.app.RequestTracker;
import com.ximalaya.ting.android.sdkdownloader.http.request.UriRequest;

import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 程序的入口 初始化信息
 */
public class MyApplication extends MultiDexApplication {
    public static final String TAG = "MyApplication";
    private static Handler sHandler;
    public static MyApplication sInstance;
    public static int sDeivceNumber = 0;
    public static int sListSize = 0;
    public static String sToken;
    public static long sAcountId = -1;
    public static int sDeviceId = -1;
    public static boolean sHeadset;
    public static ResponseInfoModel.ResultBean.DeviceListBean sDeviceListBean;
    public static ResponseInfoModel.ResultBean sResult;
    public static List<ResponseInfoModel.ResultBean.DeviceListBean> sDeviceList;
    public static String sUserAcountId = "";
    public static String sUserdeviceId = "";
    private SharedPreferences mGlobalVariablesp;
    public static boolean sInUpdata = false;
    private PlayServiceStub mService;
    public static boolean isFrontDesk = true;
    //进程保活
    public static final int LIVE_JOB_ID = 0;
    private JobScheduler mJS;
    public static final String LIVE_SERVICE = "com.cl.bjhd.service.LiveService";
    // 记录是否已经初始化
    private boolean isInit = false;
    //定义缓存的存储结构
    private static Map<String, Object> mCacheMap = new HashMap<>();
    private CommonRequest mXimalaya;
    private String mAppSecret;


    public static Handler getHandler() {
        return sHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        initService();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        sInstance = this;
        sHandler = new Handler();
        sDeviceListBean = new ResponseInfoModel.ResultBean.DeviceListBean();
        sDeviceList = new ArrayList<>();
        sResult = new ResponseInfoModel.ResultBean();
        mGlobalVariablesp = sInstance.getSharedPreferences("globalvariable", 0);
        int deivceNumber = mGlobalVariablesp.getInt("deivceNumber", 0);
        sAcountId = mGlobalVariablesp.getLong("acountId", 0);
        sToken = mGlobalVariablesp.getString("token", "");
        sDeivceNumber = deivceNumber;

//        //判断当前系统版本是否5.0以上,开启服务进程
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            if(!ServiceUtils.isServiceRunning(sInstance, LIVE_SERVICE)){
//                startService(new Intent(this, LiveService.class));
//                scheduleJob(getJobInfo());
//            }
//        }ffsdfa
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        // 初始化环信SDK
        initEasemob();
        //初始化友盟
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
//        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,
//                "594491364ad156566a0011cf","Baidushoujizhushou", MobclickAgent.EScenarioType.E_UM_NORMAL));
//        场景类型设置接口:
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);
        //初始化喜马拉雅
        initXMLY();

        //初始化微信分享
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }





    private void initXMLY() {
        x.Ext.init(this);
//        x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.
        try {
            String mp3 = getExternalFilesDir("mp3").getAbsolutePath();
            LogUtils.e(TAG, "地址是" + mp3);

            if (BaseUtil.isMainProcess(this)) {
                XmDownloadManager.Builder(this)
                        .maxDownloadThread(1)            // 最大的下载个数 默认为1 最大为3
                        .maxSpaceSize(Long.MAX_VALUE)    // 设置下载文件占用磁盘空间最大值，单位字节。不设置没有限制
                        .connectionTimeOut(15000)        // 下载时连接超时的时间 ,单位毫秒 默认 30000
                        .readTimeOut(15000)                // 下载时读取的超时时间 ,单位毫秒 默认 30000
                        .fifo(false)                    // 等待队列的是否优先执行先加入的任务. false表示后添加的先执行(不会改变当前正在下载的音频的状态) 默认为true
                        .maxRetryCount(3)                // 出错时重试的次数 默认2次
                        .progressCallBackMaxTimeSpan(1000)//  进度条progress 更新的频率 默认是800
                        .requestTracker(requestTracker)    // 日志 可以打印下载信息
                        .savePath(mp3)    // 保存的地址 会检查这个地址是否有效
                        .create();
            }

        } catch (Exception e) {

        }

        XmNotificationCreater instanse = XmNotificationCreater.getInstanse(this);
        instanse.setNextPendingIntent((PendingIntent) null);
        instanse.setPrePendingIntent((PendingIntent) null);
        instanse.setStartOrPausePendingIntent((PendingIntent) null);
        LogUtils.e(TAG, "player!!!!!!!!");
        String actionName = "com.app.test.android.Action_Close";
        Intent intent = new Intent(actionName);
        intent.setClass(this, MyPlayerReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, intent, 0);
        instanse.setClosePendingIntent(broadcast);
        mXimalaya = CommonRequest.getInstanse();

        mAppSecret = "48d762f0ca8bd3d7ed1fb4ba1884ebe2";
        mXimalaya.setAppkey("e39fc250b92ac0f401f04dc1d6ca6cfa");
        mXimalaya.setPackid("com.hojy.happyfruit");

        mXimalaya.init(this, mAppSecret);
//        initService();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
//        unbindService(mMediaServiceConn);
    }

    private void initService() {
        LogUtils.e(TAG, "initService!! " + Thread.currentThread().getName());
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);

//        Intent intent1 = new Intent(this,PlayService.class);
//        bindService(intent1, mMediaServiceConn, BIND_AUTO_CREATE);
    }


    private ServiceConnection mMediaServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = (PlayServiceStub) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;

        }
    };

    public PlayServiceStub getService() {
        return mService;
    }


    @SuppressLint("NewApi")
    public void scheduleJob(JobInfo info) {
//		LogUtils.i("xhd", "JobSchedule schedule JobInfo");
        mJS = (JobScheduler) sInstance.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJS.schedule(getJobInfo());
    }


    @SuppressLint("NewApi")
    public JobInfo getJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(LIVE_JOB_ID,
                new ComponentName(sInstance, LiveService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);//任务不需要网络
        builder.setPersisted(true);//设备重新启动时继续执行
        builder.setPeriodic(100);//该项工作多少秒重复（不定时执行，但该毫秒之内只会被执行一次）
        return builder.build();
    }


    public static Map<String, Object> getCacheMap() {
        return mCacheMap;
    }


    /**
     * 初始化环信SDK
     */
    private void initEasemob() {
        int pid = android.os.Process.myPid();//获取当前进程的id
        String processAppName = getProcessName(pid);//通过PID获取进程名

        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次

        //App默认的进程名就是包名
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        HxEaseuiHelper.getInstance().init(sInstance, initOptions());
        //设置全局监听
//        setGlobalListeners();

        // 设置初始化已经完成
        isInit = true;
    }


    /**
     * 设置一个全局的监听
     */
    private void setGlobalListeners() {
        // create the global connection listener
        EMConnectionListener connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {// 显示帐号已经被移除
                    LogUtils.e("DevicesHomeActivity", "全局的监听~~显示帐号已经被移除");
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {// 显示帐号在其他设备登录
                    LogUtils.e("DevicesHomeActivity", "全局的监听~~显示帐号在其他设备登录");
                } else {
                    if (NetUtils.hasNetwork(sInstance)) {
                        LogUtils.e("DevicesHomeActivity", "全局的监听~~连接不到服务器");
                    } else {
                        LogUtils.e("DevicesHomeActivity", "全局的监听~~当前网络不可用,请检查网络设置");
                    }
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events

            }
        };

        EMClient.getInstance().addConnectionListener(connectionListener);
    }


    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        options.setHuaweiPushAppId("100017619");
        options.setMipushConfig(Constant.XIAOMI_ID, Constant.XIAOMI_KEY);
        // 设置自动登录
        options.setAutoLogin(false);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        options.setRequireAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(true);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(true);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);

        return options;
    }


    /**
     * 根据PID获取当前进程的名字
     *
     * @param pID
     * @return
     */
    private String getProcessName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();//获取运行进程列表
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        //遍历所有的当前运行的进程
        while (i.hasNext()) {
            //获取进程的信息
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                //判断是否是当前进程对应info
                if (info.pid == pID) {
                    //获取当前进程的进程名
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    private RequestTracker requestTracker = new RequestTracker() {
        @Override
        public void onWaiting(RequestParams params) {
            Logger.log("TingApplication : onWaiting " + params);
        }

        @Override
        public void onStart(RequestParams params) {
            Logger.log("TingApplication : onStart " + params);
        }

        @Override
        public void onRequestCreated(UriRequest request) {
            Logger.log("TingApplication : onRequestCreated " + request);
        }

        @Override
        public void onSuccess(UriRequest request, Object result) {
            Logger.log("TingApplication : onSuccess " + request + "   result = " + result);
        }

        @Override
        public void onRemoved(UriRequest request) {
            Logger.log("TingApplication : onRemoved " + request);
        }

        @Override
        public void onCancelled(UriRequest request) {
            Logger.log("TingApplication : onCanclelled " + request);
        }

        @Override
        public void onError(UriRequest request, Throwable ex, boolean isCallbackError) {
            Logger.log("TingApplication : onError " + request + "   ex = " + ex + "   isCallbackError = " + isCallbackError);
        }

        @Override
        public void onFinished(UriRequest request) {
            Logger.log("TingApplication : onFinished " + request);
        }
    };





}
