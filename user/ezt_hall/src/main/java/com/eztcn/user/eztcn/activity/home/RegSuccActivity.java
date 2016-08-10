/**
 * 
 */
package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.drug.RegDrugSuccActivity;
import com.eztcn.user.eztcn.activity.mine.MyServiceActivity30;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.MessageImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;

/**
 * @author Liu Gang
 * 
 *         2016年3月7日 上午10:57:08
 * 
 */
public class RegSuccActivity extends FinalActivity implements IHttpResult {// OnClickListener,
	@ViewInject(R.id.personTv)
	private TextView personTv;
	@ViewInject(R.id.telTv)
	private TextView telTv;
	@ViewInject(R.id.regTimeTv)
	private TextView regTimeTv;
	@ViewInject(R.id.docNameTv)
	private TextView docNameTv;
	@ViewInject(R.id.deptTv)
	private TextView deptTv;
	@ViewInject(R.id.hospital)
	private TextView hospital;
	@ViewInject(R.id.backHomeBtn)
	private Button backHomeBtn;
	@ViewInject(R.id.myServerBtn)
	private Button myServerBtn;

	// private View titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regsucc);
		ViewUtils.inject(RegSuccActivity.this);
		loadTitleBar(false, "挂号结果", null);
		// titleView = loadTitleBar(false, "挂号结果", R.drawable.close);
		// titleView.setOnClickListener(this);

		getIntentData();
		MessageImpl messageImpl = new MessageImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("infoType", "16");
		// 消息所属系统类型 1预约检查 2预约病床 3预约药瓶 4预约挂号、当日挂号、大牌名医 5龙卡服务
		params.addBodyParameter("infoSysType", "4");
		if(BaseApplication.patient!=null){
			params.addBodyParameter("userId",
					String.valueOf(BaseApplication.patient.getUserId()));
		}
		params.addBodyParameter("orderNumber", "1234567890");
		messageImpl.traOrderInfo(params, this);

		RequestParams params1 = new RequestParams();
		// * sendType 信息类型 必填
		// 0验证码1通知短信2广告短信3推送信息
		params1.addBodyParameter("sendType",
				String.valueOf(EZTConfig.SENDTYPE_TZDX));
		// lyPfId 来源平台ID 必填
		params1.addBodyParameter("lyPfId", String.valueOf(355));
		// sendPfId 发送平台id 必填
		params1.addBodyParameter("sendPfId", String.valueOf(355));
		// mobile 手机号 必填
		params1.addBodyParameter("mobile", telTv.getText().toString());
		// eztCode 加密验证码 必填 用于判断是否来源可信，格式:ezt+mobile
		params1.addBodyParameter("eztCode",
				MD5.getMD5ofStr("ezt" + telTv.getText().toString()));
		// smsType 短信类型，用来区分内容 必填
		params1.addBodyParameter("smsType", "16");
		messageImpl.sendSelf(params1, this);

	}

	private void getIntentData() {
		Intent intent = getIntent();
		String regPerson = intent.getStringExtra("regPerson");
		personTv.setText(regPerson);
		String regTime = intent.getStringExtra("regTime");
		regTimeTv.setText(regTime);
		String mobile = intent.getStringExtra("mobile");
		telTv.setText(mobile);
		String docNameStr = intent.getStringExtra("docName");
		docNameTv.setText(docNameStr);
		String hosName = intent.getStringExtra("hosName");
		hospital.setText(hosName);
		String docDept = intent.getStringExtra("docDept");
		deptTv.setText(docDept);
	}

	@OnClick(R.id.backHomeBtn)
	public void backHomeBtnClick(View v) {
		BackHome(RegSuccActivity.this, 0);
	}

	@OnClick(R.id.myServerBtn)
	public void myServerClick(View v) {
		Intent intent = new Intent(RegSuccActivity.this,
				MyServiceActivity30.class);
		startActivity(intent);
		finish();
	}
	@Override
	public void onBackPressed() {
		BackHome(RegSuccActivity.this, 0);
	}

	private int numTimes = 0;

	@Override
	public void result(Object... object) {

		hideProgressToast();
		String msg;
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		switch (taskID) {

		case HttpParams.TRA_ORDER_INFO:
			if (flag) {
				numTimes += 1;
			}
			break;
		case HttpParams.SEND_SELF: {
			if (flag) {
				numTimes += 1;
			}
		}
			break;
		}
		if (numTimes == 2)
			SystemPreferences.save(EZTConfig.KEY_HAVE_MSG, true);
	}
}
