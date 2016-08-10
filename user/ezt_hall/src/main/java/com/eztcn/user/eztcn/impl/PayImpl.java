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
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.controller.PayManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @title 支付相关实现
 * @describe
 * @author ezt
 * @created 2015年1月21日
 */
public class PayImpl  {//implements IPay

//	@Override
	public void submitPayOrder(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.PAY_ORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.submitPayOrder(result
									.toString());
							callBack.result(HttpParams.PAY_ORDER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.PAY_ORDER, false, null, "服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.submitPayOrder(result
//						.toString());
//				callBack.result(HttpParams.PAY_ORDER, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.PAY_ORDER, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.PAY_ORDER, params);

	}

//	@Override
	public void getPayMoney(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_PAY_MONEY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.getPayMoney(result
									.toString());
							callBack.result(HttpParams.GET_PAY_MONEY, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_PAY_MONEY, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.getPayMoney(result
//						.toString());
//				callBack.result(HttpParams.GET_PAY_MONEY, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_PAY_MONEY, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_PAY_MONEY, params);

	}

//	@Override
	public void getPayRecord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_PAY_RECORD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.getPayRecord(result
									.toString());
							callBack.result(HttpParams.GET_PAY_RECORD, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_PAY_RECORD, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.getPayRecord(result
//						.toString());
//				callBack.result(HttpParams.GET_PAY_RECORD, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_PAY_RECORD, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_PAY_RECORD, params);
	}

//	@Override
	public void getCurrencyRecord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_CURRENCY_RECORD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.getCurrencyRecord(result
									.toString());
							callBack.result(HttpParams.GET_CURRENCY_RECORD, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_CURRENCY_RECORD, false, null,
								"服务器繁忙，请重试！");
					}
				});
		
		
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.getCurrencyRecord(result
//						.toString());
//				callBack.result(HttpParams.GET_CURRENCY_RECORD, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_CURRENCY_RECORD, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_CURRENCY_RECORD, params);

	}

	/**
	 * 获取健康币余额
	 */
//	@Override
	public void getCurrencyMoney(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_CURRENCY_MONEY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.getCurrencyMoney(result
									.toString());
							callBack.result(HttpParams.GET_CURRENCY_MONEY, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_CURRENCY_MONEY, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.getCurrencyMoney(result
//						.toString());
//				callBack.result(HttpParams.GET_CURRENCY_MONEY, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_CURRENCY_MONEY, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_CURRENCY_MONEY, params);

	}

	/**
	 * 获取可拨打分钟数
	 */
//	@Override
	public void getCllMinute(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_CALLMINUTE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.parseCallMinute(result
									.toString());
							callBack.result(HttpParams.GET_CALLMINUTE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_CALLMINUTE, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.parseCallMinute(result
//						.toString());
//				callBack.result(HttpParams.GET_CALLMINUTE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_CALLMINUTE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_CALLMINUTE, params);
	}

	
	/**
	 * 获取所有优惠券列表
	 */
//	@Override
	public void getCouponList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_COUPON_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.getCouponList(result
									.toString());
							callBack.result(HttpParams.GET_COUPON_LIST, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_COUPON_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.getCouponList(result
//						.toString());
//				callBack.result(HttpParams.GET_COUPON_LIST, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_COUPON_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_COUPON_LIST, params);
	}

	/**
	 * 获取可使用优惠券列表
	 */
//	@Override
	public void getUseCouponList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_USE_COUPON_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = PayManager.getCouponList(result
									.toString());
							callBack.result(HttpParams.GET_USE_COUPON_LIST, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_USE_COUPON_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
		
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = PayManager.getCouponList(result
//						.toString());
//				callBack.result(HttpParams.GET_USE_COUPON_LIST, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_USE_COUPON_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_USE_COUPON_LIST, params);
	}

}
