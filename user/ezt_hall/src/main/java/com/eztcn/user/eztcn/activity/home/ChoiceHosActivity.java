package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.List;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.ordercheck.ChoiceCheckItemActivity;
import com.eztcn.user.eztcn.adapter.AreaDataAdapter;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.adapter.HospitalDataAdapter;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.fragment.HomeFragment30;
import com.eztcn.user.eztcn.impl.CityImpl;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ScreenUtils;
import com.eztcn.user.hall.utils.Constant;

/**
 * @author ezt
 * @title 筛选医院
 * @describe
 * @created 2014年12月10日
 */
public class ChoiceHosActivity extends FinalActivity implements
        OnItemClickListener, IHttpResult {// OnClickListener,, OnClickListener

    private Context context = ChoiceHosActivity.this;
    @ViewInject(R.id.choice_hos_city_bt)

    private TextView btCity;// 筛选城市

    @ViewInject(R.id.choice_hos_noopsyche_bt)

    private TextView btNoopsyche;// 智能排序

    @ViewInject(R.id.choice_hos_close_bt)
    // , click = "onClick"
    private TextView btClose;// 关闭

    @ViewInject(R.id.area_item_all)
    // , click = "onClick"
    private TextView tvAll;

    @ViewInject(R.id.choice_area_lt)
    // , itemClick = "onItemClick"
    private ListView ltArea;// 区域列表

    @ViewInject(R.id.choice_hos_lt)
    // , itemClick = "onItemClick"
    private ListView ltHos;// 医院列表

    @ViewInject(R.id.tv_dept_all_line)
    // , click = "onClick"
    private View lineAllHos;// 所有的医院下面的线

    @ViewInject(R.id.tv_dept_all)
    // , click = "onClick"
    private TextView tvAllHos;// 所有的医院

    @ViewInject(R.id.bt_layout)
    private LinearLayout layoutBtn;//

    @ViewInject(R.id.tv_line)
    private TextView tvLine;

    private ListView lvPop;// 下拉选择列表

    @ViewInject(R.id.title_tv)
    private TextView tvTitle;// 标题
    @ViewInject(R.id.searchBtn)
    private ImageView searchBtn;// 选择按钮2016-03-02
    private AreaDataAdapter areaAdapter;// 区域adapter
    private HospitalDataAdapter1 hosAdapter;// 医院adapter
    private PopupWindow pop;// 下拉选择框
    private PopupWindowAdapter popAdapter;
    private View popView;
    private String[] cityNames;
    private String[] cityIds;
    private String[] noopsyches;
    private int flag;// 点击按钮标记（1为城市，2为智能筛选）

    private int selectAreaPos;// 区域选中下标
    private int selectHosPos;// 医院选中下标
    private int selectNPos;// 智能筛选选中下标
    private String strCity;// 选中的城市
    private String city_id;// 选中的城市id
    private String strN;// 智能筛序类型
    private Dialog searchDialog;
    private View searchView;
    // 2016-03-08 搜索用列表
    private HospitalDataAdapter keyWordAdapter;
    private List<Hospital> tempSearchList;
    private EditText etSearchTv;
    private View dialogSearchBtn;
    private String recordCityStr;
    /**
     * 是否预约检查
     */
    private boolean isCheck;
    private boolean isBed = false;
    private boolean isDayRegList;
    private boolean isSearchInit;
    private boolean isChangedCity = false;
    @ViewInject(R.id.downBtn_city)
    private ImageView downBtn_city;
    @ViewInject(R.id.downBtn_choice)
    private ImageView downBtn_choice;

    // private Animation animToDown, animToUp;
    @ViewInject(R.id.layout_tv1)
    private View layout_tv1;
    @ViewInject(R.id.layout_tv2)
    private View layout_tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_hos_dept);
        ViewUtils.inject(ChoiceHosActivity.this);
        savedInstanceState = getIntent().getExtras();
        isCheck = getIntent().getBooleanExtra("isOrderCheck", false);
        isBed = getIntent().getBooleanExtra("isOrderBed", false);
        isDayRegList = getIntent().getBooleanExtra("isDayRegList", false);
        isSearchInit = getIntent().getBooleanExtra("isSearchInit", false);
        strCity = SystemPreferences.getString(EZTConfig.KEY_STR_CITY);
        city_id = SystemPreferences.getString(EZTConfig.KEY_CITY_ID);
        if (null != BaseApplication.cCity) {
            strCity = BaseApplication.cCity.getCityName();
            city_id = String.valueOf(BaseApplication.cCity.getCityId());
        }

        selectNPos = SystemPreferences.getInt(EZTConfig.KEY_SELECT_N_POS, -1);
        strN = SystemPreferences.getString(EZTConfig.KEY_STR_N);//
        selectAreaPos = SystemPreferences.getInt(EZTConfig.KEY_SELECT_AREA_POS,
                -1);
        if(null!=savedInstanceState&&savedInstanceState.containsKey("isChangedCity"))
        if (!savedInstanceState.getBoolean("isChangedCity"))// 如果城市没有被改变取医院的点击位置，如果改变了就取-1
            selectHosPos = SystemPreferences.getInt(
                    EZTConfig.KEY_SELECT_HOS_POS, -1);
        else
            selectHosPos = -1;
        if (isCheck) {
            selectHosPos = -1;
        }
        if (isDayRegList) {
            selectHosPos = -1;
        }
        if (isBed) {
            selectHosPos = -1;
        }
        recordCityStr = BaseApplication.selectCity;
        initialView();
        initialData();

    }

    /**
     * 初始化界面
     */
    private void initialView() {
        tvTitle.setText("选择医院");
        areaAdapter = new AreaDataAdapter(this);
        ltArea.setAdapter(areaAdapter);

        hosAdapter = new HospitalDataAdapter1(this);
        ltHos.setAdapter(hosAdapter);
        layoutBtn.setVisibility(View.VISIBLE);

        popView = LayoutInflater.from(this).inflate(R.layout.popwindow_choice,
                null);
        lvPop = (ListView) popView.findViewById(R.id.pop_list);
        lvPop.setOnItemClickListener(this);
        handler.sendEmptyMessageDelayed(1, 200);
        strN = "智能筛选";
        initSearchDialogView();
    }

    /**
     * 初始化选择dialog
     */
    private void initSearchDialogView() {
        searchDialog = new Dialog(this, R.style.dialog);
        searchView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_hos_choice, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        params.width = ScreenUtils.gainDM(mContext).widthPixels;
        searchDialog.setContentView(searchView, params);
        Window win = searchDialog.getWindow();
        win.setGravity(Gravity.TOP);

        searchDialog.setCanceledOnTouchOutside(true);

        ListView dialog_searchLv = (ListView) searchView
                .findViewById(R.id.dialog_searchLv);
        etSearchTv = (EditText) searchView.findViewById(R.id.hos_search_et);
        dialogSearchBtn = searchView.findViewById(R.id.imageView1);
        dialogSearchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchData(etSearchTv.getText().toString().trim());
            }
        });

        tempSearchList = new ArrayList<Hospital>();
        tempSearchList = new ArrayList<Hospital>();
        keyWordAdapter = new HospitalDataAdapter(mContext);
        dialog_searchLv.setAdapter(keyWordAdapter);
        keyWordAdapter.setList(tempSearchList);
        dialog_searchLv.setOnItemClickListener(hosItemClickListener);

        View backView = searchView.findViewById(R.id.hosSearchBack);
        backView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchDialog.dismiss();
            }
        });
    }

    private OnItemClickListener hosItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            jumpToHosHome(position, keyWordAdapter, true);
        }
    };

    /**
     * 跳转到医院首页
     */
    private void jumpToHosHome(int position, HospitalDataAdapter tempAdapter,
                               boolean isSearchList) {
        if (!BaseApplication.getInstance().isNetConnected) {
            toast(getString(R.string.network_hint), Toast.LENGTH_SHORT);
            return;
        }
        // 选择医院
        tempAdapter.setSelectedPosition(position);
        ltHos.setSelection(position);
        tempAdapter.notifyDataSetChanged();
        if (!isSearchList) {
            if (selectHosPos != position) {
                SystemPreferences.save(EZTConfig.KEY_SELECT_DEPT_POS, position);
            }
            selectHosPos = position;
        }
        String hosName = tempAdapter.getList().get(position).gethName();
        String hosId = tempAdapter.getList().get(position).getId() + "";

        SystemPreferences.save(EZTConfig.KEY_HOS_NAME, hosName);
        SystemPreferences.save(EZTConfig.KEY_HOS_ID, hosId);
        // if (!isSearchList&&!isChangedCity)
        if (!isSearchList && !isDayRegList && !isCheck) {// 不是搜索 不是大牌名医 不是预约检查
            // 不是当日挂号
            SystemPreferences.save(EZTConfig.KEY_SELECT_HOS_POS, selectHosPos);

            SystemPreferences.save(EZTConfig.KEY_STR_CITY, btCity.getText()
                    .toString());
            SystemPreferences.save(EZTConfig.KEY_CITY_ID, city_id);
            SystemPreferences.save(EZTConfig.KEY_SELECT_N_POS, selectNPos);
            SystemPreferences.save(EZTConfig.KEY_STR_N, strN);
            SystemPreferences
                    .save(EZTConfig.KEY_SELECT_AREA_POS, selectAreaPos);
        }
        BaseApplication.selectCity = btCity.getText().toString();
        HomeFragment30.isChangedCity = false;
        Intent intent = null;
        // Intent intent = new Intent(this, ChoiceDeptByHosActivity.class);
        if (null == BaseApplication.patient) {
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }

        if(null==BaseApplication.patient.getEpPid()){
            hintPerfectInfo("请完善个人信息", 1, ChoiceHosActivity.this);
            return;
        }

        if (isCheck) {
            // 2016-03-03 3.0项目点医院跳转到新版医院首页
            intent = new Intent(ChoiceHosActivity.this,
                    ChoiceCheckItemActivity.class);
            intent.putExtra("hospital", tempAdapter.getList().get(position));
        } else if (isBed) {// 20160330如果是预约病床
            intent = new Intent(ChoiceHosActivity.this,
                    ChoiceDeptByHosActivity.class);
            intent.putExtra("hospital", tempAdapter.getList().get(position));
            intent.putExtra("isOrderBed", isBed);
        } else {
            // 2016-03-03 3.0项目点医院跳转到新版医院首页
            intent = new Intent(ChoiceHosActivity.this, HosHomeActivity.class);

            intent.putExtra("hospital", tempAdapter.getList().get(position));
        }
        if (null != intent)
            startActivity(intent);
    }

    /**
     * 跳转到医院首页
     */
    private void jumpToHosHome(int position, HospitalDataAdapter1 tempAdapter,
                               boolean isSearchList) {
        if (!BaseApplication.getInstance().isNetConnected) {
            toast(getString(R.string.network_hint), Toast.LENGTH_SHORT);
            return;
        }
        // 选择医院
        tempAdapter.setSelectedPosition(position);
        ltHos.setSelection(position);
        tempAdapter.notifyDataSetChanged();
        if (!isSearchList) {
            if (selectHosPos != position) {
                SystemPreferences.save(EZTConfig.KEY_SELECT_DEPT_POS, position);
            }
            selectHosPos = position;
        }
        String hosName = tempAdapter.getList().get(position).gethName();
        String hosId = tempAdapter.getList().get(position).getId() + "";

        SystemPreferences.save(EZTConfig.KEY_HOS_NAME, hosName);
        SystemPreferences.save(EZTConfig.KEY_HOS_ID, hosId);
        // if (!isSearchList&&!isChangedCity)
        if (!isSearchList && !isDayRegList && !isCheck) {// 不是搜索 不是大牌名医 不是预约检查
            // 不是当日挂号
            SystemPreferences.save(EZTConfig.KEY_SELECT_HOS_POS, selectHosPos);

            SystemPreferences.save(EZTConfig.KEY_STR_CITY, btCity.getText()
                    .toString());
            SystemPreferences.save(EZTConfig.KEY_CITY_ID, city_id);
            SystemPreferences.save(EZTConfig.KEY_SELECT_N_POS, selectNPos);
            SystemPreferences.save(EZTConfig.KEY_STR_N, strN);
            SystemPreferences
                    .save(EZTConfig.KEY_SELECT_AREA_POS, selectAreaPos);
        }
        BaseApplication.selectCity = btCity.getText().toString();
        HomeFragment30.isChangedCity = false;
        Intent intent = null;
        // Intent intent = new Intent(this, ChoiceDeptByHosActivity.class);
        if (isCheck) {
            if (null == BaseApplication.patient) {
                HintToLogin(Constant.LOGIN_COMPLETE);
                return;
            }
            if(null==BaseApplication.patient.getEpPid()){
                hintPerfectInfo("请完善个人信息", 1, ChoiceHosActivity.this);
                return;
            }
            // 2016-03-03 3.0项目点医院跳转到新版医院首页
            intent = new Intent(ChoiceHosActivity.this,
                    ChoiceCheckItemActivity.class);
            intent.putExtra("hospital", tempAdapter.getList().get(position));
        } else if (isBed) {// 20160330如果是预约病床
            intent = new Intent(ChoiceHosActivity.this,
                    ChoiceDeptByHosActivity.class);
            intent.putExtra("hospital", tempAdapter.getList().get(position));
            intent.putExtra("isOrderBed", isBed);
        } else {
            // 2016-03-03 3.0项目点医院跳转到新版医院首页
            intent = new Intent(ChoiceHosActivity.this, HosHomeActivity.class);

            intent.putExtra("hospital", tempAdapter.getList().get(position));
        }
        if (null != intent)
            startActivity(intent);
    }

    /**
     * 重置搜索数据
     */
    private void searchData(String searchWord) {
        showProgressToast();
        hideSoftInput(etSearchTv);
        if (null == tempSearchList) {
            tempSearchList = new ArrayList<Hospital>();
        } else {
            tempSearchList.clear();
        }
        if (null != hosAdapter.mList)
            for (int i = 0; i < hosAdapter.mList.size(); i++) {
                Hospital hospital = hosAdapter.mList.get(i);
                String hosName = hospital.gethName();
                if (StringUtils.isEmpty(searchWord)) {
                    tempSearchList.add(hospital);
                } else if (hosName.contains(searchWord)) {
                    tempSearchList.add(hospital);
                }
            }

        if (null == keyWordAdapter) {
            keyWordAdapter = new HospitalDataAdapter(mContext);
            ltHos.setAdapter(hosAdapter);
            keyWordAdapter.setList(tempSearchList);
        } else {
            keyWordAdapter.notifyDataSetChanged();
        }
        hideProgressToast();

        if (tempSearchList.size() == 0)
            toast("未搜索到相关医院数据", Toast.LENGTH_SHORT);
    }

    /**
     * 初始化数据
     */
    private void initialData() {
        showProgressToast();
        cityNames = getResources().getStringArray(R.array.appoint_city_name);
        cityIds = getResources().getStringArray(R.array.appoint_city_id);

        noopsyches = new String[]{"全 部", "三甲医院"};
        if (selectNPos != -1) {// 智能筛选赋值
            btNoopsyche.setText(strN);
        }

        String myCity = BaseApplication.selectCity;
        // if (!TextUtils.isEmpty(city_id)) {// 城市赋值
        // btCity.setText(strCity);
        //
        // } else {
        if (!"".equals(myCity)) {
            for (int i = 0; i < cityNames.length; i++) {
                // if (myCity.equals(cityNames[i])) {
                if (myCity.contains(cityNames[i])) {
                    btCity.setText(myCity);// 当前城市
                    city_id = cityIds[i];
                    break;
                } else if (i == cityNames.length - 1) {
//					btCity.setText("全国");
                    if (BaseApplication.cCity != null) {
                        btCity.setText(BaseApplication.cCity.getCityName());
                    }

                }
            }
        } else {
            btCity.setText("全国");// 如果列表没有当前城市,默认选中天津
            city_id = "";
        }
        // }

        if ("全国".equals(btCity.getText().toString())) {
            city_id = "";
            selectAreaPos = -1;
            if (isCheck) {
                getTestHosData("", "", "");
            } else if (isBed) {
                getBedHosData("", "", "");
            } else
                getHosData("", "", "");
            selectDefult();
        } else {
            getAreaData();
        }

    }

    /**
     * 获取区域列表
     *
     * @param //cityid
     */
    private void getAreaData() {
        // HashMap<String, Object> params = new HashMap<String, Object>();
        // CityImpl impl = new CityImpl();
        // params.put("cityid", city_id);

        RequestParams params = new RequestParams();
        CityImpl impl = new CityImpl();
        params.addBodyParameter("cityid", city_id);
        impl.getAreaList(params, this);
    }

    /**
     * @param cityIds
     * @param countyId
     * @param levelId
     */
    private void getBedHosData(String cityIds, String countyId, String levelId) {
        RequestParams params = new RequestParams();
        HospitalImpl imple = new HospitalImpl();
        params.addBodyParameter("cityid", cityIds);
        if (!countyId.equals("")) {
            params.addBodyParameter("countyid", countyId);
        }
        if (!levelId.equals("")) {
            params.addBodyParameter("level", levelId);
        }
        Log.e("tag", "?cityId=" + cityIds + "&levelId=" + levelId
                + "&countryid=" + countyId);
        // toast("cityId "+cityIds+" levelId "+levelId+" countryid "+
        // countyId,Toast.LENGTH_LONG);
        imple.getBedHosList(params, this);

    }

    /**
     * @param cityIds
     * @param countyId
     * @param levelId
     */
    private void getTestHosData(String cityIds, String countyId, String levelId) {
        RequestParams params = new RequestParams();
        HospitalImpl imple = new HospitalImpl();
        params.addBodyParameter("cityid", cityIds);
        if (!countyId.equals("")) {
            params.addBodyParameter("countyid", countyId);
        }
        if (!levelId.equals("")) {
            params.addBodyParameter("level", levelId);
        }
        Log.e("tag", "?cityId=" + cityIds + "&levelId=" + levelId
                + "&countryid=" + countyId);
        // toast("cityId "+cityIds+" levelId "+levelId+" countryid "+
        // countyId,Toast.LENGTH_LONG);
        imple.getTestHosList(params, this);

    }

    /**
     * 获取医院数据
     **/
    private void getHosData(String cityIds, String countyId, String levelId) {
        // HashMap<String, Object> params = new HashMap<String, Object>();
        // HospitalImpl imple = new HospitalImpl();
        // params.put("cityid", cityIds);
        // if (!countyId.equals("")) {
        // params.put("countyid", countyId);
        // }
        // if (!levelId.equals("")) {
        // params.put("level", levelId);
        // }\

        RequestParams params = new RequestParams();
        HospitalImpl imple = new HospitalImpl();
        params.addBodyParameter("cityid", cityIds);
        if (!countyId.equals("")) {
            params.addBodyParameter("countyid", countyId);
        }
        if (!levelId.equals("")) {
            params.addBodyParameter("level", levelId);
        }

        if (isDayRegList) {
            imple.getDayHosList(params, this);
        } else
            imple.getHospitalList(params, this);

    }

    /**
     * 区域选中
     */
    private void selectDefult() {

        if (null != areaAdapter && null != areaAdapter.getList() && areaAdapter.getList().size() > 0) {
            // 数据更新的不及时造成了下标和数据源的不对应，
            // 所以在这里做一个判断，如果下标大于数据源的长度就让下标置为-1，防止数组越界
            if (selectAreaPos > areaAdapter.getList().size() - 1) {
                selectAreaPos = -1;
            }

            if (selectAreaPos == -1) {// 默认全部
                tvAll.setBackgroundColor(getResources().getColor(
                        R.color.choicehoiceAll));
                tvAll.setTextColor(getResources().getColor(android.R.color.white));
                if (isCheck) {
                    getTestHosData(city_id, "", "");
                } else if (isBed) {
                    getBedHosData(city_id, "", "");
                } else
                    getHosData(city_id, "", "");
            } else {// 每个区域下的医院
                tvAll.setBackgroundColor(getResources().getColor(
                        android.R.color.transparent));
                tvAll.setTextColor(getResources()
                        .getColor(R.color.light_main_color));
                String nId = "";
                if (selectNPos == 1) {// 三甲医院
                    nId = "380";
                } else if (selectNPos == 2) {// 离我最近

                }
                if (isCheck) {
                    // nId = "380";//预约检查暂时380，弥补服务器错误
                    getTestHosData(city_id, areaAdapter.getList()
                            .get(selectAreaPos).getAreaId()
                            + "", nId);
                } else if (isBed) {
                    getBedHosData(city_id, areaAdapter.getList().get(selectAreaPos)
                            .getAreaId()
                            + "", nId);
                } else
                    getHosData(city_id, areaAdapter.getList().get(selectAreaPos)
                            .getAreaId()
                            + "", nId);
            }
            areaAdapter.setSelectedPosition(selectAreaPos);
            // ltArea.setSelection(selectAreaPos);
            areaAdapter.notifyDataSetInvalidated();

        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // pop = new PopupWindow(popView, btCity.getWidth(),
            // LayoutParams.MATCH_PARENT, false);
            // pop = new PopupWindow(popView, layout_tv1.getWidth(),
            // LayoutParams.WRAP_CONTENT, false);

            pop = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, false);

            // 实例化一个ColorDrawable颜色为半透明
            // pop.setBackgroundDrawable(new BitmapDrawable());
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            pop.setBackgroundDrawable(dw);
            // pop.setBackgroundDrawable(new BitmapDrawable());
            // 设置点击窗口外边窗口消失
            pop.setOutsideTouchable(true);
            // 设置此参数获得焦点，否则无法点击
            pop.setFocusable(true);
            popAdapter = new PopupWindowAdapter(ChoiceHosActivity.this);
            lvPop.setAdapter(popAdapter);

            pop.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (flag == 1) {
                        btCity.setTextColor(getResources().getColor(
                                R.color.dark_black));
                        // downBtn_city.startAnimation(animToUp);
                        downBtn_city.setImageResource(R.drawable.arrowdown);
                    } else {
                        btNoopsyche.setTextColor(getResources().getColor(
                                R.color.dark_black));
                        // downBtn_choice.startAnimation(animToUp);
                        downBtn_choice.setImageResource(R.drawable.arrowdown);
                    }
                }
            });
        }
    };

    @OnItemClick(R.id.choice_area_lt)
    public void areaItemClick(AdapterView<?> parent, View view, int position,
                              long id) {
        // 选择区域
        selectHosPos = -1;
        selectHosPos = position;
        selectAreaPos = position;
        showProgressToast();
        selectDefult();

    }

    @OnItemClick(R.id.choice_hos_lt)
    public void hosItemClick(AdapterView<?> parent, View view, int position,
                             long id) {
        jumpToHosHome(position, hosAdapter, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (lvPop == parent) {
            // 下拉选择
            if (flag == 1) {// 下拉城市
                if (recordCityStr.equals(popAdapter.getList().get(position))) {
                    isChangedCity = false;
                } else {
                    isChangedCity = true;
                    recordCityStr = popAdapter.getList().get(position);
                }

                btCity.setText(popAdapter.getList().get(position));
                city_id = cityIds[position];
                selectAreaPos = -1;
                selectHosPos = -1;
                if ("全国".equals(btCity.getText().toString())) {// 选中全国
                    if (isCheck) {
                        getTestHosData("", "", "");
                    } else if (isBed) {
                        getBedHosData("", "", "");
                    } else
                        getHosData("", "", "");
                    areaAdapter.setList(new ArrayList<City>());
                    selectDefult();
                } else {
                    getAreaData();
                }
                // downBtn_city.startAnimation(animToUp);
                btCity.setTextColor(getResources().getColor(R.color.main_color));
                downBtn_city.setImageResource(R.drawable.green_arrowup);
            } else {// 智能筛选
                // downBtn_choice.startAnimation(animToUp);
                btNoopsyche.setTextColor(getResources().getColor(
                        R.color.main_color));
                downBtn_choice.setImageResource(R.drawable.green_arrowup);

                selectNPos = position;
                strN = noopsyches[position];
                selectHosPos = -1;

                String nId = "";
                if (selectNPos == 1) {// 三甲医院
                    nId = "380";
                } else if (selectNPos == 2) {// 离我最近

                }

                if (isCheck) {
                    // if(selectAreaPos != -1)
                    // toast("city_id "+city_id+" countryId "+areaAdapter.getList().get(selectAreaPos)
                    // .getAreaId()+" nId " +nId+ " position "+position, 10000);
                    // else
                    // toast("city_id "+city_id+" areId "+" nId " +nId+
                    // " position "+position, 10000);
                    // nId = "380";//预约检查暂时380，弥补服务器错误
                    if (null != areaAdapter.getList()) {
                        if (areaAdapter.getList().size() > 0 && selectAreaPos != -1) {
                            getTestHosData(city_id, selectAreaPos == -1 ? ""
                                    : areaAdapter.getList().get(selectAreaPos)
                                    .getAreaId()
                                    + "", nId);
                        }
                    }
                } else if (isBed) {
                    if (areaAdapter.getList() != null && selectAreaPos != -1) {

                        if (areaAdapter.getList().get(selectAreaPos) != null) {
                            if (null != areaAdapter.getList().get(selectAreaPos).getAreaId()) {
                                getBedHosData(city_id, selectAreaPos == -1 ? ""
                                        : areaAdapter.getList().get(selectAreaPos)
                                        .getAreaId()
                                        + "", nId);
                            } else {
                                getBedHosData(city_id, "", nId);
                            }
//                            getBedHosData(city_id, selectAreaPos == -1 ? ""
//                                    : areaAdapter.getList().get(selectAreaPos)
//                                    .getAreaId()
//                                    + "", nId);
                        }
                    } else
                        getBedHosData(city_id, selectAreaPos == -1 ? "" : areaAdapter
                                .getList().get(selectAreaPos).getAreaId()
                                + "", nId);

                } else
                    getHosData(city_id, selectAreaPos == -1 ? "" : areaAdapter
                            .getList().get(selectAreaPos).getAreaId()
                            + "", nId);
            }
            showProgressToast();

            pop.dismiss();
        }
    }

    @OnClick(R.id.area_item_all)
    public void areaAllClick(View v) {
        // 所有区域
        selectHosPos = -1;
        selectAreaPos = -1;
        showProgressToast();
        selectDefult();
    }

    @OnClick(R.id.layout_tv1)
    public void hosCityClick(View v) {
        // 城市筛选
        flag = 1;
        // toast(pop.isShowing() ? "1" : "0", Toast.LENGTH_SHORT);
        if (pop.isShowing()) {

            pop.dismiss();

        } else {
            popAdapter.setList(cityNames);
            pop.showAsDropDown(v, 0, 0);
            // downBtn_city.startAnimation(animToDown);
            btCity.setTextColor(getResources().getColor(R.color.main_color));
            downBtn_city.setImageResource(R.drawable.green_arrowup);
        }

    }

    @OnClick(R.id.layout_tv2)
    public void hos_noopsycheClick(View v) {
        // toast(pop.isShowing() ? "1" : "0", Toast.LENGTH_SHORT);
        // 智能筛选
        flag = 2;
        if (pop.isShowing()) {
            // downBtn_choice.startAnimation(animToUp);
            pop.dismiss();

        } else {
            popAdapter.setList(noopsyches);
            pop.showAsDropDown(v, 0, 0);
            // downBtn_choice.startAnimation(animToDown);
            btNoopsyche.setTextColor(getResources()
                    .getColor(R.color.main_color));
            downBtn_choice.setImageResource(R.drawable.green_arrowup);
        }
    }

    @OnClick(R.id.choice_hos_close_bt)
    public void hos_closeClick(View v) {
        // 取消
        finish();
    }

    @OnClick(R.id.searchBtn)
    public void choiceBtnClick(View v) {// 搜索按钮弹出弹窗2016-03-02
        if (!searchDialog.isShowing()) {
            searchDialog.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) etSearchTv
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(etSearchTv, 0);
                }

            }, 200);

        } else
            searchDialog.dismiss();
    }

    // @Override
    // public void onClick(View v) {
    // switch (v.getId()) {
    // case R.id.area_item_all:// 所有区域
    // selectHosPos = -1;
    // selectAreaPos = -1;
    // showProgressToast();
    // selectDefult();
    // break;
    // case R.id.choice_hos_city_bt:// 城市筛选
    // flag = 1;
    // if (pop.isShowing()) {
    // pop.dismiss();
    // } else {
    // popAdapter.setList(cityNames);
    // pop.showAsDropDown(v, 0, 0);
    // }
    // break;
    //
    // case R.id.choice_hos_noopsyche_bt:// 智能筛选
    // flag = 2;
    // if (pop.isShowing()) {
    // pop.dismiss();
    // } else {
    // popAdapter.setList(noopsyches);
    // pop.showAsDropDown(v, 0, 0);
    // }
    // break;
    //
    // case R.id.choice_hos_close_bt:// 取消
    // finish();
    // break;
    // }
    //
    // }

    @Override
    public void result(Object... object) {
        int type = (Integer) object[0];
        boolean isSucc = false;
        if (object[1] != null) {
            isSucc = (Boolean) object[1];
        }
        switch (type) {
            case HttpParams.GET_AREAS:// 获取区域列表
                if (isSucc) {
                    ArrayList<City> lists = (ArrayList<City>) object[2];
                    areaAdapter.setList(lists);
                    if (lists != null && lists.size() != 0) {
                        selectDefult();
                    } else {
                        hideProgressToast();
                        tvLine.setVisibility(View.INVISIBLE);
                        ltHos.setVisibility(View.INVISIBLE);
                        ltArea.setVisibility(View.INVISIBLE);
                        tvAll.setVisibility(View.GONE);
                        tvAllHos.setVisibility(View.GONE);
                        lineAllHos.setVisibility(View.GONE);
                    }

                    if (isSearchInit) {
                        if(!ChoiceHosActivity.this.isFinishing())
                        searchDialog.show();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {

                            public void run() {
                                InputMethodManager inputManager = (InputMethodManager) etSearchTv
                                        .getContext().getSystemService(
                                                Context.INPUT_METHOD_SERVICE);
                                inputManager.showSoftInput(etSearchTv, 0);
                            }

                        }, 200);
                        isSearchInit = false;
                    }

                } else {
                    String error = "";
                    if (object[3] != null) {
                        error = (String) object[3];
                    }
                    Toast.makeText(ChoiceHosActivity.this, error + "失败",
                            Toast.LENGTH_SHORT).show();
                    hideProgressToast();
                }
                break;
            case HttpParams.GET_HOS:// 获取医院列表
                if (null != mContext && !ChoiceHosActivity.this.isFinishing())
                    hideProgressToast();
                // 显示区域列表
                tvLine.setVisibility(View.VISIBLE);
                ltHos.setVisibility(View.VISIBLE);
                ltArea.setVisibility(View.VISIBLE);
                tvAll.setVisibility(View.VISIBLE);

                if (isSucc) {
                    tvAllHos.setVisibility(View.VISIBLE);
                    lineAllHos.setVisibility(View.VISIBLE);
                    // hosAdapter.setSelectedPosition(selectHosPos);// 初始化
                    ltHos.setSelection(selectHosPos);
                    if (selectAreaPos == -1) {// 区域列表选择了全部
                        tvAllHos.setText(btCity.getText().toString() + "所有医院");
                        tvAllHos.setVisibility(View.VISIBLE);
                        lineAllHos.setVisibility(View.VISIBLE);
                    } else {// 选择了区域
                        tvAllHos.setVisibility(View.GONE);
                        lineAllHos.setVisibility(View.GONE);
                    }

                    ArrayList<Hospital> hosList = (ArrayList<Hospital>) object[2];
                    ArrayList<Hospital> hosList_special = new ArrayList<Hospital>();
                    hosAdapter.setList(hosList);
                    if (hosList != null && hosList.size() != 0) {
                        ltHos.setVisibility(View.VISIBLE);
                        // searchData("");
                    } else {
                        ltHos.setVisibility(View.INVISIBLE);
                    }

                    if (isSearchInit) {
                        if (!ChoiceHosActivity.this.isFinishing())
                            searchDialog.show();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {

                            public void run() {
                                InputMethodManager inputManager = (InputMethodManager) etSearchTv
                                        .getContext().getSystemService(
                                                Context.INPUT_METHOD_SERVICE);
                                inputManager.showSoftInput(etSearchTv, 0);
                            }

                        }, 200);
                        isSearchInit = false;
                    }

                } else {
                    String error = (String) object[3];
                    Toast.makeText(ChoiceHosActivity.this, error + "失败",
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    // @Override
    // public void onClick(View v) {
    // if (dialogSearchBtn == v) {
    // searchData(etSearchTv.getText().toString().trim());
    // }
    // }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressToast();
    }

    private class HospitalDataAdapter1 extends BaseArrayListAdapter<Hospital> {
        // private final int NORMAL=1;//(正常，非对接状态) 2015-12-17
        private final int WARING = 0;// (警告，医院为对接状态对接状态) 2015-12-17
        private int selectedPosition = -1;

        public HospitalDataAdapter1(Activity context) {
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
                convertView = View.inflate(mContext, R.layout.item_hospital1,
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
                // holder.hosName.setTextColor(mContext.getResources().getColor(
                // R.color.main_color));
                holder.hosName.setTextColor(mContext.getResources().getColor(
                        R.color.dark_black));
                holder.layout.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.choicehoiceAll));
            } else {
                holder.hosName.setTextColor(mContext.getResources().getColor(
                        R.color.dark_black));
                // holder.layout.setBackgroundColor(mContext.getResources().getColor(
                // android.R.color.white));
                holder.layout.setBackgroundColor(Color.TRANSPARENT);
            }
            return convertView;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

    }

}
