package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.bean.LightAccompanying;

/**
 * @title 医护帮处理类
 * @describe
 * @author ezt
 * @created 2015年6月17日
 */
public class ENurseHelpManager {

	/**
	 * 解析套餐卡列表
	 */
	public static Map<String, Object> parseMealCardList(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<HealthCard> list = new ArrayList<HealthCard>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
			}
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag || json.isNull("data")) {
				map.put("list", list);
				return map;
			}
			JSONArray data = json.getJSONArray("data");
			HealthCard card;
			for (int i = 0; i < data.length(); i++) {
				json = data.getJSONObject(i);
				card = new HealthCard();
				if (!json.isNull("id")) {
					card.setId(json.getString("id"));
				}
				if (!json.isNull("epAmount")) {// 价格
					card.setCardPrice(json.getDouble("epAmount"));
				}
				if (!json.isNull("epName")) {// 名称
					card.setCardName(json.getString("epName"));
				}
				if (!json.isNull("epPic")) {
					card.setCardCover(json.getString("epPic"));
				}
				list.add(card);
			}
			map.put("list", list);
		} catch (JSONException e) {
			map.put("flag", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 解析套餐卡详情
	 */
	public static Map<String, Object> parseMealCardDetail(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
			}
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag || json.isNull("data")) {
				return map;
			}
			JSONObject data = json.getJSONObject("data");
			json = data.getJSONObject("packageBean");
			LightAccompanying card = new LightAccompanying();

				if (!json.isNull("id")) {
					card.setId(json.getString("id"));
				}
//				if (!json.isNull("traItemid")) {// itemId
//					card.setItemId(json.getString("traItemid"));
//				}
//				if (!json.isNull("子项1")) {// 名称
//					card.setItemName(json.getString("子项1"));
//				}
				if (!json.isNull("remark")) {// 服务项
					card.setRemark(json.getString("remark"));
				}
				if (!json.isNull("notice")) {// 服务须知
					card.setNotice(json.getString("notice"));
				}
				if (!json.isNull("process")) {// 服务流程
					card.setProcess(json.getString("process"));
				}
			map.put("card", card);
		} catch (JSONException e) {
			map.put("flag", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
}
