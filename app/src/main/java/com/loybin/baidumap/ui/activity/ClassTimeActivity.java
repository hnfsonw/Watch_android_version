package com.loybin.baidumap.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BaseActivity;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.presenter.ClassTimePresenter;
import com.loybin.baidumap.ui.view.AfternoonLayout;
import com.loybin.baidumap.ui.view.EveningLayout;
import com.loybin.baidumap.ui.view.LastInputEditText;
import com.loybin.baidumap.ui.view.MorningLayout;
import com.loybin.baidumap.util.KeyboardUtil;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/29 下午2:49
 * 描   述: 添加上课时间的视图
 */
public class ClassTimeActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {


    private static final String TAG = "ClassTimeActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_right)
    TextView mTvRight;

    @BindView(R.id.select_week)
    LinearLayout mSelectWeek;

    @BindView(R.id.tv_week)
    TextView mTvWeek;

    @BindView(R.id.et_class_time_name)
    LastInputEditText mEtClassTimeName;

    private MorningLayout mFirst;
    private AfternoonLayout mElAfternoon;
    private EveningLayout mElEvening;
    public ImageView mMorningCircle;
    public ImageView mAfternoonCircle;
    public ImageView mEveningCircle;
    public String mStr = null;
    public String mOneWeek = null;
    public String mTwoWeek = null;
    public String mThreeWeek = null;
    public String mFourWeek = null;
    public String mFiveWeek = null;
    public String mSevenWeek = null;
    public String mSixWeek = null;
    private Intent mIntent;
    private SimpleDateFormat mSimpleDateFormat;
    private final static int MORNINGCLASSTIME = 0;
    private final static int MORNINGCLASSOVERTIME = 1;
    private final static int AFTERNOONCLASSTIME = 2;
    private final static int AFTERNOONCLASSOVERTIME = 3;
    private final static int EVENINGCLASSTIME = 4;
    private final static int EVENINGCLASSOVERTIME = 5;
    private TextView mTvMorning;
    private TextView mTvMorningOver;
    private TextView mTvAfternoon;
    private TextView mTvAfternoonOver;
    private TextView mTvEvening;
    private TextView mTvEveningOver;
    private ClassTimePresenter mClassTimePresenter;
    public boolean isMorning;
    public boolean isAfternoon;
    public boolean isEvening;
    private ResponseInfoModel.ResultBean.ForbiddenTimeListBean mResult;
    private String mStartTimeAmStr;
    private String mEndTimeAmStr;
    private String mStartTimePmStr;
    private String mEndTimePmStr;
    private String mStartTimeEmStr;
    private String mEndTimeEmStr;
    private String mStateAM;
    private String mStatePM;
    private String mStateEM;
    private long mId = 0;
    private String mCycle;
    private String mPosition;
    private String mName;
    private ResponseInfoModel.ResultBean.ForbiddenTimeListBean mForbiddenTimeListBean;
    private List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> mClassBanList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_class_time;
    }

    @Override
    protected void init() {
        mClassTimePresenter = new ClassTimePresenter(this, this);

        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("HH:mm");
        }
        mResult = new ResponseInfoModel.ResultBean.ForbiddenTimeListBean();

        initView();
        initListener();
        initData();

    }

    private void initData() {
        //通过设备id查询设备上课禁用时间接口
//        mClassTimePresenter.queryForbiddenTimeByDeviceId(MyApplication.sToken,MyApplication.sDeviceId);
        mClassBanList = (List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean>)
                getIntent().getSerializableExtra("mClassBanList");
        mPosition = getIntent().getStringExtra("position");

        if (!"-1".equals(mPosition)) {
            mForbiddenTimeListBean = mClassBanList.get(Integer.valueOf(mPosition));
            mName = mForbiddenTimeListBean.getName();
            mCycle = mForbiddenTimeListBean.getCycle();
            mStr = mForbiddenTimeListBean.getCycle();
            mEtClassTimeName.setText(mName);
            loadData(mForbiddenTimeListBean);
        } else {
            LogUtils.e(TAG, "setData()");
            mName = "";
            mForbiddenTimeListBean = new ResponseInfoModel.ResultBean.ForbiddenTimeListBean();
            mForbiddenTimeListBean.setStateAM("1");
            mForbiddenTimeListBean.setStatePM("1");
            mForbiddenTimeListBean.setStartTimeAmStr("8:00");
            mForbiddenTimeListBean.setEndTimeAmStr("11:30");
            mForbiddenTimeListBean.setStartTimePmStr("14:00");
            mForbiddenTimeListBean.setEndTimePmStr("16:30");
            mForbiddenTimeListBean.setCycle("1,2,3,4,5");
            mStr = mForbiddenTimeListBean.getCycle();
            loadData(mForbiddenTimeListBean);
        }
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.class_time));
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(getString(R.string.save));
        mFirst = (MorningLayout) findViewById(R.id.first);
        mElAfternoon = (AfternoonLayout) findViewById(R.id.el_afternoon);
        mElEvening = (EveningLayout) findViewById(R.id.el_evening);

        mTvMorning = (TextView) mFirst.findViewById(R.id.tv_select_morning_time);
        mTvMorningOver = (TextView) mFirst.findViewById(R.id.tv_select_morning_over_time);
        mTvAfternoon = (TextView) mElAfternoon.findViewById(R.id.tv_select_afternoon_time);
        mTvAfternoonOver = (TextView) mElAfternoon.findViewById(R.id.tv_class_afternoon_over_time);
        mTvEvening = (TextView) mElEvening.findViewById(R.id.tv_select_class_evening_time);
        mTvEveningOver = (TextView) mElEvening.findViewById(R.id.tv_select_class_evening_over_time);

        mMorningCircle = (ImageView) mFirst.findViewById(R.id.iv_morning_circle);
        mAfternoonCircle = (ImageView) mElAfternoon.findViewById(R.id.iv_morning_circle);
        mEveningCircle = (ImageView) mElEvening.findViewById(R.id.iv_morning_circle);
    }


    private void initListener() {
        mFirst.findViewById(R.id.class_over_time_morning).setOnClickListener(this);
        mFirst.findViewById(R.id.class_time_morning).setOnClickListener(this);
        mElAfternoon.findViewById(R.id.class_time_afternoon).setOnClickListener(this);
        mElAfternoon.findViewById(R.id.class_over_time_afternoon).setOnClickListener(this);
        mElEvening.findViewById(R.id.class_time_evening).setOnClickListener(this);
        mElEvening.findViewById(R.id.class_over_time_evening).setOnClickListener(this);
        mEtClassTimeName.setOnFocusChangeListener(this);
    }


    @Override
    protected void dismissNewok() {

    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.select_week})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.tv_right:
                if (mResult == null) {
                    return;
                }
                mCycle = mTvWeek.getText().toString();
                mName = mEtClassTimeName.getText().toString();
                LogUtils.e(TAG, "点击了mCycle:" + mCycle);
                mClassTimePresenter.saveTime(mId, MyApplication.sToken, MyApplication.sDeviceId,
                        MyApplication.sAcountId, isMorning, isAfternoon, isEvening, mStartTimeAmStr,
                        mEndTimeAmStr, mStartTimePmStr, mEndTimePmStr, mStartTimeEmStr, mEndTimeEmStr,
                        mStateAM, mStatePM, mStateEM, mName, mStr);
                break;

            case R.id.select_week:
                mIntent = new Intent(this, SelectWeekActivity.class);
                if (mOneWeek != null) {
                    mIntent.putExtra("oneWeek", getString(R.string.oneWeek));
                }
                if (mTwoWeek != null) {
                    mIntent.putExtra("twoWeek", getString(R.string.twoWeek));
                }
                if (mThreeWeek != null) {
                    mIntent.putExtra("threeWeek", getString(R.string.threeWeek));
                }
                if (mFourWeek != null) {
                    mIntent.putExtra("fourWeek", getString(R.string.fourWeek));
                }
                if (mFiveWeek != null) {
                    mIntent.putExtra("fiveWeek", getString(R.string.fiveWeek));
                }

                LogUtils.e(TAG, "mSixWeek " + mSixWeek);
                if (mSixWeek != null) {
                    mIntent.putExtra("sixWeek", getString(R.string.sixWeek));
                }
                LogUtils.d(TAG, "mSixWeek " + mSevenWeek);
                if (mSevenWeek != null) {
                    mIntent.putExtra("sevenWeek", getString(R.string.sevenWeek));
                }

                startActivityForResult(mIntent, DECI_CODE);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DECI_CODE) {
            mStr = null;
            if (data != null) {
                if (data.getStringExtra("weekOne") != null) {
                    mOneWeek = data.getStringExtra("weekOne");
                    mStr = data.getStringExtra("weekOne") + ",";
                } else {
                    mOneWeek = null;
                }

                if (data.getStringExtra("weekTwo") != null) {
                    mTwoWeek = data.getStringExtra("weekTwo");
                    if (mStr == null) {
                        mStr = data.getStringExtra("weekTwo") + ",";
                    } else {
                        mStr += data.getStringExtra("weekTwo") + ",";
                    }
                } else {
                    mTwoWeek = null;
                }

                if (data.getStringExtra("weekThree") != null) {
                    mThreeWeek = data.getStringExtra("weekThree");
                    if (mStr == null) {
                        mStr = data.getStringExtra("weekThree") + ",";
                    } else {
                        mStr += data.getStringExtra("weekThree") + ",";
                    }
                } else {
                    mThreeWeek = null;
                }

                if (data.getStringExtra("weekFout") != null) {
                    mFourWeek = data.getStringExtra("weekFout");
                    if (mStr == null) {
                        mStr = data.getStringExtra("weekFout") + ",";
                    } else {
                        mStr += data.getStringExtra("weekFout") + ",";
                    }
                } else {
                    mFourWeek = null;
                }

                if (data.getStringExtra("weekFive") != null) {
                    mFiveWeek = data.getStringExtra("weekFive");
                    if (mStr == null) {
                        mStr = data.getStringExtra("weekFive") + ",";
                    } else {
                        mStr += data.getStringExtra("weekFive") + ",";
                    }
                } else {
                    mFiveWeek = null;
                }

                if (data.getStringExtra("weekSix") != null) {
                    mSixWeek = data.getStringExtra("weekSix");
                    if (mStr == null) {
                        mStr = data.getStringExtra("weekSix") + ",";
                    } else {
                        mStr += data.getStringExtra("weekSix") + ",";
                    }
                } else {
                    mSixWeek = null;
                }

                if (data.getStringExtra("weekSeven") != null) {
                    mSevenWeek = data.getStringExtra("weekSeven");
                    if (mStr == null) {
                        mStr = data.getStringExtra("weekSeven") + ",";
                    } else {
                        mStr += data.getStringExtra("weekSeven") + ",";
                    }
                } else {
                    mSevenWeek = null;
                }

                if (mStr != null) {

                    mStr = mStr.substring(0, mStr.length() - 1);
                    String str = mClassTimePresenter.chekCycle(mStr);
                    mTvWeek.setText(str);
                    //                    mClassTimePresenter.setWeek(mStr);
                    LogUtils.e(TAG, "weekOne " + mStr);
                } else {
                    LogUtils.e(TAG, "data == null");
                    mTvWeek.setText(getString(R.string.select_week));
                    printn("至少选择一个星期");
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.class_time_morning:
                KeyboardUtil.hideSoftInput(this);
                toTime(MORNINGCLASSTIME);
                break;

            case R.id.class_over_time_morning:
                KeyboardUtil.hideSoftInput(this);
                toTime(MORNINGCLASSOVERTIME);
                break;

            case R.id.class_time_afternoon:
                KeyboardUtil.hideSoftInput(this);
                toTime(AFTERNOONCLASSTIME);
                break;

            case R.id.class_over_time_afternoon:
                KeyboardUtil.hideSoftInput(this);
                toTime(AFTERNOONCLASSOVERTIME);
                break;

            case R.id.class_time_evening:
                KeyboardUtil.hideSoftInput(this);
                toTime(EVENINGCLASSTIME);
                break;

            case R.id.class_over_time_evening:
                KeyboardUtil.hideSoftInput(this);
                toTime(EVENINGCLASSOVERTIME);
                break;
        }
    }


    /**
     * 选择日期
     */
    private void toTime(final int type) {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                LogUtils.e(TAG, mSimpleDateFormat.format(date));
                LogUtils.e(TAG, date + "");
                chekTime(type, mSimpleDateFormat.format(date));

            }
        }).setType(TimePickerView.Type.HOURS_MINS)
                .setSubmitColor(Color.RED)//确定按钮文字颜色
                .setCancelColor(R.color.application_of_color)//取消按钮文字颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，
        // 避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    private void chekTime(int type, String format) {
        switch (type) {
            case MORNINGCLASSTIME:
                //上午上课时间
                mStartTimeAmStr = format;
                mTvMorning.setText(mStartTimeAmStr);
                break;

            case MORNINGCLASSOVERTIME:
                //上午下课时间
                mEndTimeAmStr = format;
                mTvMorningOver.setText(mEndTimeAmStr);
                break;

            case AFTERNOONCLASSTIME:
                //下午上课时间
                mStartTimePmStr = format;
                mTvAfternoon.setText(mStartTimePmStr);
                break;

            case AFTERNOONCLASSOVERTIME:
                //下午下课时间
                mEndTimePmStr = format;
                mTvAfternoonOver.setText(mEndTimePmStr);
                break;

            case EVENINGCLASSTIME:
                //晚上上课时间
                mStartTimeEmStr = format;
                mTvEvening.setText(mStartTimeEmStr);
                break;

            case EVENINGCLASSOVERTIME:
                //晚上下课时间
                mEndTimeEmStr = format;
                mTvEveningOver.setText(mEndTimeEmStr);
                break;
        }
    }


    /**
     * 查询设备上课禁用时间查询失败的通知
     *
     * @param s
     */
    public void onTimeByError(ResponseInfoModel s) {
//        dismissLoading();
//        printn(s.getResultMsg());
    }


    /**
     * 查询设备上课禁用时间查询成功的通知
     *
     * @param data
     */
    public void onTimeBySuccess(ResponseInfoModel data) {
//        dismissLoading();
//        List<ResponseInfoModel.ResultBean.ForbiddenTimeListBean> forbiddenTimeList = data.getResult().getForbiddenTimeList();
//        if (forbiddenTimeList != null && forbiddenTimeList.size() >= 1){
//        LogUtils.e(TAG,"forbiddenTimeList "+ forbiddenTimeList.size());
//            mResult = forbiddenTimeList.get(0);
//            mClassTimePresenter.chekCycle(mResult.getCycle());
//            mClassTimePresenter.setWeek(mResult.getCycle());
//        }
//        loadData(mResult);
    }


    /**
     * @param result
     */
    private void loadData(ResponseInfoModel.ResultBean.ForbiddenTimeListBean result) {
        mStartTimeAmStr = result.getStartTimeAmStr();
        mEndTimeAmStr = result.getEndTimeAmStr();
        mStartTimePmStr = result.getStartTimePmStr();
        mEndTimePmStr = result.getEndTimePmStr();
        mStartTimeEmStr = result.getStartTimeEmStr();
        mEndTimeEmStr = result.getEndTimeEmStr();
        mStateAM = result.getStateAM();
        mStatePM = result.getStatePM();
        mStateEM = result.getStateEM();
        mId = result.getId();
        mCycle = result.getCycle();
        LogUtils.e(TAG, "上午开启时间" + mStartTimeAmStr);
        LogUtils.e(TAG, "上午结束时间" + mEndTimeAmStr);
        LogUtils.e(TAG, "下午开启时间" + mStartTimePmStr);
        LogUtils.e(TAG, "下午结束时间" + mEndTimePmStr);
        LogUtils.e(TAG, "晚上开启时间" + mStartTimeEmStr);
        LogUtils.e(TAG, "晚上结束时间" + mEndTimeEmStr);
        LogUtils.e(TAG, "ID " + result.getId());
        if (mStartTimeAmStr == null) {
            mStartTimeAmStr = "";
        }
        if (mEndTimeAmStr == null) {
            mEndTimeAmStr = "";
        }
        if (mStartTimePmStr == null) {
            mStartTimePmStr = "";
        }
        if (mEndTimePmStr == null) {
            mEndTimePmStr = "";
        }
        if (mStartTimeEmStr == null) {
            mStartTimeEmStr = "";
        }
        if (mEndTimeEmStr == null) {
            mEndTimeEmStr = "";
        }
        LogUtils.e(TAG, "mCycle:" + mCycle);
        if (mCycle != null) {
            mCycle = mClassTimePresenter.chekCycle(result.getCycle());
        }
        morningState(mStateAM, mStartTimeAmStr, mEndTimeAmStr, mCycle);
        afternoonState(mStatePM, mStartTimePmStr, mEndTimePmStr, mCycle);
        eveningState(mStateEM, mStartTimeEmStr, mEndTimeEmStr, mCycle);

        LogUtils.e(TAG, "上午开启状态" + mStateAM);
        LogUtils.e(TAG, "下午开启状态" + mStatePM);
        LogUtils.e(TAG, "晚上开启状态" + mStateEM);

    }


    /**
     * 晚上的状态
     *
     * @param stateEM
     */
    private void eveningState(String stateEM, String startTimeEmStr, String endTimeEmStr, String week) {
        if (stateEM != null) {
            if (MORNINGCLASSOVERTIME == Integer.parseInt(stateEM)) {
                mElEvening.expand(mElEvening.contentLayout);
                mEveningCircle.setImageResource(R.mipmap.ok_nice);
                mTvEvening.setText(startTimeEmStr + "");
                mTvEveningOver.setText(endTimeEmStr + "");
                if (week != null) {
                    mTvWeek.setText(week);
                }
                isEvening = true;
                mStateEM = "1";
            } else {
                mElEvening.collapse(mElEvening.contentLayout);
                mEveningCircle.setImageResource(R.drawable.circle);
                mTvEvening.setText(startTimeEmStr + "");
                mTvEveningOver.setText(endTimeEmStr + "");
                isEvening = false;
                mStateEM = "0";
            }
        }
    }


    /**
     * 下午的状态
     *
     * @param statePM
     */
    private void afternoonState(String statePM, String startTimePmStr, String endTimePmStr, String week) {
        if (statePM != null) {
            if (MORNINGCLASSOVERTIME == Integer.parseInt(statePM)) {
                LogUtils.e(TAG, "下午的状态开启");
                mElAfternoon.expand(mElAfternoon.contentLayout);
                mAfternoonCircle.setImageResource(R.mipmap.ok_nice);
                mTvAfternoon.setText(startTimePmStr + "");
                mTvAfternoonOver.setText(endTimePmStr + "");
                if (week != null) {
                    mTvWeek.setText(week);
                }
                isAfternoon = true;
                mStatePM = "1";
            } else {
                mElAfternoon.collapse(mElAfternoon.contentLayout);
                mAfternoonCircle.setImageResource(R.drawable.circle);
                isAfternoon = false;
                mStatePM = "0";
            }
        }
    }


    /**
     * 上午的状态
     *
     * @param stateAM
     */
    private void morningState(String stateAM, String startTimeAmStr, String endTimeAmStr, String mCycle) {
        if (stateAM != null) {
            if (MORNINGCLASSOVERTIME == Integer.parseInt(stateAM)) {
                //开启状态
                mFirst.expand(mFirst.contentLayout);
                mMorningCircle.setImageResource(R.mipmap.ok_nice);
                mTvMorning.setText(startTimeAmStr);
                mTvMorningOver.setText(endTimeAmStr);
                if (mCycle != null) {
                    mTvWeek.setText(mCycle);
                }
                isMorning = true;
                mStateAM = "1";
            } else {
                //关闭状态
                mFirst.collapse(mFirst.contentLayout);
                mMorningCircle.setImageResource(R.drawable.circle);
                mTvMorning.setText(startTimeAmStr);
                mTvMorningOver.setText(endTimeAmStr);
                isMorning = false;
                mStateAM = "0";
            }
        }
    }


    /**
     * 没选择时间点保存错误的通知
     */
    public void selectTimeError(String str) {
        printn(str);
    }


    /**
     * 保存失败的通知
     *
     * @param resultMsg
     */
    public void onSaveError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }


    /**
     * 保存成功的通知
     *
     * @param body
     */
    public void onSaceSuccess(ResponseInfoModel body) {
        dismissLoading();
        ResponseInfoModel.ResultBean result = body.getResult();
        String cycle = result.getCycle();
//        mId = result.getId();
        mClassTimePresenter.setWeek(cycle);
        LogUtils.e(TAG, "result cycle" + cycle);
        mClassTimePresenter.chekCycle(cycle);
        printn(getString(R.string.set_up_the_success));
        finish();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            mEtClassTimeName.setGravity(Gravity.NO_GRAVITY);
        } else {
            mEtClassTimeName.setGravity(Gravity.CENTER);
        }
    }


}
