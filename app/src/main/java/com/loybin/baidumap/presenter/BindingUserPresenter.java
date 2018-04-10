package com.loybin.baidumap.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.BindingUserActivity;
import com.loybin.baidumap.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午5:57
 * 描   述: 绑定新用户业务逻辑
 */
public class BindingUserPresenter extends BasePresenter {

    private static final String TAG = "BindingUserActivity";
    private Context mContext;

    private BindingUserActivity mBindingUserActivity;
    public Call<ResponseInfoModel> mAcountBindImei;


    public BindingUserPresenter(Context context, BindingUserActivity bindingUserActivity) {
        super(context);
        mContext = context;
        mBindingUserActivity = bindingUserActivity;
    }


    /**
     * 添加设备的绑定账户
     *
     * @param phoneNumber
     * @param relation
     * @param token
     * @param deviceId
     */
    public void binding(String phoneNumber, String relation, String token, int deviceId) {
        if (TextUtils.isEmpty(relation)) {
            mBindingUserActivity.relationEmpty();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            mBindingUserActivity.phoneEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phoneNumber)) {
            mBindingUserActivity.phoneError();
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("acountName", phoneNumber);
        params.put("deviceId", deviceId);
        params.put("relation", relation);
        params.put("token", token);
        Log.d("BasePresenter", "sendCode: " + String.valueOf(params));

        mAcountBindImei = mWatchService.acountBindImei(params);
        mBindingUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mAcountBindImei.enqueue(mCallback);

    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mBindingUserActivity.onSuccess(data);
        Log.d(TAG, "parserJson: " + data.getResultMsg());

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mBindingUserActivity.onError(s);
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        mBindingUserActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
        mBindingUserActivity.printn(mContext.getString(R.string.Network_Error));
    }
}
