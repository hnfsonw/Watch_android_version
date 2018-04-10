package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.MachineTimeActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/22 下午4:37
 * 描   述: 设置开关机的业务逻辑
 */
public class MachineTimePresenter extends BasePresenter {
    private static final String TAG = "MachineTimeActivity";
    private Context mContext;
    private MachineTimeActivity mMachineTimeActivity;
    private Call<ResponseInfoModel> mCall;

    public MachineTimePresenter(Context context, MachineTimeActivity machineTimeActivity) {
        super(context);
        mContext = context;
        mMachineTimeActivity = machineTimeActivity;
    }

    /**
     * 设置/更新设备定时开关机接口
     */
    public void save(String selectTime, String offTime, String id, int state) {
        LogUtils.e(TAG, "selectTime " + selectTime);
        LogUtils.e(TAG, "offTime  " + offTime);
        LogUtils.e(TAG, "id  " + id);
        if (selectTime.isEmpty()) {
            mMachineTimeActivity.selectTimeEmpty();
            return;
        }
        if (offTime.isEmpty()) {
            mMachineTimeActivity.offTimeEmpty();
            return;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", MyApplication.sToken);
        hashMap.put("deviceId", MyApplication.sDeviceId);
        hashMap.put("acountId", MyApplication.sAcountId);
        hashMap.put("state", state);
        hashMap.put("openTime", selectTime);
        hashMap.put("closeTime", offTime);
        hashMap.put("id", id);
        Log.d(TAG, "设置/更新设备定时开关机接口: " + String.valueOf(hashMap));
        //去保存
        mCall = mWatchService.updateDevicePower(hashMap);
        mMachineTimeActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, data.getResultMsg());
        mMachineTimeActivity.onSuccess(data);
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, s.getResultMsg());
        mMachineTimeActivity.dismissLoading();
        int errorCode = s.getErrorCode();
        LogUtils.e(TAG, "errorCode " + errorCode);
        chekErrorCodde(errorCode);
    }


    private void chekErrorCodde(int errorCode) {
        switch (errorCode) {
            case 92301:
                mMachineTimeActivity.printn(mContext.getString(R.string.cant_repeat_the_operation_for_seconds));
                break;

            case 92302:
                mMachineTimeActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92303:
                mMachineTimeActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92304:
                mMachineTimeActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 90211:
                mMachineTimeActivity.printn(mContext.getString(R.string.the_watch_is_not_online));
                break;

        }
    }

    @Override
    protected void onDissms(String s) {
        mMachineTimeActivity.dismissLoading();
        LogUtils.e(TAG, s);
    }


    /**
     * 查询开关机状态
     *
     * @param token
     * @param deviceId
     */
    public void queryState(String token, int deviceId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        Log.d(TAG, "查询开关机状态: " + String.valueOf(hashMap));
        //去保存
        mCall = mWatchService.queryDevicePowerByDeviceId(hashMap);
        mMachineTimeActivity.showLoading("",mContext);
        mCall.enqueue(mCallback2);
    }


    /**
     * 查询开关机状态成功的回调
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        List<ResponseInfoModel.ResultBean.DevicePowerListBean> devicePowerList = body.getResult().getDevicePowerList();
        LogUtils.e(TAG, devicePowerList.size() + "  size");
        if (devicePowerList.size() > 0) {
            mMachineTimeActivity.queryStateSueess(devicePowerList);
        } else {
            //还没有设置过
            mMachineTimeActivity.dismissLoading();
            LogUtils.e(TAG, "没设置过");
        }
    }


    /**
     * 查询开关机状态失败的回调
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {

    }

}
