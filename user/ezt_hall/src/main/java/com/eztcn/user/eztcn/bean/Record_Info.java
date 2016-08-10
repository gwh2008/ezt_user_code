package com.eztcn.user.eztcn.bean;
import java.io.Serializable;
/**
 * @title 预约管理
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */

public class Record_Info implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;// 挂号ID
	private String doctorName;//主诊医生。
	private String date;//日期
	private Integer doctorLevel;//医生等级。
	private String dept;//科室
	private String beginTime;//开始时间
	private String endTime;//结束时间
	private Integer doctorId;//医生nnnnn
	private String hospital;//医院
	private Integer deptId;//科室id
	private String photo;//医生头像地址。
	private String phone;//就诊人电话。
	private String patientName;// 就诊人姓名
	private String idCard;// 身份证号
	private String discribe;// 描述
	private Integer payType;// 缴费方式0是自费1是医保
	private Integer platformId;// 平台ID
	private boolean leaveSign;// 是否有号
	private String regTime;//就诊时间
	private double regDeptDocedFree;//挂号费用。

	public int getRrStatus() {
		return rrStatus;
	}

	public void setRrStatus(int rrStatus) {
		this.rrStatus = rrStatus;
	}

	private int rrStatus;//订单的状态。


	public double getRegDeptDocedFree() {
		return regDeptDocedFree;
	}

	public void setRegDeptDocedFree(double regDeptDocedFree) {
		this.regDeptDocedFree = regDeptDocedFree;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	
	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

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

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public boolean isLeaveSign() {
		return leaveSign;
	}

	public void setLeaveSign(boolean leaveSign) {
		this.leaveSign = leaveSign;
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getDiscribe() {
		return discribe;
	}

	public void setDiscribe(String discribe) {
		this.discribe = discribe;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
}
