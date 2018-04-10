package com.loybin.baidumap.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseFragment;
import com.loybin.baidumap.factory.LoadingPager;
import com.loybin.baidumap.model.WifiModel;
import com.loybin.baidumap.presenter.RecommendedsPresenter;
import com.loybin.baidumap.ui.activity.CheckMoreActivity;
import com.loybin.baidumap.ui.activity.StoryListActivity;
import com.loybin.baidumap.ui.adapter.RecommendedAdapter;
import com.loybin.baidumap.ui.adapter.StoryHistoryAdapter;
import com.loybin.baidumap.ui.view.BlurTransformation;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.widget.chatrow.Constant;
import com.umeng.analytics.MobclickAgent;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/07 下午3:22
 * 描   述: 推荐Fragment
 */
public class RecommendedsFramgent extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "RecommendedFramgent";
    private RecommendedsPresenter mRecommendedPresenter;
    public int mPage = 1;
    private List<Album> mAlbums;
    private LoadingPager mLoadingPager;
    private int mTotalCount;
    private Intent mIntent;
    private Integer mPosition;
    private TrackList mTrackList;
    private XmPlayerManager mPlayerManager;
    private boolean mPlaying;
    private ImageView mIvMaxCover;
    private TextView mTvInTo;
    private ImageView mIvCover;
    private String mImagUrl;
    private BlurTransformation mBlurTransformation;
    private Track mTrack;
    private Animation mOperatingAnim;
    private ImageView mIvTime;
    private TextView mTvHist;
    private TextView mTvOne;
    private RecyclerView mListView;
    private List<Album> mAlba;
    private LinearLayout mLLStoryHistory;
    private Gson mGson;
    private StoryHistoryAdapter mStoryHistoryAdapter;
    private LinearLayout mTvTv;
    private TextView mTvViewMore;
    private String mJsonAlbum;
    private Intent mIntent1;
    private RecyclerView mRecyclerView;
    private RecommendedAdapter mRecommendedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mBlurTransformation == null) {
            mBlurTransformation = new BlurTransformation(getContext(), 100);
        }
        if (mPlayerManager == null) {
            mPlayerManager = XmPlayerManager.getInstance(getContext());
        }
        if (mAlba == null) {
            mAlba = new ArrayList<>();
        }
        if (mGson == null) {
            mGson = new Gson();
        }

        EventBus.getDefault().register(this);
        mPlaying = mPlayerManager.isPlaying();

        if (mImagUrl != null) {
            Glide.with(this).load(mImagUrl).into(mIvCover);
            Glide.with(this).load(mImagUrl)
                    .transform(mBlurTransformation)
                    .crossFade()
                    .into(mIvMaxCover);
        }
    }


    @Override
    public LoadingPager.LoadedResult initData(LoadingPager loadingPager) {
        mLoadingPager = loadingPager;
        if (mRecommendedPresenter == null) {
            mRecommendedPresenter = new RecommendedsPresenter(getContext(), this);
        }
        try {
            mAlbums = mRecommendedPresenter.loadData(mPage, "843");
            return checkResData(mAlbums);
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }


    @Override
    public View initSuccessView() {
        View view = LayoutInflater.from(MyApplication.sInstance).inflate(R.layout.fragment_recommendeds, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        initView();
        initListener();

        return view;

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(WifiModel startModel) {
        LogUtils.e(TAG, "更新 搜索专辑了 !!!!!!!!!!!~!!!!");
        mJsonAlbum = mRecommendedPresenter.loadDataFromLocal("Album", Constant.PROTOCOL_TIMEOUT_MONTH);
        mRecommendedAdapter.setJsonAlbum(mJsonAlbum);
        mRecommendedAdapter.notifyDataSetChanged();
    }


    private void initView() {
        mJsonAlbum = mRecommendedPresenter.loadDataFromLocal("Album", Constant.PROTOCOL_TIMEOUT_MONTH);
        String track = mRecommendedPresenter.loadDataFromLocal("TrackList", Constant.PROTOCOL_TIMEOUT_MONTH);
        int position = (Integer) SharedPreferencesUtils.getParam(MyApplication.sInstance, "position", 0);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.sInstance));
        mRecommendedAdapter = new RecommendedAdapter(MyApplication.sInstance,
                mAlbums, track, mJsonAlbum, mRecommendedPresenter, this, position);
        mRecyclerView.setAdapter(mRecommendedAdapter);
    }


    private void initListener() {

    }


    /**
     * 从网络获取成功的数据
     *
     * @param alba
     */
    public void onSuccess(List<Album> alba, int totalCount) {
        LogUtils.d(TAG, "从网络获取成功的数据");
        if (mOperatingAnim != null) {
            mIvTime.clearAnimation();
        }
        mPage++;
        mTotalCount = totalCount;
        if (mAlbums == null) {
            //去更新fragmen UI
            mAlbums = alba;
            LoadingPager.LoadedResult loadedResult = checkResData(alba);
            mMProgressModel.setState(loadedResult.state);
            if (mLoadingPager != null) {
                mLoadingPager.setCurState(loadedResult.state);
                mLoadingPager.refreshUIByState();
            }
        } else {
            mAlbums.clear();
            mAlbums.addAll(alba);
            mRecommendedAdapter.setData(mAlbums);
            mRecommendedAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 从网络获取失败的通知
     */
    public void onError() {
        if (mLoadingPager != null) {
            mLoadingPager.setCurState(3);
            mLoadingPager.refreshUIByState();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_batch:
                mRecommendedPresenter.loadDataNet(mPage, "33");
                if (mOperatingAnim != null) {
                    mIvTime.startAnimation(mOperatingAnim);
                }
                break;

            case R.id.ll_there:
                if (mIntent == null) {
                    mIntent = new Intent(getContext(), StoryListActivity.class);
                }

                long albumId = mTrackList.getAlbumId();
                mIntent.putExtra("id", albumId);
                mIntent.putExtra("title", mTrackList.getAlbumTitle());
                mIntent.putExtra("imageUrl", mTrackList.getCoverUrlLarge());
                mIntent.putExtra("intro", mTrackList.getAlbumIntro());
                mIntent.putExtra("count", mTrackList.getTotalCount() + "");
                startActivity(mIntent);
                break;

            case R.id.tv_into:
                if (mIntent == null) {
                    mIntent = new Intent(getContext(), StoryListActivity.class);
                }

                long albumId2 = mTrackList.getAlbumId();
                mIntent.putExtra("id", albumId2);
                mIntent.putExtra("title", mTrackList.getAlbumTitle());
                mIntent.putExtra("imageUrl", mTrackList.getCoverUrlLarge());
                mIntent.putExtra("intro", mTrackList.getAlbumIntro());
                mIntent.putExtra("count", mTrackList.getTotalCount() + "");
                startActivity(mIntent);
                break;

            case R.id.tv_view_more:
                if (mIntent1 == null) {
                    mIntent1 = new Intent(getContext(), CheckMoreActivity.class);
                }
                mIntent1.putExtra("json", mJsonAlbum);
                getContext().startActivity(mIntent1);
                break;

            default:
                break;
        }
    }


    /**
     * 历史专辑== Null
     */
    public void jsonAlbumNull() {
        mTvTv.setVisibility(View.GONE);
        mLLStoryHistory.setVisibility(View.GONE);
    }


    /**
     * 获取了成功的搜索专辑数据
     *
     * @param list
     */
    public void successAlbum(List<Album> list) {
        Collections.reverse(list);

        mTvTv.setVisibility(View.VISIBLE);
        mTvViewMore.setVisibility(View.VISIBLE);
        mLLStoryHistory.setVisibility(View.VISIBLE);

        LogUtils.e(TAG, "获取了成功的搜索专辑数据 " + list.size());
        for (int i = 0; i < list.size(); i++) {
            LogUtils.e(TAG, "getAlbumTitle " + list.get(i).getAlbumTitle());
        }
        mAlba = list;
        mStoryHistoryAdapter.setData(mAlba);
        mStoryHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d(TAG, "RecommendedFramgent onDestroy");
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        MobclickAgent.onPageStart("RecommendedsFramgent"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
        MobclickAgent.onPageEnd("RecommendedsFramgent");
    }
}
