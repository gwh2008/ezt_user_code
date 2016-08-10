package com.eztcn.user.eztcn.api;
import java.util.HashMap;

/**
 * @title 获取城市api
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public interface ICityApi {

	/**
	 * 获取区域列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getAreaList(HashMap<String, Object> params, IHttpResult callBack);
	
	
	/**
	 * 获取城市列表
	 * @param params
	 * @param callBack
	 */
	public void getCityList(HashMap<String, Object> params, IHttpResult callBack);

}
