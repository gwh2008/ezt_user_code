package com.eztcn.user.eztcn.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.OrderBed;

/**
 * 预约病床
 * 
 * @author LX
 * @date2016-3-31 @time下午4:23:56
 */
public class OrderBedManager {

	// 将数据进行解析传递。
	public static Map<String, Object> gainData(String dataStr) {

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

			if (!dataJson.isNull("number")) {
				map.put("number", dataJson.getInt("number"));
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * {"remark":null,"deposit":null,"sysDoctorName":null,"patientSpecialNeed":
	 * "","hospitalId":"282","patientStatus":"","patientOrderType":"预约病床",
	 * "patientCardName"
	 * :"身份证","id":327,"patientId":"2195492","sysDoctorNo":null,
	 * "patientName":"刘刚"
	 * ,"patientSex":"男","userId":"4461521","chargeDepositTime"
	 * :null,"depositStatusTime"
	 * :null,"bedPrice":13,"sysDoctorMobile":null,"picUrls"
	 * :["F9D5046DC0477D01480EC48E212AF490.jpg"
	 * ],"orderTime":"2016-04-14 11:43:57"
	 * ,"timeDis":null,"patientCardNum":"110101200503017036"
	 * ,"hospitalAddress":"天津市河西区解放南路406号"
	 * ,"auditingStatus":7,"hospitalPic":"","patientPhone"
	 * :"15620316231","orderNumber"
	 * :"2016041400000808","hospitalName":"天津市天津医院","deptId":"27338"}
	 * 
	 * @param jsonDataStr
	 * @return
	 */
	public static Map<String, Object> gainOrderBedStatus(String jsonDataStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		OrderBed orderBed = new OrderBed();
		try {
			JSONObject json = new JSONObject(jsonDataStr);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (flag) {
				json = json.getJSONObject("data");

				if (!json.isNull("remark")) {
					orderBed.setRemark(json.getString("remark"));
				}
				if (!json.isNull("deposit")) {
					orderBed.setDeposit(json.getString("deposit"));
				}
				if (!json.isNull("sysDoctorName")) {
					orderBed.setSysDoctorName(json.getString("sysDoctorName"));
				}
				if (!json.isNull("patientSpecialNeed")) {
					orderBed.setPatientSpecialNeed(json
							.getString("patientSpecialNeed"));
				}
				if (!json.isNull("hospitalId")) {
					orderBed.setHospitalId(json.getString("hospitalId"));
				}
				if (!json.isNull("patientStatus")) {
					orderBed.setPatientStatus(json.getString("patientStatus"));
				}
				if (!json.isNull("hospitalId")) {
					orderBed.setHospitalId(json.getString("hospitalId"));
				}
				if (!json.isNull("patientStatus")) {
					orderBed.setPatientStatus(json.getString("patientStatus"));
				}
				if (!json.isNull("patientOrderType")) {
					orderBed.setPatientOrderType(json
							.getString("patientOrderType"));
				}
				if (!json.isNull("patientCardName")) {
					orderBed.setPatientCardName(json
							.getString("patientCardName"));
				}
				if (!json.isNull("id")) {
					orderBed.setId(json.getInt("id"));
				}

				if (!json.isNull("patientId")) {
					orderBed.setPatientId(json.getString("patientId"));
				}

				if (!json.isNull("sysDoctorNo")) {
					orderBed.setSysDoctorNo(json.getString("sysDoctorNo"));
				}

				if (!json.isNull("patientName")) {
					orderBed.setPatientName(json.getString("patientName"));
				}
				if (!json.isNull("patientSex")) {
					orderBed.setPatientSex(json.getString("patientSex"));
				}
				if (!json.isNull("userId")) {
					orderBed.setUserId(json.getString("userId"));
				}
				if (!json.isNull("chargeDepositTime")) {
					orderBed.setChargeDepositTime(json
							.getString("chargeDepositTime"));
				}

				if (!json.isNull("depositStatusTime")) {
					orderBed.setDepositStatusTime(json
							.getString("depositStatusTime"));
				}
				if (!json.isNull("bedPrice")) {
					orderBed.setBedPrice(json.getDouble("bedPrice"));
				}
				if (!json.isNull("sysDoctorMobile")) {
					orderBed.setSysDoctorMobile(json
							.getString("sysDoctorMobile"));
				}

				JSONArray picArray = json.getJSONArray("picUrls");
				String picUrils[] = new String[picArray.length()];
				for (int i = 0; i < picArray.length(); i++) {
					picUrils[i] = picArray.getString(i);
				}
				orderBed.setPicUrls(picUrils);

				if (!json.isNull("orderTime")) {
					orderBed.setOrderTime(json.getString("orderTime"));
				}

				if (!json.isNull("timeDis")) {
					orderBed.setTimeDis(json.getString("timeDis"));
				}

				if (!json.isNull("patientCardNum")) {
					orderBed.setPatientCardName(json
							.getString("patientCardNum"));
				}

				if (!json.isNull("hospitalAddress")) {
					orderBed.setHospitalAddress(json
							.getString("hospitalAddress"));
				}

				if (!json.isNull("auditingStatus")) {
					orderBed.setAuditingStatus(json.getInt("auditingStatus"));
				}

				if (!json.isNull("hospitalPic")) {
					orderBed.setHospitalPic(json.getString("hospitalPic"));
				}
				if (!json.isNull("patientPhone")) {
					orderBed.setPatientPhone(json.getString("patientPhone"));
				}

				if (!json.isNull("orderNumber")) {
					orderBed.setOrderNumber(json.getString("orderNumber"));
				}

				if (!json.isNull("hospitalName")) {
					orderBed.setHospitalName(json.getString("hospitalName"));
				}

				if (!json.isNull("deptId")) {
					orderBed.setDeptId(json.getString("deptId"));
				}
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		map.put("data", orderBed);
		return map;
	}

	/**
	 * {"data":{"orderId":133,"orderNumber":"2016040400000035"},"detailMsg":
	 * "创建订单成功","flag":true,"msg":"数据保存成功!","number":"2001"}
	 * 
	 * 
	 * 1.2. 提交预约病床订单
	 * 
	 * @param dataStr
	 * @return
	 */
	public static Map<String, Object> createBedOrder(String jsonDataStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(jsonDataStr);
			if (!dataJson.isNull("flag")) {
				if (dataJson.getBoolean("flag")) {
					map.put("flag", true);

					String dataStr = dataJson.getString("data");
					JSONObject json = new JSONObject(dataStr);
					if (!json.isNull("orderId")) {
						map.put("orderId", json.getString("orderId"));
					}
					if (!json.isNull("orderNumber")) {
						map.put("orderNumber", json.getString("orderNumber"));
					}
					String detailMsg = dataJson.getString("detailMsg");
					map.put("msg", detailMsg);
					String numberStr = dataJson.getString("number");// 状态码
					map.put("number", numberStr);
				} else {
					map.put("flag", false);
					if (StringUtils.isNotBlank("detailMsg")) {
						String detailMsg = dataJson.getString("detailMsg");
						map.put("msg", detailMsg);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 1.5. 根据约病床订单id 获取支付宝支付串（分首次支付和二次支付）
	 * 
	 * {"data":
	 * "partner=\"2088011992275770\"&seller_id=\"eztcn@sina.com\"&out_trade_no=\"2016040400000037\"&subject=\"病床挂号费订单支付\"&body=\"病床挂号费订单支付\"&total_fee=\"0.00\"&notify_url=\"http://mc.eztcn.com.cn/mc/api/v2/trading/reservationBed/AlipayNotify.do?rpNum=2016040400000037\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&return_url=\"m.alipay.com\"&sign=\"jp1aI2b6eHeTgeapeHQ%2BgHbr1nzksdDAQL7W%2BZjLLto%2F6eFUhS1sjH1k%2FcQkV3rLi%2BRMMJoGfj%2FU0cjJnY1YH8BXgvFr9wAyvVnkm7oheZGhMjWZl7%2Fl12%2FJ0VcVg3NFnk1kXO5QzjDHoWCfy8IX7jGSN4Kj98oL0vkcJnBD6Ng%3D\"&sign_type=\"RSA\""
	 * ,"detailMsg":"数据保存成功!","flag":true,"msg":"数据保存成功!","number":"2001"}
	 * 
	 * @param jsonDataStr
	 * @return
	 */
	public static Map<String, Object> getAlipayStringById(String jsonDataStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(jsonDataStr);
			if (!dataJson.isNull("flag")) {
				if (dataJson.getBoolean("flag")) {
					map.put("flag", true);
					String dataStr = dataJson.getString("data");
					map.put("data", dataStr);// 支付宝所用字符串
					String detailMsg = dataJson.getString("detailMsg");
					map.put("msg", detailMsg);
					String numberStr = dataJson.getString("number");
					map.put("number", numberStr);
				} else {
					map.put("flag", false);
					if (StringUtils.isNotBlank("detailMsg")) {
						String detailMsg = dataJson.getString("detailMsg");
						map.put("msg", detailMsg);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
