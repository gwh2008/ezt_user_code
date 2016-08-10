package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 检测报告
 * @describe
 * @author ezt
 * @created 2015年3月3日
 */
public class ExamReportAdapter extends BaseArrayListAdapter<String> {

	public ExamReportAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_examreport, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.examName = (TextView) convertView
					.findViewById(R.id.examName);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.time.setText();
		// holder.examName.setText();
		// holder.name.setText();
		return convertView;
	}

	class ViewHolder {
		TextView time;
		TextView examName;
		TextView name;
	}
}
