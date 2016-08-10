package com.eztcn.user.eztcn.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.DocEvaluateBean;
import com.eztcn.user.eztcn.bean.OrderRegisterRecord;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.bean.ThanksLetter;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 解析预约挂号
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class RegistrationManager {
	public static Map<String, Object> reg(String t){
		Map<String, Object> map = null;
		if (t != null && !t.equals("")) {
			try {
				map = new HashMap<String, Object>();
				JSONObject json = new JSONObject(t);
				boolean flag = json.getBoolean("flag");
				map.put("flag", flag);
				if (!flag) {
					map.put("msg", json.getString("detailMsg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return map;
	}
	/**
	 * 解析挂号入列
	 */
	public static Map<String, Object> enterQueue(String t) {
		Map<String, Object> map = null;
		if (t != null && !t.equals("")) {
			try {
				map = new HashMap<String, Object>();
				JSONObject json = new JSONObject(t);
				boolean flag = json.getBoolean("flag");
				map.put("flag", flag);
				if (!flag) {
					map.put("msg", json.getString("detailMsg"));
				} else {
					map.put("data", json.getString("data"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 检测是否挂号成功
	 */
	public static Map<String, Object> isReg(String t) {
		Map<String, Object> map = null;
		if (t != null && !t.equals("")) {
			try {
				map = new HashMap<String, Object>();
				JSONObject json = new JSONObject(t);
				boolean flag = json.getBoolean("flag");
				map.put("flag", flag);
				if (!flag) {
					map.put("msg", json.getString("detailMsg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 确认挂号
	 */
	public static Map<String, Object> regConfirm(String t) {
		Map<String, Object> map = null;
		if (t != null && !t.equals("")) {
			try {
				map = new HashMap<String, Object>();
				JSONObject json = new JSONObject(t);
				boolean flag = json.getBoolean("flag");
				map.put("flag", flag);
				if (!flag) {
					map.put("msg", json.getString("detailMsg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	/**
	 * 获取已预约挂号记录
	 */
	public static Map<String, Object> getRegRecord(String t) {
		Map<String, Object> map = null;
		List<Record_Info> recordList = new ArrayList<Record_Info>();
		Record_Info record = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			map = new HashMap<String, Object>();
			JSONObject json_t = new JSONObject(t);
			boolean bool = json_t.getBoolean("flag");
			map.put("flag", bool);
			map.put("msg", json_t.getString("detailMsg"));
			if (!bool) {
				map.put("list", recordList);
				return map;
			}
			JSONObject json = json_t.getJSONObject("data");
			JSONArray array = json.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
				json = array.getJSONObject(i);
				record = new Record_Info();
				if (!json.isNull("regDoctoredName")) {
					record.setDoctorName(json.getString("regDoctoredName"));
				}
				if (!json.isNull("regHospitalehName")) {
					record.setHospital(json.getString("regHospitalehName"));
				}
				if (!json.isNull("regDeptdptName")) {
					record.setDept(json.getString("regDeptdptName"));
				}
				if (!json.isNull("regDoctoredLevel")) {
					record.setDoctorLevel(json.getInt("regDoctoredLevel"));
				}
				if (!json.isNull("regRegisterdoctorId")) {
					record.setDoctorId(json.getInt("regRegisterdoctorId"));
				}
				if (!json.isNull("regRegisterpfId")) {
					record.setPlatformId(json.getInt("regRegisterpfId"));
				}
				if (!json.isNull("id")) {
					record.setId(json.getInt("id"));
				}
				if (!json.isNull("regRegisterdeptId")) {
					record.setDeptId(json.getInt("regRegisterdeptId"));
				}
				if (!json.isNull("regPatientepName")) {
					record.setPatientName(json.getString("regPatientepName"));
				}
				if (!json.isNull("regRegisterrrDate")) {
					record.setDate(json.getString("regRegisterrrDate"));
				}
				if (!json.isNull("regRegisterrrBeginTimeNode")) {
					record.setBeginTime(json
							.getString("regRegisterrrBeginTimeNode"));
				}
				if (!json.isNull("regRegisterrrEndTimeNode")) {
					record.setEndTime(json
							.getString("regRegisterrrEndTimeNode"));
				}
				if (!json.isNull("regUsereuMobile")) {
					record.setPhone(json.getString("regUsereuMobile"));
				}
				if (!json.isNull("regPatientepPid")) {
					record.setIdCard(json.getString("regPatientepPid"));
				}
				if (!json.isNull("regRegisterrrPayWay")) {
					record.setPayType(json.getInt("regRegisterrrPayWay"));
				}
				if (!json.isNull("regRegisterrrDemand")) {
					record.setDiscribe(json.getString("regRegisterrrDemand"));
				}
				if (!json.isNull("regDoctoredPic")) {
					record.setPhoto(json.getString("regDoctoredPic"));
				}
				if (!json.isNull("regDeptDocedFree")) {
					record.setRegDeptDocedFree(json.getDouble("regDeptDocedFree"));
				}
				if (!json.isNull("regRegisterrrStatus")) {
					record.setRrStatus(json.getInt("regRegisterrrStatus"));
				}
				recordList.add(record);
			}
			map.put("list", recordList);
		} catch (JSONException e) {
			e.printStackTrace();
			map = new HashMap<String, Object>();
			map.put("flag", false);
			map.put("msg", "服务器异常");
			recordList = null;
		}
		return map;
	}

	/**
	 * 解析退号返回信息
	 */
	public static Map<String, Object> parseJsonData(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取感谢信内容
	 */
	public static Map<String, Object> getThanksLetterList(String t) {
		Map<String, Object> map = null;
		String data;
		List<ThanksLetter> thanksList = new ArrayList<ThanksLetter>();
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			JSONObject contentJson;
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			data = json.getString("data");
			json = new JSONObject(data);
			JSONArray array = new JSONArray(json.getString("rows"));
			ThanksLetter letter;
			for (int i = 0; i < array.length(); i++) {
				contentJson = array.getJSONObject(i);
				letter = new ThanksLetter();
				// 患者信息
				json = contentJson.getJSONObject("eztPatientBean");
				if (!json.isNull("epName")) {
					letter.setPatientName(json.getString("epName"));
				}
				if (!json.isNull("userId")) {
					letter.setUserId(json.getInt("userId"));
				}
				if (!json.isNull("id")) {
					letter.setPatientId(json.getInt("id"));
				}

				// 感谢信内容
				json = contentJson.getJSONObject("eztThankNoteBean");
				if (!json.isNull("tnContent")) {
					letter.setContent(json.getString("tnContent"));
				}
				if (!json.isNull("id")) {
					letter.setId(json.getInt("id"));
				}
				if (!json.isNull("tnSignature")) {
					letter.setSignature(json.getString("tnSignature"));
				}
				if (!json.isNull("createtime")) {
					letter.setCreatetime(json.getString("createtime"));
				}
				thanksList.add(letter);
			}
			map.put("thanks", thanksList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取评价列表
	 */
	public static Map<String, Object> getEvaluateList(String t) {
		Map<String, Object> map = null;
		String data;
		List<DocEvaluateBean> evaluateList = new ArrayList<DocEvaluateBean>();
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			data = json.getString("data");
			json = new JSONObject(data);
			JSONArray array = json.getJSONArray("rows");
			JSONObject dataJson;
			DocEvaluateBean letter;
			for (int i = 0; i < array.length(); i++) {
				letter = new DocEvaluateBean();
				dataJson = array.getJSONObject(i);
				if (!dataJson.isNull("bizPatientBean")) {
					json = dataJson.getJSONObject("bizPatientBean");
					if (!json.isNull("id")) {
						letter.setPatientId(json.getString("id"));
					}
					if (!json.isNull("epName")) {
						letter.setUserName(json.getString("epName"));
					}
				}
				if (!dataJson.isNull("bizAfterEvaluationBean")) {
					json = dataJson.getJSONObject("bizAfterEvaluationBean");
					if (!json.isNull("aeStars")) {
						letter.setTotalRate(json.getDouble("aeStars"));
					}
					if (!json.isNull("aeEffectStars")) {
						letter.setMedicalEffect(json.getDouble("aeEffectStars"));
					}
					if (!json.isNull("aeServiceStars")) {
						letter.setServerAttitude(json
								.getDouble("aeServiceStars"));
					}
					if (!json.isNull("aeContent")) {
						letter.setContent(json.getString("aeContent"));
					}
					// if (!contentJson.isNull("tnSignature")) {
					// letter.setSignature(contentJson.getString("tnSignature"));
					// }
					if (!json.isNull("createtime")) {
						letter.setCreateTime(json.getString("createtime"));
					}
				}
				evaluateList.add(letter);
			}
			map.put("evaluate", evaluateList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析未评价数量
	 */
	public static Map<String, Object> parseUnEvaluateCount(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			// data
			if (!json.isNull("data")) {
				map.put("data", json.getString("data"));
			}
			map.put("msg", json.getString("detailMsg"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 解析未评价列表
	 */
	public static Map<String, Object> parseUnEvaluateList(String t) {
		Map<String, Object> map = null;
		List<Record_Info> list = new ArrayList<Record_Info>();
		Record_Info info;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag || json.isNull("data")) {
				return map;
			}
			JSONObject data = json.getJSONObject("data");
			JSONArray rows = data.getJSONArray("rows");
			for (int i = 0; i < rows.length(); i++) {
				data = rows.getJSONObject(i);
				info = new Record_Info();
				if (!data.isNull("regDate")) {
					info.setDate(data.getString("regDate"));
				}
				if (!data.isNull("benginTimeNode")) {
					info.setBeginTime(data.getString("benginTimeNode"));
				}
				if (!data.isNull("endTimeNode")) {
					info.setEndTime(data.getString("endTimeNode"));
				}
				if (!data.isNull("doctorId")) {
					info.setDoctorId(data.getInt("doctorId"));
				}
				if (!data.isNull("deptName")) {
					info.setDept(data.getString("deptName"));
				}
				if (!data.isNull("edlevel")) {
					info.setDoctorLevel(data.getInt("edlevel"));
				}
				if (!data.isNull("edName")) {
					info.setDoctorName(data.getString("edName"));
				}
				if (!data.isNull("deptId")) {
					info.setDeptId(data.getInt("deptId"));
				}
				if (!data.isNull("id")) {
					info.setId(data.getInt("id"));
				}
				list.add(info);
			}
			map.put("list", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	// 预约登记
	public static Map<String, Object> orderRegister(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 解析预约登记列表
	 * 
	 * @return
	 */
	public static Map<String, Object> parserORRecord(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderRegisterRecord> list;
		try {
			list = new ArrayList<OrderRegisterRecord>();
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
			JSONArray rows = data.getJSONArray("rows");
			OrderRegisterRecord record = null;
			for (int i = 0; i < rows.length(); i++) {
				data = rows.getJSONObject(i);
				record = new OrderRegisterRecord();
				if (!data.isNull("id")) {
					record.setId(data.getInt("id"));
				}
				if (!data.isNull("regRegisterReleasepatientId")) {
					record.setPatientId(data
							.getString("regRegisterReleasepatientId"));
				}
				if (!data.isNull("regPatientepName")) {
					record.setPatientName(data.getString("regPatientepName"));
				}
				if (!data.isNull("eztZCitycityname")) {
					record.setCity(data.getString("eztZCitycityname"));
				}
				if (!data.isNull("eztZCountycountyname")) {
					record.setCounty(data.getString("eztZCountycountyname"));
				}
				if (!data.isNull("regDeptCatedcName")) {
					record.setDept(data.getString("regDeptCatedcName"));
				}
				if (!data.isNull("regPatientepMobile")) {
					record.setMobile(data.getString("regPatientepMobile"));
				}
				if (!data.isNull("regPatientuserId")) {
					record.setUserId(data.getString("regPatientuserId"));
				}
				if (!data.isNull("regPatientepSex")) {
					record.setSex(data.getInt("regPatientepSex"));
				}
				if (!data.isNull("regRegisterReleasecreateTime")) {
					String str = StringUtil.dealWithDate(data
							.getString("regRegisterReleasecreateTime"));
					record.setOrderDate(str);
				}
				if (!data.isNull("regRegisterReleasecreateTime")) {
					record.setCreateTime(data
							.getString("regRegisterReleasecreateTime"));
				}
				if (!data.isNull("regRegisterReleaserrIsFirst")) {
					record.setSeeDocStatus(data
							.getInt("regRegisterReleaserrIsFirst"));
				}
				if (!data.isNull("regRegisterReleaserrPayWay")) {
					record.setPayType(data.getInt("regRegisterReleaserrPayWay"));
				}
				if (!data.isNull("regRegisterReleaserrDemand")) {
					record.setIllDiscribe(data
							.getString("regRegisterReleaserrDemand"));
				}
				if (!data.isNull("regRegisterReleaserrRecordNum")) {
					record.setMedicalNum(data
							.getString("regRegisterReleaserrRecordNum"));
				}
				if (!data.isNull("regRegisterReleaserrReplyNum")) {
					record.setReplyCount(data
							.getInt("regRegisterReleaserrReplyNum"));
				}
				list.add(record);
			}
			map.put("list", list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取预约登记库详情
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getCheckinDetails(String t) {

		Map<String, Object> map = null;
		OrderRegisterRecord r = null;

		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));

			if (!json.isNull("data")) {
				JSONObject ject = json.getJSONObject("data");

				r = new OrderRegisterRecord();
				if (!ject.isNull("id"))
					r.setId(ject.getInt("id"));

				if (!ject.isNull("epName"))
					r.setPatientName(ject.getString("epName"));

				if (!ject.isNull("mrNum"))
					r.setMedicalNum(ject.getString("mrNum"));

				if (!ject.isNull("cityName"))
					r.setCity(ject.getString("cityName"));

				if (!ject.isNull("countyName"))
					r.setCounty(ject.getString("countyName"));

				if (!ject.isNull("dcName"))
					r.setDept(ject.getString("dcName"));

				if (!ject.isNull("expectDate"))
					r.setOrderDate(ject.getString("expectDate"));

				if (!ject.isNull("rrPayWay"))
					r.setPayType(ject.getInt("rrPayWay"));

				if (!ject.isNull("rrIsFirst"))
					r.setSeeDocStatus(ject.getInt("rrIsFirst"));

				if (!ject.isNull("createTime")) {
					String str = StringUtil.dealWithDate(ject
							.getString("createTime"));
					r.setCreateTime(str);
				}

				if (!ject.isNull("rrDemand"))
					r.setIllDiscribe(ject.getString("rrDemand"));

				map.put("data", r);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析抢单医生列表
	 * 
	 * @return
	 */
	public static Map<String, Object> parserRegedDoctor(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Record_Info> list;
		try {
			list = new ArrayList<Record_Info>();
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
			JSONArray arry = data.getJSONArray("releaseDoctorBean");
			Record_Info record;
			for (int i = 0; i < arry.length(); i++) {
				data = arry.getJSONObject(i);
				record = new Record_Info();
				if (!data.isNull("id")) {
					record.setId(data.getInt("id"));
				}
				if (!data.isNull("doctorId")) {
					record.setDoctorId(data.getInt("doctorId"));
				}
				if (!data.isNull("deptId")) {
					record.setDeptId(data.getInt("deptId"));
				}
				if (!data.isNull("dptName")) {
					record.setDept(data.getString("dptName"));
				}
				if (!data.isNull("edLevel")) {
					record.setDoctorLevel(data.getInt("edLevel"));
				}
				if (!data.isNull("edName")) {
					record.setDoctorName(data.getString("edName"));
				}
				if (!data.isNull("edPic")) {
					record.setPhoto(data.getString("edPic"));
				}
				if (!data.isNull("ehName")) {
					record.setHospital(data.getString("ehName"));
				}
				if (!data.isNull("rdBeginTimeNode")) {
					record.setBeginTime(data.getString("rdBeginTimeNode"));
				}
				if (!data.isNull("rdEndTimeNode")) {
					record.setEndTime(data.getString("rdEndTimeNode"));
				}
				if (!data.isNull("rdRegDate")) {
					String str = data.getString("rdRegDate");
					str = str.split(" ")[0];
					record.setRegTime(str);
				}

				list.add(record);
			}
			map.put("list", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取预约挂号id
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> parserOrderId(String t) {
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
			map.put("regId", json.getString("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析返回状态
	 */
	public static Map<String, Object> parserJson(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
			}
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 解析最新挂号信息
	 */
	public static Map<String, Object> getRegregisterNew(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
			}
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));

			if (flag) {
				JSONObject data = json.getJSONObject("data");
				if (!data.isNull("rrRegTime")) {
					String strRrRegTime = data.getString("rrRegTime");
					String strStartTime = EZTConfig.DIDI_DATE;
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
				/*	try {
						Date d1 = sdf.parse(strStartTime);
						Date d2 = sdf.parse(strRrRegTime);// 最新挂号时间
						if (d2.after(d1)) {//
							BaseApplication.patient.setFirstAppoint(true);
						} else {
							BaseApplication.patient.setFirstAppoint(false);
						}
					} catch (java.text.ParseException e) {
						e.printStackTrace();
						BaseApplication.patient.setFirstAppoint(false);
					}*/

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
