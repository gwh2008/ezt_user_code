package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @title 全站搜索医院adapter
 * @describe
 * @author ezt
 * @created 2014年12月27日
 */
public class AllSearchHosAdapter extends BaseArrayListAdapter<Hospital> {

	public AllSearchHosAdapter(Activity context) {
		super(context);
	}

	static class ViewHolder {
		TextView tvHosName, tvTel, tvAds, tvIntro;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_search_all_hos, null);

			holder.tvHosName = (TextView) convertView
					.findViewById(R.id.item_search_hos_name);// 医院名称
			holder.tvTel = (TextView) convertView
					.findViewById(R.id.item_search_hos_tel);// 医院电话

			holder.tvAds = (TextView) convertView
					.findViewById(R.id.item_search_hos_ads);// 医院地址
			holder.tvIntro = (TextView) convertView
					.findViewById(R.id.item_search_hos_intro);// 简介

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Hospital hos = mList.get(position);
		String strTel = "电话：" + hos.gethTel();
		String strAds = "地址：" + hos.gethAddress();
		holder.tvHosName.setText(TextUtils.isEmpty(hos.gethName()) ? "医院名称"
				: hos.gethName());
		holder.tvTel.setText(TextUtils.isEmpty(strTel) ? "医院电话:暂无" : strTel);
		holder.tvAds.setText(TextUtils.isEmpty(strAds) ? "医院地址:暂无" : strAds);
		holder.tvIntro.setText(TextUtils.isEmpty(hos.gethIntro()) ? "医院简介:暂无"
				: hos.gethIntro());

		return convertView;
	}

}
