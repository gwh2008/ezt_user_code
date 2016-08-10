package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 科室分类实体类
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class DeptType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;// 科室id

	private String dptName;// 科室名称

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDptName() {
		return dptName;
	}

	public void setDptName(String dptName) {
		this.dptName = dptName;
	}
	
	

}
