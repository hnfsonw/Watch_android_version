package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.presenter.OneWayListeningPresenter;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.venmo.view.TooltipView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/27 上午10:53
 * 描   述: 单向聆听view
 */
public class OneWayListeningActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.btn_listening)
    Button mBtnListening;

    @BindView(R.id.tv_tooltip)
    TooltipView mTvTooltip;

    private OneWayListeningPresenter mOneWayListeningPresenter;
    private String mAppAccount;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_one_way_listening;
    }

    @Override
    protected void init() {
        mOneWayListeningPresenter = new OneWayListeningPresenter(this, this);
        mAppAccount = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");

        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.one_way_listening));
        mTvTooltip.setText(getString(R.string.current_mobile_phone_number) + mAppAccount);
    }


    @OnClick({R.id.iv_back, R.id.btn_listening})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_listening:
                //使用单向聆听
                mOneWayListeningPresenter.appSendCMD(Constants.COMMAND10012, Constants.message,
                        MyApplication.sToken, mAppAccount,
                        MyApplication.sDeviceListBean.getImei(), MyApplication.sDeviceListBean.getPhone());
                break;
        }
    }


    @Override
    protected void dismissNewok() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mTvTooltip.setText(getString(R.string.current_mobile_phone_number) + "-" + mAppAccount);
    }

    /**
     * 发送成功
     */
    public void onSuccess() {
        dismissLoading();
        mTvTooltip.setVisibility(View.VISIBLE);
        mTvTooltip.setText(getString(R.string.please_wait_for_a_while) + mAppAccount);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 发送失败
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }

}
