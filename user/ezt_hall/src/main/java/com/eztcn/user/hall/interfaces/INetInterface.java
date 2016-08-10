package com.eztcn.user.hall.interfaces;

import com.eztcn.user.hall.model.DataResponse;
import com.eztcn.user.hall.model.DragonStatusResponse;
import com.eztcn.user.hall.model.HotDoctorResponse;
import com.eztcn.user.hall.model.MessageResponse;
import com.eztcn.user.hall.model.MyHotDoctorRequest;
import com.eztcn.user.hall.model.MyHotDoctorResponse;
import com.eztcn.user.hall.model.OneThousandRequest;
import com.eztcn.user.hall.model.OneThousandResponse;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AllCityListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.AreaListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.AttentionDoctorResponse;
import com.eztcn.user.hall.model.ResultResponse.CityListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.CommitAppointmentSucessResponse;
import com.eztcn.user.hall.model.ResultResponse.DepartmentListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorDatasOneLevelResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorListResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorSelectRuleResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalDataResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;
import com.eztcn.user.hall.model.ResultResponse.MyAttentionResponse;
import com.eztcn.user.hall.model.ResultResponse.MyPatientResponse;
import com.eztcn.user.hall.model.ResultResponse.RankDataResponse;
import com.eztcn.user.hall.model.ResultResponse.SelectRulesResponse;
import com.eztcn.user.hall.model.ResultResponse.WeekDatasResponse;
import com.eztcn.user.hall.model.WeatherRequest;
import com.eztcn.user.hall.model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Author: lizhipeng
 * @Data: 16/4/12 下午2:57
 * @Description: 网络请求接口
 */
public interface INetInterface {
    /**
     * get 请求
     *
     * @param city
     * @return
     */
    @GET("api/v2/register/back/regEnterBackNoCode.do")
    Observable<Response> getWeather(@QueryMap Map<String, String> city);

    /**
     * 每次请求都要重写此post请求或get请求方法
     * post 请求
     *
     * @param body javabean请求体
     *             post后面是接口地址
     *             postWeather 方法名称，如登录方法 可以写成login
     * @return
     */
    @POST("")
    Observable<WeatherResponse> postWeather(@Body WeatherRequest body);

    //--------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------//
    //----------由于老版本post请求用的是form表单提交，所以post请求统一改为以下的方式------—---------//
    //--------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------//

    /**
     * post请求表单提交
     *
     * @param params map请求体
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/volidateMobile.do")
    Observable<Response> postTest(@FieldMap Map<String, String> params);

    /**
     * 登录接口
     */
    @FormUrlEncoded
    @POST("api/v2/eztPatient/login.do")
    Observable<Response<DataResponse>> postLogin(@FieldMap Map<String, String> params);

    /**
     * 获取修改注册手机短信验证码
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/sendUpdateMobileSms.do")
    Observable<Response> postGetModifyPhoneCode(@FieldMap Map<String, String> params);

    /**
     * 修改注册手机号
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/updateMobile.do")
    Observable<Response> postUserModifyPhone(@FieldMap Map<String, String> params);

    /**
     * 找回密码验证码接口
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/sendSmsforFb.do")
    Observable<Response> postGetFPwVerCode(@FieldMap Map<String, String> params);

    /**
     * 验证找回密码验证码
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/mobileActivationforFb.do")
    Observable<Response> postVerifyFPwVerCode(@FieldMap Map<String, String> params);

    /**
     * 找回密码
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/modifyPasswordFb.do")
    Observable<Response> postFindPassWord(@FieldMap Map<String, String> params);

    /**
     * 完善资料(实名认证)
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/regPatientsStep.do")
    Observable<Response> postUserAutonymName(@FieldMap Map<String, String> params);


    /**
     * 一千零医夜 获取列表数据
     */
    @POST("ezt_p1/rest/ezt_article/GetEZTUserArticles")
    Observable<OneThousandResponse> postOneThousandAll(@Body OneThousandRequest request);

    /**
     * 保存个人信息
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztPatient/edit.do")
    Observable<Response> postSavePersonInfo(@FieldMap Map<String, String> params);


    /**
     * 获取默认热门医生列表数据
     */
    @POST("api/v2/memberCenter/collect/getTenFamousDoctor.do")
    Observable<HotDoctorResponse> postDefaultDoctorList();

    /**
     * 获取我关注的热门医生列表数据
     */
    @POST("api/v2/memberCenter/collect/getUserSelectCollectsDoctorList.do")
    Observable<MyHotDoctorResponse> postMyDoctorList(@Body MyHotDoctorRequest request);

    @POST("api/v2/memberCenter/collect/getUserSelectCollectsDoctorList.do")
    Observable<String> postMyDoctorList2(@Body MyHotDoctorRequest request);

