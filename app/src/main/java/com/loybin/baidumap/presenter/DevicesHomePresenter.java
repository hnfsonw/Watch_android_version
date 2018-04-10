package com.loybin.baidumap.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.AnimationSet;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hojy.happyfruit.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.message.EMAMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.loybin.baidumap.model.HoleBean;
import com.loybin.baidumap.guide.NewbieGuide;
import com.loybin.baidumap.guide.NewbieGuideManager;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.MQTTModel;
import com.loybin.baidumap.model.ParametersModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ChatActivity;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.activity.SOSMapActivity;
import com.loybin.baidumap.ui.view.Notifier;
import com.loybin.baidumap.ui.view.UnBunDlingDialog;
import com.loybin.baidumap.ui.view.WatchNoticeDialog;
import com.loybin.baidumap.util.AppManagerUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.widget.chatrow.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 下午2:18
 * 描   述:  设备首页的业务逻辑
 */
public class DevicesHomePresenter extends BasePresenter {

    private static final String TAG = "DevicesHomeActivity";
    private static final String TAG2 = "MQTT";
    private DevicesHomeActivity mDLA;
    private Context mContext;
    private final SharedPreferences mGlobalvariable;
    public Call<ResponseInfoModel> mAcountDeivceList;
    public Call<ResponseInfoModel> mAppSendCMD;
    private ResponseInfoModel.ResultBean.DeviceListBean mDeviceListBean;
    public Call<ResponseInfoModel> mGroupMemberListAll;
    public UnBunDlingDialog mUnBunDlingDialog;
    public Marker mMarker;
    private boolean mIsEMCLogin = false;
    public boolean mIsNotice;//用来判断解绑通知重新获取列表数据
    private Gson mGson;
    private String mToJson;
    private JSONObject mParameters;
    public String mDeviceId;
    private String mImei;
    private String mNickName;
    private String mImgUrl;
    private String mBodys;
    private boolean misAdmin;
    private boolean mIsQueryEMC;
    private long mMsgId;
    private int mMsgType;
    private int mMsgAction;
    private Notifier mNotifier;


    public DevicesHomePresenter(Context context, DevicesHomeActivity devicesHomeActivity) {
        super(context);
        mContext = context;
        mDLA = devicesHomeActivity;
        mGlobalvariable = mContext.getSharedPreferences("globalvariable", 0);
        if (mNotifier == null){
            mNotifier = new Notifier();
            mNotifier.init(mContext);
        }
        setNotifier(mNotifier);
    }


