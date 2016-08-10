package com.eztcn.user.eztcn.config;

import com.eztcn.user.hall.http.HTTPHelper;

/**
 * @author ezt
 * @title 总配置文件
 * @describe
 * @created 2014年11月10日
 */
public class EZTConfig {

    /**
     * 二维码扫描下载地址
     */
    public static String QRCodeUrl = "http://mc.eztcn.com.cn/mc/toEztAppDown.ezt";

    /**
     * 服务器地址
     */
    public static String SERVER_HOST = "192.168.0.135";

    /**
     * 服务器图像地址
     */
    public static String IMG_SERVER = "http://images.eztcn.com.cn";
    // public static String IMG_SERVERBED = "http://112.74.72.126:8999";

    public static String CardId = "19";
    /**
     * 会员中心
     */
//     public static String SERVER_MC = "http://112.74.72.126/mc";// 阿里云测试
    // public static String SERVER_MC = "http://mctest.eztcn.com.cn/mc";// 线上测试
    // public static String SERVER_MC = "http://192.168.0.104:8080/mc";//天津本地


     public static String SERVER_MC = "http://mc.eztcn.com.cn/mc";//原来的线上版本的接口地址

   //  public static String SERVER_MC = "http://172.16.60.207:8081/mc";//现在的线下版本的测试接口地址，有数据验证
//    public static String SERVER_MC = "http://172.16.60.192:8081/mc";//景宗棠的现在的线下版本的测试接口地址，有数据验证

    /**
     * 打包地址
     */

//    public static String SERVER_MC = "http://test.mc.eztcn.com/mc";//现在的线下版本的测试接口地址，有数据验证


    /**
     * 预上线的地址
     */
//    public static String SERVER_MC = "http://mctest.eztcn.com.cn/mc";


    /**
     * 业务平台
     */
//     public static String SERVER_PAAS = "http://paas.eztcn.com.cn/paas";//原来的线上版本的接口地址
    // public static String SERVER_PAAS = "http://172.16.60.207:8082/paas";//现在的线下版本的测试接口地址，有数据验证
//    public static String SERVER_PAAS = "http://172.16.60.192:8082/paas";//景宗棠的现在的线下版本的测试接口地址，有数据验证

    /**
     * 打包地址。
     */

//    public static String SERVER_PAAS = "http://test.paas.eztcn.com/paas";


    /**
     * 遇预上线地址
     */
//    public static String SERVER_PAAS = "http://paastest.eztcn.com.cn/paas";


    /**
     * 线上地址。
     */
    public static final String SERVER_PAAS = "http://paas.eztcn.com.cn/paas/";//原来的线上版本的接口地址


//     public static String SERVER_PAAS = "http://112.74.72.126/paas";// 阿里云测试

    // 龙卡前缀
    public static String SERVER_Dragon = "http://www.eztcn.com/";// 龙卡前缀
    // 线上测试

    /**
     * 滴滴优惠券网址
     */
    public static String DIDI_COUPON_URL = "http://gsactivity.diditaxi.com.cn/gulfstream/activity/v2/giftpackage/index?channel=7ac85d507708074ce6d7cb94fb0fe58d";
    public static String DIDI_DATE = "2015-07-21 00:00:00";// 滴滴起始活动时间
    /**
     * 官网网址
     */
    public static String OFFICIAL_WEBSITE = "http://www.eztcn.com";

    // 医生头像
    public static String DOC_PHOTO = IMG_SERVER + "/images/doctor/ezt2.0/";

    // 医院图片
    public static String HOS_PHOTO = IMG_SERVER + "/images/hospital/eztcn2.0/";

    // 广告图
    public static String HOME_ADS_PIC = IMG_SERVER + "/images/advertise/image/";

    // 套餐相关图片
    public static String PACKAGE_IMG = IMG_SERVER + "/images/package/eztcn2.0/";

    // 预约病床上传图片
    public static String ORDERBED_IMG = IMG_SERVER
            + "/images/reservationBed/medicalRecordPic/";
    // public static String ORDERBED_IMG = IMG_SERVERBED
    // + "/app_images/reservationBed/medicalRecordPic/";

    /**
     * IM服务器客户端到服务器端口
     */
    public static int SERVER_PORT = 5222;

    /**
     * 网络连接超时时间
     */
    public static int HTTP_TIMEOUT_CONNECTION = 10 * 1000;
    /**
     * 等待数据超时时间
     */
    public static int HTTP_SOTIMOUT = 10 * 1000;

    /**
     * 设置是否打印日志
     */
    public static boolean IS_PRINT_LOG = true;

    /**
     * 收到新消息手机振动持续时间(ms)
     */
    public static int VIBRATOR_DURATION = 300;

    /**
     * 获取验证码时间
     */
    public static int GET_VERCODE_TIME = 60;

    /**
     * shape..存储键
     */
    public static String KEY_ACCOUNT = "account";// 用户名

    public static String KEY_PW = "password";// 密码

    public static String KEY_IS_AUTO_LOGIN = "autoMaticLogin";// 是否自动登录

    public static String KEY_IS_FIRST = "isFirst";// 是否第一次启动

    public static String KEY_SET_VIBRATOR = "isVibrator";// 是否震动

    public static String KEY_SET_SOUND = "isSound";// 是否有声音

    public static String KEY_SET_SHOW_ICON = "isShowIcon";// 是否显示通知栏图标

    public static String KEY_SET_MSG_HINT = "msgHint";// 新消息提醒设置

