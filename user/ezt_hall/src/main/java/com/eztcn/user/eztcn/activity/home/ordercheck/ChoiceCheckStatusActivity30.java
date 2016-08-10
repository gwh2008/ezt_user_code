package com.eztcn.user.eztcn.activity.home.ordercheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eztcn.user.R;
import com.eztcn.user.alipay.Result;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.MyServiceActivity30;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrder;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrderItem;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.OrderCheckImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @author LX 约检查的订单的状态 2016-3-15上午11:42:31
 */
@SuppressLint("ResourceAsColor")
public class ChoiceCheckStatusActivity30 extends FinalActivity implements
		IHttpResult {

	private ImageView request_order, order_pay, order_check, order_succeed;
	private TextView request_order_tx, order_pay_tx, order_check_tx,
			order_succeed_tx;
	private ImageView check_status_image;
	private TextView check_status_tx;
	private TextView patient_name_tx, patient_contact_telphone_tx,
			patient_order_require_tx;
	private Context context = ChoiceCheckStatusActivity30.this;
	private List<CheckOrderItem> OrderCheck_details_list;
	private CheckOrder order_check_bean;
	private LinearLayout order_attention_layout;
	private Button see_details_bt;
	private Button back_home_bt;
	private String order_id = "";
	private int status = -1;
	private int payType = 330;// 支付方式(330为支付宝，2为银联，3为信用卡)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_check_status30);
		ViewUtils.inject(ChoiceCheckStatusActivity30.this);
		loadTitleBar(true, "订单详情", null);
		initView();
		Intent data_intent = getIntent();
		if (data_intent != null) {
			order_id = data_intent.getStringExtra("order_id");
		}

		if (BaseApplication.getInstance().isNetConnected) {
			showProgressToast();
			initialData();
		} else {
			// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void initView() {

		request_order = (ImageView) this.findViewById(R.id.request_order);
		order_pay = (ImageView) this.findViewById(R.id.order_pay);
		order_check = (ImageView) this.findViewById(R.id.order_check);
		order_succeed = (ImageView) this.findViewById(R.id.order_succeed);

		request_order_tx = (TextView) this.findViewById(R.id.request_order_tx);
		order_pay_tx = (TextView) this.findViewById(R.id.order_pay_tx);
		order_check_tx = (TextView) this.findViewById(R.id.order_check_tx);
		order_succeed_tx = (TextView) this.findViewById(R.id.order_succeed_tx);

		patient_name_tx = (TextView) this.findViewById(R.id.patient_name_tx);
		patient_contact_telphone_tx = (TextView) this
				.findViewById(R.id.patient_contact_telphone_tx);
		patient_order_require_tx = (TextView) this
				.findViewById(R.id.patient_order_require_tx);
		patient_order_require_tx.setMovementMethod(ScrollingMovementMethod.getInstance());
		check_status_image = (ImageView) this
				.findViewById(R.id.check_status_image);
		check_status_tx = (TextView) this.findViewById(R.id.check_status_tx);
		order_attention_layout = (LinearLayout) this
				.findViewById(R.id.order_attention_layout);
		see_details_bt = (Button) this.findViewById(R.id.see_details_bt);
		see_details_bt.setOnClickListener(see_details_btClickListener);
		back_home_bt = (Button) this.findViewById(R.id.back_home_bt);
		back_home_bt.setOnClickListener(back_home_btClickListener);

	}

	// 获取数据。
	private void initialData() {
		RequestParams params = new RequestParams();
		OrderCheckImpl impl = new OrderCheckImpl();
		if (order_id != null && order_id.length() != 0) {
			params.addBodyParameter("orderId", order_id);
		}
		impl.gainCheckOrderDetail(params, this);
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		Map<String, Object> map = (Map<String, Object>) object[2];
		hideProgressToast();
		switch (type) {
		case HttpParams.GAIN_ORDER_CHECK_DETIAL:
			if (isSucc) {
				if ((Boolean) map.get("flag")) {

					order_check_bean = (CheckOrder) map.get("checkOrder");
					OrderCheck_details_list = (List<CheckOrderItem>) map
							.get("checkOrderItemList");
					setData(order_check_bean);
				} else {
					String msgStr = "";
					if (map.containsKey("msg")) {
						msgStr = String.valueOf(map.get("msg"));
					}
					Toast.makeText(this, msgStr, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(mContext, "服务器繁忙",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case HttpParams.COMMIT_ORDER_CHECK_BYID:
			if (isSucc) {

				final String data = (String) map.get("data");

				String orderNoStr = String.valueOf(map.get("orderNo"));
				switch (payType) {
				case 330:// 支付宝
					Runnable payRunnable = new Runnable() {

						@Override
						public void run() {
							// 构造PayTask 对象
							PayTask alipay = new PayTask(mContext);
							// 调用支付接口
							String result = alipay.pay(data, true);
							Message msg = new Message();
							msg.what = ALIPAY_PAY_FLAG;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					};

					Thread payThread = new Thread(payRunnable);
					payThread.start();
					break;

				}
			} else {
				Toast.makeText(mContext, object[3].toString(),
						Toast.LENGTH_SHORT);
			}
			break;
		default:
			Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
			break;
		}

	}

	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 2;
	/**
	 * 处理支付结果
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ALIPAY_PAY_FLAG: {// 支付宝
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();

					Intent check_order_intent = new Intent();
					check_order_intent.setClass(mContext,
							MyServiceActivity30.class);
					startActivity(check_order_intent);
					finish();

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}

				}

				break;
			}
			default:
				break;
			}
		};
	};

	// see details
	OnClickListener see_details_btClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (status == EZTConfig.CHECKITEM_STATUS_UNPAY) {

				OrderCheckImpl orderCheckImpl = new OrderCheckImpl();
				RequestParams params = new RequestParams();
				params.addBodyParameter("orderId", order_id);
				orderCheckImpl.commitCheckOrderById(params,
						ChoiceCheckStatusActivity30.this);

			} else

			if (null != OrderCheck_details_list
					&& OrderCheck_details_list.size() > 0) {
				Intent intent_details = new Intent();
				String guideNameStr = OrderCheck_details_list.get(0)
						.getGuider().getGuysName();
				String guideMobileStr = OrderCheck_details_list.get(0)
						.getGuider().getGuysMobile();
				if (null != guideNameStr && !"null".equals(guideNameStr)) {
					intent_details.putExtra("secretary", guideNameStr);
					intent_details.putExtra("secMobile", guideMobileStr);
				}
                intent_details.putExtra("checkOrderList",(Serializable)OrderCheck_details_list);
				intent_details
						.setClass(context, CheckOrderDetailActivity.class);
				startActivity(intent_details);
			}
		}

	};
	OnClickListener back_home_btClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BackHome(ChoiceCheckStatusActivity30.this, 0);
		}
	};

	// 设置数据刷新界面。
	private void setData(CheckOrder order_check_bean) {

		if (order_check_bean != null) {

			status = order_check_bean.getOrderState();
			setOrderStatus(status);
			setInfNotice(order_check_bean);
		}
	}

	// 设置info信息和注意。
	private void setInfNotice(CheckOrder order_check_bean) {

		patient_name_tx.setText(order_check_bean.getPatientName());
		patient_contact_telphone_tx
				.setText(order_check_bean.getPatientMobile());
		patient_order_require_tx.setText(order_check_bean.getOrderStr());

		View child_view;
		boolean showZw = false;
		List<String> temp = new ArrayList<String>();
		for (int i = 0; i < OrderCheck_details_list.size(); i++) {
			child_view = LayoutInflater.from(context).inflate(
					R.layout.item_order_check_notice30, null);
			TextView notice_tx = (TextView) child_view
					.findViewById(R.id.notice_tx);
			List<String> cars = OrderCheck_details_list.get(i).getCaresStr();
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < cars.size(); j++) {
				String temCare = cars.get(j);
				if (!temp.contains(temCare)) {
					sb.append("*");
					temp.add(temCare);
					sb.append(temCare);
				}
				if (j < cars.size() - 1) {
					sb.append("/n");
				}
			}
		
			notice_tx.setText(sb.toString());
			if (StringUtils.isNotBlank(sb.toString())) {
				order_attention_layout.addView(child_view);
				showZw = true;
			}
		}
		
		if (!showZw) {
			child_view = LayoutInflater.from(context).inflate(
					R.layout.item_order_check_notice30, null);
			TextView notice_tx = (TextView) child_view
					.findViewById(R.id.notice_tx);
			notice_tx.setText("暂无");
			order_attention_layout.addView(child_view);
		}
	}

	// 设置订单的状态。
	private void setOrderStatus(int status_type) {

		switch (status_type) {
		case EZTConfig.CHECKITEM_STATUS_UNPAY:
			request_order
					.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_pay_notpass_icon);
			order_check.setImageResource(R.drawable.order_check_notpass_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);

			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			check_status_image
					.setImageResource(R.drawable.order_details_money_icon);
			check_status_tx.setText("待支付");
			check_status_tx.setTextColor(getResources().getColor(R.color.notice_tx_color));
			see_details_bt.setText("立即支付");
			break;
		case EZTConfig.CHECKITEM_STATUS_UNAUDIT:
			request_order
					.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_pay_passed_icon);
			order_check.setImageResource(R.drawable.order_check_notpass_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			check_status_image
					.setImageResource(R.drawable.order_check_succeedicon);
			check_status_tx.setTextColor(getResources().getColor(R.color.order_normal));
			check_status_tx.setText("待审核");

			break;
		case EZTConfig.CHECKITEM_STATUS_AUDITSUCCESS:
			request_order
					.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_pay_passed_icon);
			order_check.setImageResource(R.drawable.order_check_passed_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);

			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			check_status_image
					.setImageResource(R.drawable.order_not_succeed_icon);
			check_status_tx.setText("待预约");
			check_status_tx.setTextColor(getResources().getColor(R.color.order_normal));

			break;
		case EZTConfig.CHECKITEM_STATUS_ORDERSUCCESS:
			request_order
					.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_pay_passed_icon);
			order_check.setImageResource(R.drawable.order_check_passed_icon);
			order_succeed.setImageResource(R.drawable.order_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_normal));
			check_status_image
					.setImageResource(R.drawable.order_check_succeedicon);
			check_status_tx.setText("预约成功");
			check_status_tx.setTextColor(getResources().getColor(R.color.order_normal));

			break;
		case EZTConfig.CHECKITEM_STATUS_REFUSEAUDIT:
			request_order
					.setImageResource(R.drawable.request_order_notpass_icon);
			order_pay.setImageResource(R.drawable.order_pay_notpass_icon);
			order_check.setImageResource(R.drawable.order_check_notpass_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			check_status_image
					.setImageResource(R.drawable.order_check_fail_icon);
			check_status_tx.setText("订单被拒绝");
			check_status_tx.setTextColor(getResources().getColor(R.color.notice_tx_color));
			

			break;
		case EZTConfig.CHECKITEM_STATUS_PAYBACK:
			request_order
					.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_pay_passed_icon);
			order_check.setImageResource(R.drawable.order_check_passed_icon);
			order_succeed.setImageResource(R.drawable.order_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_normal));
			check_status_image
					.setImageResource(R.drawable.order_check_succeedicon);
			check_status_tx.setText("办理退款");
			check_status_tx.setTextColor(getResources().getColor(R.color.notice_tx_color));

			break;
		case EZTConfig.CHECKITEM_STATUS_PAYOUTTIME:
			request_order
					.setImageResource(R.drawable.request_order_passed_icon);
			order_pay.setImageResource(R.drawable.order_pay_notpass_icon);
			order_check.setImageResource(R.drawable.order_check_notpass_icon);
			order_succeed.setImageResource(R.drawable.order_not_succeed_icon);
			request_order_tx.setTextColor(getResources().getColor(R.color.order_normal));
			order_pay_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_check_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			order_succeed_tx.setTextColor(getResources().getColor(R.color.order_unnormal));
			check_status_image
					.setImageResource(R.drawable.order_details_money_icon);
			check_status_tx.setText("支付超时");
			check_status_tx.setTextColor(getResources().getColor(R.color.notice_tx_color));
			see_details_bt.setVisibility(View.GONE);
			break;
		}
	}
}
