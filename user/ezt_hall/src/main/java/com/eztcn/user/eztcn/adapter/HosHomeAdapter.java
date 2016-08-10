/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Function;

/**
 * @author Liu Gang
 * 
 *         2016年3月10日 上午9:46:49
 * 
 */

public class HosHomeAdapter extends BaseArrayListAdapter<Function> {
	private Holder holder;
	private Context context;

	private class Holder {
		TextView functionTv;
		ImageView openView;
		ImageView functionView;
	}

	public HosHomeAdapter(Activity context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_hoshomfun, null);
			holder.functionTv = (TextView) convertView
					.findViewById(R.id.functionNameTv);
			holder.functionView = (ImageView) convertView
					.findViewById(R.id.functionView);
			holder.openView = (ImageView) convertView
					.findViewById(R.id.noOpenView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Function function = mList.get(position);
		String text = function.getName();
		if (!TextUtils.isEmpty(text))
			holder.functionTv.setText(text);
		if (function.getIsOpen() == 1) {
			holder.openView.setVisibility(View.INVISIBLE);
		} else {
			holder.openView.setVisibility(View.VISIBLE);
		}
		holder.functionView.setImageResource(function.getDrawableId());
		return convertView;
	}

}
