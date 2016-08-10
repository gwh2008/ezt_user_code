package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.bean.Symptom;
import com.eztcn.user.eztcn.utils.ResolveResponse;

/**
 * @title 获取症状查询数据解析
 * @describe
 * @author ezt
 * @created 2014年12月24日
 */
public class SymptomQueryManager {

	/**
	 * 解析疾病列表
	 * 
	 * @param t
	 * @param flag
	 *            0为热门疾病，1为获取疾病列表
	 * @return
	 */
	public static ArrayList<Diseases> getDiseasesList(String t, int flag) {
		ArrayList<Diseases> dList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				dList = new ArrayList<Diseases>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Diseases diseases = new Diseases();

					if (flag == 0) {
						if (!ject.isNull("name"))
							diseases.setdName(ject.getString("name"));
					} else {
						if (!ject.isNull("Name"))
							diseases.setdName(ject.getString("Name"));
					}

					if (!ject.isNull("id"))
						diseases.setId(ject.getInt("id"));
					dList.add(diseases);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					dList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			dList = null;
		}
		return dList;
	}

	/**
	 * 获取症状列表
	 */
	public static ArrayList<Symptom> getSymptomListOfPart(String t) {
		ArrayList<Symptom> sList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				sList = new ArrayList<Symptom>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Symptom symptom = new Symptom();
					if (!ject.isNull("illName"))
						symptom.setStrName(ject.getString("illName"));
					if (!ject.isNull("id"))
						symptom.setId(ject.getString("id"));
					sList.add(symptom);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					sList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			sList = null;
		}
		return sList;
	}

	/**
	 * 解析症状详情
	 */
	public static Symptom getSymptomDetailsOfId(String t) {
		Symptom symptm = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				symptm = new Symptom();

				JSONObject ject1 = null;
				if (!ject.isNull("regSymptoms")) {
					ject1 = ject.getJSONObject("regSymptoms");
					if (!ject1.isNull("subordinateDept"))
						symptm.setStrDept(ject1.getString("subordinateDept"));
					if (!ject1.isNull("introduction"))
						symptm.setIntro(ject1.getString("introduction"));
					if (!ject1.isNull("introduction"))
						symptm.setIntro(ject1.getString("introduction"));
					if (!ject1.isNull("id"))
						symptm.setId(ject1.getString("id"));
				}

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					symptm = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			symptm = null;
		}
		return symptm;
	}

	/**
	 * 根据科室获取相关症状列表
	 */
	public static ArrayList<Symptom> getSymptomListOfDept(String t) {
		ArrayList<Symptom> sList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				sList = new ArrayList<Symptom>();
				int l=6;
				if(jArray.length()<=6){
					l=jArray.length();
				}
				
				for (int i = 0; i < l; i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Symptom symptom = new Symptom();
					if (!ject.isNull("Name"))
						symptom.setStrName(ject.getString("Name"));
					if (!ject.isNull("id"))
						symptom.setId(ject.getString("id"));
					sList.add(symptom);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					sList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			sList = null;
		}
		return sList;
	}

	/**
	 * 根据症状id获取相关疾病列表
	 */
	public static ArrayList<Diseases> getDiseasesListOfId(String t) {
		ArrayList<Diseases> dList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				dList = new ArrayList<Diseases>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
//					Diseases d = new Diseases();
//					if (!ject.isNull("name"))
//						d.setdName(ject.getString("name"));
//					if (!ject.isNull("id"))
//						d.setId(ject.getInt("id"));
//					dList.add(d);
					
					//2015-12-29 修改相关疾病列表没有名字的情况
					Diseases d = new Diseases();
					if (!ject.isNull("name")){
						if(StringUtils.isEmpty(ject.getString("name"))){
							continue;
						}
						d.setdName(ject.getString("name"));
					}
					if (!ject.isNull("id"))
						d.setId(ject.getInt("id"));
					dList.add(d);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					dList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			dList = null;
		}
		return dList;
	}

	/**
	 * 解析疾病详情
	 */
	public static Diseases getDiseasesDetailsOfId(String t) {
		Diseases d = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				d = new Diseases();
				JSONObject ject = null;
				for (int i = 0; i < jArray.length(); i++) {
					ject = jArray.getJSONObject(i);
				}
				if(null==ject){//2015-12-17 处理空指针数据
					d=null;
				}else//2015-12-17 处理空指针数据
					{
				if (!ject.isNull("diagnose"))// 诊断与鉴别
					d.setDiagnose(ject.getString("diagnose"));
				if (!ject.isNull("prevent"))// 预防
					d.setPrevent(ject.getString("prevent"));
				if (!ject.isNull("nursing"))// 保健
					d.setNursing(ject.getString("nursing"));
				if (!ject.isNull("complication"))// 简介
					d.setIntro(ject.getString("complication"));
				if (!ject.isNull("guahaoDept"))// 科室
					d.setDept(ject.getString("guahaoDept"));
				if (!ject.isNull("id"))
					d.setId(ject.getInt("id"));
					}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					d = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			d = null;
		}
		return d;
	}

	/**
	 * 解析推荐医院科室
	 */
	public static ArrayList<Dept> getHosGeneralList(String t) {
		ArrayList<Dept> deptList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				JSONArray jsonArray = null;
				if (!ject.isNull("rows")) {
					jsonArray = ject.getJSONArray("rows");
					deptList = new ArrayList<Dept>();
					int jlength=0;
					if(jsonArray.length()>30){
						jlength=30;
					}else{
						jlength=jsonArray.length();
					}
					for (int i = 0; i < jlength; i++) {
						Dept dept = new Dept();
						JSONObject ject1 = jsonArray.getJSONObject(i);
						if (!ject1.isNull("ehName"))// 医院名称
							dept.setdHosName(ject1.getString("ehName"));
						if (!ject1.isNull("hospitalId"))// 医院id
							dept.setdHosId(ject1.getString("hospitalId"));
						if (!ject1.isNull("dptName"))// 科室名称
							dept.setdName(ject1.getString("dptName"));
						if (!ject1.isNull("dptId"))// 科室id
							dept.setId(ject1.getInt("dptId"));

						deptList.add(dept);
					}
				}

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					deptList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			deptList = null;
		}
		return deptList;
	}

	
	/**
	 * 获取疾病查询列表
	 * @param t
	 * @return
	 */
	public static ArrayList<Diseases> getDiseasesListOfLetter(String t) {
		ArrayList<Diseases> dList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				dList = new ArrayList<Diseases>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Diseases d = new Diseases();
					if (!ject.isNull("illName"))
						d.setdName(ject.getString("illName"));
					if (!ject.isNull("id"))
						d.setId(ject.getInt("id"));
					dList.add(d);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					dList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			dList = null;
		}
		return dList;
	}
}
