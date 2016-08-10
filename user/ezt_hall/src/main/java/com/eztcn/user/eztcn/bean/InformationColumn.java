package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 微资讯栏目
 * @describe
 * @author ezt
 * @created 2015年1月9日
 */
public class InformationColumn implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;//栏目id
	
	private String infoName;//栏目名称

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}
	
	

}
