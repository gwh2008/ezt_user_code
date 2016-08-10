/**
 * 
 */
package com.eztcn.user.eztcn.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.utils.js.WVJBWebViewClient;

/**
 * @author Liu Gang
 * 
 *         2016年2月25日 下午2:23:46
 * 
 */
@SuppressLint("JavascriptInterface")
public class EztTestActivity extends FinalActivity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		webView = (WebView) findViewById(R.id.webView);
		configWebView(webView, new MyWebViewClient(webView),
				"file:///android_asset/eztTest.html");
	}

	class MyWebViewClient extends WVJBWebViewClient {
		public MyWebViewClient(WebView webView) {

			// support js send
			super(webView, new WVJBWebViewClient.WVJBHandler() {

				@Override
				public void request(Object data, WVJBResponseCallback callback) {
					Toast.makeText(EztTestActivity.this, String.valueOf(data),
							Toast.LENGTH_LONG).show();

				}

			});
			registerHandler("DayRegister", new WVJBWebViewClient.WVJBHandler() {

				@Override
				public void request(Object data, WVJBResponseCallback callback) {
					JSONObject json;
					String dataStr = null;
					
					try {
						String str="";
						json=new JSONObject(String.valueOf(data));
						dataStr=json.getString("hosName");
						dataStr=java.net.URLDecoder.decode((String)dataStr, "UTF-8");
					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					
					Toast.makeText(EztTestActivity.this, "" + dataStr, Toast.LENGTH_SHORT)
							.show();
					callback.callback(dataStr);
				}
			});
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

	}
	
	
}
