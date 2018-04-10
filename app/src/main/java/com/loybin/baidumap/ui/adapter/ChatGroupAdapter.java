package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.LogUtils;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/05 下午5:03
 * 描   述: 群聊列表的适配器
 */
public class ChatGroupAdapter extends BaseAdapter {
    private static final String TAG = "ChatGroupActivity";
    private List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private Context mContext;
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

    public ChatGroupAdapter(Context context, List<ResponseInfoModel.ResultBean.MemberListBean> memberList) {
        this.mContext = context;
        this.mMemberList = memberList;
    }

    @Override
    public int getCount() {
        return mMemberList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_group, null);
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

        String relation = mMemberList.get(position).getRelation();
        LogUtils.e(TAG, "relation " + relation);

        for (int i = 0; i < mTitles.length; i++) {
            if (relation.equals(mTitles[i])) {
                viewHolder.ivIcon.setImageResource(mIcons[i]);
            } else if (relation.length() > 12) {
                String imgUrl = mMemberList.get(position).getImgUrl();
                if (imgUrl != null) {
                    Glide.with(mContext).load(imgUrl).into(viewHolder.ivIcon);
                } else {
                    viewHolder.ivIcon.setImageResource(mMemberList.get(position).getGender()
                            == 1 ? R.drawable.a : R.drawable.b);
                }
            }
        }

        if (mMemberList.get(position).acountType.equals("2")) {
            viewHolder.tvRelationship.setText(mMemberList.get(position).nickName);
        } else {
            viewHolder.tvRelationship.setText(mMemberList.get(position).relation);
        }

        if (mMemberList.get(position).getIsAdmin().equals("1")) {
            viewHolder.tvAdministrator.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvAdministrator.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setData(List<ResponseInfoModel.ResultBean.MemberListBean> memberList) {
        mMemberList.clear();
        mMemberList.addAll(memberList);
        notifyDataSetChanged();

    }

    class ViewHolder {
        RelativeLayout rlAdmin;
        ImageView ivIcon;
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
