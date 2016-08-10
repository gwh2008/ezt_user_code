package com.eztcn.user.eztcn.activity.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.MyMedicalRecordListActivity;
import com.eztcn.user.eztcn.activity.mine.RechargeActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.CallTimeList;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 预约通话
 * @describe 填写预约时间、接听号码、上传病例等资料
 * @author ezt
 * @created 2014年12月12日
 */
public class AppointConsultActivity extends FinalActivity implements
		IHttpResult {// OnClickListener,

	/**** 医生医院、科室信息 *********************************/
	@ViewInject(R.id.doctorPhoto)
	private RoundImageView doctorPhoto;
	@ViewInject(R.id.doctorName)
	private TextView doctorName;
	@ViewInject(R.id.doctor_level)
	private TextView doctor_level;
	@ViewInject(R.id.dept_name)
	private TextView dept_name;
	@ViewInject(R.id.hos_name)
	private TextView hos_name;

	/***** 电话医生费用信息 ******************/
	@ViewInject(R.id.money)
	private TextView money;// 通话费用（10/个）
	@ViewInject(R.id.timeOfminute)
	private TextView timeOfminute;// 每分钟通话费用（3个/分钟）
	@ViewInject(R.id.hintCallInfo)
	private TextView hintCallInfo;// 底部费用说明

	@ViewInject(R.id.appoint_consult_yes_bt)
	// , click = "onClick"
	private Button btYes;// 确定按钮

	@ViewInject(R.id.recharge)
	// , click = "onClick"
	private Button recharge;// 充值

	@ViewInject(R.id.appoint_consult_time_layout)
	// , click = "onClick"
	private RelativeLayout layoutTime;

	@ViewInject(R.id.appoint_consult_case_history_layout)
	// , click = "onClick"
	private RelativeLayout layoutCaseHis;

	@ViewInject(R.id.appoint_consult_time)
	private TextView tvTime;// 预约通话时间

	@ViewInject(R.id.appoint_consult_telnum)
	private EditText etTelNum;// 接听电话

	@ViewInject(R.id.appoint_consult_case_history)
	private TextView tvCaseHisNum;// 病历号

	@ViewInject(R.id.appoint_consult_et)
	private EditText etDescribe;// 病情描述

	@ViewInject(R.id.user_balance)
	private TextView tvBalance;// 余额

	@ViewInject(R.id.user_time)
	private TextView tvUpTime;// 通话时长

	// 自定义的底部弹出框类（可预约时间选择）
	private PopupWindow menuWindow;
	private ScrollerNumberPicker dateSelecter;
	private ScrollerNumberPicker timeSelecter;

	private List<CallTimeList> timeList;
	private List<String> dateList;
	private List<String> tList;
	private String currentDate;
	private String currentTime;
	private int timeId;
	private int type;
	private String deptId;
	private String doctorId;

	private int eztCurrency;
	private int times;

	private String mId;// 病历id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appoint_consult);
		ViewUtils.inject(AppointConsultActivity.this);
		loadTitleBar(true, "预约通话", null);
		// getUserInfo();
		// initOrderDate();
		// showTimeSelecter();
		// getOrderTime(currentDate);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			type = savedInstanceState.getInt("type");
			init(savedInstanceState);
		}
	}

	/**
	 * 初始化医生信息
	 */
	public void initDoctor(String docName, String level, String dept,
			String hos, String photoUrl) {
		doctorName.setText(docName);
		if (level != null) {
			doctor_level.setText(EztDictionaryDB.getInstance(mContext)
					.getLabelByTag("doctorLevel", level));
		}
		dept_name.setText(dept);
		hos_name.setText(hos);
		initDocPhoto(photoUrl);

	}

	/**
	 * 初始化医生头像
	 */
	public void initDocPhoto(String photo) {
		// FinalBitmap fb = FinalBitmap.create(this);
		Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_doc_img);
		BitmapUtils bitmapUtils = new BitmapUtils(AppointConsultActivity.this);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		bitmapUtils.display(doctorPhoto, EZTConfig.DOC_PHOTO + photo);
		// fb.display(doctorPhoto, EZTConfig.DOC_PHOTO + photo, defaultBit,
		// defaultBit);
	}

	/**
	 * 获取用户账户信息
	 */
	private void getUserInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		new PayImpl().getCurrencyMoney(params, this);
		showProgressToast();
		etTelNum.setText(BaseApplication.patient.getEpMobile());
	}

	/**
	 * 获取可播打分钟数
	 */
	public void getCallTime() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("deptId", deptId);
		params.addBodyParameter("doctorId", doctorId);
		new PayImpl().getCllMinute(params, this);
		showProgressToast();
		etTelNum.setText(BaseApplication.patient.getEpMobile());
	}

	public void init(Bundle savedInstanceState) {
		Doctor doctor = (Doctor) savedInstanceState.getSerializable("info");
		CallTimeList callTime = (CallTimeList) savedInstanceState
				.getSerializable("time");
		if (doctor != null) {// 医生信息
			doctorId = doctor.getId();
			deptId = doctor.getDocDeptId();
			doctorId = doctor.getId();
			// 费用
			double fees = doctor.getFees();
			if (fees > 0) {
				money.setText((int) Math.floor(fees) + "个");
			}
			double times = doctor.getMinTime();
			if (times > 0) {
				timeOfminute.setText((int) Math.floor(times) + "个/分钟");
			}
			String docName = doctor.getDocName();
			String deptName = doctor.getDocDept();
			String hosName = doctor.getDocHos();
			String level = doctor.getDocLevel() + "";
			String photoUrl = doctor.getDocHeadUrl();
			initDoctor(docName, level, deptName, hosName, photoUrl);
		}
		if (callTime != null) {// 时间段信息
			currentDate = callTime.getDate();
			String begin = callTime.getBeginTime();
			String end = callTime.getEndTime();
			currentTime = begin.substring(begin.indexOf(" "),
					begin.lastIndexOf(":"))
					+ " - "
					+ end.substring(end.indexOf(" "), end.lastIndexOf(":"));
			timeId = callTime.getId();
		}
		tvTime.setText(currentDate + "  " + currentTime);
		btYes.setEnabled(false);
		getCallTime();
	}

	@OnClick(R.id.appoint_consult_yes_bt)
	private void consult_yesClick(View v) {// 确定
		affirmOrder();
	}

	@OnClick(R.id.appoint_consult_case_history_layout)
	private void consult_caseClick(View v) {
		// 上传病历

		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		Intent intent = new Intent(this, MyMedicalRecordListActivity.class);
		intent.putExtra("enterType", 22);
		intent.putExtra("patientId", BaseApplication.patient.getId());
		startActivityForResult(intent, 22);
	}

	@OnClick(R.id.recharge)
	private void rechargeClick(View v) {
		Intent intent = new Intent(this, RechargeActivity.class);
		startActivity(intent);
	}

	/**
	 * 查看医生是否开通电话医生服务、资费情况、是否在线、是否可预约
	 * 
	 * @param docId
	 */
	private void checkTelDocState(String docId) {
		// HashMap<String, Object> param3 = new HashMap<String, Object>();
		// param3.put("doctorId", docId);

		RequestParams param3 = new RequestParams();
		param3.addBodyParameter("doctorId", "" + docId);
		new TelDocImpl().checkTelDocState(param3, this);
		showProgressToast();
	}

	/**
	 * 确定预约
	 */
	public void affirmOrder() {
		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		String phone = etTelNum.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(getApplicationContext(), "请输入需要接听的手机号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isPhone(phone)) {
			Toast.makeText(getApplicationContext(), "请输入正确格式的手机号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(tvTime.getText().toString())) {
			Toast.makeText(getApplicationContext(), "无可预约时间段",
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("doctorId", doctorId);
		params.addBodyParameter("regdate", currentDate);
		params.addBodyParameter("callScheduleDoctorId", timeId + "");
		params.addBodyParameter("patientPhone", phone);
		params.addBodyParameter("deptId", deptId + "");
		if (TextUtils.isEmpty(mId)) {
			params.addBodyParameter("medicalRecordsId", mId);
		}
		new TelDocImpl().confirmTelDocOrder(params, this);
		showProgressToast();
	}

	/**
	 * 初始化可预约日期
	 */
	public void initOrderDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String temp;
		dateList = new ArrayList<String>();
		Date date = new Date();
		int nowDay = date.getDate();
		temp = sdf.format(date);
		dateList.add(temp);
		for (int i = 0; i < 6; i++) {
			date.setDate(nowDay + 1);
			temp = sdf.format(date);
			dateList.add(temp);
			nowDay = date.getDate();
		}
		currentDate = dateList.get(0);
	}

	/**
	 * 初始化WheelView数据
	 * 
	 * @param view
	 */
	// private void initOrderData() {
	// int pos;
	// // timeAdapter.setTextSize(18);
	// dateSelecter.setData(dateList);
	// dateSelecter.setDefault(0);
	// for (int i = 0; i < dateList.size(); i++) {
	// if (currentDate.equals(dateList.get(i))) {
	// dateSelecter.setDefault(i);
	// }
	// }
	// dateSelecter.setOnSelectListener(new OnSelectListener() {
	//
	// @Override
	// public void selecting(int id, String text) {
	// }
	//
	// @Override
	// public void endSelect(int id, String text) {
	// currentDate = dateList.get(id);
	// getOrderTime(currentDate);
	// handler.sendEmptyMessage(1);
	// }
	// });
	// }

	/**
	 * 初始化WheelView数据
	 * 
	 * @param view
	 */
	// private void initOrderTime() {
	// tList = new ArrayList<String>();
	// String time;
	// if (timeList != null) {
	// for (int i = 0; i < timeList.size(); i++) {
	// time = timeList.get(i).getBeginTime();
	// tList.add(time.substring(time.indexOf(" ") + 1));
	// }
	// }
	// timeSelecter.setData(tList);
	// // timeSelecter.setDefault(0);
	// for (int i = 0; i < tList.size(); i++) {
	// if (currentTime.equals(tList.get(i))) {
	// timeSelecter.setDefault(i);
	// }
	// }
	// timeSelecter.setOnSelectListener(new OnSelectListener() {
	//
	// @Override
	// public void selecting(int id, String text) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void endSelect(int id, String text) {
	// if (id > tList.size() || id <= 0) {
	// currentTime = "";
	// return;
	// }
	// currentTime = tList.get(id);
	// timeId = timeList.get(id).getCallScheduleId();
	// handler.sendEmptyMessage(1);
	// }
	// });
	// }

	// Handler handler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// super.handleMessage(msg);
	// if (currentTime != null && !currentTime.equals("")) {
	// tvTime.setText(currentDate + " " + currentTime);
	// }
	// }
	//
	// };

	/**
	 * 获取医生可预约时间段
	 */
	public void getOrderTime(String date) {
		if (BaseApplication.patient != null) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("userId",
					BaseApplication.patient.getUserId() + "");
			params.addBodyParameter("doctorId", "1");
			params.addBodyParameter("regdate", date);
			new TelDocImpl().getTelDocTime(params, this);
		}
	}

	public void showTimeSelecter() {
		View v = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.dialog_datetime, null);
		dateSelecter = (ScrollerNumberPicker) v.findViewById(R.id.dateSelecter);
		timeSelecter = (ScrollerNumberPicker) v.findViewById(R.id.timeSelecter);
		initPopupWindow(v);
	}

	public void initPopupWindow(View v) {

		menuWindow = new PopupWindow();
		// 设置SelectPicPopupWindow的View
		menuWindow.setContentView(v);
		// 设置SelectPicPopupWindow弹出窗体的宽
		menuWindow.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		menuWindow.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		menuWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		menuWindow.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		menuWindow.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		v.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = v.findViewById(R.id.dateSelecter).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						menuWindow.dismiss();
					}
				}
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 4 && resultCode == 44) {// 预约通话成功返回
			setResult(44);
			finish();
		}
		if (resultCode == 22) {
			if (data != null) {
				MedicalRecord record = (MedicalRecord) data.getExtras()
						.getSerializable("record");
				mId = record.getId();
				tvCaseHisNum.setText(record.getRecordNum());
			}
		}

	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		btYes.setEnabled(true);
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Logger.i("电话医生", obj[3]);
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		switch (taskID) {
		case HttpParams.GET_CURRENCY_MONEY:// 获取账户信息
			if (flag) {
				Double ec = (Double) map.get("remain");
				if (ec == null) {
					eztCurrency = 0;
				} else {
					eztCurrency = (int) Math.floor(ec);
				}
			}
			tvBalance.setText(eztCurrency + "个");
			break;
		case HttpParams.GET_CALLMINUTE:// 获取可拨打分钟数
			if (flag) {
				Double ec = (Double) map.get("currency");
				if (ec == null) {
					eztCurrency = 0;
				} else {
					eztCurrency = (int) Math.floor(ec);
				}
				Integer t = (Integer) map.get("times");
				if (t != null) {
					times = t;
				}
			}
			tvBalance.setText(eztCurrency + "个");
			tvUpTime.setText(times + "分钟");
			break;
		case HttpParams.CONFIRM_TEL_DOC_ORDER:// 确定预约
			if (flag) {
				startActivityForResult(new Intent(AppointConsultActivity.this,
						SucHintActivity.class).putExtra("type", 2), 4);
			} else {
				Toast.makeText(mContext, map.get("msg").toString(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		// case HttpParams.GET_TEL_DOC_TIME:// 获取可预约时间段
		// timeList = (List<CallTimeList>) map.get("list");
		// if (timeList != null && timeList.size() > 0) {
		// currentTime = timeList.get(0).getBeginTime();
		// tvTime.setText(currentDate + " "
		// + currentTime.substring(currentTime.indexOf(" ") + 1));
		// timeId = timeList.get(0).getId();
		// } else {
		// currentTime = "";
		// }
		// initOrderTime();
		// break;
		}
	}
}
