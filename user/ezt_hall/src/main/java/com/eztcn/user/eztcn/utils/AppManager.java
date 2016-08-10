package com.eztcn.user.eztcn.utils;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.os.Process;

/**
 * @title 应用程序Activity管理类
 * @describe 用于Activity管理和应用程序退出
 * @author ezt
 * @created 2014年11月10日
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	public Stack<Activity> getAllActivity() {
		return activityStack;
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 判断activity是否启动
	 * 
	 * @param activityName
	 * @return
	 */
	public boolean isLaunch(String activityName) {
		for (int i = 0; i < activityStack.size(); i++) {
			String aName = activityStack.get(i).getClass().getSimpleName();
			if (activityName.equals(aName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		Stack<Activity> activitys = new Stack<Activity>();
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				activitys.add(activity);
			}
		}

		for (Activity activity : activitys) {
			finishActivity(activity);
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			// 两个方法最好使用在出现异常的时候
			// int pid = Process.myPid();
			// Process.killProcess(pid);
			// System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
