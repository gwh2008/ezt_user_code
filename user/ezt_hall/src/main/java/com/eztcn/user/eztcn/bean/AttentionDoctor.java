package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class AttentionDoctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;// 关注ID
	private String docId;
	private String docName;
	private String docPhoto;
	private String deptId;
	private String deptDocId;
	private String deptName;
	private Integer doctorLevel;
	private String hosName;
	//2015-12-18 医院对接
	private int ehDockingStatus=1;//（对接状态）
	private String ehDockingStr;//(对接提示语)

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocPhoto() {
		return docPhoto;
	}

	public void setDocPhoto(String docPhoto) {
		this.docPhoto = docPhoto;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptDocId() {
		return deptDocId;
	}

	public void setDeptDocId(String deptDocId) {
		this.deptDocId = deptDocId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getDoctorLevel() {
		return doctorLevel;
	}

	public void setDoctorLevel(Integer doctorLevel) {
		this.doctorLevel = doctorLevel;
	}

	public String getHosName() {
		return hosName;
	}

	public void setHosName(String hosName) {
		this.hosName = hosName;
	}

	public int getEhDockingStatus() {
		return ehDockingStatus;
	}

	public void setEhDockingStatus(int ehDockingStatus) {
		this.ehDockingStatus = ehDockingStatus;
	}

	public String getEhDockingStr() {
		return ehDockingStr;
	}

	public void setEhDockingStr(String ehDockingStr) {
		this.ehDockingStr = ehDockingStr;
	}

}
