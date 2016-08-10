package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 卡使用
 * @describe
 * @author ezt
 * @created 2015年6月18日
 */
public class CardUsedActivity extends FinalActivity implements OnClickListener {

	@ViewInject(R.id.web)
	private WebView web;

	private TextView tvBack;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardused);
		ViewUtils.inject(CardUsedActivity.this);
		String title = getIntent().getStringExtra("title");
		loadTitleBar(true, title, null);
		tvBack = (TextView) findViewById(R.id.left_btn);
		tvBack.setOnClickListener(this);
		String url = getIntent().getStringExtra("url");

		web.getSettings().setJavaScriptEnabled(true);
		web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		web.setScrollbarFadingEnabled(true);
		web.getSettings().setDomStorageEnabled(true);
		web.getSettings().setAppCacheMaxSize(1024 * 1024 * 8); // cache size:8MB
		web.getSettings().setAppCachePath(
				"/data/data/" + getPackageName() + "/cache");
//		web.getSettings().setPluginsEnabled(true);// 支持所有版本
		web.getSettings().setPluginState(WebSettings.PluginState.ON);// 支持所有版本2016-02-26
//		web.addJavascriptInterface(new openMethod(), "openMethod");
		web.setWebViewClient(new WebViewClient() {

			// 当点击链接时onPageStarted和shouldOverrideUrlLoading都执行，但返回时onPageStarted会执行，shouldOverrideUrlLoadin不执行了
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
  
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				String ss = "";
				// showProgressToast();
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
		web.setWebChromeClient(new WebChromeClient());
		web.loadUrl(url);
		showProgressToast();
	}

	/**
	 * 提供方法供JS调用
	 */
	// private final class openMethod {
	//
	// /**
	// * 支付
	// */
	// @JavascriptInterface
	// public void toPlay(String test) {
	// }
	//
	// }
	
	

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				web.setVisibility(View.VISIBLE);
				hideProgressToast();
			}
			// else {
			// if (progressbar.getVisibility() == GONE)
			// progressbar.setVisibility(VISIBLE);
			// progressbar.setProgress(newProgress);
			// }
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	public void onBackPressed() {
		if (web.canGoBack()) {
			web.goBack();
		} else {
			hideProgressToast();
			super.onBackPressed();
		}

	}

	@Override
	public void onClick(View v) {
		onBackPressed();
	}

}
