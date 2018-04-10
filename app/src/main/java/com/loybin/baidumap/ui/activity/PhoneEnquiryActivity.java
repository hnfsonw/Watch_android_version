package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hojy.happyfruit.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.ParametersModel;
import com.loybin.baidumap.model.PhoneCostsBean;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.PhoneEnquiryPresenter;
import com.loybin.baidumap.ui.adapter.PhoneCostsAdapter;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.ScreenUtils;
import com.loybin.baidumap.util.SharedPreferencesUtils;
import com.loybin.baidumap.util.ThreadUtils;
import com.loybin.baidumap.util.UserUtil;
import com.loybin.baidumap.widget.chatrow.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huangz on 17/8/18.
 * 查询话费view
 */

public class PhoneEnquiryActivity extends BaseActivity
        implements ViewTreeObserver.OnGlobalLayoutListener,
        View.OnKeyListener, View.OnTouchListener {

    private static final String TAG = "PhoneEnquiryActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_right)
    TextView tvRight;

    @BindView(R.id.iv_confirm)
    ImageView ivConfirm;

    @BindView(R.id.rv_mainList)
    RecyclerView mRvMainList;

    @BindView(R.id.iv_change_input)
    ImageView ivChangeInput;

    @BindView(R.id.tv_check_costs)
    TextView tvCheckCosts;

    @BindView(R.id.tv_check_flow)
    TextView tvCheckFlow;

    @BindView(R.id.ll_check_costs)
    LinearLayout llCheckCosts;

    @BindView(R.id.iv_change_check)
    ImageView ivChangeCheck;

    @BindView(R.id.et_check_command)
    LastInputEditText etCheckCommand;

    @BindView(R.id.ll_check_costs_input)
    LinearLayout llCheckCostsInput;

    @BindView(R.id.ll_all_items)
    LinearLayout llAllItems;

    @BindView(R.id.tv_send_btn)
    TextView mTvSendBtn;

    public int mCount;
    private List<PhoneCostsBean> mCheckList;
    private PhoneCostsAdapter mPhoneCostsAdapter;
    private PhoneCostsBean mPhoneCostsBean;
    private Intent mIntent;
    private PhoneEnquiryPresenter mPhoneEnquiryPresenter;
    private String mAppAccount;
    //话费查询指令
    private int mOperator;
    private String mBillCmd;
    private String mServiceNumber;
    private String mCustomCmd;
    private String mFlowCmd;
    private Gson mGson;
    private ParametersModel.ParametersBean mParameters;
    private long mTime;
    public String mGender;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_phone_enquiry;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mAppAccount = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");
        if (mPhoneEnquiryPresenter == null) {
            mPhoneEnquiryPresenter = new PhoneEnquiryPresenter(this, this);
        }

        mPhoneEnquiryPresenter.getSmsCmd(MyApplication.sToken, MyApplication.sDeviceId);

        mGender = getIntent().getStringExtra(STRING);

        mCheckList = (List) loadDataFromLocalBean("CheckCostsList" + MyApplication.sDeviceId,
                Constant.PROTOCOL_TIMEOUT_MONTH, 0);
        if (mCheckList == null) {
            mCheckList = new ArrayList<>();
            mCount = 0;
        } else {
            mCount = mCheckList.size();
        }

        mPhoneCostsAdapter = new PhoneCostsAdapter(this, mCheckList);
        mRvMainList.setAdapter(mPhoneCostsAdapter);
        mRvMainList.setHasFixedSize(true);
        mRvMainList.setLayoutManager(new LinearLayoutManager(this));

        initView();
        initListener();

    }

    private void initListener() {
        etCheckCommand.setOnKeyListener(this);
        etCheckCommand.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mRvMainList.setOnTouchListener(this);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    private void initView() {
        tvTitle.setText(getString(R.string.checkCosts));
        tvRight.setText("清空");
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
        mPhoneCostsAdapter.notifyDataSetChanged();
        mRvMainList.smoothScrollToPosition(mCheckList.size());
    }


    @Override
    protected void dismissNewok() {
        if (mPhoneEnquiryPresenter.mInfoModelCall != null) {
            mPhoneEnquiryPresenter.mInfoModelCall.cancel();
        }

        if (mPhoneEnquiryPresenter.mResponseInfoModelCall != null) {
            mPhoneEnquiryPresenter.mResponseInfoModelCall.cancel();
        }

        if (mPhoneEnquiryPresenter.mSmsCmd != null) {
            mPhoneEnquiryPresenter.mSmsCmd.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_title, R.id.tv_right, R.id.iv_confirm,
            R.id.iv_change_input, R.id.tv_check_costs, R.id.tv_send_btn,
            R.id.tv_check_flow, R.id.ll_check_costs, R.id.iv_change_check,
            R.id.et_check_command, R.id.ll_check_costs_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_title:
                break;

            case R.id.tv_right:
                mCheckList.clear();
                mPhoneCostsAdapter.notifyDataSetChanged();
                mCount = 0;
                saveData2Local("CheckCostsList" + MyApplication.sDeviceId, mCheckList);
                break;


            case R.id.iv_change_input:
                llCheckCosts.setVisibility(View.GONE);
                llCheckCostsInput.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_change_check:
                llCheckCostsInput.setVisibility(View.GONE);
                UserUtil.hideSoftInput(this);
                llCheckCosts.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_check_costs:
                LogUtils.e(TAG, "查询话费");
                if (!MyApplication.sDeviceListBean.getImei().equals("")) {
                    mPhoneEnquiryPresenter.phoneCost(MyApplication.sToken, mAppAccount,
                            MyApplication.sDeviceListBean.getImei(), Constants.COMMAND10022, mBillCmd,
                            mServiceNumber);
                }
                LogUtils.e(TAG, "设备ID：" + MyApplication.sDeviceId);
                break;

            case R.id.tv_check_flow:
                LogUtils.e(TAG, "查询流量");
                if (!MyApplication.sDeviceListBean.getImei().equals("")) {
                    mPhoneEnquiryPresenter.phoneFlow(MyApplication.sToken, mAppAccount,
                            MyApplication.sDeviceListBean.getImei(), Constants.COMMAND10022, mFlowCmd,
                            mServiceNumber);
                }
                break;

            case R.id.tv_send_btn:
                sendCommand();
                break;
        }
    }


    //MQTT消息通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mqttMessage (ParametersModel parametersModel) {
        LogUtils.e("MQTT","话费 + MQTT消息通知");
        if (parametersModel == null){
            return;
        }
        mParameters = parametersModel.getParameters();
        if (mParameters != null){
        chekCommand(parametersModel.getCommand(),mParameters.getContent());
        }
    }

    /**
     * 环信透传消息回调接口
     */
    public EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            if (messages == null) {
                return;
            }
        }

        @Override
        public void onCmdMessageReceived(final List<EMMessage> messages) {
            try {
                //收到透传消息
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoading();
                        Log.d(TAG, "收到透传消息: " + messages.size());
                        if (messages.size() > 0) {
                            for (int i = 0; i < messages.size(); i++) {
                                EMMessage cmdMessage = messages.get(i);
                                EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                                String action = body.action();
//                                addItem(2, );
                                parseData(action);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                LogUtils.e(TAG, "透传异常" + e.getMessage());
            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    /**
     * @param result 接收的透传消息
     */
    private void parseData(String result) {
        try {
            if (mGson == null) {
                mGson = new Gson();
            }
            Log.i(TAG, "收到透传消息 :" + result + "~~ " + Thread.currentThread().getName());

            ParametersModel parametersModel = mGson.fromJson(result, ParametersModel.class);
            if (parametersModel == null){
                return;
            }
            mParameters = parametersModel.getParameters();
            if (mParameters != null){
                String content = mParameters.getContent();
                int command = parametersModel.getCommand();

                LogUtils.e(TAG, content + "content");
                LogUtils.e(TAG, "command  :" + command);
                if (mParameters.getDeviceId() == MyApplication.sDeviceId) {
                    chekCommand(command, content);
                }
            }

        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 添加新的指令显示
     */
    public void addItem(int commandType, String content, long time) {
        mPhoneCostsBean = new PhoneCostsBean();
        mPhoneCostsBean.setId(mCount + 1);
        mPhoneCostsBean.setCommandType(commandType);
        mPhoneCostsBean.setContent(content);
        mPhoneCostsBean.setTime(time);
        mCheckList.add(mPhoneCostsBean);
        mCount = mCheckList.size();
        saveData2Local("CheckCostsList" + MyApplication.sDeviceId, mCheckList);
        mPhoneCostsAdapter.notifyDataSetChanged();
        mRvMainList.smoothScrollToPosition(mCheckList.size());
    }


    /**
     * 添加新的指令显示
     */
    public void addItem(int commandType, String content, long time, String imgurl) {
        mPhoneCostsBean = new PhoneCostsBean();
        mPhoneCostsBean.setId(mCount + 1);
        mPhoneCostsBean.setCommandType(commandType);
        mPhoneCostsBean.setContent(content);
        mPhoneCostsBean.setTime(time);
        mPhoneCostsBean.setImgrul(imgurl);
        mCheckList.add(mPhoneCostsBean);
        mCount = mCheckList.size();
        saveData2Local("CheckCostsList" + MyApplication.sDeviceId, mCheckList);
        mPhoneCostsAdapter.notifyDataSetChanged();
        mRvMainList.smoothScrollToPosition(mCheckList.size());
    }


    /**
     * 在键盘显示或隐藏时控制输入框位置
     */
    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Rect r = new Rect();
            //获取当前界面可视部分
            PhoneEnquiryActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = PhoneEnquiryActivity.this.getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;
            Log.d("Keyboard Size", "Size: " + heightDifference);
            RelativeLayout.LayoutParams rp = new
                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (heightDifference > 0) {
                rp.setMargins(0, screenHeight - heightDifference -
                        ScreenUtils.dpToPx(PhoneEnquiryActivity.this, 45), 0, 0);
                llCheckCostsInput.setLayoutParams(rp);
            } else {
                rp.setMargins(0, screenHeight - ScreenUtils.dpToPx(PhoneEnquiryActivity.this, 45), 0, 0);
                llCheckCostsInput.setLayoutParams(rp);
            }
        }
    }


    /**
     * 软键盘发送键监听
     */
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_UP) {
            sendCommand();
        }
        return false;
    }


    /**
     * 发送自定义指令
     */
    private void sendCommand() {
        LogUtils.e(TAG, "自定义指令");
        Date dateTime = new Date();
        long time = dateTime.getTime();
        mPhoneCostsBean = new PhoneCostsBean();
        String command = etCheckCommand.getText().toString().trim();
        if (command == null || "".equals(command)) {
            Toast.makeText(PhoneEnquiryActivity.this, "自定义指令不能为空", Toast.LENGTH_SHORT).show();
        } else {
            etCheckCommand.setText("");
            if (!MyApplication.sDeviceListBean.getImei().equals("")) {
                mPhoneEnquiryPresenter.customCommand(MyApplication.sToken, mAppAccount,
                        MyApplication.sDeviceListBean.getImei(), Constants.COMMAND10022, command,
                        mServiceNumber);
            }

            mRvMainList.smoothScrollToPosition(mCheckList.size());
            UserUtil.hideSoftInput(PhoneEnquiryActivity.this);
        }
    }


    /**
     * 点击屏幕隐藏虚拟键盘
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == motionEvent.ACTION_DOWN) {
            UserUtil.hideSoftInput(PhoneEnquiryActivity.this);
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        try {
            //取消事件注册
            EventBus.getDefault().unregister(this);
            super.onDestroy();
        }catch (Exception e){
            Log.d(TAG,"StoryActivity onDestroy 异常" +e.getMessage());
        }
    }


    /**
     * 查询话费流量指令参数
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        if (data.getResult() != null) {
            ResponseInfoModel.ResultBean result = data.getResult();
            mOperator = result.getOperator();
            mBillCmd = result.getBillCmd();
            mServiceNumber = result.getServiceNumber();
            mCustomCmd = result.getCustomCmd();
            mFlowCmd = result.getFlowCmd();
            mTime = data.getTime();

            LogUtils.e(TAG, "运营商 " + mOperator);
            LogUtils.e(TAG, "话费查询 " + mBillCmd);
            LogUtils.e(TAG, "短息服务中心 " + mServiceNumber);
            LogUtils.e(TAG, "自定义 " + mCustomCmd);
            LogUtils.e(TAG, "流量查询 " + mFlowCmd);

            LogUtils.e(TAG, "时间值 = " + mTime);
        }
    }


    /**
     * 判断透传类型
     *
     * @param command
     */
    private void chekCommand(int command, String result) {
        switch (command) {
            case Constants.COMMAND10021:
                Date dateTime = new Date();
                long time = dateTime.getTime();
                LogUtils.e(TAG, "当前时间 :" + time);
                addItem(1, result, time, MyApplication.sDeviceListBean.getImgUrl());
                break;
        }
    }

}
