package com.eztcn.user.eztcn.impl;

import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.RegistrationManager;
import com.eztcn.user.eztcn.controller.TelDocManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 电话医生实现
 * @describe
 * @author ezt
 * @created 2015年1月27日
 */
public class TelDocImpl {//implements ITelDoc

	// 查看电话医生状态
//	@Override
	public void checkTelDocState(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CHECK_TEL_DOC_STATE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = TelDocManager.checkTelDocState(result
									.toString());
							callBack.result(HttpParams.CHECK_TEL_DOC_STATE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CHECK_TEL_DOC_STATE, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = TelDocManager.checkTelDocState(result
//						.toString());
//				callBack.result(HttpParams.CHECK_TEL_DOC_STATE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.CHECK_TEL_DOC_STATE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.CHECK_TEL_DOC_STATE, params);

	}

	// 根据日期获取医生可预约的时间段
//	@Override
	public void getTelDocTime(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TEL_DOC_TIME, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = TelDocManager.getTelDocTime(result
									.toString());
							callBack.result(HttpParams.GET_TEL_DOC_TIME, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_TEL_DOC_TIME, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = TelDocManager.getTelDocTime(result
//						.toString());
//				callBack.result(HttpParams.GET_TEL_DOC_TIME, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_TEL_DOC_TIME, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_TEL_DOC_TIME, params);

	}

	// 确定预约电话医生
//	@Override
	public void confirmTelDocOrder(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CONFIRM_TEL_DOC_ORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = TelDocManager
									.confirmTelDocOrder(result.toString());
							callBack.result(HttpParams.CONFIRM_TEL_DOC_ORDER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CONFIRM_TEL_DOC_ORDER, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = TelDocManager
//						.confirmTelDocOrder(result.toString());
//				callBack.result(HttpParams.CONFIRM_TEL_DOC_ORDER, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.CONFIRM_TEL_DOC_ORDER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.CONFIRM_TEL_DOC_ORDER, params);

	}

	// 立即通话
//	@Override
	public void promptTeling(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.PROMPT_TELING, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = TelDocManager.promptTeling(result
									.toString());
							callBack.result(HttpParams.PROMPT_TELING, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.PROMPT_TELING, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = TelDocManager.promptTeling(result
//						.toString());
//				callBack.result(HttpParams.PROMPT_TELING, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.PROMPT_TELING, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.PROMPT_TELING, params);

	}

	// 获取通话记录
//	@Override
	public void getTelDocRecord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TEL_DOC_RECORD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = TelDocManager.getTelDocRecord(result
									.toString());
							callBack.result(HttpParams.GET_TEL_DOC_RECORD, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_TEL_DOC_RECORD, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = TelDocManager.getTelDocRecord(result
//						.toString());
//				callBack.result(HttpParams.GET_TEL_DOC_RECORD, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_TEL_DOC_RECORD, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_TEL_DOC_RECORD, params);
	}

	/**
	 * 获取电话医生列表
	 */
//	@Override
	public void getTelDocList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TEL_DOC_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = TelDocManager
									.parsePhoneDocList(result.toString());
							callBack.result(HttpParams.GET_TEL_DOC_LIST, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_TEL_DOC_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = TelDocManager
//						.parsePhoneDocList(result.toString());
//				callBack.result(HttpParams.GET_TEL_DOC_LIST, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_TEL_DOC_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_TEL_DOC_LIST, params);
	}

	/**
	 * 取消电话医生预约
	 */
//	@Override
	public void cancelPhoneOrder(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CANCEL_PHONEORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parseJsonData(result.toString());
							callBack.result(HttpParams.CANCEL_PHONEORDER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CANCEL_PHONEORDER, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = RegistrationManager
//						.parseJsonData(result.toString());
//				callBack.result(HttpParams.CANCEL_PHONEORDER, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.CANCEL_PHONEORDER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.CANCEL_PHONEORDER, params);
	}

}
