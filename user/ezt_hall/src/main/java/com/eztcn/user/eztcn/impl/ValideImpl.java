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
import com.eztcn.user.eztcn.controller.ValideManager;
import com.eztcn.user.eztcn.utils.HttpParams;

public class ValideImpl  {//implements ValideApi
	
	/**
	 * 2015-12-30 退号时候获取短信验证码
	 * @param params
	 * 		registerId
	 * 		operatorId(就是userId)
	 * @param callBack
	 * 
	 *   
	 * 
	 */
	public void getBackValideCode(RequestParams params, final IHttpResult callBack){//2015-12-30
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_BACK_VAL_CODE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ValideManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.GET_BACK_VAL_CODE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_BACK_VAL_CODE, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	

//	@Override
	public void getValide(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_VALIDE_CODE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ValideManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.GET_VALIDATE_CODE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_VALIDATE_CODE, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = ValideManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.GET_VALIDATE_CODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_VALIDATE_CODE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_VALIDE_CODE, params);
	}

//	@Override
	public void valideGuideDoc(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GUIDE_DOCNUM, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ValideManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.GUIDE_DOCNUM, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GUIDE_DOCNUM, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = ValideManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.GUIDE_DOCNUM, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GUIDE_DOCNUM, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GUIDE_DOCNUM, params);
		
	}

}
