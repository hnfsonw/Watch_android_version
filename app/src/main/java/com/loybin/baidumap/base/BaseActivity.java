package com.loybin.baidumap.base;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.hojy.happyfruit.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.util.EasyUtils;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.view.ContactDialog;
import com.loybin.baidumap.util.AppManagerUtils;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.IOUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.NetEvent;
import com.loybin.baidumap.util.StatusBarCompat;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.widget.broadcast.HeadsetRadioReceiver;
import com.loybin.baidumap.widget.broadcast.NetBroadcastReceiver;
import com.loybin.baidumap.widget.hojyprogress.HojyProgress;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 所以activity的基类,这里用来放置共同的方法
 */
public abstract class BaseActivity extends AppCompatActivity implements NetEvent {

    private static final String TAG = "BaseActivity";
    /**
     * 网络状态
     */
    private int netMobile;
    /**
     * 这里保存一个值用来判断网络是否经历了由断开到连接
     */
    private boolean isNetChanges;
    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;


    protected static final String BABY = "baby";
    private ProgressDialog dialog;
    protected Toast mToast;
    public static final int REQUEST_CODE = 1;
    public static final String SERIALIZABLE = "Serializable";
    public static final String STRING = "String";
    public static final String MODE = "Mode";
    public static final int PHOTO_REQUEST_CAMERA = 1;// 拍照

    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    protected static final int RELATION = 4;//选择关系

    protected static final int EXIT = 5;//退出

    protected static final int DECI_CODE = 100;

    protected long time = 0;

    public static BaseActivity mContext;

    protected MyBaseActiviy_Broad oBaseActiviy_Broad;
    /** 记录当前时间 **/
    private long exitTime = 0;



    public String [] mTitles = {
            "爸爸","妈妈","爷爷","奶奶",
            "外公","外婆","伯父","伯母",
            "叔叔","阿姨","哥哥","姐姐",
            "自定义"
    };


    public int [] mIcons = {
            R.mipmap.father,R.mipmap.mother,R.mipmap.grandfather,R.mipmap.grandma,
            R.mipmap.grandpa,R.mipmap.grandmother,R.mipmap.nuncle,R.mipmap.aunt,
            R.mipmap.uncle,R.mipmap.auntie,R.mipmap.elder_brother,R.mipmap.elder_sister,
            R.mipmap.other3x
    };
    private IntentFilter mIntentFilter;
    private HojyProgress mProfress;
    private ContactDialog mContactDialog;
    private boolean mIsEMCLogin;
    private HeadsetRadioReceiver mHeadsetRadioReceiver;
    private IntentFilter mFilter;
    private AlertDialog.Builder mDialog;
    //用来判断当前刚开始登入过滤消息
    public int mEMCLogin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(getLayoutRes());
        //将Activity实例添加到AppManager的堆栈
        AppManagerUtils.getAppManager().addActivity(this);
        mContext = this;
        ButterKnife.bind(this);
        StatusBarCompat.compat(this);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //动态注册广播
        if (oBaseActiviy_Broad == null){
        oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        }
        if (mIntentFilter == null){
        mIntentFilter = new IntentFilter("drc.xxx.yyy.baseActivity");
        }
        registerReceiver(oBaseActiviy_Broad, mIntentFilter);

        //耳机插入广播
        if (mFilter == null){
        mFilter = new IntentFilter();
        }
        mFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        if (mHeadsetRadioReceiver == null){
        mHeadsetRadioReceiver = new HeadsetRadioReceiver();
        }
        registerReceiver(mHeadsetRadioReceiver, mFilter);
    }


    /**
     * 设置状态栏透明
     */
    private void setWindow() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.TRANSPARENT);

