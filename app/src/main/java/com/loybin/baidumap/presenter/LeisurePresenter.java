package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.WXEntryActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/20 下午5:09
 * 描   述: 修改账户信息
 */
public class LeisurePresenter extends BasePresenter {

    private static final String TAG = "WXEntryActivity";
    private Context mContext;

    private WXEntryActivity mWXEntryActivity;
    public Call<ResponseInfoModel> mUpdateAcountInfo;
    private Call<ResponseInfoModel> mResponseInfoModelCall;
    private boolean mIsShow;


    public LeisurePresenter(Context context, WXEntryActivity WXEntryActivity) {
        super(context);
        mContext = context;
        mWXEntryActivity = WXEntryActivity;
    }


    /**
     * 修改账户信息
     *
     * @param token
     * @param acountId
     * @param gender
     * @param phone
     */
    public void updateAcountInfo(String token, String acountId, int gender, String phone, int devicele) {
        if (phone.equals(mWXEntryActivity.mAcountName)) {
            mWXEntryActivity.noModification();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)) {
            mWXEntryActivity.phoneError();
            return;
        }

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("token", token);
        paramsMap.put("gender", gender);
        paramsMap.put("phone", phone);
        paramsMap.put("deviceId", devicele);

        Log.e(TAG, "修改账户信息: " + String.valueOf(paramsMap));
        mUpdateAcountInfo = mWatchService.updateAcountInfoAndSendCMDToImei(paramsMap);
        mWXEntryActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mUpdateAcountInfo.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getResultMsg());
        mWXEntryActivity.onSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
        mWXEntryActivity.onError(s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        mWXEntryActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
        mWXEntryActivity.printn(mContext.getString(R.string.Network_Error));
    }


    /**
     * 删除联系人请求接口
     *
     * @param token
     * @param acountId
     * @param deviceId
     * @param phone
     */
    public void delDeviceContracts(String token, String acountId, String deviceId, String phone) {
        LogUtils.d(TAG, acountId + "~ " + deviceId + "~" + phone);
        mIsShow = true;
        if (deviceId != null && phone != null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("token", token);
            if (acountId != null) {
                hashMap.put("acountId", acountId);
            }
            hashMap.put("deviceId", deviceId);
            hashMap.put("phone", phone);

            LogUtils.d(TAG, "删除联系人请求接口 " + String.valueOf(hashMap));

            Call<ResponseInfoModel> call = mWatchService.delDeviceContracts(hashMap);
            mWXEntryActivity.showLoading("",mContext);
            call.enqueue(mCallback2);
        }
    }


    /**
     * 删除成功的回调
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mWXEntryActivity.dismissLoading();
        mWXEntryActivity.printn(mContext.getString(R.string.delete_success));
        mWXEntryActivity.finishActivityByAnimation(mWXEntryActivity);
    }


    /**
     * 删除失败的回调
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mWXEntryActivity.dismissLoading();
        if (mIsShow) {
            mWXEntryActivity.printn(body.getResultMsg());
        }
    }


    /**
     * 编辑修改联系人
     *
     * @param token
     * @param deviceId
     * @param phone
     * @param newPhone
     * @param relation
     */
    public void editDeviceContracts(String token, String deviceId, String phone, String newPhone,
                                    String relation, String acountId) {
        if (token != null && deviceId != null && phone != null && relation != null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("token", token);
            hashMap.put("deviceId", deviceId);
            hashMap.put("oldPhone", phone);
            hashMap.put("newPhone", newPhone);
            hashMap.put("name", relation);
            if (acountId != null) {
                hashMap.put("acountId", acountId);
            }

            LogUtils.d(TAG, "编辑修改联系人 " + String.valueOf(hashMap));

            Call<ResponseInfoModel> call = mWatchService.editDeviceContracts(hashMap);
            mWXEntryActivity.showLoading("",mContext);
            call.enqueue(mCallback3);
        }
    }


    /**
     * 编辑修改联系人成功的回调
     *
     * @param body
     */
    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mWXEntryActivity.dismissLoading();
        mWXEntryActivity.printn(mContext.getString(R.string.edit_success));
        mWXEntryActivity.finishActivityByAnimation(mWXEntryActivity);
    }


    /**
     * 邀请好友绑定设备
     *
     * @param token
     * @param phone
     * @param deviceId
     * @param acountName
     * @param acountType
     */
    public void bandDeviceRequest(String token, String phone, String deviceId, String acountName, int acountType, boolean isShow) {
        mIsShow = isShow;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("phone", phone);
        hashMap.put("deviceId", deviceId);
        hashMap.put("acountName", acountName);
        hashMap.put("acountType", acountType);

        LogUtils.d(TAG, "邀请好友绑定设备 " + String.valueOf(hashMap));
        if (isShow) {
            mWXEntryActivity.showLoading("",mContext);
        }
        mResponseInfoModelCall = mWatchService.bandDeviceRequest(hashMap);
        mResponseInfoModelCall.enqueue(mCallback4);
    }

    @Override
    protected void appCMDSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, "appCMDSuccess " + body.getResultMsg());
        mWXEntryActivity.bandSuccess();
    }
}