    /**
     * 获取我的关注的医生的数量。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/collect/getCollectCount.do")
    Observable<Response> postMyDoctorNum(@FieldMap Map<String, String> params);

    /**
     * 获取我的关注医生的列表
     */
    @POST("api/v3/memberCenter/collect/getUserSelectCollectsDoctorList.do")
    Observable<Response<MyAttentionResponse>> postMyAttentionDoctorList(@Body MyHotDoctorRequest params);

    /**
     * 关注医生接口
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/collect/AddCollectDoctorInfo.do")
    Observable<Response<AttentionDoctorResponse>> attentionDoctor(@FieldMap Map<String, String> params);

    /**
     * 取消医生关注接口
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/collect/delCollectsDoctorInfo.do")
    Observable<Response> cancelAttentionDoctor(@FieldMap Map<String, String> params);

    /**
     * 发送手机验证码接口
     */
    @FormUrlEncoded
    @POST("api/v3/captcha/registerSmsMobile.do")
    Observable<Response> sendCode(@FieldMap Map<String, String> params);

    /**
     * 验证码验证接口
     */
    @FormUrlEncoded
    @POST("api/v3/register/regRegister/eztRegister.do")
    Observable<Response<CommitAppointmentSucessResponse>> validationPhoneCode(@FieldMap Map<String, String> params);

    /**
     * 获取就诊人列表。
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztPatient/familyList.do")
    Observable<Response<List<MyPatientResponse>>> getPatientList(@FieldMap Map<String, String> params);

    /**
     * 通用 获取验证码接口。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztUser/sendCurrencySms.do")
    Observable<Response> getVerificationCode(@FieldMap Map<String, String> params);

    /**
     * 通用 验证 验证码接口。
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztNotice/vcode.do")
    Observable<Response> VerifySmsCode(@FieldMap Map<String, String> params);

    /**
     * 获取医院筛选条件接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztHospital/dict.do")
    Observable<Response<SelectRulesResponse>> getRulesData(@FieldMap Map<String, String> params);

    /**
     * 获取合作的城市列表接口
     */
    @FormUrlEncoded
    @POST("api/v3/register/region/getProvinceList.do")
    Observable<Response<ArrayList<CityListDataResponse>>> getCityListData(@FieldMap Map<String, String> params);

    /**
     * 获取城市下面的地区列表接口
     */
    @FormUrlEncoded
    @POST("api/v2/base/eztZCounty/list.do")
    Observable<Response<ArrayList<AreaListDataResponse>>> getAreaListData(@FieldMap Map<String, String> params);

    /**
     * 获取医院筛选条件接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztHospital/dict.do")
    Observable<Response> savePatientInfo(@FieldMap Map<String, String> params);

    /**
     * 获取医院搜索结果列表接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztHospital/list.do")
    Observable<Response<ArrayList<HospitalListResponse>>> getHospitalSearchData(@FieldMap Map<String, String> params);

    /**
     * 获取医院数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztHospital/detail.do")
    Observable<Response<HospitalDataResponse>> getHospitalData(@FieldMap Map<String, String> params);

    /**
     * 获取医生筛选规则接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztDoctor/getDoctorFilterCondition.do")
    Observable<Response<DoctorSelectRuleResponse>> getDoctorRulesData(@FieldMap Map<String, String> params);

    /**
     * 获取医生列表数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/register/eztDoctor/getDoctorList.do")
    Observable<Response<ArrayList<DoctorListResponse>>> getDoctorListData(@FieldMap Map<String, String> params);

    /**
     * 增加就诊人。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztPatient/familyAdd.do")
    Observable<Response> addPatientInfo(@FieldMap Map<String, String> params);

    /**
     * 修改就诊人。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztPatient/familyEdit.do")
    Observable<Response> modifyPatientInfo(@FieldMap Map<String, String> params);

    /**
     * 获取龙卡信息
     *
     * @param path
     * @return
     */
    @GET("Dragoncard/index/index/{path}")
    Observable<Response> getDragon(@Path("path") String path);

    /**
     * 获取一级科室数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztDeptCate/eztDeptEntrance.do")
    Observable<Response<ArrayList<DepartmentListDataResponse>>> getOneLevelDepartmentData(@FieldMap Map<String, String> params);

    /**
     * 获取二级科室数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztDeptCate/sonlist.do")
    Observable<Response<ArrayList<DepartmentListDataResponse>>> getTwoLevelDepartmentData(@FieldMap Map<String, String> params);

    /**
     * 获取三级科室数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztDeptCate/eztDeptList.do")
    Observable<Response<ArrayList<DepartmentListDataResponse>>> getThreeLevelDepartmentData(@FieldMap Map<String, String> params);

    /**
     * 验证个人信息是否完善接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztUser/judgeAccount.do")
    Observable<Response> validationPersonalInfo(@FieldMap Map<String, String> params);

    /**
     * 完善个人信息。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztUser/regPatientsStep.do")
    Observable<Response> completeInfo(@FieldMap Map<String, String> params);

    /**
     * 获取医生数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/register/eztDoctor/getDoctorOtherInfo.do")
    Observable<Response<DoctorDatasOneLevelResponse>> getDoctorDatas(@FieldMap Map<String, String> params);

    /**
     * 获取排行榜数据接口
     */
    @FormUrlEncoded
    @POST("api/v3/register/eztDoctor/getRankInfo.do")
    Observable<Response<RankDataResponse>> getRankDatas(@FieldMap Map<String, String> params);

