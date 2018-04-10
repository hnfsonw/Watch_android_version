package com.loybin.baidumap.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.MessageListModel;
import com.loybin.baidumap.presenter.MessageListPresenter;
import com.loybin.baidumap.ui.adapter.MessageListAdapter;
import com.loybin.baidumap.ui.view.DefaultFooter;
import com.loybin.baidumap.ui.view.DefaultHeader;
import com.loybin.baidumap.ui.view.SpringView;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/23 下午4:32
 * 描   述: 通知消息列表view
 */
public class MessageListActivity extends BaseActivity implements SpringView.OnFreshListener, AdapterView.OnItemClickListener {


    private static final String TAG = "MessageListActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.listview)
    ListView mListview;

    @BindView(R.id.springview)
    SpringView mSpringView;
    private String mName;
    private String mId;
    private MessageListPresenter mMessageListPresenter;
    private int mPage = 1;
    private int conut = 20;
    private List<MessageListModel.ResultBean.MessageListBean.ListBean> mList;
    private MessageListAdapter mMessageListAdapter;
    private double mTotalCount;
    private Gson mGson;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_list;
    }

    @Override
    protected void init() {
        if (mMessageListPresenter == null) {
            mMessageListPresenter = new MessageListPresenter(this, this);
        }
        if (mList == null) {
            mList = new ArrayList<>();
        }

        mName = getIntent().getStringExtra(STRING);
        mId = getIntent().getStringExtra(BABY);

        LogUtils.e(TAG, "mId " + mId);
        initView();
        initListener();
        initData();
    }


    private void initListener() {
        if (mId.equals("2")) {
            mListview.setOnItemClickListener(this);
        }
    }


    private void initData() {
        if (mMessageListAdapter == null) {
            mMessageListAdapter = new MessageListAdapter(this, mList);
        }
        mListview.setAdapter(mMessageListAdapter);

        mMessageListPresenter.getMessagesByType(MyApplication.sToken, MyApplication.sAcountId, mId,
                mPage, conut);
    }


    private void initView() {
        mTvTitle.setText(mName);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.delete_cc));
        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setHeader(new DefaultHeader(MyApplication.sInstance));
        mSpringView.setFooter(new DefaultFooter(MyApplication.sInstance));
    }


    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void dismissNewok() {

    }


    /**
     * 获取成功的消息
     *
     * @param messageList
     */
    public void onSuccess(MessageListModel.ResultBean.MessageListBean messageList) {
        mTotalCount = messageList.getTotalCount();
        List<MessageListModel.ResultBean.MessageListBean.ListBean> list = messageList.getList();

        if (mList.size() < 1) {
            //第一次加载
            LogUtils.d(TAG, "第一次加载");
            mSpringView.onFinishFreshAndLoad();
            mPage++;
            mList.addAll(list);
            mMessageListAdapter.setData(mList);
            mMessageListAdapter.notifyDataSetChanged();
        } else {
            //已经加载过数据
            LogUtils.d(TAG, "已经加载过数据 mPage = " + mPage);
            mSpringView.onFinishFreshAndLoad();
            mPage++;
            mList.addAll(list);
            mMessageListAdapter.setData(mList);
            mMessageListAdapter.notifyDataSetChanged();
            mListview.smoothScrollToPosition(mList.size() - 18);
        }


    }


    @Override
    public void onRefresh() {
        mList.clear();
        mPage = 1;
        mMessageListPresenter.getMessagesByType(MyApplication.sToken, MyApplication.sAcountId, mId,
                mPage, 20);

    }


    @Override
    public void onLoadmore() {
        LogUtils.d(TAG, "总数 mTotalCount = " + mTotalCount + "总数 / 20 = " + mTotalCount / 20);
        LogUtils.d(TAG, "mPage = " + mPage);
        if (mPage < mTotalCount / 20 + 1) {
            mMessageListPresenter.getMessagesByType(MyApplication.sToken, MyApplication.sAcountId, mId,
                    mPage, conut);
        } else {
            mSpringView.onFinishFreshAndLoad();
            printn(getString(R.string.no_more));
        }


    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                mMessageListPresenter.deleteMessage(MyApplication.sToken, MyApplication.sAcountId,
                        mId);
                break;
        }
    }


    /**
     * 清空成功的通知
     */
    public void deleteSuccess() {
        dismissLoading();
        printn(getString(R.string.empty_success));
        EventBus.getDefault().post(new MessageListModel());
        finishActivityByAnimation(this);
    }


    /**
     * 清空失败的通知
     *
     * @param resultMsg
     */
    public void Error(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String msgAttr = mList.get(position).getMsgAttr();
        LogUtils.e(TAG, "getMsgAttr " + msgAttr);
        toActivity(SOSMapActivity.class, msgAttr);

    }
}
