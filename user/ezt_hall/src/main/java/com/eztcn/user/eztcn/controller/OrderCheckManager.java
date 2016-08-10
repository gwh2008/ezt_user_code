/**
 * 
 */
package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrder;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrderItem;
import com.eztcn.user.eztcn.bean.ordercheck.Guider;

/**
 * @author Liu Gang
 * 
 *         2016年3月16日 下午5:17:00 预约检查管理类
 */
public class OrderCheckManager {
	/**
	 * { "data": [ { "address": "天津市河北区江都路24号", "baseUse": "10", "checkId":
	 * "12", "checkName": "核磁共振", "forceUse": "-1", "hosId": "2", "hosName":
	 * "天津市第三医院", "price": "100", "remark": "", "type": "影像检查" } ], "flag": true
	 * }
	 * 
	 * @param data
	 * @return
	 */
	public static Map<String, Object> getOrderChecks(String dataStr) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(dataStr);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				JSONArray jsonArray = json.getJSONArray("data");
				List<CheckOrderItem> checkOrderList = new ArrayList<CheckOrderItem>();
				for (int i = 0; i < jsonArray.length(); i++) {
					CheckOrderItem checkOrderItem = new CheckOrderItem();
					JSONObject jsonItem = (JSONObject) jsonArray.get(i);
					if (!jsonItem.isNull("address")) {
						checkOrderItem.setAddr(jsonItem.getString("address"));
					}
					if (!jsonItem.isNull("baseUse")) {
						String baseUse = jsonItem.getString("baseUse");
						if (!map.containsKey("baseCost"))
							map.put("baseCost", baseUse);
						checkOrderItem.setBaseCostStr(baseUse);
					}
					if (!jsonItem.isNull("checkId")) {
						checkOrderItem
								.setCheckId(jsonItem.getString("checkId"));
					}
					if (!jsonItem.isNull("checkName")) {
						checkOrderItem.setNameStr(jsonItem
								.getString("checkName"));
					}
					if (!jsonItem.isNull("forceUse")) {

					}
					if (!jsonItem.isNull("hosId")) {
						checkOrderItem.setHosId(jsonItem.getString("hosId"));
					}
					if (!jsonItem.isNull("hosName")) {
						checkOrderItem
								.setHosName(jsonItem.getString("hosName"));
					}
					if (!jsonItem.isNull("price")) {
						checkOrderItem.setCostStr(jsonItem.getString("price"));
					}
					if (!jsonItem.isNull("remark")) {// 检查项处理
						String careStrs = jsonItem.getString("remark");
						if (StringUtils.isNotEmpty(careStrs)) {
							List<String> careList = new ArrayList<String>();
							if (careStrs.contains(",")) {
								String[] carStrs = careStrs.split(",");
								for (int j = 0; j < carStrs.length; j++) {
									String careStr = carStrs[j];
									if (StringUtils.isNotEmpty(careStr)
											&& !careList.contains(careStr))
										careList.add(careStr);
								}
							} else {
								careList.add(careStrs);
							}
							checkOrderItem.setCaresStr(careList);
						}

					}
					if (!jsonItem.isNull("type")) {
						checkOrderItem.setOrderTypeStr(jsonItem
								.getString("type"));
					}
					checkOrderItem.setChecked(false);
					checkOrderList.add(checkOrderItem);
				}
				map.put("orderCheckList", checkOrderList);
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
	 * 获取预约预约单列表。
	 * 
	 * { "data":[ { "auditingStatus":1,
	 * "certificatesNumber":"120104198811244717", "certificatesType":"身份证",
	 * "guysNumebr":"", "healthSecretary":"", "hosId":"2",
	 * "hospitalName":"天津市第三医院", "id":82, "orderBusiness":"预约检查",
	 * "orderNumber":"2016031900000042", "orderTime":"2016-03-19 14:50:44",
	 * "patientId":"2029817", "patientName":"刘刚", "phone":"15620316231",
	 * "remark":"", "sex":"男", "specialRemark":"还钱", "timeDis":"1",
	 * "userId":"4334426" } ], "detailMsg":"数据获取成功!", "flag":true,
	 * "msg":"数据获取成功!", "number":"2003" }
	 * 
	 * @param dataStr
	 * @return
	 */

