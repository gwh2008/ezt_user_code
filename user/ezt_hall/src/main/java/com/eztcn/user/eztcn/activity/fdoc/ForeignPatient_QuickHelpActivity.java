package com.eztcn.user.eztcn.activity.fdoc;

import java.util.Map;
import java.util.regex.Pattern;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.SucHintActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title 快速求助
 * @describe (外地患者服务)
 * @author ezt
 * @created 2015年2月27日
 */
public class ForeignPatient_QuickHelpActivity extends FinalActivity implements
		IHttpResult {//OnClickListener

	@ViewInject(R.id.bt)//, click = "onClick"
	private Button bt;

	@ViewInject(R.id.name_et)
	private EditText etName;

	@ViewInject(R.id.phone_et)
	private EditText etPhone;

	@ViewInject(R.id.ill_describe_et)
	private EditText etIllDescribe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quickhelp);
		ViewUtils.inject(ForeignPatient_QuickHelpActivity.this);
		loadTitleBar(true, "快速求助", null);
	}

	/**
	 * 提交数据
	 */
	private void postData(String name, String tel, String intro) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("name", name);
//		params.put("mobile", tel);
//		params.put("desc", intro);
		RequestParams params=new RequestParams();
		params.addBodyParameter("name",name);
		params.addBodyParameter("mobile",tel);
		params.addBodyParameter("desc",intro);
//		IForeignPatient api = new ForeignPatientImpl();
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.quickHelp(params, this);
	}
	@OnClick(R.id.bt)
	private void btnClick(View v){

		String name = etName.getText().toString();
		String tel = etPhone.getText().toString();
		String intro = etIllDescribe.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(mContext, "请输入您的姓名", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(tel)) {
			Toast.makeText(mContext, "请输入您的手机号码", Toast.LENGTH_SHORT).show();
			return;
		} else if (!Pattern.matches(getString(R.string.validate_mobile), tel)) {
			Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(intro)) {
			Toast.makeText(mContext, "请输入病情描述", Toast.LENGTH_SHORT).show();
			return;
		} else {
			postData(name, tel, intro);
			showProgressToast();
		}

	
	}
//	@Override
//	public void onClick(View v) {	String name = etName.getText().toString();
//	String tel = etPhone.getText().toString();
//	String intro = etIllDescribe.getText().toString();
//
//	if (TextUtils.isEmpty(name)) {
//		Toast.makeText(mContext, "请输入您的姓名", Toast.LENGTH_SHORT).show();
//		return;
//	} else if (TextUtils.isEmpty(tel)) {
//		Toast.makeText(mContext, "请输入您的手机号码", Toast.LENGTH_SHORT).show();
//		return;
//	} else if (!Pattern.matches(getString(R.string.validate_mobile), tel)) {
//		Toast.makeText(mContext, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
//		return;
//	} else if (TextUtils.isEmpty(intro)) {
//		Toast.makeText(mContext, "请输入病情描述", Toast.LENGTH_SHORT).show();
//		return;
//	} else {
//		postData(name, tel, intro);
//		showProgressToast();
//	}}

	@Override
	public void result(Object... object) {
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					startActivity(new Intent(
							ForeignPatient_QuickHelpActivity.this,
							SucHintActivity.class).putExtra("type", 6));
				} else {
					Toast.makeText(mContext, map.get("msg").toString(),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
	}

}
