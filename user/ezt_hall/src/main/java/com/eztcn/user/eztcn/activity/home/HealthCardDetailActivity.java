package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter.ItemBtOnclick;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;

/**
 * @title 健康卡详情
 * @describe
 * @author ezt
 * @created 2015年1月13日
 */
public class HealthCardDetailActivity extends FinalActivity implements
		IHttpResult, ItemBtOnclick {// OnItemClickListener,

	@ViewInject(R.id.card_intro_layout)
	private LinearLayout layout2;

	@ViewInject(R.id.card_title_tv)
	private TextView tvCardTitle;// 卡标题

	@ViewInject(R.id.cardnum_tv)
	private TextView tvCardNum;// 卡号

	@ViewInject(R.id.card_state_tv)
	private TextView tvCardState;// 状态

	@ViewInject(R.id.card_date_tv)
	private TextView tvCardDate;// 有效期

	@ViewInject(R.id.service_items)
	private TextView service_items;// 服务内容
	@ViewInject(R.id.itemsLabel)
	private TextView itemsLabel;
	@ViewInject(R.id.service_notice)
	private TextView service_notice;// 服务须知
	@ViewInject(R.id.noticeLabel)
	private TextView noticeLabel;
	@ViewInject(R.id.service_process)
	private TextView service_process;// 服务流程
	@ViewInject(R.id.processLabel)
	private TextView processLabel;

	@ViewInject(R.id.intro_img)
	private ImageView imgCardIntro;// 卡详情图片

	@ViewInject(R.id.myListView)
	// , itemClick = "onItemClick"
	private MyListView ltServices;// 服务列表

	private LightAccompanyServiceAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthcard_detail);
		ViewUtils.inject(HealthCardDetailActivity.this);
		String name = getIntent().getStringExtra("name");
		loadTitleBar(true, "卡详情", null);
		tvCardTitle.setText(name);

		String cardId = getIntent().getStringExtra("carId");
		adapter = new LightAccompanyServiceAdapter(this);
		adapter.click(this);
		ltServices.setAdapter(adapter);
		intialData(cardId);
	}

	/**
	 * 获取详情
	 * 
	 * @param cardId
	 */
	private void intialData(String cardId) {

		// IEztServiceCard api = new EztServiceCardImpl();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("cardId", cardId);
		EztServiceCardImpl api = new EztServiceCardImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("cardId", cardId);
		api.getHealthcardDetail(params, this);
		showProgressToast();
	}

	public void initMealDetail(String items, String notices, String process) {
		// 服务项
		if (!TextUtils.isEmpty(items)) {
			service_items.setVisibility(View.VISIBLE);
			itemsLabel.setVisibility(View.VISIBLE);
			service_items.setText(Html.fromHtml(items));
		}
		// 服务须知
		if (!TextUtils.isEmpty(notices)) {
			service_notice.setVisibility(View.VISIBLE);
			noticeLabel.setVisibility(View.VISIBLE);
			service_notice.setText(Html.fromHtml(notices));
		}
		// 服务流程
		if (!TextUtils.isEmpty(process)) {
			service_process.setVisibility(View.VISIBLE);
			processLabel.setVisibility(View.VISIBLE);
			service_process.setText(Html.fromHtml(process));
		}
	}

	/**
	 * 设置套餐卡图片
	 */
	public void setMealImg(String url) {
		// 封面
		// FinalBitmap fb = FinalBitmap.create(this);

		Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.healthcard_default);

		if (TextUtils.isEmpty(url)) {
			url = "";
		}
		String imgurl = EZTConfig.PACKAGE_IMG + url;

		BitmapUtils bitmapUtils = new BitmapUtils(HealthCardDetailActivity.this);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		bitmapUtils.display(imgCardIntro, imgurl);
		// fb.display(imgCardIntro, imgurl, null, defaultBit);
	}

	@Override
	public void result(Object... object) {
		boolean isSuc = (Boolean) object[1];

		if (isSuc) {
			layout2.setVisibility(View.VISIBLE);
			Map<String, Object> map = (Map<String, Object>) object[2];

			if (map != null && map.size() != 0) {
				ArrayList<LightAccompanying> list = (ArrayList<LightAccompanying>) map
						.get("itemList");
				adapter.setList(list);
				// hosList = (ArrayList<Hospital>) map.get("hosList");
				// remark = (String) map.get("remark");
				String cardNo = map.get("cardNo").toString();
				String sTime = map.get("startTime").toString();
				String eTime = map.get("endTime").toString();

				if (!TextUtils.isEmpty(sTime) && !TextUtils.isEmpty(eTime)) {
					sTime = sTime.split(" ")[0];
					eTime = eTime.split(" ")[0];
				}

				String strState = "未使用";
				if (map.get("state") != null) {
					int state = (Integer) map.get("state");
					switch (state) {
					case 0:
						strState = "未使用";
						break;

					case 1:
						strState = "已使用";
						break;

					case 2:
						strState = "使用中";
						break;
					}
				}
				String url = map.get("epPic").toString();
				setMealImg(url);
				String items = map.get("remark").toString();
				String notices = map.get("notice").toString();
				String process = map.get("process").toString();
				initMealDetail(items, notices, process);

				tvCardNum.setText("卡号：\t" + cardNo);

				tvCardState.setText(strState);

				tvCardDate.setText("有效期：" + sTime + " 至 " + eTime);

			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();

		}
		hideProgressToast();

	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	@OnItemClick(R.id.myListView)
	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LightAccompanying l = adapter.getList().get(position);
		String name = l.getItemName();
		String hcuId = l.getId();
		String itemId = l.getItemId();
		startActivity(new Intent(mContext,
				HealthCardServiceDetailActivity.class).putExtra("title", name)
				.putExtra("hcuId", hcuId).putExtra("itemId", itemId));
	}

	@Override
	public void adapterOnclick(int pos, int status) {

		if (status > 0) {// 可使用
			// startActivity(new Intent(getApplicationContext(),
			// CardUsedActivity.class).putExtra("url",
			// "http://www.baidu.com").putExtra("title",
			// adapter.getList().get(pos).getItemName()));
			hintTelDialog(getString(R.string.health_tel_num), "确定拨打健康服务电话？");

		} else if (status == 2) {// 使用中

		}

	}

}
