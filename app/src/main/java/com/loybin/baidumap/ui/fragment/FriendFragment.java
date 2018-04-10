package com.loybin.baidumap.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.FriendPresenter;
import com.loybin.baidumap.ui.adapter.FriendAdapter;
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
public class FriendFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FriendFragment";
    @BindView(R.id.listview)
    ListView mListBook;

    @BindView(R.id.swiper_book)
    SwipeRefreshLayout mSwiperBook;

    @BindView(R.id.id_progress)
    public ProgressBar mIdProgress;

    @BindView(R.id.ll_placeholder)
    LinearLayout mLinearLayout;
    private FriendPresenter mFriendPresenter;
    private ArrayList<ResponseInfoModel.ResultBean.FriendsListBean> mMemberList;
    private FriendAdapter mWatchBookAdapter;
    private String mIsAdmin;
    private boolean mIsEditor;

    public FriendFragment() {

    }

    public FriendFragment(String isAdmin) {
        mIsAdmin = isAdmin;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mFriendPresenter = new FriendPresenter(getContext(), this);
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
        mWatchBookAdapter = new FriendAdapter(getContext(), mMemberList, mIsAdmin, this);
        mListBook.setAdapter(mWatchBookAdapter);
        mFriendPresenter.queryFriendsByImei(MyApplication.sToken, MyApplication.sDeviceListBean.getImei(), false);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
        MobclickAgent.onPageStart("FriendFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onRefresh() {
        mFriendPresenter.queryFriendsByImei(MyApplication.sToken, MyApplication.sDeviceListBean.getImei(), true);
        mSwiperBook.setRefreshing(false);
    }


    /**
     * 获取成功的朋友数据
     *
     * @param friendsList
     */
    public void onSuccess(List<ResponseInfoModel.ResultBean.FriendsListBean> friendsList) {
        if (friendsList.size() >= 1) {
            mLinearLayout.setVisibility(View.GONE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        mMemberList.clear();
        mMemberList.addAll(friendsList);
        mWatchBookAdapter.setData(friendsList);
        mWatchBookAdapter.notifyDataSetChanged();
    }


    /**
     * 点击了编辑
     */
    public void editor(boolean isEditor) {
        mIsEditor = isEditor;
        mWatchBookAdapter.setBoolean(isEditor);
        mWatchBookAdapter.notifyDataSetChanged();
        LogUtils.e(TAG, "点击了编辑" + isEditor);

    }

    public void deleteFriend(String acountName) {
        LogUtils.e(TAG, "deleteFriend");
        mFriendPresenter.deleteFriend(MyApplication.sToken, MyApplication.sDeviceListBean.getImei(), acountName);
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
        MobclickAgent.onPageEnd("FriendFragment");
    }
}
