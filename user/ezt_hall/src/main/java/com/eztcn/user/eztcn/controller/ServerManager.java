package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 轻陪诊解析
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class ServerManager {

	/**
	 * 解析激活卡
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getActivation(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			dataJson = new JSONObject(t);
			if (!dataJson.isNull("flag"))
				map.put("flag", dataJson.getBoolean("flag"));

			if (!dataJson.isNull("detailMsg"))
				map.put("msg", dataJson.getString("detailMsg"));
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取手表卡
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getHealthcardList(String t) {
		// 手表服务套餐id
		String id =EZTConfig.CardId;
		Map<String, Object> map = null;
		HealthCard h = null;
		try {
			map = new HashMap<String, Object>();
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject j = (JSONObject) object;
				JSONArray jArray = null;
				if (!j.isNull("rows")) {
					jArray = j.getJSONArray("rows");
				} else {
					return map;
				}
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);

					if (!ject.isNull("packageId")) {
						//套餐id
						String packId=ject.getString("packageId");
					
						if (id.equals(packId)) {// 手表卡与服务卡能对应
							h = new HealthCard();
							
							if (!ject.isNull("id")){
								h.setId(ject.getString("id"));
							}
						

							if (!ject.isNull("hcCardNo"))
								h.setCardNum(ject.getString("hcCardNo"));

							if (!ject.isNull("hcStatus"))// 使用状态0未使用，1已使用
								h.setState(ject.getInt("hcStatus"));

							if (!ject.isNull("hcActivateDt"))// 激活时间
								h.setActiveTime(ject.getString("hcActivateDt"));

							if (!ject.isNull("traPackageepName"))// 卡名称
								h.setCardName(ject
										.getString("traPackageepName"));

							if (!ject.isNull("hcEndValidity"))// 截止有效期
								h.setHcEndValidity(ject
										.getString("hcEndValidity"));

							if (!ject.isNull("hcBeginServiceValidity"))// 有效期开始
								h.setHcBeginServiceValidity(ject
										.getString("hcBeginServiceValidity"));
							if (!ject.isNull("traPackageepPic")) {// 图片
								h.setCardCover(ject
										.getString("traPackageepPic"));
							}
							// 塞入手表卡后直接跳出循环
							break;
						}
					}

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		map.put("healthCard", h);
		return map;
	}

	/**
	 * 健康卡详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getHealthcardDetail(String t) {
		Map<String, Object> map = null;
		JSONObject dataJson;
		try {
			map = new HashMap<String, Object>();
			JSONObject ject = new JSONObject(t);
			if (!ject.isNull("data")) {
				dataJson = ject.getJSONObject("data");
			} else {
				return null;
			}

			if (!dataJson.isNull("healthCarUseBeans")) {
				JSONArray itemArray = dataJson
						.getJSONArray("healthCarUseBeans");
				ArrayList<LightAccompanying> list = new ArrayList<LightAccompanying>();
				for (int i = 0; i < itemArray.length(); i++) {
					LightAccompanying l = new LightAccompanying();
					JSONObject itemJect = itemArray.getJSONObject(i);

					if (!itemJect.isNull("itemId")) {
						l.setItemId(itemJect.getString("itemId"));
					}

					if (!itemJect.isNull("id")) {
						l.setId(itemJect.getString("id"));
					}

					if (!itemJect.isNull("itemtiName")) {// 服务标题
						l.setItemName(itemJect.getString("itemtiName"));
					}

					if (!itemJect.isNull("itemremark")) {// 服务描述
						l.setRemark(itemJect.getString("itemremark"));
					}

					if (!itemJect.isNull("userRemainNums")) {// 剩余次数
						l.setRemainNums(itemJect.getInt("userRemainNums"));
					}

					if (!itemJect.isNull("itemStatus")) {
						l.setItemStatus(itemJect.getInt("itemStatus"));// 使用状态
					}

					if (!itemJect.isNull("packageremark")) {
						map.put("remark", itemJect.getString("packageremark"));
					}

					list.add(l);
				}
				map.put("itemList", list);
			}

			if (!dataJson.isNull("hosBeans")) {
				JSONArray hosArray = dataJson.getJSONArray("hosBeans");
				ArrayList<Hospital> list = new ArrayList<Hospital>();
				int CountyId = 0;
				for (int i = 0; i < hosArray.length(); i++) {
					Hospital hos = new Hospital();
					JSONObject hosJect = hosArray.getJSONObject(i);

					if (!hosJect.isNull("eztHospitalehCounty")) {
						Hospital h = new Hospital();
						int id = hosJect.getInt("eztHospitalehCounty");
						if (id != CountyId) {
							if (!hosJect.isNull("eztHospitalehCountyName"))
								h.sethAddress(hosJect
										.getString("eztHospitalehCountyName"));

							CountyId = id;
							list.add(h);
						}

					}

					if (!hosJect.isNull("eztHospitalehName")) {
						hos.sethName(hosJect.getString("eztHospitalehName"));
					}
					if (!hosJect.isNull("eztHospitalehLevelName")) {
						hos.setHosLevel(hosJect
								.getString("eztHospitalehLevelName"));
					}

					list.add(hos);
				}
				map.put("hosList", list);
			}

			if (!dataJson.isNull("healthCarBean")) {
				JSONObject packageJect = dataJson
						.getJSONObject("healthCarBean");

				if (!packageJect.isNull("hcBeginServiceValidity"))// 有效期开始
				{
					String str = StringUtil.dealWithDate(packageJect
							.getString("hcBeginServiceValidity"));
					map.put("startTime", str);
				} else {
					map.put("startTime", "");
				}

				if (!packageJect.isNull("hcEndValidity"))// 有效期结束
				{
					String str = StringUtil.dealWithDate(packageJect
							.getString("hcEndValidity"));
					map.put("endTime", str);

				} else {
					map.put("endTime", "");
				}

				if (!packageJect.isNull("hcCardNo"))// 卡号
					map.put("cardNo", packageJect.getString("hcCardNo"));

				if (!packageJect.isNull("hcStatus"))// 状态
					map.put("state", packageJect.getInt("hcStatus"));

				if (!dataJson.isNull("packageBeans")) {
					JSONObject pkgJson = dataJson.getJSONObject("packageBeans");
					if (!pkgJson.isNull("remark")) {
						map.put("remark", pkgJson.getString("remark"));
					} else {
						map.put("remark", "");
					}
					if (!pkgJson.isNull("process")) {
						map.put("process", pkgJson.getString("process"));
					} else {
						map.put("process", "");
					}
					if (!pkgJson.isNull("notice")) {
						map.put("notice", pkgJson.getString("notice"));
					} else {
						map.put("notice", "");
					}
					if (!pkgJson.isNull("epPic")) {
						map.put("epPic", pkgJson.getString("epPic"));
					} else {
						map.put("epPic", "");
					}
				}
			}

			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
