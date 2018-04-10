package com.loybin.baidumap.presenter;

import android.content.Context;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.GeoFenceListActivity;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/16 下午8:26
 * 描   述: 电子围栏列表
 */
public class GeoFencePresenter extends BasePresenter {

    private static final String TAG = "GeoFenceListActivity";
    private Context mContext;

    private GeoFenceListActivity mGeoFenceListActivity;

    public Call<ResponseInfoModel> mQueryFenceInfoByDeviceId;
    public Call<ResponseInfoModel> mDeleteFenceById;


    public GeoFencePresenter(Context context, GeoFenceListActivity geoFenceListActivity) {
        super(context);
        mContext = context;
        mGeoFenceListActivity = geoFenceListActivity;
    }


    /**
     * 根据设备id查询设备的电子围栏信息
     *
     * @param token
     * @param deviceId
     * @param isShow
     */
    public void loadingColumn(String token, int deviceId, boolean isShow) {
        Log.d(TAG, "token: " + token);
        Log.d(TAG, "deviceId: " + deviceId);

        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("deviceId", deviceId);
        Log.d("BasePresenter", "根据设备id查询设备的电子围栏信息 : " + String.valueOf(params));

        mQueryFenceInfoByDeviceId = mWatchService.queryFenceInfoByDeviceId(params);
        if (!isShow) {
            mGeoFenceListActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        }
        mQueryFenceInfoByDeviceId.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mGeoFenceListActivity.success(data);
        Log.d(TAG, "size: " + data.getResult().getResultData().size());
        Log.d(TAG, "getResultMsg: " + data.getResultMsg());
        if (data.getResult().getResultData().size() > 0) {
            Log.d(TAG, "parserJson: " + data.getResult().getResultData().get(0).fenceId);
        }

    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mGeoFenceListActivity.error(s);
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        mGeoFenceListActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
        mGeoFenceListActivity.printn(mContext.getString(R.string.Network_Error));
    }


    /**
     * 修改开关的状态
     *
     * @param resultData
     */
    public void changes(ResponseInfoModel.ResultBean.ResultDataBean resultData, String token) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("name", resultData.getName());
        params.put("alarmType", resultData.getAlarmType());
        params.put("state", resultData.getState());
        params.put("fenceType", resultData.getFenceType());
        params.put("radius", resultData.getRadius());
        params.put("lat", resultData.getLat());
        params.put("lng", resultData.getLng());
        params.put("acountId", resultData.getAcountId());
        params.put("deviceId", resultData.getDeviceId());
        params.put("desc", resultData.getDesc() + "");
        params.put("fenceId", resultData.getFenceId());
        Log.d(TAG, "修改开关的状态: " + String.valueOf(params));

        Call<ResponseInfoModel> insertOrUpdateFence = mWatchService.insertOrUpdateFence(params);
        mGeoFenceListActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        insertOrUpdateFence.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        mGeoFenceListActivity.dismissLoading();
        Log.d(TAG, "onSuccess: " + body.getResult().toString());
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        mGeoFenceListActivity.dismissLoading();
        mGeoFenceListActivity.error(body);
        Log.d(TAG, "onError: " + body.getResult());
    }


    /**
     * 删除电子围栏
     *
     * @param resultDataBean
     */
    public void delete(ResponseInfoModel.ResultBean.ResultDataBean resultDataBean, String token) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("deviceId", resultDataBean.getDeviceId());
        params.put("fenceId", resultDataBean.getFenceId());
        Log.d(TAG, "删除电子围栏: " + String.valueOf(params));

        mDeleteFenceById = mWatchService.deleteFenceById(params);
        mGeoFenceListActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mDeleteFenceById.enqueue(mCallback3);
    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        mGeoFenceListActivity.deleteSuccess();
    }
}
