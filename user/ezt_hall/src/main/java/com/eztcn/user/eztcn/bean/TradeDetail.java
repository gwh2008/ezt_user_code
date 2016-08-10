package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title	交易明细
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class TradeDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String tradeName;// 交易名称
	private Double tradeMoney;// 交易金额
	private int tradeType;// 交易类型(1、语音图文咨询 2、电话咨询 3、充值)
	private String person;// 交易人姓名
	private String tradeTime;//交易日期
	private String calledTime;// 通话时长
	private int calledType;// 通话类型(按分钟)

	public TradeDetail(int id, String tradeName, Double tradeMoney,
			int tradeType, String person, String tradeTime, String calledTime,
			int calledType) {
		super();
		this.id = id;
		this.tradeName = tradeName;
		this.tradeMoney = tradeMoney;
		this.tradeType = tradeType;
		this.person = person;
		this.tradeTime = tradeTime;
		this.calledTime = calledTime;
		this.calledType = calledType;
	}

	public TradeDetail() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public Double getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(Double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public int getTradeType() {
		return tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getCalledTime() {
		return calledTime;
	}

	public void setCalledTime(String calledTime) {
		this.calledTime = calledTime;
	}

	public int getCalledType() {
		return calledType;
	}

	public void setCalledType(int calledType) {
		this.calledType = calledType;
	}

}
