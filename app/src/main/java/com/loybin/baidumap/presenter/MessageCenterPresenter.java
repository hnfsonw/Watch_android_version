package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.MessageCenterActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/23 上午11:30
 * 描   述: 消息中心业务
 */
public class MessageCenterPresenter extends BasePresenter {
    private static final java.lang.String TAG = "MessageCenterActivity";
    private Context mContext;
    private MessageCenterActivity mMessageCenterActivity;
    private Call<ResponseInfoModel> mTypesAndLastMessage;

    public MessageCenterPresenter(Context context, MessageCenterActivity messageCenterActivity) {
        super(context);
        mContext = context;
        mMessageCenterActivity = messageCenterActivity;
    }


    /**
     * 获取历史消息分类和最新一条消息
     *
     * @param token
     * @param acountId
     */
    public void getTypesAndLastMessage(String token, long acountId) {
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);

        LogUtils.d(TAG, "getTypesAndLastMessage " + String.valueOf(hashMap));
        mTypesAndLastMessage = mWatchService.getTypesAndLastMessage(hashMap);
        mMessageCenterActivity.showLoading("",mContext);
        mTypesAndLastMessage.enqueue(mCallback);
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, "parserJson " + data.getResultMsg());
        mMessageCenterActivity.onSuccess(data.getResult().getMessageList());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, "onFaiure " + s.getResultMsg());
        mMessageCenterActivity.dismissLoading();
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, "onDissms " + s);
        mMessageCenterActivity.dismissLoading();
    }
}
