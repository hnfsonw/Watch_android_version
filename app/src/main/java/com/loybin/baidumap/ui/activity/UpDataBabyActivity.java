package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.presenter.BabyDataPresenter;
import com.loybin.baidumap.presenter.UpDataBabyPresenter;
import com.loybin.baidumap.ui.view.CircleImageView;
import com.loybin.baidumap.ui.view.HeadDialog;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UIUtils;
import com.loybin.baidumap.util.UserUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/24 下午3:44
 * 描   述: 更新宝贝的资料
 */
public class UpDataBabyActivity extends BaseActivity {


    private static final String TAG = "UpDataBabyActivity";
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.iv_head)
    CircleImageView mIvHead;

    @BindView(R.id.cb_man_baby)
    ImageView mCbManBaby;

    @BindView(R.id.cb_female_baby)
    ImageView mCbFemaleBaby;

    @BindView(R.id.ll_female)
    View mLlFemale;

    @BindView(R.id.ll_man)
    View mLlMan;

    @BindView(R.id.et_watch_number)
    LastInputEditText mEtWatchNumber;

    @BindView(R.id.et_baby_name)
    LastInputEditText mEtBabyName;

    @BindView(R.id.tv_baby_sr)
    TextView mTvBabySr;

    @BindView(R.id.et_birthday)
    TextView mEtBirthday;

    @BindView(R.id.ll_chek_baby)
    LinearLayout mLlChekBaby;

    @BindView(R.id.btn_complete)
    Button mBtnComplete;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.rg_check)
    LinearLayout mLlRgCheck;

    @BindView(R.id.et_baby_shortPhone)
    LastInputEditText mEtBabyShortPhone;

    private BabyDataPresenter mBabyDataPresenter;
    private SharedPreferences mGlobalvariable;
    private int mAcountId;
    public String mNickName;
    private String mToken;
    public String mImgUrl = "";
    private File tempFile;
    private Bitmap bitmap;
    public HeadDialog mHeadDialog;

    private SimpleDateFormat mSimpleDateFormat;
    public String mFormat;
    private UpDataBabyPresenter mUpDataBabyPresenter;
    public String mPhone;
    private String mBirthday;
    public String mSubstring;
    private int mIsAdmin;
    private long mWatchAcountId;
    private String mWatchNumber;
    private String mBabyName;
    private boolean isShowIcon = false;
    public int mGender;
    private String mShortPhone;//短号


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_up_data_baby;
    }


    @Override
    protected void init() {
        mUpDataBabyPresenter = new UpDataBabyPresenter(this, this);
        mGlobalvariable = getSharedPreferences("globalvariable", 0);
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        mImgUrl = getIntent().getStringExtra("imgUrl");
        mGender = getIntent().getIntExtra("gender", 1);
        mPhone = getIntent().getStringExtra("phone");
        mNickName = getIntent().getStringExtra("nickName");
        mFormat = getIntent().getStringExtra("birthday");
        mSubstring = mFormat.substring(0, 11);
        mIsAdmin = getIntent().getIntExtra("isAdmin", 0);
        mWatchAcountId = getIntent().getLongExtra("watchAcountId", 0);
        mShortPhone = getIntent().getStringExtra("shortPhone");
        Log.d(TAG, "mImgUrl..: " + mImgUrl);
        Log.d(TAG, "gender..: " + mGender);
        Log.d(TAG, "sPhone..: " + mPhone);
        Log.d(TAG, "mFormat..: " + mFormat);
        Log.d(TAG, "mNickName..: " + mNickName);
        Log.d(TAG, "mSubstring..: " + mSubstring);
        Log.d(TAG, "mIsAdmin..: " + mIsAdmin);
        LogUtils.e(TAG, "加载mShortPhone:" + mShortPhone);
        initView();
        initData();


    }


    private void initView() {
        mTvTitle.setText(getString(R.string.baby_data));
        mIvHead.setVisibility(View.VISIBLE);
        mLlRgCheck.setVisibility(View.VISIBLE);
        if (mImgUrl != null) {
            Glide.with(this).load(mImgUrl).into(mIvHead);
        }
        if (mGender == 1) {
            mCbManBaby.setImageResource(R.drawable.android_17);
            mCbFemaleBaby.setImageResource(R.drawable.android_19);
        } else {
            mCbManBaby.setImageResource(R.drawable.android_19);
            mCbFemaleBaby.setImageResource(R.drawable.android_17);
        }
        mEtWatchNumber.setText(mPhone);
        mEtBabyName.setText(mNickName);
        mEtBirthday.setText(mSubstring);
        if (mShortPhone != null) {
            mEtBabyShortPhone.setText(mShortPhone);
        }
    }


    private void initData() {
        if (mIsAdmin != 1) {
            mBtnComplete.setVisibility(View.GONE);
            mEtWatchNumber.setFocusable(false);
            mEtBabyName.setFocusable(false);
            mEtBabyShortPhone.setFocusable(false);
            mLlChekBaby.setFocusable(false);
            mIvHead.setFocusable(false);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(getString(R.string.editor));
            mEtWatchNumber.setEnabled(false);
            mEtBabyName.setEnabled(false);
            mLlChekBaby.setEnabled(false);
            mIvHead.setEnabled(false);
            mCbManBaby.setEnabled(false);
            mCbFemaleBaby.setEnabled(false);
            mLlMan.setEnabled(false);
            mLlFemale.setEnabled(false);
            mEtBabyShortPhone.setEnabled(false);
        }

    }


    @OnClick({R.id.iv_back, R.id.iv_head, R.id.ll_chek_baby, R.id.btn_complete, R.id.tv_right
            , R.id.ll_man, R.id.ll_female})
    public void onViewClicked(View view) {
        mWatchNumber = mEtWatchNumber.getText().toString().trim();
        mBabyName = mEtBabyName.getText().toString().trim();
        mPhone = mWatchNumber;
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.iv_head:
                if (mIsAdmin == 1) {
                    createDialog();
                }
                break;

            case R.id.ll_chek_baby:
                if (mIsAdmin == 1) {
                    UserUtil.hideSoftInput(this);
                    toTimeChoose();
                }
                break;

            case R.id.btn_complete:
                try {
                    Log.d(TAG, "mWatchAcountId: " + mWatchAcountId);
                    Log.d(TAG, "mBabyName: " + mBabyName);
                    Log.d(TAG, "gender: " + mGender);
                    Log.d(TAG, "mImgUrl: " + mImgUrl);
                    Log.d(TAG, "mWatchNumber: " + mWatchNumber);
                    Log.d(TAG, "mFormat: " + mFormat);
                    mShortPhone = mEtBabyShortPhone.getText().toString().trim();
                    mUpDataBabyPresenter.acountBindImeiFirst(MyApplication.sDeviceId, mWatchAcountId,
                            mBabyName, mGender, mImgUrl, mWatchNumber, mFormat,
                            MyApplication.sToken, mShortPhone);
                } catch (Exception e) {
                    LogUtils.e(TAG, "异常" + e.getMessage());
                }

                break;

            case R.id.tv_right:
                mBtnComplete.setVisibility(View.VISIBLE);
                mEtWatchNumber.setEnabled(true);
                mEtBabyName.setEnabled(true);
                mEtBabyShortPhone.setEnabled(true);
                mLlChekBaby.setEnabled(true);
                mIvHead.setEnabled(true);
                mCbManBaby.setEnabled(true);
                mCbFemaleBaby.setEnabled(true);
                mLlMan.setEnabled(true);
                mLlFemale.setEnabled(true);
                mEtWatchNumber.requestFocus();
                if (mPhone != null) {
                    mEtWatchNumber.setSelection(mPhone.length());
                }

                UserUtil.showSoftInput(this);

                break;

            case R.id.ll_man:
                if (mIsAdmin != 1) {
                    return;
                }
                mCbManBaby.setImageResource(R.drawable.android_17);
                mCbFemaleBaby.setImageResource(R.drawable.android_19);
                mGender = 1;
                Log.d(TAG, "onCheckedChanged: " + mGender);
                break;

            case R.id.ll_female:
                if (mIsAdmin != 1) {
                    return;
                }
                mCbManBaby.setImageResource(R.drawable.android_19);
                mCbFemaleBaby.setImageResource(R.drawable.android_17);
                mGender = 2;
                Log.d(TAG, "onCheckedChanged: " + mGender);
                break;

            default:
                break;
        }
    }

    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return true;
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
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(1970, 0, 1);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        endDate.set(year, month, day);
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mFormat = mSimpleDateFormat.format(date);
                LogUtils.e(TAG, "选择日期" + mFormat);
                mEtBirthday.setText(mFormat);
            }
        }).setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setSubmitColor(R.color.application_of_color)//确定按钮文字颜色
                .setCancelColor(R.color.application_of_color)//取消按钮文字颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setRange(1970,Calendar.getInstance().get(Calendar.YEAR))
                .setDate(endDate)
                .setRangDate(startDate, endDate)
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
                        mUpDataBabyPresenter.upload(UIUtils.getBitmapFile(), MyApplication.sToken);
