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
import com.loybin.baidumap.ui.activity.StoryListActivity;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.TimeUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

/**
 * Created by huangz on 17/8/24.
 */

public class StoryListAdapter extends BaseAdapter {

    private static final String TAG = "StoryListActivity";
    private Context mContext;
    private TrackList mTrackHotList;
    private XmPlayerManager mPlayerManager;

    public StoryListAdapter(Context context, TrackList storyList, XmPlayerManager playerManager) {
        mContext = context;
        mTrackHotList = storyList;
        mPlayerManager = playerManager;
    }


    @Override
    public int getCount() {
        if (mTrackHotList == null || mTrackHotList.getTracks() == null) {
            return 0;
        }
        return mTrackHotList.getTracks().size();
    }

    @Override
    public Object getItem(int position) {
        return mTrackHotList.getTracks().get(position);

    }


    @Override
    public long getItemId(int position) {
        return position;
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
            holder.storyDownload = (ImageView) convertView.findViewById(R.id.iv_story_download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Track sound = mTrackHotList.getTracks().get(position);
        holder.title.setText(sound.getTrackTitle());
        holder.storyDuration.setText(TimeUtil.formatTimes(sound.getDuration() * 1000));

        LogUtils.e(TAG, "isCanDownload  " + sound.isCanDownload());

        LogUtils.e(TAG, "sound.getDownloadSize() " + sound.getDownloadSize());
        LogUtils.e(TAG, "sound.getDownloadedSize() " + sound.getDownloadedSize());
        LogUtils.e(TAG, "sound.getPlaySize64() " + sound.getPlaySize64());
        LogUtils.e(TAG, "sound.getDownloadUrl() " + sound.getDownloadUrl());
        long downloadSize = sound.getPlaySize64();
        if (downloadSize == 0) {
            holder.storyDownload.setVisibility(View.GONE);
            downloadSize = sound.getPlaySize32();
        }
        holder.storySize.setText(FileUtils.getFormatSize(downloadSize));
        if (sound.getCoverUrlLarge() != null) {
            Glide.with(mContext).load(sound.getCoverUrlLarge()).into(holder.cover);
        } else {
            holder.cover.setImageResource(R.mipmap.listen_story);
        }

        holder.storyDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryListActivity storyListActivity = (StoryListActivity) mContext;
                long downloadSize = sound.getPlaySize64();
                if (downloadSize == 0) {
                    downloadSize = sound.getPlaySize32();
                }
                storyListActivity.storyDownload(sound.getDataId(), sound.getDuration(),
                        sound.getCoverUrlSmall(), sound.getTrackTitle(), downloadSize, sound.getPlayUrl64());
            }
        });
//        x.image().bind(holder.cover, sound.getCoverUrlLarge());
//            PlayableModel curr = mPlayerManager.getCurrSound();
//            if (sound.equals(curr)) {
//                holder.content.setBackgroundResource(R.color.title);
//            } else {
//                holder.content.setBackgroundColor(Color.WHITE);
//            }
        return convertView;
    }


    public void setData(TrackList trackHotList) {
        mTrackHotList = trackHotList;
    }


    public class ViewHolder {
        public ViewGroup content;
        public CircleImageView cover;
        public TextView title;
        public TextView storyDuration;
        public TextView storySize;
        public ImageView storyDownload;
    }

}
