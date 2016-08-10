package com.eztcn.user.eztcn.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 医生资料（医生站进入）
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class DoctorInfoActivity extends FinalActivity {

	@ViewInject(R.id.intro)
	private TextView intro; // 简介
	@ViewInject(R.id.goodAt)
	private TextView goodAt;// 擅长
	@ViewInject(R.id.eduBackground)
	private TextView eduBackground;// 教育背景
	@ViewInject(R.id.scienceSuccess)
	private TextView scienceSuccess;// 学术成就

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctorinfo);
		ViewUtils.inject(DoctorInfoActivity.this);
//		loadTitleBar(true, "医生资料", null);
		init();
	}

	public void init() {
		String docGoodAt = getIntent().getStringExtra("docGoodAt");
		String docIntro = getIntent().getStringExtra("docIntro");
		String docEducBg = getIntent().getStringExtra("docEducBg");
		String docSuc = getIntent().getStringExtra("docSuc");

		intro.setText(TextUtils.isEmpty(docIntro) ? "暂无相关信息" : docIntro);
		goodAt.setText(TextUtils.isEmpty(docGoodAt) ? "暂无相关信息" : docGoodAt);
		eduBackground.setText(TextUtils.isEmpty(docEducBg) ? "暂无相关信息"
				: docEducBg);
		scienceSuccess.setText(TextUtils.isEmpty(docSuc) ? "暂无相关信息" : docSuc);
	}
	

	@OnClick(R.id.close_bt)
	public void hos_closeClick(View v) {
		// 取消
		finish();
	}
}
