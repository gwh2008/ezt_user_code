package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 轻陪诊接口
 * @describe
 * @author ezt
 * @created 2015年3月27日
 */
public interface IEztServiceCard {
	/**
	 * 获取龙卡信息
	 * @param params
	 * @param callBack
	 */
	public void getCCbInfo(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取套餐详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getPackageDetail(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 激活服务卡
	 * 
	 * @param params
	 * @param callBack
	 */
	public void activateCard(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 购买健康卡
	 * 
	 * @param params
	 * @param callBack
	 */
	public void createTraOrderpayPackage(HashMap<String, Object> params,
			IHttpResult callBack);

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

	/**
	 * 获取服务项详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getItemDetail(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取龙卡信息
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHealthDragonInfo(HashMap<String, Object> params,
			IHttpResult callBack);


	/**
	 * 龙卡激活
	 * 
	 * @param params
	 * @param callBack
	 */
	public void cardBinding(HashMap<String, Object> params, IHttpResult callBack);
	/**
	 * 
	 * http://127.0.0.1:8080/ezt-paas-2.0/api/v2/register/ccb/
	 * 验证用户身份
	 * @param params
	 * ep.
	 * TransCode=01
	 * BankCardId=6217000060021548134
	 * CustName=侯五六
	 * CustId=130528198801297238  
	 * @param callBack
	 */
	public void authentication(HashMap<String, Object> params, IHttpResult callBack);
	

}
