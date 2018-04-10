package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.OneWayListeningActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/27 上午11:34
 * 描   述: 添加设备view
 */
public class OneWayListeningPresenter extends BasePresenter {
    private static final String TAG = "OneWayListeningActivity";
    private Context mContext;
    private OneWayListeningActivity mOneWayListeningActivity;
    public Call<ResponseInfoModel> mCall;

    public OneWayListeningPresenter(Context context, OneWayListeningActivity oneWayListeningActivity) {
        super(context);
        mContext = context;
        mOneWayListeningActivity = oneWayListeningActivity;
    }


    /**
     * 发送透传指令
     *
     * @param command10012
     * @param message
     * @param token
     * @param appAcount
     * @param imei
     * @param phone
     */
    public void appSendCMD(int command10012, String message, String token, String appAcount, String imei, String phone) {
        String str = "{" + "imei" + ":" + imei + "," + "phone" + ":" + phone + "}";
        String id = String.valueOf(str);
        Log.e(TAG, "appSendCMD: " + id);

        HashMap<String, Object> params = new HashMap<>();
        params.put("message", message);
        params.put("command", command10012);
        params.put("token", token);
        params.put("imei", imei);
        params.put("acountName", appAcount);
        params.put("parameters", id);
        Log.e(TAG, "发送透传指令 : " + String.valueOf(params));

        mCall = mWatchService.appSendCMD(params);
        mOneWayListeningActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, data.getResultMsg());
        mOneWayListeningActivity.onSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, s.getResultMsg());
        mOneWayListeningActivity.onError(s.getResultMsg());

    }


    @Override
    protected void onDissms(String s) {
        mOneWayListeningActivity.dismissLoading();
        LogUtils.e(TAG, s);
    }
}
