package com.loybin.baidumap.ui.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;

import com.loybin.baidumap.util.LogUtils;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/12 下午4:48
 * 描   述: 自定义故事view
 */
public class StoryTextView extends TextView {

    private static final java.lang.String TAG = "StoryTextView";

    public StoryTextView(Context context) {
        super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.d(TAG, "ACTION_DOWN");
                return true;
            case MotionEvent.ACTION_MOVE:
                LogUtils.d(TAG, "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.d(TAG, "ACTION_UP");
                break;
        }

        return super.onTouchEvent(event);
    }
}
