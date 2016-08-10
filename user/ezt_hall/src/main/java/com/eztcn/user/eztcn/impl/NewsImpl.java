package com.eztcn.user.eztcn.impl;
import java.util.Map;
import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.controller.NewsManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.http.EztHttpNet.OnAsyncCallBack;

/**
 * @title 资讯实现
 * @describe
 * @author ezt
 * @created 2015年1月7日
 */
public class NewsImpl {

	/**
	 * 获取资讯详情
	 */
	public void getNewsDetail(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_NEWS_DETAILS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = NewsManager
									.getNewsDetail(result.toString());
							callBack.result(HttpParams.GET_NEWS_DETAILS, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_NEWS_DETAILS, false,
								null, "服务器繁忙，请重试！");
					}
				});

	}

	public void getNewsColumn(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_NEWS_COLUMN, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = NewsManager
									.getNewsColumn(result.toString());
							callBack.result(HttpParams.GET_NEWS_COLUMN, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_NEWS_COLUMN, false,
								null, "服务器繁忙，请重试！");

					}
				});
	}

	public void getNewsList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_NEWS_LIST_NEW, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = NewsManager
									.getNewsList(result.toString());
							callBack.result(HttpParams.GET_NEWS_LIST, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_NEWS_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	public void addNewsComment(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ADD_NEWS_COMMENT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = NewsManager
									.addNewsComment(result.toString());
							callBack.result(HttpParams.ADD_NEWS_COMMENT, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ADD_NEWS_COMMENT, false,
								null, "服务器繁忙，请重试！");

					}
				});

	}

	public void getNewsCommentList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_NEWS_COMMENT_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = NewsManager
									.getNewsCommentList(result.toString());
							callBack.result(HttpParams.GET_NEWS_COMMENT_LIST,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_NEWS_COMMENT_LIST,
								false, null, "服务器繁忙，请重试！");

					}
				});

	}

}
