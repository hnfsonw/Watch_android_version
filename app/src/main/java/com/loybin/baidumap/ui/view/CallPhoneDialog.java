package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangz on 17/9/6.
 */

public class CallPhoneDialog extends AlertDialog {

    @BindView(R.id.tv_cancel)
    TextView mTvCancel;

    @BindView(R.id.determine)
    TextView mDetermine;

    @BindView(R.id.tv_remove)
    public TextView mTvRemove;

    @BindView(R.id.tv_user_name)
    public TextView mTvUserName;

    @BindView(R.id.tv_title)
    public TextView mTvTitle;

    private Context mContext;
    private BaseActivity mDevicesHomeActivity;

    public CallPhoneDialog(Context context, BaseActivity devicesHomeActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mDevicesHomeActivity = devicesHomeActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_watch_off);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.CENTER); //显示在中间
    }


    public void initTitle(String title, String phone, String cancel, String determine) {
        LogUtils.e("DevicesHomeActivity", "title:" + title + " Phone:" + phone);
        mTvTitle.setText(title);
        mTvRemove.setText(phone);
        mTvCancel.setText(cancel);
        mDetermine.setText(determine);
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(120, 0, 120, 0);

        getWindow().setAttributes(layoutParams);
    }


    @OnClick({R.id.tv_cancel, R.id.determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;


            case R.id.determine:
                mDevicesHomeActivity.callPhoneOn();
                dismiss();
                break;
        }
    }
}
