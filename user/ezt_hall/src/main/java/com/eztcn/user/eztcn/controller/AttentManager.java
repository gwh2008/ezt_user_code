package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.AttentionDoctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.utils.ResolveResponse;

/**
 * @title 关注管理
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class AttentManager {

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
			}
			map.put("flag", flag);
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			} else {
				if (!json.isNull("data")) {
					JSONObject json1 = json.getJSONObject("data");
					if (!json1.isNull("id")) {
						map.put("id", json1.getString("id"));
					}
				}
				if (!json.isNull("detailMsg")) {
					map.put("msg", json.getString("detailMsg"));	
				}
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取关注医生列表
	 * 
	 * @param t
	 * @return
	 */
	public static ArrayList<AttentionDoctor> getAttentDoc(String t) {
		ArrayList<AttentionDoctor> docs = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据

			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				if (!ject.isNull("rows")) {
					JSONArray jArray = ject.getJSONArray("rows");
					docs = new ArrayList<AttentionDoctor>();
					JSONObject ject1;
					JSONObject ject2;
					for (int i = 0; i < jArray.length(); i++) {
						ject1 = jArray.getJSONObject(i).getJSONObject(
								"eztHosDeptDocBean");
						AttentionDoctor doc = new AttentionDoctor();

						if (!ject1.isNull("edName"))
							doc.setDocName(ject1.getString("edName"));

						if (!ject1.isNull("edPic"))
							doc.setDocPhoto(ject1.getString("edPic"));

						if (!ject1.isNull("edLevel"))
							doc.setDoctorLevel(ject1.getInt("edLevel"));

						if (!ject1.isNull("ehName"))
							doc.setHosName(ject1.getString("ehName"));

						if (!ject1.isNull("dptame"))
							doc.setDeptName(ject1.getString("dptame"));
						
						if(!ject1.isNull("ehDockingStatus")){
							doc.setEhDockingStatus(ject1.getInt("ehDockingStatus"));
						}
						
						if(!ject1.isNull("ehDockingStr")){
							doc.setEhDockingStr(ject1.getString("ehDockingStr"));
						}
						
						if (!ject1.isNull("docId")) {
							doc.setDocId(ject1.getString("docId"));
						} else {
							continue;
						}
						if (!ject1.isNull("deptdocId"))
							doc.setDeptDocId(ject1.getString("deptdocId"));
						if (!ject1.isNull("deptId"))
							doc.setDeptId(ject1.getString("deptId"));
						
						
						ject2 = jArray.getJSONObject(i).getJSONObject(
								"eztCollectBean");
						if (!ject2.isNull("id"))
							doc.setId(ject2.getInt("id"));
						
						docs.add(doc);
					}
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					docs = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			docs = null;
		}
		return docs;
	}

	/**
	 * 获取收藏医院列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> parseAttentHos(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Hospital> list = new ArrayList<Hospital>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
			}
			if (!flag || json.isNull("data")) {
				map.put("msg", json.getString("detailMsg"));
				return map;
			}
			JSONObject dataJson = json.getJSONObject("data");
			JSONArray rows = dataJson.getJSONArray("rows");
			Hospital hospital;
			for (int i = 0; i < rows.length(); i++) {
				hospital = new Hospital();
				json = rows.getJSONObject(i).getJSONObject("eztCollectBean");
				if (!json.isNull("id")) {
					hospital.setCollectId(json.getString("id"));
				}
				if (!json.isNull("ecDatetime")) {
					hospital.setCreateDate(json.getString("ecDatetime"));
				}
				dataJson = rows.getJSONObject(i).getJSONObject(
						"eztHospitalBean");
				if (!dataJson.isNull("ehName")) {
					hospital.sethName(dataJson.getString("ehName"));
				}
				if (!dataJson.isNull("ehAddress")) {
					hospital.sethAddress(dataJson.getString("ehAddress"));
				}
				if (!dataJson.isNull("id")) {
					hospital.setId(dataJson.getInt("id"));
				}
				if (!dataJson.isNull("ehLevel")) {
					hospital.setHosLevel(dataJson.getString("ehLevel"));
				}
				if (!dataJson.isNull("ehTel")) {
					hospital.sethTel(dataJson.getString("ehTel"));
				}
				if (!dataJson.isNull("lat")) {
					hospital.setLat(dataJson.getDouble("lat"));
				}
				if (!dataJson.isNull("lng")) {
					hospital.setLon(dataJson.getDouble("lng"));
				}
				if (!dataJson.isNull("remark")) {
					hospital.sethIntro(dataJson.getString("remark"));
				}
				
				if(!dataJson.isNull("ehDockingStatus")){//2015-12-21 医院对接中 对接提示语
					hospital.setEhDockingStatus(dataJson.getInt("ehDockingStatus"));
				}
				
				if(!dataJson.isNull("ehDockingStr")){//2015-12-21 医院对接中 对接提示语
					hospital.setEhDockingStr(dataJson.getString("ehDockingStr"));
				}
				
				list.add(hospital);
			}
			map.put("list", list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