    /**
     * 获取导医图片接口
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztDoctor/getMedicalGuidePic.do")
    Observable<Response<ArrayList<String>>> getGuidePic(@FieldMap Map<String, String> params);

    /**
     * 更改手机号前的验证。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztUser/modifyMobileAuth.do")
    Observable<Response> modifyMobileVerify(@FieldMap Map<String, String> params);

    /**
     * 设置新的手机号。
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztUser/changeMobile.do")
    Observable<Response> setNewMobile(@FieldMap Map<String, String> params);

    /**
     * 获取更改手机号验证码。
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/sendUpdateMobileSms.do")
    Observable<Response> getChangeMobileVerifyCode(@FieldMap Map<String, String> params);

    /**
     * 获取忘记密码的验证码。
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/sendSmsforFb.do")
    Observable<Response> getForgetPasswordVerifyCode(@FieldMap Map<String, String> params);

    /**
     * 获取忘记密码的验证码。
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/mobileActivationforFb.do")
    Observable<Response> VerifyForgetPasswordVerifyCode(@FieldMap Map<String, String> params);

    /**
     * 确认修改密码
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/modifyPasswordFb.do")
    Observable<Response> confirmModifyPassword(@FieldMap Map<String, String> params);

    /**
     * * 获取龙卡状态信息
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/register/ccb/getCcbInfoByUid.do")
    Observable<Response<DragonStatusResponse>> postDragonStatus2(@FieldMap Map<String, String> params);

    /**
     * 获取星期表数据
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("api/v3/register/pool/new/getDoctorPool.do")
    Observable<Response<ArrayList<WeekDatasResponse>>> getWeeksDatas(@FieldMap Map<String, String> params);

    /**
     * 上传头像
     * @param params
     * @param body
     * @return
     */
    @Multipart
    @POST("api/v3/memberCenter/eztPatient/picUpload.do")
    Observable<Response<String>> upLoadAvatar(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part body);
//    Observable<Response> upLoadAvatar(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part body);

    /**
     * 检查更新。
     * @param params
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/base/eztVersionUpdate/getInfo.do")
    Observable<Response> checkUpdate(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("api/v2/trading/traOrderService/findTraOrderInfo.do")
    Observable<Response<MessageResponse>> postMessage(@FieldMap Map<String, String> params);

    /**
     * 龙卡绑定成功
     *
     * @param userId
     * @return
     */
    @GET("api/v2/register/back/dragonInfo.do")
    Observable<Response> getDragonSuccess(@Query("userId") String userId,@Query("infoSysType") String sysType,@Query("infoType") String infoType,@Query("orderNumber") String orderNum);

    /**
     * 获取我的关注医生的列表
     */
    @FormUrlEncoded
    @POST("api/v3/memberCenter/collect/getUserSelectCollectsDoctorList.do")
    Observable<Response<MyAttentionResponse>> postMyAttentionList(@FieldMap Map<String, String> params);

    /**
     * 删除就诊人。
     * @param params
     * @return
     */

    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztPatient/familyDel.do")
    Observable<Response> deletePatient(@FieldMap Map<String, String> params);
    /**
     * 注册。
     * @param params
     * @return
     */

    @FormUrlEncoded
    @POST("api/v3/memberCenter/eztUser/regPatientfStep.do")
    Observable<Response<DataResponse>> Register(@FieldMap Map<String, String> params);

    /**
     * 验证是否注册。
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/memberCenter/eztUser/findUserByMobile.do")
    Observable<Response> isRegister(@FieldMap Map<String, String> params);

    /**
     * 绑定龙卡验证码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/register/ccb/sendSmsforFb.do")
    Observable<Response<String>> postDragonAuth(@FieldMap Map<String, String> params);
    /**
     * 删除消息
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("api/v2/trading/traOrderService/delTraOrderInfo.do")
    Observable<Response> postDeleteMessage(@FieldMap Map<String, String> params);
    /**
     * 获取全部的城市列表数据
     */
    @FormUrlEncoded
    @POST("api/v3/register/region/eztZCity/list.do")
    Observable<Response<AllCityListDataResponse>> getAllCityListData(@FieldMap Map<String, String> params);

}
