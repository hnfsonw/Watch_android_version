package com.loybin.baidumap.ui.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.SplashPresenter;
import com.loybin.baidumap.ui.view.VesionDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.util.UIUtils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述:  Splash view
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private SplashPresenter mSplashPresenter;

    private SharedPreferences mGlobalVariablesp;
    private ResponseInfoModel.ResultBean mResult;
    private VesionDialog mVesionDialog;
    //强制升级
    private static final int FORCEDTOUPGRADE = 1;
    private AlertDialog.Builder mDialog;
    private boolean isNetWork4G;
    private Handler mHandler;
    private boolean isCancelInstall;
    private boolean mIsLogin;
    private int mType;
    public boolean isDownload;
    public int mLoginState = 0;
    private Boolean mHasNewVesion = false;
    private String mPhone;
    private String mDeviceSecret;
    private String mProductKey;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }


    @Override
    protected void init() {
        mSplashPresenter = new SplashPresenter(this, this);
        mGlobalVariablesp = getSharedPreferences("globalvariable", 0);
        mIsLogin = mGlobalVariablesp.getBoolean("login", false);
        mHandler = new Handler();
        LogUtils.e(TAG, "环信是否登入  " + EMClient.getInstance().isLoggedInBefore());
        LogUtils.e(TAG, "是否连接上了环信服务器 " + EMClient.getInstance().isConnected());
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            // 加载所有会话到内存
            EMClient.getInstance().chatManager().loadAllConversations();
            // 加载所有群组到内存，如果使用了群组的话
            EMClient.getInstance().groupManager().loadAllGroups();
        }

        if (mVesionDialog == null) {
            mVesionDialog = new VesionDialog(this, this);
        }

        if (mIsLogin) {
            mSplashPresenter.checkVersion(MyApplication.sToken, "v" + UIUtils.getVersion(), UIUtils.getVersionCode());
            //默认设置5秒进入主页.如果请求网络慢,
            MyApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e(TAG, "5秒时间到 判断 状态是否还是  " + mLoginState);
                    if (mLoginState == 0 && !mHasNewVesion) {
                        LogUtils.e(TAG, "5秒进入主页. 触发initView");
                        initView();
                    } else {
                        LogUtils.e(TAG, "5秒时间到  已经进入主页了");
                    }
                }
            }, 6000);

        } else {
            MyApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            }, 1000);
        }

    }


    public void initView() {
        try {
            mLoginState++;
            LogUtils.e(TAG, "initView!!!!进来了 " + mLoginState);
            if (mLoginState == 1) {
                Log.e(TAG, "login: " + mIsLogin);
                if (mIsLogin) {
                    //设备IOT信息查询
                    mPhone = mGlobalVariablesp.getString("appAccount", null);
                    mDeviceSecret = mGlobalVariablesp.getString(mPhone+"deviceSecret", null);
                    mProductKey = mGlobalVariablesp.getString("productKey", null);
                    if (mDeviceSecret == null || mProductKey == null || mPhone == null){
                        exitHomeActivity();
                        toActivity(LoginActivity.class);
                        finishActivityByAnimation(this);
                    }else{
                        Constants.productKey = mProductKey;
                        Constants.deviceName = mPhone;
                        Constants.deviceSecret = mDeviceSecret;
                        Constants.subTopic =  "/"+mProductKey+"/"+mPhone+"/get"; //下行
                        Constants.pubTopic = "/"+mProductKey+"/"+mPhone+"/pub";//上行
                        Constants.serverUri = "ssl://"+mProductKey+".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
                        Constants.mqttclientId = mPhone + "|securemode=2,signmethod=hmacsha1,timestamp="+Constants.t+"|";
                    }

                    //退出Home
                    exitHomeActivity();
                    String imei = (String) SharedPreferencesUtils.getParam(this, "imei", "");
                    Long acountId = (Long) SharedPreferencesUtils.getParam(this, "watchAcountId", 0l);
                    if (!imei.equals("") && acountId != 0) {
                        ResponseInfoModel data = (ResponseInfoModel) loadDataFromMem(imei + acountId);
                        LogUtils.d(TAG, "data " + data);
                        if (data != null && data.getResult().getDeviceList().size() > 0) {
                            List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = data.getResult().getDeviceList();
                            collections(deviceList);

                            if (deviceList.get(MyApplication.sDeivceNumber).getImgUrl() != null) {
                                toActivity(DevicesHomeActivity.class, deviceList.get(MyApplication.sDeivceNumber).getImgUrl());
                                finishActivityByAnimation(this);
                            } else {
                                toActivity(DevicesHomeActivity.class, "");
                                finishActivityByAnimation(this);
                            }
                        } else {
                            toActivity(DevicesHomeActivity.class, "");
                            finishActivityByAnimation(this);
                        }
                    } else {
                        toActivity(DevicesHomeActivity.class, "");
                        finishActivityByAnimation(this);
                    }

                } else {
                    //退出Home
                    exitHomeActivity();
                    toActivity(LoginActivity.class);
                    finishActivityByAnimation(this);
                }
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "initview 异常" + e.getMessage());
        }

    }


    //排序
    private void collections(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList) {
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1,
                               ResponseInfoModel.ResultBean.DeviceListBean o2) {
                //降序排序
                return (o2.getIsAdmin() - o1.getIsAdmin());
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            LogUtils.d(TAG, "返回键");
            initView();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {

    }


    @Override
    public void dismissVesion() {
        initView();
    }


    public void chekVersionSuccess(ResponseInfoModel data) {
            dismissLoading();
        try {
            mResult = data.getResult();
            mHasNewVesion = mResult.getHasNewVesion();
            mType = mResult.getType();
            if (mHasNewVesion) {
                //有版本更新
                if (mLoginState == 0) {
                    if (!isFinishing()) {
                        mVesionDialog.show();
                        mVesionDialog.initUserName(mResult.getDesc());
                        if (chekMandatory()) {
                            mVesionDialog.mTvCancel.setVisibility(View.GONE);
                            mVesionDialog.mLlLin.setVisibility(View.GONE);
                        }
                        mVesionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                if (keyCode == event.KEYCODE_BACK) {
                                    LogUtils.d(TAG, "~~返回");
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
                }
            } else {
                LogUtils.d(TAG, "当前是最新版本");
                //当前是最新版本
                MyApplication.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //判断下是否有未删除的安装包
                        File file = new File(getPath());
                        if (file.exists()) {
                            file.delete();
                            LogUtils.e(TAG, "删除了已安装的文件");
                        }
                        if (mLoginState == 0) {
                            initView();
                        }
                    }
                }, 500);
            }
        }catch (Exception e){
            LogUtils.e(TAG,"chekVersionSuccess 异常"+e.getMessage());
        }

    }


    public boolean chekMandatory() {
        if (mType == FORCEDTOUPGRADE) {
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消安装
        isCancelInstall = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (chekMandatory()) {
            //强制升级
        } else {
            if (isCancelInstall) {
                if (isDownload) {
                    //下载中
                } else {
                    initView();
                }
            }
        }

    }


    public String getPath() {
        String dir = Environment.getExternalStorageDirectory() + "/my/" +
                getPackageName() + "/apk/";
        return dir + "updata.apk";
    }


    /**
     * 下载完成
     */
    public void downloadComplete() {
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    File file = new File(getPath());
                    LogUtils.e(TAG, file.getAbsolutePath());
                    //指定启动的这个activity界面，放置到一个新的任务栈里面去。
                    //系统在安装APK 结束之后， 会把当前这个安装activity所处的任务栈直接清除掉。
                    //如果没有指定新的任务栈， 那么这个安装的activity将会放置到咱们应用的早前任务栈去。
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.intent.action.VIEW");
                    //指定安装的文件路径
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(intent);
                    isDownload = false;
                } catch (Exception e) {
                    LogUtils.e(TAG, "下载完成安装异常" + e.getMessage());
                }
            }
        }, 1000);

    }


    @Override
    public void upDate() {
        File file = new File(getPath());
        if (file.exists()) {
            downloadComplete();
            return;
        }
        LogUtils.d(TAG, "isNetWork4G " + isNetWork4G);
        if (isNetWork4G) {
            //提示当前是移动网
            //监听下载进度
            mDialog = new AlertDialog.Builder(this);

            mDialog.setMessage(getString(R.string.mobile_data));
            mDialog.setCancelable(false);
            mDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (chekMandatory()) {
                        dialog.dismiss();
                        finishActivityByAnimation(SplashActivity.this);
                    } else {
                        dialog.dismiss();
                        initView();
                    }


                }
            });
            mDialog.setPositiveButton(getString(R.string.local_tyrants_continued),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LogUtils.e(TAG,"mResult.getUrl() " +mResult.getUrl());
                            mSplashPresenter.download(mResult.getUrl());

                        }
                    });
            mDialog.show();
        } else {
            //当前是wifi
            LogUtils.e(TAG,"mResult.getUrl() " +mResult.getUrl());
            mSplashPresenter.download(mResult.getUrl());
        }
    }


    /**
     * 当前网络是4G的通知
     */
    @Override
    protected void netWork4G() {
        isNetWork4G = true;
        LogUtils.e(TAG, "当前网络是4G");
    }


}
