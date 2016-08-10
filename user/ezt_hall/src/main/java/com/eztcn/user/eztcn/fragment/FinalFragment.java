/**
 * 
 */
package com.eztcn.user.eztcn.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.baidu.mobstat.StatService;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.utils.js.WVJBWebViewClient;

/**
 * @author Liu Gang
 * 
 *         2016年1月25日 下午4:19:10
 * 
 */

public class FinalFragment extends Fragment {
//	private WebView webView;
	// 2016-1-25 百度 用户偏好页面停留时间统计
	@Override
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@SuppressLint("SetJavaScriptEnabled") protected void configWebView(WebView webView,
			WVJBWebViewClient webViewClient, String htmUrl) {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient());
		webView.loadUrl(htmUrl);
		webViewClient.enableLogging();
		webView.setWebViewClient(webViewClient);
//		this.webView = webView;
	}
	
	@Override
		public void onStop() {
			super.onStop();
			FinalActivity ss=(FinalActivity) getActivity();
			ss.hideProgressToast();
		}
	
	@Override
		public void onDestroy() {
			super.onDestroy();
			FinalActivity ss=(FinalActivity) getActivity();
			ss.hideProgressToast();
		}
	
	
}
