package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.MessageListModel;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/25 下午3:04
 * 描   述: 消息适配器
 */
public class MessageListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageListModel.ResultBean.MessageListBean.ListBean> mList;


    public MessageListAdapter(Context context, List<MessageListModel.ResultBean.MessageListBean.ListBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        ViewHodel viewHodel;
        if (convertView == null) {
            viewHodel = new ViewHodel();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_list, null);
            viewHodel.mTvConten = (TextView) convertView.findViewById(R.id.tv_content);
            viewHodel.mAddress = (TextView) convertView.findViewById(R.id.tv_address);
            viewHodel.mTvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHodel.mIvState = (ImageView) convertView.findViewById(R.id.iv_state);
            viewHodel.mIvLine = (ImageView) convertView.findViewById(R.id.iv_line);
            viewHodel.mIvLine2 = (ImageView) convertView.findViewById(R.id.iv_line2);
            convertView.setTag(viewHodel);
        } else {
            viewHodel = (ViewHodel) convertView.getTag();
        }


        try {
            MessageListModel.ResultBean.MessageListBean.ListBean listBean = mList.get(position);
            String msgBody = listBean.getMsgBody();
            String[] split = msgBody.split("#");
            if (split.length >= 5) {
                viewHodel.mTvConten.setText(split[2] + split[3]);
                viewHodel.mAddress.setVisibility(View.VISIBLE);
                viewHodel.mAddress.setText(split[4]);
                viewHodel.mAddress.setTextColor(Color.BLACK);
            } else {
                viewHodel.mAddress.setVisibility(View.GONE);
                viewHodel.mTvConten.setText(listBean.getMsgBody());
            }

            viewHodel.mTvTime.setText(listBean.getAddTime());

            if (listBean.getMsgStatus() == 0) {
                viewHodel.mIvLine.setBackgroundResource(R.color.title);
                viewHodel.mIvLine2.setBackgroundResource(R.color.title);
            } else {
                viewHodel.mIvLine.setBackgroundResource(R.color.title);
                viewHodel.mIvLine2.setBackgroundResource(R.color.title);
            }
        }catch (Exception e){

        }

            return convertView;

    }

    public void setData(List<MessageListModel.ResultBean.MessageListBean.ListBean> list) {
        mList = list;
    }

    class ViewHodel {
        TextView mTvConten;
        TextView mAddress;
        TextView mTvTime;
        ImageView mIvState;
        ImageView mIvLine;
        ImageView mIvLine2;
    }
}
