package com.loybin.baidumap.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.LatLanModel;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;


import butterknife.BindView;
import butterknife.OnClick;

import static com.loybin.baidumap.ui.activity.DevicesHomeActivity.sBitmap;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/17 上午10:54
 * 描   述: SOS地图位置显示 view
 */
public class SOSMapActivity extends BaseActivity {
    private static final String TAG = "SOSMapActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.amap_view)
    MapView mMapView;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private Gson mGson;
    private String mMsgAttr;
    private Marker mMarker;
    private LatLanModel mLatLanModel;
    private View mLocationInforPopupWindow;
    private BitmapDescriptor mFromView;
    private CircleImageView mCivIcon;
    private TextView mTvMs;
    private ImageView mIvOval;
    private TextView mTvTime;
    private TextView mTvAddress;
    private MarkerOptions markerOption;
    private LatLng mSourceLatLng;
    private Marker marker;
    private StringBuilder mAd;
    private String mImgUrl;
    private int mGender;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_sos_map;
    }


    @Override
    protected void init() {
        mMsgAttr = getIntent().getStringExtra(STRING);

        if (mGson == null) {
            mGson = new Gson();
        }
        mLatLanModel = mGson.fromJson(mMsgAttr, LatLanModel.class);
        String address = mLatLanModel.getAddress();
        String s = address + "发起了求助";
        String[] lu = s.split("");
        LogUtils.d(TAG, "lu " + lu.length);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lu.length; i++) {
            if (lu.length >= 36) {
                if (i < lu.length / 3 && i < 12) {
                    mAd = stringBuilder.append(lu[i]);
                } else if (i == 12) {
                    mAd = stringBuilder.append("\n");
                } else if (i == 24) {
                    mAd = stringBuilder.append("\n");
                } else {
                    mAd = stringBuilder.append(lu[i]);
                }
            } else {
                if (i < lu.length / 2 && i < 12) {
                    mAd = stringBuilder.append(lu[i]);
                } else if (i == 12) {
                    mAd = stringBuilder.append("\n");
                } else {
                    mAd = stringBuilder.append(lu[i]);
                }
            }
        }
        mImgUrl = (String) SharedPreferencesUtils.getParam(this, "imgUrl" + mLatLanModel.getDeviceId(), "");
        mGender = (int) SharedPreferencesUtils.getParam(this, "gender" + mLatLanModel.getDeviceId(), 1);
        LogUtils.e(TAG, "mImgUrl " + mImgUrl);
        LogUtils.e(TAG, "mGender " + mGender);
        LogUtils.e(TAG, "mMsgAttr " + mMsgAttr);
        LogUtils.e(TAG, "getAddress " + mLatLanModel.getAddress());
        LogUtils.e(TAG, "getAddress " + mLatLanModel.getLocationTime());
        LogUtils.e(TAG, "getDeviceId " + mLatLanModel.getDeviceId());
        LogUtils.e(TAG, "getLat " + mLatLanModel.getLat());
        LogUtils.e(TAG, "getLng " + mLatLanModel.getLng());
        mSourceLatLng = new LatLng(mLatLanModel.getLat(), mLatLanModel.getLng());
        initView();

//        mAMap.setInfoWindowAdapter();

    }


    private void initView() {
        mTvTitle.setText(getString(R.string.sos_title));
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mUiSettings.setTiltGesturesEnabled(false);
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
        }

        initLocationView();

    }


    /**
     * 当前设备标记与信息
     */
    public void DrawableCarInformation(double lat, double lng, BitmapDescriptor bitmapDescriptor) {
        if (mMarker == null) {
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(mSourceLatLng
                    , 17, 0, 0)));
            mMarker = mAMap.addMarker(new MarkerOptions().
                    position(mSourceLatLng).icon(bitmapDescriptor).title(mLatLanModel.getLocationTime())
                    .snippet(mAd.toString())
                    .draggable(true)
                    .setInfoWindowOffset(0, 50)
                    .anchor(0.5f, 0.78f));
            mMarker.setToTop();
            mMarker.showInfoWindow();
        } else {
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(mSourceLatLng
                    , 17, 0, 0)));
            mMarker.setPosition(mSourceLatLng);
            mMarker.setIcon(bitmapDescriptor);
            mMarker.setToTop();
        }
    }


    /**
     * 初始化定位view
     */
    private void initLocationView() {
        mLocationInforPopupWindow = LayoutInflater.from(SOSMapActivity.this)
                .inflate(R.layout.popuwindow_location_sos, null);
        mCivIcon = (CircleImageView) mLocationInforPopupWindow
                .findViewById(R.id.civ_icon);
        mTvMs = (TextView) mLocationInforPopupWindow.findViewById(R.id.tv_ms);
        mIvOval = (ImageView) mLocationInforPopupWindow.findViewById(R.id.iv_oval);

//        mTvTime = (TextView) mLocationInforPopupWindow.findViewById(R.id.tv_time);
//        mTvAddress = (TextView) mLocationInforPopupWindow.findViewById(R.id.tv_address);
//
//        mTvAddress.setText(mLatLanModel.getAddress()+"发起了求助");
//        mTvTime.setText(mLatLanModel.getLocationTime());

        try {
            if (MyApplication.sDeviceId == Integer.parseInt(mLatLanModel.getDeviceId())) {
                LogUtils.d(TAG, "当前设备的sos消息");
                if (sBitmap != null) {
                    LogUtils.e(TAG, "sBitmap != null");
                    mCivIcon.setImageBitmap(sBitmap);
                    mapSetView();
                } else {
                    mCivIcon.setImageResource(MyApplication.sDeviceListBean.getGender() == 1 ?
                            R.drawable.a : R.drawable.b);
                    mapSetView();
                }
            } else {
                LogUtils.d(TAG, "不是当前设备的sos消息");
                if (mImgUrl != null && !mImgUrl.equals("")) {
                    Glide.with(this).load(mImgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            mCivIcon.setImageBitmap(bitmap);
                            mapSetView();
                        }
                    });


                } else {
                    if (mGender == 1) {
                        mCivIcon.setImageResource(R.drawable.a);
                        mapSetView();
                    } else {
                        mCivIcon.setImageResource(R.drawable.b);
                        mapSetView();
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 地图显示图片
     */
    private void mapSetView() {
        mFromView = BitmapDescriptorFactory.fromView(mLocationInforPopupWindow);
        DrawableCarInformation(mLatLanModel.getLat(), mLatLanModel.getLng(), mFromView);
    }


    /**
     * 移到latLng位置
     *
     * @param cameraUpdate
     */
    private void changeCamera(CameraUpdate cameraUpdate) {
        mAMap.animateCamera(cameraUpdate, 300, null);
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(mSourceLatLng)
                .title("标题")
                .snippet("详细信息")
                .draggable(true)
                .anchor(0.1f, 0.78f);
        marker = mAMap.addMarker(markerOption);
        marker.showInfoWindow();
    }


    @Override
    protected void dismissNewok() {

    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return true;
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishActivityByAnimation(this);
    }


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        LogUtils.e(TAG, "onDestroy  ");
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        super.onDestroy();
    }

}
