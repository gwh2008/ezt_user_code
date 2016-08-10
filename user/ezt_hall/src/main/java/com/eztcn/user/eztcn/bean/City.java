package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Id;
import xutils.db.annotation.Table;

/**
 * @title 城市实体类
 * @describe
 * @author ezt
 * @created 2014年10月30日
 */
@Table(name = "ezt_city")
public class City implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(column = "_id")
	private Integer _id;
	@Column(column = "cityId")
	private String cityId;// 城市ID
	@Column(column = "cityName")
	private String cityName;// 城市名称
	@Column(column = "sortLetters")
	private String sortLetters; // 显示数据拼音的首字母
	@Column(column = "areaId")
	private String areaId;// 区域id
	@Column(column = "areaName")
	private String areaName;// 区域名称

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
