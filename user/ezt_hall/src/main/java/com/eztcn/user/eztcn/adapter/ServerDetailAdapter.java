package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 服务详情
 * @describe
 * @author ezt
 * @created 2015年1月13日
 */
public class ServerDetailAdapter extends BaseArrayListAdapter<String> {

	public ServerDetailAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_serverdetail, null);
		}
		TextView serverName = (TextView) convertView
				.findViewById(R.id.serverName);
		TextView serverCount = (TextView) convertView
				.findViewById(R.id.serverCount);
		TextView surplusCount = (TextView) convertView
				.findViewById(R.id.surplusCount);
		serverName.setText("陪诊服务");
		serverCount.setText("3");
		surplusCount.setText("3");
		return convertView;
	}

}
