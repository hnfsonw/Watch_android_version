package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.ui.activity.StoryListActivity;
import com.loybin.baidumap.ui.fragment.RecommendedsFramgent;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by huangz on 17/8/24.
 */

public class StoryHistoryAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MainActivity";
    private Context mContext;
    private List<Album> mTrackHotList;
    private Intent mIntent;
    private RecommendedsFramgent mFramgent;

    public StoryHistoryAdapter(Context context, List<Album> trackHotList) {
        mContext = context;
        mTrackHotList = trackHotList;
    }


    public StoryHistoryAdapter(Context context, List<Album> trackHotList, RecommendedsFramgent framgent) {
        mContext = context;
        mTrackHotList = trackHotList;
        mFramgent = framgent;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_history_story, parent, false);
        StoryViewHolder viewHolder = new StoryViewHolder(inflate);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        StoryViewHolder holder1 = (StoryViewHolder) holder;
        holder1.setData(position);
        holder1.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mIntent == null) {
                        mIntent = new Intent(mContext, StoryListActivity.class);
                    }
                    Album album = mTrackHotList.get(position);
                    LogUtils.e(TAG, "onItemClick" + album.getId());
                    mIntent.putExtra("id", album.getId());
                    mIntent.putExtra("title", album.getAlbumTitle());
                    mIntent.putExtra("imageUrl", album.getCoverUrlLarge());
                    mIntent.putExtra("intro", album.getAlbumIntro());
                    mIntent.putExtra("count", album.getIncludeTrackCount() + "");

                    mFramgent.startActivity(mIntent);
                } catch (Exception e) {
                    LogUtils.e(TAG, "onItemClick 异常 " + e.getMessage());
                }
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        if (mTrackHotList.size() > 10) {
            return 10;
        }
        return mTrackHotList.size();
    }


    public void setData(List<Album> trackHotList) {
        mTrackHotList = trackHotList;
    }


    public class StoryViewHolder extends RecyclerView.ViewHolder {
        public final View mItemView;
        public ImageView cover;
        public TextView title;
        public TextView intro;
        public TextView status;


        public StoryViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            cover = (ImageView) itemView.findViewById(R.id.imageview);
            title = (TextView) itemView.findViewById(R.id.trackname);
        }

        public void setData(int position) {
            Album sound = mTrackHotList.get(position);

            title.setText(sound.getAlbumTitle());
            long id = sound.getId();
            Log.e(TAG, "id" + id);
//            holder.intro.setText(sound.getAnnouncer() == null ? sound.getTrackTags() : sound.getAnnouncer().getNickname());
            if (sound.getCoverUrlLarge() != null) {
                Glide.with(mContext).load(sound.getCoverUrlLarge()).into(cover);
            }
        }
    }

}
