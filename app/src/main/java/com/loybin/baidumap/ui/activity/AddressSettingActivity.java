package com.loybin.baidumap.ui.activity;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.AddressSettingPresenter;
import com.loybin.baidumap.ui.view.AddressSettingDialog;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/16 下午5:46
 * 描   述: 安全地址设置
 */
public class AddressSettingActivity extends BaseActivity {

    private static final String TAG = "AddressSettingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_address_name)
    LastInputEditText mEtAddressName;

    @BindView(R.id.tv_reminder)
    TextView mTvReminder;

    @BindView(R.id.tv_right)
    TextView mTvRegiht;


    private AddressSettingDialog mAddressSettingDialog;
    private AddressSettingPresenter mAddressSettingPresenter;
    private int mRadius;
    private double mLng;
    private double mLat;
    private SharedPreferences mGlobalvariable;
    private String mToken;
    private int mDeviceId;
    private long mAcountId;
    private int mFenceId = -1;
    private String mAddress;
    private String mFromMark;
    private static final int CODE = -1;
    public int mNumber = 3;
    private String mName;
    private LatLng mLatLng;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_address_setting;
    }


    @Override
    protected void init() {
        try {
            if (mAddressSettingPresenter == null) {
                mAddressSettingPresenter = new AddressSettingPresenter(this, this);
            }
            mFromMark = getIntent().getStringExtra("FromMark");
            mFenceId = getIntent().getIntExtra("fenceId", CODE);
            mRadius = getIntent().getIntExtra("radius", CODE);
            mLng = getIntent().getDoubleExtra("lng", CODE);
            mLat = getIntent().getDoubleExtra("lat", CODE);
            mName = getIntent().getStringExtra("name");
            mAddress = getIntent().getStringExtra("address");
            mGlobalvariable = getSharedPreferences("globalvariable", 0);
            mToken = mGlobalvariable.getString("token", "");
            mDeviceId = mGlobalvariable.getInt("deviceId", CODE);
            mAcountId = mGlobalvariable.getLong("acountId", CODE);
            Log.d(TAG, "mRadius: " + mRadius);
            Log.d(TAG, "mLng: " + mLng);
            Log.d(TAG, "mLat: " + mLat);

            if (mLng != 0 && mLat != 0) {
                mLatLng = new LatLng(mLat, mLng);
            }
            Log.d(TAG, "mToken: " + mToken);
            Log.d(TAG, "mDeviceId: " + mDeviceId);
            Log.d(TAG, "mAcountId: " + mAcountId);
            Log.d(TAG, "mFenceId: " + mFenceId);
            Log.d(TAG, "FromMark: " + mFromMark);
        } catch (Exception e) {
            LogUtils.e(TAG, "init异常");
        }


        initView();

    }


    private void initView() {
        mTvTitle.setText(getString(R.string.security_address_settings));
        mTvRegiht.setText(getString(R.string.save));
        mTvRegiht.setVisibility(View.VISIBLE);
        mEtAddressName.addTextChangedListener(watcher);
        if (mName != null) {
            mEtAddressName.setText(mName);
            mEtAddressName.setSelection(mName.length());
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_reminder, R.id.tv_right})
    public void onViewClicked(View view) {
        String addressName = mEtAddressName.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_reminder:
                UserUtil.hideSoftInput(this);
                showAddressDialog();
                break;

            case R.id.tv_right:
                if (mLatLng == null) {
                    return;
                }
                mAddressSettingPresenter.save(addressName, mNumber, mAcountId, mDeviceId
                        , mLatLng.latitude, mLatLng.longitude, mRadius, mToken, mFenceId, mAddress);
                break;
        }
    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 12) {
                printn(getString(R.string.maximum_input));
            }
        }
    };


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showAddressDialog() {
        if (mAddressSettingDialog == null) {
            mAddressSettingDialog = new AddressSettingDialog(this, this);
        }
        mAddressSettingDialog.show();
    }


    @Override
    public void setOptions(int number) {
        mNumber = number;
        switch (number) {
            case 1:
                mTvReminder.setText(getString(R.string.enter_the_reminder));
                break;

            case 2:
                mTvReminder.setText(getString(R.string.left_to_remind));
                break;

            case 3:
                mTvReminder.setText(getString(R.string.exit_to_remind));
                break;
        }
    }


    public void nameEmpty() {
        printn(getString(R.string.please_set_the_address_name));
    }


    public void success() {
        dismissLoading();
        printn(getString(R.string.success_save));
        toActivity(GeoFenceListActivity.class, 0);
    }


    public void error(String error) {
        dismissLoading();
        printn(error);
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        try {
            if (mAddressSettingPresenter.mCall != null) {
                mAddressSettingPresenter.mCall.cancel();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }

}
