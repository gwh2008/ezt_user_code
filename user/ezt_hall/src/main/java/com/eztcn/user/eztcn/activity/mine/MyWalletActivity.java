package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.DragonCard;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 我的钱包
 * @describe
 * @author ezt
 * @created 2014年12月30日
 */
public class MyWalletActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.money)
	private TextView money;

	@ViewInject(R.id.couponLayout)//, click = "onClick"
	private LinearLayout couponLayout;// 优惠券

	@ViewInject(R.id.coupon_num)
	private TextView tvCouponNum;// 优惠券数量

	@ViewInject(R.id.moneyLayout)//, click = "onClick"
	private LinearLayout moneyLayout;
	@ViewInject(R.id.tradeDetail)//, click = "onClick"
	private TextView tradeDetail;
	@ViewInject(R.id.myOrderForm)//, click = "onClick"
	private TextView myOrderForm;
	@ViewInject(R.id.myHealthCard)//, click = "onClick"
	private TextView myService;
	@ViewInject(R.id.health_dragon)//, click = "onClick"
	private TextView myHealthDragon;

	@ViewInject(R.id.allLayout)
	private LinearLayout allLayout;

	private Intent intent;
	private int eztCurrency;// 余额

//	private boolean isBinding = false;// 是否绑定激活建行龙卡
	private DragonCard card;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mywallet);
		ViewUtils.inject(MyWalletActivity.this);
		loadTitleBar(true, "我的钱包", null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		callbackNum = 0;
		if (BaseApplication.getInstance().isNetConnected) {
			getUserInfo();
			// getHealthDragonInfo();
			getHealthDragonInfo();
			showProgressToast();

		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}
	
	@OnClick(R.id.tradeDetail)// 交易明细
	private void tradeDetailClick(View v){
		intent = new Intent();
		intent.setClass(mContext, TradeDetailActivity.class);
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	@OnClick(R.id.myOrderForm)// 我的订单
	private void myOrderFormClick(View v){
		intent = new Intent();
		intent.setClass(mContext, MyOrderListActivity.class);
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	@OnClick(R.id.myHealthCard)// 健康卡
	private void myHealthCardClick(View v){
		intent = new Intent();
		intent.setClass(mContext, MyHealthCardActivity.class);
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	@OnClick(R.id.moneyLayout)
	private void moneyLayoutClick(View v){
		intent = new Intent();
		intent.setClass(mContext, EztCurrencyActivity.class);
		intent.putExtra("ec", eztCurrency);
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	@OnClick(R.id.couponLayout)// 优惠券
	private void couponLayoutClick(View v){
		intent = new Intent();
		intent.setClass(mContext, MyCouponsActivity.class);
		intent.putExtra("ec", eztCurrency);
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	@OnClick(R.id.health_dragon)// 健康龙卡
	private void health_dragonClick(View v){
		intent = new Intent();
		// intent.putExtra("isActivate", isBinding);
					// intent.putExtra("isActivate", false); 2015/11/20 直接条健康龙卡，去那个界面做判断
//					intent.setClass(getApplicationContext(), MyDragonCardActivity.class);
					if(null==card){
						intent.setClass(getApplicationContext(), DragonToActiveActivity.class);
					}else{
						intent.setClass(getApplicationContext(), DragonCardActivity.class);
					}
		intent.putExtra("ec", eztCurrency);
		if (intent != null) {
			startActivity(intent);
		}
	}
	

//	public void onClick(View v) {
//		intent = new Intent();
//		switch (v.getId()) {
//		case R.id.tradeDetail:// 交易明细
//			// Toast.makeText(mContext, getString(R.string.function_hint),
//			// Toast.LENGTH_SHORT).show();
//			intent.setClass(mContext, TradeDetailActivity.class);
//			break;
//		case R.id.myOrderForm:// 我的订单
//			// Toast.makeText(mContext, getString(R.string.function_hint),
//			// Toast.LENGTH_SHORT).show();
//			// intent = null;
//			intent.setClass(mContext, MyOrderListActivity.class);
//			break;
//		case R.id.myHealthCard:// 健康卡
//			intent.setClass(mContext, MyHealthCardActivity.class);
//			// Toast.makeText(mContext, getString(R.string.function_hint),
//			// Toast.LENGTH_SHORT).show();
//			// intent = null;
//			break;
//		case R.id.moneyLayout:
//			intent.setClass(mContext, EztCurrencyActivity.class);
//			intent.putExtra("ec", eztCurrency);
//			break;
//
//		case R.id.couponLayout:// 优惠券
//			intent.setClass(mContext, MyCouponsActivity.class);
//			break;
//
//		case R.id.health_dragon:// 健康龙卡
//
//			// intent.putExtra("isActivate", isBinding);
//			// intent.putExtra("isActivate", false); 2015/11/20 直接条健康龙卡，去那个界面做判断
////			intent.setClass(getApplicationContext(), MyDragonCardActivity.class);
//			if(null==card){
//				intent.setClass(getApplicationContext(), DragonToActiveActivity.class);
//			}else{
//				intent.setClass(getApplicationContext(), DragonCardActivity.class);
//			}
//			break;
//		}
//		if (intent != null) {
//			startActivity(intent);
//		}
//	}

	private void getHealthDragonInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
		//TODO
		params.addBodyParameter("CustID", BaseApplication.patient.getEpPid());
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.getCCbInfo(params, this);
	}

	/**
	 * 获取用户账户信息
	 */
	private void getUserInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		new PayImpl().getCurrencyMoney(params, this);
	}

	private int callbackNum = 0;

	@Override
	public void result(Object... object) {

		if (object == null) {
			return;
		}
		int callBackFlag = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (callBackFlag) {
		case HttpParams.GET_CURRENCY_MONEY:
			callbackNum++;
			if (isSuc) {
				allLayout.setVisibility(View.VISIBLE);
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map == null || map.size() == 0) {
					return;
				}
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					Double d = (Double) map.get("remain");
					if (d != null) {
						eztCurrency = (int) Math.floor(d);
					}
					money.setText(eztCurrency + " 医通币");
				}
			}
			break;
		case HttpParams.GAIN_CCBINFO: {
			if(isSuc){
				Map<String, Object> map = (Map<String, Object>) object[2];
				if(map.containsKey("flag")){
					if((Boolean) map.get("flag")){
						//绑定卡了
					if(map.containsKey("data")){
						card=(DragonCard) map.get("data");
					}
					}else{
						//用户信息不存在 未绑定卡
						card=null;
					}
				}
			}
		}
			break;

		}
		if (callbackNum == 1) {
			hideProgressToast();
		}

	}
}
