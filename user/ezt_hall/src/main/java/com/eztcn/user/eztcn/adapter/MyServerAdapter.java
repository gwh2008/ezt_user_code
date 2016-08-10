package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 我的服务
 * @describe
 * @author ezt
 * @created 2015年1月13日
 */
public class MyServerAdapter extends BaseArrayListAdapter<String> {

	public MyServerAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_myserver, null);
		}
		TextView serverName = (TextView) convertView
				.findViewById(R.id.serverName);
		TextView orderServer = (TextView) convertView
				.findViewById(R.id.orderServer);
		orderServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		return convertView;
	}

}
