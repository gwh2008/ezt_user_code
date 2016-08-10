package com.eztcn.user.eztcn.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.MsgManageActivity;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;

/**
 * @title 消息工具类
 * @describe
 * @author ezt
 * @created 2015年1月15日
 */
public class MsgUtil {

	/**
	 * 创建通知栏
	 * 
	 * @param context
	 * @param title
	 *            消息标题
	 * @param content
	 *            消息内容
	 * @param type
	 *            消息类型
	 * @param count
	 *            消息条数
	 * 
	 * @param childId
	 *            医患沟通、患患沟通用到
	 */
	public static void createNotification(Context context, String title,
			String content, String type, int childId, int count) {
		// 得到NotificationManager
		NotificationManager nm = (NotificationManager) BaseApplication
				.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		// 实例化一个通知，指定图标、概要、时间
		Notification n = new Notification(R.drawable.notification_ic,
				type.equals("custom") || type.equals("register") ? content
						: title + "：" + content, System.currentTimeMillis());// 第二个参数为提示通知时的文本
		Class acClass = MsgManageActivity.class;// 消息箱
		Intent intent = new Intent(context, acClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈顶的活动，避免相同的活动被同时打开
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);// 栈顶则重用，否则新建实例
		// 指定点开后跳转的界面
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		// 指定通知的标题、内容和intent
//		n.setLatestEventInfo(context, title,
//				type.equals("custom") || type.equals("register") ? content
//						: "[ " + count + "条 ]" + content, pendingIntent);

		// 默认振动
		if (SystemPreferences.getBoolean(EZTConfig.KEY_SET_VIBRATOR, true))
			n.defaults |= Notification.DEFAULT_VIBRATE;
		// 指定声音
		if (SystemPreferences.getBoolean(EZTConfig.KEY_SET_SOUND, true)) {
			n.defaults |= Notification.DEFAULT_SOUND;
		}
		n.defaults |= Notification.DEFAULT_LIGHTS;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;// 点开后自动清除
		// Notification.FLAG_NO_CLEAR | //不可手工清除
		// | Notification.FLAG_ONGOING_EVENT//设置为后台常驻

		// 发送通知
		if (type.equals("custom") || type.equals("register")) {
			int flag = 0;
			if (type.equals("custom")) {
				flag = 1;
			} else if (type.equals("register")) {
				flag = 2;
			}
			nm.notify(flag, n);
		} else {// 患患沟通或医患沟通
			int newType = 0;
			if (type.equals("4")) {
				newType = Integer.parseInt("4" + childId);
			} else {
				newType = Integer.parseInt("5" + childId);
			}
			nm.notify(newType, n);
		}

	}

	/**
	 * 创建自定义更新通知栏
	 * 
	 * @param context
	 * @param title
	 * @param content
	 */
	public static void createUpdateNotification(Context context, String title,
			String createTime, String url) {

		NotificationManager nm = (NotificationManager) BaseApplication
				.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);

		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.custom_notification);
		rv.setImageViewResource(R.id.image, R.drawable.notification_ic);
		rv.setTextViewText(R.id.titie, title);
		rv.setTextViewText(R.id.time, createTime);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		builder.setContentIntent(pi);
		builder.setTicker("版本更新");
		builder.setContentTitle("");// 优先级低，不过必须设置，类似于初始化
		builder.setContentText("");// 优先级低，不过必须设置，类似于初始化
		builder.setSmallIcon(R.drawable.notification_ic);
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setContent(rv);
		builder.setAutoCancel(true);
		Notification notification = builder.build();
		nm.notify(-1, notification);

	}

	/**
	 * 判断app是否运行在后台
	 * 
	 * @return
	 */
	public static boolean isAppOnBackground() {
		// 获取运行中的进程列表
		ActivityManager activityManager = (ActivityManager) BaseApplication
				.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		String packageName = BaseApplication.getInstance().getPackageName();
		if (appProcesses == null || packageName == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& ((appProcess.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 清除消息栏
	 */
	public static void clearNotificationMsg() {
		NotificationManager nm = (NotificationManager) BaseApplication
				.getInstance().getSystemService(
						android.content.Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
	}

}
