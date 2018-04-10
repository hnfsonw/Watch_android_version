package com.hyphenate.easeui.widget.chatrow;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EaseChatRowVoice extends EaseChatRowFile{

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;
    private AnimationDrawable mDrawable;

    public EaseChatRowVoice(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice, this);
    }

    @Override
    protected void onFindViewById() {
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
        readStatusView = (ImageView) findViewById(R.id.iv_unread_voice);
    }

    @Override
    protected void onSetUpView() {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if(len>0){
            voiceLengthView.setText(voiceBody.getLength() + "\"");
            voiceLengthView.setVisibility(View.VISIBLE);
        }else{
            voiceLengthView.setVisibility(View.INVISIBLE);
        }

        if (EaseChatRowVoicePlayClickListener.playMsgId != null
                && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(+R.anim.voice_from_icon);
            } else {
                voiceImageView.setImageResource(+R.anim.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.row4);
            } else {
                voiceImageView.setImageResource(R.drawable.my_row4);
            }
        }
        
        if (message.direct() == EMMessage.Direct.RECEIVE) {

            if (message.isListened()) {
                // hide the unread icon
                readStatusView.setVisibility(View.INVISIBLE);
            } else {
                readStatusView.setVisibility(View.VISIBLE);
            }
            EMLog.d(TAG, "it is receive msg");
            if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
//                progressBar.setVisibility(View.VISIBLE);
                Log.e("ChatActivity", "onSetUpView环信消息等待  下载中: " );
                progressBar2.setVisibility(VISIBLE);
                progressBar2.setImageResource(R.drawable.juhua);
                mDrawable = (AnimationDrawable) progressBar2.getDrawable();
                mDrawable.start();
                setMessageReceiveCallback();
            }else if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
                //失败的
                Log.e("ChatActivity","onSetUpView  环信接收消息失败");
                EMClient.getInstance().chatManager().downloadAttachment(message);
                setMessageReceiveCallback();

            } else{
                Log.e("ChatActivity", "onSetUpView  接收消息成功: ");
                progressBar2.setVisibility(INVISIBLE);
                progressBar2.setImageResource(R.drawable.juhua);
                mDrawable = (AnimationDrawable) progressBar2.getDrawable();
                mDrawable.start();
            }
            return;
        }

        // until here, handle sending voice message
        handleSendMessage();
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }


    @Override
    protected void onBubbleClick() {
        new EaseChatRowVoicePlayClickListener(message, voiceImageView, readStatusView, adapter, activity).onClick(bubbleLayout);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
            EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }
    
}
