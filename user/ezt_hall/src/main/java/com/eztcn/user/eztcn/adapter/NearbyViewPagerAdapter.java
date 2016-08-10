package com.eztcn.user.eztcn.adapter;

import java.util.List;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.eztcn.user.eztcn.customView.PullToRefreshListView;

/**
 * @title 周边viewpager adapter
 * @describe
 * @author ezt
 * @param <T>
 * @created 2015年6月12日
 */
public class NearbyViewPagerAdapter<T> extends PagerAdapter {

	private List<T> list;

	public NearbyViewPagerAdapter(List<T> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		container.removeView((View) list.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView((View) list.get(position));
		container.setTag(position);
		return list.get(position);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}
