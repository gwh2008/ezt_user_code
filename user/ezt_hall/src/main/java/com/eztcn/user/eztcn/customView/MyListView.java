package com.eztcn.user.eztcn.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @title 自定义listview
 * @describe 嵌入scrollview显示全
 * @author ezt
 * @created 2014年12月22日
 */
public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.UNSPECIFIED);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}