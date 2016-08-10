/**
 * 
 */
package com.eztcn.user.eztcn.impl;

import java.util.HashMap;
import java.util.Map;

import xutils.http.RequestParams;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.api.InsReportApi;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.InsManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttpNet;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @author Liu Gang
 * 
 *         2015年11月24日 上午9:42:46 检验报告实现类
 */
public class InsReportImpl implements InsReportApi {

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.eztcn.user.eztcn.api.InsReportApi#getPatientMessage(java.util.HashMap
	 * , com.eztcn.user.eztcn.api.IHttpResult)
	 */
@Override
	public void getPatientMessage(HashMap<String,Object> params,
			final IHttpResult callBack) {
		
		
		EztHttpNet eztHttp = new EztHttpNet();
		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {

		
			public void onSuccess(Object result) {
				InsManager insManager = new InsManager();
				Map<String, Object> map = insManager.getPatientMessage(String
						.valueOf(result));
				callBack.result(HttpParams.GET_PATIENT_MSG_LIST, true, map);
			}

		
			public void onError(Object error) {
				callBack.result(HttpParams.GET_PATIENT_MSG_LIST, false, null,
						"服务器繁忙，请重试！");
			}

		});
		eztHttp.execute(EZTConfig.Do_Net_URL_PREINDEX, params,
				"GetPatientMessage");
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see
	 * com.eztcn.user.eztcn.api.InsReportApi#getSampleAndSampleDetailMessage
	 * (java.util.HashMap, com.eztcn.user.eztcn.api.IHttpResult)
	 */
@Override
	public void getSampleAndSampleDetailMessage(HashMap<String,Object> params,
			final IHttpResult callBack) {
		EztHttpNet eztHttp = new EztHttpNet();
		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {

		
			public void onSuccess(Object result) {
				InsManager insManager = new InsManager();
				Map<String, Object> map = insManager
						.getSampleAndSampleDetailMessage(String.valueOf(result));
				callBack.result(HttpParams.GET_SAMPLE_DETAIL_MSG, true, map);
			}

		
			public void onError(Object error) {
				callBack.result(HttpParams.GET_SAMPLE_DETAIL_MSG, false, null,
						"服务器繁忙，请重试！");
			}

		});
		eztHttp.execute(EZTConfig.Do_Net_URL_PREINDEX, params,
				"GetSampleAndSampleDetailMessage");

	}

}
