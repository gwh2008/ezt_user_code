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
import com.eztcn.user.eztcn.controller.OrderBedManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * 预约病床
 * 
 * @author LX
 * @date2016-3-31 @time下午4:21:24
 */
public class OrderBedImpl {

	/**
	 * orderId 预约病床订单ID
	 * 
	 * @param params
	 * @param callBack
	 */
	public void gainOrderBedStatus(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_ORDER_BED_STATUS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderBedManager
									.gainOrderBedStatus(result.toString());
							Boolean flag=false;
							if(map!=null){
								
								if(map.containsKey("flag")){
									flag=(Boolean) map.get("flag");
								}
							}
								callBack.result(HttpParams.GET_ORDER_BED_STATUS,
										flag, map);
						}
					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GAIN_ORDER_CHECK_DETIAL,
								false, null, "服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 
	 * 
	 * 1.2. 提交预约病床订单 patientId 患者ID userId 用户ID patientName 预约者姓名 patientSex
	 * 预约者性别 patientPhone 预约者电话 patientCardName 预约者证件类型 patientCardNum 证件号码
	 * patientOrderType 预约病床 hospitalId 医院ID hospitalName 医院名称 deptId 科室ID
	 * patientSpecialNeed 特殊需求 patientStatus 症状描述 picture 订单图片
	 * 
	 * @param params
	 * @param callBack
	 */
	public void createBedOrder(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(2*60*1000);
		eztHttp.send(HttpMethod.POST, EZTConfig.CREATE_BED_ORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderBedManager
									.createBedOrder(result.toString());
							callBack.result(HttpParams.CREATE_BED_ORDER,
									map.get("flag"), map);
						}
					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CREATE_BED_ORDER, false,
								null, "服务器繁忙，请重试！");
					}
				});
	}
	/**
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getAlipayStringById(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ALIPAY_STRING_BY_ID,
				params, new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderBedManager
									.getAlipayStringById(result.toString());
							callBack.result(HttpParams.GET_ALIPAY_STRING_BY_ID,
									map.get("flag"), map);
						}
					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ALIPAY_STRING_BY_ID,
								false, null, "服务器繁忙，请重试！");
					}
				});
	}
	
	/**
	 * 获取我的预约病床的列表。
	 * @param params
	 * @param callBack
	 */
	public void gainOrderBedList(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_ORDER_BED_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderBedManager
									.gainData(result.toString());
							Boolean flag=false;
							if(map!=null){
								
								if(map.containsKey("flag")){
									flag=(Boolean) map.get("flag");
								}
							}
								callBack.result(HttpParams.GET_ORDER_BED_LIST_BY_ID,
										flag, map);
						}
					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ORDER_BED_LIST_BY_ID,
								false, null, "服务器繁忙，请重试！");
					}
				});

	}
	
	
	

}
