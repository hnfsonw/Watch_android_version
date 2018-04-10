package com.loybin.baidumap.ui.activity;

import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.RemoveBindingPresenter;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:09
 * 描   述: 解除绑定的视图
 */
public class RemoveBindingActivity extends BaseActivity {


    private static final String TAG = "RemoveBindingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.ll_remove_hand_over)
    LinearLayout mLlRemoveHandOver;

    @BindView(R.id.ll_remove_binding)
    LinearLayout mLlRemoveBinding;

    @BindView(R.id.ll_remove_all)
    LinearLayout mLlRemoveAll;

    private RemoveBindingPresenter mRemoveBindingPresenter;
    private SharedPreferences mGlobalvariable;
    private String mToken;
    private int mDeviceId;
    private RemoveDialog mRemoveDialog;
    private long mAcountId;
    private String mImei;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_remove_binding;
    }

    @Override
    protected void init() {
        mTvTitle.setText(getString(R.string.remove_the_binding));
        mRemoveBindingPresenter = new RemoveBindingPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mToken = mGlobalvariable.getString("token", "");
        mDeviceId = mGlobalvariable.getInt("deviceId", 0);
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        mImei = mGlobalvariable.getString("imei", "");

        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
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


    @OnClick({R.id.iv_back, R.id.ll_remove_hand_over, R.id.ll_remove_binding, R.id.ll_remove_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.ll_remove_hand_over:
                toActivity(SelectUserActivity.class, "management");
                break;

            case R.id.ll_remove_binding:
                toActivity(SelectUserActivity.class, "ordinary");
                break;

            case R.id.ll_remove_all:
                mRemoveDialog.show();
                mRemoveDialog.initTitle(getString(R.string.remove_all) + "?", true);

                break;
        }
    }

    @Override
    public void cancel() {
        mRemoveBindingPresenter.disBandAllAcount(mToken, mDeviceId);
    }


    /**
     * 解除全部账户失败回调
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 解除全部账户成功回调
     */
    public void onSuccess() {
        try {
            deleteData();
            MyApplication.sDeviceId = -1;
            MyApplication.sInUpdata = true;
            exitHomeActivity();
            toActivity(DevicesHomeActivity.class, 0);
            finishActivityByAnimation(this);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
        dismissLoading();
        printn(getString(R.string.remove_success));
        if (MyApplication.sDeivceNumber != 0) {
            MyApplication.sDeivceNumber--;

        }

    }


    /**
     * 清除缓存
     */
    private void deleteData() {
        String locationData = getCacheFile(mImei).getAbsolutePath();
        String devcesData = getCacheFile(mImei + mAcountId).getAbsolutePath();
        //清除内存
        deleteData2Mem(mImei);
        deleteData2Mem(mImei + mAcountId);
        //清除本地
        FileUtils.deleteFile(devcesData);
        FileUtils.deleteFile(locationData);
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mRemoveBindingPresenter.mDisBandAllAcount != null) {
            mRemoveBindingPresenter.mDisBandAllAcount.cancel();
        }
    }
}
