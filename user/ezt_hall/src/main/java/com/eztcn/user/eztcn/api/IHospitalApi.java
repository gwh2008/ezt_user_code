package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 医院api
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public interface IHospitalApi {
	/**
	 * 获取当日号医院列表
	 * @param params
	 * @param callBack
	 */
	public void getDayHosList(HashMap<String, Object> params,
			IHttpResult callBack);
	/**
	 * 获取医院列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHospitalList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取医院详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHospitalDetail(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取二级科室分类
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDeptList(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取小科室分类
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDeptList2(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取科室列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getBigDeptList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取医生列表
	 * 
	 * @param params
	 * @param callBack
	 */
	// public void getDocList(HashMap<String, Object> params, IHttpResult
	// callBack);

	/**
	 * 获取排行医生列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getRankingDocList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取医生信息
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDocInfo(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取医生号池
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDocPool(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 获取搜索的医院
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSearchHos(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取搜索的科室
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSearchDept(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取搜索的医生
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSearchDoc(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取搜索的知识库
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSearchKnowLib(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 医生是否有号
	 * 
	 * @param params
	 * @param callBack
	 */
	public void ynRemain(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 根据医院ID患者ID获取医院一卡通号
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getOneCard(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getNearHosList(HashMap<String, Object> params,
			IHttpResult callBack);

}
