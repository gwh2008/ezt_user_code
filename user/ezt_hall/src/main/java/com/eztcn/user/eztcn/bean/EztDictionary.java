package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Id;
import xutils.db.annotation.Table;


@Table(name = "eztdictionary")
public class EztDictionary implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id(column = "_id")
	private Integer _id;
	@Column(column = "enName")
	private String enName;
	@Column(column = "cnName")
	private String cnName;
	@Column(column = "label")
	private String label;// 标签
	@Column(column = "value")
	private String value;//

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
