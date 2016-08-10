package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 外患服务实体
 * @describe
 * @author ezt
 * @created 2015年2月28日
 */
public class ForeignPatient_Service implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;

	private String title;//标题

	private String intro;//描述

	private String price;//价格

	private String time;// 节省时间

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
