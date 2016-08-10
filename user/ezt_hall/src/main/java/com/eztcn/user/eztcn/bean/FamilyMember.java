package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 家庭成员实体
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class FamilyMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String patientId;// 患者id
	private int userId;// 用户id
	private int familyId;// 家庭成员id
	private String memberName;// 成员名称
	private int relation;// 成员关系
	private int sex;// 性别
	private int age;// 年龄
	private String idCard;// 身份证id
	private String phone;// 电话号码
	private String medicalNo;// 医保号
	private String familyPhoto;// 成员头像
	private int mainUser;// 是否主用户(0、否 1、是)

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public FamilyMember() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFamilyId() {
		return familyId;
	}

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMedicalNo() {
		return medicalNo;
	}

	public void setMedicalNo(String medicalNo) {
		this.medicalNo = medicalNo;
	}

	public String getFamilyPhoto() {
		return familyPhoto;
	}

	public void setFamilyPhoto(String familyPhoto) {
		this.familyPhoto = familyPhoto;
	}

	public int getMainUser() {
		return mainUser;
	}

	public void setMainUser(int mainUser) {
		this.mainUser = mainUser;
	}
}
