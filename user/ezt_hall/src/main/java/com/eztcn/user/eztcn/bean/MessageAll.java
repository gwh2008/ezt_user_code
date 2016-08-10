package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Id;
import xutils.db.annotation.Table;

/**
 * @title 消息实体类
 * @describe 消息箱显示
 * @author ezt
 * @created 2014年12月15日
 */
@Table(name = "message_all")
public class MessageAll implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id(column = "_id")
	private Integer _id;// 主键id
	@Column(column = "msgTitle")
	private String msgTitle;// 标题
	@Column(column = "msgInfo")
	private String msgInfo;// 内容
	@Column(column = "msgTime")
	private String msgTime;// 时间
	@Column(column = "msgUrl")
	private String msgUrl;// 图片地址
	@Column(column = "msgId")
	private int msgId;// 消息id
	@Column(column = "versionCode")
	private int versionCode;//（推送版本升级用到）版本号
	@Column(column = "msgChildType")
	private String msgChildType;//子消息类型（custom-gg:系统公告，custom-bb:版本更新）
	@Column(column = "msgType")
	private String msgType;// 消息类型(custom:医指通类型 ;register预约提醒;3、停诊消息4、患患沟通 )
	@Column(column = "clickState")
	private int clickState;//点击状态 0为未点击，1为点击
	

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getClickState() {
		return clickState;
	}

	public void setClickState(int clickState) {
		this.clickState = clickState;
	}

	public String getMsgChildType() {
		return msgChildType;
	}

	public void setMsgChildType(String msgChildType) {
		this.msgChildType = msgChildType;
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}



	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}


}
