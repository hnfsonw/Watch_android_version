package com.loybin.baidumap.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.BabyDataPresenter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.HeadDialog;
import com.loybin.baidumap.ui.view.LastInputEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.ui.view.SelectDialog;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UIUtils;
import com.loybin.baidumap.util.UserUtil;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 添加IMEI码成功,设置watch关系信息
 */
public class BabyDataActivity extends BaseActivity {


    private static final String TAG = "BabyDataActivity";
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.rg_check)
    LinearLayout mRgCheck;

    @BindView(R.id.iv_head)
    public CircleImageView mIvHead;

    @BindView(R.id.cb_man_baby)
    ImageView mCbManBaby;

    @BindView(R.id.cb_female_baby)
    ImageView mCbFemaleBaby;

    @BindView(R.id.et_watch_number)
    LastInputEditText mEtWatchNumber;

    @BindView(R.id.et_baby_name)
    LastInputEditText mEtBabyName;

    @BindView(R.id.et_birthday)
    TextView mEtBirthday;

    @BindView(R.id.et_relation)
    TextView mEtRelation;

    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    @BindView(R.id.ll_chek_baby)
    LinearLayout mLlChekBaby;

    @BindView(R.id.ll_parents_relationship)
    LinearLayout mLlParents;

    @BindView(R.id.ll_man)
    View mLlMan;

    @BindView(R.id.ll_female)
    View mLlFemale;

    @BindView(R.id.iv_contacts)
    ImageView mIvContacts;

    private BabyDataPresenter mBabyDataPresenter;
    private SharedPreferences mGlobalvariable;
    private String mImei;
    private String mNickName;
    private int gender = 1;
    private String mImgUrl = "";
    private File tempFile;
    private Bitmap bitmap;
    public HeadDialog mHeadDialog;


    private SimpleDateFormat mSimpleDateFormat;
    private String mFormat;
    //这是新增宝贝的标识
    private String mNewBaby;
    private int mDeviceSize;
    private String mWatchNumber;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_baby_data;
    }


    @Override
    protected void init() {
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        mImei = getIntent().getStringExtra(STRING);
        mNewBaby = getIntent().getStringExtra(BABY);
        Log.d(TAG, "init: " + mImei);
        if (mNewBaby != null) {
            LogUtils.e(TAG, mNewBaby);
        }
        if (MyApplication.sListSize > 0) {
            mDeviceSize = DevicesHomeActivity.mListSize;
        }
        mNickName = mGlobalvariable.getString("nickName", "");

        mBabyDataPresenter = new BabyDataPresenter(this, this);
        Log.d(TAG, "onViewClicked: " + System.currentTimeMillis());

        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        initView();
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
        mTvTitle.setText(getString(R.string.add_message));
        mRgCheck.setVisibility(View.VISIBLE);
        mIvHead.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.iv_back, R.id.iv_head, R.id.btn_complete, R.id.ll_chek_baby,
            R.id.ll_parents_relationship, R.id.cb_female_baby, R.id.cb_man_baby,
            R.id.ll_man, R.id.ll_female, R.id.iv_contacts})
    public void onViewClicked(View view) {
        mWatchNumber = mEtWatchNumber.getText().toString().trim();
        String str = mEtBabyName.getText().toString().trim();
        String relation = mEtRelation.getText().toString().trim();
        String babyName = str.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "");
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_head:
                createDialog();
                break;

            case R.id.btn_complete:
                mBabyDataPresenter.acountBindImeiFirst(MyApplication.sAcountId, mImei, babyName, gender, mImgUrl,
                        mWatchNumber, mFormat, relation, MyApplication.sToken);
                break;

            case R.id.ll_chek_baby:
                UserUtil.hideSoftInput(this);
                toTimeChoose();
                break;

            case R.id.ll_parents_relationship:
                toActivity(RELATION, SelectRelationActivity.class);
                break;

            case R.id.ll_man:
                mCbManBaby.setImageResource(R.drawable.android_17);
                mCbFemaleBaby.setImageResource(R.drawable.android_19);
                gender = 1;
                break;

            case R.id.ll_female:
                mCbFemaleBaby.setImageResource(R.drawable.android_17);
                mCbManBaby.setImageResource(R.drawable.android_19);
                gender = 2;
                break;

            case R.id.cb_man_baby:
                mCbManBaby.setImageResource(R.drawable.android_17);
                mCbFemaleBaby.setImageResource(R.drawable.android_19);
                gender = 1;
                break;

            case R.id.cb_female_baby:
                mCbFemaleBaby.setImageResource(R.drawable.android_17);
                mCbManBaby.setImageResource(R.drawable.android_19);
                gender = 2;
                break;

            case R.id.iv_contacts:
                //通讯录
                mBabyDataPresenter.chekPermissions();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限申请成功
                mBabyDataPresenter.obtionContacts();
            } else {
                printn(getString(R.string.to_access_the_address_book_need_to_open_the_permissions));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void createDialog() {
        if (mHeadDialog == null) {
            mHeadDialog = new HeadDialog(this, this);
        }
        mHeadDialog.show();
    }


    /**
     * 选择日期
     */
    private void toTimeChoose() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mFormat = mSimpleDateFormat.format(date);
                mEtBirthday.setText(mFormat);
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setSubmitColor(Color.RED)//确定按钮文字颜色
                .setCancelColor(R.color.application_of_color)//取消按钮文字颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。

                .build();
        //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，
        // 避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: " + uri);
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (UIUtils.hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                if (tempFile.exists()) {
                    crop(Uri.fromFile(tempFile));
                } else {
//                    printn("取消拍照");
                }
            } else {
                printn("未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                if (data != null) {
                    if (data.getParcelableExtra("data") != null) {
                        bitmap = data.getParcelableExtra("data");
                        UIUtils.saveBitmapFile(bitmap);

                        mBabyDataPresenter.upload(UIUtils.getBitmapFile(), MyApplication.sToken);
//                        mIvHead.setImageBitmap(bitmap);
                    }
                    if (tempFile != null) {
                        tempFile.delete();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == RELATION) {
            if (data != null) {
                String relation = data.getStringExtra("relation");
                mEtRelation.setText(relation);

            }
        }

        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                try {
                    String[] contacts = mBabyDataPresenter.getPhoneContacts(uri);
                    if (contacts != null) {
                        mWatchNumber = contacts[1].replaceAll(" ", "").replaceAll("-", "").replaceAll("\\+86", "");
                        mEtWatchNumber.setText(mWatchNumber.toString().trim());
                        mEtWatchNumber.requestFocus();
                        mEtWatchNumber.setSelection(mWatchNumber.length());
                    }
                } catch (Exception e) {
                    printn(getString(R.string.did_not_get_to_the_phone_number));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪
     *
     * @param uri 图片
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    public void numbeiEmpty() {
        printn(getString(R.string.number_empty));
    }


    public void nameEmpty(String string) {
        printn(string);
    }


    public void birthdayNull() {
        printn(getString(R.string.birthday_null));
    }


    public void relationEmpty() {
        printn(getString(R.string.relation_empty));
    }


    public void succeed() {
        try {
            dismissLoading();
            Log.d(TAG, "mNewBaby: " + mNewBaby);
            if (mNewBaby != null) {
                LogUtils.e(TAG, "新增宝贝设置完成");
                mBabyDataPresenter.getAcountDeivceList(MyApplication.sAcountId, MyApplication.sToken);

            } else {
                LogUtils.e(TAG, "第一次新增宝贝");
                printn(getString(R.string.baby_succeed));
                MyApplication.sDeivceNumber = 0;
                mGlobalvariable.edit().putBoolean("login", true).apply();
                toActivity(DevicesHomeActivity.class);
                finishActivityByAnimation(this);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    public void error(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }

    public void onSuccess(String imgUrl) {
        dismissLoading();
        mImgUrl = imgUrl;
        mGlobalvariable.edit().putString("imgUrl", imgUrl).apply();
        Glide.with(this).load(imgUrl).into(mIvHead);
    }


    public void numberError() {
        printn(getString(R.string.phoneError));

    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        try {
            if (mBabyDataPresenter.mAcountBindImeiFirst != null) {
                mBabyDataPresenter.mAcountBindImeiFirst.cancel();
            }
            if (mBabyDataPresenter.mUpload != null) {
                mBabyDataPresenter.mUpload.cancel();

            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }


    /**
     * 弹出Dialog 选择设备
     */
    public void selectDialogShow(List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList) {
        dismissLoading();
        SelectDialog selectDialog = new SelectDialog(this, this, deviceList);
        selectDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK) {
                    LogUtils.d(TAG, "~~返回");
                    return true;
                }
                return false;
            }
        });
        selectDialog.show();

    }
}
