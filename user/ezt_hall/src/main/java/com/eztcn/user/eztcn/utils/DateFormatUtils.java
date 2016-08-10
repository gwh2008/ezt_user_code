package com.eztcn.user.eztcn.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * java的时间转换工具类,对于特殊时间格式的转化
 * Created by 蒙 on 2016/7/15.
 */
public class DateFormatUtils {

    /**
     * 将"2016-11-16T15:22:32"这样的带"T"的时间字符串转化为标准的时间字符串
     *
     * @param dateString
     * @return
     */
    public static String t2Standard(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.parse(dateString);
            Calendar c = sdf.getCalendar();
            String aa = getString(c);
            return aa;
        } catch (Exception pe) {
            System.out.print("Exception--" + pe.getMessage());
        }
        return "";
    }

    /**
     * 通过Calendar类将年月日时分秒拼接为时间字符串
     *
     * @param c
     * @return
     */
    private static String getString(Calendar c) {
        StringBuffer result = new StringBuffer();
        result.append(c.get(Calendar.YEAR));
        result.append("-");
        int month = c.get(Calendar.MONTH) + 1;
        result.append(month > 9 ? month : "0" + month);//不够10就前面拼接0
        result.append("-");
        int day = c.get(Calendar.DAY_OF_MONTH);
        result.append(day > 9 ? day : "0" + day);
        result.append(" ");
        int hour = c.get(Calendar.HOUR_OF_DAY);
        result.append(hour > 9 ? hour : "0" + hour);
        result.append(":");
        int minute = c.get(Calendar.MINUTE);
        result.append(minute > 9 ? minute : "0" + minute);
        result.append(":");
        int second = c.get(Calendar.SECOND);
        result.append(second > 9 ? second : "0" + second);
        return result.toString();
    }

    /**
     * 获取系统日期年份
     */
    public static String getCurrentTimeY() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统日期月份
     */
    public static String getCurrentTimeM() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统日
     */
    public static String getCurrentTimeD() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统年月日
     */
    public static String getCurrentTimeYMD() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统日期小时和分钟
     */
    public static String getCurrentTimeHM() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统日期分钟和秒
     */
    public static String getCurrentTimeMS() {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统日期小时分钟和秒
     */
    public static String getCurrentTimeHMS() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统日期 秒
     */

    public static String getCurrentTimeS() {
        SimpleDateFormat formatter = new SimpleDateFormat("ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统星期,返回的格式为“周一，周二 等”
     */
    public static String getDateWeek() {
        String mWay;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "周" + mWay;
    }


    /**
     * 判断时间是否超过期限
     *
     * @param issued   与当前时间对比的开始时间，格式为：Wed, 20 Feb 2013 11:41:23 GMT,格式可以自己重新定义
     * @param validate 时间期限 （秒）
     * @return
     */
    public static boolean isExpired(String issued, long validate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date d = dateFormat.parse(issued);
            long current = System.currentTimeMillis();
            return current - d.getTime() > validate * 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 将T格式的时间转化为对应的"yyyy-MM-dd HH:mm"格式的时间，输出格式可以自己定义
     *
     * @param time
     * @return
     */
    public static String fromatTTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date d = simpleDateFormat.parse(time);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将TS格式的时间转化为对应的"yyyy-MM-dd HH:mm:ss"格式的时间，输出格式可以自己定义
     *
     * @param time
     * @return
     */
    public static String fromatTSTime(String time) {
        if (TextUtils.isEmpty(time) || "null".equals(time)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Date d = simpleDateFormat.parse(time);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将时间字符串转化为Date类,时间格式可以自己定义
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 计算开始时间和结束时间中间间隔多长时间，返回的数据可能是秒，分，小时，天
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static String[] calcuateSchedule(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data[] = new String[2];
        try {
            long start = dateFormat.parse(startTime).getTime();
            long end = dateFormat.parse(endTime).getTime();
            long div = (end - start);
            if (div >= 0 && div < 1000) {
                data[0] = "毫秒";
                data[1] = div + "";
                return data;
            }
            if (div >= 1000 && div < 1000 * 60) {
                data[0] = "秒";
                data[1] = div / 1000 + "";
                return data;
            }
            if (div >= 1000 * 60 && div < 1000 * 60 * 60) {
                data[0] = "分钟";
                data[1] = div / 1000 / 60 + "";
                return data;
            }

            if (div >= 1000 * 60 * 60 && div < 1000 * 60 * 60 * 24) {
                data[0] = "小时";
                data[1] = div / 1000 / 60 / 60 + "";
                return data;
            }

            if (div >= 1000 * 60 * 60 * 24 && div < 1000 * 60 * 60 * 24 *
                    365L) {
                data[0] = "天";
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                data[1] = decimalFormat.format(div * 1.0 / 1000 / 60 / 60 / 24);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
