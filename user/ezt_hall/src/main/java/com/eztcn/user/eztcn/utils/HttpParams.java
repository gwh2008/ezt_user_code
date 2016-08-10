package com.eztcn.user.eztcn.utils;

/**
 * @title 接口参数类
 * @describe
 * @author ezt
 * @created 2014年11月10日
 */
public class HttpParams {

	/**
	 * 用户
	 */
	public static final int GET_DICT = 1;// 获取字典表
	public static final int USER_LOGIN = 1;// 登录页面登录
	public static final int USER_LOGIN2 = 111;// 加载页面登录
	public static final int USER_REG = 2;
	public static final int VERIFY_PHONE = 3;// 验证手机号是否已注册
	public static final int GET_SECURITYCODE = 4;// 获取注册手机验证码
	public static final int USER_FORGETPWD = 36;// 忘记密码
	public static final int USER_AUTONYM = 1;// 完善资料(实名认证)
	public static final int USER_MODIFY = 5;// 修改个人信息
	public static final int USER_UPLOADPHOTO = 6;// 上传头像
	public static final int ADD_FAMILY = 6;// 添加家庭成员
	public static final int MEMBER_CENTER = 6;// 获取家庭成员
	public static final int MODIFY_FAMILY = 7;// 修改家庭成员
	public static final int DEL_MEMBER = 8;// 删除家庭成员
	public static final int GET_FPW_VERCODE = 9;// 获取找回密码手机验证码
	public static final int VERIFY_FPW_VERCODE = 10;// 验证找回密码验证码
	public static final int F_PW = 11;// 找回密码
	public static final int MODIFY_PW = 12;// 修改密码
	public static final int AD_LIST = 40;// 轮播图
	public static final int MODIFY_PHONE_SECURITYCODE = 1;// 获取修改注册手机短信验证码
	public static final int MODIFY_PHONE = 2;// 修改注册手机号
	public static final int VERIFY_EMAIL = 1;// 验证邮箱地址是否被验证
	public static final int BIND_EMAIL_SECURITYCODE = 2;// 发送绑定邮箱验证码
	public static final int REMOVE_EMAIL = 3;// 验证解绑邮箱并解绑
	public static final int REMOVE_EMAIL_SECURITYCODE = 4;// 发送解绑邮箱验证码

	public static final int SUGGESTION = 1;// 意见反馈

	public static final int SOFT_VERSION = 2;// 版本信息

	/**
	 * 获取城市
	 */
	public static final int GET_AREAS = 1;// 获取区域列表

	public static final int GET_CITY = 2;// 获取城市列表

	/**
	 * 症状自查
	 */
	public static final int GET_ALL_REGDISEASES = 1;// 获取疾病列表

	public static final int GET_SYMPTOM_LIST = 2;// 根据部位获取症状列表

	public static final int GET_SYMPTOM_DETAILS = 3;// 获取症状详情

	public static final int GET_SYMPTOM_LIST_DEPT = 4;// 获取相关症状列表

	public static final int GET_DISEASE_DETAILS = 5;// 获取疾病详情

	public static final int GET_HOS_GENERAL_LIST = 6;// 获取相关医院列表

	public static final int GET_HOT_DISEASE_LIST = 7;// 获取热门疾病

	public static final int GET_DISEASE_LIST_ID = 11;// 根据症状id获取疾病列表

	public static final int GET_SYMPTOM_LETTER = 8;// 根据首字母获取症状列表

	public static final int GET_DISEASES_LETTER = 9;// 根据首字母获取疾病列表

	public static final int GET_ARTICLE_OF_SYMPTOM = 10;// 获取症状相关博文
	/**
	 * 20160309
	 */
	public static final int GAIN_HOS_CONFIG = 1;// 获取医院首页配置
	/**
	 * 获取医院
	 */

	public static final int GET_HOS = 2;// 获取医院列表

	public static final int GET_HOS_DETAIL = 5;// 获取医院详情

	public static final int GET_BIG_DEPT = 1;// 获取科室分类

	public static final int GET_DEPT_LIST = 3;// 获取科室列表

	public static final int GET_DEPT_LIST2 = 4;// 获取小科室列表

	public static final int GET_DOC_INFO = 5;// 获取医生信息

	public static final int GET_DOC_POOL = 16;// 获取医生号池

	public static final int GET_SEARCH_HOS = 7;// 获取搜索的医院

	public static final int GET_SEARCH_DEPT = 8;// 获取搜索的科室

	public static final int GET_SEARCH_DOC = 9;// 获取搜索的医生

	public static final int GET_SEARCH_LIB = 11;// 获取搜索的知识库

	public static final int GET_RANKING_DOC_LIST = 10;// 获取排行医生列表

	public static final int YN_REMAIN = 17;// 医生是否有号

