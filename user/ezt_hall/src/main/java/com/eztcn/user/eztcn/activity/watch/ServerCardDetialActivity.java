//package com.eztcn.user.eztcn.activity.watch;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.text.Html;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.afinal.bitmap.FinalBitmap;
//import com.eztcn.user.eztcn.BaseApplication;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.activity.home.BuyHealthCardActivity;
//import com.eztcn.user.eztcn.api.IHttpResult;
//import com.eztcn.user.eztcn.bean.HealthCard;
//import com.eztcn.user.eztcn.bean.LightAccompanying;
//import com.eztcn.user.eztcn.config.EZTConfig;
//import com.eztcn.user.eztcn.impl.ServerImpl;
//import com.eztcn.user.eztcn.utils.HttpParams;
//
///**
// * @title 卡套餐详情
// * @describe
// * @author ezt
// * @created 2015年6月17日
// */
//public class ServerCardDetialActivity extends FinalActivity implements
//		IHttpResult {
//
//	@ViewInject(R.id.cardImg)
//	private ImageView cardImg;// 封面
//	@ViewInject(R.id.mealName)
//	private TextView mealName;// 套餐名称
//	@ViewInject(R.id.mealPrice)
//	private TextView mealPrice;// 套餐价格
//	@ViewInject(R.id.service_items)
//	private TextView service_items;// 服务内容
//	@ViewInject(R.id.itemsLabel)
//	private TextView itemsLabel;
//	@ViewInject(R.id.service_notice)
//	private TextView service_notice;// 服务须知
//	@ViewInject(R.id.noticeLabel)
//	private TextView noticeLabel;
//	@ViewInject(R.id.service_process)
//	private TextView service_process;// 服务流程
//	@ViewInject(R.id.processLabel)
//	private TextView processLabel;
//
//	@ViewInject(R.id.buyPrice)
//	private TextView buyPrice;// 购买价格
//	@ViewInject(R.id.buy, click = "onClick")
//	private TextView buy;
//
//	private String card_id = "";
//	// private List<LightAccompanying> list;
//	private double price = -1;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.buyserverdetail);
//		loadTitleBar(true, "卡套餐详情", null);
//		savedInstanceState = getIntent().getExtras();
//		HealthCard card = null;
//		if (savedInstanceState != null) {
//			card = (HealthCard) savedInstanceState.getSerializable("info");
//		}
//		if (card != null) {
//			buy.setEnabled(true);
//			card_id = card.getId();
//			price = card.getCardPrice();
//			mealName.setText(card.getCardName());
//			String p = price > 0 ? ("￥ " + price) : "￥ 0";
//			mealPrice.setText(p);
//			buyPrice.setText(p);
//			// 封面
//			FinalBitmap fb = FinalBitmap.create(this);
//			Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
//					R.drawable.healthcard_default);
//			String imgurl = EZTConfig.PACKAGE_IMG + card.getCardCover();
//			fb.display(cardImg, imgurl, null, defaultBit);
//		} else {
//			buy.setEnabled(false);
//			Toast.makeText(getApplicationContext(),
//					getResources().getString(R.string.service_error),
//					Toast.LENGTH_SHORT).show();
//		}
//		getHealthCardDetail();
//	}
//
//	/**
//	 * 获取套餐详情
//	 */
//	public void getHealthCardDetail() {
//		ServerImpl api = new ServerImpl();
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("cardId", card_id);
//		api.getHealthcardDetail(params, this);
//		showProgressToast();
//	}
//
//	public void initMealDetail(String items,String notices,String process) {
//		
//		// 服务项
//		if (!TextUtils.isEmpty(items)) {
//			service_items.setVisibility(View.VISIBLE);
//			itemsLabel.setVisibility(View.VISIBLE);
//			service_items.setText(Html.fromHtml(items));
//		}
//		// 服务须知
//		if (!TextUtils.isEmpty(notices)) {
//			service_notice.setVisibility(View.VISIBLE);
//			noticeLabel.setVisibility(View.VISIBLE);
//			service_notice.setText(Html.fromHtml(notices));
//		}
//		// 服务流程
//		if (!TextUtils.isEmpty(process)) {
//			service_process.setVisibility(View.VISIBLE);
//			processLabel.setVisibility(View.VISIBLE);
//			service_process.setText(Html.fromHtml(process));
//		}
//	}
//
//	public void onClick(View v) {
//		if (BaseApplication.eztUser == null) {
//			HintToLogin(22);
//			return;
//		}
//		if (v.getId() == R.id.buy) {
//			if (price >= 0) {
//				Intent intent = new Intent(this, BuyHealthCardActivity.class);
//				intent.putExtra("cardId", card_id);
//				intent.putExtra("money", price + "");
//				intent.putExtra("name", mealName.getText().toString());
//				startActivity(intent);
//			} else {
//				Toast.makeText(getApplicationContext(),
//						getResources().getString(R.string.service_error),
//						Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
//
//	@Override
//	public void result(Object... object) {
//		hideProgressToast();
//		if (object == null) {
//			return;
//		}
//		Object[] obj = object;
//		Integer taskID = (Integer) obj[0];
//		if (taskID == null) {
//			return;
//		}
//		boolean status = (Boolean) obj[1];
//		if (!status) {
//			Toast.makeText(mContext,
//					getResources().getString(R.string.service_error),
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//		Map<String, Object> map = (Map<String, Object>) obj[2];
//		if (map == null || map.size() == 0) {
//			Toast.makeText(mContext,
//					getResources().getString(R.string.service_error),
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
////		boolean flag = (Boolean) map.get("flag");
////		if (!flag) {
////			Toast.makeText(mContext, map.get("msg") + "".toString(),
////					Toast.LENGTH_SHORT).show();
////			return;
////		}
//		if (taskID == HttpParams.GET_HEALTHCARD_DETAIL) {
//			
//			initMealDetail(String.valueOf(map.get("remark")),String.valueOf(map.get("notice")),String.valueOf(map.get("process")));
//		}
//	}
//}
