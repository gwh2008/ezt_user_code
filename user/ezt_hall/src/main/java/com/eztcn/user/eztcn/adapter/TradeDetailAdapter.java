package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.ChargeCurrency;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 交易明细
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class TradeDetailAdapter extends BaseArrayListAdapter<ChargeCurrency> {

	private ViewHolder holder = null;

	public TradeDetailAdapter(Activity context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChargeCurrency detail;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.tradedetail_item, null);
			holder.tradeName = (TextView) convertView
					.findViewById(R.id.tradeName);
			holder.tradeMoney = (TextView) convertView
					.findViewById(R.id.tradeMoney);
			holder.person = (TextView) convertView.findViewById(R.id.person);
			holder.calledLayout = (LinearLayout) convertView
					.findViewById(R.id.calledLayout);
			holder.tradeTime = (TextView) convertView
					.findViewById(R.id.tradeTime);
			holder.calledTime = (TextView) convertView
					.findViewById(R.id.calledTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		detail = mList.get(position);
		holder.person.setText(BaseApplication.patient.getEpName());
		holder.tradeTime
				.setText(StringUtil.dealWithDate(detail.getCreateTime()));
		if (detail.getSourceType() == 434) {// 电话医生
			holder.tradeName.setText("电话医生");
			// holder.calledLayout.setVisibility(View.VISIBLE);
			// holder.calledTime.setText(detail.getCreateTime());
			holder.tradeMoney.setTextColor(Color.RED);
		} else {// 充值
			holder.tradeName.setText("充值");
			holder.tradeMoney.setTextColor(Color.parseColor("#0aff0a"));
		}
		String temp = detail.getIsIncomeExpenditure() == 0 ? "+" : "-";
		holder.tradeMoney.setText(temp + detail.getEztCurrency());
		return convertView;
	}

	class ViewHolder {
		private TextView tradeName;// 交易名称
		private TextView tradeMoney;// 交易金额
		private TextView person;// 交易人姓名
		private TextView tradeTime;// 交易日期
		private TextView calledTime;// 通话时长
		private LinearLayout calledLayout;
	}
}
