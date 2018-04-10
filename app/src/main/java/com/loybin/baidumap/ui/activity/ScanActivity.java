package com.loybin.baidumap.ui.activity;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.ScanPresenter;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.TimeUtil;

import java.io.IOException;
import java.util.Vector;

import zxing.camera.CameraManager;
import zxing.decoding.CaptureActivityHandler;
import zxing.decoding.InactivityTimer;
import zxing.view.ViewfinderView;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 扫描二维码的view
 */
public class ScanActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = "ScanActivity";
    private CaptureActivityHandler handler;
    private ViewfinderView mViewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private boolean vibrate;
    private LinearLayout mBackImageView;
    private TextView mTitleText;
    private TextView mTvRight;
    private Dialog mProgressDialog;
    private String scanString = "";
    private SurfaceView mSurfaceView;
    private ScanPresenter mScanPresenter;
    private LastInputEditText mEtImei;
    private SharedPreferences mGlobalvariable;
    private String mToken;
    public String mNewBaby;
    private String mScan;
    private TextView mTvBindingEquipment;
    private String mImei;
    private String mIsCamera;
    private TextView mScanTips;
    private TextView mTextView;
    private TextView mTvCamera;
    private TextView mTvText3;
    private boolean mIsView;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_scan;
    }


    @Override
    protected void init() {
        mScanPresenter = new ScanPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mToken = mGlobalvariable.getString("token", "");
        mNewBaby = getIntent().getStringExtra(STRING);
        mScan = getIntent().getStringExtra("string");
        mIsCamera = getIntent().getStringExtra("isCamera");

        Log.d("BabyDataActivity", "sListSize: " + MyApplication.sListSize);
        initView();
        initListener();
    }


    private void initView() {
        mProgressDialog = new TimeUtil().createLoadingDialog(this,
                getResources().getString(R.string.app_Loding));

        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);
        mBackImageView = (LinearLayout) findViewById(R.id.iv_back);
        mTitleText = (TextView) findViewById(R.id.tv_title);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mEtImei = (LastInputEditText) findViewById(R.id.et_imei);
        mTvBindingEquipment = (TextView) findViewById(R.id.tv_binding_equipment);
        mScanTips = (TextView) findViewById(R.id.scan_Tips);
        mTextView = (TextView) findViewById(R.id.textView);
        mTvCamera = (TextView) findViewById(R.id.tv_camera);
        mTvText3 = (TextView) findViewById(R.id.textView3);
        mEtImei.addTextChangedListener(watcher);

        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.tv_next));
        mTvRight.setTextColor(Color.GRAY);
        mTvRight.setEnabled(false);
        mTitleText.setText(getResources().getString(R.string.Scan_Title));
        //照相机权限未开启,提示用户
        if (mIsCamera != null) {
            Log.e(TAG, "照相机权限未开启");
            mScanTips.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);
            mTvText3.setVisibility(View.INVISIBLE);
            mTvCamera.setVisibility(View.VISIBLE);
        }
    }


    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mTvBindingEquipment.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mImei = mEtImei.getText().toString().trim();
        switch (v.getId()) {
            case R.id.iv_back:
                if (mScan != null) {
                    toActivity(LoginActivity.class);
                    finishActivityByAnimation(this);
                }
                mIsCamera = null;
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                try {
                    mScanPresenter.queryBindInfoByImei(mImei, mToken, MyApplication.sAcountId);
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                break;

            case R.id.tv_binding_equipment:
                toActivity(UserHelpActivity.class);
                break;
        }

    }


    /**
     * 解码处理回调
     *
     * @param result  结果
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        try {
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            mImei = result.getText();
            Log.e(TAG, "handleDecode: " + mImei);
            if (mImei.isEmpty()) {
                printn(ScanActivity.this.getResources()
                        .getString(R.string.Scan_Failure));
                return;
            } else {
                mEtImei.setText(mImei);
                Log.i(TAG, mImei);
                if (handler != null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (handler != null) {
                                handler.restartPreviewAndDecode();
                            }
                        }
                    }, 2000);

                }
                if (mImei.length() == 15) {
                    mScanPresenter.queryBindInfoByImei(mImei, mToken, MyApplication.sAcountId);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "扫描异常" + e.getMessage());
        }

    }


    /**
     * EditText监听
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.length() == 15) {
                mTvRight.setEnabled(true);
                mTvRight.setTextColor(Color.WHITE);
            } else {
                mTvRight.setEnabled(false);
                mTvRight.setTextColor(Color.GRAY);
            }
        }
    };


    /**
     * 初始化二维码扫描器
     */
    public void initScan() {
        CameraManager.init(getApplication());

        inactivityTimer = new InactivityTimer(this);

        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }


    /**
     * 初始化Camera设备
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            if (!mIsView){
                mIsView = true;
                if (handler == null) {
                    handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
                }
            }

        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }


    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }


    public Handler getHandler() {
        return handler;
    }


    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();

    }


    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }


    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + 111111111);
        mIsView = false;
        initScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        asyncTaskCheckDevice.cancel(true);
        LogUtils.e(TAG, "onPause");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
            mIsView = true;
        }
        //mActivity Pause时关闭扫描设备
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: " + 22222);
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    public void isImeiEmpty() {
        printn(getString(R.string.imei_empty));
    }


    public void error(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
        if (handler != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.restartPreviewAndDecode();
                    }
                }
            }, 2000);

        }
    }


    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
    }


    public void inAdmin(String newBaby) {
        if (newBaby.equals("")) {
            //第一次添加
            toActivity(SendApplyActivity.class, mImei);
        } else {
            //有设备在添加
            toActivity(SendApplyActivity.class, mImei, newBaby);
        }

//        printn(getString(R.string.changed_my_equipment_has_been_binding_administrator));
//        if (handler != null){
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (handler != null){
//                    handler.restartPreviewAndDecode();
//                    }
//                }
//            },2000);
//
//        }
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            if (mScan != null) {
                toActivity(LoginActivity.class);
                finishActivityByAnimation(this);
            } else if (mNewBaby != null) {
                finishActivityByAnimation(this);
            } else {
                exit();// 退出方法
            }
        }
        return true;
    }

    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mScanPresenter.mQueryBindInfoByImei != null) {
            mScanPresenter.mQueryBindInfoByImei.cancel();
        }

    }
}