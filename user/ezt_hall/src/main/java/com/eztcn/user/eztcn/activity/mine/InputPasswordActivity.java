package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;

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
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;

/**
 * @title 找回密码确认
 * @describe
 * @author ezt
 * @created 2014年12月26日
 */
public class InputPasswordActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.cancel)//, click = "onClick"
	private Button cancel;
	@ViewInject(R.id.affirm)//, click = "onClick"
	private Button affirm;

	@ViewInject(R.id.password)
	private EditText etPw;// 密码

	@ViewInject(R.id.affirmPwd)
	private EditText etPw2;// 确认密码

	private String userId = "";// 用户id
	private String verCode = "";// 验证码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputpassword);
		ViewUtils.inject(InputPasswordActivity.this);
		loadTitleBar(false, "输入密码", null);
		userId = getIntent().getStringExtra("userid");
		verCode = getIntent().getStringExtra("checkCode");
	}
	
	@OnClick(R.id.cancel)
	private void cancelClick(View v){
		hintDialog();
	}
	
	@OnClick(R.id.affirm)
	private void affirmClick(View v){
		String pw = etPw.getText().toString().trim();
		String pw2 = etPw2.getText().toString().trim();

		if (TextUtils.isEmpty(pw)) {
			Toast.makeText(mContext, "请输入修改的密码", Toast.LENGTH_SHORT).show();
			return;
		} else if (pw.length() < 6) {
			Toast.makeText(mContext, "请输入的密码不小于6位", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (TextUtils.isEmpty(pw2)) {
			Toast.makeText(mContext, "请再次输入密码", Toast.LENGTH_SHORT).show();
			return;
		} else if (!pw2.equals(pw)) {
			Toast.makeText(mContext, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT)
					.show();
			etPw.setText("");
			etPw2.setText("");
			etPw.setFocusable(true);
			return;
		} else if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		else {
			pw = MD5.getMD5ofStr(pw);
//			HashMap<String, Object> params = new HashMap<String, Object>();
			RequestParams params=new RequestParams();
			params.addBodyParameter("userid", userId);
			params.addBodyParameter("checkCode", verCode);
			params.addBodyParameter("checkType", "mobile");
			params.addBodyParameter("password", pw);
			params.addBodyParameter("confirmPassword", pw);
			new UserImpl().FindPassWord(params, this);

		}
	}

//	public void onClick(View v) {
//		if (v == cancel) {
//			hintDialog();
//		} else if (v == affirm) {
//			String pw = etPw.getText().toString().trim();
//			String pw2 = etPw2.getText().toString().trim();
//
//			if (TextUtils.isEmpty(pw)) {
//				Toast.makeText(mContext, "请输入修改的密码", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (pw.length() < 6) {
//				Toast.makeText(mContext, "请输入的密码不小于6位", Toast.LENGTH_SHORT)
//						.show();
//				return;
//			} else if (TextUtils.isEmpty(pw2)) {
//				Toast.makeText(mContext, "请再次输入密码", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (!pw2.equals(pw)) {
//				Toast.makeText(mContext, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT)
//						.show();
//				etPw.setText("");
//				etPw2.setText("");
//				etPw.setFocusable(true);
//				return;
//			} else if (!BaseApplication.getInstance().isNetConnected) {
//				Toast.makeText(mContext, getString(R.string.network_hint),
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			else {
//				pw = MD5.getMD5ofStr(pw);
//				HashMap<String, Object> params = new HashMap<String, Object>();
//				params.put("userid", userId);
//				params.put("checkCode", verCode);
//				params.put("checkType", "mobile");
//				params.put("password", pw);
//				params.put("confirmPassword", pw);
//				new UserImpl().FindPassWord(params, this);
//
//			}
//
//		}
//
//	}

	/**
	 * 取消提示框
	 */
	private void hintDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				InputPasswordActivity.this);
		builder.setIcon(android.R.drawable.ic_dialog_alert).setTitle("提示")
				.setMessage("确定取消修改密码？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						setResult(11);
						finish();
					}
				}).setNegativeButton("取消", null);
		builder.create().show();

	}

	@Override
	public void onBackPressed() {
		hintDialog();
	}

	@Override
	public void result(Object... object) {
		boolean isSuc=false;
		if(object[1]!=null){
			isSuc= (Boolean) object[1];
		}
		Map<String, Object> map = (Map<String, Object>) object[2];
		if (isSuc) {// 成功
			boolean flag=false;
			if(map!=null&&map.get("flag")!=null){
				flag= (Boolean) map.get("flag");
			}
			if (flag) {// 成功
				Toast.makeText(InputPasswordActivity.this, "找回密码成功",
						Toast.LENGTH_SHORT).show();
				setResult(11);
				BackHome(this,0);
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
//				finish();
			} else {
				Toast.makeText(InputPasswordActivity.this,
						"网络请求错误", Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(InputPasswordActivity.this, object[3].toString(),
					Toast.LENGTH_SHORT).show();
		}

	}
}
