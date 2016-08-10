/**
 * 
 */
package com.eztcn.user.eztcn.api;

import java.util.HashMap;

import com.eztcn.user.eztcn.api.IHttpResult;

/**
 * @author Liu Gang
 * 
 * 2015年10月20日
 * 下午3:50:08
 *  服务接口
 */
public interface ServerAPi {

	/**
	 * 健康卡激活
	 * @param params
	 * @param callBack
	 */
	public void healthCarActivate(HashMap<String, Object> params, final IHttpResult callBack);
	
	/**
	 * 获取健康卡列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHealthcardList(HashMap<String, Object> params,
			IHttpResult callBack);
	
	/**
	 * 获取健康卡详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHealthcardDetail(HashMap<String, Object> params,
			IHttpResult callBack);
	
}
