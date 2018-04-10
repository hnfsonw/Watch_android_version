package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.BinDingActivity;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 上午11:54
 * 描   述: 普通成员解绑自己的业务
 */
public class BinDingPresenter extends BasePresenter {

    private static final String TAG = "BinDingActivity";
    private Context mContext;

    private BinDingActivity mBinDingActivity;
    public Call<ResponseInfoModel> mDisBandOneAcount;
    private Call<ResponseInfoModel> mDisBandAllDeviceContracts;


    public BinDingPresenter(Context context, BinDingActivity binDingActivity) {
        super(context);
        mContext = context;
        mBinDingActivity = binDingActivity;
    }


    /**
     * 解除绑定单个用户【非管理员】
     *
     * @param token
     * @param deviceId
     * @param acountId
     */
    public void disBandOneAcount(String token, int deviceId, long acountId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        Log.d(TAG, "解除绑定单个用户【非管理员】: " + String.valueOf(paramsMap));
        mDisBandOneAcount = mWatchService.disBandDeviceContracts(paramsMap);
        mBinDingActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandOneAcount.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mBinDingActivity.onSuccess();
        Log.d(TAG, "parserJson: " + data.getResultMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mBinDingActivity.onError(s.getResultMsg());
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mBinDingActivity.dismissLoading();
        mBinDingActivity.printn(mContext.getString(R.string.Network_Error));
    }


    /**
     * 解除所有人
     *
     * @param token
     * @param deviceId
     */
    public void disBandAllDeviceContracts(String token, int deviceId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        Log.d("BasePresenter", "sendCode: " + String.valueOf(paramsMap));
        mDisBandAllDeviceContracts = mWatchService.disBandAllDeviceContracts(paramsMap);
        mBinDingActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandAllDeviceContracts.enqueue(mCallback);
    }
}
