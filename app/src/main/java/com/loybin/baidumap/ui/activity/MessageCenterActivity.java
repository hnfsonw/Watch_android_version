package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.MessageListModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.MessageCenterPresenter;
import com.loybin.baidumap.ui.adapter.MessageCenterAdapter;
import com.loybin.baidumap.ui.view.DefaultHeader;
import com.loybin.baidumap.ui.view.SpringView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/23 上午10:50
 * 描   述: 通知消息 view
 */
public class MessageCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener, SpringView.OnFreshListener {

    private static final java.lang.String TAG = "MessageCenterActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.listview)
    ListView mListView;

    @BindView(R.id.springview)
    SpringView mSpringView;

    private MessageCenterPresenter mMessageCenterPresenter;
    private List<ResponseInfoModel.ResultBean.MessageListBean> mMessageList;
    private MessageCenterAdapter mMessageCenterAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_center;
    }


    @Override
    protected void init() {
        if (mMessageCenterPresenter == null) {
            mMessageCenterPresenter = new MessageCenterPresenter(this, this);
        }
        if (mMessageList == null) {
            mMessageList = new ArrayList<>();
        }
        EventBus.getDefault().register(this);
        initView();
        initListener();
        initData();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayStart(MessageListModel startModel) {
        LogUtils.e(TAG, "通知消息被清空了");
        mMessageCenterPresenter.getTypesAndLastMessage(MyApplication.sToken, MyApplication.sAcountId);
    }


    private void initListener() {
        mListView.setOnItemClickListener(this);
    }


    private void initData() {
        if (mMessageCenterAdapter == null) {
            mMessageCenterAdapter = new MessageCenterAdapter(this, mMessageList);
        }
        mListView.setAdapter(mMessageCenterAdapter);

        mMessageCenterPresenter.getTypesAndLastMessage(MyApplication.sToken, MyApplication.sAcountId);
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.message_center));
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setHeader(new DefaultHeader(MyApplication.sInstance));
    }


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            default:
                break;
        }
    }


    @Override
    protected void dismissNewok() {

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
     * 获取成功的消息中心记录
     *
     * @param messageList
     */
    public void onSuccess(List<ResponseInfoModel.ResultBean.MessageListBean> messageList) {
            dismissLoading();
        if (messageList != null) {
            LogUtils.d(TAG, "messageList size = " + messageList.size());
//            for (int i = 0; i < messageList.size(); i++) {
//                LogUtils.d(TAG,"最后一条未读消息的状态 = "+messageList.get(i).getLastMessageStatus());
//                LogUtils.d(TAG,"消息类型名称 = "+messageList.get(i).getTypeName());
//                LogUtils.d(TAG,"消息类型描述 = "+messageList.get(i).getTypeDesc());
//                LogUtils.d(TAG,"最后一条未读消息内容 = "+messageList.get(i).getLastMessage());
//                LogUtils.d(TAG,"lastMessageAddTime = "+messageList.get(i).getLastMessageAddTime());
//                LogUtils.d(TAG,"getId = "+messageList.get(i).getId());
//                LogUtils.d(TAG,"unReadNum = "+messageList.get(i).getUnReadNum());
//            }
            mSpringView.onFinishFreshAndLoad();
            mMessageList.clear();
            mMessageList = messageList;
            mMessageCenterAdapter.setData(mMessageList);
            mMessageCenterAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String lastMessage = mMessageList.get(position).getLastMessage();
        if (lastMessage != null && lastMessage.length() > 1) {
            toActivity(MessageListActivity.class, mMessageList.get(position).getTypeDesc(),
                    mMessageList.get(position).getId() + "");
        } else {
            printn(getString(R.string.no_message));
        }

    }


    @Override
    public void onRefresh() {
        mMessageCenterPresenter.getTypesAndLastMessage(MyApplication.sToken, MyApplication.sAcountId);
    }

    @Override
    public void onLoadmore() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}
