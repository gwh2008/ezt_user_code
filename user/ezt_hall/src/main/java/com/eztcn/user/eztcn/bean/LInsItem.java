/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @author Liu Gang
 * 
 *         2015年11月25日 上午11:15:04 大检验项
 */
public class LInsItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 患者编号
	private String patientNo;
	// 患者姓名
	private String patientName;
	// 身份证号
	private String patientIdNo;
	// 手机号
	private String patientMobile;
	// 状态(住院/门诊)
	private String state;
	// 出报告单时间
	private String timeStr;
	// 大检验项ID
	private String itemId;
	// 大检验项名称
	private String itemName;

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientIdNo() {
		return patientIdNo;
	}

	public void setPatientIdNo(String patientIdNo) {
		this.patientIdNo = patientIdNo;
	}

	public String getPatientMobile() {
		return patientMobile;
	}

	public void setPatientMobile(String patientMobile) {
		this.patientMobile = patientMobile;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getbItemId() {
		return itemId;
	}

	public void setbItemId(String bItemId) {
		this.itemId = bItemId;
	}

	public String getbItemName() {
		return itemName;
	}

	public void setbItemName(String bItemName) {
		this.itemName = bItemName;
	}

}
