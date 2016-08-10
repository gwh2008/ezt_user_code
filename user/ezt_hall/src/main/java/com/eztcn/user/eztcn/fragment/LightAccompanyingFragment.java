/**
 * 
 */
package com.eztcn.user.eztcn.fragment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
import com.eztcn.user.eztcn.activity.home.BuyHealthCardActivity;
import com.eztcn.user.eztcn.activity.home.CCBUserActivity;
import com.eztcn.user.eztcn.activity.home.HealthCardActivateActivity;
import com.eztcn.user.eztcn.activity.home.LightAccompanyApplyHosActivity;
import com.eztcn.user.eztcn.activity.home.LightAccompanyExplainActivity;
import com.eztcn.user.eztcn.activity.mine.CardUsedActivity;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter.ItemBtOnclick;
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
 * @author EZT
 * 
 */
public class LightAccompanyingFragment extends FinalFragment implements
		OnTouchListener, IHttpResult, OnClickListener, ItemBtOnclick {

	private View rootView;
	/**
	 * 标题
	 */
	private TextView tv_title;
	private TextView left_btn;
	private Activity activity;

	private TextView tvExplain;// 服务详情

	private TextView tvHosList;// 适用医院

	private TextView bankEnter;// 建行用户
	private Button activate;
	private Button buy;

	private TextView tvMoney;// 金额

	private ScrollView scrollView;

	private MyListView lvService;// 卡服务列表

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

	public static LightAccompanyingFragment newInstance() {
		LightAccompanyingFragment fragment = new LightAccompanyingFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.activity_lightaccompanying,
					null);// 缓存Fragment

		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		initView();
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWidth = dm.widthPixels;
		serviceAdapter = new LightAccompanyServiceAdapter(activity);
		lvService.setAdapter(serviceAdapter);
		serviceAdapter.click(this);
		serviceIntroAdapter = new LightAccompanyServiceIntroAdapter(activity);
		lvServiceIntro.setAdapter(serviceIntroAdapter);
		if (BaseApplication.getInstance().isNetConnected) {
			initAdImage();
			initialData();
		} else {
			Toast.makeText(activity, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();

	}

	private void initView() {

		tv_title = (TextView) rootView.findViewById(R.id.title_tv);
		tv_title.setText("E护帮");
		left_btn = (TextView) rootView.findViewById(R.id.left_btn);
		left_btn.setVisibility(View.GONE);
		tvExplain = (TextView) rootView.findViewById(R.id.serverNotes);
		tvExplain.setOnClickListener(this);

		tvHosList = (TextView) rootView.findViewById(R.id.applyHos);
		tvHosList.setOnClickListener(this);
		bankEnter = (TextView) rootView.findViewById(R.id.bankEnter);
		bankEnter.setOnClickListener(this);

		activate = (Button) rootView.findViewById(R.id.activate);
		activate.setOnClickListener(this);

		buy = (Button) rootView.findViewById(R.id.buy);
		buy.setOnClickListener(this);

		tvMoney = (TextView) rootView.findViewById(R.id.name_money_tv);

		scrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);

		lvService = (MyListView) rootView.findViewById(R.id.card_service_lv);

		lvServiceIntro = (MyListView) rootView
				.findViewById(R.id.card_service_intro_lv);

	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("id", "16");
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("id", "16");
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.getPackageDetail(params, this);
		((FinalActivity) activity).showProgressToast();

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.bankEnter:// 建行用户专享通道
			intent.setClass(activity, CCBUserActivity.class);
			break;
		case R.id.activate:// 激活
			intent.setClass(activity, HealthCardActivateActivity.class);
			break;
		case R.id.buy:// 购买
			if (BaseApplication.patient != null) {
				intent.setClass(activity, BuyHealthCardActivity.class);
				intent.putExtra("cardId", cardId);
				intent.putExtra("money", cardMoney);
			} else {
				((FinalActivity) activity).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}

			break;

		case R.id.serverNotes:// 服务详情
			intent.setClass(activity, LightAccompanyExplainActivity.class);
			intent.putExtra("remark", remark);
			break;

		case R.id.applyHos:// 适用医院
			intent.setClass(activity, LightAccompanyApplyHosActivity.class);
			intent.putExtra("list", hosList);
			break;
		}

		startActivity(intent);
	}

	/**
	 * 初始化广告图
	 */
	public void initAdImage() {
		/**
		 * 头部广告图
		 */
		adsDefault = (ImageView) rootView.findViewById(R.id.home_loading_img);
		adsPager = (MyImgScroll) rootView.findViewById(R.id.home_img_scroll);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, windowWidth / 8 * 4);
		adsDefault.setLayoutParams(params);
		adsPager.setLayoutParams(params);
		adsOvalLayout = (LinearLayout) rootView
				.findViewById(R.id.home_point_layout);
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
		/* 重新加载初始化 */
		// myPager.stopTimer();
		// myPager.oldIndex = 0;
		// myPager.curIndex = 0;
		// ovalLayout.removeAllViews();
		/*------------*/
		adsPager.start(activity, adsOvalLayout);
	}

	/**
	 * 初始化图片
	 * 
	 * @throws FileNotFoundException
	 */
	private ArrayList<View> InitAdsViewPager(ArrayList<String> imgs) {
		ArrayList<View> listViews = new ArrayList<View>();
		for (int i = 0; i < imgs.size(); i++) {
			ImageView imageView = new ImageView(activity);
			imageView.setOnTouchListener(this);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {// 设置图片点击事件

					if (adsPager.getmListViews() != null) {
						// Toast.makeText(
						// HomeActivity.activity,
						// "点击了:"
						// + advList.get(myPager.getCurIndex())
						// .getAdUrl(), Toast.LENGTH_SHORT)
						// .show();
					}
				}
			});
			if (imgs.size() == 0) {// 网络加载图片地址成功
				// finalBitMap.display(imageView, imgs.get(i), defaultBitmap,
				// failBitmap);
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
				Toast.makeText(activity, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(activity, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();

		}
		((FinalActivity) activity).hideProgressToast();

	}

	@Override
	public void adapterOnclick(int pos, int status) {

		if (status == 0) {// 可使用
			startActivity(new Intent(activity.getApplicationContext(),
					CardUsedActivity.class).putExtra("url",
					"http://www.baidu.com").putExtra("title",
					serviceAdapter.getList().get(pos).getItemName()));
		} else if (status == 2) {// 使用中

		}
	}

}
