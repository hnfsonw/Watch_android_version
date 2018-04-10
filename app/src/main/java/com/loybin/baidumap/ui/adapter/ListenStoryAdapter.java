package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by huangz on 17/8/24.
 */

public class ListenStoryAdapter extends BaseAdapter {

    private static final String TAG = "MainActivity";
    private Context mContext;
    private List<Album> mTrackHotList;

    public ListenStoryAdapter(Context context, List<Album> trackHotList) {
        mContext = context;
        mTrackHotList = trackHotList;
    }


    @Override
    public int getCount() {
        if (mTrackHotList == null) {
            return 0;
        }
        return mTrackHotList.size();
    }


    @Override
    public Object getItem(int position) {
        return mTrackHotList.get(position);

    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listen_story, parent, false);
            holder = new ViewHolder();
            holder.content = (ViewGroup) convertView;
            holder.cover = (ImageView) convertView.findViewById(R.id.imageview);
            holder.title = (TextView) convertView.findViewById(R.id.trackname);
//            holder.intro = (TextView) convertView.findViewById(R.id.intro);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Album sound = mTrackHotList.get(position);
        holder.title.setText(sound.getAlbumTitle());
        long id = sound.getId();
        Log.e(TAG, "id" + id);
//            holder.intro.setText(sound.getAnnouncer() == null ? sound.getTrackTags() : sound.getAnnouncer().getNickname());
        if (sound.getCoverUrlLarge() != null) {
            Glide.with(mContext).load(sound.getCoverUrlLarge()).into(holder.cover);
        }
//        x.image().bind(holder.cover, sound.getCoverUrlLarge());
//        PlayableModel curr = mPlayerManager.getCurrSound();
//        if (sound.equals(curr)) {
//            holder.content.setBackgroundResource(R.color.selected_bg);
//        } else {
//            holder.content.setBackgroundColor(Color.WHITE);
//        }
        return convertView;
    }

    public void setData(List<Album> trackHotList) {
        mTrackHotList = trackHotList;
    }


    public class ViewHolder {
        public ViewGroup content;
        public ImageView cover;
        public TextView title;
        public TextView intro;
        public TextView status;
        public Button downloadStatue;
        public Button pause;
        public CheckBox checkBox;
    }

}
