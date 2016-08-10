package com.eztcn.user.eztcn.api;

import java.util.HashMap;

/**
 * @title 资讯接口
 * @describe
 * @author ezt
 * @created 2015年1月7日
 */
public interface INewsApi {

	/**
	 * 获取资讯栏目
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getNewsColumn(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取资讯详情
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getNewsDetail(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取资讯列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getNewsList(HashMap<String, Object> params, IHttpResult callBack);

	/**
	 * 增加资讯评论
	 * 
	 * @param params
	 * @param callBack
	 */
	public void addNewsComment(HashMap<String, Object> params,
			IHttpResult callBack);

	/**
	 * 获取资讯评论列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getNewsCommentList(HashMap<String, Object> params,
			IHttpResult callBack);

}
