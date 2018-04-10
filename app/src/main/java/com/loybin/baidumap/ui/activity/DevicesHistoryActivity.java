package com.loybin.baidumap.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.DevicesHistoryPresenter;
import com.loybin.baidumap.ui.view.CalendarDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 历史轨迹view
 */
public class DevicesHistoryActivity extends BaseActivity {

    private static final String TAG = "DevicesHistoryActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.mapview_history)
    public MapView mMapView;

    @BindView(R.id.deviceHistoryBasicLayout)
    LinearLayout mDeviceHistoryBasicLayout;

    @BindView(R.id.tv_before)
    TextView mTvBefore;

    @BindView(R.id.tv_calendar)
    public TextView mTvCalendar;

    @BindView(R.id.tv_after)
    TextView mTvAfter;

    @BindView(R.id.location_ivPlay)
    public ImageView mBtnPlay;

    @BindView(R.id.tv_address)
    public TextView mTvAddress;

    @BindView(R.id.tv_time)
    public TextView mTvTime;

    @BindView(R.id.tv_play)
    TextView mTvPlay;

    @BindView(R.id.add_zoom)
    TextView mAddZoom;

    @BindView(R.id.narrow_zoom)
    TextView mNarrowZoom;

    public static Date mDate;
    public static Date mSelectedDate;
    public static long mTime;
    public static int mPresentDate;
    public static int mCalendarDate;
    public static int mPresenMonth;
    public DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    long day = 1000 * 60 * 60 * 24;
    //剩余时间点个数
    public int timeCount = 0;
    //剩余指针点个数
    public int pointCount = 0;
    //地图相关
    public List<LatLng> mLatLngList;
    public DevicesHistoryPresenter mDevicesHistoryPresenter;
    public int routeIndex;
    private boolean routeFlag;
    private final int ROUTE = 0;
    public final int ONES = 1;
    public CalendarDialog mSelectDialog;
    public SimpleDateFormat mSimpleDateFormat;
    public List<ResponseInfoModel.ResultBean.HistoryLocationsBean> mHistoryList;
    public int index;
    private boolean isMobile;
    public AMap mAMap;
    private UiSettings mUiSettings;
    public boolean mIsStartSmoothMove = true;
    public SmoothMoveMarker mSmoothMarker;
    private boolean mIsSwitch;
    public int mDistance = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_devices_history;
    }


    @Override
    protected void init() {
        ButterKnife.bind(this);
        mDevicesHistoryPresenter = new DevicesHistoryPresenter(this, this);
        mHistoryList = new ArrayList<>();
        //获取当前到时间
        Calendar c = Calendar.getInstance();
        mPresentDate = c.get(Calendar.DATE);
        mPresenMonth = c.get(Calendar.MONTH);

        mDate = new Date(System.currentTimeMillis());
        mTime = mDate.getTime();

        mCalendarDate = mPresentDate;

        mTvCalendar.setText(FORMATTER.format(mDate));
        Log.d(TAG, "FORMATTER.format(mDate: " + FORMATTER.format(mDate));
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        String data = mSimpleDateFormat.format(mDate);
        Log.d(TAG, "mDate: " + data);
        Log.d(TAG, "token: " + MyApplication.sToken);
        Log.d(TAG, "deviceId: " + MyApplication.sDeviceId);

        mLatLngList = new ArrayList<>();

        initView();
        initData();
        getHistoryLocations(MyApplication.sToken, MyApplication.sDeviceId, data);
    }


    /**
     * 初始化视图
     */
    private void initView() {
        mTvTitle.setText(getString(R.string.history));
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mUiSettings.setTiltGesturesEnabled(false);
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mDevicesHistoryPresenter.openLine(mHistoryList);
    }


    public void getHistoryLocations(String token, long deviceId, String data) {
        mDevicesHistoryPresenter.getHistoryLocations(data, token, deviceId);
    }


    private Marker routeMarker;
    private int ROUTETIME = 800;
    public boolean isMoveToLocation = true;
    public Handler mHandler = new Handler() {
        public void handleMessage(final Message msg) {
            LogUtils.e(TAG, "msg.obj; " + Thread.currentThread().getName());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (msg.what == ROUTE) {
                            int obj = (int) msg.obj;
                            LogUtils.e(TAG, "msg.obj; " + obj);
                            LogUtils.e(TAG, "msg.obj; " + mHistoryList.get(obj).getAddress());
                            mTvAddress.setText(mHistoryList.get(obj).getAddress());
                            mTvTime.setText(mHistoryList.get(obj).getLocationTime());
                        }

                        if (msg.what == ONES) {
                            LogUtils.e(TAG, "msg.obj; ONES");
                            mIsSwitch = true;
                            mHistoryList.clear();
                        }
                    } catch (Exception e) {

                    }
                }
            });


