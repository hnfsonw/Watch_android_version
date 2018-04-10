package com.loybin.baidumap.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程工具类
 */

public class ThreadUtils {

    private static Handler sHandler = new Handler(Looper.getMainLooper());//绑定主线程Looper,handler处理消息就在主线程

    //单线程线程池
    private static Executor sExecutor = Executors.newFixedThreadPool(3);


    public static void runOnBackgroundThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    /**
     * 在主线程执行runnable
     *
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable) {
        sHandler.post(runnable);
    }
}