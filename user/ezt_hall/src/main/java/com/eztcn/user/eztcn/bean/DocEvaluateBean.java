package com.eztcn.user.eztcn.bean;

public class DocEvaluateBean {

	private String id;
	private String patientId;
	private String userPhoto;// 用户头像
	private String userName;// 用户名
	private String evaluateType;// 评价类型
	private double totalRate;// 综合评价
	private double medicalEffect;// 医术疗效
	private double serverAttitude;// 服务态度
	private String content;// 评价内容
	private String createTime;// 评价时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}

	public double getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(double totalRate) {
		this.totalRate = totalRate;
	}

	public double getMedicalEffect() {
		return medicalEffect;
	}

	public void setMedicalEffect(double medicalEffect) {
		this.medicalEffect = medicalEffect;
	}

	public double getServerAttitude() {
		return serverAttitude;
	}

	public void setServerAttitude(double serverAttitude) {
		this.serverAttitude = serverAttitude;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
