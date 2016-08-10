/**
 * 
 */
package com.eztcn.user.eztcn.customView.wheel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.customView.wheel.adapters.AbstractWheelTextAdapter;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelChangedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelClickedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelScrollListener;
import com.eztcn.user.eztcn.customView.wheel.view.WheelView;

/**
 * @author Liu Gang
 * 
 *         2016年3月8日 下午2:46:13
 * 
 */
public class WheelDialog extends Dialog implements OnClickListener {
	private Context context;
	private int maxsize = 0;
	private int minsize = 0;
	private int defaultMaxSize=0;
	private int defaultMinSize=0;
	private int wheelCount = 5;
	private LinearLayout wheelLayout;
	private TextView wheelDialogCancleBtn;
	private TextView wheelDialogSureBtn;
	private TextView wheelTitleTv;
	private LinearLayout cancel_hint_layout;

	public interface SureBtnListener {
		public void sureClick(LinearLayout wheelLayout);
	}

	public interface CancleBtnListener {
		public void cancleClick();
	}

	private SureBtnListener sureListener;
	private CancleBtnListener cancleListener;

	public WheelDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.dialog_wheel);
		this.context = context;
		defaultMaxSize=6;
		defaultMinSize=4;
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		maxsize = (int) (defaultMaxSize* dm.density);
		minsize = (int) (defaultMinSize* dm.density);
//		maxsize = 24;
//		minsize = 20;
		wheelDialogSureBtn = (TextView) findViewById(R.id.wheelDialogSureBtn);
		wheelDialogCancleBtn = (TextView) findViewById(R.id.wheelDialogCancleBtn);
		wheelDialogSureBtn.setOnClickListener(this);
		wheelDialogCancleBtn.setOnClickListener(this);
		wheelTitleTv = (TextView) findViewById(R.id.wheelTitleTv);
		wheelLayout = (LinearLayout) findViewById(R.id.wheelLayout);
		cancel_hint_layout=(LinearLayout) findViewById(R.id.cancel_hint_layout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public void replaceData(int layoutPostion,List<String> data){
		WheelView wheelView =(WheelView) wheelLayout.getChildAt(layoutPostion);
		TextAdapter adapter = new TextAdapter(context, data,
				0, maxsize, minsize);
		setTextviewSize(0, adapter);
		wheelView.setCurrentItem(0);
		wheelView.setViewAdapter(adapter);
	}

	public void config(SureBtnListener sureListener,
			CancleBtnListener cancleListener, List<List<String>> wheelData,
			List<Integer> currIndexs, List<Integer> wheelIds,
			OnWheelChangedListener onWheelChangedListener,
			OnWheelClickedListener onWheelClickedListener,
			OnWheelScrollListener onWheelScrollListener, String titleStr) {
		this.sureListener = sureListener;
		this.cancleListener = cancleListener;
		if(wheelLayout.getChildCount()>0){
			wheelLayout.removeAllViews();//再次点击配置时候如果内部已经有控件则应该全部移除再去添加
		}
		if (StringUtils.isNotEmpty(titleStr))
			wheelTitleTv.setText(titleStr);
		for (int i = 0; i < wheelData.size(); i++) {
			WheelView wheelView = new WheelView(context);
			wheelView.setVisibleItems(wheelCount);
			wheelView.setId(wheelIds.get(i));
			TextAdapter adapter = new TextAdapter(context, wheelData.get(i),
					currIndexs.get(i), maxsize, minsize);
			setTextviewSize(currIndexs.get(i), adapter);
		
			wheelView.setViewAdapter(adapter);
			wheelView.addChangingListener(onWheelChangedListener);
			wheelView.addClickingListener(onWheelClickedListener);
			wheelView.addScrollingListener(onWheelScrollListener);
			if (wheelData.size() > 1 && i > 1) {// 添加纵向直线
				View verView = LayoutInflater.from(context).inflate(
						R.layout.ver_line, null);
				wheelLayout.addView(verView);
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
			wheelLayout.addView(wheelView,params);
			
			wheelView.setCurrentItem(currIndexs.get(i));
		}
	}

	@Override
	public void onClick(View v) {
		if (v == wheelDialogCancleBtn) {
			this.dismiss();
		}
		if (v == wheelDialogSureBtn) {
			sureListener.sureClick(wheelLayout);
		}
	}

	public class TextAdapter extends AbstractWheelTextAdapter {
		List<String> list;

		protected TextAdapter(Context context, List<String> list2,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_text, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list2;
			setItemTextResource(R.id.tv);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(int curIndex, TextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			if (curIndex == i) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	public LinearLayout getWheelLayout() {
		return wheelLayout;
	}
	
	
	public LinearLayout getHintView(){
		
		return cancel_hint_layout;
		
	}
	//设置提示可见。
	public void setHintViewVisiable(){
		
		LinearLayout hintLayout=getHintView();
		if(hintLayout!=null){
			hintLayout.setVisibility(View.VISIBLE);
		}
		
	}
}
