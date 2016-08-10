package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.utils.ResolveResponse;

/**
 * @title 获取城市相关数据解析
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class CityManager {

	/**
	 * 解析区域列表返回信息
	 */
	public static ArrayList<City> getAreaList(String t) {
		ArrayList<City> citys = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				citys = new ArrayList<City>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					City city = new City();
					if (!ject.isNull("countyname"))
						city.setAreaName(ject.getString("countyname"));
					if (!ject.isNull("id"))
						city.setAreaId(ject.getString("id"));
					citys.add(city);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					citys = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			citys = null;
		}
		return citys;
	}

	/**
	 * 解析城市列表返回信息
	 */
	public static ArrayList<City> getCityList(String t) {
		ArrayList<City> citys = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				citys = new ArrayList<City>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					City city = new City();
					if (!ject.isNull("cityname"))
						city.setCityName(ject.getString("cityname"));
					if (!ject.isNull("id"))
						city.setCityId(ject.getString("id"));
					citys.add(city);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					citys = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			citys = null;
		}
		return citys;
	}
}
