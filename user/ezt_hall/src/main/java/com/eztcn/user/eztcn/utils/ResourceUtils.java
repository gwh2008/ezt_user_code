package com.eztcn.user.eztcn.utils;

import com.eztcn.user.R.drawable;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.TypedValue;

/**
 * @title 资源转换工具类
 * @describe 根据id获取资源文件的值
 * @author ezt
 * @created 2015年2月3日
 */

public class ResourceUtils {

	private static TypedValue mTmpValue = new TypedValue();

	private ResourceUtils() {
	}

	public static int getXmlDef(Context context, int id) {
		synchronized (mTmpValue) {
			TypedValue value = mTmpValue;
			context.getResources().getValue(id, value, true);
			return (int) TypedValue.complexToFloat(value.data);
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 设置圆角背景
	 * 
	 * @param color
	 * @return
	 */
	public static ShapeDrawable setBackgroundColor(int color,int radius) {
		float[] outerR = new float[] { radius, radius, radius, radius, radius,
				radius, radius, radius };
		RoundRectShape shape = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(shape);
		drawable.getPaint().setColor(color);
		new CornerPathEffect(8);
		return drawable;
	}
}
