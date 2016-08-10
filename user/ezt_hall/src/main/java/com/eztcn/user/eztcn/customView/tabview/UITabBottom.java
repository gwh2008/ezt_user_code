package com.eztcn.user.eztcn.customView.tabview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.UITabItem;

/**
 * @title 主界面底部标签栏
 * @describe
 * @author ezt
 * @created 2015年4月28日
 */
public class UITabBottom extends LinearLayout implements View.OnClickListener {

	public static interface OnUITabChangeListener {
		public void onTabChange(int index);
	}

	private UITabItem tab0;
	private UITabItem tab1;
	private UITabItem tab2;
	private UITabItem tab3;
	private UITabItem tab4;
	private ViewPager mViewPager;
	private OnUITabChangeListener changeListener;

	private int colorClick;
	private int colorUnclick;
	private int R1;// 未选中的Red值
	private int G1;// 未选中的Green值
	private int B1;// 未选中的Blue值
	private int R2;// 选中的Red值
	private int G2;// 选中的Green值
	private int B2;// 选中的Blue值
	private int Rm = R2 - R1;// Red的差值
	private int Gm = G2 - G1;// Green的差值
	private int Bm = B2 - B1;// Blue的差值

	private int mIndex;

	public UITabBottom(Context context) {
		super(context);
	}

	public UITabBottom(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewPager getmViewPager() {
		return mViewPager;
	}

	public void setmViewPager(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
	}

	private UITabItem newChildItem(int i) {

		UITabItem tabItem = new UITabItem();
		tabItem.parent = LayoutInflater.from(getContext()).inflate(
				R.layout.item_tab, null);
		tabItem.iconView = (UITabIconView) tabItem.parent
				.findViewById(R.id.mTabIcon);
		tabItem.labelView = (TextView) tabItem.parent
				.findViewById(R.id.mTabText);
		tabItem.tipView = tabItem.parent.findViewById(R.id.mTabTip);
		tabItem.parent.setTag(i);
		tabItem.parent.setOnClickListener(this);
		return tabItem;
	}

	public void init() {

		colorClick = getResources().getColor(
				R.color.tab_footer_textcolor_select);
		colorUnclick = getResources().getColor(R.color.tab_footer_textcolor);

		int tabBottomHeight = ViewGroup.LayoutParams.MATCH_PARENT;
		setOrientation(LinearLayout.HORIZONTAL);
		// tab0
		tab0 = newChildItem(0);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				0, tabBottomHeight);
		layoutParams.weight = 1;
		tab0.labelView.setText("门诊大厅");
		tab0.labelView.setTextColor(colorClick);
		tab0.iconView.init(R.drawable.home_outpatient_hall_s, R.drawable.home_outpatient_hall_un);
		addView(tab0.parent, layoutParams);
		// tab1
		/*tab1 = newChildItem(1);
		layoutParams = new LinearLayout.LayoutParams(0, tabBottomHeight);
		layoutParams.weight = 1;
		tab1.labelView.setText("健康资讯");
		tab1.labelView.setTextColor(colorUnclick);
		tab1.iconView.init(R.drawable.home_health_consultation_s, R.drawable.home_health_consultation_un);
		addView(tab1.parent, layoutParams);*/
		
		tab1 = newChildItem(1);
		layoutParams = new LinearLayout.LayoutParams(0, tabBottomHeight);
		layoutParams.weight = 1;
		tab1.labelView.setText("服务");
		tab1.labelView.setTextColor(colorUnclick);
		tab1.iconView.init(R.drawable.home_service_s, R.drawable.home_service_un);
		addView(tab1.parent, layoutParams);
		// tab2
		tab2 = newChildItem(2);
		layoutParams = new LinearLayout.LayoutParams(0, tabBottomHeight);
		layoutParams.weight = 1;
//		tab2.labelView.setText("药给送");
//		tab2.labelView.setTextColor(colorUnclick);
//		tab2.iconView.init(R.drawable.home_tab_send_c,
//				R.drawable.home_tab_send_u);
		tab2.labelView.setText("发现");
		tab2.labelView.setTextColor(colorUnclick);
		tab2.iconView.init(R.drawable.home_discovery_s,
				R.drawable.home_discovery_un);
		addView(tab2.parent, layoutParams);
		// tab3
		tab3 = newChildItem(3);
		layoutParams = new LinearLayout.LayoutParams(0, tabBottomHeight);
		layoutParams.weight = 1;
		tab3.labelView.setText("健康头条");
		tab3.labelView.setTextColor(colorUnclick);
		tab3.iconView.init(R.drawable.home_health_headline_s,
				R.drawable.home_health_headline_n);
//		tab3.labelView.setText("消息");
//		tab3.labelView.setTextColor(colorUnclick);
//		tab3.iconView.init(R.drawable.home_message_s,
//				R.drawable.home_message_un);
		addView(tab3.parent, layoutParams);
		
		tab4 = newChildItem(4);
		layoutParams = new LinearLayout.LayoutParams(0, tabBottomHeight);
		layoutParams.weight = 1;
		tab4.labelView.setText("我");
		tab4.labelView.setTextColor(colorUnclick);
		tab4.iconView.init(R.drawable.home_mine_new_s,
				R.drawable.home_mine_new_un);
		addView(tab4.parent, layoutParams);

		R1 = (colorClick & 0xff0000) >> 16;
		G1 = (colorClick & 0xff00) >> 8;
		B1 = (colorClick & 0xff);
		R2 = (colorUnclick & 0xff0000) >> 16;
		G2 = (colorUnclick & 0xff00) >> 8;
		B2 = (colorUnclick & 0xff);
		Rm = R1 - R2;
		Gm = G1 - G2;
		Bm = B1 - B2;
		mIndex = 0;
	}

