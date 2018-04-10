package com.loybin.baidumap.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.UpDataBabyActivity;
import com.loybin.baidumap.util.LogUtils;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/24 下午4:00
 * 描   述: 更新宝贝的业务逻辑
 */
public class UpDataBabyPresenter extends BasePresenter {

    private static final String TAG = "UpDataBabyActivity";
    private Context mContext;

    private UpDataBabyActivity mUpDataBabyActivity;
    public Call<ResponseInfoModel> mUpdateAcountInfo;
    public Call<ResponseInfoModel> mUpload;

    public UpDataBabyPresenter(Context context, UpDataBabyActivity upDataBabyActivity) {
        super(context);
        mContext = context;
        mUpDataBabyActivity = upDataBabyActivity;
    }


    /**
     * 修改账户信息
     *
     * @param acountId
     * @param babyName
     * @param gender
     * @param imgUrl
     * @param watchNumber
     * @param birthday
     * @param token
     */
    public void acountBindImeiFirst(long deviceId, long acountId, String babyName, int gender,
                                    String imgUrl, String watchNumber, String birthday,
                                    String token, String shortPhone) {
        if (TextUtils.isEmpty(watchNumber)) {
            mUpDataBabyActivity.numberError();
            return;
        }

        String string = birthday;
        if (birthday.length() > 11) {
            string = birthday.substring(0, 11);
        }
        LogUtils.e(TAG, "birthday" + birthday);
        LogUtils.e(TAG, "birthdays" + string);

        LogUtils.e(TAG, "shortPhone:" + shortPhone);
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("deviceId", deviceId);
        paramsMap.put("acountId", acountId);
        paramsMap.put("nickName", babyName);
        paramsMap.put("gender", gender + "");
        if (imgUrl != null) {
            paramsMap.put("imgUrl", imgUrl);
        }
        paramsMap.put("phone", watchNumber);
        paramsMap.put("birthday", birthday);
        paramsMap.put("token", token);
        if (shortPhone != null) {
            paramsMap.put("shortPhone", shortPhone);
        }
        Log.e(TAG, "toRegis: " + String.valueOf(paramsMap));
        //执行enqueue
        mUpdateAcountInfo = mWatchService.updateAcountInfoAndSendCMDToImei(paramsMap);
        mUpDataBabyActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mUpdateAcountInfo.enqueue(mCallback);


    }

    /**
     * 宝贝资料设置完毕成功的回调
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        mUpDataBabyActivity.succeed();
        LogUtils.e(TAG, "parserJson: " + data);
        LogUtils.e(TAG, "parserJson: " + data.getResultMsg());

    }


    /**
     * 宝贝资料 80001失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, "onFaiure: " + s);
        LogUtils.e(TAG, "onFaiure: " + s.getResultMsg());
        mUpDataBabyActivity.error(s.getResultMsg());
    }


    /**
     * 没连接上服务器
     *
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d("BabyDataActivity", "onDissms: " + s);
        mUpDataBabyActivity.printn(mContext.getString(R.string.Network_Error));
        mUpDataBabyActivity.dismissLoading();
    }


    /**
     * 上传图片
     *
     * @param bitmapFile
     * @param token
     */
    public void upload(File bitmapFile, String token) {
        Log.d("BabyDataActivity", "upload: " + bitmapFile.getAbsolutePath());
        Log.d("BabyDataActivity", "upload: " + token);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), bitmapFile);
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("file", bitmapFile.getAbsolutePath());

        Log.e("BabyDataActivity", "toRegis: " + String.valueOf(paramsMap));
        //执行enqueue
        mUpload = mWatchService.upload(token, requestFile);
        mUpDataBabyActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mUpload.enqueue(mCallback2);

    }


    /**
     * 上传图片成功的回调
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        String imgUrl = body.getResult().getUrl();
        mUpDataBabyActivity.onSuccess(imgUrl);

        Log.d("BabyDataActivity", "imgUrl: " + imgUrl);
        Log.d("BabyDataActivity", "body.getResultMsg(): " + body.getResultMsg());
        Log.d("BabyDataActivity", "onSuccess: " + body.getResult().getId());
        Log.d("BabyDataActivity", "onSuccess: " + body.getResult().getCreateDate());
    }


    /**
     * 上传图片80001失败的回掉
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        mUpDataBabyActivity.error(body.getResultMsg());
        Log.d("BabyDataActivity", "onError: " + body.getResultMsg());
    }
}
