package com.eztcn.user.eztcn.activity.discover;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.InformationColumn;
import com.eztcn.user.eztcn.customView.PagerSlidingTobTab;
import com.eztcn.user.eztcn.fragment.InfomationChildFragment;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 微资讯 ----》2016-1-4点击进入【健康头条】（原微资讯，现更名为健康头条）
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class InformationActivity extends FinalActivity implements
		 IHttpResult {

	@ViewInject(R.id.information_vpager)
	private ViewPager mPager;

	@ViewInject(R.id.tabs)
	private PagerSlidingTobTab myPagerTab;

	@ViewInject(R.id.title_column_layout)
	public LinearLayout columnLayout;

	@ViewInject(R.id.subscription_lt)
	private ListView lvSubscription;// 已订阅的资讯列表

	@ViewInject(R.id.information_tv)
	private TextView tvInformation;

	@ViewInject(R.id.subscription_tv)
	private TextView tvSubscription;

	@ViewInject(R.id.information_right_btn)
	private ImageView imgRight;

	@ViewInject(R.id.left_btn)
	private TextView tvBack;

	private MyPagerAdapter adapter;
	private ArrayList<InformationColumn> columns;
	private final String INFO_COLUMN_DATA = "infoColumnData";// 缓存key-栏目
	private CacheUtils mCache;

	private int flag = 0;// 0发现正常进入，1为成功页面跳入

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information_new);
		ViewUtils.inject(InformationActivity.this);
		flag = getIntent().getIntExtra("flag", 0);
		mCache = CacheUtils.get(this);
		if (BaseApplication.getInstance().isNetConnected) {
			showProgressToast();
			getColumnData();
		} else {
			ArrayList<InformationColumn> cacheColumnData = (ArrayList<InformationColumn>) mCache
					.getAsObject(INFO_COLUMN_DATA);
			if (cacheColumnData != null && cacheColumnData.size() != 0) {
				columns = cacheColumnData;
				columnLayout.setVisibility(View.VISIBLE);
				initialTitleTab();
			} else {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	// @Override
	// public void finish() {
	// super.finish();
	// if (flag == 1) {//预约成功页面更资讯跳转
	// BackHome(InformationActivity.this);
	// }
	// }

	/**
	 * 初始化导航栏数据
	 */
	private void getColumnData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		new NewsImpl().getNewsColumn(params, this);
	}

	/**
	 * 实例化顶部导航栏
	 */
	private void initialTitleTab() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		// final int pageMargin = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
		// .getDisplayMetrics());
		// mPager.setPageMargin(pageMargin);
		myPagerTab.setTextSize(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.medium_size)));
		myPagerTab.setIndicatorColorResource(R.color.main_color);
		myPagerTab.setTabTextSelectColor(getResources().getColor(
				R.color.main_color));
		myPagerTab.setIndicatorHeight(7);
		myPagerTab.setDividerColor(getResources().getColor(
				android.R.color.transparent));
		myPagerTab.setUnderlineHeight(1);
		myPagerTab.setUnderlineColorResource(R.color.dark_gray);
		myPagerTab.setTabPaddingLeftRight(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.large_margin)));
		myPagerTab.setIndicatorMargin(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.medium_margin)));

		// shouldExpand currentPositionOffset
		myPagerTab.setViewPager(mPager);

	}
	@OnClick(R.id.information_tv)
	private void inforClick(View v){
		// 微资讯
		mPager.setVisibility(View.VISIBLE);
		lvSubscription.setVisibility(View.GONE);
	}
	@OnClick(R.id.subscription_tv)
	private void subscription(View v){
		// 已订阅
		mPager.setVisibility(View.GONE);
		lvSubscription.setVisibility(View.VISIBLE);
	}
	
	@OnClick(R.id.information_right_btn)
	private void inforright(View v){
		// 订阅
		startActivityForResult(new Intent(InformationActivity.this,
				SubscriptionActivity.class), 1);
	}
	@OnClick(R.id.left_btn)
	private void back(View v){
		finish();
		hideProgressToast();	
	}


	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_NEWS_COLUMN:// 获取资讯栏目
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					if ((Boolean) map.get("flag")) {
						columns = (ArrayList<InformationColumn>) map
								.get("columns");
						if (columns != null && columns.size() != 0) {
//							columnLayout.setVisibility(View.VISIBLE);
							mCache.put(INFO_COLUMN_DATA, columns);
							initialTitleTab();
						}

					} else {

						Logger.i("资讯栏目", map.get("msg"));
					}
				}

			} else {
				hideProgressToast();
				Logger.i("资讯栏目", object[3]);
			}
			break;

		}

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return columns.get(position).getInfoName();
		}

		@Override
		public int getCount() {
			return columns.size();
		}

		@Override
		public Fragment getItem(int position) {
			return InfomationChildFragment.newInstance(columns.get(position)
					.getId(), position,columnLayout);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 11) {// 订阅确定返回
			String index = data.getStringExtra("info");
			Toast.makeText(InformationActivity.this, index, Toast.LENGTH_SHORT)
					.show();
		}

	}

}
