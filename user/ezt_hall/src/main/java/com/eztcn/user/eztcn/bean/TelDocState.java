package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * @title 电话医生状态实体类
 * @describe 开通状态、相关收费
 * @author ezt
 * @created 2015年1月27日
 */
public class TelDocState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;//

	private int isOpenService;// 是否开通服务

	private int ynAppointment;// 是否可预约

	private int ynOnline;// 是否在线

	private int ciGearDeep;// 档位级别

	private String ciGuaranteedRate;// 保底收费

	private String ciGuaranteedTime;// 保底时间

	private String ciName;// 资费档位名称

	private String ciStandardRate;// 标准收费

	private String ciStandardTime;// 标准时间 默认1分钟/例

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsOpenService() {
		return isOpenService;
	}

	public void setIsOpenService(int isOpenService) {
		this.isOpenService = isOpenService;
	}

	public int getYnAppointment() {
		return ynAppointment;
	}

	public void setYnAppointment(int ynAppointment) {
		this.ynAppointment = ynAppointment;
	}

	public int getYnOnline() {
		return ynOnline;
	}

	public void setYnOnline(int ynOnline) {
		this.ynOnline = ynOnline;
	}

	public int getCiGearDeep() {
		return ciGearDeep;
	}

	public void setCiGearDeep(int ciGearDeep) {
		this.ciGearDeep = ciGearDeep;
	}

	public String getCiGuaranteedRate() {
		if (TextUtils.isEmpty(ciGuaranteedRate)) {
			ciGuaranteedRate = "0.0";
		}
		return ciGuaranteedRate;
	}

	public void setCiGuaranteedRate(String ciGuaranteedRate) {
		this.ciGuaranteedRate = ciGuaranteedRate;
	}

	public String getCiGuaranteedTime() {
		if (TextUtils.isEmpty(ciGuaranteedTime)) {
			ciGuaranteedTime = "0";
		}
		return ciGuaranteedTime;
	}

	public void setCiGuaranteedTime(String ciGuaranteedTime) {
		this.ciGuaranteedTime = ciGuaranteedTime;
	}

	public String getCiName() {
		return ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	public String getCiStandardRate() {
		if (TextUtils.isEmpty(ciStandardRate)) {
			ciStandardRate = "0.0";
		}
		return ciStandardRate;
	}

	public void setCiStandardRate(String ciStandardRate) {
		this.ciStandardRate = ciStandardRate;
	}

	public String getCiStandardTime() {
		return ciStandardTime;
	}

	public void setCiStandardTime(String ciStandardTime) {
		this.ciStandardTime = ciStandardTime;
	}

}
