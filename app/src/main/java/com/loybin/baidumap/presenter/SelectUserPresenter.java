package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.SelectUserActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:24
 * 描   述: 请选择用户的业务逻辑
 */
public class SelectUserPresenter extends BasePresenter {

    private static final String TAG = "SelectUserActivity";
    private Context mContext;
    private SelectUserActivity mSelectUserActivity;
    private boolean mIsShow;
    public Call<ResponseInfoModel> mGetGroupMemberList;
    public Call<ResponseInfoModel> mDisBandOneAcount;
    public Call<ResponseInfoModel> mDisBandAcountAndChangeAdmin;

    public SelectUserPresenter(Context context, SelectUserActivity selectUserActivity) {
        super(context);
        mContext = context;
        mSelectUserActivity = selectUserActivity;

    }


    /**
     * 获取群组列表
     *
     * @param acountId
     * @param token
     * @param groupId
     */
    public void loadingList(long acountId, String token, long groupId, boolean isShow) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId + "");
        paramsMap.put("token", token);
        paramsMap.put("groupId", groupId);

        mGetGroupMemberList = mWatchService.getGroupMemberList(paramsMap);
        if (!isShow) {
            mIsShow = false;
            mSelectUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        }
        mGetGroupMemberList.enqueue(mCallback);
    }


    /**
     * 获取群组列表成功的回掉
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getResultMsg());
        List<ResponseInfoModel.ResultBean.MemberListBean> memberList = data.getResult().getMemberList();
        if (memberList.size() > 0) {
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
        mSelectUserActivity.onSuccess(memberList);
    }


    /**
     * 获取群组列表失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mSelectUserActivity.dismissLoading();
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        mSelectUserActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
        mSelectUserActivity.printn(mContext.getString(R.string.Network_Error));
    }


    /**
     * 解除单个普通用户
     *
     * @param memberListBean
     * @param token
     */
    public void removeOrdinary(ResponseInfoModel.ResultBean.MemberListBean memberListBean, String token, int deviceId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", memberListBean.getAcountId());
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        Log.d(TAG, "解除单个普通用户: " + String.valueOf(paramsMap));
        mDisBandOneAcount = mWatchService.disBandOneAcount(paramsMap);
        mSelectUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandOneAcount.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        //回掉解除普通成员
        if (mSelectUserActivity.mOrdinary.equals("ordinary")) {
            mSelectUserActivity.removeSuccess();
            Log.d(TAG, "onSuccess+ordinary: " + body.getResultMsg());
        }

        //回调移交成功,解除自己
        if (mSelectUserActivity.mManagement.equals("management")) {
            mSelectUserActivity.successManagement();
            Log.d(TAG, "onSuccess + management: " + body.getResultMsg());
        }


    }


    @Override
    protected void onError(ResponseInfoModel body) {
        //解除普通成员失败
        if (mSelectUserActivity.mOrdinary.equals("ordinary")) {
            mSelectUserActivity.removeError(body.getResultMsg());
            Log.d(TAG, "onError+ordinary: " + body.getResultMsg());
        }

        if (mSelectUserActivity.mManagement.equals("management")) {
            mSelectUserActivity.errorManagement(body.getResultMsg());
            Log.d(TAG, "onError +management : " + body.getResultMsg());
        }

    }


    /**
     * 移交管理权限,解绑自己
     *
     * @param managementListBean 当前点击的条目Bean.获取要移交的ID
     * @param token
     * @param deviceId
     * @param acountId
     */
    public void handOverAdmin(ResponseInfoModel.ResultBean.MemberListBean managementListBean,
                              String token, int deviceId, long acountId) {

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);
        paramsMap.put("adminId", managementListBean.acountId);
        Log.d(TAG, "移交管理权限,解绑自己: " + String.valueOf(paramsMap));

        mDisBandAcountAndChangeAdmin = mWatchService.changeAdminBandDeviceContracts(paramsMap);
        mSelectUserActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDisBandAcountAndChangeAdmin.enqueue(mCallback2);
    }
}
