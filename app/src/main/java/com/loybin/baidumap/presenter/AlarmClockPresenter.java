package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.AlarmClockActivity;
import com.loybin.baidumap.util.MyApplication;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by huangz on 17/10/13.
 * 闹钟列表业务逻辑
 */

public class AlarmClockPresenter extends BasePresenter {

    private Context mContext;
    private AlarmClockActivity mAlarmClockActivity;
    private Call<ResponseInfoModel> mQueryAlarmClockByDeviceId;
    private Call<ResponseInfoModel> mDeleteAlarmClock;
    private Call<ResponseInfoModel> mInsertOrUpdateAlarmClock;

    public AlarmClockPresenter(Context context, AlarmClockActivity alarmClockActivity) {
        super(context);
        mContext = context;
        mAlarmClockActivity = alarmClockActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.e("AlarmClockActivity", data.getResultMsg());
        mAlarmClockActivity.getSuccess(data.getResult().getAlarmClockList());
        mAlarmClockActivity.dismissLoading();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        Log.e("AlarmClockActivity", "" + s);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {

    }


    @Override
    protected void onError(ResponseInfoModel body) {

    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        queryAlarmClockByDeviceId(MyApplication.sDeviceId, MyApplication.sToken);
    }


    /**
     * 获取设备闹钟信息列表
     *
     * @param deviceId
     * @param token
     */
    public void queryAlarmClockByDeviceId(long deviceId, String token) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deviceId", deviceId);
        hashMap.put("token", token);

        mQueryAlarmClockByDeviceId = mWatchService.queryAlarmClockByDeviceId(hashMap);
        mAlarmClockActivity.showLoading("",mContext);
        mQueryAlarmClockByDeviceId.enqueue(mCallback);
    }


    /**
     * 删除闹钟
     *
     * @param id
     * @param deviceId
     * @param token
     */
    public void deleteAlarmClock(long id, long deviceId, String token) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("deviceId", deviceId);
        hashMap.put("token", token);

        mDeleteAlarmClock = mWatchService.deleteAlarmClock(hashMap);
        mAlarmClockActivity.showLoading("",mContext);
        mDeleteAlarmClock.enqueue(mCallback2);
    }


    /**
     * 添加闹钟|修改闹钟
     */
    public void insertOrUpdateAlarmClock(long id, long acountId, long deviceId, String alarmTime,
                                         String cycle, int isActive, String token) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("acountId", acountId);
        hashMap.put("deviceId", deviceId);
        hashMap.put("alarmTime", alarmTime);
        hashMap.put("cycle", cycle);
        hashMap.put("isActive", isActive);
        hashMap.put("token", token);

        mInsertOrUpdateAlarmClock = mWatchService.insertOrUpdateAlarmClock(hashMap);
        mAlarmClockActivity.showLoading("",mContext);
        mInsertOrUpdateAlarmClock.enqueue(mCallback3);
    }
}
