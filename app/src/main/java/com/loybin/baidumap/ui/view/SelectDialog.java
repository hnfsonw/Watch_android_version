package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.adapter.SelectDialogAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  选择提示框
 */

public class SelectDialog extends AlertDialog implements AdapterView.OnItemClickListener {


    private static final String TAG = "BabyDataActivity";
    @BindView(R.id.select_list)
    ListView mListView;

    private List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceListBeanList;
    private BaseActivity mBaseActivity;
    private Context mContext;

    public SelectDialog(Context context, BaseActivity baseActivity, List<ResponseInfoModel.ResultBean.DeviceListBean> deviceListBeanList) {
        super(context, R.style.SelectDialog);
        mContext = context;
        mBaseActivity = baseActivity;
        mDeviceListBeanList = deviceListBeanList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.CENTER); //显示在中间
        setCanceledOnTouchOutside(false);
        initData(mDeviceListBeanList);
        initListener();
    }


    private void initListener() {
        mListView.setOnItemClickListener(this);
    }


    private void initData(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceListBeanList) {
        LogUtils.e(TAG, "size" + deviceListBeanList.size());
        SelectDialogAdapter adapter = new SelectDialogAdapter(mContext, deviceListBeanList);
        LogUtils.e(TAG, adapter + "~~~~~~~~~~~~~");
        mListView.setAdapter(adapter);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.e(TAG, position + "");
        MyApplication.sDeivceNumber = position;
        Log.d(TAG, "sDeivceNumber: " + MyApplication.sDeivceNumber);
        mBaseActivity.exitHomeActivity();

        Intent intent = new Intent(mContext, DevicesHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        mBaseActivity.finishActivityByAnimation(mBaseActivity);
        dismiss();
    }
}
