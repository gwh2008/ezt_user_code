package com.eztcn.user.hall.impl;

import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.UserManager;
import com.eztcn.user.eztcn.utils.HttpParams;

import java.util.Map;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.RequestParams;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import xutils.http.client.HttpRequest;

/**
 * Created by zll on 2016/6/8.
 */
//更改账户接口的实现
public class ChAcImpl {
    /**
     * 获取修改注册手机短信验证码
     */
//	@Override
    public void getModifyPhoneCode(RequestParams params,
                                   final IHttpResult callBack) {
        HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
        eztHttp.send(HttpRequest.HttpMethod.POST, EZTConfig.MODIFY_PHONE_SECURITYCODE, params,
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
//	@Override
    public void userModifyPhone(RequestParams params,
                                final IHttpResult callBack) {
        HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
        eztHttp.send(HttpRequest.HttpMethod.POST, EZTConfig.MODIFY_PHONE, params,
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
     * 找回密码验证码
     */
//	@Override
    public void getFPwVerCode(RequestParams params,
                              final IHttpResult callBack) {
        HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
        eztHttp.send(HttpRequest.HttpMethod.POST, EZTConfig.GET_FPW_VERCODE, params,
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
//	@Override
    public void VerifyFPwVerCode(RequestParams params,
                                 final IHttpResult callBack) {
        HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
        eztHttp.send(HttpRequest.HttpMethod.POST, EZTConfig.VERIFY_FPW_VERCODE, params,
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
//	@Override
    public void FindPassWord(RequestParams params,
                             final IHttpResult callBack) {
        HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
        eztHttp.send(HttpRequest.HttpMethod.POST, EZTConfig.F_PW, params,
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
     * 完善资料(实名认证)
     */
//	@Override
    public void userAutonymName(RequestParams params,
                                final IHttpResult callBack) {
        HttpUtils eztHttp = new HttpUtils(EZTConfig.TIMEOUT);
        eztHttp.send(HttpRequest.HttpMethod.POST, EZTConfig.USER_AUTONYM, params,
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

}
