package com.loybin.baidumap.ui.activity;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.MachineTimePresenter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/22 下午3:11
 * 描   述: 设置开关机时间
 */
public class MachineTimeActivity extends BaseActivity {

    private static final String TAG = "MachineTimeActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.ll_watch_boot)
    LinearLayout mLlWatchBoot;

    @BindView(R.id.ll_watch_off)
    LinearLayout mLlWatchOff;

    @BindView(R.id.btn_save)
    Button mBtnSave;

    @BindView(R.id.tv_select_time)
    TextView mTvSelectTime;

    @BindView(R.id.tv_select_off_time)
    TextView mTvSelectOffTime;

    private boolean isRight = true;
    private boolean inOpen = true;
    private SimpleDateFormat mSimpleDateFormat;
    private MachineTimePresenter mMachineTimePresenter;
    private String mID = "";
    private int mState = 1;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_machine_time;
    }

    @Override
    protected void init() {
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("HH:mm");
        }
        mMachineTimePresenter = new MachineTimePresenter(this, this);
        mMachineTimePresenter.queryState(MyApplication.sToken, MyApplication.sDeviceId);
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.setting_machine_time));
    }


    @OnClick({R.id.iv_back, R.id.ll_watch_boot, R.id.ll_watch_off, R.id.btn_save})
    public void onViewClicked(View view) {
        String offTime = mTvSelectOffTime.getText().toString().trim();
        String selectTime = mTvSelectTime.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                break;

            case R.id.ll_watch_boot:
                toTime();
                break;

            case R.id.ll_watch_off:
                toOffTime();
                break;


            case R.id.btn_save:
                mMachineTimePresenter.save(selectTime, offTime, mID, mState);
                break;
        }
    }


//    private void isRightFalse() {
//        mBtnLogin.setVisibility(View.GONE);
//        mBtnSave.setVisibility(View.GONE);
//        mLlWatchBoot.setEnabled(false);
//        mLlWatchOff.setEnabled(false);
//        isRight = true;
//    }


    private void toOffTime() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                LogUtils.e(TAG, mSimpleDateFormat.format(date));
                LogUtils.e(TAG, date + "");
                mTvSelectOffTime.setText(mSimpleDateFormat.format(date));


            }
        }).setType(TimePickerView.Type.HOURS_MINS)
                .setSubmitColor(Color.RED)//确定按钮文字颜色
                .setCancelColor(R.color.application_of_color)//取消按钮文字颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，
        // 避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 选择日期
     */
    private void toTime() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                LogUtils.e(TAG, mSimpleDateFormat.format(date));
                LogUtils.e(TAG, date + "");
                mTvSelectTime.setText(mSimpleDateFormat.format(date));


            }
        }).setType(TimePickerView.Type.HOURS_MINS)
                .setSubmitColor(Color.RED)//确定按钮文字颜色
                .setCancelColor(R.color.application_of_color)//取消按钮文字颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，
        // 避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    @Override
    protected void dismissNewok() {

    }


    public void selectTimeEmpty() {
        printn(getString(R.string.please_select_a_watch_on_time));
    }


    public void offTimeEmpty() {
        printn(getString(R.string.please_select_a_watch_off_time));
    }


    /**
     * 保存成功通知
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        printn(getString(R.string.Modify_the_success));
        finishActivityByAnimation(this);
    }


    /**
     * 查询状态成功的通知
     */
    public void queryStateSueess(List<ResponseInfoModel.ResultBean.DevicePowerListBean> devicePowerList) {
        dismissLoading();
        ResponseInfoModel.ResultBean.DevicePowerListBean devicePowerListBean = devicePowerList.get(0);
        mTvSelectTime.setText(devicePowerListBean.getOpenTimeStr());
        mTvSelectOffTime.setText(devicePowerListBean.getCloseTimeStr());
        mID = devicePowerListBean.getId() + "";
//        mState = devicePowerListBean.getState();

    }


}
