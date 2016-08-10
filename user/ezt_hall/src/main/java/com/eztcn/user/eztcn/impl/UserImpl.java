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
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.StatusCode;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.UserManager;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JsonUtil;
/**
 * @title 用户接口实现
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class UserImpl{
	public void userLogin(RequestParams params,
			final IHttpResult callBack) {
		HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
		eztHttp.send(HttpMethod.POST, EZTConfig.USER_LOGIN, params,
				new RequestCallBack<Object>() {

					public void onSuccess(ResponseInfo<Object> responseInfo) {
						if (200 == responseInfo.statusCode) {
						}

					}

					public void onFailure(HttpException error, String msg) {
						callBack.result(HttpParams.USER_LOGIN, false, null,
								"服务器繁忙，请重试！");
					}
				});
	}
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
	}
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
	}
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
	}

	/**
	 * 完善资料(实名认证)
	 */
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
	}

	/**
	 * 修改个人信息
	 */
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
	}

	/**
	 * 上传用户头像
	 */
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
	}
	/**
	 * 获取数据字典
	 */
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
	}

	/**
	 * 添加家庭成员
	 */
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
	}
	/**
	 * 获取家庭成员
	 */
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
	}
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
	}
	/**
	 * 删除家庭成员
	 */
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
	}

	/**
	 * 找回密码验证码
	 */
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
	}

	/**
	 * 验证找回密码验证码
	 */
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
	}

	/**
	 * 找回密码
	 */
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
	}

	/**
	 * 修改密码
	 */
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
	}

	/**
	 * 获取轮播图
	 */
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
	}
	/**
	 * 获取修改注册手机短信验证码
	 */
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
	}
	/**
	 * 修改注册手机号
	 */
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
	}

	/**
	 * 获取绑定邮箱验证码
	 */
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
	}
	/**
	 * 获取解除绑定邮箱验证码
	 */
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
	}

	/**
	 * 解除邮箱绑定
	 */
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
	}
	/**
	 * 验证邮箱地址是否被验证
	 */
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
	}
	/**
	 * 意见反馈
	 */
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
	}
	// 请愿医生
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
	}

	// 提交个推用到 cid 和 uid
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
	}
}
