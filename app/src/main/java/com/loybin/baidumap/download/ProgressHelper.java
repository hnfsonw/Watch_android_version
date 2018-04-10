package com.loybin.baidumap.download;

import android.util.Log;

import com.loybin.baidumap.model.ProgressBean;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by  LoyBin
 */
public class ProgressHelper {

    private static ProgressBean mProgressBean = new ProgressBean();
    private static ProgressHandler mProgressHandler;

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {

        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        final ProgressListener progressListener = new ProgressListener() {
            //该方法在子线程中运行
            @Override
            public void onProgress(long progress, long total, boolean done) {
                Log.d("progress:", String.format("%d%% done\n", (100 * progress) / total));
                if (mProgressHandler == null) {
                    return;
                }

                mProgressBean.setBytesRead(progress);
                mProgressBean.setContentLength(total);
                mProgressBean.setDone(done);
                mProgressHandler.sendMessage(mProgressBean);

            }
        };

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();

            }
        });

        return builder;
    }


    public static void setProgressHandler(ProgressHandler progressHandler) {
        mProgressHandler = progressHandler;
    }
}
