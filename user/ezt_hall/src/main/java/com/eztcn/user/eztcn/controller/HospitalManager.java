package com.eztcn.user.eztcn.controller;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Function;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 获取医院
 * @describe 解析服务器返回信息
 * @author ezt
 * @created 2014年12月18日
 */
public class HospitalManager {
	public static final String[] functionStrs = new String[] { "医院简介", "科室列表",
			"医生列表", "医院新闻", "医患互动", "健康教育", "预约挂号", "当日挂号", "大牌名医", "交通路线",
			"专病门诊", "智能导诊", "诊前咨询", "预约检查", "预约病床", "预约药品", "到院签到", "诊间支付",
			"报告查询", "排队查询" };
	public static final int[] functionIds = new int[] { -1, -1, -1,
			R.drawable.hh_yyxw, R.drawable.hh_yhhd, R.drawable.hh_jkjy,
			R.drawable.hh_yygh, R.drawable.hh_drgh, R.drawable.hh_dpmy,
			R.drawable.hh_jtlx, R.drawable.hh_zbmz, R.drawable.hh_zndz,
			R.drawable.hh_zqzx, R.drawable.hh_yyjc, R.drawable.hh_yybc,
			R.drawable.hh_yyyp, R.drawable.hh_dyqd, R.drawable.hh_zjzf,
			R.drawable.hh_bgcx, R.drawable.hh_pdcx };

	/**
	 * 
	 * {"data":[{"1":"1"}, 医院简介 当日挂号 {"2":"1"}, 科室列表 {"3":"1"}, 医生列表
	 * 
	 * {"7":"1"}, 预约挂号 {"4":"0"}, 医院新闻 {"11":"0"}, 专病门诊 {"5":"0"}, 医患互动
	 * {"6":"0"}, 健康教育
	 * {"8":"1"}],"detailMsg":"获取医院配置成功！","flag":true,"msg":"成功",
	 * "number":"2000"}
	 * 
	 * @param t
	 */
	public static Map getHosConfig(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			if (flag) {

				JSONArray dataArray = null;
				if (json.has("data")) {
					dataArray = json.getJSONArray("data");
					List<Function> functionList = new ArrayList<Function>();
					for (int i = 0; i < dataArray.length(); i++) {
						JSONObject tempJson = (JSONObject) dataArray.get(i);
						Iterator<String> it = tempJson.keys();
						while (it.hasNext()) {
							String keyStr = it.next();
							String valueStr = tempJson.getString(keyStr);
							Function function = new Function();
							function.setName(functionStrs[Integer
									.parseInt(keyStr) - 1]);
							function.setDrawableId(functionIds[Integer
									.parseInt(keyStr) - 1]);
							function.setIsOpen("1".equals(valueStr) ? 1 : 0);
							if (!keyStr.equals("1") && !keyStr.equals("2")
									&& !keyStr.equals("3"))
								functionList.add(function);
						}

					}
					// Collections.sort(functionList);
					map.put("functionList", functionList);
				}
			}
			String detailMsgStr = json.getString("detailMsg");
			map.put("flag", flag);
			map.put("msg", detailMsgStr);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析医院列表
	 */
	public static ArrayList<Hospital> getHosList(String t) {
		ArrayList<Hospital> hosList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				hosList = new ArrayList<Hospital>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Hospital hos = new Hospital();
					if (!ject.isNull("ehName"))
						hos.sethName(ject.getString("ehName"));
					if (!ject.isNull("id"))
						hos.setId(ject.getInt("id"));
					if (!ject.isNull("ehAddress"))
						hos.sethAddress(ject.getString("ehAddress"));
					if (!ject.isNull("ehLevel"))
						hos.setHosLevel(ject.getString("ehLevel"));
					if (!ject.isNull("ehLogo"))
						hos.sethLogo(ject.getString("ehLogo"));
					if (!ject.isNull("ehTel"))
						hos.sethTel(ject.getString("ehTel"));
					if (!ject.isNull("lat"))
						hos.setLat(ject.getDouble("lat"));
					if (!ject.isNull("lng"))
						hos.setLon(ject.getDouble("lng"));
					if (!ject.isNull("ehDockingStatus")) {// 2015-12-27 医院对接中
						hos.setEhDockingStatus(ject.getInt("ehDockingStatus"));
					}
					if (!ject.isNull("ehDockingStr")) {// 2015-12-27 医院对接中 对接提示语
						hos.setEhDockingStr(ject.getString("ehDockingStr"));
					}

//					if (hos.getId() != 6 && hos.getId() != 116) {// 中心妇产现场预约医院/天津市口腔医院儿科普号（不显示）
//						hosList.add(hos);
//					}
                    if (hos.getId() != 6 && hos.getId() != 116&&hos.getId()!=310) {// 中心妇产现场预约医院/南院区:中医一附属国医堂现场号（不显示）
                        hosList.add(hos);
                    }

				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					hosList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			hosList = null;
		}
		return hosList;
	}

