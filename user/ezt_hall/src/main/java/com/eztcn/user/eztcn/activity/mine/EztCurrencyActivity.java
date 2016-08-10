package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 医通币充值
 * @describe
 * @author ezt
 * @created 2015年6月10日
 */
public class EztCurrencyActivity extends FinalActivity {

	@ViewInject(R.id.eztCurrency)
	private TextView money;
	@ViewInject(R.id.recharge)//, click = "onClick"
	private TextView recharge;

	private int eztCurrency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eztcurrency);
		ViewUtils.inject(EztCurrencyActivity.this);
		loadTitleBar(true, "医通币", null);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			eztCurrency = savedInstanceState.getInt("ec");
		}
		money.setText(eztCurrency + " 医通币");
	}

	@OnClick(R.id.recharge)
	private void rechargeClick(View v){
		Intent intent = new Intent();
		intent.setClass(mContext, RechargeActivity.class);
		startActivity(intent);
	}
	
//	public void onClick(View v) {
//		Intent intent = new Intent();
//		intent.setClass(mContext, RechargeActivity.class);
//		startActivity(intent);
//	}
}
