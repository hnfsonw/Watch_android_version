package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.base.BaseFragment;
import com.loybin.baidumap.factory.FragmentFactory;
import com.loybin.baidumap.factory.LoadingPager;
import com.loybin.baidumap.model.EventPlayStartModel;
import com.loybin.baidumap.model.EventPlayStopModel;
import com.loybin.baidumap.model.LoadedDestroyModel;
import com.loybin.baidumap.model.LoadedResultModel;
import com.loybin.baidumap.presenter.StoryPresenter;
import com.loybin.baidumap.service.PlayService;
import com.loybin.baidumap.ui.view.PagerSlidingTabStripExtends;
import com.loybin.baidumap.ui.view.PopupMenu;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.UIUtils;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/07 下午2:43
 * 描   述: 听故事视图
 */
public class StoryActivity extends BaseActivity implements PopupMenu.OnMenuItemSelectedListener {

    private static final String TAG = "StoryBooksFramgent";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_music)
    ImageView mIvMusic;

    @BindView(R.id.iv_confirm)
    ImageView mIvStoryHistory;

    @BindView(R.id.tabs)
    PagerSlidingTabStripExtends mTabs;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private String[] mMainTitles;
    private MyFragmentStatePagerAdapter mAdapter;
    private FragmentManager mFragmentManager;
    private LoadedResultModel mMProgressModel;
    private LoadedDestroyModel mLoadedDestroyModel;
    private StoryPresenter mStoryPresenter;
    private XmPlayerManager mPlayerManager;
    private boolean mPlaying;
    private AnimationDrawable mDrawable;
    private PopupMenu mPopupMenu;
    private float mOffsetX;
    private float mOffsetY;
    private float mMenuWidth;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_story;
    }

    @Override
    protected void init() {

        initDialogStory();

        if (mStoryPresenter == null) {
            mStoryPresenter = new StoryPresenter(this, this);
        }

        if (mMProgressModel == null) {
            mMProgressModel = new LoadedResultModel();
        }

        if (mLoadedDestroyModel == null) {
            mLoadedDestroyModel = new LoadedDestroyModel();
        }
        LogUtils.e(TAG, "初始化");

        EventBus.getDefault().register(this);
        mPlayerManager = XmPlayerManager.getInstance(this);
        mPlaying = mPlayerManager.isPlaying();
        initView();
        initData();
        initListener();
        initService();
    }


    /**
     * 初始化对话框
     */
    private void initDialogStory() {
        View menuLayout = getLayoutInflater().inflate(R.layout.dialog_story, null);
        menuLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        mPopupMenu = new PopupMenu((ViewGroup) menuLayout);
        mPopupMenu.setMenuItemBackgroundColor(0xffb1df83);
        mPopupMenu.setMenuItemHoverBackgroundColor(0x22000000);
        mPopupMenu.setOnMenuItemSelectedListener(this);

        mOffsetX = 0;
        mOffsetY = 0;
        mMenuWidth = menuLayout.getMeasuredWidth();


    }


    //暂停了播放通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStop(EventPlayStopModel playModel) {
        mIvMusic.setVisibility(View.GONE);
        mIvStoryHistory.setImageResource(R.mipmap.more_info);
        mIvStoryHistory.setVisibility(View.VISIBLE);
    }


    //正在播放的通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(EventPlayStartModel startModel) {
//        mLlThere.setVisibility(View.GONE);
        mIvMusic.setVisibility(View.VISIBLE);
        mIvMusic.setImageResource(R.drawable.music);
        mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
        mDrawable.start();
    }


    private void initService() {
        LogUtils.e(TAG, "initService!! " + Thread.currentThread().getName());
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            FragmentFactory.mCacheFragmentMap.clear();
            finishActivityByAnimation(StoryActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initListener() {
        final MyOnpageChangeListener myOnpageChangeListener = new MyOnpageChangeListener();
        mTabs.setOnPageChangeListener(myOnpageChangeListener);

        mViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //ViewPager已经展示-->前两页已经创建成功--> -->存在于我们集合中
                //手动选中第一页
                myOnpageChangeListener.onPageSelected(0);

                //移除
                mViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                LogUtils.e(TAG, "ViewPager已经展示-->前两页已经创建成功");
            }
        });
    }


    /**
     * 搜索专辑 推送历史的对话框
     *
     * @param menuItem
     */
    @Override
    public void onMenuItemSelected(View menuItem) {
        switch (menuItem.getId()) {
            case R.id.ll_pull:
//                //推送故事历史
                toActivity(PushStoryHiStoryActivity.class);
                break;

            case R.id.ll_query:
                //搜索专辑
                LogUtils.e(TAG, "搜索专辑 ");
                toActivity(SearchStoryActivity.class);
                break;

            default:
                break;
        }
    }


    class MyOnpageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            try {
                //触发加载数据
                //BaseFragment->LoadingPager->triggerLoadData();
                LogUtils.d(TAG, " 触发加载数据" + position + "~~ " + Thread.currentThread().getName());
                LogUtils.d(TAG, " mCacheFragmentMap " + FragmentFactory.mCacheFragmentMap.size());
                BaseFragment baseFragment = FragmentFactory.mCacheFragmentMap.get(position);
                LoadingPager loadingPager = baseFragment.getLoadingPager();
                loadingPager.triggerLoadData();
            } catch (Exception e) {

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }


    private void initView() {
        mTvTitle.setText(getString(R.string.listen_story));
        if (mPlaying) {
            mIvStoryHistory.setVisibility(View.VISIBLE);
            mIvStoryHistory.setImageResource(R.mipmap.more_info);
            mIvMusic.setVisibility(View.VISIBLE);
            mIvMusic.setImageResource(R.drawable.music);
            mDrawable = (AnimationDrawable) mIvMusic.getDrawable();
            mDrawable.start();
        } else {
            mIvMusic.setVisibility(View.GONE);
            mIvStoryHistory.setVisibility(View.VISIBLE);
            mIvStoryHistory.setImageResource(R.mipmap.more_info);
        }
    }


    private void initData() {
        //dataSets-->模拟数据集
        mMainTitles = UIUtils.getStrings(R.array.main_titles);

        //viewpager设置adapter
//        MyViewPagerAdpater adapter = new MyViewPagerAdpater();
        mAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
//        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mAdapter);
        // Bind the tabs to the ViewPager
        mTabs.setViewPager(mViewpager);
        LogUtils.e(TAG, "设置适配器  mViewpager");
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.iv_music, R.id.iv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                FragmentFactory.mCacheFragmentMap.clear();
                finishActivityByAnimation(StoryActivity.this);
                break;

            case R.id.iv_music:
                toActivity(StoryPlayerActivity.class);
                break;

            case R.id.iv_confirm:

                if (mPopupMenu.isShowing()) {
                    mPopupMenu.dismiss();
                } else {
                    mPopupMenu.show(view, (int) (view.getWidth() - mOffsetX - mMenuWidth), (int) mOffsetX);
                }

                break;

            default:
                break;
        }
    }


    class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {//指定position所对应的Fragment
            LogUtils.e(TAG, "初始化->" + mMainTitles[position]);
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {//决定有多少页
            if (mMainTitles != null) {
                return mMainTitles.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitles[position];
        }
    }


    @Override
    protected void onDestroy() {
        try {
            //取消事件注册
            EventBus.getDefault().unregister(this);
            super.onDestroy();
        }catch (Exception e){
            Log.d(TAG,"StoryActivity onDestroy 异常" +e.getMessage());
        }

    }
}
