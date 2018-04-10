package com.loybin.baidumap.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.activity.InvitationAgreedActivity;
import com.loybin.baidumap.ui.activity.LoginActivity;
import com.loybin.baidumap.ui.activity.ScanActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MD5Util;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UserUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 登录的业务逻辑,请求了三个接口,环信登入,hojy服务登入,查询设备列表
 */
public class LoginPresenter extends BasePresenter {

    private static final String TAG = "LoginPresenter";

    private LoginActivity mLoginActivity;
    private Context mContext;
    public ResponseInfoModel.ResultBean mResultBean;
    private  String mDeviceSecret;
    private String mProductKey;
    private ResponseInfoModel.ResultBean mResult;

    public void setGlobalvariable(SharedPreferences globalvariable) {
        mGlobalvariable = globalvariable;
    }

    private SharedPreferences mGlobalvariable;
    private String mPhone;
    private String mMD5Password;
    private String mPassword;
    public Call<ResponseInfoModel> mLogin;

    public LoginPresenter(Context context, LoginActivity loginActivity) {
        super(context);
        mContext = context;
        mLoginActivity = loginActivity;
        mGlobalvariable = mLoginActivity.getSharedPreferences("globalvariable", 0);
    }


    /**
     * 登陆
     *
     * @param phone
     * @param password
     */
    public void login(String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            mLoginActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(phone)) {
            mLoginActivity.phoneError();
            return;
        }

