package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.UmengCustomShareBoard;

/**
 * @title 滴滴优惠券页面
 * @describe
 * @author ezt
 * @created 2015年7月7日
 */
public class DiDi_CouponActivity extends FinalActivity implements
		OnClickListener {

	@ViewInject(R.id.web)
	private WebView webInfo;
	private ImageView imgShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_didi);
		ViewUtils.inject(DiDi_CouponActivity.this);
		imgShare = loadTitleBar(true, "领取礼包", R.drawable.selector_share_bg);
		imgShare.setVisibility(View.GONE);
		imgShare.setOnClickListener(this);

		webInfo.getSettings().setJavaScriptEnabled(true);
		webInfo.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webInfo.setScrollbarFadingEnabled(true);
		webInfo.getSettings().setDomStorageEnabled(true);
		webInfo.getSettings().setAppCacheMaxSize(1024 * 1024 * 8); // cache
																	// size:8MB
		webInfo.getSettings().setAppCachePath(
				"/data/data/" + getPackageName() + "/cache");
//		webInfo.getSettings().setPluginsEnabled(true);// 支持所有版本
		webInfo.getSettings().setPluginState(WebSettings.PluginState.ON);// 支持所有版本2016-02-26
		webInfo.setWebChromeClient(new WebChromeClient());
		webInfo.loadUrl(EZTConfig.DIDI_COUPON_URL);
		showProgressToast();

		// 初始化分享
		Bitmap didi =null; 
//				BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_didi_share);
		setShareContent(getString(R.string.didi_share_msg),
				getString(R.string.didi_share_title),
				EZTConfig.DIDI_COUPON_URL, null, didi);

	}

	@Override
	public void onClick(View v) {
		UmengCustomShareBoard shareBoard = new UmengCustomShareBoard(mContext,
				true);
		shareBoard.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				webInfo.setVisibility(View.VISIBLE);
				imgShare.setVisibility(View.VISIBLE);
				hideProgressToast();
			}
			super.onProgressChanged(view, newProgress);
		}

	}

}
