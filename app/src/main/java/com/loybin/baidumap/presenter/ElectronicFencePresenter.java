package com.loybin.baidumap.presenter;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.animation.Interpolator;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.loybin.baidumap.ui.activity.ElectronicFenceActivity;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/02 上午11:51
 * 描   述: 电子围栏的业务逻辑
 */
public class ElectronicFencePresenter {
    private Context mContext;
    private ElectronicFenceActivity mElectronicFenceActivity;


    public ElectronicFencePresenter(Context context, ElectronicFenceActivity electronicFenceActivity) {
        mContext = context;
        mElectronicFenceActivity = electronicFenceActivity;
    }


    //dip和px转换
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void startJumpAnimation(Marker screenMarker) {
        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            Point point = mElectronicFenceActivity.mAMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(mContext, 80);
            LatLng target = mElectronicFenceActivity.mAMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(500);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("amap", "screenMarker is null");
        }
    }
}
