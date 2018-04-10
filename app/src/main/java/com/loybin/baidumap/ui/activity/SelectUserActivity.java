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
import com.loybin.baidumap.presenter.SelectUserPresenter;
import com.loybin.baidumap.ui.adapter.SelectUserAdapter;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午7:16
 * 描   述: 选择用户view
 */
public class SelectUserActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String TAG = "SelectUserActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.list_geofence)
    ListView mListGeofence;

    @BindView(R.id.swiper_geofence)
    public SwipeRefreshLayout mSwiperGeofence;

    private SelectUserPresenter mSelectUserPresenter;
    private SharedPreferences mGlobalvariable;
    private SelectUserAdapter mSelectUserAdapter;
    private List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private long mAcountId;
    private String mToken;
    private long mGroupId;
    public String mOrdinary;
    public String mManagement;
    private int mDeviceId;
    private RemoveDialog mRemoveDialog;
    private ResponseInfoModel.ResultBean.MemberListBean mMemberListBean;
    private ResponseInfoModel.ResultBean.MemberListBean mManagementListBean;
    private String mImei;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_select_user;
    }


    @Override
    protected void init() {
        mSelectUserPresenter = new SelectUserPresenter(this, this);
        mMemberList = new ArrayList<>();
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, this);
        }
        mOrdinary = getIntent().getStringExtra(STRING);
        mManagement = getIntent().getStringExtra(STRING);
        initView();
        initData();
        initListener();

    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initListener() {
        mListGeofence.setOnItemClickListener(this);
        mSwiperGeofence.setOnRefreshListener(this);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.Please_select_a_user));
        mSwiperGeofence.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);
    }


    private void initData() {
        mSelectUserAdapter = new SelectUserAdapter(this, mMemberList);
        mListGeofence.setAdapter(mSelectUserAdapter);

    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishActivityByAnimation(this);
    }


    @Override
    public void onRefresh() {
        mSelectUserPresenter.loadingList(mAcountId, mToken, mGroupId, true);
        mSwiperGeofence.setRefreshing(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        mToken = mGlobalvariable.getString("token", "");
        mGroupId = mGlobalvariable.getLong("groupId", 0);
        mDeviceId = mGlobalvariable.getInt("deviceId", 0);
        mImei = mGlobalvariable.getString("imei", "");

        Log.d(TAG, "acountId: " + mAcountId);
        Log.d(TAG, "token: " + mToken);
        Log.d(TAG, "groupId: " + mGroupId);
        mSelectUserPresenter.loadingList(mAcountId, mToken, mGroupId, false);
    }


    public void onSuccess(List<ResponseInfoModel.ResultBean.MemberListBean> memberList) {
        dismissLoading();
        mMemberList.addAll(memberList);

        mSelectUserAdapter.setData(memberList);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //解绑普通成员
        if (mOrdinary.equals("ordinary")) {
            mMemberListBean = mMemberList.get(position);
            mRemoveDialog.show();
            mRemoveDialog.initUserName(mMemberListBean.relation + "?");

            setSelectUserInter(new SelectUserInter() {
                @Override
                public void deleteGeod() {
                    mMemberList.remove(position);
                    mSelectUserAdapter.notifyDataSetChanged();
                }
            });

        }

        //解绑自己
        if (mManagement.equals("management")) {
            mManagementListBean = mMemberList.get(position);
            if (mAcountId == Long.parseLong(mManagementListBean.getAcountId())) {
                printn(getString(R.string.you_have_is_the_facility_manager));
                return;
            }
            mRemoveDialog.show();
            mRemoveDialog.initTitle(getString(R.string.hand_over_control_to));
            mRemoveDialog.initUserName(mManagementListBean.relation + "?");
        }

    }


    /**
     * 确定删除
     */

    @Override
    public void cancel() {
        //解除普通成员
        if (mOrdinary.equals("ordinary")) {
            mSelectUserPresenter.removeOrdinary(mMemberListBean, mToken, mDeviceId);
        }

        //移交管理权
        if (mManagement.equals("management")) {
            mSelectUserPresenter.handOverAdmin(mManagementListBean, mToken, mDeviceId, mAcountId);
        }
    }


    /**
     * 删除成功通知adapter 去删除
     */
    public void removeSuccess() {
        dismissLoading();
        if (mSelectUserInter != null) {
            mSelectUserInter.deleteGeod();
        }
        printn(getString(R.string.remove_success));
    }


    /**
     * 删除失败的回掉
     *
     * @param resultMsg
     */
    public void removeError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 移交管理员 解绑自己失败
     *
     * @param resultMsg
     */
    public void errorManagement(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 移交管理员 解绑自己成功
     */
    public void successManagement() {
        dismissLoading();
        printn(getString(R.string.transfer_success));
        if (MyApplication.sDeivceNumber != 0) {
            MyApplication.sDeivceNumber--;
        }
        //清除缓存
        deleteData();
        MyApplication.sInUpdata = true;
        exitHomeActivity();
        toActivity(DevicesHomeActivity.class, 0);
        finishActivityByAnimation(this);
    }


    /**
     * 清除缓存
     */
    private void deleteData() {
        String locationData = getCacheFile(mImei).getAbsolutePath();
        String devcesData = getCacheFile(mImei + mAcountId).getAbsolutePath();
        //清除内存
        deleteData2Mem(mImei);
        deleteData2Mem(mImei + mAcountId);
        //清除本地
        FileUtils.deleteFile(devcesData);
        FileUtils.deleteFile(locationData);

    }


    public SelectUserInter mSelectUserInter;


    public void setSelectUserInter(SelectUserInter selectUserInter) {
        mSelectUserInter = selectUserInter;
    }


    public interface SelectUserInter {

        void deleteGeod();

    }

    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mSelectUserPresenter.mDisBandAcountAndChangeAdmin != null) {
            mSelectUserPresenter.mDisBandAcountAndChangeAdmin.cancel();
        }
        if (mSelectUserPresenter.mDisBandOneAcount != null) {
            mSelectUserPresenter.mDisBandOneAcount.cancel();
        }
        if (mSelectUserPresenter.mGetGroupMemberList != null) {
            mSelectUserPresenter.mGetGroupMemberList.cancel();
        }
    }
}
