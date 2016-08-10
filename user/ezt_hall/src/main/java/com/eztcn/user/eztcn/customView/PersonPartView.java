package com.eztcn.user.eztcn.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.eztcn.user.R;

/**
 * @title 自定义view
 * @describe 症状自查
 * @author ezt
 * @created 2014年12月24日
 */
public class PersonPartView extends View {

	private Context context;
	private int img_x;
	private int img_y;
	private int img_width;
	private int img_height;
	private int point_x;
	private int point_y;
	private Rect imgRect;
	private boolean canClick;

	private int sexType = 0;// 0为男，1为女
	private int position = 0;// 0正，1反

	public PersonPartView(Context context) {
		super(context);
		this.context = context;
	}

	public PersonPartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public PersonPartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

	}

	public int getSexType() {
		return sexType;
	}

	public void setSexType(int sexType) {
		this.sexType = sexType;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	Bitmap bitmap;
	Bitmap pointBitmap;
	float tX, tY;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();

		if (sexType == 0 && position == 0) {// 男正
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.avatar_male_front);
		} else if (sexType == 0 && position == 1) {// 男反
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.avatar_male_back);
		} else if (sexType == 1 && position == 0) {// 女正
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.avatar_female_front);
		} else if (sexType == 1 && position == 1) {// 女反
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.avatar_female_back);
		}
		pointBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.avatar_click_effect1);

		img_width = bitmap.getWidth();
		img_height = bitmap.getHeight();
		point_x = pointBitmap.getWidth();
		point_y = pointBitmap.getHeight();
		img_x = getWidth() / 2 - img_width / 2;
		img_y = getHeight() / 2 - img_height / 2;
		canvas.drawBitmap(bitmap, img_x, img_y, paint);

		if (canClick) {
			canvas.drawBitmap(pointBitmap, tX - point_x / 2, tY - point_y / 2,
					paint);
		}
		paint.setColor(Color.TRANSPARENT);
		imgRect = new Rect(img_x, img_y, img_x + img_width, img_y + img_height);
		canvas.drawRect(imgRect, paint);
	}

	public void canvasTxt(float x, float y) {
		if (x < img_x || x > img_x + img_width) {
			canClick = false;
			return;
		}
		if (y < img_y || y > img_y + img_height) {
			canClick = false;
			return;
		}
		int pixel=0;
		if(x<bitmap.getWidth()){
			pixel = bitmap.getPixel((int) x - img_x, (int) y - img_y);
		}
		if (Color.TRANSPARENT == pixel) {// 判断透明区域点击无效
			return;
		}
		float point_y = y - img_y;
		float point_x = x - img_x;
		// 等分
		float count = 28;
		float count_x = 15;
		float final_y = point_y / (img_height / count);
		float final_x = point_x / (img_width / count_x);

		// 部位类型（1头部，2颈部，3，手部，4胸部，5腹部，6臀部，7腿部）
		int part = 0;
		if (final_y > 1 && final_y <= 4) {
			part = 1;
		} else if (final_y > 4 && final_y <= 5) {
			part = 2;
		}
		// 判断手部位
		if (final_x <= 5 || final_x >= 10) {
			part = 3;
		} else {
			if (final_y > 5 && final_y <= 9) {
				part = 4;
			} else if (final_y > 9 && final_y <= 13) {
				part = 5;
			} else if (final_y > 13 && final_y <= 15) {
				part = 6;
			} else if (final_y > 15) {
				part = 7;
			}
		}
		tY = y;
		tX = x;
		invalidate();
		if (canClick) {
			listener.onPartClick(part);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			canClick = true;
			canvasTxt(x, y);
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			canClick = false;
			canvasTxt(x, y);
			break;
		}
		return canClick;
	}

	onPartClickListener listener;

	public interface onPartClickListener {

		public void onPartClick(int part);
	}

	public void onClickPart(onPartClickListener listener) {
		this.listener = listener;
	}
}
