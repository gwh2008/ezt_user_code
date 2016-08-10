package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 会员VIP等级
 * @describe
 * @author ezt
 * @created 2015年6月30日
 */
public class MemberLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String egName;
	private String egRemark;// 描述
	private int egType;// 用户类型(患者、医生)
	private int egValue;// 等级所需成长值

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEgName() {
		return egName;
	}

	public void setEgName(String egName) {
		this.egName = egName;
	}

	public String getEgRemark() {
		return egRemark;
	}

	public void setEgRemark(String egRemark) {
		this.egRemark = egRemark;
	}

	public int getEgType() {
		return egType;
	}

	public void setEgType(int egType) {
		this.egType = egType;
	}

	public int getEgValue() {
		return egValue;
	}

	public void setEgValue(int egValue) {
		this.egValue = egValue;
	}

}
