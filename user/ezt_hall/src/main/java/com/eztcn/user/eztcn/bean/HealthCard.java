package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * @title 健康卡实体
 * @describe
 * @author ezt
 * @created 2015年3月31日
 */
public class HealthCard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String cardNum; // 卡号

	private int state;// 使用状态

	private String activeTime;// 激活时间

	private String cardName;// 健康卡名称

	private String hcEndValidity;// 截止有效期

	private String hcBeginServiceValidity;// 服务开始有效期

	private String cardCover;// 服务卡封面图片

	private Double cardPrice;// 服务卡价格

	private String service_items;// 服务项

	private String service_notice;// 服务须知

	private String service_process;// 服务流程

	public String getHcBeginServiceValidity() {
		return hcBeginServiceValidity;
	}

	public void setHcBeginServiceValidity(String hcBeginServiceValidity) {
		this.hcBeginServiceValidity = hcBeginServiceValidity;
	}

	public String getHcEndValidity() {
		return hcEndValidity;
	}

	public void setHcEndValidity(String hcEndValidity) {
		this.hcEndValidity = hcEndValidity;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getCardCover() {
		return cardCover;
	}

	public void setCardCover(String cardCover) {
		this.cardCover = cardCover;
	}

	public Double getCardPrice() {
		if (cardPrice == null) {
			cardPrice = 0d;
		}
		return cardPrice;
	}

	public void setCardPrice(Double cardPrice) {
		this.cardPrice = cardPrice;
	}

	public String getService_items() {
		return service_items;
	}

	public void setService_items(String service_items) {
		this.service_items = service_items;
	}

	public String getService_notice() {
		return service_notice;
	}

	public void setService_notice(String service_notice) {
		this.service_notice = service_notice;
	}

	public String getService_process() {
		return service_process;
	}

	public void setService_process(String service_process) {
		this.service_process = service_process;
	}

}