    public static String KEY_SET_VERSION_CODE = "versionCode";// 版本号

    public static String KEY_CID = "cid";// 推送cid
    public static String KEY_TOTAL = "total";
    /**
     * 2015-12-31 退号时间
     */
    public static String KEY_BACKNUM_TIME = "back_num_time";

    /*
     * 更新提醒相关
     */
    public static String KEY_IS_UPDATE = "isUpdate";// 是否可更新

    public static String KEY_IS_SETTING = "isFirstSetting";// 是否第一次点击设置

    public static String KEY_IS_USER = "isFirstUser";// 是否第一次点击我频道

    /*
     * 筛选医生
     */
    public static String KEY_SET_THREEHOS = "isThreeHos";// 是否三甲医院
    public static String KEY_SET_NUM = "isHaveNum";// 是否有号
    public static String KEY_SET_RATE = "isRate";// 预约率好低
    public static String KEY_SET_EVALUATE = "isEvaluate";// 评价高低
    public static String KEY_SET_LEVEL = "isLevel";// 级别高低
    public static String KEY_SET_STATE = "isState";// 智能筛选是否在线

    /*
     * 选择医院
     */
    public static String KEY_SELECT_AREA_POS = "selectAreaPos";// 区域选中下标
    // public static String KEY_SELECT_AREA_WB_POS = "selectAreaWBPos";//
    // 妇幼区域选中下标

    public static String KEY_SELECT_HOS_POS = "selectHosPos";// 医院选中下标
    // public static String KEY_SELECT_HOS_WB_POS = "selectHosWBPos";// 医院选中下标

    public static String KEY_SELECT_N_POS = "selectNPos";// 智能筛选选中下标
    // public static String KEY_SELECT_N_WB_POS = "selectWBNPos";// 智能筛选选中妇幼下标

    public static String KEY_STR_CITY = "strCity";// 选中城市
    // public static String KEY_STR_WB_CITY = "strWBCity";// 选中妇幼城市

    public static String KEY_CITY_ID = "city_id";// 选中的城市id
    // public static String KEY_CITY_WB_ID = "city_WB_id";// 选中的城市id

    public static String KEY_STR_N = "strN";// 智能筛选
    // public static String KEY_STR_WB_N = "strWBN";// 智能筛选

    public static String KEY_HOS_ID = "hosId";// 所选医院id
    // public static String KEY_WB_HOS_ID = "wb_hosId";// 所选妇幼医院id

    public static String KEY_HOS_NAME = "hosName";// 所选医院
    // public static String KEY_WB_HOS_NAME = "KEY_WB_HOS_NAME";// 所选妇女幼儿医院

    /*
     * 选择科室（按医院找）
     */
    public static String KEY_DEPT_ID = "deptId";// 医院科室id
    // public static String KEY_DEPT_WB_ID = "deptwbId";// 妇幼医院科室id

    public static String KEY_STR_DEPT = "strDept";// 科室名称
    // public static String KEY_STR_WB_DEPT = "strWBDept";// 妇女幼儿科室名称

    public static String KEY_SELECT_DEPT_POS = "selectDeptPos";// 医院科室下标
    // public static String KEY_SELECT_WB_DEPT_POS = "selectWBDeptPos";// 医院科室下标

    /*
     * 选择科室（按科室找）
     */
    public static String KEY_DF_DEPT_ID = "df_deptId";// 科室小分类id
    // public static String KEY_DF_WB_DEPT_ID = "df_WB_deptId";// 科室小分类id

    public static String KEY_DF_STR_DEPT = "df_strDept";// 科室小分类名称
    // public static String KEY_DF_WB_STR_DEPT = "df_wb_strDept";// 科室小分类名称

    public static String KEY_DF_SELECT_DEPT2_POS = "df_selectDept2Pos";// 科室小分类下标
    // public static String KEY_DF_WB_SELECT_DEPT2_POS =
    // "df_wb_selectDept2Pos";// 科室小分类下标

    public static String KEY_DF_SELECT_DEPT_POS = "df_selectDeptPos";// 科室大分类分类下标
    // public static String KEY_DF_WB_SELECT_DEPT_POS = "df_wb_selectDeptPos";//
    // 科室大分类分类下标
    public static String KEY_DF_IS_DAY_REG = "isDayReg";// 是否当日挂号 文件2016-03-09
    public static String KEY_HAVE_MSG = "haveMsg";// 是否当日挂号 文件2016-03-09
    /**
     * 应用资源存储目录
     */
    public static String DIR_AFINAL_IMG = "afinalImg";// Afinal图片缓存目录
    public static String RES_SAVE_DIR = "EztUser";// 本地文件缓存根目录
    public static String DIR_SD_CARD_IMG = "sdCardImg";// sdcard图片存储目录

    public static String DIR_METER_AUDIO = "meterAudio";// 语音录制文件存储目录及文件名前缀

    public static String DIR_RECEIVE_AUDIO = "receiveAudio";// 收到好友发来的语音文件存储目录及文件名前缀

    /**
     * 每页显示的条数
     */
    public static int PAGE_SIZE = 20;

    /**
     * 广告图更换时间间隔
     */
    public static int SCROLL_TIME = 5000;

    // 友盟
    public static String DESCRIPTOR = "com.umeng.share";

    /**
     * 提交，获取数据URL
     */
    // 获取字典表数据
    public static String GET_DICT = SERVER_MC
            + "/api/v2/base/dictConstant/list.do";

