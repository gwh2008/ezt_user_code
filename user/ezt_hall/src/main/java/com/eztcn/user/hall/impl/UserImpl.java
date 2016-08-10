package com.eztcn.user.hall.impl;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.StatusCode;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.UserManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest.HttpMethod;

/**
 * @title 用户接口实现
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class UserImpl {// implements IUserApi

//	@Override
	public void userLogin(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.USER_LOGIN, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
						/*	Map<String, Object> map = UserManager.parseLoginJson(result
									.toString());*/
						//	callBack.result(HttpParams.USER_LOGIN, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.USER_LOGIN, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseLoginJson(result
//						.toString());
//				callBack.result(HttpParams.USER_LOGIN, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.USER_LOGIN, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.USER_LOGIN, params);

	}

//	@Override
	public void userRegister(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.USER_REG, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.USER_REG, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.USER_REG, false, null, "服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.USER_REG, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.USER_REG, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.USER_REG, params);
	}

//	@Override
	public void verifyPhone(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.VERIFY_PHONE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.VERIFY_PHONE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.VERIFY_PHONE, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.VERIFY_PHONE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.VERIFY_PHONE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.VERIFY_PHONE, params);
	}

//	@Override
	public void getSecurityCode(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_SECURITYCODE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.GET_SECURITYCODE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_SECURITYCODE, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.GET_SECURITYCODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_SECURITYCODE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_SECURITYCODE, params);
	}

	/**
	 * 完善资料(实名认证)
	 */
//	@Override
	public void userAutonymName(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.USER_AUTONYM, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.USER_AUTONYM, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.USER_AUTONYM, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.USER_AUTONYM, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.USER_AUTONYM, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.USER_AUTONYM, params);
	}

	/**
	 * 修改个人信息
	 */
//	@Override
	public void userModify(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.USER_MODIFY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.USER_MODIFY, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.USER_MODIFY, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.USER_MODIFY, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.USER_MODIFY, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.USER_MODIFY, params);
	}

	/**
	 * 上传用户头像
	 */
//	@Override
//	public void userUploadPhoto(AjaxParams params, final IHttpResult callBack) {
//		FinalHttp http = FinalHttp.getInstance();
		
		public void userUploadPhoto(RequestParams params, final IHttpResult callBack) {
			
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.USER_UPLOADPHOTO, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String t = String.valueOf(responseInfo.result);
							Map<String, Object> map = new HashMap<String, Object>();
							StatusCode<String> data = JsonUtil.fromJson(
									t.toString(), StatusCode.class);
							if (data == null) {
								callBack.result(HttpParams.USER_UPLOADPHOTO, false,
										"上传失败");
								return;
							}
							if (data.isFlag()) {
								String path = data.getData();
								if (path != null) {
									map.put("path", path);
									// BaseApplication.eztUser.setPhoto(path
									// .substring(path.lastIndexOf("\\") + 1));
								}
							}
							map.put("flag", data.isFlag());
							map.put("msg", data.getDetailMsg());
							callBack.result(HttpParams.USER_UPLOADPHOTO, true, map);
						}

					}

					public void onFailure(HttpException error, String strMsg) {
						callBack.result(HttpParams.USER_UPLOADPHOTO, false,
								null, strMsg);
					}
				});
			
			
