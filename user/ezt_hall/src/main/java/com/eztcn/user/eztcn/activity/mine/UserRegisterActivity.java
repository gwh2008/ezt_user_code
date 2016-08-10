package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.eztcn.user.eztcn.activity.MainActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;

/**
 * @title 用户注册
 * @describe
 * @author ezt
 * @created 2014年12月17日
 */
public class UserRegisterActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.phone)
	private EditText phone;
	@ViewInject(R.id.securityCode)
	private EditText securityCode;
	@ViewInject(R.id.password)
	private EditText password;
	@ViewInject(R.id.affirmPwd)
	private EditText affirmPwd;
	@ViewInject(R.id.getSecurityCode)//, click = "onClick"
	private TextView getSecurityCode;
	@ViewInject(R.id.affirmReg)//, click = "onClick"
	private Button affirmReg;
	@ViewInject(R.id.register_close_image)//, click = "onClick"
	private ImageView register_close_image;

	private UserImpl userImpl;
	private int clickType;
	private Timer timer;
	private TimerTask timerTask;
	private int runTime;// 验证码倒计时

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_new);
		ViewUtils.inject(UserRegisterActivity.this);
		userImpl = new UserImpl();
	}

	@OnClick(R.id.getSecurityCode)
	private void getSecurityCodeClick(View v){
		judgePhone();
	}
	
	@OnClick(R.id.affirmReg)
	private void affirmRegClick(View v){
		if (judgeParams()) {
			userRegister();
		}
	}
	@OnClick(R.id.register_close_image)
	private void register_close_imageClick(View v){
		
		UserRegisterActivity.this.finish();
	}
	
//	public void onClick(View v) {
//		if (v.getId() == R.id.getSecurityCode) {
//			judgePhone();
//		} else {// 注册
//			if (judgeParams()) {
//				userRegister();
//			}
//		}
//	}

	/**affirmReg
	 * 获取验证码
	 */
	public void getSecurity() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("mobile", phone.getText().toString());
		params.addBodyParameter("eztCode", MD5.getMD5ofStr("ezt"+phone.getText().toString()));
		userImpl.getSecurityCode(params, this);
		showProgressToast();
	}

	/**
	 * 判断手机有效性
	 */
	public void judgePhone() {
		if (TextUtils.isEmpty(phone.getText().toString())) {
			Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isPhone(phone.getText().toString())) {
			Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("mobile", phone.getText().toString());
		userImpl.verifyPhone(params, this);
		getSecurityCode.setEnabled(false);
		showProgressToast();
	}

	/**
	 * 注册
	 */
	public void userRegister() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		// params.put("category", "11");
		params.addBodyParameter("mobile", phone.getText().toString());
		params.addBodyParameter("password", MD5.getMD5ofStr(password.getText().toString()));
		params.addBodyParameter("confirmPassword",
				MD5.getMD5ofStr(affirmPwd.getText().toString()));
		params.addBodyParameter("vcode", securityCode.getText().toString());
		params.addBodyParameter("V", "Y");// 参数(Y/N) 扫描二维码下载注册用到（活动）
		userImpl.userRegister(params, this);
		showProgressToast();
	}

	/**
	 * 登录
	 */
	public void userLogin() {
		showProgressToast();
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("username", phone.getText().toString());
		params.addBodyParameter("password", MD5.getMD5ofStr(affirmPwd.getText().toString()));
		UserImpl impl = new UserImpl();
		impl.userLogin(params, this);
	}

	/**
	 * 判断参数是否正确
	 */
	public boolean judgeParams() {
		if (TextUtils.isEmpty(phone.getText().toString())) {
			Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.isPhone(phone.getText().toString())) {
			Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(securityCode.getText().toString())) {
			Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (securityCode.getText().toString().length() != 6) {
			Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(password.getText().toString())) {
			Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (password.getText().toString().length() < 6
				|| password.getText().toString().length() > 20) {
			Toast.makeText(mContext, "密码长度必须为6~20位", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(affirmPwd.getText().toString())) {
			Toast.makeText(mContext, "请输入确认密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!password.getText().toString()
				.equals(affirmPwd.getText().toString())) {
			Toast.makeText(mContext, "前后密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	/**
	 * 注册成功提示
	 */
	private int intentFlag = 0;// 登录跳转标记

	public void regDialog(String msg) {
		AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
		ab.setTitle("注册成功");
		ab.setMessage(msg);
		ab.setIcon(R.drawable.ic_suc);
		ab.setPositiveButton("完善资料", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clickType = 1;
				intentFlag = 1;
				userLogin();
				dialog.cancel();
			}
		});
		ab.setNegativeButton("马上登录", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clickType = 1;
				intentFlag = 2;
				userLogin();
				dialog.cancel();
				// BackHome(mContext, 3);
				// finish();
				// Intent intent = new Intent(mContext,
				// UserLoginActivity.class);
				// startActivity(intent);
				// finish();
			}
		});
		ab.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (clickType == 0) {
					Intent intent = new Intent(mContext,LoginActivity.class);
					startActivity(intent);
					dialog.cancel();
					UserRegisterActivity.this.finish();
				}
			}
		});
		AlertDialog dialog = ab.create();
		dialog.show();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			Toast.makeText(getApplicationContext(), "注册异常", Toast.LENGTH_SHORT)
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
		case HttpParams.GET_SECURITYCODE:// 获取手机验证码
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
		case HttpParams.USER_REG:// 注册
			Map<String, Object> regMap = (Map<String, Object>) obj[2];
			if (regMap == null || regMap.size() == 0) {
				Toast.makeText(getApplicationContext(), "注册异常",
						Toast.LENGTH_SHORT).show();
				return;
			}
			boolean regFlag = (Boolean) regMap.get("flag");
			if (regFlag) {
				msg = "恭喜您，注册成功，请你选择登录或完善资料";
				regDialog(msg);
			} else {
				msg = (String) regMap.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			break;
		case HttpParams.USER_LOGIN:
			Map<String, Object> loginMap = (Map<String, Object>) obj[2];
			if (loginMap == null || loginMap.size() == 0) {
				finish();
				return;
			}
			boolean loginFlag = (Boolean) loginMap.get("flag");
			if (loginFlag) {
				Intent intent = new Intent();
				switch (intentFlag) {
				case 1:// 完善资料
					intent.setClass(mContext, AutonymAuthActivity.class);
					break;

				case 2:// 领取红包
					intent.setClass(mContext, MainActivity.class);
					intent.putExtra("selectIndex", 3);
					break;
				}
				startActivity(intent);
				UserRegisterActivity.this.finish();
			} else {
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				UserRegisterActivity.this.finish();
			}
			break;
		}
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
}
