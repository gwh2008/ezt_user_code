package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 优惠券实体
 * @describe
 * @author ezt
 * @created 2015年5月5日
 */
public class Coupons implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String id;
	
	String cardNo;//卡号
	
	String title;//标题
	
	String endDate;//有效期
	
	String money;//金额
	
	int state;//0正常，1使用中，-1已使用，2过期
	

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	

}
