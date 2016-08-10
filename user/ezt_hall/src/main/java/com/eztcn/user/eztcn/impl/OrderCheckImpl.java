/**
 * 
 */
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
import com.eztcn.user.eztcn.controller.OrderCheckManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.LogUtils;

/**
 * @author Liu Gang
 * 
 *         2016年3月16日 下午5:01:30 预约检查接口
 */
public class OrderCheckImpl {
	/**
	 * 获取检查项列表
	 */
	public void getCheckList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_ORDER_CHECK_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderCheckManager
									.getOrderChecks(result.toString());
							callBack.result(HttpParams.GAIN_ORDER_CHECK_LIST,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GAIN_ORDER_CHECK_LIST,
								false, null, "服务器繁忙，请重试！");
					}
				});

	}
	/**
	 * 提交检查项订单
	 * 
	 * @param params
	 *      orderId
	 * 
	 * 
	 * @param callBack
	 */
	public void commitCheckOrderById(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.COMMIT_ORDER_CHECK_BYID, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderCheckManager
									.commitCheckOrder(result.toString());
							callBack.result(HttpParams.COMMIT_ORDER_CHECK_BYID,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.COMMIT_ORDER_CHECK_BYID, false,
								null, "服务器繁忙，请重试！");
					}
				});

	} 

	/**
	 * 提交检查项订单
	 * 
	 * @param params
	 *            patientId 患者ID patientName 检查者姓名 patientSex 检查者性别 patientPhone
	 *            检查者电话 patientCardName 检查者证件类型 patientCardNum 证件号码
	 *            patientOrderType 订单类型 hospitalID 医院ID hospitalID 医院名称
	 *            patientSpecialNeed 特殊需求 testSelected 选择检查项表
	 * 
	 * 
	 * @param callBack
	 */
	public void commitCheckOrder(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.COMMIT_ORDER_CHECK, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderCheckManager
									.commitCheckOrder(result.toString());
							callBack.result(HttpParams.COMMIT_ORDER_CHECK,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.COMMIT_ORDER_CHECK, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}
	/**
	 * patientId	患者ID
	 * @param params
	 * @param callBack
	 */
	public void gainCheckOrderListByPId(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_ORDER_CHECK_LIST_BY_PID, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderCheckManager
									.getOrderCheckList(result.toString());
							callBack.result(HttpParams.GAIN_ORDER_CHECK_LIST_BY_PID,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GAIN_ORDER_CHECK_LIST_BY_PID, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}
	/**
	 * orderId  预约检查订单ID
	 * @param params
	 * @param callBack
	 */
	public void gainCheckOrderDetail(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_ORDER_CHECK_DETIAL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = OrderCheckManager
									.getOrderCheckDetails(result.toString());
							callBack.result(HttpParams.GAIN_ORDER_CHECK_DETIAL,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GAIN_ORDER_CHECK_DETIAL, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}
}
