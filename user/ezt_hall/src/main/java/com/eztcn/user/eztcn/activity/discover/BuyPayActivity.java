package com.eztcn.user.eztcn.activity.discover;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eztcn.user.R;
import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.hall.model.PatientBean;

/**
 * @title 购买支付
 * @describe
 * @author ezt
 * @created 2015年3月2日
 */
public class BuyPayActivity extends FinalActivity implements OnClickListener {

	private TextView login;// 登录
	@ViewInject(R.id.securityLayout)
	private LinearLayout securityLayout;// 验证码layout
	@ViewInject(R.id.name)
	private EditText name;
	@ViewInject(R.id.phone)
	private EditText phone;
	@ViewInject(R.id.securityCode)
	private EditText securityCode;// 验证码
	@ViewInject(R.id.getSecurityCode)
	private TextView getSecurityCode;// 获取验证码
	@ViewInject(R.id.city)
	private TextView city;// 城市选择
	@ViewInject(R.id.cityLayout)
	private LinearLayout cityLayout;
	@ViewInject(R.id.address)
	private EditText address;// 地址
	@ViewInject(R.id.affirmPay)
	private Button affirmPay;// 确认支付

	private PatientBean patientBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buypay);
		ViewUtils.inject(BuyPayActivity.this);
		login = loadTitleBar(true, "购买支付", "我是会员");
		login.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		patientBean = BaseApplication.patient;
		if (patientBean == null) {
			securityLayout.setVisibility(View.VISIBLE);
			login.setText("我是会员");
		} else {
			securityLayout.setVisibility(View.GONE);
			login.setText(patientBean.getEpName());
		}
		init();
	}

	public void init() {
		if (patientBean == null) {
			return;
		}
		name.setText(patientBean.getEpName());
		phone.setText(patientBean.getEpMobile());
		address.setText(patientBean.getEpAddress());

	}
	@OnClick(R.id.affirmPay)
	private void affirmPayClick(View v){
		
	}
	@OnClick(R.id.cityLayout)
	private void cityLayoutClick(View v){
		
	}
	@OnClick(R.id.getSecurityCode)
	private void getSecurityCodeClick(View v){
		
	}

	/**
	 * 支付
	 */
	public void toPay() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				

			}
		};
	}

	@Override
	public void onClick(View v) {
		//我是会员
	}
}
