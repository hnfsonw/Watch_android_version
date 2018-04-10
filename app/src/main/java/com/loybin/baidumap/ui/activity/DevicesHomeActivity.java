package com.loybin.baidumap.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.util.EMLog;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.MQTTModel;
import com.loybin.baidumap.model.MessageListModel;
import com.loybin.baidumap.model.ParametersModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.mqtt.ALiyunIotX509TrustManager;
import com.loybin.baidumap.presenter.DevicesHomePresenter;
import com.loybin.baidumap.ui.view.CallPhoneDialog;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.NavigationDialog;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.ui.view.SwitchBabyDialog;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SignUtil;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.util.TimeUtil;
import com.loybin.baidumap.util.UIUtils;
import com.loybin.baidumap.widget.chatrow.Constant;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 首页视图
 */
public class DevicesHomeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, Animation.AnimationListener {

    private static final String TAG = "DevicesHomeActivity";
    private static final String TAG2 = "MQTT";
    private static final int TIME_MINUS = -1;
    private static final int TIME_ZERO = 0;
    private static final int MQTT_CODE = 10;
    private static final int MQTT_ERROR = 44;

    @BindView(R.id.iv_icon)
    public CircleImageView mIvIcon;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.map_view)
    public MapView mMapView;

    @BindView(R.id.location_tv)
    public TextView mLocationTv;

    @BindView(R.id.cb_map_type)
    public CheckBox mCbMapType;

    @BindView(R.id.nav_left)
    public NavigationView mNavLeft;

    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    @BindView(R.id.tv_lotion_time)
    public TextView mTvLotionTime;

    @BindView(R.id.ll_lontion)
    LinearLayout mLlLontion;

    @BindView(R.id.ll_navigation)
    public LinearLayout mLlNavigation;

    @BindView(R.id.ll_history)
    LinearLayout mLlHistory;

    @BindView(R.id.btn_location)
    ImageView mBtnLocation;

    @BindView(R.id.ll_send_location)
    public LinearLayout mLlSendLocation;

    @BindView(R.id.iv_time)
    ImageView mIvTime;

    @BindView(R.id.ll_electronic_fence)
    LinearLayout mLlElectronicFence;

    @BindView(R.id.tv_nike_name)
    public TextView mTvNickName;

    @BindView(R.id.tv_accuracy)
    public TextView mTvAccuracy;

    @BindView(R.id.tv_type)
    public TextView mTvType;

    @BindView(R.id.tv_new_message)
    public TextView mTvNewMessage;

    @BindView(R.id.iv_power)
    public ImageView mIvPower;

    @BindView(R.id.ll_switch_baby)
    LinearLayout mLlSwitchBaby;

    @BindView(R.id.iv_tyoe)
    ImageView mIvType;

    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;

    @BindView(R.id.ll_map_type)
    RelativeLayout mLlMapType;

    @BindView(R.id.ll_map_type_light)
    public CircleImageView mLlMapType_light;

    @BindView(R.id.ll_history_light)
    public CircleImageView mLlHistory_light;

    @BindView(R.id.ll_electronic_fence_light)
    public CircleImageView mLlelectronic_fence_light;

    @BindView(R.id.add_zoom)
    TextView mAddZoom;

    @BindView(R.id.narrow_zoom)
    TextView mNarrowZoom;

    @BindView(R.id.ll_listen_story)
    LinearLayout mLlListenStory;

    @BindView(R.id.iv_notification_message)
    ImageView mIvNotificationMessage;

    @BindView(R.id.iv_unread)
    public ImageView mivUnread;


    // 地图相关
    public boolean mIsFirstLoc = true;// 是否首次定位
    private boolean mIsNetwork;
    public EaseUI mEaseUI;

    public View mLocationInforPopupWindow;

    public View mHeaderView;
    public DevicesHomePresenter mDevicesHomePresenter;
    public NavigationDialog mNavigationDialog;

    public SharedPreferences mGlobalvariable;
    public CircleImageView mNavIcon;
    public ImageView mIvGender;
    public TextView mTvName;
    public List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceList;
    public String newBaby = "newBaby";
    public static int mListSize = 0;
    public static int sNumber = 0;
    public CircleImageView mCivIcon;
    public String mToken;
    public long mAcountId;
    public Intent mIntent;
    public ResponseInfoModel.ResultBean.DeviceListBean mDeviceListBean;
    //指令代码
    public int mCommand = 10006;
    public String mMessage = "上传定位信息";
    public String mAppAccount;
    public Gson mGson;
    public ParametersModel.ParametersBean mParameters;
    public boolean isSend = true;
    public boolean mIsMessage = false;
    public static String sPhone = "";
    public static String sMd5Password = "";
    public boolean mChekEMCLogin = true;
    public RemoveDialog mRemoveDialog;
    public boolean isDismiss = false;
    public IntentFilter mIntentFilter;
    public SettingActiviy_Broad mSettingActiviy_Broad;
    public static int sMessageSize = 0;
    public String mStrangeCallSwitch;
    public String mName;
    private TextView mTvMs;
    private long mLocationTimes;
    private ImageView mIvOval;
    public boolean isDeviceList;
    public double mLat;
    public double mLng;
    private String mIsCamera;
    public AMap mAMap;
    public BitmapDescriptor mBitmapDescriptor;
    private String mImgUrl;
    private View mInforWindow;
    public Marker mMarker;
    private LatLng mLatLng;
    private UiSettings mUiSettings;
    private String mAccuracy;
    private String mAddress;
    private int mType;
    private String mLocationTime;
    private BitmapDescriptor mFromView;
    public static Bitmap sBitmap;
    private int mIsAdmin;
    private boolean mFirstLogin;
    private Animation mOperatingAnim;
    //手表开机状态 0 关闭 1 开机
    private String mBootState = "";
    private TextView mTvAge;
    private DateFormat mFormat;
    private String mUpdatedFlag;
    private String mSoftVersion;//手表软件版本号
    private int mSoftVersions;

    public  MqttAndroidClient mMqttAndroidClient;
    public MqttConnectOptions mMqttConnectOptions;
    public boolean mIsToken;
    private ParametersModel mParametersModel;
    private ParametersModel mModel;
    private String mMqttUsername;
    private String mMqttPassword;
    private String mTimestamp;
    private String mDeviceName;
    private String mProductKey;
    private String mDeviceSecret;
    private SSLSocketFactory mSocketFactory;
    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_devices_location;
    }


    @Override
    protected void init() throws Exception {
        mImgUrl = getIntent().getStringExtra(STRING);
        if (mImgUrl != null) {
            if (!mImgUrl.equals("")) {
                ThreadUtils.runOnBackgroundThread(new Runnable() {
                    @Override
                    public void run() {
                        sBitmap = UIUtils.returnBitmap(mImgUrl);

                    }
                });
            }
        }

        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mGlobalvariable.edit().putBoolean("login", true).apply();
        mFirstLogin = mGlobalvariable.getBoolean("firstLogin", true);
        mToken = mGlobalvariable.getString("token", "");
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        sPhone = mGlobalvariable.getString("appAccount", "");
        sMd5Password = mGlobalvariable.getString("md5Password", "");
        boolean login = mGlobalvariable.getBoolean("login", false);
        mAppAccount = mGlobalvariable.getString("appAccount", "");
        //MQTT参数获取
        mDeviceSecret = mGlobalvariable.getString(mAppAccount+"deviceSecret", "");
        mProductKey = mGlobalvariable.getString("productKey", "");
        mDeviceName = mAppAccount;
        mTimestamp = Constants.t;
        Log.d(TAG, "login: " + login);
        mDevicesHomePresenter = new DevicesHomePresenter(this, this);
        if (mGson == null) {
            mGson = new Gson();
        }

        if (mIntent == null) {
            mIntent = new Intent();
        }
        //动态注册广播
        if (mSettingActiviy_Broad == null) {
            mSettingActiviy_Broad = new SettingActiviy_Broad();
        }
        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter("jason.broadcast.action");
        }
        mEaseUI = EaseUI.getInstance();
        registerReceiver(mSettingActiviy_Broad, mIntentFilter);

        if (mFormat == null) {
            mFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        initView();
        initAmap();

        if (mFirstLogin) {
            initGuide();
        }
        EventBus.getDefault().register(this);
        initListener();
        LogUtils.e(TAG, "MyApplication.sInUpdata~~" + MyApplication.sInUpdata);

        if (!MyApplication.sInUpdata) {
            loadDataDeviceList();
        }

        mDevicesHomePresenter.chekEMCLogin();

        if (mToken.length() > 2) {
            mDevicesHomePresenter.getTypesAndLastMessage(mToken, MyApplication.sAcountId);

        } else if (MyApplication.sToken.length() > 2) {
            mDevicesHomePresenter.getTypesAndLastMessage(MyApplication.sToken, MyApplication.sAcountId);
        }


        initMQTT();
        mqttConnect();

    }


    /**
     * 通知消息被清空了,回调
     *
     * @param startModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(MessageListModel startModel) {
        LogUtils.e(TAG, "通知消息被清空了");
        mivUnread.setVisibility(View.GONE);
        mDevicesHomePresenter.getTypesAndLastMessage(MyApplication.sToken, MyApplication.sAcountId);
    }


    /**
     * 初始化引导页
     */
    private void initGuide() {
        mDevicesHomePresenter.showGuide();
    }

    /**
     * 初始化加载数据
     */
    private void loadDataDeviceList() {
        mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //用Toolbar替换ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mHeaderView = mNavLeft.getHeaderView(0);
        mTvName = (TextView) mHeaderView.findViewById(R.id.tv_name);
        mTvName.getPaint().setFakeBoldText(true);
        mNavIcon = (CircleImageView) mHeaderView.findViewById(R.id.nav_icon);
        mNavIcon.setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_nav_icon).setOnClickListener(this);
        mIvGender = (ImageView) mHeaderView.findViewById(R.id.iv_gender);
        mTvAge = (TextView) mHeaderView.findViewById(R.id.tv_age);
        mHeaderView.findViewById(R.id.ll_watch_setting).setOnClickListener(this);
