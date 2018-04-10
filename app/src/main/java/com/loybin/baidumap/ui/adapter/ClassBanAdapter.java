package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ClassBanActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.List;


/**
 * Created by huangz on 17/8/26.
 */

public class ClassBanAdapter extends BaseAdapter {

    private Context mContext;
    private ClassBanActivity mClassBanActivity;
    private List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> mClassBanList;
    private String mTurnState = "";

    public ClassBanAdapter(Context context,
                           List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> classBanList) {
        mContext = context;
        mClassBanList = classBanList;
        mClassBanActivity = (ClassBanActivity) mContext;
    }

    @Override
    public int getCount() {
        if (mClassBanList == null) {
            return 0;
        }
        return mClassBanList.size();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_class_ban, null);
            viewHolder.mTvClassBanName = (TextView) view.findViewById(R.id.tv_class_ban_name);
            viewHolder.mTvMorningTime = (TextView) view.findViewById(R.id.tv_morning_time);
            viewHolder.mTvAfternoonTime = (TextView) view.findViewById(R.id.tv_afternoon_time);
            viewHolder.mTvEveningTime = (TextView) view.findViewById(R.id.tv_evening_time);
            viewHolder.mTvCycle = (TextView) view.findViewById(R.id.tv_cycle);
            viewHolder.mIvClassBanOn = (ImageView) view.findViewById(R.id.iv_class_ban_on);
            viewHolder.mIvClassBanOff = (ImageView) view.findViewById(R.id.iv_class_ban_off);
            viewHolder.mIvClassBanDelete = (ImageView) view.findViewById(R.id.iv_class_ban_delete);
            viewHolder.mLlMorning = (LinearLayout) view.findViewById(R.id.ll_morning);
            viewHolder.mLlAfternoon = (LinearLayout) view.findViewById(R.id.ll_afternoon);
            viewHolder.mLlEvening = (LinearLayout) view.findViewById(R.id.ll_evening);
            viewHolder.mRlTime = (RelativeLayout) view.findViewById(R.id.rl_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.mTvClassBanName.setText(mClassBanList.get(i).getName());
        viewHolder.mTvCycle.setText(changeCycle(mClassBanList.get(i).getCycle()));
        if (getState(mClassBanList.get(i).getStateAM())) {
            String morning = mClassBanList.get(i).getStartTimeAmStr() + " - "
                    + mClassBanList.get(i).getEndTimeAmStr();
            viewHolder.mTvMorningTime.setText(morning);
            viewHolder.mLlMorning.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlMorning.setVisibility(View.GONE);
        }

        if (getState(mClassBanList.get(i).getStatePM())) {
            String afternoon = mClassBanList.get(i).getStartTimePmStr() + " - "
                    + mClassBanList.get(i).getEndTimePmStr();
            viewHolder.mTvAfternoonTime.setText(afternoon + "");
            viewHolder.mLlAfternoon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlAfternoon.setVisibility(View.GONE);
        }

        if (getState(mClassBanList.get(i).getStateEM())) {
            String evening = mClassBanList.get(i).getStartTimeEmStr() + " - "
                    + mClassBanList.get(i).getEndTimeEmStr();
            viewHolder.mTvEveningTime.setText(evening);
            viewHolder.mLlEvening.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlEvening.setVisibility(View.GONE);
        }


        if ("edit".equals(mTurnState)) {
            viewHolder.mIvClassBanDelete.setVisibility(View.VISIBLE);
            viewHolder.mIvClassBanOn.setVisibility(View.GONE);
            viewHolder.mIvClassBanOff.setVisibility(View.GONE);
            viewHolder.mIvClassBanDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClassBanActivity.turnPosition = i;
                    mClassBanActivity.deleteClassBan(mClassBanList.get(i).getId());
                }
            });
        } else {
            viewHolder.mIvClassBanDelete.setVisibility(View.GONE);
            if (getState(mClassBanList.get(i).getState())) {
                viewHolder.mIvClassBanOn.setVisibility(View.VISIBLE);
                viewHolder.mIvClassBanOff.setVisibility(View.GONE);
            } else {
                viewHolder.mIvClassBanOn.setVisibility(View.GONE);
                viewHolder.mIvClassBanOff.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.mIvClassBanOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.mIvClassBanOn.setVisibility(View.GONE);
                viewHolder.mIvClassBanOff.setVisibility(View.VISIBLE);
                mClassBanActivity.turnState = "1";
                mClassBanActivity.turnPosition = i;
                mClassBanActivity.changeTurnOn(mClassBanList.get(i).getId(), "0");
            }
        });
        viewHolder.mIvClassBanOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e("ClassBanActivity", "开关打开");
                viewHolder.mIvClassBanOn.setVisibility(View.VISIBLE);
                viewHolder.mIvClassBanOff.setVisibility(View.GONE);
                mClassBanActivity.turnState = "0";
                mClassBanActivity.turnPosition = i;
                mClassBanActivity.changeTurnOn(mClassBanList.get(i).getId(), "1");
            }
        });

        viewHolder.mRlTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClassBanActivity.editClassTime(String.valueOf(i));
            }
        });

        return view;
    }

    public void setData(List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> list) {
        mClassBanList = list;
    }

    public void toEdit(String state) {
        if ("编辑".equals(state)) {
            mTurnState = "edit";
            mClassBanActivity.mTvRight.setText("取消");
        } else {
            mTurnState = "";
            mClassBanActivity.mTvRight.setText("编辑");
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView mTvClassBanName;
        TextView mTvMorningTime;
        TextView mTvAfternoonTime;
        TextView mTvEveningTime;
        TextView mTvCycle;
        ImageView mIvClassBanOn;
        ImageView mIvClassBanOff;
        ImageView mIvClassBanDelete;
        LinearLayout mLlMorning;
        LinearLayout mLlAfternoon;
        LinearLayout mLlEvening;
        RelativeLayout mRlTime;
    }

    private boolean getState(String state) {
        if ("1".equals(state)) {
            return true;
        } else {
            return false;
        }
    }


    private String changeCycle(String cycle) {
        String result = "";
        String[] split = cycle.split(",");
        for (int i = 0; i < split.length; i++) {
            String week = split[i];
            int weekNumber = Integer.parseInt(week);
            switch (weekNumber) {
                case 1:
                    result = result + "一";
                    break;

                case 2:
                    result = result + "二";
                    break;

                case 3:
                    result = result + "三";
                    break;

                case 4:
                    result = result + "四";
                    break;

                case 5:
                    result = result + "五";
                    break;

                case 6:
                    result = result + "六";
                    break;

                case 7:
                    result = result + "日";
                    break;
            }

        }
        return result;
    }


    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
