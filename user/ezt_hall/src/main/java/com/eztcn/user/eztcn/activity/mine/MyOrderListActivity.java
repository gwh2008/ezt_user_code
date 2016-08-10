package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eztcn.user.R;
import com.eztcn.user.alipay.Result;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.DoctorListActivity;
import com.eztcn.user.eztcn.adapter.OrderListAdapter;
import com.eztcn.user.eztcn.adapter.OrderListAdapter.IClick;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Order;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.customView.SelectPayPopupWindow;
import com.eztcn.user.eztcn.impl.OrderImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 我的订单
 * @describe
 * @author ezt
 * @created 2015年3月31日
 */
public class MyOrderListActivity extends FinalActivity implements
		IHttpResult, OnLoadMoreListener,//OnItemClickListener, 
		OnRefreshListener, IClick {

	@ViewInject(R.id.order_list_lv)//, itemClick = "onItemClick"
	private PullToRefreshListView lvOrder;
	private OrderListAdapter adapter;

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private ArrayList<Order> orderList;
	private boolean isDel = false;// 标记是否为删除 刷新页面

	private SelectPayPopupWindow payPopwindow;
	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 1;
	private int payType = -1;// 支付方式(330为支付宝，2为银联，3为信用卡)
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LinearLayout.inflate(this, R.layout.activity_my_orderlist, null);
		setContentView(view);
//		setContentView(R.layout.activity_my_orderlist);
		ViewUtils.inject(MyOrderListActivity.this);
		loadTitleBar(true, "我的订单", null);
		adapter = new OrderListAdapter(this);
		lvOrder.setAdapter(adapter);
		adapter.adapterClick(this);
		lvOrder.setCanLoadMore(true);
		lvOrder.setCanRefresh(true);
		lvOrder.setAutoLoadMore(true);
		lvOrder.setMoveToFirstItemAfterRefresh(false);
		lvOrder.setDoRefreshOnUIChanged(false);
		lvOrder.setOnLoadListener(this);
		lvOrder.setOnRefreshListener(this);
		initialData();
		showProgressToast();
	}

	/**
	 * 初始化订单数据
	 */
	private void initialData() {
		RequestParams params=new RequestParams();
		OrderImpl api = new OrderImpl();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()+"");
		params.addBodyParameter("eoStatus", "");
		params.addBodyParameter("rowsPerPage", pageSize+"");
		params.addBodyParameter("page", currentPage+"");
		api.getOrderList(params, this);
	}

	@OnItemClick(R.id.order_list_lv)
	private void order_list_lvItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - 1;
		Order order = adapter.getList().get(position);
		startActivityForResult(
				new Intent(mContext, MyOrderDetailActivity.class).putExtra(
						"order", order), 1);
	}
	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		position = position - 1;
//		Order order = adapter.getList().get(position);
//		startActivityForResult(
//				new Intent(mContext, MyOrderDetailActivity.class).putExtra(
//						"order", order), 1);
//
//	}

	@Override
	protected void onActivityResult(int arg0, int resultCode, Intent arg2) {
		super.onActivityResult(arg0, resultCode, arg2);
		currentPage = 1;
		if (resultCode == 11) {// 删除订单
			isDel = true;
		}
		initialData();

	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		lvOrder.setAutoLoadMore(true);
		initialData();
	}

	@Override
	public void onLoadMore() {

		if (orderList != null) {
			if (orderList.size() < pageSize) {
				lvOrder.setAutoLoadMore(false);
				lvOrder.onLoadMoreComplete();
			} else {
				currentPage++;
				initialData();
			}
		}
	}

	@Override
	public void result(Object... object) {

		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_ORDER_LIST:// 获取订单列表
			hideProgressToast();
			ArrayList<Order> data = null;
			if (isSuc) {// 成功
				orderList = (ArrayList<Order>) object[2];
				if (orderList != null && orderList.size() > 0) {
					lvOrder.setVisibility(View.VISIBLE);
					if (currentPage == 1) {// 第一次加载或刷新
						data = orderList;
						if (orderList.size() < pageSize) {
							lvOrder.setAutoLoadMore(false);
							lvOrder.onLoadMoreComplete();
						}
						lvOrder.onRefreshComplete();

					} else {// 加载更多
						data = (ArrayList<Order>) adapter.getList();
						if (data == null || data.size() <= 0)
							data = orderList;
						else
							data.addAll(orderList);

						if (orderList.size() < pageSize) {
							lvOrder.setAutoLoadMore(false);
						}
						lvOrder.onLoadMoreComplete();

					}
					adapter.setList(data);

				} else {
					if (adapter.getList() != null) {// 加载
						lvOrder.setAutoLoadMore(false);
						lvOrder.onLoadMoreComplete();
						data = (ArrayList<Order>) adapter.getList();

						if (isDel) {
							adapter.setList(new ArrayList<Order>());
							isDel = false;
							lvOrder.setVisibility(View.GONE);
							Toast.makeText(mContext, "暂无订单", Toast.LENGTH_SHORT)
									.show();
						} else {
							adapter.setList(data);
						}

					} else {// 刷新
						lvOrder.onRefreshComplete();
						Toast.makeText(mContext, "暂无订单", Toast.LENGTH_SHORT)
								.show();
					}

				}
				if (data != null) {
					orderList = data;
				}

			} else {
				Logger.i("获取订单列表", object[3]);
			}
			break;

		case HttpParams.DEL_ORDER:// 删除订单
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {
						currentPage = 1;
						isDel = true;
						initialData();
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
					currentPage = 1;
					initialData();
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

	@Override
	public void click(int pos, int type) {
		Order order = adapter.getList().get(pos);
		String orderId = order.getId();

		switch (type) {
		case 1:// 按钮1(撤销订单)
			delHint("确定撤销该订单？", orderId, true);
			break;

		case 2:// 按钮2（立即支付/删除订单）
			if ("未付款".equals(order.getOrderState())) {// 立即支付
				if (BaseApplication.patient != null) {
					dialogChoicePay(view, orderId);
				} else {
					HintToLogin(Constant.LOGIN_COMPLETE);
				}

			} else {// 删除订单
				delHint("确定删除该订单？", orderId, false);
			}
			break;
		}
	}
	/**
	 * 删除订单
	 * 
	 * @param orderId
	 * @param isRepeal
	 *            是否撤销订单
	 */
	private void delOrder(String orderId, boolean isRepeal) {
		RequestParams params=new RequestParams();
		OrderImpl api = new OrderImpl();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()+ "");
		params.addBodyParameter("orderIds", orderId);
		params.addBodyParameter("revoke", isRepeal ? "Y" : "N");
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
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()+ "");
		params.addBodyParameter("orderId", orderId);
		params.addBodyParameter("payWayId", payWayId+ "");
		api.orderPay(params, this);
		showProgressToast();
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
	 * 删除订单弹出框
	 * 
	 * @param hint
	 * @param orderId
	 * @param isRepeal
	 *            //是否撤销订单
	 */
	private void delHint(String hint, final String orderId,
			final boolean isRepeal) {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(hint).setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						delOrder(orderId, isRepeal);
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

	@Override
	protected void onPause() {
		super.onPause();
		hideProgressToast();
	}
}
