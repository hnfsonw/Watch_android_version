package com.loybin.baidumap.ui.activity;

import android.app.Notification;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.EventPlayProgressModel;
import com.loybin.baidumap.model.EventPlaySeekModel;
import com.loybin.baidumap.model.EventPlayStartModel;
import com.loybin.baidumap.model.EventPlayStopModel;
import com.loybin.baidumap.model.MessageEventModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.StoryPlayerPresenter;
import com.loybin.baidumap.ui.view.BlurTransformation;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.PlayerSeekBar;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: Huangz
 * 创建时间: 2017/08/27 上午10:14
 * 描   述: 听故事播放器
 */

public class StoryPlayerActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {


    private static final String TAG = "StoryPlayerActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.sound_cover)
    CircleImageView mSoundCover;

    @BindView(R.id.seek_bar)
    PlayerSeekBar mSeekBar;

    @BindView(R.id.tv_time)
    TextView mTvCurrPos;

    @BindView(R.id.tv_duration)
    TextView mTvDuration;

    @BindView(R.id.iv_play_way)
    ImageView mIvPlayWay;

    @BindView(R.id.play_or_pause)
    ImageView mBtnPlay;

    @BindView(R.id.pre_sound)
    ImageView mBtnPreSound;

    @BindView(R.id.iv_next_story)
    ImageView mBtnNextSound;

    @BindView(R.id.iv_story_download)
    ImageView mIvStoryDownload;

    @BindView(R.id.iv_background)
    ImageView mIvBackground;

    @BindView(R.id.iv_story_push)
    ImageView mIvStoryPush;

