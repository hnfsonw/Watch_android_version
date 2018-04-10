package com.loybin.baidumap.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.ChatPresenter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.widget.chatrow.MyChatFragment;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/03 下午6:24
 * 描   述: 聊天的view
 */
public class ChatActivity extends BaseActivity {
    private static final String TAG = "ChatActivity";
    public static ChatActivity activityInstance;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_confirm)
    ImageView mIvGroup;

    @BindView(R.id.container)
    FrameLayout mContainer;

    private MyChatFragment myChatFragment;
    private ChatPresenter mChatPresenter;
    String toChatUsername;

    public List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private SharedPreferences mGlobalvariable;
    private String mPhone;
    private static final String ACTION_DOWN = "ACTION_DOWN";
    private static final String ACTION_UP = "ACTION_UP";
    private XmPlayerManager mPlayerManager;
    private boolean mPlaying;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }


    @Override
    protected void init() {
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mPhone = mGlobalvariable.getString("appAccount", "");
        toChatUsername = getIntent().getStringExtra(STRING);
        LogUtils.e(TAG, "toChatUsername" + toChatUsername);
        mMemberList = new ArrayList<>();
        myChatFragment = new MyChatFragment();
        if (mChatPresenter == null) {
            mChatPresenter = new ChatPresenter(this, this);
        }
        if (toChatUsername != null) {
            mChatPresenter.getGroupMemberList(toChatUsername, MyApplication.sAcountId, MyApplication.sToken);
        }
        //聊天人或群id
        initView();
        mPlayerManager = XmPlayerManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_DOWN");
        filter.addAction("ACTION_UP");
        registerReceiver(mBroadcastReceiver, filter);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_DOWN:

                    mPlaying = mPlayerManager.isPlaying();
                    if (mPlaying) {
                        mPlayerManager.pause();
                    }
                    break;

                case ACTION_UP:
                    int playerStatus = mPlayerManager.getPlayerStatus();
                    if (playerStatus == 5 && mPlaying) {
                        mPlayerManager.play();
                    }
                    break;

                default:
                    break;
            }

        }
    };


    private void initView() {
        mTvTitle.setText(getString(R.string.babychat));
        mIvGroup.setVisibility(View.VISIBLE);
        mIvGroup.setImageResource(R.mipmap.jagx);

    }


    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("conversation");
        myChatFragment.setArguments(bundle);
        myChatFragment.setContext(this, this);
        Log.d(TAG, "init: " + toChatUsername);
        //传入参数
        getSupportFragmentManager().beginTransaction().add(R.id.container, myChatFragment).commitAllowingStateLoss();

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:// 音量增大
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 1, 1);
                break;

            case KeyEvent.KEYCODE_VOLUME_DOWN:// 音量减小
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 1);
                break;

            case KeyEvent.KEYCODE_BACK:// 返回键
                DevicesHomeActivity.sMessageSize = 0;
                finishActivityByAnimation(this);
                break;

            default:
                break;
        }

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finishActivityByAnimation(this);
            startActivity(intent);
        }

    }


    /**
     * 获取群列表成功的通知
     *
     * @param memberList
     */
    public void onsuccess(List<ResponseInfoModel.ResultBean.MemberListBean> memberList) {
        for (ResponseInfoModel.ResultBean.MemberListBean member : memberList) {
            if (member.getAcountName().length() > 12) {
                LogUtils.e(TAG, "保存数据 " + member.acountName + MyApplication.sAcountId);
                saveData2Local(member.acountName + MyApplication.sAcountId + "", member.nickName);
                if (member.getImgUrl() != null) {
                    LogUtils.d(TAG, "getImgUrl  " + member.getImgUrl());
                    saveData2Local(member.acountName + MyApplication.sAcountId + "imgUrl", member.imgUrl);
                } else {
                    LogUtils.d(TAG, "member.gender  " + member.gender);
                    saveData2Local(member.acountName + MyApplication.sAcountId + "gender", member.gender + "");
                }
            } else {
                //保存在本地
                LogUtils.e(TAG, "保存数据 " + member.relation + MyApplication.sAcountId);
                saveData2Local(member.acountName + MyApplication.sAcountId + "", member.relation);
            }
            LogUtils.e(TAG, "list.getGroupId()  " + member.getGroupId());
            LogUtils.e(TAG, "list.acountName()  " + member.acountName);
            LogUtils.e(TAG, "list.relation()  " + member.relation);
            LogUtils.e(TAG, "list.relation()  " + member.nickName);
            LogUtils.e(TAG, "list.getImgUrl()  " + member.getImgUrl());
            if (mPhone.equals(member.getAcountName())) {
                String relation = member.getRelation();
                Log.d(TAG, "sPhone: " + mPhone);
                Log.d(TAG, "onsuccess: " + relation);
                SharedPreferencesUtils.setParam(this, "relation", relation);
                myChatFragment.setNickName(relation);
            }
        }
        dismissLoading();
        initData();
    }


    /**
     * 获取群列表失败的通知
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        dismissLoading();
        Log.d(TAG, "onError: " + resultMsg);
    }


    /**
     * 加载网络,清除缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mChatPresenter.mCall != null) {
            mChatPresenter.mCall.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.iv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                DevicesHomeActivity.sMessageSize = 0;
                finishActivityByAnimation(this);
                break;

            case R.id.iv_confirm:
                toActivity(ChatGroupActivity.class, toChatUsername);
                break;
        }
    }
}
