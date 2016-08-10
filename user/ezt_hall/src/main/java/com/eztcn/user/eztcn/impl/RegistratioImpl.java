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
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @title 预约挂号实现
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class RegistratioImpl  {

	/**
	 * 挂号入列
	 */
	public void EnterQueue(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ENTER_QUEUE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager.enterQueue(result
									.toString());
							callBack.result(HttpParams.ENTER_QUEUE, true, map);
						}

					}
					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ENTER_QUEUE, false, null,
					"服务器繁忙，请重试！");

					}
				});
	}
	/**
	 * 检测是否挂号成功
	 */
	public void isReg(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.YN_REG, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager.isReg(result
									.toString());
							callBack.result(HttpParams.YN_REG, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.YN_REG, false, null, "服务器繁忙，请重试！");

					}
				});
	}
	/**
	 * 确认挂号
	 */
	public void regConfirm(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.REG_CONFIRM, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager.regConfirm(result
									.toString());
							callBack.result(HttpParams.REG_CONFIRM, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.REG_CONFIRM, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	/**
	 * 获取已预约列表
	 */
	public void getRegRecord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_REG_RECORD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.getRegRecord(result.toString());
							callBack.result(HttpParams.GET_REG_RECORD, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_REG_RECORD, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 退号
	 */
	public void backNumber(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.BACKNUMBER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parseJsonData(result.toString());
							callBack.result(HttpParams.BACKNUMBER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.BACKNUMBER, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 写评价
	 */
	public void writeEvaluate(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ADD_EVALUATE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parseJsonData(result.toString());
							callBack.result(HttpParams.ADD_EVALUATE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ADD_EVALUATE, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 写感谢信
	 */
	public void writeThanksLetter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ADD_THANKSLETTER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parseJsonData(result.toString());
							callBack.result(HttpParams.ADD_THANKSLETTER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ADD_THANKSLETTER, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	/**
	 * 读取感谢信
	 */
	public void readThanksLetter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.READ_THANKSLETTER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.getThanksLetterList(result.toString());
							callBack.result(HttpParams.READ_THANKSLETTER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.READ_THANKSLETTER, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	public void readEvaluateRecord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.READ_EVALUATE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.getEvaluateList(result.toString());
							callBack.result(HttpParams.READ_EVALUATE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.READ_EVALUATE, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	public void getUnEvaluateCount(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.UNEVALUATE_COUNT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parseUnEvaluateCount(result.toString());
							callBack.result(HttpParams.UNEVALUATE_COUNT, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.UNEVALUATE_COUNT, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	public void getUnEvaluateList(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.UNEVALUATE_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parseUnEvaluateList(result.toString());
							callBack.result(HttpParams.UNEVALUATE_LIST, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.UNEVALUATE_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	// 预约登记
	public void orderRegister(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ORDERREGISTER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.orderRegister(result.toString());
							callBack.result(HttpParams.ORDERREGISTER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ORDERREGISTER, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 预约登记列表
	 */
//	@Override
	public void orderRegisterList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ORRecord, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parserORRecord(result.toString());
							callBack.result(HttpParams.ORRecord, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ORRecord, false, null, "服务器繁忙，请重试！");

					}
				});
	}
	public void getRegedDoctor(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GETREGEDDOCTOR, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parserRegedDoctor(result.toString());
							callBack.result(HttpParams.GETREGEDDOCTOR, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GETREGEDDOCTOR, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	public void cancelRegedRecord(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CANCELREGEDRECORD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager.parserJson(result
									.toString());
							callBack.result(HttpParams.CANCELREGEDRECORD, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CANCELREGEDRECORD, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	public void affirmOrder(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.AFFIRMORDER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.parserOrderId(result.toString());
							callBack.result(HttpParams.AFFIRMORDER, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.AFFIRMORDER, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	/**
	 * 获取预约登记详情
	 */
	public void orderRegisterInfo(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_CHECKIN_DETAILS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.getCheckinDetails(result.toString());
							callBack.result(HttpParams.GET_CHECKIN_DETAILS, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_CHECKIN_DETAILS, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	public void getRegregisterNew(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_REGREGISTER_NEW, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.getRegregisterNew(result.toString());
							callBack.result(HttpParams.GET_REGREGISTER_NEW, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_REGREGISTER_NEW, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}
	public void reg(RequestParams params, final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.REG_TIME_OUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.REG, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = RegistrationManager
									.reg(result.toString());
							callBack.result(HttpParams.REG, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.REG, false, null,
								"服务器繁忙，请重试！");


					}
				});
	}
}