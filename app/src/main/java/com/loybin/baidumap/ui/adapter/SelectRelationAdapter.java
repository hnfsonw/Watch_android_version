package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hojy.happyfruit.R;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/10 上午10:38
 * 描   述: 选择关系的适配器
 */
public class SelectRelationAdapter extends BaseAdapter {

    private Context mContext;
    String[] mTitles;
    int[] mIcons;

    public SelectRelationAdapter(Context context, String[] titles, int[] icons) {
        mContext = context;
        mTitles = titles;
        mIcons = icons;

    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_select_relation, null);
        }

        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
        TextView tvLabel = (TextView) convertView.findViewById(R.id.tv_title);

        ivIcon.setImageResource(mIcons[position]);
        tvLabel.setText(mTitles[position]);


        return convertView;
    }

    //=========================

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
