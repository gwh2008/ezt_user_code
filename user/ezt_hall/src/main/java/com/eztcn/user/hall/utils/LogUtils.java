package com.eztcn.user.hall.utils;

import android.util.Log;

import com.eztcn.user.hall.common.Constant;


/**
 * @Author: lizhipeng
 * @Data: 16/5/3 下午4:32
 * @Description: log的工具类
 */
public class LogUtils {

    /**
     * 不能被实例化
     */
    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 下面四个是默认tag的函数  
    public static void i(String msg)
    {
        if (Constant.isDebug)
            Log.i(Constant.LogConfig.TAG, msg);
    }

    public static void d(String msg)
    {
        if (Constant.isDebug)
            Log.d(Constant.LogConfig.TAG, msg);
    }

    public static void e(String msg)
    {
        if (Constant.isDebug)
            Log.e(Constant.LogConfig.TAG, msg);
    }

    public static void v(String msg)
    {
        if (Constant.isDebug)
            Log.v(Constant.LogConfig.TAG, msg);
    }

    // 下面是传入自定义tag的函数  
    public static void i(String tag, String msg)
    {
        if (Constant.isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (Constant.isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (Constant.isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (Constant.isDebug)
            Log.i(tag, msg);
    }

    /**
     * 这是我的自定义的日志打印方法，不影响其他的日志打印
     * 张蒙
     * @param str
     */
    public static void logaaaaa(String str) {
        if (Constant.isDebug) {
            Log.i("aaaaa", str);
        }
    }
}
