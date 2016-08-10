package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 金额实体类
 * @describe
 * @author ezt
 * @created 2015年2月3日
 */
public class Money implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String strMoney;//充值的金额
	
	private String strMyMoney;//平台货币

	public String getStrMoney() {
		return strMoney;
	}

	public void setStrMoney(String strMoney) {
		this.strMoney = strMoney;
	}

	public String getStrMyMoney() {
		return strMyMoney;
	}

	public void setStrMyMoney(String strMyMoney) {
		this.strMyMoney = strMyMoney;
	}

}
