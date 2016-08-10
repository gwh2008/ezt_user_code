package com.eztcn.user.eztcn;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.baidu.location.BDLocation;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.CityImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.FontUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.LocationListener;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.UserBean;
import com.eztcn.user.hall.utils.LocationUtils;
import com.igexin.sdk.PushManager;

import org.xutils.x;

import java.util.ArrayList;

import xutils.http.RequestParams;
/**
 * @author ezt
 * @title 主容器
 * @describe
 * @created 2014年11月10日
 */
public class BaseApplication extends MultiDexApplication implements IHttpResult {

   // public static EztUser eztUser = null;
    public double lon = 0.0;// 经度
    public double lat = 0.0;// 纬度
    public String city = "";// 定位城市
    public static String selectCity = "";// 选择的城市
    public static City cCity;
    /**
     * 是否第一次进入主页(首页自动登录用到)
     */
    public boolean isFirst = true;
    public boolean isUpdateFirst = true;// 是否为第一次提醒更新
    public boolean isNetConnected = true;// 是否有网络

    private static BaseApplication instance = null;
    private SharedPreferences sharedPreferences;
    public boolean isBackground = false;// 是否在后台运行
    public static UserBean user=null;//用户的user对象。
    public static PatientBean patient=null;//用户的patient的对象

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).setContext(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //  SDKInitializer.initialize(this);
        // 个推
        PushManager.getInstance().initialize(this);
        //初始化苹果字体
        FontUtils.FontApple = Typeface.createFromAsset(getAssets(), "fonts/apple.ttf");
//        CrashReport.initCrashReport(getApplicationContext(), "900029826", false);
        initXutils();

        locationPosition();

    }

    /**
     * 进行当前城市的定位
     */
    public void locationPosition() {
        //进行当前城市的定位
        LocationUtils.location(this, new LocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                Constant.CURRENT_CITY_NAME = location.getCity();//对常量里面的城市进行赋值
            }
        });
    }

    /**
     * 初始化xutils
     */
    private void initXutils() {
        x.Ext.init(this);
        x.Ext.setDebug(Constant.isDebug); // 开启debug会影响性能
    }

    public SharedPreferences getPreferences() {
        return sharedPreferences;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * 获取数据字典
     */
    public void getDictionaryData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		UserImpl impl = new UserImpl();

        RequestParams params = new RequestParams();
        UserImpl impl = new UserImpl();

        impl.getDictionary(params, this);
    }

    /**
     * 获取城市列表
     */
    public void getCityData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		CityImpl impl = new CityImpl();
        RequestParams params = new RequestParams();
        CityImpl impl = new CityImpl();
        impl.getCityList(params, this);
    }

    /**
     * 获取应用名称
     *
     * @return
     */
    public String getAppName() {

        return (String) getPackageManager().getApplicationLabel(
                getApplicationInfo());
    }

    /**
     * 获取ApiKey
     *
     * @param context
     * @param metaKey
     * @return
     */
    public String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    @Override
    public void result(Object... object) {
        if (object == null) {
            return;
        }
        int type = (Integer) object[0];
        boolean isSuc = (Boolean) object[1];

        switch (type) {
            case HttpParams.GET_CITY:// 获取城市列表
                if (isSuc) {// 成功
                    final ArrayList<City> cityList = (ArrayList<City>) object[2];
                    if (cityList == null || cityList.size() == 0) {
                        return;
                    }
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            for (int i = 0; i < cityList.size(); i++) {
                                EztDb.getInstance(instance).save(cityList.get(i));
                            }
                        }
                    }).start();
                }
                break;

            case HttpParams.GET_DICT:// 数据字典
                if (isSuc) {// 成功
                    final ArrayList<EztDictionary> list = (ArrayList<EztDictionary>) object[2];
                    if (list == null || list.size() == 0) {
                        return;
                    }

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            EztDictionaryDB dictionaryDB = EztDictionaryDB
                                    .getInstance(instance);
                            dictionaryDB.clearTable();
                            for (int i = 0; i < list.size(); i++) {
                                dictionaryDB.save(list.get(i));
                            }
                            if (dictionaryDB != null) {
                                dictionaryDB = null;
                            }

                        }
                    }).start();

                }
                break;
        }

    }

}