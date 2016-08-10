package com.eztcn.user.eztcn.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.SettingManager;
import com.eztcn.user.eztcn.controller.VersionManager;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.hall.activity.MainActivity;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.common.DragonStatusSingle;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.LocationListener;
import com.eztcn.user.hall.model.DataResponse;
import com.eztcn.user.hall.model.DragonStatusResponse;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.UserBean;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.LocationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xutils.ViewUtils;
import xutils.db.sqlite.WhereBuilder;

/**
 * @author ezt
 * @title 欢迎页面
 * @describe
 * @created 2015年1月23日
 */
public class WelcomeActivity extends FinalActivity implements ResultSubscriber.OnResultListener {

    private final int TIME = 2000;// 加载页等待时间
    private Timer timer;
    private final int LOGIN_TYPE = 0;//登录标记。
    private static final int GET_DRAGON_STATE = 10001;//获取龙卡信息
    private Context context = WelcomeActivity.this;
    private String TAG = "WelcomeActivity";
    private  int isFirstInstall=-1;//第一次安装。默认是-1 是第一次。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome30);
        ViewUtils.inject(WelcomeActivity.this);
        int code = VersionManager.getVersionCode(this);// 当前版本
        int oldCode = SystemPreferences.getInt(EZTConfig.KEY_SET_VERSION_CODE,
                0);
        if (oldCode < code) {// 覆盖升级
            SettingManager.clearCache(this, false);
            SystemPreferences.save(EZTConfig.KEY_IS_FIRST, true);
        }
        SystemPreferences.save(EZTConfig.KEY_SET_VERSION_CODE,
                VersionManager.getVersionCode(this));// 保存版本号
        isFirstInstall=FileUtils.getIntegerBySp(context,"isFirstInstall");

        try{
            userLogin();
            initial();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //进行当前城市的定位
            LocationUtils.location(mContext, new LocationListener() {
                @Override
                public void onReceiveLocation(BDLocation location) {
                    Constant.CURRENT_CITY_NAME = location.getCity();//对常量里面的城市进行赋值
                }
            });
        }
    }

    private void initial() {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                toIntent();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, TIME);
    }
    /**
     * 获取龙卡状态
     */
    private void getDragonStatus() {
        if (BaseApplication.patient == null) {
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("uid",String.valueOf(BaseApplication.patient.getUserId()));
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.DRAGON).postDragonStatus2(map, GET_DRAGON_STATE, this);
    }

    /**
     * 跳转
     */
    private void toIntent() {
        Intent intent;
        // 如果第一次，则进入引导页WelcomeActivity
        if (isFirstInstall==-1) {
            intent = new Intent(WelcomeActivity.this, GuidanceActivity.class);
            FileUtils.saveIntegerBySp(context,"isFirstInstall",0);
        } else {
            intent = new Intent(WelcomeActivity.this, MainActivity.class);
        }
        startActivity(intent);
        // 获取数据字典
        BaseApplication.getInstance().getDictionaryData();
        WhereBuilder b = null;
        ArrayList<City> citys = EztDb.getInstance(WelcomeActivity.this).queryAll(
                new City(), b, null);
        if (citys.size() == 0) {
            BaseApplication.getInstance().getCityData();
        }

        WelcomeActivity.this.finish();
    }
    /**
     * 调取登录接口。
     */
    private void userLogin() {

        String account = FileUtils.getStringBySp(context, "account");
        String pass_word = FileUtils.getStringBySp(context, "password");
        if (!"".equals(pass_word) && !"error".equals(pass_word)) {
//            pass_word = MD5.getMD5ofStr(pass_word);
            Map<String, String> params = new HashMap<>();
            params.put("username", account);
            params.put("password", pass_word);
            Request request = new Request();
            Map<String, String> map = request.getFormMap(params);
            HTTPHelper helper = HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
            helper.postLogin(map, LOGIN_TYPE, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != timer)
            timer.cancel();
    }

    @Override
    public void onStart(int requestType) {
        Log.i(TAG, "onStart");
    }

    @Override
    public void onCompleted(int requestType) {
        Log.i(TAG, "onCompleted");
    }

    @Override
    public void onError(int requestType) {
        Log.i(TAG, "onError");

        switch (requestType) {

            case LOGIN_TYPE:
                UserBean user = (UserBean) FileUtils.getObject(context, "user");
                if (user != null) {
                    BaseApplication.user = user;//初始化静态
                }
                PatientBean patient = (PatientBean) FileUtils.getObject(context, "patient");
                if (patient != null) {
                    BaseApplication.patient = patient;//初始化静态
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNext(IModel t, int requestType) {

        switch (requestType) {

            case LOGIN_TYPE:
                Response response = (Response) t;
                int number = Integer.valueOf(response.getNumber());
                if (number == 2000) {
                    DataResponse data = (DataResponse) response.getData();
                    UserBean user = data.getUserbean();
                    PatientBean patient = data.getPatientBean();
                    saveUserInfo(user, patient);
                    //将用户信息进行保存在本地
                    FileUtils.saveObject(context, "user", user);
                    FileUtils.saveObject(context, "patient", patient);
                    BaseApplication.patient=patient;
                    BaseApplication.user=user;
                    setResult(222);
                    getDragonStatus();
                }
                break;
            case GET_DRAGON_STATE:
                Response<DragonStatusResponse> dragonStatusResponse = (Response<DragonStatusResponse>) t;
                DragonStatusSingle dragonSingle = DragonStatusSingle.getInstance();
//                Log.i("DragonStatus", "我屮艸芔茻" + dragonStatusResponse);
                if ("2000".equals(dragonStatusResponse.getNumber())){
//                    Log.i("DragonStatus", "" + dragonStatusResponse.toString());
                    dragonSingle.setOpenDragon(true);
                    DragonStatusResponse dragonResponse = dragonStatusResponse.getData();
//                    Log.i("DragonStatus", "" + dragonResponse.toString());
                    dragonSingle.setActiveDate(dragonResponse.getActiveDate());
                    dragonSingle.setBankCardId(dragonResponse.getBankCardId());
                    dragonSingle.setCustId(dragonResponse.getCustId());
                    dragonSingle.setCustName(dragonResponse.getCustName());
                    dragonSingle.setEndDate(dragonResponse.getEndDate());
                    dragonSingle.setGuideNum(dragonResponse.getGuideNum());
                    dragonSingle.setId(dragonResponse.getId());
                    dragonSingle.setLeadNum(dragonResponse.getLeadNum());
                    dragonSingle.setOpencard(dragonResponse.getOpencard());
                    dragonSingle.setPayFlag(dragonResponse.getPayFlag());
                    dragonSingle.setPfId(dragonResponse.getPfId());
                    dragonSingle.setPhone(dragonResponse.getPhone());
                    dragonSingle.setRemindNum(dragonResponse.getRemindNum());
                    dragonSingle.setSex(dragonResponse.getSex());
                    dragonSingle.setUid(dragonResponse.getUid());
                    dragonSingle.setUname(dragonResponse.getUname());
                }else {
                    dragonSingle.setOpenDragon(false);
                    Log.i(TAG, "" + dragonStatusResponse.getDetailMsg());
                }
                break;
            default:
                break;
        }
    }
    /**
     * 保存用户信息。
     */
    private void saveUserInfo(UserBean user, PatientBean patient) {
        if (user != null) {
            FileUtils.saveObject(context, "user", user);
        }
        if (patient != null) {
            FileUtils.saveObject(context, "patient", patient);
        }
        BaseApplication.user = user;//初始化静态
        BaseApplication.patient = patient;//初始化静态
    }
}