package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.City;

/**
 * @title 区域adapter
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */

public class AreaDataAdapter extends BaseArrayListAdapter<City> {

	private int selectedPosition = -1;

	public AreaDataAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView area;
		RelativeLayout layout;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_area, null);
			holder.area = (TextView) convertView.findViewById(R.id.area);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.colorlayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		City city = mList.get(position);
		// 设置选中效果
		if (selectedPosition == position) {
			holder.area.setTextColor(mContext.getResources().getColor(
					android.R.color.white));
			holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.choicehoiceAll));
			
		} else {
			holder.area.setTextColor(mContext.getResources().getColor(
					R.color.dark_black));
			// holder.layout.setBackgroundColor(Color.TRANSPARENT);
			holder.layout.setBackgroundColor(mContext.getResources().getColor(
					android.R.color.white));
		}
		holder.area.setText(city.getAreaName());
		return convertView;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

}
