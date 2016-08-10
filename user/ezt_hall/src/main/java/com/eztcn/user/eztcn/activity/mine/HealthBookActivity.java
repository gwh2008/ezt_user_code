package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 健康本
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class HealthBookActivity extends FinalActivity {

	@ViewInject(R.id.checkout)//, click = "onClick"
	private TextView checkout;
	@ViewInject(R.id.emr)//, click = "onClick"
	private TextView emr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthbook);
		ViewUtils.inject(HealthBookActivity.this);
		loadTitleBar(true, "健康本", null);
	}
	
	@OnClick(R.id.checkout)
	private void checkoutClick(View v){
		Intent intent = new Intent();
		intent.setClass(mContext, ExamReportActivity.class);
	}
	
	@OnClick(R.id.emr)
	private void emrClick(View v){
		Intent intent = new Intent();
		intent.setClass(mContext, MyMedicalRecordListActivity.class);
	}

//	public void onClick(View v) {
//		Intent intent = new Intent();
//		switch (v.getId()) {
//		case R.id.checkout:
//			intent.setClass(mContext, ExamReportActivity.class);
//			break;
//		case R.id.emr:
//			intent.setClass(mContext, MyMedicalRecordListActivity.class);
//			break;
//		}
//		if (intent != null) {
//			startActivity(intent);
//		}
//	}
}
