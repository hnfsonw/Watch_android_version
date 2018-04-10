package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ChatActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/26 下午4:31
 * 描   述: 聊天的业务逻辑
 */
public class ChatPresenter extends BasePresenter {
    private static final String TAG = "ChatActivity";
    private Context mContext;
    private ChatActivity mChatActivity;
    public Call<ResponseInfoModel> mCall;


    public ChatPresenter(Context context, ChatActivity chatActivity) {
        super(context);
        mContext = context;
        mChatActivity = chatActivity;

    }


    /**
     * 获取群列表
     *
     * @param groupId
     * @param acountId
     * @param token
     */
    public void getGroupMemberList(String groupId, long acountId, String token) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId + "");
        paramsMap.put("token", token);
        paramsMap.put("groupId", groupId);

        Log.d("BasePresenter", "getGroupMemberList: " + String.valueOf(paramsMap));
        mCall = mWatchService.getGroupMemberListAll(paramsMap);
        mChatActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getResultMsg());
        List<ResponseInfoModel.ResultBean.MemberListBean> memberList = data.getResult().getMemberList();
        for (int i = 0; i < memberList.size(); i++) {
            String relation = memberList.get(i).getRelation();
            LogUtils.e(TAG, "relation" + relation);
            LogUtils.d(TAG, "parserJson: " + memberList.size());
            LogUtils.d(TAG, "acountType: " + memberList.get(i).acountType);
            LogUtils.d(TAG, "imgUrl: " + memberList.get(i).imgUrl);
            LogUtils.d(TAG, "groupId: " + memberList.get(i).groupId);
            LogUtils.d(TAG, "birthday: " + memberList.get(i).birthday);
            LogUtils.d(TAG, "phone: " + memberList.get(i).phone);
            LogUtils.d(TAG, "acountName: " + memberList.get(i).acountName);
            LogUtils.d(TAG, "address: " + memberList.get(i).address);
            LogUtils.d(TAG, "email: " + memberList.get(i).email);
            LogUtils.d(TAG, "nickName: " + memberList.get(i).nickName);
            LogUtils.d(TAG, "relation: " + memberList.get(i).relation);
            LogUtils.d(TAG, "gender: " + memberList.get(i).gender);
            LogUtils.d(TAG, "isAdmin: " + memberList.get(i).isAdmin);
            LogUtils.d(TAG, "acountId: " + memberList.get(i).acountId);
        }

        mChatActivity.onsuccess(memberList);

    }

    /**
     * 获取群组列表失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mChatActivity.onError(s.getResultMsg());
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        Log.d(TAG, "onDissms: " + s);
        mChatActivity.dismissLoading();
    }

}
