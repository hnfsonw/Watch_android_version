package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.AlarmClockActivity;

import java.util.List;

/**
 * Created by huangz on 17/10/13.
 * 闹钟列表适配器
 */

public class AlarmClockAdapter extends BaseAdapter {

    private Context mContext;
    private AlarmClockActivity mAlarmClockActivity;
    private List<ResponseInfoModel.ResultBean.alarmClockListBean> mAlarmClockList;

    public AlarmClockAdapter(Context context,
                             List<ResponseInfoModel.ResultBean.alarmClockListBean> alarmClockList) {
        mContext = context;
        mAlarmClockList = alarmClockList;
    }

    @Override
    public int getCount() {
        if (mAlarmClockList == null) {
            return 0;
        }
        return mAlarmClockList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            viewHolder.mTvClockTime = (TextView) view.findViewById(R.id.tv_clock_time);
            viewHolder.mTvClockCycle = (TextView) view.findViewById(R.id.tv_clock_cycle);
            viewHolder.IvAlarmClockOn = (ImageView) view.findViewById(R.id.iv_alarm_clock_on);
            viewHolder.IvAlarmClockOff = (ImageView) view.findViewById(R.id.iv_alarm_clock_off);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mTvClockTime.setText(mAlarmClockList.get(i).getAlarmTimeString());
        viewHolder.mTvClockCycle.setText(changeCycle(mAlarmClockList.get(i).getCycle()));
        if (mAlarmClockList.get(i).getIsActive() == 1) {
            viewHolder.IvAlarmClockOn.setVisibility(View.VISIBLE);
            viewHolder.IvAlarmClockOff.setVisibility(View.GONE);

            viewHolder.IvAlarmClockOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlarmClockActivity.changeAlarmClockTurn(mAlarmClockList.get(i).getId(),
                            mAlarmClockList.get(i).getAcountId(),
                            mAlarmClockList.get(i).getAlarmTimeString(),
                            mAlarmClockList.get(i).getCycle(), 0);
                }
            });
        } else {
            viewHolder.IvAlarmClockOn.setVisibility(View.GONE);
            viewHolder.IvAlarmClockOff.setVisibility(View.VISIBLE);

            viewHolder.IvAlarmClockOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlarmClockActivity.changeAlarmClockTurn(mAlarmClockList.get(i).getId(),
                            mAlarmClockList.get(i).getAcountId(),
                            mAlarmClockList.get(i).getAlarmTimeString(),
                            mAlarmClockList.get(i).getCycle(), 1);
                }
            });
        }
        return view;
    }


    class ViewHolder {
        TextView mTvClockTime;
        TextView mTvClockCycle;
        ImageView IvAlarmClockOn;
        ImageView IvAlarmClockOff;
    }


    private String changeCycle(String cycle) {
        String result = "";
        String[] split = cycle.split(",");
        for (int i = 0; i < split.length; i++) {
            String week = split[i];
            int weekNumber = Integer.parseInt(week);
            switch (weekNumber) {
                case 1:
                    result = result + "周一";
                    break;

                case 2:
                    result = result + "周二";
                    break;

                case 3:
                    result = result + "周三";
                    break;

                case 4:
                    result = result + "周四";
                    break;

                case 5:
                    result = result + "周五";
                    break;

                case 6:
                    result = result + "周六";
                    break;

                case 7:
                    result = result + "周日";
                    break;
            }

        }

        result = "闹钟, " + result;
        return result;
    }


    public void setData(List<ResponseInfoModel.ResultBean.alarmClockListBean> alarmClockList) {
        mAlarmClockList = alarmClockList;
    }
}