//			FinalHttp http = FinalHttp.getInstance();
//		http.post(EZTConfig.USER_UPLOADPHOTO, params,
//				new AjaxCallBack<Object>() {
//					@Override
//					public void onFailure(Throwable t, int errorNo,
//							String strMsg) {
//						super.onFailure(t, errorNo, strMsg);
//						callBack.result(HttpParams.USER_UPLOADPHOTO, false,
//								null, strMsg);
//					}
//
//					@SuppressWarnings("unchecked")
//					@Override
//					public void onSuccess(Object t) {
//						super.onSuccess(t);
//						Map<String, Object> map = new HashMap<String, Object>();
//						StatusCode<String> data = JsonUtil.fromJson(
//								t.toString(), StatusCode.class);
//						if (data == null) {
//							callBack.result(HttpParams.USER_UPLOADPHOTO, false,
//									"上传失败");
//							return;
//						}
//						if (data.isFlag()) {
//							String path = data.getData();
//							if (path != null) {
//								map.put("path", path);
//								// BaseApplication.eztUser.setPhoto(path
//								// .substring(path.lastIndexOf("\\") + 1));
//							}
//						}
//						map.put("flag", data.isFlag());
//						map.put("msg", data.getDetailMsg());
//						callBack.result(HttpParams.USER_UPLOADPHOTO, true, map);
//					}
//				});

	}

	/**
	 * 获取数据字典
	 */
//	@Override
	public void getDictionary(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_DICT, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<EztDictionary> list = UserManager
									.getDictionary(result.toString());
							callBack.result(HttpParams.GET_DICT, true, list);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_DICT, false, null, "服务器繁忙，请重试！");

					}
				});
		
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<EztDictionary> list = UserManager
//						.getDictionary(result.toString());
//				callBack.result(HttpParams.GET_DICT, true, list);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_DICT, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_DICT, params);

	}

	/**
	 * 添加家庭成员
	 */
//	@Override
	public void addFamilyMember(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ADD_FAMILY, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> addMap = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.ADD_FAMILY, true, addMap);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ADD_FAMILY, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> addMap = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.ADD_FAMILY, true, addMap);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.ADD_FAMILY, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.ADD_FAMILY, params);
	}

	/**
	 * 获取家庭成员
	 */
//	@Override
	public void getMemberCenter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_MEMBER_CENTER, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							ArrayList<FamilyMember> memberList = UserManager
									.getFamilyMember(result.toString());
							callBack.result(HttpParams.MEMBER_CENTER, true, memberList);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.MEMBER_CENTER, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				ArrayList<FamilyMember> memberList = UserManager
//						.getFamilyMember(result.toString());
//				callBack.result(HttpParams.MEMBER_CENTER, true, memberList);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.MEMBER_CENTER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_MEMBER_CENTER, params);

	}

//	@Override
	public void modifyMemberCenter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.MODIFY_FAMILY, params,
					new RequestCallBack<Object>() {

						public void onSuccess(ResponseInfo<Object> responseInfo) {
							if (200 == responseInfo.statusCode) {
								String result = String.valueOf(responseInfo.result);
								Map<String, Object> map = UserManager.parseHttpJson(result
										.toString());
								callBack.result(HttpParams.MODIFY_FAMILY, true, map);
							}

						}

						public void onFailure(HttpException error, String msg) {
							callBack.result(HttpParams.MODIFY_FAMILY, false, null,
									"服务器繁忙，请重试！");
						}
					});

//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.MODIFY_FAMILY, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.MODIFY_FAMILY, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.MODIFY_FAMILY, params);
	}

	/**
	 * 删除家庭成员
	 */
//	@Override
	public void delMemberCenter(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.DEL_MEMBER, params,
					new RequestCallBack<Object>() {

						public void onSuccess(ResponseInfo<Object> responseInfo) {
							if (200 == responseInfo.statusCode) {
								String result = String.valueOf(responseInfo.result);
								Map<String, Object> map = UserManager.parseHttpJson(result
										.toString());
								callBack.result(HttpParams.DEL_MEMBER, true, map);
							}

						}

						public void onFailure(HttpException error, String msg) {
							callBack.result(HttpParams.DEL_MEMBER, false, null,
									"服务器繁忙，请重试！");

						}
					});

//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.DEL_MEMBER, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.DEL_MEMBER, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.DEL_MEMBER, params);

	}

	/**
	 * 找回密码验证码
	 */
