package com.eztcn.user.eztcn.utils;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class PicUtils {
	/**
	 * 将图片等比例缩放 setAdjustViewBounds setMaxWidth setMaxWidth必须同时设置才有效
	 * 
	 * @param context
	 * @param view
	 *            父容器
	 * @param image
	 *            图片控件
	 * @param source
	 *            图片资源
	 */
	public static void setImageViewMathParent(Activity context,
			LinearLayout view, ImageView image, int source) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				source);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		float scalew = (float) displayMetrics.widthPixels
				/ (float) bitmap.getWidth();
		image.setScaleType(ScaleType.MATRIX);
		Matrix matrix = new Matrix();
		image.setAdjustViewBounds(true);
		if (displayMetrics.widthPixels < bitmap.getWidth()) {
			matrix.postScale(scalew, scalew);
		} else {
			matrix.postScale(1 / scalew, 1 / scalew);
		}
		image.setMaxWidth(displayMetrics.widthPixels);
		float ss = displayMetrics.heightPixels > bitmap.getHeight() ? displayMetrics.heightPixels
				: bitmap.getHeight();
		image.setMaxWidth((int) ss);
		view.removeAllViews();
		view.addView(image);
		if (bitmap != null && bitmap.isRecycled()) {
			bitmap.recycle();
		}

	}

	// 缩放图片
	public static Bitmap zoomImg(String img, int newWidth, int newHeight) {
		// 图片源
		Bitmap bm = BitmapFactory.decodeFile(img);
		if (null != bm) {
			return zoomImg(bm, newWidth, newHeight);
		}
		return null;
	}

	public static Bitmap zoomImg(Context context, String img, int newWidth,
			int newHeight) {
		// 图片源
		try {
			Bitmap bm = BitmapFactory.decodeStream(context.getAssets()
					.open(img));
			if (null != bm) {
				return zoomImg(bm, newWidth, newHeight);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}
	
	
	// 缩放图片
		public static Bitmap zoomImg(Bitmap bm, int newWidth) {
			// 获得图片的宽高
			int width = bm.getWidth();
			int height = bm.getHeight();
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth);
			// 得到新的图片
			Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
					true);
			return newbm;
		}
	
		
		// 缩放图片
				public static Bitmap zoomImgH(Bitmap bm, int newHeight) {
					// 获得图片的宽高
					int width = bm.getWidth();
					int height = bm.getHeight();
					// 计算缩放比例
					float scaleWidth = ((float) newHeight) / height;
					// 取得想要缩放的matrix参数
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleWidth);
					// 得到新的图片
					Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
							true);
					return newbm;
				}

	public static Bitmap zoomImg(int bitMapId, int newWidth, Context context) {
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				bitMapId);
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

}
