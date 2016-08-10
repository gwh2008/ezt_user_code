/**
 * 
 */
package com.eztcn.user.eztcn.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author Liu Gang
 * 
 *         2015年8月5日 下午4:09:28
 * 
 */
public class ScreenUtils {
	public static DisplayMetrics gainDM(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm;
	}

}
