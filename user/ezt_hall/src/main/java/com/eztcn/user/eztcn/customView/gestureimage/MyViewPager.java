package com.eztcn.user.eztcn.customView.gestureimage;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager {
	private GestureImageView[] images;
	float diffX;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View arg0, boolean arg1, int arg2, int arg3,
			int arg4) {
		return super.canScroll(arg0, arg1, arg2, arg3, arg4);
	}

	private final PointF current = new PointF();
	private final PointF last = new PointF();

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			last.x = event.getX();
			last.y = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (event.getPointerCount() <= 1) {
				GestureImageView image = images[getCurrentItem()];
				// 图片宽度
				float width = image.getScale() * image.getImageWidth();
				float centerX = image.getImageX();
				// 图片左边缘坐标
				float left = centerX - width / 2;
				// 图片右边缘坐标
				float right = left + width;
				current.x = event.getX();
				diffX = (current.x - last.x);
				if (diffX >= 0) {// 往左切换
					if ((int) left >= 0) {
					} else {
						return false;
					}
				} else {// 往右切换
					if ((int) right <= image.getDisplayWidth()) {
					} else {
						return false;
					}
				}
				last.x = current.x;
			}
		}
		return super.onTouchEvent(event);
	}

	public void setGestureImages(GestureImageView[] images) {
		this.images = images;
	}
}
