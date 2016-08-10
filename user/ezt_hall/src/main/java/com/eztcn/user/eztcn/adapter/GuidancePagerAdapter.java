package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @title 引导页使用的pageview适配器
 * @describe
 * @author ezt
 * @created 2015年1月23日
 */
public class GuidancePagerAdapter extends PagerAdapter {
	private List<View> views = new ArrayList<View>();

	public GuidancePagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	}
}
