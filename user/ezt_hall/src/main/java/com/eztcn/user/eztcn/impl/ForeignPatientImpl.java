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
import com.eztcn.user.eztcn.controller.ForeignPatientManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 外患实现类
 * @describe
 * @author ezt
 * @created 2015年3月25日
 */
public class ForeignPatientImpl{

	public void getTumourIntro(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TUMOUR_INTRO, params,new RequestCallBack<Object>() {

		
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getTumourIntro(result.toString());
					callBack.result(HttpParams.GET_TUMOUR_INTRO, true, map);
				}
				
			}

		
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_TUMOUR_INTRO, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
	}
	// 快速求助
	public void quickHelp(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);

		eztHttp.send(HttpMethod.POST, EZTConfig.QUICK_HELP, params,new RequestCallBack<Object>() {

		
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.quickHelp(result.toString());
					callBack.result(HttpParams.QUICK_HELP, true, map);
				}
				
			}

		
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.QUICK_HELP, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
	}

	// 获取肿瘤服务列表
	public void getProjectList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_PROJECT_LIST, params,new RequestCallBack<Object>() {

		
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getProjectList(result.toString());
					callBack.result(HttpParams.GET_PROJECT_LIST, true, map);
				}
				
			}

		
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_PROJECT_LIST, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
	}

	/**
	 * 获取服务套餐列表
	 */
	public void getTrapackage_list(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TRAPACKAGE_LIST, params,new RequestCallBack<Object>() {
		
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getTrapackage_list(result.toString());
					callBack.result(HttpParams.GET_TRAPACKAGE_LIST, true, map);
				}
			}

		
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_TRAPACKAGE_LIST, false, null,
						"服务器繁忙，请重试！");
				
			}
		});

	}

	/**
	 * 服务套餐详情
	 */
	public void getTrapackageDetail(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TRAPACKAGE_DETAIL, params,new RequestCallBack<Object>() {
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getTrapackageDetail(result.toString());
					callBack.result(HttpParams.GET_TRAPACKAGE_DETAIL, true, map);
				}
			}

		
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_TRAPACKAGE_DETAIL, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
		
	}

	/**
	 * 获取康复病历列表
	 */

	public void getRecoveryCaseList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_RECOVERY_CASE_LIST, params,new RequestCallBack<Object>() {
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getRecoveryCaseList(result.toString());
					callBack.result(HttpParams.GET_RECOVERY_CASE_LIST, true, map);
				}
			}
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_RECOVERY_CASE_LIST, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
	}

	/**
	 * 获取病历详情
	 */
	public void getRecoveryCaseDetail(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_RECOVERY_CASE_DETAIL, params,new RequestCallBack<Object>() {
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getTumourIntro(result.toString());
					callBack.result(HttpParams.GET_RECOVERY_CASE_DETAIL, true, map);
				}
			}
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_RECOVERY_CASE_DETAIL, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
	}

	// 获取患友信息
	public void getPatientGroup(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_PATIENT_GROUP, params,new RequestCallBack<Object>() {
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.getPatientGroup(result.toString());
					callBack.result(HttpParams.GET_PATIENT_GROUP, true, map);
				}
			}
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.GET_PATIENT_GROUP, false, null,
						"服务器繁忙，请重试！");
				
			}
		});
	}

	//添加购物车
	public void addTraShoppingCart(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp=new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ADD_TRASHOPPINGCART, params,new RequestCallBack<Object>() {
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String result=String.valueOf(responseInfo.result);
					Map<String, Object> map = ForeignPatientManager
							.addTraShoppingCart(result.toString());
					callBack.result(HttpParams.ADD_TRASHOPPINGCART, true, map);
				}
			}
			public void onFailure(HttpException error, String msg) {
				callBack.result(HttpParams.ADD_TRASHOPPINGCART, false, null,
						"服务器繁忙，请重试！");
			}
		});
	}
}
