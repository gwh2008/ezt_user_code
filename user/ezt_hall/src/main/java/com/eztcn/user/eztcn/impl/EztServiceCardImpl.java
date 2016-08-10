package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IEztServiceCard;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.ENurseHelpManager;
import com.eztcn.user.eztcn.controller.LightAccompanyingManager;
import com.eztcn.user.eztcn.controller.UserManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttp;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @title 轻陪诊实现
 * @describe
 * @author ezt
 * @created 2015年3月27日
 */
public class EztServiceCardImpl {

	// 获取套餐详情

	public void getPackageDetail(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_PACKAGE_DETAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.getPackageDetail(result.toString());
							callBack.result(HttpParams.GET_PACKAGE_DETAIL,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_PACKAGE_DETAIL, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	// 激活卡

	public void activateCard(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ACTIVATION, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.getActivation(result.toString());
							callBack.result(HttpParams.ACTIVATION, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ACTIVATION, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	// 创建健康卡订单

	public void createTraOrderpayPackage(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CREATE_TRAORDERPAY_PACKAGE,
				params, new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.createTraOrderpayPackage(result.toString());
							callBack.result(
									HttpParams.CREATE_TRAORDERPAY_PACKAGE,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CREATE_TRAORDERPAY_PACKAGE,
								false, null, "服务器繁忙，请重试！");
					}
				});

	}

	// 获取健康卡列表

	public void getHealthcardList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HEALTHCARD_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<HealthCard> list = LightAccompanyingManager
									.getHealthcardList(result.toString());
							callBack.result(HttpParams.GET_HEALTHCARD_LIST,
									true, list);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HEALTHCARD_LIST, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	// 获取健康卡服务详情

	public void getHealthcardDetail(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HEALTHCARD_DETAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.getHealthcardDetail(result.toString());
							callBack.result(HttpParams.GET_HEALTHCARD_DETAIL,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HEALTHCARD_DETAIL,
								false, null, "服务器繁忙，请重试！");
					}
				});

	}

	public void getItemDetail(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ITEM_DETAIL_NEW, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.getItemDetailNew(result.toString());
							callBack.result(HttpParams.GET_ITEM_DETAIL, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ITEM_DETAIL, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	// /**
	// * 获取服务项详情
	// */
	//
	// public void getItemDetail(RequestParams params,
	// final IHttpResult callBack) {
	//
	// EztHttp eztHttp = new EztHttp();
	// eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
	//
	//
	// public void onSuccess(Object result) {
	//
	// Map<String, Object> map = LightAccompanyingManager
	// .getItemDetail(result.toString());
	// callBack.result(HttpParams.GET_ITEM_DETAIL, true, map);
	// }
	//
	//
	// public void onError(Object error) {
	// callBack.result(HttpParams.GET_ITEM_DETAIL, false, null,
	// "服务器繁忙，请重试！");
	// }
	// });
	// eztHttp.execute(EZTConfig.GET_ITEM_DETAIL, params);
	// }

	// 获取龙卡详情

	public void getHealthDragonInfo(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HEALTHDRAGON_INFO, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.getHealthDragonInfo(result.toString());
							callBack.result(HttpParams.GET_HEALTHDRAGON_INFO,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HEALTHDRAGON_INFO,
								false, null, "服务器繁忙，请重试！");
					}
				});

	}

	// 绑定卡

	public void cardBinding(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CARD_BINDING, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);

							Map<String, Object> map = LightAccompanyingManager
									.Cardbinding(result.toString());
							callBack.result(HttpParams.CARD_BINDING, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CARD_BINDING, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	public void authentication(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.AUTHEN, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);

							Map<String, Object> map = LightAccompanyingManager
									.cardAuth(result.toString());
							callBack.result(HttpParams.DRAGON_AUTHEN, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.DRAGON_AUTHEN, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 判断龙卡绑定状态
	 * 
	 * @param params
	 *            uid
	 * @param callBack
	 */
	public void judgeDragonBind(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_CCB_INFO_BY_UID, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.gainCcbInfobyUid(String.valueOf(result));
							callBack.result(HttpParams.GET_CCB_INFO_BY_UID,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_CCB_INFO_BY_UID, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	public void getCCbInfo(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_CCBINFO, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = LightAccompanyingManager
									.gainCcbInfo30(String.valueOf(result));
							callBack.result(HttpParams.GAIN_CCBINFO, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GAIN_CCBINFO, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	public void sendSMS(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CCB_INfO_SEND_SMS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager
									.parseHttpJson(result.toString());
							callBack.result(HttpParams.CCB_INfO_SEND_SMS, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CCB_INfO_SEND_SMS, false,
								null, "服务器繁忙，请重试！");
					}
				});
	}

}
