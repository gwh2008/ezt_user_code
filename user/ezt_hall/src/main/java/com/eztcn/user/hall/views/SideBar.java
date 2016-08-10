package com.eztcn.user.hall.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 自定义右边选择条
 * @describe 用于城市选择页面右边选择
 * @author ezt
 * @created 2014年12月21日
 */
public class SideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public  String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选中
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context) {
		super(context);
	}

    private int paddingTop=0;
    private int paddingBottom=0;
    private int totalHeight=0;
    private int drawHeight=0;

    /**
     * 自定义右侧竖直栏显示的内容，数组
     * @param data
     */
    public void setIndexData(String[] data){
        this.b=data;
        invalidate();
    }

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        if (paddingBottom==0){
            paddingBottom=getPaddingBottom();
            paddingTop=getPaddingTop();
            totalHeight=getHeight();
            drawHeight=totalHeight-paddingTop-paddingBottom;
        }

		// 获取焦点改变背景颜色.
		int height = drawHeight;// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / b.length;// 获取每一个字母的高度

		for (int i = 0; i < b.length; i++) {
			 paint.setColor(Color.WHITE);
			paint.setAntiAlias(true);
			paint.setTextSize(26);
			// 给手指按下时选中字母设置不同的颜色
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos+paddingTop, paint);

			paint.reset();// 重置画笔
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) ((y-paddingTop) / drawHeight * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
//			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
            invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			if(del!=null){
				del.cancelDel();
			}
			
			break;

		default:
			// 点击的时候设置右侧字母列表[A,B,C,D,E....]的背景颜色
//			setBackgroundResource(R.drawable.shape_sortlistview_sidebar);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}
	
	CancelDel del;
	//首字母选中，消失处理
	public interface CancelDel{
		public void cancelDel();
		
	}
	
	public void cancelDel(CancelDel del){
		this.del=del;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}