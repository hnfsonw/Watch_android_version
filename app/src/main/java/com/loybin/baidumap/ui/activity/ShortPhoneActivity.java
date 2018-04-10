package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.ShortPhonePresenter;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangz on 17/9/14.
 * 设置短号
 */

public class ShortPhoneActivity extends BaseActivity {


    private static final String TAG = "ShortPhoneActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.iv_music)
    ImageView mIvMusic;

    @BindView(R.id.et_shortPhone)
    LastInputEditText mEtShortPhone;

    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private String mNewPhone;
    private String mShortPhone;
    private String mAcountId;
    private String mDeviceId;
    private String mRelation;
    private boolean mNetWork = false;
    private boolean mIsRegister;
    private ShortPhonePresenter mShortPhonePresenter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_short_phone;
    }


    @Override
    protected void init() {
        mNewPhone = getIntent().getStringExtra("newPhone");
        mShortPhone = getIntent().getStringExtra("shortPhone");
        mIsRegister = getIntent().getBooleanExtra("mIsRegister", true);
        if (mIsRegister) {
            mAcountId = getIntent().getStringExtra("acountId");
        }
        mRelation = getIntent().getStringExtra("relation");
        mDeviceId = getIntent().getStringExtra("deviceId");
        mShortPhonePresenter = new ShortPhonePresenter(this, this);
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.cornet_code));
        mIvConfirm.setImageResource(R.mipmap.shortphone_remove);
        mIvConfirm.setVisibility(View.VISIBLE);
        if (mShortPhone != null) {
            mEtShortPhone.setText(mShortPhone);
            mEtShortPhone.setSelection(mShortPhone.length());
        }

    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void dismissNewok() {
        if (mShortPhonePresenter.mResponseInfoModelCall != null) {
            mShortPhonePresenter.mResponseInfoModelCall.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.btn_register, R.id.iv_confirm})
    public void onViewClicked(View view) {
        mShortPhone = mEtShortPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                break;

            case R.id.btn_register:
                if (TextUtils.isEmpty(mShortPhone)) {
                    printn(getString(R.string.cornet_inEmpty));
                    return;
                }

//                if (mNetWork == false){
//                    printn(getString(R.string.no_network));
//                    return;
//                }

                LogUtils.e(TAG, "mAcountId" + mAcountId);
                mShortPhonePresenter.editDeviceContracts(MyApplication.sToken,
                        mDeviceId, mNewPhone, mNewPhone, mShortPhone, mAcountId, mRelation);
                break;

            case R.id.iv_confirm:
                if (mShortPhone.length() >= 1) {
                    //删除
                    mShortPhone = "";
                    mShortPhonePresenter.editDeviceContracts(MyApplication.sToken,
                            mDeviceId, mNewPhone, mNewPhone, mShortPhone, mAcountId, mRelation);
                }

                break;

            default:
                break;
        }
    }


    @Override
    protected void theNetwork() {
        super.theNetwork();
        mNetWork = true;
    }


    @Override
    protected void noNetwork() {
        super.noNetwork();
        mNetWork = false;
    }


    public void success() {
        Intent intent = getIntent();
        intent.putExtra("shortPhone", mShortPhone);
        setResult(100, intent);
        finishActivityByAnimation(this);
    }
}
