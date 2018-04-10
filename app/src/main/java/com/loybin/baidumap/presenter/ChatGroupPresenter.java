package com.loybin.baidumap.presenter;

import android.content.Context;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ChatGroupActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/05 下午4:52
 * 描   述: 群成员业务逻辑
 */
public class ChatGroupPresenter extends BasePresenter {
    private static final String TAG = "ChatGroupActivity";
    private Context mContext;
    private ChatGroupActivity mChatGroupActivity;
    public Call<ResponseInfoModel> mGroupMemberListAll;

    public ChatGroupPresenter(Context context, ChatGroupActivity chatGroupActivity) {
        super(context);
        mContext = context;
        mChatGroupActivity = chatGroupActivity;
    }


    /**
     * 获取设备通讯群组成员信息,包括手表
     *
     * @param groupId
     * @param acountId
     * @param token
     * @param isShow
     */
    public void getGroupMemberListAll(String groupId, long acountId, String token, boolean isShow) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("token", token);
        paramsMap.put("groupId", groupId);
        LogUtils.e(TAG, String.valueOf(paramsMap));
        mGroupMemberListAll = mWatchService.getGroupMemberListAll(paramsMap);
        if (!isShow) {
            mChatGroupActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        }
        mGroupMemberListAll.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        List<ResponseInfoModel.ResultBean.MemberListBean> memberList = data.getResult().getMemberList();

        mChatGroupActivity.onScuuess(data);
        LogUtils.e(TAG, data.getResultMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mChatGroupActivity.onError(s);
        LogUtils.e(TAG, s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        mChatGroupActivity.dismissLoading();
        LogUtils.e(TAG, s);
    }
}
