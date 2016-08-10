package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 症状自查接口
 * @describe
 * @author ezt
 * @created 2014年12月24日
 */
public interface ISymptomQueryApi {

	/**
	 * 根据疾病类型获取疾病列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getAllRegDiseases(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据部位获取症状列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSymptomListOfPart(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据症状id获取症状详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSymptomDetailsOfId(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据科室获取相关症状列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSymptomListOfDept(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据症状id获取疾病列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDiseaseListOfId(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据症状获取相关博文
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getArticleOfSymptom(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取疾病详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDiseaseDetails(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取相关医院列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHosGeneralList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取hot疾病列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHotDiseaseList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据首字母获取症状列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSymptomOfLetter(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 根据首字母获取疾病列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDiseaseOfLetter(HashMap<String, Object> params,
			IHttpResult callBack);
}
