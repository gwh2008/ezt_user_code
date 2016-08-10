package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.utils.CommonUtil;

/**
 * @title 软件使用许可协议
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class StatementActivity extends FinalActivity {

	@ViewInject(R.id.showText)
	private TextView showText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statement);
		ViewUtils.inject(StatementActivity.this);
		loadTitleBar(true, "使用协议和免责声明", null);
		showProgressToast();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				Message msg = new Message();
				String str = CommonUtil.getFromAssets(getApplicationContext(),
						"statement.txt");
				msg.obj = str;
				handler.sendMessage(msg);
				Looper.loop();
			}
		}).start();

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = msg.obj.toString();
			showText.setText(str);
			hideProgressToast();
		}

	};

}
