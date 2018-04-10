package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.ui.activity.GeoFenceListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  提示框
 */

public class PlayDialog extends AlertDialog {


    @BindView(R.id.tv_cancel)
    TextView mTvCancel;

    @BindView(R.id.determine)
    TextView mDetermine;

    private GeoFenceListActivity mGeoFenceListActivity;
    private Context mContext;

    public PlayDialog(Context context, GeoFenceListActivity geoFenceListActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mGeoFenceListActivity = geoFenceListActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.CENTER); //显示在中间


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
                dismiss();

                break;


            case R.id.determine:
                mGeoFenceListActivity.cancel();
                dismiss();
                break;
        }
    }


}
