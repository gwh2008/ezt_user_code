package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.RechargeActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 电话医生
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class PhoneDoctorActivity extends FinalActivity implements
		OnClickListener, IHttpResult {

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
	@ViewInject(R.id.money)
	private TextView money;

	@ViewInject(R.id.tv_hint)
	private TextView tvMoneyOfMinute;

	@ViewInject(R.id.timeOfminute)
	private TextView timeOfminute;
	@ViewInject(R.id.orderCall)//, click = "onClick"
	private RelativeLayout orderCall;
	@ViewInject(R.id.call)//, click = "onClick"
	private RelativeLayout call;
	@ViewInject(R.id.edit)//, click = "onClick"
	private Button editPhone;

	@ViewInject(R.id.layout)
	private LinearLayout layout;

	private TextView serverExplain;

	private Intent intent;
	private Doctor doctor;
	private TelDocState state;
	private Double eztCurrency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonedoctor);
		ViewUtils.inject(PhoneDoctorActivity.this);
		serverExplain = loadTitleBar(true, "电话医生", "");
		serverExplain.setOnClickListener(this);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			doctor = (Doctor) savedInstanceState.getSerializable("doc");
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (doctor != null) {
			init();
			checkTelDocState(doctor.getId());
		}
		getUserInfo();
	}

	public void init() {
		doctorName.setText(doctor.getDocName());
		String level = doctor.getDocLevel();
		if (level != null) {
			doctor_level.setText(EztDictionaryDB.getInstance(mContext)
					.getLabelByTag("doctorLevel", level));
		}
		dept_name.setText(doctor.getDocDept());
		hos_name.setText(doctor.getDocHos());
		initDocPhoto(doctor.getDocHeadUrl());
	}

	/**
	 * 初始化医生头像
	 */
	public void initDocPhoto(String photo) {
		Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_doc_img);
		BitmapUtils bitmapUtils=new BitmapUtils(PhoneDoctorActivity.this);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		    bitmapUtils.display(doctorPhoto, EZTConfig.DOC_PHOTO + photo);
//		FinalBitmap fb = FinalBitmap.create(this);
//	
//		fb.display(doctorPhoto, EZTConfig.DOC_PHOTO + photo, defaultBit,
//				defaultBit);
	}

	/**
	 * 查看医生是否开通电话医生服务、资费情况、是否在线、是否可预约
	 * 
	 * @param docId
	 */
	private void checkTelDocState(String docId) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("doctorId", docId);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("doctorId", docId);
		new TelDocImpl().checkTelDocState(params, this);
		showProgressToast();
	}

	/**
	 * 获取用户账户信息
	 */
	private void getUserInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		new PayImpl().getCurrencyMoney(params, this);
		showProgressToast();
	}
	public void onClick(View v) {

		intent = new Intent();
		switch (v.getId()) {
		case R.id.call:// 立即通话
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			if (state == null) {
				hidCharge();
				return;
			}
			String rate = state.getCiGuaranteedRate();
			if (rate == null) {
				hidCharge();
				return;
			}
			if (eztCurrency < Double.parseDouble(state.getCiGuaranteedRate())) {
				hidCharge();
				return;
			}
			intent.setClass(mContext, PromptlyCallActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("currency", eztCurrency);
			intent.putExtra("info", state);
			intent.putExtra("deptId", doctor.getDocDeptId());
			break;
		case R.id.orderCall:// 预约通话

			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			if (state == null) {
				hidCharge();
				return;
			}
			String rate1 = state.getCiGuaranteedRate();
			if (rate1 == null) {
				hidCharge();
				return;
			}
			if (eztCurrency < Double.parseDouble(state.getCiGuaranteedRate())) {
				hidCharge();
				return;
			}
			intent.setClass(mContext, PhoneDoctorTimeActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("state", state);
			intent.putExtra("info", doctor);
			break;
		default:
			intent = null;
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}
	@OnClick(R.id.call)
	public void callClick(View v) {

		intent = new Intent();
	
		// 立即通话
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			if (state == null) {
				hidCharge();
				return;
			}
			String rate = state.getCiGuaranteedRate();
			if (rate == null) {
				hidCharge();
				return;
			}
			if (eztCurrency < Double.parseDouble(state.getCiGuaranteedRate())) {
				hidCharge();
				return;
			}
			intent.setClass(mContext, PromptlyCallActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("currency", eztCurrency);
			intent.putExtra("info", state);
			intent.putExtra("deptId", doctor.getDocDeptId());
			startActivity(intent);
	}
	@OnClick(R.id.orderCall)
	public void orderCallClick(View v) {

		intent = new Intent();
	// 预约通话


			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			if (state == null) {
				hidCharge();
				return;
			}
			String rate1 = state.getCiGuaranteedRate();
			if (rate1 == null) {
				hidCharge();
				return;
			}
			if (eztCurrency < Double.parseDouble(state.getCiGuaranteedRate())) {
				hidCharge();
				return;
			}
			intent.setClass(mContext, PhoneDoctorTimeActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("state", state);
			intent.putExtra("info", doctor);
				startActivity(intent);
	}
	@OnClick(R.id.edit)
	public void eidtClick(View v) {
			intent = null;
	}
	/**
	 * 提示充值
	 */
	public void hidCharge() {
		AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
		ab.setTitle("温馨提示");
		ab.setMessage("您的医通币不足，请先充值");
		ab.setPositiveButton("充值", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(mContext, RechargeActivity.class);
				mContext.startActivity(intent);
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		ab.create().show();
	}

	@Override
	public void result(Object... object) {
		layout.setVisibility(View.VISIBLE);
		hideProgressToast();
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
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		switch (taskID) {
		case HttpParams.CHECK_TEL_DOC_STATE:// 获取医生状态
			if (flag) {// 成功
				state = (TelDocState) map.get("data");
				if (state != null) {
					String rate = state.getCiGuaranteedRate();
					String mt = state.getCiGuaranteedTime();
					String moneyOfMinute = state.getCiStandardRate();
					if (rate == null) {
						rate = "";
					}
					money.setText(rate + " 医通币");
					if (mt == null) {
						mt = "";
					}
					timeOfminute.setText("(" + mt + "分钟)");

					String strMoney = tvMoneyOfMinute.getText().toString();
					strMoney = strMoney.replace("@", rate + "");
					strMoney = strMoney.replace("*", mt + "");
					strMoney = strMoney.replace("#", moneyOfMinute + "个");
					tvMoneyOfMinute.setText(strMoney);
				}
			}
			break;
		case HttpParams.GET_CURRENCY_MONEY:// 获取账户信息
			if (flag) {
				eztCurrency = (Double) map.get("remain");
				if (eztCurrency == null) {
					eztCurrency = 0.0;
				}
			}
			break;
		}
	}

}
