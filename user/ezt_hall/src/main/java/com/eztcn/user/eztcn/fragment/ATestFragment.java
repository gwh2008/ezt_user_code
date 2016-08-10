package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.adapter.GridCompentAdapter;
import com.eztcn.user.eztcn.bean.compent.Compent;
import com.eztcn.user.eztcn.bean.compent.GridCompent;
import com.eztcn.user.eztcn.bean.compent.IntentParams;
import com.eztcn.user.eztcn.bean.compent.ItemCompent;
import com.eztcn.user.eztcn.bean.compent.ScrollCompent;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.CompentManager;
import com.eztcn.user.eztcn.customView.CustomGridView;
import com.eztcn.user.eztcn.customView.MyImgScroll;
import com.eztcn.user.eztcn.utils.HttpDownloader;

/**
 * @title 首页 Ce试de
 * @describe 打破一切 扩散思维
 * @author ezt
 * @created 2014年12月11日
 */
public class ATestFragment extends FinalFragment implements OnItemClickListener,
		OnTouchListener {
	private View rootView;
	private LinearLayout layoutRoot;
	private Activity activity;
	private int adHeight;
	private DisplayMetrics dm = new DisplayMetrics();
	/**
	 * 加载viewpager组件的次数
	 */
	private int viewPagerTimes;
	/**
	 * 加载gridview组件的次数
	 */
	private int gridViewTimes;
	private ArrayList<MyImgScroll> scrolList;
	private LayoutInflater inflater;
	private HashMap<Integer, List<ItemCompent>> map;

	public static ATestFragment newInstance() {
		ATestFragment fragment = new ATestFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		adHeight = dm.heightPixels / 3;

		HttpDownloader downloader = new HttpDownloader();
		downloader.readFile(activity, handler, "home");
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			String jsonStr = msg.obj.toString();
			CompentManager manager = new CompentManager();
			List<Compent> compentList = manager.parseCompent(jsonStr);
			compent2View(compentList);
		};
	};

	private void compent2View(List<Compent> compentList) {
		map = new HashMap<Integer, List<ItemCompent>>();
		for (int i = 0; i < compentList.size(); i++) {
			Compent compent = compentList.get(i);
			if (compent instanceof GridCompent) {
				GridCompent gridCompent = (GridCompent) compent;
				addGridView(gridCompent);
			} else if (compent instanceof ScrollCompent) {
				ScrollCompent scrollCompent = (ScrollCompent) compent;
				View view = inflater.inflate(R.layout.item_ads_layout, null);
				adsView(view, scrollCompent);
				layoutRoot.addView(view);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == rootView) {
			scrolList = new ArrayList<MyImgScroll>();
			viewPagerTimes = -1;
			gridViewTimes = -1;
			rootView = inflater.inflate(R.layout.fragment_test, null);
			layoutRoot = (LinearLayout) rootView.findViewById(R.id.layout_root);

		}
		this.inflater = inflater;
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void addGridView(GridCompent gridCompent) {
		gridViewTimes++;
		CustomGridView gridView = new CustomGridView(activity);
		gridView.setNumColumns(gridCompent.getRow());
		List<ItemCompent> items = gridCompent.getChildren();
		map.put(gridViewTimes, items);
		GridCompentAdapter adapter = new GridCompentAdapter(activity);
		adapter.setList(items);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 15, 0, 0);
		gridView.setLayoutParams(params);
		gridView.setTag(gridViewTimes);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		layoutRoot.addView(gridView);
	}

	private void adsView(View view, ScrollCompent scrollCompent) {
		viewPagerTimes++;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, adHeight);
		if (viewPagerTimes > 0)
			params.setMargins(0, 25, 0, 0);
		else
			params.setMargins(0, 0, 0, 0);
		view.setLayoutParams(params);
		ImageView loadingImg = (ImageView) view
				.findViewById(R.id.home_loading_img);// 图片未加载出来时显示的图片
		MyImgScroll myPager = (MyImgScroll) view
				.findViewById(R.id.home_img_scroll);// 图片容器
		scrolList.add(myPager);
		LinearLayout ovalLayout = (LinearLayout) view
				.findViewById(R.id.home_point_layout);// 圆点容器
		myPager.setmScrollTime(EZTConfig.SCROLL_TIME);
		loadingImg.setVisibility(View.GONE);
		initialAdsList(scrollCompent, myPager, ovalLayout);
	}

	/**
	 * 首页广告图赋值
	 */
	private void initialAdsList(ScrollCompent scrollCompent,
			MyImgScroll myPager, LinearLayout ovalLayout) {
		ArrayList<View> listViews = loadAdsView(scrollCompent);
		myPager.setmListViews(listViews);
		/* 重新加载初始化 */
		myPager.stopTimer();
		myPager.oldIndex = 0;
		myPager.curIndex = 0;
		ovalLayout.removeAllViews();
		/*------------*/
		myPager.start(activity, ovalLayout);
	}

	private ArrayList<View> loadAdsView(ScrollCompent scrollCompent) {
		ArrayList<View> listViews = new ArrayList<View>();
		final List<ItemCompent> items = scrollCompent.getChildren();
		for (int i = 0; i < items.size(); i++) {
			ItemCompent item = items.get(i);
			ImageView imageView = item.getImageView();
			imageView.setTag(viewPagerTimes + "," + i);
			imageView.setOnTouchListener(this);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int position = -1;
					if (null != v.getTag()) {
						position = Integer.parseInt(v.getTag().toString()
								.subSequence(2, 3).toString());
					}

					ItemCompent item = items.get(position);
					Intent intent = new Intent();
					try {
						intent.setClass(activity,
								Class.forName(item.getJumpLink()));
						List<IntentParams> params = item.getIntentParamList();
						if (null != params)
							for (int j = 0; j < params.size(); j++) {
								IntentParams param = params.get(j);
//								intent.putExtra(param.getKey(),
//										param.getValue());
							}
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			});
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, adHeight);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			listViews.add(imageView);
		}
		return listViews;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int viewPagerIndex = -1;
		if (null != v.getTag()) {
			String position = v.getTag().toString().subSequence(0, 1)
					.toString();
			viewPagerIndex = Integer.parseInt(position);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_CANCEL: {
			if (-1 != viewPagerIndex) {
				scrolList.get(viewPagerIndex).startTimer();
			}
		}
			break;
		case MotionEvent.ACTION_UP: {
			if (-1 != viewPagerIndex) {
				scrolList.get(viewPagerIndex).startTimer();
			}
		}
			break;

		default: {
			if (-1 != viewPagerIndex) {
				scrolList.get(viewPagerIndex).stopTimer();
			}

		}
			break;
		}

		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String tag = String.valueOf(parent.getTag());
		List<ItemCompent> items = (List<ItemCompent>) map.get(Integer
				.parseInt(tag));
		ItemCompent item = items.get(position);
		String link = item.getJumpLink();
		List<IntentParams> params = item.getIntentParamList();
		Intent intent;
		try {
			intent = new Intent(activity, Class.forName(link));

			for (int i = 0; i < params.size(); i++) {
//				IntentParams param = params.get(i);
//				intent.putExtra(param.getKey(), param.getValue());
			}
			startActivity(intent);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
