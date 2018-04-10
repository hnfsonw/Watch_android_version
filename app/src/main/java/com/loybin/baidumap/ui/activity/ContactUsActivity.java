package com.loybin.baidumap.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.ui.view.CallPhoneDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/28 下午5:01
 * 描   述: 联系我们 视图
 */
public class ContactUsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.rl_back)
    RelativeLayout mRlBack;

    @BindView(R.id.ll_customer_service)
    LinearLayout mLlCustomerService;

    @BindView(R.id.ll_wei_xin)
    LinearLayout mLlWeiXin;

    @BindView(R.id.ll_qq)
    LinearLayout mLlQq;

    @BindView(R.id.imageView2)
    ImageView mImageView2;

    @BindView(R.id.ll_help)
    LinearLayout mLlHelp;
    private CallPhoneDialog mCallPhoneDialog;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_contact_us;
    }


    @Override
    protected void init() {

        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.contact_us));
        mRlBack.setBackgroundResource(R.mipmap.cover_);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.ll_customer_service, R.id.ll_wei_xin, R.id.ll_qq, R.id.ll_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.ll_customer_service:
                showPhone();
                break;

            case R.id.ll_wei_xin:
                toActivity(WeiXinActivity.class);
                break;

            case R.id.ll_qq:
                toActivity(QQActivity.class);
                break;

            case R.id.ll_help:
                toActivity(AboutActivity.class);
                break;
        }
    }


    private void showPhone() {
        if (mCallPhoneDialog == null)
            mCallPhoneDialog = new CallPhoneDialog(this, this);
        mCallPhoneDialog.show();

        mCallPhoneDialog.initTitle("", getString(R.string.call_) + "400-068-4686",
                getString(R.string.cancel), getString(R.string.determine));
    }


    @Override
    public void callPhoneOn() {
        requestPermission();
    }


    /**
     * 申请电话权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        100);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }


    private void callPhone() {
        //意图：想干什么事
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        intent.setData(Uri.parse("tel:" + "4000684686"));

        //开启系统拨号器
        startActivity(intent);
    }
}
