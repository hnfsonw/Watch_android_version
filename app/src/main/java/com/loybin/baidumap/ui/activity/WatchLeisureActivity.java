package com.loybin.baidumap.ui.activity;

import android.content.Intent;
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
import com.hyphenate.chat.EMClient;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.WatchLeisurePresenter;
import com.loybin.baidumap.ui.adapter.WatchBookAdapter;
import com.loybin.baidumap.ui.view.ShareDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/19 下午4:29
 * 描   述: 手表通讯录的视图
 */
public class WatchLeisureActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {


    private static final String TAG = "WatchLeisureActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.swiper_book)
    SwipeRefreshLayout mSwiperBook;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.listview)
    ListView mListBook;


    private WatchLeisurePresenter mWatchLeisurePresenter;
    private List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private SharedPreferences mGlobalvariable;
    private long mAcountId;
    private String mToken;
    private long mGroupId;
    private int mDeviceId;
    private WatchBookAdapter mWatchBookAdapter;
    private String mIsAdmin;
    private String mPhone;
    private ShareDialog mDialog;
    public String mAdminRelation;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_watch_book;
    }


    @Override
    protected void init() {
        mWatchLeisurePresenter = new WatchLeisurePresenter(this, this);
        mMemberList = new ArrayList();
        mIsAdmin = getIntent().getStringExtra(STRING);
        LogUtils.d(TAG, "mIsAdmin " + mIsAdmin);
        if (mIsAdmin == null) {
            mIsAdmin = "0";
        }
        initView();
        initData();
        initListener();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.watch_the_address_book));
        mSwiperBook.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);
        if (mIsAdmin.equals("0")) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(getString(R.string.add_leisure));
        }
    }


    private void initData() {
        mWatchBookAdapter = new WatchBookAdapter(this, mMemberList, mIsAdmin);
        mListBook.setAdapter(mWatchBookAdapter);
    }


    private void initListener() {
        mListBook.setOnItemClickListener(this);
        LogUtils.e(TAG, "initListener ");
        mSwiperBook.setOnRefreshListener(this);


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
     * item的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.d(TAG, "position  " + position);
        ResponseInfoModel.ResultBean.MemberListBean memberListBean = mMemberList.get(position);
        String isAdmin = memberListBean.getIsAdmin();
        //状态 0 正常 1 申请中
        String status = memberListBean.getStatus();

        //是否已绑定设备0 否 1 是
        String isBind = memberListBean.getIsBind();

        //是否已注册 0 未注册APP 1 已注册APP
        String acountType = memberListBean.getAcountType();
        String phone = memberListBean.getPhone();
        String relation = memberListBean.getRelation();
        if (mIsAdmin.equals("1")) {
            Intent intent = new Intent(this, WXEntryActivity.class);
            intent.putExtra("acountId", memberListBean.getAcountId());
            intent.putExtra("deviceId", memberListBean.getDeviceId());
            intent.putExtra("gender", memberListBean.getGender());
            intent.putExtra("relation", relation);
            intent.putExtra("phone", phone);
            intent.putExtra("mAdminRelation", mAdminRelation);
            intent.putExtra("shortPhone", memberListBean.getShortPhone());
            if (isAdmin.equals("1")) {
                //是管理员自己
                intent.putExtra("isAdmin", isAdmin);
            } else {
                //不是管理员自己~已注册
                if (acountType.equals("1")) {
//                    不是管理员自己~已注册/申请中!!!!
                    if (status.equals("1")) {
                        //
                        showContactDialog(relation + ": " + relation + "(" + phone + ")" + "申请绑定手表", 0,this);
                        MyApplication.sUserAcountId = memberListBean.getAcountId();
                        MyApplication.sUserdeviceId = memberListBean.getDeviceId();
                        EMClient.getInstance().chatManager().deleteConversation(phone, true);
                        return;
                    } else {
                        //未申请
                        intent.putExtra("isBind", isBind);
                        intent.putExtra("acountType", acountType);
                    }

                } else {
                    //未注册
                    intent.putExtra("acountType", acountType);
                }
            }
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            return;
        }

    }


    @Override
    public void refused() {
        LogUtils.d(TAG, "联系人拒绝");
        mWatchLeisurePresenter.setRefused(MyApplication.sToken, MyApplication.sUserAcountId, MyApplication.sUserdeviceId, 0);
    }


    @Override
    public void agreed() {
        LogUtils.d(TAG, "联系人同意");
        mWatchLeisurePresenter.setRefused(MyApplication.sToken, MyApplication.sUserAcountId, MyApplication.sUserdeviceId, 1);
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mWatchLeisurePresenter.loadingList(mAcountId, mToken, mGroupId, true);
        mSwiperBook.setRefreshing(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        mToken = MyApplication.sToken;
        mGroupId = MyApplication.sDeviceListBean.getGroupId();
        mDeviceId = MyApplication.sDeviceListBean.getDeviceId();

        Log.d(TAG, "acountId: " + mAcountId);
        Log.d(TAG, "token: " + mToken);
        Log.d(TAG, "groupId: " + mGroupId);
        try {
            mWatchLeisurePresenter.loadingList(mAcountId, mToken, mGroupId, false);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    /**
     * 获取列表成功的通知
     *
     * @param memberList
     */
    public void onsuccess(List<ResponseInfoModel.ResultBean.MemberListBean> memberList) {
        dismissLoading();
        Log.d(TAG, "onsuccess: ");
        for (ResponseInfoModel.ResultBean.MemberListBean list : memberList) {
            LogUtils.e(TAG, "getIsAdmin " + list.getIsAdmin());
            if (list.getIsAdmin().equals("1")) {
                mAdminRelation = list.getRelation();
                Log.d(TAG, "onsuccess: " + mAdminRelation);
                LogUtils.d(TAG, "mAdminRelation " + mAdminRelation);
            }
        }

        mMemberList.addAll(memberList);
        mWatchBookAdapter.setData(memberList);
        if (mPhone != null) {
            chekAcountType();
        }
    }


    /**
     * 添加成功返回的号码
     * 用来判断此号码是否已注册
     */
    private void chekAcountType() {
        for (ResponseInfoModel.ResultBean.MemberListBean list : mMemberList) {
            if (list.getPhone().equals(mPhone)) {
                if (list.getAcountType().equals("0")) {
                    //未注册
                    LogUtils.e(TAG, mPhone + "未注册 弹出邀请");
                    mPhone = null;
                    if (mDialog == null) {
                        mDialog = new ShareDialog(this, this);
                    }
                    mDialog.show();
                    mDialog.setPhone(list.getPhone(), mAdminRelation);
                }
            }
        }
    }

    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mWatchLeisurePresenter.mGetGroupMemberList != null) {
            mWatchLeisurePresenter.mGetGroupMemberList.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                //添加联系人
                toActivity(100, AddLeisureActivity.class, "add");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            mPhone = data.getStringExtra("phone");
            LogUtils.d(TAG, "phone " + mPhone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 同意成功的回调
     */
    public void noticeSuccess() {
        mWatchLeisurePresenter.loadingList(mAcountId, mToken, mGroupId, false);
    }
}
