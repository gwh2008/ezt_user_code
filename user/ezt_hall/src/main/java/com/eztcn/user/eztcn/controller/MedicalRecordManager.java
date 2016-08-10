package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.bean.MedicalRecord_ImgType;
import com.eztcn.user.eztcn.bean.Medical_img;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.eztcn.utils.StringUtil;

public class MedicalRecordManager {

	/**
	 * 解析医院列表
	 */
	public static ArrayList<String> getHosList(String t) {
		ArrayList<String> hosList = null;
		try {
			hosList = new ArrayList<String>();
			JSONObject json = new JSONObject(t);
			if (json.isNull("data")) {
				return hosList;
			}
			JSONObject data = json.getJSONObject("data");
			JSONArray rows = data.getJSONArray("rows");
			for (int i = 0; i < rows.length(); i++) {
				json = rows.getJSONObject(i);
				if (!json.isNull("ehName")) {
					hosList.add(json.getString("ehName"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hosList;
	}

	/**
	 * 解析病历ID
	 */
	public static int parseRecord(String t) {
		int mId = 0;
		try {
			JSONObject json = new JSONObject(t);
			if (!json.isNull("data")) {
				mId = json.getInt("data");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mId;
	}

	/**
	 * 解析我的病历列表
	 * 
	 * @param t
	 * @return
	 */
	public static ArrayList<MedicalRecord> getMyIllRecords(String t) {
		ArrayList<MedicalRecord> rList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				JSONArray jsonArray = null;
				if (!ject.isNull("rows")) {
					jsonArray = ject.getJSONArray("rows");
					rList = new ArrayList<MedicalRecord>();
					for (int i = 0; i < jsonArray.length(); i++) {
						MedicalRecord record = new MedicalRecord();
						JSONObject ject1 = jsonArray.getJSONObject(i);

						if (!ject1.isNull("regMedicalRecordshospitalName"))// 医院名称
							record.setHosName(ject1
									.getString("regMedicalRecordshospitalName"));
						if (!ject1.isNull("id"))// id
							record.setId(ject1.getString("id"));
						if (!ject1.isNull("regMedicalRecordscreateTime"))// 创建时间
							record.setCreateTime(ject1
									.getString("regMedicalRecordscreateTime"));
						if (!ject1.isNull("regMedicalRecordspatientId"))
							record.setPatientId(ject1
									.getString("regMedicalRecordspatientId"));
						if (!ject1.isNull("regPatientepName"))// 患者名称
							record.setStrName(ject1
									.getString("regPatientepName"));

						if (!ject1.isNull("regMedicalRecordsmrNum"))// 病历号
							record.setRecordNum(ject1
									.getString("regMedicalRecordsmrNum"));

						if (!ject1.isNull("regPatientepPic"))// 头像
							record.setHeadImgUrl(ject1
									.getString("regPatientepPic"));

						if (!ject1.isNull("regPatientepSex"))// 性别
						{
							if (ject1.getInt("regPatientepSex") == 0) {
								record.setStrSex("男");
							} else if (ject1.getInt("regPatientepSex") == 1) {
								record.setStrSex("女");
							}
						}

						// if (!ject1.isNull("regMedicalRecordsallergy"))//
						// 药物过敏史
						// record.setAllergy(ject1
						// .getString("regMedicalRecordsallergy"));
						//
						// if
						// (!ject1.isNull("regMedicalRecordssymptomsDetail"))//
						// 症状描述
						// record.setSymptomsDetail(ject1
						// .getString("regMedicalRecordssymptomsDetail"));
						//
						// if (!ject1.isNull("regMedicalRecordsdrugDetail"))//
						// 用药描述
						// record.setDrugDetail(ject1
						// .getString("regMedicalRecordsdrugDetail"));
						//
						// if
						// (!ject1.isNull("regMedicalRecordsdiagnosisDetial"))//
						// 检验报告
						// record.setDiagnosisDetial(ject1
						// .getString("regMedicalRecordsdiagnosisDetial"));
						// if (!ject1.isNull("regMedicalRecordsdoctorName")) {
						// record.setDoctorName(ject1
						// .getString("regMedicalRecordsdoctorName"));
						// }
						// if (!ject1.isNull("regMedicalRecordsdiseaseName")) {
						// record.setDisease(ject1
						// .getString("regMedicalRecordsdiseaseName"));
						// }
						rList.add(record);
					}
				}

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					rList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			rList = null;
		}
		return rList;
	}

	/**
	 * 获取我的病历详情
	 * 
	 * @param t
	 * @return
	 */
	public static MedicalRecord getMyIllDetails(String t) {
		MedicalRecord record = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功
				JSONObject ject = (JSONObject) object;
				record = new MedicalRecord();

				JSONObject ject1 = null;
				if (!ject.isNull("releaseBean")) {
					ject1 = ject.getJSONObject("releaseBean");

					if (!ject1.isNull("regMedicalRecordshospitalName"))// 医院名称
						record.setHosName(ject1
								.getString("regMedicalRecordshospitalName"));

					if (!ject1.isNull("id"))// id
						record.setId(ject1.getString("id"));

					if (!ject1.isNull("regMedicalRecordsclinicalTime"))// 就诊时间
					{
						String str = StringUtil.dealWithDate(ject1
								.getString("regMedicalRecordsclinicalTime"));
						record.setClinicalTime(str);
					}

					if (!ject1.isNull("regMedicalRecordspatientId"))
						record.setPatientId(ject1
								.getString("regMedicalRecordspatientId"));
					if (!ject1.isNull("regPatientepName"))// 患者名称
						record.setStrName(ject1.getString("regPatientepName"));

					if (!ject1.isNull("regMedicalRecordsmrNum"))// 病历号
						record.setRecordNum(ject1
								.getString("regMedicalRecordsmrNum"));

					if (!ject1.isNull("regPatientepPic"))// 头像
						record.setHeadImgUrl(ject1.getString("regPatientepPic"));

					if (!ject1.isNull("regPatientepSex"))// 性别
					{
						if (ject1.getInt("regPatientepSex") == 0) {
							record.setStrSex("男");
						} else if (ject1.getInt("regPatientepSex") == 1) {
							record.setStrSex("女");
						}
					}
					if (!ject1.isNull("regMedicalRecordsallergy"))// 药物过敏史
						record.setAllergy(ject1
								.getString("regMedicalRecordsallergy"));

					if (!ject1.isNull("regMedicalRecordssymptomsDetail"))// 症状描述
						record.setSymptomsDetail(ject1
								.getString("regMedicalRecordssymptomsDetail"));

					if (!ject1.isNull("regMedicalRecordsdrugDetail"))// 用药描述
						record.setDrugDetail(ject1
								.getString("regMedicalRecordsdrugDetail"));

					if (!ject1.isNull("regMedicalRecordsallergy"))// 用药描述
						record.setAllergy(ject1
								.getString("regMedicalRecordsallergy"));

					if (!ject1.isNull("regMedicalRecordsdiagnosisDetial"))// 检验报告
						record.setDiagnosisDetial(ject1
								.getString("regMedicalRecordsdiagnosisDetial"));
					if (!ject1.isNull("regMedicalRecordsdoctorName")) {
						record.setDoctorName(ject1
								.getString("regMedicalRecordsdoctorName"));
					}
					if (!ject1.isNull("regMedicalRecordsdiseaseName")) {
						record.setDisease(ject1
								.getString("regMedicalRecordsdiseaseName"));
					}
				}
				if (!ject.isNull("releasePicBean")) {// 图片
					String type = "-2";
					Medical_img imgType = null;
					MedicalRecord_ImgType mr;
					ArrayList<Medical_img> imgList = new ArrayList<Medical_img>();
					// List<MedicalRecord_ImgType> urlList;//2015-12-25
					// 修改病历上传时候图片获取列表异常
					List<MedicalRecord_ImgType> urlList = null;
					JSONArray jsonArry = ject.getJSONArray("releasePicBean");
					// String rpUploadTimeStr = "";// 2015-12-25
					// 修改病历上传时候图片获取列表异常
					for (int i = 0; i < jsonArry.length(); i++) {
						String temp = "0";
						ject1 = jsonArry.getJSONObject(i);
						mr = new MedicalRecord_ImgType();
						if (!ject1.isNull("rpType")) {
							temp = ject1.getString("rpType");
						}

						// if (!ject1.isNull("rpUploadTime")) {
						// if (!rpUploadTimeStr.equals(ject1
						// .getString("rpUploadTime"))) {
						//
						// imgType = new Medical_img();
						// urlList = imgType.getUrlList();
						// } else {
						// urlList = imgType.getUrlList();
						// }
						// }

						if (temp.equals(type)) {// 2015-12-25 修改病历上传时候图片获取列表异常
							urlList = imgType.getUrlList();
						} else {
							imgType = new Medical_img();
							urlList = imgType.getUrlList();
						}

						imgType.setRecordType(temp);
						if (!ject1.isNull("rpPic")) {
							mr.setImgUrl(EZTConfig.IMG_SERVER
									+ "/images/medical/eztcn2.0/"
									+ ject1.getString("rpPic"));
						}
						if (!ject1.isNull("id")) {
							mr.setId(ject1.getInt("id"));
						}
						urlList.add(mr);

						if (!type.equals(temp)) {
							imgList.add(imgType);
						}
						type = temp;

						// if (!ject1.isNull("rpUploadTime")) {
						// if (!rpUploadTimeStr.equals(ject1
						// .getString("rpUploadTime"))) {
						// imgList.add(imgType);
						// }
						// }
						// rpUploadTimeStr = ject1.getString("rpUploadTime");

					}
					record.setImgList(imgList);
				}

			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					record = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			record = null;
		}
		return record;
	}

	/**
	 * 删除病历
	 */
	public static Map<String, Object> delMyIll(String t) {
		Map<String, Object> map = null;
		JSONObject json;
		try {
			map = new HashMap<String, Object>();
			json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			return map;
		} catch (JSONException e) {
			map = new HashMap<String, Object>();
			map.put("flag", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}

	/**
	 * 解析上传病历图片返回信息
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> parseMImage(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(t);
			boolean flag = false;
			if (!json.isNull("flag")) {
				flag = json.getBoolean("flag");
			}
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
			if (!flag) {
				return map;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

}
