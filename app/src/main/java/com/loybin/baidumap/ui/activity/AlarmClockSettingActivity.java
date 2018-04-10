package com.loybin.baidumap.ui.activity;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.AlarmClockSettingPresenter;

/**
 * Created by huangz on 17/10/13.
 */

public class AlarmClockSettingActivity extends BaseActivity {


    private long mId;
    private String mAlarmTime;
    private String mCycle;
    private int mIsActive;
    private int mNextTime;
    private int mRepeatNumber;
    private String mRemark;

    private AlarmClockSettingPresenter mAlarmClockSettingPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_alarm_clock_setting;
    }

    @Override
    protected void init() {
        String edit = getIntent().getStringExtra("edit");
        if ("1".equals(edit)) {
            mId = getIntent().getIntExtra("id", 0);

        }
    }

    @Override
    protected void dismissNewok() {

    }
}
