/**
 * 
 */
package com.eztcn.user.eztcn.bean.ordercheck;

import java.io.Serializable;
import java.util.List;

import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @author Liu Gang
 * 
 *         2016年3月14日 下午3:10:19 预约检查项目实体
 */
public class CheckOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Guider guider;
	private String userId;
	private String patientId;
	private String timeDis;
	private Hospital hospital;
	private String certificatesType;
	private String orderBusiness;
	private  EztUser eztUser;
	private String certificatesNumber;
	/**
	 * 花费
	 */
	private String costStr;
	/**
	 * 就诊人姓名
	 */
	private String patientName;
	/**
	 * 就诊人联系电话
	 */
	private String patientMobile;
	/**
	 * 预约要求
	 */
	private String orderStr;
	/**
	 * 全部的注意事项
	 */
	private List<String> caresStr;
	/**
	 * 订单状态 步骤
	 */
	private int orderState;
	/**
	 * 订单编号
	 */
	private String orderNum;
	private List<String> checkIds;
	private String id;
	private String orderDateTime;

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCostStr() {
		return costStr;
	}

	public void setCostStr(String costStr) {
		this.costStr = costStr;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientMobile() {
		return patientMobile;
	}

	public void setPatientMobile(String patientMobile) {
		this.patientMobile = patientMobile;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public List<String> getCaresStr() {
		return caresStr;
	}

	public void setCaresStr(List<String> caresStr) {
		this.caresStr = caresStr;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public List<String> getCheckIds() {
		return checkIds;
	}

	public void setCheckIds(List<String> checkIds) {
		this.checkIds = checkIds;
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

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Guider getGuider() {
		return guider;
	}

	public void setGuider(Guider guider) {
		this.guider = guider;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getCertificatesType() {
		return certificatesType;
	}

	public void setCertificatesType(String certificatesType) {
		this.certificatesType = certificatesType;
	}

	public String getCertificatesNumber() {
		return certificatesNumber;
	}

	public void setCertificatesNumber(String certificatesNumber) {
		this.certificatesNumber = certificatesNumber;
	}

	public String getOrderBusiness() {
		return orderBusiness;
	}

	public void setOrderBusiness(String orderBusiness) {
		this.orderBusiness = orderBusiness;
	}

	public EztUser getEztUser() {
		return eztUser;
	}

	public void setEztUser(EztUser eztUser) {
		this.eztUser = eztUser;
	}

}