    // 轮播图
    public static String AD_LIST = SERVER_MC
            + "/api/v2/memberCenter/advertise/getAdvertise.do";

    // 获取区域列表
    public static String GET_AREAS = SERVER_MC
            + "/api/v2/base/eztZCounty/list.do";

    // 获取城市列表
    public static String GET_CITY = SERVER_MC + "/api/v2/base/eztZCity/list.do";

    // 获取医院
    public static String GET_HOS = SERVER_MC
            + "/api/v2/memberCenter/eztHospital/list.do";
    // 获取支持约检查医院列表
    public static String GET_TEST_HOS = SERVER_MC
            + "/api/v2/memberCenter/eztHospital/checkItem.do";

    // 获取医院详情
    public static String GET_HOS_DETAIL = SERVER_MC
            + "/api/v2/memberCenter/eztHospital/detail.do";

    // 获取大科室分类
    public static String GET_BIG_DEPT = SERVER_MC
            + "/api/v2/memberCenter/eztDeptCate/list.do";

    // 获取二级科室列表
    public static String GET_DEPT_LIST = SERVER_MC
            + "/api/v2/memberCenter/eztDeptCate/sonlist.do";

    // 获取小科室列表
    public static String GET_DEPT_LIST2 = SERVER_MC
            + "/api/v2/memberCenter/eztDept/list.do";

    // 获取医生信息
    public static String GET_DOC_INFO = SERVER_MC
            + "/api/v2/memberCenter/eztDoctor/detail.do";
    // 获取医生号池
    public static String GET_DOC_POOL = SERVER_PAAS
            + "/api/v2/register/pool/getDoctorPoolAndroid.do";
    // 获取验证码
    public static String GET_VALIDE_CODE = SERVER_PAAS
            + "/api/v2/captcha/small.do";
    // 挂号入列
    public static String ENTER_QUEUE = SERVER_PAAS
            + "/api/v2/register/regRegister/enterQueue.do";

    // 是否挂号成功
    public static String YN_REG = SERVER_PAAS
            + "/api/v2/register/regRegister/ynReg.do";

    // 确认挂号
    public static String REG_CONFIRM = SERVER_PAAS
            + "/api/v2/register/regRegister/confirm.do";
    /**
     * 挂号
     */
    public static String REG = SERVER_PAAS
            + "/api/v2/register/regRegister/eztRegister.do";

    // 已查询记录
    public static String GET_REG_RECORD = SERVER_PAAS
            + "/api/v2/register/regRegister/regRecord.do";
    // 退号
    public static String BACKNUMBER_ = SERVER_PAAS
            + "/api/v2/register/back/enter.do";
    // 退号
    // public static String BACKNUMBER = SERVER_PAAS
    // + "/api/v2/register/back/regEnterBack.do";
    public static String BACKNUMBER = SERVER_PAAS
            + "/api/v2/register/back/regCommonBack.do";

    // 写评价
    public static String ADD_EVALUATE = SERVER_PAAS
            + "/api/v2/business/bizAfterEvaluation/add.do";
    // 写感谢信
    public static String ADD_THANKSLETTER = SERVER_MC
            + "/api/v2/memberCenter/eztThankNote/thankNoteAdd.do";

    // 读取感谢信
    public static String READ_THANKSLETTER = SERVER_MC
            + "/api/v2/memberCenter/eztThankNote/thankNoteDoctorList.do";

    // 读取综合评价
    public static String READ_EVALUATE = SERVER_PAAS
            + "/api/v2/business/bizAfterEvaluation/doctorList.do";

    // 获取未评价数
    public static String UNEVALUATE_COUNT = SERVER_PAAS
            + "/api/v2/business/bizAfterEvaluation/getNotAfterEvaluationConunt.do";

    // 获取未评价医生列表
    public static String UNEVALUATE_LIST = SERVER_PAAS
            + "/api/v2/business/bizAfterEvaluation/getNotAfterEvaluation.do";

    // 获取家庭成员
    public static String GET_MEMBER_CENTER = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/familyList.do";

    // 删除家庭成员
    public static String DEL_MEMBER = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/familyDel.do";

    // 获取找回密码验证码
    public static String GET_FPW_VERCODE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/sendSmsforFb.do";

    // 验证找回密码短信验证码
    public static String VERIFY_FPW_VERCODE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/mobileActivationforFb.do";

    // 找回密码
    public static String F_PW = SERVER_MC
            + "/api/v2/memberCenter/eztUser/modifyPasswordFb.do";

    // 修改密码
    public static String MODIFY_PW = SERVER_MC
            + "/api/v2/memberCenter/eztUser/modifyPassword.do";

    // 获取搜索的医院
    public static String GET_SEARCH_HOS = SERVER_MC
            + "/api/v2/totalStationSearch/eztHospital/list.do";

    // 获取搜索的科室
    public static String GET_SEARCH_DEPT = SERVER_MC
            + "/api/v2/totalStationSearch/eztDept/list.do";

    // 获取搜索的医生
    public static String GET_SEARCH_DOC = SERVER_MC
            + "/api/v2/totalStationSearch/eztDoctor/list.do";

    // 获取搜索知识库
    public static String GET_SEARCH_LIB = SERVER_MC
            + "/api/v2/searchCmsSearch/eztCmsInfoList.do";

    // 用户登录
    public static String USER_LOGIN = SERVER_MC + "/api/v2/eztPatient/login.do";

