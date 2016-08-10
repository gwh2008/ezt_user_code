/**
 * 
 */
package com.eztcn.user.eztcn.utils;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.eztcn.user.R;

/**
 * @author Liu Gang
 * 
 * 2015年11月20日
 * 下午3:51:47
 * 
 */
public class CustomPopupWindow extends PopupWindow {
	
	/**
	 * 窗口设置
	 */
	public CustomPopupWindow(View view,int width,int height) {
		setContentView(view);
		setWidth(width);
		setHeight(height);
		setAnimationStyle(R.style.aboveAnimation);// 设置动画  
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
		setFocusable(true);
		setOutsideTouchable(false);
	}
	/**
	 * 在中间显示
	 * @param view
	 */
	public void showInCenter(View view){
		showAtLocation(view, Gravity.CENTER, 0,0);
	}
	public void show(View view,int gravity,int x,int y){
		showAtLocation(view, gravity, x, y);
	}
	
}
