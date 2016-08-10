package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 肿瘤实体类
 * @describe
 * @author ezt
 * @created 2015年3月24日
 */
public class Tumour implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String strName; // 肿瘤名称

	private String imgUrl;// 对应图片

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
