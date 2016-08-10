package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 订单接口
 * @describe
 * @author ezt
 * @created 2014年12月30日
 */
public interface IOrder {

	public void createTraOrder(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取订单列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getOrderList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 删除订单
	 * 
	 * @param params
	 * @param callBack
	 */
	public void delOrder(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 订单立即支付
	 * 
	 * @param params
	 * @param callBack
	 */
	public void orderPay(HashMap<String, Object> params, IHttpResult callBack);

}
