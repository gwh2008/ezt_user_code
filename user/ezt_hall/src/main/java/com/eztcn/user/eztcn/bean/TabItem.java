package com.eztcn.user.eztcn.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.eztcn.user.R;

/**
 * @title 底部选显卡item实体
 * @describe
 * @author ezt
 * @created 2014年11月24日
 */
public class TabItem {
	protected final Context mContext;
	protected final String mTag;// fragment标记
	protected final String className;// fragment类名
	protected final Bundle mArgs;// fragment需要传的值
	protected Fragment mFragment;

	public TabItem(Context ctx, String mTag, String mClass) {
		this(ctx, mTag, mClass, null);

	}

	public TabItem(Context ctx, String mTag, String mClass, Bundle mArgs) {
		this.mContext = ctx;
		this.mTag = mTag;
		this.className = mClass;
		this.mArgs = mArgs;
	}

	public String getTag() {
		return mTag;
	}

	public void onTabSelected(TabItem tab, FragmentTransaction ft) {
		if (mFragment == null) {
			mFragment = Fragment.instantiate(mContext, className, mArgs);
			ft.add(R.id.layout_contain, mFragment, mTag);
		} else {
			ft.attach(mFragment);
		}
	}

	public void onTabUnselected(TabItem tab, FragmentTransaction ft) {
		if (mFragment != null) {
			ft.detach(mFragment);
		}
	}

	public void onTabReselected(TabItem tab, FragmentTransaction ft) {
	}
}