    // 用户注册
    public static String USER_REG = SERVER_MC
            + "/api/v2/memberCenter/eztUser/regPatientfStep.do";

    // 验证手机号是否被注册
    public static String VERIFY_PHONE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/volidateMobile.do";

    // 获取注册手机验证码
    public static String GET_SECURITYCODE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/sendRegisterSms.do";
    /**
     * 获取修改注册手机短信验证码
     */
    public static String MODIFY_PHONE_SECURITYCODE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/sendUpdateMobileSms.do";
    // 修改注册手机号
    public static String MODIFY_PHONE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/updateMobile.do";
    // 验证邮箱地址是否被验证
    public static String VERIFY_EMAIL = SERVER_MC
            + "/api/v2/memberCenter/eztUser/volidateEmail.do";
    // 绑定邮箱
    // public static String BIND_EMAIL = SERVER_MC
    // + "/api/v2/memberCenter/eztUser/updateMobile.do";
    // 发送绑定邮箱验证码
    public static String BIND_EMAIL_SECURITYCODE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/sendEmailforBinding.do";
    // 验证解绑邮箱并解绑
    public static String REMOVE_EMAIL = SERVER_MC
            + "/api/v2/memberCenter/eztUser/removeEmailActivation.do";
    // 发送解绑邮箱验证码
    public static String REMOVE_EMAIL_SECURITYCODE = SERVER_MC
            + "/api/v2/memberCenter/eztUser/removeEmail.do";

    // 提交订单
    public static String CREATE_TRA_ORDER = SERVER_PAAS
            + "/api/v2/trading/traOrder/createTraOrder.do";

    // 用户资料完善
    public static String USER_AUTONYM = SERVER_MC
            + "/api/v2/memberCenter/eztUser/regPatientsStep.do";

    // 用户信息修改
    public static String USER_MODIFY = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/edit.do";

    // 用户头像上传
    public static String USER_UPLOADPHOTO = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/picUpload.do";

    // 用户头像地址
    public static String USER_PHOTO = IMG_SERVER + "/images/patient/ezt/text";

    // 添加家庭成员
    public static String ADD_FAMILY = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/familyAdd.do";

    // 修改家庭成员
    public static String MODIFY_FAMILY = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/familyEdit.do";

    // 关注医生
    public static String ATTENT_DOC = SERVER_MC
            + "/api/v2/memberCenter/collect/AddCollectDoctorInfo.do";
    // 取消关注
    public static String CANCEL_ATTENT_DOC = SERVER_MC
            + "/api/v2/memberCenter/collect/delCollectsDoctorInfo.do";

    // 获取已关注医生列表
    public static String GET_ATTENT_DOC_LIST = SERVER_MC
            + "/api/v2/memberCenter/collect/getUserSelectCollectsDoctorList.do";

    // 获取某个医生关注状态
    public static String GET_ATTENT_DOC_STATE = SERVER_MC
            + "/api/v2/memberCenter/collect/getYesOrNoCollect.do";

    // 收藏医院
    public static String ATTENT_HOS = SERVER_MC
            + "/api/v2/memberCenter/collect/AddCollectHospitalInfo.do";
    // 取消医院收藏
    public static String CANCEL_ATTENT_HOS = SERVER_MC
            + "/api/v2/memberCenter/collect/delCollectHospitalInfo.do";

    // 获取已收藏医院列表
    public static String GET_ATTENT_HOS_LIST = SERVER_MC
            + "/api/v2/memberCenter/collect/getCollectHospitalList.do";

    // 获取某个医院收藏状态
    public static String GET_ATTENT_HOS_STATE = SERVER_MC
            + "/api/v2/memberCenter/collect/getYesOrNoHospitalCollect.do";

    // 获取通用医生列表
    public static String GET_RANKING_DOC_LIST = SERVER_PAAS
            + "/api/v2/register/eztDoctor/generalList.do";

    public static String GAIN_BIG_DOC_LIST = SERVER_PAAS
            + "/api/v2/register/eztDoctor/getFamousDoctorList.do";
    // 获取微资讯栏目
    public static String GET_NEWS_COLUMN = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsType.php";


    // 获取资讯列表
    public static String GET_NEWS_LIST = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsList.php";
    public static String GET_NEWS_LIST_NEW = "http://101.201.37.180:7800/ezt_p1/rest/ezt_article/GetEZTUserArticles";

    public static String WEB_VIEW_BASEULR = "http://101.201.37.180:7800";

    // 获取资讯详情
    public static String GET_NEWS_DETAILS = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsInfo.php";

    // 添加资讯评论
    public static String ADD_NEWS_COMMENT = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsAdd.php";

    // 获取资讯评价列表
    public static String GET_NEWS_COMMENT_LIST = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsComment.php";

    // 版本更新
    public static String CHECK_VERSION = "";

    // 查询医生是否有号
    public static String YN_REMAIN = SERVER_PAAS
            + "/api/v2/register/eztDoctor/ynRemain.do";

    // 根据医院ID患者ID获取医院一卡通号
    public static String GET_ONECARD = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/getEztPatientCartoon.do";

    // 创建支付订单
    public static String PAY_ORDER = SERVER_MC
            + "/api/v2/trading/traOrderPay/createTraPayPrepaidCurrency.do";

    // 获取要支付的金额
    public static String GET_PAY_MONEY = SERVER_MC
            + "/api/v2/trading/traPrepaid/getPrice.do";

