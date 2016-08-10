package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;
import java.util.regex.Pattern;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.graphics.Paint;
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
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
/**
 * @title 忘记密码
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class ForgetPasswordActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.affirm)//, click = "onClick"
	private Button affirm;// 确定
	@ViewInject(R.id.autonymFound)//, click = "onClick"
	private TextView autonymFound;// 验证实名

	@ViewInject(R.id.getSecurityCode)//, click = "onClick"
	private TextView tvGetVerCode;// 获取验证码

	@ViewInject(R.id.phone)
	private EditText etPhone;// 注册的手机号码

	@ViewInject(R.id.secrityCode)
	private EditText etVerCode;// 验证码

	private String userId = "";// 用户id

	// 倒计时
	private int time = EZTConfig.GET_VERCODE_TIME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpassword);
		ViewUtils.inject(ForgetPasswordActivity.this);
		loadTitleBar(true, "忘记密码", null);
		autonymFound.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}
	
	@OnClick(R.id.affirm)
	private void affirmClick(View v){
		if (TextUtils.isEmpty(etPhone.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请输入手机号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isPhone(etPhone.getText().toString())) {
			Toast.makeText(getApplicationContext(), "请输入正确的手机号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		String strVerCode = etVerCode.getText().toString();
		if (TextUtils.isEmpty(userId)) {
			Toast.makeText(getApplicationContext(), "请获取验证码",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(strVerCode)) {
			Toast.makeText(getApplicationContext(), "请输入验证码",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			RequestParams params=new RequestParams();
//			HashMap<String, Object> params = new HashMap<String, Object>();
			params.addBodyParameter("userId", userId);
			params.addBodyParameter("checkCode", strVerCode);
			new UserImpl().VerifyFPwVerCode(params, this);
		}
	}
	
	@OnClick(R.id.autonymFound)
	private void autonymFoundClick(View v){
		Intent intent2 = new Intent();
		intent2.setClass(mContext, AutonymFoundActivity.class);
		startActivity(intent2);
	}
	
	@OnClick(R.id.getSecurityCode)
	private void getSecurityCodeClick(View v){
		String strPhoneNum = etPhone.getText().toString();

		if (TextUtils.isEmpty(strPhoneNum)) {
			Toast.makeText(getApplicationContext(), "请输入手机号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!StringUtil.isPhone(strPhoneNum)) {
			Toast.makeText(getApplicationContext(), "请输入正确的手机号",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Pattern.matches(getString(R.string.validate_mobile),
				strPhoneNum)) {
			Toast.makeText(getApplicationContext(), "手机号码格式不正确",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		else {
//			HashMap<String, Object> params = new HashMap<String, Object>();
			RequestParams params=new RequestParams();
			params.addBodyParameter("mobile", strPhoneNum);
//			用户端忘记密码获取验证码 eztcode md5加密
			params.addBodyParameter("eztCode", MD5.getMD5ofStr("ezt"+strPhoneNum));
			new UserImpl().getFPwVerCode(params, this);
			tvGetVerCode.setEnabled(false);
		}
	}


	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.affirm:// 确认
			if (TextUtils.isEmpty(etPhone.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请输入手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!StringUtil.isPhone(etPhone.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请输入正确的手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String strVerCode = etVerCode.getText().toString();
			if (TextUtils.isEmpty(userId)) {
				Toast.makeText(getApplicationContext(), "请获取验证码",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (TextUtils.isEmpty(strVerCode)) {
				Toast.makeText(getApplicationContext(), "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!BaseApplication.getInstance().isNetConnected) {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
//				HashMap<String, Object> params = new HashMap<String, Object>();
				RequestParams params=new RequestParams();
				params.addBodyParameter("userId", userId);
				params.addBodyParameter("checkCode", strVerCode);
				new UserImpl().VerifyFPwVerCode(params, this);
			}

			break;
		case R.id.autonymFound:// 实名信息找回
			Intent intent2 = new Intent();
			intent2.setClass(mContext, AutonymFoundActivity.class);
			startActivity(intent2);
			break;

		case R.id.getSecurityCode:// 获取验证码
			String strPhoneNum = etPhone.getText().toString();

			if (TextUtils.isEmpty(strPhoneNum)) {
				Toast.makeText(getApplicationContext(), "请输入手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!StringUtil.isPhone(strPhoneNum)) {
				Toast.makeText(getApplicationContext(), "请输入正确的手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!Pattern.matches(getString(R.string.validate_mobile),
					strPhoneNum)) {
				Toast.makeText(getApplicationContext(), "手机号码格式不正确",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (!BaseApplication.getInstance().isNetConnected) {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
				return;
			}

			else {
//				HashMap<String, Object> params = new HashMap<String, Object>();
				RequestParams params=new RequestParams();
				params.addBodyParameter("mobile", strPhoneNum);
//				用户端忘记密码获取验证码 eztcode md5加密
				params.addBodyParameter("eztCode", MD5.getMD5ofStr("ezt"+strPhoneNum));
				new UserImpl().getFPwVerCode(params, this);
				tvGetVerCode.setEnabled(false);
			}

			break;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			time--;
			if (time != 0) {
				tvGetVerCode.setText("" + time);
				handler.sendEmptyMessageDelayed(1, 1000);
			} else {
				tvGetVerCode.setEnabled(true);
				tvGetVerCode.setText("重新获取");
				time = EZTConfig.GET_VERCODE_TIME;
				handler.removeMessages(1);
			}

		}

	};

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc=false;
		if(object[1]!=null){
			isSuc= (Boolean) object[1];
		}
		switch (type) {
		case HttpParams.GET_FPW_VERCODE:// 获取找回密码验证码
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (isSuc) {// 成功
				boolean flag=false;
				if(map!=null&&map.get("flag")!=null){
					flag= (Boolean) map.get("flag");
				}
				if (flag) {// 成功
					Toast.makeText(ForgetPasswordActivity.this,
							"发送成功，请注意查收您的短信！", Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessage(1);
					userId = (String) map.get("userId");
				} else {
					tvGetVerCode.setEnabled(true);
					if(map!=null&&map.get("msg")!=null){
						Toast.makeText(ForgetPasswordActivity.this,
								map.get("msg").toString(), Toast.LENGTH_SHORT)
								.show();
					}
				}

			} else {
				if(object[3]!=null){
					Toast.makeText(ForgetPasswordActivity.this,
							object[3].toString(), Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case HttpParams.VERIFY_FPW_VERCODE:// 验证找回密码验证码
			Map<String, Object> map2 = (Map<String, Object>) object[2];
			if (isSuc) {// 成功
				boolean flag =false;
				if(null!=map2){
					if(map2.containsKey("flag")&&null!=map2.get("flag"))
					flag=(Boolean) map2.get("flag");
				}
				if (flag) {// 成功
					Toast.makeText(ForgetPasswordActivity.this, "验证成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(mContext, InputPasswordActivity.class);
					intent.putExtra("userid", userId);
					intent.putExtra("checkCode", etVerCode.getText().toString());
					startActivityForResult(intent, 1);
				} else {
					if(map2.containsKey("msg")&&null!=map2.get("msg")){
						Toast.makeText(ForgetPasswordActivity.this,
								map2.get("msg").toString(), Toast.LENGTH_SHORT)
								.show();
					}else{
						Toast.makeText(ForgetPasswordActivity.this,
								"服务器端错误，请稍后再试", Toast.LENGTH_SHORT)
								.show();
					}
				}

			} else {
				Toast.makeText(ForgetPasswordActivity.this,
						object[3].toString(), Toast.LENGTH_SHORT).show();
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1 && arg1 == 11) {
			finish();
		}

	}
}
