package com.loybin.baidumap.ui.activity;

import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
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
 * 创建时间: 2017/07/01 上午9:57
 * 描   述: 用户帮助view
 */
public class UserHelpActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_help;
    }


    @Override
    protected void init() {
        mTvTitle.setText(getString(R.string.user_help));
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.loadUrl(Constants.HELPURL);
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