    // 获取充值记录
    public static String GET_PAY_RECORD = SERVER_MC
            + "/api/v2/trading/traOrderPay/getUserPayRecord.do";

    // 获取健康币记录
    public static String GET_CURRENCY_RECORD = SERVER_MC
            + "/api/v2/trading/traPrepaid/getCurrencyRecord.do";

    // 获取健康币余额
    public static String GET_CURRENCY_MONEY = SERVER_MC
            + "/api/v2/memberCenter/eztUser/getUserAccount.do";

    // 获取可拨打分钟数
    public static String GET_CALLMINUTE = SERVER_PAAS
            + "/api/v2/callDoctor/patient/ctdCallRecord/getUserAccount.do";

    /**
     * 电话医生
     */
    // 查看电话医生状态
    public static String CHECK_TEL_DOC_STATE = SERVER_PAAS
            + "/api/v2/callDoctor/doctor/ctdCallInfo/ynOpen.do";

    // 根据日期获取医生可预约的时间段
    public static String GET_TEL_DOC_TIME = SERVER_PAAS
            + "/api/v2/callDoctor/register/remainListByDoctor.do";

    // 确定预约电话医生
    public static String CONFIRM_TEL_DOC_ORDER = SERVER_PAAS
            + "/api/v2/callDoctor/register/confirm.do";

    // 立即通话
    public static String PROMPT_TELING = SERVER_PAAS
            + "/api/v2/callDoctor/patient/ctdCallRecord/call.do";

    // 获取通话记录
    public static String GET_TEL_DOC_RECORD = SERVER_PAAS
            + "/api/v2/callDoctor/patient/ctdCallRecord/userRecord.do";

    // 获取电话医生列表
    public static String GET_TEL_DOC_LIST = SERVER_PAAS
            + "/api/v2/callDoctor/doctor/ctdCallInfo/doctorList.do";

    // 取消电话医生预约
    public static String CANCEL_PHONEORDER = SERVER_PAAS
            + "/api/v2/callDoctor/register/cancel.do";

    /**
     * ------------电话结束------------
     */

    // 意见反馈
    public static String SUGGESTION = SERVER_MC
            + "/api/v2/memberCenter/feedBack/getAdvertise.do";
    // 版本信息 ezthall
    // public static String SOFT_VERSION = SERVER_MC
    // + "/api/v2/base/eztHallVersionUpdate/getInfo.do";

    // 版本信息
    public static String SOFT_VERSION = SERVER_MC
            + "/api/v2/base/eztVersionUpdate/getInfo.do";

    /**
     * 症状自查
     */

    // 根据部位获取症状列表
    public static String GET_SYMPTOM_LIST = SERVER_PAAS
            + "/api/v2/register/guidance/getRegSymptomsListByIllPlace.do";

    // 根据症状id获取症状详情
    public static String GET_SYMPTOM_DETAILS = SERVER_PAAS
            + "/api/v2/register/guidance/findRegSymptomsListById.do";

    // 相关博文列表
    public static String GET_ARTICLE_OF_SYMPTOM = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsCeekList.php";

    // 根据科室获取相关症状列表
    public static String GET_SYMPTOM_LIST_DEPT = SERVER_PAAS
            + "/api/v2/register/guidance/findRegSymptomsByDeptName.do";

    // 根据症状id获取相关疾病列表
    public static String GET_DISEASE_LIST_ID = SERVER_PAAS
            + "/api/v2/register/guidance/findRegDiseaseBySymptomsId.do";

    // 获取疾病详情
    public static String GET_DISEASE_DETAILS = SERVER_PAAS
            + "/api/v2/register/guidance/findRegDiseasesById.do";
    // （疾病详情）获取推荐医院
    public static String GET_HOS_GENERAL_LIST = SERVER_PAAS
            + "/api/v2/register/eztHospital/generalList.do";

    // 根据hot获取热门疾病列表
    public static String GET_HOT_DISEASE_LIST = SERVER_PAAS
            + "/api/v2/register/guidance/findRegDiseaseByHot.do";

    // 根据疾病类型获取疾病列表
    public static String GET_ALL_REGDISEASES = SERVER_PAAS
            + "/api/v2/register/guidance/findRegDiseasesByCusceptiblePopulation.do";

    // 根据首字母获取症状列表
    public static String GET_SYMPTOM_LETTER = SERVER_PAAS
            + "/api/v2/register/guidance/findAllRegSymptomsByLetter.do";

    // 根据首字母获取疾病列表
    public static String GET_DISEASES_LETTER = SERVER_PAAS
            + "/api/v2/register/guidance/findRegDiseasesByType.do";

    /**
     * 电子病历
     */
    // 根据关键字获取相关医院
    public static String GET_HOSPITAL = SERVER_MC
            + "/api/v2/totalStationSearch/eztHospital/fuzzyList.do";

    // 创建病历第一步
    public static String CREATE_EMR_1 = SERVER_PAAS
            + "/api/v2/register/health/addMedicalRecordsfstep.do";

    // 创建病历第二步
    public static String CREATE_EMR_2 = SERVER_PAAS
            + "/api/v2/register/health/addMedicalRecordsSstep.do";

    // 创建病历第三步
    public static String CREATE_EMR_UPLOAD = SERVER_PAAS
            + "/api/v2/register/health/addMedicalRecordsPic.do";

    // 获取我病历列表
    public static String GET_MY_ILL_RECORDS = SERVER_PAAS
            + "/api/v2/register/health/list.do";

