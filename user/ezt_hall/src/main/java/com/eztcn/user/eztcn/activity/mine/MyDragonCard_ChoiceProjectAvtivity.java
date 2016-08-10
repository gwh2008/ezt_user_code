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
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.hall.model.PatientBean;

/**
 * @title 激活成功选择服务项
 * @describe
 * @author ezt
 * @created 2015年7月9日
 */
public class MyDragonCard_ChoiceProjectAvtivity extends FinalActivity implements
		IHttpResult {//OnClickListener, 

	@ViewInject(R.id.card_service1_layout)//, click = "onClick"
	private LinearLayout layoutCardService1;

	@ViewInject(R.id.card_service2_layout)//, click = "onClick"
	private LinearLayout layoutCardService2;

	private String cardNo;
	/**
	 * 业务规定是
	 */
	private PatientBean tempUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choiceproject);
		ViewUtils.inject(MyDragonCard_ChoiceProjectAvtivity.this);
		loadTitleBar(false, "选择服务", null);
		Intent intent = getIntent();
		cardNo = intent.getStringExtra("cardNo");
		tempUser = BaseApplication.patient;
	}

	/**
	 * 激活绑定
	 * @param packageId
	 *            0: 表示家庭医生对应的服务 1: 表示帮你找好专家对应的服务
	 */
	private void Binding(int packageId) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("BankCardId", "6217000060021548134");
		params.addBodyParameter("CustName", "侯五六");
		params.addBodyParameter("CustId", "130528198801297238");
		params.addBodyParameter("Phone", tempUser.getEpMobile());
		params.addBodyParameter("service", packageId+ "");
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.authentication(params, this);
		showProgressToast();
		
	}
	
	@OnClick(R.id.card_service1_layout)
	private void card_service1_layoutClick(View v){
		Binding(1);
	}
	
	@OnClick(R.id.card_service2_layout)
	private void card_service2_layoutClick(View v){
		Binding(0);
	}
	@Override
	public void onBackPressed() {

	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		boolean isTrue = (Boolean) object[1];

		if (isTrue) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null && map.size() != 0) {
				boolean flag = (Boolean) map.get("flag");
				if (map.containsKey("msg")) {
					String info = map.get("msg").toString();
					Toast.makeText(getApplicationContext(), info,
							Toast.LENGTH_SHORT).show();
				}
				if (flag) {
					Intent intent = new Intent(getApplicationContext(),
							DragonCardActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					//结束二选一界面
					finish();
					//结束准备跳转到输入卡号界面
					super.finisActivity(DragonToActiveActivity.class);
					//结束输入卡号界面
					super.finisActivity(MyDragonCardActivateActivity.class);
				} else//激活失败
					finish();

			}

		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.service_error), Toast.LENGTH_SHORT)
					.show();

		}
	}

}
