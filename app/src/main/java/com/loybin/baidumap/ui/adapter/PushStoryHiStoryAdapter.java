package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.PushStoryHiStoryActivity;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.TimeUtil;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/18 下午2:33
 * 描   述: 故事推送历史的适配
 */
public class PushStoryHiStoryAdapter extends BaseAdapter {
    private static final String TAG = "PushStoryHiStoryActivity";
    private Context mContext;
    private List<ResponseInfoModel.ResultBean.storyListBean> mStoryListBeen;
    private boolean mShow = false;
    private final PushStoryHiStoryActivity mPushStoryHiStoryActivity;


    public PushStoryHiStoryAdapter(Context context, List<ResponseInfoModel.ResultBean.storyListBean> storyList) {
        mContext = context;
        mPushStoryHiStoryActivity = (PushStoryHiStoryActivity) mContext;
        mStoryListBeen = storyList;
    }


    @Override
    public int getCount() {
        return mStoryListBeen.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_story_list, parent, false);
            holder = new ViewHolder();
            holder.content = (ViewGroup) convertView;
            holder.cover = (CircleImageView) convertView.findViewById(R.id.imageview);
            holder.title = (TextView) convertView.findViewById(R.id.trackname);
            holder.storyDuration = (TextView) convertView.findViewById(R.id.tv_story_duration);
            holder.storySize = (TextView) convertView.findViewById(R.id.tv_story_size);
            holder.createTime = (TextView) convertView.findViewById(R.id.tv_create_time);
            holder.storyDownload = (ImageView) convertView.findViewById(R.id.iv_story_download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mShow) {
            holder.storyDownload.setVisibility(View.VISIBLE);
            holder.storyDownload.setImageResource(R.drawable.delete);
        } else {
            holder.storyDownload.setVisibility(View.GONE);
        }

        ResponseInfoModel.ResultBean.storyListBean storyListBean = mStoryListBeen.get(position);

        holder.createTime.setVisibility(View.VISIBLE);
        holder.createTime.setText(storyListBean.getCreateTime());
        holder.title.setText(storyListBean.getStoryName());
        holder.storyDuration.setText(TimeUtil.formatTimes(storyListBean.getStoryTime() * 1000));
        holder.storySize.setText(FileUtils.getFormatSize(storyListBean.getSize()));
        if (storyListBean.getStoryImgUrl() != null) {
            Glide.with(mContext).load(storyListBean.getStoryImgUrl()).into(holder.cover);
        } else {
            holder.cover.setImageResource(R.mipmap.listen_story);
        }

        holder.storyDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                mPushStoryHiStoryActivity.deleteStory(mStoryListBeen.get(position).getStoryId());
                }catch (Exception e){

                }
            }
        });


        return convertView;
    }


    public void setData(List<ResponseInfoModel.ResultBean.storyListBean> storyList) {
        mStoryListBeen = storyList;
    }


    public void logo() {
        setLogo(!mShow);
    }


    private void setLogo(boolean show) {
        mShow = show;
        LogUtils.e(TAG, "setLogo  " + mShow);
        if (mShow) {
            mPushStoryHiStoryActivity.mTvRight.setText(mContext.getString(R.string.cancel));
            notifyDataSetChanged();
        } else {
            mPushStoryHiStoryActivity.mTvRight.setText(mContext.getString(R.string.the_editor));
            notifyDataSetChanged();
        }
    }


    public class ViewHolder {
        public ViewGroup content;
        public CircleImageView cover;
        public TextView title;
        public TextView storyDuration;
        public TextView storySize;
        public TextView createTime;
        public ImageView storyDownload;
    }

}
