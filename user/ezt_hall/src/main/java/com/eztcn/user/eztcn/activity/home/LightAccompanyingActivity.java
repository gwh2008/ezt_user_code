package com.eztcn.user.eztcn.activity.home;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceIntroAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyImgScroll;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 轻陪诊(e护帮)
 * @describe
 * @author ezt
 * @created 2015年3月10日
 */
public class LightAccompanyingActivity extends FinalActivity implements
		OnTouchListener, IHttpResult {

	@ViewInject(R.id.serverNotes)//, click = "onClick"
	private TextView tvExplain;// 服务详情

	@ViewInject(R.id.applyHos)//, click = "onClick"
	private TextView tvHosList;// 适用医院

	@ViewInject(R.id.bankEnter)//, click = "onClick"
	private TextView bankEnter;// 建行用户
	@ViewInject(R.id.activate)//, click = "onClick"
	private Button activate;
	@ViewInject(R.id.buy)//, click = "onClick"
	private Button buy;

	@ViewInject(R.id.name_money_tv)
	private TextView tvMoney;// 金额

	@ViewInject(R.id.scroll_view)
	private ScrollView scrollView;

	@ViewInject(R.id.card_service_lv)
	private MyListView lvService;// 卡服务列表

	@ViewInject(R.id.card_service_intro_lv)
	private MyListView lvServiceIntro;// 卡服务介绍

	private LightAccompanyServiceAdapter serviceAdapter;

	private LightAccompanyServiceIntroAdapter serviceIntroAdapter;

	private ArrayList<Hospital> hosList;

	private String cardId;// 卡Id
	private String cardMoney;// 卡价格
	private String remark;// 健康卡服务须知
	/**
	 * 广告图
	 */
	private ImageView adsDefault;// 图片未加载出来时显示的图片
	private MyImgScroll adsPager;// 图片容器
	private LinearLayout adsOvalLayout;// 圆点容器
	private int windowWidth;// 屏幕宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightaccompanying);
		ViewUtils.inject(LightAccompanyingActivity.this);
		loadTitleBar(true, "护士帮", null);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWidth = dm.widthPixels;
		serviceAdapter = new LightAccompanyServiceAdapter(this);
		lvService.setAdapter(serviceAdapter);

		serviceIntroAdapter = new LightAccompanyServiceIntroAdapter(this);
		lvServiceIntro.setAdapter(serviceIntroAdapter);

		if (BaseApplication.getInstance().isNetConnected) {
			initAdImage();
			initialData();
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
		RequestParams params=new RequestParams();
		params.addBodyParameter("id", "16");
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.getPackageDetail(params, this);
		showProgressToast();

	}
	@OnClick(R.id.bankEnter)
	public void bankEnterClick(View v) {
		Intent intent = new Intent();
		// 建行用户专享通道
		intent.setClass(this, CCBUserActivity.class);
		startActivity(intent);
	}
	@OnClick(R.id.activate)
	public void activateClick(View v) {
		Intent intent = new Intent();
		// 激活
			intent.setClass(this, HealthCardActivateActivity.class);
	
		startActivity(intent);
	}
	@OnClick(R.id.buy)
	public void buyClick(View v) {
		Intent intent = new Intent();
		// 购买
			if(BaseApplication.getInstance().patient!=null){
				intent.setClass(this, BuyHealthCardActivity.class);
				intent.putExtra("cardId", cardId);
				intent.putExtra("money", cardMoney);
			}else{
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
		startActivity(intent);
	}
	@OnClick(R.id.serverNotes)
	public void serverNotesClick(View v) {
		Intent intent = new Intent();
		// 服务详情
			intent.setClass(this, LightAccompanyExplainActivity.class);
			intent.putExtra("remark", remark);
		startActivity(intent);
	}
	@OnClick(R.id.applyHos)
	public void applyHosClick(View v) {
		Intent intent = new Intent();
		// 适用医院
		intent.setClass(this, LightAccompanyApplyHosActivity.class);
		intent.putExtra("list", hosList);
		startActivity(intent);
	}
	/**
	 * 初始化广告图
	 */
	public void initAdImage() {
		/**
		 * 头部广告图
		 */
		adsDefault = (ImageView) findViewById(R.id.home_loading_img);
		adsPager = (MyImgScroll) findViewById(R.id.home_img_scroll);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, windowWidth / 8 * 4);
		adsDefault.setLayoutParams(params);
		adsPager.setLayoutParams(params);
		adsOvalLayout = (LinearLayout) findViewById(R.id.home_point_layout);
		adsPager.setmScrollTime(EZTConfig.SCROLL_TIME);
		scrollView.smoothScrollTo(0, 0);
		getAdsList();// 获取广告图数据
	}

	/**
	 * 获取轻陪诊广告图
	 */
	private void getAdsList() {
		ArrayList<String> imgs = new ArrayList<String>();
		String url = R.drawable.light_ad + "";
		imgs.add(url);
		adsDefault.setVisibility(View.GONE);
		ArrayList<View> listViews = InitAdsViewPager(imgs);
		adsPager.setmListViews(listViews);
		adsPager.start(this, adsOvalLayout);
	}

	/**
	 * 初始化图片
	 * 
	 * @throws FileNotFoundException
	 */
	private ArrayList<View> InitAdsViewPager(ArrayList<String> imgs) {
		ArrayList<View> listViews = new ArrayList<View>();
		for (int i = 0; i < imgs.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setOnTouchListener(this);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {// 设置图片点击事件

					if (adsPager.getmListViews() != null) {
					}
				}
			});
			if (imgs.size() == 0) {// 网络加载图片地址成功
			} else {
				imageView.setImageResource(Integer.parseInt(imgs.get(i)));// 本地
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 300);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			listViews.add(imageView);
		}
		return listViews;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			adsPager.startTimer();
			adsPager.requestDisallowInterceptTouchEvent(true);
		} else {
			adsPager.stopTimer();
			adsPager.requestDisallowInterceptTouchEvent(false);
		}
		return false;
	}

	@Override
	public void result(Object... object) {
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				scrollView.setVisibility(View.VISIBLE);
				ArrayList<LightAccompanying> list = (ArrayList<LightAccompanying>) map
						.get("itemList");
				String carName = (String) map.get("cardName");
				cardMoney = (String) map.get("carAmount");
				cardId = (String) map.get("cardId");
				remark = (String) map.get("remark");

				tvMoney.setText(carName + "： " + cardMoney + "元");
				serviceAdapter.setList(list);
				serviceIntroAdapter.setList(list);
				hosList = (ArrayList<Hospital>) map.get("hosList");

			} else {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();

		}
		hideProgressToast();

	}

}
