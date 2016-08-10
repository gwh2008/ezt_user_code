package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 城市、区域选择
 * @describe
 * @author ezt
 * @created 2015年4月22日
 */
public class ChoiceAreaAdapter extends BaseArrayListAdapter<String> {

	private int selectedPosition = -1;// 选中位置

	public ChoiceAreaAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_area, null);
		}
		TextView cityName = (TextView) convertView
				.findViewById(R.id.area);
		RelativeLayout layout = (RelativeLayout) convertView
				.findViewById(R.id.colorlayout);
		cityName.setText(mList.get(position));
		// 设置选中效果
		if (selectedPosition == position) {
			cityName.setTextColor(mContext.getResources().getColor(
					android.R.color.white));
			layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.green));
		} else {
			cityName.setTextColor(mContext.getResources().getColor(
					R.color.dark_black));
			layout.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	public void setSelected(int position) {
		selectedPosition = position;
		notifyDataSetInvalidated();
	}

}
