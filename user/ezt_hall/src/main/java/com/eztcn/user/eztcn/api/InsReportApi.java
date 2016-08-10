package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * 检验报告 接口
 * 
 * @author Liu Gang
 * 
 *         2015年11月24日 上午9:32:20
 *         http://114.112.102.85:10004/WebServiceShiYanKe.asmx
 *         /GetSampleAndSampleDetailMessage?samp_id=565656
 * 
 */
public interface InsReportApi {
	/**
	 * 获取检验报告列表
	 * 
	 * http://114.112.102.85:10004/WebServiceShiYanKe.asmx
	 * http://114.112.102.85:10004/WebServiceShiYanKe.asmx/GetPatientMessage
	 * social_no=string HTTP/1.1
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getPatientMessage(HashMap<String, Object> params,
			IHttpResult callBack);
	/**
	 * 获取检验报告详情
	 * http://114.112.102.85:10004/WebServiceShiYanKe.asmx/GetSampleAndSampleDetailMessage
	 * samp_id=string HTTP/1.1
	 * @param params
	 * @param callBack
	 */
	public void getSampleAndSampleDetailMessage(HashMap<String, Object> params,
			IHttpResult callBack);
}
