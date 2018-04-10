package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.ui.activity.DevicesHistoryActivity;
import com.loybin.baidumap.util.MyApplication;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 自定义弹窗日历的Dialog
 */
public class CalendarDialog extends AlertDialog implements OnDateSelectedListener {

    DevicesHistoryActivity mDevicesHistoryActivity;
    private Context mContext;


    public CalendarDialog(Context context, int theme, DevicesHistoryActivity devicesHistoryActivity) {
        super(context, theme);
        mContext = context;
        mDevicesHistoryActivity = devicesHistoryActivity;
    }


    private MaterialCalendarView mWidget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_basic);
        mWidget = (MaterialCalendarView) findViewById(R.id.calendarView);
        mWidget.setOnDateChangedListener(this);

//            getWindow().setGravity(Gravity.BOTTOM); //显示在底部

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);

    }


    @Override
    protected void onStart() {

        if (DevicesHistoryActivity.mSelectedDate != null) {
            mWidget.setSelectedDate(DevicesHistoryActivity.mSelectedDate);
        }
        super.onStart();
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mDevicesHistoryActivity.mTvCalendar.setText(mDevicesHistoryActivity.FORMATTER.format(date.getDate()));
        Log.d("DevicesHistoryActivity", "onDateSelected: " + date.toString());
        int nowDay = date.getDay();
        int nowMonth = date.getMonth();
        int nowYeay = date.getYear();

        mDevicesHistoryActivity.mCalendarDate = nowDay;
        mDevicesHistoryActivity.mSelectedDate = date.getDate();
        long time = mDevicesHistoryActivity.mSelectedDate.getTime();
        mDevicesHistoryActivity.mDate.setTime(time);

        if (nowMonth <= mDevicesHistoryActivity.mPresenMonth) {
            if (mDevicesHistoryActivity.mTime > time) {
                dismiss();
                int month = date.getMonth() + 1;
                String format = nowYeay + "-" + month + "-" + nowDay;
                Log.d("DevicesHistoryActivity", "onDateSelected: " + format);
                if (mDevicesHistoryActivity.mHandler != null) {
                    Message message = new Message();
                    message.what = mDevicesHistoryActivity.ONES;
                    mDevicesHistoryActivity.mHandler.handleMessage(message);
                    mDevicesHistoryActivity.mHandler.removeMessages(0);
                }
//                mDevicesHistoryActivity.mBtnPlay.setText(mContext.getString(R.string.play_track));
                mDevicesHistoryActivity.mTvTime.setText("");
                mDevicesHistoryActivity.mTvAddress.setText("");
                mDevicesHistoryActivity.routeIndex = 0;
                mDevicesHistoryActivity.mDistance = 0;
                mDevicesHistoryActivity.getHistoryLocations(MyApplication.sToken, MyApplication.sDeviceId, format);
                mDevicesHistoryActivity.isMoveToLocation = true;
                if (mDevicesHistoryActivity.mSmoothMarker != null) {
                    mDevicesHistoryActivity.mSmoothMarker.destroy();
                    mDevicesHistoryActivity.mIsStartSmoothMove = true;
                }
            }

        }


    }
}
