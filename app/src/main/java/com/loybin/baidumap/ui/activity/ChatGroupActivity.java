package com.loybin.baidumap.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.ChatGroupPresenter;
import com.loybin.baidumap.ui.adapter.ChatGroupAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/05 下午4:48
 * 描   述: 群成员列表的视图
 */
public class ChatGroupActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "ChatGroupActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.list_chat_group)
    ListView mListChatGroup;


    @BindView(R.id.swiper_chat_group)
    public SwipeRefreshLayout mSwiperChatGroup;

    private ChatGroupPresenter mChatGroupPresenter;
    private String mGroupId;
    private List<ResponseInfoModel.ResultBean.MemberListBean> mMemberList;
    private ChatGroupAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat_group;
    }


    @Override
    protected void init() {
        mChatGroupPresenter = new ChatGroupPresenter(this, this);
        mMemberList = new ArrayList<>();
        mGroupId = getIntent().getStringExtra(STRING);

        initView();
        initData();
        initListener();
        loadData();
    }

    private void initListener() {
        mSwiperChatGroup.setOnRefreshListener(this);
    }


    private void loadData() {
        mChatGroupPresenter.getGroupMemberListAll(mGroupId, MyApplication.sAcountId, MyApplication.sToken, false);
    }


    private void initData() {
        mAdapter = new ChatGroupAdapter(this, mMemberList);
        mListChatGroup.setAdapter(mAdapter);
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.member_list));
        mSwiperChatGroup.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);
    }


    @Override
    protected void dismissNewok() {
        if (mChatGroupPresenter.mGroupMemberListAll != null) {
            mChatGroupPresenter.mGroupMemberListAll.cancel();
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finishActivityByAnimation(this);
    }


    public void onError(ResponseInfoModel s) {
        dismissLoading();
        printn(s.getResultMsg());
    }


    public void onScuuess(ResponseInfoModel data) {
        dismissLoading();
        List<ResponseInfoModel.ResultBean.MemberListBean> memberList = data.getResult().getMemberList();
//        Collections.sort(memberList, new Comparator<ResponseInfoModel.ResultBean.MemberListBean>() {
//            @Override
//            public int compare(ResponseInfoModel.ResultBean.MemberListBean o1,
//                               ResponseInfoModel.ResultBean.MemberListBean o2) {
//                //降序排序
//                return  (o2.getIsAdmin() - o1.getIsAdmin());
//            }
//        });
        LogUtils.e(TAG, memberList.size() + "");
        mMemberList.clear();
        mMemberList.addAll(memberList);
        mAdapter.setData(memberList);
    }


    @Override
    public void onRefresh() {
        mChatGroupPresenter.getGroupMemberListAll(mGroupId, MyApplication.sAcountId, MyApplication.sToken, true);
        mSwiperChatGroup.setRefreshing(false);
    }
}
