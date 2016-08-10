package com.eztcn.user.hall.common;

import com.eztcn.user.hall.model.ResultResponse.CityListDataResponse;

/**
 * @Author: lizhipeng
 * @Data: 16/5/3 下午4:27
 * @Description:常量配置类
 */
public class Constant {
    public static boolean isDebug = true;//是否打印日志，手动更改
    public static String CURRENT_CITY_NAME = "";//定位到的当前城市，在欢迎页面就已经定位了

    public  static String PLAT_ID = "355";//安卓平台id

    public interface LogConfig {//log工具类配置字段接口

        public static final boolean isWrite = false;//是否将日志写入文件
        public static final String TAG = "ezt";
    }

    public static boolean IS_SPECIAL_HOSPITAL = false;//是否是选中了天津市肿瘤医院

    public static CityListDataResponse cityListDataResponse = new CityListDataResponse();//当前定位到的城市数据，或者默认城市

}
