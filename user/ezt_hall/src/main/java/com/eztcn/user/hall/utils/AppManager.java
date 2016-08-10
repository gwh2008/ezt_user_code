package com.eztcn.user.hall.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
/**
 * Created by lx on 2016/6/12.
 * APP的管理工具。
 */
public class AppManager  {


    public static String getVersion(Context context){

        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
