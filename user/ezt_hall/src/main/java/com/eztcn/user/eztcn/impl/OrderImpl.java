package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Order;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.LightAccompanyingManager;
import com.eztcn.user.eztcn.controller.OrderManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 订单实现
 * @describe
 * @author ezt
 * @created 2014年12月30日
 */
public class OrderImpl  {//implements IOrder

	/**
	 * 创建订单
	 */
//	@Override
	public void createTraOrder(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CREATE_TRA_ORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderManager.createOrdere(result
									.toString());
							callBack.result(HttpParams.CREATE_TRA_ORDER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CREATE_TRA_ORDER, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = OrderManager.createOrdere(result
//						.toString());
//				callBack.result(HttpParams.CREATE_TRA_ORDER, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.CREATE_TRA_ORDER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.CREATE_TRA_ORDER, params);

	}

	/**
	 * 获取订单列表
	 */
//	@Override
	public void getOrderList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST,EZTConfig.GET_ORDER_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Order> orderList = OrderManager.getOrderList(result
									.toString());
							callBack.result(HttpParams.GET_ORDER_LIST, true, orderList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ORDER_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Order> orderList = OrderManager.getOrderList(result
//						.toString());
//				callBack.result(HttpParams.GET_ORDER_LIST, true, orderList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_ORDER_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_ORDER_LIST, params);
	}

	/**
	 * 删除订单
	 */
//	@Override
	public void delOrder(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.DEL_ORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderManager.delOrder(result
									.toString());
							callBack.result(HttpParams.DEL_ORDER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.DEL_ORDER, false, null, "服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//
//				Map<String, Object> map = OrderManager.delOrder(result
//						.toString());
//				callBack.result(HttpParams.DEL_ORDER, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.DEL_ORDER, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.DEL_ORDER, params);
	}

	/**
	 * 订单立即支付
	 */
//	@Override
	public void orderPay(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ORDER_PAY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.createTraOrderpayPackage(result.toString());

							callBack.result(HttpParams.ORDER_PAY, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ORDER_PAY, false, null, "服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//
//				Map<String, Object> map = LightAccompanyingManager
//						.createTraOrderpayPackage(result.toString());
//
//				callBack.result(HttpParams.ORDER_PAY, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.ORDER_PAY, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.ORDER_PAY, params);

	}

}
