package com.loybin.baidumap.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.activity.SendApplyActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/17 下午2:39
 * 描   述: 发送申请 逻辑
 */
public class SendApplyPresenter extends BasePresenter {
    private static final String TAG = "SendApplyActivity";
    private Context mContext;
    private SendApplyActivity mSendApplyActivity;
    public Call<ResponseInfoModel> mCall;
    public Call<ResponseInfoModel> mAcountDeivceList;

    public SendApplyPresenter(Context context, SendApplyActivity sendApplyActivity) {
        super(context);
        mContext = context;
        mSendApplyActivity = sendApplyActivity;
    }

    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, data.getResultMsg());
        mSendApplyActivity.sendApplySuccess();
    }

    @Override
    protected void onFaiure(ResponseInfoModel s) {
        String resultMsg = s.getResultMsg();
        LogUtils.e(TAG, resultMsg);
        if (resultMsg.equals("该账户已与该设备绑定")) {
            Intent intent = new Intent(mContext, DevicesHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mSendApplyActivity.startActivity(intent);
            mSendApplyActivity.finishActivityByAnimation(mSendApplyActivity);
            return;
        }
        mSendApplyActivity.printn(resultMsg);
        mSendApplyActivity.dismissLoading();
    }


    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG, s);
        mSendApplyActivity.dismissLoading();
    }


    /**
     * 发送申请
     *
     * @param token
     * @param acountId
     * @param imei
     * @param relation
     */
    public void sendApply(String token, long acountId, String imei, String relation) {
        if (TextUtils.isEmpty(relation)) {
            mSendApplyActivity.relationEmpty(mContext.getString(R.string.relation_empty_));
            return;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);
        hashMap.put("imei", imei);
        hashMap.put("name", relation);

        LogUtils.e(TAG, "发送申请 " + String.valueOf(hashMap));
        mCall = mWatchService.applyBindDevice(hashMap);
        mSendApplyActivity.showLoading("",mContext);
        mCall.enqueue(mCallback);
    }


    /**
     * 判断环信有没有登入成功
     */
    public void chekEMCLogin() {
        LogUtils.e(TAG, "环信是否登入  " + EMClient.getInstance().isLoggedInBefore());
        LogUtils.e(TAG, "是否连接上了环信服务器 " + EMClient.getInstance().isConnected());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!EMClient.getInstance().isLoggedInBefore()) {
                    LogUtils.e(TAG, "环信还没登入 去登入");
                    //环信登入
                    EMClient.getInstance().login(mSendApplyActivity.mPhone, mSendApplyActivity.mMd5Password, mEMCallBack);
                } else {
                    if (!EMClient.getInstance().isConnected()) {
                        LogUtils.e(TAG, "环信登入了,服务i起没连上");
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Log.i(TAG, "退出成功");
                                //环信登入
                                EMClient.getInstance().login(mSendApplyActivity.mPhone, mSendApplyActivity.mMd5Password, mEMCallBack);
                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.i(TAG, "退出失败 " + i + " - " + s);
                            }

                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });
                    }
                }
            }
        }, 10000);
    }


    EMCallBack mEMCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            // 加载所有会话到内存
            EMClient.getInstance().chatManager().loadAllConversations();
            // 加载所有群组到内存，如果使用了群组的话
            EMClient.getInstance().groupManager().loadAllGroups();
            // 登录成功跳转界面
            Log.d(TAG, "run: " + "环信已经登入了" + Thread.currentThread().getName());
        }

        @Override
        public void onError(int i, String s) {

        }

        @Override
        public void onProgress(int i, String s) {

        }
    };


    /**
     * 获取所有设备
     *
     * @param acountId
     * @param token
     */
    public void getAcountDeivceList(long acountId, String token) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId + "");
        paramsMap.put("token", token);

        Log.e(TAG, "toRegis: " + String.valueOf(paramsMap));
        mAcountDeivceList = mWatchService.getAcountDeivceList(paramsMap);
        mSendApplyActivity.showLoading("",mContext);
        mAcountDeivceList.enqueue(mCallback3);
    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        //
        LogUtils.e(TAG, body.getResultMsg());
        List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = body.getResult().getDeviceList();
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1, ResponseInfoModel.ResultBean.DeviceListBean o2) {
                //降序排序
                return (o2.getIsAdmin() - o1.getIsAdmin());
            }
        });
        mSendApplyActivity.selectDialogShow(deviceList);
    }



    /**
     * 验证Token
     * @param token
     */
    public void verificationToken(String token) {
        HashMap<String,String > hashMap = new HashMap<>();
        hashMap.put("token",token);
        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.verificationToken(hashMap);
        responseInfoModelCall.enqueue(mCallback7);
    }


    @Override
    protected void tokenSuccess(ResponseInfoModel body) {
        mSendApplyActivity.mIsToken = false;
        ResponseInfoModel.ResultBean result = body.getResult();
        boolean verification = result.isVerification();
        if (!verification){
            exit();
        }else {
            LogUtils.e(TAG,"tokenSuccess");
//            mDLA.mqttConnect();
        }
    }
}
