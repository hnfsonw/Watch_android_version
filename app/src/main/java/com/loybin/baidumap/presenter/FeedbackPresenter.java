package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.FeedbackActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/01 上午10:13
 * 描   述: 意见反馈提交操作
 */
public class FeedbackPresenter extends BasePresenter {
    private static final String TAG = "FeedbackActivity";
    private Context mContext;
    private FeedbackActivity mFeedbackActivity;
    public Call<ResponseInfoModel> mCall;

    public FeedbackPresenter(Context context, FeedbackActivity feedbackActivity) {
        super(context);
        mContext = context;
        mFeedbackActivity = feedbackActivity;
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, data.getResultMsg());
        mFeedbackActivity.submitSuccess();
    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.d(TAG, s.getResultMsg());
        mFeedbackActivity.dismissLoading();
        mFeedbackActivity.printn(s.getResultMsg());
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, s);
        mFeedbackActivity.dismissLoading();
    }

    /**
     * 提交意见反馈
     *
     * @param content
     * @param token
     * @param acountId
     */
    public void submit(String content, String token, long acountId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);
        hashMap.put("content", content);

        LogUtils.e(TAG, "insertOrUpdateOpinions " + String.valueOf(hashMap));

        mCall = mWatchService.insertOrUpdateOpinions(hashMap);
        mFeedbackActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);

    }
}
