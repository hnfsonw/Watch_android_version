package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/04 下午4:17
 * 描   述: 注册服务协议
 */
public class RegisterServiceActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register_service;
    }


    @Override
    protected void init() {

        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.register_service));
        mWebView.loadUrl(Constants.REGISTERURL);
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


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishActivityByAnimation(this);
    }
}