//	@Override
	public void getFPwVerCode(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.GET_FPW_VERCODE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.GET_FPW_VERCODE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.GET_FPW_VERCODE, false, null,
								"服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.GET_FPW_VERCODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.GET_FPW_VERCODE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.GET_FPW_VERCODE, params);
	}

	/**
	 * 验证找回密码验证码
	 */
//	@Override
	public void VerifyFPwVerCode(RequestParams params,
			final IHttpResult callBack) {
			HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.VERIFY_FPW_VERCODE, params,
					new RequestCallBack<Object>() {

						public void onSuccess(ResponseInfo<Object> responseInfo) {
							if (200 == responseInfo.statusCode) {
								String result = String.valueOf(responseInfo.result);
								Map<String, Object> map = UserManager.parseHttpJson(result
										.toString());
								callBack.result(HttpParams.VERIFY_FPW_VERCODE, true, map);
							}

						}

						public void onFailure(HttpException error, String msg) {
							callBack.result(HttpParams.VERIFY_FPW_VERCODE, false, null,
									"服务器繁忙，请重试！");

						}
					});

//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.VERIFY_FPW_VERCODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.VERIFY_FPW_VERCODE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.VERIFY_FPW_VERCODE, params);
	}

	/**
	 * 找回密码
	 */
//	@Override
	public void FindPassWord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.F_PW, params,
					new RequestCallBack<Object>() {

						public void onSuccess(ResponseInfo<Object> responseInfo) {
							if (200 == responseInfo.statusCode) {
								String result = String.valueOf(responseInfo.result);
								Map<String, Object> map = UserManager.parseHttpJson(result
										.toString());
								callBack.result(HttpParams.F_PW, true, map);
							}

						}

						public void onFailure(HttpException error, String msg) {
							callBack.result(HttpParams.F_PW, false, null, "服务器繁忙，请重试！");

						}
					});

//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.F_PW, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.F_PW, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.F_PW, params);
	}

	/**
	 * 修改密码
	 */
//	@Override
	public void ModifyPassWord(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.MODIFY_PW, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.MODIFY_PW, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.MODIFY_PW, false, null, "服务器繁忙，请重试！");
					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.MODIFY_PW, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.MODIFY_PW, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.MODIFY_PW, params);

	}

	/**
	 * 获取轮播图
	 */
//	@Override
	public void getAdsList(RequestParams params,
			final IHttpResult callBack) {HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.AD_LIST, params,
					new RequestCallBack<Object>() {

						public void onSuccess(ResponseInfo<Object> responseInfo) {
							if (200 == responseInfo.statusCode) {
								String result = String.valueOf(responseInfo.result);
								Map<String, Object> map = UserManager.getAdList(result
										.toString());
								callBack.result(HttpParams.AD_LIST, true, map);
							}

						}

						public void onFailure(HttpException error, String msg) {
							callBack.result(HttpParams.AD_LIST, false, null, "服务器繁忙，请重试！");

						}
					});

//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.getAdList(result
//						.toString());
//				callBack.result(HttpParams.AD_LIST, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.AD_LIST, false, null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.AD_LIST, params);
	}

	/**
	 * 获取修改注册手机短信验证码
	 */
//	@Override
	public void getModifyPhoneCode(RequestParams params,
			final IHttpResult callBack) {
			HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
			eztHttp.send(HttpMethod.POST, EZTConfig.MODIFY_PHONE_SECURITYCODE, params,
					new RequestCallBack<Object>() {

						public void onSuccess(ResponseInfo<Object> responseInfo) {
							if (200 == responseInfo.statusCode) {
								String result = String.valueOf(responseInfo.result);
								Map<String, Object> map = UserManager.parseHttpJson(result
										.toString());
								callBack.result(HttpParams.MODIFY_PHONE_SECURITYCODE, true, map);
							}

						}

						public void onFailure(HttpException error, String msg) {
							callBack.result(HttpParams.MODIFY_PHONE_SECURITYCODE, false,
									null, "服务器繁忙，请重试！");

						}
					});

//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.MODIFY_PHONE_SECURITYCODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.MODIFY_PHONE_SECURITYCODE, false,
//						null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.MODIFY_PHONE_SECURITYCODE, params);
	}

	/**
	 * 修改注册手机号
	 */
