package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.SwitchBabyActivity;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/22 上午9:53
 * 描   述: 添加设备view
 */
public class SwitchBabyPresenter extends BasePresenter {

    private static final String TAG = "SwitchBabyActivity";
    private Context mContext;

    private SwitchBabyActivity mSwitchBabyActivity;
    public Call<ResponseInfoModel> mAcountDeivceList;

    public SwitchBabyPresenter(Context context, SwitchBabyActivity switchBabyActivity) {
        super(context);
        mContext = context;
        mSwitchBabyActivity = switchBabyActivity;
    }


    /**
     * 获取绑定设备列表
     *
     * @param acountId
     * @param token
     * @param isLoading
     */
    public void getAcountDeivceList(long acountId, String token, boolean isLoading) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId + "");
        paramsMap.put("token", token);

        Log.e(TAG, "toRegis: " + String.valueOf(paramsMap));
        mAcountDeivceList = mWatchService.getAcountDeivceList(paramsMap);
        if (!isLoading) {
            isLoading = false;
            mSwitchBabyActivity.showLoading("",mContext);
        }
        mAcountDeivceList.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getResultMsg());
        mSwitchBabyActivity.onSuccess(data);
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
        mSwitchBabyActivity.onError(s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        super.onDissms(s);
        Log.d(TAG, "onDissms: " + s);
        mSwitchBabyActivity.dismissLoading();
        mSwitchBabyActivity.printn(mContext.getString(R.string.Network_Error));
    }
}
