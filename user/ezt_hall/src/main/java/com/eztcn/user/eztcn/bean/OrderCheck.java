package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class OrderCheck implements Serializable{
	/**
	 * 预约检查列表的bean
	 */
	private static final long serialVersionUID = 1L;
	
	private int auditingStatus;
	private String certificatesNumber;
	private String certificatesType;
	private String guysNumebr;
	private String healthSecretary;
	private String hosId;
	private String hospitalName;
	private String id;
	private String orderBusiness;
	private String orderNumber;
	private String orderTime;
	private String patientId;
	private String patientName;
	private String phone;
	private String remark;
	private String sex;
	private String specialRemark;
	private String timeDis;
	private String userId;
	public int getAuditingStatus() {
		return auditingStatus;
	}
	public void setAuditingStatus(int auditingStatus) {
		this.auditingStatus = auditingStatus;
	}
	public String getCertificatesNumber() {
		return certificatesNumber;
	}
	public void setCertificatesNumber(String certificatesNumber) {
		this.certificatesNumber = certificatesNumber;
	}
	public String getCertificatesType() {
		return certificatesType;
	}
	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}
	public String getGuysNumebr() {
		return guysNumebr;
	}
	public void setGuysNumebr(String guysNumebr) {
		this.guysNumebr = guysNumebr;
	}
	public String getHealthSecretary() {
		return healthSecretary;
	}
	public void setHealthSecretary(String healthSecretary) {
		this.healthSecretary = healthSecretary;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderBusiness() {
		return orderBusiness;
	}
	public void setOrderBusiness(String orderBusiness) {
		this.orderBusiness = orderBusiness;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSpecialRemark() {
		return specialRemark;
	}
	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}
	public String getTimeDis() {
		return timeDis;
	}
	public void setTimeDis(String timeDis) {
		this.timeDis = timeDis;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
