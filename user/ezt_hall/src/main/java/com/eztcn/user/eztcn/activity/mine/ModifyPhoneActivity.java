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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.utils.Constant;

public class ModifyPhoneActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.newPhone)
	private EditText newPhone;
	@ViewInject(R.id.securityCode)
	private EditText securityCode;
	@ViewInject(R.id.getSecurityCode)//, click = "onClick"
	private TextView getSecurityCode;
	@ViewInject(R.id.vPassword)
	private EditText vPassword;
	@ViewInject(R.id.affirmModify)//, click = "onClick"
	private Button affirmModify;
	@ViewInject(R.id.empty_telphone)
	private ImageView empty_telphone;

	private Timer timer;
	private TimerTask timerTask;
	private int runTime;// 验证码倒计时
	private UserImpl userImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifyphone30);
		ViewUtils.inject(ModifyPhoneActivity.this);
		loadTitleBar(true, "修改手机号", null);
		userImpl = new UserImpl();
	}
	
	@OnClick(R.id.getSecurityCode)
	private void getSecurityCodeClick(View v){
		judgePhone();
	}
	
	@OnClick(R.id.affirmModify)
	private void affirmModifyClick(View v){
		modifyPhone();
	}
	@OnClick(R.id.empty_telphone)
	private void emptyTelphoneClick(View v){
		//
		newPhone.setText("");
		newPhone.setHint("请输入您手机号");
	}
//	public void onClick(View v) {
//		if (v.getId() == R.id.getSecurityCode) {// 获取手机验证码
//			judgePhone();
//		} else {// 修改
//			modifyPhone();
//		}
//	}

	/**
	 * 获取验证码
	 */
	public void getSecurity() {
		if(null==BaseApplication.patient){
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("mobile", newPhone.getText().toString());
		//用户端 修改手机号 验证码
		params.addBodyParameter("eztCode", MD5.getMD5ofStr("ezt"+newPhone.getText().toString()));
		userImpl.getModifyPhoneCode(params, this);
		showProgressToast();
	}

	/**
	 * 判断手机是否被注册
	 */
	public void judgePhone() {
		if (TextUtils.isEmpty(newPhone.getText().toString())) {
			Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isPhone(newPhone.getText().toString())) {
			Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("mobile", newPhone.getText().toString());
		userImpl.verifyPhone(params, this);
		getSecurityCode.setEnabled(false);
		showProgressToast();
	}

	/**
	 * 修改手机号
	 */
	public void modifyPhone() {
		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		if (TextUtils.isEmpty(newPhone.getText().toString())) {
			Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isPhone(newPhone.getText().toString())) {
			Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(securityCode.getText().toString())) {
			Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (securityCode.getText().toString().length() != 6) {
			Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(vPassword.getText().toString())) {
			Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (vPassword.getText().toString().trim().length() < 6) {
			Toast.makeText(mContext, "请输入6~20位的密码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("mobile", newPhone.getText().toString());
		params.addBodyParameter("checkCode", securityCode.getText().toString());
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("paasword", MD5.getMD5ofStr(vPassword.getText().toString()));
		userImpl.userModifyPhone(params, this);
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
		case HttpParams.VERIFY_PHONE:// 验证手机是否已注册
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
		case HttpParams.MODIFY_PHONE_SECURITYCODE:// 获取手机验证码
			getSecurityCode.setEnabled(true);
			Map<String, Object> map = (Map<String, Object>) obj[2];
			if (map == null || map.size() == 0) {
				Toast.makeText(getApplicationContext(), "发送异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean flag = (Boolean) map.get("flag");
			if (flag) {
				msg = "短信已发送，请注意查收...";
				codeTimer();
			} else {
				msg = (String) map.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			break;
		case HttpParams.MODIFY_PHONE:// 修改手机号
			Map<String, Object> regMap = (Map<String, Object>) obj[2];
			if (regMap == null || regMap.size() == 0) {
				Toast.makeText(getApplicationContext(), "注册异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean regFlag = (Boolean) regMap.get("flag");
			if (regFlag) {
				msg = "手机号修改成功";
				BackHome(this,0);
				SystemPreferences.remove("userName");
				SystemPreferences.remove("userPwd");
				BaseApplication.patient = null;
				finish();
			} else {
				msg = (String) regMap.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}
}
