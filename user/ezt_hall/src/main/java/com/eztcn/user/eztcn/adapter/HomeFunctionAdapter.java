/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Function;

/**
 * @author Liu Gang
 * 
 *         2016年1月4日 下午2:35:32
 * 
 */
public class HomeFunctionAdapter extends BaseArrayListAdapter<Function> {
	/**
	 * 每个组件的宽度
	 */
	private int itemWidth;

	public HomeFunctionAdapter(Activity context, int itemWidth) {
		super(context);
		this.itemWidth = itemWidth;
	}

	private class Holder {
		private TextView tv_function;
		private ImageView iv_function;
	}

	private Holder holder;
	private int onTouchPosition=-1;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new Holder();
			convertView = View.inflate(mContext, R.layout.item_home_function,
					null);
			holder.iv_function = (ImageView) convertView
					.findViewById(R.id.iv_function);
			holder.tv_function = (TextView) convertView
					.findViewById(R.id.tv_function);
//			LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
//					itemWidth, LayoutParams.WRAP_CONTENT);
//			convertView.setLayoutParams(linearLayoutParams);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
	
		// 设置选中效果
		if (onTouchPosition == position) {
			holder.tv_function.setTextColor(mContext.getResources().getColor(
					R.color.main_color));
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.light_gray));
		} else {
			holder.tv_function.setTextColor(mContext.getResources().getColor(
					R.color.dark_black));
			convertView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
		}
		Function function = mList.get(position);
		holder.iv_function.setImageBitmap(function.getImageBitmap());
		holder.tv_function.setText(function.getName());
		return convertView;
	}
	public void setTouchPosition(int position) {
		onTouchPosition = position;
		notifyDataSetInvalidated();
		handler.sendMessageDelayed(handler.obtainMessage(), 200);
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			onTouchPosition = -1;
			notifyDataSetInvalidated();
		};
	};

}
