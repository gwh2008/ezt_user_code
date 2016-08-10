package com.eztcn.user.eztcn.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * @title 医护帮银行列表
 * @describe
 * @author ezt
 * @created 2015年7月14日
 */
public class ViewPagerAdapter extends PagerAdapter {

	private List<GridView> list;

	public ViewPagerAdapter(List<GridView> list) {
		this.list = list;
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			((ViewPager) container)
					.addView(list.get(position % list.size()), 0);
		} catch (Exception e) {

		}
		return list.get(position % list.size());
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// ((ViewPager) container).removeView(list.get(position % list.size()));
	}
}
