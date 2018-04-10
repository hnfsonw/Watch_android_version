package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.WiFiSettingActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/13 下午2:32
 * 描   述: WIFI设置的业务view
 */
public class WiFiSettingPresenter extends BasePresenter {

    private static final String TAG = "WiFiSettingActivity";
    private Context mContext;
    private WiFiSettingActivity mWiFiSettingActivity;
    public Call<ResponseInfoModel> mCall;
    public Call<ResponseInfoModel> mCall1;

    public WiFiSettingPresenter(Context context, WiFiSettingActivity wiFiSettingActivity) {
        super(context);
        mContext = context;
        mWiFiSettingActivity = wiFiSettingActivity;
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {
        mWiFiSettingActivity.dismissLoading();
        LogUtils.e(TAG, data.getResultMsg());
        LogUtils.e(TAG, data.getResult().getState() + " getState");
        LogUtils.e(TAG, data.getResult().getPassword() + " getPassword");
        LogUtils.e(TAG, data.getResult().getSsid() + " getSsid");
        mWiFiSettingActivity.onSuccess(data);
    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, "onFaiure " + s.getResultMsg());
        mWiFiSettingActivity.dismissLoading();

    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, "onDissms " + s);
        mWiFiSettingActivity.dismissLoading();

    }

    /**
     * 根据设备id查询设置设备的wifi数据
     *
     * @param token
     * @param deviceId
     */
    public void queryDeviceWifiByDeviceId(String token, int deviceId, long acountId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
//        hashMap.put("acountId",acountId);

        LogUtils.e(TAG, "queryDeviceWifiByDeviceId " + String.valueOf(hashMap));
        mCall = mWatchService.queryDeviceWifiByDeviceId(hashMap);
        mWiFiSettingActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);
    }


    /**
     * app端设置设备的WiFi接口
     *
     * @param token
     * @param deviceId
     * @param ssid
     * @param password
     * @param state
     */
    public void insertOrUpdateDeviceWifi(String token, int deviceId, String ssid, String password, int state, long acountId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        hashMap.put("ssid", ssid);
        hashMap.put("password", password);
        hashMap.put("state", state);
        hashMap.put("acountId", acountId);

        LogUtils.e(TAG, "app端设置设备的WiFi接口 " + String.valueOf(hashMap));
        mCall1 = mWatchService.insertOrUpdateDeviceWifi(hashMap);
        mWiFiSettingActivity.showLoading("",mContext);
        mCall1.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mWiFiSettingActivity.dismissLoading();
    }

    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mWiFiSettingActivity.dismissLoading();
    }
}
