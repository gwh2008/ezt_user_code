package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Money;

/**
 * @title 充值金额选择adapter
 * @describe
 * @author ezt
 * @created 2015年2月3日
 */

public class MoneyChoiceAdapter extends BaseArrayListAdapter<Money> {

	public MoneyChoiceAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView tvMoney, tvMyMoney;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_choice_money,
					null);

			holder.tvMoney = (TextView) convertView
					.findViewById(R.id.item_money);

			holder.tvMyMoney = (TextView) convertView
					.findViewById(R.id.item_my_money);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Money money = mList.get(position);
		holder.tvMoney.setText(money.getStrMoney());
		holder.tvMyMoney.setText(money.getStrMyMoney());

		return convertView;
	}

}
