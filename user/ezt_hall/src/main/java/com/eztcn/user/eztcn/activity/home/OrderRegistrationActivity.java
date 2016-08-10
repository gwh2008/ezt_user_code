package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.MainActivity;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker.OnSelectListener;
import com.eztcn.user.eztcn.customView.ValidateDialog;
import com.eztcn.user.eztcn.customView.ValidateDialog.CodeSure;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.FontUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 预约挂号
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class OrderRegistrationActivity extends FinalActivity implements
		OnClickListener, IHttpResult, CodeSure {// OnCheckedChangeListener

	private ImageView index;// 返回首页
	private ValidateDialog valideDialog;
	@ViewInject(R.id.affirmOrder)
	// , click = "onClick"
	private Button affirmOrder;
	@ViewInject(R.id.timeLayout)
	// , click = "onClick"
	private RelativeLayout timeLayout;
	@ViewInject(R.id.personLayout)
	// , click = "onClick"
	private RelativeLayout personLayout;// 就诊人选择
	@ViewInject(R.id.personName)
	private TextView personName;// 就诊人姓名
	@ViewInject(R.id.date)
	private TextView tvDate;
	@ViewInject(R.id.time)
	private TextView tvTime;
	// @ViewInject(R.id.illRecord, click = "onClick")
	// private TextView illRecord;

	@ViewInject(R.id.hospital)
	// 医院
	private TextView tvHos;

	@ViewInject(R.id.doctorName)
	// 医生名称
	private TextView tvDoc;

	@ViewInject(R.id.reRequest_time_tv)
	// , click = "onClick"
	private TextView tvReTime;// 重新获取就诊时间

	@ViewInject(R.id.reRequest_person_tv)
	// , click = "onClick"
	private TextView tvRePerson;// 重新获取就诊人

	@ViewInject(R.id.cardLayout)
	private RelativeLayout cardLayout;
	@ViewInject(R.id.hosCard)
	private EditText hosCard;// 一卡通(针对深圳儿童医院，可为空)

	@ViewInject(R.id.info_et)
	private EditText etInfo;// 病情描述

	@ViewInject(R.id.pay_style_spr)
	// , click = "onClick"
	private TextView payStyleBt;// 缴费方式

	// @ViewInject(R.id.state_sbt)
	// private TxtSwitchButton stateBt;// 就诊状态
	@ViewInject(R.id.state_Tv)
	private TextView state_Tv;
	@ViewInject(R.id.specialLayout)
	private LinearLayout specialLayout;// 复诊号layout
	@ViewInject(R.id.secondOrder)
	private EditText secondOrder;// 复诊号

	@ViewInject(R.id.deptType)
	// 2016-03-07 科室类型
	private TextView deptType;

	private List<String> personList;
	private PopupWindow pWindow;

	private String orderDate;// 所选日期
	private String orderTime;// 所选时间
	private String person;// 所选就诊人

	private ArrayList<Pool> poolist;//
	private ArrayList<String> dateList;
	private List<List<String>> timeLists;// 日期对应的时间段集合
	private int pos;// 选中的日期下标
	private int tPos;// 选中的时间下标 2016-1-22
	private String poolDate;// 选中的日期
	private List<String> timeList;

	private String pfId = ""; // 平台id
	private String sourcePfId = "355";// 平台来源id
	private String poolId = "";// 号池id
	private int payWay = 0;// 缴费方式 0：自费 1：普通医保2：门特医保3：门大医保
	private int isFirst = 0;// 复诊 1，初诊0
	private String regMark = "";// 参数标识
	private String cardNum = "";// 病案号
	private String operateUserId =  "";// 操作用户id
	private String patientId = "";
	private String hosName;
	private String docName;
	private String hospitalId;
	private String idCard;
	// 2015- 11- 30 提取deptid为全局变量 方便判断肿瘤医院
	private String deptid;
	private ArrayList<FamilyMember> familyList;
	private ScrollerNumberPicker oDate, oTime, personView, illRecordPicker;
	private TextView affirmDate, add;
	private View view, view2, view3;
	private int type;
	private Builder builder;
	private final double EARTH_RADIUS = 6378.137;// 地球半径
	private int tempDateP = 0;// 临时存储日期位置2016-1-22 时间
	private int tempTimeP = 0;// 临时存储时间位置2016-1-22 时间
	private String docDeptStr;// 医生部门
	// private int timePos;//号池时间标志代表哪个时间点
	@ViewInject(R.id.typeLayout)
	private View typeLayout;
	@ViewInject(R.id.typeValue)
	private TextView typeValue;
	private String medNo = "";
	/**
	 * 20160324患者手机号
	 */
	private String patientMobileStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderregistration);
		ViewUtils.inject(OrderRegistrationActivity.this);
		if( BaseApplication.patient!=null){
			operateUserId = BaseApplication.patient.getUserId() + "";// 操作用户id
		}
		index = loadTitleBar(true, "预约挂号", R.drawable.ic_home);
		index.setOnClickListener(this);
		pos = getIntent().getIntExtra("pos", 0);
		poolDate = getIntent().getStringExtra("poolDate");
		hosName = getIntent().getStringExtra("hosName");
		docName = getIntent().getStringExtra("docName");
		hospitalId = getIntent().getStringExtra("hosId");
		docDeptStr = getIntent().getStringExtra("docDept");
		// 20160309 添加时间标志位
		tPos = getIntent().getIntExtra("timePos", 0);
		deptid = getIntent().getStringExtra("deptid");
		tvHos.setText(TextUtils.isEmpty(hosName) ? "医院" : hosName);
		tvDoc.setText(TextUtils.isEmpty(docName) ? "医生" : docName);
		deptType.setText(docDeptStr);
		specialHospital();
		// 初始化自定义带文字开关
		// stateBt.setTextOn("初诊");
		// stateBt.setTextOff("复诊");
		// stateBt.setChecked(true);// 默认选中初诊
		// stateBt.setOnCheckedChangeListener(this);
		initialDialogView();
		initialData();
		FontUtils.tvChangeFont(tvDate, this);
		FontUtils.tvChangeFont(tvTime, this);
		if(BaseApplication.patient!=null){
			patientId=BaseApplication.patient.getId()+"";// 患者id
		}

	}

	/**
	 * 特殊医院需求
	 */
	public void specialHospital() {
		if (isFirst == 1
				&& (hospitalId.equals("30") || hospitalId.equals("94"))) {
			specialLayout.setVisibility(View.VISIBLE);
		} else {
			specialLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化选择框view
	 */
	private void initialDialogView() {

		// 选择时间
		view = LayoutInflater.from(this).inflate(R.layout.selecttime_order,
				null);
		oDate = (ScrollerNumberPicker) view.findViewById(R.id.date);
		oTime = (ScrollerNumberPicker) view.findViewById(R.id.time);
		affirmDate = (TextView) view.findViewById(R.id.affirm);

		// 选择就诊人
		view2 = LayoutInflater.from(this).inflate(R.layout.selectperson, null);
		add = (TextView) view2.findViewById(R.id.add);
		personView = (ScrollerNumberPicker) view2
				.findViewById(R.id.person_wheelview);

		view3 = LayoutInflater.from(mContext).inflate(R.layout.test_wheelview,
				null);
		illRecordPicker = (ScrollerNumberPicker) view3
				.findViewById(R.id.picker);
	}

	/**
	 * 初始化缴费方式
	 */
	public void initPayType() {
		final String[] payName = { "自费", "普通医保", "门特医保", "门大医保" };
		builder = new Builder(this);
		builder.setItems(payName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				payWay = which;
				payStyleBt.setText(payName[which]);
			}
		});
		builder.create().show();
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
		// R.layout.item_spinner_right, R.id.spinner_txt, payName);
		// payStyleBt.setAdapter(adapter);
		// payStyleBt.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// payWay = position;
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// }
		// });
	}

	/**
	 * 初始化缴费类型
	 */
	public void initState_Tv() {
		final String[] payName = { "初诊", "复诊" };
		builder = new Builder(this);
		builder.setItems(payName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				state_Tv.setText(payName[which]);
				isFirst = which;
				specialHospital();
			}
		});
		builder.create().show();
	}

	/**
	 * 初始化缴费类型
	 */
	public void initPayclaz() {
		final String[] payName = { "自费", "医保" };
		builder = new Builder(this);
		builder.setItems(payName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				payWay = which;
				typeValue.setText(payName[which]);
			}
		});
		builder.create().show();
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
		// R.layout.item_spinner_right, R.id.spinner_txt, payName);
		// payStyleBt.setAdapter(adapter);
		// payStyleBt.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// payWay = position;
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		// }
		// });
	}

	@OnClick(R.id.typeLayout)
	public void typeLayoutClick(View view) {
		initPayclaz();
	}

	/**
	 * 初始化人数据
	 */
	private void initialData() {
		showProgressToast();
		// getPools();
		getPoolData();
		getMembers();
	}

	/**
	 * 直接从上一界面获取号池 就诊bug 代码优化
	 */
	private void getPoolData() {
		poolist = (ArrayList<Pool>) getIntent()
				.getSerializableExtra("poolList");
		if (null == poolist) {
			getPools();
		} else {
			backColumn++;
			if (poolist != null && poolist.size() != 0) {
				tvDate.setVisibility(View.VISIBLE);
				tvTime.setVisibility(View.VISIBLE);
				tvReTime.setVisibility(View.GONE);
				dealWithPoolData();
			} else {
				tvDate.setVisibility(View.GONE);
				tvTime.setVisibility(View.GONE);
				tvReTime.setVisibility(View.VISIBLE);
				/*
				 * Toast.makeText(getApplicationContext(),
				 * getString(R.string.sorry_pool_wrong),
				 * Toast.LENGTH_SHORT).show();
				 */
			}
		}

	}

	/**
	 * 获取就诊人
	 */
	private void getMembers() {
		// HashMap<String, Object> params1 = new HashMap<String, Object>();
		// params1.put("userId", operateUserId);
		RequestParams params1 = new RequestParams();
		params1.addBodyParameter("userId", operateUserId);
		new UserImpl().getMemberCenter(params1, this);
	}

	/**
	 * 获取号池资料
	 */
	private void getPools() {
		// 2015 11 30 提前了 deptid = getIntent().getStringExtra("deptid");
		String doctorid = getIntent().getStringExtra("doctorid");
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("deptid", deptid);
		// params.put("doctorid", doctorid);
		// params.put("isExist", 0 + "");// 默认查询预约未满的号池信息

		RequestParams params = new RequestParams();
		params.addBodyParameter("deptid", deptid);
		params.addBodyParameter("doctorid", doctorid);
		params.addBodyParameter("isExist", 0 + "");// 默认查询预约未满的号池信息

		HospitalImpl impl = new HospitalImpl();
		impl.getDocPool(params, this);
	}

	/**
	 * 处理号池信息
	 */
	private void dealWithPoolData() {

		orderDate = poolist.get(pos).getDate();
		List<PoolTimes> tlist = poolist.get(pos).getTimeList();

		if (pos == 0) {// 2016-1-22 个人医生站进入 号池显示错误bug 大患更正
			if (null == tlist) {
				for (int i = 1; i < poolist.size(); i++) {
					tlist = poolist.get(i).getTimeList();
					if (null != tlist) {
						pos = i;
						orderDate = poolist.get(i).getDate();
						poolDate = orderDate;
						break;
					}
				}
			} else if (null == poolDate) {
				poolDate = orderDate;
			}
		}
		if (tlist != null && tlist.size() > 0) {
			String startTime = poolist.get(pos).getTimeList().get(tPos)
					.getStartDates().trim();
			String endTime = poolist.get(pos).getTimeList().get(tPos)
					.getEndDates().trim();
			orderTime = " " + startTime + "-" + endTime;
			tvDate.setText(orderDate);
			tvTime.setText(orderTime);
			// 就诊时间默认值
			pfId = poolist.get(pos).getPfId();
			poolId = poolist.get(pos).getTimeList().get(tPos).getPoolId();
			regMark = poolist.get(pos).getRegMark();
		}

		dateList = new ArrayList<String>();
		timeLists = new ArrayList<List<String>>();
		List<PoolTimes> ptList;
		int nums = 0;
		for (int i = 0; i < poolist.size(); i++) {
			timeList = new ArrayList<String>();
			ptList = poolist.get(i).getTimeList();
			nums = 0;
			if (ptList == null || ptList.size() == 0) {
				continue;
			}
			for (int j = 0; j < ptList.size(); j++) {
				if (!ptList.get(j).isRemains()) {
					continue;
				}
				timeList.add(ptList.get(j).getStartDates() + "-"
						+ ptList.get(j).getEndDates());
				nums++;
			}
			if (nums > 0) {
				timeLists.add(timeList);
				dateList.add(poolist.get(i).getDate());
			}
		}
		for (int i = 0; i < dateList.size(); i++) {
			if (dateList.get(i).equals(poolDate)) {
				pos = i;
				break;
			}
		}
		// ptList = null;
	}

	// @OnClick(R.id.timeLayout)
	// public void timeLayoutClick(View v) {
	// type = 1;
	// selectOrderTime();
	// if (null != pWindow)
	// pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	// }

	@OnClick(R.id.personLayout)
	public void personLayoutClick(View v) {
		// 选择就诊人
		if (personList != null) {
			type = 2;
			selectPerson();
			pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		} else {
			Logger.i("就诊人", "就诊人数据为空");
		}
	}

	@OnClick(R.id.reRequest_time_tv)
	public void reRequest_time_tvClick(View v) {
		// 重新获取就诊时间
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// getPools();
		getPoolData();
	}

	@OnClick(R.id.reRequest_person_tv)
	public void reRequest_person_tvClick(View v) {
		// 重新获取就诊人
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		getMembers();
	}

	// @OnClick(R.id.pay_style_spr)
	// public void pay_style_sprClick(View v) {
	// initPayType();
	// }

	@OnClick(R.id.affirmOrder)
	public void affirmOrderClick(View v) {
		if (TextUtils.isEmpty(idCard)) {
			hintPerfectInfo("请完善个人信息再进行预约挂号!", 1);
			return;
		}
		if (TextUtils.isEmpty(medNo)
				&& payWay != 0) {
			Toast.makeText(mContext, "你暂无绑定医保，请更换你的支付方式！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (isFirst == 1
				&& (hospitalId.equals("30") || hospitalId.equals("94"))) {
			if (TextUtils.isEmpty(secondOrder.getText().toString())) {
				Toast.makeText(mContext, "请输入复诊病案号", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(orderTime)) {
			Toast.makeText(mContext, "请选择就诊时间", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils
				.isEmpty(BaseApplication.getInstance().patient.getEpMobile())) {
			Toast.makeText(mContext, "请完善个人信息的手机号", Toast.LENGTH_SHORT).show();
			return;
		}

		// 2015 - 11 -30 如果不是 肿瘤医院 就不用跳转免责声明页面
		if (!judgeIsTumor()) {
			valideDialog = new ValidateDialog(OrderRegistrationActivity.this,
					R.style.ChoiceDialog);
			valideDialog.setCanceledOnTouchOutside(true);

			valideDialog.setSure(this);

			valideDialog.show();
		}
	}

	public void onClick(View v) {
		if (index == v) {
			// 返回首页
			Intent home = new Intent(this, MainActivity.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);
		}

		switch (v.getId()) {
		case R.id.affirm:// 确定预约时间
			tvDate.setText(orderDate);
			tvTime.setText(orderTime);
			pos = tempDateP;
			tPos = tempTimeP;
			if (pWindow != null) {
				pWindow.dismiss();
				pWindow = null;
			}
			break;
		case R.id.add:// 确定就诊人
			personName.setText(person);
			if (pWindow != null) {
				pWindow.dismiss();
				pWindow = null;
			}
			getUserOneCard(patientId);
			break;
		case R.id.illRecord:// 上传病历
			break;
		}
	}

	/**
	 * 提醒完善信息
	 */
	private void hintPerfectInfo(String hint, final int type) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(hint).setCancelable(false)
				.setNegativeButton("完善", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (type == 1) {// 完善个人信息
							mContext.startActivity(new Intent(mContext,
									AutonymAuthActivity.class));
						} else {// 完善个人手机号
							mContext.startActivity(new Intent(mContext,
									ModifyPhoneActivity.class));
						}
						OrderRegistrationActivity.this.finish();
					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

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
		dialog.show();

	}
	/**
	 * 窗口设置
	 */
	public void configPopupWindow(final View view) {
		pWindow = new PopupWindow();

        if (view.getParent() != null) {//如果该view有父容器，就去掉它的父容器，防止父容器重复
            ViewGroup viewParent = (ViewGroup) view.getParent();
            viewParent.removeView(view);
        }

		pWindow.setContentView(view);
		pWindow.setWidth(LayoutParams.MATCH_PARENT);
		pWindow.setHeight(LayoutParams.MATCH_PARENT);
		pWindow.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pWindow.setBackgroundDrawable(dw);
		pWindow.setFocusable(true);
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float left = v.findViewById(R.id.titleLayout).getLeft();
				float top = v.findViewById(R.id.titleLayout).getTop();
				float right;
				float bottom;
				if (type == 1) {
					right = v.findViewById(R.id.affirm).getRight();
					bottom = v.findViewById(R.id.affirm).getBottom();
				} else {
					right = v.findViewById(R.id.add).getRight();
					bottom = v.findViewById(R.id.add).getBottom();
				}
				float x = event.getX();
				float y = event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {

					if (x < left && x > right) {
						if (y < top || y > bottom) {
							pWindow.dismiss();
							pWindow = null;// 2015-12-25
						}
					} else {
						if (y < top || y > bottom) {
							pWindow.dismiss();
							pWindow = null;// 2015-12-25
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * 预约时间选择器
	 */
	public void selectOrderTime() {
		if (null == dateList) {
			return;
		}
		// 日期
		initialTimeData(oDate, dateList, pos);
		// 修改开始的记录位置
		tempDateP = pos;
		tempTimeP = tPos;

		// 时间
		timeList = timeLists.get(pos);
		initialTimeData(oTime, timeList, tPos);// 2016-1-22时间
		// initialTimeData(oTime, timeList, 0);//2016-1-22时间
		configPopupWindow(view);
		affirmDate.setOnClickListener(this);
		if (timeList != null && timeList.size() > 0) {// 默认第一个被选中
			orderTime = timeList.get(tPos);// 初始位置
			// poolId = poolist.get(pos).getTimeList().get(0).getPoolId();
			for (int i = 0; i < poolist.size(); i++) {
				if (poolist.get(i).getDate().equals(orderDate)
						&& poolist.get(i).getTimeList() != null) {
					if (null != poolist.get(i).getTimeList()) { // 2016-1-20
						// 个人医生站医生号池
						// 有日子无时间点导致null异常修复
						poolId = poolist.get(i).getTimeList().get(0)
								.getPoolId();
						break;
					}
				}
			}
		} else {
			orderTime = "";
			poolId = "";
		}
		oDate.setOnSelectListener(new OnSelectListener() {

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endSelect(int id, String text) {
				orderDate = dateList.get(id);
				timeList = timeLists.get(id);
				// 联动显示
				tempDateP = id;
				tempTimeP = 0;// 2016-1-22 时间..
				if (timeList != null && timeList.size() > 0) {
					orderTime = timeList.get(0);// date滚轮修改时候切当旁边time的第0个位置
					if (null != poolist.get(id).getTimeList())// 2016-1-20
																// 个人医生站医生号池
																// 有日子无时间点导致null异常修复
						poolId = poolist.get(id).getTimeList().get(0)
								.getPoolId();
				}
				oTime.setData(timeList);
				oTime.setDefault(0);

			}
		});
		oTime.setOnSelectListener(new OnSelectListener() {

			@Override
			public void selecting(int id, String text) {
			}

			@Override
			public void endSelect(int id, String text) {
				orderTime = timeList.get(id);
				tempTimeP = id;
				for (int i = 0; i < poolist.size(); i++) {
					if (poolist.get(i).getDate().equals(orderDate)) {
						List<PoolTimes> times = poolist.get(i).getTimeList();// 2016-1-20
																				// 个人医生站医生号池
																				// 有日子无时间点导致null异常修复
						if (null != times)
							poolId = times.get(id).getPoolId();
						break;
					}
				}
			}
		});
	}

	/**
	 * 初始化WheelView数据
	 * 
	 * @param view
	 */
	private void initialTimeData(ScrollerNumberPicker view, List<String> data,
			int pos) {
		view.setData(data);
		view.setDefault(pos);
	}

	/**
	 * 就诊人选择
	 */
	private void selectPerson() {

		initialTimeData(personView, personList, 0);
		configPopupWindow(view2);
		add.setOnClickListener(this);
		if (personList != null && personList.size() > 0) {// 默认第一个被选中
			person = personList.get(0);
			// int index = person.indexOf(")") + 1;
			// person = person.substring(0, index);
			patientId = familyList.get(0).getPatientId();
			medNo = familyList.get(0).getMedicalNo();
			patientMobileStr = familyList.get(0).getPhone();
		}
		personView.setOnSelectListener(new OnSelectListener() {

			@Override
			public void selecting(int id, String text) {

			}

			@Override
			public void endSelect(int id, String text) {
				person = personList.get(id);
				patientId = familyList.get(id).getPatientId();
				idCard = familyList.get(id).getIdCard();
				medNo = familyList.get(id).getMedicalNo();
				patientMobileStr = familyList.get(id).getPhone();
			}
		});

	}

	/**
	 * 获取用户一卡通
	 */
	public void getUserOneCard(String patientId) {
		HospitalImpl impl = new HospitalImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("patientId", patientId);
		params.addBodyParameter("hospitalId", hospitalId);

		impl.getOneCard(params, this);
		showProgressToast();
	}

	/**
	 * 接口回调次数
	 */
	private int backColumn = 0;

	@Override
	public void result(Object... object) {
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		switch (type) {

		case HttpParams.MEMBER_CENTER:// 获取家庭成员
			backColumn++;
			if (isSuc) {
				familyList = (ArrayList<FamilyMember>) object[2];
				if (familyList != null) {
					personList = new ArrayList<String>();
					for (int i = 0; i < familyList.size(); i++) {
						String strName = familyList.get(i).getMemberName();
						String strCardNo = familyList.get(i).getIdCard();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext())
								.getLabelByTag("kinship",
										familyList.get(i).getRelation() + "");
						String strSex = familyList.get(i).getSex() == 0 ? "男"
								: "女";

						if (familyList.size() == 1) {
							// if (strName == null || "".equals(strName)) {
							// strName = "本人";
							// }
							if (TextUtils.isEmpty(strRelation)) {
								strRelation = "自己";
							}

						}
						if (TextUtils.isEmpty(strName)) {
							strName = "";
						}
						// String strNew = strName + "(" + strRelation + ")";
						String strNew = strName;
						personList.add(strNew);

					}
					if (familyList.size() > 0) {
						String strName = familyList.get(0).getMemberName();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext())
								.getLabelByTag("kinship",
										familyList.get(0).getRelation() + "");
						idCard = familyList.get(0).getIdCard();
						if (strName == null || "".equals(strName)) {
							strName = "本人";
						}
						if (TextUtils.isEmpty(strRelation)) {
							strRelation = "自己";
						}
						// person = strName + "(" + strRelation + ")";
						person = strName;
						personName.setVisibility(View.VISIBLE);
						tvReTime.setVisibility(View.GONE);
						personName.setText(person);
						patientMobileStr = familyList.get(0).getPhone();
						medNo = familyList.get(0).getMedicalNo();
						getUserOneCard(familyList.get(0).getPatientId());
					}
				} else {
					personName.setVisibility(View.GONE);
					tvReTime.setVisibility(View.VISIBLE);
					Toast.makeText(getApplicationContext(),
							getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				personName.setVisibility(View.GONE);
				tvReTime.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;

		case HttpParams.GET_ONECARD:// 获取一卡通
			if (isSuc) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) object[2];
				if (map == null || map.size() == 0) {
					return;
				}
				boolean flag = (Boolean) map.get("flag");
				if (!flag) {
					return;
				}
				hosCard.setText(map.get("ucNum").toString());
			}
			break;

		case HttpParams.GET_DOC_POOL:// 获取号池
			backColumn++;
			if (isSuc) {// 成功
				poolist = (ArrayList<Pool>) object[2];
				if (poolist != null && poolist.size() != 0) {
					tvDate.setVisibility(View.VISIBLE);
					tvTime.setVisibility(View.VISIBLE);
					tvReTime.setVisibility(View.GONE);
					dealWithPoolData();
				} else {
					tvDate.setVisibility(View.GONE);
					tvTime.setVisibility(View.GONE);
					tvReTime.setVisibility(View.VISIBLE);
					Toast.makeText(getApplicationContext(), "很抱歉，预约已满",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				tvDate.setVisibility(View.GONE);
				tvTime.setVisibility(View.GONE);
				tvReTime.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;

		}
		if (type != HttpParams.ENTER_QUEUE && backColumn >= 2) { // 2015-12-28
																	// 更改医生无号源时候，点击获取号源再点击就诊人后圈圈不消失bug
			hideProgressToast();
		}

	}

	/**
	 * 
	 * 判断是否为肿瘤医院 2016-02-17 上线 线上dptId=32632 阿里云dptId=7720
	 * 
	 * @return
	 */
	private boolean judgeIsTumor() {
		boolean isTumor = false;
		if ("94".equals(hospitalId) && "32632".equals(deptid)) {
			isTumor = true;
			Intent intent = new Intent(OrderRegistrationActivity.this,
					TumorCaseActivity.class);
			intent.putExtra("reg_date", orderDate);
			startActivityForResult(intent, 1);
		}
		return isTumor;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 1 && resultCode == 1) {
			boolean canReg = intent.getBooleanExtra("canReg", false);
			if (canReg) {
				valideDialog = new ValidateDialog(
						OrderRegistrationActivity.this, R.style.ChoiceDialog);
				valideDialog.setCanceledOnTouchOutside(true);
				

				valideDialog.setSure(this);
				
			
				valideDialog.show();
			}
		}
	}

	@OnClick(R.id.state_Tv)
	public void initState_TvClick(View v) {
		initState_Tv();
	}

	// @Override
	// public void onCheckedChanged(CompoundButton buttonView, boolean
	// isChecked) {
	//
	// if (buttonView.getId() == R.id.state_sbt) {// 就诊状态
	// isFirst = isChecked ? 0 : 1;
	// }
	// specialHospital();
	// }

	@Override
	public void onSureClick(String valideCode) {
		if (valideCode.isEmpty()) {
			Toast.makeText(OrderRegistrationActivity.this, "请输入验证码",
					Toast.LENGTH_SHORT).show();
		} else {
			valideDialog.dismiss();
			Intent intent = new Intent(OrderRegistrationActivity.this,
					OrderRegWaitActivity.class);

			intent.putExtra("code", valideCode);
			intent.putExtra("regMark", regMark);
			intent.putExtra("cartoonNum", hosCard.getText().toString());
			intent.putExtra("cardNum", cardNum);
			intent.putExtra("isFirst", isFirst);
			intent.putExtra("pfId", pfId);
			intent.putExtra("sourcePfId", sourcePfId);
			intent.putExtra("poolId", poolId);
			intent.putExtra("payWay", payWay);
			intent.putExtra("patientId", patientId);
			// 2016-03-07
			intent.putExtra("regPerson", person);
			intent.putExtra("mobile", BaseApplication.patient.getEpMobile());
			intent.putExtra("regTime", orderDate + " " + orderTime);
			intent.putExtra("docName", docName);
			intent.putExtra("docDept", docDeptStr);
			intent.putExtra("hosName", hosName);
			intent.putExtra("patientMobile", patientMobileStr);
			if (isFirst == 1
					&& (hospitalId.equals("30") || hospitalId.equals("94"))) {
				intent.putExtra("cardNum", secondOrder.getText().toString());
			}
			startActivity(intent);
			finish();
		}

	}
}
