package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 系统消息详情
 * @describe
 * @author ezt
 * @created 2015年2月14日
 */
public class SystemMsgInfoActivity extends FinalActivity {
	
	
	@ViewInject(R.id.tv_info)
	private TextView tvInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(SystemMsgInfoActivity.this);
		setContentView(R.layout.activity_system_msginfo);
		String title=getIntent().getStringExtra("title");
		String info=getIntent().getStringExtra("info");
		loadTitleBar(true, title, null);
		tvInfo.setText(info);
//		SystemMsgInfoActivity

	}

}
