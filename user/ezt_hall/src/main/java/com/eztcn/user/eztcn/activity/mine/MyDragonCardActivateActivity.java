package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 健康龙卡激活
 * @describe
 * @author ezt
 * @created 2015年6月16日
 */
public class MyDragonCardActivateActivity extends FinalActivity{

	@ViewInject(R.id.idcard_tv)
	private TextView idcard_tv;
	@ViewInject(R.id.realname_tv)
	private TextView realname_tv;

	@ViewInject(R.id.banknum_et)
	private EditText etBankNum;

	@ViewInject(R.id.affirm_bt)//, click = "onClick"
	private Button btAffirm;
	private String strBankNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthdragonactivate);
		ViewUtils.inject(MyDragonCardActivateActivity.this);
		loadTitleBar(true, "激活绑定", null);
		idcard_tv.setText(BaseApplication.patient.getEpPid());
		realname_tv.setText(BaseApplication.patient.getEpName());
	}
	@OnClick(R.id.affirm_bt)
	private void affirm_btClick(View v){
				strBankNum = etBankNum.getText().toString().trim();
				if (TextUtils.isEmpty(strBankNum)) {
					Toast.makeText(getApplicationContext(), "请输入银行卡号",
							Toast.LENGTH_SHORT).show();
				} else {
					//卡号输入正确后二选一 
					 startActivity(new Intent(getApplicationContext(),
					 MyDragonCard_ChoiceProjectAvtivity.class).putExtra(
					 "cardNo", strBankNum));
				}
	}
}
