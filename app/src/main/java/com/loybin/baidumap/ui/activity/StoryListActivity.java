package com.loybin.baidumap.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.EventPlayStartModel;
import com.loybin.baidumap.model.EventPlayStopModel;
import com.loybin.baidumap.presenter.StoryListPresenter;
import com.loybin.baidumap.ui.adapter.StoryListAdapter;
import com.loybin.baidumap.ui.view.BlurTransformation;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.widget.chatrow.Constant;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangz on 17/8/24.
 * 单个专辑故事列表
 */

public class StoryListActivity extends BaseActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {


    private static final String TAG = "StoryListActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.lv_story_list)
    ListView mLvStoryList;

    @BindView(R.id.iv_music)
    ImageView mIvMusic;

    @BindView(R.id.iv_max_cover)
    ImageView mIvMaxCover;

    @BindView(R.id.tv_info)
    TextView mTvInfo;

    @BindView(R.id.iv_cover)
    ImageView mIvCover;

    @BindView(R.id.tv_size)
    TextView mTvSize;

    @BindView(R.id.ll_last_play)
    LinearLayout mLlLastPlay;

    @BindView(R.id.tv_last_play)
    TextView mTvLastPlay;

    private StoryListAdapter mStoryListAdapter;
    private long mIds;
    private int mPageId = 1;
    public TrackList mTrackHotList;
    private AnimationDrawable mDrawable;
    private XmPlayerManager mPlayerManager;
    private StoryListPresenter mStoryListPresenter;
    private boolean mIsNetWork;
    private String mTitle;
    private RemoveDialog mDialog;
    private long mDataId;
    private int mDuration;
    private String mCoverUrlSmall;
    private String mTrackTitle;
    private String mAppAccount;
    private String mImagUrl;
    private String mIntro;
    private BlurTransformation mBlurTransformation;
    private String mCount;
    private Gson mGson;
    private Integer mPosition;
    private long mSize;
    private String mStoryClearUrl;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_story_list;
    }

    @Override
    protected void init() {
        mContext = this;
        if (mStoryListPresenter == null) {
            mStoryListPresenter = new StoryListPresenter(this, this);
        }
        if (mGson == null) {
            mGson = new Gson();
        }
        mAppAccount = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");
        EventBus.getDefault().register(this);
        mPlayerManager = XmPlayerManager.getInstance(this);
        mIds = getIntent().getLongExtra("id", 0);
        mTitle = getIntent().getStringExtra("title");
        mImagUrl = getIntent().getStringExtra("imageUrl");
        mIntro = getIntent().getStringExtra("intro");
        mCount = getIntent().getStringExtra("count");
        if (mBlurTransformation == null) {
            mBlurTransformation = new BlurTransformation(this, 100);
        }
        initView();
        initData();
        initListener();
        mStoryListPresenter.loadData(mPageId, mIds);

    }


    private void initListener() {
        mLvStoryList.setOnItemClickListener(this);
        mLvStoryList.setOnScrollListener(this);
    }


    private void initData() {
        mStoryListAdapter = new StoryListAdapter(StoryListActivity.this, mTrackHotList, mPlayerManager);
        mLvStoryList.setAdapter(mStoryListAdapter);
    }


    private void initView() {
        mPosition = (Integer) SharedPreferencesUtils.getParam(this, "position", 0);
        String local = loadDataFromLocal("TrackList" + mIds, Constant.PROTOCOL_TIMEOUT_MONTH);
        LogUtils.e(TAG, "local 数据 " + local);
        if (local != null) {
            Track list = mStoryListPresenter.parsingData(local, mPosition);
            if (list != null) {
                mTvLastPlay.setText(getString(R.string.there_is) + list.getTrackTitle());
            }
        } else {
            mLlLastPlay.setVisibility(View.GONE);
        }

        mTvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (mTitle != null) {
            mTvTitle.setText(mTitle);
        }
        if (mPlayerManager.isPlaying()) {
            //正在播放
            mIvMusic.setVisibility(View.VISIBLE);
            mTvRight.setVisibility(View.GONE);
            mIvMusic.setImageResource(R.drawable.music);
            mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
            mDrawable.start();
        } else {
            mIvMusic.setVisibility(View.GONE);
//                mTvRight.setVisibility(View.VISIBLE);
//                mTvRight.setText("下载列表");
        }

        if (!mImagUrl.equals("")) {
            Glide.with(this).load(mImagUrl).into(mIvCover);
            Glide.with(this).load(mImagUrl)
                    .transform(mBlurTransformation)
                    .crossFade()
                    .into(mIvMaxCover);
        }

        if (mIntro != null) {
            mTvInfo.setText(mIntro);
        }

        if (mCount != null) {
            mTvSize.setText(getString(R.string.total) + mCount + getString(R.string.first));
        }

    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.iv_music, R.id.ll_last_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_music:
                toActivity(StoryPlayerActivity.class);
                break;

            case R.id.ll_last_play:
                if (mStoryListPresenter.mTrackList != null) {
                    mPlayerManager.playList(mStoryListPresenter.mTrackList, mPosition);
                    toActivity(StoryPlayerActivity.class);
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Track> tracks = mTrackHotList.getTracks();
        mPlayerManager.playList(tracks, position);
        String toJson = mGson.toJson(mTrackHotList);
        LogUtils.e(TAG, "toJson " + toJson);
        toActivity(StoryPlayerActivity.class);
        saveData(toJson, position);
    }


    /**
     * 记住点击的故事
     *
     * @param toJson
     * @param position
     */
    private void saveData(final String toJson, final int position) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                saveData2Local("TrackList" + mIds, toJson);
                saveData2Local("Recommended.TrackList", toJson);
                SharedPreferencesUtils.setParam(StoryListActivity.this, "position" + mIds, position);
                SharedPreferencesUtils.setParam(StoryListActivity.this, "position", position);
            }
        });
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            int count = view.getCount();
            count = count - 5 > 0 ? count - 5 : count - 1;
            LogUtils.e(TAG, "view.getLastVisiblePosition() " + view.getLastVisiblePosition());
            LogUtils.e(TAG, "count " + count);
            LogUtils.e(TAG, "mTrackHotList == null " + mTrackHotList);
            LogUtils.e(TAG, "mPageId " + mPageId);
            LogUtils.e(TAG, "mTrackHotList.getTotalPage() " + mTrackHotList.getTotalPage());

            if (view.getLastVisiblePosition() > count && (mTrackHotList == null
                    || mPageId < mTrackHotList.getTotalPage())) {
                mStoryListPresenter.loadData(mPageId, mIds);
            }
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //暂停了播放通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStop(EventPlayStopModel playModel) {
        mIvMusic.setVisibility(View.GONE);
    }


    //正在播放的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(EventPlayStartModel startModel) {
        mIvMusic.setVisibility(View.VISIBLE);
        mIvMusic.setImageResource(R.drawable.music);
        mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
        mDrawable.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayerManager.isPlaying()) {
            //正在播放
            mIvMusic.setVisibility(View.VISIBLE);
            mTvRight.setVisibility(View.GONE);
            mIvMusic.setImageResource(R.drawable.music);
            mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
            mDrawable.start();
        } else {
            mIvMusic.setVisibility(View.GONE);
//                mTvRight.setVisibility(View.VISIBLE);
//                mTvRight.setText("下载列表");
        }
    }


    /**
     * 单个故事专辑获取成功的数据
     *
     * @param object
     */
    public void onSuccess(TrackList object) {
        Log.e(TAG, "onSuccess " + (object != null));
        LogUtils.e(TAG, object.getTracks().size() + " size");
        if (object != null && object.getTracks() != null && object.getTracks().size() != 0) {
            mPageId++;
            if (mTrackHotList == null) {
                mTrackHotList = object;
            } else {
                mTrackHotList.getTracks().addAll(object.getTracks());
            }

            LogUtils.e(TAG, "size " + mTrackHotList.getTracks());
            mStoryListAdapter.setData(mTrackHotList);
            mStoryListAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 单个故事专辑获取失败
     *
     * @param code
     * @param message
     */
    public void onError(int code, String message) {
        printn(getString(R.string.Network_Error));
    }


    /**
     * 移动网络
     */
    @Override
    protected void netWork4G() {
        mIsNetWork = false;
    }


    /**
     * 无网络
     */
    @Override
    protected void noNetwork() {
    }


    /**
     * wifi
     */
    @Override
    protected void theNetwork() {
        mIsNetWork = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void cancel() {
        LogUtils.e(TAG, "确定");
        mStoryListPresenter.appSendStoryInfoToImei(MyApplication.sToken, mAppAccount,
                MyApplication.sDeviceListBean.getImei(), mDataId, mDuration, mCoverUrlSmall,
                mTrackTitle, mSize, mStoryClearUrl);
    }

    /**
     * 点击推送的事件
     */
    public void storyDownload(long dataId, int duration, String coverUrlSmall,
                              String trackTitle, long size, String storyClearUrl) {
        mDataId = dataId;
        mDuration = duration;
        mCoverUrlSmall = coverUrlSmall;
        mTrackTitle = trackTitle;
        mSize = size;
        mStoryClearUrl = storyClearUrl;
        if (mDialog == null) {
            mDialog = new RemoveDialog(this, this);
        }
        mDialog.show();
        mDialog.initTitle(getString(R.string.push_story));
    }


    /**
     * 推送成功的通知
     */
    public void pushSuccess() {
        dismissLoading();
        printn(getString(R.string.push_success));
    }
}
