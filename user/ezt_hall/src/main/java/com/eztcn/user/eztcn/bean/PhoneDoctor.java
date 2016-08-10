package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

public class PhoneDoctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;// 医生id

	private String docHeadUrl;// 医生头像

	private String docName;// 医生姓名

	private String docPosition;// 医生职位

	private String docRate;// 预约率

	private String docRanking;// 排行数

	private String docFans;// 粉丝量

	private String docIntro;// 简介

	private String docGoodAt;// 擅长

	private String docEducBg;// 教育背景

	private String docAcademicSuc;// 学术成就

	private int docOverallMerit;// 综合评价

	private int docResult;// 医疗效果

	private int docServiceAttitude;// 服务态度

	private boolean isFocus;// 是否关注

	private int docUnEvaluateNum;// 未评价数

	private String docHos;// 所属医院

	private String docDept;// 所属科室

	private String docDeptId;// 所属科室id

	private String doc_DeptId;// 科室医生id

	private String docHosId;// 所属医院id

	private int docLevel;// 等级

	private int isHaveNum;// 医生是否有号(-1 没放号，0为预约已满，1有号)

	private Double fees;// 费用

	private double moneyOfMinute;// 一分钟所需费用

	private int dredge;// 是否服务开通

	private int online;// 是否在线

	private int minTime;// 最低消费通话时长

	public int getMinTime() {
		return minTime;
	}

	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDocHeadUrl() {
		return docHeadUrl;
	}

	public void setDocHeadUrl(String docHeadUrl) {
		this.docHeadUrl = docHeadUrl;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocPosition() {
		return docPosition;
	}

	public void setDocPosition(String docPosition) {
		this.docPosition = docPosition;
	}

	public String getDocRate() {
		return docRate;
	}

	public void setDocRate(String docRate) {
		this.docRate = docRate;
	}

	public String getDocRanking() {
		return docRanking;
	}

	public void setDocRanking(String docRanking) {
		this.docRanking = docRanking;
	}

	public String getDocFans() {
		return docFans;
	}

	public void setDocFans(String docFans) {
		this.docFans = docFans;
	}

	public String getDocIntro() {
		return docIntro;
	}

	public void setDocIntro(String docIntro) {
		this.docIntro = docIntro;
	}

	public String getDocGoodAt() {
		return docGoodAt;
	}

	public void setDocGoodAt(String docGoodAt) {
		this.docGoodAt = docGoodAt;
	}

	public String getDocEducBg() {
		return docEducBg;
	}

	public void setDocEducBg(String docEducBg) {
		this.docEducBg = docEducBg;
	}

	public String getDocAcademicSuc() {
		return docAcademicSuc;
	}

	public void setDocAcademicSuc(String docAcademicSuc) {
		this.docAcademicSuc = docAcademicSuc;
	}

	public int getDocOverallMerit() {
		return docOverallMerit;
	}

	public void setDocOverallMerit(int docOverallMerit) {
		this.docOverallMerit = docOverallMerit;
	}

	public int getDocResult() {
		return docResult;
	}

	public void setDocResult(int docResult) {
		this.docResult = docResult;
	}

	public int getDocServiceAttitude() {
		return docServiceAttitude;
	}

	public void setDocServiceAttitude(int docServiceAttitude) {
		this.docServiceAttitude = docServiceAttitude;
	}

	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
	}

	public int getDocUnEvaluateNum() {
		return docUnEvaluateNum;
	}

	public void setDocUnEvaluateNum(int docUnEvaluateNum) {
		this.docUnEvaluateNum = docUnEvaluateNum;
	}

	public String getDocHos() {
		return docHos;
	}

	public void setDocHos(String docHos) {
		this.docHos = docHos;
	}

	public String getDocDept() {
		return docDept;
	}

	public void setDocDept(String docDept) {
		this.docDept = docDept;
	}

	public String getDocDeptId() {
		return docDeptId;
	}

	public void setDocDeptId(String docDeptId) {
		this.docDeptId = docDeptId;
	}

	public String getDoc_DeptId() {
		return doc_DeptId;
	}

	public void setDoc_DeptId(String doc_DeptId) {
		this.doc_DeptId = doc_DeptId;
	}

	public String getDocHosId() {
		return docHosId;
	}

	public void setDocHosId(String docHosId) {
		this.docHosId = docHosId;
	}

	public int getDocLevel() {
		return docLevel;
	}

	public void setDocLevel(int docLevel) {
		this.docLevel = docLevel;
	}

	public int getIsHaveNum() {
		return isHaveNum;
	}

	public void setIsHaveNum(int isHaveNum) {
		this.isHaveNum = isHaveNum;
	}

	public Double getFees() {
		return fees;
	}

	public void setFees(Double fees) {
		this.fees = fees;
	}

	public double getMoneyOfMinute() {
		return moneyOfMinute;
	}

	public void setMoneyOfMinute(double moneyOfMinute) {
		this.moneyOfMinute = moneyOfMinute;
	}

	public int getDredge() {
		return dredge;
	}

	public void setDredge(int dredge) {
		this.dredge = dredge;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}
	
}
