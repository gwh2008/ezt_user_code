package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @title 号池实体
 * @describe
 * @author ezt
 * @created 2014年12月22日
 */
public class Pool implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int remain;// (-1 没放号，0为预约已满，1有号)

	private String DateWeek;// 对应的星期

	private String Date;// 对应的日期

	private String RegMark;// 参数标志

	private String PfId;// 平台id

	private String docId;// 医生id

	private String hosId;// 医生所属医院id

	private List<PoolTimes> timeList;

	public int getRemain() {
		return remain;
	}

	public void setRemain(int remain) {
		this.remain = remain;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getDateWeek() {
		return DateWeek;
	}

	public void setDateWeek(String dateWeek) {
		DateWeek = dateWeek;
	}

	public String getRegMark() {
		return RegMark;
	}

	public void setRegMark(String regMark) {
		RegMark = regMark;
	}

	public String getPfId() {
		return PfId;
	}

	public void setPfId(String pfId) {
		PfId = pfId;
	}

	public List<PoolTimes> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<PoolTimes> timeList) {
		this.timeList = timeList;
	}

}
