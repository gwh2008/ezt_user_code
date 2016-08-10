package com.eztcn.user.hall.http;
import android.content.Context;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.interfaces.INetInterface;
import com.eztcn.user.hall.model.DataResponse;
import com.eztcn.user.hall.model.DragonStatusResponse;
import com.eztcn.user.hall.model.HotDoctorResponse;
import com.eztcn.user.hall.model.IModel;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * @Author: lizhipeng
 * @Data: 16/4/12 下午3:27
 * @Description: 网络请求数据类（单例）
 */
public class HTTPHelper {

    /**
     * 这一部分配置常量，可以抽取出常量类
     */
    public static final String BASE_PATH_HOT_DOCTOR_IMG = "http://images.eztcn.com.cn/images/doctor/ezt2.0/";//关注的医生头像地址

    /**
     * 会员中心
     */
    public static final String BASE_PATH_MC = "http://mc.eztcn.com.cn/mc/";//原来的线上版本的接口地址
//    public static final String BASE_PATH_MC = "http://172.16.60.207:8081/mc/";//现在的线下版本的测试接口地址，有数据验证
//    private static final String BASE_PATH_MC = "http://test.mc.eztcn.com/mc/";//打包地址
//      private static final String BASE_PATH_MC = "http://mctest.eztcn.com.cn/mc/";//预上线地址。
//    private static final String BASE_PATH_MC = "http://112.74.72.126/mc/";//阿里云地址
//    private static final String BASE_PATH_MC = "http://172.16.60.235:8090/mc/";//建新的测试地址


    /**
     * 业务平台
     */
    public static final String BASE_PATH_PASS = "http://paas.eztcn.com.cn/paas/";//原来的线上版本的接口地址
//    public static final String BASE_PATH_PASS = "http://test.paas.eztcn.com/paas/";//打包地址
//    public static final String BASE_PATH_PASS = "http://paastest.eztcn.com.cn/paas/";//预上线地址
//    public static final String BASE_PATH_PASS = "http://112.74.72.126/paas/";//阿里云测试地址。



    //private static final String BASE_PATH_HOT_DOCTOR = "http://mc.eztcn.com.cn/mc/";//访问的地址
    private static final String BASE_PATH_HOT_DOCTOR = "http://112.74.72.126/mc/";//访问的地址
    //public static String BASE_PATH_IMG = "http://images.eztcn.com.cn/";//图片地址


    public static String BASE_PATH_IMG = "http://101.201.37.180:7800/";//图片地址
    public static String BASE_PATH_ARTICLE = "http://101.201.37.180:7800/";//图片地址
    //    private static String BASE_PATH_PASS = "http://paas.eztcn.com.cn/paas/";//业务平台
    private static String BASE_PATH_DATA_REPOSITORY = "http://101.201.37.180:7800/";//业务平台
    public static String BASE_PATH_DRAGON = "http://mc.eztcn.com.cn/mc/";//龙卡
//    private static String BASE_PATH_DRAGON = "http://172.16.60.207/";//龙卡
    private static String BASE_PATH_DRAGON_SUCCESS = "http://mc.eztcn.com.cn/mc/";//龙卡绑定成功

    public static String BASE_PATH_IMG_AVATAR = "http://images.eztcn.com.cn/images/patient/eztcn2.0/";//头像地址

    /**
     * 服务器图片地址
     */
    public static String IMG_SERVER = "http://images.eztcn.com.cn";

    // 用户头像地址
    public static String USER_PHOTO = IMG_SERVER + "/images/patient/ezt/text";
    /**
     * 医院图片
     */
    public static String HOSPITAL_PIC = IMG_SERVER + "/images/hospital/eztcn2.0/";

    private static final long DEFAULT_TIMEOUT = 30000;//默认超时时间(毫秒)

    private static Retrofit mRetrofit;
    private static INetInterface mNetService;

    public enum URL_TYPE {
        MC, PASS, TEST, IMG, COUPON_URL, DATA_REPOSITORY, HOT_DOCTOR, DRAGON, DRAGON_SUCCESS
    }

    private static HTTPHelper mHttpHelper;

    private HTTPHelper() {

    }

