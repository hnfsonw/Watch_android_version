package com.loybin.baidumap.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.loybin.baidumap.download.DownloadApi;
import com.loybin.baidumap.download.DownloadProgressHandler;
import com.loybin.baidumap.download.ProgressHelper;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.SplashActivity;
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
 * 创建时间: 2017/05/08 下午3:00
 * 描   述: 欢迎界面的业务逻辑,用来做初始化判断
 */
public class SplashPresenter extends BasePresenter {


    private static final String TAG = "SplashPresenter";
    private SplashActivity mSplashActivity;
    private boolean mBoolean;
    private SharedPreferences mGlobalvariable;
    private Context mContext;
    public Call<ResponseBody> mDownloadCall;
    public File mFile;


    public SplashPresenter(Context context, SplashActivity splashActivity) {
        super(context);
        mContext = context;
        mSplashActivity = splashActivity;
        mGlobalvariable = mSplashActivity.getSharedPreferences("globalvariable", 0);
    }


    /**
     * 获取绑定设备列表
     *
     * @param token
     * @param acountId
     */
    public void getAcountDeivceList(String token, int acountId, boolean aBoolean) {
        mBoolean = aBoolean;
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId);
        paramsMap.put("token", token);

        Log.e(TAG, "获取绑定设备列表: " + String.valueOf(paramsMap));
        Call<ResponseInfoModel> login = mWatchService.getAcountDeivceList(paramsMap);
        login.enqueue(mCallback);
    }


    /**
     * 检查当前app是否最新版本  回调
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        Log.d(TAG, "parserJson: " + data.getResultMsg());
        mSplashActivity.chekVersionSuccess(data);

    }


    /**
     * 检查当前app是否最新版本 80001 回调
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
        mSplashActivity.initView();

    }


    /**
     * 没连接上服务器
     *
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mSplashActivity.initView();
    }

    @Override
    protected void tokenError() {
        mSplashActivity.mLoginState++;
    }

    /**
     * 判断当前版本号
     *
     * @param token
     * @param s
     * @param versionCode
     */
    public void checkVersion(String token, String s, int versionCode) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("version", s);
        paramsMap.put("versionCode", versionCode);

        Log.e(TAG, "判断当前版本号: " + String.valueOf(paramsMap));
        Call<ResponseInfoModel> call = mWatchService.checkVersion(paramsMap);
        call.enqueue(mCallback);

    }


    /**
     * 下载APK;
     *
     * @param url
     */
    public void download(String url) {
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
                    if (mSplashActivity.chekMandatory()) {
                        mDownloadCall.cancel();
                        mSplashActivity.finishActivityByAnimation(mSplashActivity);
                    } else {
                        mDownloadCall.cancel();
                        mSplashActivity.initView();
                    }

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
                mSplashActivity.isDownload = true;
                if (done) {
                    Log.d(TAG, "onProgress: " + "下载完成");
                    dialog.dismiss();
                    mSplashActivity.downloadComplete();
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
