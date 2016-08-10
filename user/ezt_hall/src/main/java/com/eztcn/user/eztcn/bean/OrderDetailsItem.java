package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class OrderDetailsItem implements Serializable{
	
    /**
	 * 约检查订单的详情的list的item的bean。
	 */
	private static final long serialVersionUID = 1L;
	private int flag;
    private String address;
	private String baseUse;
	private String checkId;
	private String checkName;
	private String forceUse;
	private String hosId;
	private String hosName;
	private String id;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBaseUse() {
		return baseUse;
	}
	public void setBaseUse(String baseUse) {
		this.baseUse = baseUse;
	}
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getForceUse() {
		return forceUse;
	}
	public void setForceUse(String forceUse) {
		this.forceUse = forceUse;
	}
	public String getHosId() {
		return hosId;
	}
	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	public String getHosName() {
		return hosName;
	}
	public void setHosName(String hosName) {
		this.hosName = hosName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRejection() {
		return rejection;
	}
	public void setRejection(String rejection) {
		this.rejection = rejection;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String price;
	private String rejection;
	private String remark;
    	
    	

}
