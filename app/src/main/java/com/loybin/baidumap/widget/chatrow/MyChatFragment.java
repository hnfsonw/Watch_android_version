package com.loybin.baidumap.widget.chatrow;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hojy.happyfruit.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ChatActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/11.
 */

public class MyChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {


    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    //red packet code : 红包功能使用的常量
    private static final int ITEM_RED_PACKET = 16;
    private static final int ITEM_TRANSFER_PACKET = 17;
    //end of red packet code
    private String mNickName;
    List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private Context mContext;
    private ChatActivity mChatActivity;
    /**
     * if it is chatBot
     */
    private boolean isRobot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void setUpView() {
        mMemberList = new ArrayList<>();
        setChatFragmentListener(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = HxEaseuiHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();
    }


    @Override
    public void onSetMessageAttributes(EMMessage message) {
        Log.d(TAG, "onSetMessageAttributes: " + mNickName);
        if (isRobot) {
            message.setAttribute("em_robot_message", isRobot);
        }
        Log.d(TAG, "发送消息设置:getUserName " + message.getUserName());
        Log.d(TAG, "发送消息设置:getFrom " + message.getFrom());
        Log.d(TAG, "发送消息设置: getMsgId" + message.getMsgId());
        Log.d(TAG, "发送消息设置:getBody " + message.getBody());
        Log.d(TAG, "发送消息设置:mNickName " + mNickName);
        //设置要发送扩展消息用户昵称
        if (mNickName != null) {
            message.setAttribute(Constant.USER_NAME, mNickName);
        }
        //设置要发送扩展消息用户头像
        message.setAttribute(Constant.HEAD_IMAGE_URL,
                "http://tupian.qqjay.com/tou2/2017/0120/39b35eed7d7000fc214d3f5198032f11.jpg");
    }


    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("mUsername", mUsername);
//        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //处理接收到的消息
        for (EMMessage message : messages) {
            //接收并处理扩展消息
            String userName = message.getStringAttribute(Constant.USER_NAME, "");
            String userId = message.getStringAttribute(Constant.USER_ID, "");
            String userPic = message.getStringAttribute(Constant.HEAD_IMAGE_URL, "");
            String hxIdFrom = message.getFrom();
            LogUtils.e(TAG, "接收到的用户名：" + userName + " 接收到的id：" + userId + "头像：" + userPic);
            EaseUser easeUser = new EaseUser();
            easeUser.setAvatar(userPic);
            easeUser.setNick(userName);
            //存入内存

        }
        super.onMessageReceived(messages);
    }


    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //red packet code : 处理红包回执透传消息
//        for (EMMessage message : messages) {
//            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//            String action = cmdMsgBody.action();//获取自定义action
//            if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                RedPacketUtil.receiveRedPacketAckMessage(message);
//                messageList.refresh();
//            }
//        }
        //end of red packet code


        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
//         no message forward when in chat room
        LogUtils.d(TAG, "删除");

        AlertDialog.Builder builder = new AlertDialog.Builder(mChatActivity);
        builder.setTitle(R.string.Confirm_to_delete_this_message);

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                conversation.removeMessage(contextMenuMessage.getMsgId());
                messageList.refresh();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO://视频
//                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
//                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
//                startVideoCall();
                break;
            //red packet code : 进入发红包页面
            case ITEM_RED_PACKET:
//                if (chatType == Constant.CHATTYPE_SINGLE) {
//                    //单聊红包修改进入红包的方法，可以在小额随机红包和普通单聊红包之间切换
//                    RedPacketUtil.startRandomPacket(new RPRedPacketUtil.RPRandomCallback() {
//                        @Override
//                        public void onSendPacketSuccess(Intent data) {
//                            sendMessage(RedPacketUtil.createRPMessage(getActivity(), data, toChatUsername));
//                        }
//
//                        @Override
//                        public void switchToNormalPacket() {
//                            RedPacketUtil.startRedPacketActivityForResult(ChatFragment.this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
//                        }
//                    },getActivity(),toChatUsername);
//                } else {
//                    RedPacketUtil.startRedPacketActivityForResult(this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
//                }
                break;
            case ITEM_TRANSFER_PACKET://进入转账页面
//                RedPacketUtil.startTransferActivityForResult(this, toChatUsername, REQUEST_CODE_SEND_TRANSFER_PACKET);
                break;
            //end of red packet code
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }


    public void setNickName(String relation) {
        mNickName = relation;

    }

    public void setContext(Context context, ChatActivity chatActivity) {
        mContext = context;
        mChatActivity = chatActivity;
    }


    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to mCount the number of different chat row
            return 12;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //red packet code : 红包消息、红包回执消息以及转账消息的chatrow type
//                else if (RedPacketUtil.isRandomRedPacket(message)) {
//                    //小额随机红包
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RANDOM : MESSAGE_TYPE_SEND_RANDOM;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
//                    //发送红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET : MESSAGE_TYPE_SEND_RED_PACKET;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    //领取红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET_ACK : MESSAGE_TYPE_SEND_RED_PACKET_ACK;
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_TRANSFER_PACKET_MESSAGE, false)) {
//                    //转账消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TRANSFER_PACKET : MESSAGE_TYPE_SEND_TRANSFER_PACKET;
//                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
//            if(message.getType() == EMMessage.Type.TXT){
            // voice call or video call
//                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
//                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
//                }
            //red packet code : 红包消息、红包回执消息以及转账消息的chat row
//                else if (RedPacketUtil.isRandomRedPacket(message)) {//小额随机红包
//                    return new ChatRowRandomPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {//红包消息
//                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//红包回执消息
//                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_TRANSFER_PACKET_MESSAGE, false)) {//转账消息
//                    return new ChatRowTransfer(getActivity(), message, position, adapter);
//                }
            //end of red packet code
//            }
            return null;
        }

    }
}
