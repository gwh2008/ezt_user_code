package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.ChargeCurrency;
import com.eztcn.user.eztcn.bean.Coupons;

/**
 * @title 支付相关数据解析
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class PayManager {

	/**
	 * 提交支付订单
	 */
	public static Map<String, Object> submitPayOrder(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				map.put("data", json.getString("data"));
			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取支付金额
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getPayMoney(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				map.put("data", json.getString("data"));
			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取充值记录
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getPayRecord(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				map.put("data", json.getString("data"));
			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取健康币记录
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getCurrencyRecord(String t) {
		List<ChargeCurrency> list = new ArrayList<ChargeCurrency>();
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			String data = json.getString("data");
			json = new JSONObject(data);
			JSONArray arrays = json.getJSONArray("rows");
			ChargeCurrency currency;
			JSONObject dataJson;
			for (int i = 0; i < arrays.length(); i++) {
				json = arrays.getJSONObject(i);
				currency = new ChargeCurrency();
				if (!json.isNull("traCurrencyRecordBean")) {
					dataJson = json.getJSONObject("traCurrencyRecordBean");
					if (!dataJson.isNull("id")) {
						currency.setId(dataJson.getInt("id"));
					}
					if (!dataJson.isNull("crEztCurrency")) {
						currency.setEztCurrency(dataJson
								.getDouble("crEztCurrency"));
					}
					if (!dataJson.isNull("createTime")) {
						currency.setCreateTime(dataJson.getString("createTime"));
					}
					if (!dataJson.isNull("sourceType")) {
						currency.setSourceType(dataJson.getInt("sourceType"));
					}
					if (!dataJson.isNull("isIncomeExpenditure")) {
						currency.setIsIncomeExpenditure(dataJson
								.getInt("isIncomeExpenditure"));
					}
					list.add(currency);
				}
			}
			map.put("list", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取健康币余额
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getCurrencyMoney(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			if (!json.isNull("data")) {
				JSONObject data = json.getJSONObject("data");
				if (!data.isNull("uaEztCurrencyRemain")) {
					map.put("remain", data.getDouble("uaEztCurrencyRemain"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析可拨打分钟数
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> parseCallMinute(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			if (json.isNull("data")) {
				return map;
			}
			JSONObject data = json.getJSONObject("data");
			if (!data.isNull("currency")) {
				map.put("currency", data.getDouble("currency"));
			}
			if (!data.isNull("times")) {
				map.put("times", data.getInt("times"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析优惠券列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getCouponList(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			if (json.isNull("data")) {
				return map;
			}
			JSONArray jsonArray = json.getJSONArray("data");
			ArrayList<Coupons> cList = new ArrayList<Coupons>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject josn = jsonArray.getJSONObject(i);
				Coupons coupon = new Coupons();

				if (!josn.isNull("id")) {
					coupon.setId(josn.getString("id"));
				}
				if (!josn.isNull("ccCardNo")) {
					coupon.setCardNo(josn.getString("ccCardNo"));
				}
				if (!josn.isNull("ccFaceValue")) {
					coupon.setMoney(josn.getString("ccFaceValue"));
				}
				if (!josn.isNull("status")) {
					coupon.setState(josn.getInt("status"));
				}
				if (!josn.isNull("ccDesc")) {
					coupon.setTitle(josn.getString("ccDesc"));
				}
				if (!josn.isNull("ccDeadlineEnd")) {
					String endDate=(josn.getString("ccDeadlineEnd").split(" "))[0];
					coupon.setEndDate(endDate); 
				}
				cList.add(coupon);
			}
			map.put("data", cList);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

}
