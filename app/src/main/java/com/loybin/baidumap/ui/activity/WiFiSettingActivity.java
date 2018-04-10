package com.loybin.baidumap.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.model.WifiModel;
import com.loybin.baidumap.presenter.WiFiSettingPresenter;
import com.loybin.baidumap.ui.adapter.WifiAdapter;
import com.loybin.baidumap.ui.view.LinearTvView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.StringUtils;
import com.loybin.baidumap.widget.DataActionListener;
import com.loybin.baidumap.widget.OnItemClickListener;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/12 下午5:42
 * 描   述: wifi设置 view
 */
public class WiFiSettingActivity extends BaseActivity {


    private static final String TAG = "WiFiSettingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView layTitle;

    @BindView(R.id.dataView)
    RecyclerView dataView;

    @BindView(R.id.dataEmpty)
    TextView mDataEmpty;

    @BindView(R.id.lt_stranger_calls)
    LinearTvView mLtWifiSetting;

    private static final int REQUEST_CODE = 11000;//权限请求code

    private WifiReceiver wifiReceiver;//广播接受器

    public WifiManager wifiManager;//wifi管理器

    private WifiAdapter dataAdapter;//wifi列表适配器

    private List<WifiModel> dataList;//wifi列表显示

    private List<ScanResult> scanResultList;//扫描wifi列表

    private List<WifiConfiguration> wifiConfigList;//配置好的wifi列表

    private boolean isGranted;//是否有权限

