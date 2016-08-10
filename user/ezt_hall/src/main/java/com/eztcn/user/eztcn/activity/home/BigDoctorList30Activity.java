package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.adapter.CityCommonAdapter;
import com.eztcn.user.eztcn.adapter.DoctorListAdapter30;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.fragment.HomeFragment30;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ScreenUtils;
import com.eztcn.user.hall.utils.LogUtils;

/**
 * @author ezt
 * @title 医生列表
 * @describe （按医生找、按科室找）
 * @created 2013年03月03日
 */
public class BigDoctorList30Activity extends FinalActivity implements
        OnClickListener, IHttpResult, OnLoadMoreListener, OnRefreshListener,
        OnItemClickListener {
    private int flag = -1;
    private ListView cityLv;
    private ListView hosLv;
    @ViewInject(R.id.docs_lv)
    // , itemClick = "onItemClick"
    private PullToRefreshListView lv;// 医生列表

    @ViewInject(R.id.docs_choice_tv2)
    // , click = "onClick"
    // 第二个筛选按钮
    public TextView tvChoiceCity;//

    @ViewInject(R.id.docs_choice_tv3)
    // , click = "onClick"
    // 第三个筛选按钮
    public TextView tvChoiceHos;//

    @ViewInject(R.id.bt_layout)
    public LinearLayout layoutBt;//

    private DoctorListAdapter30 adapter;

    private DoctorListAdapter30 searchAdapter;

    private String hosName = "";// 医院名称
    private String deptName = "";// 科室名称
    private String hosId = "";// 医院id
    private String deptId = "";// 医院科室id
    private String deptTypeId = "";// 科室小分类id
    private String cityId = "";// 所选的城市id
    private String time = "";// 所选的时间
    private String sourcePfId = "355";// 平台来源id

    private int currentPage = 1;// 当前页数
    private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
    private ArrayList<Doctor> docList;

    private int result_Code = 0;// 标记是否为回调返回调用接口（非0为回调接口）
    private final String BIG_DOC_LIST_DATA = "Hos_BigDocListData";// 缓存key-大牌名医

    private CacheUtils mCache;
    private Integer pDoctorPosition;

    private boolean isChoice = false;// 是否选择医院（专家看病历）

    /**
     * 特殊医院需求：泰达心血管病医院，确认预约挂号页面没有初复诊选项，特殊需求该医院：复诊时需要填写复诊病案号
     */
    public static int hospatialId;
    private boolean isAllSearch;
    private Hospital hospital;
    private boolean isDayReg = false;// 是否当日挂号
    private View searchBtn;
    private boolean getHosData;// 是否获取医院数据，点医院列表时候不用获取
    private ListView dialog_searchLv;

    @ViewInject(R.id.downBtn_tv1)
    private ImageView downBtn_tv1;
    @ViewInject(R.id.downBtn_tv2)
    private ImageView downBtn_tv2;
    @ViewInject(R.id.downBtn_tv3)
    private ImageView downBtn_tv3;

    // private Animation animToDown, animToUp;
    @ViewInject(R.id.Layout_tv1)
    private View Layout_tv1;
    @ViewInject(R.id.Layout_tv2)
    private View Layout_tv2;
    @ViewInject(R.id.Layout_tv3)
    private View Layout_tv3;

    @ViewInject(R.id.docs_choice_tv1)
    private TextView docs_choice_tv1;
    @ViewInject(R.id.docs_choice_tv2)
    private TextView docs_choice_tv2;
    @ViewInject(R.id.docs_choice_tv3)
    private TextView docs_choice_tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorlist30);
        ViewUtils.inject(BigDoctorList30Activity.this);
        // animToDown = AnimationUtils.loadAnimation(mContext,
        // R.anim.rotatedown);
        // animToUp = AnimationUtils.loadAnimation(mContext, R.anim.rotateup);
        gainIntent();
        mCache = CacheUtils.get(this);
        isChoice = getIntent().getBooleanExtra("isChoice", false);
        isAllSearch = getIntent().getBooleanExtra("isAllSearch", false);// 是否为全站搜索进入
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            hospital = (Hospital) savedInstanceState
                    .getSerializable("hospital");
        }

        searchBtn = loadTitleBar(true, "大牌名医", R.drawable.big_searchbtn);
        searchBtn.setOnClickListener(this);
        deptId = getIntent().getStringExtra("deptId");
        deptTypeId = getIntent().getStringExtra("deptTypeId");
        hosId = getIntent().getStringExtra("hosId");
        layoutBt.setVisibility(View.VISIBLE);

        // 初始化下拉框
        initialCityBtPop();
        initialHosBtPop();

        deptName = getIntent().getStringExtra("deptName");

