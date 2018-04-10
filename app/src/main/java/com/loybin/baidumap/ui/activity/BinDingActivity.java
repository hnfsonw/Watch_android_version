package com.loybin.baidumap.ui.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.BinDingPresenter;
import com.loybin.baidumap.ui.view.AddressSettingDialog;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午5:22
 * 描   述: 绑定与解绑的视图
 */
public class BinDingActivity extends BaseActivity {


    private static final String TAG = "BinDingActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;

    @BindView(R.id.tv_imei)
    TextView mTvImei;

    @BindView(R.id.btn_unbinding)
    Button mBtnUnBinding;

    @BindView(R.id.btn_unbind_submit)
    Button mBtnUnBindSubmit;

    private SharedPreferences mGlobalvariable;
    private int mIsAdmin;
    private BinDingPresenter mBinDingPresenter;
    private String mToken;
    private int mDeviceId;
    private RemoveDialog mRemoveDialog;
    private long mAcountId;
    private String mImei;
    private long mGroupId;
    private Bitmap mBitmap;
    private AddressSettingDialog mAdminDialog;
    private boolean isRemoveDialog;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_binding;
    }


    @Override
    protected void init() {
        mBinDingPresenter = new BinDingPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        mGroupId = mGlobalvariable.getLong("groupId", 0);
        mToken = MyApplication.sToken;
        mDeviceId = MyApplication.sDeviceListBean.getDeviceId();
        mImei = MyApplication.sDeviceListBean.getImei();
        mIsAdmin = MyApplication.sDeviceListBean.getIsAdmin();
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
        }

        initView();

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.binding_unbundling));
        if (mIsAdmin != 1) {
            mBtnUnBinding.setText(getString(R.string.remove_the_binding));
            mBtnUnBindSubmit.setVisibility(View.INVISIBLE);
        }
        mBitmap = UserUtil.createQRCode(mImei, 500);
        ivQrCode.setImageBitmap(mBitmap);
        mTvImei.setText(getString(R.string.watch_imei) + " " + mImei);

    }


    @OnClick({R.id.iv_back, R.id.btn_unbinding, R.id.btn_unbind_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_unbinding:
                //管理员 解除所有人, 非管理员解除自己
                unBinding();
                break;

            case R.id.btn_unbind_submit:
                //解绑并移交管理员
                toActivity(SelectUserActivity.class, "management");
                break;
        }
    }


    /**
     * 解绑或移交
     */
    private void unBinding() {
        if (mIsAdmin == 1) {
            //管理员解绑
            //解除所有人
            isRemoveDialog = true;
            mRemoveDialog.show();
            mRemoveDialog.initTitle(getString(R.string.remove_all) + "?", true);
//
//            if (mAdminDialog == null){
//                mAdminDialog = new AddressSettingDialog(this,this);
//            }
//            mAdminDialog.show();
//            mAdminDialog.setText(getString(R.string.remove_and_handed_over_to_the_administrator),
//                    getString(R.string.solutions_to_all_of_us),getString(R.string.cancel));
        } else {
            //接触自己
            mRemoveDialog.show();
            mRemoveDialog.initTitle(getString(R.string.unbinding));
        }
    }


    @Override
    public void setOptions(int number) {
        switch (number) {
            case 1:

                break;

            case 2:

                break;

            case 3:
                //取消
                break;
        }
    }

    @Override
    public void cancel() {
        try {
            //普通成员解绑
            if (!isRemoveDialog) {
                mBinDingPresenter.disBandOneAcount(mToken, mDeviceId, mAcountId);
            } else {
                //管理员解除全部
                mBinDingPresenter.disBandAllDeviceContracts(MyApplication.sToken, MyApplication.sDeviceId);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    /**
     * 解除自己失败回调
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 解除自己成功回调
     */
    public void onSuccess() {
        try {
            dismissLoading();
            printn(getString(R.string.remove_success));
            if (MyApplication.sDeivceNumber != 0) {
                MyApplication.sDeivceNumber--;
            }
            MyApplication.sDeviceId = -1;
            //清除缓存
            deleteData();
            MyApplication.sInUpdata = true;
            exitHomeActivity();
            toActivity(DevicesHomeActivity.class, 0);
            finishActivityByAnimation(this);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 清除缓存
     */
    private void deleteData() {
        String locationData = getCacheFile(mImei).getAbsolutePath();
        String devcesData = getCacheFile(mImei + mAcountId).getAbsolutePath();
        String groupIdData = getCacheFile(mGroupId + mAcountId + "").getAbsolutePath();
        //清除内存
        deleteData2Mem(mImei);
        deleteData2Mem(mImei + mAcountId);
        //清除本地
        FileUtils.deleteFile(devcesData);
        FileUtils.deleteFile(locationData);
        FileUtils.deleteFile(groupIdData);

    }


    /**
     * 加载网络,清除缓存队列
     */
    @Override
    protected void dismissNewok() {
        try {
            if (mBinDingPresenter.mDisBandOneAcount != null) {
                mBinDingPresenter.mDisBandOneAcount.cancel();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }
}
