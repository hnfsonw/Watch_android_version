package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.SelectUserActivity;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:27
 * 描   述: 选择用户的适配器
 */
public class SelectUserAdapter extends BaseAdapter {

    private Context mContext;

    private List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;

    public String[] mTitles = {
            "爸爸", "妈妈", "爷爷", "奶奶",
            "外公", "外婆", "伯父", "伯母",
            "叔叔", "阿姨", "哥哥", "姐姐",
            "自定义"
    };

    public int[] mIcons = {
            R.mipmap.fatcher, R.mipmap.mother, R.mipmap.grandfather, R.mipmap.grandma,
            R.mipmap.grandpa, R.mipmap.grandmother, R.mipmap.nuncle, R.mipmap.aunt,
            R.mipmap.uncle, R.mipmap.auntie, R.mipmap.elder_brother, R.mipmap.elder_sister,
            R.mipmap.other3x
    };
    private SelectUserActivity mSelectUserActivity;


    public SelectUserAdapter(Context context, List<ResponseInfoModel.ResultBean.MemberListBean> list) {
        mContext = context;
        mMemberList = list;
        mSelectUserActivity = (SelectUserActivity) mContext;
    }


    @Override
    public int getCount() {
        return mMemberList.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_user, null);
            viewHolder = new ViewHolder();
            viewHolder.rlAdmin = (LinearLayout) convertView.findViewById(R.id.rl_admin);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvRelationship = (TextView) convertView.findViewById(R.id.tv_relationship);
            viewHolder.tvAdministrator = (TextView) convertView.findViewById(R.id.tv_administrator);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tvCornet = (TextView) convertView.findViewById(R.id.tv_cornet);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //解绑普通成员
        if (mSelectUserActivity.mOrdinary.equals("ordinary")) {
            if (mMemberList.get(position).isAdmin.equals("1")) {
                viewHolder.rlAdmin.setVisibility(View.GONE);
            }
            if (mMemberList.get(position).imgUrl == null) {
                String relation = mMemberList.get(position).relation;

                for (int i = 0; i < mTitles.length; i++) {
                    if (relation.equals(mTitles[i])) {
                        viewHolder.ivIcon.setImageResource(mIcons[i]);
                    }
                }
            } else {
                Glide.with(mContext).load(mMemberList.get(position).imgUrl).into(viewHolder.ivIcon);
            }

            viewHolder.tvRelationship.setText(mMemberList.get(position).relation);
            viewHolder.tvPhone.setText(mMemberList.get(position).acountName);

            if (mMemberList.get(position).isAdmin.equals("1")) {
                viewHolder.tvAdministrator.setVisibility(View.VISIBLE);
            }
        } else {
            if (mMemberList.get(position).isAdmin.equals("1")) {
                viewHolder.rlAdmin.setVisibility(View.GONE);
            }

            if (mMemberList.get(position).imgUrl == null) {
                String relation = mMemberList.get(position).relation;

                for (int i = 0; i < mTitles.length; i++) {
                    if (relation.equals(mTitles[i])) {
                        viewHolder.ivIcon.setImageResource(mIcons[i]);
                    }
                }
            } else {
                Glide.with(mContext).load(mMemberList.get(position).imgUrl).into(viewHolder.ivIcon);
            }

            viewHolder.tvRelationship.setText(mMemberList.get(position).relation);
            viewHolder.tvPhone.setText(mMemberList.get(position).acountName);

            if (mMemberList.get(position).isAdmin.equals("1")) {
                viewHolder.tvAdministrator.setVisibility(View.VISIBLE);
            }
        }


        return convertView;
    }

    public void setData(List<ResponseInfoModel.ResultBean.MemberListBean> memberList) {
        mMemberList.clear();
        mMemberList.addAll(memberList);
        notifyDataSetChanged();
    }


    class ViewHolder {
        LinearLayout rlAdmin;
        ImageView ivIcon;
        TextView tvRelationship;
        TextView tvAdministrator;
        TextView tvPhone;
        TextView tvCornet;

    }
}
