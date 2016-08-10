package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.CallTimeList;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.PhoneRecordBean;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 电话医生相关数据解析
 * @describe
 * @author ezt
 * @created 2015年1月27日
 */
public class TelDocManager {

	// 查看电话医生状态
	public static Map<String, Object> checkTelDocState(String t) {
		Map<String, Object> map = null;

		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				TelDocState state = new TelDocState();

				JSONObject ject = json.getJSONObject("data");

				if (!ject.isNull("ctdCallGearBean")) {
					JSONObject ject1 = ject.getJSONObject("ctdCallGearBean");

					if (!ject1.isNull("ciGearDeep"))
						state.setCiGearDeep(ject1.getInt("ciGearDeep"));

					if (!ject1.isNull("ciGuaranteedRate"))
						state.setCiGuaranteedRate(ject1
								.getString("ciGuaranteedRate"));

					if (!ject1.isNull("ciGuaranteedTime"))
						state.setCiGuaranteedTime(ject1
								.getString("ciGuaranteedTime"));

					if (!ject1.isNull("ciName"))
						state.setCiName(ject1.getString("ciName"));

					if (!ject1.isNull("ciStandardRate"))
						state.setCiStandardRate(ject1
								.getString("ciStandardRate"));

					if (!ject1.isNull("ciStandardTime"))
						state.setCiStandardTime(ject1
								.getString("ciStandardTime"));

					if (!ject1.isNull("id"))
						state.setId(ject1.getInt("id"));

				}
				if (!ject.isNull("isOpenService"))
					state.setIsOpenService(ject.getInt("isOpenService"));

				if (!ject.isNull("ynAppointment"))
					state.setYnAppointment(ject.getInt("ynAppointment"));

				if (!ject.isNull("ynOnline"))
					state.setYnOnline(ject.getInt("ynOnline"));

				map.put("data", state);

			}
			if (!flag) {
				map.put("msg", json.getString("msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	// 根据日期获取医生可预约的时间段
	public static Map<String, Object> getTelDocTime(String t) {
		Map<String, Object> map = null;
		List<CallTimeList> list;
		try {
			map = new HashMap<String, Object>();
			list = new ArrayList<CallTimeList>();
			JSONObject json = new JSONObject(t);
			JSONObject detailJson;
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {
				JSONArray dataJson = json.getJSONArray("data");
				CallTimeList time;
				for (int i = 0; i < dataJson.length(); i++) {
					detailJson = dataJson.getJSONObject(i);
					time = new CallTimeList();
					if (!detailJson.isNull("callScheduleId")) {
						time.setCallScheduleId(detailJson
								.getInt("callScheduleId"));
					}
					if (!detailJson.isNull("id")) {
						time.setId(detailJson.getInt("id"));
					}
					if (!detailJson.isNull("rsBeginTimeNode")) {
						time.setBeginTime(detailJson
								.getString("rsBeginTimeNode"));
					}
					if (!detailJson.isNull("rsEndTimeNode")) {
						time.setEndTime(detailJson.getString("rsEndTimeNode"));
					}
					if (!detailJson.isNull("rsTimeMark")) {
						time.setTimeMark(detailJson.getInt("rsTimeMark"));
					}
					if (!detailJson.isNull("rsWeekDay")) {
						int week = detailJson.getInt("rsWeekDay");
						time.setWeek(week);
						time.setDate(StringUtil.getDateByWeek(week+1));
					}
					if (!detailJson.isNull("serviceId")) {
						time.setServiceId(detailJson.getInt("serviceId"));
					}
					list.add(time);
				}
			}
			map.put("list", list);
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	// 确定预约电话医生
	public static Map<String, Object> confirmTelDocOrder(String t) {
		Map<String, Object> map = null;
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
		return map;
	}

	// 立即通话
	public static Map<String, Object> promptTeling(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	// 获取通话记录
	public static Map<String, Object> getTelDocRecord(String t) {
		List<PhoneRecordBean> list = new ArrayList<PhoneRecordBean>();
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			if (json.isNull("data")) {
				map.put("list", list);
				return map;
			}
			String data = json.getString("data");
			json = new JSONObject(data);
			JSONArray rows = json.getJSONArray("rows");
			PhoneRecordBean record = null;
			for (int i = 0; i < rows.length(); i++) {
				json = rows.getJSONObject(i);
				record = new PhoneRecordBean();
				if (!json.isNull("ctdCallRecordid")) {
					record.setId(json.getInt("ctdCallRecordid"));
				}
				if (!json.isNull("ctdCallRecordcallRegisterId")) {
					record.setCallRegisterId(json
							.getInt("ctdCallRecordcallRegisterId"));
				}
				if (!json.isNull("ctdCallRecordcrBeginTime")) {
					record.setBeginTime(json
							.getString("ctdCallRecordcrBeginTime"));
				}
				if (!json.isNull("ctdCallRecordcrEndTime")) {
					record.setEndTime(json.getString("ctdCallRecordcrEndTime"));
				}
				if (!json.isNull("ctdCallRecordcrEztCurrency")) {
					record.setEztCurrency(json
							.getDouble("ctdCallRecordcrEztCurrency"));
				}
				if (!json.isNull("ctdCallRecordcrStatus")) {
					record.setCallStatus(json.getInt("ctdCallRecordcrStatus"));
				}
				if (!json.isNull("ctdCallRecordsendMobile")) {
					record.setReceivePhone(json
							.getString("ctdCallRecordsendMobile"));
				}
				if (!json.isNull("ctdCallRecorddoctorId")) {
					record.setDoctorId(json.getInt("ctdCallRecorddoctorId"));
				}
				if (!json.isNull("regDoctoredName")) {
					record.setDoctorName(json.getString("regDoctoredName"));
				}
				if (!json.isNull("regHospitalehName")) {
					record.setHospital(json.getString("regHospitalehName"));
				}
				if (!json.isNull("ctdCallRecorddeptId")) {
					record.setDeptId(json.getInt("ctdCallRecorddeptId"));
				}
				if (!json.isNull("regDeptdptName")) {
					record.setDept(json.getString("regDeptdptName"));
				}
				if (!json.isNull("regDoctoredPic")) {
					record.setPhoto(json.getString("regDoctoredPic"));
				}
				if (!json.isNull("regDoctoredLevel")) {
					record.setDoctorLevel(json.getInt("regDoctoredLevel"));
				}
				if (!json.isNull("ctdCallRecordcrTimeMinute")) {
					record.setCall_minute(json
							.getString("ctdCallRecordcrTimeMinute"));
				}
				list.add(record);
			}
			map.put("list", list);
		} catch (JSONException e) {
			e.printStackTrace();
			map = new HashMap<String, Object>();
			map.put("flag", false);
			map.put("msg", "服务器异常");
		}
		return map;
	}

	/**
	 * 解析电话医生列表
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> parsePhoneDocList(String t) {
		Map<String, Object> map = null;
		try {
			List<Doctor> doctorList;
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			JSONArray dataJson;
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
				map.put("flag", flag);
				map.put("msg", json.getString("detailMsg"));
				if (!flag || json.isNull("data")) {
					return map;
				}
			}
			JSONObject data = json.getJSONObject("data");
			if (!data.isNull("rows")) {
				dataJson = data.getJSONArray("rows");
				Doctor doctor = null;
				doctorList = new ArrayList<Doctor>();
				for (int i = 0; i < dataJson.length(); i++) {
					json = dataJson.getJSONObject(i);
					doctor = new Doctor();
					if (!json.isNull("regDoctorid")) {
						doctor.setId(json.getString("regDoctorid"));
					}
					if (!json.isNull("regDoctoredName")) {
						doctor.setDocName(json.getString("regDoctoredName"));
					}
					if (!json.isNull("regDoctoredLevel")) {
						doctor.setDocLevel(json.getString("regDoctoredLevel"));
					}
					if (!json.isNull("regHospitalehName")) {
						doctor.setDocHos(json.getString("regHospitalehName"));
					}
					if (!json.isNull("regHospitalid")) {
						doctor.setDocHosId(json.getString("regHospitalid"));
					}
					if (!json.isNull("regDeptdptName")) {
						doctor.setDocDept(json.getString("regDeptdptName"));
					}
					if (!json.isNull("regDeptid")) {
						doctor.setDocDeptId(json.getString("regDeptid"));
					}
					if (!json.isNull("regDeptDocid")) {
						doctor.setDeptDocId(json.getString("regDeptDocid"));
					}
					if (!json.isNull("regDoctoredPic")) {
						doctor.setDocHeadUrl(json.getString("regDoctoredPic"));
					}
					if (!json.isNull("ctdCallGearciGuaranteedRate")) {
						doctor.setFees(json
								.getDouble("ctdCallGearciGuaranteedRate"));
					}
					if (!json.isNull("ctdCallGearciStandardRate")) {// 每分钟收费
						doctor.setMoneyOfMinute(json
								.getDouble("ctdCallGearciStandardRate"));
					}
					if (!json.isNull("ctdCallGearciGuaranteedTime")) {// 最低通话时长
						doctor.setMinTime(json
								.getInt("ctdCallGearciGuaranteedTime"));
					}
					if (!json.isNull("ctdCallInfociYnAppointment")) {
						doctor.setCallDoctorYnAppointment(json
								.getInt("ctdCallInfociYnAppointment"));
					}
					if (!json.isNull("ctdCallInfociYnOnline")) {
						doctor.setCallDoctorYnOnline(json
								.getInt("ctdCallInfociYnOnline"));
					}
					//2015-12-18 医院对接
					if (!json.isNull("ehDockingStatus")) {
						doctor.setEhDockingStatus(json
								.getInt("ehDockingStatus"));
					}
					
					if (!json.isNull("ehDockingStr")) {
						doctor.setEhDockingStr(json
								.getString("ehDockingStr"));
					}
					
					doctorList.add(doctor);
				}
				map.put("doctorList", doctorList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}
