package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.City;

/**
 * @title 常用城市adapter
 * @describe
 * @author ezt
 * @created 2014年12月21日
 */
public class CityCommonAdapter extends BaseArrayListAdapter<City> {

	public CityCommonAdapter(Activity context) {
		super(context);
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final City mContent = mList.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_city,
					null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tvTitle.setText(mContent.getCityName());
		return view;

	}

	final static class ViewHolder {
		TextView tvTitle;
	}

}