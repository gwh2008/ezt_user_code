package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.adapter.Order_RecordAdapter.onRecordClickListener;
import com.eztcn.user.eztcn.bean.Record_Info;

/**
 * @title 用户评价
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
public class UnEvaluateAdapter extends BaseArrayListAdapter<Record_Info> {

	private int itemType;
	private onRecordClickListener listener;

	public UnEvaluateAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Record_Info info = null;
		if (convertView == null) {
			convertView = View
					.inflate(mContext, R.layout.item_unevaluate, null);
		}
		TextView time = (TextView) convertView.findViewById(R.id.time);
		info = mList.get(position);
		String beginTime = info.getBeginTime();
		String endTime = info.getEndTime();
		String date = info.getDate();
		try {
			String temp = date.substring(0, date.indexOf(" "))
					+ " "
					+ beginTime.substring(beginTime.indexOf(" ") + 1,
							beginTime.lastIndexOf(":"))
					+ "-"
					+ endTime.substring(endTime.indexOf(" ") + 1,
							endTime.lastIndexOf(":"));
			time.setText(temp);
		} catch (Exception e) {

		}

		return convertView;
	}

}
