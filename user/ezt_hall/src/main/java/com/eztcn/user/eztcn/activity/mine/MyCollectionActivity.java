/**
 * 
 */
package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.customView.PagerSlidingTobTab;
import com.eztcn.user.eztcn.fragment.CollectionDocFragment;
import com.eztcn.user.eztcn.fragment.CollectionHospitalFragment;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.eztcn.utils.ScreenUtils;

/**
 * @author Liu Gang
 * 
 * 2016年1月8日
 * 下午5:53:42
 * 我的收藏
 */
public class MyCollectionActivity extends FinalActivity {
	
	@ViewInject(R.id.myCollectionPSTT)
	private PagerSlidingTobTab myPagerTab;
	
	@ViewInject(R.id.myCollectionVP)
	private ViewPager mPager;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
	private final int COLLECTION_HOS=1,COLLECTION_DOC=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycollection);
		loadTitleBar(true, "我的收藏", null);
		ViewUtils.inject(MyCollectionActivity.this);
		initFragment();
		initViewPager();
	}
	private void initFragment(){
		fragmentList=new ArrayList<Fragment>();
		CollectionDocFragment docFragment=CollectionDocFragment.newInstance();
		CollectionHospitalFragment hospitalFragment=CollectionHospitalFragment.newInstance();
		fragmentList.add(COLLECTION_DOC, docFragment);
		fragmentList.add(COLLECTION_HOS, hospitalFragment);
	}
	private void initViewPager(){
		adapter = new MyPagerAdapter(getSupportFragmentManager());//初始化数据适配器
		mPager.setAdapter(adapter);
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
//		myPagerTab.setTabPaddingLeftRight(ResourceUtils.dip2px(mContext,
//				ResourceUtils.getXmlDef(mContext, R.dimen.large_margin)));
		myPagerTab.setTabPaddingLeftRight((int)(ScreenUtils.gainDM(MyCollectionActivity.this).widthPixels/4.8));
//		myPagerTab.setIndicatorMargin(ResourceUtils.dip2px(mContext,
//				ResourceUtils.getXmlDef(mContext, R.dimen.medium_margin)));
		myPagerTab.setViewPager(mPager);
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		@Override
		public CharSequence getPageTitle(int position) {
			return position==COLLECTION_DOC?"医生":"医院";
		}
	}
}
