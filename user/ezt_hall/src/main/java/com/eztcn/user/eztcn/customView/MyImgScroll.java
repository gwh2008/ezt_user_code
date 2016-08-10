package com.eztcn.user.eztcn.customView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.utils.FixedSpeedScroller;

/**
 * @title 自定义图片滚动类
 * @describe 首页广告图使用到
 * @author ezt
 * @created 2014年12月10日
 */
public class MyImgScroll extends ViewPager {

	int mScrollTime = 0;
	List<View> mListViews; // 图片组

	Timer timer;
	public int oldIndex = 0;
	public int curIndex = 0;
	Activity mActivity; // 上下文

	public MyImgScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setmListViews(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	public List<View> getmListViews() {
		return mListViews;
	}

	public void setmScrollTime(int mScrollTime) {
		this.mScrollTime = mScrollTime;
	}

	private boolean scrollble = true;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!scrollble) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}

	/**
	 * 开始广告滚动
	 * 
	 * @param mainActivity
	 */
	public void start(Activity mainActivity, LinearLayout ovalLayout) {
		mActivity = mainActivity;
		if (mListViews.size() <= 1) {
			ovalLayout.setVisibility(View.GONE);
		} else {
			ovalLayout.setVisibility(View.VISIBLE);
			// 设置圆点
			setOvalLayout(ovalLayout, R.layout.item_bottom_point,
					R.id.point_view, R.drawable.home_point_green,
					R.drawable.home_potint_grey);
		}

		this.setAdapter(new MyPagerAdapter());// 设置适配器

		if (mScrollTime != 0 && mListViews.size() > 1) {
			// 设置滑动动画时间 ,如果用默认动画时间可不用 ,反射技术实现
			new FixedSpeedScroller(mActivity).setDuration(this, 700);
			startTimer();
		}
	}

	// 设置圆点
	private void setOvalLayout(final LinearLayout ovalLayout, int ovalLayoutId,
			final int ovalLayoutItemId, final int focusedId, final int normalId) {
		if (ovalLayout != null) {
			LayoutInflater inflater = LayoutInflater.from(mActivity);
			for (int i = 0; i < mListViews.size(); i++) {
				ovalLayout.addView(inflater.inflate(ovalLayoutId, null));

			}
			// 选中第一个
			View view = ovalLayout.getChildAt(0);
			View view1 = view.findViewById(ovalLayoutItemId);
			view1.setBackgroundResource(focusedId);//

			this.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int i) {

					curIndex = i % mListViews.size();
										// 取消圆点选中
					Log.i(VIEW_LOG_TAG, ""+curIndex);
					
					
					ovalLayout.getChildAt(oldIndex)
							.findViewById(ovalLayoutItemId)
							.setBackgroundResource(normalId);
					// 圆点选中
					ovalLayout.getChildAt(curIndex)
							.findViewById(ovalLayoutItemId)
							.setBackgroundResource(focusedId);
					oldIndex = curIndex;
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
	}

	/**
	 * 取得当明选中下标
	 * 
	 * @return
	 */
	public int getCurIndex() {
		return curIndex;
	}

	/**
	 * 停止滚动
	 */
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 开始滚动
	 */
	public void startTimer() {
		MyImgScroll.this.setCurrentItem(mListViews.size()*100);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						MyImgScroll.this.setCurrentItem(MyImgScroll.this
								.getCurrentItem() + 1);
					}
				});
			}
		}, mScrollTime, mScrollTime);
	}

	// 适配器 //循环设置
	private class MyPagerAdapter extends PagerAdapter {
		public void finishUpdate(View arg0) {
		}

		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}

		public int getCount() {
			if (mListViews.size() == 1) {// 一张图片时不用流动
				return mListViews.size();
			}
			return Integer.MAX_VALUE;
		}

		public Object instantiateItem(View v, int i) {
			if (((ViewPager) v).getChildCount() >= mListViews.size()) {
				((ViewPager) v)
						.removeView(mListViews.get(i % mListViews.size()));
			}
			try {
				((ViewPager) v).addView(mListViews.get(i % mListViews.size()),
						0);
			} catch (Exception e) {
				return v;
			}
			return mListViews.get(i % mListViews.size());
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
		}
	}

}
