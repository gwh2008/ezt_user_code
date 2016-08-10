package com.eztcn.user.eztcn.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.adapter.GuidancePagerAdapter;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.hall.activity.*;

/**
 * @title 引导页面
 * @describe
 * @author ezt
 * @created 2015年1月23日
 */
public class GuidanceActivity extends FinalActivity implements
		OnPageChangeListener {//,OnClickListener

	@ViewInject(R.id.viewpage)
	private ViewPager viewPager;//

	@ViewInject(R.id.skip)//, click = "onClick"
	private TextView skip;// 跳过按钮
	@ViewInject(R.id.start_Button)//, click = "onClick"
	private TextView startButton;// 开始按钮

	private ArrayList<View> views;
	private int[] images;
	private PagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guidance);
		ViewUtils.inject(GuidanceActivity.this);
		images = new int[] { R.drawable.loading_bg1, R.drawable.loading_bg2,
				R.drawable.loading_bg3,R.drawable.loading_bg4 };
		initial();
	}

	/**
	 * 初始化
	 */
	private void initial() {
		views = new ArrayList<View>();
		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			ImageView imageView = new ImageView(mContext);
			imageView.setBackgroundResource(images[i]);
			views.add(imageView);
		}
		pagerAdapter = new GuidancePagerAdapter(views);
		viewPager.setAdapter(pagerAdapter); // 设置适配器
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

		// 显示最后一个图片时显示按钮
		if (arg0 == views.size() - 1) {
			skip.setVisibility(View.GONE);
			startButton.setVisibility(View.VISIBLE);
		} else {
			startButton.setVisibility(View.INVISIBLE);
			skip.setVisibility(View.VISIBLE);
		}

	}
	
	

//	@Override
//	public void onClick(View v) {
	@OnClick({R.id.skip,R.id.start_Button})
	public void click(View v){
	if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_FIRST, true)) {
			SystemPreferences.save(EZTConfig.KEY_IS_FIRST, false);
			startActivity(new Intent(mContext, com.eztcn.user.hall.activity.MainActivity.class));
		}
		this.finish();
	}

}
