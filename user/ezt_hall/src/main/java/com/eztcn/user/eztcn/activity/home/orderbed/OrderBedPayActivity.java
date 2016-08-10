/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.orderbed;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eztcn.user.R;
import com.eztcn.user.alipay.Result;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.mine.MyServiceActivity30;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.MessageImpl;
import com.eztcn.user.eztcn.impl.OrderBedImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * @author Liu Gang 2016年3月16日 上午10:10:20 确认支付
 */
public class OrderBedPayActivity extends FinalActivity implements
		IWXAPIEventHandler, IHttpResult {
	@ViewInject(R.id.costNameTv)
	private TextView costNameTv;

	@ViewInject(R.id.costTv)
	private TextView costTv;

	@ViewInject(R.id.allCostTv)
	private TextView allCostTv;

	@ViewInject(R.id.wechatView)
	private ImageView wechatView;

	@ViewInject(R.id.zhifuView)
	private ImageView zhifuView;

	@ViewInject(R.id.zhifuRLayout)
	private View zhifuRLayout;

	@ViewInject(R.id.wechatRLayout)
	private View wechatRLayout;

	@ViewInject(R.id.hintTv)
	private TextView hintTv;
	@ViewInject(R.id.orderBtn)
	private View orderBtn;
	private int currentClickPos = 1;
	private final int WECHAT = 0, ZHIFU = 1;
	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 2;
	// private SelectPayPopupWindow payPopwindow;
	private int payType = 330;// 支付方式(330为支付宝，2为银联，3为信用卡)
	/**
	 * 订单编号
	 */
	private String orderNoStr;
	private String orderBedId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bed_pay);
		ViewUtils.inject(OrderBedPayActivity.this);
		loadTitleBar(true, "确认支付", null);
		getData();
	}

	private void getData() {
		String sumCostStr = getIntent().getStringExtra("sumCost");
		String costStr = getIntent().getStringExtra("cost");
		boolean isFirstPay = getIntent().getBooleanExtra("isFirstPay", false);
		Bundle bundle = getIntent().getExtras();
		orderNoStr = bundle.getString("orderNoStr");
		orderBedId = bundle.getString("orderId");
		if (isFirstPay) {
			costNameTv.setText("挂号费");
		} else {
			costNameTv.setText("住院押金");
		}
		// 住院押金：羊59
		costTv.setText(costNameTv.getText().toString() + ": ¥" + costStr);
		allCostTv.setText("¥" + sumCostStr);
		String hintStr = hintTv.getText().toString().trim();
		SpannableStringBuilder builder = new SpannableStringBuilder(hintStr);
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan txtSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.main_txt_color));

		ForegroundColorSpan txtSpan0 = new ForegroundColorSpan(getResources()
				.getColor(R.color.main_txt_color));

		ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.wait_pay));

		builder.setSpan(txtSpan0, 0, hintStr.indexOf("3"),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan, hintStr.indexOf("3"), hintStr.indexOf("完"),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		builder.setSpan(txtSpan, hintStr.indexOf("完"), hintStr.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		hintTv.setText(builder);

	}

	@OnClick(R.id.wechatRLayout)
	public void wechatClick(View view) {
		currentClickPos = WECHAT;
		changeState();

	}

	@OnClick(R.id.zhifuRLayout)
	public void zhifuClick(View view) {
		currentClickPos = ZHIFU;
		changeState();

	}

	/**
	 * 改变状态
	 */
	public void changeState() {
		switch (currentClickPos) {

		case WECHAT: {
			zhifuView.setImageResource(R.drawable.cinocheck);
			wechatView.setImageResource(R.drawable.cichecked);
		}
			break;
		case ZHIFU: {
			zhifuView.setImageResource(R.drawable.cichecked);
			wechatView.setImageResource(R.drawable.cinocheck);
		}
			break;
		}

	}

	@OnClick(R.id.orderBtn)
	public void orderBtnClick(View view) {
		// 提交按钮
		// orderBedId
		OrderBedImpl createBedOrder = new OrderBedImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("orderBedId", orderBedId);
		createBedOrder.getAlipayStringById(params, this);
		showProgressToast();
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
					// 调用消息接口
					MessageImpl messageImpl = new MessageImpl();
					RequestParams params = new RequestParams();
					params.addBodyParameter("infoType", "5");
					// 消息所属系统类型 1预约检查 2预约病床 3预约药瓶 4预约挂号、当日挂号、大牌名医 5龙卡服务
					params.addBodyParameter("infoSysType", "2");
					params.addBodyParameter("userId",
							String.valueOf(BaseApplication.patient.getUserId()));
					params.addBodyParameter("orderNumber", orderNoStr);
					messageImpl.traOrderInfo(params, OrderBedPayActivity.this);
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
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp resp) {
		// 支付结果回调

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Log.d(OrderBedPayActivity.this.getClass().getSimpleName(),
					"onPayFinish,errCode=" + resp.errCode);

			// 回调中errCode值列表：
			//
			// 名称 描述 解决方案
			// 0 成功 展示成功页面
			// -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
			// -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
		}
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {

		case HttpParams.GET_ALIPAY_STRING_BY_ID:// 提交订单
			if (isSuc) {// 成功
				Map<String, Object> map = (Map<String, Object>) object[2];
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					String number = String.valueOf(map.get("number"));
					// if ("2000".equals(number)) {
					final String data = (String) map.get("data");
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
					// }
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

		case HttpParams.TRA_ORDER_INFO:
			if (isSuc) {
				SystemPreferences.save(EZTConfig.KEY_HAVE_MSG, true);
				// 支付成功结束填写支付单界面
				finisActivity(ActivityOrderBedWrite.class);
				// 支付成功结束选择检医院界面
				finisActivity(ChoiceHosActivity.class);
				finisActivity(OrderBedNoticeActivity.class);
				Intent check_order_intent = new Intent();
				check_order_intent
						.setClass(mContext, MyServiceActivity30.class);
				startActivity(check_order_intent);
				finish();
			}
			break;
		}

	}
}
