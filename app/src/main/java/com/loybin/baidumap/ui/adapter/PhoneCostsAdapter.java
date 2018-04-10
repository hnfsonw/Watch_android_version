package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.PhoneCostsBean;
import com.loybin.baidumap.ui.activity.PhoneEnquiryActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UserUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by huangz on 17/8/18.
 */

public class PhoneCostsAdapter extends RecyclerView.Adapter {

    public static final int TYPE_LEFT = 1; //返回的命令
    public static final int TYPE_RIGT = 2; //发送的命令
    public static final int TYPE_RESULT = 3; //结果的命令
    private static final String TAG = "PhoneEnquiryActivity";

    private Context mContext;
    private List<PhoneCostsBean> mCheckList;
    private PhoneEnquiryActivity mPhoneEnquiryActivity;


    public PhoneCostsAdapter(Context mContext, List<PhoneCostsBean> mCheckList) {
        this.mContext = mContext;
        this.mCheckList = mCheckList;
        mPhoneEnquiryActivity = (PhoneEnquiryActivity) mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_LEFT:
                itemView = View.inflate(mContext, R.layout.item_phone_enquiry_response, null);
                return new LeftHolder(itemView);

            case TYPE_RIGT:
                itemView = View.inflate(mContext, R.layout.item_phone_enquiry_command, null);
                return new RightHolder(itemView);

            case TYPE_RESULT:
                itemView = View.inflate(mContext, R.layout.item_phone_enquiry_request, null);
                return new ResponseHolder(itemView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_LEFT:
                LeftHolder leftHolder = (LeftHolder) holder;
                leftHolder.setData(mCheckList.get(position).getContent(), mCheckList.get(position).getImgrul());

                leftHolder.mLeftView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneEnquiryActivity phoneEnquiryActivity = (PhoneEnquiryActivity) mContext;
                        UserUtil.hideSoftInput(phoneEnquiryActivity);
                    }
                });
                break;

            case TYPE_RIGT:
                RightHolder rightHolder = (RightHolder) holder;
                rightHolder.setData(mCheckList.get(position).getContent(), position);
                rightHolder.mRightView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneEnquiryActivity phoneEnquiryActivity = (PhoneEnquiryActivity) mContext;
                        UserUtil.hideSoftInput(phoneEnquiryActivity);
                    }
                });
                break;

            case TYPE_RESULT:
                ResponseHolder responseHolder = (ResponseHolder) holder;
                responseHolder.setData(mCheckList.get(position).getContent(), position);
                responseHolder.mResponse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneEnquiryActivity phoneEnquiryActivity = (PhoneEnquiryActivity) mContext;
                        UserUtil.hideSoftInput(phoneEnquiryActivity);
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (mCheckList != null) {
            return mCheckList.size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        switch (mCheckList.get(position).getCommandType()) {
            case 1:
                return TYPE_LEFT;

            case 2:
                return TYPE_RIGT;

            case 3:
                return TYPE_RESULT;

            default:
                return 0;
        }
    }


    class LeftHolder extends RecyclerView.ViewHolder {

        public final TextView mLeftView;
        public final ImageView mIvReponseIcon;

        public LeftHolder(View itemView) {
            super(itemView);
            mLeftView = (TextView) itemView.findViewById(R.id.tv_check_result);
            mIvReponseIcon = (ImageView) itemView.findViewById(R.id.iv_response_icon);

        }

        public void setData(String data, String imgurl) {
            mLeftView.setText(data);
            if (imgurl != null) {
                Glide.with(mContext).load(imgurl).into(mIvReponseIcon);
            } else {
                if (mPhoneEnquiryActivity.mGender.equals("1")) {
                    mIvReponseIcon.setImageResource(R.drawable.a);
                } else {
                    mIvReponseIcon.setImageResource(R.drawable.b);
                }
            }
        }
    }


    class RightHolder extends RecyclerView.ViewHolder {

        public final TextView mRightView;
        public final TextView mTime;

        public RightHolder(View itemView) {
            super(itemView);
            mRightView = (TextView) itemView.findViewById(R.id.tv_command);
            mTime = (TextView) itemView.findViewById(R.id.tv_request_time);
        }

        public void setData(String data, int position) {
            mRightView.setText(data);
            String time = getDateTime(position);
            if (time == null || "".equals(time)) {
                mTime.setVisibility(View.GONE);
            } else {
                mTime.setVisibility(View.VISIBLE);
                mTime.setText(time);
            }
        }
    }


    class ResponseHolder extends RecyclerView.ViewHolder {

        public final TextView mTime;
        public final TextView mResponse;


        public ResponseHolder(View itemView) {
            super(itemView);
            mTime = (TextView) itemView.findViewById(R.id.tv_response_time);
            mResponse = (TextView) itemView.findViewById(R.id.tv_check_response);
        }

        public void setData(String data, int position) {
//            long l = SystemClock.currentThreadTimeMillis();
            String time = getDateTime(position);

            if (time == null || "".equals(time)) {
                mTime.setVisibility(View.GONE);
            } else {
                mTime.setVisibility(View.VISIBLE);
                mTime.setText(time);
            }

            mResponse.setText(data);
        }
    }


    public String getDateTime(int position) {
        String result = "";

        long time = mCheckList.get(position).getTime();
        long systemTime = new Date().getTime();
        long dateInterval = time / (24 * 60 * 60 * 1000) - systemTime / (24 * 60 * 60 * 1000);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm");

        if (position == 0) {
            String dateTime = format.format(new Date(time));
            result = dateTime;
            return result;

        } else if (dateInterval > 2) {
            String dateTime = format2.format(new Date(systemTime));
            result = dateTime;
            return result;

        } else if (dateInterval > 1) {
            String dateTime = format.format(new Date(systemTime));
            result = "前天  " + dateTime;
            return result;

        } else if (dateInterval > 0) {
            String dateTime = format.format(new Date(systemTime));
            result = "昨天  " + dateTime;
            return result;

        } else {
            long last_time = mCheckList.get(position - 1).getTime();
            String dateTime = format.format(new Date(time));
            long timeInterval = (time - last_time) / 1000;
            if (timeInterval >= 60) {
                LogUtils.e(TAG, "时间差值 :" + timeInterval);
                result = dateTime;
                return result;
            }
        }

        return result;
    }
}
