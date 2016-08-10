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
import com.eztcn.user.eztcn.controller.MessageMangager;
import com.eztcn.user.eztcn.controller.NewsManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @author Liu Gang
 * 
 *         2016年4月6日 上午9:31:04
 * 
 */
public class MessageImpl {
	/**
	 * 
	 * @param params
	 * @param callBack
	 * 
	 * sendType	信息类型 	必填
lyPfId	来源平台ID	必填
sendPfId	发送平台id	必填
mobile	手机号	必填
eztCode	加密验证码	必填
smsType	短信类型，用来区分内容	必填
sysDoctorName	导医姓名	必填
sysDoctorMobile	导医电话	必填

	 * 
	 * 
	 */
	public void sendSelf(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.SEND_SELF, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = MessageMangager
									.sendSelf(result.toString());

							callBack.result(HttpParams.SEND_SELF,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.SEND_SELF, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 
	 * @param params
	 * @param callBack
	 *            infoType 消息类型 userId 用户ID doctorName 医生姓名 hospitalName 医院名称
	 *            putPoolDate 放号时间
	 * 
	 *            1.8. 向服务器汇报状态，服务器返回消息，并将消息插入数据库
	 */
	public void traOrderInfo(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.TRA_ORDER_INFO, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = MessageMangager
									.traOrderInfo(result.toString());

							callBack.result(HttpParams.TRA_ORDER_INFO,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.TRA_ORDER_INFO, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * userId 用户ID type 消息类型 page 当前第几页
	 * 
	 * 1.9. 通过用户userId查询服务器消息列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void findOrderInfo(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.FIND_TRAORDER_INFO, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = MessageMangager
									.getOrderInfo(result.toString());
							if(map.get("flag")!=null){
								callBack.result(HttpParams.FIND_TRAORDER_INFO,
										map.get("flag"), map);
							}else{
								callBack.result(HttpParams.FIND_TRAORDER_INFO,
										false, map);
							}

						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.FIND_TRAORDER_INFO, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

}