    // 获取病历详情
    public static String GET_MY_ILL_DETAILS = SERVER_PAAS
            + "/api/v2/register/health/detail.do";

    // 预约登记
    public static String ORDERREGISTER = SERVER_PAAS
            + "/api/v2/register/release/patient/add.do";

    // 获取预约登记列表
    public static String ORRecord = SERVER_PAAS
            + "/api/v2/register/release/patient/list.do";

    // 获取预约登记详情
    public static String GET_CHECKIN_DETAILS = SERVER_PAAS
            + "/api/v2/register/release/detail.do";

    // 获取抢单医生列表
    public static String GETREGEDDOCTOR = SERVER_PAAS
            + "/api/v2/register/release/detail.do";

    // 取消预约登记
    public static String CANCELREGEDRECORD = SERVER_PAAS
            + "/api/v2/register/release/patient/quit.do";

    // 确认预约挂号
    public static String AFFIRMORDER = SERVER_PAAS
            + "/api/v2/register/release/patient/confirm.do";

    // 病历删除
    public static String DEL_MY_ILL = SERVER_PAAS
            + "/api/v2/register/health/del.do";
    // 病历图片删除
    public static String DEL_MY_ILL_IMG = SERVER_PAAS
            + "/api/v2/register/health/delMedicalRecordsPic.do";
    /***
     * --------------------结束--------------------
     */

    // 请愿
    public static String ADD_PETITION = SERVER_MC
            + "/api/v2/memberCenter/eztPatient/addPetition.do";

    // 个推 提交cid 跟 userid
    public static String SET_CLIENTID = SERVER_MC
            + "/api/v2/login/mobile/setClientid.do";

    /**
     * 外患
     */
    // 肿瘤中心医院介绍
    public static String GET_TUMOUR_INTRO = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsInfo.php";

    // 快速求助
    public static String QUICK_HELP = SERVER_PAAS
            + "/api/v2/remoteService/remquickhelp/createRemQuickHelp.do";

    // 服务项目列表
    public static String GET_PROJECT_LIST = SERVER_MC
            + "/api/v2/memberCenter/eztDept/remlist.do";

    // 获取服务套餐列表
    public static String GET_TRAPACKAGE_LIST = SERVER_MC
            + "/api/v2/traPackage/list.do";

    // 套餐详情
    public static String GET_TRAPACKAGE_DETAIL = SERVER_MC
            + "/api/v2/traPackage/detail.do";

    // 获取康复病历列表
    public static String GET_RECOVERY_CASE_LIST = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsList.php";

    // 获取病历详情
    public static String GET_RECOVERY_CASE_DETAIL = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsInfo.php";

    // 获取患友交流信息
    public static String GET_PATIENT_GROUP = OFFICIAL_WEBSITE
            + "/eztcms/plus/news/APP/EztCmsNewsInfo.php";

    // 添加购物车
    public static String ADD_TRASHOPPINGCART = SERVER_MC
            + "/api/v2/trading/traShoppingCart/ceartTraShoppingCart.do";

    /**
     * 轻陪诊
     */

    /**
     * 验证用户身份
     */
    public static String AUTHEN = HTTPHelper.BASE_PATH_DRAGON
            + "/api/v2/register/ccb/authentication.do";
    /**
     * 获取银行卡信息
     */
    public static String GAIN_CCBINFO = SERVER_MC
            + "/api/v2/register/ccb/infor.do";

    // 获取套餐详情
    public static String GET_PACKAGE_DETAIL = SERVER_MC
            + "/api/v2/traPackage/itemsDetail.do";

    // 健康卡激活
    public static String ACTIVATION = SERVER_MC
            + "/api/v2/traHealthCar/activation.do";

    // 购买健康卡
    public static String CREATE_TRAORDERPAY_PACKAGE = SERVER_MC
            + "/api/v2/trading/traOrderPay/createTraOrderPayPackage.do";

    // 获取健康卡列表
    public static String GET_HEALTHCARD_LIST = SERVER_MC
            + "/api/v2/traHealthCar/list.do";

    // 获取健康卡详情
    public static String GET_HEALTHCARD_DETAIL = SERVER_MC
            + "/api/v2/traHealthCar/detail.do";

    // 获取服务项详情
    public static String GET_ITEM_DETAIL = SERVER_MC
            + "/api/v2/traPackage/itemDetail.do";

    public static String GET_ITEM_DETAIL_NEW = SERVER_MC
            + "/api/v2/register/ccb/itemInfor.do";

    /*** --------------------结束-------------------- */

    /**
     * 订单
     */

    // 获取订单列表

    public static String GET_ORDER_LIST = SERVER_MC
            + "/api/v2/trading/traOrder/list.do";

    // 删除订单
    public static String DEL_ORDER = SERVER_MC
            + "/api/v2/trading/traOrder/del.do";

    // 订单立即支付
    public static String ORDER_PAY = SERVER_MC
            + "/api/v2/trading/traOrderPay/createTraOrderPayPackageforOrderList.do";

    /***
     * --------------------结束--------------------
     */

    // 获取离我最近的医院
    public static String NEAR_HOS_LIST = SERVER_MC
            + "/api/v2/memberCenter/eztHospital/nearList.do";

    // 获取所有优惠券列表
    public static String GET_COUPON_LIST = SERVER_MC
            + "/api/v2/memberCenter/coupons/getCouponsCardByUserId.do";

    // 获取能使用的优惠券列表
    public static String GET_USE_COUPON_LIST = SERVER_MC
            + "/api/v2/memberCenter/coupons/getCouponsCardByDate.do";

