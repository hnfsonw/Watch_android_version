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
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/17 上午11:33
 * 描   述: 新增选择设备的Dialog适配器
 */
public class SelectDialogAdapter extends BaseAdapter {
    private static final java.lang.String TAG = "SelectDialogAdapter";
    private Context mContext;
    private List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceListBeanList;

    public SelectDialogAdapter(Context context, List<ResponseInfoModel.ResultBean.DeviceListBean> deviceListBeanList) {
        mContext = context;
        mDeviceListBeanList = deviceListBeanList;
    }


    @Override
    public int getCount() {
        return mDeviceListBeanList.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
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
                Glide.with(MyApplication.sInstance).load(mDeviceListBeanList.get(position).getImgUrl()).into(viewHolder.ivIcon);
            } else {
                viewHolder.ivIcon.setImageResource(mDeviceListBeanList.get(position).getGender()
                        == 1 ? R.drawable.a : R.drawable.b);
            }

            viewHolder.tvRelationship.setText(mDeviceListBeanList.get(position).getNickName());

            viewHolder.tvPhone.setText(mDeviceListBeanList.get(position).getPhone());


            if (mDeviceListBeanList.get(position).getIsAdmin() == 1) {
                viewHolder.tvAdministrator.setVisibility(View.VISIBLE);
            }

            return convertView;
        } catch (Exception e) {
            LogUtils.d(TAG, "getview 异常 " + e.getMessage());
            return null;
        }

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
