package com.eztcn.user.hall.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.BigDoctorList30Activity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.home.dragoncard.ActivityDragonCard;
import com.eztcn.user.eztcn.activity.home.orderbed.OrderBedNoticeActivity;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.hall.activity.AppointmentMainActivity;
import com.eztcn.user.hall.activity.EnterActivity;
import com.eztcn.user.hall.activity.FeatureServiceActivity;
import com.eztcn.user.hall.activity.MedicalCardDetailsActivity;
import com.eztcn.user.hall.activity.OneThousandDoctorActivity;
import com.eztcn.user.hall.activity.SearchActivity;
import com.eztcn.user.hall.activity.SelectAllCityActivity;
import com.eztcn.user.hall.activity.SelectAppointmentTimeActivity;
import com.eztcn.user.hall.activity.WebViewActivity;
import com.eztcn.user.hall.activity.dragonCard.MyDragonCardActivity;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.adapter.BaseCommonAdapter;
import com.eztcn.user.hall.adapter.BaseCommonRvAdapter;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.common.DragonStatusSingle;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.CommonDialogOkListener;
import com.eztcn.user.hall.interfaces.LocationListener;
import com.eztcn.user.hall.model.HotDoctorResponse;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.OneThousandRequest;
import com.eztcn.user.hall.model.OneThousandResponse;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.CityListDataResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorListResponse;
import com.eztcn.user.hall.utils.CommonDialog;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.LocationUtils;
import com.eztcn.user.hall.utils.SaveGetDataUtil;
import com.eztcn.user.hall.views.EztBannerView;
import com.eztcn.user.phone.bean.ADInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zll on 2016/5/29.
 * 门诊大厅的Fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, ResultSubscriber.OnResultListener {

    private static final int REQUEST_ONE_THOUSAND = 1001;//一千零一夜
    private static final int REQUEST_HOT_DOCTOR_DEF = 1002;//热门医生
    private static final int REQUEST_HOT_DOCTOR = 1003;//热门医生
    private static final int REQUEST_CITY_LIST = 1004;//热门医生

    private Activity activity;
    private View rootView;

    private RecyclerView mHotDoctorRv;
    private TextView mMoreOneThousandTv;
    private TextView search_city;
    private ListView mOneThousandLv;
    private EditText new_activity_search_edit;
    private SwipeRefreshLayout swipe_refresh_layout;

    private EztBannerView mEBVBanner;

    private ArrayList<CityListDataResponse> cityListMap;//有合作的城市列表数据


    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();


    //轮播图的id
    private int[] imageUrls = {
            R.drawable.new_home_circlr_medical_card,
            R.drawable.new_home_circlr_appointment_check,
            R.drawable.new_home_circle_appointment_bed,
            R.drawable.new_home_circle_stay_in,
            R.drawable.new_home_circle_thounds_night
             };

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this.getActivity();
    }

    @Override
    protected void onFragmentFirstResume() {
        Log.i("HomeFragment", "onFragmentFirstResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 避免UI重新加载
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.new_fragment_home, null);// 缓存Fragment
            initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /*
    初始化view控件
     */
    private void initView() {
        mMoreOneThousandTv = (TextView) rootView.findViewById(R.id.new_item_content_home_tv_more);
        mEBVBanner = (EztBannerView) rootView.findViewById(R.id.home_fragment_banner);
        search_city = (TextView) rootView.findViewById(R.id.search_city);
        search_city.setOnClickListener(this);
        mMoreOneThousandTv.setOnClickListener(this);
        rootView.findViewById(R.id.new_item_module_home_appointment).setOnClickListener(this);//进入预约挂号页面
        rootView.findViewById(R.id.new_item_module_home_special_service).setOnClickListener(this);//进入特色服务页面
        rootView.findViewById(R.id.new_item_module_home_famous_doctor).setOnClickListener(this);//进入大牌名医页面
        rootView.findViewById(R.id.new_item_module_home_special_service).setOnClickListener(this);//进入预约挂号页面
        rootView.findViewById(R.id.home_fragment_dragon_pic).setOnClickListener(this);//进入龙卡详情页面
        mHotDoctorRv = (RecyclerView) rootView.findViewById(R.id.new_home_fragment_hot_doctor_rv);
        LinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(mHotDoctorRv.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHotDoctorRv.setLayoutManager(linearLayoutManager);
        mOneThousandLv = (ListView) rootView.findViewById(R.id.new_item_home_fragment_one_thousand_lv);
        new_activity_search_edit = (EditText) rootView.findViewById(R.id.new_activity_search_edit);
        new_activity_search_edit.setOnClickListener(new_activity_search_editOnClickListener);
        swipe_refresh_layout= (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(swipe_refresh_layoutListener);
        swipe_refresh_layout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipe_refresh_layout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white));
        initCycleViewPager();
        initData();
    }

    public class FullyLinearLayoutManager extends LinearLayoutManager {

        private final String TAG = FullyLinearLayoutManager.class.getSimpleName();

        public FullyLinearLayoutManager(Context context) {
            super(context);
        }

        public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                              int widthSpec, int heightSpec) {

            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            Log.i(TAG, "onMeasure called. \nwidthMode " + widthMode
                    + " \nheightMode " + heightSpec
                    + " \nwidthSize " + widthSize
                    + " \nheightSize " + heightSize
                    + " \ngetItemCount() " + getItemCount());

            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                measureScrapChild(recycler, i,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                       int heightSpec, int[] measuredDimension) {
            try {
                View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException

                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();

                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                            getPaddingLeft() + getPaddingRight(), p.width);

                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                            getPaddingTop() + getPaddingBottom(), p.height);

                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }





    /**
     * 初始化轮播图
     */
    public void initCycleViewPager() {
        //设置布局文件
        mEBVBanner.setPointColor(R.drawable.new_ezt_banner_view_bg_point_home_selector);
        mEBVBanner.setItemView(R.layout.new_item_one_thousand_banner, imageUrls.length);
        mEBVBanner.setOnBannerClickListener(new EztBannerView.OnBannerClickListener() {
            @Override
            public void onBannerClick(View v, int position) {
                Intent intent = new Intent();
                switch (position % imageUrls.length) {
                    case 0://医疗卡
                        intent.setClass(getContext(), MedicalCardDetailsActivity.class);
                        break;
                    case 1://预约检查

                        intent.setClass(getContext(), ChoiceHosActivity.class);
                        intent.putExtra("isChangedCity", false);
                        intent.putExtra("isOrderCheck", true);

                        break;
                    case 2://预约病床
                        intent.setClass(getContext(), OrderBedNoticeActivity.class);

                        break;
                    case 3://强势入驻

                        intent.setClass(getContext(), EnterActivity.class);
                        break;
                    case 4://一千零一夜
                        intent.setClass(getContext(), OneThousandDoctorActivity.class);
                        break;
                    default:
                        break;
                }
                    startActivity(intent);

            }
        });
        mEBVBanner.setInitItemViewListener(new EztBannerView.InitItemViewListener() {
            @Override
            public void initItemView(View v, int position) {
                ImageView imageView = (ImageView) v.findViewById(R.id.new_item_one_thousand_banner_iv);
                TextView textView = (TextView) v.findViewById(R.id.new_item_one_thousand_banner_tv);
                textView.setVisibility(View.GONE);
                GlideUtils.intoRound(getContext(), imageUrls[position % imageUrls.length],7, imageView);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("一千零医", "onPause");
        mEBVBanner.stopScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("一千零医", "onResume");
        mEBVBanner.startScroll();
    }

    private void initData() {

        if ("定位中".equals(search_city.getText())){
            search_city.setText(TextUtils.isEmpty(Constant.CURRENT_CITY_NAME)?"定位中":Constant.CURRENT_CITY_NAME);
        }

//        if (BaseApplication.eztUser == null){
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).postDefaultDoctorList(REQUEST_HOT_DOCTOR_DEF, this);
//        }else {
//            MyHotDoctorRequest doctorRequest = new MyHotDoctorRequest();
//            doctorRequest.setUserId(BaseApplication.eztUser.getUserId()+"");
////            doctorRequest.setUserId("4549924");
//            doctorRequest.setPage(1);
//            doctorRequest.setRowsPerPage(3);
//            HTTPHelper.getInstance(HTTPHelper.URL_TYPE.HOT_DOCTOR).postMyDoctorList2(doctorRequest, new Subscriber<String>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(String o) {
//                    Log.i("doctorResponse",""+o);
//                }
//            });
//        }
        OneThousandRequest request = new OneThousandRequest();
        OneThousandRequest.DataBean bean = new OneThousandRequest.DataBean();
        bean.setPageNumber("1");
        bean.setUserID("0");
        request.setData(bean);
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.IMG).postOneThousandAll(request, REQUEST_ONE_THOUSAND, this);

        getCityListData();
    }

    /**
     * 获取有合作的城市列表
     */
    private void getCityListData() {
        Request request = new Request();

        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getCityListData(
                request.getFormMap(new HashMap<String, String>()), REQUEST_CITY_LIST, this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.new_item_content_home_tv_more://一千零医夜 更多
                intent = new Intent(getContext(), OneThousandDoctorActivity.class);
                startActivity(intent);
                break;
            case R.id.new_item_module_home_appointment://预约挂号页面
                intent = new Intent(activity, AppointmentMainActivity.class);
                intent.putExtra("cityData", Constant.cityListDataResponse);
                startActivity(intent);


                //跳转到以前的预约挂号流程
//                intent = new Intent(activity, ChoiceHosActivity.class);
//                intent.putExtra("isDayRegList", false);
//                intent.putExtra("isChangedCity", false);//目前先默认没有选择新的城市，需要修改
//                startActivity(intent);
                break;

            case R.id.new_item_module_home_special_service:
                startActivity(new Intent(mContext, FeatureServiceActivity.class));
                break;
            case R.id.home_fragment_dragon_pic://跳转到以前的龙卡详情页面
                //   startActivity(new Intent(mContext, ActivityDragonCard.class));
                if (BaseApplication.patient != null) {
                    if (DragonStatusSingle.getInstance().isOpenDragon()) {
                        startActivity(new Intent(mContext, ActivityDragonCard.class));
                    } else {
                        startActivity(new Intent(mContext, MyDragonCardActivity.class));
                    }
                } else {
                    hintToLogin(com.eztcn.user.hall.utils.Constant.LOGIN_COMPLETE);
                }
                break;
            case R.id.new_item_module_home_famous_doctor:
                //跳转到以前的大牌名医流程
                intent = new Intent(activity, BigDoctorList30Activity.class);
                intent.putExtra("isChangedCity", false);//目前先默认没有选择新的城市，需要修改
                startActivity(intent);
                break;
            case R.id.search_city://选择城市页面
                intent = new Intent(mContext, SelectAllCityActivity.class);
                intent.putExtra("cityData", Constant.cityListDataResponse);
                startActivityForResult(intent, 222);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart(int requestType) {

        showProgressDialog("正在加载");
    }

    @Override
    public void onCompleted(int requestType) {
        dismissProgressDialog();
        //取消刷新。
       if(swipe_refresh_layout.isRefreshing()){
           swipe_refresh_layout.setRefreshing(false);
       }
    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        if(swipe_refresh_layout.isRefreshing()){
            swipe_refresh_layout.setRefreshing(false);
        }
        switch (requestType) {
            case REQUEST_CITY_LIST://如果获取城市列表失败了就显示默认天津
                search_city.setText("天津");
                Constant.cityListDataResponse.setId("2");
                Constant.cityListDataResponse.setName("天津");
                break;
        }
    }

    /**
     * 访问接口判断用户信息是否完善
     */
    public void validationPersonalInfo() {
        showProgressDialog("验证信息中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();

        params.put("userId", ((PatientBean) FileUtils.getObject(mContext, "patient")).getUserId() + "");//用户id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).validationPersonalInfo(request.getFormMap(params), 222, this);
    }

    private HotDoctorResponse.DataBean dataBean;

    @Override
    public void onNext(IModel t, int requestType) {
        switch (requestType) {
            case REQUEST_HOT_DOCTOR_DEF:
                HotDoctorResponse ddResponse = (HotDoctorResponse) t;
                List<HotDoctorResponse.DataBean> ddDatas = ddResponse.getData();
                if (ddDatas.size() > 0) {
                    BaseCommonRvAdapter rvAdapter = new BaseCommonRvAdapter<HotDoctorResponse.DataBean>(getContext(), R.layout.new_item_home_fragment_hot_doctor, ddDatas) {
                        @Override
                        public void convert(ViewHolder holder, HotDoctorResponse.DataBean dataBean) {
                            ImageView portrait = holder.getView(R.id.new_home_fragment_hot_doctor_iv_img_1);
                            TextView name = holder.getView(R.id.new_home_fragment_hot_doctor_tv_name_1);
                            TextView edLevelName = holder.getView(R.id.new_home_fragment_hot_doctor_tv_dec_1);

                            GlideUtils.intoRound(getContext(), HTTPHelper.BASE_PATH_HOT_DOCTOR_IMG + dataBean.getEdPic(), 0, portrait);
                            name.setText(dataBean.getEdName());
                            edLevelName.setText(dataBean.getEdLevelName());
                        }
                    };
                    mHotDoctorRv.setAdapter(rvAdapter);
                    rvAdapter.setOnItemClickListener(new BaseCommonRvAdapter.OnItemClickListener<HotDoctorResponse.DataBean>() {
                        @Override
                        public void onItemClick(ViewGroup parent, View view, HotDoctorResponse.DataBean dataBean2, int position) {
                            if (null == FileUtils.getObject(mContext, "patient")) {//如果没有登录，就去登录页面
                                startActivity(new Intent(mContext, LoginActivity.class));
                                return;
                            }
                            dataBean = dataBean2;
                            validationPersonalInfo();
                        }

                        @Override
                        public boolean onItemLongClick(ViewGroup parent, View view, HotDoctorResponse.DataBean dataBean, int position) {
                            return false;
                        }
                    });
                }
                break;
            case REQUEST_HOT_DOCTOR:
//                Log.i("doctorResponse",""+t);
//                MyHotDoctorResponse doctorResponse = (MyHotDoctorResponse) t;
//                Log.i("doctorResponse",""+doctorResponse.toString());
//                MyHotDoctorResponse.DataBean dataBean = doctorResponse.getData();
//                List<MyHotDoctorResponse.DataBean.RowsBean> doctordatas = dataBean.getRows();
//                if (doctordatas.size() >= 3){
//                    List<MyHotDoctorResponse.DataBean.RowsBean> newDatas = doctordatas.subList(0,3);
//                    GlideUtils.intoRound(getContext(),HTTPHelper.BASE_PATH_HOT_DOCTOR_IMG+newDatas.get(0).getEztHosDeptDocBean().getEdPic(),0,mHotDocImg_1);
//                    GlideUtils.intoRound(getContext(),HTTPHelper.BASE_PATH_HOT_DOCTOR_IMG+newDatas.get(1).getEztHosDeptDocBean().getEdPic(),0,mHotDocImg_2);
//                    GlideUtils.intoRound(getContext(),HTTPHelper.BASE_PATH_HOT_DOCTOR_IMG+newDatas.get(2).getEztHosDeptDocBean().getEdPic(),0,mHotDocImg_3);
//
//                    mHotDocName_1.setText(newDatas.get(0).getEztHosDeptDocBean().getDcName());
//                    mHotDocName_2.setText(newDatas.get(1).getEztHosDeptDocBean().getDcName());
//                    mHotDocName_3.setText(newDatas.get(2).getEztHosDeptDocBean().getDcName());
//
//                    mHotDocDec_1.setText(newDatas.get(0).getEztHosDeptDocBean().getEdLevelName());
//                    mHotDocDec_2.setText(newDatas.get(1).getEztHosDeptDocBean().getEdLevelName());
//                    mHotDocDec_3.setText(newDatas.get(2).getEztHosDeptDocBean().getEdLevelName());
//                }
                break;
            case REQUEST_ONE_THOUSAND:
                OneThousandResponse response = (OneThousandResponse) t;
                final List<OneThousandResponse.DataBean> datas = response.getData();
                mOneThousandLv.setAdapter(new BaseCommonAdapter<OneThousandResponse.DataBean>(getContext(), datas.subList(0,2)) {
                    @Override
                    public void convert(ViewHolder holder, OneThousandResponse.DataBean item, int position, View convertView, ViewGroup parent) {
                        ImageView oneThousandIv = holder.getView(R.id.new_item_home_fragment_one_thousand_iv_doctor);
                        TextView oneThousandTv = holder.getView(R.id.new_item_home_fragment_one_thousand_tv_dec);
                        TextView oneThousandNumTv = holder.getView(R.id.new_item_home_fragment_one_thousand_tv_number);

                        GlideUtils.into(getContext(), HTTPHelper.BASE_PATH_IMG + item.getImageUrl(), oneThousandIv);
                        oneThousandTv.setText(item.getTitle());
                        oneThousandNumTv.setText("10000阅读");
                    }

                    @Override
                    public ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
                        return ViewHolder.get(getContext(), convertView, parent, R.layout.new_item_home_fragment_one_thousand, position);
                    }


                });
                mOneThousandLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), WebViewActivity.class);
                        intent.putExtra("infoUrl", HTTPHelper.BASE_PATH_ARTICLE + datas.subList(0, 2).get(position).getArticleUrl());
                        intent.putExtra("title", "一千零医夜");
                        startActivity(intent);
                    }
                });
                setListViewHeight(mOneThousandLv, datas);
                break;
            case REQUEST_CITY_LIST:
                Response cityListResponse = (Response) t;
                if (cityListResponse.isFlag()) {
                    cityListMap = (ArrayList<CityListDataResponse>) cityListResponse.getData();
                    SaveGetDataUtil.setCityListData(cityListMap);//将列表数据保存到本地
                    for (CityListDataResponse dataResponse : cityListMap) {//判断本地城市是否在合作的城市列表中
                        if (dataResponse.getName().contains(Constant.CURRENT_CITY_NAME) ||
                                Constant.CURRENT_CITY_NAME.contains(dataResponse.getName())) {
//                            search_city.setText(dataResponse.getName());
                            Constant.cityListDataResponse.setId(dataResponse.getId());
                            Constant.cityListDataResponse.setName(dataResponse.getName());
                            break;
                        }
                    }
                    if (TextUtils.isEmpty(Constant.cityListDataResponse.getName())) {//显示默认的城市 天津
//                        search_city.setText("天津");
                        Constant.cityListDataResponse.setId("2");
                        Constant.cityListDataResponse.setName("天津");
                    }

                } else {
//                    search_city.setText("天津");
                    Constant.cityListDataResponse.setId("2");
                    Constant.cityListDataResponse.setName("天津");
                }

                break;
            case 222:
                Response response2 = (Response) t;
                if ("2000".equals(response2.getNumber())) {//信息已经完善了
                    //对天津市肿瘤医院进行标记
                    if ("1".equals(dataBean.getEhIsneedPatientCard())) {
                        Constant.IS_SPECIAL_HOSPITAL = true;
                    } else {
                        Constant.IS_SPECIAL_HOSPITAL = false;
                    }
                    //构建选择预约时间需要的数据模型
                    DoctorListResponse doctorListResponse = new DoctorListResponse();
                    doctorListResponse.setDocId(dataBean.getDocId() + "");
                    doctorListResponse.setDocName(dataBean.getEdName());
                    doctorListResponse.setLevelId(dataBean.getEdLevel() + "");
                    doctorListResponse.setLevelName(dataBean.getEdLevelName());
                    doctorListResponse.setHospitalId(dataBean.getHid() + "");
                    doctorListResponse.setHospitalName(dataBean.getEhName());
                    doctorListResponse.setDeptId(dataBean.getDeptId() + "");
                    doctorListResponse.setDptName(dataBean.getDptame());
                    doctorListResponse.setGoodAt(dataBean.getEdGoodat());
                    doctorListResponse.setOrderNum(0);
                    doctorListResponse.setDocPic(HTTPHelper.BASE_PATH_HOT_DOCTOR_IMG + dataBean.getEdPic());
                    doctorListResponse.setDocSex(dataBean.getEdSex() + "");
                    startActivity(new Intent(mContext, SelectAppointmentTimeActivity.class)
                            .putExtra("hospital", doctorListResponse));
                } else {//信息没有完善
                    CommonDialog.showCommonDialog(getActivity(),
                            "请先完善您的个人信息", "知道了", new CommonDialogOkListener() {
                                @Override
                                public void onClick() {
                                    startActivity(new Intent(mContext, CompletePersonalInfoActivity.class));//去完善个人信息页面
                                }
                            }, false);
                }
                break;
        }
    }



    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     */
    public void setListViewHeight(ListView listView,final List<OneThousandResponse.DataBean> datas) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len =listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 登录提示。
     */
    public void hintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.item_login_dialog, null);
        TextView login = (TextView) dialogView.findViewById(R.id.dialog_login);
        TextView cancel = (TextView) dialogView
                .findViewById(R.id.dialog_cancel);
        dialog.show();
        dialog.setContentView(dialogView);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), requestCode);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 222) {//选择城市页面返回
//            Constant.cityListDataResponse.setName(data.getStringExtra("cityName"));
//            Constant.cityListDataResponse.setId(data.getStringExtra("cityId"));


            String cityName=data.getStringExtra("name");


            if(TextUtils.isEmpty(cityName)){

                search_city.setText("");
            }else {
                if(cityName.length()<=3){
                    search_city.setText(cityName);
                }else {
                    String city=cityName.substring(0,3);
                    search_city.setText(city);
                }
            }

//            search_city.setText(data.getStringExtra("name"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 搜索的点击事件。
     */
    View.OnClickListener new_activity_search_editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(mContext, SearchActivity.class));

        }
    };
    /**
     * 下拉刷新监听。
     */
    SwipeRefreshLayout.OnRefreshListener  swipe_refresh_layoutListener=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //进行当前城市的定位
            LocationUtils.location(mContext, new LocationListener() {
                @Override
                public void onReceiveLocation(BDLocation location) {
                    Constant.CURRENT_CITY_NAME = location.getCity();//对常量里面的城市进行赋值
                    if ("定位中".equals(search_city.getText())){
                        search_city.setText(Constant.CURRENT_CITY_NAME);
                    }
                }
            });
            initData();//进行数据刷新。
        }
    };
}



