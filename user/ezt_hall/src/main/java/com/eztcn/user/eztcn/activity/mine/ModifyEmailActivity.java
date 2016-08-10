package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.StringUtil;
public class ModifyEmailActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.title_tv)
	private TextView title;
	@ViewInject(R.id.email)
	private EditText email;
	@ViewInject(R.id.securityCode)
	private EditText securityCode;
	@ViewInject(R.id.getSecurityCode)//, click = "onClick"
	private TextView getSecurityCode;
	@ViewInject(R.id.affirmModify)//, click = "onClick"
	private Button affirmModify;

	private Timer timer;
	private TimerTask timerTask;
	private int runTime;// 验证码倒计时
	private UserImpl userImpl;

	private String emailStr;
	private int executeType;// 执行类型(1、绑定 2、解绑)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifyemail);
		ViewUtils.inject(ModifyEmailActivity.this);
		loadTitleBar(true, "绑定邮箱", null);
		userImpl = new UserImpl();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (BaseApplication.patient == null) {
			return;
		}
		emailStr = BaseApplication.user.getEuEmail();
		email.setText(emailStr);
		if (TextUtils.isEmpty(emailStr)) {
			executeType = 1;
			title.setText("绑定邮箱");
		} else {
			title.setText("解除邮箱绑定");
			executeType = 2;
			affirmModify.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.getSecurityCode)
	private void getSecurityCodeClick(View v){
		judgeEmail();
	}
	
	@OnClick(R.id.affirmModify)
	private void affirmModifyClick(View v){
		removeEmail();
	}
	/**
	 * 获取验证码
	 */
	public void getSecurity() {
		if (BaseApplication.patient == null) {
			HintToLogin(1);
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		if (executeType == 1) {// 绑定验证码
			params.addBodyParameter("email", email.getText().toString());
			userImpl.getBindEmailCode(params, this);
		} else {
			userImpl.getRemoveEmailCode(params, this);
		}
		showProgressToast();
	}

	/**
	 * 判断邮箱是否被注册
	 */
	public void judgeEmail() {
		if (BaseApplication.patient == null) {
			HintToLogin(1);
			return;
		}
		
		if (TextUtils.isEmpty(email.getText().toString())) {
			Toast.makeText(mContext, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isEmail(email.getText().toString())) {
			Toast.makeText(mContext, "邮箱格式有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("ehEmail", email.getText().toString());
		userImpl.VerifyEmail(params, this);
		getSecurityCode.setEnabled(false);
		showProgressToast();
	}

	/**
	 * 解除邮箱绑定
	 */
	public void removeEmail() {
		if (BaseApplication.patient == null) {
			HintToLogin(1);
			return;
		}
		if (TextUtils.isEmpty(email.getText().toString())) {
			Toast.makeText(mContext, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isEmail(email.getText().toString())) {
			Toast.makeText(mContext, "邮箱格式有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("checkCode", securityCode.getText().toString());
		userImpl.removeEmail(params, this);
		showProgressToast();
	}

	/**
	 * 验证码倒计时
	 */
	public void codeTimer() {
		getSecurityCode.setEnabled(false);
		runTime = 60;
		timerTask = new TimerTask() {

			@Override
			public void run() {
				runTime--;
				handler.sendEmptyMessage(runTime);
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);
	}

	/**
	 * 关闭计时器
	 */
	public void cancelTimerTask() {
		getSecurityCode.setEnabled(true);
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int num = msg.what;
			if (num == 0) {
				getSecurityCode.setText("获取验证码");
				cancelTimerTask();
				return;
			}
			getSecurityCode.setText(num + "s后重发");
		}

	};

	@Override
	protected void onDestroy() {
		cancelTimerTask();
		super.onDestroy();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			Toast.makeText(getApplicationContext(), "服务器繁忙", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		String msg;
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			getSecurityCode.setEnabled(true);
			return;
		}
		switch (taskID) {
		case HttpParams.VERIFY_EMAIL:// 验证邮箱是否已注册
			Map<String, Object> verifyMap = (Map<String, Object>) obj[2];
			if (verifyMap == null || verifyMap.size() == 0) {
				getSecurityCode.setEnabled(true);
				return;
			}
			boolean valid = (Boolean) verifyMap.get("flag");
			if (valid) {
				getSecurity();
			} else {
				getSecurityCode.setEnabled(true);
				Toast.makeText(getApplicationContext(),
						verifyMap.get("msg") + "", Toast.LENGTH_SHORT).show();
			}
			break;
		case HttpParams.BIND_EMAIL_SECURITYCODE:// 获取绑定邮箱验证码
			getSecurityCode.setEnabled(true);
			Map<String, Object> map = (Map<String, Object>) obj[2];
			if (map == null || map.size() == 0) {
				Toast.makeText(getApplicationContext(), "发送异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean flag = (Boolean) map.get("flag");
			if (flag) {
				msg = "邮箱验证码已发送至邮箱，请登录邮箱进行激活";
				finish();
			} else {
				msg = (String) map.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			break;
		case HttpParams.REMOVE_EMAIL_SECURITYCODE:// 解除绑定邮箱验证码
			Map<String, Object> regMap = (Map<String, Object>) obj[2];
			if (regMap == null || regMap.size() == 0) {
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean regFlag = (Boolean) regMap.get("flag");
			if (regFlag) {
				msg = "解除验证码已发送，请注意查收";
				codeTimer();
			} else {
				msg = (String) regMap.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			break;
		case HttpParams.REMOVE_EMAIL:// 解除邮箱
			Map<String, Object> removeMap = (Map<String, Object>) obj[2];
			if (removeMap == null || removeMap.size() == 0) {
				Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean removeFlag = (Boolean) removeMap.get("flag");
			if (removeFlag) {
				msg = "解除邮箱成功";
				BackHome(this,0);
				BaseApplication.patient = null;
				finish();
			} else {
				msg = (String) removeMap.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}
}
