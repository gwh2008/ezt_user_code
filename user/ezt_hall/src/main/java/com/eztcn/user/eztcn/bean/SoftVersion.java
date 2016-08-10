package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class SoftVersion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int versionNum;
	private String versionName;
	private String content;
	private String time;
	private int force;// 是否强制更新
	private int type;
	private String url;

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

}
