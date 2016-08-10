package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @title 医生实体
 * @describe
 * @author ezt
 * @created 2014年12月10日
 */
public class Doctor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;// 医生id

	@Override
	public String toString() {
		return "Doctor [id=" + id + ", docHeadUrl=" + docHeadUrl + ", docName="
				+ docName + ", docPosition=" + docPosition + ", docRate="
				+ docRate + ", docRanking=" + docRanking + ", docFans="
				+ docFans + ", docAllocaeNum=" + docAllocaeNum + ", docIntro="
				+ docIntro + ", docGoodAt=" + docGoodAt + ", docEducBg="
				+ docEducBg + ", docAcademicSuc=" + docAcademicSuc
				+ ", docOverallMerit=" + docOverallMerit + ", docResult="
				+ docResult + ", docServiceAttitude=" + docServiceAttitude
				+ ", isFocus=" + isFocus + ", docUnEvaluateNum="
				+ docUnEvaluateNum + ", docHos=" + docHos + ", docDept="
				+ docDept + ", docDeptId=" + docDeptId + ", deptDocId="
				+ deptDocId + ", docHosId=" + docHosId + ", docLevel="
				+ docLevel + ", isHaveNum=" + isHaveNum + ", pools=" + pools
				+ ", callDoctorYnAppointment=" + callDoctorYnAppointment
				+ ", callDoctorYnOnline=" + callDoctorYnOnline + ", hosLon="
				+ hosLon + ", hosLat=" + hosLat + ", fees=" + fees
				+ ", minTime=" + minTime + ", moneyOfMinute=" + moneyOfMinute
				+ ", ehDockingStatus=" + ehDockingStatus + ", ehDockingStr="
				+ ehDockingStr + "]";
	}

	private String docHeadUrl;// 医生头像

	private String docName;// 医生姓名

	private String docPosition;// 医生职位

	private String docRate;// 预约率

	private String docRanking;// 排行数

	private String docFans;// 粉丝量

	private String docAllocaeNum;// 放号量

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

	private String deptDocId;// 科室医生id

	private String docHosId;// 所属医院id

	private String docLevel;// 等级

	private int isHaveNum;// 医生是否有号(-1 没放号，0为预约已满，1有号)

	private ArrayList<Pool> pools;// 号池集合

	private int callDoctorYnAppointment;// 电话医生是否可预约

	private int callDoctorYnOnline;// 电话医生是否在线

	private double hosLon;// 所属医院纬度

	private double hosLat;// 所属医院经度

	private Double fees;// 电话医生费用

	private int minTime;// 最低消费通话时长

	private double moneyOfMinute;// 一分钟所需费用
	
	private int ehDockingStatus=1;//2015-12-18 对接状态
	
	private String ehDockingStr;//2015-12-19 对接提示语

	public int getIsHaveNum() {
		return isHaveNum;
	}

	public void setIsHaveNum(int isHaveNum) {
		this.isHaveNum = isHaveNum;
	}

	public String getDocHosId() {
		return docHosId;
	}

	public void setDocHosId(String docHosId) {
		this.docHosId = docHosId;
	}

	public ArrayList<Pool> getPools() {
		return pools;
	}

	public void setPools(ArrayList<Pool> pools) {
		this.pools = pools;
	}

	public String getDeptDocId() {
		return deptDocId;
	}

	public void setDeptDocId(String doc_DeptId) {
		this.deptDocId = doc_DeptId;
	}

	public String getDocDeptId() {
		return docDeptId;
	}

	public void setDocDeptId(String docDeptId) {
		this.docDeptId = docDeptId;
	}

	public String getDocLevel() {
		return docLevel;
	}

	public void setDocLevel(String docLevel) {
		this.docLevel = docLevel;
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

	public boolean isFocus() {
		return isFocus;
	}

	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
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

	public String getDocFans() {
		return docFans;
	}

	public void setDocFans(String docFans) {
		this.docFans = docFans;
	}

	public String getDocAllocaeNum() {
		return docAllocaeNum;
	}

	public void setDocAllocaeNum(String docAllocaeNum) {
		this.docAllocaeNum = docAllocaeNum;
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

	public int getDocUnEvaluateNum() {
		return docUnEvaluateNum;
	}

	public void setDocUnEvaluateNum(int docUnEvaluateNum) {
		this.docUnEvaluateNum = docUnEvaluateNum;
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

	public int getCallDoctorYnAppointment() {
		return callDoctorYnAppointment;
	}

	public void setCallDoctorYnAppointment(int callDoctorYnAppointment) {
		this.callDoctorYnAppointment = callDoctorYnAppointment;
	}

	public int getCallDoctorYnOnline() {
		return callDoctorYnOnline;
	}

	public void setCallDoctorYnOnline(int callDoctorYnOnline) {
		this.callDoctorYnOnline = callDoctorYnOnline;
	}

	public double getHosLon() {
		return hosLon;
	}

	public void setHosLon(double hosLon) {
		this.hosLon = hosLon;
	}

	public double getHosLat() {
		return hosLat;
	}

	public void setHosLat(double hosLat) {
		this.hosLat = hosLat;
	}

	public Double getFees() {
		if (fees == null) {
			return 0d;
		}
		return fees;
	}

	public void setFees(Double fees) {
		this.fees = fees;
	}

	public int getMinTime() {
		return minTime;
	}

	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}

	public double getMoneyOfMinute() {
		return moneyOfMinute;
	}

	public void setMoneyOfMinute(double moneyOfMinute) {
		this.moneyOfMinute = moneyOfMinute;
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