//	@Override
	public void userModifyPhone(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.MODIFY_PHONE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.MODIFY_PHONE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.MODIFY_PHONE, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.MODIFY_PHONE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.MODIFY_PHONE, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.MODIFY_PHONE, params);
	}

	/**
	 * 获取绑定邮箱验证码
	 */
//	@Override
	public void getBindEmailCode(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.BIND_EMAIL_SECURITYCODE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.BIND_EMAIL_SECURITYCODE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.BIND_EMAIL_SECURITYCODE, false,
								null, "服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.BIND_EMAIL_SECURITYCODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.BIND_EMAIL_SECURITYCODE, false,
//						null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.BIND_EMAIL_SECURITYCODE, params);

	}

	/**
	 * 获取解除绑定邮箱验证码
	 */
//	@Override
	public void getRemoveEmailCode(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.REMOVE_EMAIL_SECURITYCODE, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.REMOVE_EMAIL_SECURITYCODE, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.REMOVE_EMAIL_SECURITYCODE, false,
								null, "服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.REMOVE_EMAIL_SECURITYCODE, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.REMOVE_EMAIL_SECURITYCODE, false,
//						null, "服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.REMOVE_EMAIL_SECURITYCODE, params);
	}

	/**
	 * 解除邮箱绑定
	 */
//	@Override
	public void removeEmail(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.REMOVE_EMAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.REMOVE_EMAIL, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.REMOVE_EMAIL, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.REMOVE_EMAIL, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.REMOVE_EMAIL, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.REMOVE_EMAIL, params);
	}

	/**
	 * 验证邮箱地址是否被验证
	 */
//	@Override
	public void VerifyEmail(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.VERIFY_EMAIL, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.VERIFY_EMAIL, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.VERIFY_EMAIL, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.VERIFY_EMAIL, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.VERIFY_EMAIL, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.VERIFY_EMAIL, params);
	}

	/**
	 * 意见反馈
	 */
//	@Override
	public void sendSuggestion(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.SUGGESTION, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
							.toString());
							callBack.result(HttpParams.SUGGESTION, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.SUGGESTION, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.SUGGESTION, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.SUGGESTION, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.SUGGESTION, params);

	}

	// 请愿医生
//	@Override
	public void addPetition(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.ADD_PETITION, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.addPetition(result
									.toString());
							callBack.result(HttpParams.ADD_PETITION, true, map);

						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.ADD_PETITION, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.addPetition(result
//						.toString());
//				callBack.result(HttpParams.ADD_PETITION, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.ADD_PETITION, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.ADD_PETITION, params);
	}

	// 提交个推用到 cid 和 uid
//	@Override
	public void setClientid(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.SET_CLIENTID, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
							String result = String.valueOf(responseInfo.result);
							Map<String, Object> map = UserManager.parseHttpJson(result
									.toString());
							callBack.result(HttpParams.SET_CLIENTID, true, map);
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.SET_CLIENTID, false, null,
								"服务器繁忙，请重试！");

					}
				});
//		EztHttp eztHttp = new EztHttp();
//		eztHttp.setOnAsyncCallBack(new OnAsyncCallBack() {
//
//			@Override
//			public void onSuccess(Object result) {
//				Map<String, Object> map = UserManager.parseHttpJson(result
//						.toString());
//				callBack.result(HttpParams.SET_CLIENTID, true, map);
//			}
//
//			@Override
//			public void onError(Object error) {
//				callBack.result(HttpParams.SET_CLIENTID, false, null,
//						"服务器繁忙，请重试！");
//			}
//		});
//		eztHttp.execute(EZTConfig.SET_CLIENTID, params);

	}
}
