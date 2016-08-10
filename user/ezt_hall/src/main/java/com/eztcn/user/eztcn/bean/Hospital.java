package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 医院表实体类
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class Hospital implements Serializable {

	private static final long serialVersionUID = 2238768374141309836L;

	private Integer id;
	private String collectId;// 收藏医院id
	private String hName;// 医院名称
	private String hTel;// 医院电话
	private String hFax;// 医院传真
	private String hZipcode;// 邮政编码
	private String hWebsite;// 医院网站
	private Integer hProvince;// 省
	private Integer hCity;// 市
	private Integer hCounty;// 县,区
	private String hAddress;// 地址
	private String hLogo;// 医院logo
	private String hCode; // 医院编码
	private Integer hOrder; // 医院序号
	private String hIntro; // 医院简介
	private String hKeywords; // 关键字
	private String hShortName; // 医院简称
	private String createBy;// 新增人
	private String createDate;// 新增时间
	private String lasteDitBy;// 最后修改人
	private String lasteDitDate;// 最后修改时间
	private Integer hStatus;// 医院状态 0正常 1暂停
	private Integer hPriority;// 优先级
	private Integer hIsView;// 医院是否有打开手机号权限 0 关闭 1打开身份证手机 2打开身份证号 3打开手机号
	private String hBeanName;// 不同医院不同规则，反射不同的BEAN。
	private Integer hIsRedShow;// 是否添加红色桃心标识 0 否 1 是
	private Integer hIsTvShow;// 是否在广电频道显示 0否1是
	private String hosLevel;// 医院等级
	private String hDistance;// 距离
	private double lon;
	private double lat;
	private int ehDockingStatus=1;//2015-12-27 医院是否对接 0  对接  1 未对接（正常）
	private String ehDockingStr;//2015-12-27 医院对接后 （对接提示语）
	public String gethDistance() {
		return hDistance;
	}

	public void sethDistance(String hDistance) {
		this.hDistance = hDistance;
	}

	public String getHosLevel() {
		return hosLevel;
	}

	public void setHosLevel(String hosLevel) {
		this.hosLevel = hosLevel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCollectId() {
		return collectId;
	}

	public void setCollectId(String collectId) {
		this.collectId = collectId;
	}

	public String gethName() {
		return hName;
	}

	public void sethName(String hName) {
		this.hName = hName;
	}

	public String gethTel() {
		return hTel;
	}

	public void sethTel(String hTel) {
		this.hTel = hTel;
	}

	public String gethFax() {
		return hFax;
	}

	public void sethFax(String hFax) {
		this.hFax = hFax;
	}

	public String gethZipcode() {
		return hZipcode;
	}

	public void sethZipcode(String hZipcode) {
		this.hZipcode = hZipcode;
	}

	public String gethWebsite() {
		return hWebsite;
	}

	public void sethWebsite(String hWebsite) {
		this.hWebsite = hWebsite;
	}

	public Integer gethProvince() {
		return hProvince;
	}

	public void sethProvince(Integer hProvince) {
		this.hProvince = hProvince;
	}

	public Integer gethCity() {
		return hCity;
	}

	public void sethCity(Integer hCity) {
		this.hCity = hCity;
	}

	public Integer gethCounty() {
		return hCounty;
	}

	public void sethCounty(Integer hCounty) {
		this.hCounty = hCounty;
	}

	public String gethAddress() {
		return hAddress;
	}

	public void sethAddress(String hAddress) {
		this.hAddress = hAddress;
	}

	public String gethLogo() {
		return hLogo;
	}

	public void sethLogo(String hLogo) {
		this.hLogo = hLogo;
	}

	public String gethCode() {
		return hCode;
	}

	public void sethCode(String hCode) {
		this.hCode = hCode;
	}

	public Integer gethOrder() {
		return hOrder;
	}

	public void sethOrder(Integer hOrder) {
		this.hOrder = hOrder;
	}

	public String gethIntro() {
		return hIntro;
	}

	public void sethIntro(String hIntro) {
		this.hIntro = hIntro;
	}

	public String gethKeywords() {
		return hKeywords;
	}

	public void sethKeywords(String hKeywords) {
		this.hKeywords = hKeywords;
	}

	public String gethShortName() {
		return hShortName;
	}

	public void sethShortName(String hShortName) {
		this.hShortName = hShortName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLasteDitBy() {
		return lasteDitBy;
	}

	public void setLasteDitBy(String lasteDitBy) {
		this.lasteDitBy = lasteDitBy;
	}

	public String getLasteDitDate() {
		return lasteDitDate;
	}

	public void setLasteDitDate(String lasteDitDate) {
		this.lasteDitDate = lasteDitDate;
	}

	public Integer gethStatus() {
		return hStatus;
	}

	public void sethStatus(Integer hStatus) {
		this.hStatus = hStatus;
	}

	public Integer gethPriority() {
		return hPriority;
	}

	public void sethPriority(Integer hPriority) {
		this.hPriority = hPriority;
	}

	public Integer gethIsView() {
		return hIsView;
	}

	public void sethIsView(Integer hIsView) {
		this.hIsView = hIsView;
	}

	public String gethBeanName() {
		return hBeanName;
	}

	public void sethBeanName(String hBeanName) {
		this.hBeanName = hBeanName;
	}

	public Integer gethIsRedShow() {
		return hIsRedShow;
	}

	public void sethIsRedShow(Integer hIsRedShow) {
		this.hIsRedShow = hIsRedShow;
	}

	public Integer gethIsTvShow() {
		return hIsTvShow;
	}

	public void sethIsTvShow(Integer hIsTvShow) {
		this.hIsTvShow = hIsTvShow;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
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
	/**
	 * 设置对接提示语
	 * @param ehDockingStr
	 */
	public void setEhDockingStr(String ehDockingStr) {
		this.ehDockingStr = ehDockingStr;
	}

}
