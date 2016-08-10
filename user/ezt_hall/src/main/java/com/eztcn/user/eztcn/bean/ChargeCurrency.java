package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 充值明细
 * @describe
 * @author ezt
 * @created 2015年2月6日
 */
public class ChargeCurrency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int sourceType;// 数据来源类型
	private double eztCurrency;// 医通币
	private int isIncomeExpenditure;// 收入或支出 0收入1支出
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public double getEztCurrency() {
		return eztCurrency;
	}

	public void setEztCurrency(double eztCurrency) {
		this.eztCurrency = eztCurrency;
	}

	public int getIsIncomeExpenditure() {
		return isIncomeExpenditure;
	}

	public void setIsIncomeExpenditure(int isIncomeExpenditure) {
		this.isIncomeExpenditure = isIncomeExpenditure;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
