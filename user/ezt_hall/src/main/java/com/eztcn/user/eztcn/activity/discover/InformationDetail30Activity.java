package com.eztcn.user.eztcn.activity.discover;

import org.json.JSONException;
import org.json.JSONObject;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.utils.js.WVJBWebViewClient;

/**
 * @title 资讯详情
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
@SuppressLint("JavascriptInterface")
public class InformationDetail30Activity extends FinalActivity {
	@ViewInject(R.id.webView)
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		ViewUtils.inject(InformationDetail30Activity.this);
		String url = "http://192.168.0.105/weixin/Dragoncard/CmsInfo/index/infoid/"+ getIntent().getStringExtra("infoId")+".html";
		webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
		showProgressToast();
		configWebView(webView, new MyWebViewClient(webView), url);
	}
	class MyWebViewClient extends WVJBWebViewClient {
		public MyWebViewClient(WebView webView) {

			super(webView, new WVJBWebViewClient.WVJBHandler() {
				@Override
				public void request(Object data, WVJBResponseCallback callback) {

				}

			});
			registerHandler("goBack", new WVJBWebViewClient.WVJBHandler() {

				@Override
				public void request(Object data, WVJBResponseCallback callback) {
					String jsonStr = String.valueOf(data);
					if (null != jsonStr) {
						try {
							JSONObject json = new JSONObject(jsonStr);
							//infoid
							finish();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}
			});


		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			hideProgressToast();
			webView.setOverScrollMode(WebView.OVER_SCROLL_IF_CONTENT_SCROLLS);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		hideProgressToast();
		return super.onKeyDown(keyCode, event);
	}
}