//        mHeaderView.findViewById(R.id.ll_switch).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_watch_message).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_the_new_baby).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_baby_card).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_watch_address_book).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_clas_time).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_call_setup).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_setting).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_contact_us).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_binding_unbundling).setOnClickListener(this);
        mHeaderView.findViewById(R.id.ll_step_counter).setOnClickListener(this);
        mNavIcon.setOnClickListener(this);

        mOperatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        mOperatingAnim.setInterpolator(lin);

        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    /**
     * 地图初始化
     */
    private void initAmap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mUiSettings.setTiltGesturesEnabled(false);
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
        }
    }


    /**
     * 初始化监听
     */
    public void initListener() {
        mIvIcon.setOnClickListener(this);
        mCbMapType.setOnCheckedChangeListener(this);
        mOperatingAnim.setAnimationListener(this);
    }


    /**
     * 导航地图的点击事件
     */
    public void navigation() {
        if (mNavigationDialog == null) {
            mNavigationDialog = new NavigationDialog(this, this);
        }
        mNavigationDialog.show();
    }


    /**
     * 语聊
     *
     * @param view
     */
    public void languageChat(View view) {
        if (mDeviceListBean == null) {
            mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
            return;
        }
        toVoicepermissions();
    }


    /**
     * 语音权限打开的通知
     */
    @Override
    protected void voiceSuccess() {
        if (EMClient.getInstance().isLoggedInBefore() == false || EMClient.getInstance().isConnected() == false) {
            printn(getString(R.string.The_server_is_connected));
            return;
        }

        sMessageSize = 0;
        mTvNewMessage.setVisibility(View.GONE);

        if (mDeviceList.size() >= 2) {
            toActivity(GroupChatActivity.class, mDeviceListBean.getGroupId() + "");
        } else {
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, mDeviceListBean.getGroupId() + "");
            //传入参数
            Bundle args = new Bundle();
            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
            args.putString(EaseConstant.EXTRA_USER_ID, mDeviceListBean.getGroupId() + "");
            intent.putExtra("conversation", args);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }


    /**
     * 语音权限没打开
     */
    @Override
    protected void voiceError() {
        printn(getString(R.string.please_open_the_chat_privileges));
    }


    /**
     * 拨打电话
     *
     * @param view
     */
    public void callPhone(View view) {
        if (mDeviceListBean == null) {
            mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
            return;
        }

        String phone = mDeviceListBean.getPhone();
        String shortPhone = mDeviceListBean.getShortPhone();


        CallPhoneDialog callPhoneDialog = new CallPhoneDialog(this, this);
        callPhoneDialog.show();
        LogUtils.d(TAG, "shortPhone " + shortPhone);
        if (shortPhone != null && !shortPhone.equals("")) {
            callPhoneDialog.initTitle("", getString(R.string.call_) + shortPhone,
                    getString(R.string.cancel), getString(R.string.determine));
        } else {
            callPhoneDialog.initTitle("", getString(R.string.call_) + phone,
                    getString(R.string.cancel), getString(R.string.determine));
        }

    }


    /**
     * 打电话确认
     */
    public void callPhoneOn() {
        requestPermission();
    }


    /**
     * 申请电话权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        100);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            switch (requestCode) {

                case 100:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        callPhone();
                    } else {
                        printn(getString(R.string.phone_error_premission));
                    }
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "打电话权限异常");
        }

    }


    /**
     * 拨打电话
     */
    private void callPhone() {
        String phone = mDeviceListBean.getPhone();
        LogUtils.d(TAG, "phone: " + phone);
        //意图：想干什么事
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        String shortPhone = mDeviceListBean.getShortPhone();
        if (shortPhone != null && shortPhone.length() >= 2) {
            intent.setData(Uri.parse("tel:" + shortPhone));
        } else if (phone != null) {
            Log.d(TAG, "phone: " + phone);
            intent.setData(Uri.parse("tel:" + phone));
        }

        //开启系统拨号器
        startActivity(intent);
    }


    /**
     * 定位类型切换:　true为手机定位, false为设备定位
     * 我的位置是否显示:
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_map_type:
                if (isChecked) {
                    mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                } else {
                    mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                }
                break;

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nav_icon:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                mIntent.setClass(this, UpDataBabyActivity.class);
                mIntent.putExtra("imgUrl", mDeviceListBean.getImgUrl());
                mIntent.putExtra("gender", mDeviceListBean.getGender());
                mIntent.putExtra("phone", mDeviceListBean.getPhone());
                mIntent.putExtra("nickName", mDeviceListBean.getNickName());
                mIntent.putExtra("birthday", mDeviceListBean.getBirthday());
                mIntent.putExtra("isAdmin", mDeviceListBean.getIsAdmin());
                mIntent.putExtra("watchAcountId", mDeviceListBean.getAcountId());
                mIntent.putExtra("shortPhone", mDeviceListBean.getShortPhone());
                startActivityForResult(mIntent, DECI_CODE);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.nav_icon:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                mIntent.setClass(this, UpDataBabyActivity.class);
                mIntent.putExtra("imgUrl", mDeviceListBean.getImgUrl());
                mIntent.putExtra("gender", mDeviceListBean.getGender());
                mIntent.putExtra("phone", mDeviceListBean.getPhone());
                mIntent.putExtra("nickName", mDeviceListBean.getNickName());
                mIntent.putExtra("birthday", mDeviceListBean.getBirthday());
                mIntent.putExtra("isAdmin", mDeviceListBean.getIsAdmin());
                mIntent.putExtra("watchAcountId", mDeviceListBean.getAcountId());
                mIntent.putExtra("shortPhone", mDeviceListBean.getShortPhone());
                startActivityForResult(mIntent, DECI_CODE);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;


            case R.id.ll_the_new_baby:
                //话费查询
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }

                toActivity(PhoneEnquiryActivity.class, mDeviceListBean.getGender() + "");
                break;

            case R.id.ll_baby_card:
                printn("ll_baby_card");
                break;

            case R.id.ll_watch_address_book:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                mIsAdmin = mDeviceListBean.getIsAdmin();
                if (mSoftVersions >= 9){
                    //有好友功能
                    toActivity(WatchLeisuresActivity.class, mIsAdmin + "");
                }else {
                    toActivity(WatchLeisureActivity.class, mIsAdmin + "");
                }

                break;

            case R.id.ll_watch_setting:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                mIsAdmin = mDeviceListBean.getIsAdmin();
                boolean connected = mMqttAndroidClient.isConnected();
                LogUtils.e(TAG2,"connected "+connected);
                toActivity(WatchSettingActivity.class, mIsAdmin + "",mSoftVersions+"");
                break;

            case R.id.ll_clas_time:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                toActivity(ClassBanActivity.class);
                break;

            case R.id.ll_binding_unbundling:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                toActivity(BinDingActivity.class);
                break;

            case R.id.ll_call_setup:
                printn("ll_call_setup");
                break;

            case R.id.ll_setting:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                mIntent.setClass(this, SettingActivity.class);
                startActivity(mIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.ll_contact_us:
                toActivity(ContactUsActivity.class);
                //联系我们
                break;

            case R.id.iv_icon:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.ll_watch_message:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                toActivity(100, WatchMessageActivity.class, mDeviceListBean.getImei(), mUpdatedFlag);
                break;

            case R.id.ll_step_counter:
                //健康计步
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }

                toActivity(StepCounterActivity.class, mDeviceListBean.getImgUrl(),
                        mDeviceListBean.getNickName());
                break;
        }
    }


    /**
     * 照相机打开的通知
     */
    @Override
    protected void cameraSuccess() {
        toActivity(ScanActivity.class, newBaby);
    }


    /**
     * 照相机拒绝的通知
     */
    @Override
    protected void cameraError() {
        mIsCamera = "mIsCamera";
        Intent intent = new Intent(mContext, ScanActivity.class);
        intent.putExtra("isCamera", mIsCamera);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            mUpdatedFlag = data.getStringExtra("state");
            Log.d(TAG, "onActivityResult: " + "收到消息" + mUpdatedFlag);
        } else if (resultCode == 104) {
            //同意邀请选择关系返回的
            mDevicesHomePresenter.mIsNotice = true;
            mDevicesHomePresenter.getAcountDeivceList(MyApplication.sToken, MyApplication.sAcountId);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode == EXIT) {
            finishActivityByAnimation(this);
        } else if (resultCode == DECI_CODE) {
            Log.d(TAG, "onActivityReenter: " + "收到消息" + mUpdatedFlag);
        }

        super.onActivityReenter(resultCode, data);
    }


    @OnClick({R.id.ll_navigation, R.id.ll_history, R.id.ll_send_location,
            R.id.ll_electronic_fence, R.id.ll_switch_baby, R.id.add_zoom, R.id.narrow_zoom
            , R.id.ll_listen_story, R.id.iv_notification_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_navigation:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                navigation();
                break;

            case R.id.ll_history:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                toActivity(DevicesHistoryActivity.class);
                break;

            case R.id.ll_send_location:
                LogUtils.e(TAG, isSend + "~~~isSend");
                //发送透传
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }

                if (!isSend) {
                    printn(getString(R.string.is_access_to_location_please_wait_a_moment));
                    return;
                }


                if (mMqttAndroidClient != null){
                    if (!mMqttAndroidClient.isConnected()){
                        EMLog.e(TAG,"MQTT不在线 去手动重连");
                        mqttConnect();
                    }else {
                        EMLog.e(TAG,"MQTT在线");
                    }
                }

                if (mBootState.equals("1")) {
                    if (mOperatingAnim != null) {
                        mIvTime.startAnimation(mOperatingAnim);
                    }
                    isSend = false;
                }else {
                    printn(getString(R.string.watch_off));

                }
//                mDevicesHomePresenter.chekEMCLogin(true);

                mDevicesHomePresenter.appSendCMD(MyApplication.sToken,
                        mAppAccount, mDeviceListBean.getImei(),
                        mMessage, mCommand, MyApplication.sAcountId);
                break;

            case R.id.ll_electronic_fence:
                if (mDeviceListBean == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }
                toActivity(GeoFenceListActivity.class);
                break;

            case R.id.ll_switch_baby:
                if (mDeviceList == null) {
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                    return;
                }

                if (mHandler != null) {
                    LogUtils.e(TAG, "onStop  mHandler != null)");
                    isSend = true;
                    mIsMessage = true;
                    time = 0;
                }
                SwitchBabyDialog switchBabyDialog = new SwitchBabyDialog(this, this, mDeviceList);
                switchBabyDialog.show();
                break;

            case R.id.add_zoom:
                changeCamera(CameraUpdateFactory.zoomIn(), null);
                break;

            case R.id.narrow_zoom:
                changeCamera(CameraUpdateFactory.zoomOut(), null);
                break;

            case R.id.ll_listen_story:
                toActivity(StoryActivity.class);
                break;

            case R.id.iv_notification_message:
                toActivity(MessageCenterActivity.class);
                break;

            default:
                break;
        }
    }


    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        mAMap.animateCamera(update, 300, callback);
    }


    /**
     * 加载列表失败的通知
     *
     * @param s
     */
    public void onError(ResponseInfoModel s) {
        dismissLoading();
    }


    /**
     * 获取设备列表成功的通知
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = data.getResult().getDeviceList();
        if (deviceList.size() <= 0) {
            //当前无其他设备可以用
            if (mRemoveDialog == null) {
                mRemoveDialog = new RemoveDialog(this, this);
            }
            mRemoveDialog.show();
            mRemoveDialog.setCanceledOnTouchOutside(false);
            mRemoveDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    LogUtils.e(TAG, "dismiss~~~");
                    if (!isDismiss) {
                        isDismiss = false;
                        dismiss();
                    }
                }
            });
            mRemoveDialog.initTitle(getString(R.string.add_other_administrators_to_remove));
            mRemoveDialog.setTvCancel(getString(R.string.exit_the_login));
            mRemoveDialog.setDetermine(getString(R.string.to_add));
            mGlobalvariable.edit().putBoolean("login", false).apply();
            return;
        }
        loadDataDevice(data);
    }


    /**
     * 取消的操作
     */
    @Override
    public void dismiss() {
        XmPlayerManager.release();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishActivityByAnimation(this);
    }


    /**
     * 去添加
     */
    @Override
    public void cancel() {
//        try {
//            mMqttConnectOptions.setAutomaticReconnect(false);
//            mMqttAndroidClient.disconnect();
//            mMqttAndroidClient.unregisterResources();
//            mMqttAndroidClient = null;
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
        LogUtils.e(TAG, "去添加");
        XmPlayerManager.release();
        isDismiss = true;
        Intent intent = new Intent(this, ScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("string", "sacn");
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishActivityByAnimation(this);
    }


    /**
     * 加载设备列表数据
     *
     * @param data
     */
    private void loadDataDevice(ResponseInfoModel data) {
        try {
            mDeviceList = data.getResult().getDeviceList();
            collections(mDeviceList);
            mListSize = mDeviceList.size();
            sNumber = MyApplication.sDeivceNumber;
            MyApplication.sListSize = mDeviceList.size();

            Log.d(TAG, "MyApplication.sDeivceNumber: " + MyApplication.sDeivceNumber);
            mDeviceListBean = mDeviceList.get(MyApplication.sDeivceNumber);
            MyApplication.sDeviceListBean = mDeviceListBean;
            //异步加载微聊数据
            mDevicesHomePresenter.queryDeviceStateByDeviceId(MyApplication.sToken,
                    MyApplication.sDeviceId);

            MyApplication.sDeviceId = mDeviceListBean.getDeviceId();
            Log.d(TAG, "size: " + mListSize);
            mSoftVersion = mDeviceListBean.getSoftVersion();
            if (mSoftVersion != null){
                mSoftVersions = Integer.parseInt(mSoftVersion.substring(6, 8));
            LogUtils.e(TAG,"getSoftVersion "+ mSoftVersions);
            }
            LogUtils.e(TAG,"getHardVersion"+mDeviceListBean.getHardVersion());
            //保存数据
            saveData(data);
            //设置主页的view
            setHomeView();
            //重置更新状态
            MyApplication.sInUpdata = false;
            //加载定位数据
            loadData();
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 保存数据
     */
    private void saveData(final ResponseInfoModel data) {
        if (mDeviceListBean == null) {
            return;
        }
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //保存数据到本地
                saveData2Local(mDeviceListBean.getImei() + MyApplication.sAcountId, data);
                //保存数据到内存
                saveData2Mem(mDeviceListBean.getImei() + MyApplication.sAcountId, data);
            }
        });
    }


    //排序
    private void collections(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList) {
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1,
                               ResponseInfoModel.ResultBean.DeviceListBean o2) {
                //降序排序
                return (o2.getIsAdmin() - o1.getIsAdmin());
            }
        });
    }


    /**
     * 加载定位数据
     */
    private void loadData() {
        if (mDeviceListBean == null) {
            return;
        }
        try {
            String result = (String) loadDataFromMem(mDeviceListBean.getImei());
            if (result == null) {
                //从内存加载,缓存时间一个月
                String fileResult = loadDataFromLocal(mDeviceListBean.getImei(),
                        Constant.PROTOCOL_TIMEOUT_MONTH);
                LogUtils.e(TAG, "当前时间" + System.currentTimeMillis() + "");
                if (fileResult == null) {
                    //本地没有,从网络加载
                    //请求获取定位位置
                    LogUtils.e(TAG, "从网络获取");
                    mDevicesHomePresenter.appSendCMD(MyApplication.sToken,
                            mAppAccount, mDeviceListBean.getImei(),
                            mMessage, mCommand, MyApplication.sAcountId);
                } else {
                    //本地
                    LogUtils.e(TAG, "从本地取");
                    parseData(fileResult);
                    //如果是从本地取,那就是程序退出从新进来,那么就去请求一次定位指令
                    mDevicesHomePresenter.appSendCMD(MyApplication.sToken,
                            mAppAccount, mDeviceListBean.getImei(),
                            mMessage, mCommand, MyApplication.sAcountId);
                }
            } else {
                //内存有
                LogUtils.e(TAG, "从内存拿");
                parseData(result);
                mDevicesHomePresenter.appSendCMD(MyApplication.sToken,
                        mAppAccount, mDeviceListBean.getImei(),
                        mMessage, mCommand, MyApplication.sAcountId);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    /**
     * @param result 接收的透传消息
     */
    private void parseData(String result) {
        if (mDeviceListBean == null) {
            return;
        }
        try {
            if (mGson == null){
                mGson = new Gson();
            }
            Log.i(TAG, "收到透传消息 :" + result + "~~ " + Thread.currentThread().getName() +mMqttAndroidClient.isConnected());
            if (mMqttAndroidClient != null && mMqttConnectOptions != null){
                    EMLog.e(TAG2,"收到环信透传消息 判断MQTT是否断线 isConnected = "+mMqttAndroidClient.isConnected());
                if (!mMqttAndroidClient.isConnected()){
                    EMLog.e(TAG2,"重新去连接 MQTT ~");
                    mMqttConnectOptions.setAutomaticReconnect(true);
                   mqttConnect();
                }
            LogUtils.e(TAG, "收到透传消息  isConnected:"+ mMqttAndroidClient.isConnected());
            }
            mParametersModel = mGson.fromJson(result, ParametersModel.class);
            mParameters = mParametersModel.getParameters();
            //定位透传指令
            chekCommand(mParametersModel.getCommand(), result, mParameters);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 判断透传类型
     *
     * @param command
     */
    private void chekCommand(int command, String result, ParametersModel.ParametersBean parameters) {
        switch (command) {
            case Constants.COMMAND10006:
                //定位透传指令
                int deviceId = MyApplication.sDeviceId;
                long locationId = mParameters.getDeviceId();
                LogUtils.e(TAG, "deviceId " + deviceId);
                LogUtils.e(TAG, "locationId " + locationId);
                if (deviceId != locationId) {
                    LogUtils.e(TAG, "收到的透传消息,不是当前该设备的,return");
                    return;
                }
                mLng = mParameters.getLng();
                mLat = mParameters.getLat();
                mAccuracy = mParameters.getAccuracy();
                mAddress = mParameters.getAddress();
                mType = mParameters.getType();
                mLocationTime = parameters.getLocationTime();
                //保存定位数据
                LogUtils.d(TAG, "保存定位数据~~");
                LogUtils.d(TAG, "time~~ " + time);
                saveLocationData(result);
                setView(mAccuracy, mAddress, mType,
                        mLocationTime, mLat, mLng);
                mIsMessage = true;
                time = 0;

                break;

            case Constants.COMMAND10009:
                //管理员解绑设备绑定成员通知被解绑人指令
                LogUtils.e(TAG, "管理员解绑设备绑定成员通知被解绑人指令 " + result);
                mDevicesHomePresenter.receivesSolutionMessage(result, parameters.getDeviceId() + "",
                        parameters.getNickName());
                break;

            case Constants.COMMAND10010:
                //管理员移交管理员权限，并解绑自己，通知新管理员指令
                mDevicesHomePresenter.receivesAdmin(result, parameters);
                break;

            case Constants.COMMAND10011:
                //管理员给设备添加绑定成员通知
                LogUtils.e(TAG, result);
                mDevicesHomePresenter.membersNotification(result, parameters);
                break;

            case Constants.COMMAND100023:
                //手表传来的电量信息
                try {
                    LogUtils.d(TAG, result);
                    if (MyApplication.sDeviceId != mParameters.getDeviceId()) {
                        LogUtils.e(TAG, "收到的透传消息,不是当前该设备的,return");
                        return;
                    }
                    mBootState = parameters.getBootState();
                    LogUtils.d(TAG, "透传收到的开关机状态 " + mBootState);
                    mDevicesHomePresenter.chekPower(Integer.parseInt(parameters.getPowerLevel()));
                    if (mBootState.equals("0")) {
                        mBootState = "-1";
                        mDevicesHomePresenter.chekPower(Integer.parseInt(mBootState));
                        mBootState = "0";
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception " + e.getMessage());
                }

                break;

            case Constants.COMMAND10027:
                LogUtils.e(TAG2,"收到APP接收设备上报的wifi数据指令");
                EventBus.getDefault().post(mModel);
                break;

            case Constants.COMMAND10021:
                LogUtils.e(TAG2,"收到话费查询指令");
                EventBus.getDefault().post(mModel);
                break;

            case Constants.COMMAND10036:
                //设备环信已注册成功指令
                LogUtils.e(TAG,"设备环信已注册成功指令" + parameters.getImGroupId());
                LogUtils.e(TAG,"设备环信已注册成功指令" + parameters.getDeviceId());
                if (parameters.getDeviceId() == MyApplication.sDeviceId){
                mDeviceListBean.setGroupId(parameters.getImGroupId());
                mGlobalvariable.edit().putLong("groupId",parameters.getImGroupId());
                }
                break;

            case Constants.COMMAND10037:
                //重新登录指令
                EMLog.e(TAG2,"重新登录指令");
                if (parameters.getDeviceId() == MyApplication.sDeviceId){
                mDevicesHomePresenter.exit();
                }
                break;

            case Constants.COMMAND10039:
                //重新登录指令
                EMLog.e(TAG2,"设备信息更新指令,客户端重新拉取数据");
                if (parameters.getDeviceId() == MyApplication.sDeviceId){
                    mDevicesHomePresenter.mIsNotice = true;
                    mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                }
                break;
        }
    }


    /**
     * 从新选择设备
     */
    @Override
    public void selectEquipment() {
        toActivity(SwitchBabyActivity.class, "again");
    }


    /**
     * 设备列表数据获取成功设置View
     */
    private void setHomeView() {
        if (mDeviceListBean == null) {
            return;
        }
        try {
            int deivceNumber = mGlobalvariable.getInt("deivceNumber", 0);
            Log.d(TAG, "deivceNumber: " + deivceNumber);
            mGlobalvariable.edit().putInt("deivceNumber", MyApplication.sDeivceNumber).apply();

            mIvGender.setImageResource(mDeviceListBean.getGender() == 1 ?
                    R.mipmap.male : R.mipmap.female);
            mImgUrl = mDeviceListBean.getImgUrl();
            //从本地获取图片
            if (mImgUrl != null) {
//                LogUtils.d(TAG,"path " + getCacheFile(mDeviceListBean.getDeviceId()+"imgurl"+".jpg" ).getAbsolutePath());
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 2;
//                sBitmap = BitmapFactory.decodeFile(getCacheFile(mDeviceListBean.getDeviceId()+"imgurl").getAbsolutePath(),options);
//                LogUtils.d(TAG,"path " + sBitmap);
                if (mImgUrl != null && sBitmap == null) {
                    ThreadUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            sBitmap = UIUtils.returnBitmap(mImgUrl);
                            if (sBitmap != null){
                            String path = saveImage(sBitmap, mDeviceListBean.getDeviceId() + "imgurl");
                            LogUtils.d(TAG, "sBitmap路径  = " + path);
                            }
                        }
                    });
                } else {
                    LogUtils.d(TAG, "setHomeView sBitmap != null");
                }
            }

            Log.d(TAG, "getImgUrl: " + mImgUrl);

            if (mImgUrl != null) {
                Glide.with(this).load(mImgUrl).into(mNavIcon);
                Glide.with(this).load(mImgUrl).into(mIvIcon);
            } else {
                mNavIcon.setImageResource(mDeviceListBean.getGender() == 1 ?
                        R.drawable.a : R.drawable.b);
                mIvIcon.setImageResource(mDeviceListBean.getGender() == 1 ?
                        R.drawable.a : R.drawable.b);
            }

            mTvAge.setText(TimeUtil.getAge(mFormat.parse(mDeviceListBean.getBirthday()))
                    + getString(R.string.age));
            mTvName.setText(mDeviceListBean.getNickName());
            mTvNickName.setText(mDeviceListBean.getNickName());
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    /**
     * 环信透传消息回调接口
     */
    public EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            if (messages == null) {
                return;
            }
            if (mMqttAndroidClient != null &&  mMqttConnectOptions != null){
                EMLog.e(TAG2,"收到环信普通消息 判断MQTT是否断线 isConnected = "+mMqttAndroidClient.isConnected());
                if (!mMqttAndroidClient.isConnected()){
                    EMLog.e(TAG2,"重新去连接 MQTT ~");
                        mMqttConnectOptions.setAutomaticReconnect(true);
                        mqttConnect();
                }
                LogUtils.e(TAG, "收到透传消息  isConnected:"+ mMqttAndroidClient.isConnected());
            }
            //普通消息
            mDevicesHomePresenter.receivedCommonMessage(messages,null);

        }

        @Override
        public void onCmdMessageReceived(final List<EMMessage> messages) {
            try {
                //收到透传消息
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        EMLog.d(TAG, "收到透传消息: " + messages.size() +"isConnected "+ mMqttAndroidClient.isConnected());
                        if (messages.size() > 0) {
                            for (int i = 0; i < messages.size(); i++) {
                                EMMessage cmdMessage = messages.get(i);
                                EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                                String action = body.action();
                                parseData(action);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                LogUtils.e(TAG, "透传异常" + e.getMessage());
            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    /**
     * 本地推送
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return mEaseUI.getNotifier();
    }


    /**
     * 保存定位数据
     */
    private void saveLocationData(final String action) {
        if (mDeviceListBean == null) {
            return;
        }
        try {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    //保存到本地
                    saveData2Local(mDeviceListBean.getImei(), action);
                    //保存数据到内存中
                    saveData2Mem(mDeviceListBean.getImei(), action);

                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, "保存定位数据异常" + e.getMessage());
        }
    }


    /**
     * 得到透传数据,展示信息
     */
    private void setView(String accuracy, String address, int locationType, String locationTime,
                         double lat, double lng) {
            mLocationTimes = TimeUtil.getMs(locationTime);
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - mLocationTimes;

            LogUtils.d(TAG, "getAccuracy: " + accuracy);
            LogUtils.d(TAG, "当前毫秒值 : " + currentTime);
            LogUtils.d(TAG, "时间差 : " + timeDifference);
            LogUtils.d(TAG, "locationTimes : " + mLocationTimes);
            LogUtils.d(TAG, "address: " + address);
            LogUtils.d(TAG, "locationType: " + locationType);
            LogUtils.d(TAG, "locationTime: " + locationTime);
            LogUtils.d(TAG, "lat: " + lat);
            LogUtils.d(TAG, "lng: " + lng);

            if (!accuracy.equals("-1")) {
                mTvAccuracy.setVisibility(View.VISIBLE);
                mTvAccuracy.setText("  精度(" + accuracy + "米)");
            }
            mTvLotionTime.setVisibility(View.VISIBLE);
            mIvType.setVisibility(View.VISIBLE);
            mIvType.setImageResource(mDevicesHomePresenter.chekType(locationType));
            mTvLotionTime.setText(locationTime + "");
            mLocationTv.setText(address);
            initLocationView(lat, lng, locationTime);
            //转换坐标
    }


    /**
     * 初始化定位view
     */
    private void initLocationView(final double lat, final double lng, String locationTime) {
        mLocationInforPopupWindow = LayoutInflater.from(DevicesHomeActivity.this)
                .inflate(R.layout.popuwindow_location_information, null);
        mCivIcon = (CircleImageView) mLocationInforPopupWindow
                .findViewById(R.id.civ_icon);
        mTvMs = (TextView) mLocationInforPopupWindow.findViewById(R.id.tv_ms);
        mIvOval = (ImageView) mLocationInforPopupWindow.findViewById(R.id.iv_oval);

        mLocationTimes = TimeUtil.getMs(locationTime);
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - mLocationTimes;

        if (timeDifference >= 1000) {
            mTvMs.setText(TimeUtil.formatTime(timeDifference));
        } else {
            mTvMs.setText(getString(R.string.one_ss));
        }

        if (sBitmap != null) {
            LogUtils.e(TAG, "sBitmap != null");
            mCivIcon.setImageBitmap(sBitmap);
            mapSetView(lat, lng);
        } else if (mImgUrl != null) {
            LogUtils.e(TAG, "mImgUrl != null");

            Glide.with(this).load(mImgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    sBitmap = bitmap;
                    mCivIcon.setImageBitmap(bitmap);
                    mapSetView(lat, lng);
                }
            });

        } else {
            LogUtils.e(TAG,"mImgUrl == null");
            mCivIcon.setImageResource(mDeviceListBean.getGender() == 1 ?
                    R.drawable.a : R.drawable.b);
            mapSetView(lat, lng);
        }
    }


    private void mapSetView(double lat, double lng) {
        mFromView = BitmapDescriptorFactory.fromView(mLocationInforPopupWindow);

        mDevicesHomePresenter.DrawableCarInformation(lat, lng, mFromView);
    }


    /**
     * 同意添加联系人
     */
    @Override
    public void agreed() {
        LogUtils.e(TAG, "同意添加联系人");
        mDevicesHomePresenter.setRefused(MyApplication.sToken, MyApplication.sUserAcountId,
                MyApplication.sUserdeviceId, 1);
    }


    /**
     * 拒绝添加联系人
     */
    @Override
    public void refused() {
        LogUtils.e(TAG, "拒绝添加联系人");
        mDevicesHomePresenter.setRefused(MyApplication.sToken, MyApplication.sUserAcountId,
                MyApplication.sUserdeviceId, 0);
    }

    @Override
    public void refusedAgreed() {
        mDevicesHomePresenter.replyBandDeviceRequest(MyApplication.sToken, "N", mAppAccount);
        LogUtils.d(TAG, "拒绝邀请");
    }


    @Override
    public void agreedAdd() {
        toActivity(104, SelectRelationActivity.class, "Y", mDevicesHomePresenter.mDeviceId);
        LogUtils.d(TAG, "同意邀请");
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //定义一个广播
    public class SettingActiviy_Broad extends BroadcastReceiver {

        public void onReceive(Context arg0, Intent intent) {
            //接收发送过来的广播内容
            LogUtils.e(TAG, "收到广播内容");
            int closeAll = intent.getIntExtra("closeAll", 0);
            if (closeAll == 1) {
                //销毁Activity
                LogUtils.e(TAG, "收到销毁广播");
                finish();
            }
        }
    }




    @Override
    public void onResume() {
        try {
            //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
            mMapView.onResume();
            //新消息显示
            if (sMessageSize > 0) {
                mTvNewMessage.setVisibility(View.VISIBLE);
            } else {
                mTvNewMessage.setVisibility(View.GONE);
            }
            Log.d(TAG, "onResume: +sNumber " + sNumber);
            Log.d(TAG, "onResume:+MyApplication.sDeivceNumber " + MyApplication.sDeivceNumber);
            mIsFirstLoc = false;
            Log.d(TAG, "onResume: " + MyApplication.sInUpdata);
//            boolean sInUpdata = (boolean) SharedPreferencesUtils.getParam(this,"sInUpdata",false);

            if (MyApplication.sInUpdata) {
                mDevicesHomePresenter.moveMarker();
            }
//            else if (sInUpdata){
//                mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
//                SharedPreferencesUtils.setParam(this,"sInUpdata",false);
//            }
            else if (mDeviceListBean != null && mAddress != null && mLng != 0) {
                LogUtils.d(TAG, "onResume " + mLocationTime);
                setView(mAccuracy, mAddress, mType, mLocationTime, mLat, mLng);
            }

            if (!MyApplication.sInUpdata) {
                if (isDeviceList) {
                    if (mDeviceListBean == null) {
                        mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                        isDeviceList = false;
                    }
                }
            }

        } catch (Exception e) {
            LogUtils.e(TAG, "onResume异常" + e.getMessage());

        }
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        LogUtils.d(TAG, "onPause " + mLocationTime);
        mMapView.onPause();
        LogUtils.e(TAG, "onPause");
        if (mHandler != null) {
            LogUtils.e(TAG, "onStop  mHandler != null)");
            isSend = true;
            mIsMessage = true;
            time = 0;
        }
        if (mToast != null) {
            mToast.cancel();
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e(TAG, "onStop  ");

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG, "onRestart");
//        mDevicesHomePresenter.chekEMCLogin(false);
    }


    @Override
    public void onDestroy() {
        LogUtils.e(TAG, "onDestroy  ");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mAMap.clear();
        sBitmap = null;
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        if (mSettingActiviy_Broad != null) {
            unregisterReceiver(mSettingActiviy_Broad);//注销广播

        }
        if (mMqttAndroidClient != null){
            try {
                mMqttConnectOptions.setAutomaticReconnect(false);
                mMqttAndroidClient.disconnect();
                mMqttAndroidClient.unregisterResources();
                mMqttAndroidClient.clearAbortBroadcast();
                LogUtils.e("MQTT","mMqttAndroidClient 退出成功");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }


    /**
     * 立即定位指令发送成功通知
     */
    public void cmdSuccess(ResponseInfoModel.ResultBean result) {
        dismissLoading();
        if (result != null && result.getAddress() != null) {
            mAddress = result.getAddress();
            mAccuracy = result.getAccuracy() + "";
            mType = result.getType();
            mLocationTime = result.getLocationTime();
            LogUtils.d(TAG, "立即定位指令发送成功通知 " + result);
            LogUtils.d(TAG, "mLocationTime " + mLocationTime);
            LogUtils.e(TAG, "立即定位指令发送成功通知 " + result.getLng());
            LogUtils.e(TAG, "立即定位指令发送成功通知 " + mAddress);
            mLat = result.getLat();
            mLng = result.getLng();
            mLatLng = new LatLng(mLat, mLng);
            if (mBootState.equals("1")) {
                if ((System.currentTimeMillis() - exitTime) > 3500){
                    upTime();
                }else {
                    EMLog.e(TAG2,"小于 3.5秒");
                }
            }
            setView(mAccuracy, mAddress, mType, mLocationTime, mLat, mLng);
        }
    }


    /**
     * 透传指令发送失败通知
     *
     * @param resultMsg
     */
    public void onCMDError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
        if (mOperatingAnim != null) {
            mIvTime.clearAnimation();
        }
    }


    /**
     * 有网络的通知
     */
    @Override
    protected void theNetwork() {
        Log.d(TAG, "theNetwork: " + "有网络");
        if (mIsNetwork) {
            if (mDeviceListBean == null) {
                mDevicesHomePresenter.getAcountDeivceList(mToken, mAcountId);
                printn(getString(R.string.are_connected));
                mIsNetwork = false;
            }
        }

    }


    /**
     * 无网络的通知
     */
    @Override
    protected void noNetwork() {
        Log.d(TAG, "noNetwork: " + "没有网络");
        mIsNetwork = true;

        dismissAnimation();

    }


    public void dismissAnimation() {
        if (mHandler != null) {
            LogUtils.e(TAG, "noNetwork  mHandler != null)");
            isSend = true;
            mIsMessage = true;
            time = 0;
        }

        if (mOperatingAnim != null) {
            mIvTime.clearAnimation();
        }
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        try {
            if (mDevicesHomePresenter.mAcountDeivceList != null) {
                mDevicesHomePresenter.mAcountDeivceList.cancel();
            }
            if (mDevicesHomePresenter.mAppSendCMD != null) {
                mDevicesHomePresenter.mAppSendCMD.cancel();
            }
            if (mDevicesHomePresenter.mGroupMemberListAll != null) {
                mDevicesHomePresenter.mGroupMemberListAll.cancel();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 微聊数据初始化成功通知
     *
     * @param body
     */
    public void memberListSuccess(ResponseInfoModel body) {
        ResponseInfoModel.ResultBean result = body.getResult();
        String powerLevel = result.getPowerLevel();
        mUpdatedFlag = result.getUpdatedFlag();
        mBootState = result.getBootState();
        mStrangeCallSwitch = result.getStrangeCallSwitch();
        mDevicesHomePresenter.chekPower(Integer.parseInt(powerLevel));
        if (mBootState.equals("0")) {
            mBootState = "-1";
            mDevicesHomePresenter.chekPower(Integer.parseInt(mBootState));
            mBootState = "0";
        }

    }


    /**
     * 动画的监听
     *
     * @param animation
     */
    @Override
    public void onAnimationStart(Animation animation) {
        LogUtils.e(TAG, "开始动画中" + Thread.currentThread().getName());
        isSend = false;
    }


    @Override
    public void onAnimationEnd(Animation animation) {
        LogUtils.e(TAG, "结束动画");
    }


    @Override
    public void onAnimationRepeat(Animation animation) {
        LogUtils.e(TAG, "正在动画中");
    }


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MINUS:
                    LogUtils.e(TAG, time + " TIME_MINUS  time");
                    LogUtils.e(TAG, isSend + "TIME_MINUS  isSend");
                    LogUtils.e(TAG, mIsMessage + "TIME_MINUS  mIsMessage");
                    //超过了60秒还没显示定位位置提示用户当前信号弱
                    if (time <= 0 && !mIsMessage) {
                        printn(getString(R.string.the_current_signal_is_weak_please_try_again_later));
                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        isSend = true;
                        time = 60;
                        if (mOperatingAnim != null) {
                            mIvTime.clearAnimation();
                        }
                    }else {
                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        isSend = true;
                        time = 60;
                        if (mOperatingAnim != null) {
                            mIvTime.clearAnimation();
                        }
                    }
                    break;

                case MQTT_CODE:
                    LogUtils.e(TAG,"MQTT handler 收到的消息");
                    MqttMessage message = (MqttMessage) msg.obj;
                    String messages = new String(message.getPayload());
                    if (messages == null){
                        return;
                    }
                    LogUtils.e(TAG2,"Message: " +  Thread.currentThread().getName());
                    if (mGson == null) {
                        mGson = new Gson();
                    }
                    if (messages.indexOf("command") != -1){
                        LogUtils.e(TAG2,"MQTT收到 command 消息");
                        EMLog.e(TAG2,"MQTT收到 command 消息" + messages);
                        mModel = mGson.fromJson(messages, ParametersModel.class);
                        mParameters = mModel.getParameters();
                        //定位透传指令
                        chekCommand(mModel.getCommand(), messages, mParameters);
                    }else {
                        LogUtils.e(TAG2,"MQTT收到 TXT消息");
                        EMLog.e(TAG2,"MQTT收到 TXT消息" +messages);
                        MQTTModel mqttModel = mGson.fromJson(messages, MQTTModel.class);
                        mDevicesHomePresenter.receivedCommonMessage(null,mqttModel);
                    }


                    break;
            }

        }
    };


    public int time = 60;


    public void upTime() {
        //记录调用的时间
        exitTime = System.currentTimeMillis();
        isSend = false;
        mIsMessage = false;
        initMarker();
        LogUtils.e(TAG, time + "time~~~~~~~~~~~~~~~~~~~");
        time = 60;
        new Thread() {
            @Override
            public void run() {
                for (; time > 0; time--) {
                    if (mMarker != null) {
                        mDevicesHomePresenter.playHeartbeatAnimation(mMarker);
                    }
                    SystemClock.sleep(1000);

                    LogUtils.e(TAG, time + "  time-------");
                    if (time <= 0) {
                        mHandler.sendEmptyMessage(TIME_MINUS);
                        return;
                    }
                }
            }
        }.start();
    }


    /**
     * 初始化marker
     */
    private void initMarker() {
        mInforWindow = LayoutInflater.from(mContext)
                .inflate(R.layout.popuwindow_information, null);
        mBitmapDescriptor = BitmapDescriptorFactory.fromView(mInforWindow);
        mMarker = mAMap.addMarker(new MarkerOptions().position(mLatLng)
                .anchor(0.5f, 0.5f).alpha(0.5f).icon(mBitmapDescriptor));
    }


    private void initMQTT() throws Exception {
        EMLog.e(TAG2,"初始化 MQTT");
        mMqttAndroidClient = new MqttAndroidClient(this,Constants.serverUri,Constants.mqttclientId);
        mMqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                EMLog.d(TAG2, "MQTT 连接成功:  connected = " +mMqttAndroidClient.isConnected());
                EMLog.e(TAG2,"connectComplete " + reconnect  + serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                if (cause == null){
                    return;
                }
                try {
                String message = cause.toString();
                    EMLog.e(TAG,"connectionLost 异常" + message +  mMqttAndroidClient.isConnected());
                    if (message != null){
                        if (message.indexOf("32109") != -1){
                            if (!mIsToken){
                                    mIsToken = true;
                                    mDevicesHomePresenter.verificationToken(MyApplication.sToken);
                            }
                        }
                    }

                }catch (Exception e){
                    EMLog.e(TAG,"MQTTconnectionLost "+e.getMessage());
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (mMqttAndroidClient != null){
                    LogUtils.e(TAG,"topic" + topic + new String(message.getPayload()));
                    Message message1 = new Message();
                    message1.what = MQTT_CODE;
                    message1.obj = message;
                    mHandler.sendMessage(message1);
                }
                LogUtils.e(TAG2,"messageArrived " + new String(message.getPayload()));
                EMLog.e(TAG2,"messageArrived " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                EMLog.e(TAG2,"deliveryComplete ");
            }
        });
        boolean connected = mMqttAndroidClient.isConnected();
        LogUtils.e(TAG2,"connected "+connected);
        //设备认证

        Map<String, String> params = new HashMap<String, String>();
        params.put("productKey", mProductKey); //这个是对应用户在控制台注册的 设备productkey
        params.put("deviceName", mDeviceName); //这个是对应用户在控制台注册的 设备name
        params.put("clientId", mDeviceName);
        params.put("timestamp", mTimestamp);
        //客户端ID格式:
        LogUtils.e(TAG2,"initMQTT  params"+String.valueOf(params));
        EMLog.e(TAG2,"initMQTT  params"+String.valueOf(params));
        //mqtt用户名格式
        mMqttUsername = mDeviceName+"&" + mProductKey;
        //签名
        mMqttPassword = SignUtil.sign(params, mDeviceSecret, "hmacsha1");
        //连接mqtt的代码片段
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mSocketFactory = SignUtil.createSSLSocket();
        }

        getOptions(true);
    }


    public MqttConnectOptions getOptions(boolean isAutomaticReconnect) {
        mMqttConnectOptions = new MqttConnectOptions();
        //设置证书
        if (mSocketFactory != null){
        mMqttConnectOptions.setSocketFactory(mSocketFactory);
        }
        //设置是否自动重连
        mMqttConnectOptions.setAutomaticReconnect(isAutomaticReconnect);
        //如果是true 那么清理所有离线消息，即qos1 或者 2的所有未接收内容
        mMqttConnectOptions.setCleanSession(false);
        mMqttConnectOptions.setServerURIs(new String[] { Constants.serverUri });
        mMqttConnectOptions.setUserName(mMqttUsername);
        mMqttConnectOptions.setPassword(mMqttPassword.toCharArray());
        mMqttConnectOptions.setKeepAliveInterval(90);//心跳时间 建议60s以上
        return mMqttConnectOptions;
    }


    public void mqttConnect() {
        try {
            mMqttAndroidClient.connect(mMqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mMqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    boolean connected = mMqttAndroidClient.isConnected();
                    LogUtils.e(TAG2,"connected onSuccess"+connected);
                    EMLog.d(TAG2, "MQTT 连接成功:  connected = " + connected);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    boolean connected = mMqttAndroidClient.isConnected();
                    try {
                        if (mMqttAndroidClient != null &&  mMqttConnectOptions != null){
                            EMLog.e(TAG2,"重新连接 MQTT");
                            mMqttConnectOptions.setAutomaticReconnect(true);
                            mMqttAndroidClient.connect();
                        }
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    LogUtils.e(TAG2,"connected  onFailure "+connected);
                    EMLog.e(TAG2,"Failed to connect to: " + Constants.serverUri +"~~"  +exception);
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    public void subscribeToTopic(){
        LogUtils.e(TAG2,"subscribeToTopic" + Constants.subTopic);
        try {
            mMqttAndroidClient.subscribe(Constants.subTopic, 1, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, final MqttMessage message) throws Exception {
                    LogUtils.e(TAG2,"messageArrived 2222" + new String(message.getPayload()));
                    if (mMqttAndroidClient != null){
                        LogUtils.e(TAG,"topic" + topic + new String(message.getPayload()));
                        Message message1 = new Message();
                        message1.what = MQTT_CODE;
                        message1.obj = message;
                        mHandler.sendMessage(message1);
                    }

                }
            });

        } catch (MqttException ex){
            EMLog.e(TAG2,"Exception whilst subscribing");
            ex.printStackTrace();
        }
    }






}
