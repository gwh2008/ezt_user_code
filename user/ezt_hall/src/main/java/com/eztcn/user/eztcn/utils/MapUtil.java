package com.eztcn.user.eztcn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.Html;
import android.text.Spanned;

import com.eztcn.user.eztcn.BaseApplication;

/**
 * @title 地图工具类
 * @describe
 * @author ezt
 * @created 2014年12月17日
 */
public class MapUtil {

	private static final double EARTH_RADIUS = 6378.137;// 地球半径

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append(space);
		}
		return result.toString();
	}

	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * long类型时间格式化
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";

	/**
	 * 根据经纬度计算路程
	 */
	public static double calculatePath(double lat2, double lng2) {
		double s;
		double lon = BaseApplication.getInstance().lon;// 个人位置
		double lat = BaseApplication.getInstance().lat;// 个人位置
		if (lon == 0.0d || lat == 0.0d) {
			s = 1.0d;
			return s;
		}
		double radLat1 = rad(lat);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lon) - rad(lng2);

		s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
