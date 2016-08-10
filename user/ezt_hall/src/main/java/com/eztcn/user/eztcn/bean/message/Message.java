/**
 * 
 */
package com.eztcn.user.eztcn.bean.message;

import java.io.Serializable;

/**
 * @author Liu Gang
 * 
 *         2016年4月6日 上午11:30:04
 * 
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5073246373567208700L;
	private int id;
	private String userId;
	private int iconType;
	private int type;
	private String titleStr;
	private String contentStr;
	private String dateStr;
	private int infoSysType;

	public int getIconType() {
		return iconType;
	}

	public void setIconType(int iconType) {
		this.iconType = iconType;
	}

	public String getTitleStr() {
		return titleStr;
	}

	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
	}

	public String getContentStr() {
		return contentStr;
	}

	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getInfoSysType() {
		return infoSysType;
	}

	public void setInfoSysType(int infoSysType) {
		this.infoSysType = infoSysType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
