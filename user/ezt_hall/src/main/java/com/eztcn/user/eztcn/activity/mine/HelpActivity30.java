package com.eztcn.user.eztcn.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.GuidanceActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog;

import xutils.view.annotation.ViewInject;

public class HelpActivity30 extends FinalActivity implements OnClickListener,MyDialog.DialogCancle,MyDialog.DialogSure {
	
	@ViewInject(R.id.suggest_feedback_relayout)
	private RelativeLayout suggest_feedback_relayout;
	@ViewInject(R.id.function_introduce_relayout)
	private RelativeLayout function_introduce_relayout;
	@ViewInject(R.id.hot_line_relayout)
	private RelativeLayout hot_line_relayout;
	private MyDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		loadTitleBar(true, "帮助", null);
		suggest_feedback_relayout=(RelativeLayout) this.findViewById(R.id.suggest_feedback_relayout);		
		function_introduce_relayout=(RelativeLayout) this.findViewById(R.id.function_introduce_relayout);		
		hot_line_relayout=(RelativeLayout) this.findViewById(R.id.hot_line_relayout);		
		suggest_feedback_relayout.setOnClickListener(this);
		function_introduce_relayout.setOnClickListener(this);
		hot_line_relayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.suggest_feedback_relayout:
			Intent intent = new Intent(this, TicklingContentActivity.class);
			startActivity(intent);
			break;
		case R.id.function_introduce_relayout:
			Intent intentGuidance = new Intent(this, GuidanceActivity.class);
			startActivity(intentGuidance);
			break;
		case R.id.hot_line_relayout:
			View viewContent = View.inflate(mContext, R.layout.ordercheck_dialog,
					null);
			TextView costTv = (TextView) viewContent.findViewById(R.id.orderHintTv);
			costTv.setText("即将为您拨打"+getString(R.string.hotline_telphone)+"请确定");
			 dialog= new MyDialog(HelpActivity30.this,"确定", "取消","拨打电话", viewContent);
			dialog.setDialogSure(this);
			dialog.setDialogCancle(this);
			// guideDocNum.setFocusable(true);
			// guideDocNum.setFocusableInTouchMode(true);
			// guideDocNum.requestFocus();
			dialog.show();
			break;
		}
		
	}

	@Override
	public void dialogSure() {
		if(null!=dialog&&!HelpActivity30.this.isFinishing()){
			Intent intent=new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + getString(R.string.hotline_telphone)));
			startActivity(intent);
			dialog.dissMiss();
		}
	}

	@Override
	public void dialogCancle() {
		if(null!=dialog&&!HelpActivity30.this.isFinishing()){
			dialog.dissMiss();
		}
	}
}
