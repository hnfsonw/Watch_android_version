package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.view.CircleImageView;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 下午4:45
 * 描   述: 手表通讯录的适配器
 */
public class WatchBookAdapter extends BaseAdapter {

    private static final String TAG = "WatchLeisureActivity";
    private Context mContext;
    private String mIsAdmin;

    private List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;

    public String[] mTitles = {
            "爸爸", "妈妈", "爷爷", "奶奶",
            "外公", "外婆", "伯父", "伯母",
            "叔叔", "阿姨", "哥哥", "姐姐",
            "自定义"
    };

    public int[] mIcons = {
            R.mipmap.father, R.mipmap.mother, R.mipmap.grandfather, R.mipmap.grandma,
            R.mipmap.grandpa, R.mipmap.grandmother, R.mipmap.nuncle, R.mipmap.aunt,
            R.mipmap.uncle, R.mipmap.auntie, R.mipmap.elder_brother, R.mipmap.elder_sister,
            R.mipmap.other3x
    };

    public WatchBookAdapter(Context context, List<ResponseInfoModel.ResultBean.MemberListBean> list, String isAdmin) {
        mContext = context;
        mMemberList = list;
        mIsAdmin = isAdmin;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_select_user, null);
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

        String relation = mMemberList.get(position).getRelation();
        boolean custom = true;
        for (int i = 0; i < mTitles.length; i++) {
            if (relation.equals(mTitles[i])) {
                viewHolder.ivIcon.setImageResource(mIcons[i]);
                custom = false;
                break;
            }
        }
        if (custom == true) {
            int i = mTitles.length - 1;
            viewHolder.ivIcon.setImageResource(mIcons[i]);
        }

        viewHolder.tvRelationship.setText(mMemberList.get(position).getRelation());

        viewHolder.tvPhone.setText(mMemberList.get(position).getPhone());

        if (mMemberList.get(position).getShortPhone() != null && mMemberList.get(position).getShortPhone().length() >= 1) {
            viewHolder.tvRelationshipShortPhone.setText(mMemberList.get(position).getShortPhone());
            viewHolder.tvRelationshipShortPhone.setTextColor(mContext.getResources().getColor(R.color.phone_black));
        } else {
            viewHolder.tvRelationshipShortPhone.setText(mContext.getString(R.string.is_not_set));
            viewHolder.tvRelationshipShortPhone.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }

        Log.d(TAG, "getView: " + mMemberList.get(position).getIsAdmin() + "position" + position);
        if (mMemberList.get(position).getIsAdmin().equals("1")) {
            viewHolder.tvAdministrator.setVisibility(View.VISIBLE);
            viewHolder.tvBinding.setVisibility(View.GONE);
            viewHolder.ivWatch.setVisibility(View.VISIBLE);
            viewHolder.tvNotRegister.setVisibility(View.GONE);
        } else {
            viewHolder.tvAdministrator.setVisibility(View.GONE);
        }

        if (mIsAdmin.equals("1")) {
            //是否已注册 0 未注册APP 1 已注册APP
            if (mMemberList.get(position).getAcountType().equals("0")) {
                viewHolder.tvBinding.setVisibility(View.GONE);
                viewHolder.ivWatch.setVisibility(View.GONE);
                viewHolder.tvNotRegister.setVisibility(View.VISIBLE);

            } else {
                //已注册 是否已绑定设备0 否   1 是
                if (mMemberList.get(position).getIsBind().equals("1")) {
                    viewHolder.tvBinding.setVisibility(View.GONE);
                    viewHolder.ivWatch.setImageResource(R.mipmap.watch_bound);
                    viewHolder.ivWatch.setVisibility(View.VISIBLE);
                    viewHolder.tvNotRegister.setVisibility(View.GONE);
                } else {
                    if (mMemberList.get(position).getStatus().equals("1")) {
                        viewHolder.tvBinding.setVisibility(View.VISIBLE);
                        viewHolder.tvBinding.setText(mContext.getString(R.string.apply_for_the_binding));
                        viewHolder.ivWatch.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.tvBinding.setVisibility(View.VISIBLE);
                        viewHolder.ivWatch.setVisibility(View.VISIBLE);
                        viewHolder.ivWatch.setImageResource(R.mipmap.watch_unbound);
                        viewHolder.tvNotRegister.setVisibility(View.GONE);
                    }
                }
            }
        }


        return convertView;
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
    public void setData(List<ResponseInfoModel.ResultBean.MemberListBean> data) {
        mMemberList.clear();
        mMemberList.addAll(data);
        notifyDataSetChanged();
    }
}
