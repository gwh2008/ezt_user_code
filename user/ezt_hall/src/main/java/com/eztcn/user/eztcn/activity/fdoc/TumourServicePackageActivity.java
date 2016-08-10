package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.ShoppingCarActivity;
import com.eztcn.user.eztcn.adapter.MyFragmentPagerAdapter;
import com.eztcn.user.eztcn.fragment.TumourPackageFragment;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 外患服务套餐
 * @describe
 * @author ezt
 * @created 2015年3月25日
 */
public class TumourServicePackageActivity extends FinalActivity implements
		OnClickListener {

	@ViewInject(R.id.ong_range_tv)
	private TextView tvOngRange;// 远程

	@ViewInject(R.id.inside_tv)
	private TextView tvInside;// 院内

	@ViewInject(R.id.outside_tv)
	private TextView tvOutside;// 院外

	@ViewInject(R.id.in_hos_tv)
	private TextView tvInHos;// 住院

	@ViewInject(R.id.detail_pager)
	private ViewPager vPager;// 滑动页

	@ViewInject(R.id.v_buttom_line)
	private View viewLine;// 底部标记线

	private ArrayList<Fragment> fragmentList;

	private int currIndex;// 当前页卡编号

	private ImageView imgShopping;

	
	private String deptId;//科室id
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tumour_package);
		ViewUtils.inject(TumourServicePackageActivity.this);
		String title = getIntent().getStringExtra("title");
		deptId=getIntent().getStringExtra("deptId");
		imgShopping = loadTitleBar(true, title, R.drawable.shopping_icon);
		imgShopping.setOnClickListener(this);
		
		initialView();
	}

	/**
	 * 初始化控件
	 */
	private void initialView() {
		tvOngRange.setOnClickListener(new txListener(0));
		tvInside.setOnClickListener(new txListener(1));
		tvOutside.setOnClickListener(new txListener(2));
		tvInHos.setOnClickListener(new txListener(3));

		tvOngRange.setTextColor(getResources().getColor(R.color.title_bar_bg));
		tvInside.setTextColor(getResources().getColor(R.color.dark_black));
		tvOutside.setTextColor(getResources().getColor(R.color.dark_black));
		tvInHos.setTextColor(getResources().getColor(R.color.dark_black));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				getWindowWidth() / 4, 6);

		viewLine.setLayoutParams(params);
		// 初始化颜色
		viewLine.setBackgroundColor(getResources().getColor(
				R.color.title_bar_bg));
		initialPager();
	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			vPager.setCurrentItem(index);
		}
	}

	/**
	 * 初始化viewPager
	 */
	private void initialPager() {
		fragmentList = new ArrayList<Fragment>();
		String[] ids = getResources().getStringArray(R.array.tumour_service_id);

		for (int i = 0; i < ids.length; i++) {
			Fragment fragment = TumourPackageFragment.newInstance(ids[i],deptId);
			fragmentList.add(fragment);
		}

		// 给ViewPager设置适配器
		vPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		vPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		vPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				slideView(position);
				currIndex = position;
				if (position == 0) {
					tvOngRange.setTextColor(getResources().getColor(
							R.color.title_bar_bg));
					tvInside.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvOutside.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvInHos.setTextColor(getResources().getColor(
							R.color.dark_black));

				} else if (position == 1) {

					tvOngRange.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvInside.setTextColor(getResources().getColor(
							R.color.title_bar_bg));
					tvOutside.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvInHos.setTextColor(getResources().getColor(
							R.color.dark_black));
				} else if (position == 2) {
					tvOngRange.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvInside.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvOutside.setTextColor(getResources().getColor(
							R.color.title_bar_bg));
					tvInHos.setTextColor(getResources().getColor(
							R.color.dark_black));

				} else {
					tvOngRange.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvInside.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvOutside.setTextColor(getResources().getColor(
							R.color.dark_black));
					tvInHos.setTextColor(getResources().getColor(
							R.color.title_bar_bg));
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// 底部滑动， positionId， 你要滑动到的位置
	private void slideView(int positionIndex) {
		TranslateAnimation tran = null;
		tran = new TranslateAnimation(calcPosition(currIndex),
				calcPosition(positionIndex), 0, 0);
		tran.setDuration(100);
		tran.setFillAfter(true);
		viewLine.startAnimation(tran);
	}

	// 计算位置 position 需要移动到的位置
	private int calcPosition(int positionIndex) {
		int position = getWindowWidth() / 4;
		int currentPosition = 0;
		for (int i = 0; i < positionIndex; i++) {
			currentPosition += position;
		}
		return currentPosition;
	}

	@Override
	public void onClick(View v) {
		if (BaseApplication.getInstance().patient != null) {
			startActivity(new Intent(mContext, ShoppingCarActivity.class));
		} else {
			HintToLogin(Constant.LOGIN_COMPLETE);
		}
	}

}
