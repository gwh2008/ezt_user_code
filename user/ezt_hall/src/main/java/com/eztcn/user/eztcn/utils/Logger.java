package com.eztcn.user.eztcn.utils;

import android.util.Log;

import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @title 日志管理类
 * @describe 方便打包时，关闭调试日志
 * @author ezt
 * @created 2014年11月10日
 */
public class Logger {

	private final static String TAG = "eztuser";

	/**
	 * Send an INFO log message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, Object msg) {
		if (EZTConfig.IS_PRINT_LOG) {
			Log.i(TAG, tag + " == " + msg);
		}
	}

	/**
	 * Send an ERROR log message.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, Object msg) {
		if (EZTConfig.IS_PRINT_LOG) {
			Log.e(TAG, tag + " == " + msg);
		}
	}

	/**
	 * Send a AppConfig.IS_PRINT_LOG log message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, Object msg) {
		if (EZTConfig.IS_PRINT_LOG) {
			Log.d(TAG, tag + " == " + msg);
		}
	}

}