	public static final int GET_ONECARD = 18;// 根据医院ID患者ID获取医院一卡通号

	public static final int ADD_PETITION = 19;// 请愿

	public static final int SET_CLIENTID = 20;// 推送提交uid和cid到服务器后台

	public static final int NEAR_HOS_LIST = 21;// 获取离我附近的医院

	public static final int ENTER_QUEUE = 1;// 挂号入列

	public static final int YN_REG = 2;// 检测挂号

	public static final int REG_CONFIRM = 3;// 确认挂号

	public static final int GET_REG_RECORD = 4;// 已预约列表

	public static final int BACKNUMBER = 5;// 退号

	public static final int ADD_EVALUATE = 6;// 写评价

	public static final int ADD_THANKSLETTER = 7;// 写感谢信

	public static final int READ_THANKSLETTER = 8;// 获取感谢信列表

	public static final int READ_EVALUATE = 9;// 获取评价列表

	public static final int UNEVALUATE_COUNT = 10;// 查看未评价数

	public static final int UNEVALUATE_LIST = 11;// 患者未评价列表

	public static final int ORDERREGISTER = 12;// 预约登记

	public static final int ORRecord = 13;// 预约登记列表

	public static final int GETREGEDDOCTOR = 14;// 获取抢单医生列表

	public static final int CANCELREGEDRECORD = 15;// 取消预约登记

	public static final int AFFIRMORDER = 16;// 确认预约挂号

	public static final int GET_CHECKIN_DETAILS = 17;// 获取预约登记详情

	public static final int GET_REGREGISTER_NEW = 18;// 获取最新挂号信息

	public static final int GET_VALIDATE_CODE = 19;// 获取图片验证码
	public static final int GET_BACK_VAL_CODE = 20;// 2015-12-30 退号时候获取短信验证码
	public static final int GUIDE_DOCNUM = 21;// 提交导医号
	/**
	 * 挂号
	 */
	public static final int REG = 20;
	/**
	 * 订单
	 */
	public static final int CREATE_TRA_ORDER = 1;// 创建订单
	public static final int GET_ORDER_LIST = 2;// 获取订单列表
	public static final int DEL_ORDER = 3;// 删除订单
	public static final int ORDER_PAY = 4;// 立即支付

	/**
	 * 资讯
	 */
	public static final int GET_NEWS_DETAILS = 1;// 资讯详情

	public static final int GET_NEWS_COLUMN = 2;// 资讯栏目

	public static final int GET_NEWS_LIST = 3;// 资讯列表

	public static final int ADD_NEWS_COMMENT = 4;// 增加资讯评价

	public static final int GET_NEWS_COMMENT_LIST = 5;// 获取资讯评价列表

	/**
	 * 关注
	 */

	public static final int ATTENT_DOC = 1;// 关注医生

	public static final int GET_ATTENT_DOC_LIST = 2;// 获取关注医生列表

	public static final int CANCEL_ATTENT_DOC = 3;// 取消关注

	public static final int GET_ATTENT_DOC_STATE = 4;// 获取关注状态

	public static final int ATTENT_HOS = 1;// 收藏医院

	public static final int GET_ATTENT_HOS_LIST = 2;// 获取收藏医院列表

	public static final int CANCEL_ATTENT_HOS = 3;// 取消医院收藏

	public static final int GET_ATTENT_HOS_STATE = 4;// 获取医院收藏状态

	/**
	 * 电话医生
	 */
	public static final int CHECK_TEL_DOC_STATE = 11;// 查看电话医生状态

	public static final int GET_TEL_DOC_TIME = 12;// 根据日期获取医生可预约的时间段

	public static final int CONFIRM_TEL_DOC_ORDER = 13;// 确定预约电话医生

	public static final int PROMPT_TELING = 14;// 立即通话

	public static final int GET_TEL_DOC_RECORD = 15;// 获取通话记录

	public static final int GET_TEL_DOC_LIST = 16;// 获取电话医生列表

	public static final int CANCEL_PHONEORDER = 17;// 取消电话医生预约

	/**
	 * 电子病历
	 */
	public static final int GET_HOSPITAL = 1;// 获取相关医院

	public static final int CREATE_EMR_1 = 2;// 创建病历第一步

	public static final int CREATE_EMR_2 = 3;// 创建病历第二步

	public static final int CREATE_EMR_UPLOAD = 4;// 创建病历第三步

	public static final int GET_MY_ILL_RECORDS = 5;// 获取我的病历列表

	public static final int GET_MY_ILL_DETAILS = 6;// 获取病历详情

	public static final int DEL_MY_ILL = 7;// 删除病历

	public static final int DEL_MY_ILL_IMG = 8;// 删除病历图片

	/**
	 * 设置
	 */
	public static final int GET_VERSION = 1;// 版本更新

