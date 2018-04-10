package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.WatchMessageActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/03 上午11:36
 * 描   述: 手表信息业务逻辑
 */
public class WatchMessagePresenter extends BasePresenter {

    private static final String TAG = "WatchMessageActivity";
    private Context mContext;
    private WatchMessageActivity mWatchMessageActivity;
    public Call<ResponseInfoModel> mCall;
    private Call<ResponseInfoModel> mResponseInfoModelCall;

    public WatchMessagePresenter(Context context, WatchMessageActivity watchMessageActivity) {
        super(context);
        mContext = context;
        mWatchMessageActivity = watchMessageActivity;
    }


    /**
     * 获取手表信息
     */
    public void loadWatchMessage(Object deviceId, String token) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("deviceId", deviceId);

        LogUtils.e(TAG, "toRegis: " + String.valueOf(paramsMap));
        mCall = mWatchService.queryDeviceByDeviceId(paramsMap);
        mWatchMessageActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mWatchMessageActivity.onSuccess(data);
        Log.d(TAG, "parserJson: " + data.getResultMsg());

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, s.getResultMsg());
        mWatchMessageActivity.onError(s);
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        mWatchMessageActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
    }


    /**
     * 根据设备id，新增/更新设备的自动更新状态
     *
     * @param token
     * @param deviceId
     * @param state
     */
    public void insertOrUpdateDeviceAttr(String token, int deviceId, String state) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        hashMap.put("updatedFlag", state);
        mWatchMessageActivity.showLoading("",mContext);
        LogUtils.d(TAG, "insertOrUpdateDeviceAttr " + String.valueOf(hashMap));
        mResponseInfoModelCall = mWatchService.insertOrUpdateDeviceAttr(hashMap);
        mResponseInfoModelCall.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, "onSuccess " + body.getResultMsg());
        mWatchMessageActivity.dismissLoading();
    }

    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.d(TAG, "onError " + body.getResultMsg());
        mWatchMessageActivity.onWatchError(body.getResultMsg());

    }
}
