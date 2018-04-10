package com.loybin.baidumap.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 时间转换的工具类
 */
public class TimeUtil {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 界面跳转
     *
     * @param context
     * @param class1
     */
    public static void startNewAcyivity(Context context, Class<?> class1) {
        Intent intent = new Intent(context, class1);
        context.startActivity(intent);
    }


    public Double GetTimeZone() {
        //1、取得本地时间：
        final Calendar cal = Calendar.getInstance();
        Log.i("TimeZone", cal.getTime() + "本地时间");
        //2、取得时间偏移量：
        final int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        Log.i("TimeZone", zoneOffset + "时间偏移量");
        //3、取得夏令时差：
        final int dstOffset = cal.get(Calendar.DST_OFFSET);
        Log.i("TimeZone", "取得夏令时差：" + dstOffset);
        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Log.i("TimeZone", "UTC时间：" + cal.getTime());

        double RawOff = (double) TimeZone.getDefault().getRawOffset()
                / (double) 3600000;
        Log.i("TimeZone", "RawOff：" + RawOff);

        return RawOff + dstOffset / 3600000;

    }


    public ProgressDialog createLoadingDialog(Context context, String msg) {
        ProgressDialog loadingDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        loadingDialog.setTitle(msg);
        loadingDialog.setCancelable(true);
        return loadingDialog;

    }


    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "出生时间大于当前时间!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    //do nothing
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        } else {
            //monthNow<monthBirth
            //donothing
        }

        return age;
    }


