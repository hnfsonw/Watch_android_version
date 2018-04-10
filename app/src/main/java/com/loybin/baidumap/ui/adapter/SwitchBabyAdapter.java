package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.view.CircleImageView;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/22 上午10:01
 * 描   述: 选择宝宝的适配器
 */
public class SwitchBabyAdapter extends BaseAdapter {


    private Context mContext;
    private List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceListBeanList;


    public SwitchBabyAdapter(Context context, List<ResponseInfoModel.ResultBean.DeviceListBean>
            deviceListBeanList) {
        mContext = context;
        mDeviceListBeanList = deviceListBeanList;
    }


    @Override
    public int getCount() {
        return mDeviceListBeanList.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_swicth_baby, null);
            viewHolder = new ViewHolder();
            viewHolder.rlAdmin = (RelativeLayout) convertView.findViewById(R.id.rl_admin);
            viewHolder.ivIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvRelationship = (TextView) convertView.findViewById(R.id.tv_relationship);
            viewHolder.tvAdministrator = (TextView) convertView.findViewById(R.id.tv_administrator);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tvCornet = (TextView) convertView.findViewById(R.id.tv_cornet);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (mDeviceListBeanList.get(position).getImgUrl() != null) {
            Glide.with(mContext).load(mDeviceListBeanList.get(position).getImgUrl()).into(viewHolder.ivIcon);
        }

        viewHolder.tvRelationship.setText(mDeviceListBeanList.get(position).getNickName());

        viewHolder.tvPhone.setText(mDeviceListBeanList.get(position).getPhone());


        if (mDeviceListBeanList.get(position).getIsAdmin() == 1) {
            viewHolder.tvAdministrator.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setData(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList) {
        mDeviceListBeanList.clear();
        mDeviceListBeanList.addAll(deviceList);
        notifyDataSetChanged();

    }


    class ViewHolder {
        RelativeLayout rlAdmin;
        CircleImageView ivIcon;
        TextView tvRelationship;
        TextView tvAdministrator;
        TextView tvPhone;
        TextView tvCornet;

    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }
}
