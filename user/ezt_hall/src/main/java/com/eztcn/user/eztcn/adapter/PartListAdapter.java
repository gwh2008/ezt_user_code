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
 * @title 部位(/疾病类型)adapter
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class PartListAdapter extends BaseArrayListAdapter<String> {

	private int selectedPosition = -1;

	public PartListAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView partName;
		RelativeLayout layout;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_part, null);
			holder.partName = (TextView) convertView
					.findViewById(R.id.item_part_name);// 部位
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.colorlayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String str = mList.get(position);
		holder.partName.setText(str);

		// 设置选中效果
		if (selectedPosition == position) {
			holder.partName.setTextColor(mContext.getResources().getColor(
					android.R.color.white));
			holder.layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.green));
		} else {
			holder.partName.setTextColor(mContext.getResources().getColor(
					android.R.color.black));
			holder.layout.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

}
