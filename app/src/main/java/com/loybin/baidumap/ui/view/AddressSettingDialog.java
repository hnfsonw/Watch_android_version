package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  安全地址设置的Dialog
 */

public class AddressSettingDialog extends AlertDialog {


    @BindView(R.id.ll_enter_the_reminder)
    LinearLayout mLlEnterTheReminder;

    @BindView(R.id.ll_left_to_remind)
    LinearLayout mLlLeftToRemind;

    @BindView(R.id.ll_exit_to_remind)
    LinearLayout mLlExitToRemind;

    @BindView(R.id.tv_tv1)
    TextView mTvAttributeOne;

    @BindView(R.id.tv_tv2)
    TextView mTvAttributeTow;

    @BindView(R.id.tv_tv3)
    TextView mTvAttributeThree;


    private BaseActivity mAddressSettingActivity;
    private Context mContext;

    public AddressSettingDialog(Context context, BaseActivity addressSettingActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mAddressSettingActivity = addressSettingActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_address_setting);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM); //显示在底部

    }

    public void setText(String attributeOne, String attributeTow, String attributeThree) {
        mTvAttributeOne.setText(attributeOne);
        mTvAttributeTow.setText(attributeTow);
        mTvAttributeThree.setText(attributeThree);
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


    @OnClick({R.id.ll_enter_the_reminder, R.id.ll_left_to_remind, R.id.ll_exit_to_remind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_enter_the_reminder:
                mAddressSettingActivity.setOptions(1);
                dismiss();
                break;

            case R.id.ll_left_to_remind:
                mAddressSettingActivity.setOptions(2);
                dismiss();
                break;

            case R.id.ll_exit_to_remind:
                mAddressSettingActivity.setOptions(3);
                dismiss();
                break;

        }
    }
}
