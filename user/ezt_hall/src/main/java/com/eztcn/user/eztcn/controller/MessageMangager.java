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

import com.eztcn.user.eztcn.bean.message.Message;

/**
 * @author Liu Gang
 * 
 *         2016年4月6日 上午9:28:11 消息推送管理
 */
public class MessageMangager {
	/**
	 * 告诉后台发短信接口
	 * @param t
	 * @return
	 */
	public static Map<String,Object> sendSelf(String t){
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
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * {"data":"您申请的预约挂号已经成功，请注意预约时间，避免错过时间。","detailMsg":"成功","flag":true,"msg"
	 * :"成功","number":"2000"}
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> traOrderInfo(String t) {
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
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * 
	 * @param t
	 * @return
	 * 
	 *         {"data":{"combo":{},"footer":[],"outher":{},"page":1,"rows":[{
	 *         "content":"您申请的预约挂号已经成功，请注意预约时间，避免错过时间。","createtime":
	 *         "2016-04-06 16:21:39"
	 *         ,"id":10,"infoSysType":4,"title":"预约挂号、当日挂号、大牌名医"
	 *         ,"type":16,"userId"
	 *         :4052151},{"content":"您申请的预约挂号已经成功，请注意预约时间，避免错过时间。"
	 *         ,"createtime":"2016-04-06 16:21:55"
	 *         ,"id":11,"infoSysType":4,"title"
	 *         :"预约挂号、当日挂号、大牌名医","type":16,"userId"
	 *         :4052151},{"content":"您申请的预约挂号已经成功，请注意预约时间，避免错过时间。"
	 *         ,"createtime":"2016-04-06 16:23:38"
	 *         ,"id":12,"infoSysType":4,"title"
	 *         :"预约挂号、当日挂号、大牌名医","type":16,"userId"
	 *         :4052151},{"content":"您申请的预约挂号已经成功，请注意预约时间，避免错过时间。"
	 *         ,"createtime":"2016-04-06 16:24:07"
	 *         ,"id":13,"infoSysType":4,"title"
	 *         :"预约挂号、当日挂号、大牌名医","type":16,"userId"
	 *         :4052151}],"rowsPerPage":15,"total"
	 *         :4},"detailMsg":"成功","flag":true,"msg":"成功","number":"2000"}
	 * 
	 */
	public static Map<String, Object> getOrderInfo(String t) {
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
					String dataStr = json.getString("data");
					JSONObject jsonMsgList = new JSONObject(dataStr);
					if (!jsonMsgList.isNull("page")) {
						map.put("cPage", jsonMsgList.getString("page"));
					}
					if (!jsonMsgList.isNull("rowsPerPage")) {
						map.put("rowsPerPage", jsonMsgList.getString("rowsPerPage"));
					}
					
					if (!jsonMsgList.isNull("total")) {
						map.put("total", jsonMsgList.getString("total"));
					}
					
					
					if (!jsonMsgList.isNull("page")) {
						map.put("cPage", jsonMsgList.getString("page"));
					}
					
					// 未做 combo footer other处理
					if (!jsonMsgList.isNull("rows")) {
						JSONArray msgListArray = jsonMsgList.getJSONArray("rows");
						List<Message> msgList = new ArrayList<Message>();
						for (int i = 0; i < msgListArray.length(); i++) {
							JSONObject msgObj = (JSONObject) msgListArray
									.get(i);
							Message msg = new Message();
							if (!msgObj.isNull("content")) {
								String contetStr = msgObj.getString("content");
								msg.setContentStr(contetStr);
							}
							if (!msgObj.isNull("createtime")) {
								String createtimeStr = msgObj
										.getString("createtime");
								msg.setDateStr(createtimeStr.substring(0,
										createtimeStr.indexOf(" ")));
							}

							if (!msgObj.isNull("id")) {
								int id = msgObj.getInt("id");
								msg.setId(id);
							}
							if (!msgObj.isNull("infoSysType")) {
								int infoSysType = msgObj.getInt("infoSysType");
								msg.setInfoSysType(infoSysType);
							}

							if (!msgObj.isNull("title")) {
								String titleStr = msgObj.getString("title");
								msg.setTitleStr(titleStr);
							}

							if (!msgObj.isNull("type")) {
								int type = msgObj.getInt("type");
								msg.setType(type);
							}

							if (!msgObj.isNull("userId")) {
								String userId = msgObj.getString("userId");
								msg.setUserId(userId);
							}
							msgList.add(msg);

						}
						map.put("msgList", msgList);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
