package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.EventPlayStartModel;
import com.loybin.baidumap.model.ParametersModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.model.WifiModel;
import com.loybin.baidumap.presenter.WiFiSettingsPresenter;
import com.loybin.baidumap.ui.adapter.WifiAdapter;
import com.loybin.baidumap.ui.view.LinearTvView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.widget.DataActionListener;
import com.loybin.baidumap.widget.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/21 下午3:39
 * 描   述: wifi设置 view
 */
public class WifiSettingsActivity extends BaseActivity implements OnItemClickListener, DataActionListener {


    private static final String TAG = "WifiSettingsActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_music)
    ImageView mIvMusic;

    @BindView(R.id.lt_stranger_calls)
    LinearTvView mLtStrangerCalls;

    @BindView(R.id.dataView)
    RecyclerView mDataView;

    @BindView(R.id.dataEmpty)
    TextView mDataEmpty;

    @BindView(R.id.id_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rl_relative)
    RelativeLayout mRelativeLayout;

    @BindView(R.id.ll_sele_wifi)
    LinearLayout mLlSeleWifi;

    @BindView(R.id.ll_wifi_success)
    LinearLayout mLlWiFiSuccess;
    private List<WifiModel> mDataList;//wifi列表显示
    private WifiAdapter mDataAdapter;
    private String mSsid;
    private WiFiSettingsPresenter mWiFiSettingPresenter;
    private String mPassword;
    private int mCommand = 10026;
    private String mMessage = "设备上报wifi信息指令";
    private String mAcountName;
    private Gson mGson;
    private ParametersModel.ParametersBean mParameters;
    private List<ParametersModel.WifiDataListBean> mWifiDataList;
    private int mState;
    private ParametersModel.WifiDataListBean mData;
    private ResponseInfoModel.ResultBean mResult;
    private String mConnectedSsid;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wifi_setting;
    }


    @Override
    protected void init() {
        mWiFiSettingPresenter = new WiFiSettingsPresenter(this, this);
        mWifiDataList = new ArrayList<>();
        mDataList = new ArrayList<>();
        mAcountName = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");
        EventBus.getDefault().register(this);
        initView();
        initListener();
        initData();
    }


    private void initData() {
        mWiFiSettingPresenter.queryDeviceWifiByDeviceId(MyApplication.sToken
                , MyApplication.sDeviceId, MyApplication.sAcountId);

    }


    private void initListener() {
        mDataAdapter.setOnItemClickListener(this);
        mDataAdapter.setDataActionListener(this);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.wifi_setting));
        mTvRight.setVisibility(View.GONE);
        mIvMusic.setVisibility(View.VISIBLE);
        mIvMusic.setImageResource(R.mipmap.rotating);
        mLtStrangerCalls.mIvSwitch.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress_small));

        mDataAdapter = new WifiAdapter(this, mWifiDataList);
        mDataView.setHasFixedSize(true);
        mDataView.setLayoutManager(new LinearLayoutManager(this));
        mDataView.setAdapter(mDataAdapter);

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 连接Wifi
     */
    private void connectWifi(final String ssid, final String wifiType) {
        LogUtils.e(TAG, "ssid " + ssid);
        int networkId = -1;
        //不需要密码

        LogUtils.d(TAG, "networkId " + networkId);
        if (wifiType.equals("off")) {
            mPassword = "";
            mWiFiSettingPresenter.insertOrUpdateDeviceWifi(MyApplication.sToken,
                    MyApplication.sDeviceId, ssid, mPassword, 1, MyApplication.sAcountId);
            return;
        }

        Intent intent = new Intent(this, WiFiPasswordActivity.class);
        intent.putExtra("ssid", ssid);
        intent.putExtra("networkId", networkId);
        intent.putExtra("wifiType", wifiType);
        startActivityForResult(intent, 100);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            mPassword = data.getStringExtra("password");
            mSsid = data.getStringExtra("ssid");
            mState = 1;
            mLtStrangerCalls.mIvSwitch.setImageResource(R.mipmap.on);
            mLtStrangerCalls.mIsToggle = true;
            LogUtils.e(TAG, "onActivityResult " + mPassword);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void dismissNewok() {
        if (mWiFiSettingPresenter.mCall != null) {
            mWiFiSettingPresenter.mCall.cancel();
        }

        if (mWiFiSettingPresenter.mCall1 != null) {
            mWiFiSettingPresenter.mCall1.cancel();
        }

        if (mWiFiSettingPresenter.mAppSendCMD != null) {
            mWiFiSettingPresenter.mAppSendCMD.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.lt_stranger_calls, R.id.iv_music})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                break;

            case R.id.lt_stranger_calls:
                if (mSsid == null) {
                    printn("请设置wifi在开启");
                    return;
                }

                mLtStrangerCalls.toggle();
                boolean toggle = mLtStrangerCalls.getToggle();
                if (toggle) {
                    mState = 1;
                } else {
                    mState = 0;
                }
                mWiFiSettingPresenter.insertOrUpdateDeviceWifi(MyApplication.sToken,
                        MyApplication.sDeviceId, mSsid, mPassword, mState, MyApplication.sAcountId);
//                if (!toggle){
//                    dataView.setVisibility(View.GONE);
//                }else {
//                    dataView.setVisibility(View.VISIBLE);
//                }
                LogUtils.d(TAG, "mLtStrangerCalls: " + toggle);
                break;

            case R.id.iv_music:
                mProgressBar.setVisibility(View.VISIBLE);
                mDataEmpty.setVisibility(View.VISIBLE);
                mLlSeleWifi.setVisibility(View.GONE);
                mDataEmpty.setText(getString(R.string.scan_loading));
                mDataView.setVisibility(View.GONE);
                mWiFiSettingPresenter.appSendCMD(mCommand, mMessage, mAcountName, MyApplication.sToken,
                        MyApplication.sDeviceListBean.getImei());
                break;

            default:
                break;
        }
    }


    public void onSuccess(ResponseInfoModel data) {
        mResult = data.getResult();
        mState = mResult.getState();
        mPassword = mResult.getPassword();
        mSsid = mResult.getSsid();
        if (mState == 0) {
            mLtStrangerCalls.mIvSwitch.setImageResource(R.mipmap.off);
            mLtStrangerCalls.mIsToggle = false;
        } else {
            mLtStrangerCalls.mIvSwitch.setImageResource(R.mipmap.on);
            mLtStrangerCalls.mIsToggle = true;
        }


        mWiFiSettingPresenter.appSendCMD(mCommand, mMessage, mAcountName, MyApplication.sToken,
                MyApplication.sDeviceListBean.getImei());
    }


    //MQTT消息通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mqttMessage (ParametersModel parametersModel) {
        LogUtils.e("MQTT","wifi + MQTT消息通知");
        if (parametersModel == null){
            return;
        }
        chekCommand(parametersModel.getCommand(),parametersModel);
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
        }

        @Override
        public void onCmdMessageReceived(final List<EMMessage> messages) {
            try {
                //收到透传消息
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        LogUtils.d(TAG, "收到透传消息: " + messages.size());
                        if (messages.size() > 0) {
                            for (int i = 0; i < messages.size(); i++) {
                                EMMessage cmdMessage = messages.get(i);
                                EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                                String action = body.action();
                                parseData(action);
                            }
                        }
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


    /**
     * @param result 接收的透传消息
     */
    private void parseData(String result) {
        try {
            if (mGson == null) {
                mGson = new Gson();
            }
            Log.i(TAG, "收到透传消息 :" + result + "~~ " + Thread.currentThread().getName());
            ParametersModel parametersModel = mGson.fromJson(result, ParametersModel.class);

            //判断透传指令
            chekCommand(parametersModel.getCommand(), parametersModel);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 判断透传指令
     *
     * @param command
     * @param parametersModel
     */
    private void chekCommand(int command, ParametersModel parametersModel) {
        LogUtils.d(TAG, "command " + command);
        switch (command) {
            case Constants.COMMAND10027:
                mParameters = parametersModel.getParameters();
                if (mParameters == null ){
                    LogUtils.e(TAG,"mParameters  == null" );
                    return;
                }
                if (MyApplication.sDeviceId != mParameters.getDeviceId()) {
                    LogUtils.e(TAG, "收到的透传消息,不是当前该设备的,return");
                    return;
                }

                LogUtils.d(TAG, "getConnectedSuccess " + mParameters.getConnectedSuccess());
                LogUtils.d(TAG, "getConnectedSsid " + mParameters.getConnectedSsid());

                if (parametersModel.getParameters().getWifiDataList() != null) {
                    List<ParametersModel.WifiDataListBean> wifiDataList = parametersModel.getParameters().getWifiDataList();
                    mWifiDataList.clear();
                    mWifiDataList.addAll(wifiDataList);
                    for (ParametersModel.WifiDataListBean list : wifiDataList) {
                        if (list.getSsid().equals(mParameters.getConnectedSsid())) {
                            list.setConnect(true);
                            mConnectedSsid = mParameters.getConnectedSsid();

                            LogUtils.d(TAG, "匹配 ssid" + mConnectedSsid);
                        }
                    }

                    LogUtils.d(TAG, "wifiDataList size = " + wifiDataList.size());
                    mDataView.setVisibility(View.VISIBLE);
                    mDataEmpty.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mRelativeLayout.setVisibility(View.GONE);
                    mDataAdapter.setData(wifiDataList);
                    mDataAdapter.notifyDataSetChanged();
                    mLlSeleWifi.setVisibility(View.VISIBLE);
                } else {
                    LogUtils.e(TAG, "wifiDataList == null");
                }
                break;

            default:
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        try {
            //取消事件注册
            EventBus.getDefault().unregister(this);
            super.onDestroy();
        }catch (Exception e){
            Log.d(TAG,"StoryActivity onDestroy 异常" +e.getMessage());
        }
    }


    @Override
    public void onItemClick(int position) {
        mData = mWifiDataList.get(position);
        if (mData.isConnect()) {
            connectWifi(mData.getSsid(), mData.getLocked());
        } else {
            connectWifi(mData.getSsid(), mData.getLocked());
        }
    }


    @Override
    public void onItemLongClick(int position) {

    }


    @Override
    public void onShow(int position) {
//        ParametersModel.WifiDataListBean data = mWifiDataList.get(position);
//        data.setShowDetail(!data.isShowDetail());
//        mDataAdapter.notifyDataSetChanged();
    }


    /**
     * 透传发送成功
     */
    public void onAppSendSuccess() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mDataEmpty.setText(getString(R.string.scan_loading));
    }


    /**
     * 手表不在线
     */
    public void watchOnline(String string) {
        mProgressBar.setVisibility(View.GONE);
        mDataEmpty.setVisibility(View.VISIBLE);
        mDataEmpty.setText(string);
    }
}
