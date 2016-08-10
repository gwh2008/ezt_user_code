package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 药给送
 * @describe
 * @author ezt
 * @created 2015年4月17日
 */
public class SendMedicineActivity extends FinalActivity {
	/**
	 * 药给送展示界面
	 */
	@ViewInject(R.id.sendmedicine_webView)
	private WebView sendmedicine_webView;
	/**
	 * 网址
	 */
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmedicine);
		ViewUtils.inject(SendMedicineActivity.this);
		loadTitleBar(true, "药给送", null);
		// url=getIntent().getStringExtra("send_url");
		url = "http://192.168.0.107:8080/appHtml5/dev/examples/tab-bar/index.html";
		if (BaseApplication.getInstance().isNetConnected) {
			initWebConfig();
		} else {
			sendmedicine_webView.setVisibility(View.GONE);
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化 vebview配置
	 */
	private void initWebConfig() {
		sendmedicine_webView.setWebViewClient(new WebViewClient());
		// webView.loadUrl("http://news.baidu.com/");
		showProgressToast();
		sendmedicine_webView.loadUrl(url);

		WebSettings webSettings = sendmedicine_webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		/**
		 * 此处很重要，必须要
		 */
		sendmedicine_webView.setWebChromeClient(new WebChromeClient());
		/**
		 * 以下部分可以不要
		 */
		// //启用数据库
		// webSettings.setDatabaseEnabled(true);
		// String dir = this.getApplicationContext().getDir("database",
		// Context.MODE_PRIVATE).getPath();
		//
		// //启用地理定位
		// webSettings.setGeolocationEnabled(true);
		// //设置定位的数据库路径
		// webSettings.setGeolocationDatabasePath(dir);

		/**
		 * 此处很重要，必须要
		 */
		// ***最重要的方法，一定要设置，这就是出不来的主要原因
		webSettings.setDomStorageEnabled(true);
//		sendmedicine_webView.setWebChromeClient(new WebChromeClient() {
//
//			// 配置权限（同样在WebChromeClient中实现）
//			@Override
//			public void onGeolocationPermissionsShowPrompt(String origin,
//					Callback callback) {
//				callback.invoke(origin, true, false);
//				super.onGeolocationPermissionsShowPrompt(origin, callback);
//			}
//
//		});
		sendmedicine_webView.setWebChromeClient(client);
	}

	private WebChromeClient client = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			SendMedicineActivity.this.setProgress(newProgress * 1000);
			if (newProgress == 100) {
				hideProgressToast();
			}
		};

		@Override
		public boolean onJsTimeout() {

			hideProgressToast();
			Toast.makeText(SendMedicineActivity.this, "链接超时，请稍后再实",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	};

	protected void onPause() {
		hideProgressToast();
		super.onPause();
	};
}
