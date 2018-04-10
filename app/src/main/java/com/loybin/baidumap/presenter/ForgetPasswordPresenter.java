package com.loybin.baidumap.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ForgetPasswordActivity;
import com.loybin.baidumap.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/20 上午10:12
 * 描   述: 验证手机号码有没有注册
 */
public class ForgetPasswordPresenter extends BasePresenter {

    private static final String TAG = "ForgetPasswordActivity";
    private Context mContext;

    private ForgetPasswordActivity mForgetPasswordActivity;
    public Call<ResponseInfoModel> mCheckAcount;


    public ForgetPasswordPresenter(Context context, ForgetPasswordActivity forgetPasswordActivity) {
        super(context);
        mContext = context;
        mForgetPasswordActivity = forgetPasswordActivity;
    }


    /**
     * 验证手机号码有没注册
     *
     * @param phone
     */
    public void checkAcount(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mForgetPasswordActivity.phoneEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mForgetPasswordActivity.phoneError();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        Log.d("BasePresenter", "sendCode: " + String.valueOf(params));

        mCheckAcount = mWatchService.checkAcount(params);
        mForgetPasswordActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCheckAcount.enqueue(mCallback);

    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mForgetPasswordActivity.onSuccess();
        Log.d(TAG, "parserJson: " + data.getResultMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mForgetPasswordActivity.onError(s.getResultMsg());
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mForgetPasswordActivity.dismissLoading();
        mForgetPasswordActivity.printn(mContext.getString(R.string.Network_Error));
    }


}
