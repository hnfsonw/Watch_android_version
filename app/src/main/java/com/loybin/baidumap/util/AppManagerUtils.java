package com.loybin.baidumap.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/17 下午7:07
 * 描   述: Activity管理类
 */
public class AppManagerUtils {
    private static ArrayList<Activity> activityStack;
    private static AppManagerUtils instance;

    private AppManagerUtils() {
    }

    /**
     * 单一实例
     */
    public static AppManagerUtils getAppManager() {
        if (instance == null) {
            synchronized (AppManagerUtils.class) {
                if (instance == null) {
                    instance = new AppManagerUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            synchronized (AppManagerUtils.class) {
                if (activityStack == null) {
                    activityStack = new ArrayList<Activity>();
                }
            }
        }
        activityStack.add(activity);
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        try {
            Activity activity = activityStack.get(activityStack.size() - 1);
            LogUtils.e("DevicesHomeActivity ~~", "activity =  " + activity);
            return activity;
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity2() {
        Activity activity = activityStack.get(activityStack.size() - 2);
        LogUtils.e("DevicesHomeActivity ~~", "activity =  " + activity);
        return activity;
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.get(activityStack.size() - 1);
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        } catch (Exception e) {

        }

    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
