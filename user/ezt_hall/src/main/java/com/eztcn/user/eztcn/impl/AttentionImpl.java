package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IAttentionApi;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.AttentionDoctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.AttentManager;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttp;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;
import com.eztcn.user.hall.utils.LogUtils;

/**
 * @title 关注实现
 * @describe
 * @author ezt
 * @created 2015年1月4日
 */
public class AttentionImpl {

	/**
	 * 关注医生
	 */

	public void attentDoc(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ATTENT_DOC, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.ATTENT_DOC, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ATTENT_DOC, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	// 获取关注医生列表

	public void getAttentDocs(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ATTENT_DOC_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<AttentionDoctor> docList = AttentManager
									.getAttentDoc(result.toString());
							callBack.result(HttpParams.GET_ATTENT_DOC_LIST,
									true, docList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ATTENT_DOC_LIST, false,
								null, "服务器繁忙，请重试！");

					}
				});
	}

	// 取消关注

	public void cancelAttentDoc(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CANCEL_ATTENT_DOC, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.CANCEL_ATTENT_DOC, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CANCEL_ATTENT_DOC, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	public void getAttentDocState(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ATTENT_DOC_STATE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.GET_ATTENT_DOC_STATE,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ATTENT_DOC_STATE, false,
								null, "服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 收藏医院
	 */

	public void attentHos(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ATTENT_HOS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.ATTENT_HOS, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ATTENT_HOS, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 取消医院收藏
	 */

	public void cancelAttentHos(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CANCEL_ATTENT_HOS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.CANCEL_ATTENT_HOS, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CANCEL_ATTENT_HOS, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取收藏医院列表
	 */

	public void getAttentHos(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ATTENT_HOS_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseAttentHos(result.toString());
							callBack.result(HttpParams.GET_ATTENT_HOS_LIST,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ATTENT_HOS_LIST, false,
								null, "服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 获取医院收藏状态
	 */

	public void getAttentHosState(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ATTENT_HOS_STATE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = AttentManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.GET_ATTENT_HOS_STATE,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ATTENT_HOS_STATE, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

}
