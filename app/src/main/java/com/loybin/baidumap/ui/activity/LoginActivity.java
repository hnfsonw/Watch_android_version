package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.LoginPresenter;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 登录view
 */
public class LoginActivity extends BaseActivity implements View.OnKeyListener {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_login_username)
    LastInputEditText mEtLoginUsername;

    @BindView(R.id.et_login_password)
    LastInputEditText mEtLoginPassword;

    @BindView(R.id.tv_login_forget)
    TextView mTvLoginForget;

    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindView(R.id.tv_login_register)
    TextView mTvLoginRegister;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    @BindView(R.id.ll_eyes)
    LinearLayout mLlEyes;

    private LoginPresenter mLoginPresenter;
    private SharedPreferences mGlobalvariable;
    private String mMd5Password;
    private String mAppAccount;
    private String mPhone;
    private String mPassword;
    private boolean isLogin = true;
    public String isCamera;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }


    @Override
    protected void init() {
        ButterKnife.bind(this);
        toSDmissions();
        mLoginPresenter = new LoginPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mMd5Password = mGlobalvariable.getString("password", "");
        mAppAccount = mGlobalvariable.getString("appAccount", "");
        initView();
        initListener();

//        if (DevicesHomeActivity.mMqttAndroidClient != null){
//            LogUtils.e(TAG,"isConnected "+DevicesHomeActivity.mMqttAndroidClient.isConnected()+"");
//        }
    }


    private void initListener() {
        mEtLoginPassword.setOnKeyListener(this);
//        mEtLoginUsername.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int mCount, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int mCount) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() >= 11){
//                    mBtnRegister.setEnabled(true);
//                    mBtnRegister.setBackgroundResource(R.drawable.login_shape_white_selector);
//                }else {
//                    mBtnRegister.setEnabled(false);
//                    mBtnRegister.setTextColor(Color.GRAY);
//                }
//            }
//        });
    }


    private void initView() {
        mEtLoginUsername.setText(mAppAccount);
        mEtLoginPassword.setText(mMd5Password);

//        if (mAppAccount.length() < 10){
//            mBtnRegister.setEnabled(false);
//            mBtnRegister.setTextColor(Color.GRAY);
//        }else {
//            mBtnRegister.setEnabled(true);
//            mBtnRegister.setBackgroundResource(R.drawable.login_shape_white_selector);
//        }
    }


    @OnClick({R.id.btn_login, R.id.tv_login_register, R.id.tv_login_forget
            , R.id.iv_no_view, R.id.ll_eyes})
    public void onViewClicked(View view) {
        if (!isLogin) {
            printn(getString(R.string.no_network));
            return;
        }
        mPhone = mEtLoginUsername.getText().toString().trim();
        mPassword = mEtLoginPassword.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_login_register:
                toActivity(100, RegisterActivity.class);
                break;

            case R.id.tv_login_forget:
                toActivity(ForgetPasswordActivity.class);
                break;

            case R.id.btn_login:
                toCanmeraPermissions();
                break;

            case R.id.ll_eyes:
                viewPassword();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            if (data != null) {
                LogUtils.d(TAG, "注册反回来的数据");
                mPhone = data.getStringExtra("phone");
                mPassword = data.getStringExtra("password");
                mEtLoginUsername.setText(mPhone);
                mEtLoginPassword.setText(mPassword);
                toCanmeraPermissions();
            }
        } else if (resultCode == 101) {
            LogUtils.d(TAG, "邀请被拒绝了");
            mLoginPresenter.getAcountDeivceList(mLoginPresenter.mResultBean.getToken(),
                    mLoginPresenter.mResultBean.getAcountId());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 查看密码
     */
    private void viewPassword() {
        if (mIvNoView.isSelected()) {
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }


    /**
     * 登入先申请相机权限成功的通知
     */
    @Override
    protected void cameraSuccess() {
        mLoginPresenter.login(mPhone, mPassword);
    }


    /**
     * 申请权限拒绝的通知
     */
    @Override
    protected void cameraError() {
        isCamera = "true";
        mLoginPresenter.login(mPhone, mPassword);
//        finishActivityByAnimation(this);
    }


    /**
     * 存储权限被拒绝
     */
    @Override
    protected void grantedError() {
        finishActivityByAnimation(this);
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    public void passwordIsEmpty() {
        printn(getString(R.string.passwordIsEmpty));
    }


    public void succeed() {

    }


    public void resultMsg(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    public void Error(String resultMsg) {
        dismissLoading();
    }


    public void getAcountDeivceList(ResponseInfoModel.ResultBean resultBean) {
        mLoginPresenter.getAcountDeivceList(resultBean.getToken(), resultBean.getAcountId());
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mLoginPresenter.mLogin != null) {
            mLoginPresenter.mLogin.cancel();
        }
    }


    /**
     * 无网络的时候通知
     */
    @Override
    protected void noNetwork() {
//        isLogin = false;
        MyApplication.sInUpdata = true;
    }


    /**
     * 有网络的通知
     */
    @Override
    protected void theNetwork() {
        isLogin = true;
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_UP) {
            mPhone = mEtLoginUsername.getText().toString().trim();
            mPassword = mEtLoginPassword.getText().toString().trim();
            toCanmeraPermissions();
        }
        return false;
    }


}
