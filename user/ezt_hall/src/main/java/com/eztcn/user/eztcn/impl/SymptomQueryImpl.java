package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.bean.Symptom;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.NewsManager;
import com.eztcn.user.eztcn.controller.SymptomQueryManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 症状自查实现
 * @describe
 * @author ezt
 * @created 2014年12月24日
 */
public class SymptomQueryImpl  {//implements ISymptomQueryApi

	/**
	 * 根据部位获取症状列表
	 */
//	@Override
	public void getSymptomListOfPart(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SYMPTOM_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Symptom> sList = SymptomQueryManager
									.getSymptomListOfPart(result.toString());
							callBack.result(HttpParams.GET_SYMPTOM_LIST, true, sList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SYMPTOM_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Symptom> sList = SymptomQueryManager
//						.getSymptomListOfPart(result.toString());
//				callBack.result(HttpParams.GET_SYMPTOM_LIST, true, sList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_SYMPTOM_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_SYMPTOM_LIST, params);
	}

	/**
	 * 获取症状详情
	 */
//	@Override
	public void getSymptomDetailsOfId(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SYMPTOM_DETAILS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Symptom symptom = SymptomQueryManager
									.getSymptomDetailsOfId(result.toString());
							callBack.result(HttpParams.GET_SYMPTOM_DETAILS, true, symptom);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SYMPTOM_DETAILS, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Symptom symptom = SymptomQueryManager
//						.getSymptomDetailsOfId(result.toString());
//				callBack.result(HttpParams.GET_SYMPTOM_DETAILS, true, symptom);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_SYMPTOM_DETAILS, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_SYMPTOM_DETAILS, params);

	}

	/**
	 * 获取相关症状列表
	 */
//	@Override
	public void getSymptomListOfDept(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SYMPTOM_LIST_DEPT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Symptom> symptomList = SymptomQueryManager
									.getSymptomListOfDept(result.toString());
							callBack.result(HttpParams.GET_SYMPTOM_LIST_DEPT, true,
									symptomList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SYMPTOM_LIST_DEPT, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Symptom> symptomList = SymptomQueryManager
//						.getSymptomListOfDept(result.toString());
//				callBack.result(HttpParams.GET_SYMPTOM_LIST_DEPT, true,
//						symptomList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_SYMPTOM_LIST_DEPT, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_SYMPTOM_LIST_DEPT, params);

	}

	/**
	 * 根据症状id获取相关疾病列表
	 */
//	@Override
	public void getDiseaseListOfId(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DISEASE_LIST_ID, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Diseases> symptomList = SymptomQueryManager
									.getDiseasesListOfId(result.toString());
							callBack.result(HttpParams.GET_DISEASE_LIST_ID, true,
									symptomList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DISEASE_LIST_ID, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Diseases> symptomList = SymptomQueryManager
//						.getDiseasesListOfId(result.toString());
//				callBack.result(HttpParams.GET_DISEASE_LIST_ID, true,
//						symptomList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_DISEASE_LIST_ID, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_DISEASE_LIST_ID, params);

	}

	// 获取疾病详情
//	@Override
	public void getDiseaseDetails(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DISEASE_DETAILS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Diseases diseases = SymptomQueryManager
									.getDiseasesDetailsOfId(result.toString());
							callBack.result(HttpParams.GET_DISEASE_DETAILS, true, diseases);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DISEASE_DETAILS, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Diseases diseases = SymptomQueryManager
//						.getDiseasesDetailsOfId(result.toString());
//				callBack.result(HttpParams.GET_DISEASE_DETAILS, true, diseases);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_DISEASE_DETAILS, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_DISEASE_DETAILS, params);

	}

	/**
	 * 获取推荐医院科室
	 */
//	@Override
	public void getHosGeneralList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HOS_GENERAL_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Dept> deptList = SymptomQueryManager
									.getHosGeneralList(result.toString());
							callBack.result(HttpParams.GET_HOS_GENERAL_LIST, true, deptList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOS_GENERAL_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Dept> deptList = SymptomQueryManager
//						.getHosGeneralList(result.toString());
//				callBack.result(HttpParams.GET_HOS_GENERAL_LIST, true, deptList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_HOS_GENERAL_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_HOS_GENERAL_LIST, params);

	}

	/**
	 * 获取热门疾病列表
	 */
//	@Override
	public void getHotDiseaseList(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HOT_DISEASE_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Diseases> dList = SymptomQueryManager
									.getDiseasesList(result.toString(), 0);
							callBack.result(HttpParams.GET_HOT_DISEASE_LIST, true, dList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOT_DISEASE_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Diseases> dList = SymptomQueryManager
//						.getDiseasesList(result.toString(), 0);
//				callBack.result(HttpParams.GET_HOT_DISEASE_LIST, true, dList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_HOT_DISEASE_LIST, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_HOT_DISEASE_LIST, params);

	}

	/**
	 * 获取疾病列表
	 */
//	@Override
	public void getAllRegDiseases(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ALL_REGDISEASES, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Diseases> dList = SymptomQueryManager
									.getDiseasesList(result.toString(), 1);
							callBack.result(HttpParams.GET_ALL_REGDISEASES, true, dList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ALL_REGDISEASES, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Diseases> dList = SymptomQueryManager
//						.getDiseasesList(result.toString(), 1);
//				callBack.result(HttpParams.GET_ALL_REGDISEASES, true, dList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_ALL_REGDISEASES, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_ALL_REGDISEASES, params);

	}

	/**
	 * 根据首字母获取症状列表
	 */
//	@Override
	public void getSymptomOfLetter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SYMPTOM_LETTER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Symptom> sList = SymptomQueryManager
									.getSymptomListOfPart(result.toString());
							callBack.result(HttpParams.GET_SYMPTOM_LETTER, true, sList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SYMPTOM_LETTER, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Symptom> sList = SymptomQueryManager
//						.getSymptomListOfPart(result.toString());
//				callBack.result(HttpParams.GET_SYMPTOM_LETTER, true, sList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_SYMPTOM_LETTER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_SYMPTOM_LETTER, params);

	}

	/**
	 * 根据首字母获取疾病列表
	 */
//	@Override
	public void getDiseaseOfLetter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DISEASES_LETTER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Diseases> dList = SymptomQueryManager
									.getDiseasesListOfLetter(result.toString());
							callBack.result(HttpParams.GET_DISEASES_LETTER, true,
									dList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DISEASES_LETTER, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<Diseases> dList = SymptomQueryManager
//						.getDiseasesListOfLetter(result.toString());
//				callBack.result(HttpParams.GET_DISEASES_LETTER, true,
//						dList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_DISEASES_LETTER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_DISEASES_LETTER, params);

	}

	/**
	 * 获取症状详情相关博文
	 */
//	@Override
	public void getArticleOfSymptom(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ARTICLE_OF_SYMPTOM, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = NewsManager.getNewsList(result
									.toString());
							callBack.result(HttpParams.GET_ARTICLE_OF_SYMPTOM, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ARTICLE_OF_SYMPTOM, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = NewsManager.getNewsList(result
//						.toString());
//				callBack.result(HttpParams.GET_ARTICLE_OF_SYMPTOM, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_ARTICLE_OF_SYMPTOM, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_ARTICLE_OF_SYMPTOM, params);
		
	}

}
