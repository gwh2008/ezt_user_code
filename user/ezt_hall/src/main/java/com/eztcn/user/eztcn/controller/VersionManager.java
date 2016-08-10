package com.eztcn.user.eztcn.controller;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @title 版本信息
 * @describe
 * @author ezt
 * @created 2014年11月4日
 */
public class VersionManager {

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context mContext) {
		int versionCode = 0;

		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		try {
			versionCode = mContext.getPackageManager().getPackageInfo(
					"com.eztcn.user", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context mContext) {
		String versionCode = null;

		// 获取软件版本号，对应AndroidManifest.xml下android:versionName
		try {
			versionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
			if (versionCode != null) {
				return " " + versionCode;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
