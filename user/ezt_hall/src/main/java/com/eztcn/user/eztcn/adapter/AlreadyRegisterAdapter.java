package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.OrderRegisterRecord;

/**
 * @title 登记列表adapter
 * @describe
 * @author ezt
 * @created 2015年4月14日
 */
public class AlreadyRegisterAdapter extends
		BaseArrayListAdapter<OrderRegisterRecord> {

	public AlreadyRegisterAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		OrderRegisterRecord record;
		if (convertView == null) {
			convertView = View
					.inflate(mContext, R.layout.alreadyregister, null);
		}
		TextView registerTime = (TextView) convertView
				.findViewById(R.id.registerTime);
		TextView reply = (TextView) convertView.findViewById(R.id.reply);
		TextView replyCount = (TextView) convertView
				.findViewById(R.id.replyCount);
		record = mList.get(position);
		String orderDate = record.getOrderDate();
		if (orderDate != null && !orderDate.equals("")) {
			registerTime.setText(orderDate);
		}
		replyCount.setText("(" + record.getReplyCount() + ")");
		// convertView.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// listener.regItemClick(v, position);
		// }
		// });
		return convertView;
	}

	// private AlreadyRegItemClickListener listener;
	//
	// public void setOnRegItemClickListener(AlreadyRegItemClickListener
	// listener) {
	// this.listener = listener;
	// }
	//
	// public interface AlreadyRegItemClickListener {
	//
	// public void regItemClick(View v, int position);
	// }
}
