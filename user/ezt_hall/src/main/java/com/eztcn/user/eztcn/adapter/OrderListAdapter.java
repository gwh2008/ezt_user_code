package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.ChildOrder;
import com.eztcn.user.eztcn.bean.Order;
import com.eztcn.user.eztcn.customView.MyListView;

/**
 * @title 订单列表adapter
 * @describe
 * @author ezt
 * @created 2015年3月31日
 */
public class OrderListAdapter extends BaseArrayListAdapter<Order> implements
		OnClickListener {

	private ViewHolder holder;
	private ChildOrderListAdapter adapter;

	public OrderListAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order, null);

			holder.tvOrderNo = (TextView) convertView
					.findViewById(R.id.item_order_no);
			holder.tvOrderState = (TextView) convertView
					.findViewById(R.id.item_order_state);
			holder.tvCreateTime = (TextView) convertView
					.findViewById(R.id.item_create_time);
			holder.tvOrderAmount = (TextView) convertView
					.findViewById(R.id.item_amount);
			holder.tvTotalPrice = (TextView) convertView
					.findViewById(R.id.item_total_price);
			holder.bt1 = (TextView) convertView.findViewById(R.id.item_bt1);
			holder.bt2 = (TextView) convertView.findViewById(R.id.item_bt2);
			holder.lv = (MyListView) convertView.findViewById(R.id.item_lv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lv.setClickable(false);
		holder.lv.setFocusable(false);
		
		Order order = mList.get(position);
		String strState = order.getOrderState();
		holder.tvOrderNo.setText("订单号：" + order.getOrderNo());
		holder.tvOrderState.setText(strState);
		String time="";
		if (!TextUtils.isEmpty(time=order.getCreateTime())) {
			holder.tvCreateTime.setText(time);
		}
		holder.tvOrderAmount.setText("共" + order.getOrderAmount() + "件");
		holder.tvTotalPrice.setText("实付：" + order.getTotalPrice() + "元");
		adapter = new ChildOrderListAdapter(mContext);
		holder.lv.setAdapter(adapter);
		holder.lv.setEnabled(false);
		ArrayList<ChildOrder> childList = order.getChildOrderList();
		if (childList != null && childList.size() != 0) {
			adapter.setList(childList);
		}

		if ("未付款".equals(strState)) {
			holder.bt1.setVisibility(View.VISIBLE);
			holder.bt2.setVisibility(View.VISIBLE);
			holder.bt1.setText("撤销订单");
			holder.bt2.setText("立即支付");

		} else if ("已付款".equals(strState)) {
			holder.bt1.setVisibility(View.GONE);
			holder.bt2.setVisibility(View.VISIBLE);
			holder.bt2.setText("删除订单");
		}
		holder.bt1.setOnClickListener(this);
		holder.bt2.setOnClickListener(this);

		holder.bt1.setTag(position);
		holder.bt2.setTag(position);

		return convertView;
	}

	class ViewHolder {

		TextView tvOrderNo;
		TextView tvOrderState;
		TextView tvCreateTime;
		TextView tvOrderAmount;
		TextView tvTotalPrice;
		TextView bt1, bt2;
		MyListView lv;

	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.item_bt1:// 撤销订单
			this.iclick.click(pos, 1);
			break;

		case R.id.item_bt2:// 删除订单/立即支付
			this.iclick.click(pos, 2);
			break;

		}

	}

	IClick iclick;

	public interface IClick {

		public void click(int pos, int type);// type 1为按钮1 ，2为按钮2

	};

	public void adapterClick(IClick iclick) {
		this.iclick = iclick;
	};
}
