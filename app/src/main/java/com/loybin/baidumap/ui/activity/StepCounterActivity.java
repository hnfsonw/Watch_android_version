package com.loybin.baidumap.ui.activity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.StepCounterPresenter;
import com.loybin.baidumap.ui.adapter.StepCounterAdapter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangz on 17/9/8.
 */

public class StepCounterActivity extends BaseActivity implements AbsListView.OnScrollListener {

    private static final String TAG = "StepCounterActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_head)
    CircleImageView mIvHead;

    @BindView(R.id.tv_baby_name)
    TextView mTvBabyName;

    @BindView(R.id.tv_steps)
    TextView mTvSteps;

    @BindView(R.id.lv_step_counter)
    ListView mLvStepCounter;

    @BindView(R.id.tv_baby_rownum)
    TextView mTvBabyRownum;

    private StepCounterPresenter mStepCounterPresenter;
    private StepCounterAdapter mStepCounterAdapter;
    private String mImgUrl;
    private String mNickName;
    private int mRownum;
    private int mPage;
    private boolean mNetWork = true;
    private List<ResponseInfoModel.ResultBean.StepsRankingListBean> mStepsRankingListBeen;
    private ResponseInfoModel.ResultBean.deviceStepsData mDeviceStepsData;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_step_counter;
    }

    @Override
    protected void init() {
        mImgUrl = getIntent().getStringExtra(STRING);
        mNickName = getIntent().getStringExtra(BABY);
        mStepsRankingListBeen = new ArrayList<>();
        mStepCounterPresenter = new StepCounterPresenter(this, this);
        mStepCounterAdapter = new StepCounterAdapter(this, mStepsRankingListBeen);
        mLvStepCounter.setAdapter(mStepCounterAdapter);

        mPage = 1;
        mStepCounterPresenter.queryDeviceStepsRanking(MyApplication.sToken, String.valueOf(mPage),
                MyApplication.sDeviceId);
        initView();
        initListener();

    }

    private void initView() {
        Glide.with(this).load(mImgUrl).into(mIvHead);
        mTvBabyName.setText(mNickName);
    }


    private void initListener() {
        mLvStepCounter.setOnScrollListener(this);
    }


    @Override
    protected void dismissNewok() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
        }
    }


    /**
     * app查询手表步数排行请求成功回调
     *
     * @param stepsRankingListBeen
     */
    public void setData(List<ResponseInfoModel.ResultBean.StepsRankingListBean> stepsRankingListBeen,
                        ResponseInfoModel.ResultBean.deviceStepsData deviceStepsData) {
        LogUtils.e(TAG, "stepsRankingListBeen :" + stepsRankingListBeen.size());
        if (stepsRankingListBeen.size() != 0) {
            mStepsRankingListBeen.addAll(stepsRankingListBeen);
            mPage++;
        } else {
            printn("已加载所有排行");
        }
        mDeviceStepsData = deviceStepsData;
        LogUtils.e(TAG, "deviceStepsData" + deviceStepsData.getSteps());
        mTvSteps.setText(String.valueOf(mDeviceStepsData.getSteps()));
        mTvBabyRownum.setText(String.valueOf(mDeviceStepsData.getRownum()));
        mStepCounterAdapter.setDate(mStepsRankingListBeen);
        mStepCounterAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (mNetWork) {
                int count = absListView.getCount();
                count = count - 5 > 0 ? count - 5 : count - 1;
                if (absListView.getLastVisiblePosition() + 1 > count) {
                    LogUtils.e(TAG, "mPage:" + mPage);
                    mStepCounterPresenter.queryDeviceStepsRanking(MyApplication.sToken,
                            String.valueOf(mPage), MyApplication.sDeviceId);
                }
            } else {
                printn(getString(R.string.no_network));
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    protected void theNetwork() {
        super.theNetwork();
        mNetWork = true;
    }

    @Override
    protected void noNetwork() {
        super.noNetwork();
        mNetWork = false;
    }
}
