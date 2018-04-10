package com.loybin.baidumap.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.util.EMLog;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.MQTTModel;
import com.loybin.baidumap.model.ParametersModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.SendApplyPresenter;
import com.loybin.baidumap.ui.view.SelectDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SignUtil;
import com.loybin.baidumap.util.ThreadUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/15 上午10:04
 * 描   述: 发送申请view
 */
public class SendApplyActivity extends BaseActivity {


    private static final String TAG = "SendApplyActivity";
    private static final String TAG2 = "MQTT";
    private static final int MQTT_CODE = 10;
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_select_call)
    LinearLayout mEtSelectCall;

    @BindView(R.id.btn_send)
    Button mBtnSend;

    @BindView(R.id.iv_ok_nike)
    ImageView mIvOkNike;

    @BindView(R.id.tv_request)
    TextView mTvRequest;

    @BindView(R.id.btn_resend)
    Button mBtnResend;

    @BindView(R.id.tv_binding)
    TextView mTvBinding;

    @BindView(R.id.tv_relation)
    TextView mTvRelation;

    private String mImei;
    private SendApplyPresenter mSendApplyPresenter;
    private String mRelation;
    private SharedPreferences mGlobalvariable;
    public String mPhone;
    public String mMd5Password;
    private String mNewBaby;
    private MqttConnectOptions mMqttConnectOptions;
    public boolean mIsToken;
    private String mBodyss;
    private Gson mGson;
    private MqttAndroidClient mMqttAndroidClient;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_send_apply;
    }


    @Override
    protected void init() throws Exception {
        mSendApplyPresenter = new SendApplyPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mPhone = mGlobalvariable.getString("appAccount", "");
        mMd5Password = mGlobalvariable.getString("md5Password", "");
        mImei = getIntent().getStringExtra(STRING);
        mNewBaby = getIntent().getStringExtra(BABY);
        LogUtils.e(TAG, "mImei " + mImei);
        initView();
        initListener();
        mSendApplyPresenter.chekEMCLogin();

        initMQTT();
        mqttConnect();
    }


    private void initListener() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.send_apply));
    }


    @Override
    protected void dismissNewok() {
        if (mSendApplyPresenter.mCall != null) {
            mSendApplyPresenter.mCall.cancel();
        }
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back, R.id.et_select_call, R.id.btn_send, R.id.btn_resend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.et_select_call:
                toActivity(100, SelectRelationActivity.class);
                break;

            case R.id.btn_send:
                if (mImei == null) {
                    return;
                }
                mSendApplyPresenter.sendApply(MyApplication.sToken, MyApplication.sAcountId, mImei, mRelation);
                break;

            case R.id.btn_resend:
                mTvRequest.setText(getString(R.string.the_request_has_been_sent));
                mIvOkNike.setImageResource(R.mipmap.ok_nice);
                mSendApplyPresenter.sendApply(MyApplication.sToken, MyApplication.sAcountId, mImei, mRelation);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RELATION) {
            if (data != null) {
                mRelation = data.getStringExtra("relation");
                mTvRelation.setText(mRelation);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 称呼为空
     */
    public void relationEmpty(String string) {
        printn(string);
    }


    /**
     * 发送成功
     */
    public void sendApplySuccess() {
        dismissLoading();
        mTvBinding.setVisibility(View.GONE);
        mEtSelectCall.setVisibility(View.GONE);
        mBtnSend.setVisibility(View.GONE);
        mIvOkNike.setVisibility(View.VISIBLE);
        mTvRequest.setVisibility(View.VISIBLE);
        mBtnResend.setVisibility(View.VISIBLE);
        mIvOkNike.setImageResource(R.mipmap.ok_nice);
        mTvRequest.setText(getString(R.string.the_request_has_been_sent));
    }


    /**
     * 环信透传消息回调接口
     */
    public EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            if (messages == null) {
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
            onMessage(messages,null);
                }
            });
        }

        @Override
        public void onCmdMessageReceived(final List<EMMessage> messages) {
            try {
                //收到透传消息
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        Log.d(TAG, "收到透传消息: " + messages.size());

                    }
                });
            } catch (Exception e) {
                LogUtils.e(TAG, "透传异常" + e.getMessage());
            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    private void onMessage(List<EMMessage> messages, MQTTModel mqttMessage) {
        if (messages != null){
            EMLog.d(TAG, "收到 环信 一条消息");
            if (messages.size() > 0) {
                final EMMessage emMessage = messages.get(0);
                LogUtils.d(TAG, "emMessage.getFrom " + emMessage.getFrom());
                LogUtils.d(TAG, "emMessage.getUserName " + emMessage.getUserName());

                EMMessage.Type type = emMessage.getType();
                EMMessageBody body = emMessage.getBody();
                String bodys = body + "";
                final String[] split = bodys.split(":");
                if (split.length > 1) {
                    mBodyss = split[1];
                }
                LogUtils.e(TAG, "收到一条消息getType " + type);
                LogUtils.e(TAG, "收到一条消息getTo " + emMessage.getTo());
                LogUtils.e(TAG, "收到一条消息getBody " + body);
                if (EMMessage.Type.TXT.equals(type)) {
                    //文本消息
                    long msgId = emMessage.getLongAttribute("msgId", -1);
                    final int msgType = emMessage.getIntAttribute("msgType", -1);
                    try {
                        JSONObject parameters = emMessage.getJSONObjectAttribute("parameters");
                        MyApplication.sUserAcountId = parameters.getString("acountId");
                        final String replayStatus = parameters.getString("replayStatus");

                        chekReplayStatus(replayStatus,mBodyss);

                        LogUtils.d(TAG, "parameters " + parameters);
                        LogUtils.d(TAG, "replayStatus " + replayStatus);
                        LogUtils.d(TAG, "acountId " + MyApplication.sUserAcountId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtils.e(TAG, "msgId " + msgId);
                    LogUtils.e(TAG, "msgType " + msgType);
                }


            }
        }else if (mqttMessage != null){
            EMLog.e(TAG,"MQTT 收到TXT消息");
            MyApplication.sUserAcountId = mqttMessage.getParameters().getAcountId();
            String msg = mqttMessage.getMsg();
            MQTTModel.ParametersBean parameters = mqttMessage.getParameters();
            String replayStatus = parameters.getReplayStatus();
            chekReplayStatus(replayStatus,msg);
        }else {
            EMLog.e(TAG,"服务器的数据异常");
        }


    }

    private void chekReplayStatus(String replayStatus,String body) {
        if (replayStatus == null) {
            return;
        }
        if (replayStatus.equals("0")) {
            //拒绝
            mIvOkNike.setImageResource(R.mipmap.refuse_icon);
            mTvRequest.setText(getString(R.string.fefuse));
            if (body != null){
                printn(body);
            }

        } else if (replayStatus.equals("1")) {
            //同意
            if (body != null){
                printn(body);
            }

            if (mNewBaby == null) {
                Intent intent = new Intent(SendApplyActivity.this, DevicesHomeActivity.class);
                intent.putExtra(STRING, "");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishActivityByAnimation(SendApplyActivity.this);
            } else {
                //有设备,多次添加
                mSendApplyPresenter.getAcountDeivceList(MyApplication.sAcountId, MyApplication.sToken);
            }

        } else {
            //未知
            LogUtils.d(TAG, "未知~~");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        if (mMqttAndroidClient != null){
//            try {
////                mMqttAndroidClient.disconnect();
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
            mMqttAndroidClient.clearAbortBroadcast();
            mMqttAndroidClient.unregisterResources();
        }

    }


    /**
     * 弹出Dialog 选择设备
     *
     * @param deviceList
     */
    public void selectDialogShow(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList) {
        dismissLoading();
        SelectDialog selectDialog = new SelectDialog(this, this, deviceList);
        selectDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK) {
                    LogUtils.d(TAG, "~~返回");
                    return true;
                }
                return false;
            }
        });
        if (!isFinishing()){
         selectDialog.show();
        }
    }


    private void initMQTT() throws Exception {
        mMqttAndroidClient = new MqttAndroidClient(this, Constants.serverUri,Constants.mqttclientId);
        mMqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.e(TAG2,"connectComplete " + reconnect + serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                if (cause == null){
                    return;
                }
                try {
                    String message = cause.toString();
                    LogUtils.e(TAG,"connectionLost " + message +  mMqttAndroidClient.isConnected());
                    if (message != null){
                        if (message.indexOf("32109") != -1){
                            if (!mIsToken){
                                if (!mMqttAndroidClient.isConnected()){
                                    mIsToken = true;
                                    mSendApplyPresenter.verificationToken(MyApplication.sToken);
                                }
                            }
                        }
                    }

                }catch (Exception e){

                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        boolean connected = mMqttAndroidClient.isConnected();
        LogUtils.e(TAG2,"connected "+connected);
        //设备认证
        Map<String, String> params = new HashMap<String, String>();
        params.put("productKey", Constants.productKey); //这个是对应用户在控制台注册的 设备productkey
        params.put("deviceName", Constants.deviceName); //这个是对应用户在控制台注册的 设备name
        params.put("clientId", Constants.deviceName);
        params.put("timestamp", Constants.t);
        //客户端ID格式:
        LogUtils.e(TAG2,String.valueOf(params));
        String mqttUsername = Constants.deviceName+"&" + Constants.productKey;//mqtt用户名格式
        String mqttPassword = SignUtil.sign(params, Constants.deviceSecret, "hmacsha1");//签名
        //连接mqtt的代码片段

//        SSLSocketFactory socketFactory = SignUtil.createSSLSocket();

        mMqttConnectOptions = new MqttConnectOptions();
//        mMqttConnectOptions.setSocketFactory(socketFactory);
        //设置是否自动重连
        mMqttConnectOptions.setAutomaticReconnect(true);
        //如果是true 那么清理所有离线消息，即qos1 或者 2的所有未接收内容
        mMqttConnectOptions.setCleanSession(false);
        mMqttConnectOptions.setServerURIs(new String[] { Constants.serverUri });
        mMqttConnectOptions.setUserName(mqttUsername);
        mMqttConnectOptions.setPassword(mqttPassword.toCharArray());
        mMqttConnectOptions.setKeepAliveInterval(65);//心跳时间 建议60s以上
    }


    public void mqttConnect() {
        try {
            mMqttAndroidClient.connect(mMqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mMqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    Log.d(TAG2, "onSuccess: ");
                    boolean connected = mMqttAndroidClient.isConnected();
                    LogUtils.e(TAG2,"connected "+connected);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    boolean connected = mMqttAndroidClient.isConnected();
                    LogUtils.e(TAG2,"connected  onFailure "+connected);
                    Log.e(TAG2,"Failed to connect to: " + Constants.serverUri +"~~"  +exception);
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    public void subscribeToTopic(){
        LogUtils.e(TAG2,"subscribeToTopic" + Constants.subTopic);
        try {
            mMqttAndroidClient.subscribe(Constants.subTopic, 1, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, final MqttMessage message) throws Exception {
                    if (mMqttAndroidClient != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String mqttMessages = new String(message.getPayload());
                                LogUtils.d(TAG, "收到 MQTT 一条消息  " + mqttMessages);
                                if (mGson == null){
                                    mGson = new Gson();
                                }

                                if (mqttMessages.indexOf("command") != -1){
                                    LogUtils.e(TAG2,"MQTT收到 command 消息");

                                }else {
                                    LogUtils.e(TAG2,"MQTT收到 TXT消息");
                                    MQTTModel mqttModel = mGson.fromJson(mqttMessages, MQTTModel.class);
                                    if (mqttModel != null && mqttModel.getParameters() != null){
                                    onMessage(null,mqttModel);
                                    }else {
                                        printn("MQTT收到 TXT消息 异常");
                                        EMLog.e(TAG,"MQTT收到 TXT消息 异常");
                                    }
                                }
                            }
                        });
                    }

                }
            });

        } catch (MqttException ex){
            LogUtils.e(TAG2,"Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

}
