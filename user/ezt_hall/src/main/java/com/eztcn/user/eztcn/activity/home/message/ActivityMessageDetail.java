/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.message;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @author Liu Gang
 * 
 *         2016年4月8日 下午1:34:06
 * 消息详情界面
 */
public class ActivityMessageDetail extends FinalActivity {
	@ViewInject(R.id.msgContentTv)
	private TextView msgContentTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_detail);
		ViewUtils.inject(ActivityMessageDetail.this);
		loadTitleBar(true, "消息详情", null);
		String msgDetailStr = getIntent().getStringExtra("msgDetail");
		msgContentTv.setText(msgDetailStr);
	}

}
