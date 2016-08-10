package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

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
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 健康卡激活
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class HealthCardActivateActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.healthcard)
	private EditText healthcard;
	@ViewInject(R.id.password)
	private EditText password;//
	@ViewInject(R.id.affirmActivate)
	// , click = "onClick"
	private Button affirmActivate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthcardactivate);
		ViewUtils.inject(HealthCardActivateActivity.this);
		loadTitleBar(true, "激活", null);
	}

	// public void onClick(View v) {
	@OnClick(R.id.affirmActivate)
	public void click(View v) {
		String cardNo = healthcard.getText().toString();
		String pw = password.getText().toString();
		if (TextUtils.isEmpty(cardNo)) {
			Toast.makeText(this, "请输入卡号", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(pw)) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		} else if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		} else {
			initialActiveCard(cardNo, pw);
			showProgressToast();
		}
	}

	/**
	 * 激活卡
	 */
	private void initialActiveCard(String cardNo, String cardPassword) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", ""
				+ BaseApplication.getInstance().patient.getUserId());
		params.addBodyParameter("cardNo", cardNo);
		params.addBodyParameter("cardPassword", cardPassword);
		EztServiceCardImpl api = new EztServiceCardImpl();

		api.activateCard(params, this);
	}

	@Override
	public void result(Object... object) {

		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					Intent intent = new Intent(this, SucHintActivity.class);
					intent.putExtra("type", 7);
					startActivity(intent);
				} else {
					if (map.get("msg") != null) {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}

				}

			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();

		}
		hideProgressToast();
	}
}
