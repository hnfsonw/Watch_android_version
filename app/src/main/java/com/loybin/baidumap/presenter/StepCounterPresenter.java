package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.StepCounterActivity;
import com.loybin.baidumap.util.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by huangz on 17/9/8.
 */

public class StepCounterPresenter extends BasePresenter {

    private static final String TAG = "StepCounterActivity";
    private Context mContext;
    private StepCounterActivity mStepCounterActivity;
    private Call<ResponseInfoModel> mResponseInfoModelCall;

    public StepCounterPresenter(Context context, StepCounterActivity stepCounterActivity) {
        super(context);
        mContext = context;
        mStepCounterActivity = stepCounterActivity;
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, "请求成功:" + data.getResultMsg());

        mStepCounterActivity.setData(data.getResult().getStepsRankingList()
                , data.getResult().getDeviceStepsData());
        mStepCounterActivity.dismissLoading();
    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, "请求失败" + s.getErrorCode());
        mStepCounterActivity.dismissLoading();
    }

    public void queryDeviceStepsRanking(String token, String page, long deviceId) {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String queryDate = date.format(new Date());
        LogUtils.e(TAG, "当前日期:" + queryDate);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("queryDate", queryDate);
        hashMap.put("mPage", page);
        hashMap.put("deviceId", deviceId);
        hashMap.put("pageSize", "5");

        LogUtils.e(TAG, "token:" + token);
        LogUtils.e(TAG, "deviceId:" + deviceId);
        LogUtils.e(TAG, "queryDeviceStepsRanking " + String.valueOf(hashMap));

        mResponseInfoModelCall = mWatchService.queryDeviceStepsRanking(hashMap);
        mStepCounterActivity.showLoading("",mContext);
        mResponseInfoModelCall.enqueue(mCallback);
    }

}
