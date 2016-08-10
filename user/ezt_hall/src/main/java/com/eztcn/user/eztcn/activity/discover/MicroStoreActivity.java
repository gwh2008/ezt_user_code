package com.eztcn.user.eztcn.activity.discover;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.ShoppingCarActivity;
import com.eztcn.user.eztcn.adapter.Micro_HealthmanagerAdapter;
import com.eztcn.user.eztcn.adapter.Micro_WearAdapter;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyGridView;
import com.eztcn.user.eztcn.customView.MyImgScroll;

/**
 * @title 微商城
 * @describe
 * @author ezt
 * @created 2015年2月27日
 */
public class MicroStoreActivity extends FinalActivity implements
		OnTouchListener {

	@ViewInject(R.id.left_btn)
	private TextView back;
	@ViewInject(R.id.search)// click = "onClick"
	private ImageView search;// 搜索
	@ViewInject(R.id.shoppingCar)//, click = "onClick"
	private ImageView shoppingCar;// 购物车
	@ViewInject(R.id.hmanagerList)
	private MyGridView hmanagerList;// 健康服务list
	@ViewInject(R.id.wearList)
	private MyGridView wearList;// 可穿戴设备list
	@ViewInject(R.id.hotProList)
	private MyGridView hotProList;// 热门产品推荐list
	@ViewInject(R.id.scrollView1)
	private ScrollView scrollView;

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
		setContentView(R.layout.microstore);
		ViewUtils.inject(MicroStoreActivity.this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWidth = dm.widthPixels;
		initAdImage();
		initHManagerList();
		initWearList();
		initHotproList();
	}
	@OnClick(R.id.left_btn)
	private void back(View v){
		finish();
	}
	@OnClick(R.id.shoppingCar)
	private void shoppingCar(View v){
		Intent intent = new Intent(this, ShoppingCarActivity.class);
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
	 * 初始化健康管理list
	 */
	public void initHManagerList() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		Micro_HealthmanagerAdapter adapter = new Micro_HealthmanagerAdapter(
				mContext);
		hmanagerList.setAdapter(adapter);
		adapter.setList(list);
	}

	/**
	 * 初始化可穿戴设备list
	 */
	public void initWearList() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		Micro_WearAdapter adapter = new Micro_WearAdapter(mContext);
		wearList.setAdapter(adapter);
		adapter.setList(list);
	}

	/**
	 * 初始化热门产品推荐list
	 */
	public void initHotproList() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		Micro_WearAdapter adapter = new Micro_WearAdapter(mContext);
		hotProList.setAdapter(adapter);
		adapter.setList(list);
	}

	/**
	 * 获取资讯广告图
	 */
	private void getAdsList() {
		ArrayList<String> imgs = new ArrayList<String>();
		String url = "";
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case 0:
				url = R.drawable.info_ad_1 + "";
				break;

			case 1:
				url = R.drawable.info_ad_2 + "";
				break;

			case 2:
				url = R.drawable.info_ad_3 + "";
				break;
			}
			imgs.add(url);
		}
		adsDefault.setVisibility(View.GONE);
		ArrayList<View> listViews = InitAdsViewPager(imgs);
		adsPager.setmListViews(listViews);
		/* 重新加载初始化 */
		// myPager.stopTimer();
		// myPager.oldIndex = 0;
		// myPager.curIndex = 0;
		// ovalLayout.removeAllViews();
		/*------------*/
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
						// Toast.makeText(
						// HomeActivity.this,
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
	@OnItemClick({R.id.hmanagerList,R.id.wearList,R.id.hotProList})
	private void hmanagerListItemClick(AdapterView<?> parent, View view, int position,
			long id){
		Intent intent = new Intent(this, ProductDetailActivity.class);
		startActivity(intent);
	}
}
