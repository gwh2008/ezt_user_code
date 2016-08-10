/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liu Gang
 * 
 *         2015年11月26日 上午9:47:48 龙卡
 */
public class DragonCard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 激活时间
	 */
	private String activeDate;
	private String endDate;
	private String levelNum;
	private String remindNum;
	private String openCard;
	/**
	 * 建行卡号
	 */
	private String bankCardId;
	/**
	 * 办卡用户
	 */
	private EztUser user;
	/**
	 * 办卡时间
	 */
	private String hasDate;
	/**
	 * Id值
	 */
	private String id;
	/**
	 * 缴费标示，1标示已缴费，0标示未缴费
	 */
	private Integer payFlag;
	/**
	 * 保留字段
	 */
	private String reserve;
	/**
	 * 状态 0标示已激活 1标示作废2标示禁用 3标示已销卡
	 */
	private Integer status;
	private List<LightAccompanying> serverList;
	/**
	 * 交易码
	 */
	private String transCode;
	/**
	 * 有效时间
	 */
	private String valid;
	private String guideNum;

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public EztUser getUser() {
		return user;
	}

	public void setUser(EztUser user) {
		this.user = user;
	}

	public String getHasDate() {
		return hasDate;
	}

	// public void setHasDate(String hasDate) {
	// this.hasDate = hasDate;
	// }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(Integer payFlag) {
		this.payFlag = payFlag;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<LightAccompanying> getServerList() {
		return serverList;
	}

	public void setServerList(List<LightAccompanying> server) {
		this.serverList = server;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getGuideNum() {
		return guideNum;
	}

	public void setGuideNum(String guideNum) {
		this.guideNum = guideNum;
	}

	public String getLevelNum() {
		return levelNum;
	}

	public void setLevelNum(String levelNum) {
		this.levelNum = levelNum;
	}

	public String getRemindNum() {
		return remindNum;
	}

	public void setRemindNum(String remindNum) {
		this.remindNum = remindNum;
	}

	public String getOpenCard() {
		return openCard;
	}

	public void setOpenCard(String openCard) {
		this.openCard = openCard;
	}

}
