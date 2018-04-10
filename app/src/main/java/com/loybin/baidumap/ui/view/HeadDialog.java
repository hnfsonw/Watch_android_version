package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.util.UIUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  选择头像的dialog
 */

public class HeadDialog extends AlertDialog {

    private static final java.lang.String TAG = "HeadDialog";
    @BindView(R.id.user_setting_photo)
    LinearLayout mLlBaiduMap;

    @BindView(R.id.user_setting_album)
    LinearLayout mLlGaodeMap;


    private BaseActivity mBabyDataActivity;
    private Context mContext;

    public HeadDialog(Context context, BaseActivity babyDataActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mBabyDataActivity = babyDataActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_heade);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM); //显示在底部

    }


    /**
     * 设置宽度全屏，要设置在show的后面
     */
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }


    @OnClick({R.id.user_setting_photo, R.id.user_setting_album})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_setting_photo:
                fromCamera();
                dismiss();

                break;

            case R.id.user_setting_album:
                fromAlubm();
                dismiss();
                break;

        }
    }


    /**
     * 拍照选择
     */
    private void fromCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (UIUtils.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), mBabyDataActivity.PHOTO_FILE_NAME)));
        }
        mBabyDataActivity.startActivityForResult(intent, mBabyDataActivity.PHOTO_REQUEST_CAMERA);
    }


    /**
     * 选择系统照片
     */
    private void fromAlubm() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mBabyDataActivity.startActivityForResult(intent, mBabyDataActivity.PHOTO_REQUEST_GALLERY);
    }


}
