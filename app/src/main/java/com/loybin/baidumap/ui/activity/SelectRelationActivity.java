package com.loybin.baidumap.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.SelectRelationPresenter;
import com.loybin.baidumap.ui.adapter.SelectRelationAdapter;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/10 上午10:10
 * 描   述: 选择关系
 */
public class SelectRelationActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "SelectRelationActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.gridview)
    GridView mGridview;
    private Intent mIntent;

    //用来判断是否新注册 同意邀请的
    private String mAfreed;
    private SelectRelationAdapter mSelectRelationAdapter;
    private SelectRelationPresenter mSelectRelationPresenter;
    private String mDeviceId;
    private String mAppAccount;
    private String mRelation;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_select_relation;
    }


    @Override
    protected void init() {
        if (mSelectRelationPresenter == null) {
            mSelectRelationPresenter = new SelectRelationPresenter(this, this);
        }
        mAppAccount = (String) SharedPreferencesUtils.getParam(this, "appAccount", "");
        mAfreed = getIntent().getStringExtra(STRING);
        mDeviceId = getIntent().getStringExtra(BABY);
        initView();
        initGridData();
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


    private void initView() {
        mTvTitle.setText(getString(R.string.the_choice));
    }


    private void initGridData() {
        if (mSelectRelationAdapter == null) {
            mSelectRelationAdapter = new SelectRelationAdapter(this, mTitles, mIcons);
        }
        mGridview.setAdapter(mSelectRelationAdapter);
    }


    private void initListener() {
        mGridview.setOnItemClickListener(this);
    }


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mRelation = mTitles[position];
        mIntent = getIntent();
        if (position == mTitles.length - 1) {
            //点击了其它
            customNikeName();
        } else {

            if (mAfreed != null) {
                mSelectRelationPresenter.replyBandDeviceRequest(MyApplication.sToken, mDeviceId, mAppAccount, mRelation, "Y");
            } else {
                mIntent.putExtra("relation", mRelation);
                setResult(RELATION, mIntent);
                finishActivityByAnimation(this);
            }
        }
        LogUtils.d(TAG, "position " + position);
        LogUtils.d(TAG, "mTitles.length " + mTitles.length);


    }


    private void customNikeName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.please_enter_nickname));
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.lay_dialog_input, null);
        final EditText etDialogInput = (EditText) view.findViewById(R.id.etDialogInput);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRelation = etDialogInput.getText().toString().trim();
                if (TextUtils.isEmpty(mRelation)) {
                    printn(getString(R.string.please_enter_nickname));
                    return;
                }
                if (!mRelation.matches("(([\u4E00-\u9FA5]{1,3})|([a-zA-Z]{1,6}))")) {
                    printn("设置关系昵称中文长度不能超过3位字母不能超过6位");
                    return;
                }

                if (mAfreed != null) {
                    mSelectRelationPresenter.replyBandDeviceRequest(MyApplication.sToken, mDeviceId, mAppAccount, mRelation, "Y");
                } else {
                    if (mIntent != null) {
                        mIntent.putExtra("relation", mRelation);
                        setResult(RELATION, mIntent);
                        finishActivityByAnimation(SelectRelationActivity.this);
                    }
                }


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(true);
        builder.show();
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mSelectRelationPresenter.mResponseInfoModelCall != null) {
            mSelectRelationPresenter.mResponseInfoModelCall.cancel();
        }
    }


    /**
     * 被邀请人 同意的通知
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        if (!mAfreed.equals("Y")) {
            toActivity(DevicesHomeActivity.class, 10);
            finishActivityByAnimation(this);
        } else {
            printn(getString(R.string.add_success));
            setResult(104);
            finishActivityByAnimation(this);
        }

    }
}
