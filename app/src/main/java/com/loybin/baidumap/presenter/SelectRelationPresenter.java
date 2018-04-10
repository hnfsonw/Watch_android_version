package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.SelectRelationActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/24 下午3:17
 * 描   述: 选择用户关系的  逻辑
 */
public class SelectRelationPresenter extends BasePresenter {
    private static final String TAG = "SelectRelationActivity";
    private Context mContext;
    private SelectRelationActivity mSelectRelationActivity;
    public Call<ResponseInfoModel> mResponseInfoModelCall;

    public SelectRelationPresenter(Context context, SelectRelationActivity selectRelationActivity) {
        super(context);
        mContext = context;
        mSelectRelationActivity = selectRelationActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, "parserJson " + data.getResultMsg());
        mSelectRelationActivity.onSuccess(data);
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, "onFaiure " + s.getResultMsg());
        mSelectRelationActivity.printn(s.getResultMsg());
        mSelectRelationActivity.dismissLoading();
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.d(TAG, "onDissms " + s);
        mSelectRelationActivity.dismissLoading();
    }

    /**
     * 被邀请人，回复接收/拒绝
     *
     * @param token
     * @param deviceId
     * @param appAccount
     * @param relation
     * @param y
     */
    public void replyBandDeviceRequest(String token, String deviceId, String appAccount, String relation, String y) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);
        hashMap.put("acountName", appAccount);
        hashMap.put("nickName", relation);
        hashMap.put("reply", y);

        LogUtils.e(TAG, "被邀请人，回复接收/拒绝 " + String.valueOf(hashMap));
        mSelectRelationActivity.showLoading("",mContext);
        mResponseInfoModelCall = mWatchService.replyBandDeviceRequest(hashMap);
        mResponseInfoModelCall.enqueue(mCallback);
    }
}
