package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class CallTimeList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int callScheduleId;// 选中id
	private int timeMark;//一天的标志  早 0  中  1  晚 2
	private String beginTime;
	private String endTime;
	private int week;
	private int serviceId;
	private String date;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCallScheduleId() {
		return callScheduleId;
	}

	public void setCallScheduleId(int callScheduleId) {
		this.callScheduleId = callScheduleId;
	}

	public int getTimeMark() {
		return timeMark;
	}

	public void setTimeMark(int timeMark) {
		this.timeMark = timeMark;
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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
