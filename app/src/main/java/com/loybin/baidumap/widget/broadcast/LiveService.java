package com.loybin.baidumap.widget.broadcast;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

/**
 * @author LoyBin
 * @Description: Android 5.0 以上保活
 */
@SuppressLint("NewApi")
public class LiveService extends JobService {

    @Override
    public void onCreate() {
//		LogUtils.i("xhd", "JobService onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//		LogUtils.i("xhd", "JobService onStart");
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
//		LogUtils.i("xhd", "JobService onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
//		LogUtils.i("xhd", "执行任务：调用 onStartJob 方法");
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
