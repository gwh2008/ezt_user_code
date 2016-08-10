package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 号池时间实体类
 * @describe
 * @author ezt
 * @created 2015年3月23日
 */
public class PoolTimes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String poolId;// 号池id
	private boolean isRemains;// 是否有号
	private String startDates;// 开始时间
	private String endDates;// 结束时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public boolean isRemains() {
		return isRemains;
	}

	public void setRemains(boolean isRemains) {
		this.isRemains = isRemains;
	}

	public String getStartDates() {
		return startDates;
	}

	public void setStartDates(String startDates) {
		this.startDates = startDates;
	}

	public String getEndDates() {
		return endDates;
	}

	public void setEndDates(String endDates) {
		this.endDates = endDates;
	}

}
