package com.loybin.baidumap.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.AddLeisurePresenter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/07/19 下午2:59
 * 描   述: 添加联系人视图
 */
public class AddLeisureActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "AddLeisureActivity";
    private static final int PICK_CONTACT = 102;
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_head)
    CircleImageView mIvHead;

    @BindView(R.id.tv_jagx)
    TextView mTvJagx;

    @BindView(R.id.et_relation)
    TextView mEtRelation;

    @BindView(R.id.ll_parents_relationship)
    LinearLayout mLlParentsRelationship;

    @BindView(R.id.et_phone_number)
    LastInputEditText mEtPhoneNumber;

    @BindView(R.id.iv_contacts)
    ImageView mIvContacts;

    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    @BindView(R.id.tv_attribute)
    TextView mTvAttribute;

    @BindView(R.id.ll_attribute)
    LinearLayout mLlAttribute;

    @BindView(R.id.ll_delete)
    LinearLayout mLlDelete;

    @BindView(R.id.et_leisure_shortPhone)
    LastInputEditText mEtLeisureShortPhone;

    @BindView(R.id.cb_add_watch)
    CheckBox mCbAddWatch;

    private String mPhoneNumber;
    private AddLeisurePresenter mAddLeisurePresenter;
    private String mRelation;
    private String mAdd;
    private String username;
    private String usernumber;
    private String mShortPhone;
    private String mBandRequest = "";
    private Intent mIntent;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_leisure;
    }


    @Override
    protected void init() {
        if (mIntent == null) {
            mIntent = new Intent();
        }
        mAdd = getIntent().getStringExtra(STRING);
        mAddLeisurePresenter = new AddLeisurePresenter(this, this);
        initView();
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.add_leisure));
        if (mAdd != null) {
            mLlAttribute.setVisibility(View.GONE);
            mLlDelete.setVisibility(View.GONE);
        }

        mCbAddWatch.setOnCheckedChangeListener(this);
    }


    @Override
    protected void dismissNewok() {
        if (mAddLeisurePresenter.mCall != null) {
            mAddLeisurePresenter.mCall.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.ll_parents_relationship, R.id.iv_contacts,
            R.id.btn_complete, R.id.ll_attribute, R.id.ll_delete})
    public void onViewClicked(View view) {
        mPhoneNumber = mEtPhoneNumber.getText().toString().trim();
        mShortPhone = mEtLeisureShortPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.ll_parents_relationship:
                toActivity(100, SelectRelationActivity.class);
                break;

            case R.id.iv_contacts:
                chekPermissions();

                break;

            case R.id.btn_complete:
                LogUtils.d(TAG, "mPhoneNumber " + mPhoneNumber);
                mAddLeisurePresenter.binding(mPhoneNumber, mRelation, MyApplication.sToken,
                        MyApplication.sDeviceId, mShortPhone, mBandRequest, DevicesHomeActivity.sPhone);
                break;

            case R.id.ll_attribute:
                break;

            case R.id.ll_delete:
                break;

            default:
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


    /**
     * 验证通讯录权限
     */
    private void chekPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        } else {//已授权
            obtionContacts();
        }
    }


    private void obtionContacts() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, 0);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限申请成功
                obtionContacts();
            } else {
                printn(getString(R.string.to_access_the_address_book_need_to_open_the_permissions));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RELATION) {
            if (data != null) {
                mIvHead.setVisibility(View.VISIBLE);
                mIvHead.setImageResource(mIcons[mIcons.length - 1]);
                mRelation = data.getStringExtra("relation");
                LogUtils.d(TAG, mRelation);
                mEtRelation.setText(mRelation);
                for (int i = 0; i < mTitles.length; i++) {
                    if (mRelation.equals(mTitles[i])) {
                        mIvHead.setImageResource(mIcons[i]);
                    }
                }
            }
        }

        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                Log.e(TAG, "uri: " + uri);
                try {
                    String[] contacts = getPhoneContacts(uri);
                    if (contacts != null) {
                        Log.e(TAG, "contacts: " + contacts[1]);
                        mPhoneNumber = contacts[1].replaceAll(" ", "").replaceAll("-", "").replaceAll("\\+86", "");
                        mEtPhoneNumber.setText(mPhoneNumber.toString().trim());
                        mEtPhoneNumber.requestFocus();
                        mEtPhoneNumber.setSelection(mPhoneNumber.length());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "" + e);
                    printn(getString(R.string.did_not_get_to_the_phone_number));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String[] getPhoneContacts(Uri uri) {

        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone.close();
            }
            cursor.close();
        } else {
            return null;
        }
        return contact;
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


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mBandRequest = "Y";
        } else {
            mBandRequest = "";
        }
        LogUtils.d(TAG, "isChecked = " + isChecked);
    }


    /**
     * 添加成功
     */
    public void onSuccess() {
        dismissLoading();
        if (mBandRequest.equals("Y")) {
            mIntent.putExtra("phone", mPhoneNumber);
            setResult(100, mIntent);
        }


        printn(mContext.getString(R.string.add_success));
        finishActivityByAnimation(this);
    }
}
