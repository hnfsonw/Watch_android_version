package com.loybin.baidumap.guide;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.ScreenUtils;

/**
 *  引导页业务
 */
public class NewbieGuideManager {

    private static final String TAG = "newbie_guide";

    public static final int TYPE_First = 1;//收藏
    public static final int TYPE_TWICE = 2;//list
    public static final int TYPE_THIRD = 3;

    private Activity mActivity;
    private SharedPreferences sp;
    private NewbieGuide mNewbieGuide;
    private int mType;

    public NewbieGuideManager(Activity activity, int type) {
        LogUtils.d("yindaoye","实例化引导页管理");
        mNewbieGuide = new NewbieGuide(activity);
//        sp = activity.getSharedPreferences(TAG, Activity.MODE_PRIVATE);
        mActivity = activity;
        mType = type;
    }

    public NewbieGuideManager addView(View view, int shape) {
        mNewbieGuide.addHighLightView(view, shape);
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(int delayTime) {
//       SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(TAG + mType, false);
//        editor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_First:
                        mNewbieGuide.addIndicateImg(R.mipmap.guide_right,
                                ScreenUtils.dpToPx(mActivity, -50),
                                ScreenUtils.dpToPx(mActivity, 110))
                                .addMessage(mActivity.getString(R.string.switch_map),
                                        ScreenUtils.dpToPx(mActivity, -40)
                                        ,ScreenUtils.dpToPx(mActivity, 130))
                                .addIndicateImg(R.mipmap.guide_left,
                                        ScreenUtils.dpToPx(mActivity,60),
                                        ScreenUtils.dpToPx(mActivity,40))
                                .addMessage(mActivity.getString(R.string.open_menu),
                                        ScreenUtils.dpToPx(mActivity,85),
                                        ScreenUtils.dpToPx(mActivity,60)).show();

                        break;

                    case TYPE_TWICE:
                        mNewbieGuide.addIndicateImg(R.mipmap.guide_right2,
                                ScreenUtils.dpToPx(mActivity, -50),
                                ScreenUtils.dpToPx(mActivity, -170))

                                .addMessage(mActivity.getString(R.string.see_the_history),
                                ScreenUtils.dpToPx(mActivity, -50),
                                 ScreenUtils.dpToPx(mActivity,-185)).

                                addIndicateImg(R.mipmap.guide_down,
                                ScreenUtils.dpToPx(mActivity,-105),
                                ScreenUtils.dpToPx(mActivity,-135))

                                .addMessage(mActivity.getString(R.string.navigate_to_the_watch),
                                ScreenUtils.dpToPx(mActivity,-115),
                                ScreenUtils.dpToPx(mActivity,-160)).show();
                        break;

                    case TYPE_THIRD:
                        mNewbieGuide.addIndicateImg(R.mipmap.guide_right2,
                                ScreenUtils.dpToPx(mActivity, -50),
                                ScreenUtils.dpToPx(mActivity,-230))
                                .addMessage("设置安全区域",
                                        ScreenUtils.dpToPx(mActivity,-50),
                                        ScreenUtils.dpToPx(mActivity,-250))
                                .addIndicateImg(R.mipmap.guide_down,
                                        ScreenUtils.dpToPx(mActivity, -50),
                                        ScreenUtils.dpToPx(mActivity, -130))
                                .addMessage("立即刷新位置",
                                        ScreenUtils.dpToPx(mActivity, -50),
                                        ScreenUtils.dpToPx(mActivity,-155))
                                .show();
                }
            }
        }, delayTime);
    }

    public void showWithListener(int delayTime, NewbieGuide.OnGuideChangedListener onGuideChangedListener) {
        mNewbieGuide.setOnGuideChangedListener(onGuideChangedListener);
        show(delayTime);
    }



}
