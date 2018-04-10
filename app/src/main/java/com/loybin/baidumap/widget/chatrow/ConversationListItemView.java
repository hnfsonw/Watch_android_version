package com.loybin.baidumap.widget.chatrow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.hojy.happyfruit.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/03 下午2:37
 * 描   述: 群聊列表自定义view
 */

public class ConversationListItemView extends RelativeLayout {

    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.timestamp)
    TextView mTimestamp;

    @BindView(R.id.unread_count)
    TextView mUnreadCount;


    public ConversationListItemView(Context context) {
        this(context, null);
    }


    public ConversationListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_item, this);
        ButterKnife.bind(this, this);
    }


    public void bindView(EMConversation emConversation, EMGroup emGroup) {
        mUserName.setText(emGroup.getGroupId());

        if (emConversation != null) {
            EMMessage lastMessage = emConversation.getLastMessage();
            if (lastMessage != null) {
                String timestampString = DateUtils.getTimestampString(new Date(lastMessage.getMsgTime()));
                mTimestamp.setText(timestampString);
            }
            //未读消息个数
            int count = emConversation.getUnreadMsgCount();
            if (count == 0) {
                mUnreadCount.setVisibility(GONE);
            } else {
                mUnreadCount.setVisibility(VISIBLE);
                mUnreadCount.setText(String.valueOf(count));
            }
        }


    }
}
