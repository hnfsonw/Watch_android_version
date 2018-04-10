package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.PushStoryHiStoryActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/18 上午11:10
 * 描   述: 添加设备view
 */
public class PushStoryHiStoryPresenter extends BasePresenter {
    private static final String TAG = "PushStoryHiStoryActivity";
    private Context mContext;
    private PushStoryHiStoryActivity mPushStoryHiStoryActivity;
    private Call<ResponseInfoModel> mResponseInfoModelCall;
    private Call<ResponseInfoModel> mCall;


    public PushStoryHiStoryPresenter(Context context, PushStoryHiStoryActivity pushStoryHiStoryActivity) {
        super(context);
        mContext = context;
        mPushStoryHiStoryActivity = pushStoryHiStoryActivity;
    }


    /**
     * 查询故事推送历史
     *
     * @param token
     * @param imei
     */
    public void queryStoryList(String token, String imei) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("imei", imei);

        LogUtils.e(TAG, String.valueOf(hashMap));
        mResponseInfoModelCall = mWatchService.queryHimalayanStoryByImei(hashMap);
        mPushStoryHiStoryActivity.showLoading("",mContext);
        mResponseInfoModelCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, "parserJson " + data.getResultMsg());
        if (data.getResult().getStoryList() != null) {
            mPushStoryHiStoryActivity.onSuccess(data.getResult().getStoryList());
        }
        mPushStoryHiStoryActivity.dismissLoading();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, "onFaiure " + s.getResultMsg());
        mPushStoryHiStoryActivity.printn(s.getResultMsg());
        mPushStoryHiStoryActivity.dismissLoading();
    }


    @Override
    protected void onDissms(String s) {
        LogUtils.d(TAG, "onDissms " + s);
        mPushStoryHiStoryActivity.dismissLoading();
    }


    /**
     * 根据设备imei和喜马拉雅故事id 删除该设备已订阅喜马拉雅故事
     *
     * @param id
     * @param token
     * @param imei
     */
    public void deleteStory(long id, String token, String imei) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("storyId", id);
        hashMap.put("token", token);
        hashMap.put("imei", imei);

        LogUtils.e(TAG, String.valueOf(hashMap));
        mCall = mWatchService.deleteByImeiAndStoryId(hashMap);
        mPushStoryHiStoryActivity.showLoading("",mContext);
        mCall.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, "onSuccess " + body.getResult());
        queryStoryList(MyApplication.sToken, MyApplication.sDeviceListBean.getImei());
        mPushStoryHiStoryActivity.dismissLoading();
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e(TAG, "onError " + body.getResult());
        mPushStoryHiStoryActivity.dismissLoading();

    }
}
