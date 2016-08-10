package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 电子病历
 * @describe
 * @author ezt
 * @created 2015年3月17日
 */
public interface IMedicalRecordApi {

	/**
	 * 获取相关医院列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHospitalList(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 创建病历第一步
	 * 
	 * @param params
	 * @param callBack
	 */
	public void createEMR(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 创建病历第二步
	 * 
	 * @param params
	 * @param callBack
	 */
	public void createEMR_second(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 创建病历第三步(上传图片)
	 * 
	 * @param params
	 * @param callBack
	 */
	public void createEMR_upload(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取我的病历列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getMyIllRecords(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取病历详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getMyIllDetails(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 删除病历
	 * 
	 * @param params
	 * @param callBack
	 */
	public void delMyIll(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 删除病历图片
	 * 
	 * @param params
	 * @param callBack
	 */
	public void delIllRecordImg(HashMap<String, Object> params,
			IHttpResult callBack);

}
