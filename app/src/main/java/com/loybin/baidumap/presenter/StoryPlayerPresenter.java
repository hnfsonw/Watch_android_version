package com.loybin.baidumap.presenter;

import android.content.Context;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.StoryPlayerActivity;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/29 下午5:26
 * 描   述: 音乐播放器业务
 */
public class StoryPlayerPresenter extends BasePresenter {
    private static final String TAG = "StoryPlayerActivity";
    private Context mContext;
    private StoryPlayerActivity mStoryPlayerActivity;
    private String mTitle;
    private String mCoverUrl;

    public StoryPlayerPresenter(Context context, StoryPlayerActivity storyPlayerActivity) {
        super(context);
        mContext = context;
        mStoryPlayerActivity = storyPlayerActivity;
    }


    /**
     * 获取故事title
     *
     * @return
     */
    public String getTitle() {
        return mTitle;
    }

    public int getPlayModelState(int playModelState) {
        switch (playModelState) {
            case 0:
                return R.mipmap.xunhuan;
            case 1:
                return R.mipmap.suiji;

            case 2:
                return R.mipmap.play_way;

            default:
                return R.mipmap.xunhuan;

        }
    }


    public String getImagurl(XmPlayerManager mPlayerManager) {
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
            return mCoverUrl;

        }
        return mCoverUrl;
    }


    /**
     * 设置播放循环
     *
     * @param playModelState
     * @param mPlayerManager
     */
    public void setModelState(int playModelState, XmPlayerManager mPlayerManager) {
        switch (playModelState) {
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


    /**
     * 推送故事给手表
     *
     * @param token
     * @param appAccount
     * @param imei
     * @param dataId
     * @param duration
     * @param coverUrlSmall
     * @param trackTitle
     */
    public void appSendStoryInfoToImei(String token, String appAccount, String imei, long dataId,
                                       int duration, String coverUrlSmall, String trackTitle,
                                       String storyClearUrl) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountName", appAccount);
        hashMap.put("imei", imei);
        hashMap.put("storyId", dataId);
        hashMap.put("storyTime", duration);
        hashMap.put("storyImgUrl", coverUrlSmall);
        hashMap.put("storyName", trackTitle);
        hashMap.put("storyClearUrl", storyClearUrl);

        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.appSendStoryInfoToImei(hashMap);
        mStoryPlayerActivity.showLoading("",mContext);
        responseInfoModelCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mStoryPlayerActivity.dismissLoading();
        mStoryPlayerActivity.pushSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mStoryPlayerActivity.dismissLoading();
        LogUtils.e(TAG, "onFaiure " + s.getResultMsg());
        int errorCode = s.getErrorCode();
        chekErrorCode(errorCode);
        mStoryPlayerActivity.pushFaiure(s);

    }


    @Override
    protected void onDissms(String s) {
        mStoryPlayerActivity.dismissLoading();
        LogUtils.e(TAG, "onDissms " + s);
    }


    /**
     * 强制手表关机判断错误码
     *
     * @param errorCode
     */
    public void chekErrorCode(int errorCode) {
        switch (errorCode) {
            case 92301:
                mStoryPlayerActivity.printn(mContext.getString(R.string.cant_repeat_the_operation_for_seconds));
                break;

            case 92302:
                mStoryPlayerActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92303:
                mStoryPlayerActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92304:
                mStoryPlayerActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 90211:
                mStoryPlayerActivity.printn(mContext.getString(R.string.the_watch_is_not_online));
                break;

            case 96005:
                mStoryPlayerActivity.printn(mContext.getString(R.string.the_watches_have_subscribed_to_the_story));
                break;

            case 96007:
                mStoryPlayerActivity.printn(mContext.getString(R.string.subscription_amount_has_the_ultra_limit));
                break;

            case 96008:
                mStoryPlayerActivity.printn(mContext.getString(R.string
                        .subscribe_to_the_story_the_total_capacity_is_too_large_suggest_to_clear_subscription_again));
                break;

            default:
                break;
        }
    }

}
