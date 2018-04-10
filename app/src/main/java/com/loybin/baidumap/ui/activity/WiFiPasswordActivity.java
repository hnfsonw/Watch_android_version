package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.WiFiPasswordPresenter;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.KeyboardUtil;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/13 下午3:51
 * 描   述: wifi设置密码view
 */
public class WiFiPasswordActivity extends BaseActivity {

    private static final String TAG = "WiFiSettingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_search)
    LastInputEditText mEtSearch;

    @BindView(R.id.btn_login)
    Button mBtnLogin;

    private WiFiPasswordPresenter mWiFiPasswordPresenter;
    private String mSsid;
    private int mWifiType;
    private int mNetworkId;
    private String mPassword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wifi_password;
    }

    @Override
    protected void init() {
        mWiFiPasswordPresenter = new WiFiPasswordPresenter(this, this);
        mSsid = getIntent().getStringExtra("ssid");
        mWifiType = getIntent().getIntExtra("wifiType", -1);
        mNetworkId = getIntent().getIntExtra("networkId", -1);

        LogUtils.e(TAG, "mSsid " + mSsid);
        LogUtils.e(TAG, "mWifiType  " + mWifiType);
        LogUtils.e(TAG, "mNetworkId  " + mNetworkId);


        initView();
        initListener();
        KeyboardUtil.showSoftInput(this);
    }


    private void initListener() {

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 8) {
                    mBtnLogin.setEnabled(true);
                    mBtnLogin.setBackgroundResource(R.mipmap.register);
                } else {
                    mBtnLogin.setEnabled(false);
                    mBtnLogin.setBackgroundResource(R.drawable.back_shape_corners_tou_black_mask_back);
                }
            }
        });
    }


    private void initView() {
        if (mSsid != null) {
            mTvTitle.setText(mSsid);
        }

        if (mEtSearch.getText().length() < 8) {
            mBtnLogin.setEnabled(false);
            mBtnLogin.setBackgroundResource(R.drawable.back_shape_corners_tou_black_mask_back);
        } else {
            mBtnLogin.setEnabled(true);
            mBtnLogin.setBackgroundResource(R.mipmap.register);
        }

    }

    @Override
    protected void dismissNewok() {
        if (mWiFiPasswordPresenter.mCall != null) {
            mWiFiPasswordPresenter.mCall.cancel();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back, R.id.btn_login})
    public void onViewClicked(View view) {
        mPassword = mEtSearch.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_login:
                if (TextUtils.isEmpty(mPassword) || mPassword.length() < 8) {
                    printn("wifi密码至少是8位");
                    return;
                }
                mWiFiPasswordPresenter.insertOrUpdateDeviceWifi(MyApplication.sToken,
                        MyApplication.sDeviceId, mSsid, mPassword, 1, MyApplication.sAcountId);

                break;
        }
    }

    public WifiConfiguration createWifiInfo(String SSID, String password, int wifiType) {
        //清空config
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\""; //wifi名称

        if (wifiType == 0) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (wifiType == 1) {
            config.hiddenSSID = false;
            config.wepKeys[0] = "\"" + password + "\"";//密码
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        if (wifiType == 2) {
            config.hiddenSSID = false;
            config.preSharedKey = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }


    /**
     * 设置wifi密码成功的通知
     */
    public void onSuccess() {
        dismissLoading();
        Intent intent = new Intent();
        intent.putExtra("password", mPassword);
        intent.putExtra("ssid", mSsid);
        setResult(100, intent);
        finishActivityByAnimation(this);
        printn("手表正在连接WiFi...");
    }
}
