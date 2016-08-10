package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @title 病历分类图片实体类
 * @describe
 * @author ezt
 * @created 2015年3月18日
 */
public class MedicalRecord_ImgType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer type;

	private String imgUrl;

	private Integer id;

	public Integer getType() {
		if (type == null) {
			return 0;
		}
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
