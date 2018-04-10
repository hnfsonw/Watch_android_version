package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.LeisurePresenter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.RemoveDialog;
import com.loybin.baidumap.ui.view.ShareDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/20 下午3:31
 * 描   述: 手表通讯录详情
 * <p>
 * 微信只能回调指定包下的指定Activity!!!
 */
public class WXEntryActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "WXEntryActivity";
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.iv_head)
    CircleImageView mIvHead;

    @BindView(R.id.et_leisure_name)
    TextView mEtLeisureName;

    @BindView(R.id.et_phone_number)
    EditText mEtPhoneNumber;

    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    @BindView(R.id.ll_leisure)
    LinearLayout mLlLeisure;

    @BindView(R.id.tv_attribute)
    TextView mTvAttribute;

    @BindView(R.id.ll_attribute)
    LinearLayout mLlAttribute;

    @BindView(R.id.ll_delete)
    LinearLayout mLlDelete;

    @BindView(R.id.iv_iv)
    ImageView mIvIv;

    @BindView(R.id.et_leisure_shortPhone)
    EditText mEtLeisureShortPhone;

    @BindView(R.id.tv_edit_shortPhone)
    TextView mTvEditShortPhone;

    private String mRelation;
    public String mAcountName;
    private LeisurePresenter mLeisurePresenter;


    private String mAcountId;
    private int mGender;
    private SharedPreferences mGlobalvariable;
    private String mToken;
    private String mPhone;
    private String mIsAdmin;
    private String mIsBind;
    private String mAcountType;
    private boolean misLeisure;
    private String mDeviceId;
    private boolean mIsBindRegis;
    private RemoveDialog mRemoveDialog;
    private String mNewRelation;
    private boolean mIsRegister;
    private String mShortPhone;
    private Intent mIntent;
    private String mAppAccount;
    private ShareDialog mDialog;
    private String mAdminRelation;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_leisure;
    }


    @Override
    protected void init() {

        mLeisurePresenter = new LeisurePresenter(this, this);

        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mToken = mGlobalvariable.getString("token", "");

        mAppAccount = mGlobalvariable.getString("appAccount", "");
        mRelation = getIntent().getStringExtra("relation");
        mAcountName = getIntent().getStringExtra("acountName");
        mAcountId = getIntent().getStringExtra("acountId");
        mDeviceId = getIntent().getStringExtra("deviceId");
        mGender = getIntent().getIntExtra("gender", 0);
        mPhone = getIntent().getStringExtra("phone");
        mIsAdmin = getIntent().getStringExtra("isAdmin");
        mIsBind = getIntent().getStringExtra("isBind");
        mAcountType = getIntent().getStringExtra("acountType");
        mShortPhone = getIntent().getStringExtra("shortPhone");
        mAdminRelation = getIntent().getStringExtra("mAdminRelation");


        LogUtils.e(TAG, "relation: " + mRelation);
        LogUtils.e(TAG, "mAdminRelation: " + mAdminRelation);
        LogUtils.e(TAG, "sPhone: " + mPhone);
        LogUtils.e(TAG, "mIsAdmin: " + mIsAdmin);
        LogUtils.e(TAG, "mIsBind: " + mIsBind);
        LogUtils.d(TAG, "mDeviceId: " + mDeviceId);
        LogUtils.e(TAG, "mAcountType: " + mAcountType);
        LogUtils.e(TAG, "mAcountId: " + mAcountId);
        initView();
        initListener();
    }


    private void initListener() {
        mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvRight.setVisibility(View.VISIBLE);
                mTvRight.setText(getString(R.string.save));
            }
        });
    }


    private void initView() {
        mTvTitle.setText(R.string.leisure);
        mEtLeisureName.setText(mRelation);
        mEtPhoneNumber.setText(mPhone);
        mEtLeisureShortPhone.setText(mShortPhone);
        mIvHead.setVisibility(View.VISIBLE);
        mIvHead.setImageResource(mIcons[mIcons.length - 1]);
        //管理员显示
        if (mIsAdmin != null) {
            mIsRegister = true;
            mEtLeisureName.setEnabled(false);
            mEtPhoneNumber.setEnabled(false);
            mEtLeisureShortPhone.setEnabled(false);
            mEtLeisureName.setTextColor(Color.GRAY);
            mEtPhoneNumber.setTextColor(Color.GRAY);
            mEtLeisureShortPhone.setTextColor(Color.GRAY);
            mLlDelete.setVisibility(View.GONE);
            mLlAttribute.setVisibility(View.GONE);
        }

        //不是管理员
        if (mIsAdmin == null && mAcountType != null) {
            //不是管理员并且已注册
            if (mAcountType.equals("1")) {
                if (mIsBind != null && mIsBind.equals("1")) {
                    //不是管理员并且已注册已绑定设备
                    mIsBindRegis = true;
                    mIsRegister = true;
                    mEtLeisureName.setEnabled(false);
                    mEtPhoneNumber.setEnabled(false);
                    mEtLeisureShortPhone.setEnabled(false);
                    mEtLeisureName.setTextColor(Color.GRAY);
                    mEtPhoneNumber.setTextColor(Color.GRAY);
                    mEtLeisureShortPhone.setTextColor(Color.GRAY);
                    mTvAttribute.setText(getString(R.string.binding_watch));

                } else if (mIsBind != null && mIsBind.equals("0")) {
                    //不是管理员并且已注册未绑定设备
                    mEtLeisureName.setEnabled(false);
                    mEtPhoneNumber.setEnabled(false);
                    mIsRegister = true;
                    mEtLeisureShortPhone.setEnabled(false);
                    mEtLeisureName.setTextColor(Color.GRAY);
                    mEtPhoneNumber.setTextColor(Color.GRAY);
                    mEtLeisureShortPhone.setTextColor(Color.GRAY);
                    mTvAttribute.setText(getString(R.string.invite_binding_watches));
                    mTvAttribute.setTextColor(Color.RED);
                    mTvAttribute.setOnClickListener(this);

                }
            } else {
                //未注册
                mIvIv.setVisibility(View.VISIBLE);
                mLlAttribute.setVisibility(View.VISIBLE);
                mLlDelete.setVisibility(View.VISIBLE);
                mTvAttribute.setText(getString(R.string.invite_binding_watches));
                mTvAttribute.setTextColor(Color.RED);
                mTvAttribute.setOnClickListener(this);

                mEtLeisureName.setEnabled(true);
                mEtLeisureShortPhone.setEnabled(false);
                mEtLeisureShortPhone.setTextColor(Color.GRAY);
                misLeisure = true;
                mIsRegister = false;
                mEtPhoneNumber.requestFocus();
                if (mPhone != null) {
                    mEtPhoneNumber.setSelection(mPhone.length());
                }
            }
        }


        for (int i = 0; i < mTitles.length; i++) {
            if (mRelation.equals(mTitles[i])) {
                mIvHead.setImageResource(mIcons[i]);
            }
        }
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back, R.id.btn_complete, R.id.tv_right, R.id.ll_attribute, R.id.ll_delete,
            R.id.ll_leisure, R.id.tv_edit_shortPhone})
    public void onViewClicked(View view) {
        String newPhone = mEtPhoneNumber.getText().toString().trim();
        String shortPhone = mEtLeisureShortPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_complete:
                mLeisurePresenter.updateAcountInfo(mToken, mAcountId, mGender, newPhone, MyApplication.sDeviceId);
                break;

            case R.id.tv_right:
                LogUtils.d(TAG, "保存");
                mLeisurePresenter.editDeviceContracts(MyApplication.sToken, mDeviceId, mPhone, newPhone, mRelation
                        , mAcountId);
                break;

            case R.id.ll_attribute:
                break;

            case R.id.ll_delete:
                //删除联系人
                deleteLeisure();
                break;

            case R.id.ll_leisure:
                if (misLeisure) {
                    toActivity(SelectRelationActivity.class);
                } else {
                    printn(getString(R.string.do_not_modify_the_registered_account));
                }
                break;

            case R.id.tv_edit_shortPhone:
                if (mIntent == null) {
                    mIntent = new Intent(this, ShortPhoneActivity.class);
                }
                mIntent.putExtra("newPhone", newPhone);
                mIntent.putExtra("shortPhone", shortPhone);
                mIntent.putExtra("acountId", mAcountId);
                mIntent.putExtra("mIsRegister", mIsRegister);
                mIntent.putExtra("deviceId", mDeviceId);
                mIntent.putExtra("relation", mRelation);
                startActivityForResult(mIntent, 100);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }


    /**
     * 删除联系人
     */
    private void deleteLeisure() {
        if (mRemoveDialog == null) {
            mRemoveDialog = new RemoveDialog(this, WXEntryActivity.this);
        }
        mRemoveDialog.show();
        //已绑定
        if (mIsBindRegis) {
            mRemoveDialog.initTitle(getString(R.string.delete_watch_leisure));
        } else {
            //已注册未绑定
            mRemoveDialog.initTitle(getString(R.string.delete_no_binding_watch));
        }
    }


    /**
     * 删除确认的回调
     */
    @Override
    public void cancel() {
        LogUtils.d(TAG, "删除确认");
        mLeisurePresenter.delDeviceContracts(MyApplication.sToken, mAcountId, mDeviceId, mPhone);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RELATION) {
            if (data != null) {
                boolean custom = true;
                mRelation = data.getStringExtra("relation");
                mTvRight.setVisibility(View.VISIBLE);
                mTvRight.setText(getString(R.string.save));
                LogUtils.d(TAG, mRelation);
                mEtLeisureName.setText(mRelation);
                for (int i = 0; i < mTitles.length; i++) {
                    if (mRelation.equals(mTitles[i])) {
                        mIvHead.setImageResource(mIcons[i]);
                        custom = false;
                        break;
                    }
                }
                if (custom == true) {
                    int i = mTitles.length - 1;
                    mIvHead.setImageResource(mIcons[i]);
                }
            }
        }

        if (requestCode == 100) {
            if (data != null) {
                String shortPhone = data.getStringExtra("shortPhone");
                mEtLeisureShortPhone.setText(shortPhone);
                LogUtils.e(TAG, "保存成功");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void noModification() {
        printn(getString(R.string.update_is_successful));
    }


    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    public void onSuccess() {
        dismissLoading();
        printn(getString(R.string.update_is_successful));
        finishActivityByAnimation(this);

    }

    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mLeisurePresenter.mUpdateAcountInfo != null) {
            mLeisurePresenter.mUpdateAcountInfo.cancel();
        }
    }


    public void phoneError() {
        printn(getString(R.string.phoneError));
    }


    /**
     * 点击邀请绑定手表
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mIsBind != null && mIsBind.equals("0")) {
            //已注册 绑定
            LogUtils.d(TAG, "点击邀请绑定手表");
            mLeisurePresenter.bandDeviceRequest(MyApplication.sToken, mPhone, mDeviceId, mAppAccount, 1, true);
        }

        if (mAcountType != null && mAcountType.equals("0")) {
            //未注册
            mLeisurePresenter.bandDeviceRequest(MyApplication.sToken, mPhone, mDeviceId, mAppAccount, 0, false);
            LogUtils.e(TAG, mPhone + "未注册 弹出邀请");
            if (mDialog == null) {
                mDialog = new ShareDialog(this, this);
            }
            mDialog.show();
            mDialog.setPhone(mPhone, mAdminRelation);
        }

    }


    /**
     * 邀请发送成功
     */
    public void bandSuccess() {
        dismissLoading();
        if (mIsBind != null && mIsBind.equals("0")) {
            printn(getString(R.string.send_success));
            finishActivityByAnimation(this);
        }
    }


}
