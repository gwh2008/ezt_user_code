package com.eztcn.user.eztcn.activity.home;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.drug.RegDrugSuccActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 挂号等待页面
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class OrderRegWaitActivity extends FinalActivity implements IHttpResult {// OnClickListener,
	@ViewInject(R.id.orderWaitImg)
	private ImageView orderWaitImg;
	// @ViewInject(R.id.order_reg_wait_time)
	// private TextView tvTime;// 倒计时

	@ViewInject(R.id.order_reg_wait_hint)
	private TextView tvHint;// 提示

	@ViewInject(R.id.failed_icon)
	private ImageView failed_Img;

	@ViewInject(R.id.order_reg_wait_bt)
	// , click = "onClick"
	private TextView bt;// 按钮

	@ViewInject(R.id.order_reg_bar)
	private FrameLayout layoutBar;

	private int TIME = 30;// 定义倒计时
	// private Animation numAnim;// 数字倒计时
	private AnimationDrawable animDrawable;
	private String pfId = ""; // 号池平台ID
	private String sourcePfId = "355";// 来源平台ID
	private String poolId = "";// 号池ID
	private int payWay = 0;// 缴费方式 0：自费 1：普通医保2：门特医保3：门大医保
	private int isFirst = 0;// 是否出诊 复诊 1，初诊0
	private String regMark = "";// 参数标识
	private String cardNum = "";// 病案号
	private String operateUserId ="";
	private String patientId = "";// 患者ID
	private String cartoonNum;// 医院一卡通
	private String epHiid = "";
	private String ip = "";// IP地址
	private String idno ="";
	private String code; // 验证码
	private Timer mTimer;

	private String regPerson;
	private String regTime;
	private String docName;
	private String docDept;
	private String hosName;
	private String patientMobile;
	private boolean isOrderDrug;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_reg_wait);

		ViewUtils.inject(OrderRegWaitActivity.this);
        if(BaseApplication.patient!=null){
            operateUserId=BaseApplication.patient.getUserId() + "";// 操作用户id
			epHiid=BaseApplication.patient.getEpHiid();// 医保号
			idno= BaseApplication.patient.getEpPid();// 身份证号码
        }
		isOrderDrug = getIntent().getBooleanExtra("isOrderDrug", false);
		if (isOrderDrug) {
			loadTitleBar(false, "预约过程", null);
		} else {
			loadTitleBar(false, "挂号过程", null);
		}

		orderWaitImg.setBackgroundResource(R.drawable.waitanim);
		animDrawable = (AnimationDrawable) orderWaitImg.getBackground();
		animDrawable.start();
		gainIntentData();
		
		if (isOrderDrug) {
			tvHint.setText("正在预约中，请稍等");
		} else
			tvHint.setText("正在挂号中，请稍等");
		
		RegistratioImpl registImpl = new RegistratioImpl();
		RequestParams params = new RequestParams();
		// patientId 患者ID
		params.addBodyParameter("patientId", patientId);
		// pfId 号池平台ID
		params.addBodyParameter("pfId", pfId);
		// sourcePfId 来源平台ID
		params.addBodyParameter("sourcePfId", sourcePfId);
		// poolId 号池ID
		params.addBodyParameter("poolId", poolId);
		// payWay 缴费方式
		params.addBodyParameter("payWay", "" + payWay);
		// isFirst 是否出诊
		params.addBodyParameter("isFirst", "" + isFirst);
		// cardNum 病案号
		params.addBodyParameter("cardNum", cardNum);
		// cartoonNum 医院一卡通
		params.addBodyParameter("cartoonNum", cartoonNum);
		// regMark 参数标识
		params.addBodyParameter("regMark", regMark);
		// operateUserId 操作用户ID
		params.addBodyParameter("operateUserId", operateUserId);
		// epHiid 医保号
		params.addBodyParameter("epHiid", epHiid);
		// ip IP地址
		params.addBodyParameter("ip", ip);
		// idno 身份证号码
		params.addBodyParameter("idno", idno);
		// mobile 手机号码
		String mobile = BaseApplication.patient.getEpMobile();
		params.addBodyParameter("mobile", mobile);
		// code 验证码
		params.addBodyParameter("code", code);
		registImpl.reg(params, this);
		handler.sendEmptyMessageDelayed(2, 1000);

	}

	private void gainIntentData() {
		Intent intent = getIntent();
		pfId = intent.getStringExtra("pfId");
		sourcePfId = intent.getStringExtra("sourcePfId");
		poolId = intent.getStringExtra("poolId");
		payWay = intent.getIntExtra("payWay", 0);
		isFirst = intent.getIntExtra("isFirst", 0);
		cardNum = intent.getStringExtra("cardNum");
		cartoonNum = intent.getStringExtra("cartoonNum");
		regMark = intent.getStringExtra("regMark");
		code = intent.getStringExtra("code");
		patientId = intent.getStringExtra("patientId");
		// 2016-03-07
		regPerson = intent.getStringExtra("regPerson");
		regTime = intent.getStringExtra("regTime");
		docName = intent.getStringExtra("docName");
		docDept = intent.getStringExtra("docDept");
		hosName = intent.getStringExtra("hosName");
		patientMobile = intent.getStringExtra("patientMobile");
	}

	// @Override
	// public void onClick(View v) {
	@OnClick(R.id.order_reg_wait_bt)
	public void click(View v) {
		// if ("取消等待".equals(bt.getText().toString())) {
		// } else if ("重新预约".equals(bt.getText().toString())) {
		// }
		// 取消等待、或重新预约
		handler.removeMessages(2);
		finish();

	}

	@Override
	protected void onStop() {
		super.onStop();
		handler.removeMessages(2);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:// 倒计时
				if (TIME > 0) {
					TIME--;
					// tvTime.setText(TIME + "");
					// numAnim.reset();
					// orderWaitImg.startAnimation(numAnim);
					// tvTime.startAnimation(numAnim);
					handler.sendEmptyMessageDelayed(2, 1000);
				} else {
					handler.removeMessages(2);
					handler.removeMessages(1);
					animDrawable.stop();// 20160314
					if (isOrderDrug) {
						tvHint.setText("很遗憾，预约药品失败，请您重新预约");
					} else
						tvHint.setText("很遗憾，挂号失败，请您重新预约");
					bt.setText("重新预约");
					layoutBar.setVisibility(View.GONE);
					failed_Img.setVisibility(View.VISIBLE);
					delayFinsh();
				}

				break;
			}
		}
	};

	@Override
	public void result(Object... object) {
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		switch (type) {

		case HttpParams.REG:// 确认挂号
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map == null || map.size() == 0) {

					return;
				}
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					Intent intent = null;
					if (isOrderDrug) {
						intent = new Intent(OrderRegWaitActivity.this,
								RegDrugSuccActivity.class);
						intent.putExtra("regPerson", regPerson);
						intent.putExtra("mobile", patientMobile);
						intent.putExtra("regTime", regTime);
						intent.putExtra("docDept", docDept);
						intent.putExtra("hosName", hosName);
					} else {
						intent = new Intent(OrderRegWaitActivity.this,
								RegSuccActivity.class);
						intent.putExtra("regPerson", regPerson);
						intent.putExtra("mobile", patientMobile);
						intent.putExtra("regTime", regTime);
						intent.putExtra("docName", docName);
						intent.putExtra("docDept", docDept);
						intent.putExtra("hosName", hosName);
					}
					startActivity(intent);
					finish();
				} else {
					Logger.i("确认挂号", map.get("msg"));
					Toast.makeText(OrderRegWaitActivity.this,
							String.valueOf(map.get("msg")), Toast.LENGTH_LONG)
							.show();
					if (isOrderDrug) {
						tvHint.setText("很遗憾，预约药品失败，请您重新预约");
					} else
						tvHint.setText("很遗憾，挂号失败，请您重新预约");
					bt.setText("重新预约");
					failed_Img.setVisibility(View.VISIBLE);
					layoutBar.setVisibility(View.GONE);
					delayFinsh();
				}
				handler.removeMessages(2);

			} else {
				if (isOrderDrug) {
					tvHint.setText("很遗憾，预约药品失败，请您重新预约");
				} else
					tvHint.setText("很遗憾，挂号失败，请您重新预约");
				bt.setText("重新预约");
				failed_Img.setVisibility(View.VISIBLE);
				layoutBar.setVisibility(View.GONE);
				delayFinsh();
				handler.removeMessages(2);
			}
			break;
		}

	}

	private void delayFinsh() {
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				finish();
			}
		};
		if (null == mTimer){
			mTimer = new Timer();
			mTimer.schedule(timerTask, 3000);
		}else{
			try{
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finish();
		}


	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onDestroy() {
		if (null != mTimer)
			mTimer.cancel();
		if (null != animDrawable && animDrawable.isRunning())
			animDrawable.stop();// 20160314
		handler.removeMessages(2);
		super.onDestroy();
	}

}
