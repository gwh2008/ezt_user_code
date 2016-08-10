package com.eztcn.user.eztcn.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class RotateTextView extends TextView {
	private static final   String NAME_SPACE = "http://www.baidu.com/apk/res/custom";
	
	private static final String ATTR_ROTATE = "rotate";
	private static final int DEFAULT_VALUE_ROTATE = 0;
	private static final String ATTR_TRANSLATE_X= "translateX";
	private static final String ATTR_TRANSLATE_Y = "translateY";
	private static final float DEFAULT_VALUE_TRANSLATE_X = 0f;
	private static final float DEFAULT_VALUE_TRANSLATE_Y = 0f;
	
	private int rotate = DEFAULT_VALUE_ROTATE;
	
	private float translateX = DEFAULT_VALUE_TRANSLATE_X;
	private float translateY = DEFAULT_VALUE_TRANSLATE_Y;
	
	public RotateTextView(Context context) {
		super(context);
	}
	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		rotate = attrs.getAttributeIntValue(NAME_SPACE, ATTR_ROTATE, DEFAULT_VALUE_ROTATE);//旋转度数
		translateX = attrs.getAttributeFloatValue(NAME_SPACE, ATTR_TRANSLATE_X, DEFAULT_VALUE_TRANSLATE_X);//获取在布局中的x轴偏移百分比
		translateY = attrs.getAttributeFloatValue(NAME_SPACE, ATTR_TRANSLATE_Y, DEFAULT_VALUE_TRANSLATE_Y);//获取在布局中的y轴偏移百分比
	}

	
	public RotateTextView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs,defStyle);
		rotate = attrs.getAttributeIntValue(NAME_SPACE, ATTR_ROTATE, DEFAULT_VALUE_ROTATE);//旋转度数
		translateX = attrs.getAttributeFloatValue(NAME_SPACE, ATTR_TRANSLATE_X, DEFAULT_VALUE_TRANSLATE_X);//获取在布局中的x轴偏移百分比
		translateY = attrs.getAttributeFloatValue(NAME_SPACE, ATTR_TRANSLATE_Y, DEFAULT_VALUE_TRANSLATE_Y);//获取在布局中的y轴偏移百分比
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.translate(getMeasuredWidth()*translateX, getMeasuredHeight()*translateY);
		//首先偏移在旋转，是因为，如果先旋转，本身xy坐标系也会跟着旋转，之后在偏移会不方便我们的控制，也不直观
		canvas.rotate(rotate);
		super.onDraw(canvas);
	}
	
}
