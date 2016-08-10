/**
 * 
 */
package com.eztcn.user.eztcn.fragment;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.fdoc.HospitalDetailActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceDeptByHosActivity;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.js.WVJBWebViewClient;

/**
 * @author Liu Gang
 * 
 *         2016年2月26日 下午3:48:08
 * 
 */
public class HomeJsFragment extends FinalFragment {
	private View rootView;
	private Activity activity;
	private WebView webView;

	public static HomeJsFragment newInstance() {
		HomeJsFragment fragment = new HomeJsFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_test, null);// 缓存Fragment
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		webView = (WebView) rootView.findViewById(R.id.webView);
		configWebView(webView, new MyWebViewClient(webView),
				"file:///android_asset/eztTest.html");

		return rootView;
	}

	class MyWebViewClient extends WVJBWebViewClient {
		public MyWebViewClient(WebView webView) {

			// support js send
			super(webView, new WVJBWebViewClient.WVJBHandler() {

				@Override
				public void request(Object data, WVJBResponseCallback callback) {

				}

			});
			/**
			 * 日常挂号
			 */
			registerHandler("DayRegister", new WVJBWebViewClient.WVJBHandler() {

				@Override
				public void request(Object data, WVJBResponseCallback callback) {
					String jsonStr = String.valueOf(data);
					if (null != jsonStr) {
						try {
							JSONObject json = new JSONObject(jsonStr);
							String hosId=json.getString("hosId");
							String hosName=json.getString("hosName");
							hosName=java.net.URLDecoder.decode((String)hosName, "UTF-8");
							SystemPreferences.save(EZTConfig.KEY_HOS_ID, hosId);
							SystemPreferences.save(EZTConfig.KEY_HOS_NAME, hosName);
						
							Intent intent_ = new Intent(activity,
									ChoiceDeptByHosActivity.class);
							
							activity.startActivity(intent_.putExtra("hosId", hosId)
									.putExtra("hosName",hosName)
									.putExtra("isNearHos", false));

						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}

				}
			});
			
			/**
			 * 日常挂号
			 */
			registerHandler("signInHos", new WVJBWebViewClient.WVJBHandler() {

				@Override
				public void request(Object data, WVJBResponseCallback callback) {
					String jsonStr = String.valueOf(data);
					if (null != jsonStr) {
						try {
							JSONObject json = new JSONObject(jsonStr);
							String hosId=json.getString("signInHos");
							Intent intent=new Intent(getActivity(), HospitalDetailActivity.class);
							Hospital hospital=new Hospital();
							hospital.setId(Integer.parseInt(hosId));
							intent.putExtra("hospital", hospital);
							activity.startActivity(
									intent);
						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}

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
