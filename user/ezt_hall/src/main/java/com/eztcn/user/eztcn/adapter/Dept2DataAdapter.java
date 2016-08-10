package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Dept;

/**
 * @title 科室（列表）adapter
 * @describes
 * @author ezt
 * @created 2014年12月18日
 */

public class Dept2DataAdapter extends BaseArrayListAdapter<Dept> {

	private int selectedPosition = -1;

	private boolean isPop;// 是否为下拉

	public Dept2DataAdapter(Activity context, boolean isPop) {
		super(context);
		this.isPop = isPop;
	}

	class ViewHolder {
		TextView deptName;
		RelativeLayout layout;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();

			if (this.isPop) {
				convertView = View.inflate(mContext, R.layout.pop_item_dept,
						null);
			} else {
				convertView = View.inflate(mContext, R.layout.item_dept, null);
			}

			holder.deptName = (TextView) convertView
					.findViewById(R.id.item_dept_name);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.colorlayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Dept dept = mList.get(position);

		holder.deptName.setText(dept.getdName());
		// 设置选中效果
		if (selectedPosition == position) {
			holder.deptName.setTextColor(mContext.getResources().getColor(
					android.R.color.white));
			holder.layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.choicehoiceAll));
		} else {
			holder.deptName.setTextColor(mContext.getResources().getColor(
					R.color.dark_black));
			holder.layout.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

}
