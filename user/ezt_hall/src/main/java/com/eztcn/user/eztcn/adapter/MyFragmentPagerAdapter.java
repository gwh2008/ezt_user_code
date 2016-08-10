package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @title fragment adapter
 * @describe
 * @author ezt
 * @created 2015年3月2日
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> list;

	public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int pos) {
		return list.get(pos);
	}
	
	

}