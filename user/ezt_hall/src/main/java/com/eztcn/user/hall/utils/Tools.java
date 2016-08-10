package com.eztcn.user.hall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.eztcn.user.BuildConfig;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.*;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

import xutils.db.sqlite.WhereBuilder;

/**
 * 项目的工具类
 * @author 蒙
 */
public class Tools {


    /**
     * 判断当前应用签名是否是debug签名
     *
     * @param ctx
     * @return
     */
    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;
            for (int i = 0; i < signatures.length; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable)
                    break;
            }

        } catch (NameNotFoundException e) {
        } catch (CertificateException e) {
        } catch (NullPointerException e) {
            debuggable = BuildConfig.DEBUG;
        }
        return debuggable;
    }
    /**
     * 强制软键盘隐藏
     */
    public static void forceHidenSoftKeyboad(Context context,Activity activity) {
        View view2 = activity.getWindow().peekDecorView();
        InputMethodManager inputmanger = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view2 != null) {
            inputmanger.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }
    /**
     * 强制软键盘隐藏
     */
    public static void forceHidenSoftKeyboad(Activity activity) {
        View view2 = activity.getWindow().peekDecorView();
        InputMethodManager inputmanger = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view2 != null) {
            inputmanger.hideSoftInputFromWindow(view2.getWindowToken(), 0);
        }
    }
    public static boolean isMobile(String mobiles) {

        Pattern p = Pattern.compile("^1(3[0-9]|7[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return  m.matches();

    }
    public static void exitApp(Context context){

        BaseApplication.patient = null;
        BaseApplication.user=null;
        /**
         * 移除用户信息。
         */
        SystemPreferences.remove(EZTConfig.KEY_IS_AUTO_LOGIN);
        SystemPreferences.remove(EZTConfig.KEY_ACCOUNT);
        SystemPreferences.remove(EZTConfig.KEY_PW);
        /**
         * 移除选择医院、科室操作数据
         */
        SystemPreferences.remove(EZTConfig.KEY_DEPT_ID);
        SystemPreferences.remove(EZTConfig.KEY_STR_DEPT);
        SystemPreferences.remove(EZTConfig.KEY_SELECT_DEPT_POS);
        SystemPreferences.remove(EZTConfig.KEY_SELECT_AREA_POS);
        SystemPreferences.remove(EZTConfig.KEY_SELECT_HOS_POS);
        SystemPreferences.remove(EZTConfig.KEY_SELECT_N_POS);
        SystemPreferences.remove(EZTConfig.KEY_STR_CITY);
        SystemPreferences.remove(EZTConfig.KEY_CITY_ID);
        SystemPreferences.remove(EZTConfig.KEY_STR_N);
        SystemPreferences.remove(EZTConfig.KEY_HOS_ID);
        SystemPreferences.remove(EZTConfig.KEY_HOS_NAME);

        SystemPreferences.remove(EZTConfig.KEY_DF_DEPT_ID);
        SystemPreferences.remove(EZTConfig.KEY_DF_STR_DEPT);
        SystemPreferences.remove(EZTConfig.KEY_DF_SELECT_DEPT2_POS);
        SystemPreferences.remove(EZTConfig.KEY_DF_SELECT_DEPT_POS);
        //移除消息提醒
        SystemPreferences.remove(EZTConfig.KEY_HAVE_MSG);
        SystemPreferences.remove(EZTConfig.KEY_TOTAL);
        WhereBuilder delTypeB = WhereBuilder.b("typeId", "=", "register")
                .or("typeId", "=", "4").or("typeId", "=", "5");
        // 删除用户有关的本地存储数据（消息箱用到）
        EztDb.getInstance(context).delDataWhere(new MsgType(),
                delTypeB);
        WhereBuilder delAllB = WhereBuilder.b("msgType", "=", "register")
                .or("msgType", "=", "4").or("msgType", "=", "5");
        EztDb.getInstance(context).delDataWhere(new MessageAll(),
                delAllB);
        FileUtils.saveEztUserObject(context,"user",null);
        FileUtils.saveObject(context,"patient",null);
        FileUtils.saveStringBySp(context,"account","");//保存账户。
        FileUtils.saveStringBySp(context,"password","");//保存密码本地用于自动登录。
        com.eztcn.user.eztcn.utils.AppManager.getAppManager().AppExit(context);
        FileUtils.saveBooleanBySp(context,"isSkipMain",false);

    }

    /**
     * 正则式校验
     * @param matchString 正则表达式
     * @param targetString 需要校验的字符串
     * @return 是否匹配的结果
     */
    public static boolean isMatchString(String matchString,String targetString){
        Pattern pattern = Pattern.compile(matchString);
        Matcher matcher = pattern.matcher(targetString);
        return matcher.matches();
    }

    /**
     * 判断是否是手机号码
     * @param phone
     * @return
     */
    public static  boolean  isPhone(String phone){

        String regEXP="^((1[3,5,8][0-9])|(14[5,7])|(17[0,1,6,7,8]))\\d{8}$";
        Pattern p = Pattern.compile(regEXP);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (int) (dm.heightPixels);
    }

}