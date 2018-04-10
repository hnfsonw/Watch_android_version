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
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/09 上午9:49
 * 描   述: Fragmen列表适配器
 */
public class FragmentItemAdapter extends BaseAdapter {
    private static final String TAG = "FragmentItemAdapter";
    private Context mContext;
    private List<Album> mTrackHotList;

    public FragmentItemAdapter(Context context, List<Album> albums) {
        mContext = context;
        mTrackHotList = albums;
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
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_story, parent, false);
            holder = new ViewHolder();
            holder.content = (ViewGroup) convertView;
            holder.cover = (ImageView) convertView.findViewById(R.id.imageview);
            holder.title = (TextView) convertView.findViewById(R.id.trackname);
            holder.set = (TextView) convertView.findViewById(R.id.tv_set);
//            holder.intro = (TextView) convertView.findViewById(R.id.intro);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Album sound = mTrackHotList.get(position);
        holder.title.setText(sound.getAlbumTitle());
        holder.set.setText(mContext.getString(R.string.total) + sound.getIncludeTrackCount() + mContext.getString(R.string.first));
        long id = sound.getId();
        Log.e(TAG, "id" + id);
//            holder.intro.setText(sound.getAnnouncer() == null ? sound.getTrackTags() : sound.getAnnouncer().getNickname());
        Glide.with(mContext).load(sound.getCoverUrlLarge()).into(holder.cover);
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
        public TextView set;
        public Button downloadStatue;
        public Button pause;
        public CheckBox checkBox;
    }
}
