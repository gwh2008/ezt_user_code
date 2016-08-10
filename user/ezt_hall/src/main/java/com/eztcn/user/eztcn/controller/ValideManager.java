package com.eztcn.user.eztcn.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class ValideManager {
	/**
	 * 解析服务器返回信息
	 */
	public static Map<String, Object> parseHttpJson(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			} else {
				if (!json.isNull("data")) {
					map.put("data", json.getString("data"));
				}
				if(!json.isNull("detailMsg")){
					map.put("msg", json.getString("detailMsg"));
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

}
