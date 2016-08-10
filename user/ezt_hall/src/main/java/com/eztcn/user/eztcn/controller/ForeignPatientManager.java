package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.ForeignPatient_Service;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.bean.Tumour;

/**
 * @title 外患管理
 * @describe
 * @author ezt
 * @created 2015年3月25日
 */
public class ForeignPatientManager {

	/**
	 * 解析肿瘤医院详情
	 */
	public static Map<String, Object> getTumourIntro(String t) {

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
				map.put("msg", json.getString("msg"));
			} else {
				if (!json.isNull("data")) {
					JSONObject json1 = json.getJSONObject("data");
					if (!json1.isNull("id")) {
						map.put("id", json1.getString("id"));
					}

					if (!json1.isNull("body")) {
						map.put("body", json1.getString("body"));
					}

					if (!json1.isNull("litpic")) {
						map.put("pic", json1.getString("litpic"));
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析快速求助
	 */
	public static Map<String, Object> quickHelp(String t) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			map.put("msg", json.getString("detailMsg"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析肿瘤服务列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getProjectList(String t) {

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
					JSONArray jArray = json.getJSONArray("data");
					ArrayList<Tumour> list = new ArrayList<Tumour>();
					for (int i = 0; i < jArray.length(); i++) {
						Tumour tumour = new Tumour();
						JSONObject ject = jArray.getJSONObject(i);
						if (!ject.isNull("id"))
							tumour.setId(ject.getString("id"));
						if (!ject.isNull("dptName"))
							tumour.setStrName(ject.getString("dptName"));
						list.add(tumour);
					}
					map.put("data", list);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析肿瘤服务套餐列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getTrapackage_list(String t) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			} else {
				if (!json.isNull("data")) {
					JSONArray jArray = json.getJSONArray("data");
					ArrayList<ForeignPatient_Service> list = new ArrayList<ForeignPatient_Service>();
					for (int i = 0; i < jArray.length(); i++) {
						ForeignPatient_Service f = new ForeignPatient_Service();
						JSONObject ject = jArray.getJSONObject(i);
						if (!ject.isNull("id"))
							f.setId(ject.getString("id"));
						if (!ject.isNull("epSavetime")) {
							int min = ject.getInt("epSavetime");
							int day = min / 60 / 24;
							f.setTime(day + "天");
						}

						if (!ject.isNull("epPreAmount"))
							f.setPrice(ject.getString("epPreAmount"));

						if (!ject.isNull("epName"))
							f.setTitle(ject.getString("epName"));

						if (!ject.isNull("epContain"))
							f.setIntro(ject.getString("epContain"));
						list.add(f);
					}
					map.put("data", list);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 服务套餐详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getTrapackageDetail(String t) {

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
					if (!json.isNull("data")) {
						JSONArray jArray = json.getJSONArray("data");
						ArrayList<ForeignPatient_Service> list = new ArrayList<ForeignPatient_Service>();
						for (int i = 0; i < jArray.length(); i++) {
							ForeignPatient_Service f = new ForeignPatient_Service();
							JSONObject ject = jArray.getJSONObject(i);
							if (!ject.isNull("id"))
								f.setId(ject.getString("id"));

							if (!ject.isNull("epSavetime")) {
								int min = ject.getInt("epSavetime");
								int day = min / 60 / 24;
								f.setTime(day + "天");
							}

							if (!ject.isNull("epPreAmount"))
								f.setPrice(ject.getString("epPreAmount"));

							if (!ject.isNull("epContain"))
								f.setTitle(ject.getString("epContain"));

							if (!ject.isNull("remark"))
								f.setIntro(ject.getString("remark"));
							list.add(f);
						}
						map.put("data", list);
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 康复病历列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getRecoveryCaseList(String t) {

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
				map.put("msg", json.getString("msg"));
			} else {
				if (!json.isNull("data")) {
					JSONArray jArray = json.getJSONArray("data");
					ArrayList<Information> list = new ArrayList<Information>();
					for (int i = 0; i < jArray.length(); i++) {
						Information info = new Information();
						JSONObject ject = jArray.getJSONObject(i);
						if (!ject.isNull("id"))
							info.setId(ject.getString("id"));
						if (!ject.isNull("litpic"))
							info.setImgUrl(ject.getString("litpic"));
						if (!ject.isNull("title"))
							info.setInfoTitle(ject.getString("title"));
						if (!ject.isNull("description"))
							info.setInfoDescription(ject
									.getString("description"));
						list.add(info);
					}
					map.put("data", list);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析患友交流信息
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getPatientGroup(String t) {

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
				map.put("msg", json.getString("msg"));
			} else {
				if (!json.isNull("data")) {
					JSONObject json1 = json.getJSONObject("data");
					if (!json1.isNull("id")) {
						map.put("id", json1.getString("id"));
					}
					if (!json1.isNull("body")) {
						map.put("body", json1.getString("body"));
					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 添加购物车
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> addTraShoppingCart(String t) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			map.put("msg", json.getString("msg"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

}
