package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.util.LogUtils;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/23 下午2:21
 * 描   述: 消息中心适配器
 */
public class MessageCenterAdapter extends BaseAdapter {

    private static final java.lang.String TAG = "MessageCenterActivity";
    private Context mContext;
    private List<ResponseInfoModel.ResultBean.MessageListBean> mMessageListBeen;

    public MessageCenterAdapter(Context context, List<ResponseInfoModel.ResultBean.MessageListBean> messageList) {
        mContext = context;
        mMessageListBeen = messageList;
    }

    @Override
    public int getCount() {
        LogUtils.d(TAG, "getCount " + mMessageListBeen.size());
        return mMessageListBeen.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_measage_center, null);
            viewHolder = new ViewHolder();
            viewHolder.mIvIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.mIvunread = (ImageView) convertView.findViewById(R.id.iv_unread);
            viewHolder.mTvContent = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.mTvMessageType = (TextView) convertView.findViewById(R.id.tv_message_type);
            viewHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ResponseInfoModel.ResultBean.MessageListBean messageListBean = mMessageListBeen.get(position);
        LogUtils.e(TAG, "getLastMessage " + messageListBean.getLastMessage());

        viewHolder.mTvMessageType.setText(messageListBean.getTypeDesc());
        if (messageListBean.getLastMessage() != null && messageListBean.getLastMessage().length() > 1) {
            viewHolder.mTvContent.setText(messageListBean.getLastMessage());
        } else {
            viewHolder.mTvContent.setText("暂无通知消息");
        }
        viewHolder.mTvTime.setText(messageListBean.getLastMessageAddTime());
        switch (messageListBean.getId()) {
            case 1:
                viewHolder.mIvIcon.setImageResource(R.mipmap.safety_areas);
                break;

            case 2:
                viewHolder.mIvIcon.setImageResource(R.mipmap.soss);
                if (messageListBean.getLastMessage() != null) {
                    String lastMessage = messageListBean.getLastMessage();
                    String[] split = lastMessage.split("#");
                    if (split.length >= 3) {
                        viewHolder.mTvContent.setText(split[2] + split[3]);
                    }
                    for (int i = 0; i < split.length; i++) {
                        LogUtils.e(TAG, split[i] + " == " + i);
                    }
                }
                break;

            case 3:
                viewHolder.mIvIcon.setImageResource(R.mipmap.calls);
                break;

            case 4:
                viewHolder.mIvIcon.setImageResource(R.mipmap.notice_lbss);
                break;

            case 5:
                viewHolder.mIvIcon.setImageResource(R.mipmap.off_ons);
                break;

            default:
                break;
        }

        if (messageListBean.getUnReadNum() == 0) {
            viewHolder.mIvunread.setVisibility(View.GONE);
        } else {
            viewHolder.mIvunread.setVisibility(View.VISIBLE);
        }


        return convertView;
    }


    public void setData(List<ResponseInfoModel.ResultBean.MessageListBean> messageList) {
        mMessageListBeen.clear();
        mMessageListBeen.addAll(messageList);
    }


    class ViewHolder {
        ImageView mIvIcon;
        TextView mTvMessageType;
        TextView mTvContent;
        TextView mTvTime;
        ImageView mIvunread;
    }
}
