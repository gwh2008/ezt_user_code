package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 关注接口
 * @describe
 * @author ezt
 * @created 2015年1月4日
 */
public interface IAttentionApi {

	// 关注医生
	public void attentDoc(HashMap<String, Object> params, IHttpResult callBack);

	// 获取关注医生列表
	public void getAttentDocs(HashMap<String, Object> params,
			IHttpResult callBack);

	// 取消关注
	public void cancelAttentDoc(HashMap<String, Object> params,
			IHttpResult callBack);

	// 获取医生关注状态
	public void getAttentDocState(HashMap<String, Object> params,
			IHttpResult callBack);

	/**** 医院 ***********************/
	// 收藏医院
	public void attentHos(HashMap<String, Object> params, IHttpResult callBack);

	// 取消医院收藏
	public void cancelAttentHos(HashMap<String, Object> params,
			IHttpResult callBack);

	// 获取收藏医院列表
	public void getAttentHos(HashMap<String, Object> params,
			IHttpResult callBack);

	// 获取医院收藏状态
	public void getAttentHosState(HashMap<String, Object> params,
			IHttpResult callBack);
}
