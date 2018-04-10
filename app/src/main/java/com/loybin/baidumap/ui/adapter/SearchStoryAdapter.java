package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.LogUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/28 下午2:56
 * 描   述: 搜索专辑适配器
 */
public class SearchStoryAdapter extends BaseAdapter {
    private static final String TAG = "SearchStoryActivity";
    private Context mContext;
    private List<Album> mAlba;
    private List<Album> mData;

    public SearchStoryAdapter(Context context, List<Album> albums) {
        mContext = context;
        mAlba = albums;
    }

    @Override
    public int getCount() {
        return mAlba.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_story, parent, false);
            holder = new ViewHolder();
            holder.content = (ViewGroup) convertView;
            holder.cover = (CircleImageView) convertView.findViewById(R.id.imageview);
            holder.title = (TextView) convertView.findViewById(R.id.trackname);
            holder.storyDuration = (TextView) convertView.findViewById(R.id.tv_story_duration);
            holder.storySize = (TextView) convertView.findViewById(R.id.tv_story_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final Album sound = mAlba.get(position);
        LogUtils.e(TAG, "是否付费 " + sound.isPaid());

        if (!sound.isPaid()) {
            holder.title.setText(sound.getAlbumTitle());
            holder.storyDuration.setText(sound.getAlbumIntro());
            if (sound.getCoverUrlLarge() != null) {
                Glide.with(mContext).load(sound.getCoverUrlLarge()).into(holder.cover);
            }
        }


        return convertView;
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(List<Album> data) {
        mData = data;
    }

    public class ViewHolder {
        public ViewGroup content;
        public CircleImageView cover;
        public TextView title;
        public TextView storyDuration;
        public TextView storySize;
    }
}
