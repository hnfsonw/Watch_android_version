package com.loybin.baidumap.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.GeoFenceModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.ElectronicFencePresenter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 电子围栏view
 */
public class ElectronicFenceActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.tv_address)
    TextView mTvAddress;


    @BindView(R.id.seekbar_radius)
    SeekBar mSeekbarRadius;


    @BindView(R.id.amap_view)
    MapView mMapView;

    @BindView(R.id.tv_the_scope_of)
    TextView mTvScopeOf;

    @BindView(R.id.btn_next)
    Button mBtnNext;

    @BindView(R.id.iv_location)
    ImageView mIvLocation;

    @BindView(R.id.iv_pregess)
    ImageView mProgressBar;

    @BindView(R.id.add_zoom)
    TextView mAddZoom;

    @BindView(R.id.narrow_zoom)
    TextView mNarrowZoom;


    private Context mContext;
    private static final String TAG = "ElectronicFenceActivity";
    private GeoFenceModel mGeoFenceModel;
    private String mFromMark = "";
    private String mCity;
    private int mFenceId;
    private String mName;
    private String mLng;
    private String mLat;
    private String mDesc;
    private boolean mInNowork = false;
    private AnimationDrawable mDrawable;
    private View mLocationInforPopupWindow;
    private ResponseInfoModel.ResultBean mResultBean;
    public AMap mAMap;
    private UiSettings mUiSettings;
    private Marker mScreenMarker;
    private LatLng mLatLng;
    private GeocodeSearch mGeocoderSearch;
    private Circle mCircle;
    private boolean isAnimateCamera = true;
    private ElectronicFencePresenter mElectronicFencePresenter;
    private CircleImageView mCivIcon;
    private Point mScreenPosition;
    private String mRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_electronic_fence;
    }


    @Override
    protected void init() {
        ButterKnife.bind(this);
        mContext = this;
        mGeoFenceModel = new GeoFenceModel();
        mFromMark = getIntent().getStringExtra("FromMark");
        mFenceId = getIntent().getIntExtra("fenceId", 0);
        mName = getIntent().getStringExtra("name");
        mLng = getIntent().getStringExtra("lng");
        mLat = getIntent().getStringExtra("lat");
        mDesc = getIntent().getStringExtra("desc");
        mRadius = getIntent().getStringExtra("radius");
        Log.d(TAG, "mFromMark: " + mFromMark);
        Log.d(TAG, "mFenceId: " + mFenceId);
        Log.d(TAG, "mName: " + mName);
        Log.d(TAG, "mLng:  " + mLng);
        Log.d(TAG, "mLat:  " + mLat);
        Log.d(TAG, "mDesc:  " + mDesc);
        if (mElectronicFencePresenter == null) {
            mElectronicFencePresenter = new ElectronicFencePresenter(this, this);
        }

        initView();
        initListener();
        initData();
    }


    private void initView() {
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(getResources().getString(R.string.Geofence_Title));
        mIvConfirm.setVisibility(View.VISIBLE);

        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mUiSettings.setTiltGesturesEnabled(false);
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
        }

        mGeocoderSearch = new GeocodeSearch(this);

    }


    private void initListener() {
        mIvBack.setOnClickListener(this);
        mAddZoom.setOnClickListener(this);
        mNarrowZoom.setOnClickListener(this);
        mAMap.setOnMapLoadedListener(this);
        // 设置可视范围变化时的回调的接口方法
        mAMap.setOnCameraChangeListener(this);

        mGeocoderSearch.setOnGeocodeSearchListener(this);

        //设置围栏范围 围栏半径
        mSeekbarRadius.setOnSeekBarChangeListener(this);

        mBtnNext.setOnClickListener(this);
        mIvConfirm.setOnClickListener(this);


    }


    private void initData() {
        Log.i(TAG, "FormMark=" + mFromMark);
        //添加
        if ("Add".equals(mFromMark)) {
            mResultBean = MyApplication.sResult;
            if (mResultBean != null) {
                mLatLng = new LatLng(mResultBean.getLat(), mResultBean.getLng());
                mGeoFenceModel.Latitude = mLatLng.latitude;
                mGeoFenceModel.Longitude = mLatLng.longitude;
                LogUtils.e(TAG, "mDesLatLng.longitude " + mLatLng.latitude);
                LogUtils.e(TAG, "mDesLatLng.latitude " + mLatLng.longitude);
                String address = mResultBean.getAddress();
                mTvAddress.setText(address);
            }

            mGeoFenceModel.Radius = 200;
            mTvScopeOf.setText(getString(R.string.security_zone_radius) + (200) + getResources().getString(R.string.Geofence_Meter));
            //编辑
        } else if ("Edit".equals(mFromMark)) {
            mTvTitle.setText(getString(R.string.edit_the_electronic_fence));
            if (mLat != null && mLng != null && mDesc != null) {
                mGeoFenceModel.Latitude = Double.parseDouble(mLat);
                mGeoFenceModel.Longitude = Double.parseDouble(mLng);
                mLatLng = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
                mTvAddress.setText(mDesc);
                mGeoFenceModel.Radius = Integer.parseInt(mRadius);
                mTvScopeOf.setText(getString(R.string.security_zone_radius) + (mRadius) + getResources().getString(R.string.Geofence_Meter));
            }

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_confirm:
                if (mInNowork) {
                    printn(getString(R.string.no_network));
                    return;
                }

                if (mCity == null) {
                    mCity = (String) SharedPreferencesUtils.getParam(mContext, "city", "深圳");
                    LogUtils.e(TAG, "mCity==null  " + mCity);
                }

                LogUtils.e(TAG, "mCity  " + mCity);

                toActivity(RELATION, SearchActivity.class, mCity);
                break;

            case R.id.iv_delete:
                break;

            case R.id.btn_next:
                if (mInNowork) {
                    printn(getString(R.string.no_network));
                    return;
                }
                //下一步
                nextFence();
                break;

            case R.id.add_zoom:
                changeCamera(CameraUpdateFactory.zoomIn(), null);
                break;

            case R.id.narrow_zoom:
                changeCamera(CameraUpdateFactory.zoomOut(), null);
                break;
        }
    }


    /**
     * 下一步设置围栏
     */
    private void nextFence() {
        try {
            String trim = mTvAddress.getText().toString().trim();
            if (trim == null || trim.equals("")) {
                printn(getString(R.string.invalid_address));
                return;
            }
            LogUtils.d(TAG, "Radius " + mGeoFenceModel.Radius + "");
            LogUtils.d(TAG, "Longitude " + mGeoFenceModel.Longitude + "");
            LogUtils.d(TAG, "Latitude  " + mGeoFenceModel.Latitude + "");
            LogUtils.d(TAG, "address " + trim);
            Intent intent = new Intent(this, AddressSettingActivity.class);
            //修改电子围栏
            if ("Edit".equals(mFromMark)) {
                intent.putExtra("radius", mGeoFenceModel.Radius);
                intent.putExtra("lng", mGeoFenceModel.Longitude);
                intent.putExtra("lat", mGeoFenceModel.Latitude);
                intent.putExtra("address", trim);
                intent.putExtra("FromMark", mFromMark);
                intent.putExtra("fenceId", mFenceId);
                intent.putExtra("name", mName);
                startActivity(intent);
            } else {
                intent.putExtra("radius", mGeoFenceModel.Radius);
                intent.putExtra("lng", mGeoFenceModel.Longitude);
                intent.putExtra("lat", mGeoFenceModel.Latitude);
                intent.putExtra("address", trim);
                startActivity(intent);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RELATION) {
            if (data != null) {
                Log.d("SearchActivity", "onActivityResult: " + "");
                mLatLng = data.getParcelableExtra("location");
                String address = data.getStringExtra("address");
                mTvAddress.setText(address);
                LogUtils.e(TAG, "onActivityResult " + mLatLng.latitude + "~~ " + mLatLng.longitude);
//                addMarkersToMap(mLatLng);
                moveToPoint(mLatLng);
                if (mScreenMarker != null && mCircle != null) {
                    mScreenMarker.setPosition(mLatLng);
                    mScreenMarker.setPositionByPixels(mScreenPosition.x, mScreenPosition.y);
                    mScreenMarker.setToTop();
                    mCircle.setCenter(mLatLng);
                    LogUtils.e(TAG, "mScreenMarker != null && mCircle != null");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return true;
    }


    //移动指针
    private void moveToPoint(LatLng latLng) {
        LogUtils.e(TAG, "纬度 " + latLng.latitude);
        LogUtils.e(TAG, "经度 " + latLng.longitude);
        mGeoFenceModel.Latitude = latLng.latitude;
        mGeoFenceModel.Longitude = latLng.longitude;
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng
                , 16, 0, 0)));
    }


    /**
     * 移到latLng位置
     *
     * @param cameraUpdate
     */
    private void changeCamera(CameraUpdate cameraUpdate) {
        mAMap.moveCamera(cameraUpdate);
    }


    /**
     * 在地图上添加marker
     *
     * @param latLng
     */
    private void addMarkersToMap(LatLng latLng) {
        mAMap.clear();
        mScreenPosition = mAMap.getProjection().toScreenLocation(latLng);
        // 绘制一个圆形
        CircleOptions circleOptions = new CircleOptions().center(latLng)
                .radius(mGeoFenceModel.Radius).strokeColor(Color.argb(180, 63, 145, 252))
                .fillColor(Color.argb(40, 68, 188, 249)).strokeWidth(2F);
        initLocationView();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(mLocationInforPopupWindow);

        mScreenMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.9f)
                .icon(bitmapDescriptor));
        mScreenMarker.setToTop();
        mCircle = mAMap.addCircle(circleOptions);
        //设置Marker在屏幕上,不跟随地图移动
        mScreenMarker.setPositionByPixels(mScreenPosition.x, mScreenPosition.y);
    }


    /**
     * 初始化定位view
     */
    private void initLocationView() {
        mLocationInforPopupWindow = LayoutInflater.from(this)
                .inflate(R.layout.popuwindow_text, null);
        mCivIcon = (CircleImageView) mLocationInforPopupWindow
                .findViewById(R.id.civ_icon);
        if (DevicesHomeActivity.sBitmap != null) {
            LogUtils.e(TAG, "sBitmap != null");
            mCivIcon.setImageBitmap(DevicesHomeActivity.sBitmap);
        } else if (MyApplication.sDeviceListBean.getImgUrl() != null) {
            LogUtils.e(TAG, "mImgUrl != null");
            Glide.with(this).load(MyApplication.sDeviceListBean.getImgUrl()).into(mCivIcon);
        } else {
            mCivIcon.setImageResource(R.drawable.a);
        }
    }


    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        if (isAnimateCamera) {
            mAMap.animateCamera(update, 300, callback);
        }
    }


    /**
     * SeekBar 设置围栏范围
     * Radius 围栏半径
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress < 20) {
            progress = 20;
        }
        Log.i(TAG, "progress : " + progress);
        mGeoFenceModel.Radius = (int) (progress * 10.0);
        if (mCircle != null){
        mCircle.setRadius(progress * 10.0);
        }
        mTvScopeOf.setText(getString(R.string.security_zone_radius) + (mGeoFenceModel.Radius) + getResources().getString(R.string.Geofence_Meter));
        if (mGeoFenceModel.Radius > 560) {
            //缩小地图
            changeCamera(CameraUpdateFactory.zoomOut(), null);
            isAnimateCamera = false;
        } else {
            //放大地图
            if (!isAnimateCamera) {
                isAnimateCamera = true;
                changeCamera(CameraUpdateFactory.zoomIn(), null);
            }
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {

    }


    /**
     * 当前没网的通知
     */
    @Override
    protected void noNetwork() {
        LogUtils.e(TAG, "当前没网的通知");
        mInNowork = true;
    }


    /**
     * 有网的通知
     */
    @Override
    protected void theNetwork() {
        LogUtils.e(TAG, "有网的通知");
        if (mGeoFenceModel != null && mGeoFenceModel.Latitude != 0) {
            LogUtils.e(TAG, "有网移动了");
            LatLng latLng = new LatLng(mGeoFenceModel.Latitude, mGeoFenceModel.Longitude);
            moveToPoint(latLng);
        }
        mInNowork = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
        mMapView.onResume();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
        mMapView.onPause();
    }


    @Override
    public void onMapLoaded() {
        LogUtils.e(TAG, "onMapLoaded!!");
        addMarkersToMap(mLatLng);
        if ("Edit".equals(mFromMark)) {
            int progress = Integer.valueOf(mRadius) / 10;
            LogUtils.e(TAG, "prgress :" + progress);
            mSeekbarRadius.setProgress(progress);
        }
    }


    /**
     * 大头针移动的时候触发
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition == null) {
            return;
        }
        LogUtils.e(TAG, "大头针移动的时候触发");
        mCircle.setCenter(cameraPosition.target);
        mTvAddress.setVisibility(View.GONE);
        mTvAddress.setText("");
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setImageResource(com.hyphenate.easeui.R.drawable.juhua);
        mDrawable = (AnimationDrawable) mProgressBar.getDrawable();
        mDrawable.start();
    }


    /**
     * 大头针移动结束后触发
     *
     * @param cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (cameraPosition == null) {
            return;
        }
        //屏幕中心的Marker跳动
        mGeoFenceModel.Latitude = cameraPosition.target.latitude;
        mGeoFenceModel.Longitude = cameraPosition.target.longitude;
        LogUtils.e(TAG, "屏幕中心的Marker跳动 latitude" + cameraPosition.target.latitude);
        LogUtils.e(TAG, "屏幕中心的Marker跳动  longitude" + cameraPosition.target.longitude);
        LogUtils.e(TAG, "屏幕中心的Marker跳动  longitude" + cameraPosition.toString());

        //经纬度解析地址
        LatLonPoint lp = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
        RegeocodeQuery query = new RegeocodeQuery(lp, 1000, GeocodeSearch.AMAP);
        mGeocoderSearch.getFromLocationAsyn(query);

        //跳动画
        mElectronicFencePresenter.startJumpAnimation(mScreenMarker);
    }

    /**
     * 经纬度解析地址回调
     *
     * @param regeocodeResult
     * @param rCode
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        LogUtils.e(TAG, "onRegeocodeSearched " + rCode);
        if (rCode == 1000) {
            if (regeocodeResult != null) {
                if (regeocodeResult.getRegeocodeAddress() != null) {
                    if (regeocodeResult.getRegeocodeAddress().getFormatAddress() != null
                            && regeocodeResult.getRegeocodeAddress().getCity() != null) {

                        mProgressBar.setVisibility(View.GONE);
                        mTvAddress.setVisibility(View.VISIBLE);
                        mTvAddress.setText(regeocodeResult.getRegeocodeAddress().getFormatAddress());
                        mCity = regeocodeResult.getRegeocodeAddress().getCity();
                        LogUtils.d(TAG, "定位地址  " + regeocodeResult.getRegeocodeAddress().getFormatAddress() + "");
                        LogUtils.d(TAG, "定位城市  " + mCity + "");
                    }
                }
            }
        }
    }


    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        LogUtils.e(TAG, "onGeocodeSearched " + i);
    }


}
