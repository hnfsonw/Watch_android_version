package com.loybin.baidumap.ui.activity;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.ForgetPresenter;
import com.loybin.baidumap.ui.view.LastInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/20 上午10:27
 * 描   述: 忘记密码
 */
public class ForgetActivity extends BaseActivity {


    private static final String TAG = "ForgetActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_code)
    LastInputEditText mEtCode;

    @BindView(R.id.tv_send_code)
    public TextView mTvSendCode;

    @BindView(R.id.et_password)
    LastInputEditText mEtPassword;

    @BindView(R.id.et_confirm_password)
    LastInputEditText mEtConfigPassword;

    @BindView(R.id.btn_register)
    Button mBtnResetpassword;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    private ForgetPresenter mForgetPresenter;
    private String mPhone;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget;
    }


    @Override
    protected void init() {
        mForgetPresenter = new ForgetPresenter(this, this);
        mPhone = getIntent().getStringExtra(STRING);

        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.login_forgot_pwd_text));
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }


    @OnClick({R.id.iv_back, R.id.tv_send_code, R.id.btn_register, R.id.iv_no_view})
    public void onViewClicked(View view) {
        String code = mEtCode.getText().toString().trim();
        String password = mEtPassword.getText().toString();
        String configPassword = mEtConfigPassword.getText().toString();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_send_code:
                mForgetPresenter.resetCode(mPhone);
                break;

            case R.id.btn_register:
                mForgetPresenter.resetPassword(mPhone, code, password, configPassword);
                break;

            case R.id.iv_no_view:
                viewPassword();
                break;
        }
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void viewPassword() {
        if (mIvNoView.isSelected()) {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            Log.e(TAG, "可见");
        } else {
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            Log.e(TAG, "不可见");
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mForgetPresenter.mEdit != null) {
            mForgetPresenter.mEdit.cancel();
        }
        if (mForgetPresenter.mSendCheckCode != null) {
            mForgetPresenter.mSendCheckCode.cancel();
        }
    }


    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    public void codeIsEmpty() {
        printn(getString(R.string.codeIsEmpty));
    }


    public void passwordInEmpty() {
        printn(getString(R.string.passwordIsEmpty));
    }


    public void passwordError() {
        printn(getString(R.string.Register_PasswordError_Tips));
    }


    public void CheckCode() {
        dismissLoading();
        printn(getString(R.string.CheckCode_Success));
    }


    public void error(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    public void passwordInconformity() {
        printn(getString(R.string.password_error));
    }


    public void editSuccess() {
        dismissLoading();
        printn(getString(R.string.deit_password_success));
        finishActivityByAnimation(this);

    }


    public void editError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);

    }
}
