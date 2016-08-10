/**
 * 
 */
package com.eztcn.user.eztcn.customView;

import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import xutils.http.RequestParams;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.ValideImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @author Liu Gang 2015-12-30 2015年12月30日 退号时候dialog
 */
public class BackValidateDialog extends Dialog implements OnClickListener,
		IHttpResult {
	private EditText message_et;
	/**
	 * 获取短信验证码按钮
	 */
	private Button btn_gainCode;
	private Button btn_sure;
	private CodeSure codeSure;
	private Integer registerId;
	private Integer userId;
	private Timer timer;
	private TimerTask timerTask;
	private int runTime;// 验证码倒计时
	
	private View closeMe;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int num = msg.what;
			if (num == 0) {
				btn_gainCode.setText("获取验证码");
				cancelTimerTask();
				return;
			}
			btn_gainCode.setText(num + "s后重发");
		}
	};
	public BackValidateDialog(Context context, Integer registerId, Integer userId) {
		super(context,R.style.ChoiceDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.back_dialog_validcode);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent); 
		this.registerId = registerId;
		this.userId = userId;
		message_et = (EditText) findViewById(R.id.message_et);
		btn_gainCode = (Button) findViewById(R.id.gainMsg);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		btn_gainCode.setOnClickListener(this);
		closeMe = findViewById(R.id.closeMe);
		closeMe.setOnClickListener(this);
	}

	public void setSure(CodeSure codeSure) {
		this.codeSure = codeSure;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure: {
			codeSure.onSureClick(message_et.getText().toString());
		}
			break;
		case R.id.closeMe: {
			this.dismiss();
		}
			break;
			
		case R.id.gainMsg: {
			if(judgeTime()){
				ValideImpl api = new ValideImpl();
				RequestParams params = new RequestParams();
				params.addBodyParameter("registerId", ""+registerId);
				params.addBodyParameter("operatorId", ""+userId);
				api.getBackValideCode(params, this);
			}else {
				Toast.makeText(getContext(),
						"获取验证码过于频繁",
						Toast.LENGTH_SHORT).show();
			}
		
		}
			break;
		}
	}
	
	/**
	 * 关闭计时器
	 */
	public void cancelTimerTask() {
		btn_gainCode.setEnabled(true);
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}
	
	
	/**
	 * 判断短信验证时间
	 * 
	 * @return
	 */
	private boolean judgeTime() {
		
		long now = Calendar.getInstance().getTimeInMillis();// 现在的时间
		long recordTime = SystemPreferences.getLong(EZTConfig.KEY_BACKNUM_TIME+"_"+registerId);// 记录的时间
		if (now - recordTime > EZTConfig.CAN_BACKNUM_TIME) {
			SystemPreferences.save(EZTConfig.KEY_BACKNUM_TIME+"_"+registerId, now);
			return true;
		} else
			return false;

	}
	public interface CodeSure {
		void onSureClick(String valideCode);
	}

	@Override
	public void result(Object... object) {
		if (object == null) {
			Toast.makeText(getContext(), "获取验证码错误", Toast.LENGTH_SHORT).show();
			return;
		}
		String msg;
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(getContext(), obj[3] + "", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		switch (taskID) {
		case HttpParams.GET_BACK_VAL_CODE: {
			Map<String, Object> map = (Map<String, Object>) obj[2];
			if (map.containsKey("flag")) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					String msgStr="您的短信验证码已经发送，请注意查收";
					codeTimer();
					if (map.containsKey("msg")) {
						msgStr=String.valueOf(map.get("msg"));
					}
					Toast.makeText(getContext(), msgStr,
							Toast.LENGTH_SHORT).show();
				} else {
					if (map.containsKey("msg")) {
						String detailStr = String.valueOf(map.get("msg"));
						Toast.makeText(getContext(), detailStr,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		}
	}
	/**
	 * 验证码倒计时
	 */
	public void codeTimer() {
		btn_gainCode.setEnabled(false);
		runTime = 60;
		timerTask = new TimerTask() {

			@Override
			public void run() {
				runTime--;
				handler.sendEmptyMessage(runTime);
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);
	}

}
