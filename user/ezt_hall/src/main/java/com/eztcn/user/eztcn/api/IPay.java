package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 支付相关接口
 * @describe
 * @author ezt
 * @created 2015年1月21日
 */
public interface IPay {

	/**
	 * 提交订单
	 * 
	 * @param params
	 * @param callBack
	 */
	public void submitPayOrder(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取支付金额
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getPayMoney(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取充值记录
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getPayRecord(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取健康币记录
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getCurrencyRecord(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取健康币余额
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getCurrencyMoney(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取可拨打分钟数
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getCllMinute(HashMap<String, Object> params, IHttpResult callBack);
	
	
	/**
	 * 获取优惠券列表
	 * @param params
	 * @param callBack
	 */
	public void getCouponList(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取可使用的优惠券列表
	 * @param params
	 * @param callBack
	 */
	public void getUseCouponList(HashMap<String, Object> params, IHttpResult callBack);
}
