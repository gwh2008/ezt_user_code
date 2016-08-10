package org.fireking.app.imagelib.view.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * @title 自定义裁剪图片layout
 * @describe
 * @author ezt
 * @created 2015年6月30日
 */
public class ClipImageLayout extends RelativeLayout {

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	private int mHorizontalPadding = 30;// 默认图片横向外边距

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
		initialHPadding();
	}

	/**
	 * 初始化边距
	 * 
	 * @param mHorizontalPadding
	 */
	private void initialHPadding() {
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}
	
	
	/**
	 * 设置裁剪图片
	 * @param bitmap
	 */
	public void initialImg(Bitmap bitmap){
		mZoomImageView.setImageBitmap(bitmap);
	}
	

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
		initialHPadding();
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

}
