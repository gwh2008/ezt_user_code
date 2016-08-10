package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 症状实体
 * @describe 
 * @author ezt
 * @created 2015年3月11日
 */
public class Symptom implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;//症状id
	
	private String strName;//症状名称
	
	private String strDept;//所属科室
	
	private String intro;//症状详情
	
	private String sortLetters; // 显示数据拼音的首字母
	
	
	
	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getStrDept() {
		return strDept;
	}

	public void setStrDept(String strDept) {
		this.strDept = strDept;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

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
	

}
