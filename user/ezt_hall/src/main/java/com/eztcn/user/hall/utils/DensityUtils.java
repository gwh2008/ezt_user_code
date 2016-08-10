package com.eztcn.user.hall.utils;

import android.content.Context;

/**
 * 这个是根据屏幕密度做dp和px之间转换的工具类
 * @author 蒙
 */
public class DensityUtils {

	/**
	 * 根据手机的分辨率 dp(相对单位) 转成 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率  px(像素) 的单位 转成dp(相对单位)

	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}