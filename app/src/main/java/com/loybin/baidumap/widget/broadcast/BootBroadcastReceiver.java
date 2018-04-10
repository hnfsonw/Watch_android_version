package com.loybin.baidumap.widget.broadcast;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.ServiceUtils;

public class BootBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    public static final int LIVE_JOB_ID = 0;
    private JobScheduler mJS;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_BOOT.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (!ServiceUtils.isServiceRunning(context, MyApplication.LIVE_SERVICE)) {
                    context.startService(new Intent(context, LiveService.class));
                    scheduleJob(context, getJobInfo(context));
                }
            }
            //TODO 其他后台任务的执行
        }
    }

    @SuppressLint("NewApi")
    public void scheduleJob(Context context, JobInfo info) {
        Log.i("xhd", "JobSchedule 调度 JobInfo");
        mJS = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJS.schedule(getJobInfo(context));
    }

    @SuppressLint("NewApi")
    public JobInfo getJobInfo(Context context) {
        JobInfo.Builder builder = new JobInfo.Builder(LIVE_JOB_ID,
                new ComponentName(context, LiveService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);//任务不需要网络
        builder.setPersisted(true);//设备重新启动时继续执行
        //该项工作多少秒重复（不定时执行，但该毫秒之内只会被执行一次）
        builder.setPeriodic(100);
        return builder.build();
    }

}
