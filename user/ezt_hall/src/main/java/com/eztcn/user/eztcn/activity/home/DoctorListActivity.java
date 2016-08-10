package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.CityCommonAdapter;
import com.eztcn.user.eztcn.adapter.Dept2DataAdapter;
import com.eztcn.user.eztcn.adapter.DeptDataAdapter;
import com.eztcn.user.eztcn.adapter.DoctorListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.PinyinComparatorDept;

;

/**
 * @title 医生列表
 * @describe （按医生找、按科室找）
 * @author ezt
 * @created 2014年12月10日
 */
public class DoctorListActivity extends FinalActivity implements
		OnClickListener, IHttpResult, OnLoadMoreListener, OnRefreshListener,
		OnItemClickListener {
	private ListView cityLv;
	@ViewInject(R.id.docs_lv)
	// , itemClick = "onItemClick"
	private PullToRefreshListView lv;// 医生列表

	@ViewInject(R.id.docs_choice_tv2)
	// , click = "onClick"
	// 第二个筛选按钮
	public TextView tvChoice2;//

	@ViewInject(R.id.docs_choice_tv3)
	// , click = "onClick"
	// 第三个筛选按钮
	public TextView tvChoice3;//

	@ViewInject(R.id.docs_choice_tv1)
	// , click = "onClick"
	// 第一个筛选按钮
	public TextView tvChoice1;//

	@ViewInject(R.id.line2)
	public View Line2;

	@ViewInject(R.id.bt_layout)
	public LinearLayout layoutBt;//

	private DoctorListAdapter adapter;

	public int fromType;// 1为按医院找，2为按科室找
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
	private final String HOS_DOC_LIST_DATA = "Hos_DocListData";// 缓存key-医院找
	private final String DEPT_DOC_LIST_DATA = "Dept_DocListData";// 缓存key-科室找

	private final String HOS_DOC_WB_LIST_DATA = "Hos_DocWBListData";// 缓存key-医院找
	private final String DEPT_DOC_WB_LIST_DATA = "Dept_DocWBListData";// 缓存key-科室找
	private CacheUtils mCache;
	private Integer pDoctorPosition;

	private boolean isChoice = false;// 是否选择医院（专家看病历）

	private boolean isWB = false;
	/**
	 * 特殊医院需求：泰达心血管病医院，确认预约挂号页面没有初复诊选项，特殊需求该医院：复诊时需要填写复诊病案号
	 */
	public static int hospatialId;
	private boolean isAllSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctorlist);
		ViewUtils.inject(DoctorListActivity.this);
		gainIntent();
		mCache = CacheUtils.get(this);
		fromType = getIntent().getIntExtra("type", 0);
		isChoice = getIntent().getBooleanExtra("isChoice", false);
		isAllSearch = getIntent().getBooleanExtra("isAllSearch", false);// 是否为全站搜索进入

		switch (fromType) {
		case 1:// 医院找
			hosName = getIntent().getStringExtra("hosName");
			loadTitleBar(true, hosName, null);
			if (isAllSearch) {// 全站搜索跳转传值
				hosId = getIntent().getStringExtra("hosId");
				deptId = getIntent().getStringExtra("deptId");
				layoutBt.setVisibility(View.VISIBLE);

			} else {// 快速预约进入
				if (!isWB) {
					hosId = SystemPreferences.getString(EZTConfig.KEY_HOS_ID);
					deptId = SystemPreferences.getString(EZTConfig.KEY_DEPT_ID);
				} 
//				else {
//					hosId = SystemPreferences
//							.getString(EZTConfig.KEY_WB_HOS_ID);
//					deptId = SystemPreferences
//							.getString(EZTConfig.KEY_DEPT_WB_ID);
//				}
			}
			break;

		case 2:// 科室找
			loadTitleBar(true, "按科室找", null);
			Line2.setVisibility(View.VISIBLE);
			tvChoice1.setVisibility(View.VISIBLE);
			deptId = getIntent().getStringExtra("deptId");
			deptTypeId = getIntent().getStringExtra("deptTypeId");
			hosId = getIntent().getStringExtra("hosId");
			layoutBt.setVisibility(View.VISIBLE);

			// 初始化下拉框
			initialOneBtPop();

			break;
		default:
			finish();
			break;
		}
		initialTwoBtPop();
		initialThreeBtPop();
		deptName = getIntent().getStringExtra("deptName");
		tvChoice2.setText(TextUtils.isEmpty(deptName) ? "选择科室" : deptName);
		tvChoice3.setText("智能筛选");

		if ((fromType == 1 || fromType == 2) && !isAllSearch) {// 城市选择赋值
//			if (isWB) {
//				cityId = SystemPreferences.getString(EZTConfig.KEY_CITY_WB_ID);
//			} else {
				cityId = SystemPreferences.getString(EZTConfig.KEY_CITY_ID);
//			}
			if (!isWB) {
				if (TextUtils.isEmpty(SystemPreferences
						.getString(EZTConfig.KEY_STR_CITY))) {// 第一次进入医生列表(城市选择在外部如果有选择城市)
					String[] cityNames = getResources().getStringArray(
							R.array.appoint_city_name);
					String[] cityIds = getResources().getStringArray(
							R.array.appoint_city_id);
					String myCity = BaseApplication.selectCity;
					if (!TextUtils.isEmpty(myCity)) {
						for (int i = 0; i < cityNames.length; i++) {
							if (myCity.equals(cityNames[i])) {
								cityId = cityIds[i];
								break;
							} else if (i == cityNames.length - 1) {
								cityId = "";
							}
						}
					} else {
						cityId = "";
					}
				}
			} 
//			else {
//				if (TextUtils.isEmpty(SystemPreferences
//						.getString(EZTConfig.KEY_STR_WB_CITY))) {// 第一次进入医生列表(城市选择在外部如果有选择城市)
//					String[] cityNames = getResources().getStringArray(
//							R.array.appoint_city_name);
//					String[] cityIds = getResources().getStringArray(
//							R.array.appoint_city_id);
//					String myCity = BaseApplication.selectCity;
//					if (!TextUtils.isEmpty(myCity)) {
//						for (int i = 0; i < cityNames.length; i++) {
//							if (myCity.equals(cityNames[i])) {
//								cityId = cityIds[i];
//								break;
//							} else if (i == cityNames.length - 1) {
//								cityId = "";
//							}
//						}
//					} else {
//						cityId = "";
//					}
//				}
//			}

		}
		adapter = new DoctorListAdapter(this);
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
			initialData();
		} else {// 无网络
			ArrayList<Doctor> cacheData;
			if (fromType == 1) {
				if (!isWB) {
					cacheData = (ArrayList<Doctor>) mCache
							.getAsObject(HOS_DOC_LIST_DATA);
				} else {
					cacheData = (ArrayList<Doctor>) mCache
							.getAsObject(HOS_DOC_WB_LIST_DATA);
				}
			} else {
				if (!isWB) {
					cacheData = (ArrayList<Doctor>) mCache
							.getAsObject(DEPT_DOC_LIST_DATA);
				} else {
					cacheData = (ArrayList<Doctor>) mCache
							.getAsObject(DEPT_DOC_WB_LIST_DATA);
				}
			}
			adapter.setList(cacheData);
			if (adapter.getList() != null && adapter.getList().size() != 0) {
				lv.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void gainIntent() {
		Intent intentFrom = getIntent();

		fromType = intentFrom.getIntExtra("type", 0);
		isChoice = intentFrom.getBooleanExtra("isChoice", false);
		isAllSearch = intentFrom.getBooleanExtra("isAllSearch", false);// 是否为全站搜索进入
		if (null != intentFrom && intentFrom.getIntExtra("isWomenBaby", 0) == 1) {
			isWB = true;

		} else {
			isWB = false;
		}

	}

	@Override
	public void onBackPressed() {
		hideProgressToast();
		super.onBackPressed();
	}

	/**
	 * 初始化科室选择下拉框(按科室找)/初始化科室选择下拉框(按医院找)
	 */
	// 选择科室下拉框
	private DeptDataAdapter adapterType;// 科室类型adapter
	private Dept2DataAdapter adapterDept;// 科室adapter
	private ListView ltDept;// 大科室分类
	private ListView ltDept2;// 小科室分类
	private final String LEVEL = "1";// 科室分类级别（1大分类，2小分类）
	private int selectDeptPos;// 选择科室大分类下标
	private int selectDept2Pos = -1;// 选择科室小分类下标
	private CharacterParser characterParser;// 汉字转换成拼音的类
	private TextView tvLine;
	private LinearLayout body_layout, layoutLeft;
	private View deptView;

	private void initialTwoBtPop() {
		deptView = LayoutInflater.from(this).inflate(R.layout.pop_choice_dept,
				null);
		body_layout = (LinearLayout) deptView.findViewById(R.id.body_layout);
		layoutLeft = (LinearLayout) deptView.findViewById(R.id.left_layout);
		tvLine = (TextView) deptView.findViewById(R.id.tv_line);
		ltDept = (ListView) deptView.findViewById(R.id.choice_area_lt);

		ltDept.setOnItemClickListener(this);// 科室一級列表

		ltDept2 = (ListView) deptView.findViewById(R.id.choice_hos_lt);
		ltDept2.setOnItemClickListener(this);// 科室二级列表

		adapterDept = new Dept2DataAdapter(this, true);
		ltDept2.setAdapter(adapterDept);
		adapterType = new DeptDataAdapter(this, true);
		ltDept.setAdapter(adapterType);
	}

	/**
	 * 智能筛选
	 */
//	private CheckBox cbNum;// 是否有号
//	private CheckBox cbThreeHos;// 是否三家医院
//	private CheckBox cbRate;// 预约率高到低
//	private CheckBox cbLevel;// 医生级别
//	private CheckBox cbEvaluate;// 评价高低
	private Button btPop;
	private View noopsycheView;
	private FrameLayout layoutThreeHos;

	private void initialThreeBtPop() {
		noopsycheView = LayoutInflater.from(this).inflate(
				R.layout.popwindows_noopsyche, null);
//		cbNum = (CheckBox) noopsycheView
//				.findViewById(R.id.noopsyche_appoint_cb);
//		cbRate = (CheckBox) noopsycheView.findViewById(R.id.noopsyche_rate_cb);
//		cbLevel = (CheckBox) noopsycheView
//				.findViewById(R.id.noopsyche_level_cb);
//		cbEvaluate = (CheckBox) noopsycheView
//				.findViewById(R.id.noopsyche_evaluate_cb);
//		cbThreeHos = (CheckBox) noopsycheView
//				.findViewById(R.id.noopsyche_threehos_cb);
		layoutThreeHos = (FrameLayout) noopsycheView
				.findViewById(R.id.noopsyche_threehos_layout);

		if (fromType == 2) {// 按科室找可筛选是否为三甲医院
			layoutThreeHos.setVisibility(View.VISIBLE);
		}
		btPop = (Button) noopsycheView.findViewById(R.id.pop_bt);
		btPop.setOnClickListener(this);
	}

	/**
	 * 城市筛选（按科室找需要显示）
	 */
	private LinearLayout cityLayout;
	private CityCommonAdapter cityAdapter;

	private void initialOneBtPop() {
		String cityName = "";
		if (!isWB) {
			cityName = SystemPreferences.getString(EZTConfig.KEY_STR_CITY);
		} 
//		else {
//			cityName = SystemPreferences.getString(EZTConfig.KEY_STR_WB_CITY);
//		}
		if (!TextUtils.isEmpty(cityName)) {
			tvChoice1.setText(cityName);
		} else {
			tvChoice1.setText("全国");
		}
		// tvChoice1
		cityLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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

	/**
	 * 弹出下拉
	 * 
	 * @param view
	 * @param flag
	 */
	private PopupWindow choicePop;// 下拉弹出框

	private void initialChoicePopwindow(final View view, int flag) {

		choicePop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, false);
		choicePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {

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
		switch (flag) {
		case 1:

			break;

		case 2:// 科室选择 (科室找)/(医院找)根据医院id获取科室列表
			body_layout.getLayoutParams().height = getWindowHeight() / 2;
			// 实例化汉字转拼音类
			characterParser = CharacterParser.getInstance();
			if (fromType == 1) {// 按医院找
				getDeptTypeOfHosId(hosId);
			} else {// 按按科室找
				layoutLeft.setVisibility(View.VISIBLE);
				if (!isWB) {
					selectDeptPos = SystemPreferences.getInt(
							EZTConfig.KEY_DF_SELECT_DEPT_POS, -1);
					selectDept2Pos = SystemPreferences.getInt(
							EZTConfig.KEY_DF_SELECT_DEPT2_POS, -1);
				}
//				else {
//					selectDeptPos = SystemPreferences.getInt(
//							EZTConfig.KEY_DF_WB_SELECT_DEPT_POS, -1);
//					selectDept2Pos = SystemPreferences.getInt(
//							EZTConfig.KEY_DF_WB_SELECT_DEPT2_POS, -1);
//				}
				// showProgressToast();
				getDeptTypes();
			}
			break;
		case 3:
			break;
		}

	}

	/**
	 * 数据
	 */
	private void initialData() {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// HospitalImpl impl = new HospitalImpl();
		// params.put("orderLevel", isLevel ? "1" : "0");// 是否按职务排行
		// params.put("orderRate", isRate ? "1" : "0");// 是否按预约率排序
		// params.put("orderYnRemain", isNum ? "1" : "0");// 是否有号
		// params.put("orderYnEvaluation", isEvaluate ? "1" : "0");// 按评价高低
		// // private boolean isThreeHos=false;
		// params.put("dcOrderParm", "6");// 1热门2新闻页3名医堂4最受欢迎5妇婴名医6本院推荐
		// params.put("cityId", cityId);// 城市id
		// if (fromType != 2) {
		// params.put("hospitalId", hosId);// 医院id
		// }
		// params.put("deptCateId", deptTypeId);// 小科室分类id（无医院id 获取）
		// params.put("deptId", deptId);// 医院的科室id（根据医院id 获取）
		// // ps:deptCateId 和 deptId 独立
		// params.put("rowsPerPage", pageSize + "");
		// params.put("page", currentPage + "");
		// params.put("sourcePfId", sourcePfId);

		RequestParams params = new RequestParams();
		HospitalImpl impl = new HospitalImpl();
		params.addBodyParameter("orderLevel", isLevel ? "1" : "0");// 是否按职务排行
		params.addBodyParameter("orderRate", isRate ? "1" : "0");// 是否按预约率排序
		params.addBodyParameter("orderYnRemain", isNum ? "1" : "0");// 是否有号
		params.addBodyParameter("orderYnEvaluation", isEvaluate ? "1" : "0");// 按评价高低
		// private boolean isThreeHos=false;
		params.addBodyParameter("dcOrderParm", "6");// 1热门2新闻页3名医堂4最受欢迎5妇婴名医6本院推荐
		params.addBodyParameter("cityId", cityId);// 城市id
		if (fromType != 2) {
			params.addBodyParameter("hospitalId", hosId);// 医院id
		}
		params.addBodyParameter("deptCateId", deptTypeId);// 小科室分类id（无医院id 获取）
		params.addBodyParameter("deptId", deptId);// 医院的科室id（根据医院id 获取）
		// ps:deptCateId 和 deptId 独立
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		params.addBodyParameter("sourcePfId", sourcePfId);

		impl.getRankingDocList(params, this);
	}

	/**
	 * 电话医生按钮点击事件
	 */
	// @Override
	// public void telDoctorClick(int position) {
	// if (adapter == null) {
	// return;
	// }
	// if (adapter.getList() == null) {
	// return;
	// }
	// if (BaseApplication.eztUser == null) {
	// HintToLogin(11);
	// return;
	// }
	//
	// pDoctorPosition = position;
	// checkTelDocState(adapter.getList().get(position).getId());
	//
	// }

	/**
	 * 查看医生是否开通电话医生服务、资费情况、是否在线、是否可预约
	 * 
	 * @param docId
	 */
	private void checkTelDocState(String docId) {
		// HashMap<String, Object> param = new HashMap<String, Object>();
		// param.put("doctorId", docId);

		RequestParams param = new RequestParams();
		param.addBodyParameter("doctorId", "" + docId);
		new TelDocImpl().checkTelDocState(param, this);
		showProgressToast();
	}

	private boolean isNum = false;
	private boolean isThreeHos = false;
	private boolean isRate = false;
	private boolean isLevel = false;
	private boolean isEvaluate = false;

	// @OnClick(R.id.pop_bt)
	// public void pop_btClick(View v) {
	//
	// if (BaseApplication.getInstance().isNetConnected) {
	//
	// // 智能筛选确定
	// isNum = cbNum.isChecked();// 是否有号
	// isThreeHos = cbThreeHos.isChecked();// 是否三家医院
	// isRate = cbRate.isChecked();// 预约率高到低
	// isLevel = cbLevel.isChecked();// 医生级别高低
	// isEvaluate = cbEvaluate.isChecked();// 评价高低
	// choicePop.dismiss();
	// getData();
	//
	// } else {// 无网络
	// Toast.makeText(mContext, getString(R.string.network_hint),
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	@OnClick(R.id.docs_choice_tv2)
	public void docs_choice_tv2Click(View v) {

		if (BaseApplication.getInstance().isNetConnected) {

			// 科室分类选择
			initialChoicePopwindow(deptView, 2);// 科室（科室筛选进入）/ 医院（医院筛选进入）
			if (choicePop != null) {
				if (!choicePop.isShowing()) {
					choicePop.showAsDropDown(v, 0, 0);
				} else {
					choicePop.dismiss();
				}
			}

		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	@OnClick(R.id.docs_choice_tv3)
	public void docs_choice_tv3Click(View v) {

		if (BaseApplication.getInstance().isNetConnected) {

			// 智能筛选
			initialChoicePopwindow(noopsycheView, 3);
			if (choicePop != null) {
				if (!choicePop.isShowing()) {
					choicePop.showAsDropDown(v, 0, 0);
				} else {
					choicePop.dismiss();
				}
			}

		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	@OnClick(R.id.docs_choice_tv1)
	public void docs_choice_tv1Click(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
			// 选择城市（按科室找）
			initialChoicePopwindow(cityLayout, 1);
			if (choicePop != null) {
				if (!choicePop.isShowing()) {
					choicePop.showAsDropDown(v, 0, 0);
				} else {
					choicePop.dismiss();
				}
			}

		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {

		if (BaseApplication.getInstance().isNetConnected) {
			switch (v.getId()) {
			case R.id.pop_bt:// 智能筛选确定
//				isNum = cbNum.isChecked();// 是否有号
//				isThreeHos = cbThreeHos.isChecked();// 是否三家医院
//				isRate = cbRate.isChecked();// 预约率高到低
//				isLevel = cbLevel.isChecked();// 医生级别高低
//				isEvaluate = cbEvaluate.isChecked();// 评价高低
				choicePop.dismiss();
				getData();
				break;
			//
			// case R.id.docs_choice_tv2:// 科室分类选择
			// initialChoicePopwindow(deptView, 2);// 科室（科室筛选进入）/ 医院（医院筛选进入）
			// if (choicePop != null) {
			// if (!choicePop.isShowing()) {
			// choicePop.showAsDropDown(v, 0, 0);
			// } else {
			// choicePop.dismiss();
			// }
			// }
			// break;
			//
			// case R.id.docs_choice_tv3:// 智能筛选
			// initialChoicePopwindow(noopsycheView, 3);
			// if (choicePop != null) {
			// if (!choicePop.isShowing()) {
			// choicePop.showAsDropDown(v, 0, 0);
			// } else {
			// choicePop.dismiss();
			// }
			// }
			// break;
			//
			// case R.id.docs_choice_tv1:// 选择城市（按科室找）
			// initialChoicePopwindow(cityLayout, 1);
			// if (choicePop != null) {
			// if (!choicePop.isShowing()) {
			// choicePop.showAsDropDown(v, 0, 0);
			// } else {
			// choicePop.dismiss();
			// }
			// }
			//
			// break;
			}
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取大科室分类列表
	 */
	private void getDeptTypes() {// level=1
	// HashMap<String, Object> params = new HashMap<String, Object>();
	// HospitalImpl impl = new HospitalImpl();
	// params.put("level", LEVEL);// 分类级别
	// impl.getBigDeptList(params, this);
		RequestParams params = new RequestParams();
		HospitalImpl impl = new HospitalImpl();
		params.addBodyParameter("level", LEVEL);// 分类级别
		impl.getBigDeptList(params, this);
		showProgressToast();
	}

	/**
	 * 根据大科室分类id获取小科室分类列表
	 */
	private void getDeptList(String selectDeptTypeId) {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// HospitalImpl impl = new HospitalImpl();
		// params.put("pid", selectDeptTypeId);

		RequestParams params = new RequestParams();
		HospitalImpl impl = new HospitalImpl();
		params.addBodyParameter("pid", selectDeptTypeId);
		impl.getDeptList(params, this);
	}

	/**
	 * 根据医院id获取科室分类列表
	 */
	private void getDeptTypeOfHosId(String hosId) {// hospitalId=18&level=1
	// HashMap<String, Object> params = new HashMap<String, Object>();
	// HospitalImpl impl = new HospitalImpl();
	// params.put("hospitalId", hosId);
	// impl.getDeptList2(params, this);

		RequestParams params = new RequestParams();
		HospitalImpl impl = new HospitalImpl();
		params.addBodyParameter("hospitalId", hosId);
		impl.getDeptList2(params, this);

		showProgressToast();
	}

	/**
	 * 选择大科室分类
	 * 
	 * @param position
	 */
	private void selectDeptTypeBig(int position) {
		ltDept.setSelection(position);
		adapterType.setSelectedPosition(position);
		adapterType.notifyDataSetChanged();
		String selectDeptTypeId = adapterType.getList().get(position).getId()
				+ "";
		getDeptList(selectDeptTypeId);
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_BIG_DEPT:// 分类大科室
			if (isSucc) {
				ArrayList<Dept> deptList = (ArrayList<Dept>) object[2];
				for (int i = 0; i < deptList.size(); i++) {
					Dept sortModel = new Dept();
					String dName = deptList.get(i).getdName();
					sortModel.setdName(dName);
					sortModel.setId(deptList.get(i).getId());
					// 汉字转换成拼音
					String pinyin = characterParser.getSelling(dName);
					String sortString = pinyin.substring(0, 1).toUpperCase();

					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						sortModel.setSortLetters(sortString.toUpperCase());
					} else {
						sortModel.setSortLetters("#");
					}
					deptList.set(i, sortModel);
					// mSortList.add(sortModel);
				}

				// 根据a-z进行排序源数据
				Collections.sort(deptList, new PinyinComparatorDept());
				adapterType.setList(deptList);

				if (deptList != null) {
					if (deptList.size() != 0) {
						tvLine.setVisibility(View.VISIBLE);
						ltDept.setVisibility(View.VISIBLE);
						if (selectDeptPos != -1) {// 已有操作
							selectDeptTypeBig(selectDeptPos);
						} else {
							selectDeptTypeBig(0);// 默认选中第一个
						}
					} else {
						tvLine.setVisibility(View.INVISIBLE);
						ltDept.setVisibility(View.INVISIBLE);
					}
				} else {

					Logger.i("科室分类", "数据为null");
				}

			} else {
				Logger.i("科室分类", object[3]);
			}
			break;

		case HttpParams.GET_DEPT_LIST:// 分类小科室列表
			hideProgressToast();
			if (isSucc) {
				ArrayList<Dept> deptList = (ArrayList<Dept>) object[2];
				if (deptList != null) {
					if (deptList.size() != 0) {
						for (int i = 0; i < deptList.size(); i++) {
							Dept sortModel = new Dept();
							String dName = deptList.get(i).getdName();
							sortModel.setdName(dName);
							sortModel.setId(deptList.get(i).getId());
							// 汉字转换成拼音
							String pinyin = characterParser.getSelling(dName);
							String sortString = pinyin.substring(0, 1)
									.toUpperCase();

							// 正则表达式，判断首字母是否是英文字母
							if (sortString.matches("[A-Z]")) {
								sortModel.setSortLetters(sortString
										.toUpperCase());
							} else {
								sortModel.setSortLetters("#");
							}
							deptList.set(i, sortModel);
							// mSortList.add(sortModel);
						}

						// 根据a-z进行排序源数据
						Collections.sort(deptList, new PinyinComparatorDept());
						adapterDept.setList(deptList);
						ltDept2.setVisibility(View.VISIBLE);
						// tvAll.setVisibility(View.VISIBLE);
						ltDept2.setSelection(selectDept2Pos);
						adapterDept.setSelectedPosition(selectDept2Pos);
						adapterDept.notifyDataSetChanged();

					} else {
						ltDept2.setVisibility(View.INVISIBLE);
						// tvAll.setVisibility(View.GONE);
					}
				} else {//
					ltDept2.setVisibility(View.INVISIBLE);
					// tvAll.setVisibility(View.GONE);
				}

			} else {
				Logger.i("科室列表", object[3]);
			}

			break;

		case HttpParams.GET_DEPT_LIST2:// 获取医院科室列表（根据医院id）
			hideProgressToast();
			if (isSucc) {
				// ArrayList<Dept> deptList = (ArrayList<Dept>) object[2];

				// if (deptList == null) {2015-12-16 对接接口
				// deptList = new ArrayList<Dept>();
				// }
				// if (deptList.size() == 0) {2015-12-16 对接接口
				// Toast.makeText(this, "暂无可预约科室", Toast.LENGTH_SHORT).show();
				// }
				ArrayList<Dept> deptList = null;
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map.containsKey("deptList")) {
					deptList = (ArrayList<Dept>) map.get("deptList");
				} else {
					String msgStr = "暂无可预约科室";
					if (map.containsKey("msg")) {
						msgStr = String.valueOf(map.get("msg"));
					}
					Toast.makeText(this, msgStr, Toast.LENGTH_SHORT).show();
				}
				if (null != deptList) {// 2015-12-16 对接接口
					for (int i = 0; i < deptList.size(); i++) {
						Dept sortModel = new Dept();
						String dName = deptList.get(i).getdName();
						sortModel.setdName(dName);
						sortModel.setId(deptList.get(i).getId());
						// 汉字转换成拼音
						String pinyin = characterParser.getSelling(dName);
						String sortString = pinyin.substring(0, 1)
								.toUpperCase();

						// 正则表达式，判断首字母是否是英文字母
						if (sortString.matches("[A-Z]")) {
							sortModel.setSortLetters(sortString.toUpperCase());
						} else {
							sortModel.setSortLetters("#");
						}
						deptList.set(i, sortModel);
						// mSortList.add(sortModel);
					}
					// 根据a-z进行排序源数据
					Collections.sort(deptList, new PinyinComparatorDept());
					ltDept2.setVisibility(View.VISIBLE);
					adapterDept.setList(deptList);

					int pos = -1;
					if (!isWB) {
						pos = SystemPreferences.getInt(
								EZTConfig.KEY_SELECT_DEPT_POS, -1);
					} 
					
//					else {
//						pos = SystemPreferences.getInt(
//								EZTConfig.KEY_SELECT_WB_DEPT_POS, -1);
//					}
					if (pos != -1 && !isAllSearch) {
						ltDept2.setSelection(pos);
						adapterDept.setSelectedPosition(pos);
						adapterDept.notifyDataSetChanged();
					}
				}
			} else {
				Logger.i("医院科室列表", object[3]);
			}

			break;

		case HttpParams.GET_RANKING_DOC_LIST:// 获取医生列表

			if (isSucc) {
				Map<String,Object> map;
				ArrayList<Doctor> data = null;
//				docList = (ArrayList<Doctor>) object[2]; // 2015-12-26 接口對接
				map=(Map<String, Object>) object[2];
				if(map.containsKey("docList")){
					docList=(ArrayList<Doctor>) map.get("docList");
				}
				hideProgressToast();
				if (docList != null && docList.size() > 0) {
					if (currentPage == 1) {// 第一次加载或刷新
						data = docList;
						adapter.resetStateValue();
						if (docList.size() < pageSize) {
							lv.setAutoLoadMore(false);
							lv.onLoadMoreComplete();
						}
						lv.onRefreshComplete();
						if (!isWB) {
							if (fromType == 1) {// 医院找
								mCache.put(HOS_DOC_LIST_DATA, docList);
							} else {// 科室找
								mCache.put(DEPT_DOC_LIST_DATA, docList);
							}

						} else {
							if (fromType == 1) {// 医院找
								mCache.put(HOS_DOC_WB_LIST_DATA, docList);
							} else {// 科室找
								mCache.put(DEPT_DOC_WB_LIST_DATA, docList);
							}

						}

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
		// case HttpParams.CHECK_TEL_DOC_STATE:// 获取医生状态
		// Map<String, Object> map = (Map<String, Object>) object[2];
		// if (map == null || map.size() == 0) {
		// Toast.makeText(getApplicationContext(), "该医生暂未开启电话服务",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// boolean flag = (Boolean) map.get("flag");
		// if (flag) {// 成功
		// TelDocState state = (TelDocState) map.get("data");
		// if (judgeTelStatus(state)) {
		//
		// startActivity(new Intent(mContext,
		// PhoneDoctorActivity.class).putExtra("doc", adapter
		// .getList().get(pDoctorPosition)));
		// }
		//
		// } else {
		// Toast.makeText(getApplicationContext(), "该医生暂未开启电话服务",
		// Toast.LENGTH_SHORT).show();
		// }
		// break;
		default:
			break;
		}

	}

	@OnItemClick(R.id.docs_lv)
	public void docs_lvItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (BaseApplication.getInstance().isNetConnected) {
			// 医生列表
			position = position - 1;
			String docId = adapter.getList().get(position).getId();
			String DeptId = adapter.getList().get(position).getDocDeptId();
			if (!isChoice) {

				String deptDocId = adapter.getList().get(position)
						.getDeptDocId();
				//2015-15-18 医院对接
				int ehDockingStatus=adapter.getList().get(position).getEhDockingStatus();

				Intent intent = new Intent(DoctorListActivity.this,
						DoctorIndexActivity.class)
						.putExtra("deptId", DeptId)
						.putExtra("docId", docId)
						.putExtra("deptDocId", deptDocId).putExtra("ehDockingStatus",ehDockingStatus);
				startActivity(intent);
			} else {// 选择医生
				String docName = adapter.getList().get(position).getDocName();
				String DeptName = adapter.getList().get(position).getDocDept();
				String hosName = adapter.getList().get(position).getDocHos();
				String hosId = adapter.getList().get(position).getDocHosId();

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
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (parent.getId()) {
		case R.id.choice_area_lt:// 分类大科室
			selectDeptPos = position;
			selectDept2Pos = -1;
			selectDeptTypeBig(position);
			showProgressToast();
			break;
		case R.id.choice_hos_lt:// 分类小科室/医院科室

			if (fromType == 1) {// 医院找
				Dept dept = adapterDept.getList().get(position);
				String dName = dept.getdName();
				this.deptId = dept.getId() + "";
				if (!isAllSearch) {
					if (!isWB) {
						SystemPreferences.save(EZTConfig.KEY_DEPT_ID,
								this.deptId);
						SystemPreferences.save(EZTConfig.KEY_STR_DEPT, dName);
						SystemPreferences.save(EZTConfig.KEY_SELECT_DEPT_POS,
								position);
					} 
//					else {
//						SystemPreferences.save(EZTConfig.KEY_DEPT_WB_ID,
//								this.deptId);
//						SystemPreferences
//								.save(EZTConfig.KEY_STR_WB_DEPT, dName);
//						SystemPreferences.save(
//								EZTConfig.KEY_SELECT_WB_DEPT_POS, position);
//					}
				}
				tvChoice2.setText(dName);
			} else {// 科室找
				selectDept2Pos = position;
				adapterDept.setSelectedPosition(position);
				adapterDept.notifyDataSetChanged();
				this.deptTypeId = adapterDept.getList().get(position).getId()
						+ "";
				String deptName = adapterDept.getList().get(position)
						.getdName();
				tvChoice2.setText(deptName);
				if (!isWB) {
					SystemPreferences.save(EZTConfig.KEY_DF_DEPT_ID,
							this.deptTypeId);
					SystemPreferences.save(EZTConfig.KEY_DF_STR_DEPT, deptName);
					SystemPreferences.save(EZTConfig.KEY_DF_SELECT_DEPT2_POS,
							selectDept2Pos);
					SystemPreferences.save(EZTConfig.KEY_DF_SELECT_DEPT_POS,
							selectDeptPos);

				} 
//				else {
//					SystemPreferences.save(EZTConfig.KEY_DF_WB_DEPT_ID,
//							this.deptTypeId);
//					SystemPreferences.save(EZTConfig.KEY_DF_WB_STR_DEPT,
//							deptName);
//					SystemPreferences.save(
//							EZTConfig.KEY_DF_WB_SELECT_DEPT2_POS,
//							selectDept2Pos);
//					SystemPreferences.save(EZTConfig.KEY_DF_WB_SELECT_DEPT_POS,
//							selectDeptPos);
//				}
			}
			choicePop.dismiss();
			getData();

			break;

		// case R.id.docs_lv:// 医生列表
		// if (BaseApplication.getInstance().isNetConnected) {
		// position = position - 1;
		// String docId = adapter.getList().get(position).getId();
		// String DeptId = adapter.getList().get(position).getDocDeptId();
		// if (!isChoice) {
		//
		// String deptDocId = adapter.getList().get(position)
		// .getDeptDocId();
		// Intent intent = new Intent(DoctorListActivity.this,
		// DoctorIndexActivity.class)
		// .putExtra("deptId", DeptId)
		// .putExtra("docId", docId)
		// .putExtra("deptDocId", deptDocId);
		// startActivity(intent);
		// } else {// 选择医生
		// String docName = adapter.getList().get(position)
		// .getDocName();
		// String DeptName = adapter.getList().get(position)
		// .getDocDept();
		// String hosName = adapter.getList().get(position)
		// .getDocHos();
		// String hosId = adapter.getList().get(position)
		// .getDocHosId();
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
		// break;

		// default:// 筛选城市
		// City city = cityAdapter.getList().get(position);
		// cityId = city.getCityId();
		// String cityName = city.getCityName();
		// if (!isWB) {
		// SystemPreferences.save(EZTConfig.KEY_CITY_ID, cityId);
		// SystemPreferences.save(EZTConfig.KEY_STR_CITY, cityName);
		// } else {
		//
		// SystemPreferences.save(EZTConfig.KEY_CITY_WB_ID, cityId);
		// SystemPreferences.save(EZTConfig.KEY_STR_WB_CITY, cityName);
		// }
		// tvChoice1.setText(cityName);
		//
		// choicePop.dismiss();
		// getData();
		// break;
		}
		if (parent == cityLv) {
			// 筛选城市
			City city = cityAdapter.getList().get(position);
			cityId = city.getCityId();
			String cityName = city.getCityName();
			if (!isWB) {
				SystemPreferences.save(EZTConfig.KEY_CITY_ID, cityId);
				SystemPreferences.save(EZTConfig.KEY_STR_CITY, cityName);
			} 
//			else {
//
//				SystemPreferences.save(EZTConfig.KEY_CITY_WB_ID, cityId);
//				SystemPreferences.save(EZTConfig.KEY_STR_WB_CITY, cityName);
//			}
			tvChoice1.setText(cityName);

			choicePop.dismiss();
			getData();
		}

	}

	/**
	 * 筛选后获取医生列表
	 */
	private void getData() {
		showProgressToast();
		lv.setSelection(0);
		if (adapter.mList != null) {// 返回刷新，如果之前adapter，则清除
			adapter.mList.clear();
			lv.setAdapter(adapter);
			lv.setVisibility(View.GONE);
		}
		currentPage = 1;// 回调返回初始化页码
		initialData();
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
				initialData();
			}
		}
	}

	@Override
	public void onRefresh() {
		lv.setAutoLoadMore(true);
		currentPage = 1;
		initialData();
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
