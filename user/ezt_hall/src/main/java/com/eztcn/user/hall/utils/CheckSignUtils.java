package com.eztcn.user.hall.utils;

import java.util.Map;

/**
 * @Author: lizhipeng
 * @Data: 16/6/1 上午9:15
 * @Description: 验证签名工具类
 */
@SuppressWarnings("JniMissingFunction")
public class CheckSignUtils {

    static {
        System.loadLibrary("EztSo");
    }

    public static native String getSign(String secretKey, String[] strings,Map<String, String> params);

    /**
     * 获取应用id
     * @return
     */
    public static native String getAppId();

    /**
     * MD5验证
     * @param str
     * @return
     */
    public static native String MD5(String str);
}
