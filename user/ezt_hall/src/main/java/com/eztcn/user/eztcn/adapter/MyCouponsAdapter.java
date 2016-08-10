package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Coupons;

/**
 * @title 优惠券adapter
 * @describe
 * @author ezt
 * @created 2015年5月5日
 */

public class MyCouponsAdapter extends BaseArrayListAdapter<Coupons> {

	public MyCouponsAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView tvMoney, tvDate, tvTitle;
		ImageView imgState;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_coupon, null);

			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			holder.imgState = (ImageView) convertView
					.findViewById(R.id.img_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Coupons coupon = mList.get(position);
		holder.tvMoney.setText(coupon.getMoney());
		holder.tvDate.setText("有效日期:" + coupon.getEndDate());
		holder.tvTitle.setText(coupon.getTitle());

		switch (coupon.getState()) {// 0正常，1使用中，-1已使用，2过期
		case 0:// 未使用
			holder.imgState.setVisibility(View.GONE);
			break;

		case -1:// 已使用
			holder.imgState.setVisibility(View.VISIBLE);
			holder.imgState.setBackgroundResource(R.drawable.ic_coupon_pastuse);
			break;

		case 1:// 使用中
			holder.imgState.setVisibility(View.VISIBLE);
			holder.imgState.setBackgroundResource(R.drawable.ic_coupon_useing);
			break;

		case 2:// 过期
			holder.imgState.setVisibility(View.VISIBLE);
			holder.imgState
					.setBackgroundResource(R.drawable.ic_coupon_pastdate);
			break;
		}

		return convertView;
	}

}
