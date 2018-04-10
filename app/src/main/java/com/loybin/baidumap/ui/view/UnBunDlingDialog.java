package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  移除提示框
 */

public class UnBunDlingDialog extends AlertDialog {


    @BindView(R.id.tv_remove)
    TextView mTvRemove;

    @BindView(R.id.tv_user_name)
    TextView mTvUserName;

    @BindView(R.id.tv_know_the)
    TextView mTvKnowThe;

    @BindView(R.id.ll_know_the)
    View mLlKnowThe;

    private BaseActivity mBaseActivity;
    private Context mContext;
    private boolean mIsCanl;

    public UnBunDlingDialog(Context context, BaseActivity baseActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mBaseActivity = baseActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unbundling);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.CENTER); //显示在中间

    }

    public void setTvRemove(String name) {
        mTvRemove.setText(name);
    }


    public void setTvKnowThe(String name) {
        mTvKnowThe.setText(name);
        mTvKnowThe.setTextColor(Color.BLUE);
    }


    public void setIsCanl(boolean isCanl) {
        mIsCanl = isCanl;
    }


    /**
     * 设置宽度全屏，要设置在show的后面
     */
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


    @OnClick({R.id.ll_know_the})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_know_the:
                if (mIsCanl) {
                    mBaseActivity.selectEquipment();
                    mIsCanl = false;
                    dismiss();
                } else {
                    dismiss();
                }
                break;


        }
    }


}
