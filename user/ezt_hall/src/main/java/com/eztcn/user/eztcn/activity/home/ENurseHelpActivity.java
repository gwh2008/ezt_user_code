package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.MyHealthCardActivity;
import com.eztcn.user.hall.utils.Constant;
/**
 * @title e护帮 Activity
 * @describe
 * @author ezt
 * @created 2016-1-8
 */
public class ENurseHelpActivity extends FinalActivity implements
		OnClickListener {
	private ImageView healthCard;// 健康卡包
	@ViewInject(R.id.call_Nurse)
	private Button call_Nurse;// 我要医护帮
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enurse);
		ViewUtils.inject(ENurseHelpActivity.this);
		initView();
	}

	private void initView() {
		healthCard=loadTitleBar(true, "e护帮", R.drawable.healthcard_icon);
		healthCard.setOnClickListener(this);
//		initViewPage();
	}
	@OnClick(R.id.call_Nurse)
	public void call_NurseClick(View v){
		Intent intent = new Intent();
		intent.setClass(ENurseHelpActivity.this, CallENurseActivity.class);
		startActivity(intent);
	}

	public void onClick(View v) {
		if(v==healthCard){
			if (BaseApplication.patient == null) {
				((FinalActivity) ENurseHelpActivity.this).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			Intent intent=new Intent();
			intent.setClass(ENurseHelpActivity.this, MyHealthCardActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 初始化tab
	 */

	/**
	 * 初始化gridView
	 */

	/**
	 * 获取相应页银行
	 * @param page
	/**
	 * @title gridView点击事件
	 * @describe
	 * @author ezt
	 * @created 2015年7月14日
	 * 初始化滚动圆点
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
