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
import com.eztcn.user.eztcn.controller.ENurseHelpManager;
import com.eztcn.user.eztcn.utils.HttpParams;

public class ENurseHelpImpl {

	public void getENurseCard(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_MEALCARD_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ENurseHelpManager
									.parseMealCardList(result.toString());
							callBack.result(HttpParams.GET_MEALCARD_LIST, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_MEALCARD_LIST, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	public void getENurseCardDetail(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_MEALCARD_DETAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = ENurseHelpManager
									.parseMealCardDetail(result.toString());
							callBack.result(HttpParams.GET_MEALCARD_DETAIL,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_MEALCARD_DETAIL, false,
								null, "服务器繁忙，请重试！");
					}
				});
	}

}
