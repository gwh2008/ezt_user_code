package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 预约登记实体类
 * @describe
 * @author ezt
 * @created 2015年3月24日
 */
public class OrderRegisterRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String userId;// 患者用户id
	private String patientId;// 患者id
	private String patientName;
	private int sex;
	private String mobile;
	private String orderDate;// 期望就诊时间
	private String createTime;// 登记时间
	private Integer replyCount;// 回复（抢单）人数
	private String city;
	private String county;
	private String dept;
	private int payType;// 缴费方式
	private int seeDocStatus;// 就诊状态
	private String medicalNum;// 病历
	private String illDiscribe;// 病情描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public Integer getReplyCount() {
		if (replyCount == null) {
			replyCount = 0;
		}
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getSeeDocStatus() {
		return seeDocStatus;
	}

	public void setSeeDocStatus(int seeDocStatus) {
		this.seeDocStatus = seeDocStatus;
	}

	public String getMedicalNum() {
		return medicalNum;
	}

	public void setMedicalNum(String medicalNum) {
		this.medicalNum = medicalNum;
	}

	public String getIllDiscribe() {
		return illDiscribe;
	}

	public void setIllDiscribe(String illDiscribe) {
		this.illDiscribe = illDiscribe;
	}

}
