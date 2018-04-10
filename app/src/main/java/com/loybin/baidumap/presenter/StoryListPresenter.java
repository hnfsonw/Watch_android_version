package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.StoryListActivity;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/30 下午4:46
 * 描   述: 单个故事专辑的业务类
 */
public class StoryListPresenter extends BasePresenter {
    private static final String TAG = "StoryListActivity";
    private Context mContext;
    private StoryListActivity mStoryListActivity;
    private boolean mLoading;
    private Gson mGson;
    public TrackList mTrackList;

    public StoryListPresenter(Context context, StoryListActivity storyListActivity) {
        super(context);
        mContext = context;
        mStoryListActivity = storyListActivity;
    }


    public void loadData(int pageId, long ids) {
        if (mLoading) {
            return;
        }
        mLoading = true;
        Map<String, String> param = new HashMap<String, String>();
        LogUtils.e(TAG, "mIds " + ids);
        param.put("album_id", ids + "");
        param.put("page", "" + pageId);
        param.put("sort", "desc");
        param.put("count", "" + 100);
        CommonRequest.getTracks(param, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList object) {
                mStoryListActivity.onSuccess(object);

                mLoading = false;
            }

            @Override
            public void onError(int code, String message) {
                Log.e(TAG, "onError " + code + ", " + message);
                mStoryListActivity.onError(code, message);
                mLoading = false;
            }
        });
    }


    /**
     * APP发送喜马拉雅讲故事的信息到手表端
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
                                       int duration, String coverUrlSmall, String trackTitle
            , long size, String storyClearUrl) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountName", appAccount);
        hashMap.put("imei", imei);
        hashMap.put("storyId", dataId);
        hashMap.put("storyTime", duration);
        hashMap.put("storyImgUrl", coverUrlSmall);
        hashMap.put("storyName", trackTitle);
        hashMap.put("size", size);
        hashMap.put("storyClearUrl", storyClearUrl);
        LogUtils.e(TAG, "appSendStoryInfoToImei " + String.valueOf(hashMap));
        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.appSendStoryInfoToImei(hashMap);
        mStoryListActivity.showLoading("",mContext);
        responseInfoModelCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, "parserJson " + data.getResultMsg());
        mStoryListActivity.pushSuccess();
    }


    @Override
    protected void onFaiure(ResponseInfoModel data) {
        LogUtils.e(TAG, "onFaiure " + data.getResultMsg());
        mStoryListActivity.dismissLoading();
        int errorCode = data.getErrorCode();
        LogUtils.e(TAG, "errorCode " + errorCode);
        chekErrorCode(errorCode);
    }


    @Override
    protected void onDissms(String data) {
        LogUtils.e(TAG, data);
        mStoryListActivity.dismissLoading();
    }


    /**
     * 强制手表关机判断错误码
     *
     * @param errorCode
     */
    public void chekErrorCode(int errorCode) {
        switch (errorCode) {
            case 92301:
                mStoryListActivity.printn(mContext.getString(R.string.cant_repeat_the_operation_for_seconds));
                break;

            case 92302:
                mStoryListActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92303:
                mStoryListActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 92304:
                mStoryListActivity.printn(mContext.getString(R.string.device_off));
                break;

            case 90211:
                mStoryListActivity.printn(mContext.getString(R.string.the_watch_is_not_online));
                break;

            case 96005:
                mStoryListActivity.printn(mContext.getString(R.string.the_watches_have_subscribed_to_the_story));
                break;

            case 96007:
                mStoryListActivity.printn(mContext.getString(R.string.subscription_amount_has_the_ultra_limit));
                break;

            case 96008:
                mStoryListActivity.printn(mContext.getString(R.string
                        .subscribe_to_the_story_the_total_capacity_is_too_large_suggest_to_clear_subscription_again));
                break;

        }
    }


    public Track parsingData(String local, int position) {
        try {
            if (mGson == null) {
                mGson = new Gson();
            }
            mTrackList = mGson.fromJson(local, TrackList.class);
            if (mTrackList != null && mTrackList.getTracks() != null) {
                return mTrackList.getTracks().get(position);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "解析异常 " + e.getMessage());
        }
        return null;
    }
}
