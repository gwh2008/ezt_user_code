package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 电话医生接口
 * @describe
 * @author ezt
 * @created 2015年1月27日
 */
public interface ITelDoc {

	/**
	 * 获取电话医生状态
	 * 
	 * @param params
	 * @param callBack
	 */
	public void checkTelDocState(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取医生可预约的时间段
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getTelDocTime(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 确认预约电话医生
	 * 
	 * @param params
	 * @param callBack
	 */
	public void confirmTelDocOrder(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 立即通话
	 * 
	 * @param params
	 * @param callBack
	 */
	public void promptTeling(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取通话记录
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getTelDocRecord(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取电话医生列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getTelDocList(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 取消电话预约
	 * 
	 * @param params
	 * @param callBack
	 */
	public void cancelPhoneOrder(HashMap<String, Object> params, final IHttpResult callBack);

}
