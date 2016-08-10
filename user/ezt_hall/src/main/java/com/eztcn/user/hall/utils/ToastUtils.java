package com.eztcn.user.hall.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 显示工具类
 * 
 * 
 */
public class ToastUtils {

	/**
	 * 弹出短时间toast
	 * 
	 * @param str
	 *            显示的内容
	 */
	public static void shortToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 弹出长时间的toast
	 * 
	 * @param str
	 *            显示的内容
	 */
	public static void longToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	
	private static Toast toast;
	
	/**
	 * 显示单例的吐司，能连续快速弹的吐司
	 * @param text
	 */
	public static void singleShortToast(Context context ,String text){
		if(toast==null){
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}
	/**
	 * 显示单例的长吐司
	 * @param text
	 */
	public static void singleLongToast(Context context ,String text){
		if(toast==null){
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}
}
