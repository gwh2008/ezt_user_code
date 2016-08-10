package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eztcn.user.R;
import com.eztcn.user.alipay.Result;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.MoneyChoiceAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Money;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 充值页面
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class RechargeActivity extends FinalActivity implements IHttpResult,
		OnItemClickListener, TextWatcher {

	@ViewInject(R.id.money_hint)
	private TextView tvMoneyHint;// 充值对应金额

	@ViewInject(R.id.et_money)
	private EditText etMoney;// 医通币数量

	@ViewInject(R.id.money_layout)//, click = "onClick"
	private LinearLayout layoutMoney;

	@ViewInject(R.id.alipay_layout)//, click = "onClick"
	private RelativeLayout layoutAlipay;// 支付宝

	@ViewInject(R.id.unionpay_layout)//, click = "onClick"
	private RelativeLayout layoutUnionpay;// 银联

	@ViewInject(R.id.blue_card_layout)//, click = "onClick"
	private RelativeLayout layoutBlueCard;// 信用卡

	private int payType = -1;// 支付方式(330为支付宝，2为银联，3为信用卡)

	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 1;

	private AlertDialog dialog;

	private MoneyChoiceAdapter adapter;
	private ListView dialogLt;
	private TextView dialogTv;
	private ArrayList<Money> moneyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge);
		ViewUtils.inject(RechargeActivity.this);
		loadTitleBar(true, "充值", null);
		etMoney.addTextChangedListener(this);
		// initialView();
		// initialData();

	}

	/**
	 * 初始化view
	 */
	private void initialView() {
		View view = LinearLayout.inflate(mContext,
				R.layout.dialog_choice_money, null);
		dialogLt = (ListView) view.findViewById(R.id.dialog_lt);
		dialogTv = (TextView) view.findViewById(R.id.dialog_title);
		dialog = new AlertDialog.Builder(this).create();
		dialogLt.setOnItemClickListener(this);
		dialog.setView(view);
		adapter = new MoneyChoiceAdapter(this);
		dialogLt.setAdapter(adapter);

	}

	/**
	 * 获取数据
	 */
	private void initialData() {
		String[] arrys = { "男", "女", "女", "女" };
		String[] arry = { "男", "女", "女", "女" };
		moneyList = new ArrayList<Money>();
		for (int i = 0; i < arry.length; i++) {
			Money money = new Money();
			money.setStrMoney(arrys[i]);
			money.setStrMyMoney(arry[i]);
			moneyList.add(money);
		}
		adapter.setList(moneyList);

	}
	
	@OnClick(R.id.money_layout)
	private void money_layoutClick(View v){
		payType = -1;
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow()
				.getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		int totalHeight = 0;
		for (int i = 0, len = adapter.getCount(); i < len; i++) {
			View listItem = adapter.getView(i, null, dialogLt);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int item_height = listItem.getMeasuredHeight()
					+ dialogLt.getDividerHeight();
			totalHeight += item_height; // 统计所有子项的总高度
		}
		totalHeight = totalHeight
				+ ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
						mContext, R.dimen.dialog_title_bar_size));
		params.height = totalHeight > getWindowHeight() / 2 ? getWindowHeight() / 2
				: WindowManager.LayoutParams.WRAP_CONTENT;

		dialog.getWindow().setAttributes(params);
		
		
	}
	
	@OnClick(R.id.alipay_layout)// 支付宝
	private void alipay_layoutClick(View v){
		payType = 330;
		
		String strMoney = etMoney.getText().toString().trim();
		if (TextUtils.isEmpty(strMoney)) {
			Toast.makeText(mContext, "请输入医通币数量", Toast.LENGTH_SHORT).show();
			return;
		}
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("currency", strMoney);// 充值的医通币数量
		params.addBodyParameter("discountId", "1");// 优惠id
		new PayImpl().getPayMoney(params, this);
		showProgressToast();
		
		
	}
	
	@OnClick(R.id.unionpay_layout) // 银联
	private void unionpay_layoutClick(View v){
		payType = -1;
		
		String strMoney = etMoney.getText().toString().trim();
		if (TextUtils.isEmpty(strMoney)) {
			Toast.makeText(mContext, "请输入医通币数量", Toast.LENGTH_SHORT).show();
			return;
		}
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("currency", strMoney);// 充值的医通币数量
		params.addBodyParameter("discountId", "1");// 优惠id
		new PayImpl().getPayMoney(params, this);
		showProgressToast();	
	}
	
	@OnClick(R.id.blue_card_layout) // 信用卡
	private void blue_card_layoutClick(View v){
		payType = -1;
		
		String strMoney = etMoney.getText().toString().trim();
		if (TextUtils.isEmpty(strMoney)) {
			Toast.makeText(mContext, "请输入医通币数量", Toast.LENGTH_SHORT).show();
			return;
		}
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("currency", strMoney);// 充值的医通币数量
		params.addBodyParameter("discountId", "1");// 优惠id
		new PayImpl().getPayMoney(params, this);
		showProgressToast();
		
		
	}


