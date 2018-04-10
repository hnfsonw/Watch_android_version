package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.GeoFenceModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.GeoFenceListActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 安全区域的适配器
 */
public class GeoFenceListAdapter extends BaseAdapter {
    private static final String TAG = "GeoFenceListActivity";
    private List<GeoFenceModel> list = null;
    private Context mContext;
    public boolean mIsToggle = false; //默认就是开着
    private boolean mShow = false;
    private GeoFenceListActivity mGeoFenceListActivity;
    private List<ResponseInfoModel.ResultBean.ResultDataBean> mResultData;
    private static final int STATE_ONS = 1;
    private static final int STATE_ZERO = 0;
    private int mFenceId;

    public GeoFenceListAdapter(Context mContext, List<GeoFenceModel> list) {
        this.mContext = mContext;
        this.list = list;
        mGeoFenceListActivity = (GeoFenceListActivity) mContext;
        mResultData = new ArrayList<>();
    }


    public int getCount() {
        return mResultData.size() > 0 ? mResultData.size() : list.size();
    }


    public Object getItem(int position) {
        return mResultData.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            LogUtils.d(TAG, "view == null");
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_geofence_list, null);
            viewHolder.tvName = (TextView) view.findViewById(R.id.geo_gence_name);
            viewHolder.tvAddress = (TextView) view.findViewById(R.id.geo_gence_address);
            viewHolder.ivSwitch = (ImageView) view.findViewById(R.id.iv_switch);
            viewHolder.ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (mResultData.size() > 0) {
            viewHolder.tvAddress.setVisibility(View.VISIBLE);
            viewHolder.tvName.setText(mResultData.get(position).name);
            viewHolder.tvAddress.setText(mResultData.get(position).desc);
            LogUtils.e(TAG, "getView  " + mShow);
            viewHolder.ivSwitch.setImageResource(mResultData.get(position).state == 1
                    ? R.mipmap.on : R.mipmap.off);
            viewHolder.ivSwitch.setVisibility(mShow ? View.GONE : View.VISIBLE);

            viewHolder.ivDelete.setVisibility(mShow ? View.VISIBLE : View.GONE);


        } else {
            LogUtils.d(TAG, "getView11111111: " + mShow);
            viewHolder.tvName.setText(list.get(position).FenceName);
            viewHolder.tvAddress.setText(list.get(position).Address);
            viewHolder.ivSwitch.setVisibility(mShow ? View.GONE : View.GONE);
            viewHolder.ivDelete.setVisibility(mShow ? View.GONE : View.GONE);
        }


        viewHolder.ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG, "ivSwitch  onClick " + mShow);
                if (mShow) {
                    return;
                }
                ImageView iv = (ImageView) v;
                Log.e("GeoFenceListActivity", "onClick: " + "开关" + position);
                ResponseInfoModel.ResultBean.ResultDataBean resultDataBean = mResultData.get(position);
                if (resultDataBean.state == STATE_ONS) {
                    mIsToggle = true;
                } else {
                    mIsToggle = false;
                }
                toggle(iv);
                Log.d("GeoFenceListActivity", "mIsToggle: " + getToggle());
                if (getToggle()) {
                    //true是开
                    ViewHolder.ivDelete.setVisibility(View.GONE);
                    resultDataBean.state = STATE_ONS;
                    mGeoFenceListActivity.changeState(resultDataBean);
                } else {
                    //false是关
                    ViewHolder.ivDelete.setVisibility(View.GONE);
                    mResultData.get(position).state = STATE_ZERO;
                    mGeoFenceListActivity.changeState(resultDataBean);
                }
            }
        });


        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(TAG, "ivDelete  onClick " + mShow);
                if (!mShow) {
                    return;
                }
                Log.e("GeoFenceListActivity", "onClick: " + "删除" + position);
                ResponseInfoModel.ResultBean.ResultDataBean resultDataBean = mResultData.get(position);
                if (mResultData.size() > 0) {
                    ViewHolder.ivSwitch.setVisibility(View.GONE);
                    mGeoFenceListActivity.playDialog(resultDataBean);
                    mGeoFenceListActivity.setDeleteGeoFence(new GeoFenceListActivity.DeleteGeoFence() {
                        @Override
                        public void deleteGeo() {
                            mResultData.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                } else {
                    list.remove(position);
                    notifyDataSetChanged();
                }


            }
        });

        //围栏开关

        return view;

    }

    static class ViewHolder {
        TextView tvName;
        TextView tvAddress;
        static ImageView ivSwitch;
        static ImageView ivDelete;
    }


    public void logo() {
        setLogo(!mShow);
    }


    public void setLogo(boolean isShow) {
        mShow = isShow;
        LogUtils.e(TAG, "setLogo  " + mShow);
        if (isShow) {
            mGeoFenceListActivity.mTvRight.setText(mContext.getString(R.string.cancel));
            notifyDataSetChanged();
        } else {
            mGeoFenceListActivity.mTvRight.setText(mContext.getString(R.string.the_editor));
            notifyDataSetChanged();
        }

    }

    public void setData(List<ResponseInfoModel.ResultBean.ResultDataBean> resultData) {
        mResultData.clear();
        mResultData.addAll(resultData);
    }

    /**
     * 设置开关状态
     *
     * @param isToggle true : 打开开关， false , 关闭开关
     */
    public void setToggle(boolean isToggle, ImageView ivSwitch) {
        mIsToggle = isToggle;
        if (isToggle) {
            ivSwitch.setImageResource(R.mipmap.on);
        } else {
            ivSwitch.setImageResource(R.mipmap.off);
        }
    }

    /**
     * 用于返回当前开关的状态
     *
     * @return true :开关是开着， false : 开关是关着
     */
    public boolean getToggle() {
        return mIsToggle;
    }


    public void toggle(ImageView iv) {
        setToggle(!mIsToggle, iv);
    }


}