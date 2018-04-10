package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.AlarmClockPresenter;
import com.loybin.baidumap.ui.adapter.AlarmClockAdapter;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangz on 17/10/13.
 * 闹钟列表视图
 */

public class AlarmClockActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_music)
    ImageView mIvMusic;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.lv_alarm_clock)
    ListView mLvAlarmClock;

    @BindView(R.id.btn_add_alarm_clock)
    Button mBtnAddAlarmClock;

    private AlarmClockAdapter mAlarmClockAdapter;
    private AlarmClockPresenter mAlarmClockPresenter;
    private List<ResponseInfoModel.ResultBean.alarmClockListBean> mAlarmClockList;
    private int mChangePosition = -1;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_alarm_clock;
    }


    @Override
    protected void init() {
        mAlarmClockList = new ArrayList<>();
        if (mAlarmClockAdapter == null) {
            mAlarmClockAdapter = new AlarmClockAdapter(this, mAlarmClockList);
        }
        mLvAlarmClock.setAdapter(mAlarmClockAdapter);
        initView();
        initListener();
    }


    private void initListener() {
        mLvAlarmClock.setOnItemClickListener(this);
    }


    private void initView() {
        mTvTitle.setText(R.string.alarm_clock);
        mTvRight.setText(R.string.editor);
    }


    @Override
    protected void dismissNewok() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAlarmClockPresenter == null) {
            mAlarmClockPresenter = new AlarmClockPresenter(this, this);
        }
        mAlarmClockPresenter.queryAlarmClockByDeviceId(MyApplication.sDeviceId, MyApplication.sToken);

    }


    @OnClick({R.id.iv_back, R.id.btn_add_alarm_clock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_add_alarm_clock:
                toActivity(AlarmClockSettingActivity.class);
                break;
        }
    }


    /**
     * 改变闹钟开关接口
     *
     * @param id
     * @param acountId
     * @param alarmTime
     * @param cycle
     * @param turn
     */
    public void changeAlarmClockTurn(long id, long acountId, String alarmTime, String cycle,
                                     int turn) {
        mAlarmClockPresenter.insertOrUpdateAlarmClock(id, acountId, MyApplication.sDeviceId,
                alarmTime, cycle, turn, MyApplication.sToken);

    }


    /**
     * 获取设备闹钟列表请求成功回调
     */
    public void getSuccess(List<ResponseInfoModel.ResultBean.alarmClockListBean> alarmClockList) {
        mAlarmClockList = alarmClockList;
        mAlarmClockAdapter.setData(mAlarmClockList);
        mAlarmClockAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, AlarmClockSettingActivity.class);
        intent.putExtra("id", mAlarmClockList.get(i).getId());
        intent.putExtra("alarmTime", mAlarmClockList.get(i).getAlarmTimeString());
        intent.putExtra("cycle", mAlarmClockList.get(i).getCycle());
        intent.putExtra("isActive", mAlarmClockList.get(i).getIsActive());
        intent.putExtra("nextTime", mAlarmClockList.get(i).getNextTime());
        intent.putExtra("repeatNumber", mAlarmClockList.get(i).getRepeatNumber());
        intent.putExtra("remark", mAlarmClockList.get(i).getRemark());
        intent.putExtra("edit", "1");
        startActivity(intent);
    }
}
