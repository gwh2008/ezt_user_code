package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class Advertise implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String eaHdPic;
	private String eaPic;
	private String eaUrl;
	private String createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEaHdPic() {
		return eaHdPic;
	}
	public void setEaHdPic(String eaHdPic) {
		this.eaHdPic = eaHdPic;
	}
	public String getEaPic() {
		return eaPic;
	}
	public void setEaPic(String eaPic) {
		this.eaPic = eaPic;
	}
	public String getEaUrl() {
		return eaUrl;
	}
	public void setEaUrl(String eaUrl) {
		this.eaUrl = eaUrl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
