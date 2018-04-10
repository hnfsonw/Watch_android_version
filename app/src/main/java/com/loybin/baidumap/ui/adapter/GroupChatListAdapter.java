package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.util.DateUtils;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ChatActivity;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.LogUtils;

import java.util.Date;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/03 下午2:37
 * 描   述: 群聊列表适配器
 */
public class GroupChatListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "GroupChatActivity";
    private Context mContext;

    private List<EMConversation> mEMConversations;
    private List<ResponseInfoModel.ResultBean.DeviceListBean> mMemberList;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_DIVISION = 1;
    private int mIndex;
    private int mPosition;


    public GroupChatListAdapter(Context context, List<EMConversation> conversations
            , List<ResponseInfoModel.ResultBean.DeviceListBean> list) {
        mContext = context;
        mEMConversations = conversations;
        mMemberList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_TITLE:
                itemView = View.inflate(mContext, R.layout.item_title, null);
                TitleHolder titleHolder = new TitleHolder(itemView);
                return titleHolder;

            case TYPE_DIVISION:
                itemView = View.inflate(mContext, R.layout.view_conversation_item, null);
                DivisionHolder divisionHolder = new DivisionHolder(itemView);
                return divisionHolder;
            default:
                return null;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_TITLE:
                TitleHolder titleHolder = (TitleHolder) holder;
                if (mMemberList.size() >= 2) {
                    titleHolder.mTvQun.setVisibility(VISIBLE);
                    titleHolder.mViewById.setVisibility(VISIBLE);
                }
                if (mMemberList.size() >= 2) {
                    if (position == 2) {
                        titleHolder.mTvQun.setVisibility(VISIBLE);
                        titleHolder.setData(mContext.getString(R.string.title_holder));
                    }
                } else {
                    titleHolder.mTvQun.setVisibility(GONE);
                }
                break;

            case TYPE_DIVISION:
                LogUtils.e(TAG, "position  " + position);
                if (position == 1) {
                    mIndex = position - 1;
                }
                if (position >= 3) {
                    mIndex = position - 2;
                }
                DivisionHolder divisionHolder = (DivisionHolder) holder;
                LogUtils.e(TAG, "环信的消息会话个数  " + mEMConversations.size());
                LogUtils.e(TAG, "群的个数  " + mMemberList.size());

                if (mMemberList.size() > 0) {
                    LogUtils.e(TAG, "~~~~~~~~~~~bindview");
                    LogUtils.e(TAG, "index  " + mIndex);
                    divisionHolder.setId(mMemberList.get(mIndex));
                }

                if (mEMConversations.size() > 0) {
                    LogUtils.d(TAG, "bindView  mIndex  " + mIndex);
                    divisionHolder.bindView(mEMConversations, mMemberList.get(mIndex));

                }

//                if (mEMConversations.size() > 0  && mEMConversations.size() > mIndex){
//
//
//                    LogUtils.d(TAG,"bindView  mIndex  " + mIndex);
//                    divisionHolder.bindView(mEMConversations.get(mIndex),mMemberList.get(mIndex));
//                }

                divisionHolder.mItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (position == 1) {
                            mPosition = 0;
                        }
                        if (position == 3) {
                            mPosition = 1;
                        }
                        if (position > 3) {
                            mPosition = position - 2;
                        }
                        if (EMClient.getInstance().isLoggedInBefore()) {
                            Intent intent = new Intent(mContext, ChatActivity.class);
                            LogUtils.d(TAG, "position  ~~~~~~" + position + "");
                            LogUtils.d(TAG, "mIndex  ~~~~~~" + mPosition + "");
                            intent.putExtra(EaseConstant.EXTRA_USER_ID, mMemberList.get(mPosition).getGroupId() + "");
                            //传入参数
                            Bundle args = new Bundle();
                            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                            args.putString(EaseConstant.EXTRA_USER_ID, mMemberList.get(mPosition).getGroupId() + "");
                            intent.putExtra("conversation", args);
                            Log.d(TAG, "onClick: " + mMemberList.get(mPosition).getGroupId());
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.abnormal_micro_chat_server_please_try_again_later), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        int count = 1;
        if (mMemberList.size() >= 2) {
            count = 2;
        }
        if (mMemberList.size() > 0) {
            count += mMemberList.size();
        }
        LogUtils.d(TAG, "mMemberList.size() mCount~~~" + count);
        LogUtils.d(TAG, "mMemberList.size() " + mMemberList.size());
        return count;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        } else if (position == 2 && mMemberList.size() >= 2) {
            return TYPE_TITLE;
        } else {
            return TYPE_DIVISION;
        }
    }


    public void setEMConversation(List<EMConversation> emConversations) {
        mEMConversations.clear();
        mEMConversations.addAll(emConversations);
        LogUtils.e(TAG, "会话加载了  " + mEMConversations.size());

    }

    public void setEMGroupInfo(List<ResponseInfoModel.ResultBean.DeviceListBean> grouplist) {
        LogUtils.d(TAG, "群列表加载了  " + grouplist.size());
        for (ResponseInfoModel.ResultBean.DeviceListBean list : grouplist) {
            LogUtils.d(TAG, "群列表加载了  " + list.getGroupId());
        }
        mMemberList.clear();
        mMemberList.addAll(grouplist);
        LogUtils.e(TAG, "群列表加载了~~" + mMemberList.size());
        notifyDataSetChanged();
        LogUtils.e(TAG, "群列表加载了" + mMemberList.size());
    }


    public class DivisionHolder extends RecyclerView.ViewHolder {

        public View mItemView;
        private final TextView mUserName;
        private final TextView mTimestamp;
        private final TextView mUnreadCount;
        private final CircleImageView mIvAcatar;
        private final View mViewById;


        public DivisionHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mUserName = (TextView) mItemView.findViewById(R.id.user_name);
            mTimestamp = (TextView) mItemView.findViewById(R.id.timestamp);
            mUnreadCount = (TextView) mItemView.findViewById(R.id.unread_count);
            mIvAcatar = (CircleImageView) mItemView.findViewById(R.id.avatar);
            mViewById = mItemView.findViewById(R.id.rl_is_show);
        }


        public void bindView(EMConversation emConversation, ResponseInfoModel.ResultBean.DeviceListBean listBean) {
            if (emConversation != null) {
                EMMessage lastMessage = emConversation.getLastMessage();
                if (lastMessage != null) {
                    String to = lastMessage.getTo();
                    long groupId = listBean.getGroupId();

                    if (to.equals(groupId + "")) {
                        LogUtils.d(TAG, "return 啊啊啊啊");
                        String timestampString = DateUtils.getTimestampString(new Date(lastMessage.getMsgTime()));
                        mTimestamp.setVisibility(VISIBLE);
                        mTimestamp.setText(timestampString);
                        LogUtils.e(TAG, "timestampString  " + timestampString);

                    }
                    //未读消息个数
                    int count = emConversation.getUnreadMsgCount();
                    LogUtils.d(TAG, "mCount  " + count);
                    if (count == 0) {
                        mUnreadCount.setVisibility(GONE);
                    } else {
                        mUnreadCount.setVisibility(VISIBLE);
                        LogUtils.d(TAG, "lastMessage.getTo  " + lastMessage.getTo());
                        mUnreadCount.setText(String.valueOf(count));
                    }
                }

            }
        }


        public void setId(ResponseInfoModel.ResultBean.DeviceListBean data) {
            mViewById.setVisibility(VISIBLE);
            mUserName.setText(data.getNickName() + "的微聊");
            if (data.getImgUrl() != null) {
                Glide.with(mContext).load(data.getImgUrl()).into(mIvAcatar);
            } else {
                mIvAcatar.setImageResource(data.getGender() == 1 ? R.drawable.a : R.drawable.b);
            }
        }


        public void bindView(List<EMConversation> emConversations, ResponseInfoModel.ResultBean.DeviceListBean listBean) {
            for (int i = 0; i < emConversations.size(); i++) {
                EMConversation emConversation = emConversations.get(i);
                if (emConversation != null) {
                    EMMessage lastMessage = emConversation.getLastMessage();
                    if (lastMessage != null) {
                        String to = lastMessage.getTo();
                        long id = listBean.getGroupId();
                        String groupId = id + "";
                        LogUtils.e(TAG, "会话.getTo()" + to);
                        LogUtils.e(TAG, "群列表.getGroupId()" + groupId);
                        LogUtils.e(TAG, "设置listBean.getGroupId()" + to.equals(groupId + ""));
                        if (to.equals(groupId)) {
                            LogUtils.d(TAG, "return 啊啊啊啊");
                            String timestampString = DateUtils.getTimestampString(new Date(lastMessage.getMsgTime()));
                            mTimestamp.setVisibility(VISIBLE);
                            mTimestamp.setText(timestampString);
                            LogUtils.e(TAG, "timestampString  " + timestampString);

                            //未读消息个数
                            int count = emConversation.getUnreadMsgCount();
                            LogUtils.d(TAG, "mCount  " + count);
                            if (count == 0) {
                                mUnreadCount.setVisibility(GONE);
                            } else {
                                mUnreadCount.setVisibility(VISIBLE);
                                LogUtils.d(TAG, "lastMessage.getTo  " + lastMessage.getTo());
                                mUnreadCount.setText(String.valueOf(count));
                            }
                        }

                    }
                }
            }
        }
    }


    class TitleHolder extends RecyclerView.ViewHolder {

        public final TextView mTvQun;
        public final View mViewById;

        TitleHolder(View view) {
            super(view);
            mTvQun = (TextView) view.findViewById(R.id.tv_qun);
            mViewById = view.findViewById(R.id.ll_ling);
        }

        public void setData(String data) {
            mViewById.setVisibility(VISIBLE);
            mTvQun.setVisibility(VISIBLE);
            mTvQun.setText(data);
        }
    }
}
