package com.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.testandroid.R;
import com.zxing.camera.CameraManager;

/**
 * @title 自定义扫描框
 * @describe
 * @author ezt
 * @created 2014年11月27日
 */
public final class ScanView extends View {

	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;
	private final Paint paint;
	private Paint textPaint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private int i = 0;
	private Rect lineRect;
	private Drawable scanLine;
	private float circleWidth = 0;// 修饰边框长度
	private float circleheight = 0;// 修饰边框高度
	private Context context;

	public ScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		lineRect = new Rect();
		scanLine = context.getResources().getDrawable(R.drawable.scan_line);
		paint = new Paint();
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		if (circleWidth == 0 && circleheight == 0) {
			circleWidth = (frame.bottom - frame.top) / 10;
			circleheight = (frame.bottom - frame.top) / 42;
		}
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {
			// 绘制最外层线
			paint.setColor(context.getResources().getColor(
					android.R.color.white));
			paint.setAlpha(50);
			canvas.drawRect(frame.left, frame.top, frame.right + 1,
					frame.top + 2, paint);
			canvas.drawRect(frame.left, frame.top + 2, frame.left + 2,
					frame.bottom - 1, paint);
			canvas.drawRect(frame.right - 1, frame.top, frame.right + 1,
					frame.bottom - 1, paint);
			canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1,
					frame.bottom + 1, paint);
			// 绘制修饰框
			paint.setColor(context.getResources().getColor(R.color.main_color));
			// 左上（横/纵）
			canvas.drawRect(frame.left + 1, frame.top + 1, frame.left
					+ circleWidth - 1, frame.top + circleheight - 1, paint);
			canvas.drawRect(frame.left + 1, frame.top + 1, frame.left
					+ circleheight - 1, frame.top + circleWidth - 1, paint);
			// 右上（横/纵）
			canvas.drawRect(frame.right - circleWidth + 1, frame.top + 1,
					frame.right, frame.top + circleheight - 1, paint);
			canvas.drawRect(frame.right - circleheight + 2, frame.top + 1,
					frame.right, frame.top + circleWidth - 1, paint);
			// 左下（横/纵）
			canvas.drawRect(frame.left + 1, frame.bottom - circleheight + 2,
					frame.left + circleWidth - 1, frame.bottom, paint);
			canvas.drawRect(frame.left + 1, frame.bottom - circleWidth + 2,
					frame.left + circleheight - 1, frame.bottom, paint);
			// 右下（横/纵）
			canvas.drawRect(frame.right - circleWidth + 1, frame.bottom
					- circleheight + 2, frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - circleheight + 2, frame.bottom
					- circleWidth + 2, frame.right, frame.bottom, paint);

			// 绘制提醒文字
			int middle = width / 2;
			textPaint.setTextSize(getResources().getDimension(
					R.dimen.medium_size));
			textPaint.setColor(Color.WHITE);
			textPaint.setAlpha(100);

			String text = getResources().getString(R.string.qrcode_scan_tip);
			float textHalf = textPaint.measureText(text) / 2;
			middle = middle - (int) textHalf;
			canvas.drawText(text, middle, frame.bottom
					+ circleheight*10,
					textPaint);

			// 绘制扫描线
			if ((i += 5) < frame.bottom - frame.top) {
				lineRect.set(frame.left + (int) circleheight, frame.top
						- (int) circleheight + i, frame.right
						- (int) circleheight, frame.top + (int) circleheight
						+ i);
				scanLine.setBounds(lineRect);
				scanLine.setAlpha(180);
				scanLine.draw(canvas);
				invalidate();
			} else {
				i = 0;
			}
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	// public void addPossibleResultPoint(ResultPoint point) {

	// Logger.i("------------>", "触发");
	// }

}
