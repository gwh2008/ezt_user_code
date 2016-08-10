/**
 * 
 */
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
import com.eztcn.user.eztcn.controller.ServerManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @author Liu Gang
 * 
 * 2015年10月20日
 * 下午3:56:33
 * 
 */
public class ServerImpl  {//implements ServerAPi

	/* （非 Javadoc）
	 * @see com.eztcn.watch.api.ServerAPi#healthCarActivate(java.util.HashMap, com.eztcn.user.eztcn.api.IHttpResult)
	 */
//	@Override
	public void healthCarActivate(RequestParams params,
		final	IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ACTIVATION, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ServerManager
									.getActivation(result.toString());
							callBack.result(HttpParams.ACTIVATION, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ACTIVATION, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = ServerManager
//						.getActivation(result.toString());
//				callBack.result(HttpParams.ACTIVATION, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.ACTIVATION, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.ACTIVATION, params);
	}

//	@Override
	public void getHealthcardList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HEALTHCARD_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String,Object> map=ServerManager.getHealthcardList(result.toString());
							callBack.result(HttpParams.GET_HEALTHCARD_LIST, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HEALTHCARD_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String,Object> map=ServerManager.getHealthcardList(result.toString());
//				callBack.result(HttpParams.GET_HEALTHCARD_LIST, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_HEALTHCARD_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_HEALTHCARD_LIST, params);
	}

//	@Override
	public void getHealthcardDetail(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HEALTHCARD_DETAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ServerManager
									.getHealthcardDetail(result.toString());
							callBack.result(HttpParams.GET_HEALTHCARD_DETAIL, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HEALTHCARD_DETAIL, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//
//				Map<String, Object> map = ServerManager
//						.getHealthcardDetail(result.toString());
//				callBack.result(HttpParams.GET_HEALTHCARD_DETAIL, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_HEALTHCARD_DETAIL, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_HEALTHCARD_DETAIL, params);
	}

}
