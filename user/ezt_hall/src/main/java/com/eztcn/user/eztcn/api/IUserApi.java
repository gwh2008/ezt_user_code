package com.eztcn.user.eztcn.api;

import java.util.HashMap;

public interface IUserApi {


	/**
	 * 获取轮播图
	 * 
	 */
	public void getAdsList(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 用户登录
	 * 
	 */
	public void userLogin(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 用户注册
	 * 
	 */
	public void userRegister(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 验证手机号是否被注册
	 * 
	 * @param params
	 * @param callBack
	 */
	public void verifyPhone(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取注册手机验证码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getSecurityCode(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 完善资料(实名认证)
	 * 
	 * @param params
	 * @param callBack
	 */
	public void userAutonymName(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 修改个人信息
	 * 
	 * @param params
	 * @param callBack
	 */
	public void userModify(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 上传用户头像
	 * 
	 * @param params
	 * @param callBack
	 */
//	public void userUploadPhoto(AjaxParams params, final IHttpResult callBack);

	/**
	 * 获取修改注册手机短信验证码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getModifyPhoneCode(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 修改注册手机号
	 * 
	 * @param params
	 * @param callBack
	 */
	public void userModifyPhone(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 验证邮箱是否注册
	 * 
	 * @param params
	 * @param callBack
	 */
	public void VerifyEmail(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取绑定邮箱验证码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getBindEmailCode(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取解除邮箱验证码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getRemoveEmailCode(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 解除邮箱
	 * 
	 * @param params
	 * @param callBack
	 */
	public void removeEmail(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取找回密码手机验证码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getFPwVerCode(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 验证找回密码验证码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void VerifyFPwVerCode(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 找回密码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void FindPassWord(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 修改密码
	 * 
	 * @param params
	 * @param callBack
	 */
	public void ModifyPassWord(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取字典表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getDictionary(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 添加家庭成员
	 * 
	 * @param params
	 * @param callBack
	 */
	public void addFamilyMember(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 获取家庭成员列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void getMemberCenter(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 修改家庭成员列表
	 * 
	 * @param params
	 * @param callBack
	 */
	public void modifyMemberCenter(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 删除家庭成员
	 * 
	 * @param params
	 * @param callBack
	 */
	public void delMemberCenter(HashMap<String, Object> params, final IHttpResult callBack);

	/**
	 * 意见反馈
	 * 
	 * @param params
	 * @param callBack
	 */
	public void sendSuggestion(HashMap<String, Object> params, final IHttpResult callBack);
	
	
	/**
	 * 请愿医生
	 * @param params
	 * @param callBack
	 */
	public void addPetition(HashMap<String, Object> params, final IHttpResult callBack);
	
	/**
	 * 提交个推用到 cid 和 uid
	 * @param params
	 * @param callBack
	 */
	public void setClientid(HashMap<String, Object> params, final IHttpResult callBack);

}
