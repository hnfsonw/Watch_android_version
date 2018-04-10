package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.StepCounterActivity;
import com.loybin.baidumap.ui.view.CircleImageView;

import java.util.List;

/**
 * Created by huangz on 17/9/8.
 */

public class StepCounterAdapter extends BaseAdapter {

    private Context mContext;
    private StepCounterActivity mStepCounterActivity;
    private List<ResponseInfoModel.ResultBean.StepsRankingListBean> mStepsRankingListBeen;

    public StepCounterAdapter(Context context,
                              List<ResponseInfoModel.ResultBean.StepsRankingListBean> stepsRankingListBeen) {
        mContext = context;
        mStepCounterActivity = (StepCounterActivity) mContext;
        mStepsRankingListBeen = stepsRankingListBeen;
    }

    @Override
    public int getCount() {
        if (mStepsRankingListBeen == null) {
            return 0;
        }
        return mStepsRankingListBeen.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_step_counter, null);
            viewHolder.mHead = (CircleImageView) view.findViewById(R.id.iv_head_item);
            viewHolder.mBabyName = (TextView) view.findViewById(R.id.tv_baby_name_item);
            viewHolder.mTvSteps = (TextView) view.findViewById(R.id.tv_steps_item);
            viewHolder.mStepCounter = (TextView) view.findViewById(R.id.tv_step_counter_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(mContext).load(mStepsRankingListBeen.get(i).getImgUrl()).into(viewHolder.mHead);
        viewHolder.mBabyName.setText(mStepsRankingListBeen.get(i).getNickName());
        viewHolder.mTvSteps.setText(String.valueOf(mStepsRankingListBeen.get(i).getSteps()));
        viewHolder.mStepCounter.setText(String.valueOf(i + 1));

        return view;
    }

    public class ViewHolder {
        CircleImageView mHead;
        TextView mBabyName;
        TextView mTvSteps;
        TextView mStepCounter;
    }

    public void setDate(List<ResponseInfoModel.ResultBean.StepsRankingListBean> stepsRankingListBeen) {
        mStepsRankingListBeen = stepsRankingListBeen;
    }
}
