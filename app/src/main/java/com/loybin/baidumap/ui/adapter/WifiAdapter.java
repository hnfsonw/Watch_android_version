package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ParametersModel;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UserUtil;
import com.loybin.baidumap.widget.DataActionListener;
import com.loybin.baidumap.ui.view.IconTextView;
import com.loybin.baidumap.widget.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Wifi适配器
 */

public class WifiAdapter extends RecyclerView.Adapter {

    private static final String TAG = "WifiSettingsActivity";
    private Context context;

    private List<ParametersModel.WifiDataListBean> mDataList;

    private LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    private DataActionListener dataActionListener;


    public WifiAdapter(Context context, List<ParametersModel.WifiDataListBean> dataList) {
        this.context = context;
        this.mDataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setDataActionListener(DataActionListener dataActionListener) {
        this.dataActionListener = dataActionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_wifi, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            CustomViewHolder viewHolder = (CustomViewHolder) holder;
            ParametersModel.WifiDataListBean data = mDataList.get(position);
            viewHolder.tvWifiName.setText(data.getSsid());
//            if (data.isShowDetail()) {
//                viewHolder.ivDetail.setText(R.string.icon_up);
//                viewHolder.tvWifiDetail.setVisibility(View.VISIBLE);
//                viewHolder.tvWifiDetail.setText(data.getWifiDetail());
//            } else {
            viewHolder.ivDetail.setText(R.string.icon_down);
            viewHolder.tvWifiDetail.setVisibility(View.GONE);
//            }

            LogUtils.e(TAG, "getLocked " + data.getLocked());
            LogUtils.e(TAG, "getSsid " + data.getSsid());
            LogUtils.e(TAG, "getSignal " + data.getSignal());

            if (data.getLocked().equals("on")) {
                viewHolder.ivNeedCode.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivNeedCode.setVisibility(View.INVISIBLE);
            }

            viewHolder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataActionListener != null) {
                        dataActionListener.onShow(position);
                    }
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            if (data.isConnect()) {
                LogUtils.d(TAG, "isConnect + getSsid " + data.getSsid());
                viewHolder.ivIntensity.setTextColor(ContextCompat.getColor(context, R.color.font_green));
            } else {
                viewHolder.ivIntensity.setTextColor(ContextCompat.getColor(context, R.color.font_text));
            }

            int signalLevel = UserUtil.calculateSignalLevel(data.getSignal(), 5);
            LogUtils.d(TAG, "signalLevel   " + signalLevel);
            switch (signalLevel) {
                case 0:
                    viewHolder.ivIntensity.setText(R.string.icon_signal_off);
                    break;

                case 1:
                    viewHolder.ivIntensity.setText(R.string.icon_signal_one);
                    break;

                case 2:
                    viewHolder.ivIntensity.setText(R.string.icon_signal_two);
                    break;

                case 3:
                    viewHolder.ivIntensity.setText(R.string.icon_signal_three);
                    break;

                case 4:
                    viewHolder.ivIntensity.setText(R.string.icon_signal_four);
                    break;

                default:
                    viewHolder.ivIntensity.setText(R.string.icon_signal_four);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public void setData(List<ParametersModel.WifiDataListBean> wifiDataList) {
        mDataList.clear();
        mDataList.addAll(wifiDataList);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvWifiName)
        TextView tvWifiName;

        @BindView(R.id.tvWifiDetail)
        TextView tvWifiDetail;

        @BindView(R.id.ivIntensity)
        IconTextView ivIntensity;

        @BindView(R.id.ivNeedCode)
        IconTextView ivNeedCode;

        @BindView(R.id.ivDetail)
        IconTextView ivDetail;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
