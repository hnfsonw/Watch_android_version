package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.fragment.FamilyFragment;
import com.loybin.baidumap.ui.view.ContactFragmentDialog;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 下午4:41
 * 描   述: 手表通讯录的业务逻辑
 */
public class WatchLeisuresPresenter extends BasePresenter {

    private static final String TAG = "WatchLeisureActivity";
    private Context mContext;

    private FamilyFragment mBookActivity;
    public Call<ResponseInfoModel> mGetGroupMemberList;
    private ContactFragmentDialog mContactDialog;


    public WatchLeisuresPresenter(Context context, FamilyFragment watchLeisureActivity) {
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
        LogUtils.d(TAG, "获取手表通讯录列表: " + String.valueOf(paramsMap));
        mGetGroupMemberList = mWatchService.queryDeviceContractsListForApp(paramsMap);
        if (!inShow) {
            inShow = false;
        }
        mGetGroupMemberList.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.d(TAG, "parserJson: " + data.getResultMsg());
        LogUtils.d(TAG, "parserJson: " + data.getResult().memberList.size());
        List<ResponseInfoModel.ResultBean.MemberListBean> memberList = data.getResult().getMemberList();
        if (memberList.size() > 0) {
            LogUtils.d(TAG, "parserJson: " + memberList.size());
            LogUtils.d(TAG, "acountType: " + memberList.get(0).acountType);
            LogUtils.d(TAG, "imgUrl: " + memberList.get(0).imgUrl);
            LogUtils.d(TAG, "groupId: " + memberList.get(0).groupId);
            LogUtils.d(TAG, "birthday: " + memberList.get(0).birthday);
            LogUtils.d(TAG, "phone: " + memberList.get(0).phone);
            LogUtils.d(TAG, "acountName: " + memberList.get(0).acountName);
            LogUtils.d(TAG, "address: " + memberList.get(0).address);
            LogUtils.d(TAG, "email: " + memberList.get(0).email);
            LogUtils.d(TAG, "nickName: " + memberList.get(0).nickName);
            LogUtils.d(TAG, "relation: " + memberList.get(0).relation);
            LogUtils.d(TAG, "gender: " + memberList.get(0).gender);
            LogUtils.d(TAG, "isAdmin: " + memberList.get(0).isAdmin);
            LogUtils.d(TAG, "acountId: " + memberList.get(0).acountId);
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
//        mBookActivity.dismissLoading();
        LogUtils.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
//        mBookActivity.dismissLoading();
//        mBookActivity.printn(mContext.getString(R.string.Network_Error));
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
//        mBookActivity.dismissLoading();
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
//        mBookActivity.dismissLoading();
        mBookActivity.noticeSuccess();
    }


    /**
     * 全局的联系人添加通知
     *
     * @param body
     */
    public void showContactDialog(String body) {
        try {
            LogUtils.e(TAG, "showContactDialog");
            if (mContactDialog != null) {
                mContactDialog.dismiss();
                mContactDialog = null;
            }
            mContactDialog = new ContactFragmentDialog(mContext, mBookActivity);
            mContactDialog.show();
            String bodys = body + "";
            LogUtils.e(TAG, "bodys " + bodys);
            String[] split = bodys.split(":");
            if (split.length > 1) {
                mContactDialog.initMessage(split[1]);
            } else {
                mContactDialog.initMessage(bodys);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "showContactDialog 异常 " + e.getMessage());
        }

    }
}
