package com.eztcn.user.eztcn.activity.discover;

import android.os.Bundle;
import android.webkit.WebView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;

/**
 * @title 大数据阅读
 * @describe
 * @author ezt
 * @created 2015年3月26日
 */
public class BigDataReadActivity extends FinalActivity {

	@ViewInject(R.id.web)
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bigdata_read);
		ViewUtils.inject(BigDataReadActivity.this);
		loadTitleBar(true, "健康大数据", null);
	}
}
