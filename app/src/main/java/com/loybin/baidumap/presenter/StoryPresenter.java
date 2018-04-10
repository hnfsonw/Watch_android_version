package com.loybin.baidumap.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.loybin.baidumap.ui.activity.StoryActivity;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/09 下午4:38
 * 描   述: 添加设备view
 */
public class StoryPresenter {
    private static final String TAG = "StoryActivity";
    private Context mContext;
    private StoryActivity mStoryActivity;
    private Gson mGson;
    private String mToJson;
    private int mId;
    private String mKey;
    private int mAb;

    public StoryPresenter(Context context, StoryActivity storyActivity) {
        mContext = context;
        mStoryActivity = storyActivity;
    }


}
