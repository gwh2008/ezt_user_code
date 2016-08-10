package com.eztcn.user.eztcn.activity.mine;

import java.util.HashMap;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eztcn.user.R;
import com.eztcn.user.alipay.Result;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.ChildOrderListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.api.IOrder;
import com.eztcn.user.eztcn.bean.Order;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.customView.SelectPayPopupWindow;
import com.eztcn.user.eztcn.impl.OrderImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 我的订单详情
 * @describe
 * @author ezt
 * @created 2015年4月9日
 */
public class MyOrderDetailActivity extends FinalActivity implements
		IHttpResult {//OnClickListener, 

	@ViewInject(R.id.tv_order_no)
	private TextView tvNo;// 订单编号

	@ViewInject(R.id.tv_order_state)
	private TextView tvState;// 订单状态

	@ViewInject(R.id.tv_order_num)
	private TextView tvNum;// 订单数量

	@ViewInject(R.id.tv_order_tra_fee)
	private TextView tvTraFee;// 运费

	@ViewInject(R.id.tv_order_price)
	private TextView tvPrice;// 实付

	@ViewInject(R.id.tv_order_create_time)
	private TextView tvCreateTime;// 下单时间

	@ViewInject(R.id.tv_order_pay_time)
	private TextView tvPayTime;// 付款时间

	@ViewInject(R.id.tv_sum_price)
	private TextView tvSumPrice;// 总额

	@ViewInject(R.id.tv_revoke)//, click = "onClick"
	private TextView tvRevoke;// 撤销订单

	@ViewInject(R.id.tv_pay)//, click = "onClick"
	private TextView tvPay;// 立即支付

	@ViewInject(R.id.item_lv)
	private MyListView lv;

	private Order order;
	private String strState;
	private View view;
	private String orderId;

	private SelectPayPopupWindow payPopwindow;
	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 1;
	private int payType = -1;// 支付方式(330为支付宝，2为银联，3为信用卡)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LinearLayout.inflate(this, R.layout.activity_my_order_detail,
				null);
		
		setContentView(view);
		ViewUtils.inject(MyOrderDetailActivity.this);
		loadTitleBar(true, "订单详情", null);
		order = (Order) getIntent().getSerializableExtra("order");
		strState = order.getOrderState();
		orderId = order.getId();
		if ("未付款".equals(strState)) {
			tvRevoke.setVisibility(View.VISIBLE);
			tvPay.setVisibility(View.VISIBLE);
			tvRevoke.setText("撤销订单");
			tvPay.setText("立即支付");

		} else if ("已付款".equals(strState)) {
			tvRevoke.setVisibility(View.INVISIBLE);
			tvSumPrice.setVisibility(View.INVISIBLE);
			tvPay.setVisibility(View.VISIBLE);
			tvPay.setText("删除订单");
		}
		tvNo.setText("订单编号：" + order.getOrderNo());
		tvState.setText("订单状态：" + order.getOrderState());
		tvNum.setText("商品数量：" + order.getOrderAmount());
		tvTraFee.setText("运费：0");
		tvPrice.setText("实付：" + order.getTotalPrice() + "元");

		tvCreateTime.setText("下单时间：" + order.getCreateTime());
		tvPayTime.setText("付款时间：" + order.getPayTime());
		tvSumPrice.setText("总额：" + order.getTotalPrice() + "元");

		ChildOrderListAdapter adapter = new ChildOrderListAdapter(mContext);
		lv.setAdapter(adapter);
		adapter.setList(order.getChildOrderList());
	}
	
	@OnClick(R.id.tv_revoke)// 撤销订单
	private void tv_revokeClick(View v){
		delHint("确定撤销该订单？", orderId,true);
	}
	
	
	@OnClick(R.id.tv_pay)// 立即支付/删除订单
	private void tv_payClick(View v){
		if ("未付款".equals(strState)) {// 立即支付

			if (BaseApplication.getInstance().patient != null) {
				dialogChoicePay(view, orderId);
			} else {
				HintToLogin(Constant.LOGIN_COMPLETE);
			}
		} else if ("已付款".equals(strState)) {// 删除订单
			delHint("确定删除该订单？", orderId,false);
		}
	}
	/**
	 * 选择支付方式
	 */
	private void dialogChoicePay(View v, String orderId) {
		payPopwindow = new SelectPayPopupWindow(mContext, new ChoicePayClick(
				orderId));
		// 显示窗口
		payPopwindow.showAtLocation(v, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	public class ChoicePayClick implements OnClickListener {

		String orderId;// 订单id

		public ChoicePayClick(String orderId) {
			super();
			this.orderId = orderId;
		}

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.alipay_layout:// 支付宝
				payType = 330;
				break;
			}
			payOrder(this.orderId, payType);
			payPopwindow.dismiss();
		}

	}

	/**
	 * 删除订单
	 * 
	 * @param orderId
	 * @param isRepeal 是否撤销订单
	 */
	private void delOrder(String orderId,boolean isRepeal) {
		RequestParams params=new RequestParams();
		OrderImpl api = new OrderImpl();
		params.addBodyParameter("userId", BaseApplication.getInstance().patient.getUserId()+ "");
		params.addBodyParameter("orderIds", orderId+ "");
		params.addBodyParameter("revoke", isRepeal?"Y":"N");
		api.delOrder(params, this);
		showProgressToast();
	}

	/**
	 * 立即支付
	 * 
	 * @param orderId
	 * @param payWayId
	 *            支付方式id
	 */
	private void payOrder(String orderId, int payWayId) {
		RequestParams params=new RequestParams();
		OrderImpl api = new OrderImpl();
		params.addBodyParameter("userId", BaseApplication.getInstance().patient.getUserId()+ "");
		params.addBodyParameter("orderId", orderId+ "");
		params.addBodyParameter("payWayId", payWayId+ "");
		api.orderPay(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {

		case HttpParams.DEL_ORDER:// 删除订单
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {
						setResult(11);
						finish();
					} else {
						if (map.get("msg") != null) {
							Toast.makeText(mContext, map.get("msg").toString(),
									Toast.LENGTH_SHORT).show();
							hideProgressToast();
						}

					}

				}

			} else {
				hideProgressToast();
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();
			}

			break;

		case HttpParams.ORDER_PAY:// 立即支付
			hideProgressToast();

			if (isSuc) {// 成功
				Map<String, Object> map = (Map<String, Object>) object[2];
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					final String dataPay = (String) map.get("data");
					switch (payType) {
					case 330:// 支付宝
						Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								// 构造PayTask 对象
								PayTask alipay = new PayTask(mContext);
								// 调用支付接口
								String result = alipay.pay(dataPay,true);
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
					if (map.get("msg") != null) {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();
			}

			break;
		}

	}

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
					finish();
					showProgressToast();
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

	/**
	 * 删除订单弹出框
	 * 
	 * @param hint
	 * @param orderId
	 * @param isRepeal //是否撤销订单
	 */
	private void delHint(String hint, final String orderId,final boolean isRepeal) {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(hint).setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						delOrder(orderId,isRepeal);
					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		dialog.show();

	}

}
