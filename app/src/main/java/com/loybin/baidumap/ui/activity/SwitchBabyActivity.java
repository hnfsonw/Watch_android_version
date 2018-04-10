package com.loybin.baidumap.ui.activity;

import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.SwitchBabyPresenter;
import com.loybin.baidumap.ui.adapter.SwitchBabyAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/22 上午9:28
 * 描   述: 选择宝宝的视图
 */
public class SwitchBabyActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "SwitchBabyActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.list_baby)
    ListView mListBaby;

    @BindView(R.id.swiper_baby)
    SwipeRefreshLayout mSwiperBaby;

    private SwitchBabyPresenter mSwitchBabyPresenter;
    private List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceListBeanList;
    private SharedPreferences mGlobalvariable;
    private SwitchBabyAdapter mSwitchBabyAdapter;
    private long mAcountId;
    private String mToken;
    private String mAgain;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_switch_baby;
    }


    @Override
    protected void init() {
        mSwitchBabyPresenter = new SwitchBabyPresenter(this, this);
        mDeviceListBeanList = new ArrayList<>();
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        mToken = mGlobalvariable.getString("token", "");
        //从新选择
        mAgain = getIntent().getStringExtra(STRING);


        initView();
        initData();
        initListener();
        loadDeviceList();

    }


    private void initView() {
        if (mAgain != null) {
            mIvBack.setVisibility(View.GONE);
        }
        mTvTitle.setText(getString(R.string.switch_baby_));

    }


    private void initData() {
        mSwitchBabyAdapter = new SwitchBabyAdapter(this, mDeviceListBeanList);
        mListBaby.setAdapter(mSwitchBabyAdapter);
    }


    private void initListener() {
        mListBaby.setOnItemClickListener(this);
        mSwiperBaby.setOnRefreshListener(this);

    }

    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.e(TAG, "mAgain " + mAgain);
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            if (mAgain != null) {
                MyApplication.sInUpdata = true;
                finishActivityByAnimation(this);
            } else {
                finishActivityByAnimation(this);
            }
        }
        return true;
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishActivityByAnimation(this);
    }


    /**
     * 获取列表数据
     */
    private void loadDeviceList() {
        mSwiperBaby.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);
        mSwitchBabyPresenter.getAcountDeivceList(mAcountId, mToken, false);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyApplication.sDeivceNumber = position;
        mGlobalvariable.edit().putInt("deivceNumber", position).apply();
        MyApplication.sInUpdata = true;
        Log.d(TAG, "onItemClick: " + mDeviceListBeanList.get(position).getNickName());
        finishActivityByAnimation(this);
    }


    @Override
    public void onRefresh() {
        mSwitchBabyPresenter.getAcountDeivceList(mAcountId, mToken, true);
        mSwiperBaby.setRefreshing(false);

    }


    /**
     * 获取设备列表成功的通知
     *
     * @param data 设备列表集合
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        //保存在本地
        saveData2Local(MyApplication.sAcountId + "", data);
        //保存数据到内存中
        saveData2Mem(MyApplication.sAcountId + "", data);
        loadData(data);

    }


    /**
     * 加载数据
     *
     * @param data
     */
    private void loadData(ResponseInfoModel data) {
        try {
            List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = data.getResult().getDeviceList();
            Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
                @Override
                public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1, ResponseInfoModel.ResultBean.DeviceListBean o2) {
                    //降序排序
                    return (o2.getIsAdmin() - o1.getIsAdmin());
                }
            });
            LogUtils.e(TAG, deviceList.size() + "");
            LogUtils.e(TAG, deviceList.get(0).getAcountName());
            LogUtils.e(TAG, mSwitchBabyAdapter + "");
            mDeviceListBeanList.clear();
            mDeviceListBeanList.addAll(deviceList);
            mSwitchBabyAdapter.setData(deviceList);
        } catch (Exception e) {
            LogUtils.e(TAG, "加载数据异常 " + e.getMessage());
        }


    }


    /**
     * 获取设备列表失败通知
     *
     * @param resultMsg
     */
    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mSwitchBabyPresenter.mAcountDeivceList != null) {
            mSwitchBabyPresenter.mAcountDeivceList.cancel();
        }
    }
}
