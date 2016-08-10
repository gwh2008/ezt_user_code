package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.LightAccompanying;

/**
 * @title 轻陪诊服务介绍adapter
 * @describe
 * @author ezt
 * @created 2015年3月30日
 */

public class LightAccompanyServiceIntroAdapter extends
		BaseArrayListAdapter<LightAccompanying> {

	public LightAccompanyServiceIntroAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView tvServiceName, tvIntro;

	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.item_card_service_intro, null);
			holder.tvServiceName = (TextView) convertView
					.findViewById(R.id.card_service_name);
			holder.tvIntro = (TextView) convertView
					.findViewById(R.id.card_service_intro);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LightAccompanying l = mList.get(position);
		holder.tvServiceName.setText(l.getItemName());
		holder.tvIntro.setText("\t"+l.getRemark());
		return convertView;
	}

}
