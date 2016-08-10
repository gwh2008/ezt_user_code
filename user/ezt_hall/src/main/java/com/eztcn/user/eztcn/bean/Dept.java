package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 科室实体
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class Dept implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;// 科室id

	private String dName;// 科室名称

	private String intro;// 科室简介

	private String dHosName;// 所属医院

	private String deptcateId;// 科室分类id

	private String dHosId;// 所属医院id

	private String sortLetters; // 显示数据拼音的首字母

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getdHosId() {
		return dHosId;
	}

	public void setdHosId(String dHosId) {
		this.dHosId = dHosId;
	}

	public String getDeptcateId() {
		return deptcateId;
	}

	public void setDeptcateId(String deptcateId) {
		this.deptcateId = deptcateId;
	}

	public String getdHosName() {
		return dHosName;
	}

	public void setdHosName(String dHosName) {
		this.dHosName = dHosName;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}


}
