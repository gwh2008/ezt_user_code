package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 轻陪诊服务说明
 * @describe
 * @author ezt
 * @created 2015年3月30日
 */
public class LightAccompanyExplainActivity extends FinalActivity {

	@ViewInject(R.id.txt)
	private TextView tvTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightaccompany_explain);
		ViewUtils.inject(LightAccompanyExplainActivity.this);
		loadTitleBar(true, "服务详情", null);
		String txt = getIntent().getStringExtra("remark");
		if(!TextUtils.isEmpty(txt)){
			tvTxt.setText(txt);  
		}
	
	}

}
