package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UIUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  导航的dialog
 */

public class NavigationDialog extends AlertDialog {

    private static final String TAG = "DevicesHomeActivity";
    @BindView(R.id.ll_baidu_map)
    LinearLayout mLlBaiduMap;

    @BindView(R.id.ll_gaode_map)
    LinearLayout mLlGaodeMap;

    @BindView(R.id.tv_baidu)
    TextView mTvBaidu;

    @BindView(R.id.tv_gaode)
    TextView mTvGaode;

    private DevicesHomeActivity mDevicesHomeActivity;
    private Context mContext;
    private LatLng mDesLatLng;

    public NavigationDialog(Context context, DevicesHomeActivity devicesHomeActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mDevicesHomeActivity = devicesHomeActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_navigation);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM); //显示在底部


    }


    /**
     * 设置宽度全屏，要设置在show的后面
     */
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }


    @OnClick({R.id.ll_baidu_map, R.id.ll_gaode_map, R.id.tv_baidu, R.id.tv_gaode})
    public void onViewClicked(View view) {
        String address = (String) mDevicesHomeActivity.mLocationTv.getText();
        double lat = mDevicesHomeActivity.mLat;
        double lng = mDevicesHomeActivity.mLng;
        switch (view.getId()) {
            case R.id.tv_baidu:
                navigationBaiduMap(lat, lng);
                break;

            case R.id.tv_gaode:
                try {

                    navigationGaoDeMap(lat, lng);
                } catch (Exception e) {
                    LogUtils.e(TAG, e.getMessage());
                }

                break;
        }
    }


    /**
     * 调起百度地图导航
     */
    private void navigationBaiduMap(double lat, double lng) {
        try {
            if (lat == 0) {
                mDevicesHomeActivity.printn(mContext.getString(R.string.don__get_to_watch));
                dismiss();
                return;
            }
            LogUtils.e(TAG, "导航 lat" + lat);
            LogUtils.e(TAG, "导航 lon" + lng);
            mDesLatLng = UIUtils.gcj02_To_Bd09(lat, lng);

            LogUtils.d(TAG, "address " + mDesLatLng.latitude + "~~~~~" + mDesLatLng.longitude);
            if (isInstallByread("com.baidu.BaiduMap")) {
                Intent i1 = new Intent();
                // 步行导航
                i1.setData(Uri.parse("baidumap://map/direction?region=&origin=&destination=" +
                        "" + mDesLatLng.latitude + "," + mDesLatLng.longitude + "&mode=walking"));
                mDevicesHomeActivity.startActivity(i1);
                dismiss();
            } else {
                mDevicesHomeActivity.printn(mContext.getString(R.string.install_baidu_map));
                dismiss();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "调起百度地图导航 异常" + e.getMessage());
        }


    }


    /**
     * 调起高德地图导航
     *
     * @param lat 纬度
     * @param lon 经度
     */
    private void navigationGaoDeMap(double lat, double lon) {
        if (lat == 0) {
            mDevicesHomeActivity.printn(mContext.getString(R.string.don__get_to_watch));
            dismiss();
            return;
        }
        LogUtils.e(TAG, "导航 lat" + lat);
        LogUtils.e(TAG, "导航 lon" + lon);
        try {
            if (isInstallByread("com.autonavi.minimap")) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                //将功能Scheme以URI的方式传入data
                Uri uri = Uri.parse("amapuri://route/plan/?sid=BGVIS1&slat=&slon=" +
                        "&sname=&did=BGVIS2&dlat=" + lat + "&dlon=" + lon + "&dname=&dev=0&t=0");
                intent1.setData(uri);
                //启动该页面即可
                mDevicesHomeActivity.startActivity(intent1);
                dismiss();
            } else {
                mDevicesHomeActivity.printn(mContext.getString(R.string.install_gaode));
                dismiss();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}
