///**
// * 
// */
//package com.eztcn.user.eztcn.activity.watch;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.BaseApplication;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.api.IEztServiceCard;
//import com.eztcn.user.eztcn.api.IHttpResult;
//import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
//
///**
// * @author Liu Gang 激活绑定 2015年10月14日 下午2:35:39
// * 
// */
//public class ActivateServerActivity extends FinalActivity implements
//		IHttpResult {
//
//	@ViewInject(R.id.healthcard)
//	private EditText healthcard;
//	@ViewInject(R.id.password)
//	private EditText password;//
//	@ViewInject(R.id.affirmActivate, click = "onClick")
//	private Button affirmActivate;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.healthcardactivate);
//		loadTitleBar(true, "激活", null);
//	}
//
//	public void onClick(View v) {
//		String cardNo = healthcard.getText().toString();
//		String pw = password.getText().toString();
//		if (TextUtils.isEmpty(cardNo)) {
//			Toast.makeText(this, "请输入卡号", Toast.LENGTH_SHORT).show();
//			return;
//		} else if (TextUtils.isEmpty(pw)) {
//			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
//			return;
//		} else if (BaseApplication.eztUser == null) {
//			HintToLogin(1);
//			return;
//		} else {
//			initialActiveCard(cardNo, pw);
//			showProgressToast();
//		}
//	}
//
//	/**
//	 * 激活卡
//	 */
//	private void initialActiveCard(String cardNo, String cardPassword) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userId", BaseApplication.getInstance().eztUser.getUserId());
//		params.put("cardNo", cardNo);
//		params.put("cardPassword", cardPassword);
//		IEztServiceCard api = new EztServiceCardImpl();
//		api.activateCard(params, this);
//	}
//
//	@Override
//	public void result(Object... object) {
//
//		boolean isSuc = (Boolean) object[1];
//		if (isSuc) {
//			Map<String, Object> map = (Map<String, Object>) object[2];
//			if (map != null) {
//				boolean flag = (Boolean) map.get("flag");
//				if (flag) {
//					finish();
//				} else {
//					if (map.get("msg") != null) {
//						Toast.makeText(mContext, map.get("msg").toString(),
//								Toast.LENGTH_SHORT).show();
//					}
//
//				}
//
//			}
//
//		} else {
//			Toast.makeText(mContext, getString(R.string.service_error),
//					Toast.LENGTH_SHORT).show();
//
//		}
//		hideProgressToast();
//	}
//}
