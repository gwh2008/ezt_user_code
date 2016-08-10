package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @title 适用医院
 * @describe
 * @author ezt
 * @created 2015年3月30日
 */
public class LightAccompany_ApplyHosAdapter extends
		BaseArrayListAdapter<Hospital> {

	private ViewHolder holder;

	public LightAccompany_ApplyHosAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_apply_hos, null);
			holder.tvArea = (TextView) convertView.findViewById(R.id.item_area);
			holder.tvHosName = (TextView) convertView
					.findViewById(R.id.item_hos_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Hospital hos = mList.get(position);
		String area = hos.gethAddress();
		if (!TextUtils.isEmpty(area)) {
			holder.tvArea.setVisibility(View.VISIBLE);
			holder.tvHosName.setVisibility(View.GONE);
			holder.tvArea.setText(area);
		} else {
			holder.tvHosName.setVisibility(View.VISIBLE);
			holder.tvArea.setVisibility(View.GONE);
			String newHosName = hos.gethName() + "(" + hos.getHosLevel() + ")";
			holder.tvHosName.setText(newHosName);
		}

		return convertView;
	}

	class ViewHolder {

		TextView tvArea;
		TextView tvHosName;
	}
}