    /**
     * 当前设备标记与信息
     */
    public void DrawableCarInformation(double lat, double lng, BitmapDescriptor bitmapDescriptor) {
        LatLng sourceLatLng = new LatLng(lat, lng);
        if (mMarker == null) {
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(sourceLatLng
                    , 17, 0, 0)));
            mMarker = mDLA.mAMap.addMarker(new MarkerOptions().
                    position(sourceLatLng).icon(bitmapDescriptor).anchor(0.5f, 0.78f));
            mMarker.setToTop();
        } else {
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(sourceLatLng
                    , 17, 0, 0)));
            mMarker.setPosition(sourceLatLng);
            mMarker.setIcon(bitmapDescriptor);
            mMarker.setToTop();
        }
    }


    /**
     * 移到latLng位置
     *
     * @param cameraUpdate
     */
    private void changeCamera(CameraUpdate cameraUpdate) {
//        mDLA.mAMap.moveCamera(cameraUpdate);
        mDLA.mAMap.animateCamera(cameraUpdate, 300, null);
    }


    /**
     * 获取设备列表
     *
     * @param token
     * @param acountId
     */
    public void getAcountDeivceList(String token, long acountId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("token", token);

        Log.e(TAG, "toRegis: " + String.valueOf(paramsMap) + "mIsNotice =" + mIsNotice);
        mAcountDeivceList = mWatchService.getAcountDeivceList(paramsMap);
        if (!mIsNotice) {
            mDLA.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        }
        mAcountDeivceList.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        //用来判断解绑通知重新获取列表数据
        if (mIsNotice) {
            LogUtils.d(TAG, "用来判断解绑通知重新获取列表数据" + "misAdmin = " + misAdmin);
            if (mDLA.mDeviceList != null) {
                mDLA.mDeviceList.clear();
                List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = data.getResult().getDeviceList();
                Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
                    @Override
                    public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1,
                                       ResponseInfoModel.ResultBean.DeviceListBean o2) {
                        //降序排序
                        return (o2.getIsAdmin() - o1.getIsAdmin());
                    }
                });
                mDLA.mDeviceList = deviceList;
                mDLA.mListSize = mDLA.mDeviceList.size();
                mDLA.mDeviceListBean = deviceList.get(MyApplication.sDeivceNumber);
                mIsNotice = false;
            }
            return;
        }

        LogUtils.d(TAG, "token: " + MyApplication.sToken);
        LogUtils.d(TAG, "acountId: " + MyApplication.sAcountId);
        LogUtils.d(TAG, "deviceId: " + MyApplication.sDeviceId);
        MyApplication.sDeviceList.clear();
        List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = data.getResult().getDeviceList();
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1,
                               ResponseInfoModel.ResultBean.DeviceListBean o2) {
                //降序排序
                return (o2.getIsAdmin() - o1.getIsAdmin());
            }
        });
        MyApplication.sDeviceList.addAll(deviceList);
        LogUtils.d(TAG, "deviceList.size(): " + deviceList.size());
        LogUtils.d(TAG, "MyApplication.sDeivceNumber: " + MyApplication.sDeivceNumber);
        if (deviceList.size() > 0) {
            try {

                mDeviceListBean = data.getResult().getDeviceList().get(MyApplication.sDeivceNumber);
                mDLA.isDeviceList = true;
                LogUtils.e(TAG, MyApplication.sDeviceListBean.getGroupId() + "    getGroupId");
                MyApplication.sDeviceId = mDeviceListBean.getDeviceId();
                mGlobalvariable.edit()
                        .putString("birthday", mDeviceListBean.getBirthday())
                        .putString("phone", mDeviceListBean.getPhone())
                        .putString("imei", mDeviceListBean.getImei())
                        .putString("imgUrl" + mDeviceListBean.getDeviceId(), mDeviceListBean.getImgUrl())
                        .putString("imgUrl", mDeviceListBean.getImgUrl())
                        .putString("sosPhone", mDeviceListBean.getSosPhone())
                        .putString("deviceName", mDeviceListBean.getDeviceName())
                        .putString("acountName", mDeviceListBean.getDeviceName())
                        .putString("nickName", mDeviceListBean.getNickName())
                        .putString("shortPhone", mDeviceListBean.getShortPhone())
                        .putString("email", mDeviceListBean.getEmail())
                        .putString("address", mDeviceListBean.getAddress())
                        .putString("deviceType", mDeviceListBean.getDeviceType())
                        .putLong("groupId", mDeviceListBean.getGroupId())
                        .putInt("gender" + mDeviceListBean.getDeviceId(), mDeviceListBean.getGender())
                        .putInt("gender", mDeviceListBean.getGender())
                        .putInt("isAdmin", mDeviceListBean.getIsAdmin())
                        .putInt("deviceId", mDeviceListBean.getDeviceId())
                        .putLong("watchAcountId", mDeviceListBean.getAcountId()).apply();
                LogUtils.d(TAG, "parserJson: " + data.getResultMsg());
                LogUtils.d(TAG, "getImgUrl: " + mDeviceListBean.getImgUrl());
                LogUtils.d(TAG, "getAcountName: " + mDeviceListBean.getAcountName());
                LogUtils.d(TAG, "getAddress: " + mDeviceListBean.getAddress());
                LogUtils.d(TAG, "getBirthday: " + mDeviceListBean.getBirthday());
                LogUtils.d(TAG, "getDeviceName: " + mDeviceListBean.getDeviceName());
                LogUtils.d(TAG, "getDeviceType: " + mDeviceListBean.getDeviceType());
                LogUtils.d(TAG, "getEmail: " + mDeviceListBean.getEmail());
                LogUtils.d(TAG, "getImei: " + mDeviceListBean.getImei());
                LogUtils.d(TAG, "getNickName: " + mDeviceListBean.getNickName());
                LogUtils.d(TAG, "getPhone: " + mDeviceListBean.getPhone());
                LogUtils.d(TAG, "getSosPhone: " + mDeviceListBean.getSosPhone());
                LogUtils.d(TAG, "getAcountId: " + mDeviceListBean.getAcountId());
                LogUtils.d(TAG, "getGender: " + mDeviceListBean.getGender());
                LogUtils.d(TAG, "getIsAdmin: " + mDeviceListBean.getIsAdmin());
                LogUtils.d(TAG, "getGroupId: " + mDeviceListBean.getGroupId());
                LogUtils.d(TAG, "watchAcountId: " + mDeviceListBean.getDeviceId());
            } catch (Exception e) {
                LogUtils.e(TAG, "异常" + e.getMessage());
                if (MyApplication.sDeivceNumber > 0) {
                    MyApplication.sDeivceNumber--;
                }
                getAcountDeivceList(MyApplication.sToken, MyApplication.sAcountId);
                mDLA.dismissLoading();
                mDLA.mAMap.clear();
            }
        }

        mDLA.onSuccess(data);

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mDLA.dismissLoading();
        mDLA.onError(s);
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        Log.d(TAG, "onDissms: " + s);
        mDLA.dismissLoading();
        mDLA.printn(mContext.getString(R.string.Network_Error));
        mDLA.dismissAnimation();
    }


    /**
     * 发送透传指令
     *
     * @param token
     * @param acountName
     * @param imei
     * @param message
     * @param command
     */
    public void appSendCMD(String token, String acountName, String imei, String message,
                           int command, long acountId) {
        String str = "{" + "acountId" + ":" + acountId + "}";
        String id = String.valueOf(str);
        Log.e(TAG, "appSendCMD: " + id);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("acountName", acountName);
        params.put("imei", imei);
        params.put("message", message);
        params.put("command", command);
        params.put("parameters", id);
        Log.e(TAG, "appSendCMD: " + String.valueOf(params));

        mAppSendCMD = mWatchService.appSendCMD(params);
        mAppSendCMD.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        ResponseInfoModel.ResultBean result = body.getResult();
        if (result != null) {
            MyApplication.sResult = result;
        }
        Log.d(TAG, "onSuccess: " + body.getResultMsg());
        Log.d(TAG, "getErrorCode: " + body.getErrorCode());
        if (body.getErrorCode() == 90211) {
            onError(body);
            mDLA.cmdSuccess(result);
            mDLA.dismissAnimation();
        } else {
            mDLA.cmdSuccess(result);
        }
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        Log.d(TAG, "onError: " + body.getResultMsg());
        if (body.getErrorCode() == 92302 || body.getErrorCode() == 90211) {
            mDLA.onCMDError(mContext.getString(R.string.watch_off));
            return;
        }
        mDLA.onCMDError(body.getResultMsg());
        mDLA.dismissAnimation();
    }


    public void queryDeviceStateByDeviceId(String token, int deviceId) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("token", token);

        Log.d(TAG, "queryDeviceStateByDeviceId: " + String.valueOf(paramsMap));
        mGroupMemberListAll = mWatchService.queryDeviceStateByDeviceId(paramsMap);
        mGroupMemberListAll.enqueue(mCallback3);
    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        mDLA.dismissLoading();
        LogUtils.e(TAG, "群聊设备获取成功" + body.getResultMsg());
        LogUtils.e(TAG, "设备开启关闭状态" + body.getResult().getBootState());
        LogUtils.e(TAG, "设备运行模式" + body.getResult().getBootState());
        LogUtils.e(TAG, "设备电池电量" + body.getResult().getPowerLevel());
        LogUtils.e(TAG, "拒接陌生来电开启状态 " + body.getResult().getStrangeCallSwitch());
        LogUtils.e(TAG, "定位模式 " + body.getResult().getLocationStyle());
        LogUtils.e(TAG, "打电话模式 " + body.getResult().getMobileStyle());
        LogUtils.d(TAG, "手表自动更新 " + body.getResult().getUpdatedFlag());
        mDLA.memberListSuccess(body);

    }


    /**
     * 收到解除消息
     *
     * @param result
     */
    public void receivesSolutionMessage(String result, String deviceId, String nickName) {
        if (MyApplication.sDeviceId == -1) {
            return;
        }
        LogUtils.d(TAG, "mDLA.mListSize " + mDLA.mListSize);

        if (mDLA.mListSize > 1) {
            MyApplication.sDeivceNumber--;
            if (MyApplication.sDeviceId == Long.parseLong(deviceId)) {
                //当前界面是自己
                LogUtils.e(TAG, "当前界面是自己");
                mUnBunDlingDialog = new UnBunDlingDialog(mContext, mDLA);
                mUnBunDlingDialog.show();
                mUnBunDlingDialog.setIsCanl(true);
                mUnBunDlingDialog.setCanceledOnTouchOutside(false);
                mUnBunDlingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mDLA.selectEquipment();
                    }
                });
                mUnBunDlingDialog.setTvRemove(nickName);
                mUnBunDlingDialog.setTvKnowThe(mContext.getString(R.string.to_select_equipment));
            } else {
                //当前界面是其它设备
                LogUtils.e(TAG, "当前界面是其它设备");
                mUnBunDlingDialog = new UnBunDlingDialog(mContext, mDLA);
                mUnBunDlingDialog.show();
                mUnBunDlingDialog.setIsCanl(false);
                mUnBunDlingDialog.setTvRemove(nickName);
            }

        } else {
            LogUtils.d(TAG, "MyApplication.sDeivceNumber =" + MyApplication.sDeivceNumber);
            MyApplication.sDeivceNumber = 0;
            LogUtils.e(TAG, "只有一个设备");
            mDLA.mAMap.clear();
            getAcountDeivceList(mDLA.mToken, mDLA.mAcountId);
        }
    }


    /**
     * 管理员移交管理员权限，并解绑自己，通知新管理员指令
     *
     * @param result
     * @param parameters
     */
    public void receivesAdmin(String result, ParametersModel.ParametersBean parameters) {
        mUnBunDlingDialog = new UnBunDlingDialog(mContext, mDLA);
        mUnBunDlingDialog.show();
        mUnBunDlingDialog.setTvRemove(parameters.getNickName() + mContext.getString(R.string.management_is_handed_over_to_you));
        getAcountDeivceList(mDLA.mToken, mDLA.mAcountId);
        mDLA.mAMap.clear();
    }


    /**
     * 管理员给设备添加绑定成员通知
     *
     * @param result
     * @param parameters
     */
    public void membersNotification(String result, ParametersModel.ParametersBean parameters) {
        WatchNoticeDialog watchNoticeDialog = new WatchNoticeDialog(mContext, mDLA);
        watchNoticeDialog.show();
        watchNoticeDialog.initTitle(parameters.getNickName() + mContext.getString(R.string.administrators_will_you_bound_to_watch));
        watchNoticeDialog.setTvCancel(mContext.getString(R.string.know_the));
        watchNoticeDialog.setDetermine(mContext.getString(R.string.check_the_equipment));
    }


    /**
     * 同意|拒绝
     *
     * @param token
     * @param useracountId
     * @param deviceId
     * @param replayStatus 0 拒绝 1 同意
     */
    public void setRefused(String token, String useracountId, String deviceId, int replayStatus) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", useracountId);
        hashMap.put("deviceId", deviceId);
        hashMap.put("replayStatus", replayStatus);

        LogUtils.e(TAG, "同意|拒绝 " + String.valueOf(hashMap));

        Call<ResponseInfoModel> call = mWatchService.replayApplyBindDevice(hashMap);
        call.enqueue(mCallback4);
    }


    /**
     * 同意成功的回调
     *
     * @param body
     */
    @Override
    protected void appCMDSuccess(ResponseInfoModel body) {
        LogUtils.d(TAG, body.getResultMsg());
        mDLA.dismissLoading();
    }


    /**
     * 判断环信有没有登入成功
     */
    public void chekEMCLogin() {
        LogUtils.d(TAG, "判断环信有没有登入成功");
        if (EMClient.getInstance().isLoggedInBefore() == true && EMClient.getInstance().isConnected() == true) {
            LogUtils.e(TAG, "环信已登入 " + " 环信已连接服务器");
            chekCache();
            getAllConversations();
            return;
        }
        if (!EMClient.getInstance().isLoggedInBefore()) {
            mDLA.mChekEMCLogin = false;
            LogUtils.e(TAG, "环信还没登入 去登入 " + Thread.currentThread().getName());
            //环信登入
            mDLA.mEMCLogin = 0;
            emcLogin();
        } else {
            if (!EMClient.getInstance().isConnected()) {
                LogUtils.e(TAG, "环信登入了,服务i起没连上");
                mDLA.mEMCLogin = 0;
                emcLogin();
            }
        }
    }


    public void chekCache() {
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDLA.mEMCLogin = 1;
            }
        }, 3000);
    }


    /**
     * 环信登入
     */
    private void emcLogin() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "退出成功");
                //环信登入
                EMClient.getInstance().login(mDLA.sPhone, mDLA.sMd5Password, mEMCallBack);
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


    public void chekEMCLogin(boolean isEMCLogin) {
        mIsQueryEMC = isEMCLogin;
        //用来判断环信是否登入的状态
        if (EMClient.getInstance().isLoggedInBefore() == true && EMClient.getInstance().isConnected() == true) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    mIsEMCLogin = true;
                    LogUtils.e(TAG, "环信已登入 " + " 环信已连接服务器");
                }
            });
            return;
        }
        LogUtils.e(TAG, "mIsEMCLogin " + mIsEMCLogin);
        if (!mIsEMCLogin) {
            if (mDLA.mHandler != null) {
                mDLA.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!EMClient.getInstance().isLoggedInBefore()) {
                            mDLA.mChekEMCLogin = false;
                            LogUtils.e(TAG, "环信还没登入 去登入 " + Thread.currentThread().getName());
                            //环信登入
                            emcLogin();
                            mIsEMCLogin = true;
                        } else {
                            if (!EMClient.getInstance().isConnected()) {
                                LogUtils.e(TAG, "环信登入了,环信服务器没连接上");
                                emcLogin();
                                mIsEMCLogin = true;
                            }
                        }
                    }
                });
            }
        }

    }


    /**
     * 获取环信会话消息,查询是否有未清理的通知消息
     */
    private void getAllConversations() {
//        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mDLA.mAppAccount);
////        List<EMMessage> allMessages1 = conversation.getAllMessages();
////        if (allMessages1 != null){
////        LogUtils.d(TAG,"EMMessage size = "+allMessages1.size());
////            for (int i = 0; i < allMessages1.size(); i++) {
////                EMMessage emMessage = allMessages1.get(i);
////                LogUtils.d(TAG,"getType =" +emMessage.getType());
////            }
////        }
//        LogUtils.d(TAG,"获取未读消息数量 " + conversation.getUnreadMsgCount());
        LogUtils.d(TAG, "getAllConversations");
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                LogUtils.e(TAG, "获取环信会话消息size = " + conversations.size() + "~~" + Thread.currentThread().getName());
                if (conversations.size() == 0) {
                    return;
                }

                Collection<EMConversation> values = conversations.values();
                for (EMConversation list : values) {
                    List<EMMessage> allMessages = list.getAllMessages();
                    for (final EMMessage message : allMessages) {
                        EMMessage.Type type = message.getType();
                        //判断是否是语音消息未显示
                        if (EMMessage.Type.VOICE.equals(type)) {
                            LogUtils.d(TAG, "判断是否是语音消息未显示");
//                            EMClient.getInstance().chatManager().deleteConversation(message.getFrom(), true);
//                          setMessageVisible();
                        }


                        if (EMMessage.Type.TXT.equals(type)) {
                            int msgType = message.getIntAttribute("msgType", -1);
                            final int msgAction = message.getIntAttribute("msgAction", -1);
                            LogUtils.d(TAG, "msgAction " + msgAction);
                            //判断sos 有没有新消息
                            if (msgType == 2) {
                                if (msgAction == 1) {
                                    //sos 消息
                                    try {
                                        JSONObject parameters = message.getJSONObjectAttribute("parameters");
                                        mDLA.toActivity(SOSMapActivity.class, String.valueOf(parameters));
                                        EMClient.getInstance().chatManager().deleteConversation(message.getFrom(), true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return;
                                }
                            }

                            if (msgType == 3) {
                                if (msgAction == 1) {
                                    try {
                                        JSONObject parameters = message.getJSONObjectAttribute("parameters");
                                        MyApplication.sUserAcountId = parameters.getString("acountId");
                                        MyApplication.sUserdeviceId = parameters.getString("deviceId");
                                        LogUtils.d(TAG, "parameters " + parameters);
                                        LogUtils.d(TAG, "acountId " + MyApplication.sUserAcountId);
                                        LogUtils.d(TAG, "MyApplication.sUserdeviceId " + MyApplication.sUserdeviceId);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDLA.showContactDialog(message.getBody() + "", 0,mContext);
                                            EMClient.getInstance().chatManager().deleteConversation(message.getFrom(), true);
                                        }
                                    });
                                    return;
                                } else if (msgAction == 11) {
                                    try {
                                        JSONObject parameters = message.getJSONObjectAttribute("parameters");
                                        mDeviceId = parameters.getString("deviceId");
                                        mImei = parameters.getString("imei");
                                        mNickName = parameters.getString("nickName");
                                        mImgUrl = parameters.getString("imgUrl");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    ThreadUtils.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mDLA.showContactDialog(message.getBody() + "", 1,mContext);
                                            EMClient.getInstance().chatManager().deleteConversation(message.getFrom(), true);
                                        }
                                    });
                                    return;
                                } else {
                                    EMClient.getInstance().chatManager().deleteConversation(message.getFrom(), true);
                                }

                            }
                        }
                    }
                }
            }
        }, 3 * 1000);

    }


    /**
     * 设置语音新消息显示
     */
    private void setMessageVisible() {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mDLA.sMessageSize++;
                if (mDLA.sMessageSize > 0) {
                    mDLA.mTvNewMessage.setVisibility(View.VISIBLE);
                    LogUtils.e(TAG, "!!!收到语音消息");
                }
            }
        });
    }


    EMCallBack mEMCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            mDLA.mChekEMCLogin = true;
            // 加载所有会话到内存
            EMClient.getInstance().chatManager().loadAllConversations();
            // 加载所有群组到内存，如果使用了群组的话
            EMClient.getInstance().groupManager().loadAllGroups();
            mIsEMCLogin = true;
            chekCache();
            Log.d(TAG, "run: " + "环信已经登入了" + Thread.currentThread().getName());
            if (!mIsQueryEMC) {
                getAllConversations();
            }
        }

        @Override
        public void onError(int i, String s) {
            mIsEMCLogin = false;
        }

        @Override
        public void onProgress(int i, String s) {

        }
    };


    /**
     * 判断手表电量
     *
     * @param powerLevel
     */
    public void chekPower(int powerLevel) {
        switch (powerLevel) {
            case -1:
                mDLA.mIvPower.setImageResource(R.drawable.power_unknown);
                break;

            case 0:
                mDLA.mIvPower.setImageResource(R.drawable.power_zero);
                break;

            case 1:
                mDLA.mIvPower.setImageResource(R.drawable.power_one);
                break;

            case 2:
                mDLA.mIvPower.setImageResource(R.drawable.power_two);
                break;

            case 3:
                mDLA.mIvPower.setImageResource(R.drawable.power_three);
                break;

            case 4:
                mDLA.mIvPower.setImageResource(R.drawable.power_four);
                break;
        }
    }


    /**
     * 判断透传消息的类型
     *
     * @param msgType   消息主类型 1-电子围栏消息 2-SOS紧急求助消息 3-联系人消息 4-定位信息消息 5-系统消息
     * @param msgAction 1、申请绑定成功后，通知设备管理员
     *                  2、管理员审核拒绝|同意，通知申请的APP账户
     *                  3、管理员添加联系人，通知手表刷新联系人,通知被添加的联系人
     *                  4、管理员删除联系人，通知手表刷新信息，通知被删除的联系人
     *                  5、管理员编辑联系人，通知手表刷新信息，通知被删除的联系人
     *                  6、联系人主动解除绑定，通知手表刷新信息，通知设备管理员
     *                  7、管理员解除绑定所有已绑定设备的联系人，通知手表刷新信息，通知被解除绑定的所有联系人
     *                  8、管理员移交权限并解除绑定，通知手表刷新信息，通知新管理员
     */
    public void chekMsgType(int msgType, String body, int msgAction, JSONObject parameters) {
        switch (msgType) {
            case 1:
                //电子围栏消息
                break;

            case 2:
                //SOS紧急求助消息
                break;

            case 3:
                //联系人消息
                LogUtils.e(TAG, "msgAction " + msgAction);
                chekMsgAction(msgAction, body, parameters);
                break;

            case 4:
                //定位信息消息
                break;

            case 5:
                //系统消息
                break;


        }
    }


    //联系人消息
    private void chekMsgAction(int msgAction, String body, JSONObject parameters) {
        try {
            LogUtils.e(TAG,"body " + body);
            mBodys = body;
            String[] split = mBodys.split(":");
            if (split.length > 1) {
                mBodys = split[1];
            }
            Log.e(TAG, split.length + " split.length");

            switch (msgAction) {
                case 1:
                    try {
                        MyApplication.sUserAcountId = parameters.getString("acountId");
                        MyApplication.sUserdeviceId = parameters.getString("deviceId");
                        LogUtils.d(TAG, "acountId " + MyApplication.sUserAcountId);
                        LogUtils.d(TAG, "MyApplication.sUserdeviceId " + MyApplication.sUserdeviceId);
                    } catch (Exception e) {
                        LogUtils.e(TAG, "解析txt 异常");
                    }
                    //申请绑定成功后，通知设备管理员
                    mDLA.showContactDialog(body + "", 0,mContext);
                    break;

                case 2:
                    //管理员审核拒绝|同意，通知申请的APP账户
                    mIsNotice = true;
                    getAcountDeivceList(MyApplication.sToken, MyApplication.sAcountId);
                    LogUtils.e(TAG, "管理员审核拒绝|同意，通知申请的APP账户");
                    break;

                case 3:
                    //管理员添加联系人，通知手表刷新联系人,通知被添加的联系人
                    break;

                case 4:
                    //管理员删除联系人，通知手表刷新信息，通知被删除的联系人
                    try {
                        MyApplication.sUserAcountId = parameters.getString("acountId");
                        MyApplication.sUserdeviceId = parameters.getString("deviceId");
                        String isBand = parameters.getString("isBand");
                        LogUtils.d(TAG, "acountId " + MyApplication.sUserAcountId);
                        LogUtils.d(TAG, "MyApplication.sUserdeviceId " + MyApplication.sUserdeviceId);
                        if (isBand.equals("0")) {
                            return;
                        }

                    } catch (Exception e) {
                        LogUtils.d(TAG, "解析txt 异常");
                    }
//                    mIsNotice = true;
//                    misAdmin = true;
//                    getAcountDeivceList(MyApplication.sToken,MyApplication.sAcountId);
                    receivesSolutionMessage("", MyApplication.sUserdeviceId, mBodys);
                    LogUtils.d(TAG, "管理员删除联系人");

                    break;

                case 5:
                    //管理员编辑联系人，通知手表刷新信息，通知被删除的联系人
                    break;

                case 6:
                    //联系人主动解除绑定，通知手表刷新信息，通知设备管理员
                    break;

                case 7:
                    try {
                        MyApplication.sUserdeviceId = parameters.getString("deviceId");
                    } catch (Exception e) {
                        LogUtils.d(TAG, "解析txt 异常");
                    }

                    LogUtils.e(TAG,"mBodys " + mBodys);
                    //管理员解除绑定所有已绑定设备的联系人，通知手表刷新信息，通知被解除绑定的所有联系人
                    receivesSolutionMessage("", MyApplication.sUserdeviceId, mBodys);
                    break;

                case 8:
                    //管理员移交权限并解除绑定，通知手表刷新信息，通知新管理员
                    getAcountDeivceList(mDLA.mToken, mDLA.mAcountId);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(mBodys);

                    builder.setPositiveButton(mContext.getString(R.string.know_the), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    break;

                case 11:
                    //邀请好友绑定该设备消息
                    try {
                        mDeviceId = parameters.getString("deviceId");
                        mImei = parameters.getString("imei");
                        mNickName = parameters.getString("nickName");
//                        mImgUrl = parameters.getString("imgUrl");
                        mDLA.showContactDialog(mNickName + "邀请你绑定手表", 1,mContext);
                    } catch (Exception e) {
                        LogUtils.d(TAG, "解析txt 异常");
                    }

                    break;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }


    }


    public int chekType(int type) {
        LogUtils.d(TAG, "定位类型type  " + type);
        switch (type) {
            case 0:
                //GPS定位
                return R.mipmap.gps;

            case 1:
                //基站定位
                return R.mipmap.lbs;

            case 2:
                //wifi定位
                return R.mipmap.wifi;

            case 3:
                //混合定位
                return R.mipmap.location_address;

            default:
                return R.mipmap.gps;
        }
    }


    /**
     * 收到一条普通消息
     *
     * @param messages
     */
    public void receivedCommonMessage(final List<EMMessage> messages, MQTTModel mqttModel) {
        if (messages != null){
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (messages.size() > 0){
                        EMMmessages(messages,messages.get(0).getBody()+"");

                    }
                }
            });

        }else if (mqttModel != null && mqttModel.getParameters() != null){
            mqttMessage(mqttModel);
        }else {
            EMLog.e(TAG,"收到MQTT消息异常");
        }

    }


    /**
     * MQTT收到的消息
     */
    private void mqttMessage(MQTTModel mqttModel) {
        MQTTModel.ParametersBean parameters = mqttModel.getParameters();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imgUrl",parameters.getImgUrl());
            jsonObject.put("deviceId",parameters.getDeviceId());
            jsonObject.put("imgUrl",parameters.getImgUrl());
            jsonObject.put("nickName",parameters.getNickName());
            jsonObject.put("imei",parameters.getImei());
            jsonObject.put("acountId",parameters.getAcountId());
            jsonObject.put("address",parameters.getAddress());
            jsonObject.put("isBand",parameters.getIsBand());
            jsonObject.put("lat",parameters.getLat());
            jsonObject.put("lng",parameters.getLng());
            jsonObject.put("locationTime",parameters.getLocationTime());
            jsonObject.put("replayStatus",parameters.getReplayStatus());
            jsonObject.put("ssid",parameters.getSsid());
            jsonObject.put("success",parameters.getSuccess());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        txtMessage(mqttModel.getMsgId(),mqttModel.getMsgType(),mqttModel.getMsgAction()
                ,mqttModel.getMsg(),mqttModel.getFrom(),jsonObject);

        mNotifier.onNewMsg(mqttModel, false);
        LogUtils.e(TAG,"MQTT收到的消息" +"getMsgType = "+mqttModel.getMsgType()+"getMsgAction = "+mqttModel.getMsgAction());
    }


    /**
     * 环信透传来的消息
     */
    private void EMMmessages(List<EMMessage> messages,String bodys) {
        EMLog.e(TAG2,"环信收到的消息");

        //用来判断当前刚开始登入过滤消息
        if (mDLA.mEMCLogin == 1) {
            if (messages.size() > 0) {
                final EMMessage emMessage = messages.get(0);
                long msgTime = emMessage.getMsgTime();
                String msgId1 = emMessage.getMsgId();

                LogUtils.e(TAG, "msgId1 =" + msgId1);
                LogUtils.e(TAG, "msgTime =" + msgTime);
                LogUtils.d(TAG, "emMessage.getFrom " + emMessage.getFrom());
                LogUtils.d(TAG, "emMessage.getUserName " + emMessage.getUserName());
                LogUtils.d(TAG, "mDeviceListBean.getNickName() " + mDeviceListBean.getNickName() +
                        MyApplication.sAcountId);


                if (mDeviceListBean.getNickName() != null) {
                    mDLA.mName = mDLA.loadDataFromLocal(emMessage.getFrom() +
                            MyApplication.sAcountId + "", Constant.PROTOCOL_TIMEOUT_MONTH);
                }
                LogUtils.e(TAG, "name " + mDLA.mName);
                EMMessage.Type type = emMessage.getType();
                LogUtils.e(TAG, "收到一条消息getType " + type);
                LogUtils.e(TAG, "getTo " + emMessage.getTo());
                String from = emMessage.getFrom();
                LogUtils.e(TAG, "getFrom " + from);
                LogUtils.e(TAG, "getBody " + bodys);

                if (EMMessage.Type.TXT.equals(type)) {
                    //文本消息
                    if (mDLA.mName != null) {
                        emMessage.setFrom(mDLA.mName);
                    }
                    long msgId = emMessage.getLongAttribute("msgId", -1);
                    final int msgType = emMessage.getIntAttribute("msgType", -1);
                    final int msgAction = emMessage.getIntAttribute("msgAction", -1);
                    try {
                        mParameters = emMessage.getJSONObjectAttribute("parameters");
                        LogUtils.d(TAG, "parameters " + mParameters);

                    } catch (Exception e) {
                        EMLog.e(TAG, "收到普通消息异常" + e.getMessage());
                        e.printStackTrace();
                    }
                    LogUtils.e(TAG, "msgId " + msgId);
                    LogUtils.e(TAG, "msgType " + msgType);
                    if (msgType == -1) {
                        return;
                    }
                    EMClient.getInstance().chatManager().deleteConversation(emMessage.getFrom(), true);
                    txtMessage(msgId,msgType,msgAction,bodys,from ,mParameters);

                    if (msgAction == 3) {
                        mDLA.getNotifier().onNewMsg(emMessage,bodys, true);
                    } else {
                        mDLA.getNotifier().onNewMsg(emMessage,bodys);
                    }


                } else if (EMMessage.Type.VOICE.equals(type)) {
                    //设置现实新消息通知
                    setMessageVisible();
                    //设置本地消息推送通知
                    if (!mDLA.mEaseUI.hasForegroundActivies()) {
                        mDLA.getNotifier().onNewMsg(emMessage,true);
                    }
                }
            }

        }else {
            if (messages.size() > 0){
                EMMessage emMessage = messages.get(0);
                if (EMMessage.Type.VOICE.equals(emMessage.getType())) {
                    //设置现实新消息通知
                    setMessageVisible();
                    //设置本地消息推送通知
                    if (!mDLA.mEaseUI.hasForegroundActivies()) {
                        mDLA.getNotifier().onNewMsg(emMessage,true);
                    }
                    return;
                }
            }

        }
    }

    private void txtMessage(long msgId, int msgType, int msgAction, String bodys, String from, JSONObject parameters) {
        mDLA.mivUnread.setVisibility(View.VISIBLE);
        chekMsgType(msgType, bodys, msgAction, parameters);
    }


    public void playHeartbeatAnimation(final Marker marker) {
        final AnimationSet swellAnimationSet = new AnimationSet(true);
        swellAnimationSet.addAnimation(new ScaleAnimation(0.8f, 1.4f, 0.8f, 1.4f));
        swellAnimationSet.setDuration(300);
        swellAnimationSet.setInterpolator(new AccelerateInterpolator());
        swellAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                AnimationSet shrinkAnimationSet = new AnimationSet(true);
                shrinkAnimationSet.addAnimation(new ScaleAnimation(1.4f, 0.8f, 1.4f, 0.8f));
                shrinkAnimationSet.setDuration(700);
                shrinkAnimationSet.setInterpolator(new DecelerateInterpolator());
                marker.setAnimation(shrinkAnimationSet);
                marker.startAnimation();
            }
        });

        marker.setAnimation(swellAnimationSet);
        marker.startAnimation();

    }


    /**
     * 弹出没网络连接框
     */
    public void showNoNetworkDialog() {
        if (mDeviceListBean == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.current_network_connection_fails_make_sure_to_connect));

            builder.setPositiveButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getAcountDeivceList(mDLA.mToken, mDLA.mAcountId);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }


    /**
     * 清除Marker
     */
    public void moveMarker() {
        mDLA.mTvLotionTime.setVisibility(View.GONE);
        mDLA.mTvAccuracy.setVisibility(View.GONE);
        mDLA.mTvType.setVisibility(View.GONE);
        mDLA.mLocationTv.setText(mContext.getString(R.string.no_location_information));
        if (mDLA.sBitmap != null) {
            mDLA.sBitmap.recycle();
            mDLA.sBitmap = null;
        }
        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
            if (mMarker != null) {
                mMarker.destroy();
            }
            mDLA.mAMap.clear();
        }

        getAcountDeivceList(MyApplication.sToken, MyApplication.sAcountId);
    }


    private int mBgColor = 0xb2000000;


    /**
     * show引导页
     */
    public void showGuide() {
        final Window window = mDLA.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setNavigationBarColor(mBgColor);
        }
        NewbieGuideManager guideManager = new NewbieGuideManager(mDLA,
                NewbieGuideManager.TYPE_First);
        guideManager.addView(mDLA.mIvIcon, HoleBean.TYPE_CIRCLE);
        guideManager.addView(mDLA.mLlMapType_light, HoleBean.TYPE_CIRCLE);
        guideManager.showWithListener(200, new NewbieGuide.OnGuideChangedListener() {
            @Override
            public void onShowed() {
                LogUtils.e(TAG, "我在外面show了");
                mGlobalvariable.edit().putBoolean("firstLogin", false).apply();
            }

            @Override
            public void onRemoved() {
                LogUtils.e(TAG, "我在外面removed了");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setNavigationBarColor(Color.TRANSPARENT);
                }
                NewbieGuideManager secondPage = new NewbieGuideManager(mDLA,
                        NewbieGuideManager.TYPE_TWICE);
                secondPage.addView(mDLA.mLlHistory_light, HoleBean.TYPE_CIRCLE);
                secondPage.addView(mDLA.mLlNavigation, HoleBean.TYPE_CIRCLE);
                secondPage.showWithListener(0, new NewbieGuide.OnGuideChangedListener() {
                    @Override
                    public void onShowed() {

                    }

                    @Override
                    public void onRemoved() {
                        NewbieGuideManager thirdPage = new NewbieGuideManager(mDLA,
                                NewbieGuideManager.TYPE_THIRD);
                        thirdPage.addView(mDLA.mLlelectronic_fence_light, HoleBean.TYPE_CIRCLE);
                        thirdPage.addView(mDLA.mLlSendLocation, HoleBean.TYPE_CIRCLE);
                        thirdPage.showWithListener(0, new NewbieGuide.OnGuideChangedListener() {
                            @Override
                            public void onShowed() {

                            }

                            @Override
                            public void onRemoved() {
                                showActionBar();
                            }
                        });
                    }
                });
            }
        });
    }


    public void showActionBar() {
        final Window window = mDLA.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                    window.setNavigationBarColor(Color.BLACK);
                }
            }, 600);

        }
    }


    /**
     * 查询通知的状态
     *
     * @param token
     * @param acountId
     */
    public void getTypesAndLastMessage(String token, long acountId) {
        LogUtils.e(TAG, "查询通知的状态");
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);

        Call<ResponseInfoModel> typesAndLastMessage = mWatchService.getTypesAndLastMessage(hashMap);
        typesAndLastMessage.enqueue(mCallback6);
    }

    @Override
    protected void messageSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, "messageSuccess " + body.getResultMsg());
        List<ResponseInfoModel.ResultBean.MessageListBean> messageList = body.getResult().getMessageList();

        for (ResponseInfoModel.ResultBean.MessageListBean list : messageList) {
            if (list.getLastMessageStatus() == 0 && list.getLastMessage() != null) {
                mDLA.mivUnread.setVisibility(View.VISIBLE);
                return;
            }
        }
    }


    /**
     * 被邀请人，回复接收/拒绝
     *
     * @param token
     */
    public void replyBandDeviceRequest(String token, String reply, String acountName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("deviceId", mDeviceId);
        hashMap.put("acountName", acountName);
        hashMap.put("reply", reply);

        LogUtils.d(TAG, "被邀请人，回复接收/拒绝 " + String.valueOf(hashMap));
        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.replyBandDeviceRequest(hashMap);
        responseInfoModelCall.enqueue(mCallback4);
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
        mDLA.mIsToken = false;
        ResponseInfoModel.ResultBean result = body.getResult();
        boolean verification = result.isVerification();
        if (!verification){
            exit();
        }else {
//            mDLA.mqttConnect();
        }
    }


    /**
     * 设置通知栏
     */
    public void setNotifier(Notifier notifier) {
        notifier.setEaseNotificationInfo(new Notifier.EaseNotificationInfo() {
            @Override
            public Intent getLaunchIntent(MQTTModel message) {
                LogUtils.e(TAG, "Intent");
                final int msgType = message.getMsgType();
                final int msgAction = message.getMsgAction();
                LogUtils.e(TAG, "msgType " + msgType);
                LogUtils.e(TAG, "msgAction " + msgAction);
                    //普通文本消息
                    if (msgType == 2) {
                        Intent intent = new Intent(MyApplication.sInstance, SOSMapActivity.class);
                        MQTTModel.ParametersBean parameters1 = message.getParameters();
                        if (mGson == null){
                            mGson = new Gson();
                        }
                        String parameters = mGson.toJson(parameters1);
                        LogUtils.e(TAG, "parameters " + parameters);
                        intent.putExtra("String", parameters);
                        return intent;

                    }
                Activity activity = AppManagerUtils.getAppManager().currentActivity();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName(MyApplication.sInstance, activity.getClass()));//用ComponentName得到class对象
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
                return intent;
            }
        });
    }
}


