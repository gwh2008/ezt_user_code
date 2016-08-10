package com.eztcn.user.eztcn.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

import com.eztcn.user.eztcn.api.IHospitalApi;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 医院实现
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class HospitalImpl {

	/**
	 * hospitalId=2
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getHosConfig(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_HOS_CONFIG, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map map = HospitalManager.getHosConfig(result);
							callBack.result(HttpParams.GAIN_HOS_CONFIG,
									map.get("flag"), map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GAIN_HOS_CONFIG, false,
								null, "服务器繁忙，请重试！");

					}
				});

	}

	public void getDayHosList(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_REG_HOSlIST_DAY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Hospital> hosList = HospitalManager
									.getHosList(result.toString());
							callBack.result(HttpParams.GET_HOS, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOS, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 查询支持预约病床服务医院
	 * 
	 * @param params
	 * 
	 *            cityid 城市ID level 医院等级 hosType 医院类型（三级） countyid 地区ID
	 * 
	 * @param callBack
	 */
	public void getBedHosList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_BED_HOS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Hospital> hosList = HospitalManager
									.getHosList(result.toString());
							callBack.result(HttpParams.GET_HOS, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOS, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取预约检查医院列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getTestHosList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_TEST_HOS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Hospital> hosList = HospitalManager
									.getHosList(result.toString());
							callBack.result(HttpParams.GET_HOS, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOS, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取医院列表
	 */
	public void getHospitalList(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HOS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Hospital> hosList = HospitalManager
									.getHosList(result.toString());
							callBack.result(HttpParams.GET_HOS, true, hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOS, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	public void getHospitalDetail(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_HOS_DETAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = HospitalManager
									.getHosDetail(result.toString());
							callBack.result(HttpParams.GET_HOS_DETAIL, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_HOS_DETAIL, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取大科室分类
	 */

	public void getBigDeptList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_BIG_DEPT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Dept> deptList = HospitalManager
									.getBigDeptList(result.toString());
							callBack.result(HttpParams.GET_BIG_DEPT, true,
									deptList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_BIG_DEPT, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取二级科室列表
	 */

	public void getDeptList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DEPT_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Dept> deptList = HospitalManager
									.getDeptList(result.toString());
							callBack.result(HttpParams.GET_DEPT_LIST, true,
									deptList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DEPT_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}
	public void findResBedDept(RequestParams params, final IHttpResult callBack){

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.FIND_RES_BED_DEPT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							// ArrayList<Dept> deptList = HospitalManager
							// .getDeptList2(result.toString());
//							ArrayList<Dept> deptList = null;

							Map<String, Object> map = HospitalManager
									.findResBedDept(result.toString());
//							if (map.containsKey("deptList")) {
//								deptList = (ArrayList<Dept>) map
//										.get("deptList");
//							}
							// callBack.result(HttpParams.GET_DEPT_LIST2, true,
							// deptList);

							callBack.result(HttpParams.GET_DEPT_LIST2, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DEPT_LIST2, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取小科室列表
	 */

	public void getDeptList2(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DEPT_LIST2, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							// ArrayList<Dept> deptList = HospitalManager
							// .getDeptList2(result.toString());
							ArrayList<Dept> deptList = null;

							Map<String, Object> map = HospitalManager
									.getDeptList2(result.toString());
							if (map.containsKey("deptList")) {
								deptList = (ArrayList<Dept>) map
										.get("deptList");
							}
							// callBack.result(HttpParams.GET_DEPT_LIST2, true,
							// deptList);

							callBack.result(HttpParams.GET_DEPT_LIST2, true,
									map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DEPT_LIST2, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取医生信息 deptdocid=652&deptid=196&doctorid=652
	 */

	public void getDocInfo(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DOC_INFO, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Doctor doc = HospitalManager.getDocInfo(result
									.toString());
							callBack.result(HttpParams.GET_DOC_INFO, true, doc);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DOC_INFO, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取特定医院医生号池（专给预约药品用）
	 */
	public void getDocPool(RequestParams params, final IHttpResult callBack,
			final int whichHos) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DOC_POOL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Pool> pools = HospitalManager
									.getDocPool(result.toString());
							callBack.result(whichHos, true, pools);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(whichHos, false, null, "服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取医生号池
	 */

	public void getDocPool(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DOC_POOL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Pool> pools = HospitalManager
									.getDocPool(result.toString());
							callBack.result(HttpParams.GET_DOC_POOL, true,
									pools);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DOC_POOL, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取搜索的医院
	 */

	public void getSearchHos(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SEARCH_HOS, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> hosMap = HospitalManager
									.getSearchHosList(result.toString());
							callBack.result(HttpParams.GET_SEARCH_HOS, true,
									hosMap);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SEARCH_HOS, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取搜索的科室
	 */

	public void getSearchDept(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SEARCH_DEPT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> deptMap = HospitalManager
									.getSearchDeptList(result.toString());
							callBack.result(HttpParams.GET_SEARCH_DEPT, true,
									deptMap);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SEARCH_DEPT, false,
								null, "服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 获取搜索的医生
	 */

	public void getSearchDoc(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SEARCH_DOC, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> docMap = HospitalManager
									.getSearchDocList(result.toString());
							callBack.result(HttpParams.GET_SEARCH_DOC, true,
									docMap);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SEARCH_DOC, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取搜索的知识库
	 */

	public void getSearchKnowLib(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SEARCH_LIB, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> docMap = HospitalManager
									.getSearchKnowLib(result.toString());
							callBack.result(HttpParams.GET_SEARCH_LIB, true,
									docMap);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SEARCH_LIB, false, null,
								"服务器繁忙，请重试！");
					}
				});

	}

	/**
	 * 获取大牌名医列表
	 * 
	 * @param params
	 *            orderLevel=0& rowsPerPage=20& page=1& cityId=2& deptCateId=&
	 *            hospitalId=38& orderYnEvaluation=0& orderRate=0&
	 *            orderYnRemain=0& deptId=1278& sourcePfId=355& dcOrderParm=3
	 * 
	 * 
	 * 
	 * @param callBack
	 */
	public void getBigDocList(RequestParams params, final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GAIN_BIG_DOC_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							// ArrayList<Doctor> docList = HospitalManager
							// //2015-12-16 接口對接
							// .getRankDocList(result.toString());
							Map<String, Object> map = HospitalManager
									.getRankDocList(result.toString());
							// callBack.result(HttpParams.GET_RANKING_DOC_LIST,
							// true, docList);
							callBack.result(HttpParams.GET_RANKING_DOC_LIST,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_RANKING_DOC_LIST, false,
								null, "服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取排行医生列表
	 */

	public void getRankingDocList(RequestParams params,
			final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_RANKING_DOC_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							// ArrayList<Doctor> docList = HospitalManager
							// //2015-12-16 接口對接
							// .getRankDocList(result.toString());
							Map<String, Object> map = HospitalManager
									.getRankDocList(result.toString());
							// callBack.result(HttpParams.GET_RANKING_DOC_LIST,
							// true, docList);
							callBack.result(HttpParams.GET_RANKING_DOC_LIST,
									true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_RANKING_DOC_LIST, false,
								null, "服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 医生是否有号
	 */

	public void ynRemain(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.YN_REMAIN, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> docMap = HospitalManager
									.ynRemain(result.toString());
							callBack.result(HttpParams.YN_REMAIN, true, docMap);
						}

					}

					public void onFailure(HttpException error, String msg) {

						callBack.result(HttpParams.YN_REMAIN, false, null,
								"服务器繁忙，请重试！");

					}
				});
	}

	/**
	 * 根据医院ID患者ID获取医院一卡通号
	 */

	public void getOneCard(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_ONECARD, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> docMap = HospitalManager
									.parseOneCard(result.toString());
							callBack.result(HttpParams.GET_ONECARD, true,
									docMap);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_ONECARD, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

	/**
	 * 获取离我附近的医院
	 */

	public void getNearHosList(RequestParams params, final IHttpResult callBack) {

		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.NEAR_HOS_LIST, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<Hospital> hosList = HospitalManager
									.getNearHosList(result.toString());
							callBack.result(HttpParams.NEAR_HOS_LIST, true,
									hosList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.NEAR_HOS_LIST, false, null,
								"服务器繁忙，请重试！");

					}
				});

	}

}
