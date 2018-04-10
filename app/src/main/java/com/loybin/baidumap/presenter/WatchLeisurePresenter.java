package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.WatchLeisureActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 下午4:41
 * 描   述: 手表通讯录的业务逻辑
 */
public class WatchLeisurePresenter extends BasePresenter {

    private static final String TAG = "WatchLeisureActivity";
    private Context mContext;

    private WatchLeisureActivity mBookActivity;
    public Call<ResponseInfoModel> mGetGroupMemberList;


    public WatchLeisurePresenter(Context context, WatchLeisureActivity watchLeisureActivity) {
        super(context);
        mContext = context;
        mBookActivity = watchLeisureActivity;
    }


    /**
     * 获取设备联系人列表【APP】
     *
     * @param acountId
     * @param token
     * @param groupId
     */
    public void loadingList(long acountId, String token, long groupId, boolean inShow) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("groupId", groupId);
        Log.d(TAG, "获取手表通讯录列表: " + String.valueOf(paramsMap));
        mGetGroupMemberList = mWatchService.queryDeviceContractsListForApp(paramsMap);
        if (!inShow) {
            inShow = false;
            mBookActivity.showLoading("",mContext);
        }
        mGetGroupMemberList.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getResultMsg());
        Log.d(TAG, "parserJson: " + data.getResult().memberList.size());
        List<ResponseInfoModel.ResultBean.MemberListBean> memberList = data.getResult().getMemberList();
        if (memberList.size() > 0) {
            mBookActivity.mAdminRelation = memberList.get(0).relation;
            Log.d(TAG, "parserJson: " + memberList.size());
            Log.d(TAG, "acountType: " + memberList.get(0).acountType);
            Log.d(TAG, "imgUrl: " + memberList.get(0).imgUrl);
            Log.d(TAG, "groupId: " + memberList.get(0).groupId);
            Log.d(TAG, "birthday: " + memberList.get(0).birthday);
            Log.d(TAG, "phone: " + memberList.get(0).phone);
            Log.d(TAG, "acountName: " + memberList.get(0).acountName);
            Log.d(TAG, "address: " + memberList.get(0).address);
            Log.d(TAG, "email: " + memberList.get(0).email);
            Log.d(TAG, "nickName: " + memberList.get(0).nickName);
            Log.d(TAG, "relation: " + memberList.get(0).relation);
            Log.d(TAG, "gender: " + memberList.get(0).gender);
            Log.d(TAG, "isAdmin: " + memberList.get(0).isAdmin);
            Log.d(TAG, "acountId: " + memberList.get(0).acountId);
        }
        mBookActivity.onsuccess(memberList);
    }


    /**
     * 获取群组列表失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mBookActivity.dismissLoading();
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        mBookActivity.dismissLoading();
        mBookActivity.printn(mContext.getString(R.string.Network_Error));
        Log.d(TAG, "onDissms: " + s);
    }


    /**
     * 申请同意拒绝
     *
     * @param token
     * @param userAcountId
     * @param userdeviceId
     * @param replayStatus
     */
    public void setRefused(String token, String userAcountId, String userdeviceId, int replayStatus) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", userAcountId);
        hashMap.put("deviceId", userdeviceId);
        hashMap.put("replayStatus", replayStatus);

        LogUtils.e(TAG, "同意|拒绝 " + String.valueOf(hashMap));
        Call<ResponseInfoModel> call = mWatchService.replayApplyBindDevice(hashMap);
        call.enqueue(mCallback2);
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mBookActivity.dismissLoading();
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mBookActivity.dismissLoading();
        mBookActivity.noticeSuccess();
    }
}
