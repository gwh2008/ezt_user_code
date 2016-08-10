package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.OrderBed;
import com.eztcn.user.eztcn.customView.RoundImageView;

/**
 * 我的预约病床的列表的adapter
 * @author LX
 * @date2016-4-5 @time下午2:30:11
 */
public class OrderBedAdapter extends BaseArrayListAdapter<OrderBed> {

	public OrderBedAdapter(Activity context) {
		super(context);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder view_holder;
		if (convertView == null) {
			view_holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_my_order_bed, null);
			view_holder.user_head = (RoundImageView) convertView
					.findViewById(R.id.user_head);
			view_holder.name = (TextView) convertView
					.findViewById(R.id.name);
			view_holder.telphone = (TextView) convertView
					.findViewById(R.id.telphone);
			view_holder.order_list_tx = (TextView) convertView
					.findViewById(R.id.order_list_tx);
			view_holder.order_bed_status = (TextView) convertView
					.findViewById(R.id.order_bed_status);
			convertView.setTag(view_holder);

		} else {
			view_holder = (ViewHolder) convertView.getTag();
		}
		OrderBed order_bed = (OrderBed) getItem(position);

		view_holder.user_head.setImageResource(R.drawable.userdefault);
		view_holder.name.setText(order_bed.getPatientName());
		view_holder.telphone.setText(order_bed.getPatientPhone());
		view_holder.order_list_tx.setText(order_bed.getHospitalName());
		setOrderStatus(view_holder.order_bed_status,
				order_bed.getAuditingStatus());
		return convertView;
	}

	/** 预约病床订单状态- 待支付 */
	public final static Integer BED_STATUS_UNPAY = 1;
	/** 预约病床订单状态- 待审核 */
	public final static Integer BED_STATUS_UNAUDIT = 2;
	/** 预约病床订单状态- 审核通过&待预约&正在咨询 */
	public final static Integer BED_STATUS_AUDITSUCCESS = 3;
	/** 预约病床订单状态- 预约成功 */
	public final static Integer BED_STATUS_ORDERSUCCESS = 4;
	/** 预约病床订单状态- 拒绝订单 */
	public final static Integer BED_STATUS_REFUSEAUDIT = 5;
	/** 预约病床订单状态- 办理退款 */
	public final static Integer BED_STATUS_PAYBACK = 6;
	/** 预约病床订单状态- 支付超时 */
	public final static Integer BED_STATUS_PAYOUTTIME = 7;
	/** 预约病床订单状态- 收取押金 */
	public final static Integer BED_STATUS_DEPOSIT = 8;
	/** 预约病床订单状态- 支付成功 */
	public final static Integer BED_STATUS_SUCCESS = 9;
	/** 预约病床订单状态- 暂无病床 */
	public final static Integer BED_STATUS_NOTBED = 10;
	/** 预约病床订单状态- 关闭订单 */
	public final static Integer BED_STATUS_ORDERCLOSE = 11;

	// 设置订单的状态的显示。
	@SuppressWarnings("deprecation")
	private void setOrderStatus(TextView status_tx, int status) {

		Resources resources = mContext.getResources();
		Drawable drawable1 = resources
				.getDrawable(R.drawable.order_bed_status_bg_one);// red
		Drawable drawable2 = resources
				.getDrawable(R.drawable.order_bed_status_bg_two); // orange
		Drawable drawable3 = resources
				.getDrawable(R.drawable.order_bed_status_bg_three); // green

		if (status_tx != null) {
			switch (status) {
			case 1:
				status_tx.setText("待支付");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			case 2:
				status_tx.setText("待审核");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			case 3:
				status_tx.setText("正在咨询");
				status_tx.setBackgroundDrawable(drawable3);
				break;
			case 4:
				status_tx.setText("预约成功");
				status_tx.setBackgroundDrawable(drawable3);

				break;
			case 5:
				status_tx.setText("拒绝订单");
				status_tx.setBackgroundDrawable(drawable1);

				break;
			case 7:
				status_tx.setText("支付超时");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			case 8:
				status_tx.setText("收取押金");
				status_tx.setBackgroundDrawable(drawable2);

				break;
			case 9:
				status_tx.setText("支付成功");
				status_tx.setBackgroundDrawable(drawable3);
				break;
			case 10:
				status_tx.setText("暂无病床");
				status_tx.setBackgroundDrawable(drawable1);
				break;
			}

		}
	}

	class ViewHolder {
		RoundImageView user_head;
		TextView name;
		TextView telphone;
		TextView order_list_tx;
		TextView order_bed_status;
	}

}
