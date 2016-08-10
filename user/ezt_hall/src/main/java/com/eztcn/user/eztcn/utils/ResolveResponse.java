package com.eztcn.user.eztcn.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @title 解析返回的JSON对象数据
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */

public class ResolveResponse {
	public static Object resolveData(String resStr) throws JSONException {
		if (resStr != null && !resStr.equals("")) {
			JSONObject json = new JSONObject(resStr);
			if (json != null) {
				if (!json.isNull("flag")) {
					boolean flag = json.getBoolean("flag");
					if (flag) {// 成功
						if (!json.isNull("data")) {
							Object data = json.get("data");
							if (data == null || data.toString().equals("")) {
								return flag;// 无数据
							} else {
								return data;// 返回数据
							}

						} else {
							return false;// 失败
						}
					} else {
						Logger.i("msg", flag);
						return flag;// 返回具体业务逻辑
					}
				} else
					Logger.e("", "msg为null!");
				return false;// 失败
			}
		} else {
			Logger.i("", "resStr为null!");
			return false;// 失败

		}
		throw new JSONException("");
	}
}
