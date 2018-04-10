package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.WatchSettingActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/14 上午10:50
 * 描   述: 手表设置 业务类
 */
public class WatchSettingPresenter extends BasePresenter {
    private static final String TAG = "WatchSettingActivity";
    private Context mContext;
    private WatchSettingActivity mWatchSettingActivity;
    public Call<ResponseInfoModel> mQueryDeviceStateByDeviceId;
    public Call<ResponseInfoModel> mInsertOrUpdateDeviceSwtich;
    public Call<ResponseInfoModel> mAppSendCMD;


    public WatchSettingPresenter(Context context, WatchSettingActivity watchSettingActivity) {
        super(context);
        mContext = context;
        mWatchSettingActivity = watchSettingActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        ResponseInfoModel.ResultBean result = body.getResult();
        LogUtils.e(TAG, "群聊设备获取成功" + body.getResultMsg());
        LogUtils.e(TAG, "设备开启关闭状态" + body.getResult().getBootState());
        LogUtils.e(TAG, "设备运行模式" + body.getResult().getBootState());
        LogUtils.e(TAG, "设备电池电量" + body.getResult().getBootState());
        LogUtils.e(TAG, "拒接陌生来电开启状态 " + body.getResult().getStrangeCallSwitch());
        LogUtils.e(TAG, "定位模式 " + body.getResult().getLocationStyle());
        LogUtils.e(TAG, "打电话模式 " + body.getResult().getMobileStyle());
        mWatchSettingActivity.onSuccessDeviceState(result);
    }


    @Override
    protected void onFaiure(ResponseInfoModel body) {
        mWatchSettingActivity.dismissLoading();
        LogUtils.e(TAG, body.getResultMsg());
        LogUtils.d(TAG, body.getErrorCode() + "getErrorCode");
        int errorCode = body.getErrorCode();
        chekErrorCode(errorCode);
        queryDeviceStateByDeviceId(MyApplication.sToken, MyApplication.sDeviceId);
    }


    /**
     * 查询手表设置状态
     *
     * @param token
     * @param deviceId
     */
    public void queryDeviceStateByDeviceId(String token, int deviceId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);

        LogUtils.e(TAG, "queryDeviceStateByDeviceId:  " + String.valueOf(hashMap));
        mQueryDeviceStateByDeviceId = mWatchService.queryDeviceStateByDeviceId(hashMap);
        mWatchSettingActivity.showLoading(mContext.getString(R.string.Login_Loding),mContext);
        mQueryDeviceStateByDeviceId.enqueue(mCallback);
    }


    /**
     * 强制手表关机判断错误码
     *
     * @param errorCode
     */
    public void chekErrorCode(int errorCode) {
        switch (errorCode) {
            case 92301:
                mWatchSettingActivity.printn(mContext.getString(R.string.cant_repeat_the_operation_for_seconds));
                break;

            case 92302:
                mWatchSettingActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92303:
                mWatchSettingActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92304:
                mWatchSettingActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 90211:
                mWatchSettingActivity.printn(mContext.getString(R.string.the_watch_is_not_online));
                break;

        }
    }


    /**
     * 定位模式设置
     *
     * @param locationStyle
     */
    public void switchLocationStyle(int locationStyle) {
        switch (locationStyle) {
            case -1:
                mWatchSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.accurate_model));
                break;

            case 1:
                mWatchSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.accurate_model));
                break;

            case 2:
                mWatchSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.normal_mode));
                break;

            case 3:
                mWatchSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.Save_mode));
                break;

            case 4:
                mWatchSettingActivity.mTvLocationStyle.setText(mContext.getString(R.string.text_model));
                break;

        }

    }


    /**
     * 修改拒绝陌生人来电
     *
     * @param token
     * @param deviceId
     * @param acountId
     * @param state
     * @param isSwitch
     */
    public void insertOrUpdateDeviceSwtich(String token, int deviceId, long acountId, int state, boolean isSwitch) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        hashMap.put("acountId", acountId);
        if (isSwitch) {
            //拒绝陌生人来电状态
            LogUtils.e(TAG, "拒绝陌生人来电状态");
            hashMap.put("strangeCallSwitch", state);
        } else {
            //电话模式状态
            LogUtils.e(TAG, "电话模式状态");
            hashMap.put("mobileStyle", state);
        }

        Log.e(TAG, "insertOrUpdateDeviceAttr:  " + String.valueOf(hashMap));
        mInsertOrUpdateDeviceSwtich = mWatchService.insertOrUpdateDeviceAttr(hashMap);
        mInsertOrUpdateDeviceSwtich.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        queryDeviceStateByDeviceId(MyApplication.sToken, MyApplication.sDeviceId);
    }


    @Override
    protected void onDissms(String s) {
        mWatchSettingActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        mWatchSettingActivity.dismissLoading();
        LogUtils.e(TAG, body.getResultMsg());
        LogUtils.d(TAG, body.getErrorCode() + "getErrorCode");
        int errorCode = body.getErrorCode();
        chekErrorCode(errorCode);
        queryDeviceStateByDeviceId(MyApplication.sToken, MyApplication.sDeviceId);
    }


    /**
     * 定时开关机更改状态
     *
     * @param token
     * @param devicePowerId
     * @param switchMachine
     */
    public void saveState(String token, String devicePowerId, int switchMachine) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("devicePowerId", devicePowerId);
        params.put("state", switchMachine);
        Log.e(TAG, "更新定时开关机状态: " + String.valueOf(params));

        Call<ResponseInfoModel> call = mWatchService.updateDevicePowerState(params);
        call.enqueue(mCallback2);
    }


    /**
     * 强制手表关机
     *
     * @param command10002
     * @param message10002
     * @param token
     * @param appAccount
     * @param imei
     */
    public void sendCMDWatchOff(int command10002, String message10002, String token, String appAccount, String imei) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("acountName", appAccount);
        params.put("imei", imei);
        params.put("message", message10002);
        params.put("command", command10002);
        Log.e(TAG, "发送指令强制手表关机: " + String.valueOf(params));

        mAppSendCMD = mWatchService.appSendCMD(params);
        mWatchSettingActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mAppSendCMD.enqueue(mCallback3);
    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mWatchSettingActivity.onCMDSuccess(body);
    }
}
