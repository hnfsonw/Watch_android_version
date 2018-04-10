package com.loybin.baidumap.ui.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.util.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/30 上午9:59
 * 描   述: 官方微信视图介绍
 */
public class WeiXinActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.imageView3)
    ImageView mImageView3;

    @BindView(R.id.btn_save)
    Button mBtnSave;
    private Bitmap mBitmap;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wei_xin;
    }


    @Override
    protected void init() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mBitmap = UIUtils.returnBitmap("https://kidwatch.hojy.com/hgts/img/Wechat.jpeg");
            }
        });
        initView();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.wei_xin));
        Glide.with(this).load("https://kidwatch.hojy.com/hgts/img/Wechat.jpeg").into(mImageView3);
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


    @OnClick({R.id.iv_back, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_save:
                if (mBitmap != null) {
                    UIUtils.saveImageToGallery(this, mBitmap);
                    printn(getString(R.string.save_success));
                } else {
                    printn(getString(R.string.save_error));
                }

                break;
        }
    }


//    android 保存Bitmap 到本地 哦

    public String saveImage(Bitmap bmp, String key) {

        String fileName = key + ".jpg";
        File appDir = getCacheFile(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(appDir);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appDir.getAbsolutePath();
    }
}
