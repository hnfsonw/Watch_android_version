package com.loybin.baidumap.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.SettingPresenter;
import com.loybin.baidumap.ui.view.LinearTvView;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.ui.view.VesionDialog;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.util.UIUtils;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/26 下午4:03
 * 描   述: 设备设置的视图
 */
public class SettingActivity extends BaseActivity {


    private static final String TAG = "SettingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.lt_change_the_password)
    LinearTvView mLtChangeThePassword;

    @BindView(R.id.lt_clear_the_cache)
    LinearTvView mLtClearTheCache;

    @BindView(R.id.lt_check_version_update)
    LinearTvView mLtCheckTheUpdate;

    @BindView(R.id.lt_about)
    LinearTvView mLtAbout;

    @BindView(R.id.btn_exit)
    Button mBtnEXIT;

    @BindView(R.id.tv_phone)
    TextView mTvPhone;

    private SharedPreferences mGlobalvariable;
    private RemoveDialog mRemoveDialog;
    private SettingPresenter mSettingPresenter;
    private VesionDialog mVesionDialog;
    private ResponseInfoModel.ResultBean mResult;
    private boolean isClertCache;
    private boolean isNetWork4G;
    private AlertDialog.Builder mDialog;
    private String path = FileUtils.getCachePath();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_setting;
    }


    @Override
    protected void init() {
        String flags = getIntent().getStringExtra("flags");
        LogUtils.e(TAG, "flags " + flags);
        mSettingPresenter = new SettingPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        initView();
    }


    private void initView() {
        mLtChangeThePassword.mMageview.setImageResource(R.mipmap.password_change);
        mLtAbout.mMageview.setImageResource(R.mipmap.about);
        mLtClearTheCache.mMageview.setImageResource(R.mipmap.clear_caching);
        mLtCheckTheUpdate.mMageview.setImageResource(R.mipmap.gengxin);

        mTvTitle.setText(getString(R.string.setting));
        if (DevicesHomeActivity.sPhone != null) {
            mTvPhone.setText(DevicesHomeActivity.sPhone);
        }
        mLtCheckTheUpdate.setAttribute("V" + UIUtils.getVersion());
        getFileSIze();
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
        }
        if (mVesionDialog == null) {
            mVesionDialog = new VesionDialog(this, this);
        }

        LogUtils.e(TAG,"path " + path);

    }


    /**
     * 获取缓存大小
     */
    private void getFileSIze() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                LogUtils.e(TAG, "获取缓存大小222");
                String totalCacheSize = "";
                try {
                    totalCacheSize = FileUtils.getTotalCacheSize();
                    LogUtils.e(TAG, "文件大小 " + totalCacheSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String finalTotalCacheSize = totalCacheSize;
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalTotalCacheSize != null) {
                            mLtClearTheCache.setAttribute(finalTotalCacheSize);
                        }
                    }
                });

            }
        });
    }


    @OnClick({R.id.iv_back,
            R.id.lt_change_the_password, R.id.lt_clear_the_cache,
            R.id.lt_check_version_update, R.id.lt_about, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.lt_change_the_password:
                toActivity(ModifyPasswordActivity.class);
                break;

            case R.id.lt_clear_the_cache:
                //清除缓存
                mRemoveDialog.show();
                mRemoveDialog.initTitle(getString(R.string.sure_you_want_to_clear_the_cache), true, getString(R.string.delete_care));
                isClertCache = true;
                break;

            case R.id.lt_check_version_update:
                mSettingPresenter.checkVersion(MyApplication.sToken, "V" + UIUtils.getVersion(), UIUtils.getVersionCode());
                break;

            case R.id.lt_about:
                toActivity(AboutActivity.class);
                break;

            case R.id.btn_exit:
                showDialog();
                break;
        }
    }


    private void showDialog() {
        mRemoveDialog.show();
        mRemoveDialog.initTitle(getString(R.string.confirm_to_exit), false);
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void cancel() {
        if (isClertCache) {
            FileUtils.cleanInternal();
            try {
                mLtClearTheCache.setAttribute(FileUtils.getTotalCacheSize());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < MyApplication.sDeviceList.size(); i++) {
                EMClient.getInstance().chatManager().deleteConversation(MyApplication.sDeviceList.get(i).getGroupId() + "", true);
            }

            isClertCache = false;
        } else {
            try { // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
                dismissLoading();
                showLoading("",mContext);
//                if (mPlayerManager.isPlaying()){
//                    mPlayerManager.stop();
//                }
                XmPlayerManager.release();

                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("LoginPresenter", "退出成功");

                        mGlobalvariable.edit().putBoolean("login", false).putInt("deivceNumber", 0).apply();
                        MyApplication.sDeivceNumber = 0;
                        MyApplication.sAcountId = -1;
                        MyApplication.sInUpdata = true;
                        //清除缓存
                        deleteData();
                        //退出Home
                        exitHomeActivity();
                        LogUtils.e("DevicesHomeActivity", "发送广播成功");
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishActivityByAnimation(SettingActivity.this);
                        dismissLoading();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i("LoginPresenter", "退出失败 " + i + " - " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });


            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
    }


    /**
     * 清除缓存
     */
    private void deleteData() {
        //清除内存
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();
        cacheMap.clear();
        //清除本地
        FileUtils.cleanInternalCache();
    }


    /**
     * 检查版本更新失败的通知
     *
     * @param resultMsg
     */
    public void chekVersionError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 检查版本更新成功的通知
     *
     * @param data
     */
    public void chekVersionSuccess(ResponseInfoModel data) {
        dismissLoading();
        mResult = data.getResult();
        Boolean hasNewVesion = mResult.getHasNewVesion();
        if (hasNewVesion) {
            //有版本更新
            Log.d(TAG, "chekVersionSuccess: " + mResult.getUrl());
            mVesionDialog.show();
            mVesionDialog.initUserName(mResult.getDesc());
        } else {
            //当前是最新版本
            printn(getString(R.string.is_currently_the_latest_version));
        }
    }


    /**
     * 更新版本
     */
    @Override
    public void upDate() {
        String dir = Environment.getExternalStorageDirectory() + "/my/" +
                getPackageName() + "/apk/";
        String path = dir + "updata.apk";
        File file = new File(path);
        if (file.exists()) {
            downloadComplete();
            return;
        }
        if (isNetWork4G) {
            //提示当前是移动网
            //监听下载进度
            mDialog = new AlertDialog.Builder(this);

            mDialog.setMessage(getString(R.string.mobile_data));
            mDialog.setCancelable(false);
            mDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            mDialog.setPositiveButton(getString(R.string.local_tyrants_continued),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSettingPresenter.download(mResult.getUrl());
                        }
                    });
            mDialog.show();
        } else {
            //当前是wifi
            mSettingPresenter.download(mResult.getUrl());
        }
    }


    /**
     * 下载完成
     */
    public void downloadComplete() {
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String dir = Environment.getExternalStorageDirectory() + "/my/" +
                            getPackageName() + "/apk/";
                    String path = dir + "updata.apk";
                    File file = new File(path);
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
                    MyApplication.sInUpdata = true;
                } catch (Exception e) {
                    LogUtils.e(TAG, "下载完成安装异常" + e.getMessage());
                }
            }
        }, 1000);


    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mSettingPresenter.mCall != null) {
            mSettingPresenter.mCall.cancel();
        }
        if (mSettingPresenter.mQueryDeviceStateByDeviceId != null) {
            mSettingPresenter.mQueryDeviceStateByDeviceId.cancel();
        }
        if (mSettingPresenter.mInsertOrUpdateDeviceSwtich != null) {
            mSettingPresenter.mInsertOrUpdateDeviceSwtich.cancel();
        }
        if (mSettingPresenter.mAppSendCMD != null) {
            mSettingPresenter.mAppSendCMD.cancel();
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


    /**
     * 当前网络是wifi的通知
     */
    @Override
    protected void theNetwork() {
//        isNetWork4G = false;
        LogUtils.e(TAG, "当前网络是wifi");
    }

}
