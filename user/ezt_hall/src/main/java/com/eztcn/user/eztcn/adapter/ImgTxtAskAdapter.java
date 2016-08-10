package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 图文问诊记录
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class ImgTxtAskAdapter extends BaseArrayListAdapter<String> {

	private ViewHolder holder;

	public ImgTxtAskAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_imgtxt, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.question = (TextView) convertView
					.findViewById(R.id.question);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {

		TextView name;
		TextView time;
		TextView question;
	}
}
