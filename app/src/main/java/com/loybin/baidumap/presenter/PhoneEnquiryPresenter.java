package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.PhoneEnquiryActivity;
import com.loybin.baidumap.util.LogUtils;


import java.util.HashMap;

import retrofit2.Call;


/**
 * Created by huangz on 17/8/21.
 */

public class PhoneEnquiryPresenter extends BasePresenter {
    private static final String TAG = "PhoneEnquiryActivity";
    private Context mContext;
    private String mCommand;
    private PhoneEnquiryActivity mPhoneEnquiryActivity;
    public Call<ResponseInfoModel> mSmsCmd;
    public Call<ResponseInfoModel> mResponseInfoModelCall;
    public Call<ResponseInfoModel> mInfoModelCall;

    public PhoneEnquiryPresenter(Context context, PhoneEnquiryActivity phoneEnquiryActivity) {
        super(context);
        mContext = context;
        mPhoneEnquiryActivity = phoneEnquiryActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, data.getResultMsg());
        mPhoneEnquiryActivity.onSuccess(data);

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, "onFaiure " + s.getResultMsg());
        mPhoneEnquiryActivity.printn(s.getResultMsg());
        mPhoneEnquiryActivity.dismissLoading();
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, s);
        mPhoneEnquiryActivity.dismissLoading();
    }

    /**
     * 查询话费
     *
     * @param token
     * @param appAccount
     * @param imei
     * @param command
     */
    public void phoneCost(String token, String appAccount, String imei, int command,
                          String billCmd, String serviceNumber) {
        String str = "{" + "smsCmd" + ":" + billCmd + "," + "serviceNumber" + ":" + serviceNumber + "}";
        String id = String.valueOf(str);
        Log.e(TAG, "appSendCMD: " + str);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("acountName", appAccount);
        params.put("imei", imei);
        params.put("message", "设备发送短信");
        params.put("command", command);
        params.put("parameters", str);
        Log.e(TAG, "appSendCMD: " + String.valueOf(params));

        mResponseInfoModelCall = mWatchService.appSendCMD(params);
        mPhoneEnquiryActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mResponseInfoModelCall.enqueue(mCallback2);
    }


    public void phoneFlow(String token, String appAccount, String imei, int command,
                          String flowCmd, String serviceNumber) {
        String str = "{" + "smsCmd" + ":'" + flowCmd + "'," + "serviceNumber" + ":" + serviceNumber + "}";
        String id = String.valueOf(str);
        Log.e(TAG, "appSendCMD: " + str);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("acountName", appAccount);
        params.put("imei", imei);
        params.put("message", "设备发送短信");
        params.put("command", command);
        params.put("parameters", str);
        Log.e(TAG, "appSendCMD: " + String.valueOf(params));

        mResponseInfoModelCall = mWatchService.appSendCMD(params);
        mPhoneEnquiryActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mResponseInfoModelCall.enqueue(mCallback3);
    }


    public void customCommand(String token, String appAccount, String imei, int command,
                              String customCmd, String serviceNumber) {
        mCommand = customCmd;
        String str = "{" + "smsCmd" + ":'" + customCmd + "'," + "serviceNumber" + ":" + serviceNumber + "}";
        String id = String.valueOf(str);
        Log.e(TAG, "appSendCMD: " + str);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("acountName", appAccount);
        params.put("imei", imei);
        params.put("message", "设备发送短信");
        params.put("command", command);
        params.put("parameters", str);
        Log.e(TAG, "appSendCMD: " + String.valueOf(params));

        mInfoModelCall = mWatchService.appSendCMD(params);
        mPhoneEnquiryActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mInfoModelCall.enqueue(mCallback4);
    }


    /**
     * 查询流量成功的回调
     *
     * @param body
     */
    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mPhoneEnquiryActivity.dismissLoading();
        mPhoneEnquiryActivity.addItem(3, mContext.getString(R.string.checkFlow_request_success),
                body.getTime());
    }

    /**
     * 查询话费成功的通知
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mPhoneEnquiryActivity.dismissLoading();
        mPhoneEnquiryActivity.addItem(3, mContext.getString(R.string.check_costs_request),
                body.getTime());
    }

    /**
     * 自定义查询成功的回调
     *
     * @param body
     */
    @Override
    protected void appCMDSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mPhoneEnquiryActivity.dismissLoading();
        mPhoneEnquiryActivity.addItem(3, "自定义指令" + mCommand + "指令已发送，请稍后查收",
                body.getTime());
    }

    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e(TAG, "onError " + body.getResultMsg());
        mPhoneEnquiryActivity.dismissLoading();
        mPhoneEnquiryActivity.printn(body.getResultMsg());
        mPhoneEnquiryActivity.addItem(3, mContext.getString(R.string.checkCost_request_success),
                body.getTime());
    }

    /**
     * 获取设备短信指令模板
     *
     * @param token
     * @param deviceId
     */
    public void getSmsCmd(String token, int deviceId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);

        mSmsCmd = mWatchService.getSmsCmd(hashMap);
        mPhoneEnquiryActivity.showLoading("",mContext);
        mSmsCmd.enqueue(mCallback);
    }
}
