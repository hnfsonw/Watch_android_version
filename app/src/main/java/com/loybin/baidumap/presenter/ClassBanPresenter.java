package com.loybin.baidumap.presenter;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ClassBanActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by huangz on 17/8/26.
 */

public class ClassBanPresenter extends BasePresenter {

    private Context mContext;
    private ClassBanActivity mClassBanActivity;
    public Call<ResponseInfoModel> mForbiddenTimeList;
    public Call<ResponseInfoModel> mResponseInfoModelCall;

    public ClassBanPresenter(Context context, ClassBanActivity classBanActivity) {
        super(context);
        mContext = context;
        mClassBanActivity = classBanActivity;
    }

    /**
     * 查询上课禁用列表成功回调
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e("ClassBanActivity", "查询成功:" + data.getResultMsg());
        mClassBanActivity.setClassBanList(data.getResult().getForbiddenTimeList());
        mClassBanActivity.dismissLoading();
    }


    /**
     * 查询上课禁用列表失败回调
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e("ClassBanActivity", "请求失败:" + s.getResultMsg());
        mClassBanActivity.dismissLoading();

    }


    /**
     * 更改上课禁用列表开关成功回调
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e("ClassBanActivity", body.getResultMsg());
        mClassBanActivity.dismissLoading();
        mClassBanActivity.changeTurnSuccess();
    }


    /**
     * 更改上课禁用列表开关失败回调
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e("ClassBanActivity", "请求错误:" + body.getResultMsg());
        mClassBanActivity.dismissLoading();

    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e("ClassBanActivity", "请求消失~~~");
    }

    /**
     * 删除成功回调
     *
     * @param body
     */
    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        LogUtils.e("ClassBanActivity", "删除成功");
        mClassBanActivity.dismissLoading();
        mClassBanActivity.deleteClassBanSuccess();
    }


    /**
     * 获取上课禁用列表
     */
    public void getClassBanList(String token, int deviceId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", deviceId);

        LogUtils.e("ClassBanActivity", String.valueOf(hashMap));
        mForbiddenTimeList = mWatchService.queryForbiddenTimeByDeviceId(hashMap);
        mClassBanActivity.showLoading("",mContext);
        mForbiddenTimeList.enqueue(mCallback);
    }


    /**
     * 更新开关接口
     * @param token
     * @param id
     * @param state
     */
    public void updateForbiddenTimeStateById(String token, long id, String state) {
        LogUtils.e("ClassBanActivity", "发送开关更改请求");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("id", id);
        hashMap.put("state", state);

        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.updateForbiddenTimeStateById(hashMap);

        mClassBanActivity.showLoading("",mContext);
        responseInfoModelCall.enqueue(mCallback2);

    }


    public void deleteForbiddenTimeById(String token, long id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("id", id);

        mForbiddenTimeList = mWatchService.deleteForbiddenTimeById(hashMap);
        mClassBanActivity.showLoading("",mContext);
        mForbiddenTimeList.enqueue(mCallback3);
    }
}
