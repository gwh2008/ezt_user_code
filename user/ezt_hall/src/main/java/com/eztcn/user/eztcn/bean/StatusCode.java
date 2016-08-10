package com.eztcn.user.eztcn.bean;

/**
 * @title 服务器信息返回数据格式封装
 * @describe
 * @author ezt
 * @created 2014年12月17日
 */
public class StatusCode<T> {

	private String number;// 状态码
	private boolean flag = false;// 状态码状态 0失败true 1成功false
	private String msg;// 状态码值
	private String detailMsg;// 状态码详细值
	private T data;

	public StatusCode() {
		super();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDetailMsg() {
		return detailMsg;
	}

	public void setDetailMsg(String detailMsg) {
		this.detailMsg = detailMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
