package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 外患接口
 * @describe
 * @author ezt
 * @created 2015年3月25日
 */
public interface IForeignPatient {
	
	/**
	 * 获取肿瘤医院介绍
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getTumourIntro(HashMap<String, Object> params,
			IHttpResult callBack);
	
	/**
	 * 快速求助
	 * @param params
	 * @param callBack
	 */
	public void quickHelp(HashMap<String, Object> params,
			IHttpResult callBack);
	
	/**
	 * 获取肿瘤服务项目列表
	 * @param params
	 * @param callBack
	 */
	public void getProjectList(HashMap<String, Object> params,
			IHttpResult callBack);
	

	/**
	 * 获取服务套餐列表
	 * @param params
	 * @param callBack
	 */
	public void getTrapackage_list(HashMap<String, Object> params,
			IHttpResult callBack);
	
	/**
	 * 获取服务套餐详情
	 * @param params
	 * @param callBack
	 */
	public void getTrapackageDetail(HashMap<String, Object> params,
			IHttpResult callBack);
	
	/**
	 * 获取康复病历列表
	 * @param params
	 * @param callBack
	 */
	public void getRecoveryCaseList(HashMap<String, Object> params,
			IHttpResult callBack);
	
	/**
	 * 获取病历详情
	 * @param params
	 * @param callBack
	 */
	public void getRecoveryCaseDetail(HashMap<String, Object> params,
			IHttpResult callBack);
	
	
	/**
	 * 获取患友信息
	 * @param params
	 * @param callBack
	 */
	public void getPatientGroup(HashMap<String, Object> params,
			IHttpResult callBack);
	
	
	/**
	 * 添加购物车
	 * @param params
	 * @param callBack
	 */
	public void addTraShoppingCart(HashMap<String, Object> params,
			IHttpResult callBack);
}