//    //获取版本名
//    public String getVersionName(Context context) {
//        PackageManager packageManager;
//        PackageInfo packInfo;
//        try {
//            // 获取packagemanager的实例
//            packageManager = context.getPackageManager();
//
//            // getPackageName()是你当前类的包名，0代表是获取版本信息
//            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//
//        } catch (Exception e) {
//            packInfo = null;
//        }
//        return packInfo.versionName;
//    }


    public String GetLanguage() {
        return Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
    }


    public String GetPackageName(Context context) {
        PackageManager packageManager;
        PackageInfo packInfo = null;
        try {
            packageManager = context.getPackageManager();
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return packInfo.packageName;
    }


    public String format(String s) {
        String str = s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str + ".amr";
    }


    public String getVoiceFilePath(String DeviceID, String VoiceName) {
        String filePath = getSDPath() + "/" + "BeiBeiAnRecord" + "/" + DeviceID;
        return filePath + "/" + VoiceName + ".amr";
    }


    public static String getVoiceFirstDirectoryPath() {
        String filePath = getSDPath() + "/" + "BeiBeiAnRecord";
        return filePath;
    }


    public static String getVoiceSecondDirectoryPath(String DeviceID) {
        String filePath = getSDPath() + "/" + "BeiBeiAnRecord" + "/" + DeviceID;
        return filePath;
    }


    //删除音频
    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }


    // 获得SD卡路径
    public static String getSDPath() {
        String sdDir = null;
        if (isHaveSDcard()) {
            sdDir = Environment.getExternalStorageDirectory().toString();// 获得根目录
        }
        return sdDir;
    }


    // 判断SD卡是否存在
    public static boolean isHaveSDcard() {
        // 判断SD卡是否存在 存在返回true 不存在返回false
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    public String VoiceToBase64String(File file) {
        byte[] data = null;
        try {
            InputStream is = new FileInputStream(file);
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        return Base64.encodeToString(data, Base64.DEFAULT);
    }


    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public static String getCurrentTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    public Boolean DateCompare(String s1, String s2) {
        Log.i("DateCompare", s1 + "," + s2);
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到指定模范的时间
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(s1);
            d2 = sdf.parse(s2);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //比较
        if (((d1.getTime() - d2.getTime())) > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean TimeCompare(String s1, String s2) throws Exception {
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //得到指定模范的时间
        Date d1 = sdf.parse(s1);
        Date d2 = sdf.parse(s2);
        if (d1.getHours() > d2.getHours()) {
            return true;
        } else if (d1.getHours() == d2.getHours() && d1.getMinutes() > d2.getMinutes()) {
            return true;
        } else {
            return false;
        }
        //比较
        //		if(((d1.getTime() - d2.getTime())/(3600*1000))>0) {
        //			return true;
        //		}else{
        //			return false;
        //		}
    }


    public String getUTCTimeStr() {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer UTCTimeBuffer = new StringBuffer();

        // 1、取得本地时间：

        Calendar cal = Calendar.getInstance();

        // 2、取得时间偏移量：

        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：

        int dstOffset = cal.get(Calendar.DST_OFFSET);

        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：

        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH) + 1;

        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);

        int second = cal.get(Calendar.SECOND);


        String monthStr = "";

        String dayStr = "";

        String hourStr = "";

        String minuteStr = "";

        String secondStr = "";

        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = monthStr + month;
        }

        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = dayStr + day;
        }

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = hourStr + hour;
        }

        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = minuteStr + minute;
        }

        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = secondStr + second;
        }

        UTCTimeBuffer.append(year).append("-").append(monthStr).append("-")

                .append(dayStr);

        UTCTimeBuffer.append(" ").append(hourStr).append(":").append(minuteStr)

                .append(":").append(secondStr);

        try {

            format.parse(UTCTimeBuffer.toString());

            return UTCTimeBuffer.toString();

        } catch (ParseException e) {

            e.printStackTrace();

        } catch (java.text.ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return null;

    }


    public String getStringToCal(String date) {
        //2015-05-10 14:52:29
        //0123456789
        final String year = date.substring(0, 4);
        final String month = date.substring(5, 7);
        final String day = date.substring(8, 10);
        final String hour = date.substring(11, 13);
        final String minute = date.substring(14, 16);
        String second = "00";
        try {
            second = date.substring(17, 19);
        } catch (Exception e) {
            // TODO: handle exception
        }
        //        final int millisecond = Integer.valueOf(date.substring(20, 23));
        Calendar result =
                new GregorianCalendar(Integer.valueOf(year),
                        Integer.valueOf(month) - 1, Integer.valueOf(day),
                        Integer.valueOf(hour), Integer.valueOf(minute),
                        Integer.valueOf(second));
        //        result.set(Calendar.MILLISECOND, millisecond);
        result.set(Calendar.MILLISECOND, 0);
        result.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

        // 1、取得本地时间：

        Calendar cal = Calendar.getInstance();

        // 2、取得时间偏移量：

        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：

        int dstOffset = cal.get(Calendar.DST_OFFSET);

        // 4、从UTC时间里加上这些差量，即可以取得本地时间：

        result.add(result.MILLISECOND, (zoneOffset + dstOffset));


        int year1 = result.get(Calendar.YEAR);

        int month1 = result.get(Calendar.MONTH) + 1;

        int day1 = result.get(Calendar.DAY_OF_MONTH);

        int hour1 = result.get(Calendar.HOUR_OF_DAY);

        int minute1 = result.get(Calendar.MINUTE);

        int second1 = result.get(Calendar.SECOND);

        StringBuffer UTCTimeBuffer = new StringBuffer();

        String monthStr = "";

        String dayStr = "";

        String hourStr = "";

        String minuteStr = "";

        String secondStr = "";

        if (month1 < 10) {
            monthStr = "0" + month1;
        } else {
            monthStr = monthStr + month1;
        }

        if (day1 < 10) {
            dayStr = "0" + day1;
        } else {
            dayStr = dayStr + day1;
        }

        if (hour1 < 10) {
            hourStr = "0" + hour1;
        } else {
            hourStr = hourStr + hour1;
        }

        if (minute1 < 10) {
            minuteStr = "0" + minute1;
        } else {
            minuteStr = minuteStr + minute1;
        }

        if (second1 < 10) {
            secondStr = "0" + second1;
        } else {
            secondStr = secondStr + second1;
        }

        UTCTimeBuffer.append(year1).append("-").append(monthStr).append("-")

                .append(dayStr);

        UTCTimeBuffer.append(" ").append(hourStr).append(":").append(minuteStr)

                .append(":").append(secondStr);

        try {

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            format.parse(UTCTimeBuffer.toString());

            return UTCTimeBuffer.toString();

        } catch (ParseException e) {

            e.printStackTrace();

        } catch (java.text.ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return null;
    }


    public String getStringToUTC(String date) {
        //2015-05-10 14:52:29
        //0123456789
        final String year = date.substring(0, 4);
        final String month = date.substring(5, 7);
        final String day = date.substring(8, 10);
        final String hour = date.substring(11, 13);
        final String minute = date.substring(14, 16);
        String second = "";
        try {
            second = date.substring(17, 19);
        } catch (Exception e) {
            second = "00";
        }
        //        final int millisecond = Integer.valueOf(date.substring(20, 23));
        Calendar result =
                new GregorianCalendar(Integer.valueOf(year),
                        Integer.valueOf(month) - 1, Integer.valueOf(day),
                        Integer.valueOf(hour), Integer.valueOf(minute),
                        Integer.valueOf(second));
        //        result.set(Calendar.MILLISECOND, millisecond);
        result.set(Calendar.MILLISECOND, 0);
        result.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

        // 1、取得本地时间：

        Calendar cal = Calendar.getInstance();

        // 2、取得时间偏移量：

        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：

        int dstOffset = cal.get(Calendar.DST_OFFSET);

        // 4、从UTC时间里加上这些差量，即可以取得本地时间：

        result.add(result.MILLISECOND, -(zoneOffset + dstOffset));


        int year1 = result.get(Calendar.YEAR);

        int month1 = result.get(Calendar.MONTH) + 1;

        int day1 = result.get(Calendar.DAY_OF_MONTH);

        int hour1 = result.get(Calendar.HOUR_OF_DAY);

        int minute1 = result.get(Calendar.MINUTE);

        int second1 = result.get(Calendar.SECOND);

        StringBuffer UTCTimeBuffer = new StringBuffer();

        String monthStr = "";

        String dayStr = "";

        String hourStr = "";

        String minuteStr = "";

        String secondStr = "";

        if (month1 < 10) {
            monthStr = "0" + month1;
        } else {
            monthStr = monthStr + month1;
        }

        if (day1 < 10) {
            dayStr = "0" + day1;
        } else {
            dayStr = dayStr + day1;
        }

        if (hour1 < 10) {
            hourStr = "0" + hour1;
        } else {
            hourStr = hourStr + hour1;
        }

        if (minute1 < 10) {
            minuteStr = "0" + minute1;
        } else {
            minuteStr = minuteStr + minute1;
        }

        if (second1 < 10) {
            secondStr = "0" + second1;
        } else {
            secondStr = secondStr + second1;
        }

        UTCTimeBuffer.append(year1).append("-").append(monthStr).append("-")

                .append(dayStr);

        UTCTimeBuffer.append(" ").append(hourStr).append(":").append(minuteStr)

                .append(":").append(secondStr);

        try {

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            format.parse(UTCTimeBuffer.toString());

            return UTCTimeBuffer.toString();

        } catch (ParseException e) {

            e.printStackTrace();

        } catch (java.text.ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return null;
    }


//    /**
//     * 获得指定日期的前一天
//     *
//     * @param specifiedDay
//     * @return
//     * @throws Exception
//     */
//    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数
//        Calendar c = Calendar.getInstance();
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        c.setTime(date);
//        int day = c.get(Calendar.DATE);
//        c.set(Calendar.DATE, day - 1);
//
//        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
//                .getTime());
//        return dayBefore;
//    }


//    /**
//     * 获得指定日期的后一天
//     *
//     * @param specifiedDay
//     * @return
//     */
//    public static String getSpecifiedDayAfter(String specifiedDay) {
//        Calendar c = Calendar.getInstance();
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        c.setTime(date);
//        int day = c.get(Calendar.DATE);
//        c.set(Calendar.DATE, day + 1);
//
//        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
//                .format(c.getTime());
//        return dayAfter;
//    }


//    // 获取指定日期在当月中的第一天
//    public static Date getFirstDayOfMonth(String dateStr) {
//        Date date = null;
//        Date parse = null;
//        try {
//            date = simpleDateFormat.parse(dateStr);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            String str = "";
//            calendar.set(Calendar.DATE, 1);
//            str = simpleDateFormat.format(calendar.getTime());
//            parse = formatter.parse(str + " 00:00:00");
//        } catch (java.text.ParseException e) {
//            Log.e("DayOfMonth", "解析异常");
//            e.printStackTrace();
//        }
//        return parse;
//    }

//
//    // 获取指定日期在当月中的最后一天
//    public static Date getLastDayOfMonth(String dateStr) {
//        Date date = null;
//        Date parseResult = null;
//        try {
//            date = simpleDateFormat.parse(dateStr);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            String str = "";
//            calendar.set(Calendar.DATE, 1);
//            calendar.add(Calendar.MONTH, 1);
//            calendar.add(Calendar.DATE, -1);
//            str = simpleDateFormat.format(calendar.getTime());
//            parseResult = formatter.parse(str + " 23:59:59");
//        } catch (java.text.ParseException e) {
//            Log.e("DayOfMonth", "解析异常");
//            e.printStackTrace();
//        }
//        return parseResult;
//    }


//    //获取指定日期在当周中的第一天
//    public static Date getFirstDayOfWeek(String dateStr) {
//        //			dateStr="2016-12-11";
//        Log.i("DayOfWeek", dateStr);
//        Date date = null;
//        Date parse = null;
//        try {
//            date = simpleDateFormat.parse(dateStr);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
//            if (1 == dayWeek) {
//                cal.add(Calendar.DAY_OF_MONTH, -1);
//            }
//            cal.setFirstDayOfWeek(Calendar.MONDAY);
//            int day = cal.get(Calendar.DAY_OF_WEEK);
//            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
//            String str = simpleDateFormat.format(cal.getTime());
//            parse = formatter.parse(str + " 00:00:00");
//            Log.i("DayOfWeek", "getFirstDayOfWeek=" + str + " 00:00:00");
//        } catch (java.text.ParseException e) {
//            Log.e("DayOfWeek", "解析异常");
//            e.printStackTrace();
//        }
//        return parse;
//    }

//
//    //获取指定日期在当周中的最后一天
//    public static Date getLastDayOfWeek(String dateStr) {
//        //			dateStr="2016-12-11";
//        Log.i("DayOfWeek", dateStr);
//        Date date = null;
//        Date parse = null;
//        try {
//            date = simpleDateFormat.parse(dateStr);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
//            if (1 == dayWeek) {
//                cal.add(Calendar.DAY_OF_MONTH, -1);
//            }
//            cal.setFirstDayOfWeek(Calendar.MONDAY);
//            int day = cal.get(Calendar.DAY_OF_WEEK);
//            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
//            cal.add(Calendar.DATE, 6);
//            String str = simpleDateFormat.format(cal.getTime());
//            parse = formatter.parse(str + " 23:59:59");
//            Log.i("DayOfWeek", "getLastDayOfWeek=" + str + " 23:59:59");
//        } catch (java.text.ParseException e) {
//            Log.e("DayOfWeek", "解析异常");
//            e.printStackTrace();
//        }
//        return parse;
//    }


    //获取当天所属的一周时间段
    public static String getWeekTimeQuantumStr(Date startDate, Date endDate) {
        //2016-12-11
        String month = "";
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        int m = startCalendar.get(Calendar.MONTH) + 1;
        if (m < 10) {
            month = "0" + m;
        } else {
            month = String.valueOf(m);
        }

        return startCalendar.get(Calendar.YEAR) + " " + month + " " + startCalendar.get(Calendar.DAY_OF_MONTH) + "-" +
                endCalendar.get(Calendar.DAY_OF_MONTH);
    }


    /*
    * 毫秒转化时分
    */
    public static String formatTime(Long between) {
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天前");
            return sb.toString();
        }
        if (hour > 0) {
            sb.append(hour + "小时前");
            return sb.toString();
        }
        if (min > 0) {
            sb.append(min + "分钟前");
            return sb.toString();
        }
        if (s > 0) {
            sb.append(s + "秒前");
        }
        return sb.toString();
    }


    /**
     * one hour in ms
     */
    private static final int ONE_HOUR = 1 * 60 * 60 * 1000;
    /**
     * one minute in ms
     */
    private static final int ONE_MIN = 1 * 60 * 1000;
    /**
     * one second in ms
     */
    private static final int ONE_SECOND = 1 * 1000;

    /**
     * HH:mm:ss
     */
    public static String formatTimes(long ms) {
        StringBuilder sb = new StringBuilder();
        int hour = (int) (ms / ONE_HOUR);
        int min = (int) ((ms % ONE_HOUR) / ONE_MIN);
        int sec = (int) (ms % ONE_MIN) / ONE_SECOND;
        if (hour == 0) {
//			sb.append("00:");
        } else if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (min == 0) {
            sb.append("00:");
        } else if (min < 10) {
            sb.append("0").append(min).append(":");
        } else {
            sb.append(min).append(":");
        }
        if (sec == 0) {
            sb.append("00");
        } else if (sec < 10) {
            sb.append("0").append(sec);
        } else {
            sb.append(sec);
        }
        return sb.toString();
    }


    public static long getMs(String data) {
        long millionSeconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            millionSeconds = sdf.parse(data).getTime();//毫秒

            return millionSeconds;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;


    }


}
