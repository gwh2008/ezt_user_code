package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Medical_img implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String recordType;
	private String typeName;
	private List<MedicalRecord_ImgType> urlList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<MedicalRecord_ImgType> getUrlList() {
		if (urlList == null) {
			urlList = new ArrayList<MedicalRecord_ImgType>();
		}
		return urlList;
	}

	public void setUrlList(List<MedicalRecord_ImgType> urlList) {
		this.urlList = urlList;
	}

}