    /**
     * 单例控制器
     */
    private static void initData(URL_TYPE type) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addNetworkInterceptor(new HTTPInterceptor());
        okHttpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);//链接超时时间
        okHttpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);//等待服务器响应的时间
        okHttpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient.build())
//                .addConverterFactory(MyGsonConverter.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        switch (type) {
            case MC:
                builder.baseUrl(BASE_PATH_MC);
                break;
            case PASS:
                builder.baseUrl(BASE_PATH_PASS);
                break;
            case TEST:
                break;
            case IMG:
                builder.baseUrl(BASE_PATH_IMG);
                break;
            case COUPON_URL:
                break;
            case DATA_REPOSITORY:
                builder.baseUrl(BASE_PATH_DATA_REPOSITORY);
                break;
            case HOT_DOCTOR:
                builder.baseUrl(BASE_PATH_HOT_DOCTOR);
                break;
            case DRAGON:
                builder.baseUrl(BASE_PATH_DRAGON);
                break;
            case DRAGON_SUCCESS:
                builder.baseUrl(BASE_PATH_DRAGON_SUCCESS);
                break;
            default:
                break;
        }

        mRetrofit = builder.build();

        mNetService = mRetrofit.create(INetInterface.class);
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static HTTPHelper getInstance(URL_TYPE type) {
        initData(type);
        if (mHttpHelper == null) {
            mHttpHelper = new HTTPHelper();
        }
        return mHttpHelper;
    }

    private Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 类型转换，用来统一处理返回值，通常为公共message返回字段等。具体业务这里要具体操作
     *
     * @param <T> Subscriber真正需要的数据类型，也就是返回值针对的model
     */
    private class HttpResultFunc<T> implements Func1<IModel, T> {
        @Override
        public T call(IModel iModel) {
            if (iModel == null) {
                try {
                    throw new Exception("result model is null");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            T t = (T) iModel;
            if (t == null) {
                try {
                    throw new Exception("cast to the model is null");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return t;
        }
    }

    /**
     * 初始化观察者
     *
     * @param observable
     * @param resultType
     * @param listener
     */
    private Subscriber initObservable(Observable observable, int resultType, ResultSubscriber.OnResultListener listener) {
        ResultSubscriber subscriber = new ResultSubscriber(mContext);
        subscriber.setOnResultListener(listener);
        subscriber.setRequestType(resultType);
        observable
//                .map(new HttpResultFunc<WeatherResponse>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscriber;
    }
    //********************************对应 INetService接口中定义的请求方法*************************************************//

    /**
     * get获取网络数据的方法
     *
     * @param
     */
    public Subscriber getWeather(Map<String, String> map, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.getWeather(map);
        return initObservable(observable, resultType, listener);
    }

    /**
     * post获取网络数据的方法(每次请求网络数据都要重写此post或者get方法)
     *
     * @param body       请求体的class,
     * @param resultType 请求类型
     * @param listener   接口监听(每次都是这个listener)
     * @param body
     */
    public Subscriber postWeather(WeatherRequest body, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<WeatherResponse> observable = mNetService.postWeather(body);
        return initObservable(observable, resultType, listener);
    }

    /**
     * post form 表单 获取网络数据的方法
     *
     * @param options
     */
    public void postFormTest(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.postTest(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 登录
     */
    public Subscriber postLogin(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<DataResponse>> observable = mNetService.postLogin(options);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 找回密码验证码
     */
    public void postGetFPwVerCode(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.postGetFPwVerCode(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 一千零医夜 获取列表数据
     *
     * @param options
     * @param resultType
     * @param listener
     */
    public void postOneThousandAll(OneThousandRequest options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<OneThousandResponse> observable = mNetService.postOneThousandAll(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 保存个人信息
     */

    public void postSavePersonInfo(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.postSavePersonInfo(options);
        initObservable(observable, resultType, listener);
    }


    /**
     * 获取默认热门医生列表数据
     *
     * @param resultType
     * @param listener
     */
    public void postDefaultDoctorList(int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<HotDoctorResponse> observable = mNetService.postDefaultDoctorList();
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取我关注的热门医生列表数据
     *
     * @param request
     * @param resultType
     * @param listener
     */
    public void postMyDoctorList(MyHotDoctorRequest request, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<MyHotDoctorResponse> observable = mNetService.postMyDoctorList(request);
        initObservable(observable, resultType, listener);
    }

    public void postMyDoctorList2(MyHotDoctorRequest request, Subscriber subscriber) {
        Observable<String> observable = mNetService.postMyDoctorList2(request);
        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我的关注的医生的数量。
     */

    public void postMyDoctorNum(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.postMyDoctorNum(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 关注医生接口
     */
    public void attentionDoctor(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<AttentionDoctorResponse>> observable = mNetService.attentionDoctor(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 取消医生关注接口
     */
    public void cancelAttentionDoctor(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.cancelAttentionDoctor(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 我的关注医生列表。
     */

    public Subscriber postMyAttentionDoctorList(MyHotDoctorRequest request, int resultType, ResultSubscriber.OnResultListener listener) {

        Observable<Response<MyAttentionResponse>> observable = mNetService.postMyAttentionDoctorList(request);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 发送验证码接口
     */
    public void sendCode(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.sendCode(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 验证码验证接口
     */
    public void validationPhoneCode(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<CommitAppointmentSucessResponse>> observable = mNetService.validationPhoneCode(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 就诊人管理的列表界面。
     */

    public Subscriber getPatientList(Map<String, String> params, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<List<MyPatientResponse>>> observable = mNetService.getPatientList(params);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 通用的获取验证码
     *
     * @param params
     * @param resultType
     * @param listener
     * @return
     */

    public Subscriber getVerificationCode(Map<String, String> params, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.getVerificationCode(params);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 验证验证码
     *
     * @param params
     * @param resultType
     * @param listener
     * @return
     */
    public Subscriber VerifySmsCode(Map<String, String> params, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.VerifySmsCode(params);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 获取医院筛选条件接口
     */
    public void getRulesData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<SelectRulesResponse>> observable = mNetService.getRulesData(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取合作的城市列表接口
     */
    public void getCityListData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<CityListDataResponse>>> observable = mNetService.getCityListData(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取城市下面的地区列表接口
     */
    public void getAreaListData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<AreaListDataResponse>>> observable = mNetService.getAreaListData(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 保存就诊人信息。
     *
     * @param options
     * @param resultType
     * @param listener
     */
    public void savePatientInfo(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable observable = mNetService.getRulesData(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取医院搜索结果列表接口
     */
    public void getHospitalSearchData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<HospitalListResponse>>> observable = mNetService.getHospitalSearchData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取龙卡信息
     */
    public Subscriber getDragon(String path, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.getDragon(path);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 获取医院数据接口
     */
    public void getHospitalData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<HospitalDataResponse>> observable = mNetService.getHospitalData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 增加就诊人
     */
    public void addPatientInfo(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.addPatientInfo(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 修改就诊人
     */
    public void modifyPatientInfo(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.modifyPatientInfo(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取医生筛选条件接口
     */
    public void getDoctorRulesData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<DoctorSelectRuleResponse>> observable = mNetService.getDoctorRulesData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取医生列表接口
     */
    public void getDoctorListData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<DoctorListResponse>>> observable = mNetService.getDoctorListData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取一级科室数据接口
     */
    public void getOneLevelDepartmentData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<DepartmentListDataResponse>>> observable = mNetService.getOneLevelDepartmentData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取二级科室数据接口
     */
    public void getTwoLevelDepartmentData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<DepartmentListDataResponse>>> observable = mNetService.getTwoLevelDepartmentData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取三级科室数据接口
     */
    public void getThreeLevelDepartmentData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<DepartmentListDataResponse>>> observable = mNetService.getThreeLevelDepartmentData(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取三级科室数据接口
     */
    public void validationPersonalInfo(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.validationPersonalInfo(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 完善个人信息接口
     */
    public void completeInfo(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.completeInfo(options);
        initObservable(observable, resultType, listener);

    }

    /**
     * 获取医生数据接口
     *
     * @param options
     * @param resultType
     * @param listener
     */
    public void getDoctorDatas(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<DoctorDatasOneLevelResponse>> observable = mNetService.getDoctorDatas(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取排行榜数据接口
     */
    public void getRankDatas(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<RankDataResponse>> observable = mNetService.getRankDatas(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取导医图片接口
     */
    public void getGuidePic(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<String>>> observable = mNetService.getGuidePic(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取龙卡状态信息
     *
     * @param params
     * @param requestType
     * @param listener
     * @return
     */
    public Subscriber postDragonStatus2(Map<String, String> params, int requestType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<DragonStatusResponse>> observable = mNetService.postDragonStatus2(params);
        return initObservable(observable, requestType, listener);
    }

    /**
     * 更改手机号。
     */
    public void modifyMobileVerify(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.modifyMobileVerify(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 设置新的手机号。
     */
    public void setNewMobile(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.setNewMobile(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取更改手机号验证码
     */
    public void getChangeMobileVerifyCode(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.getChangeMobileVerifyCode(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取忘记密码的验证码。
     */
    public void getForgetPasswordVerifyCode(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.getForgetPasswordVerifyCode(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 验证忘记密码的验证码。
     */
    public void VerifyForgetPasswordVerifyCode(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.VerifyForgetPasswordVerifyCode(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 确认修改密码。
     */
    public void confirmModifyPassword(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.confirmModifyPassword(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取星期表数据
     */
    public void getWeeksDatas(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<ArrayList<WeekDatasResponse>>> observable = mNetService.getWeeksDatas(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 上传头像
     *
     * @param file
     * @param requestType
     * @param listener
     * @return
     */
    public Subscriber upLoadAvatar(File file, int requestType, ResultSubscriber.OnResultListener listener) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), BaseApplication.patient.getId() + "");
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("myfile", file.getName(), fileBody);
        params.put("patientId", textBody);
        Observable<Response<String>> observable = mNetService.upLoadAvatar(params, body);
//        Observable<Response> observable = mNetService.upLoadAvatar(params, body);
        return initObservable(observable, requestType, listener);
    }

    /**
     * 检查更新。
     */
    public void checkUpdate(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.checkUpdate(options);
        initObservable(observable, resultType, listener);
    }

    /**
     * 获取消息界面
     *
     * @param params
     * @param requestType
     * @param listener
     * @return
     */
    public Subscriber postMessage(Map<String, String> params, int requestType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<MessageResponse>> observable = mNetService.postMessage(params);
        return initObservable(observable, requestType, listener);
    }

    /**
     * 龙卡绑定成功
     *
     * @param userId
     * @param requestType
     * @param listener
     * @return
     */
    public Subscriber getDragonSuccess(String userId, int requestType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.getDragonSuccess(userId,"5","17","0987654321");
        return initObservable(observable, requestType, listener);
    }

    /**
     * 我的关注列表。
     *
     * @param options
     * @param resultType
     * @param listener
     * @return
     */
    public Subscriber postMyAttentionList(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<MyAttentionResponse>> observable = mNetService.postMyAttentionList(options);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 删除就诊人。
     */
    public Subscriber deletePatient(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.deletePatient(options);
        return initObservable(observable, resultType, listener);
    }
      /**
     * 注册
     */
    public Subscriber Register(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<DataResponse>> observable = mNetService.Register(options);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 验证手机号码是否注册
     */
    public Subscriber isRegister(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.isRegister(options);
        return initObservable(observable, resultType, listener);
    }

    /**
     * 绑定龙卡验证码
     * @param options
     * @param resultType
     * @param listener
     * @return
     */
    public Subscriber postDragonAuth(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<String>> observable = mNetService.postDragonAuth(options);
        return initObservable(observable, resultType, listener);
    }
    /**
     * 删除消息
     * @param options
     * @param resultType
     * @param listener
     * @return
     */
    public Subscriber postDeleteMessage(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response> observable = mNetService.postDeleteMessage(options);
        return initObservable(observable, resultType, listener);
    }
    /**
     * 获取全部的城市列表数据
     */
    public Subscriber getAllCityListData(Map<String, String> options, int resultType, ResultSubscriber.OnResultListener listener) {
        Observable<Response<AllCityListDataResponse>> observable = mNetService.getAllCityListData(options);
        return initObservable(observable, resultType, listener);
    }


}
