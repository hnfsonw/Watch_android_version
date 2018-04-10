package com.loybin.baidumap.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.GroupChatPresenter;
import com.loybin.baidumap.ui.adapter.GroupChatListAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.util.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/25 下午4:24
 * 描   述: 群聊列表view
 */
public class GroupChatActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "GroupChatActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swiper_language)
    SwipeRefreshLayout mSwiperLanguage;

    private GroupChatPresenter mGroupChatPresenter;

    public GroupChatListAdapter mGroupChatListAdapter;

    private String mGroupId;
    private List<ResponseInfoModel.ResultBean.DeviceListBean> mDeviceList;
    private String mPhone;
    private String mMd5Password;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_chat;
    }


    @Override
    protected void init() {
        ButterKnife.bind(this);
        mGroupChatPresenter = new GroupChatPresenter(this, this);
        mDeviceList = new ArrayList<>();
        mGroupId = getIntent().getStringExtra(STRING);
        LogUtils.e(TAG, mGroupId);
        mPhone = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");
        mMd5Password = (String) SharedPreferencesUtils.getParam(this, "md5Password", "");
        initView();
        initListener();
        mGroupChatPresenter.initData(MyApplication.sToken, MyApplication.sAcountId);
        //加载会话列表
        mGroupChatPresenter.loadConversations(mGroupId);

        //环信还没登入
        if (!EMClient.getInstance().isLoggedInBefore()) {
            //环信登入
            mEMCLogin = 0;
            EMClient.getInstance().login(mPhone, mMd5Password, EMCazllzBack);
        } else {
            if (!EMClient.getInstance().isConnected()) {
                mEMCLogin = 0;
                LogUtils.e(TAG, "环信登入了,服务i起没连上");
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "退出成功");
                        //环信登入

                        EMClient.getInstance().login(mPhone, mMd5Password, EMCazllzBack);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "退出失败 " + i + " - " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });
            }
        }
        LogUtils.e(TAG, "环信是否登入  " + EMClient.getInstance().isLoggedInBefore());
        LogUtils.e(TAG, "是否连接上了环信服务器 " + EMClient.getInstance().isConnected());
    }

    private void initListener() {
        mSwiperLanguage.setOnRefreshListener(this);
    }


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                DevicesHomeActivity.sMessageSize = 0;
                finishActivityByAnimation(this);
                break;

        }
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.small_talk));
        mSwiperLanguage.setColorSchemeResources(R.color.btn, R.color.possible_result_points,
                R.color.tou_black_mask_ripple);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "initView: " + mGroupChatPresenter.getConversations().size());
        mGroupChatListAdapter = new GroupChatListAdapter(this, mGroupChatPresenter.getConversations(), mDeviceList);
        mRecyclerView.setAdapter(mGroupChatListAdapter);

        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        mGroupChatListAdapter.notifyDataSetChanged();
        DevicesHomeActivity.sMessageSize = 0;
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            DevicesHomeActivity.sMessageSize = 0;
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
    }


    EMMessageListener mEMMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //重新加载数据
            LogUtils.e(TAG, "重新加载数据");
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (mGroupId != null) {
                        mGroupChatPresenter.loadConversations(mGroupId);
                    }
                }
            });

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mGroupChatPresenter.mGroupMemberListAll != null) {
            mGroupChatPresenter.mGroupMemberListAll.cancel();
        }
    }


    /**
     * 获取群列表成功的通知
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        mDeviceList.clear();
        List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = data.getResult().getDeviceList();
        for (ResponseInfoModel.ResultBean.DeviceListBean list : deviceList) {
            LogUtils.e(TAG, "ID~~~~~~ " + list.getGroupId());
        }
        for (ResponseInfoModel.ResultBean.DeviceListBean list : deviceList) {
            String id = list.getGroupId() + "";
            if (id.equals(mGroupId)) {
                mDeviceList.add(list);
            }
        }
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1,
                               ResponseInfoModel.ResultBean.DeviceListBean o2) {
                long o1Id = o1.getGroupId();
                long o2Id = o2.getGroupId();
                return (int) (o2Id - o1Id);
            }
        });
        for (ResponseInfoModel.ResultBean.DeviceListBean list : deviceList) {
            String id = list.getGroupId() + "";
            if (!id.equals(mGroupId)) {
                mDeviceList.add(list);
            }
        }
        for (int i = 0; i < mDeviceList.size(); i++) {
            LogUtils.d(TAG, "获取成功的通知  " + mDeviceList.get(i).getGroupId() + "");
        }

        LogUtils.e(TAG, "获取成功的通知" + mDeviceList.size());
        deviceList.clear();
        deviceList.addAll(mDeviceList);
        mGroupChatListAdapter.setEMGroupInfo(deviceList);
    }


    /**
     * 获取群列表失败的回掉
     *
     * @param data
     */
    public void onError(ResponseInfoModel data) {
        dismissLoading();
        printn(data.getResultMsg());
    }


    @Override
    public void onRefresh() {
        mGroupChatPresenter.initData(MyApplication.sToken, MyApplication.sAcountId);
        mSwiperLanguage.setRefreshing(false);
    }


    /**
     * 环信登入成功失败回调
     */
    EMCallBack EMCazllzBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chekCache();
                    // 加载所有会话到内存
                    EMClient.getInstance().chatManager().loadAllConversations();
                    // 加载所有群组到内存，如果使用了群组的话
                    EMClient.getInstance().groupManager().loadAllGroups();

                    // 登录成功跳转界面
                    Log.d(TAG, "run: " + "环信已经登入了" + Thread.currentThread().getName());

                }
            });
        }

        @Override
        public void onError(final int i, final String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("LoginPresenter", "Error code:" + i + ", mMessage:" + s);
                    /**
                     * 关于错误码可以参考官方api详细说明
                     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                     */
                    switch (i) {
                        // 网络异常 2
                        case EMError.NETWORK_ERROR:
                            printn(getString(R.string.network_error));
                            break;
                        // 用户认证失败，用户名或密码错误 202
                        case EMError.USER_AUTHENTICATION_FAILED:
                            Log.d(TAG, "run: " + "环信账号或密码错误");
                            printn(getString(R.string.user_name_password_error));
                            break;
                        // 用户不存在 204
                        case EMError.USER_NOT_FOUND:
                            printn(getString(R.string.user_does_not_exist));
                            break;
                        // 无法访问到服务器 300
                        case EMError.SERVER_NOT_REACHABLE:
                            printn(getString(R.string.cannot_access_to_the_server));
                            break;
                        // 等待服务器响应超时 301
                        case EMError.SERVER_TIMEOUT:
                            printn(getString(R.string.service_timeout));
                            break;
                        // 服务器繁忙 302
                        case EMError.SERVER_BUSY:
                            printn(getString(R.string.server_is_busy));
                            break;
                        // 未知 Server 异常 303 一般断网会出现这个错误
                        case EMError.SERVER_UNKNOWN_ERROR:
                            printn(getString(R.string.unknown_server_exception));
                            break;

                        case EMError.USER_ALREADY_LOGIN:
                            Log.d(TAG, "200: " + "账号已登陆状态");
//                            onExit();

                            break;
                        default:
                            printn("ml_sign_in_failed code: " + i + ", mMessage:" + s + Thread.currentThread().getName());
                            break;
                    }
                }
            });
        }


        @Override
        public void onProgress(int i, String s) {

        }
    };
}
