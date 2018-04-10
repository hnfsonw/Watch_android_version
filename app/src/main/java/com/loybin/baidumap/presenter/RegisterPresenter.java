package com.loybin.baidumap.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.RegisterActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MD5Util;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 注册界面的业务逻辑
 */
public class RegisterPresenter extends BasePresenter {

    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    private boolean flag = true;

    private static final String TAG = "RegisterPresenter";
    private RegisterActivity mRegisterActivity;
    private Context mContext;
    public Call<ResponseInfoModel> mSendCheckCode;
    public Call<ResponseInfoModel> mRegister;

    public RegisterPresenter(Context context, RegisterActivity registerActivity) {
        super(context);
        mContext = context;
        mRegisterActivity = registerActivity;

    }


    /**
     * 验证电话是否可以发验证码
     */
    public void checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mRegisterActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mRegisterActivity.phoneError();
            return;
        }

        sendCode(phone);
    }

    /**
     * 验证注册的帐号信息
     */
    public void registerNumber(String userName, String code, String passWord, String confrimPassword) {
        if (TextUtils.isEmpty(userName)) {
            mRegisterActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(userName)) {
            mRegisterActivity.phoneError();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            mRegisterActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(passWord)) {
            mRegisterActivity.passWordIsEmpty();
            return;
        }

        if (!UserUtil.judgePassword(passWord)) {
            mRegisterActivity.passwordInconformity();
            return;
        }

        if (passWord.equals(confrimPassword) && UserUtil.judgePassword(passWord)) {
            String md5PassWord = MD5Util.getInstance().getMD5String(passWord);
            Log.d(TAG, "md5PassWord: " + md5PassWord);
            toRegister(userName, code, md5PassWord);
        } else {
            mRegisterActivity.passwordError();
        }
    }


    /**
     * 请求网络发送验证码
     */
    private void sendCode(String phone) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        LogUtils.d("BasePresenter", "sendCode: " + String.valueOf(params));

        mSendCheckCode = mWatchService.sendCheckCode(params);
        mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mSendCheckCode.enqueue(mCallback);
    }


    /**
     * 注册请求网络
     *
     * @param phone    电话
     * @param code     验证码
     * @param passWord 密码
     */
    private void toRegister(String phone, String code, String passWord) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", phone);
        paramsMap.put("password", passWord);
        paramsMap.put("code", code);

        Log.e(TAG, "注册请求网络: " + String.valueOf(paramsMap));
        Log.d(TAG, "toRegister: " + passWord);
        //执行enqueue
        mRegister = mWatchService.register(paramsMap);
        mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mRegister.enqueue(mCallback2);
    }


    /**
     * 验证码成功发送回调
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        upTime();
        mRegisterActivity.dismissLoading();

        //通知v层验证码发送成功
        mRegisterActivity.CheckCode();
    }


    /**
     * 验证码发送失败回调
     *
     * @param data
     */
    @Override
    protected void onFaiure(ResponseInfoModel data) {
        Log.d(TAG, "onFaiure: " + data.getResultMsg());
        mRegisterActivity.dismissLoading();
        mRegisterActivity.error(data.getResultMsg());
    }


    /**
     * 注册成功返回
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        ResponseInfoModel.ResultBean result = body.getResult();

        Log.d(TAG, "onSuccess: " + body.getResultMsg());
        mRegisterActivity.succeed();
    }


    /**
     * 失败返回
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        Log.d(TAG, "onError: " + body.getResultMsg());
        mRegisterActivity.onError(body.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mRegisterActivity.dismissLoading();
        mRegisterActivity.printn(mContext.getString(R.string.Network_Error));
    }


    protected void upTime() {
        mRegisterActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
        mRegisterActivity.mTvSendCode.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                for (; time > 0; time--) {
                    SystemClock.sleep(1000);
                    mHandler.sendEmptyMessage(TIME_MINUS);
                }
                mHandler.sendEmptyMessage(TIME_IS_OUT);
            }
        }.start();
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MINUS:
                    mRegisterActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mRegisterActivity.mTvSendCode.setText(MyApplication.sInstance.getString(R.string.To_Resend));
                    time = 120;
                    mRegisterActivity.mTvSendCode.setEnabled(true);
                    flag = false;
                    break;
            }

        }
    };

    protected int time = 120;


}
