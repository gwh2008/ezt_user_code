package com.eztcn.user.eztcn.bean;

/**
 * @title 具体预约时间
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class OrderTime_Info {
	private Integer id;
	private Integer dlLeaveNum;
	private Integer dlSid;
	private String dlBeginTime;
	private Integer doctorId;
	private String dlPlanDay;
	private String dlEndTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDlLeaveNum() {
		return dlLeaveNum;
	}
	public void setDlLeaveNum(Integer dlLeaveNum) {
		this.dlLeaveNum = dlLeaveNum;
	}
	public Integer getDlSid() {
		return dlSid;
	}
	public void setDlSid(Integer dlSid) {
		this.dlSid = dlSid;
	}
	public String getDlBeginTime() {
		return dlBeginTime;
	}
	public void setDlBeginTime(String dlBeginTime) {
		this.dlBeginTime = dlBeginTime;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public String getDlPlanDay() {
		return dlPlanDay;
	}
	public void setDlPlanDay(String dlPlanDay) {
		this.dlPlanDay = dlPlanDay;
	}
	public String getDlEndTime() {
		return dlEndTime;
	}
	public void setDlEndTime(String dlEndTime) {
		this.dlEndTime = dlEndTime;
	}

}
