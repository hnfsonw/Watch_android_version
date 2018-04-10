package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ShortPhoneActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by huangz on 17/9/14.
 */

public class ShortPhonePresenter extends BasePresenter {

    private static final String TAG = "ShortPhoneActivity";
    private Context mContext;
    private ShortPhoneActivity mShortPhoneActivity;
    public Call<ResponseInfoModel> mResponseInfoModelCall;

    public ShortPhonePresenter(Context context, ShortPhoneActivity shortPhoneActivity) {
        super(context);
        mContext = context;
        mShortPhoneActivity = shortPhoneActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, "请求成功" + data);
        mShortPhoneActivity.dismissLoading();
        mShortPhoneActivity.success();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, "请求失败" + s);
        mShortPhoneActivity.dismissLoading();
    }


    public void editDeviceContracts(String token, String deviceId, String oldPhone, String newPhone,
                                    String shortPhone, String acountId, String name) {

        LogUtils.e(TAG, "token:" + token);
        LogUtils.e(TAG, "deviceId:" + deviceId);
        LogUtils.e(TAG, "oldPhone:" + oldPhone);
        LogUtils.d(TAG, "newPhone:" + newPhone);
        LogUtils.e(TAG, "shortPhone:" + shortPhone);
        LogUtils.e(TAG, "acountId:" + acountId);
        LogUtils.e(TAG, "name:" + name);

        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        hashMap.put("oldPhone", oldPhone);
        hashMap.put("newPhone", newPhone);
        hashMap.put("shortPhone", shortPhone);
        if (acountId != null) {
            hashMap.put("acountId", acountId);
        }
        hashMap.put("name", name);

        mResponseInfoModelCall = mWatchService.editDeviceContracts(hashMap);
        mShortPhoneActivity.showLoading("",mContext);
        mResponseInfoModelCall.enqueue(mCallback);
    }
}
