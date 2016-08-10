package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 通话记录
 * @describe
 * @author ezt
 * @created 2015年2月8日
 */
public class PhoneRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;// ID
	private String doctorName;
	private String date;
	private Integer doctorLevel;
	private String dept;
	private String beginTime;
	private String endTime;
	private String call_minute;// 通话计时
	private Integer doctorId;
	private String hospital;
	private Integer deptId;
	private String photo;
	private int callRegisterId;// 电话医生预约ID
	private Integer callStatus;// 通话状态
	private double eztCurrency;// 消费医通币
	private int yn_evaluate;// 是否评价
	private int yn_deduction;// 是否扣费
	private int gear_id;// 收费档位ID
	private String receivePhone;// 接听方手机

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getDoctorLevel() {
		return doctorLevel;
	}

	public void setDoctorLevel(Integer doctorLevel) {
		this.doctorLevel = doctorLevel;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCall_minute() {
		return call_minute;
	}

	public void setCall_minute(String call_minute) {
		this.call_minute = call_minute;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getCallRegisterId() {
		return callRegisterId;
	}

	public void setCallRegisterId(int callRegisterId) {
		this.callRegisterId = callRegisterId;
	}

	public Integer getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Integer callStatus) {
		this.callStatus = callStatus;
	}

	public double getEztCurrency() {
		return eztCurrency;
	}

	public void setEztCurrency(double eztCurrency) {
		this.eztCurrency = eztCurrency;
	}

	public int getYn_evaluate() {
		return yn_evaluate;
	}

	public void setYn_evaluate(int yn_evaluate) {
		this.yn_evaluate = yn_evaluate;
	}

	public int getYn_deduction() {
		return yn_deduction;
	}

	public void setYn_deduction(int yn_deduction) {
		this.yn_deduction = yn_deduction;
	}

	public int getGear_id() {
		return gear_id;
	}

	public void setGear_id(int gear_id) {
		this.gear_id = gear_id;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

}