//            try {
//                if (msg.what == ROUTE) {
//                    Log.d(TAG, "handleMessage: " + "33" + routeIndex);
//                    if (mHistoryList.size() != routeIndex) {
//                        mTvTime.setText(mHistoryList.get(routeIndex).getLocationTime());
//                        mTvAddress.setText(mHistoryList.get(routeIndex).getAddress());
//                    }
//
//                    mHandler.sendEmptyMessage(PROGRESS);
//                    if (routeIndex == mLatLngList.size() - 1) {
//                        routeFlag = false;
//                        mBtnPlay.setText(getString(R.string.play_track));
//                        LogUtils.e(TAG,"播放完毕");
//                        printn(getString(R.string.was_over));
//                        routeIndex = 0;
//                        if (routeMarker != null) {
//                            routeMarker.remove();
//                            routeMarker = null;
//                        }
//                        addRouteLine(mLatLngList);
//                    mDevicesHistoryPresenter.recordStartingPoint(mHistoryList);
//                    return;
//                }
//                if (routeIndex != 0) {
//                    Log.d(TAG, "handleMessage: " + routeIndex);
//                        addRouteLine(mLatLngList);
//                    mAMap.addMarker(new MarkerOptions().position(mLatLngList.get(routeIndex))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.starting_point)));
//                }
////                     页面跟随移动,注掉这行就是在原图上绘制
////                        moveToLocation(mLatLngList.get(routeIndex), false);
//
//                    if (routeMarker == null) {
//                        Log.d(TAG, "handleMessage: " + "55~~" + routeIndex);
//                        Log.d(TAG, "mLatLngList.size(): " + mLatLngList.size());
//                        Log.d(TAG, "mHistoryList.size(): " + mHistoryList.size());
//                        new MarkerOptions().position(mLatLngList.get(routeIndex++))
//                                .perspective(false).anchor(0.5f, 0.5f).zIndex(10);
//                    } else {
//                        routeMarker.setPosition(mLatLngList.get(routeIndex++));
//                    }
//                    mHandler.sendEmptyMessageDelayed(ROUTE, ROUTETIME);
//                }
//            } catch (Exception e) {
//                LogUtils.e(TAG, "handleMessage异常" + e.getMessage());
//            }
        }

        ;
    };


    /**
     * 添加路线
     *
     * @param routeList
     */
    private void addRouteLine(List<LatLng> routeList) {
        try {
            mAMap.clear();
            // 高德地图最多支持10000个点连线
            LogUtils.e(TAG, "添加路线  " + routeList.size());

            if (routeList.size() > 10000) {
                routeList = routeList.subList(0, 10000);
            }
            mDevicesHistoryPresenter.drawLine(routeList);


            if (isMobile) {
                isMobile = false;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "添加路线异常 " + e.getMessage());
        }

    }


    private final int PROGRESS = 1234;

    @OnClick({R.id.iv_back, R.id.tv_before, R.id.tv_calendar, R.id.tv_after,
            R.id.location_ivPlay, R.id.tv_play, R.id.add_zoom, R.id.narrow_zoom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                mHandler.removeMessages(0);
                finishActivityByAnimation(DevicesHistoryActivity.this);
                break;

            case R.id.tv_before:
                upDay();
                break;

            case R.id.tv_calendar:
                showCalendar();
                break;

            case R.id.tv_after:
                nextDay();
                break;

            case R.id.location_ivPlay:
                play();
                break;

            case R.id.tv_play:
                play();
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
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        mAMap.animateCamera(update, 300, callback);
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            mHandler.removeMessages(0);
            finishActivityByAnimation(DevicesHistoryActivity.this);
        }
        return true;
    }


    private void play() {
        if (mHistoryList == null || mHistoryList.size() <= 0) {
            return;
        }
//            routeFlag = !routeFlag;
//            mBtnPlay.setText(getString(R.string.On_TV));
//
//            if (routeFlag) {
//                isMobile = true;
//                mHandler.sendEmptyMessageDelayed(ROUTE, 0);
//            } else {
//                mBtnPlay.setText(getString(R.string.play_track));
//                mHandler.removeMessages(0);
//            }
        if (!mIsStartSmoothMove) {
            getString(R.string.there_is_in);
            return;
        }
        mDevicesHistoryPresenter.moveToPoint(mDevicesHistoryPresenter.mLatLng, true);
        // 读取轨迹点
        List<LatLng> points = mLatLngList;
        if (points.size() >= 2 && mIsStartSmoothMove) {
            mIsStartSmoothMove = false;
            mIsSwitch = false;

            // 实例 SmoothMoveMarker 对象
            mSmoothMarker = new SmoothMoveMarker(mAMap);
            // 设置 平滑移动的 图标
            mSmoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.starting_point));

            // 取轨迹点的第一个点 作为 平滑移动的启动
            LatLng drivePoint = points.get(0);
            Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
            points.set(pair.first, drivePoint);
            List<LatLng> subList = points.subList(pair.first, points.size());

            // 设置轨迹点
            mSmoothMarker.setPoints(subList);
            // 设置平滑移动的总时间  单位  秒
            mSmoothMarker.setTotalDuration(7);