    private CommonRequest mXimalaya;
    private XmPlayerManager mPlayerManager;
    private boolean mUpdateProgress = true;
    private BlurTransformation mBlurTransformation;
    private int mPlayModelState = 0;
    private StoryPlayerPresenter mStoryPlayerPresenter;
    private boolean mIsNetWork;
    private String mAppAccount;
    private Track mTrack;
    private static int sIsService = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_story_player;
    }


    @Override
    protected void init() {
        //注册事件
        mPlayModelState = (int) SharedPreferencesUtils.getParam(this, "mPlayModelState", 0);
        if (mBlurTransformation == null) {
            mBlurTransformation = new BlurTransformation(this, 100);
        }
        if (mStoryPlayerPresenter == null) {
            mStoryPlayerPresenter = new StoryPlayerPresenter(this, this);
        }
        mAppAccount = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");
        mXimalaya = CommonRequest.getInstanse();
        mPlayerManager = XmPlayerManager.getInstance(this);
        initView();
        initData();
        initListener();
        initService();
        EventBus.getDefault().register(this);

//        warmPrompt(mIsNetWork);
        LogUtils.e(TAG, "mIsNetWork " + mIsNetWork);
    }


    private void initData() {
        mTrack = (Track) mPlayerManager.getCurrSound();
        if (mTrack != null) {
            if (mTrack.getDownloadSize() == 0) {
                mIvStoryPush.setVisibility(View.GONE);
            } else {
                mIvStoryPush.setVisibility(View.VISIBLE);
            }
        } else {
            mIvStoryPush.setVisibility(View.GONE);
        }

    }


    private void initService() {
        sIsService++;
        LogUtils.e(TAG, "sIsService =  " + sIsService);
        if (sIsService > 1) {
            return;
        }
        mPlayerManager = XmPlayerManager.getInstance(mContext);
        Notification mNotification = XmNotificationCreater.getInstanse(this)
                .initNotification(this.getApplicationContext(),
                        StoryPlayerActivity.class);
        mPlayerManager.init(10, mNotification);
    }


    @Override
    protected void netWorkCancel() {

    }


    @Override
    protected void netWorkContinue() {
        mPlayerManager.play();
    }


    //进度条 播放时间回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetTitle(MessageEventModel messageEvent) {
        if (messageEvent == null) {
            LogUtils.e(TAG, " messageEvent == null");
            return;
        }
        if (messageEvent.getTitle() != null) {
            String title = messageEvent.getTitle();
            String[] split = title.split("：");
            if (split.length > 1) {
                mTvTitle.setText(split[1]);
            } else {
                mTvTitle.setText(title);
            }
        }

        if (messageEvent.getImagurl() != null) {
            Glide.with(this).load(messageEvent.getImagurl()).into(mSoundCover);
            Glide.with(this).load(messageEvent.getImagurl())
                    .transform(mBlurTransformation)
                    .crossFade()
                    .into(mIvBackground);
        }

        if (messageEvent.getCurrPos() != null) {
            mTvCurrPos.setText(messageEvent.getCurrPos());
        }

        if (messageEvent.getDuration() != null) {
            mTvDuration.setText(messageEvent.getDuration());
        }

        if (messageEvent.getDurations() != 0 && mUpdateProgress) {
            mSeekBar.setProgress(messageEvent.getProgress());
        }

    }


    //播放按钮设置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStop(EventPlayStopModel playModel) {
        mBtnPlay.setImageResource(R.mipmap.play_btn);
    }


    //暂停按钮设置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(EventPlayStartModel startModel) {
        mBtnPlay.setImageResource(R.mipmap.play_stop);
    }


    //进度条缓存设置
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPosition(EventPlaySeekModel seekModel) {
        if (seekModel == null) {
            return;
        }
        mSeekBar.setSecondaryProgress(seekModel.getPosition());
    }

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isLoding(EventPlayProgressModel progressModel) {
        if (progressModel == null) {
            return;
        }
        mSeekBar.setEnabled(!progressModel.isProgress());
        mSeekBar.setLoading(progressModel.isProgress());
    }


    private void initView() {
        mIvPlayWay.setImageResource(mStoryPlayerPresenter.getPlayModelState(mPlayModelState));
        mStoryPlayerPresenter.setModelState(mPlayModelState, mPlayerManager);

        if (mPlayerManager.isPlaying()) {
            mBtnPlay.setImageResource(R.mipmap.play_stop);
        } else {
            mBtnPlay.setImageResource(R.mipmap.play_btn);
        }

        String imagurl = mStoryPlayerPresenter.getImagurl(mPlayerManager);
        if (imagurl != null) {
            Glide.with(this).load(imagurl).into(mSoundCover);
            Glide.with(this).load(imagurl)
                    .transform(mBlurTransformation)
                    .crossFade()
                    .into(mIvBackground);
        }

//        if (mStoryPlayerPresenter.getTitle() != null){
//        mTvTitle.setText(mStoryPlayerPresenter.getTitle());
//        }
    }


    private void initListener() {
        mSeekBar.setOnSeekBarChangeListener(this);
    }


    @Override
    protected void dismissNewok() {

    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back, R.id.iv_play_way, R.id.play_or_pause, R.id.pre_sound,
            R.id.iv_next_story, R.id.iv_story_download, R.id.iv_story_push})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_play_way:
                mPlayModelState++;
                if (mPlayModelState == 1) {
                    mIvPlayWay.setImageResource(R.mipmap.suiji);
                    printn(getString(R.string.random));
                }

                if (mPlayModelState == 2) {
                    mIvPlayWay.setImageResource(R.mipmap.play_way);
                    printn(getString(R.string.single));
                }

                if (mPlayModelState == 3) {
                    mIvPlayWay.setImageResource(R.mipmap.xunhuan);
                    mPlayModelState = 0;
                    printn(getString(R.string.cycle));
                }
                mStoryPlayerPresenter.setModelState(mPlayModelState, mPlayerManager);
                SharedPreferencesUtils.setParam(this, "mPlayModelState", mPlayModelState);
                //单曲循环
                break;

            case R.id.play_or_pause:
                //播放
                if (mPlayerManager.isPlaying()) {
                    mPlayerManager.pause();
                } else {
                    mPlayerManager.play();
                }
                break;

            case R.id.pre_sound:
                //上一曲
                mPlayerManager.playPre();
                mXimalaya.setDefaultPagesize(100);
                break;

            case R.id.iv_next_story:
                //下一曲
                mPlayerManager.playNext();
                break;

            case R.id.iv_story_download:
                //下载
                break;

            case R.id.iv_story_push:
                if (mTrack != null) {
                    if (mTrack.getTrackTitle() != null) {
                        mStoryPlayerPresenter.appSendStoryInfoToImei(MyApplication.sToken, mAppAccount,
                                MyApplication.sDeviceListBean.getImei(), mTrack.getDataId(),
                                mTrack.getDuration(), mTrack.getCoverUrlSmall(), mTrack.getTrackTitle(),
                                mTrack.getPlayUrl64());
                    }
                }

                //推送给手表
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mUpdateProgress = false;
    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPlayerManager.seekToByPercent(seekBar.getProgress() / (float) seekBar.getMax());
        mUpdateProgress = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }


    /**
     * 移动网络
     */
    @Override
    protected void netWork4G() {
        mIsNetWork = false;
    }


    /**
     * 无网络
     */
    @Override
    protected void noNetwork() {
    }


    /**
     * wifi
     */
    @Override
    protected void theNetwork() {
        mIsNetWork = true;
    }


    /**
     * 推送故事成功的通知
     */
    public void pushSuccess() {
        printn(getString(R.string.push_success));

    }


    /**
     * 推送故事失败的通知
     *
     * @param s
     */
    public void pushFaiure(ResponseInfoModel s) {

    }
}
