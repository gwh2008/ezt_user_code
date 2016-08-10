package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 预约挂号
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public interface IRegistrationApi {
	
	public void reg(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 挂号入列
	 * 
	 * @param params
	 * @param callBack
	 */
	public void EnterQueue(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 检测是否挂号成功
	 * 
	 * @param params
	 * @param callBack
	 */
	public void isReg(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 确认挂号
	 * 
	 * @param params
	 * @param callBack
	 */
	public void regConfirm(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取已预约记录
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getRegRecord(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 退号
	 * 
	 * @param params
	 * @param callBack
	 */
	public void backNumber(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 写评价
	 * 
	 * @param params
	 * @param callBack
	 */
	public void writeEvaluate(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 写感谢信
	 * 
	 * @param params
	 * @param callBack
	 */
	public void writeThanksLetter(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取感谢信
	 * 
	 * @param params
	 * @param callBack
	 */
	public void readThanksLetter(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取综合评价
	 * 
	 * @param params
	 * @param callBack
	 */
	public void readEvaluateRecord(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取未评价数
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getUnEvaluateCount(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取未评价医生列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getUnEvaluateList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 预约登记
	 * 
	 * @param params
	 * @param callBack
	 */
	public void orderRegister(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 预约登记列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void orderRegisterList(HashMap<String, Object> params,
			IHttpResult callBack);
	
	
	/**
	 * 获取预约登记详情
	 * @param params
	 * @param callBack
	 */
	public void orderRegisterInfo(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取抢单医生列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getRegedDoctor(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 取消预约登记
	 * 
	 * @param params
	 * @param callBack
	 */
	public void cancelRegedRecord(HashMap<String, Object> params,
			IHttpResult callBack);
	
	
	/**
	 * 确认预约挂号
	 * 
	 * @param params
	 * @param callBack
	 */
	public void affirmOrder(HashMap<String, Object> params, IHttpResult callBack);
	
	
	/**
	 * 获取最新挂号信息
	 * @param params
	 * @param callBack
	 */
	public void getRegregisterNew(HashMap<String, Object> params, IHttpResult callBack);

}
