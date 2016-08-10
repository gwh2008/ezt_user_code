package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.ICityApi;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.AttentManager;
import com.eztcn.user.eztcn.controller.CityManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttp;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @title 获取城市 实现
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class CityImpl {
	// 获取区域列表
	public void getAreaList(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_AREAS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<City> cityList = CityManager.getAreaList(result
									.toString());
							callBack.result(HttpParams.GET_AREAS, true, cityList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_AREAS, false, null, "服务器繁忙，请重试！");

					}
				});
	}

	// 获取区域列表
	public void getCityList(RequestParams params,
			final IHttpResult callBack) {
		
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_CITY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<City> cityList = CityManager.getCityList(result
									.toString());
							callBack.result(HttpParams.GET_CITY, true, cityList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ATTENT_DOC, false, null,
								"服务器繁忙，请重试！");

					}
				});


	}

}
