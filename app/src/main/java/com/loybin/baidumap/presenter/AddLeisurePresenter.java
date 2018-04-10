package com.loybin.baidumap.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.AddLeisureActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/19 下午3:05
 * 描   述: 添加联系人业务与逻辑
 */
public class AddLeisurePresenter extends BasePresenter {
    private static final String TAG = "AddLeisureActivity";
    private Context mContext;
    private AddLeisureActivity mAddLeisureActivity;
    public Call<ResponseInfoModel> mCall;

    public AddLeisurePresenter(Context context, AddLeisureActivity addLeisureActivity) {
        super(context);
        mContext = context;
        mAddLeisureActivity = addLeisureActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, data.getResultMsg());
        mAddLeisureActivity.onSuccess();

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, s.getResultMsg());
        mAddLeisureActivity.dismissLoading();
        mAddLeisureActivity.printn(s.getResultMsg());
    }


    /**
     * 保存联系人
     *
     * @param phoneNumber
     * @param relation
     * @param token
     * @param deviceId
     */
    public void binding(String phoneNumber, String relation, String token, int deviceId,
                        String shortPhone, String bandRequest, String acountName) {
        if (TextUtils.isEmpty(relation)) {
            mAddLeisureActivity.relationEmpty();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            mAddLeisureActivity.phoneEmpty();
            return;
        }
        String phone = phoneNumber.replaceAll(" ", "").replaceAll("-", "");

        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("deviceId", deviceId);
        params.put("name", relation);
        params.put("token", token);
        params.put("bandRequest", bandRequest);
        params.put("acountName", acountName);
        if (shortPhone != null) {
            params.put("shortPhone", shortPhone);
        }
        Log.d("AddLeisureActivity", "添加联系人 : " + String.valueOf(params));

        mCall = mWatchService.addDeviceContracts(params);
        mAddLeisureActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCall.enqueue(mCallback);
    }
}