        if (!TextUtils.isEmpty(password)) {
            onStartLogin(phone, password);
        } else {
//            密码不能为空
            mLoginActivity.passwordIsEmpty();
        }

    }


    /**
     * 请求网络登入
     *
     * @param phone
     * @param password
     */
    public void onStartLogin(String phone, String password) {
        mMD5Password = MD5Util.getInstance().getMD5String(password);
        LogUtils.e(TAG, "MD5 :" + mMD5Password);
        mPassword = password;
        //环信登入
        EMCLogin(phone, mMD5Password, EMCazllzBack);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", phone);
        hashMap.put("password", mMD5Password);
        //登陆接口增加参数
//        communicationType    1-环信 2-MQTT
        hashMap.put("communicationType", "2");
        Log.d(TAG, "请求网络登入: " + String.valueOf(hashMap));
        mLogin = mWatchService.login(hashMap);
        mLoginActivity.showLoading(mContext.getString(R.string.Login_Loding),mContext);
        mLogin.enqueue(mCallback);
    }


    /**
     * 环信登陆调用接口
     *
     * @param phone
     * @param md5Password
     * @param emCazllzBack
     */
    private void EMCLogin(String phone, String md5Password, EMCallBack emCazllzBack) {
        mPhone = phone;
        mMD5Password = md5Password;
        EMClient.getInstance().login(phone, md5Password, emCazllzBack);
    }


    /**
     * 登入成功的回调
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        //请求成功在去获取设备列表,
        mResultBean = data.getResult();
        mGlobalvariable.edit().putString("md5Password", mMD5Password).apply();
        mGlobalvariable.edit().putString("appAccount", mPhone).apply();
        mGlobalvariable.edit().putString("password", mPassword).apply();

        mGlobalvariable.edit()
                .putLong("acountId", mResultBean.getAcountId())
                .putString("token", mResultBean.getToken())
                .putString("acountName", mResultBean.getAcountName())
                .putString("nickName", mResultBean.getAcountName())
                .putString("birthday", String.valueOf(mResultBean.getBirthday()))
                .putString("email", mResultBean.getEmail())
                .putString("address", mResultBean.getAddress())
                .apply();

        //存储token
        String token = data.getResult().getToken();
        MyApplication.sToken = token;
        MyApplication.sAcountId = data.getResult().getAcountId();
        LogUtils.d(TAG, "token: " + MyApplication.sToken);
        LogUtils.d(TAG, "acountId: " + MyApplication.sAcountId);

        mResult = data.getResult();
        queryIotDevice(token,mPhone);



    }


    /**
     * 设备IOT信息查询
     */
    private void queryIotDevice(String token,String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("imei", phone);
        Call<ResponseInfoModel> responseInfoModelCall = mWatchService.queryIotDevice(hashMap);
        responseInfoModelCall.enqueue(mCallback3);
    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        if (body == null || body.getResult() == null){
            return;
        }
        ResponseInfoModel.ResultBean result = body.getResult();
        String productKey = result.getProductKey();
        String deviceSecret = result.getDeviceSecret();

        if (deviceSecret != null && productKey != null){
            mGlobalvariable.edit().putString(mPhone+"deviceSecret",deviceSecret)
                    .putString("productKey",productKey).apply();
            Constants.productKey = productKey;
            Constants.deviceName = mPhone;
            Constants.deviceSecret = deviceSecret;
            Constants.subTopic =  "/"+productKey+"/"+mPhone+"/get"; //下行
            Constants.pubTopic = "/"+productKey+"/"+mPhone+"/pub";//上行
            Constants.serverUri = "ssl://"+productKey+".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
            Constants.mqttclientId = mPhone + "|securemode=2,signmethod=hmacsha1,timestamp="+Constants.t+"|";
        }

        //被邀请新注册跳转到同意界面
        if (mResult.getBandRequestList() != null && mResult.getBandRequestList().size() > 0) {
            LogUtils.e(TAG, "被邀请新注册跳转到同意界面 size = " + mResult.getBandRequestList().size());
            ResponseInfoModel.ResultBean.bandRequestListBean bandRequestListBean = mResult.
                    getBandRequestList().get(mResult.getBandRequestList().size() - 1);
            Intent intent = new Intent(mContext, InvitationAgreedActivity.class);
            intent.putExtra("deviceId", bandRequestListBean.getDeviceId() + "");
            intent.putExtra("imei", bandRequestListBean.getImei());
            intent.putExtra("nickName", bandRequestListBean.getNickName());
            intent.putExtra("imgUrl", bandRequestListBean.getImgUrl());
            mLoginActivity.dismissLoading();
            mLoginActivity.startActivityForResult(intent, 101);
            mLoginActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        } else {
            mLoginActivity.dismissLoading();
            mLoginActivity.getAcountDeivceList(mResultBean);
        }
        LogUtils.e(TAG,body.getResult().getDeviceSecret());
    }

    /**
     * 登陆失败80001回调
     *
     * @param data
     */
    @Override
    protected void onFaiure(ResponseInfoModel data) {
        Log.d(TAG, "onFaiure: " + data.getResultMsg());
        mLoginActivity.resultMsg(data.getResultMsg());
    }


    /**
     * 没连上服务器回调
     *
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mLoginActivity.dismissLoading();
        mLoginActivity.printn(mContext.getString(R.string.Network_Error));
    }


    /**
     * 获取绑定设备列表
     *
     * @param token
     * @param acountId
     */
    public void getAcountDeivceList(String token, long acountId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("acountId", acountId);

        Log.d(TAG, "getAcountDeivceList:  " + String.valueOf(hashMap));
        Call<ResponseInfoModel> login = mWatchService.getAcountDeivceList(hashMap);
        mLoginActivity.showLoading(mContext.getString(R.string.Login_Loding),mContext);
        login.enqueue(mCallback2);
    }


    /**
     * 绑定设备列表成功回调
     * 绑定设备列表,根据size判断是否有绑定设备
     * size 0  表示无设备绑定跳转扫描绑定
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = body.getResult().getDeviceList();
        mLoginActivity.dismissLoading();
        Log.d(TAG, "onSuccess: " + deviceList.size());

        if (deviceList.size() > 0) {
            try {
                //已经有设备跳转到首页
                //退出Home
                collections(deviceList);
                ResponseInfoModel.ResultBean.DeviceListBean deviceListBean = deviceList.get(MyApplication.sDeivceNumber);

                mLoginActivity.exitHomeActivity();
                if (deviceListBean != null && deviceListBean.getImgUrl() != null) {
                    mLoginActivity.toActivity(DevicesHomeActivity.class, deviceListBean.getImgUrl());
                } else {
                    mLoginActivity.toActivity(DevicesHomeActivity.class, "");
                }
                mLoginActivity.finishActivityByAnimation(mLoginActivity);
            } catch (Exception e) {
                LogUtils.e(TAG, "onSuccess 异常" + e.getMessage());
            }

        } else {
            //刚注册还没设备,跳转到扫描imei码界面
            //退出Home
            LogUtils.e(TAG, "刚注册还没设备,跳转到扫描imei码界面");
            startImei();


        }
    }


    /**
     * 跳转到扫描界面
     */
    public void startImei() {
        mLoginActivity.exitHomeActivity();
        Intent intent = new Intent(mContext, ScanActivity.class);
        intent.putExtra("isCamera", mLoginActivity.isCamera);
        mLoginActivity.startActivity(intent);
        mLoginActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    //排序
    private void collections(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList) {
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1,
                               ResponseInfoModel.ResultBean.DeviceListBean o2) {
                //降序排序
                return (o2.getIsAdmin() - o1.getIsAdmin());
            }
        });
    }


    /**
     * 绑定设备列表失败回调
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        Log.d(TAG, "onError: " + body.getResultMsg());
        mLoginActivity.Error(body.getResultMsg());
    }

    /**
     * 环信登入成功失败回调
     */
    EMCallBack EMCazllzBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            mLoginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 加载所有会话到内存
                    EMClient.getInstance().chatManager().loadAllConversations();
                    // 加载所有群组到内存，如果使用了群组的话
                    EMClient.getInstance().groupManager().loadAllGroups();

                    // 登录成功跳转界面
                    Log.d(TAG, "run: " + "环信已经登入了" + Thread.currentThread().getName());

                }
            });
        }

        @Override
        public void onError(final int i, final String s) {
            mLoginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginActivity.dismissLoading();
                    Log.d("LoginPresenter", "Error code:" + i + ", mMessage:" + s);
                    /**
                     * 关于错误码可以参考官方api详细说明
                     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                     */
                    switch (i) {
                        // 网络异常 2
                        case EMError.NETWORK_ERROR:
                            Log.d(TAG, "run: " + "网络异常 2");
                            break;
                        // 用户认证失败，用户名或密码错误 202
                        case EMError.USER_AUTHENTICATION_FAILED:
                            Log.d(TAG, "run: " + "用户认证失败，用户名或密码错误 202");
                            break;
                        // 用户不存在 204
                        case EMError.USER_NOT_FOUND:
                            Log.d(TAG, "run: " + "用户不存在 204");
                            break;
                        // 无法访问到服务器 300
                        case EMError.SERVER_NOT_REACHABLE:
                            Log.d(TAG, "run: " + "无法访问到服务器");
                            // 调用退出成功，结束app
                            EMCLogin(mPhone, mMD5Password, EMCazllzBack);
                            break;
                        // 等待服务器响应超时 301
                        case EMError.SERVER_TIMEOUT:
                            Log.d(TAG, "run: " + "等待服务器响应超时");
                            // 调用退出成功，结束app
                            EMCLogin(mPhone, mMD5Password, EMCazllzBack);
                            break;
                        // 服务器繁忙 302
                        case EMError.SERVER_BUSY:
                            Log.d(TAG, "run: " + "服务器繁忙");
                            // 调用退出成功，结束app
                            EMCLogin(mPhone, mMD5Password, EMCazllzBack);
                            break;
                        // 未知 Server 异常 303 一般断网会出现这个错误
                        case EMError.SERVER_UNKNOWN_ERROR:
                            Log.d(TAG, "run: " + "未知 Server 异常 303 一般断网会出现这个错误");
                            EMCLogin(mPhone, mMD5Password, EMCazllzBack);
                            break;

                        case EMError.USER_ALREADY_LOGIN:
                            Log.d(TAG, "200: " + "账号已登陆状态");
                            onExit();
                            break;
                        default:
                            //200 是已经登入 那么就让环信退出登录 在重新登录
                            LogUtils.e(TAG, "run " + "default");
                            onExit();
                            break;
                    }
                }
            });
        }


        @Override
        public void onProgress(int i, String s) {
            LogUtils.e(TAG, "环信登入中......");
        }
    };


    private void onExit() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("LoginPresenter", "logout success" + "退出了登录 在重新登入");
                // 调用退出成功，结束app
                EMCLogin(mPhone, mMD5Password, EMCazllzBack);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("LoginPresenter", "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