//        if (!isAllSearch) {// 城市选择赋值
//            cityId = SystemPreferences.getString(EZTConfig.KEY_CITY_ID);
//            // if (TextUtils.isEmpty(SystemPreferences
//            // .getString(EZTConfig.KEY_STR_CITY))) {//
//            // 第一次进入医生列表(城市选择在外部如果有选择城市)
//            String[] cityNames = getResources().getStringArray(
//                    R.array.appoint_city_name);
//            String[] cityIds = getResources().getStringArray(
//                    R.array.appoint_city_id);
//            String myCity = BaseApplication.selectCity;
//            if (!TextUtils.isEmpty(myCity)) {
//                for (int i = 0; i < cityNames.length; i++) {
//                    // if (myCity.equals(cityNames[i])) {
//                    if (myCity.contains(cityNames[i])) {
//                        cityId = cityIds[i];
//                        break;
//                    } else if (i == cityNames.length - 1) {
//                        cityId = "";
//                    }
//                }
//            } else {
//                cityId = "";
//            }
//        }
        cityId = "";//临时将cityid初始化为2，获取天津的数据，以后需要修改
        // }
        adapter = new DoctorListAdapter30(this, true);
        adapter.isChoice = isChoice;
        // adapter.setOnTelDoctorListener(this);
        lv.setAdapter(adapter);
        lv.setCanLoadMore(true);
        lv.setCanRefresh(true);
        lv.setAutoLoadMore(true);
        lv.setMoveToFirstItemAfterRefresh(false);
        lv.setDoRefreshOnUIChanged(false);
        lv.setOnLoadListener(this);
        lv.setOnRefreshListener(this);

        if (BaseApplication.getInstance().isNetConnected) {
            showProgressToast();
            initialData(true);
        } else {// 无网络
            ArrayList<Doctor> cacheData = (ArrayList<Doctor>) mCache
                    .getAsObject(BIG_DOC_LIST_DATA);
            adapter.setList(cacheData);
            if (adapter.getList() != null && adapter.getList().size() != 0) {
                lv.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(mContext, getString(R.string.network_hint),
                        Toast.LENGTH_SHORT).show();
            }
        }
        initSearchDialogView();
    }

    private void gainIntent() {
        Intent intentFrom = getIntent();
        isChoice = intentFrom.getBooleanExtra("isChoice", false);
        isAllSearch = intentFrom.getBooleanExtra("isAllSearch", false);// 是否为全站搜索进入

    }

    @Override
    public void onBackPressed() {
        hideProgressToast();
        super.onBackPressed();
    }

    private int selectCityPos = -1;// 选择地区下标
    private int selectHosPos = -1;// 选择医院下标
    private CharacterParser characterParser;// 汉字转换成拼音的类
    private TextView tvLine;
    private LinearLayout body_layout, layoutLeft;

    private Button btPop;
    private View noopsycheView;
    private FrameLayout layoutThreeHos;

    /**
     * 城市筛选（按科室找需要显示）
     */
    private LinearLayout cityLayout;
    private CityCommonAdapter cityAdapter;
    private LinearLayout hosLayout;
    private BigHospitalDataAdapter hosAdapter;

    private void initialHosBtPop() {
        String hosName = "";
        // hosName = SystemPreferences.getString(EZTConfig.KEY_HOS_NAME);
        if (null != hospital)
            hosName = hospital.gethName();
        if (!TextUtils.isEmpty(hosName)) {
            tvChoiceHos.setText(hosName);
        } else {
            tvChoiceHos.setText("医院");
        }
        hosLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        hosLayout.setOrientation(LinearLayout.VERTICAL);
        // ListView cityLv
        hosLv = new ListView(mContext);
        hosLv.setBackgroundResource(R.color.main_bg_color);
        hosLv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        hosLv.setSelector(getResources().getDrawable(
                R.drawable.selector_listitem_bg));
        hosLv.setDividerHeight(0);
        hosLv.setDivider(getResources()
                .getDrawable(android.R.color.transparent));
        hosLayout.addView(hosLv);
        hosLv.setOnItemClickListener(this);
        hosAdapter = new BigHospitalDataAdapter(mContext);
        hosLv.setAdapter(hosAdapter);
    }

    /**
     * @author ezt
     * @title 医院adapter
     * @describe
     * @created 2014年12月18日
     */

    private class BigHospitalDataAdapter extends BaseArrayListAdapter<Hospital> {
        // private final int NORMAL=1;//(正常，非对接状态) 2015-12-17
        private final int WARING = 0;// (警告，医院为对接状态对接状态) 2015-12-17
        private int selectedPosition = -1;

        public BigHospitalDataAdapter(Activity context) {
            super(context);
        }

        class ViewHolder {
            TextView hosName;
            RelativeLayout layout;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_bighospital,
                        null);
                holder.hosName = (TextView) convertView
                        .findViewById(R.id.item_hos_name);
                holder.layout = (RelativeLayout) convertView
                        .findViewById(R.id.colorlayout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Hospital hos = mList.get(position);

            // 2015-12-17 医院对接中
            // holder.hosName.setText(hos.gethName());

            String showName = hos.getEhDockingStatus() == WARING ? hos
                    .gethName() + "(" + hos.getEhDockingStr() + ")" : hos
                    .gethName();
            holder.hosName.setText(showName);

            // 设置选中效果
            if (selectedPosition == position) {
                holder.hosName.setTextColor(mContext.getResources().getColor(
                        R.color.main_color));
                holder.layout.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.light_gray));
            } else {
                holder.hosName.setTextColor(mContext.getResources().getColor(
                        R.color.dark_black));
                holder.layout.setBackgroundColor(mContext.getResources()
                        .getColor(android.R.color.white));
                // holder.layout.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

    }

    private void initialCityBtPop() {
        String cityName = "";
        // cityName = SystemPreferences.getString(EZTConfig.KEY_STR_CITY);
        cityName = BaseApplication.selectCity;
//        if (!TextUtils.isEmpty(cityName)) {
//            tvChoiceCity.setText(cityName);
//        } else {
        tvChoiceCity.setText("全国");
//        }
        if (null != mContext) {
            // tvChoice1
            cityLayout = new LinearLayout(mContext);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            cityLayout.setOrientation(LinearLayout.VERTICAL);
            // ListView cityLv
            cityLv = new ListView(mContext);
            cityLv.setBackgroundResource(R.color.main_bg_color);
            cityLv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            cityLv.setSelector(getResources().getDrawable(
                    R.drawable.selector_listitem_bg));
            cityLv.setDividerHeight(0);
            cityLv.setDivider(getResources().getDrawable(
                    android.R.color.transparent));
            cityLayout.addView(cityLv);
            cityLv.setOnItemClickListener(this);
            cityAdapter = new CityCommonAdapter(mContext);
            cityLv.setAdapter(cityAdapter);
            String[] cityNames = getResources().getStringArray(
                    R.array.appoint_city_name);
            // 城市数组
            String[] cityIds = getResources().getStringArray(
                    R.array.appoint_city_id);
            // 城市id数组
            ArrayList<City> citys = new ArrayList<City>();
            for (int i = 0; i < cityIds.length; i++) {
                City city = new City();
                city.setCityId(cityIds[i]);
                city.setCityName(cityNames[i]);
                citys.add(city);
            }
            cityAdapter.setList(citys);
        }
    }

    /**
     * 弹出下拉
     *
     * @param view
     * @param flag
     */
    private PopupWindow choicePop;// 下拉弹出框

    private void initialChoicePopwindow(final View view) {

        if (view.getParent() != null) {//如果该view有父容器，就去掉它的父容器，防止父容器重复
            ViewGroup viewParent = (ViewGroup) view.getParent();
            viewParent.removeView(view);
        }
        choicePop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, false);
        choicePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switch (flag) {
                    case 1: {
                        // downBtn_tv1.startAnimation(animToUp);
                        docs_choice_tv1.setTextColor(getResources().getColor(
                                R.color.dark_black));
                        downBtn_tv1.setImageResource(R.drawable.arrowdown);
                    }
                    break;
                    case 2: {
                        // downBtn_tv2.startAnimation(animToUp);
                        docs_choice_tv2.setTextColor(getResources().getColor(
                                R.color.dark_black));
                        downBtn_tv2.setImageResource(R.drawable.arrowdown);
                    }
                    break;
                    case 3: {
                        // downBtn_tv3.startAnimation(animToUp);
                        docs_choice_tv3.setTextColor(getResources().getColor(
                                R.color.dark_black));
                        downBtn_tv3.setImageResource(R.drawable.arrowdown);
                    }
                    break;
                }
            }
        });

        // 设置点击窗口外边窗口消失
        choicePop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        choicePop.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        choicePop.setBackgroundDrawable(dw);

        view.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                choicePop.dismiss();
                return true;
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initialData(boolean getHosData) {
        initialDocData();
        if (getHosData)
            getHosData(cityId, "", "");
    }

    /**
     * 查询大牌名医数据
     */
    private void initialDocData() {
        // toast(" 城市id " + cityId + " 医院id " + hosId + " 小科室分类id " + deptTypeId
        // + " 医院的科室id（根据医院id 获取） " + deptId, Toast.LENGTH_LONG);
        RequestParams params = new RequestParams();
        HospitalImpl impl = new HospitalImpl();
        params.addBodyParameter("orderLevel", isLevel ? "1" : "0");// 是否按职务排行
        params.addBodyParameter("orderRate", isRate ? "1" : "0");// 是否按预约率排序
        params.addBodyParameter("orderYnRemain", isNum ? "1" : "1");// 是否有号
        params.addBodyParameter("orderYnEvaluation", isEvaluate ? "1" : "0");// 按评价高低
        // private boolean isThreeHos=false;
        // params.addBodyParameter("dcOrderParm", "6");//
        // 1热门2新闻页3名医堂4最受欢迎5妇婴名医6本院推荐
        params.addBodyParameter("cityId", cityId);// 城市id
        params.addBodyParameter("hospitalId", hosId);// 医院id
        params.addBodyParameter("deptCateId", deptTypeId);// 小科室分类id（无医院id 获取）
        params.addBodyParameter("deptId", deptId);// 医院的科室id（根据医院id 获取）
        // ps:deptCateId 和 deptId 独立
        params.addBodyParameter("rowsPerPage", pageSize + "");
        params.addBodyParameter("page", currentPage + "");
        params.addBodyParameter("sourcePfId", sourcePfId);
        params.addBodyParameter("dcOrderParm", "3");
        impl.getBigDocList(params, this);
    }

    private boolean isNum = false;
    private boolean isThreeHos = false;
    private boolean isRate = false;
    private boolean isLevel = false;
    private boolean isEvaluate = false;
    private final int CITY = 1, HOSPITAL = 2;

    /**
     * 点击医院
     *
     * @param v
     */
    @OnClick(R.id.Layout_tv3)
    public void hosTvClick(View v) {
        flag = 3;
        // if (BaseApplication.getInstance().isNetConnected) {
        // initialHosBtPop();

        initialChoicePopwindow(hosLayout);// 科室（科室筛选进入）/ 医院（医院筛选进入）
        if (choicePop != null) {
            if (!choicePop.isShowing()) {
                choicePop.showAsDropDown(v, 0, 0);
                // downBtn_tv3.startAnimation(animToDown);

                docs_choice_tv3.setTextColor(getResources().getColor(
                        R.color.main_color));
                downBtn_tv3.setImageResource(R.drawable.green_arrowup);
            } else {
                choicePop.dismiss();
                // downBtn_tv3.startAnimation(animToUp);
            }
        }

        // } else {// 无网络
        // Toast.makeText(mContext, getString(R.string.network_hint),
        // Toast.LENGTH_SHORT).show();
        // }

    }

    /**
     * 点击城市
     *
     * @param v
     */
    @OnClick(R.id.Layout_tv2)
    public void cityTvClick(View v) {
        // if (BaseApplication.getInstance().isNetConnected) {
        // 选择城市（按科室找）
        flag = 2;
        hosId = "";
        initialChoicePopwindow(cityLayout);
        if (choicePop != null) {
            if (!choicePop.isShowing()) {
                choicePop.showAsDropDown(v, 0, 0);
                // downBtn_tv2.startAnimation(animToDown);
                docs_choice_tv2.setTextColor(getResources().getColor(
                        R.color.main_color));
                downBtn_tv2.setImageResource(R.drawable.green_arrowup);

            } else {
                choicePop.dismiss();
                // downBtn_tv2.startAnimation(animToUp);
            }
        }

        // } else {// 无网络
        // Toast.makeText(mContext, getString(R.string.network_hint),
        // Toast.LENGTH_SHORT).show();
        // }

    }

    /**
     * 获取医院数据
     *
     * @param cityIds
     * @param countyId
     * @param levelId
     */
    private void getHosData(String cityIds, String countyId, String levelId) {

        RequestParams params = new RequestParams();
        HospitalImpl imple = new HospitalImpl();
        params.addBodyParameter("cityid", cityIds);
        if (!countyId.equals("")) {
            params.addBodyParameter("countyid", countyId);
        }
        if (!levelId.equals("")) {
            params.addBodyParameter("level", levelId);
        }
        Boolean isDayRegList = getIntent().getBooleanExtra("isDayRegList",
                false);
        if (isDayRegList) {
            imple.getDayHosList(params, this);
        } else
            imple.getHospitalList(params, this);

    }

    private Dialog searchDialog;
    private View searchView, dialogSearchBtn;
    private EditText etSearchTv;
    ArrayList<Doctor> tempSearchList;

    /**
     * 初始化选择dialog
     */
    private void initSearchDialogView() {
        searchDialog = new Dialog(this, R.style.dialog);
        searchView = LayoutInflater.from(mContext).inflate(R.layout.pop_bigdoc,
                null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.width = ScreenUtils.gainDM(mContext).widthPixels;
        searchDialog.setContentView(searchView, params);
        Window win = searchDialog.getWindow();
        win.setGravity(Gravity.TOP);

        // win.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
        // WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        searchDialog.setCanceledOnTouchOutside(true);

        dialog_searchLv = (ListView) searchView
                .findViewById(R.id.dialog_searchLv);
        etSearchTv = (EditText) searchView.findViewById(R.id.bigDoc_search_et);
        dialogSearchBtn = searchView.findViewById(R.id.imageView1);
        dialogSearchBtn.setOnClickListener(this);

        tempSearchList = new ArrayList<Doctor>();
        searchAdapter = new DoctorListAdapter30(BigDoctorList30Activity.this,
                true);
        dialog_searchLv.setAdapter(searchAdapter);
        searchAdapter.setList(tempSearchList);
        dialog_searchLv.setOnItemClickListener(this);

        View backView = searchView.findViewById(R.id.bigDocSearchBack);
        backView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // if (null != etSearchTv)
                // hideSoftInput(etSearchTv);
                searchDialog.dismiss();
            }
        });
    }

    /**
     * 重置搜索数据
     */
    private void searchData(String searchWord) {
        showProgressToast();
        hideSoftInput(etSearchTv);
        if (null == tempSearchList) {
            tempSearchList = new ArrayList<Doctor>();
        } else {
            tempSearchList.clear();
        }
        if (null != adapter.mList)
            for (int i = 0; i < adapter.mList.size(); i++) {
                Doctor doc = adapter.mList.get(i);
                String docDeptStr = doc.getDocDept();
                String docHosStr = doc.getDocHos();
                String docNameStr = doc.getDocName();
                if (StringUtils.isEmpty(searchWord)) {
                    tempSearchList.add(doc);
                } else if (docDeptStr.contains(searchWord)
                        || docHosStr.contains(searchWord)
                        || docNameStr.contains(searchWord)) {
                    tempSearchList.add(doc);
                }
            }

        if (null == searchAdapter) {
            searchAdapter = new DoctorListAdapter30(mContext, true);
            dialog_searchLv.setAdapter(searchAdapter);
            searchAdapter.setList(tempSearchList);
        } else {
            searchAdapter.notifyDataSetChanged();
        }
        hideProgressToast();

        if (tempSearchList.size() == 0)
            toast("未搜索到相关医生数据", Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            // 搜索按钮

            if (!searchDialog.isShowing()) {
                searchDialog.show();

                // etSearchTv.setFocusable(true);
                // etSearchTv.setFocusableInTouchMode(true);
                // etSearchTv.requestFocus();
                // InputMethodManager inputManager =
                // (InputMethodManager)etSearchTv.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // inputManager.showSoftInput(etSearchTv, 0);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) etSearchTv
                                .getContext().getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(etSearchTv, 0);
                    }

                }, 100);

                // showSoftInput(etSearchTv);
            } else {
                // if (null != etSearchTv)
                // hideSoftInput(etSearchTv);
                searchDialog.dismiss();
            }
        }
        if (v == dialogSearchBtn) {
            searchData(etSearchTv.getText().toString().trim());
        }
        // if (BaseApplication.getInstance().isNetConnected) {
        // switch (v.getId()) {
        // case R.id.pop_bt:// 智能筛选确定
        //
        // choicePop.dismiss();
        // getData();
        // break;
        // }
        // } else {// 无网络
        // Toast.makeText(mContext, getString(R.string.network_hint),
        // Toast.LENGTH_SHORT).show();
        // }

    }

    @Override
    public void result(Object... object) {
        int type = (Integer) object[0];
        boolean isSucc = (Boolean) object[1];
        hideProgressToast();
        switch (type) {
            case HttpParams.GET_HOS: {
                if (isSucc) {
                    List<Hospital> list = (ArrayList<Hospital>) object[2];
                    if (null != list)
                        hosAdapter.setList(list);
                    if (null == hospital)
                        tvChoiceHos.setText("医院");
                } else {
                    String error = (String) object[3];
                    toast(error, Toast.LENGTH_SHORT);
                }
            }
            break;
            case HttpParams.GET_RANKING_DOC_LIST:// 获取医生列表

                if (isSucc) {
                    Map<String, Object> map;
                    ArrayList<Doctor> data = null;
                    // docList = (ArrayList<Doctor>) object[2]; // 2015-12-26 接口對接
                    map = (Map<String, Object>) object[2];
                    if (map.containsKey("docList")) {
                        docList = (ArrayList<Doctor>) map.get("docList");
                    }
                    // hideProgressToast();
                    if (docList != null && docList.size() > 0) {
                        if (currentPage == 1) {// 第一次加载或刷新
                            data = docList;
                            adapter.resetStateValue();
                            if (docList.size() < pageSize) {
                                lv.setAutoLoadMore(false);
                                lv.onLoadMoreComplete();
                            }
                            lv.onRefreshComplete();
                            // 将医生数据塞入缓存
                            mCache.put(BIG_DOC_LIST_DATA, docList);

                        } else {// 加载更多
                            data = (ArrayList<Doctor>) adapter.getList();
                            if (data == null || data.size() <= 0)
                                data = docList;
                            else
                                data.addAll(docList);
                            lv.onLoadMoreComplete();

                        }
                        adapter.setList(data);
                        lv.setVisibility(View.VISIBLE);

                    } else {
                        if (adapter.getList() != null
                                && adapter.getList().size() != 0) {// 加载
                            if (currentPage > 1) {
                                currentPage--;
                            }
                            if (docList != null) {
                                if (docList.size() == 0) {
                                    lv.setAutoLoadMore(false);
                                }
                            } else {
                                Toast.makeText(mContext,
                                        getString(R.string.request_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                            lv.onLoadMoreComplete();
                            data = (ArrayList<Doctor>) adapter.getList();
                            if (result_Code != 0) {
                                adapter.mList.clear();
                                adapter.notifyDataSetChanged();
                                lv.setVisibility(View.GONE);
                            }

                        } else {// 刷新
                            lv.onRefreshComplete();
                            Toast.makeText(mContext, "抱歉，暂无可预约的医生",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressToast();
                        }

                    }

                    if (data != null) {
                        docList = data;
                    }

                } else {
                    hideProgressToast();
                    if (currentPage > 1) {
                        currentPage--;
                    }
                    lv.onLoadMoreComplete();
                    Toast.makeText(mContext, getString(R.string.service_error),
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private void bigDocItemClick(int position, DoctorListAdapter30 temAdapter) {

        if (BaseApplication.getInstance().isNetConnected) {
            boolean isOk = true;
            if (!temAdapter.getVisJump(position - 1)) {
                // Toast.makeText(mContext, getString(R.string.couldnotreg),
                // Toast.LENGTH_SHORT).show();
                // return;
                isOk = false;
            }

            // 医生列表
            position = position - 1;
            if (position > -1 && position < temAdapter.getList().size()) {
                String docId = temAdapter.getList().get(position).getId();
                String DeptId = temAdapter.getList().get(position).getDocDeptId();


                BaseApplication.selectCity = tvChoiceCity.getText().toString();
                HomeFragment30.isChangedCity = false;

                if (!isChoice) {
                    // isOk=true;
                    // if (!adapter.getVisJump(position - 1)) {
                    // // Toast.makeText(mContext, getString(R.string.couldnotreg),
                    // // Toast.LENGTH_SHORT).show();
                    // // return;
                    // isOk=false;
                    // }

                    String deptDocId = temAdapter.getList().get(position)
                            .getDeptDocId();
                    // 2015-15-18 医院对接
                    int ehDockingStatus = temAdapter.getList().get(position)
                            .getEhDockingStatus();

                    Intent intent = new Intent(BigDoctorList30Activity.this,
                            DoctorIndex30Activity.class).putExtra("deptId", DeptId)
                            .putExtra("docId", docId)
                            .putExtra("deptDocId", deptDocId)
                            .putExtra("hospital", hospital)
                            .putExtra("ehDockingStatus", ehDockingStatus)
                            .putExtra("canReg", isOk);
                    intent.putExtra("isDayReg", isDayReg);// 设置是否当日挂号
                    startActivity(intent);
                } else {// 选择医生
                    String docName = temAdapter.getList().get(position)
                            .getDocName();
                    String DeptName = temAdapter.getList().get(position)
                            .getDocDept();
                    String hosName = temAdapter.getList().get(position).getDocHos();
                    String hosId = temAdapter.getList().get(position).getDocHosId();
                    setResult(
                            33,
                            new Intent().putExtra("docId", docId)
                                    .putExtra("docName", docName)
                                    .putExtra("deptName", DeptName)
                                    .putExtra("hosName", hosName)
                                    .putExtra("deptId", DeptId)
                                    .putExtra("hosId", hosId)
                    );
                    finish();
                }
            }
        } else {// 无网络
            Toast.makeText(mContext, getString(R.string.network_hint),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @OnItemClick(R.id.docs_lv)
    public void docs_lvItemClick(AdapterView<?> parent, View view,
                                 int position, long id) {
        // if (BaseApplication.getInstance().isNetConnected) {
        //
        // if (!adapter.getVisJump(position - 1)) {
        // Toast.makeText(mContext, getString(R.string.couldnotreg),
        // Toast.LENGTH_SHORT).show();
        // return;
        // }
        //
        // // 医生列表
        // position = position - 1;
        // String docId = adapter.getList().get(position).getId();
        // String DeptId = adapter.getList().get(position).getDocDeptId();
        // if (!isChoice) {
        //
        // String deptDocId = adapter.getList().get(position)
        // .getDeptDocId();
        // // 2015-15-18 医院对接
        // int ehDockingStatus = adapter.getList().get(position)
        // .getEhDockingStatus();
        //
        // Intent intent = new Intent(BigDoctorList30Activity.this,
        // DoctorIndex30Activity.class).putExtra("deptId", DeptId)
        // .putExtra("docId", docId)
        // .putExtra("deptDocId", deptDocId)
        // .putExtra("hospital", hospital)
        // .putExtra("ehDockingStatus", ehDockingStatus);
        // intent.putExtra("isDayReg", isDayReg);// 设置是否当日挂号
        // startActivity(intent);
        // } else {// 选择医生
        // String docName = adapter.getList().get(position).getDocName();
        // String DeptName = adapter.getList().get(position).getDocDept();
        // String hosName = adapter.getList().get(position).getDocHos();
        // String hosId = adapter.getList().get(position).getDocHosId();
        //
        // setResult(
        // 33,
        // new Intent().putExtra("docId", docId)
        // .putExtra("docName", docName)
        // .putExtra("deptName", DeptName)
        // .putExtra("hosName", hosName)
        // .putExtra("deptId", DeptId)
        // .putExtra("hosId", hosId)
        //
        // );
        // finish();
        //
        // }
        // } else {// 无网络
        // Toast.makeText(mContext, getString(R.string.network_hint),
        // Toast.LENGTH_SHORT).show();
        // }
        bigDocItemClick(position, adapter);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        if (parent == cityLv) {// 筛选 城市选择
            flag = 2;
            // 筛选城市
            City city = cityAdapter.getList().get(position);
            cityId = city.getCityId();
            String cityName = city.getCityName();
            SystemPreferences.save(EZTConfig.KEY_CITY_ID, cityId);
            SystemPreferences.save(EZTConfig.KEY_STR_CITY, cityName);
            tvChoiceCity.setText(cityName);
            choicePop.dismiss();
            getHosData = true;
            getData();
        }
        if (parent == hosLv) {// 筛选 医院选择
            flag = 3;
            Hospital hosPital = hosAdapter.getList().get(position);
            this.hospital = hosPital;
            String hName = hosPital.gethName();
            hosId = String.valueOf(hosPital.getId());
            SystemPreferences.save(EZTConfig.KEY_HOS_NAME, hName);
            tvChoiceHos.setText(hName);
            choicePop.dismiss();
            getHosData = false;
            getData();
        }

        if (dialog_searchLv == parent) {
            bigDocItemClick(position + 1, searchAdapter);
        }
    }

    /**
     * 筛选后获取医生列表
     */
    private void getData() {

        if (BaseApplication.getInstance().isNetConnected) {
            showProgressToast();
            lv.setSelection(0);
            if (adapter.mList != null) {// 返回刷新，如果之前adapter，则清除
                adapter.mList.clear();
                lv.setAdapter(adapter);
                lv.setVisibility(View.GONE);
            }
            currentPage = 1;// 回调返回初始化页码
            initialData(getHosData);
        } else {// 无网络
            toast(getString(R.string.network_hint), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onLoadMore() {
        if (docList != null) {
            if (docList.size() < pageSize
                    || (docList.size() > pageSize && docList.size() % pageSize != 0)) {
                lv.setAutoLoadMore(false);
                lv.onLoadMoreComplete();
            } else {
                currentPage++;
                initialData(getHosData);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 从文件读取是否当日挂号
        isDayReg = SystemPreferences.getBoolean(EZTConfig.KEY_DF_IS_DAY_REG);
    }

    @Override
    public void onRefresh() {
        lv.setAutoLoadMore(true);
        currentPage = 1;
        initialData(getHosData);
    }

    /**
     * 判断电话医生状态
     */
    public boolean judgeTelStatus(TelDocState state) {
        boolean bool = true;
        int msg;
        if (state == null) {
            Toast.makeText(getApplicationContext(), "该医生暂未开通电话服务",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (state.getIsOpenService() != 1) {
            Toast.makeText(getApplicationContext(), "该医生暂未开启电话服务",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        // if (state.getYnAppointment() != 1) {
        // Toast.makeText(getApplicationContext(), "该医生暂不可预约",
        // Toast.LENGTH_SHORT).show();
        // return false;
        // }
        // if (pDoctorPosition == null) {
        // Toast.makeText(getApplicationContext(), "该医生暂不可预约",
        // Toast.LENGTH_SHORT).show();
        // return false;
        // }
        return true;
    }

}
