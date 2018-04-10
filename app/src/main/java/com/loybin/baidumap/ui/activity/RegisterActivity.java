package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.RegisterPresenter;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 注册view
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.et_phone)
    LastInputEditText mEtPhone;

    @BindView(R.id.et_code)
    LastInputEditText mEtCode;

    @BindView(R.id.tv_send_code)
    public TextView mTvSendCode;

    @BindView(R.id.et_password)
    LastInputEditText mEtPassword;


    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;


    @BindView(R.id.et_confirm_password)
    LastInputEditText mEtConfirmPassword;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    @BindView(R.id.tv_register_service)
    TextView mTvService;

    @BindView(R.id.cb_service)
    CheckBox mCbService;

    private RegisterPresenter mRegisterPresenter;
    private String mPhone;
    private String mPassword;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }


    @Override
    protected void init() {
        ButterKnife.bind(this);
        mRegisterPresenter = new RegisterPresenter(this, this);

        initView();
        initListener();
    }


    private void initListener() {
        mCbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.d(TAG, "onCheckedChanged " + isChecked);
                if (isChecked) {
                    mBtnRegister.setEnabled(true);
                    mBtnRegister.setBackgroundResource(R.mipmap.register);
                } else {
                    mBtnRegister.setEnabled(false);
                    mBtnRegister.setBackgroundResource(R.drawable.back_shape_corners_tou_black_mask_back);
                }
            }
        });
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.Register_Title));
        mIvBack.setVisibility(View.VISIBLE);
        mCbService.setChecked(true);
    }


    @OnClick({R.id.btn_register, R.id.tv_send_code, R.id.iv_back, R.id.iv_no_view, R.id.tv_register_service})
    public void onViewClicked(View view) {
        mPhone = mEtPhone.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        String confirmPassword = mEtConfirmPassword.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_register:
                mRegisterPresenter.registerNumber(mPhone, code, mPassword, confirmPassword);
                break;

            case R.id.tv_send_code:
                mRegisterPresenter.checkPhone(mPhone);
                break;

            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_no_view:
                viewPassword();
                if (mPassword != null) {
                    mEtPassword.setSelection(mPassword.length());
                }
                break;

            case R.id.tv_register_service:
                toActivity(RegisterServiceActivity.class);
                break;
        }
    }

    private void viewPassword() {
        if (mIvNoView.isSelected()) {
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void succeed() {
        printn(getString(R.string.Register_Success));
        dismissLoading();
        Intent intent = getIntent();
        intent.putExtra("phone", mPhone);
        intent.putExtra("password", mPassword);
        setResult(100, intent);
        finishActivityByAnimation(this);
    }


    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }


    public void codeIsEmpty() {
        printn(getString(R.string.codeIsEmpty));
    }


    public void passWordIsEmpty() {
        printn(getString(R.string.passwordIsEmpty));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    public void CheckCode() {
        printn(getString(R.string.CheckCode_Success));
    }


    public void error(String resultMsg) {
        printn(resultMsg);
    }


    public void passwordError() {
        printn(getString(R.string.two_input_password_is_not_the_same));
    }


    public void onError(String resultMsg) {
        printn(resultMsg);
        dismissLoading();
    }


    public void passwordInconformity() {
        printn(getString(R.string.password_error));
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mRegisterPresenter.mRegister != null) {
            mRegisterPresenter.mRegister.cancel();
        }
        if (mRegisterPresenter.mSendCheckCode != null) {
            mRegisterPresenter.mSendCheckCode.cancel();
        }
    }
}
