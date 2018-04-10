package com.loybin.baidumap.presenter;

import android.content.Context;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.ClassTimeActivity;
import com.loybin.baidumap.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/30 下午2:27
 * 描   述: 上课时间的业务逻辑
 */
public class ClassTimePresenter extends BasePresenter {
    private static final String TAG = "ClassTimeActivity";
    private Context mContext;
    private ClassTimeActivity mClassTimeActivity;
    public Call<ResponseInfoModel> mCall;
    private String mWeek = null;
    private Call<ResponseInfoModel> mInsertOrUpdateForbiddenTime;

    public ClassTimePresenter(Context context, ClassTimeActivity classTimeActivity) {
        super(context);
        mContext = context;
        mClassTimeActivity = classTimeActivity;
    }



    /**
     * 设备上课禁用时间查询成功的回调
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        LogUtils.e(TAG, data.getResultMsg());
        mClassTimeActivity.onTimeBySuccess(data);
    }


    /**
     * 查询设备上课禁用时间查询失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        LogUtils.e(TAG, s.getResultMsg());
        mClassTimeActivity.onTimeByError(s);
    }


    /**
     * 保存上课禁用时间
     *
     * @param id
     * @param token
     * @param deviceId
     * @param acountId
     * @param isMorning      true 上午时间表示开启状态
     * @param isAfternoon    true 下午时间表示开启状态
     * @param isEvening      true 晚上时间表示开启状态
     * @param startTimeAmStr 上午开启时间
     * @param endTimeAmStr   上午结束时间
     * @param startTimePmStr 下午上课时间
     * @param endTimePmStr   下午结束时间
     * @param startTimeEmStr 晚上上课时间
     * @param endTimeEmStr   晚上结束时间
     */
    public void saveTime(long id, String token, int deviceId, long acountId, boolean isMorning,
                         boolean isAfternoon, boolean isEvening, String startTimeAmStr,
                         String endTimeAmStr, String startTimePmStr, String endTimePmStr,
                         String startTimeEmStr, String endTimeEmStr, String stateAM, String statePM,
                         String stateEM, String name, String cycle) {


        if (stateEM == null || "".equals(stateEM)) {
            stateEM = "0";
        }

        boolean morningFlag = false;
        boolean afternoonFlag = false;
        boolean eveningFlag = false;

        if (isMorning || isAfternoon || isEvening) {
            if (isMorning) {
                morningFlag = checkMorning(startTimeAmStr, endTimeAmStr);
                stateAM = "1";
            } else {
                morningFlag = true;
                stateAM = "0";
            }

            if (isAfternoon) {
                statePM = "1";
                afternoonFlag = checkAfternoon(startTimePmStr, endTimePmStr);
            } else {
                afternoonFlag = true;
                statePM = "0";
            }

            if (isEvening) {
                stateEM = "1";
                eveningFlag = checkEvening(startTimeEmStr, endTimeEmStr);
            } else {
                eveningFlag = true;
                stateEM = "0";
            }

            if (morningFlag && afternoonFlag && eveningFlag) {
                if ("".equals(cycle) || cycle == null) {
                    mClassTimeActivity.selectTimeError("请选择星期");
                    return;
                } else {
                    mWeek = cycle;
                }

                if ("".equals(name)) {
                    mClassTimeActivity.selectTimeError("请输入名称");
                    return;
                }

                upDataTime(id, token, deviceId, acountId, startTimeAmStr, endTimeAmStr,
                        startTimePmStr, endTimePmStr, startTimeEmStr, endTimeEmStr, stateAM,
                        statePM, stateEM, name);
            }

        } else {
            mClassTimeActivity.selectTimeError(mContext.getString(R.string.please_select_disable_time_for_class));
        }
    }


    private boolean checkAfternoon(String startTime, String endTime) {
        if ("".equals(startTime) || "".equals(endTime)) {
            mClassTimeActivity.selectTimeError("下午时间选择不能为空");
            return false;
        }
        int startHour = Integer.valueOf(startTime.substring(0, startTime.indexOf(":")));
        int startMinute = Integer.valueOf(startTime.substring(startTime.indexOf(":") + 1,
                startTime.length()));
        int endHour = Integer.valueOf(endTime.substring(0, endTime.indexOf(":")));
        int endMinute = Integer.valueOf(endTime.substring(endTime.indexOf(":") + 1,
                endTime.length()));

        LogUtils.e(TAG, "startHour:" + startHour + " startMinute:" + startMinute + " endHour:" + endHour
                + " endMinute:" + endMinute);

        if (startHour > endHour) {
            mClassTimeActivity.selectTimeError("下午时间选择不合理");
            return false;
        }

        if (((endHour == 18) && (endMinute > 0)) || (endHour > 18) || (startHour < 12)) {
            mClassTimeActivity.selectTimeError("下午时间选择范围应在12:00至18:00之间");
            return false;
        }

        if (startHour == endHour) {
            if (startMinute >= endMinute) {
                mClassTimeActivity.selectTimeError("下午时间选择不合理");
                return false;
            }
        }

        return true;
    }


    private boolean checkEvening(String startTime, String endTime) {
        if ("".equals(startTime) || "".equals(endTime)) {
            mClassTimeActivity.selectTimeError("晚上时间选择不能为空");
            return false;
        }
        int startHour = Integer.valueOf(startTime.substring(0, startTime.indexOf(":")));
        int startMinute = Integer.valueOf(startTime.substring(startTime.indexOf(":") + 1,
                startTime.length()));
        int endHour = Integer.valueOf(endTime.substring(0, endTime.indexOf(":")));
        int endMinute = Integer.valueOf(endTime.substring(endTime.indexOf(":") + 1,
                endTime.length()));


        if (startHour > endHour) {
            mClassTimeActivity.selectTimeError("晚上时间选择不合理");
            return false;
        }

        if (startHour == endHour) {
            if (startMinute >= endMinute) {
                mClassTimeActivity.selectTimeError("晚上时间选择不合理");
                return false;
            }
        }

        if (startHour < 18) {
            mClassTimeActivity.selectTimeError("晚上时间选择范围应在18:00至24:00之间");
            return false;
        }

        return true;
    }


