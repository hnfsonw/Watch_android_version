package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.ClassBanPresenter;
import com.loybin.baidumap.ui.adapter.ClassBanAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangz on 17/8/26.
 * 描   述: 上课禁用的视图
 */

public class ClassBanActivity extends BaseActivity {


    private static final String TAG = "ClassBanActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    public TextView mTvRight;

    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;

    @BindView(R.id.iv_holiday_ban)
    ImageView mIvHolidayBan;

    @BindView(R.id.lv_class_time_list)
    ListView mLvClassTimeList;

    @BindView(R.id.btn_add_class_ban)
    Button mBtnAddClassBan;

    @BindView(R.id.rl_class)
    RelativeLayout mRlClass;

    @BindView(R.id.iv_class_add)
    ImageView mIvClassAdd;

    private List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> mClassBanList;
    private ClassBanAdapter mClassBanAdapter;
    private ClassBanPresenter mClassBanPresenter;
    public int turnPosition = 0;
    public String turnState = "";
    private Intent mIntent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_class_ban;
    }

    @Override
    protected void init() {
        mClassBanList = new ArrayList<>();
        if (mClassBanAdapter == null) {
            mClassBanAdapter = new ClassBanAdapter(this, mClassBanList);
        }
        if (mClassBanPresenter == null) {
            mClassBanPresenter = new ClassBanPresenter(this, this);
        }
        mLvClassTimeList.setAdapter(mClassBanAdapter);

        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mClassBanPresenter.getClassBanList(MyApplication.sToken, MyApplication.sDeviceId);
    }


    private void initView() {
        mTvTitle.setText(R.string.class_ban);
        mTvRight.setVisibility(View.GONE);
        mTvRight.setText(R.string.editor);
    }


    @Override
    protected void dismissNewok() {
        if (mClassBanPresenter.mForbiddenTimeList != null) {
            mClassBanPresenter.mForbiddenTimeList.cancel();

        }
    }

    @OnClick({R.id.iv_back, R.id.iv_holiday_ban, R.id.btn_add_class_ban, R.id.tv_right, R.id.iv_class_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                String mRight = mTvRight.getText().toString();
                mClassBanAdapter.toEdit(mRight);
                break;

            case R.id.iv_holiday_ban:
                break;

            case R.id.btn_add_class_ban:
                editClassTime("-1");
                break;

            case R.id.iv_class_add:
                editClassTime("-1");
                break;

            default:
                break;
        }
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 获取上课禁用列表
     *
     * @param classBanList
     */
    public void setClassBanList(List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> classBanList) {
        LogUtils.e(TAG, "classBanList.size():" + classBanList.size());
        if (classBanList.size() <= 0) {
            mTvRight.setVisibility(View.GONE);
            mRlClass.setVisibility(View.VISIBLE);
            mBtnAddClassBan.setVisibility(View.GONE);
        } else {
            mRlClass.setVisibility(View.GONE);
            mTvRight.setVisibility(View.VISIBLE);
            mBtnAddClassBan.setVisibility(View.VISIBLE);
        }
        mClassBanList.clear();
        mClassBanList = classBanList;
        mClassBanAdapter.setData(mClassBanList);
        mClassBanAdapter.notifyDataSetChanged();
    }


    /**
     * 更改开关请求发送
     *
     * @param id
     * @param state
     */
    public void changeTurnOn(long id, String state) {
        LogUtils.e(TAG, "更改开关");
        mClassBanPresenter.updateForbiddenTimeStateById(MyApplication.sToken, id, state);
    }


    /**
     * 更改开关成功回调
     */
    public void changeTurnSuccess() {
        if (turnState == "0") {
            mClassBanList.get(turnPosition).setState("1");
        } else {
            mClassBanList.get(turnPosition).setState("0");
        }
        mClassBanAdapter.setData(mClassBanList);
        mClassBanAdapter.notifyDataSetChanged();
    }


    /**
     * 删除上课禁用item
     *
     * @param id
     */
    public void deleteClassBan(long id) {
        mClassBanPresenter.deleteForbiddenTimeById(MyApplication.sToken, id);
    }


    /**
     * 删除上课禁用成功回调
     */
    public void deleteClassBanSuccess() {
        mClassBanList.remove(turnPosition);
        mClassBanAdapter.setData(mClassBanList);
        mClassBanAdapter.notifyDataSetChanged();
    }


    /**
     * 跳转至上课时间页面
     */
    public void editClassTime(String position) {
        if (mIntent == null) {
            mIntent = new Intent(this, ClassTimeActivity.class);
        }
        mIntent.putExtra("mClassBanList", (Serializable) mClassBanList);
        mIntent.putExtra("position", position);
        startActivity(mIntent);
    }
}
