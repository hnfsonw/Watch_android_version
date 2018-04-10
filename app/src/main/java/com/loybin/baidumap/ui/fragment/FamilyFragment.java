package com.loybin.baidumap.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hojy.happyfruit.R;
import com.hyphenate.chat.EMClient;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.WatchLeisuresPresenter;
import com.loybin.baidumap.ui.activity.WXEntryActivity;
import com.loybin.baidumap.ui.adapter.WatchBookAdapter;
import com.loybin.baidumap.ui.view.ShareDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/26 下午2:51
 * 描   述: 添加设备view
 */
public class FamilyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {


    private static final String TAG = "FamilyFragment";
    @BindView(R.id.listview)
    ListView mListBook;

    @BindView(R.id.swiper_book)
    SwipeRefreshLayout mSwiperBook;

    @BindView(R.id.id_progress)
    ProgressBar mProgressBar;

    private String mIsAdmin;
    private WatchLeisuresPresenter mWatchLeisurePresenter;
    private ArrayList<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private WatchBookAdapter mWatchBookAdapter;

    private SharedPreferences mGlobalvariable;
    private long mAcountId;
    private String mToken;
    private long mGroupId;
    private int mDeviceId;
    private String newPhone;
    private ShareDialog mDialog;
    private String mAdminRelation;

    public FamilyFragment() {
    }

    public FamilyFragment(String isAdmin) {
        mIsAdmin = isAdmin;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mWatchLeisurePresenter = new WatchLeisuresPresenter(getContext(), this);
        mMemberList = new ArrayList();

        initData();
        initListener();

        super.onViewCreated(view, savedInstanceState);
    }


    private void initListener() {
        mSwiperBook.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);

        mListBook.setOnItemClickListener(this);
        LogUtils.e(TAG, "initListener ");
        mSwiperBook.setOnRefreshListener(this);
    }

    private void initData() {
        mWatchBookAdapter = new WatchBookAdapter(getContext(), mMemberList, mIsAdmin);
        mListBook.setAdapter(mWatchBookAdapter);
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
            Intent intent = new Intent(getContext(), WXEntryActivity.class);
            intent.putExtra("acountId", memberListBean.getAcountId());
            intent.putExtra("deviceId", memberListBean.getDeviceId());
            intent.putExtra("gender", memberListBean.getGender());
            intent.putExtra("relation", relation);
            intent.putExtra("phone", phone);
            intent.putExtra("shortPhone", memberListBean.getShortPhone());
            intent.putExtra("mAdminRelation", mAdminRelation);
            if (isAdmin.equals("1")) {
                //是管理员自己
                intent.putExtra("isAdmin", isAdmin);
            } else {
                //不是管理员自己~已注册
                if (acountType.equals("1")) {
//                    不是管理员自己~已注册/申请中!!!!
                    if (status.equals("1")) {
                        //
                        mWatchLeisurePresenter.showContactDialog(relation + ": " + relation + "(" + phone + ")" + "申请绑定手表");
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
        } else {
            return;
        }

    }


    public void refused() {
        LogUtils.d(TAG, "联系人拒绝");
        mWatchLeisurePresenter.setRefused(MyApplication.sToken, MyApplication.sUserAcountId
                , MyApplication.sUserdeviceId, 0);
    }


    public void agreed() {
        LogUtils.d(TAG, "联系人同意");
        mWatchLeisurePresenter.setRefused(MyApplication.sToken, MyApplication.sUserAcountId
                , MyApplication.sUserdeviceId, 1);
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mWatchLeisurePresenter.loadingList(mAcountId, mToken, mGroupId, true);
        mSwiperBook.setRefreshing(false);
    }


    /**
     * 获取设备联系人列表
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        MobclickAgent.onPageStart("FamilyFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        mProgressBar.setVisibility(View.VISIBLE);
        mGlobalvariable = getContext().getSharedPreferences("globalvariable", 0);
        mAcountId = mGlobalvariable.getLong("acountId", 0);
        mToken = MyApplication.sToken;
        mGroupId = MyApplication.sDeviceListBean.getGroupId();
        mDeviceId = MyApplication.sDeviceListBean.getDeviceId();

        LogUtils.d(TAG, "acountId: " + mAcountId);
        LogUtils.d(TAG, "token: " + mToken);
        LogUtils.d(TAG, "groupId: " + mGroupId);
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
        LogUtils.e(TAG, "memberList.size " + memberList.size());
        for (ResponseInfoModel.ResultBean.MemberListBean list : memberList) {
            LogUtils.e(TAG, "getIsAdmin " + list.getIsAdmin());
            if (list.getIsAdmin().equals("1")) {
                mAdminRelation = list.getRelation();
                LogUtils.d(TAG, "mAdminRelation " + mAdminRelation);
            }
        }


        mProgressBar.setVisibility(View.GONE);
        mMemberList.clear();
        mMemberList.addAll(memberList);
        mWatchBookAdapter.setData(memberList);
        if (newPhone != null) {
            chekAcountType();
        }
    }


    /**
     * 添加成功返回的号码
     * 用来判断此号码是否已注册
     */
    private void chekAcountType() {
        for (ResponseInfoModel.ResultBean.MemberListBean list : mMemberList) {
            if (list.getPhone().equals(newPhone)) {
                if (list.getAcountType().equals("0")) {
                    //未注册
                    LogUtils.e(TAG, newPhone + "未注册 弹出邀请");
                    newPhone = null;
                    if (mDialog == null) {
                        mDialog = new ShareDialog(getActivity(), this);
                    }
                    mDialog.show();
                    mDialog.setPhone(list.getPhone(), mAdminRelation);
                }
            }
        }
    }


    /**
     * 同意成功的回调
     */
    public void noticeSuccess() {
        mWatchLeisurePresenter.loadingList(mAcountId, mToken, mGroupId, false);
    }


    /**
     * @param phone
     */
    public void setPhone(String phone) {
        newPhone = phone;
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
        MobclickAgent.onPageEnd("FamilyFragment");
    }


}
