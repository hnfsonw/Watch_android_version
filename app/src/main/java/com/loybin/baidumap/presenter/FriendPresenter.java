package com.loybin.baidumap.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.fragment.FriendFragment;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/27 下午2:40
 * 描   述: 手表朋友的列表 逻辑
 */
public class FriendPresenter extends BasePresenter {
    private static final String TAG = "FriendFragment";
    private Context mContext;
    private FriendFragment mFriendFragment;
    private Call<ResponseInfoModel> mMResponseInfoModelCall;

    public FriendPresenter(Context context, FriendFragment friendFragment) {
        super(context);
        mContext = context;
        mFriendFragment = friendFragment;
    }


    /**
     * 根据设备imei查询设备的好友列表信息接口
     *
     * @param token
     * @param imei
     */
    public void queryFriendsByImei(String token, String imei, boolean isShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("imei", imei);

        mMResponseInfoModelCall = mWatchService.queryFriendsByImei(hashMap);
        if (!isShow) {
            mFriendFragment.mIdProgress.setVisibility(View.VISIBLE);
        }
        mMResponseInfoModelCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, "parserJson " + data.getResultMsg());
        List<ResponseInfoModel.ResultBean.FriendsListBean> friendsList = data.getResult().getFriendsList();
//        for (int i = 0; i < 10; i++) {
//            ResponseInfoModel.ResultBean.FriendsListBean bean = new ResponseInfoModel.ResultBean.FriendsListBean();
//            if (i == 0 ){
//            bean.setNickName("张三");
//            }else {
//                bean.setNickName("李四");
//            }
//            bean.setPhone("电话 " +i);
//            if (i < 5){
//                bean.setGender(1);
//            }else {
//            bean.setGender(0);
//            }
//            friendsList.add(bean);
//        }
        LogUtils.e(TAG, "friendsList size " + friendsList.size());
        mFriendFragment.onSuccess(friendsList);
        mFriendFragment.mIdProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onFaiure(ResponseInfoModel data) {
        LogUtils.e(TAG, "onFaiure " + data.getResultMsg());
        mFriendFragment.mIdProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, "onDissms " + s);
        mFriendFragment.mIdProgress.setVisibility(View.GONE);
    }


    /**
     * 根据设备账户名和好友账户名删除设备好友
     *
     * @param token
     * @param acountId
     * @param acountName
     */
    public void deleteFriend(String token, String acountId, String acountName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountName", acountId);
        hashMap.put("memberName", acountName);

        LogUtils.e(TAG, "deleteFriend " + String.valueOf(hashMap));
        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.delDeviceFriendByAcountName(hashMap);
        responseInfoModelCall.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
        queryFriendsByImei(MyApplication.sToken, MyApplication.sDeviceListBean.getImei(), false);
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        Toast.makeText(mContext, body.getResultMsg(), Toast.LENGTH_SHORT).show();
    }
}
