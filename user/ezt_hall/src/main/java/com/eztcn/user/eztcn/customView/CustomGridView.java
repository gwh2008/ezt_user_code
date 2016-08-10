/**
 * 
 */
package com.eztcn.user.eztcn.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author Liu Gang
 * 
 * 2015年12月1日
 * 下午3:28:05
 * 
 */
public class CustomGridView extends GridView {

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public CustomGridView(Context context) {
		super(context);
	}
	 /** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
    }  
	

}
