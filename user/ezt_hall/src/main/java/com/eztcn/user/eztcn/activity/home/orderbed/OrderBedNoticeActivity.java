package com.eztcn.user.eztcn.activity.home.orderbed;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;

public class OrderBedNoticeActivity extends FinalActivity {

	@ViewInject(R.id.order_check_bt)
	private Button order_check_bt;
	@ViewInject(R.id.service_introduce_tx)
	private TextView service_introduce_tx;
	@ViewInject(R.id.service_notice_tx)
	private TextView service_notice_tx;
	@ViewInject(R.id.service_introduce)
	private View service_introduce;
	@ViewInject(R.id.service_notice)
	private View service_notice;
	@ViewInject(R.id.service_introduce_tx_layout)
	private LinearLayout service_introduce_tx_layout;
	@ViewInject(R.id.service_process_notice_layout)
	private RelativeLayout service_process_notice_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_servive_order_check_details30);
		ViewUtils.inject(OrderBedNoticeActivity.this);
		loadTitleBar(true, "预约病床", null);
	}

	@OnClick(R.id.order_check_bt)
	public void order_check_btClick(View v) {

		startActivity(new Intent(this, ChoiceHosActivity.class).putExtra(
				"isOrderBed", true));

	}

	@OnClick(R.id.service_introduce_tx)
	public void service_introduce_txClick(View v) {
		service_introduce_tx.setTextColor(getResources().getColor(
				R.color.border_line));
		service_notice_tx.setTextColor(getResources().getColor(
				R.color.drug_black));
		service_introduce.setVisibility(View.VISIBLE);
		service_notice.setVisibility(View.GONE);
		service_introduce_tx_layout.setVisibility(View.VISIBLE);
		service_process_notice_layout.setVisibility(View.GONE);

	}

	@OnClick(R.id.service_notice_tx)
	public void service_notice_txClick(View v) {

		service_notice_tx.setTextColor(getResources().getColor(
				R.color.border_line));
		service_introduce_tx.setTextColor(getResources().getColor(
				R.color.drug_black));
		service_introduce.setVisibility(View.GONE);
		service_notice.setVisibility(View.VISIBLE);
		service_introduce_tx_layout.setVisibility(View.GONE);
		service_process_notice_layout.setVisibility(View.VISIBLE);
	}

}
