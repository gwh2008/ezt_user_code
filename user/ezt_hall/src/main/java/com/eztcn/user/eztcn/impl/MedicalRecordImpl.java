package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.api.IMedicalRecordApi;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.controller.MedicalRecordManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @title 病历
 * @describe
 * @author ezt
 * @created 2015年3月17日
 */
public class MedicalRecordImpl {

	
	public void getHospitalList(RequestParams params,
			final IHttpResult callBack) {
		
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HOSPITAL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<String> hosList = MedicalRecordManager
									.getHosList(result.toString());
							callBack.result(HttpParams.GET_HOSPITAL, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOSPITAL, false, null,
								"服务器繁忙，请重试！");
					}
				});
		
		
	}

	
	public void createEMR(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CREATE_EMR_1, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							int mId = MedicalRecordManager.parseRecord(result.toString());
							callBack.result(HttpParams.CREATE_EMR_1, true, mId);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CREATE_EMR_1, false, null,
								"服务器繁忙，请重试！");

					}
				});
		
		
	}

	
	public void createEMR_second(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CREATE_EMR_2, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<String> hosList = MedicalRecordManager
									.getHosList(result.toString());
							callBack.result(HttpParams.CREATE_EMR_2, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CREATE_EMR_2, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	
	public void createEMR_upload(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.CREATE_EMR_UPLOAD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<String> hosList = MedicalRecordManager
									.getHosList(result.toString());
							callBack.result(HttpParams.CREATE_EMR_UPLOAD, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.CREATE_EMR_UPLOAD, false, null,
								"服务器繁忙，请重试！");

					}
				});
		
	}

	// 获取我的病历列表
	
	public void getMyIllRecords(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_MY_ILL_RECORDS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<MedicalRecord> recordList = MedicalRecordManager
									.getMyIllRecords(result.toString());
							callBack.result(HttpParams.GET_MY_ILL_RECORDS, true, recordList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_MY_ILL_RECORDS, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	// 获取病历详情
	
	public void getMyIllDetails(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_MY_ILL_DETAILS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							MedicalRecord record = MedicalRecordManager
									.getMyIllDetails(result.toString());
							callBack.result(HttpParams.GET_MY_ILL_DETAILS, true, record);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_MY_ILL_DETAILS, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 删除病历
	 */
	
	public void delMyIll(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.DEL_MY_ILL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = MedicalRecordManager.delMyIll(result
									.toString());
							callBack.result(HttpParams.DEL_MY_ILL, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.DEL_MY_ILL, false, null,
								"服务器繁忙，请重试！");

					}
				});


	}

	/**
	 * 删除病历图片
	 */
	
	public void delIllRecordImg(RequestParams params,
			final IHttpResult callBack) {
		
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.DEL_MY_ILL_IMG, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = MedicalRecordManager.delMyIll(result
									.toString());
							callBack.result(HttpParams.DEL_MY_ILL_IMG, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.DEL_MY_ILL_IMG, false, null,
								"服务器繁忙，请重试！");
					}
				});
	}

}
