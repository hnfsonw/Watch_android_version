package com.loybin.baidumap.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.wifi.WifiManager;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.WatchSettingPresenter;
import com.loybin.baidumap.ui.view.LinearTvView;
import com.loybin.baidumap.ui.view.WatchOffDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UserUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/12 下午5:20
 * 描   述: 手表设置 view
 */
public class WatchSettingActivity extends BaseActivity {
    private static final String TAG = "WatchSettingActivity";
    private static final int GPS_REQUEST_CODE = 4;
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.lt_stranger_calls)
    LinearTvView mLtStrangerCalls;

    @BindView(R.id.lt_one_way_listening)
    LinearTvView mLtOneWayListening;

    @BindView(R.id.ll_positioning_mode)
    LinearLayout mLlPositioningMode;

    @BindView(R.id.ll_phone_mode)
    LinearTvView mLlPhoneMode;

    @BindView(R.id.tv_location_style)
    public TextView mTvLocationStyle;

    @BindView(R.id.lt_time_switch_machine)
    LinearTvView mLtTimeSwitchMachine;

    @BindView(R.id.tv_off)
    TextView mTvOff;

    @BindView(R.id.ll_watch_off)
    LinearLayout mLlWatchOff;

    @BindView(R.id.lt_wifi_setting)
    LinearTvView mLtWifiSetting;

    @BindView(R.id.lt_wifi_settings)
    LinearTvView mLtWifiSettings;

    @BindView(R.id.lt_class_ban)
    LinearTvView mLtClassBan;

    @BindView(R.id.lt_alarm_clock)
    LinearTvView mLtAlarmClock;

    private WatchSettingPresenter mWatchSettingPresenter;
    private SharedPreferences mGlobalvariable;
    private String mImei;
    private String mAppAccount;
    private String mDevicePowerId;
    private int mLocationStyle;
    private int mMobileStyle;
    private String mBootState;
    private int mSwitchMachine;
    private int mState;
    private int mPhoneModel;
    private WifiManager mWifiManager;
    private boolean mIsWiFiSetting;
    private boolean mNetwork = true;
    private boolean mOPen;
    private int mSoftVersion;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_watch_setting;
    }

    @Override
    protected void init() {
        if (mWatchSettingPresenter == null) {
            mWatchSettingPresenter = new WatchSettingPresenter(this, this);
        }
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mImei = mGlobalvariable.getString("imei", "");
        mAppAccount = mGlobalvariable.getString("appAccount", "");
        String stringExtra = getIntent().getStringExtra(BABY);
        if (stringExtra != null){
            mSoftVersion = Integer.parseInt(stringExtra);
        }
        initView();
        initListener();
        initData();
    }


    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (UserUtil.isOPen(this)) {
            //wifi设置
            mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (mWifiManager.isWifiEnabled()) {
                toActivity(WiFiSettingActivity.class);
            } else {
                LogUtils.e(TAG, "未打开wifi~~~~!!!!!!");
                mIsWiFiSetting = true;
                mWifiManager.setWifiEnabled(true);
            }

        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    printn("未开启GPS权限");
                                }
                            })
                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })
                    .setCancelable(false)
                    .show();

        }
    }


    private void initData() {
    }


    private void initListener() {
        mLtTimeSwitchMachine.mIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNetwork) {
                    mLtTimeSwitchMachine.toggle();
                    boolean toggle = mLtTimeSwitchMachine.getToggle();
                    Log.d(TAG, "onClick: " + toggle);
                    if (toggle) {
                        mSwitchMachine = 1;
                    } else {
                        mSwitchMachine = 0;
                    }
                    mWatchSettingPresenter.saveState(MyApplication.sToken, mDevicePowerId, mSwitchMachine);
                } else {
                    Toast.makeText(WatchSettingActivity.this,
                            getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.watch_setting));
        mLtStrangerCalls.mMageview.setImageResource(R.mipmap.intercept_the_stranger_calls);
        mLtOneWayListening.mMageview.setImageResource(R.mipmap.one_way_listening);
        mLlPhoneMode.mMageview.setImageResource(R.mipmap.phone_mode);
        mLtTimeSwitchMachine.mMageview.setImageResource(R.mipmap.time_switch_machine);
        mLtWifiSetting.mMageview.setImageResource(R.mipmap.wifi);
        mLtWifiSettings.mMageview.setImageResource(R.mipmap.wifi);
        mLtClassBan.mMageview.setImageResource(R.mipmap.class_setting);
        mLtStrangerCalls.mIvSwitch.setVisibility(View.VISIBLE);
        mLtTimeSwitchMachine.mIvSwitch.setVisibility(View.VISIBLE);
        mLlPhoneMode.mIvSwitch.setVisibility(View.VISIBLE);

