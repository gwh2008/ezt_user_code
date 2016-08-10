package com.eztcn.user.eztcn.activity.mine;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import android.content.Context;
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
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.hall.activity.loginSetting.ChangeSuccessActivity;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.utils.ToastUtils;
/**
 * @title 修改密码
 * @describe
 * @author ezt
 * @created 2014年12月15日
 */
public class PWDModifyActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.cancel)//, click = "onClick"
	private Button cancel;
	@ViewInject(R.id.affirm)//, click = "onClick"
	private Button affirm;

	@ViewInject(R.id.oldPassword)
	private EditText oldPassword;
	@ViewInject(R.id.newPassword)
	private EditText newPassword;
	@ViewInject(R.id.affirmPassword)
	private EditText affirmPassword;
	@ViewInject(R.id.current_account)
	private TextView current_account;
	private Context context=PWDModifyActivity.this;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password30);
		ViewUtils.inject(PWDModifyActivity.this);
		loadTitleBar(true, "修改密码", null);
		setMobile();
	}
	/**
	 * 设置手机号码。
	 */
	private void setMobile() {
		current_account.setText(BaseApplication.patient.getEpMobile());
	}

	@OnClick(R.id.cancel)
	private void cancelClick(View v){
		finish();
	}
	
	@OnClick(R.id.affirm)
	private void affirmClick(View v){
		String oldPw = oldPassword.getText().toString();
		String newPw = newPassword.getText().toString();
		String newPw2 = affirmPassword.getText().toString();

		if (TextUtils.isEmpty(oldPw)) {
			Toast.makeText(PWDModifyActivity.this, "请输入原密码",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(newPw)) {
			Toast.makeText(PWDModifyActivity.this, "请输入新密码",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (newPw.length() < 6) {
			Toast.makeText(PWDModifyActivity.this, "密码不小于6位",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(newPw2)) {
			Toast.makeText(PWDModifyActivity.this, "请确认密码",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!newPw2.equals(newPw)) {
			Toast.makeText(PWDModifyActivity.this, "两次输入的密码不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
			newPassword.setText("");
			affirmPassword.setText("");
			newPassword.setFocusable(true);
			return;

		} else if (newPw2.equals(oldPw)) {
			Toast.makeText(PWDModifyActivity.this, "新密码与原密码不能一致",
					Toast.LENGTH_SHORT).show();
			newPassword.setText("");
			affirmPassword.setText("");
			newPassword.setFocusable(true);
			return;
		} else if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			RequestParams params=new RequestParams();
			params.addBodyParameter("oldPassword", MD5.getMD5ofStr(oldPw));
			params.addBodyParameter("userid", BaseApplication.patient.getUserId() + "");
			params.addBodyParameter("password", MD5.getMD5ofStr(newPw));
			params.addBodyParameter("confirmPassword", MD5.getMD5ofStr(newPw2));
			new UserImpl().ModifyPassWord(params, this);
		}
	}
	@Override
	public void result(Object... object) {
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (isSuc) {// 成功
				boolean flag = (Boolean) map.get("flag");
				if (flag) {// 成功
					ToastUtils.shortToast(context,"密码修改成功...");
					startActivity(new Intent(context,ChangeSuccessActivity.class));
					PWDModifyActivity.this.finish();
				} else {
					Toast.makeText(PWDModifyActivity.this,
							map.get("msg").toString(), Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(PWDModifyActivity.this, object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
