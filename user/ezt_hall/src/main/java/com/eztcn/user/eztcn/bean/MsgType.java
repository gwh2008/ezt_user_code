package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Id;
import xutils.db.annotation.Table;

/**
 * @title 消息类型实体
 * @describe
 * @author ezt
 * @created 2015年1月15日
 */
@Table(name = "message_type")
public class MsgType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id(column = "_id")
	private int _id;
	@Column(column = "typeId")
	private String typeId;//类型id
	@Column(column = "typeTitle")
	private String typeTitle;// 类型标题
	@Column(column = "typeCount")
	private int typeCount;// 类型显示消息条数
	@Column(column = "typeContent")
	private String typeContent;// 类型内容
	@Column(column = "createTypeTime")
	private String createTypeTime;//创建时间
	@Column(column = "clickState")
	private int clickState;//点击状态 0为未点击，1为点击
	@Column(column = "patientId")
	private int patientId;//患者id （患患沟通用到）
	
	

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getClickState() {
		return clickState;
	}

	public void setClickState(int clickState) {
		this.clickState = clickState;
	}

	public String getCreateTypeTime() {
		return createTypeTime;
	}

	public void setCreateTypeTime(String createTypeTime) {
		this.createTypeTime = createTypeTime;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getTypeTitle() {
		return typeTitle;
	}

	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}

	public int getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}

	public String getTypeContent() {
		return typeContent;
	}

	public void setTypeContent(String typeContent) {
		this.typeContent = typeContent;
	}

}