    private WiFiSettingPresenter mWiFiSettingPresenter;
    private int mState;
    private String mPassword;
    private String mSsid;
    private NetworkInfo mInfo;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wifi_setting;
    }

    @Override
    protected void init() {
        mWiFiSettingPresenter = new WiFiSettingPresenter(this, this);
        initView();

        checkPermission();
    }


    @Override
    protected void onResume() {
        super.onResume();
        wifiReceiver = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//wifi的打开与关闭
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//wifi扫描
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);//wifi验证密码
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//wifi连接成功
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);//wifi信号强度
        registerReceiver(wifiReceiver, intentFilter);

        mWiFiSettingPresenter.queryDeviceWifiByDeviceId(MyApplication.sToken
                , MyApplication.sDeviceId, MyApplication.sAcountId);
    }

    @Override
    protected void onPause() {
        if (wifiReceiver != null) {
            unregisterReceiver(wifiReceiver);
        }
        super.onPause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        layTitle.setText("WiFi设置");
        mLtWifiSetting.mIvSwitch.setVisibility(View.VISIBLE);
        scanResultList = new ArrayList<>();
        wifiConfigList = new ArrayList<>();

//        dataAdapter = new WifiAdapter(this, dataList = new ArrayList<>());
        dataView.setHasFixedSize(true);
        dataView.setLayoutManager(new LinearLayoutManager(this));
        dataView.setAdapter(dataAdapter);

        dataAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WifiModel data = dataList.get(position);
                if (data.isConnect()) {

//                    getCurrentWifi();
                    connectWifi(data.getWifiName(), data.getWifiType());
                } else {
                    connectWifi(data.getWifiName(), data.getWifiType());
                }
            }

            @Override
            public void onItemLongClick(int position) {
            }
        });
        dataAdapter.setDataActionListener(new DataActionListener() {
            @Override
            public void onShow(int position) {
                WifiModel data = dataList.get(position);
                data.setShowDetail(!data.isShowDetail());
                dataAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 检查权限
     */
    private void checkPermission() {
        int perm = ContextCompat.checkSelfPermission(this, Constants.LOCATION_PERMISSION);
        if (perm == PackageManager.PERMISSION_GRANTED) {
            isGranted = true;
            openWifi();//打开WIFI
            LogUtils.e(TAG, "有权限");
        } else {
            LogUtils.e(TAG, "没有权限去申请");
            ActivityCompat.requestPermissions(this,
                    new String[]{Constants.LOCATION_PERMISSION}, REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isGranted = true;
                    openWifi();//打开WIFI
                    LogUtils.e(TAG, "定位权限设置完毕");
                } else {
                    LogUtils.e(TAG, "用户拒绝了定位权限");
                    isGranted = false;
                    mDataEmpty.setText("扫描WIFI缺少定位权限，请授予权限");
                }
                break;
        }
    }


    /**
     * 打开wifi
     */
    private void openWifi() {
        LogUtils.e(TAG, "isGranted " + isGranted);
        if (isGranted) {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                wifiManager.startScan();//启动扫描
                mDataEmpty.setText("扫描中...");
            } else {
                LogUtils.e(TAG, "未打开wifi~~~~!!!!!!");
                wifiManager.setWifiEnabled(true);
            }
        }

    }


    /**
     * 连接Wifi
     */
    private void connectWifi(final String ssid, final int wifiType) {
        LogUtils.e(TAG, "ssid " + ssid);
        mSsid = ssid;
        int networkId = -1;
        for (WifiConfiguration configuration : wifiConfigList) {
            String configId = configuration.SSID.replaceAll("\"", "");
            if (configId.equals(ssid)) {
                networkId = configuration.networkId;
                break;
            }
        }
        //不需要密码
        LogUtils.d(TAG, "networkId " + networkId);
        if (wifiType == 0) {
            mPassword = "";
            mWiFiSettingPresenter.insertOrUpdateDeviceWifi(MyApplication.sToken,
                    MyApplication.sDeviceId, mSsid, mPassword, 1, MyApplication.sAcountId);
            return;
        }

        Intent intent = new Intent(this, WiFiPasswordActivity.class);
        intent.putExtra("ssid", ssid);
        intent.putExtra("networkId", networkId);
        intent.putExtra("wifiType", wifiType);
        startActivity(intent);

    }


    @Override
    protected void dismissNewok() {
        if (mWiFiSettingPresenter.mCall != null) {
            mWiFiSettingPresenter.mCall.cancel();
        }

        if (mWiFiSettingPresenter.mCall1 != null) {
            mWiFiSettingPresenter.mCall1.cancel();
        }
    }


    /**
     * 获取wifi列表
     */
    private void loadData() {
        scanResultList.clear();
        wifiConfigList.clear();
        scanResultList.addAll(wifiManager.getScanResults());
        wifiConfigList.addAll(wifiManager.getConfiguredNetworks());

        LogUtils.d(TAG, "wifiConfigList = " + wifiConfigList.size());
        LogUtils.d(TAG, "scanResultList = " + scanResultList.size());
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String connectWifi = "";
        if (wifiInfo != null) {
            connectWifi = wifiInfo.getSSID().replaceAll("\"", "");
        }
        if (scanResultList != null && scanResultList.size() > 0) {
            LogUtils.d(TAG, "scanResultList != null && scanResultList.size() > 0");
            dataList.clear();
            for (ScanResult result : scanResultList) {
                if (StringUtils.isEmpty(result.SSID)) {
                    continue;
                }
                WifiModel model = new WifiModel();
                model.setWifiName(result.SSID);
                StringBuilder detail = new StringBuilder();
                detail.append("加密方案:" + result.capabilities + "\n");
                detail.append("物理地址(MAC):" + result.BSSID + "\n");
                detail.append("信号电平(RSSI):" + result.level + "\n");
                detail.append("热点频率(MHz):" + result.frequency);
                model.setWifiDetail(detail.toString());
                if (result.capabilities.contains("WEP")) {
                    model.setWifiType(1);
                } else if (result.capabilities.contains("WPA")) {
                    model.setWifiType(2);
                } else {
                    model.setWifiType(0);
                }
                model.setIntensity(wifiManager.calculateSignalLevel(result.level, 5));//信号强度
                model.setConnect(connectWifi.equals(result.SSID));//是否连接
                dataList.add(model);
            }
        }


        if (dataList.size() > 0) {
            mDataEmpty.setVisibility(View.GONE);
            dataView.setVisibility(View.VISIBLE);
            dataAdapter.notifyDataSetChanged();
        } else {
            mDataEmpty.setVisibility(View.VISIBLE);
            mDataEmpty.setText("未获取到WiFi信息");
            dataView.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.iv_back, R.id.lt_stranger_calls})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.lt_stranger_calls:
                if (mSsid == null || mPassword == null) {
                    printn("请设置wifi在开启");
                    return;
                }

                mLtWifiSetting.toggle();
                boolean toggle = mLtWifiSetting.getToggle();
                if (toggle) {
                    mState = 1;
                } else {
                    mState = 0;
                }
                mWiFiSettingPresenter.insertOrUpdateDeviceWifi(MyApplication.sToken,
                        MyApplication.sDeviceId, mSsid, mPassword, mState, MyApplication.sAcountId);
                if (!toggle) {
                    dataView.setVisibility(View.GONE);
                } else {
                    dataView.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "mLtStrangerCalls: " + toggle);
                break;
        }
    }


    /**
     * 查询成功的wifi
     */
    public void onSuccess(ResponseInfoModel data) {
        ResponseInfoModel.ResultBean result = data.getResult();
        mState = result.getState();
        mPassword = result.getPassword();
        mSsid = result.getSsid();
        if (mState == 0) {
            mLtWifiSetting.mIvSwitch.setImageResource(R.mipmap.off);
            mLtWifiSetting.mIsToggle = false;
        } else {
            dataView.setVisibility(View.VISIBLE);
            mLtWifiSetting.mIvSwitch.setImageResource(R.mipmap.on);
            mLtWifiSetting.mIsToggle = true;
        }
    }

    /**
     * 当前网络是4G的通知
     */
    @Override
    protected void netWork4G() {
        LogUtils.e(TAG, "当前网络是4G");
    }


    /**
     * 当前网络是wifi的通知
     */
    @Override
    protected void theNetwork() {
//        isNetWork4G = false;
        LogUtils.e(TAG, "当前网络是wifi");
    }


    /**
     * wifi广播
     */
    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isGranted) {
                return;
            }
            String action = intent.getAction();
            LogUtils.e(TAG, "action " + action);

            if (WifiManager.NETWORK_IDS_CHANGED_ACTION.equals(action)) {
                LogUtils.d(TAG, "连接wifi成功了~~~~!!!!");
            }


            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        LogUtils.e(TAG, "Wifi关闭");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        wifiManager.startScan();//Wifi打开,启动扫描
                        LogUtils.e(TAG, "wifi打开了");
                        break;
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                LogUtils.d(TAG, "扫描完成 ~~~~~!!!!!!");
                loadData();//扫描完成
            } else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
//                wifiManager.startScan();//信号强度变化，重新扫描

            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
                WifiInfo info = wifiManager.getConnectionInfo();
                SupplicantState state = info.getSupplicantState();
                LogUtils.d(TAG, "获取IP中 ~~ " + state);
                if (state == SupplicantState.COMPLETED) {
//                    wifiManager.startScan();//验证成功,启动扫描
                }

                int errorCode = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                int errorCode2 = intent.getIntExtra(ConnectivityManager.CONNECTIVITY_ACTION, -1);

                LogUtils.e(TAG, "errorCode~ " + errorCode);
                LogUtils.e(TAG, "errorCode2~ " + errorCode2);

                if (errorCode == WifiManager.ERROR_AUTHENTICATING) {
                    LogUtils.e(TAG, "验证失败 ~~" + errorCode);
                }

            } else if (WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION.equals(action)) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    String wifiSSID = wifiInfo.getSSID();
                    LogUtils.e(TAG, "连接成功 ~~" + wifiSSID);
                }

            }
        }
    }
}
