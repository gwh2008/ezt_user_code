
package com.eztcn.user.eztcn.activity.home.ordercheck;
import java.util.List;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrderItem;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.MessageImpl;
import com.eztcn.user.eztcn.impl.OrderCheckImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * @author Liu Gang 2016年3月16日 上午10:10:20
 */
public class CheckItemPayActivity extends FinalActivity implements
		IWXAPIEventHandler, IHttpResult {

	@ViewInject(R.id.orderLLayout)
	private LinearLayout orderLLayout;

	@ViewInject(R.id.orderNumTv)
	private TextView orderNumTv;

	@ViewInject(R.id.costTv)
	private TextView costTv;

	@ViewInject(R.id.wechatView)
	private ImageView wechatView;

	@ViewInject(R.id.zhifuView)
	private ImageView zhifuView;
	@ViewInject(R.id.hintTv)
	private TextView hintTv;
	@ViewInject(R.id.orderBtn)
	private View orderBtn;
	private int currentClickPos = 1;
	private final int WECHAT = 0, ZHIFU = 1;
	// 返回支付结果标记
	private static final int ALIPAY_PAY_FLAG = 2;
	private int payType = 330;// 支付方式(330为支付宝，2为银联，3为信用卡)
	private FamilyMember familyMember;
	private Hospital hospital;
	private String patientSpecialNeed;
	private String patientPhone;
	private List<CheckOrderItem> checkOrderItemList;
	/**
	 * 订单编号
	 */
	private String orderNoStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		ViewUtils.inject(CheckItemPayActivity.this);
		loadTitleBar(true, "确认支付", null);
		getData();
	}

	private void getData() {
		String sumCostStr = getIntent().getStringExtra("sumCost");
		Bundle bundle = getIntent().getExtras();
		hospital = (Hospital) bundle.get("hospital");
		familyMember = (FamilyMember) bundle.get("familyMember");

		patientSpecialNeed = bundle.getString("patientSpecialNeed");
		patientPhone=bundle.getString("patientPhone");
		
		checkOrderItemList = (List<CheckOrderItem>) getIntent().getExtras()
				.get("checkOrderItemList");
		orderNumTv.setVisibility(View.GONE);
		orderNumTv.setText("");
		costTv.setText("¥" + sumCostStr);
		String hintStr = hintTv.getText().toString().trim();
		SpannableStringBuilder builder = new SpannableStringBuilder(hintStr);
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan txtSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.main_txt_color));

		ForegroundColorSpan txtSpan0 = new ForegroundColorSpan(getResources()
				.getColor(R.color.main_txt_color));

		ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources()
				.getColor(R.color.red));

		builder.setSpan(txtSpan0, 0, hintStr.indexOf("3"),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan, hintStr.indexOf("3"), hintStr.indexOf("完"),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		builder.setSpan(txtSpan, hintStr.indexOf("完"), hintStr.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		hintTv.setText(builder);
		int i=0;
		for (CheckOrderItem checkItem:
			 checkOrderItemList) {
			i++;
			View convertView = LayoutInflater.from(CheckItemPayActivity.this).inflate(
						R.layout.item_orderdetial, null);

					TextView checkNameTv = (TextView) convertView.findViewById(R.id.itemCheckNameTv);
				TextView costTv = (TextView) convertView
						.findViewById(R.id.costTv);


			checkNameTv.setText(checkItem.getNameStr());
			costTv.setText("检查费" + ":¥"
					+ checkItem.getCostStr());
			if(i==checkOrderItemList.size()){
				View dividerView=convertView.findViewById(R.id.dividerLine);
				dividerView.setVisibility(View.INVISIBLE);
			}


			orderLLayout.addView(convertView);
		}
//		adapter.setList(checkOrderItemList);
	}

	@OnClick(R.id.wechatRLayout)
	public void wechatClick(View view) {
		currentClickPos = WECHAT;
		//changeState();
	}

	@OnClick(R.id.zhifuRLayout)
	public void zhifuClick(View view) {
		currentClickPos = ZHIFU;
	//	changeState();

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

		OrderCheckImpl orderCheckImpl = new OrderCheckImpl();
		RequestParams params = new RequestParams();
		// patientId 患者ID
		params.addBodyParameter("patientId", familyMember.getPatientId());
		// userId 用户ID
		params.addBodyParameter("userId",
				String.valueOf(BaseApplication.patient.getUserId()));
		// patientName 检查者姓名
		params.addBodyParameter("patientName", familyMember.getMemberName());
		// patientSex 检查者性别
		params.addBodyParameter("patientSex", familyMember.getSex() == 0 ? "男"
				: "女");
		params.addBodyParameter("patientPhone",
				String.valueOf(patientPhone));
		// patientCardName 检查者证件类型
		params.addBodyParameter("patientCardName", "身份证");
		// patientCardNum 证件号码
		params.addBodyParameter("patientCardNum", familyMember.getIdCard());
		// patientOrderType 订单类型 ("预约检查")
		params.addBodyParameter("patientOrderType", "预约检查");
		// hospitalID 医院ID
		params.addBodyParameter("hospitalID", String.valueOf(hospital.getId()));
		// hospitalName 医院名称
		params.addBodyParameter("hospitalName", hospital.gethName());
		// patientSpecialNeed 特殊需求
		params.addBodyParameter("patientSpecialNeed", patientSpecialNeed);
		// testSelected 选择检查项表
		StringBuffer idSB = new StringBuffer();
		for (int i = 0; i < checkOrderItemList.size(); i++) {
			CheckOrderItem item = checkOrderItemList.get(i);
			if (null != item.getCheckId()) {// 过滤掉基本费用
				idSB.append(item.getCheckId());
				if (i != checkOrderItemList.size() - 1) {
					idSB.append(",");
				}
			}
		}
		params.addBodyParameter("testSelected", idSB.toString());
		orderCheckImpl.commitCheckOrder(params, CheckItemPayActivity.this);
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
					params.addBodyParameter("infoType", "1");
					// 消息所属系统类型 1预约检查 2预约病床 3预约药瓶 4预约挂号、当日挂号、大牌名医 5龙卡服务
					params.addBodyParameter("infoSysType", "1");
					params.addBodyParameter("userId",
							String.valueOf(BaseApplication.patient.getUserId()));
					params.addBodyParameter("orderNumber", orderNoStr);
					messageImpl.traOrderInfo(params, CheckItemPayActivity.this);
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
			Log.d(CheckItemPayActivity.this.getClass().getSimpleName(),
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
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		switch (type) {

		case HttpParams.COMMIT_ORDER_CHECK:// 提交订单
			if (isSuc) {// 成功
				Map<String, Object> map = (Map<String, Object>) object[2];
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					final String data = (String) map.get("data");

					orderNoStr = String.valueOf(map.get("orderNo"));
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
				finisActivity(WrittingOrderActivity.class);
				// 支付成功结束选择检查项界面
				finisActivity(ChoiceCheckItemActivity.class);
				// 支付成功结束选择检医院界面
				finisActivity(ChoiceHosActivity.class);
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
