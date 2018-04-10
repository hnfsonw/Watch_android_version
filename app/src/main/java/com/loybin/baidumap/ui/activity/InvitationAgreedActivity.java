package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.InvitationAgreedPresenter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/24 下午2:46
 * 描   述: 新注册用户,被管理员邀请 同意的  view
 */
public class InvitationAgreedActivity extends BaseActivity {
    private static final String TAG = "InvitationAgreedActivity";
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_icon)
    ImageView mIvIcon;

    @BindView(R.id.btn_afreed)
    Button mBtnAfreed;

    @BindView(R.id.btn_refused)
    Button mBtnRefused;

    @BindView(R.id.tv_message)
    TextView mTvMessage;
    private String mDeviceId;
    private String mImei;
    private String mNickName;
    private String mImgUrl;
    private InvitationAgreedPresenter mInvitationAgreedPresenter;
    private String mAppAccount;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_invitation_agreed;
    }

    @Override
    protected void init() {
        if (mInvitationAgreedPresenter == null) {
            mInvitationAgreedPresenter = new InvitationAgreedPresenter(this, this);
        }
        mAppAccount = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");

        mDeviceId = getIntent().getStringExtra("deviceId");
        mImei = getIntent().getStringExtra("imei");
        mNickName = getIntent().getStringExtra("nickName");
        mImgUrl = getIntent().getStringExtra("imgUrl");

        LogUtils.d(TAG, "deviceId " + mDeviceId);
        LogUtils.d(TAG, "imei " + mImei);
        LogUtils.d(TAG, "nickName " + mNickName);
        LogUtils.d(TAG, "imgUrl " + mImgUrl);
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.InvitationAgreedActivity));
        mTvMessage.setText(mNickName + getString(R.string.watch));
        if (mImgUrl != null) {
            Glide.with(this).load(mImgUrl).into(mIvIcon);
        }
    }


    @Override
    protected void dismissNewok() {
        if (mInvitationAgreedPresenter.mResponseInfoModelCall != null) {
            mInvitationAgreedPresenter.mResponseInfoModelCall.cancel();
        }
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            setResult(101);
            finishActivityByAnimation(this);
        }
        return true;
    }


    @OnClick({R.id.iv_back, R.id.btn_afreed, R.id.btn_refused})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(101);
                finishActivityByAnimation(this);
                break;

            case R.id.btn_afreed:
                toActivity(SelectRelationActivity.class, "afreed", mDeviceId);
                break;
            case R.id.btn_refused:
                //拒绝
                mInvitationAgreedPresenter.replyBandDeviceRequest(MyApplication.sToken, mDeviceId, mAppAccount, "N");
                break;
        }
    }


    /**
     * 拒绝成功的通知
     */
    public void onSuccess() {
        dismissLoading();
        setResult(101);
        finishActivityByAnimation(this);
    }

}
