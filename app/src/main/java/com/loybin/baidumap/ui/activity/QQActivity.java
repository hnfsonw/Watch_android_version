package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/30 上午10:33
 * 描   述: 添加设备view
 */
public class QQActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_qq;
    }

    @Override
    protected void init() {
        mTvTitle.setText(getString(R.string.qq));
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
