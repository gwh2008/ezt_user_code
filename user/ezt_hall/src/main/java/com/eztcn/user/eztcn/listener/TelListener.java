package com.eztcn.user.eztcn.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.utils.PhoneUtil;

/**
 * @title 电话接听器
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class TelListener extends PhoneStateListener implements OnClickListener {

	private Context context;
	private WindowManager wm;
	private View view;

	public TelListener(Context context) {
		this.context = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		if (state == TelephonyManager.CALL_STATE_RINGING) {

			wm = (WindowManager) BaseApplication.getInstance()
					.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);
			WindowManager.LayoutParams params = new WindowManager.LayoutParams();
			 params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			// params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			// | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//悬浮窗口失去焦点
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			params.height = WindowManager.LayoutParams.MATCH_PARENT;
			// params.format = PixelFormat.RGBA_8888;//背景透明
			params.format = context.getResources().getColor(
					android.R.color.white);
			view = LinearLayout.inflate(context, R.layout.tel_docinfo, null);
			view.findViewById(R.id.tel_answer_bt).setOnClickListener(this);
			view.findViewById(R.id.tel_refuse_bt).setOnClickListener(this);
			wm.addView(view, params);

		} else if (state == TelephonyManager.CALL_STATE_IDLE) {
            if (wm != null&&view!=null&&view.getParent()!=null) {
                try{
                    //对这行代码捕获异常，防止view从窗体脱落，删除失败
                    wm.removeView(view);
                }catch (IllegalArgumentException e){

                }

            }
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tel_answer_bt:// 接听
			PhoneUtil.answerRingingCall(context);
			break;

		case R.id.tel_refuse_bt:// 拒听
			PhoneUtil.endCall(context);
			break;
		}

	}
}