//        if (mSoftVersion >= 21){
//            mLtWifiSettings.setVisibility(View.VISIBLE);
//        }else {
//            mLtWifiSettings.setVisibility(View.GONE);
//        }

    }


    @Override
    protected void dismissNewok() {
        if (mWatchSettingPresenter.mAppSendCMD != null) {
            mWatchSettingPresenter.mAppSendCMD.cancel();
        }

        if (mWatchSettingPresenter.mInsertOrUpdateDeviceSwtich != null) {
            mWatchSettingPresenter.mInsertOrUpdateDeviceSwtich.cancel();
        }

        if (mWatchSettingPresenter.mQueryDeviceStateByDeviceId != null) {
            mWatchSettingPresenter.mQueryDeviceStateByDeviceId.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.lt_stranger_calls, R.id.lt_one_way_listening, R.id.lt_alarm_clock,
            R.id.ll_positioning_mode, R.id.ll_phone_mode, R.id.lt_time_switch_machine,
            R.id.ll_watch_off, R.id.lt_wifi_setting, R.id.lt_wifi_settings, R.id.lt_class_ban})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.lt_stranger_calls:
                //拒绝陌生人来电
                if (mNetwork) {
                    mLtStrangerCalls.toggle();
                    boolean toggle = mLtStrangerCalls.getToggle();
                    if (toggle) {
                        mState = 1;
                    } else {
                        mState = 0;
                    }

                    mWatchSettingPresenter.insertOrUpdateDeviceSwtich(MyApplication.sToken,
                            MyApplication.sDeviceId, MyApplication.sAcountId, mState, true);
                    Log.d(TAG, "mLtStrangerCalls: " + toggle);
                } else {
                    Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.lt_one_way_listening:
                //单向聆听
                toActivity(OneWayListeningActivity.class);
                break;

            case R.id.lt_class_ban:
                //上课禁用
                toActivity(ClassBanActivity.class);
                break;

            case R.id.lt_alarm_clock:
                //闹钟
                toActivity(AlarmClockActivity.class);
                break;
            case R.id.lt_wifi_setting:

                openGPSSettings();

                break;

            case R.id.ll_positioning_mode:
                //定位模式
                toActivity(100, PositioningModeActivity.class, mLocationStyle + "");
                break;

            case R.id.ll_phone_mode:
                //电话模式
                if (mNetwork) {
                    mLlPhoneMode.toggle();
                    boolean isPhoneMode = mLlPhoneMode.getToggle();
                    LogUtils.e(TAG, "ll_phone_mode " + isPhoneMode);
                    if (isPhoneMode) {
                        mPhoneModel = 0;
                    } else {
                        mPhoneModel = 1;
                    }
                    mWatchSettingPresenter.insertOrUpdateDeviceSwtich(MyApplication.sToken,
                            MyApplication.sDeviceId, MyApplication.sAcountId,
                            mPhoneModel, false);
                } else {
                    Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.lt_time_switch_machine:
                //定时开关机
                toActivity(MachineTimeActivity.class);
                break;

            case R.id.ll_watch_off:
                //强制手表关机
                WatchOffDialog dialog = new WatchOffDialog(this, this);
                dialog.show();
                break;

            case R.id.lt_wifi_settings:
                toActivity(WifiSettingsActivity.class);
                break;

            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 强制手表关机
     */
    public void watchOff() {
        if (mBootState == null) {
            printn(getString(R.string.no_network));
            return;
        }
        LogUtils.e(TAG, "mBootState " + mBootState);
        if (mBootState.equals("1")) {
            mWatchSettingPresenter.sendCMDWatchOff(Constants.COMMAND10002, Constants.message10002,
                    MyApplication.sToken, mAppAccount, mImei);
        } else {
            printn(mContext.getString(R.string.the_watch_is_not_online));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            if (data != null) {
                int locationStyle = data.getIntExtra("locationStyle", -1);
                LogUtils.e(TAG, "返回界面的数据locationStyle  " + locationStyle);
                mLocationStyle = locationStyle;
                mWatchSettingPresenter.switchLocationStyle(locationStyle);
            }
        }

        if (requestCode == GPS_REQUEST_CODE) {
            //做需要做的事情，比如再次检测是否打开GPS了 或者定位
            openGPSSettings();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mWatchSettingPresenter.queryDeviceStateByDeviceId(MyApplication.sToken, MyApplication.sDeviceId);
    }


    /**
     * 获取状态成功的通知
     *
     * @param result
     */
    public void onSuccessDeviceState(ResponseInfoModel.ResultBean result) {
        dismissLoading();
        LogUtils.e(TAG, "拒绝陌生人来电的状态 " + result.getStrangeCallSwitch());
        //设备设置表
        mDevicePowerId = result.getDevicePowerId();
        int strangeCallSwitch = Integer.parseInt(result.getStrangeCallSwitch());
        int fenceSwitch = Integer.parseInt(result.getFenceSwitch());
        int forbiddenTimeSwitch = Integer.parseInt(result.getForbiddenTimeSwitch());
        int devicePowerSwitch = Integer.parseInt(result.getDevicePowerSwitch());
        mLocationStyle = result.getLocationStyle();
        mMobileStyle = result.getMobileStyle();
        mBootState = result.getBootState();
        mSwitchMachine = devicePowerSwitch;
        chekDeviceState(strangeCallSwitch, fenceSwitch, forbiddenTimeSwitch, devicePowerSwitch, mBootState
                , mLocationStyle, mMobileStyle);
    }


    /**
     * 判断状态
     *
     * @param strangeCallSwitch   拒绝陌生人来电的开关
     * @param fenceSwitch         电子围栏开启状态，0:关闭，1:开启
     * @param forbiddenTimeSwitch 上课禁用开启状态，0:关闭，1:开启
     * @param devicePowerSwitch   定时开关机开启状态，0:关闭，1:开启
     * @param bootState           设备开关机开启状态，0:关闭，1:开启
     * @param locationStyle       定位模式， 1:精准模式、2:正常模式、3:省电模式、4：测试模
     * @param mobileStyle         手表电话模式 0 VoLTE 1 CSFB
     */
    private void chekDeviceState(int strangeCallSwitch, int fenceSwitch, int forbiddenTimeSwitch,
                                 int devicePowerSwitch, String bootState, int locationStyle, int mobileStyle) {
        //拒绝陌生人来电的开关
        if (strangeCallSwitch == 0) {
            mLtStrangerCalls.mIvSwitch.setImageResource(R.mipmap.off);
            mLtStrangerCalls.mIsToggle = false;
        } else {
            mLtStrangerCalls.mIvSwitch.setImageResource(R.mipmap.on);
            mLtStrangerCalls.mIsToggle = true;
        }

        LogUtils.e(TAG, "定时开关机开启状态 " + devicePowerSwitch);
        if (devicePowerSwitch == 0) {
            mLtTimeSwitchMachine.mIvSwitch.setImageResource(R.mipmap.off);
            mLtTimeSwitchMachine.mIsToggle = false;
        } else {
            mLtTimeSwitchMachine.mIvSwitch.setImageResource(R.mipmap.on);
            mLtTimeSwitchMachine.mIsToggle = true;
        }

        LogUtils.e(TAG, "手表电话模式 " + mobileStyle);

        if (mobileStyle == 0) {
            mLlPhoneMode.mIvSwitch.setImageResource(R.mipmap.on);
            mLlPhoneMode.mIsToggle = true;
        } else {
            mLlPhoneMode.mIvSwitch.setImageResource(R.mipmap.off);
            mLlPhoneMode.mIsToggle = false;
        }


        mWatchSettingPresenter.switchLocationStyle(locationStyle);

        mLtStrangerCalls.mIvSwitch.setVisibility(View.VISIBLE);
    }


    /**
     * 强制手表关机
     *
     * @param body
     */
    public void onCMDSuccess(ResponseInfoModel body) {
        dismissLoading();
        printn(mContext.getString(R.string.shutdown_instructions));

    }

    @Override
    protected void theNetwork() {
        mNetwork = true;
    }

    @Override
    protected void noNetwork() {
        mNetwork = false;
    }
}