    /******
     * 医护帮
     **********************************/
    // 获取服务套餐卡列表
    public static String GET_MEALCARD_LIST = SERVER_MC
            + "/api/v2/traPackage/list.do";

    // 获取服务套餐卡详情
    public static String GET_MEALCARD_DETAIL = SERVER_MC
            + "/api/v2/traPackage/itemsDetail.do";

    /**
     * 滴滴优惠券用到
     */
    // 获取最新的预约记录信息
    public static String GET_REGREGISTER_NEW = SERVER_PAAS
            + "/api/v2/register/regRegister/regRegisterNew.do";

    /**
     * 健康龙卡
     */

    // 获取龙卡信息
    public static String GET_HEALTHDRAGON_INFO = SERVER_MC
            + "/api/v2/memberCenter/affinity/getUserAffinityCard.do";

    // 龙卡激活绑定
    public static String CARD_BINDING = SERVER_MC
            + "/api/v2/memberCenter/affinity/binding.do";
    /**
     * 提交邀请码
     */
    public static String GUIDE_DOCNUM = SERVER_MC
            + "/api/v2/memberCenter/eztDoctor/createGuideDoctorExtensionInfo.do";
    /**
     * .NET webService 请求前缀
     */
    public static String Do_Net_URL_PREINDEX = "http://114.112.102.85:10004/WebServiceShiYanKe.asmx";
    /**
     * e聊列表界面 消息类型
     */
    public final static int TYPE_ZIXUN = 0, TYPE_XINXI = 1, TYPE_CHAT_MSG = 2,
            TYPE_Q = 3;

    public final static int TIMEOUT = 5000;
    public final static int REG_TIME_OUT = 30000;

    /**
     * 2015-12-30 获取短信验证码
     * <p/>
     * registerId operatorId(就是userId) post提交
     */
    public final static String GET_BACK_VAL_CODE = SERVER_PAAS
            + "/api/v2/captcha/backSms.do";

    /**
     * 2015-12-31 退号钱可以获取短信验证码的时间长度设置为10分钟
     */
    private final static int BACK_MSG_WAIT_TIME = 30;
    /**
     * 2015-12-31 退号钱可以获取短信验证码的时间长度设置为10分钟
     */
    public static long CAN_BACKNUM_TIME = BACK_MSG_WAIT_TIME * 1000;

    public final static String MSG_RECORDID = "msgRecordId";
    /**
     * 当日号医院列表
     */
    public final static String GAIN_REG_HOSlIST_DAY = SERVER_MC
            + "/api/v2/memberCenter/eztHospital/rplist.do";
    /**
     * 医院配置列表 hospitalId=2
     */
    public final static String GAIN_HOS_CONFIG = SERVER_MC
            + "/api/v2/memberCenter/eztHospital/homePage.do";
    // /////////////////////预约检查//////////////////////
    /**
     * 查询医院检查项列表
     */
    public final static String GAIN_ORDER_CHECK_LIST = SERVER_MC
            + "/api/v2/trading/checkItem/findByHosId.do";
    /**
     * 提交预约检查订单
     */
    public final static String COMMIT_ORDER_CHECK = SERVER_MC
            + "/api/v2/trading/checkItem/creatCheckOrder.do";

    /**
     * 支付详情界面弹出立即支付 orderId =11
     */
    public final static String COMMIT_ORDER_CHECK_BYID = SERVER_MC
            + "/api/v2/trading/checkItem/findExistOrderById.do";
    /**
     * 根据患者Id查询订单列表 patientId
     */
    public final static String GAIN_ORDER_CHECK_LIST_BY_PID = SERVER_MC
            + "/api/v2/trading/checkItem/getOrderListByUserId.do";
    /**
     * 预约检查订单详情
     */
    public final static String GAIN_ORDER_CHECK_DETIAL = SERVER_MC
            + "/api/v2/trading/checkItem/findByOrderId.do";
    /**
     * 约检查状态- 全部
     */
    public final static Integer CHECKITEM_STATUS_ALL = 0;
    /**
     * 约检查状态- 待支付
     */
    public final static int CHECKITEM_STATUS_UNPAY = 1;
    /**
     * 约检查状态- 待审核
     */
    public final static int CHECKITEM_STATUS_UNAUDIT = 2;
    /**
     * 约检查状态- 审核通过&待预约
     */
    public final static int CHECKITEM_STATUS_AUDITSUCCESS = 3;
    /**
     * 约检查状态- 预约成功
     */
    public final static int CHECKITEM_STATUS_ORDERSUCCESS = 4;
    /**
     * 约检查状态- 拒绝订单
     */
    public final static int CHECKITEM_STATUS_REFUSEAUDIT = 5;
    /**
     * 约检查状态- 办理退款
     */
    public final static int CHECKITEM_STATUS_PAYBACK = 6;
    /**
     * 约检查状态- 支付超时
     */
    public final static int CHECKITEM_STATUS_PAYOUTTIME = 7;
    /**
     * 1.1. 查询支持预约病床服务医院
     * <p/>
     * cityid 城市ID level 医院等级 hosType 医院类型（三级） countyid 地区ID
     */
    public static String GET_BED_HOS = SERVER_MC
            + "/api/v2/trading/reservationBed/findResBedHos.do";
    /**
     * 1.2. 提交预约病床订单 patientId 患者ID userId 用户ID patientName 预约者姓名 patientSex
     * 预约者性别 patientPhone 预约者电话 patientCardName 预约者证件类型 patientCardNum 证件号码
     * patientOrderType 订单类型 hospitalId 医院ID hospitalName 医院名称 deptId 科室ID
     * patientSpecialNeed 特殊需求 patientStatus 症状描述 picture 订单图片
     */
    public static String CREATE_BED_ORDER = SERVER_MC
            + "/api/v2/trading/reservationBed/createBedOrder.do";
    /**
     * 1.3. 根据用户ID查询病床订单列表 userId 用户ID
     */
    public static String FIND_ORDER_BY_USER_ID = SERVER_MC
            + "/api/v2/trading/reservationBed/findOrderByUserId.do";

