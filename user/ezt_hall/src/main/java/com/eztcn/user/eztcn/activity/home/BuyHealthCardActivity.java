package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eztcn.user.R;
import com.eztcn.user.alipay.Result;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.ChoiceCouponAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Coupons;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.customView.SelectPayPopupWindow;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 购买健康卡
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class BuyHealthCardActivity extends FinalActivity implements
		IHttpResult, TextWatcher {//OnClickListener

	@ViewInject(R.id.price)
	private TextView tvPrice;
	@ViewInject(R.id.count_et)
	private EditText etCount;
	@ViewInject(R.id.mealName)
	private TextView name;

	@ViewInject(R.id.layout_coupon)
	private LinearLayout layoutCoupon;

	@ViewInject(R.id.lv_coupon)
	private MyListView lv;// 优惠券列表

	@ViewInject(R.id.subtract_tv)//, click = "onClick"
	private TextView tvSubtract;// 减

	@ViewInject(R.id.add_tv)//, click = "onClick"
	private TextView tvAdd;// 加

	@ViewInject(R.id.affirmBuy)//, click = "onClick"
	private Button affirmBuy;

	private String cardId;
	private String money;
	private String mealName;

	private SelectPayPopupWindow payPopwindow;
	private int payType = -1;// 支付方式(330为支付宝，2为银联，3为信用卡)
	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 1;

	private ChoiceCouponAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buyhealthcard);
		ViewUtils.inject(BuyHealthCardActivity.this);
		loadTitleBar(true, "购 买", null);
		etCount.addTextChangedListener(this);
		cardId = getIntent().getStringExtra("cardId");
		money = getIntent().getStringExtra("money");
		mealName = getIntent().getStringExtra("name");
		tvPrice.setText(money + "元");
		if (!TextUtils.isEmpty(mealName)) {
			name.setText(mealName);
		}
		initialData();
	}

	/**
	 * 初始化优惠券列表
	 */
	private void initialData() {
		showProgressToast();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		PayImpl api = new PayImpl();
		api.getUseCouponList(params, this);

	}
	@OnClick(R.id.affirmBuy)
	public void affirmBuyClick(View v) {

		int nums = 1;
		if (!TextUtils.isEmpty(etCount.getText().toString())) {
			nums = Integer.parseInt(etCount.getText().toString());
		} else {
			etCount.setText(nums + "");
		}

	// 购买
			if (BaseApplication.getInstance().patient != null) {
				String count = etCount.getText().toString();
				if (TextUtils.isEmpty(count)) {
					Toast.makeText(mContext, "请输入购买数量", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					dialogChoicePay(v);
				}

			} else {
				HintToLogin(Constant.LOGIN_COMPLETE);
			}
	}
	@OnClick(R.id.subtract_tv)
	public void subtract_tvClick(View v) {
		int nums = 1;
		if (!TextUtils.isEmpty(etCount.getText().toString())) {
			nums = Integer.parseInt(etCount.getText().toString());
		} else {
			etCount.setText(nums + "");
		}

		
		// 减
			etCount.clearFocus();
			hideSoftInput(etCount);
			if (nums > 1) {
				nums--;
				etCount.setText(nums + "");
			} else {
				Toast.makeText(mContext, "受不了了，不能再减少啦", Toast.LENGTH_SHORT)
						.show();
				return;
			}
	}
	@OnClick(R.id.add_tv)
	public void add_tvClick(View v) {

		int nums = 1;
		if (!TextUtils.isEmpty(etCount.getText().toString())) {
			nums = Integer.parseInt(etCount.getText().toString());
		} else {
			etCount.setText(nums + "");
		}
			etCount.clearFocus();
			hideSoftInput(etCount);
			nums++;
			etCount.setText(nums + "");
	}
	/**
	 * 购买健康卡
	 */
	private void buyCard(String userId, String packageId, String nums,
			String payWayId, boolean isUseCoupon, String cardNos) {
		EztServiceCardImpl api = new EztServiceCardImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("packageId", packageId);
		params.addBodyParameter("userId", userId);
		params.addBodyParameter("nums", nums);
		params.addBodyParameter("payWayId", nums);
		if (isUseCoupon) {
			params.addBodyParameter("eztCoupon", "Y");
			params.addBodyParameter("ccCardNo", cardNos);
		}
		api.createTraOrderpayPackage(params, this);
		showProgressToast();
	}

	private void updatePrice(int nums) {
		double newPrice = Double.parseDouble(money);
		double sumPrice = newPrice * nums + cPrice;
		tvPrice.setText(sumPrice + "元");

	}

	private double cPrice = 0.0;// 优惠券金额

	public void updatePrice(double price) {
		cPrice = cPrice + price;
		updatePrice(Integer.parseInt(etCount.getText().toString()));

	}

	/**
	 * 选择支付方式
	 */
	private void dialogChoicePay(View v) {
		payPopwindow = new SelectPayPopupWindow(mContext, new ChoicePayClick());
		// 显示窗口
		payPopwindow.showAtLocation(v, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	public class ChoicePayClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.alipay_layout:// 支付宝
				payType = 330;
				break;
			}

			// 获取所选中优惠券
			boolean isUseCoupon = false;
			String cardNos = "";

			if (adapter != null) {
				Set<Integer> checkedIds = adapter.checkedRecId;
				for (Integer pos : checkedIds) {
					Coupons c = adapter.getList().get(pos);
					cardNos = c.getCardNo() + ",";
				}
				if (checkedIds != null && checkedIds.size() != 0) {
					isUseCoupon = true;
				}
			}
			buyCard(BaseApplication.patient.getUserId() + "", cardId, etCount
					.getText().toString(), payType + "", isUseCoupon, cardNos);
			payPopwindow.dismiss();
		}

	}

	@Override
	public void result(Object... object) {
		boolean isSuc = (Boolean) object[1];
		int type = (Integer) object[0];
		switch (type) {
		case HttpParams.GET_USE_COUPON_LIST:// 获取可使用的优惠券
			hideProgressToast();
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				boolean isFlag = (Boolean) map.get("flag");
				if (isFlag) {// 成功
					ArrayList<Coupons> cList = (ArrayList<Coupons>) map
							.get("data");
					if (cList != null && cList.size() != 0) {
						adapter = new ChoiceCouponAdapter(this);
						lv.setAdapter(adapter);
						adapter.setList(cList);

						// 初始化价格
						cPrice = 0.0;
						updatePrice(Integer.parseInt(etCount.getText()
								.toString()));

						layoutCoupon.setVisibility(View.VISIBLE);
					} else {
						layoutCoupon.setVisibility(View.GONE);
					}
				} else {
					// Toast.makeText(mContext,
					// getString(R.string.service_error),
					// Toast.LENGTH_SHORT).show();
				}
			}

			break;

		default:// 下单
			if (isSuc) {// 成功
				Map<String, Object> map = (Map<String, Object>) object[2];
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					final String data = (String) map.get("data");
					switch (payType) {
					case 330:// 支付宝
						Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								// 构造PayTask 对象
								PayTask alipay = new PayTask(mContext);
								// 调用支付接口
								String result = alipay.pay(data,true);
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
		hideProgressToast();
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

					if (adapter != null) {
						adapter.checkedRecId.clear();// 清理选中的内容
						initialData();
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
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (!TextUtils.isEmpty(s.toString())
				&& Integer.parseInt(s.toString()) == 0) {
			etCount.setText("1");
			etCount.clearFocus();
			hideSoftInput(etCount);
			updatePrice(1);
		} else if (!TextUtils.isEmpty(s.toString())
				&& Integer.parseInt(s.toString()) != 0) {
			updatePrice(Integer.parseInt(s.toString()));
		} else if (TextUtils.isEmpty(s.toString())) {
			updatePrice(0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
