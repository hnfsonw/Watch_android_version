package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.GroupChatActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.ThreadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/03 下午2:29
 * 描   述: 群聊列表的业务逻辑
 */
public class GroupChatPresenter extends BasePresenter {

    private static final String TAG = "GroupChatActivity";
    private Context mContext;

    private GroupChatActivity mGroupChatActivity;

    public List<EMConversation> mEMConversations;
    public List<EMConversation> mEMCList;
    public Call<ResponseInfoModel> mGroupMemberListAll;


    public GroupChatPresenter(Context context, GroupChatActivity groupChatActivity) {
        super(context);
        mContext = context;
        mGroupChatActivity = groupChatActivity;
        mEMConversations = new ArrayList<EMConversation>();
        mEMCList = new ArrayList<>();
    }


    /**
     * 获取所有的会话
     *
     * @param groupId
     */
    public void loadConversations(final String groupId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //清空旧数据
                mEMConversations.clear();
                mEMCList.clear();
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                //从会话hashmap获取所有的会话
                mEMConversations.addAll(conversations.values());
                Log.d(TAG, "获取所有的会话size : " + mEMConversations.size());

                for (EMConversation list : mEMConversations) {
                    LogUtils.d(TAG, "type   " + list.conversationId());
                    if (groupId.equals(list.conversationId())) {
                        mEMCList.add(list);
                        LogUtils.e(TAG, "chatType 添加成功");
                    }
                }

                for (EMConversation list : mEMConversations) {
                    if (!groupId.equals(list.conversationId()) && list.conversationId().length() != 15) {
                        mEMCList.add(list);
                    }
                    LogUtils.d(TAG, "list.conversationId()" + list.conversationId());
                }

                for (EMConversation list : mEMCList) {
                    LogUtils.e(TAG, "会话 " + list.conversationId());
                }


                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mGroupChatActivity.mGroupChatListAdapter.setEMConversation(mEMCList);
                        mGroupChatActivity.mGroupChatListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();


    }


    public List<EMConversation> getConversations() {
        return mEMConversations;
    }


    public void initData(String token, long acountId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("token", token);
        LogUtils.e(TAG, String.valueOf(paramsMap));
        mGroupMemberListAll = mWatchService.getAcountDeivceList(paramsMap);
        mGroupChatActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mGroupMemberListAll.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mGroupChatActivity.onSuccess(data);
        LogUtils.e(TAG, data.getResultMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel data) {
        mGroupChatActivity.onError(data);
        LogUtils.e(TAG, data.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, s);
        mGroupChatActivity.dismissLoading();
    }


    /**
     * 判断是否中文 true中午
     *
     * @param str
     * @return
     */
    public boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
