package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/01 上午9:47
 * 描   述: 关于view
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.ll_feedback)
    LinearLayout mLlFeedback;

    @BindView(R.id.ll_user_help)
    LinearLayout mLlUserHelp;

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.about));
        mRlBack.setBackgroundResource(R.mipmap.cover_);
    }


    @Override
    protected void dismissNewok() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back, R.id.ll_feedback, R.id.ll_user_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.ll_feedback:
                toActivity(FeedbackActivity.class);
                break;

            case R.id.ll_user_help:
                toActivity(UserHelpActivity.class);
                break;
        }
    }
}
