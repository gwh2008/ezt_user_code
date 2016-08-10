package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 建行用户专享
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class CCBUserActivity extends FinalActivity {

	@ViewInject(R.id.title_tv)
	private TextView title;// 标题
	@ViewInject(R.id.activate)
	// , click = "onClick"
	private TextView activate;// 激活
	@ViewInject(R.id.specialLine)
	// , click = "onClick"
	private LinearLayout specialLine;// 健康专线

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ccbuser);
		ViewUtils.inject(CCBUserActivity.this);
		loadTitleBar(true, "建行用户专享", null);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			String bankName = savedInstanceState.getString("bankName");
			title.setText(bankName == null ? "银行用户专享" : bankName);
		}
	}

	@OnClick(R.id.activate)
	public void activateClick(View v) {
		// 激活
		Intent intent = new Intent(this, HealthCardActivateActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.specialLine)
	public void specialLineClick(View v) {
		hintTelDialog(getString(R.string.health_tel_num), "确定拨打健康服务电话？");
	}
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.activate:// 激活
	// Intent intent = new Intent(this, HealthCardActivateActivity.class);
	// startActivity(intent);
	// break;
	// case R.id.specialLine://
	// hintTelDialog(getString(R.string.health_tel_num), "确定拨打健康服务电话？");
	// break;
	// }
	// }
}
