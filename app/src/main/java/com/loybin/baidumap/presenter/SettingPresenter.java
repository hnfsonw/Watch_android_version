package com.loybin.baidumap.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.download.DownloadApi;
import com.loybin.baidumap.download.DownloadProgressHandler;
import com.loybin.baidumap.download.ProgressHelper;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.SettingActivity;
import com.loybin.baidumap.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/27 下午6:22
 * 描   述: app设置的业务类
 */
public class SettingPresenter extends BasePresenter {

    private static final String TAG = "SettingActivity";
    private Context mContext;
    private SettingActivity mSettingActivity;
    private File mFile;
    public Call<ResponseInfoModel> mCall;
    public Call<ResponseBody> mDownloadCall;
    public Call<ResponseInfoModel> mQueryDeviceStateByDeviceId;
    public Call<ResponseInfoModel> mInsertOrUpdateDeviceSwtich;
    public Call<ResponseInfoModel> mAppSendCMD;

    public SettingPresenter(Context context, SettingActivity settingActivity) {
        super(context);
        mContext = context;
        mSettingActivity = settingActivity;
    }


    /**
     * 检查当前app是否最新版本
     *
     * @param token
     * @param versionCode
     */
    public void checkVersion(String token, String version, int versionCode) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("version", version);
        paramsMap.put("versionCode", versionCode);

        Log.e(TAG, "toRegis: " + String.valueOf(paramsMap));
        mCall = mWatchService.checkVersion(paramsMap);
        mSettingActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mCall.enqueue(mCallback);
    }


    @Override
    protected void parserJson(ResponseInfoModel data) {
        mSettingActivity.chekVersionSuccess(data);
        Log.d(TAG, "parserJson: " + data.getResultMsg());
    }


    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mSettingActivity.chekVersionError(s.getResultMsg());
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        mSettingActivity.dismissLoading();
        Log.d(TAG, "onDissms: " + s);
    }


    /**
     * 下载APK;
     *
     * @param url
     */
    public void download(final String url) {
        //监听下载进度
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setProgressNumberFormat("%1d KB/%2d KB");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDownloadCall != null) {
                    mDownloadCall.cancel();
                }
            }
        });
        dialog.show();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://msoftdl.360.cn");
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        DownloadApi retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
                Log.e("onProgress", String.format("%d%% done\n", (100 * bytesRead) / contentLength));
                Log.e("done", "--->" + String.valueOf(done));
                dialog.setMax((int) (contentLength / 1024));
                dialog.setProgress((int) (bytesRead / 1024));

                if (done) {
                    Log.d(TAG, "onProgress: " + "下载完成");
                    dialog.dismiss();
                    mSettingActivity.downloadComplete();
                }
            }
        });

        mDownloadCall = retrofit.downloadFileWithDynamicUrlSync(url);
        mDownloadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    String dir = Environment.getExternalStorageDirectory() + "/my/" +
                            mContext.getPackageName() + "/apk/";
                    String path = dir + "updata.apk";
                    File fileDir = new File(dir);
                    mFile = new File(path);
                    if (fileDir.exists()) {
                        fileDir.delete();
                    }
                    fileDir.mkdirs();
                    if (mFile.exists() && mFile.canWrite()) {
                        mFile.delete();
                    }

                    Log.d(TAG, "111Environment: "
                            + Environment.getExternalStorageDirectory().getAbsolutePath());
                    Log.d(TAG, "222mFile.getAbsolutePath(): " + mFile.getAbsolutePath());
                    FileOutputStream fos = new FileOutputStream(mFile);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    LogUtils.e(TAG, "文件写入异常" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


}