//	public void onClick(View v) {
//		payType = -1;
//		switch (v.getId()) {
//		case R.id.money_layout:
//			dialog.show();
//			WindowManager.LayoutParams params = dialog.getWindow()
//					.getAttributes();
//			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//
//			int totalHeight = 0;
//			for (int i = 0, len = adapter.getCount(); i < len; i++) {
//				View listItem = adapter.getView(i, null, dialogLt);
//				listItem.measure(0, 0); // 计算子项View 的宽高
//				int item_height = listItem.getMeasuredHeight()
//						+ dialogLt.getDividerHeight();
//				totalHeight += item_height; // 统计所有子项的总高度
//			}
//			totalHeight = totalHeight
//					+ ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
//							mContext, R.dimen.dialog_title_bar_size));
//			params.height = totalHeight > getWindowHeight() / 2 ? getWindowHeight() / 2
//					: WindowManager.LayoutParams.WRAP_CONTENT;
//
//			dialog.getWindow().setAttributes(params);
//			return;
//		case R.id.alipay_layout:// 支付宝
//			payType = 330;
//			break;
//		case R.id.unionpay_layout: // 银联
//			break;
//		case R.id.blue_card_layout:// 信用卡
//			break;
//		}
//		String strMoney = etMoney.getText().toString().trim();
//		if (TextUtils.isEmpty(strMoney)) {
//			Toast.makeText(mContext, "请输入医通币数量", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("currency", strMoney);// 充值的医通币数量
//		params.put("discountId", "1");// 优惠id
//		new PayImpl().getPayMoney(params, this);
//		showProgressToast();
//	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		int resultType = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		Map<String, Object> map = null;
		if (isSuc) {// 访问成功
			map = (Map<String, Object>) object[2];
			if (map == null) {
				return;
			}
			boolean flag = (Boolean) map.get("flag");
			switch (resultType) {
			case HttpParams.PAY_ORDER:

				if (flag) {// 获取成功
					final String payInfo = (String) map.get("data");
					switch (payType) {
					case 330:// 支付宝
						Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								// 构造PayTask 对象
								PayTask alipay = new PayTask(mContext);
								// 调用支付接口
								String result = alipay.pay(payInfo,true);
								Message msg = new Message();
								msg.what = ALIPAY_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};

						Thread payThread = new Thread(payRunnable);
						payThread.start();
						break;

					case 2:// 银联

						break;

					case 3:// 信用卡

						break;
					}
				} else {
					Logger.i("提交订单", "获取失败---");
				}

				break;

			case HttpParams.GET_PAY_MONEY:// 获取支付金额
				if (flag) {// 获取成功
					// Toast.makeText(mContext, "获取支付金额成功", Toast.LENGTH_SHORT)
					// .show();
					String payMoney = (String) map.get("data");
					switch (payType) {
					case 330:
						payType = 330;
						submitOrder(payType + "", payMoney);
						break;

					case R.id.unionpay_layout:
						payType = 2;
						break;

					case R.id.blue_card_layout:
						payType = 3;
						break;
					}
				} else {
					Logger.i("支付金额", "获取失败---");
				}
				break;
			}

		} else {// 服务器访问失败
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
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
	 * 提交订单
	 */
	public void submitOrder(String payWayId, String price) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("payWayId", payWayId);
		params.addBodyParameter("currency", price);
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		new PayImpl().submitPayOrder(params, this);
		showProgressToast();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		dialog.dismiss();

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		tvMoneyHint.setText(TextUtils.isEmpty(s) ? "(消费金额：0 元)" : "(消费金额："
				+ s.toString() + " 元)");
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
