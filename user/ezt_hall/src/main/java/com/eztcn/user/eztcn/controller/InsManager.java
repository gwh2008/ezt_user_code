/**
 * 
 */
package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.InsItemDetial;
import com.eztcn.user.eztcn.bean.LInsItem;

/**
 * @author Liu Gang
 * 
 *         2015年11月25日 上午10:42:04
 * 
 */
public class InsManager {
	/**
	 * 获取大检验项目列表
	 * @param t
	 * @return
	 */
	public Map<String, Object> getPatientMessage(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = false;
		final String MSGSTR_ERROR = "服务器异常,请您稍后再试";
		String msgStr = "";
		try {
			if (null == t) {
				flag = false;
				msgStr = MSGSTR_ERROR;
			} else {
				JSONObject json = new JSONObject(t);
				if (!json.isNull("flag")) {
					// 如果返回里面有flag
					flag = json.getBoolean("flag");
					if (flag) {
						// 解析到正常数据
						if (!json.isNull("data")) {
							JSONArray insbArray = (JSONArray) json.get("data");
							List<LInsItem> bInsItems = new ArrayList<LInsItem>();
							for (int i = 0; i < insbArray.length(); i++) {
								JSONObject insb = insbArray.getJSONObject(i);
								LInsItem largeInsItem = new LInsItem();
								if (!insb.isNull("inpatient_no")) {
									largeInsItem.setPatientIdNo(insb
											.getString("inpatient_no"));
								}
								if (!insb.isNull("name")) {
									largeInsItem.setbItemName(insb
											.getString("name"));
								}
								if (!insb.isNull("social_no")) {
									largeInsItem.setPatientIdNo(insb
											.getString("social_no"));
								}
								if (!insb.isNull("home_tel")) {
									largeInsItem.setPatientIdNo(insb
											.getString("home_tel"));
								}
								if (!insb.isNull("patient_type")) {
									largeInsItem.setState(insb
											.getString("patient_type"));
								}
								if (!insb.isNull("confirm_time")) {
									largeInsItem.setTimeStr(insb
											.getString("confirm_time"));
								}
								if (!insb.isNull("samp_id")) {
									largeInsItem.setbItemId(insb
											.getString("samp_id"));
								}
								if (!insb.isNull("item_name")) {
									largeInsItem.setbItemName(insb
											.getString("item_name"));
								}
								bInsItems.add(largeInsItem);
							}
							map.put("data", bInsItems);
						}
					} else {
						// 解析到错误信息
						if (!json.isNull("detailMsg"))
							msgStr = json.getString("detailMsg");
					}
				} else {
					// 返回时候没有flag
					flag = false;
					msgStr = MSGSTR_ERROR;
				}

			}
			map.put("msg", msgStr);
			map.put("flag", flag);

		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 获取小检验项目列表（某大检验项目详情）
	 * @param t
	 * @return
	 */
	public Map<String, Object> getSampleAndSampleDetailMessage(String t) {
		Map<String, Object> map = new HashMap<String, Object>();

		boolean flag = false;
		final String MSGSTR_ERROR = "服务器异常,请您稍后再试";
		String msgStr = "";
		try {
			if (null == t) {
				flag = false;
				msgStr = MSGSTR_ERROR;
			} else {
				JSONObject json = new JSONObject(t);
				if (!json.isNull("flag")) {
					// 如果返回里面有flag
					flag = json.getBoolean("flag");
					if (flag) {
						// 解析到正常数据
						if (!json.isNull("data")) {
							JSONArray insDetialArray = (JSONArray) json
									.get("data");
							List<InsItemDetial> insItemDetials = new ArrayList<InsItemDetial>();
							for (int i = 0; i < insDetialArray.length(); i++) {
								JSONObject insDetialJson = insDetialArray
										.getJSONObject(i);
								InsItemDetial insItemDetial = new InsItemDetial();
								if (!insDetialJson.isNull("confirm_time")) {
									insItemDetial.setConfirmTime(insDetialJson
											.getString("confirm_time"));
								}
								if (!insDetialJson.isNull("item_name")) {
									insItemDetial.setItemName(insDetialJson
											.getString("item_name"));
								}
								if (!insDetialJson.isNull("samp_id")) {
									insItemDetial.setSampId(insDetialJson
											.getString("samp_id"));
								}
								if (!insDetialJson.isNull("samp_sn")) {
									insItemDetial.setSampSn(insDetialJson
											.getString("samp_sn"));
								}
								if (!insDetialJson.isNull("detail_name")) {
									insItemDetial.setDetailName(insDetialJson
											.getString("detail_name"));
								}
								if (!insDetialJson.isNull("standard_value")) {
									insItemDetial
											.setStandardValue(insDetialJson
													.getString("standard_value"));
								}
								if (!insDetialJson.isNull("samp_result")) {
									insItemDetial.setSampResult(insDetialJson
											.getString("samp_result"));
								}
								insItemDetials.add(insItemDetial);
							}
							map.put("data", insItemDetials);
						}
					} else {
						// 解析到错误信息
						if (!json.isNull("detailMsg"))
							msgStr = json.getString("detailMsg");
					}
				} else {
					// 返回时候没有flag
					flag = false;
					msgStr = MSGSTR_ERROR;
				}

			}
			map.put("msg", msgStr);
			map.put("flag", flag);

		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return map;
	}
}
