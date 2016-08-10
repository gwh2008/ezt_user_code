package com.eztcn.user.eztcn.activity.mine;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.orderbed.MyOrderBedListActivity;
import com.eztcn.user.eztcn.activity.home.ordercheck.ChoiceCheckOrderListActivity30;
/**
 * @author Administrator
 * 我的服务界面
 */

public class MyServiceActivity30 extends FinalActivity{
	
	private RelativeLayout reservation_form_relayout;
	private RelativeLayout order_check_layout,order_bed_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myservice30);
		loadTitleBar(true, "我的服务", null);
		reservation_form_relayout=(RelativeLayout) this.findViewById(R.id.reservation_form_relayout);
		reservation_form_relayout.setOnClickListener(reservation_form_relayoutClickListener);
		order_check_layout=(RelativeLayout) this.findViewById(R.id.order_check_layout);
		order_check_layout.setOnClickListener(order_check_layoutClickListener);
		order_bed_layout=(RelativeLayout) this.findViewById(R.id.order_bed_layout);
		order_bed_layout.setOnClickListener(order_bed_layoutClickListener);
//		medical_help_layout=(RelativeLayout) this.findViewById(R.id.medical_help_layout);
//		medical_help_layout.setOnClickListener(medical_help_layoutClickListener);
		
	}
	//预约挂号
	 OnClickListener reservation_form_relayoutClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			startActivity(new Intent(MyServiceActivity30.this, MyRecordActivity.class));
		}
	};
	//约检查。
	OnClickListener order_check_layoutClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			startActivity(new Intent(MyServiceActivity30.this, ChoiceCheckOrderListActivity30.class));
			
		}
	};
	//约病床
	OnClickListener order_bed_layoutClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			startActivity(new Intent(MyServiceActivity30.this, MyOrderBedListActivity.class));
			
		}
	};
	/*//医护帮
	OnClickListener medical_help_layoutClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Toast.makeText(mContext, "暂未开放", Toast.LENGTH_SHORT).show();
		}
	};*/
	
	

}