    private boolean checkMorning(String startTime, String endTime) {
        if ("".equals(startTime) || "".equals(endTime)) {
            mClassTimeActivity.selectTimeError("上午时间选择不能为空");
            return false;
        }
        int startHour = Integer.valueOf(startTime.substring(0, startTime.indexOf(":")));
        int startMinute = Integer.valueOf(startTime.substring(startTime.indexOf(":") + 1,
                startTime.length()));
        int endHour = Integer.valueOf(endTime.substring(0, endTime.indexOf(":")));
        int endMinute = Integer.valueOf(endTime.substring(endTime.indexOf(":") + 1,
                endTime.length()));


        if ((startHour > endHour)) {
            mClassTimeActivity.selectTimeError("上午时间选择不合理");
            return false;
        }

        if (startHour == endHour) {
            if (startMinute >= endMinute) {
                mClassTimeActivity.selectTimeError("上午时间选择不合理");
                return false;
            }
        }

        if ((endHour > 12) || (endHour == 12 && endMinute > 0)) {
            mClassTimeActivity.selectTimeError("上午时间选择范围应在0:00至12:00之间");
            return false;
        }

        return true;
    }





    /**
     * 设置/更新设备上课禁用时间接口
     *
     * @param id
     * @param token
     * @param deviceId
     * @param acountId
     * @param startTimeAmStr
     * @param endTimeAmStr
     * @param startTimePmStr
     * @param endTimePmStr
     * @param startTimeEmStr
     * @param endTimeEmStr
     * @param stateAM
     * @param statePM
     * @param stateEM
     */
    private void upDataTime(long id, String token, int deviceId, long acountId,
                            String startTimeAmStr, String endTimeAmStr, String startTimePmStr,
                            String endTimePmStr, String startTimeEmStr, String endTimeEmStr,
                            String stateAM, String statePM, String stateEM, String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        if (id != 0) {
            hashMap.put("id", id);
        }
        if ("1".equals(stateAM)) {
            hashMap.put("startTimeAM", startTimeAmStr);
            hashMap.put("endTimeAM", endTimeAmStr);
        }
        if ("1".equals(statePM)) {
            hashMap.put("startTimePM", startTimePmStr);
            hashMap.put("endTimePM", endTimePmStr);
        }
        if ("1".equals(stateEM)) {
            hashMap.put("startTimeEM", startTimeEmStr);
            hashMap.put("endTimeEM", endTimeEmStr);
        }

        LogUtils.e(TAG, "stateEM:" + stateEM);

        hashMap.put("deviceId", deviceId);
        hashMap.put("acountId", acountId);
        hashMap.put("stateAM", stateAM);
        hashMap.put("statePM", statePM);
        hashMap.put("stateEM", stateEM);
        hashMap.put("cycle", mWeek);
        hashMap.put("name", name);

        LogUtils.e(TAG, "保存上课禁用时间 " + String.valueOf(hashMap));
        mInsertOrUpdateForbiddenTime = mWatchService.insertOrUpdateForbiddenTime(hashMap);
        mClassTimeActivity.showLoading("",mContext);
        mInsertOrUpdateForbiddenTime.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mClassTimeActivity.onSaceSuccess(body);
    }


    @Override
    protected void onError(ResponseInfoModel body) {
        LogUtils.e(TAG, body.getResultMsg());
        mClassTimeActivity.onSaveError(body.getResultMsg());
    }


    @Override
    protected void onDissms(String s) {
        mClassTimeActivity.dismissLoading();
        LogUtils.e(TAG, s);
    }


    public void setWeek(String str) {
        mWeek = str;
        LogUtils.d(TAG, "mWeek  " + mWeek);
    }


    /**
     * 返回对应的星期
     *
     * @param cycle
     */
    public String chekCycle(String cycle) {
        String weekStr = "";

        String[] split = cycle.split(",");
        for (int i = 0; i < split.length; i++) {
            LogUtils.e(TAG, "split[" + i + "]:" + split[i]);
            String week = split[i];
            int weekNumber = Integer.parseInt(week);
            LogUtils.e(TAG, "week " + weekNumber);
            switch (weekNumber) {
                case 1:
                    mClassTimeActivity.mOneWeek = "1";
                    weekStr += mContext.getString(R.string.oneWeek);
                    break;

                case 2:
                    mClassTimeActivity.mTwoWeek = "2";
                    weekStr += mContext.getString(R.string.twoWeek);
                    break;

                case 3:
                    mClassTimeActivity.mThreeWeek = "3";
                    weekStr += mContext.getString(R.string.threeWeek);
                    break;

                case 4:
                    mClassTimeActivity.mFourWeek = "4";
                    weekStr += mContext.getString(R.string.fourWeek);
                    break;

                case 5:
                    mClassTimeActivity.mFiveWeek = "5";
                    weekStr += mContext.getString(R.string.fiveWeek);
                    break;

                case 6:
                    mClassTimeActivity.mSixWeek = "6";
                    weekStr += mContext.getString(R.string.sixWeek);
                    break;

                case 7:
                    mClassTimeActivity.mSevenWeek = "7";
                    weekStr += mContext.getString(R.string.sevenWeek);
                    break;


            }
        }
        return weekStr;
    }
}
