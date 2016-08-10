package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 疾病实体类
 * @describe
 * @author ezt
 * @created 2014年12月24日
 */
public class Diseases implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;//疾病id
	
	private String dName;//疾病名称
	
	private String sortLetters; // 显示数据拼音的首字母
	
	private String diagnose;//诊断与鉴别
	
	private String prevent;//预防
	
	private String nursing;//保健
	
	private String dept;//对应科室
	
	private String intro;//简介
	
	
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public String getPrevent() {
		return prevent;
	}

	public void setPrevent(String prevent) {
		this.prevent = prevent;
	}

	public String getNursing() {
		return nursing;
	}

	public void setNursing(String nursing) {
		this.nursing = nursing;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
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
