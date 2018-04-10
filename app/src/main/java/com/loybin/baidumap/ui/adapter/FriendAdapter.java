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
import com.loybin.baidumap.ui.fragment.FriendFragment;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.MyApplication;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 下午4:45
 * 描   述: 手表朋友的适配器
 */
public class FriendAdapter extends BaseAdapter {

    private static final String TAG = "WatchLeisureActivity";
    private Context mContext;
    private String mIsAdmin;
    private FriendFragment mFriendFragment;

    private List<ResponseInfoModel.ResultBean.FriendsListBean> mMemberList;
    private boolean mIsEditor;


    public FriendAdapter(Context context, List<ResponseInfoModel.ResultBean.FriendsListBean> list,
                         String isAdmin, FriendFragment friendFragment) {
        mContext = context;
        mMemberList = list;
        mIsAdmin = isAdmin;
        mFriendFragment = friendFragment;
    }


    @Override
    public int getCount() {
        return mMemberList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friend_user, null);
            viewHolder = new ViewHolder();
            viewHolder.rlAdmin = (LinearLayout) convertView.findViewById(R.id.rl_admin);
            viewHolder.ivIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvRelationship = (TextView) convertView.findViewById(R.id.tv_relationship);
            viewHolder.tvAdministrator = (TextView) convertView.findViewById(R.id.tv_administrator);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tvBinding = (TextView) convertView.findViewById(R.id.iv_binding);
            viewHolder.tvNotRegister = (TextView) convertView.findViewById(R.id.tv_not_register);
            viewHolder.tvRelationshipShortPhone = (TextView) convertView.findViewById(R.id.tv_relationship_shortPhone);
            viewHolder.ivWatch = (ImageView) convertView.findViewById(R.id.iv_watch);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ResponseInfoModel.ResultBean.FriendsListBean friendsListBean = mMemberList.get(position);

        if (friendsListBean.getImgUrl() != null) {
            Glide.with(MyApplication.sInstance).load(friendsListBean.getImgUrl()).into(viewHolder.ivIcon);
        } else {
            if (friendsListBean.getGender() == 1) {
                viewHolder.ivIcon.setImageResource(R.drawable.a);
            } else {
                viewHolder.ivIcon.setImageResource(R.drawable.b);
            }
        }


        if (mIsEditor) {
            viewHolder.ivWatch.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivWatch.setVisibility(View.GONE);
        }

        viewHolder.tvRelationship.setText(friendsListBean.getNickName());
        viewHolder.tvPhone.setText(friendsListBean.getPhone());

        viewHolder.ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriendFragment.deleteFriend(friendsListBean.getAcountName());
            }
        });

        return convertView;
    }

    public void setBoolean(boolean isEditor) {
        mIsEditor = isEditor;
    }


    class ViewHolder {
        LinearLayout rlAdmin;
        CircleImageView ivIcon;
        TextView tvRelationship;
        TextView tvAdministrator;
        TextView tvRelationshipShortPhone;
        TextView tvPhone;
        TextView tvBinding;
        TextView tvNotRegister;
        ImageView ivWatch;

    }


    /**
     * 网络请求回来设置新的数据集
     *
     * @param data
     */
    public void setData(List<ResponseInfoModel.ResultBean.FriendsListBean> data) {
        mMemberList.clear();
        mMemberList.addAll(data);
        notifyDataSetChanged();
    }
}
