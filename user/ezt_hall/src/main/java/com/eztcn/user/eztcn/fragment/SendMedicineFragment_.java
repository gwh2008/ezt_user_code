package com.eztcn.user.eztcn.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;

/**
 * @title 首页
 * @describe
 * @author ezt
 * @created 2014年12月11日
 */
public class SendMedicineFragment_ extends FinalFragment implements OnClickListener {

	private View rootView;

	private WebView sendmedicine_webView;
	private String url = "http://192.168.0.107:8080/appHtml5/dev/examples/tab-bar/index.html";

	public static SendMedicineFragment_ newInstance() {
		SendMedicineFragment_ fragment = new SendMedicineFragment_();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Activity activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.sendmedicine, null);// 缓存Fragment

		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView();
		
		return rootView;
	}

	private void initView() {

		sendmedicine_webView = (WebView) rootView
				.findViewById(R.id.sendmedicine_webView);
		if (BaseApplication.getInstance().isNetConnected) {
			// initWebConfig();
			initWebConfig_();
		
		} else {
			sendmedicine_webView.setVisibility(View.GONE);
			Toast.makeText(getActivity(), getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根

	}

private void initWebConfig_(){
		sendmedicine_webView.setWebViewClient(new WebViewClient());
		// webView.loadUrl("http://news.baidu.com/");
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
		sendmedicine_webView.setWebChromeClient(new WebChromeClient() {

			// 配置权限（同样在WebChromeClient中实现）
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

		});
	}
	/**
	 * 初始化 vebview配置
	 */
	private void initWebConfig() {
		WebSettings webSettings = sendmedicine_webView.getSettings();
		webSettings.setDomStorageEnabled(true);
		// User settings
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setUseWideViewPort(true);// 关键点

		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
		webSettings.setAllowFileAccess(true); // 允许访问文件
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		webSettings.setSupportZoom(true); // 支持缩放
		webSettings.setLoadWithOverviewMode(true);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		Log.d("SendMedicineActivity", "densityDpi = " + mDensity);
		if (mDensity == 240) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == DisplayMetrics.DENSITY_TV) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		}

		/**
		 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
		 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
		 */

		sendmedicine_webView.setWebChromeClient(client);
		if (null != url) {
			sendmedicine_webView.loadUrl(url);
			// showProgressToast();
		}
	}

	private WebChromeClient client = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			getActivity().setProgress(newProgress * 1000);
			if (newProgress == 100) {
				// hideProgressToast();
			}
		};

		@Override
		public boolean onJsTimeout() {
			Toast.makeText(getActivity(), "链接超时，请稍后再实", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	};

}
