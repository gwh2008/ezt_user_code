package com.eztcn.user.eztcn.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * @title 处理scrollview
 * @describe 子控件横向滚动冲突问题 
 * @author ezt
 * @created 2015年1月10日
 */
public class MyScrollView extends ScrollView {

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 添加了一个手势选择器
		gestureDetector = new GestureDetector(new Yscroll());
		setFadingEdgeLength(0);
	}

	GestureDetector gestureDetector;
	View.OnTouchListener onTouchListener;

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& gestureDetector.onTouchEvent(ev);
	}

	class Yscroll extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 控制手指滑动的距离
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}

}
