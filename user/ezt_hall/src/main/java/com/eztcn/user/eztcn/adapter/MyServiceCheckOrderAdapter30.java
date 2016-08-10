package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrder;
import com.eztcn.user.eztcn.customView.RoundImageView;

public class MyServiceCheckOrderAdapter30 extends
		BaseArrayListAdapter<CheckOrder> {

	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;
	private Context context;

	public MyServiceCheckOrderAdapter30(Activity context) {
		super(context);
		this.context = context;
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	static class ViewHolder {
		TextView user_name, user_telphone, choice_check_describe,
				choice_check_order_status_tx;
		RoundImageView user_head;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_choice_check30, null);

			holder.user_head = (RoundImageView) convertView
					.findViewById(R.id.user_head);// 头像

			holder.user_name = (TextView) convertView
					.findViewById(R.id.user_name);// 名称

			holder.user_telphone = (TextView) convertView
					.findViewById(R.id.user_telphone);// 电话
			holder.choice_check_describe = (TextView) convertView
					.findViewById(R.id.choice_check_describe);// 描述
			holder.choice_check_order_status_tx = (TextView) convertView
					.findViewById(R.id.choice_check_order_status_tx);// 订单的状态
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CheckOrder order=(CheckOrder) getItem(position);
		
		holder.user_name.setText(order.getPatientName());
		holder.user_telphone .setText(order.getPatientMobile());
		switch (order.getOrderState()) {
		case 1:
			holder.choice_check_order_status_tx.setText("待支付");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.wait_pay));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_one));
			break;
		case 2:
			holder.choice_check_order_status_tx.setText("待审核");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.wait_check));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_two));
			
			break;
		case 3:
			holder.choice_check_order_status_tx.setText("待预约");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.wait_order));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_three));
			break;
		case 4:
			holder.choice_check_order_status_tx.setText("预约成功");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.order_succeed));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_four));
			break;
		case 5:
			holder.choice_check_order_status_tx.setText("审核拒绝");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.check_refuse));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_five));
			
			break;
		case 6:
			holder.choice_check_order_status_tx.setText("办理退款");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.order_unnormal));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_six));
			
			break;
		case 7:
			holder.choice_check_order_status_tx.setText("支付超时");
			holder.choice_check_order_status_tx.setBackgroundColor(context.getResources().getColor(R.color.pay_time_out));
			holder.choice_check_describe.setText(context.getResources().getString(R.string.order_check_status_senven));
			break;
		}
		return convertView;
	}
}
