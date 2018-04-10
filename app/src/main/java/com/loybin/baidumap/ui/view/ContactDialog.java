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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  强制手表关机
 */

public class ContactDialog extends AlertDialog {


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

    private BaseActivity mBaseActivity;
    private Context mContext;
    /***
     * type == 0; 用户申请绑定手表 同意拒绝的全局dialog
     * type == 1: 管理员邀请 已注册未绑定设备的用户 全局dialog
     */
    private int mType;

    public ContactDialog(Context context, BaseActivity settingActivity, int type) {
        super(context, R.style.MyDialog);
        mContext = context;
        mBaseActivity = settingActivity;
        mType = type;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.CENTER); //显示在中间
        setCanceledOnTouchOutside(false);


    }

    public void initUserName(String string) {
        mTvUserName.setVisibility(View.VISIBLE);
        mTvUserName.setText(string);
    }


    public void initMessage(String body) {
        mTvRemove.setText(body);
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


    @OnClick({R.id.tv_cancel, R.id.determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                switch (mType) {
                    case 0:
                        mBaseActivity.refused();
                        dismiss();
                        break;

                    case 1:
                        mBaseActivity.refusedAgreed();
                        dismiss();
                        break;

                    default:
                        break;
                }

                break;


            case R.id.determine:
                switch (mType) {
                    case 0:
                        mBaseActivity.agreed();
                        dismiss();
                        break;

                    case 1:
                        mBaseActivity.agreedAdd();
                        dismiss();
                        break;

                    default:
                        break;
                }


                break;
        }
    }


}
