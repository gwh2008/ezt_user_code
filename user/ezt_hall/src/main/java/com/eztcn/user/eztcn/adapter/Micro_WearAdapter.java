package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eztcn.user.R;

public class Micro_WearAdapter extends BaseArrayListAdapter<String> {

	public Micro_WearAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_micro_wear, null);
		}
		return convertView;
	}

}
