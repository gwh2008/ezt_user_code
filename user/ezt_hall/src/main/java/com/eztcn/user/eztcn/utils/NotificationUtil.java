package com.eztcn.user.eztcn.utils;

import android.app.NotificationManager;

import com.eztcn.user.eztcn.BaseApplication;

/**
 * @title 封装了通知相关方法
 * @describe
 * @author ezt
 * @created 2014年11月19日
 */
public class NotificationUtil {

	public static boolean notificationIsShow = false;// 通知是否正在显示

	public static final int NOTIFICATION_ID = 1;// 通知ID

	/**
	 * 展示常驻通知
	 */
	public void showNotification() {
		if (!MsgUtil.isAppOnBackground()) {
			Logger.i("", "前台运行");
			notificationIsShow = false;
		} else {
			Logger.i("", "后台运行");
			notificationIsShow = true;
		}
	}

	/**
	 * 取消常驻通知
	 */
	public void cancelNotification() {
		if (notificationIsShow) {
			NotificationManager nm = (NotificationManager) BaseApplication
					.getInstance().getSystemService(
							android.content.Context.NOTIFICATION_SERVICE);
			nm.cancel(NOTIFICATION_ID);
			notificationIsShow = false;
		}
	}

}