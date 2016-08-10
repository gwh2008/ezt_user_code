package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @title 我的病历实体
 * @describe
 * @author ezt
 * @created 2015年3月18日
 */
public class MedicalRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String recordNum;// 病历号

	private String createTime;// 创建时间

	private String clinicalTime;// 就诊时间

	private String patientId;// 患者id

	private String strName;// 患者名称

	private String hosName;// 医院名称

	private String doctorName;// 医生姓名

	private String headImgUrl;// 患者头像url

	private String strSex;// 性别

	private String allergy;// 药物过敏史

	private String symptomsDetail;// 症状描述

	private String drugDetail;// 用药描述

	private String diagnosisDetial;// 检验报告

	private String disease;// 所犯疾病

	private ArrayList<Medical_img> imgList;// 病历分类图片

	public String getClinicalTime() {
		return clinicalTime;
	}

	public void setClinicalTime(String clinicalTime) {
		this.clinicalTime = clinicalTime;
	}

	public String getStrSex() {
		return strSex;
	}

	public void setStrSex(String strSex) {
		this.strSex = strSex;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public String getSymptomsDetail() {
		return symptomsDetail;
	}

	public void setSymptomsDetail(String symptomsDetail) {
		this.symptomsDetail = symptomsDetail;
	}

	public String getDrugDetail() {
		return drugDetail;
	}

	public void setDrugDetail(String drugDetail) {
		this.drugDetail = drugDetail;
	}

	public String getDiagnosisDetial() {
		return diagnosisDetial;
	}

	public void setDiagnosisDetial(String diagnosisDetial) {
		this.diagnosisDetial = diagnosisDetial;
	}

	public ArrayList<Medical_img> getImgList() {
		if (imgList == null) {
			imgList = new ArrayList<Medical_img>();
		}
		return imgList;
	}

	public void setImgList(ArrayList<Medical_img> imgList) {
		this.imgList = imgList;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

}