//                        mIvHead.setImageBitmap(bitmap);

                    }
                    if (tempFile != null) {
                        tempFile.delete();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


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


    public void noUpdata() {
        printn(getString(R.string.please_amend_after_the_update));
    }


    public void numberError() {
        printn(getString(R.string.phoneError));

    }

    public void succeed() {
        dismissLoading();
        if (isShowIcon) {
            isShowIcon = false;
            Glide.with(this).load(mImgUrl).into(mIvHead);
            MyApplication.sInUpdata = true;
//            SharedPreferencesUtils.setParam(this,"sInUpdata",true);
        } else {
            printn(getString(R.string.update_is_successful));
            MyApplication.sInUpdata = true;
//            SharedPreferencesUtils.setParam(this,"sInUpdata",true);
        }

    }

    /**
     * 图片上传失败的通知
     *
     * @param resultMsg
     */
    public void error(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }

    /**
     * 图片上传成功的通知
     *
     * @param imgUrl
     */
    public void onSuccess(String imgUrl) {
        dismissLoading();
        isShowIcon = true;
        mUpDataBabyPresenter.acountBindImeiFirst(MyApplication.sDeviceId, mWatchAcountId, mBabyName,
                mGender, imgUrl, mWatchNumber, mFormat, MyApplication.sToken, mShortPhone);
        mImgUrl = imgUrl;

    }


    /**
     * 加载网络,清楚缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mUpDataBabyPresenter.mUpdateAcountInfo != null) {
            mUpDataBabyPresenter.mUpdateAcountInfo.cancel();
        }
        if (mUpDataBabyPresenter.mUpload != null) {
            mUpDataBabyPresenter.mUpload.cancel();
        }
    }


}
