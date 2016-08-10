package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.customView.PagerSlidingTobTab;
import com.eztcn.user.eztcn.fragment.RegRecordFragment;
import com.eztcn.user.eztcn.fragment.TelRecordFragment;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.eztcn.utils.ScreenUtils;

/**
 * @title 我的记录
 * @describe
 * @author ezt
 * @created 2016年01月11日
 */
public class NewMyRecordActivity extends FinalActivity implements IHttpResult {
	/**
	 *
	 */
	@ViewInject(R.id.regRecordTV)
	private TextView regRecordTV;
	@ViewInject(R.id.regCheckRecordTV)
	private TextView regCheckRecordTV;
	@ViewInject(R.id.regBedRecordTV)
	private TextView regBedRecordTV;
	@ViewInject(R.id.telDocRecordTV)
	private TextView telDocRecordTV;
	@ViewInject(R.id.myRecordPSTT)
	private PagerSlidingTobTab myRecordPSTT;
	@ViewInject(R.id.myRecordVP)
	private ViewPager myRecordVP;
	private List<Fragment> regCheckRecordFragmentList;
	private List<Fragment> regBedRecordFragmentList;
	private List<Fragment> telRecordFragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_record);
		ViewUtils.inject(NewMyRecordActivity.this);
		loadTitleBar(true, "预约记录", null);
		RegRecordAdapter fpa = new RegRecordAdapter(getSupportFragmentManager());
		initViewPager(fpa, 4);
	}

	@OnClick(R.id.regRecordTV)
	private void regRecordTVClick(View v) {// 预约记录
		RegRecordAdapter fpa = new RegRecordAdapter(getSupportFragmentManager());
		initViewPager(fpa, 4);
	}

	@OnClick(R.id.regCheckRecordTV)
	private void regCheckRecordClick(View v) {// 预约检查记录

	}

	@OnClick(R.id.regBedRecordTV)
	private void regBedRecordClick(View v) {// 预约病床记录

	}

	@OnClick(R.id.telDocRecordTV)
	private void telDocRecordTVClick(View v) {// 电话医生记录
		TelRecordAdapter fpa = new TelRecordAdapter(getSupportFragmentManager());
		initViewPager(fpa, 3);
	}

	private void initViewPager(FragmentPagerAdapter fpa, int num) {
		myRecordVP.setAdapter(fpa);
		myRecordPSTT.setTextSize(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.medium_size)));
		myRecordPSTT.setIndicatorColorResource(R.color.main_color);
		myRecordPSTT.setTabTextSelectColor(getResources().getColor(
				R.color.main_color));
		myRecordPSTT.setIndicatorHeight(7);
		myRecordPSTT.setDividerColor(getResources().getColor(
				android.R.color.transparent));
		myRecordPSTT.setUnderlineHeight(1);
		myRecordPSTT.setUnderlineColorResource(R.color.dark_gray);
		// myRecordPSTT.setTabPaddingLeftRight(ResourceUtils.dip2px(mContext,
		// ResourceUtils.getXmlDef(mContext, R.dimen.large_margin)));
		// myRecordPSTT.setTabPaddingLeftRight(ResourceUtils.dip2px(mContext,
		// ResourceUtils.getXmlDef(mContext, R.dimen.large_margin)));
		myRecordPSTT
				.setTabPaddingLeftRight(ScreenUtils.gainDM(mContext).widthPixels
						/ (num * num));
		// myRecordPSTT.setIndicatorMargin(ResourceUtils.dip2px(mContext,
		// ResourceUtils.getXmlDef(mContext, R.dimen.medium_margin)));
		myRecordPSTT.setViewPager(myRecordVP);
	}

	/**
	 * 电话医生
	 * 
	 * @author Liu Gang
	 * 
	 *         2016年1月13日 下午1:58:31
	 * 
	 */
	private class TelRecordAdapter extends FragmentPagerAdapter {

		public TelRecordAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
			TelRecordFragment telRecordFragment = null;
			switch(position){
			case 0:{
//				telRecordFragment=TelRecordFragment.newInstance(TelRecordFragment.)
			}
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String msg = "";
			switch (position) {
			case 0: {
				msg = "已预约";
			}
				break;
			case 1: {
				msg = "通话";
			}
				break;
			case 2: {
				msg = "已关闭";
			}
				break;
			}
			return msg;
		}

	}

	/**
	 * 预约记录
	 * 
	 * @author Liu Gang
	 * 
	 *         2016年1月12日 上午9:47:29
	 * 
	 */
	private class RegRecordAdapter extends FragmentPagerAdapter {

		public RegRecordAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			RegRecordFragment regRecordFragment = null;
			switch (position) {
			case 0: {
				// 已预约
				regRecordFragment = RegRecordFragment
						.newInstance(RegRecordFragment.ITEMTYPE_READY_REGED);
			}
				break;
			case 1: {
				// 已就诊
				regRecordFragment = RegRecordFragment
						.newInstance(RegRecordFragment.ITEMTYPE_READY_VISIT);
			}
				break;
			case 2: {
				// 已爽约
				regRecordFragment = RegRecordFragment
						.newInstance(RegRecordFragment.ITEMTYPE_READY_MISS);
			}
				break;
			case 3: {
				// 已退号
				regRecordFragment = RegRecordFragment
						.newInstance(RegRecordFragment.ITEMTYPE_READY_BACKNUM);
			}
				break;
			}
			return regRecordFragment;
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String msg = "";
			switch (position) {
			case 0: {
				msg = "已预约";
			}
				break;
			case 1: {
				msg = "已就诊";
			}
				break;
			case 2: {
				msg = "爽约";
			}
				break;
			case 3: {
				msg = "已退号";
			}
				break;
			}
			return msg;
		}
	}

	/**
	 * 预约检查
	 * 
	 * @author Liu Gang
	 * 
	 *         2016年1月12日 上午9:47:14
	 * 
	 */
	private class RegCheckAdapter extends FragmentPagerAdapter {

		public RegCheckAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return regCheckRecordFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return regCheckRecordFragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}
	}

	/**
	 * 预约病床适配器
	 * 
	 * @author Liu Gang
	 * 
	 *         2016年1月12日 上午9:46:42
	 * 
	 */
	private class RegBedAdapter extends FragmentPagerAdapter {

		public RegBedAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return regBedRecordFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return regBedRecordFragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}
	}

	private void initRegCheckRecordFragment() {
		regCheckRecordFragmentList = new ArrayList<Fragment>();
	}

	private void initRegBedRecordFragment() {
		regBedRecordFragmentList = new ArrayList<Fragment>();
	}

	private void initTelRecordFragment() {
		telRecordFragmentList = new ArrayList<Fragment>();
	}

	@Override
	public void result(Object... object) {

	}
}
