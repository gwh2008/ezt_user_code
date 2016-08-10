package com.eztcn.user.eztcn.activity.home;

import java.util.Map;
import java.util.regex.Pattern;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.RechargeActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.common.Constant;

/**
 * @title 立即通话
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class PromptlyCallActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.affirmCall)//, click = "onClick"
	private Button affirmCall;

	@ViewInject(R.id.recharge)//, click = "onClick"
	private Button btRecharge;

	@ViewInject(R.id.phone)
	private EditText phone;
	@ViewInject(R.id.balance)
	private TextView balance;// 余额
	@ViewInject(R.id.callTime)
	private TextView callTime;

	@ViewInject(R.id.hint_tv)
	private TextView tvHint;

	private int type;
	private Doctor pd;
	private int eztCurrency;
	private int times;
	private double doctorFee;
	private double money;
	private String deptId;
	private String doctorId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promptlycall);
		ViewUtils.inject(PromptlyCallActivity.this);
		loadTitleBar(true, "立即通话", null);
		init();
		getCallTime();
	}

	/**
	 * 获取可播打分钟数
	 */
	public void getCallTime() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
			params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
			params.addBodyParameter("deptId", deptId);
			params.addBodyParameter("doctorId", doctorId);
		new PayImpl().getCllMinute(params, this);
		showProgressToast();
		phone.setText(BaseApplication.patient.getEpMobile());
	}

	public void init() {
		Bundle savedInstanceState = getIntent().getExtras();
		type = savedInstanceState.getInt("type", -1);
		if (type == 1) {// 电话医生列表
			Doctor pd = (Doctor) savedInstanceState
					.getSerializable("info");
			if (pd != null) {
				deptId = pd.getDocDeptId();
				doctorFee = pd.getMoneyOfMinute();
				doctorId = pd.getId();
				money = pd.getFees();
			}

		} else if (type == 2) {// 预约挂号医生列表
			TelDocState docState = (TelDocState) savedInstanceState
					.getSerializable("info");
			if (docState != null) {
				deptId = savedInstanceState.getString("deptId");
				doctorId = savedInstanceState.getString("docId");
				String mt = docState.getCiStandardRate();
				if (mt != null) {
					doctorFee = Double.parseDouble(mt);
				}
				String f = docState.getCiGuaranteedRate();
				if (f != null) {
					money = Integer.parseInt(f);
				}
			}
		}  
		String strHint = tvHint.getText().toString();
		strHint = strHint.replace("#", (int)doctorFee + "个");
		tvHint.setText(strHint);
	}
	@OnClick(R.id.affirmCall)
	public void affirmCallClick(View v) {
		Intent intent = new Intent();
		

			if (BaseApplication.patient == null) {
				HintToLogin(com.eztcn.user.hall.utils.Constant.LOGIN_COMPLETE);
				return;
			}
			if (times <= 0) {
				showResultView("您的医通币不足，请先充值");
				return;
			}
			String p = phone.getText().toString();
			if (TextUtils.isEmpty(p)) {
				Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!Pattern.matches(getString(R.string.validate_mobile), p)) {
				Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
				return;
			} else {
				affirmToCall();
			}
	}
	
	@OnClick(R.id.recharge)
	public void chargeClick(View v) {
			Intent intent = new Intent();
			intent.setClass(this, RechargeActivity.class);
			startActivity(intent);
	}
	/**
	 * 确定通话
	 */
	public void affirmToCall() {
		RequestParams params=new RequestParams();
			params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
			params.addBodyParameter("doctorId", doctorId);
			params.addBodyParameter("deptId", deptId);
			params.addBodyParameter("patientMobile", phone.getText().toString());
		new TelDocImpl().promptTeling(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		String msg;
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
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			Toast.makeText(mContext, getString(R.string.request_fail),
					Toast.LENGTH_SHORT).show();
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (taskID == HttpParams.GET_CURRENCY_MONEY) {// 获取账户信息
			if (flag) {
				Double ec = (Double) map.get("remain");
				if (ec == null) {
					eztCurrency = 0;
				} else {
					eztCurrency = (int) Math.floor(ec);
				}

			}
		} else if (taskID == HttpParams.GET_CALLMINUTE) {// 获取可拨打分钟数
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
			balance.setText(eztCurrency + " 医通币");
			callTime.setText(times + "分钟");
		} else {
			if (flag) {
				startActivityForResult(new Intent(PromptlyCallActivity.this,
						SucHintActivity.class).putExtra("type", 1), 5);
			} else {
				showResultView(map.get("msg").toString());
			}
		}
	}

	public void showResultView(String msg) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("温馨提示");
		ab.setMessage(msg);
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.create().show();
	}
}