    /**
     * 获取病床的订单的状态。
     */

    public final static String GAIN_ORDER_BED_STATUS = SERVER_MC
            + "/api/v2/trading/reservationBed/findOrderById.do";
    /**
     * 获取预约病床的订单的列表。
     */
    public final static String GAIN_ORDER_BED_LIST = SERVER_MC
            + "/api/v2/trading/reservationBed/findOrderByUserId.do";

    /**
     * 1.4. 预约病床订单状态 orderBedId 预约病床订单ID
     */
    public static String FIND_ORDER_BY_ID = SERVER_MC
            + "/api/v2/trading/reservationBed/findOrderById.do";

    /**
     * 1.5. 根据约病床订单id 获取支付宝支付串（分首次支付和二次支付） orderBedId 预约检查订单ID
     */
    public static String GET_ALIPAY_STRING_BY_ID = SERVER_MC
            + "/api/v2/trading/reservationBed/getAlipayStringById.do";
    /**
     * 龙卡获取验证码
     */
    public static String CCB_INfO_SEND_SMS = HTTPHelper.BASE_PATH_DRAGON
            + "api/v2/register/ccb/sendSmsforFb.do";
    /**
     * ?uid=3
     */
    public static String GET_CCB_INFO_BY_UID = SERVER_MC
            + "/api/v2/register/ccb/getCcbInfoByUid.do";
    /**
     * hospitalId
     */
    public static String FIND_RES_BED_DEPT = SERVER_MC
            + "/api/v2/trading/reservationBed/findResBedDept.do";

    /****
     * 消息推送接口
     * <p/>
     * 1.8. 向服务器汇报状态，服务器返回消息，并将消息插入数据库 infoType 消息类型 userId 用户ID doctorName 医生姓名
     * hospitalName 医院名称 putPoolDate 放号时间
     */
    public static String TRA_ORDER_INFO = SERVER_MC
            + "/api/v2/trading/traOrderService/traOrderInfo.do";
    /**
     * 1.9. 通过用户userId查询服务器消息列表
     * <p/>
     * userId 用户ID type 消息类型 page 当前第几页
     */
    public static String FIND_TRAORDER_INFO = SERVER_MC
            + "/api/v2/trading/traOrderService/findTraOrderInfo.do";
    /**
     * 1.10. 向服务器提交信息，服务器发送短信 sendSelf
     */
    public static String SEND_SELF = SERVER_MC
            + "/api/v2/memberCenter/eztNotice/sendSelf.do";
    /**
     * 1预约检查 2预约病床 3预约药瓶 4预约挂号、当日挂号、大牌名医 5龙卡服务
     */
    public static final int MSG_YYJC = 1, MSG_YYBC = 2, MSG_YYYP = 3,
            MSG_GHMY = 4, MSG_LKFW = 5;

    // 预约病床的状态码。

    /**
     * 预约病床订单状态- 全部
     */
    public final static int BED_STATUS_ALL = 0;
    /**
     * 预约病床订单状态- 待支付
     */
    public final static int BED_STATUS_UNPAY = 1;
    /**
     * 预约病床订单状态- 待审核
     */
    public final static int BED_STATUS_UNAUDIT = 2;
    /**
     * 预约病床订单状态- 审核通过&待预约&正在咨询
     */
    public final static int BED_STATUS_AUDITSUCCESS = 3;// 按照正在咨询的状态进行处理。
    /**
     * 预约病床订单状态- 预约成功
     */
    public final static int BED_STATUS_ORDERSUCCESS = 4;
    /**
     * 预约病床订单状态- 拒绝订单
     */
    public final static int BED_STATUS_REFUSEAUDIT = 5;
    /**
     * 预约病床订单状态- 办理退款
     */
    public final static int BED_STATUS_PAYBACK = 6;
    /**
     * 预约病床订单状态- 支付超时
     */
    public final static int BED_STATUS_PAYOUTTIME = 7;
    /**
     * 预约病床订单状态- 收取押金
     */
    public final static int BED_STATUS_DEPOSIT = 8;
    /**
     * 预约病床订单状态- 支付成功
     */
    public final static int BED_STATUS_SUCCESS = 9;
    /**
     * 预约病床订单状态- 暂无病床
     */
    public final static int BED_STATUS_NOTBED = 10;
    /**
     * 预约病床订单状态- 关闭订单
     */
    public final static int BED_STATUS_ORDERCLOSE = 11;
    /**
     * 短信 sendType
     * <p/>
     * sendType 信息类型 必填 0验证码1通知短信2广告短信3推送信息
     */
    public final static int SENDTYPE_YZM = 0, SENDTYPE_TZDX = 1,
            SENDTYPE_GGDX = 2, SENDTYPE_TSXX = 3;

}
