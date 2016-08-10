package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @title 离我最近医院
 * @describe
 * @author ezt
 * @created 2015年4月2日
 */
public class NearHosAdapter extends BaseArrayListAdapter<Hospital> {

	public NearHosAdapter(Activity context) {
		super(context);
	}

	static class ViewHolder {
		TextView hosName, hosDistance;

		View view;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_near_hos, null);

			holder.hosName = (TextView) convertView
					.findViewById(R.id.item_near_hos);// 医院名称

			holder.hosDistance = (TextView) convertView
					.findViewById(R.id.item_distance);// 距离
			holder.view = convertView.findViewById(R.id.line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Hospital hos = mList.get(position);
		holder.hosName.setText(hos.gethName());
		holder.hosDistance.setText(hos.gethDistance() + "km");

		return convertView;
	}

}
