package com.loybin.baidumap.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseFragment;
import com.loybin.baidumap.factory.LoadingPager;
import com.loybin.baidumap.presenter.StoryBooksPresenter;
import com.loybin.baidumap.ui.activity.StoryListActivity;
import com.loybin.baidumap.ui.adapter.FragmentItemAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.ui.view.DefaultFooter;
import com.loybin.baidumap.ui.view.DefaultHeader;
import com.loybin.baidumap.ui.view.SpringView;
import com.umeng.analytics.MobclickAgent;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.io.IOException;
import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/07 下午3:32
 * 描   述: 儿歌大全Framgent
 */
public class ChildrenSongFramgent extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, SpringView.OnFreshListener {
    private static final String TAG = "ChildrenSongFramgent";
    private List<Album> mAlbums;
    private int mPage = 1;
    private StoryBooksPresenter mStoryBooksPresenter;
    private ListView mListView;
    private FragmentItemAdapter mFragmenItemAdapter;
    private LoadingPager mLoadingPager;
    private int mTotalCount;
    private Intent mIntent;
    private SpringView mSpringView;

    @Override
    public LoadingPager.LoadedResult initData(LoadingPager loadingPager) {
        mLoadingPager = loadingPager;
        LogUtils.e(TAG, "StoryBooksFramgent  initData");
        if (mStoryBooksPresenter == null) {
            mStoryBooksPresenter = new StoryBooksPresenter(getContext(), this, "ChildrenSong");
        }
        try {
            mAlbums = mStoryBooksPresenter.loadData(mPage, "840");
            return checkResData(mAlbums);
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }


    @Override
    public View initSuccessView() {
        try {
            View view = LayoutInflater.from(MyApplication.sInstance).inflate(R.layout.fragment_item, null);
            mSpringView = (SpringView) view.findViewById(R.id.springview);
            mListView = (ListView) view.findViewById(R.id.listview);
            mTotalCount = (int) SharedPreferencesUtils.getParam(MyApplication.sInstance,
                    "ChildrenSong_totalCount", 0);
            initListener();
            mPage++;
            if (mFragmenItemAdapter == null) {
                mFragmenItemAdapter = new FragmentItemAdapter(MyApplication.sInstance, mAlbums);
            }
            mListView.setAdapter(mFragmenItemAdapter);

            LogUtils.e(TAG, "数据 !!!!! " + mAlbums);

            mFragmenItemAdapter.notifyDataSetChanged();

            return view;
        } catch (Exception e) {
            LogUtils.e(TAG, "initSuccessView 异常");
            return new TextView(MyApplication.sInstance);
        }

    }


    private void initListener() {
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setHeader(new DefaultHeader(MyApplication.sInstance));
        mSpringView.setFooter(new DefaultFooter(MyApplication.sInstance));
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
    }


    @Override
    public void onSuccess(List<Album> alba, int totalCount) {
        if (mSpringView != null) {
            mSpringView.onFinishFreshAndLoad();
        }
        mTotalCount = totalCount;
        SharedPreferencesUtils.setParam(MyApplication.sInstance, "ChildrenSong_totalCount", mTotalCount);
        if (mAlbums == null) {
            //去更新fragmen UI
            mAlbums = alba;
            LoadingPager.LoadedResult loadedResult = checkResData(alba);
            mStoryBooksPresenter.setResult(alba);
            mMProgressModel.setState(loadedResult.state);
            if (mLoadingPager != null) {
                mLoadingPager.setCurState(loadedResult.state);
                mLoadingPager.refreshUIByState();
            }
        } else {
            mPage++;
            mAlbums.addAll(alba);
            mFragmenItemAdapter.setData(mAlbums);
            mFragmenItemAdapter.notifyDataSetChanged();
            mListView.smoothScrollToPosition(mAlbums.size() - 29);
        }
    }

    @Override
    public void onError() {
        if (mLoadingPager != null) {
            mLoadingPager.setCurState(3);
            mLoadingPager.refreshUIByState();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LogUtils.e(TAG, "onItemClick" + mAlbums.get(position).getId());
            if (mIntent == null) {
                mIntent = new Intent(getContext(), StoryListActivity.class);
            }
            mIntent.putExtra("id", mAlbums.get(position).getId());
            mIntent.putExtra("title", mAlbums.get(position).getAlbumTitle());
            mIntent.putExtra("imageUrl", mAlbums.get(position).getCoverUrlLarge());
            mIntent.putExtra("intro", mAlbums.get(position).getAlbumIntro());
            mIntent.putExtra("count", mAlbums.get(position).getIncludeTrackCount() + "");

            startActivity(mIntent);
        } catch (Exception e) {
            LogUtils.e(TAG, "onItemClick 异常 " + e.getMessage());
        }


    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            int count = view.getCount();
            count = count - 5 > 0 ? count - 5 : count - 1;
            if (view.getLastVisiblePosition() > count && (mAlbums == null
                    || mPage < mTotalCount / 30)) {
                mStoryBooksPresenter.loadDataNet(mPage, "840");
                LogUtils.d(TAG, "去加载了......" + mPage);
            } else {
                LogUtils.d(TAG, "拉倒底了  " + mPage + "~~ " + mAlbums.size());
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    @Override
    public void onRefresh() {
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }
        }, 400);
    }


    @Override
    public void onLoadmore() {
        if (mPage < mTotalCount / 30) {
            mStoryBooksPresenter.loadDataNet(mPage, "838");
        } else {
            if (mSpringView != null) {
                mSpringView.onFinishFreshAndLoad();
                Toast.makeText(MyApplication.sInstance, getString(R.string.no_more), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        MobclickAgent.onPageStart("ChildrenSongFramgent"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
        MobclickAgent.onPageEnd("ChildrenSongFramgent");
    }
}
