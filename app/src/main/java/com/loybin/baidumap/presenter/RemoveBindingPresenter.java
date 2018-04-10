package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.RemoveBindingActivity;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 下午12:45
 * 描   述: 解除所有账户的绑定
 */
public class RemoveBindingPresenter extends BasePresenter {

    private static final String TAG = "RemoveBindingActivity";
    private Context mContext;

    private RemoveBindingActivity mRemoveBindingActivity;
    public Call<ResponseInfoModel> mDisBandAllAcount;

    public RemoveBindingPresenter(Context context, RemoveBindingActivity removeBindingActivity) {
        super(context);
        mContext = context;
        mRemoveBindingActivity = removeBindingActivity;
    }


    /**
     * 解除所有人
     *
     * @param token
     * @param deviceId
     */
    public void disBandAllAcount(String token, int deviceId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        Log.d("BasePresenter", "sendCode: " + String.valueOf(paramsMap));
        mDisBandAllAcount = mWatchService.disBandAllAcount(paramsMap);
        mRemoveBindingActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandAllAcount.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mRemoveBindingActivity.onSuccess();
        Log.d(TAG, "parserJson: " + data.getResultMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mRemoveBindingActivity.onError(s.getResultMsg());
        Log.d(TAG, "onFaiure: " + s.getResultMsg());

    }


    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mRemoveBindingActivity.dismissLoading();
        mRemoveBindingActivity.printn(mContext.getString(R.string.Network_Error));
    }

}
