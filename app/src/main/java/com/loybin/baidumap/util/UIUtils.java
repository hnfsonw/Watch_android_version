package com.loybin.baidumap.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


import com.amap.api.maps.model.LatLng;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 类    名:  UIUtils
 * 描    述： 和ui相关的工具类
 */
public class UIUtils {

    private static File mFile;
    public static double pi = 3.141592653589793 * 3000.0 / 180.0;

    /**
     * 得到上下文
     */
    public static Context getContext() {
        return MyApplication.sInstance;
    }


    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }


    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }


    /**
     * 得到String.xml中字符串数组
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }


    /**
     * 得到Color.xml中的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }


    /**
     * dip-->px
     *
     * @param dip
     * @return
     */
    public static int dp2px(int dip) {
        //dip和px的转换关系
        //1. px/(ppi/160) = dp
        //2  px/dip = density
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (density * dip + .5f);
        return px;
    }


    /**
     * 获取版本信息
     *
     * @return
     */
    public static String getVersion() {
        PackageManager packageManager = getContext().getPackageManager();
        String version = "";
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            version = versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageManager packageManager = getContext().getPackageManager();
        int version = 1;
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = info.versionCode;
            version = versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }


    /**
     * px-->dip
     *
     * @param px
     * @return
     */
    public static int px2Dip(int px) {
        //dip和px的转换关系
        //1. px/(ppi/160) = dp
        //2  px/dip = density
        float density = getResources().getDisplayMetrics().density;
        int dip = (int) (px / density + .5f);
        return dip;
    }


    /**
     * 是否存在sdcard
     *
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 将bitmap保存file路径
     *
     * @param bitmap
     */
    public static void saveBitmapFile(Bitmap bitmap) {
        File filesDir = MyApplication.sInstance.getFilesDir();
        Log.d("BabyDataActivity", "saveBitmapFile: " + filesDir);
        //将要保存图片的路径
        mFile = new File(String.valueOf(filesDir) + "/01.jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {

        }
    }


    /**
     * 获取存储图片的路径
     *
     * @return
     */
    public static File getBitmapFile() {
        return mFile;
    }


    /**
     * 获取活动网路信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    /**
     * 判断网络是否可用
     * <p>需添加权限 android.permission.ACCESS_NETWORK_STATE</p>
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }


    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param gg_lat
     * @param gg_lon
     * @return
     */
    public static LatLng gcj02_To_Bd09(double gg_lon, double gg_lat) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLng(bd_lon, bd_lat);
    }


    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法   将 BD-09 坐标转换成GCJ-02 坐标
     *
     * @param bd_lon
     * @param bd_lat
     * @return
     */
    public static LatLng bd09_To_Gcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLng(gg_lon, gg_lat);
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "开心果";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
