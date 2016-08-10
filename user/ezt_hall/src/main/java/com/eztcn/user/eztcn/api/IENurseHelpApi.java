package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 医护帮
 * @describe
 * @author ezt
 * @created 2015年6月17日
 */
public interface IENurseHelpApi {

	/**
	 * 获取医护帮套餐卡列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getENurseCard(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取医护帮套餐卡详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getENurseCardDetail(HashMap<String, Object> params,
			IHttpResult callBack);
}
