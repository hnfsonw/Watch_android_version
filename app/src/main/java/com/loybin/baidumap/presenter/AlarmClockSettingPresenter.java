package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.AlarmClockSettingActivity;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by huangz on 17/10/14.
 */

public class AlarmClockSettingPresenter extends BasePresenter {

    private Context mContext;
    private AlarmClockSettingActivity mAlarmClockSettingActivity;
    private Call<ResponseInfoModel> mInsertOrUpdateAlarmClock;

    public AlarmClockSettingPresenter(Context context, AlarmClockSettingActivity alarmClockSettingActivity) {
        super(context);
        mContext = context;
        mAlarmClockSettingActivity = alarmClockSettingActivity;
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {

    }


    /**
     * 添加闹钟|修改闹钟
     */
    public void insertOrUpdateAlarmClock(long id, long acountId, long deviceId, String alarmTime,
                                         String cycle, int isActive, int nextTime, int repeatNumber,
                                         String token) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (id != 0) {
            hashMap.put("id", id);
        }
        hashMap.put("acountId", acountId);
        hashMap.put("deviceId", deviceId);
        hashMap.put("alarmTime", alarmTime);
        hashMap.put("cycle", cycle);
        hashMap.put("isActive", isActive);
        if (nextTime != 0) {
            hashMap.put("nextTime", nextTime);
        }
        if (repeatNumber != 0) {
            hashMap.put("repeatNumber", repeatNumber);
        }

        hashMap.put("token", token);

//        mInsertOrUpdateAlarmClock = mWatchService.insertOrUpdateAlarmClock(hashMap);
        mAlarmClockSettingActivity.showLoading("",mContext);
        mInsertOrUpdateAlarmClock.enqueue(mCallback);
    }
}
