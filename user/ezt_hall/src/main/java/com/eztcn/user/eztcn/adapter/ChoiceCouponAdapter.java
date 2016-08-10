package com.eztcn.user.eztcn.adapter;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.BuyHealthCardActivity;
import com.eztcn.user.eztcn.bean.Coupons;

/**
 * @title 选择优惠券
 * @describe
 * @author ezt
 * @created 2015年5月5日
 */
@SuppressLint("UseSparseArrays")
public class ChoiceCouponAdapter extends BaseArrayListAdapter<Coupons> {

	public Set<Integer> checkedRecId;

	public ChoiceCouponAdapter(Activity context) {
		super(context);
		checkedRecId = new HashSet<Integer>();// 保存所有选中的
	}

	class ViewHolder {
		TextView tvTime;
		TextView tvTitle;
		CheckBox cbChoice;
	}

	ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(mContext, R.layout.item_choice_coupon, null);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.item_msg_time);// 到期时间
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_msg_title);// 优惠券名称
			holder.cbChoice = (CheckBox) convertView
					.findViewById(R.id.item_msg_cb);// 选择框

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Coupons c = mList.get(position);
		holder.tvTime.setText("有效期至" + c.getEndDate());
		holder.tvTitle.setText(c.getTitle());
		holder.cbChoice.setText(c.getMoney() + "元");
		holder.cbChoice
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						double choicePrice = 0.0;
						if (isChecked) {
							choicePrice = -1 * (Double.parseDouble(c.getMoney()));

							checkedRecId.add(position);
						} else {
							choicePrice = 1
									* (Double.parseDouble(c.getMoney()));

							checkedRecId.remove(position);

						}
						((BuyHealthCardActivity) mContext)
								.updatePrice(choicePrice);

					}
				});

		return convertView;
	}

}
