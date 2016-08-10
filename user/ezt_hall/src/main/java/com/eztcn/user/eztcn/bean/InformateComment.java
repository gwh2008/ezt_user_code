package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @title 资讯评价实体类
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
public class InformateComment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String userImg;// 用户头像
	private String userName;// 用户名
	private String content;// 评价内容
	private String createTime;// 评价时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
