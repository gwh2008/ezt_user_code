package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 意见反馈
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class TicklingContentActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.send)//, click = "onClick"
	private Button send;
	@ViewInject(R.id.content)
	private EditText content;
	@ViewInject(R.id.title_view)
	private RelativeLayout title_view;
	private TextView right_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tickling_input);
		ViewUtils.inject(TicklingContentActivity.this);
		loadTitleBar(true, "意见反馈", "提交");
		right_btn=(TextView) title_view.findViewById(R.id.right_btn);
		right_btn.setOnClickListener(commmitSuggestOnClickListener);
		content.addTextChangedListener(watcher);
		}

	public void onBackClick(View v) {
		finish();
	}
	//提交意见
	OnClickListener  commmitSuggestOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if (content.getText().toString() == null
					|| content.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "请输入您的建议!",
						Toast.LENGTH_SHORT).show();
				return;
			}
			sendSuggestion();
		}
	};
	
	@OnClick(R.id.send)
	private void sendClick(View v){
		if (content.getText().toString() == null
				|| content.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "请输入您的建议!",
					Toast.LENGTH_SHORT).show();
			return;
		}
		sendSuggestion();
	}
	/**
	 * 发送意见反馈
	 */
	public void sendSuggestion() {
		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("efContent", content.getText().toString());
		new UserImpl().sendSuggestion(params, this);
		showProgressToast();
	}

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
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (flag) {
			msg = "您的意见已发送";
			finish();
		} else {
			msg = map.get("msg").toString();
		}
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	private TextWatcher watcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;
		int num = 200;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s.toString();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (s.length() == 0) {
				showSoftInput(content);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = content.getSelectionStart();
			editEnd = content.getSelectionEnd();
			if (s.length() == 0) {// 当字符串为0时候进行处理
				content.setVisibility(View.INVISIBLE);
				hideSoftInput(content);
				content.setVisibility(View.VISIBLE);
			} else if (s.length() > num) {
				toast("意见反馈最多可以填写" + num + "字", Toast.LENGTH_SHORT);
				// hopeEt.removeTextChangedListener(watcher);
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				content.setText(s);
				content.setSelection(tempSelection);
			}
			// int number = num - s.length();
			// editStart = hopeEt.getSelectionStart();
			// editEnd = hopeEt.getSelectionEnd();
			// if (temp.length() > num) {
			// s.delete(editStart - 1, editEnd);
			// int tempSelection = editEnd;
			// hopeEt.setText(s);
			// hopeEt.setSelection(tempSelection);//设置光标在最后
			// }

		}
	};

}