	public static Map<String, Object> getOrderCheckList(String dataStr) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(dataStr);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				JSONArray array = json.getJSONArray("data");
				List<CheckOrder> list = new ArrayList<CheckOrder>();
				for (int i = 0; i < array.length(); i++) {
					CheckOrder checkOrder = new CheckOrder();
					JSONObject obj = (JSONObject) array.get(i);
					checkOrder.setOrderState(obj.getInt("auditingStatus"));
					checkOrder.setOrderStr(obj.getString("specialRemark"));
					checkOrder.setTimeDis(obj.getString("timeDis"));

					EztUser eztUser = new EztUser();
					eztUser.setUserId(Integer.parseInt(obj.getString("userId")));
					eztUser.setSex(obj.getString("sex").equals("男") ? 0 : 1);
					checkOrder.setEztUser(eztUser);

					checkOrder.setPatientName(obj.getString("patientName"));
					checkOrder.setPatientMobile(obj.getString("phone"));
					checkOrder.setPatientId(obj.getString("patientId"));
					checkOrder.setOrderDateTime(obj.getString("orderTime"));
					List<String> carStr = new ArrayList<String>();
					String remarks[] = obj.getString("remark").split(",");
					for (int j = 0; j < remarks.length; j++) {
						String string = remarks[j];

						if (StringUtils.isNotEmpty(string)
								&& !carStr.contains(string))
							carStr.add(string);
					}
					checkOrder.setCaresStr(carStr);
					Guider guider = new Guider();
					if (!obj.isNull("guysNumebr"))
						guider.setGuysNumer(obj.getString("guysNumebr"));
					if (!obj.isNull("healthSecretary"))
						guider.setHealthSecretary(obj
								.getString("healthSecretary"));
					checkOrder.setGuider(guider);

					Hospital hospital = new Hospital();
					hospital.sethName(obj.getString("hospitalName"));
					hospital.setId(Integer.parseInt(obj.getString("hosId")));
					checkOrder.setHospital(hospital);

					checkOrder.setId(obj.getString("id"));
					checkOrder.setCertificatesNumber(obj
							.getString("certificatesNumber"));
					checkOrder.setCertificatesType(obj
							.getString("certificatesType"));
					checkOrder.setOrderBusiness(obj.getString("orderBusiness"));
					checkOrder.setOrderNum(obj.getString("orderNumber"));
					list.add(checkOrder);
				}
				map.put("checkOrderList", list);
			}
			if (!json.isNull("number")) {
				int number = json.getInt("number");
				map.put("number", number);
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
	 * 
	 {"data":{"itemlist":[{"address":"门诊楼三楼406","appointmentTime":null,
	 * "baseUse"
	 * :"0.01","checkId":"35","checkName":"血常规","checkType":"分泌物检查","flag"
	 * :2,"forceUse"
	 * :"-1","guysName":null,"guysPhone":null,"hosId":"2","hosName":
	 * "天津市第三医院","price"
	 * :"0.01","rejection":"","remark":""}],"ordercheck":{"auditingStatus"
	 * :1,"certificatesNumber"
	 * :"120104198811244717","certificatesType":"身份证","guysNumebr"
	 * :"","healthSecretary"
	 * :"","hosId":"2","hospitalName":"天津市第三医院","id":106,"orderBusiness"
	 * :"预约检查","orderNumber"
	 * :"2016032100000123","orderTime":"2016-03-21 10:11:04"
	 * ,"patientId":"2029817"
	 * ,"patientName":"刘刚","phone":"15620316231","remark":""
	 * ,"sex":"男","specialRemark"
	 * :"","userId":"4334426"}},"detailMsg":"成功","flag"
	 * :true,"msg":"成功","number":"2000"}
	 * 
	 * ©2014 Json.cn All right reserved. 京ICP备15025187号-1
	 * 
	 * @param dataStr
	 * @return
	 */
	public static Map<String, Object> getOrderCheckDetails(String dataStr) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(dataStr);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				JSONObject jsonStr = (JSONObject) json.get("data");
				if (!jsonStr.isNull("itemlist")) {
					JSONArray itemList = jsonStr.getJSONArray("itemlist");
					List<CheckOrderItem> checkOrderItems = new ArrayList<CheckOrderItem>();
					for (int i = 0; i < itemList.length(); i++) {
						JSONObject itemJson = (JSONObject) itemList.get(i);
						CheckOrderItem item = new CheckOrderItem();

						// "address":"天津市河北区江都路24号",
						item.setAddr(itemJson.getString("address"));
						// "baseUse":"0.01",
						item.setBaseCostStr(itemJson.getString("baseUse"));
						// "appointmentTime":null,
						if (!itemJson.isNull("appointmentTime"))
							item.setTimeStr(itemJson
									.getString("appointmentTime"));
						// "checkId":"33",
						item.setCheckId(itemJson.getString("checkId"));
						// "checkName":"验血",
						item.setNameStr(itemJson.getString("checkName"));
						// "checkType":"分泌物检查",
						item.setOrderTypeStr(itemJson.getString("checkType"));
						// "flag":2,
						// "forceUse":"-1",
						Guider guider = new Guider();
						// "guysPhone":null,
						if (!itemJson.isNull("guysPhone"))
							guider.setGuysMobile(itemJson
									.getString("guysPhone"));
						// "guysName":null,
						if (!itemJson.isNull("guysName"))
							guider.setGuysName(itemJson.getString("guysName"));
						item.setGuider(guider);

						Hospital hospital = new Hospital();
						// "hosId":"2",
						hospital.setId(Integer.parseInt(itemJson
								.getString("hosId")));
						// "hosName":"天津市第三医院",
						hospital.sethName(itemJson.getString("hosName"));
						item.setHospital(hospital);
						// "price":"0.01",
						item.setCostStr(itemJson.getString("price"));
						List<String> caresStr = new ArrayList<String>();
						String remarkStr = itemJson.getString("remark");
						String tempStr[] = remarkStr.split(",");

						for (int j = 0; j < tempStr.length; j++) {
							String string = tempStr[j];
							if (StringUtils.isNotEmpty(string)
									&& !caresStr.contains(string))
								caresStr.add(string);
						}
						item.setCaresStr(caresStr);

						checkOrderItems.add(item);
					}
					map.put("checkOrderItemList", checkOrderItems);
				}

				if (!jsonStr.isNull("ordercheck")) {

					JSONObject obj = jsonStr.getJSONObject("ordercheck");

					CheckOrder checkOrder = new CheckOrder();
					// "auditingStatus":7,
					checkOrder.setOrderState(obj.getInt("auditingStatus"));
					// "certificatesType":"身份证",
					checkOrder.setCertificatesType(obj
							.getString("certificatesType"));

					Guider guider = new Guider();
					// "guysNumebr":"",
					if (!obj.isNull("guysNumebr"))
						guider.setGuysNumer(obj.getString("guysNumebr"));
					// healthSecretary
					if (!obj.isNull("healthSecretary"))
						guider.setHealthSecretary(obj
								.getString("healthSecretary"));
					checkOrder.setGuider(guider);

					Hospital hospital = new Hospital();
					// "hospitalName":"天津市第三医院"
					hospital.sethName(obj.getString("hospitalName"));
					// "hosId":"2",
					hospital.setId(Integer.parseInt(obj.getString("hosId")));
					checkOrder.setHospital(hospital);
					// "id":102,
					checkOrder.setId(obj.getString("id"));
					// "orderBusiness":"预约检查",
					checkOrder.setOrderBusiness(obj.getString("orderBusiness"));
					// "certificatesNumber":"120104198811244717",
					checkOrder.setCertificatesNumber(obj
							.getString("certificatesNumber"));
					// "orderNumber":"2016032100000119",
					checkOrder.setOrderNum(obj.getString("orderNumber"));
					// "orderTime":"2016-03-21 09:07:37",
					checkOrder.setTimeDis(obj.getString("orderTime"));
					// "patientId":"2029817",
					checkOrder.setPatientId(obj.getString("patientId"));
					// "patientName":"刘刚",
					checkOrder.setPatientName(obj.getString("patientName"));
					// "phone":"15620316231",
					checkOrder.setPatientMobile(obj.getString("phone"));

					List<String> carStr = new ArrayList<String>();
					String remarks[] = obj.getString("remark").split(",");
					for (int j = 0; j < remarks.length; j++) {
						String string = remarks[j];
						if (StringUtils.isNotEmpty(string)
								&& !carStr.contains(string))
							carStr.add(string);
					}
					checkOrder.setCaresStr(carStr);

					EztUser eztUser = new EztUser();
					// "userId":"4334426",
					eztUser.setUserId(Integer.parseInt(obj.getString("userId")));

					// "sex":"男",
					eztUser.setSex(obj.getString("sex").equals("男") ? 0 : 1);
					checkOrder.setEztUser(eztUser);

					// checkOrder.setOrderDateTime(obj.getString("orderTime"));
					// "remark":"",
					// "specialRemark":"11",
					checkOrder.setOrderStr(obj.getString("specialRemark"));

					map.put("checkOrder", checkOrder);

				}
			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;

	}

	// 获取数据
	public static Map<String, Object> getData(String dataStr) {

		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(dataStr);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				Object obj = json.get("data");
				map.put("data", obj);
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
	 * {"data":
	 * "partner=\"2088011992275770\"&seller_id=\"eztcn@sina.com\"&out_trade_no=\"2016031700000031\"&subject=\"预约检查订单支付\"&body=\"预约检查订单支付\"&total_fee=\"10\"¬ify_url=\"http://mc.eztcn.com.cn/mc/api/v2/trading/checkItem/AlipayNotify.do?rpNum=2016031700000031\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&return_url=\"m.alipay.com\"&sign=\"gVZ7sxjRvtUYQMdTCcY%2FIt7m75a2SG%2BsTwfwO%2Bt0qGnPsyalT12iALNJVccx6kSLTOK1ArHJ0lJVvYNolCjiQUTPwDgvEJ6QaZ0Q5xPK07aLOnLlkOnICSe68iyE1BhcyfkEFiaqT9KamWzl%2FrtodiLkXAJo3ds08zeJzYzrSxo%3D\"&sign_type=\"RSA\""
	 * ,"detailMsg":"创建支付成功","flag":true,"msg":"数据保存成功!","number":"2001"}
	 * 
	 * @param data
	 * @return
	 */
	public static Map<String, Object> commitCheckOrder(String dataStr) {

		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			dataJson = new JSONObject(dataStr);
			boolean isFlag = false;
			if (!dataJson.isNull("flag")) {
				isFlag = dataJson.getBoolean("flag");
				map.put("flag", isFlag);
			}
			if (isFlag) {// 成功
				if (!dataJson.isNull("data")) {
					map.put("data", dataJson.getString("data"));

					// partner="2088011992275770"&seller_id="eztcn@sina.com"&out_trade_no="2016041100000146"&subject="预约检查订单支付"&body="预约检查订单支付"&total_fee="73.20"&notify_url="http://mc.eztcn.com.cn/mc/api/v2/trading/checkItem/AlipayNotify.do?rpNum=2016041100000146"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&return_url="m.alipay.com"&sign="ZpMQ4SZbaiGgl%2BRU9k%2B6b2m7OvIeZ8cAJKvCFI9FwJ8Rm77g3c3KSap3rkjlkPrd1e5bL3%2BkEKqOWFdOheyRJue%2B63NtNvOvvkXAqMQOfS34sBYk1kSefqsT3DwdcTFSLojAyF1agerYj6N%2BGPTZzDK%2B3rWqWbYDf9fBBQhkG8M%3D"&sign_type="RSA"
					String dataStr1 = dataJson.getString("data");
					String[] dataStrs = dataStr1.split("&");
					for (int i = 0; i < dataStrs.length; i++) {
						String string = dataStrs[i];
						if (string.startsWith("out_trade_no")) {
							String orderNoStr = String.valueOf(string
									.subSequence(string.indexOf("\"") + 1,
											string.lastIndexOf("\"")));
							map.put("orderNo", orderNoStr);
							break;
						}
					}
				}
				// map.put("orderNo", dataJson.get("orderNo"));

			} else {
				if (!dataJson.isNull("detailMsg"))
					map.put("msg", dataJson.getString("detailMsg"));
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Map<String, Object> gainCheckOrderList(String dataStr) {

		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			dataJson = new JSONObject(dataStr);
			boolean isFlag = false;
			if (!dataJson.isNull("flag")) {
				isFlag = dataJson.getBoolean("flag");
				map.put("flag", isFlag);
			}
			if (isFlag) {// 成功
				if (!dataJson.isNull("data")) {
					map.put("data", dataJson.getString("data"));
				}

			} else {
				if (!dataJson.isNull("detailMsg"))
					map.put("msg", dataJson.getString("detailMsg"));
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Map<String, Object> gainCheckOrderDetial(String dataStr) {

		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			dataJson = new JSONObject(dataStr);
			boolean isFlag = false;
			if (!dataJson.isNull("flag")) {
				isFlag = dataJson.getBoolean("flag");
				map.put("flag", isFlag);
			}
			if (isFlag) {// 成功
				if (!dataJson.isNull("data")) {
					map.put("data", dataJson.getString("data"));
				}

			} else {
				if (!dataJson.isNull("detailMsg"))
					map.put("msg", dataJson.getString("detailMsg"));
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}
}
