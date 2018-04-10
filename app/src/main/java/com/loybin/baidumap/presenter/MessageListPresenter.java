package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.MessageListModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.MessageListActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/23 下午4:56
 * 描   述: 添加设备view
 */
public class MessageListPresenter extends BasePresenter {
    private static final String TAG = "MessageListActivity";
    private Context mContext;
    private MessageListActivity mMessageListActivity;
    private Call<MessageListModel> mMessagesByType;
    private Call<ResponseInfoModel> mCall;

    public MessageListPresenter(Context context, MessageListActivity messageListActivity) {
        super(context);
        mContext = context;
        mMessageListActivity = messageListActivity;
    }


    /**
     * 获取未读消息
     *
     * @param token
     * @param acountId
     * @param id       类型ID
     * @param page     页数
     * @param conut    返回条数
     */
    public void getMessagesByType(String token, long acountId, String id, int page, int conut) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);
        hashMap.put("msgType", id);
        hashMap.put("page", page);
        hashMap.put("limit", conut);

        LogUtils.e(TAG, "getMessagesByType " + String.valueOf(hashMap));

        mMessagesByType = mWatchService.getMessagesByType(hashMap);
        mMessageListActivity.showLoading("",mContext);
        mMessagesByType.enqueue(mCallback5);
    }


    @Override
    protected void parserJson(MessageListModel data) {
        LogUtils.e(TAG, "parserJson " + data.getResultMsg());
        MessageListModel.ResultBean.MessageListBean messageList = data.getResult().getMessageList();
        mMessageListActivity.onSuccess(messageList);
        LogUtils.d(TAG, messageList.getList().size() + " size ");
        mMessageListActivity.dismissLoading();
    }


    @Override
    protected void onFaiure(MessageListModel s) {
        LogUtils.e(TAG, "onFaiure " + s);
        mMessageListActivity.dismissLoading();
    }


    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, "onDissms " + s);
        mMessageListActivity.dismissLoading();
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, "parserJson " + body.getResultMsg());
        mMessageListActivity.deleteSuccess();
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e(TAG, "onFaiure " + body.getResultMsg());
        mMessageListActivity.Error(body.getResultMsg());
    }



    @Override
    protected void parserJson(ResponseInfoModel data) {

    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {

    }


    /**
     * 根据主类型ID情况消息
     *
     * @param token
     * @param acountId
     * @param id
     */
    public void deleteMessage(String token, long acountId, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);
        hashMap.put("msgType", id);

        LogUtils.e(TAG, "deleteMessage " + String.valueOf(hashMap));
        mCall = mWatchService.deleteMessagesByMsgType(hashMap);
        mMessageListActivity.showLoading("",mContext);
        mCall.enqueue(mCallback2);
    }
}
