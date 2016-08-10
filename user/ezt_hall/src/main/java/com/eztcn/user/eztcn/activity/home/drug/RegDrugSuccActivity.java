/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.drug;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
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
 *         2016年3月7日 上午10:57:08 预约药品成功
 */
public class RegDrugSuccActivity extends FinalActivity implements IHttpResult {
	/**
	 * 预约人
	 */
	@ViewInject(R.id.personTv)
	private TextView personTv;
	/**
	 * 联系电话
	 */
	@ViewInject(R.id.telTv)
	private TextView telTv;
	/**
	 * 预约药品
	 */
	@ViewInject(R.id.regDrugDeptTv)
	private TextView regDrugDeptTv;
	/**
	 * 预约医院
	 */
	@ViewInject(R.id.regDrugHosTv)
	private TextView regDrugHosTv;
	/**
	 * 预约时间
	 */
	@ViewInject(R.id.regTimeTv)
	private TextView regTimeTv;
	/**
	 * 我的服务
	 */
	@ViewInject(R.id.myServerBtn)
	private Button myServerBtn;
	/**
	 * 返回首页
	 */
	@ViewInject(R.id.drugToHomeBtn)
	private Button drugToHomeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regdrugsucc);
		ViewUtils.inject(RegDrugSuccActivity.this);
		loadTitleBar(false, "预约结果", null);
		getIntentData();

		MessageImpl messageImpl = new MessageImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("infoType", "15");
		// 消息所属系统类型 1预约检查 2预约病床 3预约药瓶 4预约挂号、当日挂号、大牌名医 5龙卡服务
		params.addBodyParameter("infoSysType", "3");
		params.addBodyParameter("userId",
				String.valueOf(BaseApplication.patient.getUserId()));
		params.addBodyParameter("orderNumber", "5432167890");
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
		params1.addBodyParameter("smsType", "15");
		messageImpl.sendSelf(params1,this);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		String regPerson = intent.getStringExtra("regPerson");
		personTv.setText(regPerson);
		String regTime = intent.getStringExtra("regTime");
		regTimeTv.setText(regTime);
		String mobile = intent.getStringExtra("mobile");
		telTv.setText(mobile);
		String hosName = intent.getStringExtra("hosName");
		regDrugHosTv.setText(hosName);
		String docDept = intent.getStringExtra("docDept");
		// regDrugDeptTv.setText(docDept);
	}

	/**
	 * 返回首页
	 * 
	 * @param v
	 */
	@OnClick(R.id.drugToHomeBtn)
	public void toHomeClick(View v) {
		BackHome(RegDrugSuccActivity.this, 0);
	}

	@OnClick(R.id.myServerBtn)
	public void myServerClick(View v) {
		Intent intent = new Intent(RegDrugSuccActivity.this,
				MyServiceActivity30.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		BackHome(RegDrugSuccActivity.this, 0);
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
//		boolean status = (Boolean) obj[1];
//		if (!status) {
////			Toast.makeText(mContext, obj[2] + "", Toast.LENGTH_SHORT).show();
//			return;
//		}
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