	/**
	 * 支付相关
	 */
	public static final int PAY_ORDER = 1;// 提交订单
	public static final int GET_PAY_RECORD = 3;// 获取充值记录
	public static final int GET_CURRENCY_RECORD = 4;// 获取健康币记录
	public static final int GET_CURRENCY_MONEY = 5;// 获取健康币余额
	public static final int GET_CALLMINUTE = 6;// 获取可拨打分钟数
	public static final int GET_PAY_MONEY = 2;// 获取支付金额
	public static final int GET_COUPON_LIST = 7;// 获取所有优惠券列表
	public static final int GET_USE_COUPON_LIST = 8;// 获取可使用的优惠券列表

	/**
	 * 外患相关
	 */
	public static final int GET_TUMOUR_INTRO = 1;// 肿瘤医院介绍
	public static final int QUICK_HELP = 2;// 快速求助
	public static final int GET_PROJECT_LIST = 3;// 获取肿瘤服务列表
	public static final int GET_TRAPACKAGE_LIST = 4;// 获取服务套餐列表
	public static final int GET_TRAPACKAGE_DETAIL = 5;// 服务套餐详情
	public static final int GET_RECOVERY_CASE_LIST = 6;// 康复病历列表
	public static final int GET_RECOVERY_CASE_DETAIL = 7;// 获取病历详情
	public static final int GET_PATIENT_GROUP = 8;// 获取患友信息
	public static final int ADD_TRASHOPPINGCART = 9;// 添加购物车

	/**
	 * 轻陪诊
	 */
	public static final int GET_PACKAGE_DETAIL = 1;// 获取套餐详情
	public static final int ACTIVATION = 2;// 激活卡
	public static final int CREATE_TRAORDERPAY_PACKAGE = 3;// 购买健康卡
	public static final int GET_HEALTHCARD_LIST = 4;// 获取健康卡列表
	public static final int GET_HEALTHCARD_DETAIL = 5;// 获取健康卡详情
	public static final int GET_ITEM_DETAIL = 6;// 获取卡服务项详情

	/**
	 * 健康龙卡
	 */
	public static final int GET_HEALTHDRAGON_INFO = 1;// 获取龙卡详情
	public static final int CARD_BINDING = 2;// 龙卡激活绑定
	public static final int DRAGON_AUTHEN = 3;// 龙卡用户身份验证
	public static final int GAIN_CCBINFO = 6;// 龙卡信息获取
	public static final int CCB_INfO_SEND_SMS = 11;// 龙卡获取短信验证码
	public static final int GET_CCB_INFO_BY_UID = 12;// 根据用户id获取龙卡信息

	/**
	 * 医护帮
	 */
	public static final int GET_MEALCARD_LIST = 1;// 获取服务套餐卡列表
	public static final int GET_MEALCARD_DETAIL = 2;// 获取服务套餐卡详情

	/**
	 * 我的记录 检验报告
	 */
	/**
	 * 大检验项目
	 */
	public static final int GET_PATIENT_MSG_LIST = 1;
	/**
	 * 检验项目详情
	 */
	public static final int GET_SAMPLE_DETAIL_MSG = 2;

	/**
	 * 访问出错
	 */
	public static final int POST_ERROR = 404;
	// ///////////////////////////////约检查///////////////////////////////////////////
	/**
	 * 获取检查项列表
	 */
	public static final int GAIN_ORDER_CHECK_LIST = 1;
	/**
	 * 提交预约检查订单
	 */
	public final static int COMMIT_ORDER_CHECK = 2;
	/**
	 * 根据患者Id查询订单列表
	 */
	public final static int GAIN_ORDER_CHECK_LIST_BY_PID = 3;
	/**
	 * 预约检查订单详情
	 */
	public final static int GAIN_ORDER_CHECK_DETIAL = 4;
	/**
	 * 根据订单id查询订单详情
	 */
	public final static int COMMIT_ORDER_CHECK_BYID = 5;

	public static final int GET_DOC_POOL_YZX = 17;// 获取天津市医中心医生号池
	public static final int GET_DOC_POOL_GRYY = 18;// 获取天津市工人医院医生号池
	public static final int GET_ORDER_BED_STATUS = 4;// 获取预约病床订单的状态
	public static final int CREATE_BED_ORDER = 5;// 提交约病床订单
	public static final int GET_ALIPAY_STRING_BY_ID = 6;// 提交约病床订单
	public static final int GET_ORDER_BED_LIST_BY_ID = 1;// 根据userId获取列表。
	
	public static final int TRA_ORDER_INFO=1;//1.8.	向服务器汇报状态，服务器返回消息，并将消息插入数据库
	public static final int FIND_TRAORDER_INFO=2;//1.9.	通过用户userId查询服务器消息列表
	public static final int SEND_SELF=3;//1.10.	向服务器提交信息，服务器发送短信
	
}
