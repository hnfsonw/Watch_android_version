package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.PositioningModePreserter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/10 下午2:55
 * 描   述: 定位模式view
 */
public class PositioningModeActivity extends BaseActivity {


    private static final String TAG = "PositioningModeActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.cb_text)
    ImageView mCbText;

    @BindView(R.id.ll_text_model)
    LinearLayout mLlTextModel;

    @BindView(R.id.cb_accurate)
    ImageView mCbAccurate;

    @BindView(R.id.ll_accurate_mode)
    LinearLayout mLlAccurateMode;

    @BindView(R.id.cb_normal)
    ImageView mCbNormal;

    @BindView(R.id.ll_normal_mode)
    LinearLayout mLlNormalMode;

    @BindView(R.id.cb_save)
    ImageView mCbSave;

    @BindView(R.id.ll_save_mode)
    LinearLayout mLlSaveMode;

    @BindView(R.id.ll_mobile_standard_mode)
    LinearLayout mLMobileStandardMode;

    @BindView(R.id.ll_mobile_save_mode)
    LinearLayout mLMobileSaveMode;

    @BindView(R.id.cb_mobile_standard)
    ImageView mCbMobileStandard;

    @BindView(R.id.cb_mobile_save)
    ImageView mCbMobileSave;

    private boolean isTextModel;
    private boolean isAccurateModel;
    private boolean isNormalModel;
    private boolean isSaveModel;
    private boolean isMobileStandardModel;
    private boolean isMobileSaveModel;
    private int mLocationStyle;
    private PositioningModePreserter mPositioningModePreserter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_positioning_mode;
    }

    @Override
    protected void init() {
        mPositioningModePreserter = new PositioningModePreserter(this, this);
        String stringExtra = getIntent().getStringExtra(STRING);
        mLocationStyle = Integer.parseInt(stringExtra);
        LogUtils.e(TAG, "mLocationStyle " + mLocationStyle);
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.positioning_mode));
        switch (mLocationStyle) {
            case -1:
                setAccurateMode();
                break;

            case 1:
                setAccurateMode();
                break;

            case 2:
                setNormalMode();
                break;

            case 3:
                setSaveMode();
                break;

            case 4:
                setTextModel();
                break;

            case 5:
                setMobileStandardModel();
                break;

            case 6:
                setMobileSaveModel();
                break;
        }
    }


    /**
     * 设置移动标准模式
     */
    private void setMobileStandardModel() {
        if (!isMobileStandardModel) {
            mCbMobileStandard.setVisibility(View.VISIBLE);
            mCbText.setVisibility(View.GONE);
            mCbAccurate.setVisibility(View.GONE);
            mCbNormal.setVisibility(View.GONE);
            mCbSave.setVisibility(View.GONE);
            mCbMobileSave.setVisibility(View.GONE);


            isAccurateModel = false;
            isNormalModel = false;
            isSaveModel = false;
            isTextModel = false;
            isMobileSaveModel = false;

        } else {
            mCbMobileStandard.setVisibility(View.GONE);
        }
        isMobileStandardModel = !isMobileStandardModel;

        LogUtils.e(TAG, "isMobileStandardModel " + isMobileStandardModel);
    }


    /**
     * 设置移动省点模式
     */
    private void setMobileSaveModel() {
        if (!isMobileSaveModel) {
            mCbMobileSave.setVisibility(View.VISIBLE);
            mCbMobileStandard.setVisibility(View.GONE);
            mCbText.setVisibility(View.GONE);
            mCbAccurate.setVisibility(View.GONE);
            mCbNormal.setVisibility(View.GONE);
            mCbSave.setVisibility(View.GONE);


            isAccurateModel = false;
            isNormalModel = false;
            isSaveModel = false;
            isTextModel = false;
            isMobileStandardModel = false;

        } else {
            mCbMobileStandard.setVisibility(View.GONE);
        }
        isMobileSaveModel = !isMobileSaveModel;

        LogUtils.e(TAG, "isMobileSaveModel " + isMobileSaveModel);
    }


    @Override
    protected void dismissNewok() {
        if (mPositioningModePreserter.mCall != null) {
            mPositioningModePreserter.mCall.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.ll_text_model, R.id.ll_accurate_mode, R.id.ll_normal_mode, R.id.ll_save_mode
            , R.id.ll_mobile_standard_mode, R.id.ll_mobile_save_mode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent = getIntent();
                intent.putExtra("locationStyle", mLocationStyle);
                setResult(100, intent);
                finishActivityByAnimation(this);
                break;

            case R.id.ll_text_model:
                setTextModel();
                if (isTextModel) {
                    mLocationStyle = 4;
                    mPositioningModePreserter.upLocationStyle(MyApplication.sToken, MyApplication.sDeviceId, mLocationStyle);
                }
                break;

            case R.id.ll_accurate_mode:
                setAccurateMode();
                if (isAccurateModel) {
                    mLocationStyle = 1;
                    mPositioningModePreserter.upLocationStyle(MyApplication.sToken, MyApplication.sDeviceId, mLocationStyle);
                }
                break;

            case R.id.ll_normal_mode:
                setNormalMode();
                if (isNormalModel) {
                    mLocationStyle = 2;
                    mPositioningModePreserter.upLocationStyle(MyApplication.sToken, MyApplication.sDeviceId, mLocationStyle);
                }
                break;

            case R.id.ll_save_mode:
                setSaveMode();
                if (isSaveModel) {
                    mLocationStyle = 3;
                    mPositioningModePreserter.upLocationStyle(MyApplication.sToken, MyApplication.sDeviceId, mLocationStyle);
                }
                break;

            case R.id.ll_mobile_standard_mode:
                setMobileStandardModel();
                if (isMobileStandardModel) {
                    mLocationStyle = 5;
                    mPositioningModePreserter.upLocationStyle(MyApplication.sToken, MyApplication.sDeviceId, mLocationStyle);
                }
                break;

            case R.id.ll_mobile_save_mode:
                setMobileSaveModel();
                if (isMobileSaveModel) {
                    mLocationStyle = 6;
                    mPositioningModePreserter.upLocationStyle(MyApplication.sToken, MyApplication.sDeviceId, mLocationStyle);
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Intent intent = getIntent();
            intent.putExtra("locationStyle", mLocationStyle);
            setResult(100, intent);
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置省点模式
     */
    private void setSaveMode() {
        if (!isSaveModel) {
            mCbText.setVisibility(View.GONE);
            mCbAccurate.setVisibility(View.GONE);
            mCbNormal.setVisibility(View.GONE);
            mCbMobileSave.setVisibility(View.GONE);
            mCbMobileStandard.setVisibility(View.GONE);
            mCbSave.setVisibility(View.VISIBLE);

            isTextModel = false;
            isAccurateModel = false;
            isNormalModel = false;
            isMobileStandardModel = false;
            isMobileSaveModel = false;

        } else {
            mCbSave.setVisibility(View.GONE);
        }
        isSaveModel = !isSaveModel;

        if (!isSaveModel) {
            mCbSave.setVisibility(View.VISIBLE);
        }

        LogUtils.e(TAG, "isSaveModel " + isSaveModel);
    }


    /**
     * 设置正常模式
     */
    private void setNormalMode() {
        if (!isNormalModel) {
            mCbText.setVisibility(View.GONE);
            mCbAccurate.setVisibility(View.GONE);
            mCbNormal.setVisibility(View.VISIBLE);
            mCbSave.setVisibility(View.GONE);
            mCbMobileSave.setVisibility(View.GONE);
            mCbMobileStandard.setVisibility(View.GONE);

            isTextModel = false;
            isAccurateModel = false;
            isSaveModel = false;
            isMobileStandardModel = false;
            isMobileSaveModel = false;

        } else {
            mCbNormal.setVisibility(View.GONE);
        }
        isNormalModel = !isNormalModel;

        if (!isNormalModel) {
            mCbNormal.setVisibility(View.VISIBLE);
        }


        LogUtils.e(TAG, "isNormalModel " + isNormalModel);
    }


    /**
     * 设置精准模式
     */
    private void setAccurateMode() {
        if (!isAccurateModel) {
            mCbText.setVisibility(View.GONE);
            mCbAccurate.setVisibility(View.VISIBLE);
            mCbNormal.setVisibility(View.GONE);
            mCbSave.setVisibility(View.GONE);
            mCbMobileSave.setVisibility(View.GONE);
            mCbMobileStandard.setVisibility(View.GONE);
            isTextModel = false;
            isNormalModel = false;
            isSaveModel = false;
            isMobileStandardModel = false;
            isMobileSaveModel = false;

        } else {
            mCbAccurate.setVisibility(View.GONE);
        }
        isAccurateModel = !isAccurateModel;

        if (!isAccurateModel) {
            mCbAccurate.setVisibility(View.VISIBLE);
        }

        LogUtils.e(TAG, "isAccurateModel " + isAccurateModel);
    }


    /**
     * 设置测试模式
     */
    private void setTextModel() {
        if (!isTextModel) {
            mCbText.setVisibility(View.VISIBLE);
            mCbAccurate.setVisibility(View.GONE);
            mCbNormal.setVisibility(View.GONE);
            mCbSave.setVisibility(View.GONE);
            mCbMobileSave.setVisibility(View.GONE);
            mCbMobileStandard.setVisibility(View.GONE);

            isAccurateModel = false;
            isNormalModel = false;
            isSaveModel = false;
            isMobileStandardModel = false;
            isMobileSaveModel = false;

        } else {
            mCbText.setVisibility(View.GONE);
        }
        isTextModel = !isTextModel;

        LogUtils.e(TAG, "isTextModel " + isTextModel);

    }


    /**
     * 设置成功
     *
     * @param str
     */
    public void onSuccess(String str) {
        dismissLoading();
        printn(str);
    }
}
