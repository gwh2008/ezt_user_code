/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.dragoncard;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @author Liu Gang
 * 
 *         2015年11月26日 上午10:13:08
 * 
 */
public class DragonToActive30Activity extends FinalActivity {// implements
																// OnClickListener

	@ViewInject(R.id.bindCardBtn)
	// ,click="onClick"
	private Button bindCardBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragontoactivite30);
		ViewUtils.inject(DragonToActive30Activity.this);
		loadTitleBar(true, "我的龙卡", null);
	}

	@OnClick(R.id.bindCardBtn)
	private void activate_btClick(View v) {
		String idCard = BaseApplication.patient.getEpHiid();
		String mobileStr=BaseApplication.patient.getEpMobile();
		if (TextUtils.isEmpty(idCard)) {
			hintPerfectInfo("请完善个人信息再进行龙卡绑定!", 1, DragonToActive30Activity.this);
			return;
		}

		startActivity(new Intent(getApplicationContext(),
				DragonWriteActivity.class));
	}
}
