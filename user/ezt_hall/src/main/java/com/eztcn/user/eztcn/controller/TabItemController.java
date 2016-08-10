package com.eztcn.user.eztcn.controller;

import java.util.HashMap;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.eztcn.user.eztcn.bean.TabItem;

/**
 * @title 选项卡控制器
 * @describe
 * @author ezt
 * @created 2014年11月24日
 */
public class TabItemController {
	private FragmentManager mFragmentManager;
	private HashMap<String, TabItem> mTabItems = null;
	private TabItem mSelectedTabItem;

	public TabItemController(FragmentManager mFragmentManager) {
		this.mFragmentManager = mFragmentManager;
		if (null == mTabItems) {
			mTabItems = new HashMap<String, TabItem>();
		}
	}

	public void addTabItem(TabItem item) {
		mTabItems.put(item.getTag(), item);
	}

	public void selectTab(String tag) {
		selectTab(mTabItems.get(tag));
	}

	public void selectTab(TabItem tabItem) {

		final FragmentTransaction trans = mFragmentManager.beginTransaction()
				.disallowAddToBackStack();

		if (mSelectedTabItem == tabItem) {
			if (mSelectedTabItem != null) {
				mSelectedTabItem.onTabReselected(mSelectedTabItem, trans);
			}
		} else {
			if (mSelectedTabItem != null) {
				mSelectedTabItem.onTabUnselected(mSelectedTabItem, trans);
			}
			mSelectedTabItem = tabItem;
			if (mSelectedTabItem != null) {
				mSelectedTabItem.onTabSelected(mSelectedTabItem, trans);
			}
		}

		if (!trans.isEmpty()) {
			// trans.commit();
			trans.commitAllowingStateLoss();// (IllegalStateException: Can not
											// perform this action after
											// onSaveInstanceState)
											// 用这个方法，解决注释报错问题
		}
	}
}