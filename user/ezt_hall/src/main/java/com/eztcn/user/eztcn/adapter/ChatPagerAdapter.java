package com.eztcn.user.eztcn.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

/**
 * @title 表情/其他选项pagerAdapter
 * @describe
 * @author ezt
 * @created 2014年11月14日
 */
public class ChatPagerAdapter extends PagerAdapter {

	private GridView[] gvViews;

	public ChatPagerAdapter(GridView[] gvViews) {
		super();
		this.gvViews = gvViews;
	}

	@Override
	public void destroyItem(View view, int pos, Object o) {
		((ViewPager) view).removeView(gvViews[pos]);

	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return gvViews.length;
	}

	@Override
	public Object instantiateItem(View arg0, int position) {
		((ViewPager) arg0).addView(gvViews[position]);
		return gvViews[position];
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}
