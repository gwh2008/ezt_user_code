/**
 * 
 */
package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @author Liu Gang
 * 
 *         2015年11月26日 上午10:13:08
 * 
 */
public class DragonToActiveActivity extends FinalActivity{// implements OnClickListener
	
	@ViewInject(R.id.activate_bt)//,click="onClick"
	private  Button activate_bt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragontoactivite);
		ViewUtils.inject(DragonToActiveActivity.this);
		loadTitleBar(true, "建行医指通龙卡", null);
	}

	
	@OnClick(R.id.activate_bt)
	private void activate_btClick(View v){
		startActivity(new Intent(getApplicationContext(),
				MyDragonCardActivateActivity.class));
	}
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.activate_bt: {// 激活绑定
//				startActivity(new Intent(getApplicationContext(),
//						MyDragonCardActivateActivity.class));
//			}
//		}
//
//	}
}
