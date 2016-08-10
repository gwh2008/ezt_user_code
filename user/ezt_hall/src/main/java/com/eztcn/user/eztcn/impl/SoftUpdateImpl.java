package com.eztcn.user.eztcn.impl;

import java.util.Map;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.SettingManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

public class SoftUpdateImpl {

	/**
	 * 获取版本更新信息
	 */
	public void getSoftVersion(RequestParams params, final IHttpResult callback) {
		HttpUtils http=new HttpUtils(EZTConfig.TIMEOUT);
		
		http.send(HttpMethod.POST, EZTConfig.SOFT_VERSION,params, new RequestCallBack<Object>() {
			@Override
			public void onSuccess(ResponseInfo<Object> responseInfo) {
				if(200==responseInfo.statusCode){
					String t=String.valueOf(responseInfo.result);
					Map<String, Object> map = SettingManager.parseVersion(t
							.toString());
					callback.result(HttpParams.SOFT_VERSION, true, map);
				}
			
			}

			@Override
			public void onFailure(HttpException error, String strMsg) {
				callback.result(HttpParams.SOFT_VERSION, false, strMsg);
				
			}
		});
	}
}
