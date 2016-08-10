package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 轻陪诊实体
 * @describe
 * @author ezt
 * @created 2015年3月30日
 */
public class LightAccompanying implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String id;// id

	String itemId;

	String remark;// 服务内容

	String process;// 服务流程

	String notice;// 服务须知

	String itemName;// 服务标题
	
	private String appUsage;//服务使用 分类

	private Integer remainNums;// 剩余次数

	private int itemStatus;// 使用状态（0可使用，1已使用，2使用中）
	
	

	public String getAppUsage() {
		return appUsage;
	}

	public void setAppUsage(String appUsage) {
		this.appUsage = appUsage;
	}

	public int getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getRemainNums() {
		if (remainNums == null) {
			return 0;
		}
		return remainNums;
	}

	public void setRemainNums(Integer remainNums) {
		this.remainNums = remainNums;
	}

}
