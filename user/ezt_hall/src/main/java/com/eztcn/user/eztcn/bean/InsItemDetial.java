/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @author Liu Gang 检验项目详情 2015年11月25日 上午11:22:50
 * 
 */
public class InsItemDetial implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 出报告单时间
	private String confirmTime;
	// 大检验项名称
	private String itemName;
	// 大检验项ID
	private String sampId;
	// 小检验项编号
	private String sampSn;
	// 小检验项名称
	private String detailName;
	// 标准值范围
	private String standardValue;
	// 检验结果
	private String sampResult;

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSampId() {
		return sampId;
	}

	public void setSampId(String sampId) {
		this.sampId = sampId;
	}

	public String getSampSn() {
		return sampSn;
	}

	public void setSampSn(String sampSn) {
		this.sampSn = sampSn;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(String standardValue) {
		this.standardValue = standardValue;
	}

	public String getSampResult() {
		return sampResult;
	}

	public void setSampResult(String sampResult) {
		this.sampResult = sampResult;
	}

}