	/**
	 * 解析医院详情
	 */
	public static Map<String, Object> getHosDetail(String t) {
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
			Hospital hospital = new Hospital();
			JSONObject dataJson = json.getJSONObject("data");
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
			// 2015-12-18医院对接中
			if (!dataJson.isNull("ehDockingStatus")) {
				hospital.setEhDockingStatus(dataJson.getInt("ehDockingStatus"));
			}
			// 2015-12-18医院对接中
			if (!dataJson.isNull("ehDockingStr")) {
				hospital.setEhDockingStr(dataJson.getString("ehDockingStr"));
			}
			map.put("hospital", hospital);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析大科室分类列表
	 */
	public static ArrayList<Dept> getBigDeptList(String t) {
		ArrayList<Dept> deptList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				deptList = new ArrayList<Dept>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Dept deptType = new Dept();
					if (!ject.isNull("dcName"))
						deptType.setdName(ject.getString("dcName"));
					if (!ject.isNull("id"))
						deptType.setId(ject.getInt("id"));
					deptList.add(deptType);
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
	 * 解析二级科室列表
	 */
	public static ArrayList<Dept> getDeptList(String t) {
		ArrayList<Dept> deptList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				deptList = new ArrayList<Dept>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Dept dept = new Dept();
					if (!ject.isNull("dcName"))
						dept.setdName(ject.getString("dcName"));
					if (!ject.isNull("id"))
						dept.setId(ject.getInt("id"));
					deptList.add(dept);
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
	 * 
	 * @param t
	 * 
	 * 
	 *            {"bedPrice":0.01,"eztDeptCatedcName":null,"eztDeptcreateby":
	 *            null,"eztDeptcreatedate":null,"eztDeptdptCateId":331,
	 *            "eztDeptdptCode"
	 *            :"","eztDeptdptFax":"","eztDeptdptHid":null,"eztDeptdptId"
	 *            :null,"eztDeptdptLogo":"","eztDeptdptName":"糖尿病科",
	 *            "eztDeptdptOrder"
	 *            :1,"eztDeptdptProfile":"","eztDeptdptStatus":0
	 *            ,"eztDeptdptTel":
	 *            "","eztDeptdptWebsite":"","eztDeptdptZipcode":
	 *            "","eztDeptisCharacteristic"
	 *            :0,"eztDeptlasteditby":"2015-01-28 10:35:46.0"
	 *            ,"eztDeptlasteditdate"
	 *            :"2015-01-28 10:35:46","eztDeptstatus":0,
	 *            "eztDeptsynStatus":1,"eztHospitalehName"
	 *            :null,"id":39}],"detailMsg"
	 *            :"成功","flag":true,"msg":"成功","number":"2000"}
	 * 
	 * 
	 *            private Integer id;//主键 private Integer
	 *            eztDeptdptCateId;//科室类别ID private String eztDeptdptName;//科室名称
	 *            private Integer eztDeptdptHid;//医院ID private String
	 *            eztDeptdptTel;//电话 private String eztDeptdptFax;//传真 private
	 *            String eztDeptdptZipcode;//邮政编码 private String
	 *            eztDeptdptWebsite;//科室网址 private String
	 *            eztDeptdptLogo;//科室LOGO private String eztDeptdptCode;//科室代码
	 *            private Integer eztDeptdptOrder;//科室序号 private Integer
	 *            eztDeptdptStatus;//科室状态 private String
	 *            eztDeptdptProfile;//科室简介 private Integer
	 *            eztDeptisCharacteristic;//是否门特科室 private Integer
	 *            eztDeptdptId;//科室ID(移动数据同步用) private String
	 *            eztDeptcreateby;//新增人 private Date eztDeptcreatedate;//新增时间
	 *            private String eztDeptlasteditby;//最后修改人 private Date
	 *            eztDeptlasteditdate;//最后修改时间 private Integer
	 *            eztDeptsynStatus;//同步状态 private Integer eztDeptstatus;//数据状态
	 *            private String eztDeptCatedcName;//科室类别名称 private BigDecimal
	 *            bedPrice;//病床挂号费
	 * 
	 * 
	 * 
	 * @return
	 * 
	 * 
	 * 
	 */
	public static Map<String, Object> findResBedDept(String t) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Dept> deptList = null;
		try {
			JSONObject json = new JSONObject(t);
			if (json.getBoolean("flag")) {

				Object object = null;
				if (json.has("data")) {
					object = json.getJSONArray("data");
				}

				if (object instanceof JSONArray) {// 成功
					JSONArray jArray = (JSONArray) object;
					deptList = new ArrayList<Dept>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject ject = jArray.getJSONObject(i);
						Dept dept = new Dept();
						if(!ject.isNull("bedPrice")){
							hashMap.put("bedPrice", ject.getString("bedPrice"));
						}
						if (!ject.isNull("eztDeptdptName")) {
							String deptName = ject.getString("eztDeptdptName");
							// 2015-11-30 if (deptName.equals("病历复印科")) {//
							// 屏蔽天津市肿瘤医院（病历复印科）
							// continue;
							// }
							dept.setdName(deptName);
						}
						if (!ject.isNull("id"))
							dept.setId(ject.getInt("id"));
						deptList.add(dept);
					}
				} else if (object instanceof Boolean) {
					boolean isSuc = (Boolean) object;
					if (!isSuc) {// 访问失败
						deptList = null;
					} else {// 数据为空

					}
				}

				hashMap.put("deptList", deptList);
			} else {
				if (json.has("detailMsg")) {
					String errorMsg = json.getString("detailMsg");
					hashMap.put("msg", errorMsg);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			deptList = null;
		}
		return hashMap;
	}

	/**
	 * 解析小科室列表
	 */
	public static Map<String, Object> getDeptList2(String t) {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Dept> deptList = null;
		try {
			JSONObject json = new JSONObject(t);
			if (json.getBoolean("flag")) {

				Object object = null;
				if (json.has("data")) {
					object = json.getJSONArray("data");
				}

				if (object instanceof JSONArray) {// 成功
					JSONArray jArray = (JSONArray) object;
					deptList = new ArrayList<Dept>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject ject = jArray.getJSONObject(i);
						Dept dept = new Dept();
						if (!ject.isNull("dptName")) {
							String deptName = ject.getString("dptName");
							// 2015-11-30 if (deptName.equals("病历复印科")) {//
							// 屏蔽天津市肿瘤医院（病历复印科）
							// continue;
							// }
							dept.setdName(deptName);
						}
						if (!ject.isNull("id"))
							dept.setId(ject.getInt("id"));
						deptList.add(dept);
					}
				} else if (object instanceof Boolean) {
					boolean isSuc = (Boolean) object;
					if (!isSuc) {// 访问失败
						deptList = null;
					} else {// 数据为空

					}
				}

				hashMap.put("deptList", deptList);
			} else {
				if (json.has("detailMsg")) {
					String errorMsg = json.getString("detailMsg");
					hashMap.put("msg", errorMsg);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			deptList = null;
		}
		return hashMap;
	}

	// /**
	// * 解析小科室列表
	// */
	// public static ArrayList<Dept> getDeptList2(String t) {
	// ArrayList<Dept> deptList = null;
	// try {
	// Object object = ResolveResponse.resolveData(t);// 解析返回的数据
	// if (object instanceof JSONArray) {// 成功
	// JSONArray jArray = (JSONArray) object;
	// deptList = new ArrayList<Dept>();
	// for (int i = 0; i < jArray.length(); i++) {
	// JSONObject ject = jArray.getJSONObject(i);
	// Dept dept = new Dept();
	// if (!ject.isNull("dptName")) {
	// String deptName = ject.getString("dptName");
	// // 2015-11-30 if (deptName.equals("病历复印科")) {// 屏蔽天津市肿瘤医院（病历复印科）
	// // continue;
	// // }
	// dept.setdName(deptName);
	// }
	// if (!ject.isNull("id"))
	// dept.setId(ject.getInt("id"));
	// deptList.add(dept);
	// }
	// } else if (object instanceof Boolean) {
	// boolean isSuc = (Boolean) object;
	// if (!isSuc) {// 访问失败
	// deptList = null;
	// } else {// 数据为空
	//
	// }
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// deptList = null;
	// }
	// return deptList;
	// }

	/**
	 * 获取排行医生列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getRankDocList(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Doctor> docList = null;
		try {
			JSONObject json = new JSONObject(t);
			JSONObject object = null;
			if (!json.isNull("data")) {
				object = json.getJSONObject("data");
			}
			if (null != object && json.getBoolean("flag")) {

				if (object instanceof JSONObject) {// 成功
					JSONObject ject = (JSONObject) object;
					if (!ject.isNull("rows")) {
						JSONArray jArray = ject.getJSONArray("rows");
						docList = new ArrayList<Doctor>();

						for (int i = 0; i < jArray.length(); i++) {
							Doctor doc = new Doctor();
							JSONObject ject1 = jArray.getJSONObject(i);
							if (!ject1.isNull("doctorId"))
								doc.setId(ject1.getString("doctorId"));

							if (!ject1.isNull("edName"))
								doc.setDocName(ject1.getString("edName"));

							if (!ject1.isNull("edLevelName"))
								doc.setDocPosition(ject1
										.getString("edLevelName"));
							if (!ject1.isNull("edLevel"))
								doc.setDocLevel(ject1.getString("edLevel"));
							if (!ject1.isNull("ehName"))
								doc.setDocHos(ject1.getString("ehName"));

							if (!ject1.isNull("dptName"))
								doc.setDocDept(ject1.getString("dptName"));

							if (!ject1.isNull("dptId"))
								doc.setDocDeptId(ject1.getString("dptId"));

							if (!ject1.isNull("hospitalId"))
								doc.setDocHosId(ject1.getString("hospitalId"));

							if (!ject1.isNull("rate"))
								doc.setDocRate(ject1.getString("rate"));

							if (!ject1.isNull("edPic"))
								doc.setDocHeadUrl(ject1.getString("edPic"));

							if (!ject1.isNull("deptDocId"))
								doc.setDeptDocId(ject1.getString("deptDocId"));

							// 电话医生是否可预约
							if (ject1.isNull("callDoctorYnAppointment")) {
								doc.setCallDoctorYnAppointment(0);
							} else {
								doc.setCallDoctorYnAppointment(ject1
										.getInt("callDoctorYnAppointment"));
							}

							// 电话医生是否在线
							if (ject1.isNull("callDoctorYnOnline")) {
								doc.setCallDoctorYnOnline(0);
							} else {
								doc.setCallDoctorYnOnline(ject1
										.getInt("callDoctorYnOnline"));
							}

							boolean isHave = false;
							if (!ject1.isNull("ynRemain")) {
								doc.setIsHaveNum(ject1.getInt("ynRemain"));
								if (doc.getIsHaveNum() == 1) {// 有号
									isHave = true;
								}
							}

							// if (isHave) {// 有号，则获取号池信息

							if (!ject1.isNull("daysPools")) {
								JSONArray jArray1 = ject1
										.getJSONArray("daysPools");

								ArrayList<Pool> pools = new ArrayList<Pool>();
								for (int j = 0; j < jArray1.length(); j++) {
									Pool pool = new Pool();
									JSONObject ject2 = jArray1.getJSONObject(j);
									if (!ject2.isNull("regDateDay"))
										pool.setDate(ject2
												.getString("regDateDay"));
									if (!ject2.isNull("regDateWeekDay"))
										pool.setDateWeek(ject2
												.getString("regDateWeekDay"));
									if (!ject2.isNull("ynRemainDay"))
										pool.setRemain(ject2
												.getInt("ynRemainDay"));
									pools.add(pool);

								}
								doc.setPools(pools);

							}

							// }
							// 2015-12-18 医院对接
							if (!ject1.isNull("ehDockingStatus"))
								doc.setEhDockingStatus(ject1
										.getInt("ehDockingStatus"));
							if (!ject1.isNull("ehDockingStr"))
								doc.setEhDockingStr(ject1
										.getString("ehDockingStr"));

							if (Integer.parseInt(doc.getDocHosId()) != 6
									&& Integer.parseInt(doc.getDocHosId()) != 116) {// 中心妇产现场预约医院/天津市口腔医院儿科普号（不显示）
								// if (null != doc.getDeptDocId()
								// && null != doc.getId()
								// && null != doc.getDocDeptId()) {
								// if (doc.getDeptDocId().equals("360")
								// && doc.getId().equals("360")
								// && doc.getDocDeptId()
								// .equals("1123")) {
								// // 戚务芳: id=83253 docDeptId=27484,
								// deptDocId=97446, docHosId=283, docLevel=55,
								// isHaveNum=0
								// // {deptdocid=97446, doctorid=83253,
								// // deptid=27484} 天津一中心医院免疫门诊科室 大夫医院部要求换id
								// // 且要求随时有号
								// doc.setDeptDocId("97446");
								// doc.setId("83253");
								// doc.setDocDeptId("27484");
								// doc.setIsHaveNum(1);
								//
								// }
								// if(doc.getDeptDocId().equals("97446")
								// && doc.getId().equals("83253")
								// && doc.getDocDeptId()
								// .equals("27484")){//天津第一中心医院（免疫科） 医院 戚务芳:
								// doc.setIsHaveNum(1);
								// }
								// if (doc.getDeptDocId().equals("5031")
								// && doc.getId().equals("5031")
								// && doc.getDocDeptId()
								// .equals("1123")) {
								// // 孔纯玉:
								//
								// // id=83254 docName=孔纯玉 docDeptId=27484,
								// deptDocId=97447, docHosId=283
								// // {deptdocid=97447, doctorid=83254,
								// // deptid=27484}
								// // 天津一中心医院免疫门诊科室 大夫医院部要求换id
								// // 且要求随时有号
								// doc.setDeptDocId("97447");
								// doc.setId("83254");
								// doc.setDocDeptId("27484");
								// doc.setIsHaveNum(1);
								//
								// }
								//
								// if(doc.getDeptDocId().equals("97447")
								// && doc.getId().equals("83254")
								// && doc.getDocDeptId()
								// .equals("27484")){//天津第一中心医院（免疫科） 医院 孔纯玉:
								// doc.setIsHaveNum(1);
								// }
								// }

								docList.add(doc);
							}

						}

					}

				}
				map.put("docList", docList);
			} else {
				if (!json.isNull("detailMsg")) {
					map.put("msg", json.get("detailMsg"));
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
			docList = null;
		}
		return map;
	}

	// /**
	// * 获取排行医生列表
	// *
	// * @param t
	// * @return
	// */
	// public static ArrayList<Doctor> getRankDocList(String t) {
	// ArrayList<Doctor> docList = null;
	// try {
	// Object object = ResolveResponse.resolveData(t);// 解析返回的数据
	// if (object instanceof JSONObject) {// 成功
	// JSONObject ject = (JSONObject) object;
	// if (!ject.isNull("rows")) {
	// JSONArray jArray = ject.getJSONArray("rows");
	// docList = new ArrayList<Doctor>();
	//
	// for (int i = 0; i < jArray.length(); i++) {
	// Doctor doc = new Doctor();
	// JSONObject ject1 = jArray.getJSONObject(i);
	// if (!ject1.isNull("doctorId"))
	// doc.setId(ject1.getString("doctorId"));
	//
	// if (!ject1.isNull("edName"))
	// doc.setDocName(ject1.getString("edName"));
	//
	// if (!ject1.isNull("edLevelName"))
	// doc.setDocPosition(ject1.getString("edLevelName"));
	// if (!ject1.isNull("edLevel"))
	// doc.setDocLevel(ject1.getString("edLevel"));
	// if (!ject1.isNull("ehName"))
	// doc.setDocHos(ject1.getString("ehName"));
	//
	// if (!ject1.isNull("dptName"))
	// doc.setDocDept(ject1.getString("dptName"));
	//
	// if (!ject1.isNull("dptId"))
	// doc.setDocDeptId(ject1.getString("dptId"));
	//
	// if (!ject1.isNull("hospitalId"))
	// doc.setDocHosId(ject1.getString("hospitalId"));
	//
	// if (!ject1.isNull("rate"))
	// doc.setDocRate(ject1.getString("rate"));
	//
	// if (!ject1.isNull("edPic"))
	// doc.setDocHeadUrl(ject1.getString("edPic"));
	//
	// if (!ject1.isNull("deptDocId"))
	// doc.setDeptDocId(ject1.getString("deptDocId"));
	//
	// // 电话医生是否可预约
	// if (ject1.isNull("callDoctorYnAppointment")) {
	// doc.setCallDoctorYnAppointment(0);
	// } else {
	// doc.setCallDoctorYnAppointment(ject1
	// .getInt("callDoctorYnAppointment"));
	// }
	//
	// // 电话医生是否在线
	// if (ject1.isNull("callDoctorYnOnline")) {
	// doc.setCallDoctorYnOnline(0);
	// } else {
	// doc.setCallDoctorYnOnline(ject1
	// .getInt("callDoctorYnOnline"));
	// }
	//
	// boolean isHave = false;
	// if (!ject1.isNull("ynRemain")) {
	// doc.setIsHaveNum(ject1.getInt("ynRemain"));
	// if (doc.getIsHaveNum() == 1) {// 有号
	// isHave = true;
	// }
	// }
	//
	// // if (isHave) {// 有号，则获取号池信息
	//
	// if (!ject1.isNull("daysPools")) {
	// JSONArray jArray1 = ject1.getJSONArray("daysPools");
	//
	// ArrayList<Pool> pools = new ArrayList<Pool>();
	// for (int j = 0; j < jArray1.length(); j++) {
	// Pool pool = new Pool();
	// JSONObject ject2 = jArray1.getJSONObject(j);
	// if (!ject2.isNull("regDateDay"))
	// pool.setDate(ject2.getString("regDateDay"));
	// if (!ject2.isNull("regDateWeekDay"))
	// pool.setDateWeek(ject2
	// .getString("regDateWeekDay"));
	// if (!ject2.isNull("ynRemainDay"))
	// pool.setRemain(ject2.getInt("ynRemainDay"));
	// pools.add(pool);
	//
	// }
	// doc.setPools(pools);
	//
	// }
	//
	// // }
	// if (Integer.parseInt(doc.getDocHosId()) != 6
	// && Integer.parseInt(doc.getDocHosId()) != 116) {//
	// 中心妇产现场预约医院/天津市口腔医院儿科普号（不显示）
	// docList.add(doc);
	// }
	//
	// }
	//
	// }
	//
	// } else if (object instanceof Boolean) {
	// boolean isSuc = (Boolean) object;
	// if (!isSuc) {// 访问失败
	// docList = null;
	// } else {// 数据为空
	//
	// }
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// docList = null;
	// }
	// return docList;
	// }

	/**
	 * 解析医生信息
	 */
	public static Doctor getDocInfo(String t) {
		Doctor doc = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				doc = new Doctor();

				if (!ject.isNull("fansCount"))
					doc.setDocFans(ject.getString("fansCount"));// 粉丝数

				if (!ject.isNull("rate"))
					doc.setDocRate(ject.getString("rate"));// 预约率

				if (!ject.isNull("hospitalName"))
					doc.setDocHos(ject.getString("hospitalName"));// 获取所属医院

				if (!ject.isNull("weekRpNum"))
					doc.setDocAllocaeNum(ject.getString("weekRpNum"));// 放号量

				if (!ject.isNull("assessCount"))
					doc.setDocOverallMerit(ject.getInt("assessCount"));// 综合评价

				if (!ject.isNull("aeEffecstars"))
					doc.setDocResult(ject.getInt("aeEffecstars"));// 医疗效果

				if (!ject.isNull("aeServiceStars"))
					doc.setDocServiceAttitude(ject.getInt("aeServiceStars"));// 服务态度

				if (!ject.isNull("doctorBean")) {
					JSONObject docJect = ject.getJSONObject("doctorBean");

					if (!docJect.isNull("id"))
						doc.setId(docJect.getString("id"));

					if (!docJect.isNull("edPic"))
						doc.setDocHeadUrl(docJect.getString("edPic"));// 头像

					if (!docJect.isNull("edName"))
						doc.setDocName(docJect.getString("edName"));// 名称

					if (!docJect.isNull("edLevel"))
						doc.setDocPosition(docJect.getString("edLevel"));// 职位

					if (!docJect.isNull("edProfile"))
						doc.setDocIntro(docJect.getString("edProfile"));// 简介

					if (!docJect.isNull("edGoodat"))
						doc.setDocGoodAt(docJect.getString("edGoodat"));// 擅长

					if (!docJect.isNull("edCulturalLeave"))
						doc.setDocEducBg(docJect.getString("edCulturalLeave"));// 教育背景

					if (!docJect.isNull(""))
						doc.setDocAcademicSuc(docJect.getString(""));// 学术成就

				}

				if (!ject.isNull("deptBean")) {
					JSONObject deptJect = ject.getJSONObject("deptBean");

					if (!deptJect.isNull("id"))
						doc.setDocDeptId(deptJect.getString("id"));
					if (!deptJect.isNull("dptName"))
						doc.setDocDept(deptJect.getString("dptName"));// 获取所属科室
					if (!deptJect.isNull("dptHid")) {
						doc.setDocHosId(deptJect.getString("dptHid"));
					}
				}
				if (!ject.isNull("hospitalBean")) {
					JSONObject hosJect = ject.getJSONObject("hospitalBean");
					if (!hosJect.isNull("lat")) {
						doc.setHosLat(hosJect.getDouble("lat"));
					}
					if (!hosJect.isNull("lng")) {
						doc.setHosLon(hosJect.getDouble("lng"));
					}
				}

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					doc = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			doc = null;
		}
		return doc;
	}

	/**
	 * 解析医生号池
	 */
	public static ArrayList<Pool> getDocPool(String t) {
		ArrayList<Pool> poolList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			int nums = 0;
			if (object instanceof JSONArray) {// 成功
				poolList = new ArrayList<Pool>();
				JSONArray jarray = (JSONArray) object;
				JSONArray jarray2;
				Pool pool;
				for (int i = 0; i < jarray.length(); i++) {
					pool = new Pool();
					JSONObject json = jarray.getJSONObject(i);
					if (!json.isNull("doctorPoolDate"))
						pool.setDate(json.getString("doctorPoolDate"));// 日期

					if (!json.isNull("doctorPools")) {
						jarray2 = json.getJSONArray("doctorPools");
						List<PoolTimes> timeList = new ArrayList<PoolTimes>();
						PoolTimes pt;
						for (int j = 0; j < jarray2.length(); j++) {
							JSONObject json1 = jarray2.getJSONObject(j);
							pt = new PoolTimes();
							if (!json1.isNull("isRemain")) {
								int flag = json1.getInt("isRemain");
								pt.setRemains(flag == 1);
								if (flag != 1) {
									continue;
								}
							}
							if (!json1.isNull("startDate")) {
								String start = json1.getString("startDate");
								pt.setStartDates(start.substring(
										start.indexOf(" "),
										start.lastIndexOf(":")));// 开始时间
							}

							if (!json1.isNull("endDate")) {
								String end = json1.getString("endDate");
								pt.setEndDates(end.substring(end.indexOf(" "),
										end.lastIndexOf(":")));// 结束时间
							}

							if (!json1.isNull("poolId"))
								pt.setPoolId(json1.getString("poolId"));// 号池id

							if (!json1.isNull("regMark"))
								pool.setRegMark(json1.getString("regMark"));// 参数标志

							if (!json1.isNull("pfId"))
								pool.setPfId(json1.getString("pfId"));// 平台id
							String week = "";
							if (!json1.isNull("regDateWeek")) {
								switch (json1.getInt("regDateWeek")) {
								case 1:
									week = "星期一";
									break;

								case 2:
									week = "星期二";
									break;
								case 3:
									week = "星期三";
									break;
								case 4:
									week = "星期四";
									break;
								case 5:
									week = "星期五";
									break;
								case 6:
									week = "星期六";
									break;
								case 7:
									week = "星期日";
									break;
								}
							}
							pool.setDateWeek(week); // 对应的星期
							timeList.add(pt);
							pool.setTimeList(timeList);
						}
						poolList.add(pool);
					}

				}
				jarray2 = null;
				pool = null;
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					poolList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			poolList = null;
		}
		return poolList;
	}

	/**
	 * 解析搜索的医院
	 */
	public static Map<String, Object> getSearchHosList(String t) {
		ArrayList<Hospital> hosList = null;
		Map<String, Object> map = null;
		try {

			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				map = new HashMap<String, Object>();

				JSONObject ject = (JSONObject) object;
				if (!ject.isNull("total"))
					map.put("total", ject.getInt("total"));

				if (!ject.isNull("rows")) {
					JSONArray jArray = ((JSONObject) object)
							.getJSONArray("rows");
					hosList = new ArrayList<Hospital>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject ject2 = jArray.getJSONObject(i);
						Hospital hos = new Hospital();
						if (!ject2.isNull("ehName"))
							hos.sethName(ject2.getString("ehName"));// 名称
						if (!ject2.isNull("hid"))
							hos.setId(ject2.getInt("hid"));// id
						if (!ject2.isNull("ehAddress"))
							hos.sethAddress(ject2.getString("ehAddress"));// 地址
						if (!ject2.isNull("ehTel"))
							hos.sethTel(ject2.getString("ehTel"));// 电话
						if (!ject2.isNull("ehRemark"))
							hos.sethIntro(ject2.getString("ehRemark"));// 简介

						if (!ject2.isNull("ehLevel")) {// 等级
							hos.setHosLevel(ject2.getString("ehLevel"));
						}

						if (hos.getId() != 6 && hos.getId() != 116) {// 中心妇产现场预约医院/天津市口腔医院儿科普号（不显示）
							hosList.add(hos);
						}

					}

				}
				map.put("hosList", hosList);

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					map = null;
				} else {// 数据为空
					map = new HashMap<String, Object>();
					map.put("hosList", hosList);
					map.put("total", 0);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	/**
	 * 解析搜索的科室
	 */
	public static Map<String, Object> getSearchDeptList(String t) {
		ArrayList<Dept> deptList = null;
		Map<String, Object> map = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				map = new HashMap<String, Object>();

				JSONObject ject = (JSONObject) object;
				if (!ject.isNull("total"))
					map.put("total", ject.getInt("total"));

				if (!ject.isNull("rows")) {
					JSONArray jArray = ((JSONObject) object)
							.getJSONArray("rows");
					deptList = new ArrayList<Dept>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject ject2 = jArray.getJSONObject(i);
						Dept dept = new Dept();
						if (!ject2.isNull("dptame"))
							dept.setdName(ject2.getString("dptame"));// 名称
						if (!ject2.isNull("deptId"))
							dept.setId(ject2.getInt("deptId"));// id

						if (!ject2.isNull("deptcateId"))
							dept.setDeptcateId(ject2.getInt("deptcateId") + "");// 科室分类id

						if (!ject2.isNull("hid"))
							dept.setdHosId(ject2.getInt("hid") + "");// 医院id

						if (!ject2.isNull("ehName"))
							dept.setdHosName(ject2.getString("ehName"));// 所属医院

						if (!ject2.isNull("dptProfile"))
							dept.setIntro(ject2.getString("dptProfile"));// 简介

						if (Integer.parseInt(dept.getdHosId()) != 6
								&& Integer.parseInt(dept.getdHosId()) != 116) {// 中心妇产现场预约医院/天津市口腔医院儿科普号（不显示）
							deptList.add(dept);
						}

					}

				}
				map.put("deptList", deptList);

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					map = null;
				} else {// 数据为空
					map = new HashMap<String, Object>();
					map.put("deptList", deptList);
					map.put("total", 0);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	/**
	 * 解析搜索的医生
	 */
	public static Map<String, Object> getSearchDocList(String t) {
		ArrayList<Doctor> docList = null;
		Map<String, Object> map = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				map = new HashMap<String, Object>();

				JSONObject ject = (JSONObject) object;
				if (!ject.isNull("total"))
					map.put("total", ject.getInt("total"));

				if (!ject.isNull("rows")) {
					JSONArray jArray = ((JSONObject) object)
							.getJSONArray("rows");
					docList = new ArrayList<Doctor>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject ject2 = jArray.getJSONObject(i);
						Doctor doc = new Doctor();

						if (!ject2.isNull("edName"))
							doc.setDocName(ject2.getString("edName"));// 名称
						if (!ject2.isNull("docId"))
							doc.setId(ject2.getString("docId"));// id

						if (!ject2.isNull("deptdocId"))
							doc.setDeptDocId(ject2.getString("deptdocId"));

						if (!ject2.isNull("deptId"))
							doc.setDocDeptId(ject2.getString("deptId"));

						if (!ject2.isNull("edLevelName"))
							doc.setDocLevel(ject2.getString("edLevelName"));// 级别

						if (!ject2.isNull("ehName"))
							doc.setDocHos(ject2.getString("ehName"));// 医院

						if (!ject2.isNull("dptame"))
							doc.setDocDept(ject2.getString("dptame"));// 科室

						if (!ject2.isNull("edPic"))
							doc.setDocHeadUrl(ject2.getString("edPic"));// 头像

						int hid = 0;
						if (!ject2.isNull("hid"))
							hid = ject2.getInt("hid");

						if (!ject2.isNull("ehDockingStatus"))
							doc.setEhDockingStatus(ject2
									.getInt("ehDockingStatus"));// 2015-12-21
																// 医院对接状态
						if (!ject2.isNull("ehDockingStr"))
							doc.setEhDockingStr(ject2.getString("ehDockingStr"));// 2015-12-21
																					// 医院对接提示语

						if (hid != 6 && hid != 116) {// 中心妇产现场预约医院/天津市口腔医院儿科普号（不显示）
							docList.add(doc);
						}

					}

				}
				map.put("docList", docList);

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					map = null;
				} else {// 数据为空
					map = new HashMap<String, Object>();
					map.put("docList", docList);
					map.put("total", 0);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	/**
	 * 解析搜索的知识库
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getSearchKnowLib(String t) {
		ArrayList<Information> infoList = null;
		Map<String, Object> map = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				map = new HashMap<String, Object>();

				JSONObject ject = (JSONObject) object;
				if (!ject.isNull("total"))
					map.put("total", ject.getInt("total"));

				if (!ject.isNull("rows")) {
					JSONArray jArray = ((JSONObject) object)
							.getJSONArray("rows");
					infoList = new ArrayList<Information>();
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject ject2 = jArray.getJSONObject(i);
						Information info = new Information();

						if (!ject2.isNull("title"))
							info.setInfoTitle(ject2.getString("title"));// 标题

						if (!ject2.isNull("id"))
							info.setId(ject2.getString("id"));// id

						if (!ject2.isNull("description"))
							info.setInfoDescription(ject2
									.getString("description"));// 内容

						if (!ject2.isNull("litpic"))
							info.setImgUrl(ject2.getString("litpic"));// id

						infoList.add(info);
					}

				}
				map.put("infoList", infoList);

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					map = null;
				} else {// 数据为空
					map = new HashMap<String, Object>();
					map.put("infoList", infoList);
					map.put("total", 0);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}

	/**
	 * 解析医生是否有号
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> ynRemain(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				map.put("isHaveNum", json.getString("data"));
			}
			if (!flag) {
				map.put("msg", json.getString("msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据医院ID患者ID获取医院一卡通号
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> parseOneCard(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			if (!json.isNull("flag")) {
				boolean flag = json.getBoolean("flag");
				map.put("flag", flag);
				map.put("detailMsg", json.getString("detailMsg"));
				if (!flag) {
					return map;
				}
			}
			if (json.isNull("data")) {
				map.put("ucNum", "");
				return map;
			}
			JSONObject data = json.getJSONObject("data");
			if (!data.isNull("ucNum")) {
				map.put("ucNum", data.getString("ucNum"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取离我最近的医院列表
	 * 
	 * @param t
	 * @return
	 */
	public static ArrayList<Hospital> getNearHosList(String t) {
		ArrayList<Hospital> hosList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				JSONArray hosArray = null;
				if (!ject.isNull("rows")) {
					hosArray = ject.getJSONArray("rows");
				} else {
					return hosList;
				}
				hosList = new ArrayList<Hospital>();
				for (int i = 0; i < hosArray.length(); i++) {
					Hospital hos = new Hospital();
					JSONObject ject1 = hosArray.getJSONObject(i);
					if (!ject1.isNull("id"))
						hos.setId(ject1.getInt("id"));

					if (!ject1.isNull("ehName"))
						hos.sethName(ject1.getString("ehName"));

					if (!ject1.isNull("distance")) {
						double distance = ject1.getDouble("distance");
						String strDis = StringUtil.getOneRadixPoint(distance);
						hos.sethDistance(strDis);
					}

					hosList.add(hos);
				}

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					hosList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			hosList = null;
		}
		return hosList;
	}
}
