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

import android.net.ParseException;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.Advertise;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.utils.FileUtils;

import xutils.DbUtils;
import xutils.db.sqlite.WhereBuilder;

/**
 * @title 用户管理
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class UserManager {

	/**
	 * 解析服务器返回广告信息
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> getAdList(String t) {
		Map<String, Object> map = null;
		List<Advertise> list = new ArrayList<Advertise>();
		JSONObject json;
		JSONObject dataJson;
		JSONArray rowJson = null;
		String data;
		Advertise ad = null;
		try {
			map = new HashMap<String, Object>();
			json = new JSONObject(t);
			data = json.getString("data");
			dataJson = new JSONObject(data);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
				return map;
			}
			rowJson = dataJson.getJSONArray("rows");
			for (int i = 0; i < rowJson.length(); i++) {
				dataJson = rowJson.getJSONObject(i);
				ad = new Advertise();
				if (!dataJson.isNull("id")) {
					ad.setId(dataJson.getString("id"));
				}
				if (!dataJson.isNull("eaHdPic")) {
					ad.setEaHdPic(dataJson.getString("eaHdPic"));
				}
				if (!dataJson.isNull("eaPic")) {
					ad.setEaPic(dataJson.getString("eaPic"));
				}
				if (!dataJson.isNull("eaUrl")) {
					ad.setEaUrl(dataJson.getString("eaUrl"));
				}
				if (!dataJson.isNull("createTime")) {
					ad.setCreateTime(dataJson.getString("createTime"));
				}
				list.add(ad);
			}
			map.put("ad", list);
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析服务器返回信息(登录)
	 *//*
	public static Map<String, Object> parseLoginJson(String t) {
		Map<String, Object> map = null;
		JSONObject json;
		JSONObject userJson;
		JSONObject patientJson = null;
		PatientBean patientBean;
		String data;
		try {

			patientBean = new PatientBean();
			map = new HashMap<String, Object>();
			json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
			data = json.getString("data");
			JSONObject dataJson = new JSONObject(data);
			// 患者信息
			if (!dataJson.isNull("patientBean")) {
				patientJson = new JSONObject(dataJson.getString("patientBean"));
				if (!patientJson.isNull("id")) {
					patientBean.setId(patientJson.getString("id"));
				}
				if (!patientJson.isNull("epPic")) {
					patientBean.setPhoto(patientJson.getString("epPic"));
				}
				if (!patientJson.isNull("epName")) {
					patientBean.setUserName(patientJson.getString("epName"));
				}
				if (!patientJson.isNull("epSex")) {
					patientBean.setSex(patientJson.getInt("epSex"));
				}
				if (!patientJson.isNull("epPid")) {
					patientBean.setIdCard(patientJson.getString("epPid"));
				}
				if (!patientJson.isNull("epProvince")) {
					patientBean.setProvince(patientJson.getInt("epProvince"));
				}
				if (!patientJson.isNull("epAge")) {
					patientBean.setAge(patientJson.getInt("epAge"));
				}
				if (!patientJson.isNull("epBirthday")) {
					patientBean.setBirthday(patientJson.getString("epBirthday"));
				}
				if (!patientJson.isNull("epHiid")) {
					patientBean.setMedicalNo(patientJson.getString("epHiid"));
				}
				if (!patientJson.isNull("epWedlock")) {
					patientBean.setWedLock(patientJson.getInt("epWedlock"));
				}
				if (!patientJson.isNull("epProfession")) {
					patientBean.setProfession(patientJson.getInt("epProfession"));
				}
				if (!patientJson.isNull("epCulturalLeave")) {
					patientBean.setCulturalLeave(patientJson.getInt("epCulturalLeave"));
				}
				if (!patientJson.isNull("epAddress")) {
					patientBean.setAddress(patientJson.getString("epAddress"));
				}
				if (!patientJson.isNull("epMobile")) {
					patientBean.setOtherPhone(patientJson.getString("epMobile"));
				}
			}
			// 会员信息
			userJson = new JSONObject(dataJson.getString("userbean"));
			if (!userJson.isNull("id")) {
				user.setUserId(userJson.getInt("id"));
			}
			if (!userJson.isNull("euNumber")) {
				user.setUserNo(userJson.getString("euNumber"));
			}
			if (!userJson.isNull("euPassword")) {
				user.setPassword(userJson.getString("euPassword"));
			}
			if (!userJson.isNull("euMobile")) {
				user.setMobile(userJson.getString("euMobile"));
			}
			if (!userJson.isNull("euNickName")) {
				user.setNickName(userJson.getString("euNickName"));
			}
			if (!userJson.isNull("euPGradeUserId")) {// 会员等级id
				user.setmLevel_id(userJson.getInt("euPGradeUserId"));
			}
			if (!userJson.isNull("euPRecordTotal")) {// 会员成长值
				user.setGrowthValue(userJson.getInt("euPRecordTotal"));
			}
			if (!userJson.isNull("euPRecord")) {// 会员积分
				user.setMemberIntegral(userJson.getInt("euPRecord"));
			}

			if (!userJson.isNull("createtime")) {// 创建时间
				String strCreateTime = userJson.getString("createtime");
				String strStartTime = EZTConfig.DIDI_DATE;
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				try {
					Date d1 = sdf.parse(strStartTime);
					Date d2 = sdf.parse(strCreateTime);
					if (d2.after(d1)) {// 创建时间
						user.setFirstReg(true);
					} else {
						user.setFirstReg(false);
					}
				} catch (java.text.ParseException e) {
					e.printStackTrace();
					user.setFirstReg(false);
				}

			}

			// 会员vip等级
			if (!dataJson.isNull("gradeuserList")) {
				JSONArray level = new JSONArray(
						dataJson.getString("gradeuserList"));
				SystemPreferences.save("mlevel_length", level.length());
				for (int i = 0; i < level.length(); i++) {
					userJson = level.getJSONObject(i);
					SystemPreferences.save("memberLevel_" + i,
							userJson.toString());
				}
			}
//			//将user对象存入数据库,替换数据库中的user对象的数据
//			EztDb eztdb=null;
//			if(null!=BaseApplication.getInstance()){
//				eztdb=EztDb.getInstance(BaseApplication.getInstance());
//				WhereBuilder builder=null;
//				ArrayList<EztUser> usersInDb=eztdb.queryAll(new EztUser(), builder, null);
//				if(null!=usersInDb&&usersInDb.size()>0){
//					eztdb.delItemData(usersInDb);
//				}
//				eztdb.save(user);
//			}
			BaseApplication.patient = user;
			map.put("eztUser", user);
		} catch (JSONException e) {
			map = new HashMap<String, Object>();
			map.put("flag", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}*/

	/**
	 * 解析服务器返回信息
	 */
	public static Map<String, Object> parseHttpJson(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			// 找回密码验证码 userid返回
			if (!json.isNull("data")) {
				map.put("userId", json.getString("data"));
			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 存储数据字典
	 * 
	 * @param t
	 */
	public static ArrayList<EztDictionary> getDictionary(String t) {
		ArrayList<EztDictionary> dicList = new ArrayList<EztDictionary>();
		EztDictionary dictionary;
		try {
			JSONArray json = new JSONArray(t.toString());
			JSONArray labelJson;
			JSONObject obj;
			JSONObject labelObj;
			String cnName = null;
			String enName = null;
			for (int i = 0; i < json.length(); i++) {
				obj = json.getJSONObject(i);
				if (!obj.isNull("cnName")) {
					cnName = obj.getString("cnName");
				}
				if (!obj.isNull("enName")) {
					enName = obj.getString("enName");
				}
				if (!obj.isNull("labelValues")) {
					labelJson = obj.getJSONArray("labelValues");
					for (int j = 0; j < labelJson.length(); j++) {
						labelObj = labelJson.getJSONObject(j);
						dictionary = new EztDictionary();
						dictionary.setCnName(cnName);
						dictionary.setEnName(enName);
						if (!labelObj.isNull("value")) {
							dictionary.setValue(labelObj.getString("value"));
						}
						if (!labelObj.isNull("label")) {
							dictionary.setLabel(labelObj.getString("label"));
						}
						dicList.add(dictionary);

					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dicList;
	}

	/**
	 * 解析家庭成员列表
	 */
	public static ArrayList<FamilyMember> getFamilyMember(String t) {
		ArrayList<FamilyMember> memberList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONArray) {// 成功
				JSONArray jArray = (JSONArray) object;
				memberList = new ArrayList<FamilyMember>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					FamilyMember member = new FamilyMember();

					if (!ject.isNull("family")) {
						JSONObject familyJect = ject.getJSONObject("family");

						if (!familyJect.isNull("kinship")) {
							member.setRelation(familyJect.getInt("kinship"));
						}
						if (!familyJect.isNull("id")) {
							member.setFamilyId(familyJect.getInt("id"));
						}
						if (!familyJect.isNull("epPic")) {
							member.setFamilyPhoto(familyJect.getString("epPic"));
						}
						if (!familyJect.isNull("isMain")) {
							member.setMainUser(familyJect.getInt("isMain"));
						}
					}

					if (!ject.isNull("patient")) {
						JSONObject patientJect = ject.getJSONObject("patient");

						if (!patientJect.isNull("epName")) {
							member.setMemberName(patientJect
									.getString("epName"));
						}
						if (!patientJect.isNull("id")) {
							member.setPatientId(patientJect.getString("id"));
						}

						if (!patientJect.isNull("userId")) {
							member.setUserId(patientJect.getInt("userId"));
						}

						if (!patientJect.isNull("epSex")) {
							member.setSex(patientJect.getInt("epSex"));
						}

						if (!patientJect.isNull("epAge")) {
							member.setAge(patientJect.getInt("epAge"));
						}

						if (!patientJect.isNull("epPid")) {
							member.setIdCard(patientJect.getString("epPid"));
						}

						if (!patientJect.isNull("epMobile")) {
							member.setPhone(patientJect.getString("epMobile"));
						}
						if (!patientJect.isNull("epHiid")) {
							member.setMedicalNo(patientJect.getString("epHiid"));
						}

					}
					memberList.add(member);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					memberList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			memberList = null;
		}
		return memberList;
	}

	/**
	 * 解析请愿返回信息
	 */
	public static Map<String, Object> addPetition(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!flag) {
				if (!json.isNull("detailMsg")) {
					map.put("msg", json.getString("detailMsg"));
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

}
