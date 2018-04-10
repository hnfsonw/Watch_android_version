package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.InvitationAgreedActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/25 下午5:27
 * 描   述: app邀请用户同意的 业务
 */
public class InvitationAgreedPresenter extends BasePresenter {
    private static final java.lang.String TAG = "InvitationAgreedActivity";
    private Context mContext;
    private InvitationAgreedActivity mInvitationAgreedActivity;
    public Call<ResponseInfoModel> mResponseInfoModelCall;

    public InvitationAgreedPresenter(Context context, InvitationAgreedActivity invitationAgreedActivity) {
        super(context);
        mContext = context;
        mInvitationAgreedActivity = invitationAgreedActivity;
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, "parserJson " + data.getResultMsg());
        mInvitationAgreedActivity.onSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, "onFaiure " + s.getResultMsg());
        mInvitationAgreedActivity.dismissLoading();
        mInvitationAgreedActivity.printn(s.getResultMsg());
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.d(TAG, "onDissms " + s);
        mInvitationAgreedActivity.dismissLoading();
    }

    /**
     * 被邀请人，回复接收/拒绝
     *
     * @param token
     * @param mDeviceId
     * @param acountName
     * @param reply
     */
    public void replyBandDeviceRequest(String token, String mDeviceId, String acountName, String reply) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", mDeviceId);
        hashMap.put("acountName", acountName);
        hashMap.put("reply", reply);

        LogUtils.d(TAG, "被邀请人，回复接收/拒绝 " + String.valueOf(hashMap));
        mInvitationAgreedActivity.showLoading("",mContext);
        mResponseInfoModelCall = mWatchService.replyBandDeviceRequest(hashMap);
        mResponseInfoModelCall.enqueue(mCallback);
    }


}
