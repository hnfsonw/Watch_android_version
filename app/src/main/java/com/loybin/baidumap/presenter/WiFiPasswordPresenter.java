package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.WiFiPasswordActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/13 下午4:06
 * 描   述: wifi密码确认业务
 */
public class WiFiPasswordPresenter extends BasePresenter {
    private static final String TAG = "WiFiPasswordActivity";
    private Context mContext;
    private WiFiPasswordActivity mWiFiPasswordActivity;
    public Call<ResponseInfoModel> mCall;

    public WiFiPasswordPresenter(Context context, WiFiPasswordActivity wiFiPasswordActivity) {
        super(context);
        mContext = context;
        mWiFiPasswordActivity = wiFiPasswordActivity;
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, data.getResultMsg());
        mWiFiPasswordActivity.onSuccess();

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, s.getResultMsg());
        mWiFiPasswordActivity.dismissLoading();
        mWiFiPasswordActivity.printn(s.getResultMsg());
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.d(TAG, s);
        mWiFiPasswordActivity.dismissLoading();
    }

    public void insertOrUpdateDeviceWifi(String token, int deviceId, String ssid, String password, int state, long acountId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        hashMap.put("ssid", ssid);
        hashMap.put("password", password);
        hashMap.put("state", state);
        hashMap.put("acountId", acountId);

        LogUtils.e(TAG, "app端设置设备的WiFi接口 " + String.valueOf(hashMap));
        mCall = mWatchService.insertOrUpdateDeviceWifi(hashMap);
        mWiFiPasswordActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);
    }
}
