package com.loybin.baidumap.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/29 下午6:36
 * 描   述: 选择星期view
 */
public class SelectWeekActivity extends BaseActivity {
    private static final String TAG = "SelectWeekActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.ll_week_one)
    LinearLayout mLlWeekOne;

    @BindView(R.id.cb_one)
    ImageView mCbOne;

    @BindView(R.id.cb_two)
    ImageView mCbTwo;

    @BindView(R.id.ll_week_two)
    LinearLayout mLlWeekTwo;

    @BindView(R.id.cb_three)
    ImageView mCbThree;

    @BindView(R.id.ll_week_three)
    LinearLayout mLlWeekThree;

    @BindView(R.id.cb_four)
    ImageView mCbFour;

    @BindView(R.id.ll_week_four)
    LinearLayout mLlWeekFour;

    @BindView(R.id.cb_five)
    ImageView mCbFive;

    @BindView(R.id.ll_week_five)
    LinearLayout mLlWeekFive;

    @BindView(R.id.cb_six)
    ImageView mCbSix;

    @BindView(R.id.ll_week_six)
    LinearLayout mLlWeekSix;

    @BindView(R.id.cb_seven)
    ImageView mCbSeven;

    @BindView(R.id.ll_week_seven)
    LinearLayout mLlWeekSeven;

    @BindView(R.id.btn_determine)
    Button mBtnDetermine;

    private boolean mIsCbOne = true;
    private boolean mIsCbTwo = true;
    private boolean mIsCbThree = true;
    private boolean mIsCbFour = true;
    private boolean mIsCbFive = true;
    private boolean mIsCbSeven = true;
    private boolean mIsCbSix = true;
    private String mOneWeek;
    private String mTwoWeek;
    private String mThreeWeek;
    private String mFourWeek;
    private String mFiveWeek;
    private String mSevenWeek;
    private String mSixWeek;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_select_week;
    }

    @Override
    protected void init() {
        mOneWeek = getIntent().getStringExtra("oneWeek");
        mTwoWeek = getIntent().getStringExtra("twoWeek");
        mThreeWeek = getIntent().getStringExtra("threeWeek");
        mFourWeek = getIntent().getStringExtra("fourWeek");
        mFiveWeek = getIntent().getStringExtra("fiveWeek");
        mSixWeek = getIntent().getStringExtra("sixWeek");
        mSevenWeek = getIntent().getStringExtra("sevenWeek");

        LogUtils.e(TAG, "mSevenWeek ~" + mSevenWeek);
        initView();
        initListener();
    }


    private void initListener() {

    }


    private void initView() {
        mTvTitle.setText(getString(R.string.week));
        if (mOneWeek != null) {
            mCbOne.setVisibility(View.VISIBLE);
            mIsCbOne = false;
        }
        if (mTwoWeek != null) {
            mCbTwo.setVisibility(View.VISIBLE);
            mIsCbTwo = false;
        }
        if (mThreeWeek != null) {
            mCbThree.setVisibility(View.VISIBLE);
            mIsCbThree = false;
        }
        if (mFourWeek != null) {
            mCbFour.setVisibility(View.VISIBLE);
            mIsCbFour = false;
        }
        if (mFiveWeek != null) {
            mCbFive.setVisibility(View.VISIBLE);
            mIsCbFive = false;
        }
        if (mSevenWeek != null) {
            mCbSeven.setVisibility(View.VISIBLE);
            mIsCbSeven = false;
        }
        if (mSixWeek != null) {
            mCbSix.setVisibility(View.VISIBLE);
            mIsCbSix = false;
        }
    }


    @Override
    protected void dismissNewok() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            determine();
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.iv_back, R.id.ll_week_one, R.id.ll_week_two, R.id.ll_week_three, R.id.ll_week_four
            , R.id.ll_week_five, R.id.ll_week_seven, R.id.ll_week_six, R.id.btn_determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                determine();
                break;

            case R.id.ll_week_one:
                LogUtils.e(TAG, "~~~~ " + mIsCbOne);
                if (mIsCbOne) {
                    mCbOne.setVisibility(View.VISIBLE);
                } else {
                    mCbOne.setVisibility(View.GONE);
                }
                mIsCbOne = !mIsCbOne;
                break;

            case R.id.ll_week_two:
                if (mIsCbTwo) {
                    mCbTwo.setVisibility(View.VISIBLE);
                } else {
                    mCbTwo.setVisibility(View.GONE);
                }
                mIsCbTwo = !mIsCbTwo;
                break;

            case R.id.ll_week_three:
                if (mIsCbThree) {
                    mCbThree.setVisibility(View.VISIBLE);
                } else {
                    mCbThree.setVisibility(View.GONE);
                }
                mIsCbThree = !mIsCbThree;
                break;

            case R.id.ll_week_four:
                if (mIsCbFour) {
                    mCbFour.setVisibility(View.VISIBLE);
                } else {
                    mCbFour.setVisibility(View.GONE);
                }
                mIsCbFour = !mIsCbFour;
                break;

            case R.id.ll_week_five:
                if (mIsCbFive) {
                    mCbFive.setVisibility(View.VISIBLE);
                } else {
                    mCbFive.setVisibility(View.GONE);
                }
                mIsCbFive = !mIsCbFive;
                break;


            case R.id.ll_week_six:
                if (mIsCbSix) {
                    mCbSix.setVisibility(View.VISIBLE);
                } else {
                    mCbSix.setVisibility(View.GONE);
                }
                mIsCbSix = !mIsCbSix;
                break;

            case R.id.ll_week_seven:
                if (mIsCbSeven) {
                    mCbSeven.setVisibility(View.VISIBLE);
                } else {
                    mCbSeven.setVisibility(View.GONE);
                }
                mIsCbSeven = !mIsCbSeven;
                break;


            case R.id.btn_determine:
                determine();
                break;
        }
    }


    private void determine() {
        Intent intent = getIntent();
        if (!mIsCbOne) {
            intent.putExtra("weekOne", "1");
        }

        if (!mIsCbTwo) {
            intent.putExtra("weekTwo", "2");
        }

        if (!mIsCbThree) {
            intent.putExtra("weekThree", "3");
        }

        if (!mIsCbFour) {
            intent.putExtra("weekFout", "4");
        }

        LogUtils.e(TAG, "mIsCbFive " + mIsCbFive);
        if (!mIsCbFive) {
            intent.putExtra("weekFive", "5");
        }

        if (!mIsCbSix) {
            intent.putExtra("weekSix", "6");
        }

        if (!mIsCbSeven) {
            intent.putExtra("weekSeven", "7");
        }
        setResult(DECI_CODE, intent);
        finishActivityByAnimation(this);
    }


}