	private OnUITabChangeListener getChangeListener() {
		return changeListener;
	}

	public void setChangeListener(OnUITabChangeListener changeListener) {
		this.changeListener = changeListener;
	}

	/**
	 * 底部导航栏红点显示/隐藏
	 * 
	 * @param index
	 *            下标
	 * @param isShow
	 *            是否显示
	 */
	public void setTabRedDot(int index, boolean isShow) {

		if (index == 0) {
			tab0.tipView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
		if (index == 1) {
			tab1.tipView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
		if (index == 2) {
			tab2.tipView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
		if (index == 3) {
			tab3.tipView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
		if (index == 4) {
			tab4.tipView.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}

	}

	public void selectTab(int index) {

		if (mIndex == index) {
			return;
		}

		mIndex = index;
		if (changeListener != null) {
			changeListener.onTabChange(mIndex);
		}
		// mIndex表示处于mIndex到mIndex+1页面之间
		switch (mIndex) {
		case 0:
			tab0.iconView.setmAlpha(255);
			tab1.iconView.setmAlpha(0);
			tab2.iconView.setmAlpha(0);
			tab3.iconView.setmAlpha(0);
			tab4.iconView.setmAlpha(0);
			tab0.labelView.setTextColor(colorClick);
			tab1.labelView.setTextColor(colorUnclick);
			tab2.labelView.setTextColor(colorUnclick);
			tab3.labelView.setTextColor(colorUnclick);
			tab4.labelView.setTextColor(colorUnclick);
			break;
		case 1:
			tab0.iconView.setmAlpha(0);
			tab1.iconView.setmAlpha(255);
			tab2.iconView.setmAlpha(0);
			tab3.iconView.setmAlpha(0);
			tab4.iconView.setmAlpha(0);
			tab0.labelView.setTextColor(colorUnclick);
			tab1.labelView.setTextColor(colorClick);
			tab2.labelView.setTextColor(colorUnclick);
			tab3.labelView.setTextColor(colorUnclick);
			tab4.labelView.setTextColor(colorUnclick);
			break;
		case 2:
			tab0.iconView.setmAlpha(0);
			tab1.iconView.setmAlpha(0);
			tab2.iconView.setmAlpha(255);
			tab3.iconView.setmAlpha(0);
			tab4.iconView.setmAlpha(0);
			tab0.labelView.setTextColor(colorUnclick);
			tab1.labelView.setTextColor(colorUnclick);
			tab2.labelView.setTextColor(colorClick);
			tab3.labelView.setTextColor(colorUnclick); 
			tab4.labelView.setTextColor(colorUnclick); 
			break;
		case 3:
			tab0.iconView.setmAlpha(0);
			tab1.iconView.setmAlpha(0);
			tab2.iconView.setmAlpha(0);
			tab3.iconView.setmAlpha(255);
			tab4.iconView.setmAlpha(0);
			tab0.labelView.setTextColor(colorUnclick);
			tab1.labelView.setTextColor(colorUnclick);
			tab2.labelView.setTextColor(colorUnclick);
			tab3.labelView.setTextColor(colorClick);
			tab4.labelView.setTextColor(colorUnclick);
			break;
		case 4:
			tab0.iconView.setmAlpha(0);
			tab1.iconView.setmAlpha(0);
			tab2.iconView.setmAlpha(0);
			tab3.iconView.setmAlpha(0);
			tab4.iconView.setmAlpha(255);
			tab0.labelView.setTextColor(colorUnclick);
			tab1.labelView.setTextColor(colorUnclick);
			tab2.labelView.setTextColor(colorUnclick);
			tab3.labelView.setTextColor(colorUnclick);
			tab4.labelView.setTextColor(colorClick);
			break;
		}

	}

	/**
	 * 拼成颜色值
	 * 
	 * @param f
	 * @return
	 */
	private int getColorInt(float f) {
		int R = (int) (R2 + f * Rm);
		int G = (int) (G2 + f * Gm);
		int B = (int) (B2 + f * Bm);
		return 0xff << 24 | R << 16 | G << 8 | B;

	}

	/**
	 * location为最左边页面的index,e.g.,fragment0到fragment1,传入location=0 f为滑动距离的百分比
	 * 
	 * @param location
	 * @param f
	 */
	public void scroll(int location, float f) {
		int leftAlpha = (int) (255 * (1 - f));
		int rightAlpha = (int) (255 * f);
		int leftColor = getColorInt(1 - f);
		int rightColor = getColorInt(f);
		switch (location) {
		case 0:
			tab0.iconView.setmAlpha(leftAlpha);
			tab1.iconView.setmAlpha(rightAlpha);

			tab0.labelView.setTextColor(leftColor);
			tab1.labelView.setTextColor(rightColor);
			break;
		case 1:
			tab1.iconView.setmAlpha(leftAlpha);
			tab2.iconView.setmAlpha(rightAlpha);

			tab1.labelView.setTextColor(leftColor);
			tab2.labelView.setTextColor(rightColor);
			break;
		case 2:
			tab2.iconView.setmAlpha(leftAlpha);
			tab3.iconView.setmAlpha(rightAlpha);

			tab2.labelView.setTextColor(leftColor);
			tab3.labelView.setTextColor(rightColor);
			break;
		case 3:
			tab3.iconView.setmAlpha(leftAlpha);
			tab4.iconView.setmAlpha(rightAlpha);

			tab3.labelView.setTextColor(leftColor);
			tab4.labelView.setTextColor(rightColor);
		}
	}

	@Override
	public void onClick(View v) {
		int i = ((Integer) v.getTag()).intValue();
		tabIntent(i);
	}

	/**
	 * 页面切换
	 * 
	 * @param pos
	 */
	public void tabIntent(int pos) {
		mViewPager.setCurrentItem(pos, false);
		selectTab(pos);

	}
}
