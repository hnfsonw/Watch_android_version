package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.BindingUserPresenter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/18 下午5:41
 * 描   述: 绑定用户
 */
public class BindingUserActivity extends BaseActivity {


    private static final String TAG = "BindingUserActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.ll_parents_relationship)
    LinearLayout mLlParentsRelationship;

    @BindView(R.id.et_phone_number)
    LastInputEditText mEtPhoneNumber;

    @BindView(R.id.et_relation)
    TextView mEtRelation;

    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    @BindView(R.id.iv_head)
    CircleImageView mIvHead;

    private BindingUserPresenter mBindingUserPresenter;
    private SharedPreferences mGlobalvariable;
    private String mToken;
    private int mDeviceId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_binding_user;
    }


    @Override
    protected void init() {
        mBindingUserPresenter = new BindingUserPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mToken = mGlobalvariable.getString("token", "");
        mDeviceId = mGlobalvariable.getInt("deviceId", 0);

        Log.d(TAG, "mToken: " + mToken);
        Log.d(TAG, "mDeviceId: " + mDeviceId);

        initView();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.binding_user));
    }


    @OnClick({R.id.iv_back, R.id.ll_parents_relationship, R.id.btn_complete})
    public void onViewClicked(View view) {
        String phoneNumber = mEtPhoneNumber.getText().toString().trim();
        String relation = mEtRelation.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.ll_parents_relationship:
                toActivity(RELATION, SelectRelationActivity.class);
                break;

            case R.id.btn_complete:
                mBindingUserPresenter.binding(phoneNumber, relation, mToken, mDeviceId);
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RELATION) {
            if (data != null) {
                String relation = data.getStringExtra("relation");
                mEtRelation.setText(relation);
                for (int i = 0; i < mTitles.length; i++) {
                    if (relation.equals(mTitles[i])) {
                        mIvHead.setImageResource(mIcons[i]);
                    }
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void relationEmpty() {
        printn(getString(R.string.please_select_a_parent_relationship));
    }


    public void phoneEmpty() {
        printn(getString(R.string.please_enter_the_phone_number));
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    /**
     * 绑定成功的回掉
     *
     * @param data
     */
    public void onSuccess(ResponseInfoModel data) {
        dismissLoading();
        printn(getString(R.string.binding_success));
        finishActivityByAnimation(this);
    }


    /**
     * 绑定失败的回掉
     *
     * @param s
     */
    public void onError(ResponseInfoModel s) {
        dismissLoading();
        printn(s.getResultMsg());
    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        try {
            if (mBindingUserPresenter.mAcountBindImei != null) {
                mBindingUserPresenter.mAcountBindImei.cancel();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }
}
