package com.loybin.baidumap.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.BabyDataActivity;
import com.loybin.baidumap.ui.activity.ScanActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/08 下午4:12
 * 描   述: 扫一扫业务逻辑
 */
public class ScanPresenter extends BasePresenter {

    private static final String TAG = "ScanActivity";
    private String iMei;

    private Context mContext;

    private ScanActivity mScanActivity;
    public Call<ResponseInfoModel> mQueryBindInfoByImei;


    public ScanPresenter(Context context, ScanActivity scanActivity) {
        super(context);
        mContext = context;
        mScanActivity = scanActivity;
    }


    /**
     * 验证IMEI码请求接口
     *
     * @param imei
     * @param token
     */
    public void queryBindInfoByImei(String imei, String token, long acountId) {
        iMei = imei;
        if (TextUtils.isEmpty(imei)) {
            mScanActivity.isImeiEmpty();
            return;
        }

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("imei", imei);
        paramsMap.put("token", token);
        paramsMap.put("acountId", acountId);

        //执行enqueue
        mQueryBindInfoByImei = mWatchService.checkDeviceBindStatus(paramsMap);
        mScanActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mQueryBindInfoByImei.enqueue(mCallback);
    }


    /**
     * 验证IMEI码成功回调
     *
     * @param data deviceBindStatus  0-未绑定管理员 1-已绑定管理员，未绑定APP账户 2-已绑定管理员，已绑定APP账户
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        mScanActivity.dismissLoading();
        int deviceBindStatus = data.getResult().getDeviceBindStatus();
        LogUtils.e(TAG, "deviceBindStatus " + deviceBindStatus);
        switch (deviceBindStatus) {
            case 0:
                if (mScanActivity.mNewBaby != null) {
                    Log.d(TAG, "parserJson: " + "新增宝贝");
                    mScanActivity.onSuccess(data);
                    mScanActivity.toActivity(BabyDataActivity.class, iMei, mScanActivity.mNewBaby);
                    mScanActivity.finishActivityByAnimation(mScanActivity);
                } else {
                    LogUtils.e(TAG, "第一次添加");
                    mScanActivity.onSuccess(data);
                    mScanActivity.toActivity(BabyDataActivity.class, iMei);
                    mScanActivity.finishActivityByAnimation(mScanActivity);
                }
                break;

            case 1:
                if (mScanActivity.mNewBaby != null) {
                    mScanActivity.inAdmin(mScanActivity.mNewBaby);
                } else {
                    mScanActivity.mNewBaby = "";
                    mScanActivity.inAdmin(mScanActivity.mNewBaby);
                }
                break;

            case 2:
                mScanActivity.printn(mContext.getString(R.string.you_have_the_binding_equipment));
                break;

        }
    }


    /**
     * 验证IMEI码失败 80001 回调
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mScanActivity.error(s.getResultMsg());
    }


    /**
     * 没连接到服务器回调
     *
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mScanActivity.dismissLoading();
        mScanActivity.printn(mContext.getString(R.string.Network_Error));
    }

}