//             设置移动的监听事件  返回 距终点的距离  单位 米
            mSmoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
                @Override
                public void move(final double distance) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(TAG, "distance " + distance);
                            if (distance == 0.0) {
                                mDistance++;
                                if (mDistance == 2) {
                                    mSmoothMarker.destroy();
                                    mIsStartSmoothMove = true;
                                    mDevicesHistoryPresenter.mMarker.setVisible(true);
                                    printn(getString(R.string.was_over));
                                    mDistance = 0;
                                }

                            }

                            MyApplication.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (distance == 0.0 && mDistance == 1) {
                                        mSmoothMarker.destroy();
                                        mIsStartSmoothMove = true;
                                        mDevicesHistoryPresenter.mMarker.setVisible(true);
                                        printn(getString(R.string.was_over));
                                        mDistance = 0;
                                    }
                                }
                            }, 3500);

                        }
                    });

                }
            });
            // 开始移动
            mSmoothMarker.startSmoothMove();
            startAddress();
            mDevicesHistoryPresenter.mMarker.setVisible(false);
        }
    }


    /**
     * 循环地址信息
     */
    private void startAddress() {
        final int time = 6750 / mHistoryList.size();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mHistoryList.size(); i++) {
                    if (!mIsSwitch) {
                        LogUtils.e(TAG, "mHistoryList " + mHistoryList.get(i).getAddress());
                        Message message = new Message();
                        message.what = ROUTE;
                        message.obj = i;
                        mHandler.handleMessage(message);
                        SystemClock.sleep(time);
                    }

                }
            }
        }).start();
    }


    /**
     * 日历的弹窗样式设置
     */
    private void showCalendar() {
        //创建Dialog并设置样式主题
        mSelectDialog = new CalendarDialog
                (DevicesHistoryActivity.this, R.style.dialog, DevicesHistoryActivity.this);
        Window win = mSelectDialog.getWindow();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = 0;//设置x坐标
        params.y = -175;//设置y坐标
        win.setAttributes(params);

        //设置点击Dialog外部任意区域关闭Dialog
        mSelectDialog.setCanceledOnTouchOutside(true);
        mSelectDialog.show();

    }


    /**
     * 日历的上一天点击事件
     */
    public void upDay() {
        if (mHandler != null) {
            Message message = new Message();
            message.what = ONES;
            mHandler.handleMessage(message);
            mHandler.removeMessages(0);
        }
//            mBtnPlay.setText(getString(R.string.play_track));
        mTvTime.setText("");
        mTvAddress.setText("");
        routeIndex = 0;
        mDistance = 0;
        long al = mDate.getTime() - day;
        mCalendarDate--;
        mDate.setTime(al);
        mTvCalendar.setText(FORMATTER.format(al));
        String data = mSimpleDateFormat.format(al);
        Log.d(TAG, "upDay: " + data);
        getHistoryLocations(MyApplication.sToken, MyApplication.sDeviceId, data);
        if (mSmoothMarker != null) {
            mSmoothMarker.destroy();
            mIsStartSmoothMove = true;
        }
        isMoveToLocation = true;

    }


    /**
     * 日历的下一天点击事件
     */
    public void nextDay() {
        if (mPresentDate <= mCalendarDate) {
            return;
        }
        if (mHandler != null) {
            Message message = new Message();
            message.what = ONES;
            mHandler.handleMessage(message);
            mHandler.removeMessages(0);
        }
//            mBtnPlay.setText(getString(R.string.play_track));
        mTvTime.setText("");
        mTvAddress.setText("");
        routeIndex = 0;
        mDistance = 0;
        long al = mDate.getTime() + day;
        mCalendarDate++;
        mDate.setTime(al);
        mTvCalendar.setText(FORMATTER.format(al));
        String data = mSimpleDateFormat.format(al);
        Log.d(TAG, "nextDay: " + data);
        getHistoryLocations(MyApplication.sToken, MyApplication.sDeviceId, data);
        if (mSmoothMarker != null) {
            mSmoothMarker.destroy();
            mIsStartSmoothMove = true;
        }
        isMoveToLocation = true;

    }


    /**
     * 获取历史轨迹失败的通知
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 获取历史轨迹成功的通知
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        List<ResponseInfoModel.ResultBean.HistoryLocationsBean> historyLocations
                = data.getResult().getHistoryLocations();
        if (historyLocations.size() <= 0) {
            printn(getString(R.string.no_history));
        } else {
            mTvTime.setText(historyLocations.get(0).getLocationTime());
            mTvAddress.setText(historyLocations.get(0).getAddress());
        }
        collections(historyLocations);

        mHistoryList.clear();
        mLatLngList.clear();
        mHistoryList.addAll(historyLocations);
        initData();
    }


    /**
     * 按照时间的先后排序
     *
     * @param historyLocations
     */
    private void collections(List<ResponseInfoModel.ResultBean.HistoryLocationsBean> historyLocations) {
        Collections.sort(historyLocations,
                new Comparator<ResponseInfoModel.ResultBean.HistoryLocationsBean>() {
                    @Override
                    public int compare(ResponseInfoModel.ResultBean.HistoryLocationsBean o1,
                                       ResponseInfoModel.ResultBean.HistoryLocationsBean o2) {
                        long time2 = 0;
                        long time = 0;
                        //降序排序
                        String locationTime = o1.getLocationTime();
                        String locationTime1 = o2.getLocationTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                        try {
                            long timea = sdf.parse(locationTime).getTime();
                            long timeb = sdf.parse(locationTime1).getTime();
                            time = timea;
                            time2 = timeb;
                            Log.d(TAG, "compare: " + time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Log.d(TAG, "compare: " + e.getMessage());
                        }

                        return (int) (time - time2);
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mHandler.removeMessages(0);
        if (mSmoothMarker != null) {
            mSmoothMarker.destroy();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mDevicesHistoryPresenter.mHistoryLocations != null) {
            mDevicesHistoryPresenter.mHistoryLocations.cancel();
        }

    }
}