//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * 对话框取消
     */
    public void dismiss() {

    }


    /**
     * 选择设备
     */
    public void selectEquipment() {

    }


    /**
     * 更新
     */
    public void upDate() {

    }


    /**
     * 取消更新
     */
    public void dismissVesion() {

    }


    public void watchOff() {

    }


    /**
     * 联系人拒绝
     */
    public void refused() {
        LogUtils.d(TAG,"联系人拒绝");
    }


    /**
     * 联系人同意
     */
    public void agreed() {
        LogUtils.d(TAG,"联系人同意");
    }


    /**
     * 全局的联系人添加通知
     * @param body
     */
    public void showContactDialog(String body,int type,Context context) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return ;
        }

        try {
            LogUtils.e(TAG,"showContactDialog");
            if (mContactDialog != null){
                mContactDialog.dismiss();
                mContactDialog = null;
            }
            mContactDialog = new ContactDialog(AppManagerUtils.getAppManager().currentActivity(),this,type);
            if (AppManagerUtils.getAppManager().currentActivity() != null &&
                    !AppManagerUtils.getAppManager().currentActivity().isFinishing()){
            mContactDialog.show();
            }

            String bodys = body + "";
            LogUtils.e(TAG,"bodys " + bodys);
            String[] split = bodys.split(":");
            if (split.length > 1){
                mContactDialog.initMessage(split[1]);
            }else {
                mContactDialog.initMessage(bodys);
            }
        }catch (Exception e){
            LogUtils.e(TAG,"showContactDialog 异常 "+e.getMessage());
        }

    }


    /**
     * 设置底部dialog
     * @param i
     */
    public void setOptions(int i) {

    }


    /**
     * 拒绝邀请
     */
    public void refusedAgreed() {

    }


    /**
     * 同意邀请
     */
    public void agreedAdd() {

    }

    //定义一个广播
    public class MyBaseActiviy_Broad extends BroadcastReceiver {

        public void onReceive(Context arg0, Intent intent) {
                //接收发送过来的广播内容
            int closeAll = intent.getIntExtra("closeAll", 0);
            if (closeAll == 1) {
                //销毁BaseActivity
                finish();
            }
        }
    }
    public void exitHomeActivity(){
        Intent intent = new Intent("jason.broadcast.action");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);//发送广播
    }


    //退出方法
    protected void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            printn(getString(R.string.exit_the_program));
        } else {
            Intent intent = new Intent("drc.xxx.yyy.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播
        }
    }


    protected void sendBroadcast(){
        Intent intent = new Intent("drc.xxx.yyy.baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);//发送广播
    }


    //申请定位权限
    public void toLocationPermission() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //拒绝
        if (checkSelfPermission == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE},100);

        }else if (checkSelfPermission == PackageManager.PERMISSION_GRANTED){
                    LogUtils.e(TAG,"已经定位开启");
            locationSuccess();
        }
    }



    //申请语音权限
    public void toVoicepermissions() {
        int checkVoicepermissions= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        //拒绝
        if (checkVoicepermissions == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.RECORD_AUDIO},101);

        }else if (checkVoicepermissions == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启语音权限");
            voiceSuccess();
        }
    }


    //申请相机权限
    public void toCanmeraPermissions(){
        int checkCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //拒绝
        if (checkCameraPermission == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.CAMERA},102);

        }else if (checkCameraPermission == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启相机权限");
            cameraSuccess();
        }
    }


    //申请存储权限
    public void toSDmissions() {
        int checkSad= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //拒绝
        if (checkSad == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},103);
        }else if (checkSad == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启存储权限");
        }
    }


    /**
     * 申请权限返回值
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            LogUtils.e(TAG,grantResults.length + "~~~~~~~");
        try {
            switch (requestCode){
                case 100:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"定位权限设置完毕");
                        locationSuccess();
                    }else {
                        LogUtils.e(TAG,"用户拒绝了定位权限");
                        locationError();
                    }
                    break;

                case 101:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"语音权限设置完毕");
                        voiceSuccess();
                    }else {
                        LogUtils.e(TAG,"语音权限被拒绝");
                        voiceError();
                    }
                    break;

                case 102:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"相机权限设置完毕");
                        cameraSuccess();
                    }else {
                        LogUtils.e(TAG,"相机权限被拒绝");
                        cameraError();
                    }
                    break;

                case 103:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"存储权限设置完毕");
                    }else {
                        LogUtils.e(TAG,"存储权限被拒绝");
                        grantedError();
                    }
                    break;
            }
        }catch (Exception e){
            LogUtils.d(TAG,"权限打开异常");
        }

    }


    /**
     * 定位权限被拒绝的通知
     */
    protected void locationError() {

    }


    /**
     * SD存储权限被拒绝
     */
    protected void grantedError() {

    }


    /**
     * 语音权限拒绝的通知
     */
    protected void voiceError() {

    }


    /**
     * 语音权限打开的通知
     */
    protected void voiceSuccess() {

    }


    /**
     * 定位权限打开
     */
    protected void locationSuccess() {

    }

    /**
     * 相机权限被拒绝
     */
    protected void cameraError() {

    }


    /**
     * 相机权限打开
     */
    protected void cameraSuccess() {

    }



    /**
     * 触发loading
     * @param message
     */
    public void showLoading(String message,Context context) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return ;
        }
        try {
            if (mProfress == null){
                mProfress = HojyProgress.create(this,mOnKeyListener)
                        .setStyle(HojyProgress.Style.SPIN_INDETERMINATE);
            }
            mProfress.show();
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }


    /**
     * 取消loading
     */
    public void dismissLoading() {
        try {
            LogUtils.e(TAG,"取消loading");
            if (mProfress != null && mProfress.isShowing()) {
                mProfress.dismiss();
            }
        }catch (Exception e){
        }

    }


    /**
     * dialog监听
     */
    DialogInterface.OnKeyListener mOnKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 3000) {
                    printn(getString(R.string.dismiss));
                    exitTime = System.currentTimeMillis();
                } else {
                    dismissLoading();
                    dismissNewok();
                }
                return true;
            }
            return true;
        }
    };


    /**
     * 吐司show
     * @param message
     */
    public void printn(String message) {
        if (null == mToast) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            // mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    public void toActivity(Class<?> toClass) {
        toActivity(0,toClass);
    }


    public void toActivity(int code,Class<?> toClass) {
        toActivity(code,toClass,null);
    }


    public void toActivity(int code,Class<?> toClass,String string){
        toActivity(code,toClass,string,null);
    }


    public void toActivity(int code ,Class<?> toClass, String string,String baby) {
        toActivity(code,toClass, string,baby, 0);
    }


    public void toActivity(Class<?> toClass, String string) {
        toActivity(toClass, string,null, 0,0);
    }


    protected void toActivity(Class<?> toClass,int flag) {
        Intent intent = new Intent(this, toClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    public void toActivity(Class<?> toClass, String string,String baby) {
        toActivity(toClass, string,baby, 0,0);
    }


    protected void toActivity(Class<?> toClass, String string, String baby,int mode,int flag) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(MODE, mode);
        if (string != null) {
            intent.putExtra(STRING, string);
        }

        if (baby != null){
            intent.putExtra(BABY,baby);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    protected void toActivity(int REQUEST_CODE, Class<?> toClass,
                              String string,String baby, int mode) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(MODE, mode);

        if (string != null) {
            intent.putExtra(STRING, string);
        }

        if (baby != null){
            intent.putExtra(BABY,baby);
        }
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    /**
     * 监听网络状态的回调
     * @param netMobile
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }


    /**
     * 当前的网络状态
     */
    private void isNetConnect() {
        switch (netMobile) {
            case 1://wifi
                isNetChanges = true;
                Log.d(TAG, "isNetConnect: "+"wifi");
                theNetwork();
                break;

            case 0://移动数据
                isNetChanges = true;
                Log.d(TAG, "isNetConnect: "+"移动数据");
                netWork4G();
                theNetwork();
                break;

            case -1://没有网络
                isNetChanges = false;
                Log.d(TAG, "isNetConnect: " +"没有网络");
                noNetwork();
                break;
        }

    }


    protected void warmPrompt(boolean IsNetWork){
        if (!IsNetWork){
            mDialog = new AlertDialog.Builder(this);
            mDialog.setMessage(getString(R.string.mobile_data));
            mDialog.setCancelable(false);
            mDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    netWorkCancel();
                    dialog.dismiss();
                }
            });
            mDialog.setPositiveButton(getString(R.string.local_tyrants_continued),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            netWorkContinue();
                            dialog.dismiss();
                        }


                    });
            mDialog.show();
        }

    }


    /**
     * 继续
     */
    protected void netWorkContinue() {

    }


    /**
     * 不继续
     */
    protected void netWorkCancel() {

    }


    /**
     * 保存数据到本地
     *
     * @param key
     * @param resJson
     */
    public void saveData2Local(String key, String resJson) {
        BufferedWriter writer = null;
        try {
            File cacheFile = getCacheFile(key);

            writer = new BufferedWriter(new FileWriter(cacheFile));
            //写入第一行,当前时间,缓存的生成时间
            writer.write(System.currentTimeMillis() + "");

            //写入第二行
            writer.newLine();//换行
            writer.write(resJson);
            //打印一个日志
            LogUtils.e(TAG, "缓存数据到本地-->: "+ cacheFile.getAbsolutePath()+System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }


//    android 保存Bitmap 到本地 哦

    public String saveImage(Bitmap bmp,String key) {

        String fileName = key+ ".jpg";
        File appDir = getCacheFile(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(appDir);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appDir.getAbsolutePath();
    }



    /**
     * 保存数据到本地
     * @param key
     * @param resJson
     */
    public void saveData2Local(String key, ResponseInfoModel resJson) {
        LogUtils.e(TAG,"保存数据到本地");
        ObjectOutputStream oos = null;
        try {
            File cacheFile = getCacheFile(key);

            oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
            //写入当前时间
            oos.writeLong(System.currentTimeMillis());
            oos.writeObject(resJson);
            //打印一个日志
            LogUtils.e(TAG, "缓存数据到本地-->: "+ cacheFile.getAbsolutePath()+System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG,e.getMessage());
        } finally {
            IOUtils.close(oos);
        }
    }

    /**
     * 保存数据到本地
     *
     * @param key
     * @param resJson
     */
    public void saveData2Local(String key, Object resJson) {
        LogUtils.e(TAG,"保存数据到本地");
        ObjectOutputStream oos = null;
        try {
            File cacheFile = getCacheFile(key);

            oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
            //写入当前时间
            oos.writeLong(System.currentTimeMillis());
            oos.writeObject(resJson);
            //打印一个日志
            LogUtils.e(TAG, "缓存数据到本地-->: "+ cacheFile.getAbsolutePath()+System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG,"异常"+e.getMessage());
        } finally {
            IOUtils.close(oos);
        }
    }


    /**
     * 得到缓存文件
     *
     * @param imei
     * @return
     */
    protected File getCacheFile(String imei) {
        String dir = FileUtils.getDir("cache");//sdcard/Android/data/包目录/bean
        File cacheFile = new File(dir, imei);
        return cacheFile;
    }


    /**
     * 保存数据到内存中
     *
     * @param key
     * @param cacheData
     */
    public void saveData2Mem(String key, Object cacheData) {
        //缓存的key

        //存储结构
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();

        //开始存
        cacheMap.put(key, cacheData);

        LogUtils.e(TAG, "缓存数据到内存了"+key);
    }


    /**
     * 根据KEY 内存中删除
     * @param imei
     */
    protected void deleteData2Mem(String imei){
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();
        if (cacheMap.containsKey(imei)){
            cacheMap.remove(imei);
            LogUtils.e(TAG,"从内存清除");
        }
    }


    /**
     * 生成缓存的Key
     *
     * @param index
     * @return
     */
    public String generateCacheKey(int index) {
        return getInterfaceKey() + "." + index;
    }


    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    public String loadDataFromLocal(String imei,long timeout) {
        LogUtils.e(TAG,timeout+"");
        BufferedReader reader = null;
        try {
            File cacheFile = getCacheFile(imei);
            LogUtils.e(TAG,cacheFile.exists()+"");

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                reader = new BufferedReader(new FileReader(cacheFile));

                //缓存文件的第一行
                String firstLine = reader.readLine();

                long cacheInsertTime = Long.parseLong(firstLine);
                LogUtils.e(TAG,System.currentTimeMillis()+"~~~" + cacheInsertTime+"~~~"+timeout);

//                if ((System.currentTimeMillis() - cacheInsertTime) < timeout) {
                    //有效缓存
                    String cacheData = reader.readLine();


                    //保存数据到内存中
                    saveData2Mem(imei, cacheData);

                    //解析
                    return cacheData ;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(reader);
        }
        return null;
    }


    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    protected ResponseInfoModel loadDataFromLocalBean(String imei,long timeout) {
        ObjectInputStream ois = null;
        try {
            File cacheFile = getCacheFile(imei);
            LogUtils.e(TAG,cacheFile.exists()+"");

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                ois = new ObjectInputStream(new FileInputStream(cacheFile));

                //缓存文件的第一行
                Long cacheInsertTime = ois.readLong();

                LogUtils.e(TAG,System.currentTimeMillis()+"~~~" + cacheInsertTime+"~~~"+timeout);

                if ((System.currentTimeMillis() - cacheInsertTime) < timeout) {
                    //有效缓存
                    ResponseInfoModel cacheData = (ResponseInfoModel) ois.readObject();


                    //保存数据到内存中
                    saveData2Mem(imei, cacheData);

                    //解析
                    return cacheData ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(ois);
        }
        return null;
    }

    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    protected Object loadDataFromLocalBean(String imei,long timeout,int in) {
        ObjectInputStream ois = null;
        try {
            File cacheFile = getCacheFile(imei);
            LogUtils.e(TAG,cacheFile.exists()+"");

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                ois = new ObjectInputStream(new FileInputStream(cacheFile));

                //缓存文件的第一行
                Long cacheInsertTime = ois.readLong();

                LogUtils.e(TAG,System.currentTimeMillis()+"~~~" + cacheInsertTime+"~~~"+timeout);

                if ((System.currentTimeMillis() - cacheInsertTime) < timeout) {
                    //有效缓存
                    Object cacheData = (Object) ois.readObject();
                    //保存数据到内存中
                    saveData2Mem(imei, cacheData);

                    //解析
                    return cacheData ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(ois);
        }
        return null;
    }



    /**
     * 从内存加载数据
     * @param imei 缓存唯一标识
     * @return
     */
    protected Object loadDataFromMem(String imei) {
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();
        if (cacheMap.containsKey(imei)){
            Object memData = cacheMap.get(imei);
            return memData;
        }
        return null;
    }



    protected String getInterfaceKey() {
        return null;
    }


    /**
     * 当前是移动网的通知
     */
    protected void netWork4G() {


    }


    /**
     * 有网络的通知
     */
    protected void theNetwork() {

    }


    /**
     * 没有网络的通知
     */
    protected void noNetwork() {

    }


    /**
     * dialog确定,让子类去实现
     */
    public void cancel() {

    }

    /**
     * 打电话dialog确定，子类实现
     */
    public void callPhoneOn() {
    }


    /**
     * 初始化layout
     * @return
     */
    protected abstract int getLayoutRes();


    /**
     * 初始化方法
     */
    protected abstract void init() throws Exception;


    /**
     * 强制取消网络加载,把网络队列清除
     */
    protected abstract void dismissNewok();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //将Activity实例从AppManager的堆栈中移除
            AppManagerUtils.getAppManager().finishActivity(this);
            if (netBroadcastReceiver != null) {
                unregisterReceiver(netBroadcastReceiver);
            }
            if (oBaseActiviy_Broad != null){
                unregisterReceiver(oBaseActiviy_Broad);//注销广播
            }

            if (mHeadsetRadioReceiver != null){
                unregisterReceiver(mHeadsetRadioReceiver);
            }

            if (!MyApplication.isFrontDesk){
                MyApplication.isFrontDesk = true;
                EaseUI.getInstance().getNotifier().reset();
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage()+"onDestroy 异常");
        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!EasyUtils.isAppRunningForeground(this)){
            LogUtils.d(TAG,"onStop 在后台");
            MyApplication.isFrontDesk = false;
        }else {
            LogUtils.d(TAG,"onStop 在前台");
            MyApplication.isFrontDesk = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            /**
             * 设置监听
             */
            netBroadcastReceiver.setNetEvent(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG,"base onResume  "+getRunningActivityName());
        if (!MyApplication.isFrontDesk){
            MyApplication.isFrontDesk = true;
        EaseUI.getInstance().getNotifier().reset();
        }
        MobclickAgent.onPageStart(getRunningActivityName());
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e(TAG,"base onPause  "+getRunningActivityName());
        MobclickAgent.onPageEnd(getRunningActivityName());
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG,"onRestart");
        LogUtils.d(TAG, DevicesHomeActivity.sPhone + " DevicesHomeActivity.sPhone");
        if (!DevicesHomeActivity.sPhone.equals("") && !DevicesHomeActivity.sMd5Password.equals("")){
        chekEMCLogin();
        }
    }


    public void chekEMCLogin() {
        //用来判断环信是否登入的状态
        if (EMClient.getInstance().isLoggedInBefore() == true && EMClient.getInstance().isConnected() == true){
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    mIsEMCLogin = true;
                    chekCache();
                    LogUtils.e(TAG,"环信已登入 "+" 环信已连接服务器" );
                }
            });
            return;
        }else {
            mIsEMCLogin = false;
        }

        LogUtils.e(TAG,"mIsEMCLogin " + mIsEMCLogin);
        if (!mIsEMCLogin){
            if (MyApplication.getHandler() != null){
                MyApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!EMClient.getInstance().isLoggedInBefore()){
                            LogUtils.e(TAG,"环信还没登入 去登入 " + Thread.currentThread().getName());
                            mIsEMCLogin = true;
                            //环信登入
                            mEMCLogin = 0;
                            emcLogin();
                        }else {
                            if (!EMClient.getInstance().isConnected()){
                                LogUtils.e(TAG,"环信登入了,环信服务器没连接上");
                                mIsEMCLogin = true;
                                mEMCLogin = 0;
                                emcLogin();
                            }
                        }
                    }
                });
            }
        }

    }


    public void chekCache(){
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 mEMCLogin = 1;
            }
        },3000);
    }

    /**
     * 环信登入
     */
    private void emcLogin() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "退出成功");
                //环信登入
                EMClient.getInstance().login(DevicesHomeActivity.sPhone, DevicesHomeActivity.sMd5Password, mEMCallBack);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "退出失败 " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }




    EMCallBack mEMCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            // 加载所有会话到内存
            chekCache();
            EMClient.getInstance().chatManager().loadAllConversations();
            // 加载所有群组到内存，如果使用了群组的话
            EMClient.getInstance().groupManager().loadAllGroups();
            // 登录成功跳转界面
            mIsEMCLogin = false;
            Log.d(TAG, "run: " + "环信已经登入了" + Thread.currentThread().getName());
        }

        @Override
        public void onError(int i, String s) {
            mIsEMCLogin = false;
        }

        @Override
        public void onProgress(int i, String s) {

        }
    };


    /**
     * finish 动画
     * @param activity
     */
    public void finishActivityByAnimation(Activity activity){
        activity.finish();
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    private String getRunningActivityName(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity.substring(runningActivity.lastIndexOf(".")+1);
    }

}
