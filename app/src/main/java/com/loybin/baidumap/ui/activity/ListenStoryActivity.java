package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.EventPlayStartModel;
import com.loybin.baidumap.model.EventPlayStopModel;
import com.loybin.baidumap.presenter.ListenStoryPresenter;
import com.loybin.baidumap.service.PlayService;
import com.loybin.baidumap.service.PlayServiceStub;
import com.loybin.baidumap.ui.adapter.ListenStoryAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.widget.chatrow.Constant;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.customized.ColumnAlbumItem;
import com.ximalaya.ting.android.opensdk.model.customized.CustomizedAlbumColumnDetail;
import com.ximalaya.ting.android.opensdk.model.customized.XmCustomizedModelUtil;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by huangz on 17/8/24.
 * 故事专辑列表 view
 */

public class ListenStoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ListenStoryActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.gv_listen_story)
    GridView mGvListenStory;

    @BindView(R.id.iv_music)
    ImageView mIvMusic;

    @BindView(R.id.tv_there)
    TextView mTvThere;

    @BindView(R.id.ll_there)
    LinearLayout mLlThere;


    private Map<String, Object> mStoryItem;
    private ListenStoryAdapter mListenStoryAdapter;
    private List<Album> mAlbums;
    private boolean mLoading;
    private int mPageId = 0;
    private Intent mIntent;
    public PlayServiceStub mService;
    private AnimationDrawable mDrawable;
    private XmPlayerManager mPlayerManager;
    private ListenStoryPresenter mListenStoryPresenter;
    private Integer mPosition;
    private TrackList mTrackList;
    private boolean mPlaying;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_listen_story;
    }


    @Override
    protected void init() {
        if (mListenStoryPresenter == null) {
            mListenStoryPresenter = new ListenStoryPresenter(this, this);
        }
        EventBus.getDefault().register(this);
        mPlayerManager = XmPlayerManager.getInstance(this);
        mPlaying = mPlayerManager.isPlaying();
        initView();
        initListener();
        initData();
        initService();
        mListenStoryPresenter.loadData();

    }


    private void initData() {
        if (mListenStoryAdapter == null) {
            mListenStoryAdapter = new ListenStoryAdapter(this, mAlbums);
        }
        mGvListenStory.setAdapter(mListenStoryAdapter);

        mPosition = (Integer) SharedPreferencesUtils.getParam(this, "position", 0);
        String track = loadDataFromLocal("TrackList", Constant.PROTOCOL_TIMEOUT_MONTH);
        LogUtils.e(TAG, "position" + mPosition);
        LogUtils.e(TAG, "trackList" + track);
//
        if (track != null) {
            Gson gson = new Gson();
            mTrackList = gson.fromJson(track, TrackList.class);
        } else {
            mLlThere.setVisibility(View.GONE);
        }

        if (mTrackList != null) {
            mLlThere.setVisibility(View.VISIBLE);
            mTvThere.setText(getString(R.string.there_is) + mTrackList.getTracks().get(mPosition).getTrackTitle());
            if (mPlaying) {
                mLlThere.setVisibility(View.GONE);
            } else {
                mLlThere.setVisibility(View.VISIBLE);
            }
        }


    }


    private void initService() {
        LogUtils.e(TAG, "initService!! " + Thread.currentThread().getName());
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayerManager.isPlaying()) {
            //正在播放
            mIvMusic.setVisibility(View.VISIBLE);
            mIvMusic.setImageResource(R.drawable.music);
            mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
            mDrawable.start();
        } else {
            mIvMusic.setVisibility(View.GONE);
        }
    }


    private void initView() {
        mTvTitle.setText("听故事");
        if (mPlayerManager.isPlaying()) {
            //正在播放
            mIvMusic.setVisibility(View.VISIBLE);
            mIvMusic.setImageResource(R.drawable.music);
            mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
            mDrawable.start();
        } else {
            mIvMusic.setVisibility(View.GONE);
        }
    }


    private void initListener() {
        mGvListenStory.setOnItemClickListener(this);
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    //暂停了播放通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStop(EventPlayStopModel playModel) {
        mIvMusic.setVisibility(View.GONE);
    }


    //正在播放的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(EventPlayStartModel startModel) {
        mLlThere.setVisibility(View.GONE);
        mIvMusic.setVisibility(View.VISIBLE);
        mIvMusic.setImageResource(R.drawable.music);
        mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
        mDrawable.start();
    }

    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.iv_music, R.id.ll_there})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_music:
                toActivity(StoryPlayerActivity.class);
                break;

            case R.id.ll_there:
                mPlayerManager.playList(mTrackList, mPosition);
                toActivity(StoryPlayerActivity.class);
                mLlThere.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        LogUtils.e(TAG, "onItemClick" + mAlbums.get(position).getId());
        if (mIntent == null) {
            mIntent = new Intent(this, StoryListActivity.class);
        }
        mIntent.putExtra("id", mAlbums.get(position).getId());
        mIntent.putExtra("title", mAlbums.get(position).getAlbumTitle());
        mIntent.putExtra("imageUrl", mAlbums.get(position).getCoverUrlLarge());
        mIntent.putExtra("intro", mAlbums.get(position).getAlbumIntro());
        mIntent.putExtra("count", mAlbums.get(position).getIncludeTrackCount() + "");

        startActivity(mIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    /**
     * 获取故事专辑成功
     *
     * @param object
     */
    public void onSuccess(CustomizedAlbumColumnDetail object) {
        Log.e(TAG, "onSuccess " + (object != null));
        if (object != null && object.getColumnItemses() != null) {
            List<ColumnAlbumItem> columnItemses = object.getColumnItemses();
            List<Album> alba = XmCustomizedModelUtil.customizedAlbumListToAlbumList(columnItemses);
            if (alba.size() != 0) {
                mPageId++;
                if (mAlbums == null) {
                    mAlbums = alba;
                } else {
                    mAlbums.addAll(alba);
                }

                mListenStoryAdapter.setData(mAlbums);
                mListenStoryAdapter.notifyDataSetChanged();
            }

        }
    }


    /**
     * 获取故事专辑失败
     *
     * @param code
     * @param message
     */
    public void onError(int code, String message) {
        printn(getString(R.string.Network_Error));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}
