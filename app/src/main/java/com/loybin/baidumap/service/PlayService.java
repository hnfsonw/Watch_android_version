package com.loybin.baidumap.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loybin.baidumap.model.EventPlayProgressModel;
import com.loybin.baidumap.model.EventPlaySeekModel;
import com.loybin.baidumap.model.EventPlayStartModel;
import com.loybin.baidumap.model.EventPlayStopModel;
import com.loybin.baidumap.model.MessageEventModel;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.TimeUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerConfig;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager;

import org.greenrobot.eventbus.EventBus;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/28 上午10:36
 * 描   述: 喜马拉雅故事播放器服务
 */
public class PlayService extends Service {


    private static final String TAG = "PlayService";
    private CommonRequest mXimalaya;
    private XmPlayerManager mPlayerManager;
    private PlayServiceStub mBinder = new PlayServiceStub(this);
    private String mTitle;
    private String mCoverUrl;
    private MessageEventModel mMessageEvent;
    private EventPlayStopModel mPlayModel;
    private EventPlayStartModel mStartModel;
    private EventPlaySeekModel mSeekModel;
    private EventPlayProgressModel mProgressModel;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initMediaPlayer();
        return START_STICKY;
    }


    private void initMediaPlayer() {
        LogUtils.e(TAG,"initMediaPlayer");
        XmPlayerConfig.getInstance(this).setUseSystemLockScreen(false);
        if (mMessageEvent == null){
        mMessageEvent = new MessageEventModel();
        }
        if (mPlayModel == null){
        mPlayModel = new EventPlayStopModel();
        }
        if (mStartModel == null){
        mStartModel = new EventPlayStartModel();
        }
        if (mSeekModel == null){
            mSeekModel = new EventPlaySeekModel();
        }
        if (mProgressModel == null){
        mProgressModel = new EventPlayProgressModel();
        }

        mXimalaya = CommonRequest.getInstanse();
        mPlayerManager = XmPlayerManager.getInstance(this);

        mPlayerManager.init();
        mPlayerManager.addPlayerStatusListener(mPlayerStatusListener);
//        mPlayerManager.addAdsStatusListener(mAdsListener);
        mPlayerManager.addOnConnectedListerner(new XmPlayerManager.IConnectListener() {
            @Override
            public void onConnected() {
                mPlayerManager.removeOnConnectedListerner(this);

                mXimalaya.setDefaultPagesize(50);
                mPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP);
                LogUtils.e(TAG,"播放器初始化成功");
            }
        });

        // 此代码表示播放时会去监测下是否已经下载
        XmPlayerManager.getInstance(this).setCommonBusinessHandle(XmDownloadManager.getInstance());
    }


    private IXmPlayerStatusListener mPlayerStatusListener = new IXmPlayerStatusListener() {

        @Override
        public void onSoundPrepared() {
            LogUtils.i(TAG, "onSoundPrepared");
//            mSeekBar.setEnabled(true);
            mProgressModel.setProgress(false);
            EventBus.getDefault().post(mProgressModel);
        }

        @Override
        public void onSoundSwitch(PlayableModel laModel, PlayableModel curModel) {
            LogUtils.i(TAG, "onSoundSwitch index:" + curModel);
            PlayableModel model = mPlayerManager.getCurrSound();
            if (model != null) {
                mTitle = null;
                mCoverUrl = null;
                if (model instanceof Track) {
                    Track info = (Track) model;
                    mTitle = info.getTrackTitle();
                    mCoverUrl = info.getCoverUrlLarge();
                } else if (model instanceof Schedule) {
                    Schedule program = (Schedule) model;
                    mTitle = program.getRelatedProgram().getProgramName();
                    mCoverUrl = program.getRelatedProgram().getBackPicUrl();
                } else if (model instanceof Radio) {
                    Radio radio = (Radio) model;
                    mTitle = radio.getRadioName();
                    mCoverUrl = radio.getCoverUrlLarge();
                }
                mMessageEvent.setTitle(mTitle);
                mMessageEvent.setImagurl(mCoverUrl);
                LogUtils.e("StoryPlayerActivity",mTitle + "`!!" + mCoverUrl +"33"+Thread.currentThread().getName());
                EventBus.getDefault().post(mMessageEvent);

            }
            updateButtonStatus();
        }


        private void updateButtonStatus() {
//            if (mPlayerManager.hasPreSound()) {
//                mBtnPreSound.setEnabled(true);
//            } else {
//                mBtnPreSound.setEnabled(false);
//            }
//            if (mPlayerManager.hasNextSound()) {
//                mBtnNextSound.setEnabled(true);
//            } else {
//                mBtnNextSound.setEnabled(false);
//            }
        }


        @Override
        public void onPlayStop() {
            LogUtils.i(TAG, "onPlayStop");
            EventBus.getDefault().post(mPlayModel);
        }


        @Override
        public void onPlayStart() {
            LogUtils.i(TAG, "onPlayStart");
            EventBus.getDefault().post(mStartModel);
        }


        @Override
        public void onPlayProgress(int currPos, int duration) {
            String title = "";
            PlayableModel info = mPlayerManager.getCurrSound();
            if (info != null) {
                if (info instanceof Track) {
                    title = ((Track) info).getTrackTitle();
                } else if (info instanceof Schedule) {
                    title = ((Schedule) info).getRelatedProgram().getProgramName();
                } else if (info instanceof Radio) {
                    title = ((Radio) info).getRadioName();
                }
            }
            mMessageEvent.setTitle(title);
            mMessageEvent.setCurrPos(TimeUtil.formatTimes(currPos));
            mMessageEvent.setDuration(TimeUtil.formatTimes(duration));
            mMessageEvent.setProgress((int) (100 * currPos / (float) duration));
            mMessageEvent.setDurations(duration);
            EventBus.getDefault().post(mMessageEvent);
        }


        @Override
        public void onPlayPause() {
            LogUtils.i(TAG, "onPlayPause");
            EventBus.getDefault().post(mPlayModel);
        }


        @Override
        public void onSoundPlayComplete() {
            EventBus.getDefault().post(mPlayModel);
            LogUtils.i(TAG, "onSoundPlayComplete");
        }


        @Override
        public boolean onError(XmPlayerException exception) {
            EventBus.getDefault().post(mPlayModel);
            LogUtils.i(TAG, "onError " + exception.getMessage());
            return false;
        }


        @Override
        public void onBufferProgress(int position) {
            mSeekModel.setPosition(position);
            EventBus.getDefault().post(mSeekModel);

        }


        public void onBufferingStart() {
//            mSeekBar.setEnabled(false);
//            mProgress.setVisibility(View.VISIBLE);
            mProgressModel.setProgress(true);
            EventBus.getDefault().post(mProgressModel);
        }


        public void onBufferingStop() {
//            mSeekBar.setEnabled(true);
            mProgressModel.setProgress(false);
            EventBus.getDefault().post(mProgressModel);
        }

    };


    private IXmAdsStatusListener mAdsListener = new IXmAdsStatusListener() {


        @Override
        public void onStartPlayAds(Advertis ad, int position) {
            LogUtils.e(TAG, "onStartPlayAds, Ad:" + ad.getName() + ", pos:" + position);
            if (ad != null) {
//                x.image().bind(mSoundCover ,ad.getImageUrl());
            }
        }


        @Override
        public void onStartGetAdsInfo() {
            LogUtils.e(TAG, "onStartGetAdsInfo");
//            mBtnPlay.setEnabled(false);
//            mSeekBar.setEnabled(false);
        }


        @Override
        public void onGetAdsInfo(AdvertisList ads) {
            LogUtils.e(TAG, "onGetAdsInfo " + (ads != null));
        }


        @Override
        public void onError(int what, int extra) {
            LogUtils.e(TAG, "onError what:" + what + ", extra:" + extra);
        }


        @Override
        public void onCompletePlayAds() {
            LogUtils.e(TAG, "onCompletePlayAds");
//            mBtnPlay.setEnabled(true);
//            mSeekBar.setEnabled(true);
//            PlayableModel model = mPlayerManager.getCurrSound();
//            if (model != null && model instanceof Track) {
//                x.image().bind(mSoundCover ,((Track) model).getCoverUrlLarge());
//            }
        }


        @Override
        public void onAdsStopBuffering() {
            Log.e(TAG, "onAdsStopBuffering");
        }


        @Override
        public void onAdsStartBuffering() {
            Log.e(TAG, "onAdsStartBuffering");
        }
    };



    @Override
    public void onDestroy() {
        if (mPlayerManager != null) {
            mPlayerManager.removePlayerStatusListener(mPlayerStatusListener);
        }
        XmPlayerManager.release();
        super.onDestroy();
    }


    /**
     * 播放歌曲列表
     * @param list
     * @param position
     */
    public void playList(TrackList list, int position) {
        LogUtils.e(TAG,"播放歌曲列表 " + position);
        mPlayerManager.playList(list,position);
    }

    public String getTitle() {
        return mTitle;
    }


    /**
     * 下一曲
     */
    public void playNext() {
        mPlayerManager.playNext();
    }


    /**
     * 上一曲
     */
    public void playPre() {
        mPlayerManager.playPre();
        mXimalaya.setDefaultPagesize(100);
    }


    /**
     * 获取播放状态
     * @return
     */
    public boolean getIsPlaying() {
        if (mPlayerManager != null){
            return mPlayerManager.isPlaying();
        }
        return false;
    }


    /**
     * 播放
     */
    public void play() {
        mPlayerManager.play();
    }


    /**
     * 暂停
     */
    public void pause() {
        mPlayerManager.pause();
    }

    /**
     * 滑动进度条
     * @param v
     */
    public void seekToByPercent(float v) {
        mPlayerManager.seekToByPercent(v);
    }


    /**
     * 设置播放模式
     * @param playModelState 0循环 1随机 2单曲
     */
    public void setModelState(int playModelState) {
        switch (playModelState){
            case 0:
                mPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP);
            break;

            case 1:
                mPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM);
            break;

            case 2:
                mPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP);
            break;

            default:
                mPlayerManager.setPlayMode(XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP);
                break;
        }
    }


    public String getImagurl() {
        return mCoverUrl;
    }
}
