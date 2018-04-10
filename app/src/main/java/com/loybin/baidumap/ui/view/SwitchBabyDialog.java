package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.adapter.SwitchDialogAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  选择宝贝提示框
 */
public class SwitchBabyDialog extends AlertDialog implements AdapterView.OnItemClickListener, View.OnClickListener {


    private static final String TAG = "BabyDataActivity";
    @BindView(R.id.select_list)
    ListView mListView;

    @BindView(R.id.ll_add_device)
    LinearLayout mLlAddDevice;

    private List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceListBeanList;
    private BaseActivity mBaseActivity;
    private Context mContext;

    public SwitchBabyDialog(Context context, BaseActivity baseActivity, List<ResponseInfoModel.ResultBean.DeviceListBean> deviceListBeanList) {
        super(context, R.style.SelectDialog2);
        mContext = context;
        mBaseActivity = baseActivity;
        mDeviceListBeanList = deviceListBeanList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_switch_baby);
        ButterKnife.bind(this);
//        getWindow().setGravity(Gravity.CENTER); //显示在中间
//        setCanceledOnTouchOutside(false);
        initData(mDeviceListBeanList);
        initListener();
    }


    private void initListener() {
        mListView.setOnItemClickListener(this);
        mLlAddDevice.setOnClickListener(this);
    }


    private void initData(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceListBeanList) {
        LogUtils.e(TAG, "size" + deviceListBeanList.size());
        SwitchDialogAdapter adapter = new SwitchDialogAdapter(mContext, deviceListBeanList);
        mListView.setAdapter(adapter);
    }


    /**
     * 设置宽度全屏，要设置在show的后面
     */
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        layoutParams.gravity = Gravity.TOP;
//        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = ScreenUtils.dpToPx(mContext, 0); // 新位置X坐标
        layoutParams.y = ScreenUtils.dpToPx(mContext, -200); // 新位置Y坐标
        if (mDeviceListBeanList.size() > 1) {
            layoutParams.height = ScreenUtils.dpToPx(mContext, 150);
            layoutParams.y = ScreenUtils.dpToPx(mContext, -180);
        }
        getWindow().getDecorView().setPadding(120, 0, 120, 0);
        getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LogUtils.e(TAG, position + "");
            if (mDeviceListBeanList.get(position).getDeviceId() == MyApplication.sDeviceId){
                dismiss();
                return;
            }
//            if (position == MyApplication.sDeivceNumber) {
//                dismiss();
//                return;
//            }
            MyApplication.sDeivceNumber = position;
            DevicesHomeActivity devicesHomeActivity = (DevicesHomeActivity) mContext;
            LogUtils.d(TAG, "sDeivceNumber: " + MyApplication.sDeivceNumber);

            devicesHomeActivity.mDevicesHomePresenter.moveMarker();

            dismiss();
        } catch (Exception e) {
            LogUtils.e(TAG, "切换宝贝 异常" + e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        DevicesHomeActivity devicesHomeActivity = (DevicesHomeActivity) mContext;

        devicesHomeActivity.toCanmeraPermissions();
        dismiss();
    }
}
