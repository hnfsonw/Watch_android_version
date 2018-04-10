package com.loybin.baidumap.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.PushStoryHiStoryPresenter;
import com.loybin.baidumap.ui.adapter.PushStoryHiStoryAdapter;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/18 上午11:05
 * 描   述: 推送故事历史 view
 */
public class PushStoryHiStoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    public TextView mTvRight;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.listview)
    ListView mListview;

    @BindView(R.id.swiper_book)
    SwipeRefreshLayout mSwiperBook;

    private PushStoryHiStoryPresenter mPushStoryHiStoryPresenter;
    private List<ResponseInfoModel.ResultBean.storyListBean> mStoryList;
    private PushStoryHiStoryAdapter mPushStoryHiStoryAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_push_story_history;
    }


    @Override
    protected void init() {
        if (mPushStoryHiStoryPresenter == null) {
            mPushStoryHiStoryPresenter = new PushStoryHiStoryPresenter(this, this);
        }
        if (mStoryList == null) {
            mStoryList = new ArrayList<>();
        }

        initView();
        initListener();
        initData();
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.editor));
        mTvTitle.setText(getString(R.string.push_story_history));
    }


    private void initListener() {
        mSwiperBook.setOnRefreshListener(this);
    }


    private void initData() {
        mPushStoryHiStoryPresenter.queryStoryList(MyApplication.sToken, MyApplication.sDeviceListBean.getImei());
        if (mPushStoryHiStoryAdapter == null) {
            mPushStoryHiStoryAdapter = new PushStoryHiStoryAdapter(this, mStoryList);
        }
        mListview.setAdapter(mPushStoryHiStoryAdapter);
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.iv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                mPushStoryHiStoryAdapter.logo();
                break;

            case R.id.iv_confirm:
                break;

            default:
                break;
        }
    }


    /**
     * 获取成功的推送故事历史集合
     *
     * @param storyList
     */
    public void onSuccess(List<ResponseInfoModel.ResultBean.storyListBean> storyList) {
        mStoryList.clear();
        mStoryList.addAll(storyList);
        mPushStoryHiStoryAdapter.setData(mStoryList);
        mPushStoryHiStoryAdapter.notifyDataSetChanged();
    }

    /**
     * 删除故事
     */
    public void deleteStory(long id) {
        mPushStoryHiStoryPresenter.deleteStory(id, MyApplication.sToken, MyApplication.sDeviceListBean.getImei());
    }


    @Override
    public void onRefresh() {
        mPushStoryHiStoryPresenter.queryStoryList(MyApplication.sToken, MyApplication.sDeviceListBean.getImei());
        mSwiperBook.setRefreshing(false);
    }
}
