package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.WatchMessagePresenter;
import com.loybin.baidumap.ui.view.LinearTvView;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.widget.chatrow.Constant;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/24 下午12:22
 * 描   述: 手表信息视图
 */
public class WatchMessageActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "WatchMessageActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_model)
    LinearTvView mTvModel;

    @BindView(R.id.tv_watch_version)
    LinearTvView mTvWatchVersion;

    @BindView(R.id.tv_imei_number)
    LinearTvView mTvImeiNumber;

    @BindView(R.id.tv_watch_upgrade)
    LinearTvView mTvWatchUpgrade;
    private String mImei;
    private WatchMessagePresenter mWatchMessagePresenter;
    private Object mDeviceId;
    private String mUpdatedFlag;
    private String mState;
    private Intent mIntent;
    private boolean mToggle;
//    private String path = Environment.getRootDirectory().getPath();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_watch_message;
    }


    @Override
    protected void init() {
        if (mIntent == null) {
            mIntent = new Intent();
        }
        mImei = getIntent().getStringExtra(STRING);
        mUpdatedFlag = getIntent().getStringExtra(BABY);
        mWatchMessagePresenter = new WatchMessagePresenter(this, this);
        mDeviceId = SharedPreferencesUtils.getParam(this, "deviceId", 0);

        initView();
        loadData();


//        String dir = FileUtils.getDir("cache");//sdcard/Android/data/包目录/bean
//        File cacheFile = new File(dir, "fota.conf");
//        LogUtils.e(TAG,cacheFile.getAbsolutePath());
//        String fileFromSD = FileUtils.getFileFromSD(cacheFile.getAbsolutePath());
//        LogUtils.e(TAG,"fileFromSD " + path);

    }


    private void loadData() {
        ResponseInfoModel data = (ResponseInfoModel)
                loadDataFromMem(mDeviceId + getString(R.string.watch_message));
        if (data == null) {
            //从本地拿
            ResponseInfoModel localData = loadDataFromLocalBean(mDeviceId + getString(R.string.watch_message),
                    Constant.PROTOCOL_TIMEOUT_MONTH);
            if (localData == null) {
                //从网络加载
                LogUtils.e(TAG, "从网络加载");
                mWatchMessagePresenter.loadWatchMessage(mDeviceId, MyApplication.sToken);
            } else {
                //
                LogUtils.e(TAG, "从本地获取");
                loadView(localData);
            }
        } else {
            LogUtils.e(TAG, "从内存获取");
            loadView(data);
        }
    }

    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            mIntent.putExtra("state", mState);
            setResult(100, mIntent);
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.watch_message));
        mTvWatchUpgrade.mIvSwitch.setOnClickListener(this);
        mTvWatchUpgrade.mMageview.setVisibility(View.GONE);
        mTvWatchUpgrade.mIvSwitch.setVisibility(View.VISIBLE);
        mTvModel.findViewById(R.id.iv_imageview).setVisibility(View.GONE);
        mTvWatchVersion.findViewById(R.id.iv_imageview).setVisibility(View.GONE);
        mTvImeiNumber.findViewById(R.id.iv_imageview).setVisibility(View.GONE);

        if (mUpdatedFlag != null) {
            if (mUpdatedFlag.equals("1")) {
                mState = "1";
                mTvWatchUpgrade.setToggle(true);
            } else {
                mState = "0";
                mTvWatchUpgrade.setToggle(false);
            }
        }


    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        mIntent.putExtra("state", mState);
        setResult(100, mIntent);
        finishActivityByAnimation(this);
    }


    /**
     * 获取手表信息成功的通知
     *
     * @param data
     */
    public void onSuccess(final ResponseInfoModel data) {
        dismissLoading();

        loadView(data);
//        LogUtils.e(TAG,"保存到本地");
        //保存到本地
//        saveData2Local(mDeviceId+getString(R.string.watch_message),data);
//        //保存到内存
//        saveData2Mem(mDeviceId+getString(R.string.watch_message),data);

    }


    private void loadView(ResponseInfoModel data) {
        ResponseInfoModel.ResultBean result = data.getResult();
        mTvModel.setAttribute(result.getDeviceType());
        mTvImeiNumber.setAttribute(result.getImei());
        mTvWatchVersion.setAttribute(result.getSoftVersion());

    }


    public void onError(ResponseInfoModel s) {
        dismissLoading();
        printn(s.getResultMsg());
    }

    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mWatchMessagePresenter.mCall != null) {
            mWatchMessagePresenter.mCall.cancel();
        }
    }


    @Override
    public void onClick(View v) {
        //手表自动更新
        mTvWatchUpgrade.toggle();
        mToggle = mTvWatchUpgrade.getToggle();
        if (mToggle) {
            mState = "1";
        } else {
            mState = "0";
        }

        mWatchMessagePresenter.insertOrUpdateDeviceAttr(MyApplication.sToken,
                MyApplication.sDeviceId, mState);
        Log.d(TAG, "mLtStrangerCalls: " + mToggle);
    }


    public void onWatchError(String body) {
        mTvWatchUpgrade.setToggle(!mToggle);
        if (!mToggle) {
            mState = "1";
        } else {
            mState = "0";
        }
        printn(body);
        dismissLoading();
    }
}
